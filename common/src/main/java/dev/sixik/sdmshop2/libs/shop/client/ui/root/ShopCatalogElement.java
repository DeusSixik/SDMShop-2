package dev.sixik.sdmshop2.libs.shop.client.ui.root;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopOfferPresentation;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopOfferView;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopPurchasePolicy;
import dev.sixik.sdmshop2.libs.shop.client.ui.assets.ShopIcons;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.ShopEditorScreen;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.ShopEditorState;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopTheme;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopContentButton;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopItemWidget;
import dev.sixik.sdmshop2.libs.shop.components.api.CostComponent;
import dev.sixik.sdmshop2.libs.shop.network.ShopNetworkManager;
import dev.sixik.unigui.api.event.EventPhase;
import dev.sixik.unigui.api.event.PointerExitedEvent;
import dev.sixik.unigui.api.event.PointerPressedEvent;
import dev.sixik.unigui.api.event.PointerReleasedEvent;
import dev.sixik.unigui.api.input.MouseCursor;
import dev.sixik.unigui.api.input.PointerButton;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.layout.Alignment;
import dev.sixik.unigui.api.layout.Justify;
import dev.sixik.unigui.api.render.SimpleTextureHandle;
import dev.sixik.unigui.api.widget.Visibility;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.Box;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.LinearBox;
import dev.sixik.unigui.widgets.containers.ScrollView;
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
import net.minecraft.world.item.Items;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

/**
 * Карточка торгового предложения в каталоге магазина в стиле "Obsidian & Gold". Точно воспроизводит
 * эталонный дизайн LDLib (Shop_1.png): - Аккуратный слот 38x38 с предметом 32x32 и vanilla-тултипом
 * - Иконки валют (предметы 14x14 или золотой символ ◎ для монет) - Звезда избранного без уродливых
 * серых рамок кнопок - Золотая кнопка покупки Buy
 */
public class ShopCatalogElement extends LinearBox implements ShopOfferView {
    private final @Nullable ShopOffer offer;
    private final @Nullable UUID offerUuid;
    private final String title;
    private final List<ItemStack> rewards;
    private final List<CostGroup> costGroups;
    private final double discountPercent;
    private boolean favorite;
    private boolean selected = false;
    private boolean showFavorite = true;
    private Button favoriteButton;
    private ImageView favoriteIcon;
    private @Nullable Consumer<ShopCatalogElement> onOpenPurchase;

    /** Item slots and their screen-local tooltip owner. */
    private final List<ShopItemWidget> itemSlots = new ArrayList<>();

    private @Nullable OverlayLayer tooltipOverlay;

    private int activeCostGroupIndex = 0;
    private HBox pricesBox;
    private final List<ShopItemWidget> priceSlots = new ArrayList<>();
    private float lastClickRootX = -1.0f;
    private float lastClickRootY = -1.0f;
    private boolean cardPressed = false;
    private Button buyBtn;

    public ShopCatalogElement(
            ShopOffer offer, @Nullable Consumer<ShopCatalogElement> onOpenPurchase) {
        this(
                offer,
                offer.getUUID(),
                resolveTitle(offer),
                resolveRewardItems(offer),
                resolveCostGroups(offer),
                resolveDiscount(offer),
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
        super(Orientation.VERTICAL);
        this.offer = offer;
        this.offerUuid = offer != null ? offer.getUUID() : offerUuid;
        this.title = title;
        this.rewards =
                rewards == null || rewards.isEmpty()
                        ? List.of(Items.BARRIER.getDefaultInstance())
                        : rewards;
        this.costGroups = costGroups == null || costGroups.isEmpty() ? List.of() : costGroups;
        this.discountPercent = discountPercent;
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
        layout(style -> style.height(96.0f).flexNone().padding(6.0f, 8.0f));
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
                });
        initChildren();
    }

    private void initChildren() {
        // --- 1. Шапка (Бейдж / Заголовок / Звезда) ---
        HBox header = new HBox();
        header.layout(
                style ->
                        style.height(14.0f)
                                .alignItems(Align.CENTER)
                                .justifyContent(Justify.SPACE_BETWEEN)
                                .flexNone());
        // Левая часть: скидка или бейдж набора, либо пустая распорка для симметрии
        if (discountPercent > 0) {
            Box discountBadge = new Box();
            discountBadge.themeEnabled(false);
            discountBadge.boxVisualEnabled(true);
            discountBadge.backgroundVisible(true);
            discountBadge.background(ShopTheme.color(ShopTheme.DISCOUNT_BADGE));
            discountBadge.radius(ShopTheme.RADIUS_BADGE);
            discountBadge.layout(style -> style.padding(2.0f, 0.0f).flexNone());
            Label dLabel = new Label("-" + (int) discountPercent + "%");
            dLabel.color(ShopTheme.color(ShopTheme.TEXT_PRIMARY));
            discountBadge.addChild(dLabel);
            header.addChild(discountBadge);
        } else if (rewards.size() > 1) {
            Box bundleBadge = new Box();
            bundleBadge.themeEnabled(false);
            bundleBadge.boxVisualEnabled(true);
            bundleBadge.backgroundVisible(true);
            bundleBadge.background(ShopTheme.color(ShopTheme.BUNDLE_BADGE));
            bundleBadge.radius(ShopTheme.RADIUS_BADGE);
            bundleBadge.layout(style -> style.padding(2.0f, 0.0f).flexNone());
            Label bLabel = new Label("x" + rewards.size());
            bLabel.color(ShopTheme.color(ShopTheme.TEXT_PRIMARY));
            bundleBadge.addChild(bLabel);
            header.addChild(bundleBadge);
        } else {
            Box leftSpacer = new Box();
            leftSpacer.layout(style -> style.fixed(12.0f, 12.0f).flexNone());
            header.addChild(leftSpacer);
        }
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
        // --- 2. Центр (Слот с предметом строго по центру слота) ---
        if (rewards.size() == 1) {
            ShopItemWidget itemSlot =
                    new ShopItemWidget(rewards.get(0), 32.0f).asSlot(38.0f, 38.0f);
            itemSlot.decorations(true);
            itemSlots.add(itemSlot);

            addChild(itemSlot);
        } else {
            HBox bundleRow = new HBox();
            bundleRow.layout(
                    style ->
                            style.alignSelf(Align.CENTER)
                                    .alignItems(Align.CENTER)
                                    .gap(4.0f)
                                    .height(38.0f)
                                    .flexNone());
            int maxDisplay = Math.min(4, rewards.size());
            for (int i = 0; i < maxDisplay; i++) {
                ShopItemWidget miniSlot =
                        new ShopItemWidget(rewards.get(i), 18.0f).asSlot(26.0f, 26.0f);
                miniSlot.decorations(true);
                itemSlots.add(miniSlot);

                bundleRow.addChild(miniSlot);
            }
            if (rewards.size() > 4) {
                Box moreSlot = new Box();
                moreSlot.themeEnabled(false);
                moreSlot.boxVisualEnabled(true);
                moreSlot.backgroundVisible(true);
                moreSlot.background(ShopTheme.color(ShopTheme.BG_SLOT));
                moreSlot.borderVisible(true);
                moreSlot.border(ShopTheme.color(ShopTheme.BORDER_SLOT));
                moreSlot.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
                moreSlot.radius(ShopTheme.RADIUS_SLOT);
                moreSlot.layout(
                        s ->
                                s.fixed(26.0f, 26.0f)
                                        .alignItems(Align.CENTER)
                                        .justifyContent(Justify.CENTER));
                Label moreLabel = new Label("+" + (rewards.size() - 4));
                moreLabel.color(ShopTheme.color(ShopTheme.TEXT_SECONDARY));
                moreSlot.addChild(moreLabel);
                bundleRow.addChild(moreSlot);
            }
            addChild(bundleRow);
        }
        // --- 3. Разделитель ---
        Separator separator = new Separator();
        separator.orientation(Orientation.HORIZONTAL);
        separator.thickness(1.0f);
        separator.color().setArgb(ShopTheme.BORDER_SEPARATOR);
        separator.layout(
                style -> style.widthPercent(100.0f).height(1.0f).margin(2.0f, 0.0f).flexNone());
        addChild(separator);
        // --- 4. Подвал (Цены и золотая кнопка покупки) ---
        HBox footer = new HBox();
        footer.layout(
                style ->
                        style.height(18.0f)
                                .alignItems(Align.CENTER)
                                .justifyContent(Justify.SPACE_BETWEEN)
                                .flexNone());
        pricesBox = new HBox();
        pricesBox.layout(style -> style.alignItems(Align.CENTER).gap(3.0f).flexGrow(1.0f));
        refreshPrices();
        footer.addChild(pricesBox);
        buyBtn = new Button(Component.translatable("shop.ui.offer_element.button.buy").getString());
        buyBtn.backgroundVisible(true);
        buyBtn.focusRingVisible(false);
        buyBtn.textPadding(8.0f, 2.0f);
        buyBtn.layout(style -> style.height(18.0f).flexNone());
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
        super.dispose();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            border(ShopTheme.color(ShopTheme.GOLD_PRIMARY));
            borderWidth(ShopTheme.BORDER_WIDTH_ACCENT);
        } else {
            border(ShopTheme.color(ShopTheme.BORDER_SUBTLE));
            borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        }
    }

    public @Nullable UUID getOfferUuid() {
        return offer != null ? offer.getUUID() : offerUuid;
    }

    public void attachTooltips(@Nullable OverlayLayer overlayLayer) {
        tooltipOverlay = overlayLayer;
        for (ShopItemWidget slot : itemSlots) {
            slot.attachTooltip(overlayLayer);
        }
    }

    public void detachTooltips(@Nullable OverlayLayer overlayLayer) {
        for (ShopItemWidget slot : itemSlots) {
            slot.detachTooltip(overlayLayer);
        }
        detachDropdownTooltips(overlayLayer);
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

    public List<ItemStack> getRewards() {
        return rewards;
    }

    public List<CostGroup> getCostGroups() {
        return costGroups;
    }

    public double getDiscountPercent() {
        return discountPercent;
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
        pricesBox.clearChildren();
        OverlayLayer activeOverlay = tooltipOverlay;
        for (ShopItemWidget slot : priceSlots) {
            if (activeOverlay != null) {
                slot.detachTooltip(activeOverlay);
            }
            itemSlots.remove(slot);
        }
        priceSlots.clear();
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
                        style.height(18.0f)
                                .alignItems(Align.CENTER)
                                .padding(4.0f, 1.0f)
                                .gap(3.0f)
                                .flexNone());
        int maxVisible = 3;
        int displayCount = Math.min(costs.size(), maxVisible);
        for (int i = 0; i < displayCount; i++) {
            if (i > 0) {
                Label plusLabel = new Label("+");
                plusLabel.color(ShopTheme.color(ShopTheme.TEXT_MUTED));
                plusLabel.layout(s -> s.alignSelf(Align.CENTER).margin(0.0f, 1.0f));
                pricePanel.addChild(plusLabel);
            }
            CostEntry cost = costs.get(i);
            if (cost.itemIcon() != null && !cost.itemIcon().isEmpty()) {
                ShopItemWidget costIcon = new ShopItemWidget(cost.itemIcon(), 12.0f);
                costIcon.decorations(false);
                priceSlots.add(costIcon);
                itemSlots.add(costIcon);
                if (activeOverlay != null) {
                    costIcon.attachTooltip(activeOverlay);
                }
                costIcon.layout(style -> style.fixed(12.0f, 12.0f).flexNone());
                pricePanel.addChild(costIcon);
            } else if (cost.textureIcon() != null) {
                String tex = cost.textureIcon().toString();
                ImageView texIcon = new ImageView(new SimpleTextureHandle(tex, 12, 12, tex));
                texIcon.layout(style -> style.fixed(12.0f, 12.0f).flexNone());
                pricePanel.addChild(texIcon);
            } else if (cost.textIcon() != null && !cost.textIcon().isBlank()) {
                Label textIcon = new Label(cost.textIcon());
                textIcon.color(ShopTheme.color(ShopTheme.GOLD_PRIMARY));
                pricePanel.addChild(textIcon);
            } else {
                ShopItemWidget coinIcon =
                        new ShopItemWidget(new ItemStack(Items.GOLD_NUGGET), 12.0f);
                coinIcon.decorations(false);
                priceSlots.add(coinIcon);
                itemSlots.add(coinIcon);
                if (activeOverlay != null) {
                    coinIcon.attachTooltip(activeOverlay);
                }
                coinIcon.layout(style -> style.fixed(12.0f, 12.0f).flexNone());
                pricePanel.addChild(coinIcon);
            }
            Label amountLabel = new Label(cost.amount());
            amountLabel.color(ShopTheme.color(ShopTheme.TEXT_PRIMARY));
            pricePanel.addChild(amountLabel);
        }
        if (costs.size() > maxVisible) {
            Label moreLabel = new Label("+" + (costs.size() - maxVisible));
            moreLabel.color(ShopTheme.color(ShopTheme.TEXT_MUTED));
            pricePanel.addChild(moreLabel);
        }
        pricesBox.addChild(pricePanel);
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
    private final List<ShopItemWidget> activeDropdownSlots = new ArrayList<>();

    private void detachDropdownTooltips(@Nullable OverlayLayer layer) {
        if (layer != null) {
            for (ShopItemWidget slot : activeDropdownSlots) {
                slot.detachTooltip(layer);
            }
        }
        activeDropdownSlots.clear();
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
                if (cost.itemIcon() != null && !cost.itemIcon().isEmpty()) {
                    ShopItemWidget costIcon = new ShopItemWidget(cost.itemIcon(), 12.0f);
                    costIcon.decorations(false);
                    costIcon.layout(s -> s.fixed(12.0f, 12.0f).flexNone());
                    row.addChild(costIcon);
                } else if (cost.textureIcon() != null) {
                    String tex = cost.textureIcon().toString();
                    ImageView texIcon = new ImageView(new SimpleTextureHandle(tex, 12, 12, tex));
                    texIcon.layout(s -> s.fixed(12.0f, 12.0f).flexNone());
                    row.addChild(texIcon);
                } else if (cost.textIcon() != null && !cost.textIcon().isBlank()) {
                    Label textIcon = new Label(cost.textIcon());
                    textIcon.color(ShopTheme.color(ShopTheme.GOLD_PRIMARY));
                    row.addChild(textIcon);
                } else {
                    ShopItemWidget coinIcon =
                            new ShopItemWidget(new ItemStack(Items.GOLD_NUGGET), 12.0f);
                    coinIcon.decorations(false);
                    coinIcon.layout(s -> s.fixed(12.0f, 12.0f).flexNone());
                    row.addChild(coinIcon);
                }
                Label amountLabel = new Label(cost.amount());
                amountLabel.color(
                        ShopTheme.color(isSelected ? ShopTheme.TEXT_GOLD : ShopTheme.TEXT_PRIMARY));
                row.addChild(amountLabel);
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
                        activeCostDropdown = null;
                    });
            btn.layout(s -> s.height(20.0f).minWidth(80.0f).flexNone().margin(1.0f, 0.0f));
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
        buyBtn.background(ShopTheme.color(ShopTheme.BG_CARD));
        buyBtn.hoverBackground(
                ShopTheme.color(enabled ? ShopTheme.BG_CARD_HOVER : ShopTheme.BG_CARD));
        buyBtn.pressedBackground(
                ShopTheme.color(enabled ? ShopTheme.GOLD_PRIMARY : ShopTheme.BG_CARD));
        buyBtn.borderVisible(true);
        buyBtn.border(ShopTheme.color(enabled ? ShopTheme.GOLD_PRIMARY : ShopTheme.BORDER_SUBTLE));
        buyBtn.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        buyBtn.radius(ShopTheme.RADIUS_BUTTON);
        buyBtn.textColor(ShopTheme.color(enabled ? ShopTheme.TEXT_PRIMARY : ShopTheme.TEXT_MUTED));
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
