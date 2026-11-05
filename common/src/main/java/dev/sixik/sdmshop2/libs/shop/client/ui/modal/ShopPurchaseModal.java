package dev.sixik.sdmshop2.libs.shop.client.ui.modal;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopScreenController;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopModalHost;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopOfferPresentation;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopPurchasePolicy;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopRenderElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopCatalogElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopStyles;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopTheme;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopComponentPanel;
import dev.sixik.sdmshop2.libs.shop.components.ItemCostComponent;
import dev.sixik.sdmshop2.libs.shop.components.ItemTagCostComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ConditionComponent;
import dev.sixik.sdmshop2.libs.shop.components.money.MoneyCostComponent;
import dev.sixik.sdmshop2.libs.shop.limiter.ShopLimiters;
import dev.sixik.sdmshop2.libs.shop.network.ShopNetworkManager;
import dev.sixik.unigui.api.layout.*;
import dev.sixik.unigui.api.math.ColorView;
import dev.sixik.unigui.api.math.MutableColor;
import dev.sixik.unigui.api.math.MutableRect;
import dev.sixik.unigui.api.math.RectView;
import dev.sixik.unigui.api.render.DrawScope;
import dev.sixik.unigui.api.render.RenderContext;
import dev.sixik.unigui.api.widget.Visibility;
import dev.sixik.unigui.impl.widget.WidgetBase;
import dev.sixik.unigui.widgets.containers.Box;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.ScrollView;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.core.Orientation;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.display.Separator;
import dev.sixik.unigui.widgets.feedback.OverlayHostAware;
import dev.sixik.unigui.widgets.interaction.Button;
import dev.sixik.unigui.widgets.interaction.NumberField;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.resources.language.I18n;

/**
 * Модальное окно покупки товара в стиле "Obsidian & Gold". - Центрируется на экране - Занимает
 * фиксированную ширину (320px) и 80% высоты экрана - Средняя секция Description (валюты, расчет,
 * описания) имеет собственный скролл - Секция покупки (количество и кнопка "Купить") зафиксирована
 * внизу - Увеличенные отступы между всеми элементами
 */
public class ShopPurchaseModal extends Box implements OverlayHostAware {
    private static final float CARD_WIDTH = 320.0f;
    private static final float HEIGHT_RATIO = 0.80f;

    /** Modal host captured at creation rather than looked up on close. */
    private final ShopModalHost host;

    private final ShopCatalogElement sourceCard;
    private final @Nullable ShopOffer offer;

    /** Factories from the source offer; no widgets are shared with the catalog. */
    private final List<ShopRenderElement> renderElements;

    private ShopComponentPanel componentPanel;
    private final List<ShopComponentPanel> costPanels = new ArrayList<>();

    /** Prevents duplicate disposal when the host and dialog both close. */
    private boolean closed;

    private final List<ShopCatalogElement.CostGroup> costGroups;

    private int activeGroupIndex = 0;
    private int quantity = 1;
    /** Верхняя граница количества, если лимитер не задаёт меньшую. */
    private static final int MAX_QUANTITY = 9999;

    private int maxQuantity = MAX_QUANTITY;
    /** Защита от рекурсии при программной записи значения в поле количества. */
    private boolean syncingQuantity;
    private VBox dialogCard;
    private VBox scrollableContent;
    private NumberField quantityField;
    private Button buyButton;
    private Label limitLabel;

    /**
     * Creates a purchase dialog in the default shop window.
     *
     * @param sourceCard source offer card.
     */
    public ShopPurchaseModal(ShopCatalogElement sourceCard) {
        this(sourceCard, ShopScreenController.context());
    }

    /**
     * Creates a purchase dialog in a specific window.
     *
     * @param sourceCard source offer card.
     * @param host owner of this modal and its tooltips.
     */
    public ShopPurchaseModal(ShopCatalogElement sourceCard, ShopModalHost host) {
        this.host = java.util.Objects.requireNonNull(host, "host");
        this.sourceCard = sourceCard;
        this.offer = sourceCard.getOffer();
        this.renderElements = sourceCard.getRenderElements();
        this.costGroups = sourceCard.getCostGroups();

        this.activeGroupIndex =
                Math.min(
                        Math.max(0, sourceCard.getActiveCostGroupIndex()),
                        Math.max(0, this.costGroups.size() - 1));
        // Полноэкранный фон-затемнение
        boxVisualEnabled(true);
        backgroundVisible(true);
        background(new MutableColor(0.0f, 0.0f, 0.0f, 0.72f));
        layout(style -> style.position(PositionType.ABSOLUTE).inset(0.0f).fill());
        initDialog();
    }

    @Override
    public void measure(LayoutContext context) {
        super.measure(context);
        float cardH = context.availableHeight() * HEIGHT_RATIO;
        dialogCard.measure(new LayoutContext(CARD_WIDTH, cardH));
    }

    @Override
    public void arrangeInHost(RectView hostBounds) {
        // Растягиваем оверлей затемнения на весь экран
        this.arrange(hostBounds);
        // Центрируем диалог по горизонтали и вертикали, занимая 80% высоты
        float hostW = hostBounds.width();
        float hostH = hostBounds.height();
        float cardH = hostH * HEIGHT_RATIO;
        float cardX = hostBounds.x() + (hostW - CARD_WIDTH) * 0.5f;
        float cardY = hostBounds.y() + (hostH - cardH) * 0.5f;
        dialogCard.layout(style -> style.fixed(CARD_WIDTH, cardH));
        dialogCard.arrange(new MutableRect(cardX, cardY, CARD_WIDTH, cardH));
    }

    private void initDialog() {
        dialogCard = new VBox();
        dialogCard.themeEnabled(false);
        dialogCard.boxVisualEnabled(true);
        dialogCard.backgroundVisible(true);
        dialogCard.background(ShopTheme.color(ShopTheme.BG_MODAL));
        dialogCard.borderVisible(true);
        dialogCard.border(ShopTheme.color(ShopTheme.BORDER_SUBTLE));
        dialogCard.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        dialogCard.radius(ShopTheme.RADIUS_MODAL);
        dialogCard.spacing(8.0f);
        dialogCard.layout(style -> style.width(CARD_WIDTH).padding(10.0f, 12.0f).flexNone());
        // ==========================================
        // 1. ВЕРХНИЙ БЛОК: Шапка (Фиксированная)
        // ==========================================
        HBox header = new HBox();
        header.layout(
                style ->
                        style.height(18.0f)
                                .alignItems(Align.CENTER)
                                .justifyContent(Justify.SPACE_BETWEEN)
                                .flexNone());
        Label title = new Label(I18n.get("shop.ui.purchase.title"));
        title.color(ShopTheme.color(ShopTheme.TEXT_GOLD));
        header.addChild(title);
        Button closeBtn = new Button("✕");
        closeBtn.themeEnabled(false);
        closeBtn.boxVisualEnabled(false);
        closeBtn.backgroundVisible(false);
        closeBtn.borderVisible(false);
        closeBtn.focusRingVisible(false);
        closeBtn.textPadding(0.0f, 0.0f);
        closeBtn.textColor(ShopTheme.color(ShopTheme.TEXT_MUTED));
        closeBtn.hoverBackground(ShopTheme.color(ShopTheme.BG_CARD_HOVER));
        closeBtn.layout(style -> style.fixed(16.0f, 16.0f).flexNone());
        closeBtn.onClick(event -> close());
        header.addChild(closeBtn);
        dialogCard.addChild(header);
        dialogCard.addChild(createSeparator());
        // ==========================================
        // 2. ВЕРХНИЙ БЛОК: Карточка товара (Фиксированная)
        // ==========================================
        HBox productRow = new HBox();
        productRow.layout(style -> style.alignItems(Align.CENTER).gap(12.0f).flexNone());
        VBox productDetails = new VBox();
        productDetails.spacing(3);
        productDetails.layout(style -> style.expand());
        productDetails.addChild(new Label(sourceCard.getTitle()));
        componentPanel = new ShopComponentPanel(renderElements);
        componentPanel.attachTooltips(host.overlay());
        limitLabel = new Label(I18n.get("shop.ui.purchase.limit_unlimited"));
        limitLabel.color(ShopTheme.color(ShopTheme.TEXT_MUTED));
        productDetails.addChild(limitLabel);
        productRow.addChild(productDetails);
        dialogCard.addChild(productRow);
        dialogCard.addChild(createSeparator());
        // ==========================================
        // 3. СРЕДНИЙ БЛОК: Скроллируемая область Description
        // (Валюты, расчет, статус баланса, описание/лор)
        // ==========================================
        scrollableContent = new VBox();
        scrollableContent.spacing(8.0f);
        scrollableContent.layout(style -> style.widthPercent(100.0f).padding(2.0f, 2.0f));
        VBox detailsContent = new VBox();
        detailsContent.layout(style -> style.widthPercent(100).flexNone().gap(8));
        detailsContent.addChild(componentPanel);
        detailsContent.addChild(scrollableContent);
        ScrollView descriptionScroll =
                new ScrollView(detailsContent).scrollStep(20.0f).scrollbarGap(2.0f);
        descriptionScroll.layout(
                style ->
                        style.expand()
                                .widthPercent(100.0f)
                                .overflowX(Overflow.HIDDEN)
                                .overflowY(Overflow.AUTO));
        descriptionScroll.overlayScrollBars(false);
        dialogCard.addChild(descriptionScroll);
        // ==========================================
        // 4. НИЖНИЙ БЛОК: Покупка (Фиксированная внизу)
        // Покупка НЕ входит в скролл!
        // ==========================================
        dialogCard.addChild(createSeparator());
        // Селектор количества
        HBox quantityRow = new HBox();
        quantityRow.layout(
                style ->
                        style.alignItems(Align.CENTER)
                                .justifyContent(Justify.CENTER)
                                .gap(6.0f)
                                .height(22.0f)
                                .flexNone());
        Button btnMin = createSmallButton(I18n.get("shop.ui.purchase.button.min"), () -> setQuantity(1));
        Button btnMinus = createSmallButton("-", () -> setQuantity(quantity - 1));
        quantityField = new NumberField();
        quantityField.themeEnabled(false);
        quantityField.backgroundVisible(true);
        quantityField.background(ShopTheme.color(ShopTheme.BG_INPUT));
        quantityField.borderVisible(true);
        quantityField.border(ShopTheme.color(ShopTheme.BORDER_SLOT));
        quantityField.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        quantityField.radius(ShopTheme.RADIUS_SLOT);
        quantityField.textColor().setArgb(ShopTheme.TEXT_PRIMARY);
        quantityField.range(1.0, maxQuantity);
        quantityField.step(1.0);
        quantityField.value(quantity);
        quantityField.layout(style -> style.fixed(52.0f, 20.0f).flexNone());
        // Ввод с клавиатуры: значение само упирается в границы [1, maxQuantity]
        quantityField.onValueChanged(
                event -> {
                    if (syncingQuantity) return;
                    setQuantity((int) Math.round(event.newValue()));
                });
        Button btnPlus = createSmallButton("+", () -> setQuantity(quantity + 1));
        Button btnMax = createSmallButton(I18n.get("shop.ui.purchase.button.max"), () -> setQuantity(affordableQuantity()));
        quantityRow.addChild(btnMin);
        quantityRow.addChild(btnMinus);
        quantityRow.addChild(quantityField);
        quantityRow.addChild(btnPlus);
        quantityRow.addChild(btnMax);
        dialogCard.addChild(quantityRow);
        // Кнопка подтверждения покупки
        buyButton = new Button(I18n.get("shop.ui.offer_element.button.buy"));
        ShopStyles.button(buyButton);
        buyButton.backgroundVisible(true);
        buyButton.background(ShopTheme.color(ShopTheme.BG_CARD));
        buyButton.hoverBackground(ShopTheme.color(ShopTheme.BG_CARD_HOVER));
        buyButton.pressedBackground(ShopTheme.color(ShopTheme.GOLD_PRIMARY));
        buyButton.borderVisible(true);
        buyButton.border(ShopTheme.color(ShopTheme.GOLD_PRIMARY));
        buyButton.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        buyButton.radius(ShopTheme.RADIUS_BUTTON);
        buyButton.textColor(ShopTheme.color(ShopTheme.TEXT_PRIMARY));
        buyButton.layout(style -> style.height(26.0f).widthPercent(100.0f).flexNone());
        buyButton.onClick(event -> executePurchase());
        dialogCard.addChild(buyButton);
        addChild(dialogCard);
        refreshDynamicContent();
    }

    private void refreshDynamicContent() {
        for (ShopComponentPanel panel : costPanels) panel.dispose();
        costPanels.clear();
        scrollableContent.clearChildren();
        // 1. Если есть несколько групп оплаты, добавляем переключатель вариантов
        if (costGroups.size() > 1) {
            Label groupTitle = new Label(I18n.get("shop.ui.purchase.payment_method"));
            groupTitle.color(ShopTheme.color(ShopTheme.TEXT_SECONDARY));
            scrollableContent.addChild(groupTitle);
            HBox groupTabs = new HBox();
            groupTabs.layout(style -> style.gap(5.0f).flexNone());
            Minecraft mc = Minecraft.getInstance();
            for (int i = 0; i < costGroups.size(); i++) {
                final int idx = i;
                ShopCatalogElement.CostGroup group = costGroups.get(i);
                boolean isActive = (i == activeGroupIndex);
                String tabText = I18n.get("shop.ui.purchase.variant", String.valueOf(i + 1));
                Button tabBtn = new Button(tabText);
                ShopStyles.button(tabBtn);
                tabBtn.backgroundVisible(true);
                tabBtn.background(
                        ShopTheme.color(isActive ? ShopTheme.BG_CARD_HOVER : ShopTheme.BG_CARD));
                tabBtn.borderVisible(true);
                tabBtn.border(
                        ShopTheme.color(
                                isActive ? ShopTheme.GOLD_PRIMARY : ShopTheme.BORDER_SUBTLE));
                tabBtn.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
                tabBtn.radius(ShopTheme.RADIUS_BUTTON);
                tabBtn.textColor(
                        ShopTheme.color(isActive ? ShopTheme.TEXT_GOLD : ShopTheme.TEXT_SECONDARY));
                tabBtn.textPadding(8.0f, 2.0f);
                float textW =
                        (mc != null && mc.font != null)
                                ? mc.font.width(tabText)
                                : (tabText.length() * 6.5f);
                tabBtn.layout(s -> s.height(20.0f).minWidth(textW + 18.0f).flexNone());
                tabBtn.onClick(
                        e -> {
                            activeGroupIndex = idx;
                            // Другой способ оплаты — другой расчёт: счётчик начинается заново
                            quantity = 1;
                            refreshDynamicContent();
                        });
                groupTabs.addChild(tabBtn);
            }
            scrollableContent.addChild(groupTabs);
        }
        // Обновляем лимит и максимальное количество
        Minecraft mc = Minecraft.getInstance();
        Player player = mc != null ? mc.player : null;
        ShopLimiters.LimitSnapshot snapshot = ShopLimiters.getSnapshot(offer, player);
        if (snapshot.isLimited()) {
            int available = snapshot.getAvailable();
            this.maxQuantity = Math.max(1, Math.min(MAX_QUANTITY, available));
            if (quantity > maxQuantity && available > 0) {
                quantity = maxQuantity;
            }
            if (limitLabel != null) {
                limitLabel.text(I18n.get("shop.ui.purchase.limit_value", String.valueOf(available), String.valueOf(snapshot.getCapacity())));
                limitLabel.color(
                        ShopTheme.color(available <= 0 ? ShopTheme.DANGER : ShopTheme.TEXT_MUTED));
            }
        } else {
            this.maxQuantity = MAX_QUANTITY;
            if (limitLabel != null) {
                limitLabel.text(I18n.get("shop.ui.purchase.limit_unlimited"));
                limitLabel.color(ShopTheme.color(ShopTheme.TEXT_MUTED));
            }
        }
        syncQuantityField();
        // 2. Расчет общей стоимости по выбранной группе
        ShopCatalogElement.CostGroup currentGroup =
                costGroups.isEmpty()
                        ? new ShopCatalogElement.CostGroup("", List.of())
                        : costGroups.get(Math.min(activeGroupIndex, costGroups.size() - 1));
        boolean canAffordAll = true;
        Label costsTitle = new Label(I18n.get("shop.ui.purchase.cost_and_balance"));
        costsTitle.color(ShopTheme.color(ShopTheme.TEXT_SECONDARY));
        scrollableContent.addChild(costsTitle);
        VBox costsList = new VBox();
        costsList.spacing(5.0f);
        for (ShopCatalogElement.CostEntry cost : currentGroup.costs()) {
            double unitAmount =
                    cost.component() != null
                            ? cost.component().getBaseAmount()
                            : parseAmount(cost.amount());
            double totalAmount = unitAmount * quantity;
            Box costCard = new Box();
            costCard.themeEnabled(false);
            costCard.boxVisualEnabled(true);
            costCard.backgroundVisible(true);
            costCard.background(ShopTheme.color(ShopTheme.BG_INPUT));
            costCard.borderVisible(true);
            costCard.border(ShopTheme.color(ShopTheme.BORDER_SLOT));
            costCard.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
            costCard.radius(ShopTheme.RADIUS_BUTTON);
            costCard.layout(style -> style.padding(5.0f, 8.0f).widthPercent(100.0f).flexNone());
            HBox costRow = new HBox();
            costRow.layout(
                    style ->
                            style.alignItems(Align.CENTER)
                                    .justifyContent(Justify.SPACE_BETWEEN)
                                    .widthPercent(100.0f));
            HBox leftPart = new HBox();
            leftPart.layout(style -> style.alignItems(Align.CENTER).gap(5.0f));
            ShopComponentPanel costView = new ShopComponentPanel(List.of(cost::createRender));
            costView.attachTooltips(host.overlay());
            costPanels.add(costView);
            leftPart.addChild(costView);
            Label calcLabel =
                    new Label(
                            // Единичное количество уже показано иконкой стоимости слева,
                            // поэтому здесь только множитель и итог
                            I18n.get("shop.ui.purchase.cost_multiplier", String.valueOf(quantity), formatAmount(totalAmount)));
            calcLabel.color(ShopTheme.color(ShopTheme.TEXT_PRIMARY));
            leftPart.addChild(calcLabel);
            costRow.addChild(leftPart);
            // Проверка баланса игрока
            double playerBalance = checkCostBalance(cost);
            boolean enough =
                    cost.component() != null
                            ? player != null && cost.component().canPay(player, totalAmount)
                            : playerBalance >= totalAmount;
            if (!enough) {
                canAffordAll = false;
            }
            double missing = Math.max(0.0, totalAmount - playerBalance);
            if (cost.component() == null
                    || cost.component() instanceof ItemCostComponent
                    || cost.component() instanceof ItemTagCostComponent
                    || cost.component() instanceof MoneyCostComponent) {
                costRow.addChild(new CostStatusBadge(enough, missing));
            } else {
                costRow.addChild(new Label(enough ? "✓" : "✗"));
            }
            costCard.addChild(costRow);
            costsList.addChild(costCard);
        }
        scrollableContent.addChild(costsList);
        // 3. Дополнительное описание и свойства предмета (Lore / Tooltips)
        // Проверка условий (conditions)
        boolean conditionsMet = true;
        if (offer != null && player != null) {
            for (ConditionComponent condition : offer.getComponents(ConditionComponent.class)) {
                if (condition.verifiedOnClient() && !condition.isChecked(player)) {
                    conditionsMet = false;
                    break;
                }
            }
        }
        // Проверка лимитера
        boolean limiterOk =
                offer == null
                        || player == null
                        || ShopLimiters.canPurchase(offer, player, quantity);
        // Обновляем кнопку покупки
        boolean canBuy = player != null && canAffordAll && conditionsMet && limiterOk;
        buyButton.enabled(canBuy);
        buyButton.textColor(
                ShopTheme.color(canBuy ? ShopTheme.TEXT_PRIMARY : ShopTheme.TEXT_MUTED));
        buyButton.border(
                ShopTheme.color(canBuy ? ShopTheme.GOLD_PRIMARY : ShopTheme.BORDER_SUBTLE));
        buyButton.background(ShopTheme.color(ShopTheme.BG_CARD));
        buyButton.hoverBackground(
                ShopTheme.color(canBuy ? ShopTheme.BG_CARD_HOVER : ShopTheme.BG_CARD));
        buyButton.pressedBackground(
                ShopTheme.color(canBuy ? ShopTheme.GOLD_PRIMARY : ShopTheme.BG_CARD));
        if (!limiterOk) {
            buyButton.text(I18n.get("shop.ui.purchase.button.limit_reached"));
        } else if (!conditionsMet) {
            buyButton.text(I18n.get("shop.ui.purchase.button.condition_failed"));
        } else if (!canAffordAll) {
            buyButton.text(I18n.get("shop.ui.purchase.button.not_enough_funds"));
        } else {
            buyButton.text(I18n.get("shop.ui.purchase.button.buy_quantity", String.valueOf(quantity)));
        }
    }

    private void setQuantity(int newQty) {
        this.quantity = Math.max(1, Math.min(maxQuantity, newQty));
        refreshDynamicContent();
    }

    /** Записывает текущее количество и границы в поле ввода, не вызывая повторную обработку. */
    private void syncQuantityField() {
        if (quantityField == null) return;
        syncingQuantity = true;
        try {
            quantityField.range(1.0, maxQuantity);
            quantityField.value(quantity);
        } finally {
            syncingQuantity = false;
        }
    }

    /**
     * Максимум, который игрок может оплатить выбранным способом: минимум по всем валютам и
     * предметам стоимости, ограниченный лимитом предложения. Не меньше 1.
     */
    private int affordableQuantity() {
        if (costGroups.isEmpty()) return maxQuantity;
        ShopCatalogElement.CostGroup group =
                costGroups.get(Math.min(activeGroupIndex, costGroups.size() - 1));
        long best = maxQuantity;
        for (ShopCatalogElement.CostEntry cost : group.costs()) {
            double unit =
                    cost.component() != null
                            ? cost.component().getBaseAmount()
                            : parseAmount(cost.amount());
            if (unit <= 0.0) continue;
            long canPay = (long) Math.floor(checkCostBalance(cost) / unit + 1e-9);
            best = Math.min(best, canPay);
        }
        return (int) Math.max(1L, best);
    }

    private void executePurchase() {
        if (offer != null) {
            String groupId = "";
            if (!costGroups.isEmpty()) {
                groupId =
                        costGroups.get(Math.min(activeGroupIndex, costGroups.size() - 1)).groupId();
            }
            ShopNetworkManager.purchaseOffer(offer, groupId, quantity);
        }
        close();
    }

    /** Closes this dialog and releases all reward and cost tooltips. */
    public void close() {
        if (closed) return;
        closed = true;
        componentPanel.dispose();
        for (ShopComponentPanel panel : costPanels) panel.dispose();
        costPanels.clear();
        host.closeModal(this);
        super.dispose();
    }

    /** Releases owned tooltips when the containing screen is disposed. */
    @Override
    public void dispose() {
        close();
    }

    private Button createSmallButton(String text, Runnable action) {
        Button btn = new Button(text);
        ShopStyles.button(btn);
        btn.textPadding(4.0f, 1.0f);
        float w = text.length() > 1 ? 34.0f : 24.0f;
        btn.layout(style -> style.fixed(w, 20.0f).flexNone());
        btn.onClick(event -> action.run());
        return btn;
    }

    private Separator createSeparator() {
        Separator sep = new Separator();
        sep.orientation(Orientation.HORIZONTAL);
        sep.thickness(1.0f);
        sep.color().setArgb(ShopTheme.BORDER_SEPARATOR);
        sep.layout(style -> style.widthPercent(100.0f).height(1.0f).margin(2.0f, 0.0f).flexNone());
        return sep;
    }

    private double checkCostBalance(ShopCatalogElement.CostEntry cost) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null) return 0.0;
        Player player = mc.player;
        if (player == null) return 0.0;
        return ShopPurchasePolicy.checkCostBalance(cost, player);
    }

    private static double parseAmount(String str) {
        return ShopPurchasePolicy.parseAmount(str);
    }

    private static String formatAmount(double amount) {
        return ShopOfferPresentation.formatAmount(amount);
    }

    /**
     * Плашка статуса стоимости ("✓ Хватает" / "✗ Не хватает: N"), отрисовываемая через DrawScope
     * (DrawCommand) с полупрозрачным фоном, тонкой обводкой и вычисленным значением недостающих
     * средств.
     */
    public static class CostStatusBadge extends WidgetBase {
        private final boolean enough;
        private final String text;
        private final float badgeWidth;

        public CostStatusBadge(boolean enough, double missing) {
            this.enough = enough;
            if (enough) {
                this.text = I18n.get("shop.ui.purchase.badge.enough");
            } else {
                this.text = I18n.get("shop.ui.purchase.badge.missing", formatAmount(missing));
            }
            Minecraft mc = Minecraft.getInstance();
            float fontW =
                    (mc != null && mc.font != null)
                            ? mc.font.width(this.text)
                            : (this.text.length() * 6.5f);
            this.badgeWidth = fontW + 14.0f;
            layout(style -> style.fixed(badgeWidth, 16.0f).alignSelf(Align.CENTER).flexNone());
        }

        public String getText() {
            return text;
        }

        public boolean isEnough() {
            return enough;
        }

        @Override
        public void measure(LayoutContext context) {
            if (visibility() == Visibility.COLLAPSED) {
                setDesiredSize(LayoutSize.ZERO);
                return;
            }
            setDesiredSize(resolveDesiredSize(context, badgeWidth, 16.0f));
        }

        @Override
        public void render(RenderContext context) {
            if (visibility() != Visibility.VISIBLE || context == null) return;
            DrawScope draw = new DrawScope(context, transform(), layoutBounds());
            RectView b = layoutBounds();
            float x = b.x();
            float y = b.y();
            float w = b.width();
            float h = b.height();
            if (w <= 0.0f || h <= 0.0f) return;
            ColorView bg =
                    enough
                            ? MutableColor.rgba255(46, 204, 113, 38)
                            : MutableColor.rgba255(231, 76, 60, 38);
            ColorView border =
                    enough
                            ? MutableColor.rgba255(46, 204, 113, 120)
                            : MutableColor.rgba255(231, 76, 60, 120);
            ColorView textColor =
                    enough ? ShopTheme.color(ShopTheme.SUCCESS) : ShopTheme.color(ShopTheme.DANGER);
            draw.roundedRectFilled(x, y, w, h, 3.0f, bg);
            draw.roundedRectStroke(x, y, w, h, 3.0f, border, 1.0f);
            draw.pushTextClip(
                    x + 1.0f, y + 1.0f, Math.max(0.0f, w - 2.0f), Math.max(0.0f, h - 2.0f));
            try {
                float textY = y + Math.max(0.0f, (h - 8.0f) * 0.5f);
                draw.addText(text, x + 6.0f, textY, Math.max(0.0f, w - 12.0f), h, textColor);
            } finally {
                draw.popClip();
            }
        }
    }
}
