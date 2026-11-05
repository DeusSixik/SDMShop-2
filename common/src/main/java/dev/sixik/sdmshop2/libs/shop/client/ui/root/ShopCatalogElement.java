package dev.sixik.sdmshop2.libs.shop.client.ui.root;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopCatalogItem;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopOfferPresentation;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopOfferView;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopPurchasePolicy;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopRenderElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.assets.ShopIcons;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.ShopEditorScreen;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.ShopEditorState;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopTheme;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopComponentPanel;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopContentButton;
import dev.sixik.sdmshop2.libs.shop.components.api.CostComponent;
import dev.sixik.sdmshop2.libs.shop.network.ShopNetworkManager;
import dev.sixik.unigui.api.event.EventPhase;
import dev.sixik.unigui.api.event.PointerEnteredEvent;
import dev.sixik.unigui.api.event.PointerExitedEvent;
import dev.sixik.unigui.api.event.PointerPressedEvent;
import dev.sixik.unigui.api.event.PointerReleasedEvent;
import dev.sixik.unigui.api.input.MouseCursor;
import dev.sixik.unigui.api.input.PointerButton;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.layout.Alignment;
import dev.sixik.unigui.api.layout.Justify;
import dev.sixik.unigui.api.layout.Overflow;
import dev.sixik.unigui.api.widget.Visibility;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.Box;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.LinearBox;
import dev.sixik.unigui.widgets.containers.ScrollView;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.core.Orientation;
import dev.sixik.unigui.widgets.display.ImageView;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.display.Separator;
import dev.sixik.unigui.widgets.feedback.ContextMenu;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.interaction.Button;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

/**
 * Catalog card owning a fresh component widget tree, favorite action and payment-group selection.
 * Component content controls its appearance within a centered horizontal scroll viewport.
 */
public class ShopCatalogElement extends LinearBox implements ShopOfferView {
    /** Fixed product viewport height in logical UI pixels, including scrollbar space. */
    private static final float PRODUCT_VIEWPORT_HEIGHT = 46;

    private final @Nullable ShopOffer offer;
    private final @Nullable UUID offerUuid;
    private final String title;

    /** Component-driven presentation shared as factories, never as widget instances. */
    private final ShopCatalogItem presentation;

    private ShopComponentPanel componentPanel;
    private final List<ShopComponentPanel> costPanels = new ArrayList<>();
    private final List<CostGroup> costGroups;

    private boolean favorite;
    private boolean selected = false;
    private boolean showFavorite = true;
    private Button favoriteButton;
    private ImageView favoriteIcon;
    private @Nullable Consumer<ShopCatalogElement> onOpenPurchase;

    /** Actual overlay hosting the component tree's nested item tooltips. */
    private @Nullable OverlayLayer tooltipOverlay;

    private int activeCostGroupIndex = 0;
    private HBox pricesBox;

    private float lastClickRootX = -1.0f;
    private float lastClickRootY = -1.0f;
    private boolean cardPressed = false;
    private boolean hovered = false;
    private Button buyBtn;

    public ShopCatalogElement(
            ShopOffer offer, @Nullable Consumer<ShopCatalogElement> onOpenPurchase) {
        this(
                new ShopCatalogItem(offer),
                ShopClientCache.isFavorite(offer.getUUID()),
                onOpenPurchase);
    }

    public ShopCatalogElement(ShopOffer offer) {
        this(offer, elem -> ShopNetworkManager.purchaseOffer(offer, "", 1));
    }

    public ShopCatalogElement(
            String title,
            ItemStack itemStack,
            List<CostEntry> costs,
            boolean favorite,
            @Nullable Runnable onBuy) {
        this(title, itemStack, costs, favorite, onBuy != null ? elem -> onBuy.run() : null);
    }

    public ShopCatalogElement(
            String title,
            ItemStack itemStack,
            List<CostEntry> costs,
            boolean favorite,
            @Nullable Consumer<ShopCatalogElement> onOpenPurchase) {
        this(
                null,
                null,
                title,
                List.of(itemStack),
                List.of(new CostGroup("", costs)),
                0.0,
                favorite,
                onOpenPurchase);
    }

    public ShopCatalogElement(
            String title,
            List<ItemStack> rewards,
            List<CostGroup> costGroups,
            double discountPercent,
            boolean favorite,
            @Nullable Consumer<ShopCatalogElement> onOpenPurchase) {
        this(null, null, title, rewards, costGroups, discountPercent, favorite, onOpenPurchase);
    }

    public ShopCatalogElement(
            @Nullable UUID offerUuid,
            String title,
            List<ItemStack> rewards,
            List<CostGroup> costGroups,
            double discountPercent,
            boolean favorite,
            @Nullable Consumer<ShopCatalogElement> onOpenPurchase) {
        this(
                null,
                offerUuid,
                title,
                rewards,
                costGroups,
                discountPercent,
                favorite,
                onOpenPurchase);
    }

    private ShopCatalogElement(
            @Nullable ShopOffer offer,
            @Nullable UUID offerUuid,
            String title,
            List<ItemStack> rewards,
            List<CostGroup> costGroups,
            double discountPercent,
            boolean favorite,
            @Nullable Consumer<ShopCatalogElement> onOpenPurchase) {
        this(
                new ShopCatalogItem(offerUuid, "ALL", title, rewards, costGroups, discountPercent),
                favorite,
                onOpenPurchase);
    }

    /**
     * Creates a card from component factories without restricting the rendered widget types.
     *
     * @param presentation offer presentation containing component factories and payment groups.
     * @param favorite initial favorite state.
     * @param onOpenPurchase purchase or selection action; null disables the action.
     */
    public ShopCatalogElement(
            ShopCatalogItem presentation,
            boolean favorite,
            @Nullable Consumer<ShopCatalogElement> onOpenPurchase) {
        super(Orientation.VERTICAL);
        this.presentation = presentation;
        this.offer = presentation.offer;
        this.offerUuid = presentation.uuid;
        this.title = presentation.title;
        this.costGroups = presentation.costGroups;
        this.favorite = favorite;
        this.onOpenPurchase = onOpenPurchase;
        boxVisualEnabled(true);
        backgroundVisible(true);
        background(ShopTheme.color(ShopTheme.BG_CARD));
        borderVisible(true);
        border(ShopTheme.color(ShopTheme.BORDER_SUBTLE));
        borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        radius(ShopTheme.RADIUS_CARD);
        spacing(4.0f);
        layout(style -> style.minHeight(104.0f).flexNone().padding(7.0f, 8.0f));
        on(
                PointerEnteredEvent.TYPE,
                event -> {
                    hovered = true;
                    applyCardChrome();
                });
        on(
                PointerPressedEvent.TYPE,
                event -> {
                    if (event.phase() == EventPhase.CAPTURE) return;
                    if (event.button() == PointerButton.PRIMARY) {
                        cardPressed = true;
                    }
                });
        on(
                PointerReleasedEvent.TYPE,
                event -> {
                    if (event.phase() == EventPhase.CAPTURE) return;
                    if (event.button() != PointerButton.PRIMARY) return;
                    if (!cardPressed) return;
                    cardPressed = false;
                    if (event.isCancelled()) return;
                    if (ShopEditorState.get().isEditorMode() && offer != null) {
                        ShopEditorState.get().selectTarget(offer);
                    }
                });
        on(
                PointerExitedEvent.TYPE,
                event -> {
                    cardPressed = false;
                    hovered = false;
                    applyCardChrome();
                });
        initChildren();
    }

    private void initChildren() {
        // --- 1. Шапка (Бейдж / Заголовок / Звезда) ---
        HBox header = new HBox();
        header.layout(
                style ->
                        style.minHeight(14.0f)
                                .alignItems(Align.CENTER)
                                .justifyContent(Justify.SPACE_BETWEEN)
                                .flexNone());
        VBox separatorRow = new VBox();
        separatorRow.layout(
                style ->
                        style.widthPercent(100)
                                .minWidth(0)
                                .minHeight(18.0f)
                                .gap(3)
                                .margin(2, 0)
                                // Высота резервируется всегда: у карточек без бейджей цены
                                // стоят на одной линии с карточками со скидкой
                                .flexNone());
        Separator separator = new Separator();
        separator.orientation(Orientation.HORIZONTAL);
        separator.thickness(1);
        // Достаточно контрастный и на обычном фоне карточки, и на hover-фоне
        separator.color().setArgb(ShopTheme.BORDER_SLOT);
        separator.layout(
                style -> style.widthPercent(100).height(1).verticalAlignment(Alignment.CENTER));
        componentPanel =
                new ShopComponentPanel(
                        presentation.getRenderElements(), separatorRow, PRODUCT_VIEWPORT_HEIGHT);
        separatorRow.addChild(separator);
        componentPanel.layout(style -> style.widthPercent(100).minWidth(0));
        Label titleLabel = new Label(title);
        titleLabel.color(ShopTheme.color(ShopTheme.TEXT_PRIMARY));
        titleLabel.textAlignment(Alignment.CENTER);
        titleLabel.layout(style -> style.expand());
        header.addChild(titleLabel);
        // Кнопка избранного: без системного фона и рамок кнопок!
        favoriteButton = new Button("");
        favoriteButton.themeEnabled(false);
        favoriteButton.mouseCursor(MouseCursor.POINTER);
        favoriteButton.boxVisualEnabled(false);
        favoriteButton.backgroundVisible(false);
        favoriteButton.borderVisible(false);
        favoriteButton.focusRingVisible(false);
        favoriteButton.textPadding(0.0f, 0.0f);
        favoriteButton.layout(style -> style.fixed(12.0f, 12.0f).flexNone());
        favoriteIcon = new ImageView(favorite ? ShopIcons.STAR_FULL : ShopIcons.STAR_EMPTY);
        favoriteIcon.layout(style -> style.fixed(12.0f, 12.0f));
        favoriteIcon.enabled(false);
        favoriteButton.addChild(favoriteIcon);
        favoriteButton.onClick(
                event -> {
                    UUID id = getOfferUuid();
                    if (id != null) {
                        boolean newFav = ShopClientCache.toggleFavorite(id);
                        setFavorite(newFav);
                    } else {
                        setFavorite(!this.favorite);
                    }
                });
        header.addChild(favoriteButton);
        if (!showFavorite || ShopEditorState.get().isEditorMode()) {
            favoriteButton.visibility(Visibility.HIDDEN);
        }
        addChild(header);
        addChild(componentPanel);
        addChild(separatorRow);
        // --- 4. Подвал (Цены и золотая кнопка покупки) ---
        HBox footer = new HBox();
        footer.layout(
                style ->
                        style.minHeight(24.0f)
                                .minWidth(0.0f)
                                .alignItems(Align.CENTER)
                                .justifyContent(Justify.SPACE_BETWEEN)
                                .flexNone());
        pricesBox = new HBox();
        // minWidth(0): иначе цены не могут сжаться и выталкивают кнопку за карточку
        pricesBox.layout(
                style -> style.alignItems(Align.CENTER).gap(3.0f).minWidth(0.0f).flexGrow(1.0f));
        refreshPrices();
        footer.addChild(pricesBox);
        buyBtn = new Button(Component.translatable("shop.ui.offer_element.button.buy").getString());
        buyBtn.themeEnabled(false);
        buyBtn.boxVisualEnabled(true);
        buyBtn.backgroundVisible(true);
        buyBtn.focusRingVisible(false);
        buyBtn.textPadding(10.0f, 2.0f);
        buyBtn.layout(style -> style.minHeight(18.0f).minWidth(40.0f).flexNone());
        buyBtn.onClick(
                event -> {
                    if (ShopEditorState.get().isEditorMode()) {
                        if (offer != null) {
                            ShopEditorState.get().selectTarget(offer);
                        }
                        return;
                    }
                    if (onOpenPurchase != null) {
                        onOpenPurchase.accept(this);
                    }
                });
        footer.addChild(buyBtn);
        updateBuyButtonState();
        addChild(footer);
    }

    /** Releases tooltips and the payment menu owned by this card. */
    @Override
    public void dispose() {
        detachTooltips(tooltipOverlay);
        componentPanel.dispose();
        for (ShopComponentPanel panel : costPanels) panel.dispose();
        super.dispose();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        applyCardChrome();
    }

    /** Applies the card surface for the current selected/hovered state. */
    private void applyCardChrome() {
        if (selected) {
            background(ShopTheme.color(ShopTheme.BG_CARD_HOVER));
            border(ShopTheme.color(ShopTheme.GOLD_PRIMARY));
            borderWidth(ShopTheme.BORDER_WIDTH_ACCENT);
        } else if (hovered) {
            background(ShopTheme.color(ShopTheme.BG_CARD_HOVER));
            border(ShopTheme.color(ShopTheme.BORDER_CARD_HOVER));
            borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        } else {
            background(ShopTheme.color(ShopTheme.BG_CARD));
            border(ShopTheme.color(ShopTheme.BORDER_SUBTLE));
            borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        }
    }

    public @Nullable UUID getOfferUuid() {
        return offer != null ? offer.getUUID() : offerUuid;
    }

    public void attachTooltips(@Nullable OverlayLayer overlayLayer) {
        tooltipOverlay = overlayLayer;
        componentPanel.attachTooltips(overlayLayer);
        for (ShopComponentPanel panel : costPanels) panel.attachTooltips(overlayLayer);
    }

    public void detachTooltips(@Nullable OverlayLayer overlayLayer) {
        componentPanel.attachTooltips(null);
        for (ShopComponentPanel panel : costPanels) panel.attachTooltips(null);
        detachDropdownTooltips(overlayLayer);
        for (ShopComponentPanel panel : activeDropdownPanels) panel.dispose();
        activeDropdownPanels.clear();
        if (activeCostDropdown != null) {
            activeCostDropdown.close();
            if (tooltipOverlay != null) tooltipOverlay.removeOverlay(activeCostDropdown);
            activeCostDropdown = null;
        }
        tooltipOverlay = null;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
        if (favoriteIcon != null) {
            favoriteIcon.texture(favorite ? ShopIcons.STAR_FULL : ShopIcons.STAR_EMPTY);
        }
    }

    public boolean isFavorite() {
        return favorite;
    }

    public boolean isShowFavorite() {
        return showFavorite;
    }

    public ShopCatalogElement showFavorite(boolean show) {
        this.showFavorite = show;
        if (favoriteButton != null) {
            favoriteButton.visibility(show ? Visibility.VISIBLE : Visibility.HIDDEN);
        }
        return this;
    }

    public @Nullable ShopOffer getOffer() {
        return offer;
    }

    public String getTitle() {
        return title;
    }

    /**
     * @return component factories used by this card; each view mounts fresh widgets.
     */
    @Override
    public List<ShopRenderElement> getRenderElements() {
        return presentation.getRenderElements();
    }

    @Deprecated
    public List<ItemStack> getRewards() {
        return presentation.getRewards();
    }

    public List<CostGroup> getCostGroups() {
        return costGroups;
    }

    @Deprecated
    public double getDiscountPercent() {
        return presentation.getDiscountPercent();
    }

    public void setOnOpenPurchase(@Nullable Consumer<ShopCatalogElement> onOpenPurchase) {
        this.onOpenPurchase = onOpenPurchase;
    }

    public int getActiveCostGroupIndex() {
        return activeCostGroupIndex;
    }

    public HBox getPricesBox() {
        return pricesBox;
    }

    public void setActiveCostGroupIndex(int activeCostGroupIndex) {
        this.activeCostGroupIndex =
                Math.max(0, Math.min(activeCostGroupIndex, costGroups.size() - 1));
        refreshPrices();
    }

    public void refreshPrices() {
        if (pricesBox == null) return;
        for (ShopComponentPanel panel : costPanels) panel.dispose();
        costPanels.clear();
        pricesBox.clearChildren();
        if (costGroups.isEmpty()) {
            return;
        }
        int currentGroupIdx = Math.min(Math.max(0, activeCostGroupIndex), costGroups.size() - 1);
        CostGroup currentGroup = costGroups.get(currentGroupIdx);
        List<CostEntry> costs = currentGroup.costs();
        // 1. Одна общая панель для всех компонентов стоимости текущего способа оплаты
        HBox pricePanel = new HBox();
        pricePanel.themeEnabled(false);
        pricePanel.boxVisualEnabled(true);
        pricePanel.backgroundVisible(true);
        pricePanel.background(ShopTheme.color(ShopTheme.BG_SLOT));
        pricePanel.borderVisible(true);
        pricePanel.border(ShopTheme.color(ShopTheme.BORDER_SLOT));
        pricePanel.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        pricePanel.radius(ShopTheme.RADIUS_SLOT);
        pricePanel.layout(
                style ->
                        style.minHeight(18.0f)
                                .alignItems(Align.CENTER)
                                .padding(4.0f, 1.0f)
                                .gap(3.0f)
                                .flexNone());
        int displayCount = costs.size();
        for (int i = 0; i < displayCount; i++) {
            if (i > 0) {
                Label plusLabel = new Label("+");
                plusLabel.color(ShopTheme.color(ShopTheme.TEXT_MUTED));
                plusLabel.layout(s -> s.alignSelf(Align.CENTER).margin(0.0f, 1.0f));
                pricePanel.addChild(plusLabel);
            }
            CostEntry cost = costs.get(i);
            ShopComponentPanel costView = new ShopComponentPanel(List.of(cost::createRender));
            costView.layout(style -> style.flexNone());
            costView.attachTooltips(tooltipOverlay);
            costPanels.add(costView);
            pricePanel.addChild(costView);
        }
        // Много валют не помещаются в карточку: панель цен прокручивается по горизонтали,
        // а кнопка переключения и «Buy» всегда остаются на месте
        ScrollView priceViewport =
                new ScrollView(pricePanel)
                        .overlayScrollBars(false)
                        .scrollbarSize(3)
                        .scrollbarGap(1)
                        .scrollStep(16);
        priceViewport.layout(
                style ->
                        style.flexInitial()
                                .minWidth(0.0f)
                                .height(24.0f)
                                .overflowX(Overflow.AUTO)
                                .overflowY(Overflow.HIDDEN));
        pricesBox.addChild(priceViewport);
        // 2. Переключение способов оплаты:
        // Если ровно 2 способа оплаты — кнопка быстрого переключения ⇄ 1/2
        // Если больше 2 способов оплаты — выпадающий список с выбором
        if (costGroups.size() == 2) {
            Button switchBtn = new Button("⇄ " + (currentGroupIdx + 1) + "/2");
            switchBtn.backgroundVisible(true);
            switchBtn.background(ShopTheme.color(ShopTheme.BG_SLOT));
            switchBtn.hoverBackground(ShopTheme.color(ShopTheme.BG_CARD_HOVER));
            switchBtn.pressedBackground(ShopTheme.color(ShopTheme.BG_CARD));
            switchBtn.borderVisible(true);
            switchBtn.border(ShopTheme.color(ShopTheme.BORDER_SLOT));
            switchBtn.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
            switchBtn.radius(ShopTheme.RADIUS_SLOT);
            switchBtn.textColor(ShopTheme.color(ShopTheme.TEXT_GOLD));
            switchBtn.focusRingVisible(false);
            switchBtn.textPadding(4.0f, 1.0f);
            switchBtn.layout(s -> s.height(18.0f).flexNone().margin(0.0f, 1.0f));
            switchBtn.onClick(
                    e -> {
                        activeCostGroupIndex = (activeCostGroupIndex == 0) ? 1 : 0;
                        refreshPrices();
                    });
            pricesBox.addChild(switchBtn);
        } else if (costGroups.size() > 2) {
            Button dropdownBtn =
                    new Button("⇄ " + (currentGroupIdx + 1) + "/" + costGroups.size() + " ▾");
            dropdownBtn.backgroundVisible(true);
            dropdownBtn.background(ShopTheme.color(ShopTheme.BG_SLOT));
            dropdownBtn.hoverBackground(ShopTheme.color(ShopTheme.BG_CARD_HOVER));
            dropdownBtn.pressedBackground(ShopTheme.color(ShopTheme.BG_CARD));
            dropdownBtn.borderVisible(true);
            dropdownBtn.border(ShopTheme.color(ShopTheme.BORDER_SLOT));
            dropdownBtn.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
            dropdownBtn.radius(ShopTheme.RADIUS_SLOT);
            dropdownBtn.textColor(ShopTheme.color(ShopTheme.TEXT_GOLD));
            dropdownBtn.focusRingVisible(false);
            dropdownBtn.textPadding(4.0f, 1.0f);
            dropdownBtn.layout(s -> s.height(18.0f).flexNone().margin(0.0f, 1.0f));
            dropdownBtn.on(
                    PointerPressedEvent.TYPE,
                    e -> {
                        lastClickRootX = e.rootX();
                        lastClickRootY = e.rootY();
                    });
            dropdownBtn.onClick(e -> openCostGroupsDropdown(dropdownBtn));
            pricesBox.addChild(dropdownBtn);
        }
        updateBuyButtonState();
        pricesBox.applyQueuedMutations();
    }

    private @Nullable ContextMenu activeCostDropdown = null;

    /** Component trees owned by the payment menu. */
    private final List<ShopComponentPanel> activeDropdownPanels = new ArrayList<>();

    private void detachDropdownTooltips(@Nullable OverlayLayer layer) {
        for (ShopComponentPanel panel : activeDropdownPanels) panel.attachTooltips(null);
    }

    /** Payment row with decorative content and one pointer target. */
    public static class CostOptionButton extends ShopContentButton {
        /**
         * Creates a payment option button.
         *
         * @param content decorative icons, amounts and selection indicator.
         */
        public CostOptionButton(Widget content) {
            super(content);
        }
    }

    private void openCostGroupsDropdown(Button triggerBtn) {
        OverlayLayer layer = findOverlayLayer();
        if (layer == null) return;
        if (activeCostDropdown != null) {
            boolean wasOpened = activeCostDropdown.opened();
            detachDropdownTooltips(layer);
            try {
                activeCostDropdown.close();
            } catch (Exception ignored) {
            }
            activeCostDropdown = null;
            if (wasOpened) {
                return;
            }
        }
        for (ShopComponentPanel panel : activeDropdownPanels) panel.dispose();
        activeDropdownPanels.clear();
        ContextMenu menu = new ContextMenu();
        menu.themeEnabled(false);
        menu.boxVisualEnabled(true);
        menu.backgroundVisible(true);
        menu.background(ShopTheme.color(ShopTheme.BG_MODAL));
        menu.borderVisible(true);
        menu.border(ShopTheme.color(ShopTheme.BORDER_SUBTLE));
        menu.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        menu.radius(ShopTheme.RADIUS_MODAL);
        menu.closeOnOutsideClick(true);
        for (int i = 0; i < costGroups.size(); i++) {
            final int idx = i;
            CostGroup group = costGroups.get(i);
            boolean isSelected = (i == activeCostGroupIndex);
            HBox row = new HBox();
            row.layout(style -> style.alignItems(Align.CENTER).gap(3.0f));
            // Индикатор выбора: фиксированный контейнер 12x12 с галочкой для идеального
            // выравнивания строк
            Box checkContainer = new Box();
            checkContainer.layout(
                    style ->
                            style.fixed(12.0f, 12.0f)
                                    .alignItems(Align.CENTER)
                                    .justifyContent(Justify.CENTER)
                                    .flexNone());
            if (isSelected) {
                Label checkLabel = new Label("✔");
                checkLabel.color(ShopTheme.color(ShopTheme.GOLD_PRIMARY));
                checkContainer.addChild(checkLabel);
            }
            row.addChild(checkContainer);
            // Визуальный предпросмотр компонентов стоимости (строго БЕЗ нумерации)
            List<CostEntry> costs = group.costs();
            for (int j = 0; j < costs.size(); j++) {
                if (j > 0) {
                    Label plusLabel = new Label("+");
                    plusLabel.color(ShopTheme.color(ShopTheme.TEXT_MUTED));
                    plusLabel.layout(s -> s.alignSelf(Align.CENTER).margin(0.0f, 1.0f));
                    row.addChild(plusLabel);
                }
                CostEntry cost = costs.get(j);
                ShopComponentPanel costView = new ShopComponentPanel(List.of(cost::createRender));
                costView.layout(style -> style.flexNone());
                activeDropdownPanels.add(costView);
                row.addChild(costView);
            }
            row.applyQueuedMutations();
            CostOptionButton btn = new CostOptionButton(row);
            btn.themeEnabled(false);
            btn.boxVisualEnabled(true);
            btn.backgroundVisible(true);
            btn.background(
                    ShopTheme.color(isSelected ? ShopTheme.BG_CARD_HOVER : ShopTheme.BG_CARD));
            btn.hoverBackground(ShopTheme.color(ShopTheme.BG_CARD_HOVER));
            btn.pressedBackground(ShopTheme.color(ShopTheme.BG_CARD));
            btn.borderVisible(true);
            btn.border(
                    ShopTheme.color(isSelected ? ShopTheme.GOLD_PRIMARY : ShopTheme.BORDER_SLOT));
            btn.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
            btn.radius(ShopTheme.RADIUS_SLOT);
            btn.focusRingVisible(false);
            btn.textPadding(6.0f, 2.0f);
            menu.item(btn);
            btn.onClick(
                    e -> {
                        activeCostGroupIndex = idx;
                        refreshPrices();
                        detachDropdownTooltips(layer);
                        for (ShopComponentPanel panel : activeDropdownPanels) panel.dispose();
                        activeDropdownPanels.clear();
                        menu.close();
                        layer.removeOverlay(menu);
                        activeCostDropdown = null;
                    });
            btn.layout(s -> s.minHeight(20.0f).minWidth(80.0f).flexNone().margin(1.0f, 0.0f));
        }
        float openX = lastClickRootX >= 0.0f ? lastClickRootX : getWidgetScreenX(triggerBtn);
        float openY =
                lastClickRootY >= 0.0f
                        ? lastClickRootY
                        : (getWidgetScreenY(triggerBtn)
                                + triggerBtn.layoutBounds().height()
                                + 2.0f);
        lastClickRootX = -1.0f;
        lastClickRootY = -1.0f;
        menu.openAt(openX, openY);
        layer.addOverlay(menu);
        activeCostDropdown = menu;
    }

    private @Nullable OverlayLayer findOverlayLayer() {
        if (tooltipOverlay != null) return tooltipOverlay;
        Widget current = this;
        while (current != null) {
            if (current instanceof OverlayLayer layer) return layer;
            current = current.parent();
        }
        OverlayLayer editorOverlay = ShopEditorScreen.getActiveOverlay();
        if (editorOverlay != null) {
            return editorOverlay;
        }
        return null;
    }

    private static float getWidgetScreenX(Widget widget) {
        float x = 0.0f;
        Widget cur = widget;
        while (cur != null) {
            if (cur instanceof OverlayLayer) break;
            x += cur.layoutBounds().x();
            if (cur instanceof ScrollView sv) {
                x -= sv.scrollX();
            }
            cur = cur.parent();
        }
        return x;
    }

    private static float getWidgetScreenY(Widget widget) {
        float y = 0.0f;
        Widget cur = widget;
        while (cur != null) {
            if (cur instanceof OverlayLayer) break;
            y += cur.layoutBounds().y();
            if (cur instanceof ScrollView sv) {
                y -= sv.scrollY();
            }
            cur = cur.parent();
        }
        return y;
    }

    /**
     * Delegates to the shared offer presentation API.
     *
     * @param group group used by this operation.
     * @param index index used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public static String formatCostGroupTitle(CostGroup group, int index) {
        return ShopOfferPresentation.formatCostGroupTitle(group, index);
    }

    // --- Хелперы для парсинга сущности ShopOffer ---

    /**
     * Delegates to the shared offer presentation API.
     *
     * @param offer offer used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public static String resolveTitle(ShopOffer offer) {
        return ShopOfferPresentation.resolveTitle(offer);
    }

    /**
     * Delegates to the shared offer presentation API.
     *
     * @param offer offer used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public static List<ItemStack> resolveRewardItems(ShopOffer offer) {
        return ShopOfferPresentation.resolveRewardItems(offer);
    }

    /**
     * Delegates to the shared offer presentation API.
     *
     * @param offer offer used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public static List<CostGroup> resolveCostGroups(ShopOffer offer) {
        return ShopOfferPresentation.resolveCostGroups(offer);
    }

    /**
     * Delegates to the shared offer presentation API.
     *
     * @param cost cost used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public static CostEntry resolveCostEntry(CostComponent cost) {
        return ShopOfferPresentation.resolveCostEntry(cost);
    }

    /**
     * Delegates to the shared offer presentation API.
     *
     * @param offer offer used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public static double resolveDiscount(ShopOffer offer) {
        return ShopOfferPresentation.resolveDiscount(offer);
    }

    /**
     * Delegates to the shared offer presentation API.
     *
     * @param amount amount used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public static String formatAmount(double amount) {
        return ShopOfferPresentation.formatAmount(amount);
    }

    public void updateBuyButtonState() {
        if (buyBtn == null) return;
        Minecraft mc = Minecraft.getInstance();
        Player player = mc != null ? mc.player : null;
        // В режиме редактора (при предварительном просмотре в инспекторе) кнопка активна для выбора
        if (ShopEditorState.get().isEditorMode() && onOpenPurchase == null) {
            buyBtn.enabled(true);
            applyBuyBtnVisual(true);
            return;
        }
        boolean canBuy = canPlayerPurchase(player);
        buyBtn.enabled(canBuy);
        applyBuyBtnVisual(canBuy);
    }

    private void applyBuyBtnVisual(boolean enabled) {
        if (buyBtn == null) return;
        // Активная кнопка — тёплый золотой акцент; неактивная — явно приглушена
        buyBtn.background(
                ShopTheme.color(enabled ? ShopTheme.BUY_BG : ShopTheme.BUY_BG_DISABLED));
        buyBtn.hoverBackground(
                ShopTheme.color(enabled ? ShopTheme.BUY_BG_HOVER : ShopTheme.BUY_BG_DISABLED));
        buyBtn.pressedBackground(
                ShopTheme.color(enabled ? ShopTheme.BUY_BG_PRESSED : ShopTheme.BUY_BG_DISABLED));
        buyBtn.borderVisible(true);
        buyBtn.border(
                ShopTheme.color(enabled ? ShopTheme.GOLD_PRIMARY : ShopTheme.BORDER_SEPARATOR));
        buyBtn.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        buyBtn.radius(ShopTheme.RADIUS_BUTTON);
        buyBtn.textColor(
                ShopTheme.color(enabled ? ShopTheme.TEXT_GOLD : ShopTheme.TEXT_DISABLED));
    }

    /**
     * Delegates to the shared purchase policy.
     *
     * @param player player used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public boolean canPlayerPurchase(@Nullable Player player) {
        return ShopPurchasePolicy.canPurchase(offer, costGroups, activeCostGroupIndex, player);
    }

    /**
     * Delegates to the shared purchase policy.
     *
     * @param cost cost used by this operation.
     * @param player player used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public static double checkCostBalance(CostEntry cost, @Nullable Player player) {
        return ShopPurchasePolicy.checkCostBalance(cost, player);
    }

    /**
     * Delegates to the shared purchase policy.
     *
     * @param str str used by this operation.
     * @return the operation result; absent objects may be null.
     */
    private static double parseAmount(String str) {
        return ShopPurchasePolicy.parseAmount(str);
    }
}
