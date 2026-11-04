package dev.sixik.sdmshop2.libs.shop.client.ui.root;

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
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.ShopEditorState;
import dev.sixik.unigui.api.event.PointerPressedEvent;
import dev.sixik.unigui.api.input.PointerButton;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.layout.Alignment;
import dev.sixik.unigui.api.layout.Justify;
import dev.sixik.unigui.widgets.containers.Box;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.LinearBox;
import dev.sixik.unigui.widgets.core.Orientation;
import dev.sixik.unigui.widgets.display.ImageView;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.display.Separator;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.interaction.Button;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
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
            @Nullable String textIcon,
            String amount,
            @Nullable CostComponent component
    ) {
        public CostEntry(ItemStack itemIcon, String amount) {
            this(itemIcon, null, amount, null);
        }

        public CostEntry(String textIcon, String amount) {
            this(null, textIcon, amount, null);
        }

        public CostEntry(ItemStack itemIcon, @Nullable String textIcon, String amount) {
            this(itemIcon, textIcon, amount, null);
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
    private Button favoriteButton;
    private ImageView favoriteIcon;
    private @Nullable Consumer<ShopCatalogElement> onOpenPurchase;
    private final List<ShopItemWidget> itemSlots = new ArrayList<>();

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
            if (ShopEditorState.get().isEditorMode()) {
                if (offer != null) {
                    ShopEditorState.get().selectTarget(offer);
                    event.cancel();
                }
            }
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

        HBox pricesBox = new HBox();
        pricesBox.layout(style -> style
                .alignItems(Align.CENTER)
                .gap(8.0f)
                .flexGrow(1.0f)
        );

        if (!costGroups.isEmpty()) {
            CostGroup primaryGroup = costGroups.get(0);
            for (CostEntry cost : primaryGroup.costs()) {
                HBox costRow = new HBox();
                costRow.layout(style -> style.alignItems(Align.CENTER).gap(3.0f));

                if (cost.itemIcon() != null && !cost.itemIcon().isEmpty()) {
                    ShopItemWidget costIcon = new ShopItemWidget(cost.itemIcon(), 14.0f);
                    costIcon.decorations(false);
                    itemSlots.add(costIcon);
                    costIcon.attachTooltip(ShopScreenController.getActiveOverlay());
                    costIcon.layout(style -> style.fixed(14.0f, 14.0f).flexNone());
                    costRow.addChild(costIcon);
                } else if (cost.textIcon() != null && !cost.textIcon().isBlank()) {
                    Label textIcon = new Label(cost.textIcon());
                    textIcon.color(ShopTheme.color(ShopTheme.GOLD_PRIMARY));
                    costRow.addChild(textIcon);
                } else {
                    ShopItemWidget coinIcon = new ShopItemWidget(new ItemStack(Items.GOLD_NUGGET), 14.0f);
                    coinIcon.decorations(false);
                    itemSlots.add(coinIcon);
                    coinIcon.attachTooltip(ShopScreenController.getActiveOverlay());
                    coinIcon.layout(style -> style.fixed(14.0f, 14.0f).flexNone());
                    costRow.addChild(coinIcon);
                }

                Label amountLabel = new Label(cost.amount());
                amountLabel.color(ShopTheme.color(ShopTheme.TEXT_PRIMARY));
                costRow.addChild(amountLabel);

                pricesBox.addChild(costRow);
            }

            if (costGroups.size() > 1) {
                Label altLabel = new Label("...");
                altLabel.color(ShopTheme.color(ShopTheme.TEXT_MUTED));
                pricesBox.addChild(altLabel);
            }
        }
        footer.addChild(pricesBox);

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
            ICurrency currency = SDMEconomyServiceClient.getCurrency(moneyCost.getMoneyId());
            if (currency != null) {
                CurrencyIcon curIcon = currency.getIcon();
                if (curIcon != null) {
                    if (curIcon.type() == IconType.ITEM) {
                        if (curIcon.icon() instanceof ItemStack s) return new CostEntry(s, null, formatted, cost);
                        if (curIcon.icon() instanceof Item item) return new CostEntry(item.getDefaultInstance(), null, formatted, cost);
                    } else if (curIcon.type() == IconType.TEXTURE && curIcon.icon() instanceof String text) {
                        if (text.equals("◎") || text.isBlank()) {
                            return new CostEntry(new ItemStack(Items.GOLD_NUGGET), null, formatted, cost);
                        }
                        return new CostEntry(null, text, formatted, cost);
                    }
                }
            }
            return new CostEntry(new ItemStack(Items.GOLD_NUGGET), null, formatted, cost);
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
