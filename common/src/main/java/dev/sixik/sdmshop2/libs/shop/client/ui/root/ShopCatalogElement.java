package dev.sixik.sdmshop2.libs.shop.client.ui.root;

import dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.IconType;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopScreenController;
import dev.sixik.sdmshop2.libs.shop.client.ui.assets.ShopIcons;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopTheme;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopItemWidget;
import dev.sixik.sdmshop2.libs.shop.components.ItemCostComponent;
import dev.sixik.sdmshop2.libs.shop.components.ItemRewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.ItemTagCostComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.CostComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.RewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import dev.sixik.sdmshop2.libs.shop.components.money.MoneyCostComponent;
import dev.sixik.sdmshop2.libs.shop.components.promo.effects.DiscountComponent;
import dev.sixik.sdmshop2.libs.shop.network.ShopNetworkManager;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.ShopEditorScreen;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.ShopEditorState;
import dev.sixik.unigui.api.event.Event;
import dev.sixik.unigui.api.event.EventPhase;
import dev.sixik.unigui.api.event.PointerEnteredEvent;
import dev.sixik.unigui.api.event.PointerExitedEvent;
import dev.sixik.unigui.api.event.PointerPressedEvent;
import dev.sixik.unigui.api.event.PointerReleasedEvent;
import dev.sixik.unigui.api.input.PointerButton;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.layout.Alignment;
import dev.sixik.unigui.api.layout.Justify;
import dev.sixik.unigui.api.layout.LayoutContext;
import dev.sixik.unigui.api.layout.PositionType;
import dev.sixik.unigui.api.math.MutableRect;
import dev.sixik.unigui.api.math.RectView;
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
import dev.sixik.unigui.widgets.feedback.OverlayHostAware;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.interaction.Button;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import dev.sixik.unigui.api.render.SimpleTextureHandle;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

/**
 * Карточка торгового предложения в каталоге магазина в стиле "Obsidian & Gold".
 * Точно воспроизводит эталонный дизайн LDLib (Shop_1.png):
 * - Аккуратный слот 38x38 с предметом 32x32 и vanilla-тултипом
 * - Иконки валют (предметы 14x14 или золотой символ ◎ для монет)
 * - Звезда избранного без уродливых серых рамок кнопок
 * - Золотая кнопка покупки Buy
 */
public class ShopCatalogElement extends LinearBox {

    public record CostEntry(
            @Nullable ItemStack itemIcon,
            @Nullable ResourceLocation textureIcon,
            @Nullable String textIcon,
            String amount,
            @Nullable CostComponent component
    ) {
        public CostEntry(@Nullable ItemStack itemIcon, @Nullable String textIcon, String amount, @Nullable CostComponent component) {
            this(itemIcon, null, textIcon, amount, component);
        }

        public CostEntry(ItemStack itemIcon, String amount) {
            this(itemIcon, null, null, amount, null);
        }

        public CostEntry(String textIcon, String amount) {
            this(null, null, textIcon, amount, null);
        }

        public CostEntry(ItemStack itemIcon, @Nullable String textIcon, String amount) {
            this(itemIcon, null, textIcon, amount, null);
        }

        public ItemStack icon() {
            return itemIcon != null ? itemIcon : ItemStack.EMPTY;
        }
    }

    public record CostGroup(String groupId, List<CostEntry> costs) {}

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
    private final List<ShopItemWidget> itemSlots = new ArrayList<>();
    private int activeCostGroupIndex = 0;
    private HBox pricesBox;
    private final List<ShopItemWidget> priceSlots = new ArrayList<>();
    private float lastClickRootX = -1.0f;
    private float lastClickRootY = -1.0f;
    private boolean cardPressed = false;

    public ShopCatalogElement(ShopOffer offer, @Nullable Consumer<ShopCatalogElement> onOpenPurchase) {
        this(
                offer,
                offer.getUUID(),
                resolveTitle(offer),
                resolveRewardItems(offer),
                resolveCostGroups(offer),
                resolveDiscount(offer),
                ShopClientCache.isFavorite(offer.getUUID()),
                onOpenPurchase
        );
    }

    public ShopCatalogElement(ShopOffer offer) {
        this(offer, elem -> ShopNetworkManager.purchaseOffer(offer, "", 1));
    }

    public ShopCatalogElement(
            String title,
            ItemStack itemStack,
            List<CostEntry> costs,
            boolean favorite,
            @Nullable Runnable onBuy
    ) {
        this(title, itemStack, costs, favorite, onBuy != null ? elem -> onBuy.run() : null);
    }

    public ShopCatalogElement(
            String title,
            ItemStack itemStack,
            List<CostEntry> costs,
            boolean favorite,
            @Nullable Consumer<ShopCatalogElement> onOpenPurchase
    ) {
        this(
                null,
                null,
                title,
                List.of(itemStack),
                List.of(new CostGroup("", costs)),
                0.0,
                favorite,
                onOpenPurchase
        );
    }

    public ShopCatalogElement(
            String title,
            List<ItemStack> rewards,
            List<CostGroup> costGroups,
            double discountPercent,
            boolean favorite,
            @Nullable Consumer<ShopCatalogElement> onOpenPurchase
    ) {
        this(null, null, title, rewards, costGroups, discountPercent, favorite, onOpenPurchase);
    }

    public ShopCatalogElement(
            @Nullable UUID offerUuid,
            String title,
            List<ItemStack> rewards,
            List<CostGroup> costGroups,
            double discountPercent,
            boolean favorite,
            @Nullable Consumer<ShopCatalogElement> onOpenPurchase
    ) {
        this(null, offerUuid, title, rewards, costGroups, discountPercent, favorite, onOpenPurchase);
    }

    private ShopCatalogElement(
            @Nullable ShopOffer offer,
            @Nullable UUID offerUuid,
            String title,
            List<ItemStack> rewards,
            List<CostGroup> costGroups,
            double discountPercent,
            boolean favorite,
            @Nullable Consumer<ShopCatalogElement> onOpenPurchase
    ) {
        super(Orientation.VERTICAL);
        this.offer = offer;
        this.offerUuid = offer != null ? offer.getUUID() : offerUuid;
        this.title = title;
        this.rewards = rewards == null || rewards.isEmpty() ? List.of(Items.BARRIER.getDefaultInstance()) : rewards;
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

        layout(style -> style
                .height(96.0f)
                .flexNone()
                .padding(6.0f, 8.0f)
        );

        on(PointerPressedEvent.TYPE, event -> {
            if (event.phase() == EventPhase.CAPTURE) return;
            if (event.button() == PointerButton.PRIMARY) {
                cardPressed = true;
            }
        });

        on(PointerReleasedEvent.TYPE, event -> {
            if (event.phase() == EventPhase.CAPTURE) return;
            if (event.button() != PointerButton.PRIMARY) return;
            if (!cardPressed) return;
            cardPressed = false;
            if (event.isCancelled()) return;
            if (ShopEditorState.get().isEditorMode() && offer != null) {
                ShopEditorState.get().selectTarget(offer);
            }
        });

        on(PointerExitedEvent.TYPE, event -> {
            cardPressed = false;
        });

        initChildren();
    }

    private void initChildren() {
        // --- 1. Шапка (Бейдж / Заголовок / Звезда) ---
        HBox header = new HBox();
        header.layout(style -> style
                .height(14.0f)
                .alignItems(Align.CENTER)
                .justifyContent(Justify.SPACE_BETWEEN)
                .flexNone()
        );

        // Левая часть: скидка или бейдж набора, либо пустая распорка для симметрии
        if (discountPercent > 0) {
            Box discountBadge = new Box();
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
        favoriteButton.boxVisualEnabled(false);
        favoriteButton.backgroundVisible(false);
        favoriteButton.borderVisible(false);
        favoriteButton.focusRingVisible(false);
        favoriteButton.textPadding(0.0f, 0.0f);
        favoriteButton.layout(style -> style.fixed(12.0f, 12.0f).flexNone());

        favoriteIcon = new ImageView(favorite ? ShopIcons.STAR_FULL : ShopIcons.STAR_EMPTY);
        favoriteIcon.layout(style -> style.fixed(12.0f, 12.0f));
        favoriteButton.addChild(favoriteIcon);

        favoriteButton.onClick(event -> {
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
            ShopItemWidget itemSlot = new ShopItemWidget(rewards.get(0), 32.0f).asSlot(38.0f, 38.0f);
            itemSlot.decorations(true);
            itemSlots.add(itemSlot);
            itemSlot.attachTooltip(ShopScreenController.getActiveOverlay());
            addChild(itemSlot);
        } else {
            HBox bundleRow = new HBox();
            bundleRow.layout(style -> style
                    .alignSelf(Align.CENTER)
                    .alignItems(Align.CENTER)
                    .gap(4.0f)
                    .height(38.0f)
                    .flexNone()
            );

            int maxDisplay = Math.min(4, rewards.size());
            for (int i = 0; i < maxDisplay; i++) {
                ShopItemWidget miniSlot = new ShopItemWidget(rewards.get(i), 18.0f).asSlot(26.0f, 26.0f);
                miniSlot.decorations(true);
                itemSlots.add(miniSlot);
                miniSlot.attachTooltip(ShopScreenController.getActiveOverlay());
                bundleRow.addChild(miniSlot);
            }
            if (rewards.size() > 4) {
                Box moreSlot = new Box();
                moreSlot.boxVisualEnabled(true);
                moreSlot.backgroundVisible(true);
                moreSlot.background(ShopTheme.color(ShopTheme.BG_SLOT));
                moreSlot.borderVisible(true);
                moreSlot.border(ShopTheme.color(ShopTheme.BORDER_SLOT));
                moreSlot.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
                moreSlot.radius(ShopTheme.RADIUS_SLOT);
                moreSlot.layout(s -> s.fixed(26.0f, 26.0f).alignItems(Align.CENTER).justifyContent(Justify.CENTER));
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
        separator.layout(style -> style
                .widthPercent(100.0f)
                .height(1.0f)
                .margin(2.0f, 0.0f)
                .flexNone()
        );
        addChild(separator);

        // --- 4. Подвал (Цены и золотая кнопка покупки) ---
        HBox footer = new HBox();
        footer.layout(style -> style
                .height(18.0f)
                .alignItems(Align.CENTER)
                .justifyContent(Justify.SPACE_BETWEEN)
                .flexNone()
        );

        pricesBox = new HBox();
        pricesBox.layout(style -> style
                .alignItems(Align.CENTER)
                .gap(3.0f)
                .flexGrow(1.0f)
        );
        refreshPrices();
        footer.addChild(pricesBox);

        if (ShopEditorState.get().isEditorMode()) {
            Box buyBadge = new Box();
            buyBadge.boxVisualEnabled(true);
            buyBadge.backgroundVisible(true);
            buyBadge.background(ShopTheme.color(ShopTheme.BG_CARD));
            buyBadge.borderVisible(true);
            buyBadge.border(ShopTheme.color(ShopTheme.GOLD_PRIMARY));
            buyBadge.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
            buyBadge.radius(ShopTheme.RADIUS_BUTTON);
            buyBadge.layout(style -> style.height(18.0f).padding(8.0f, 2.0f).flexNone());
            Label buyLabel = new Label(Component.translatable("shop.ui.offer_element.button.buy").getString());
            buyLabel.color(ShopTheme.color(ShopTheme.TEXT_PRIMARY));
            buyBadge.addChild(buyLabel);
            footer.addChild(buyBadge);
        } else {
            Button buyBtn = new Button(Component.translatable("shop.ui.offer_element.button.buy").getString());
            buyBtn.backgroundVisible(true);
            buyBtn.background(ShopTheme.color(ShopTheme.BG_CARD));
            buyBtn.hoverBackground(ShopTheme.color(ShopTheme.BG_CARD_HOVER));
            buyBtn.pressedBackground(ShopTheme.color(ShopTheme.GOLD_PRIMARY));
            buyBtn.borderVisible(true);
            buyBtn.border(ShopTheme.color(ShopTheme.GOLD_PRIMARY));
            buyBtn.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
            buyBtn.radius(ShopTheme.RADIUS_BUTTON);
            buyBtn.textColor(ShopTheme.color(ShopTheme.TEXT_PRIMARY));
            buyBtn.focusRingVisible(false);
            buyBtn.textPadding(8.0f, 2.0f);
            buyBtn.layout(style -> style.height(18.0f).flexNone());

            buyBtn.onClick(event -> {
                if (onOpenPurchase != null) {
                    onOpenPurchase.accept(this);
                }
            });
            footer.addChild(buyBtn);
        }
        addChild(footer);
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
        if (overlayLayer == null) return;
        for (ShopItemWidget slot : itemSlots) {
            slot.attachTooltip(overlayLayer);
        }
    }

    public void detachTooltips(@Nullable OverlayLayer overlayLayer) {
        if (overlayLayer == null) return;
        for (ShopItemWidget slot : itemSlots) {
            slot.detachTooltip(overlayLayer);
        }
        detachDropdownTooltips(overlayLayer);
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
        this.activeCostGroupIndex = activeCostGroupIndex;
        refreshPrices();
    }

    public void refreshPrices() {
        if (pricesBox == null) return;
        pricesBox.clearChildren();

        OverlayLayer activeOverlay = ShopScreenController.getActiveOverlay();
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
        pricePanel.boxVisualEnabled(true);
        pricePanel.backgroundVisible(true);
        pricePanel.background(ShopTheme.color(ShopTheme.BG_SLOT));
        pricePanel.borderVisible(true);
        pricePanel.border(ShopTheme.color(ShopTheme.BORDER_SLOT));
        pricePanel.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        pricePanel.radius(ShopTheme.RADIUS_SLOT);
        pricePanel.layout(style -> style
                .height(18.0f)
                .alignItems(Align.CENTER)
                .padding(4.0f, 1.0f)
                .gap(3.0f)
                .flexNone()
        );

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
                ShopItemWidget coinIcon = new ShopItemWidget(new ItemStack(Items.GOLD_NUGGET), 12.0f);
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

            switchBtn.onClick(e -> {
                activeCostGroupIndex = (activeCostGroupIndex == 0) ? 1 : 0;
                refreshPrices();
            });
            pricesBox.addChild(switchBtn);
        } else if (costGroups.size() > 2) {
            Button dropdownBtn = new Button("⇄ " + (currentGroupIdx + 1) + "/" + costGroups.size() + " ▾");
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

            dropdownBtn.on(PointerPressedEvent.TYPE, e -> {
                lastClickRootX = e.rootX();
                lastClickRootY = e.rootY();
            });
            dropdownBtn.onClick(e -> openCostGroupsDropdown(dropdownBtn));
            pricesBox.addChild(dropdownBtn);
        }

        pricesBox.applyQueuedMutations();
    }

    private static @Nullable ContextMenu activeCostDropdown = null;
    private static final List<ShopItemWidget> activeDropdownSlots = new ArrayList<>();

    private static void detachDropdownTooltips(@Nullable OverlayLayer layer) {
        if (layer != null) {
            for (ShopItemWidget slot : activeDropdownSlots) {
                slot.detachTooltip(layer);
            }
        }
        activeDropdownSlots.clear();
    }

    public static class CostOptionButton extends Button {
        private final Widget content;

        public CostOptionButton(Widget content) {
            super("");
            this.content = content;
            if (content != null) {
                addChild(content);
            }
        }

        public Widget getContent() {
            return content;
        }

        @Override
        public void measure(LayoutContext context) {
            super.measure(context);
            if (content != null) {
                content.measure(context);
                float w = content.desiredSize().width() + textPaddingX() * 2;
                float h = Math.max(intrinsicTextHeight(), content.desiredSize().height() + textPaddingY() * 2);
                setDesiredSize(resolveDesiredSize(context, w, h));
            }
        }

        @Override
        public void arrange(RectView bounds) {
            super.arrange(bounds);
            if (content != null) {
                float padX = textPaddingX();
                float padY = textPaddingY();
                float x = bounds.x() + padX;
                float y = bounds.y() + padY;
                float w = Math.max(0.0f, bounds.width() - padX * 2);
                float h = Math.max(0.0f, bounds.height() - padY * 2);
                content.arrange(new MutableRect(x, y, w, h));
            }
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
            } catch (Exception ignored) {}
            activeCostDropdown = null;
            if (wasOpened) {
                return;
            }
        }

        ContextMenu menu = new ContextMenu();
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
            row.layout(style -> style
                    .alignItems(Align.CENTER)
                    .gap(3.0f)
            );

            // Индикатор выбора: фиксированный контейнер 12x12 с галочкой для идеального выравнивания строк
            Box checkContainer = new Box();
            checkContainer.layout(style -> style
                    .fixed(12.0f, 12.0f)
                    .alignItems(Align.CENTER)
                    .justifyContent(Justify.CENTER)
                    .flexNone()
            );
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
                    activeDropdownSlots.add(costIcon);
                    costIcon.attachTooltip(layer);
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
                    ShopItemWidget coinIcon = new ShopItemWidget(new ItemStack(Items.GOLD_NUGGET), 12.0f);
                    coinIcon.decorations(false);
                    activeDropdownSlots.add(coinIcon);
                    coinIcon.attachTooltip(layer);
                    coinIcon.layout(s -> s.fixed(12.0f, 12.0f).flexNone());
                    row.addChild(coinIcon);
                }

                Label amountLabel = new Label(cost.amount());
                amountLabel.color(ShopTheme.color(isSelected ? ShopTheme.TEXT_GOLD : ShopTheme.TEXT_PRIMARY));
                row.addChild(amountLabel);
            }

            row.applyQueuedMutations();

            CostOptionButton btn = new CostOptionButton(row);
            btn.boxVisualEnabled(true);
            btn.backgroundVisible(true);
            btn.background(ShopTheme.color(isSelected ? ShopTheme.BG_CARD_HOVER : ShopTheme.BG_CARD));
            btn.hoverBackground(ShopTheme.color(ShopTheme.BG_CARD_HOVER));
            btn.pressedBackground(ShopTheme.color(ShopTheme.BG_CARD));
            btn.borderVisible(true);
            btn.border(ShopTheme.color(isSelected ? ShopTheme.GOLD_PRIMARY : ShopTheme.BORDER_SLOT));
            btn.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
            btn.radius(ShopTheme.RADIUS_SLOT);
            btn.focusRingVisible(false);
            btn.textPadding(6.0f, 2.0f);

            menu.item(btn);

            btn.onClick(e -> {
                activeCostGroupIndex = idx;
                refreshPrices();
                detachDropdownTooltips(layer);
                activeCostDropdown = null;
            });
            btn.layout(s -> s.height(20.0f).minWidth(80.0f).flexNone().margin(1.0f, 0.0f));
        }

        float openX = lastClickRootX >= 0.0f ? lastClickRootX : getWidgetScreenX(triggerBtn);
        float openY = lastClickRootY >= 0.0f ? lastClickRootY : (getWidgetScreenY(triggerBtn) + triggerBtn.layoutBounds().height() + 2.0f);
        lastClickRootX = -1.0f;
        lastClickRootY = -1.0f;

        menu.openAt(openX, openY);
        layer.addOverlay(menu);
        activeCostDropdown = menu;
    }

    private @Nullable OverlayLayer findOverlayLayer() {
        Widget current = this;
        while (current != null) {
            if (current instanceof OverlayLayer layer) return layer;
            current = current.parent();
        }
        OverlayLayer editorOverlay = ShopEditorScreen.getActiveOverlay();
        if (editorOverlay != null) {
            return editorOverlay;
        }
        return ShopScreenController.getActiveOverlay();
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

    public static String formatCostGroupTitle(CostGroup group, int index) {
        List<String> parts = new ArrayList<>();
        for (CostEntry cost : group.costs()) {
            String name;
            if (cost.itemIcon() != null && !cost.itemIcon().isEmpty()) {
                name = cost.itemIcon().getHoverName().getString();
            } else if (cost.textureIcon() != null) {
                name = cost.textureIcon().getPath();
            } else if (cost.textIcon() != null && !cost.textIcon().isBlank()) {
                name = cost.textIcon();
            } else {
                name = "Coins";
            }
            parts.add(cost.amount() + "x " + name);
        }
        if (parts.isEmpty()) {
            return "Вариант " + (index + 1);
        }
        return (index + 1) + ". " + String.join(" + ", parts);
    }

    // --- Хелперы для парсинга сущности ShopOffer ---

    public static String resolveTitle(ShopOffer offer) {
        return offer.getComponent(NameComponent.class)
                .map(NameComponent::getName)
                .filter(name -> !name.isBlank())
                .map(name -> I18n.exists(name) ? Component.translatable(name).getString() : name)
                .orElseGet(() -> offer.getComponents(RewardComponent.class).stream()
                        .map(RewardComponent::getDisplayTitle)
                        .filter(Objects::nonNull)
                        .map(Component::getString)
                        .filter(title -> !title.isBlank())
                        .findFirst()
                        .orElse(offer.getUUID().toString()));
    }

    public static List<ItemStack> resolveRewardItems(ShopOffer offer) {
        List<ItemStack> list = new ArrayList<>();
        for (RewardComponent reward : offer.getComponents(RewardComponent.class)) {
            if (reward instanceof ItemRewardComponent itemReward) {
                list.add(itemReward.getRewardItem().copyWithCount(itemReward.getAmount()));
            }
        }
        if (list.isEmpty()) {
            list.add(Items.BARRIER.getDefaultInstance());
        }
        return list;
    }

    public static List<CostGroup> resolveCostGroups(ShopOffer offer) {
        Map<String, List<CostEntry>> groups = new LinkedHashMap<>();
        for (CostComponent cost : offer.getComponents(CostComponent.class)) {
            String groupId = cost.getGroupId() == null ? "" : cost.getGroupId();
            CostEntry entry = resolveCostEntry(cost);
            groups.computeIfAbsent(groupId, k -> new ArrayList<>()).add(entry);
        }
        List<CostGroup> result = new ArrayList<>();
        for (Map.Entry<String, List<CostEntry>> entry : groups.entrySet()) {
            result.add(new CostGroup(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    public static CostEntry resolveCostEntry(CostComponent cost) {
        String formatted = formatAmount(cost.getBaseAmount());
        if (cost instanceof ItemCostComponent itemCost) {
            return new CostEntry(itemCost.getItem(), null, formatted, cost);
        } else if (cost instanceof MoneyCostComponent moneyCost) {
            if (ShopEditorState.get().isEditorMode()) {
                CurrencyDraft draft = ShopEditorState.get().findCurrencyDraft(moneyCost.getMoneyId());
                if (draft != null) {
                    if (draft.kind() == CurrencyDraft.Kind.ITEM && draft.itemStack() != null && !draft.itemStack().isEmpty()) {
                        return new CostEntry(draft.itemStack(), null, null, formatted, cost);
                    } else if (draft.kind() == CurrencyDraft.Kind.TEXTURE && draft.iconTexture() != null) {
                        return new CostEntry(null, draft.iconTexture(), null, formatted, cost);
                    } else if (draft.kind() == CurrencyDraft.Kind.TEXT) {
                        String sym = draft.iconText();
                        return new CostEntry(null, null, sym != null && !sym.isBlank() ? sym : "$", formatted, cost);
                    }
                }
            }
            ICurrency currency = SDMEconomyServiceClient.getCurrency(moneyCost.getMoneyId());
            if (currency != null) {
                CurrencyIcon curIcon = currency.getIcon();
                if (curIcon != null) {
                    if (curIcon.type() == IconType.ITEM) {
                        if (curIcon.icon() instanceof ItemStack s) return new CostEntry(s, null, null, formatted, cost);
                        if (curIcon.icon() instanceof Item item) return new CostEntry(item.getDefaultInstance(), null, null, formatted, cost);
                    } else if (curIcon.type() == IconType.TEXTURE) {
                        if (curIcon.icon() instanceof ResourceLocation loc) {
                            return new CostEntry(null, loc, null, formatted, cost);
                        } else if (curIcon.icon() instanceof String str) {
                            if (str.contains(":") || str.contains("/") || str.endsWith(".png")) {
                                ResourceLocation loc = ResourceLocation.tryParse(str);
                                if (loc != null) return new CostEntry(null, loc, null, formatted, cost);
                            }
                            return new CostEntry(null, null, str, formatted, cost);
                        }
                    } else if (curIcon.type() == IconType.TEXT) {
                        if (curIcon.icon() instanceof String text) {
                            return new CostEntry(null, null, text, formatted, cost);
                        }
                    }
                }
            }
            return new CostEntry(new ItemStack(Items.GOLD_NUGGET), null, null, formatted, cost);
        } else if (cost instanceof ItemTagCostComponent tagCost) {
            ItemStack stack = BuiltInRegistries.ITEM.getTag(tagCost.getTagKey())
                    .flatMap(tag -> tag.stream().findFirst().map(h -> h.value().getDefaultInstance()))
                    .orElse(Items.NAME_TAG.getDefaultInstance());
            return new CostEntry(stack, null, formatted, cost);
        }
        return new CostEntry(Items.EMERALD.getDefaultInstance(), null, formatted, cost);
    }

    public static double resolveDiscount(ShopOffer offer) {
        return offer.getComponent(DiscountComponent.class)
                .map(DiscountComponent::getDiscount)
                .orElse(0.0);
    }

    public static String formatAmount(double amount) {
        if (amount == (long) amount) {
            return String.valueOf((long) amount);
        }
        return String.format(Locale.ROOT, "%.2f", amount).replaceAll("\\.00$", "");
    }
}
