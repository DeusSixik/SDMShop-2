package dev.sixik.sdmshop2.libs.shop.client.ui.modal;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopScreenController;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopModalHost;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopOfferPresentation;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopPurchasePolicy;
import dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopCatalogElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopStyles;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopTheme;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopItemWidget;
import dev.sixik.sdmshop2.libs.shop.components.api.ConditionComponent;
import dev.sixik.sdmshop2.libs.shop.limiter.ShopLimiters;
import dev.sixik.sdmshop2.libs.shop.network.ShopNetworkManager;
import dev.sixik.unigui.api.layout.*;
import dev.sixik.unigui.api.math.ColorView;
import dev.sixik.unigui.api.math.MutableColor;
import dev.sixik.unigui.api.math.MutableRect;
import dev.sixik.unigui.api.math.RectView;
import dev.sixik.unigui.api.render.DrawScope;
import dev.sixik.unigui.api.render.RenderContext;
import dev.sixik.unigui.api.render.SimpleTextureHandle;
import dev.sixik.unigui.api.widget.Visibility;
import dev.sixik.unigui.impl.widget.WidgetBase;
import dev.sixik.unigui.widgets.containers.Box;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.ScrollView;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.core.Orientation;
import dev.sixik.unigui.widgets.display.ImageView;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.display.Separator;
import dev.sixik.unigui.widgets.feedback.OverlayHostAware;
import dev.sixik.unigui.widgets.interaction.Button;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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

    /** Every item tooltip created by this dialog. */
    private final List<ShopItemWidget> tooltipSlots = new ArrayList<>();

    /** Slots rebuilt when quantity or payment group changes. */
    private final List<ShopItemWidget> dynamicTooltipSlots = new ArrayList<>();

    private final ShopCatalogElement sourceCard;
    private final @Nullable ShopOffer offer;
    private final List<ItemStack> rewards;
    private final List<ShopCatalogElement.CostGroup> costGroups;
    private final double discountPercent;
    private int activeGroupIndex = 0;
    private int quantity = 1;
    private int maxQuantity = 64;
    private VBox dialogCard;
    private VBox scrollableContent;
    private Label quantityLabel;
    private Button buyButton;
    private Label limitLabel;
    private ShopItemWidget mainSlot;

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
        this.rewards = sourceCard.getRewards();
        this.costGroups = sourceCard.getCostGroups();
        this.discountPercent = sourceCard.getDiscountPercent();
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
        Label title = new Label(Component.translatable("shop.ui.purchase_modal.title").getString());
        if (title.text().isBlank() || title.text().equals("shop.ui.purchase_modal.title")) {
            title.text("Покупка товара");
        }
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
        ItemStack primaryItem =
                rewards.isEmpty() ? Items.BARRIER.getDefaultInstance() : rewards.get(0);
        mainSlot = new ShopItemWidget(primaryItem, 32.0f).asSlot(42.0f, 42.0f);
        mainSlot.decorations(true);
        attachTooltip(mainSlot, false);
        productRow.addChild(mainSlot);
        VBox productDetails = new VBox();
        productDetails.spacing(3.0f);
        productDetails.layout(style -> style.expand());
        Label nameLabel = new Label(sourceCard.getTitle());
        nameLabel.color(ShopTheme.color(ShopTheme.TEXT_PRIMARY));
        productDetails.addChild(nameLabel);
        if (rewards.size() > 1) {
            Label bundleInfo = new Label("Набор из " + rewards.size() + " предметов");
            bundleInfo.color(ShopTheme.color(ShopTheme.TEXT_GOLD));
            productDetails.addChild(bundleInfo);
        }
        if (discountPercent > 0) {
            Label discountLabel = new Label("Скидка: -" + (int) discountPercent + "%");
            discountLabel.color(ShopTheme.color(ShopTheme.DISCOUNT_BADGE));
            productDetails.addChild(discountLabel);
        }
        limitLabel = new Label("Лимит: без ограничений");
        limitLabel.color(ShopTheme.color(ShopTheme.TEXT_MUTED));
        productDetails.addChild(limitLabel);
        productRow.addChild(productDetails);
        dialogCard.addChild(productRow);
        // Если это набор, показываем слоты предметов
        if (rewards.size() > 1) {
            HBox bundleRow = new HBox();
            bundleRow.layout(
                    style ->
                            style.gap(6.0f)
                                    .alignItems(Align.CENTER)
                                    .padding(2.0f, 0.0f)
                                    .flexNone());
            for (ItemStack stack : rewards) {
                ShopItemWidget p = new ShopItemWidget(stack, 18.0f).asSlot(24.0f, 24.0f);
                p.decorations(true);
                attachTooltip(p, false);
                bundleRow.addChild(p);
            }
            dialogCard.addChild(bundleRow);
        }
        dialogCard.addChild(createSeparator());
        // ==========================================
        // 3. СРЕДНИЙ БЛОК: Скроллируемая область Description
        // (Валюты, расчет, статус баланса, описание/лор)
        // ==========================================
        scrollableContent = new VBox();
        scrollableContent.spacing(8.0f);
        scrollableContent.layout(style -> style.widthPercent(100.0f).padding(2.0f, 2.0f));
        ScrollView descriptionScroll =
                new ScrollView(scrollableContent).scrollStep(20.0f).scrollbarGap(2.0f);
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
        Button btnMin = createSmallButton("Min", () -> setQuantity(1));
        Button btnMinus = createSmallButton("-", () -> setQuantity(quantity - 1));
        Box qtyBox = new Box();
        qtyBox.themeEnabled(false);
        qtyBox.boxVisualEnabled(true);
        qtyBox.backgroundVisible(true);
        qtyBox.background(ShopTheme.color(ShopTheme.BG_INPUT));
        qtyBox.borderVisible(true);
        qtyBox.border(ShopTheme.color(ShopTheme.BORDER_SLOT));
        qtyBox.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        qtyBox.radius(ShopTheme.RADIUS_SLOT);
        qtyBox.layout(
                style ->
                        style.fixed(42.0f, 20.0f)
                                .alignItems(Align.CENTER)
                                .justifyContent(Justify.CENTER));
        quantityLabel = new Label(String.valueOf(quantity));
        quantityLabel.color(ShopTheme.color(ShopTheme.TEXT_PRIMARY));
        quantityLabel.textAlignment(Alignment.CENTER);
        qtyBox.addChild(quantityLabel);
        Button btnPlus = createSmallButton("+", () -> setQuantity(quantity + 1));
        Button btnMax = createSmallButton("Max", () -> setQuantity(maxQuantity));
        quantityRow.addChild(btnMin);
        quantityRow.addChild(btnMinus);
        quantityRow.addChild(qtyBox);
        quantityRow.addChild(btnPlus);
        quantityRow.addChild(btnMax);
        dialogCard.addChild(quantityRow);
        // Кнопка подтверждения покупки
        buyButton = new Button("Купить");
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
        for (ShopItemWidget slot : dynamicTooltipSlots) slot.detachTooltip(host.overlay());
        tooltipSlots.removeAll(dynamicTooltipSlots);
        dynamicTooltipSlots.clear();
        scrollableContent.clearChildren();
        // 1. Если есть несколько групп оплаты, добавляем переключатель вариантов
        if (costGroups.size() > 1) {
            Label groupTitle = new Label("Способ оплаты:");
            groupTitle.color(ShopTheme.color(ShopTheme.TEXT_SECONDARY));
            scrollableContent.addChild(groupTitle);
            HBox groupTabs = new HBox();
            groupTabs.layout(style -> style.gap(5.0f).flexNone());
            Minecraft mc = Minecraft.getInstance();
            for (int i = 0; i < costGroups.size(); i++) {
                final int idx = i;
                ShopCatalogElement.CostGroup group = costGroups.get(i);
                boolean isActive = (i == activeGroupIndex);
                String tabText;
                if (group.groupId() != null
                        && !group.groupId().isBlank()
                        && !group.groupId().startsWith("group_")) {
                    tabText = group.groupId();
                } else {
                    tabText = "Вариант " + (i + 1);
                }
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
                            refreshDynamicContent();
                        });
                groupTabs.addChild(tabBtn);
            }
            scrollableContent.addChild(groupTabs);
        }
        // 2. Расчет общей стоимости по выбранной группе
        if (costGroups.isEmpty()) {
            return;
        }
        ShopCatalogElement.CostGroup currentGroup =
                costGroups.get(Math.min(activeGroupIndex, costGroups.size() - 1));
        boolean canAffordAll = true;
        Label costsTitle = new Label("Стоимость и баланс:");
        costsTitle.color(ShopTheme.color(ShopTheme.TEXT_SECONDARY));
        scrollableContent.addChild(costsTitle);
        VBox costsList = new VBox();
        costsList.spacing(5.0f);
        for (ShopCatalogElement.CostEntry cost : currentGroup.costs()) {
            double unitAmount = parseAmount(cost.amount());
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
            if (cost.itemIcon() != null && !cost.itemIcon().isEmpty()) {
                ShopItemWidget costIcon = new ShopItemWidget(cost.itemIcon(), 16.0f);
                costIcon.decorations(false);
                attachTooltip(costIcon, true);
                costIcon.layout(style -> style.fixed(16.0f, 16.0f));
                leftPart.addChild(costIcon);
            } else if (cost.textureIcon() != null) {
                String tex = cost.textureIcon().toString();
                ImageView texIcon = new ImageView(new SimpleTextureHandle(tex, 16, 16, tex));
                texIcon.layout(style -> style.fixed(16.0f, 16.0f));
                leftPart.addChild(texIcon);
            } else if (cost.textIcon() != null && !cost.textIcon().isBlank()) {
                Label textIcon = new Label(cost.textIcon());
                textIcon.color(ShopTheme.color(ShopTheme.GOLD_PRIMARY));
                leftPart.addChild(textIcon);
            } else {
                ShopItemWidget coinIcon =
                        new ShopItemWidget(new ItemStack(Items.GOLD_NUGGET), 16.0f);
                coinIcon.decorations(false);
                coinIcon.layout(style -> style.fixed(16.0f, 16.0f));
                leftPart.addChild(coinIcon);
            }
            Label calcLabel =
                    new Label(
                            formatAmount(unitAmount)
                                    + " × "
                                    + quantity
                                    + " = "
                                    + formatAmount(totalAmount));
            calcLabel.color(ShopTheme.color(ShopTheme.TEXT_PRIMARY));
            leftPart.addChild(calcLabel);
            costRow.addChild(leftPart);
            // Проверка баланса игрока
            double playerBalance = checkCostBalance(cost);
            boolean enough = playerBalance >= totalAmount;
            if (!enough) {
                canAffordAll = false;
            }
            double missing = Math.max(0.0, totalAmount - playerBalance);
            CostStatusBadge balanceStatus = new CostStatusBadge(enough, missing);
            costRow.addChild(balanceStatus);
            costCard.addChild(costRow);
            costsList.addChild(costCard);
        }
        scrollableContent.addChild(costsList);
        // 3. Дополнительное описание и свойства предмета (Lore / Tooltips)
        ItemStack primaryItem =
                rewards.isEmpty() ? Items.BARRIER.getDefaultInstance() : rewards.get(0);
        Minecraft mc = Minecraft.getInstance();
        Player player = mc != null ? mc.player : null;
        if (player != null && !primaryItem.isEmpty()) {
            List<Component> tooltipLines = primaryItem.getTooltipLines(player, TooltipFlag.NORMAL);
            if (tooltipLines.size() > 1) {
                Label descHeader = new Label("Описание предмета:");
                descHeader.color(ShopTheme.color(ShopTheme.TEXT_SECONDARY));
                scrollableContent.addChild(descHeader);
                VBox loreBox = new VBox();
                loreBox.themeEnabled(false);
                loreBox.boxVisualEnabled(true);
                loreBox.backgroundVisible(true);
                loreBox.background(ShopTheme.color(ShopTheme.BG_INPUT));
                loreBox.borderVisible(true);
                loreBox.border(ShopTheme.color(ShopTheme.BORDER_SLOT));
                loreBox.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
                loreBox.radius(ShopTheme.RADIUS_BUTTON);
                loreBox.spacing(2.0f);
                loreBox.layout(style -> style.padding(5.0f, 8.0f).widthPercent(100.0f));
                for (int i = 1; i < tooltipLines.size(); i++) {
                    Component line = tooltipLines.get(i);
                    Label loreLabel = new Label(line.getString());
                    loreLabel.color(ShopTheme.color(ShopTheme.TEXT_MUTED));
                    loreBox.addChild(loreLabel);
                }
                scrollableContent.addChild(loreBox);
            }
        }
        // Обновляем лимит и максимальное количество
        ShopLimiters.LimitSnapshot snapshot = ShopLimiters.getSnapshot(offer, player);
        if (snapshot.isLimited()) {
            int available = snapshot.getAvailable();
            this.maxQuantity = Math.max(1, Math.min(64, available));
            if (quantity > maxQuantity && available > 0) {
                quantity = maxQuantity;
                if (quantityLabel != null) {
                    quantityLabel.text(String.valueOf(quantity));
                }
            }
            if (limitLabel != null) {
                limitLabel.text("Лимит: " + available + " / " + snapshot.getCapacity());
                limitLabel.color(
                        ShopTheme.color(available <= 0 ? ShopTheme.DANGER : ShopTheme.TEXT_MUTED));
            }
        } else {
            this.maxQuantity = 64;
            if (limitLabel != null) {
                limitLabel.text("Лимит: без ограничений");
                limitLabel.color(ShopTheme.color(ShopTheme.TEXT_MUTED));
            }
        }
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
        boolean canBuy = canAffordAll && conditionsMet && limiterOk;
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
            buyButton.text("Лимит исчерпан");
        } else if (!conditionsMet) {
            buyButton.text("Условие не выполнено");
        } else if (!canAffordAll) {
            buyButton.text("Недостаточно средств");
        } else {
            buyButton.text("Купить (" + quantity + " шт.)");
        }
    }

    private void setQuantity(int newQty) {
        this.quantity = Math.max(1, Math.min(maxQuantity, newQty));
        if (quantityLabel != null) {
            quantityLabel.text(String.valueOf(this.quantity));
        }
        refreshDynamicContent();
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
        for (ShopItemWidget slot : tooltipSlots) slot.detachTooltip(host.overlay());
        tooltipSlots.clear();
        dynamicTooltipSlots.clear();
        host.closeModal(this);
    }

    /** Releases owned tooltips when the containing screen is disposed. */
    @Override
    public void dispose() {
        close();
        super.dispose();
    }

    /**
     * Registers an item tooltip with this dialog.
     *
     * @param slot item widget to register.
     * @param dynamic whether the widget is rebuilt on a quantity change.
     */
    private void attachTooltip(ShopItemWidget slot, boolean dynamic) {
        tooltipSlots.add(slot);
        if (dynamic) dynamicTooltipSlots.add(slot);
        slot.attachTooltip(host.overlay());
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
                this.text = "✓ Хватает";
            } else {
                this.text = "✗ Не хватает: " + formatAmount(missing);
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
