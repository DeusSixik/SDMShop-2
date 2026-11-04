package dev.sixik.sdmshop2.libs.shop.client.ui.root;

import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.IconType;
import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.SDMShopClient;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopScreenController;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopScreenElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.modal.ShopPurchaseModal;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopTheme;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopItemWidget;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.layout.AlignContent;
import dev.sixik.unigui.api.layout.GridTrack;
import dev.sixik.unigui.api.layout.Justify;
import dev.sixik.unigui.api.layout.Overflow;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.api.widget.visual.BackgroundKind;
import dev.sixik.unigui.widgets.containers.GridBox;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.PanelWidget;
import dev.sixik.unigui.widgets.containers.ScrollView;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.interaction.Button;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Основная панель магазина:
 * - Левая колонка: Категории (сверху) и Баланс игрока (снизу, 40% высоты)
 * - Центральная область: Каталог предложений магазина (сетка предложений)
 */
public class ShopRootPanel implements ShopScreenElement {

    public static final String CAT_ALL = "ALL";
    public static final String CAT_RESOURCES = "RESOURCES";
    public static final String CAT_BUILDING = "BUILDING_BLOCKS";
    public static final String CAT_FOOD = "FOOD";

    public record CategoryEntry(String id, String displayName) {}

    public static class CatalogItemModel {
        public final @Nullable ShopOffer offer;
        public final @Nullable UUID uuid;
        public final String categoryId;
        public final String title;
        public final List<ItemStack> rewards;
        public final List<ShopCatalogElement.CostGroup> costGroups;
        public final double discountPercent;

        public CatalogItemModel(ShopOffer offer) {
            this.offer = offer;
            this.uuid = offer.getUUID();
            CatalogComponent cat = offer.getComponent(CatalogComponent.class).orElse(null);
            this.categoryId = cat != null && cat.getId() != null ? cat.getId() : CAT_ALL;
            this.title = ShopCatalogElement.resolveTitle(offer);
            this.rewards = ShopCatalogElement.resolveRewardItems(offer);
            this.costGroups = ShopCatalogElement.resolveCostGroups(offer);
            this.discountPercent = ShopCatalogElement.resolveDiscount(offer);
        }

        public CatalogItemModel(
                UUID uuid,
                String categoryId,
                String title,
                List<ItemStack> rewards,
                List<ShopCatalogElement.CostGroup> costGroups,
                double discountPercent
        ) {
            this.offer = null;
            this.uuid = uuid;
            this.categoryId = categoryId;
            this.title = title;
            this.rewards = rewards;
            this.costGroups = costGroups;
            this.discountPercent = discountPercent;
        }

        public boolean isFavorite() {
            return uuid != null && ShopClientCache.isFavorite(uuid);
        }
    }

    private String selectedCategoryId = CAT_ALL;
    private String currentSearchQuery = "";
    private boolean favoritesFirst = false;

    private VBox categoriesPanel;
    private final Map<String, Button> categoryButtons = new LinkedHashMap<>();
    private VBox moneyPanel;

    private ScrollView catalogScroll;
    private GridBox itemsGrid;

    private final List<ShopCatalogElement> currentCards = new ArrayList<>();
    private final List<CatalogItemModel> mockOffers = new ArrayList<>();

    @Override
    public Widget initWidget(PanelWidget root) {
        currentCards.clear();
        categoryButtons.clear();

        HBox mainContainer = new HBox();
        mainContainer.layout(style -> style
                .expand()
                .padding(0.0f)
                .gap(6.0f)
        );

        // --- 1. Левая колонка (Категории + Баланс) ---
        VBox leftSidebar = new VBox();
        leftSidebar.layout(style -> style
                .width(140.0f)
                .flexNone()
                .gap(6.0f)
        );

        VBox categoriesCard = new VBox();
        categoriesCard.layout(style -> style.expand());
        categoriesCard.boxVisualEnabled(true);
        categoriesCard.backgroundVisible(true);
        categoriesCard.backgroundKind(BackgroundKind.COLOR);
        categoriesCard.background(ShopTheme.color(ShopTheme.BG_SURFACE));
        categoriesCard.borderVisible(true);
        categoriesCard.border(ShopTheme.color(ShopTheme.BORDER_SUBTLE));
        categoriesCard.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        categoriesCard.radius(ShopTheme.RADIUS_CARD);

        categoriesPanel = new VBox();
        categoriesPanel.layout(style -> style
                .padding(4.0f)
                .gap(4.0f)
        );

        buildCategoriesList();

        ScrollView categoriesPanelScroll = new ScrollView(categoriesPanel)
                .scrollStep(18.0f)
                .scrollbarGap(2.0f);
        categoriesPanelScroll.layout(layout -> layout
                .fill()
                .overflowX(Overflow.HIDDEN)
                .overflowY(Overflow.AUTO)
        );
        categoriesPanelScroll.overlayScrollBars(false);
        categoriesCard.addChild(categoriesPanelScroll);

        VBox moneyCard = new VBox();
        moneyCard.layout(style -> style.heightPercent(40.0f));
        moneyCard.boxVisualEnabled(true);
        moneyCard.backgroundVisible(true);
        moneyCard.backgroundKind(BackgroundKind.COLOR);
        moneyCard.background(ShopTheme.color(ShopTheme.BG_SURFACE));
        moneyCard.borderVisible(true);
        moneyCard.border(ShopTheme.color(ShopTheme.BORDER_SUBTLE));
        moneyCard.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        moneyCard.radius(ShopTheme.RADIUS_CARD);

        moneyPanel = new VBox();
        moneyPanel.layout(style -> style
                .padding(8.0f, 6.0f)
                .gap(4.0f)
        );

        refreshCurrencies();

        ScrollView moneyPanelScroll = new ScrollView(moneyPanel)
                .scrollStep(18.0f)
                .scrollbarGap(2.0f);
        moneyPanelScroll.layout(layout -> layout
                .fill()
                .overflowX(Overflow.HIDDEN)
                .overflowY(Overflow.AUTO)
        );
        moneyPanelScroll.overlayScrollBars(false);
        moneyCard.addChild(moneyPanelScroll);

        leftSidebar.addChild(categoriesCard);
        leftSidebar.addChild(moneyCard);

        // --- 2. Центральная панель (Каталог товаров) ---
        VBox catalogPanel = new VBox();
        catalogPanel.layout(style -> style
                .expand()
                .padding(6.0f)
                .gap(6.0f)
        );
        catalogPanel.boxVisualEnabled(true);
        catalogPanel.backgroundVisible(true);
        catalogPanel.backgroundKind(BackgroundKind.COLOR);
        catalogPanel.background(ShopTheme.color(ShopTheme.BG_SURFACE));
        catalogPanel.borderVisible(true);
        catalogPanel.border(ShopTheme.color(ShopTheme.BORDER_SUBTLE));
        catalogPanel.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        catalogPanel.radius(ShopTheme.RADIUS_CARD);

        itemsGrid = new GridBox();
        itemsGrid.columns(3);
        itemsGrid.horizontalSpacing(8.0f);
        itemsGrid.verticalSpacing(8.0f);
        itemsGrid.layout(style -> style
                .gridTemplateColumns(GridTrack.fr(1.0f), GridTrack.fr(1.0f), GridTrack.fr(1.0f))
                .alignContent(AlignContent.START)
                .gap(8.0f)
                .widthPercent(100.0f)
        );

        catalogScroll = new ScrollView(itemsGrid)
                .scrollStep(24.0f)
                .scrollbarGap(2.0f);
        catalogScroll.layout(layout -> layout
                .fill()
                .overflowX(Overflow.HIDDEN)
                .overflowY(Overflow.AUTO)
        );
        catalogScroll.overlayScrollBars(false);
        catalogPanel.addChild(catalogScroll);

        mainContainer.addChild(leftSidebar);
        mainContainer.addChild(catalogPanel);

        refreshOffers();

        root.addChild(mainContainer);
        return mainContainer;
    }

    /**
     * Построение кнопок категорий в левой боковой панели.
     */
    public void buildCategoriesList() {
        if (categoriesPanel == null) return;
        categoriesPanel.clearChildren();
        categoryButtons.clear();

        List<CategoryEntry> categories = new ArrayList<>();
        categories.add(new CategoryEntry(CAT_ALL, "All"));

        ShopInstance shop = SDMShopClient.Shop;

        if (shop != null && shop.getCategories() != null) {
            List<CatalogComponent> comps = shop.getCategories().getCatalogsComponents();
            if (comps != null && !comps.isEmpty()) {
                for (CatalogComponent comp : comps) {
                    if (comp.getId() != null && !comp.getId().isBlank()) {
                        String name = I18n.exists(comp.getId()) ? Component.translatable(comp.getId()).getString() : comp.getId();
                        categories.add(new CategoryEntry(comp.getId(), name));
                    }
                }
            }
        }

        if (categories.size() == 1) {
            categories.add(new CategoryEntry(CAT_RESOURCES, "Resources"));
            categories.add(new CategoryEntry(CAT_BUILDING, "Building Blocks"));
            categories.add(new CategoryEntry(CAT_FOOD, "Farming & Food"));
        }

        for (CategoryEntry cat : categories) {
            Button btn = createCategoryBtn(cat);
            categoryButtons.put(cat.id(), btn);
            categoriesPanel.addChild(btn);
        }
    }

    private Button createCategoryBtn(CategoryEntry entry) {
        Button btn = new Button(entry.displayName());
        btn.backgroundVisible(true);
        btn.borderVisible(true);
        btn.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        btn.radius(ShopTheme.RADIUS_BUTTON);
        btn.textPadding(6.0f, 2.0f);
        btn.layout(style -> style
                .height(22.0f)
                .flexNone()
        );
        updateCategoryButtonStyle(btn, entry.id().equalsIgnoreCase(selectedCategoryId));
        btn.onClick(event -> selectCategory(entry.id()));
        return btn;
    }

    public void selectCategory(String categoryId) {
        if (categoryId == null || categoryId.isBlank()) categoryId = CAT_ALL;
        this.selectedCategoryId = categoryId;
        for (Map.Entry<String, Button> e : categoryButtons.entrySet()) {
            updateCategoryButtonStyle(e.getValue(), e.getKey().equalsIgnoreCase(this.selectedCategoryId));
        }
        refreshOffers();
    }

    private void updateCategoryButtonStyle(Button btn, boolean isSelected) {
        if (isSelected) {
            btn.background(ShopTheme.color(ShopTheme.BG_CARD_HOVER));
            btn.border(ShopTheme.color(ShopTheme.GOLD_PRIMARY));
            btn.textColor(ShopTheme.color(ShopTheme.TEXT_GOLD));
        } else {
            btn.background(ShopTheme.color(ShopTheme.BG_CARD));
            btn.hoverBackground(ShopTheme.color(ShopTheme.BG_CARD_HOVER));
            btn.border(ShopTheme.color(ShopTheme.BORDER_SUBTLE));
            btn.textColor(ShopTheme.color(ShopTheme.TEXT_PRIMARY));
        }
    }

    public void setSearchQuery(String query) {
        this.currentSearchQuery = query != null ? query.trim().toLowerCase(Locale.ROOT) : "";
        refreshOffers();
    }

    public boolean toggleFavoritesFirst() {
        this.favoritesFirst = !this.favoritesFirst;
        refreshOffers();
        return this.favoritesFirst;
    }

    public boolean isFavoritesFirst() {
        return favoritesFirst;
    }

    public void onFavoritesChanged(UUID offerId, boolean favorite) {
        if (favoritesFirst) {
            refreshOffers();
        } else {
            for (ShopCatalogElement card : currentCards) {
                if (offerId != null && offerId.equals(card.getOfferUuid())) {
                    card.setFavorite(favorite);
                    break;
                }
            }
        }
    }

    public void refreshCurrencies() {
        if (moneyPanel == null) return;
        moneyPanel.clearChildren();

        Label yourMoneyLabel = new Label(Component.translatable("shop.ui.tabs_panel.your_money").getString());
        yourMoneyLabel.color(ShopTheme.color(ShopTheme.GOLD_PRIMARY));
        moneyPanel.addChild(yourMoneyLabel);

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        for (ICurrency currency : SDMEconomyServiceClient.getAllCurrencies().values()) {
            HBox row = new HBox();
            row.layout(style -> style
                    .alignItems(Align.CENTER)
                    .justifyContent(Justify.SPACE_BETWEEN)
                    .height(18.0f)
                    .widthPercent(100.0f)
                    .flexNone()
            );

            HBox leftPart = new HBox();
            leftPart.layout(style -> style
                    .alignItems(Align.CENTER)
                    .gap(4.0f)
                    .flexNone()
            );

            dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon curIcon = currency.getIcon();
            if (curIcon != null && curIcon.type() == IconType.ITEM) {
                ItemStack iconStack = curIcon.icon() instanceof ItemStack s ? s :
                        (curIcon.icon() instanceof Item it ? it.getDefaultInstance() : Items.GOLD_NUGGET.getDefaultInstance());
                ShopItemWidget itemWidget = new ShopItemWidget(iconStack, 14.0f);
                leftPart.addChild(itemWidget);
            } else if (curIcon != null && curIcon.type() == IconType.TEXTURE && curIcon.icon() instanceof String text) {
                Label iconLbl = new Label(text);
                iconLbl.color(ShopTheme.color(ShopTheme.GOLD_PRIMARY));
                leftPart.addChild(iconLbl);
            }

            Label nameLabel = new Label(currency.getDisplayName().getString());
            nameLabel.color(ShopTheme.color(ShopTheme.TEXT_SECONDARY));
            leftPart.addChild(nameLabel);

            java.math.BigDecimal balance = SDMEconomyServiceClient.getBalance(currency, player);
            String balanceStr = balance != null ? balance.stripTrailingZeros().toPlainString() : "0";
            Label balanceLabel = new Label(balanceStr);
            balanceLabel.color(ShopTheme.color(ShopTheme.TEXT_PRIMARY));

            row.addChild(leftPart);
            row.addChild(balanceLabel);
            moneyPanel.addChild(row);
        }
    }

    /**
     * Фильтрация и перерисовка карточек предложений в каталоге.
     */
    public void refreshOffers() {
        if (itemsGrid == null) return;

        OverlayLayer overlay = ShopScreenController.getActiveOverlay();
        for (ShopCatalogElement card : currentCards) {
            card.detachTooltips(overlay);
        }
        currentCards.clear();
        itemsGrid.clearChildren();

        List<CatalogItemModel> allItems = getAllItems();
        List<CatalogItemModel> filtered = new ArrayList<>();

        for (CatalogItemModel item : allItems) {
            if (!selectedCategoryId.equalsIgnoreCase(CAT_ALL)) {
                if (!selectedCategoryId.equalsIgnoreCase(item.categoryId)) {
                    continue;
                }
            }

            if (!currentSearchQuery.isEmpty()) {
                boolean matches = false;
                if (item.title != null && item.title.toLowerCase(Locale.ROOT).contains(currentSearchQuery)) {
                    matches = true;
                }
                if (!matches && item.rewards != null) {
                    for (ItemStack reward : item.rewards) {
                        if (reward.getHoverName().getString().toLowerCase(Locale.ROOT).contains(currentSearchQuery)) {
                            matches = true;
                            break;
                        }
                        String itemId = BuiltInRegistries.ITEM.getKey(reward.getItem()).toString().toLowerCase(Locale.ROOT);
                        if (itemId.contains(currentSearchQuery)) {
                            matches = true;
                            break;
                        }
                    }
                }
                if (!matches) {
                    continue;
                }
            }

            filtered.add(item);
        }

        if (favoritesFirst) {
            filtered.sort((a, b) -> {
                boolean aFav = a.isFavorite();
                boolean bFav = b.isFavorite();
                if (aFav != bFav) {
                    return aFav ? -1 : 1;
                }
                return 0;
            });
        }

        if (filtered.isEmpty()) {
            VBox emptyBox = new VBox();
            emptyBox.layout(s -> s
                    .gridColumn(1, 3)
                    .alignSelf(Align.CENTER)
                    .alignItems(Align.CENTER)
                    .padding(20.0f)
            );
            Label emptyLbl = new Label("No offers found");
            emptyLbl.color(ShopTheme.color(ShopTheme.TEXT_MUTED));
            emptyBox.addChild(emptyLbl);
            itemsGrid.addChild(emptyBox);
        } else {
            for (CatalogItemModel item : filtered) {
                ShopCatalogElement card;
                if (item.offer != null) {
                    card = new ShopCatalogElement(item.offer, c -> {
                        ShopScreenController.openModal(new ShopPurchaseModal(c));
                    });
                } else {
                    card = new ShopCatalogElement(
                            item.uuid,
                            item.title,
                            item.rewards,
                            item.costGroups,
                            item.discountPercent,
                            item.isFavorite(),
                            c -> {
                                ShopScreenController.openModal(new ShopPurchaseModal(c));
                            }
                    );
                }

                card.attachTooltips(overlay);
                currentCards.add(card);
                itemsGrid.addChild(card);
            }
        }
    }

    private List<CatalogItemModel> getAllItems() {
        ShopInstance shop = SDMShopClient.Shop;

        if (shop != null && shop.getEntries() != null) {
            Map<UUID, ShopOffer> entryMap = shop.getEntries().getEntryMap();
            if (entryMap != null && !entryMap.isEmpty()) {
                List<CatalogItemModel> list = new ArrayList<>();
                for (ShopOffer offer : entryMap.values()) {
                    list.add(new CatalogItemModel(offer));
                }
                return list;
            }
        }
        return getMockOffers();
    }

    private List<CatalogItemModel> getMockOffers() {
        if (mockOffers.isEmpty()) {
            mockOffers.addAll(createMockOffers());
        }
        return mockOffers;
    }

    private List<CatalogItemModel> createMockOffers() {
        List<CatalogItemModel> list = new ArrayList<>();

        list.add(new CatalogItemModel(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                CAT_RESOURCES,
                "Glowstone",
                List.of(new ItemStack(Items.GLOWSTONE, 8)),
                List.of(new ShopCatalogElement.CostGroup("", List.of(new ShopCatalogElement.CostEntry(new ItemStack(Items.EMERALD), "4")))),
                0.0
        ));

        list.add(new CatalogItemModel(
                UUID.fromString("00000000-0000-0000-0000-000000000002"),
                CAT_RESOURCES,
                "Gold Ingot",
                List.of(new ItemStack(Items.GOLD_INGOT, 1)),
                List.of(new ShopCatalogElement.CostGroup("", List.of(
                        new ShopCatalogElement.CostEntry(new ItemStack(Items.EMERALD), "4"),
                        new ShopCatalogElement.CostEntry(new ItemStack(Items.DIAMOND), "4")
                ))),
                0.0
        ));

        list.add(new CatalogItemModel(
                UUID.fromString("00000000-0000-0000-0000-000000000003"),
                CAT_RESOURCES,
                "Elytra",
                List.of(new ItemStack(Items.ELYTRA, 1)),
                List.of(new ShopCatalogElement.CostGroup("", List.of(
                        new ShopCatalogElement.CostEntry(new ItemStack(Items.DIAMOND), "64"),
                        new ShopCatalogElement.CostEntry(new ItemStack(Items.NETHERITE_INGOT), "4")
                ))),
                0.0
        ));

        list.add(new CatalogItemModel(
                UUID.fromString("00000000-0000-0000-0000-000000000004"),
                CAT_FOOD,
                "Enchanted Golden Apple",
                List.of(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 1)),
                List.of(new ShopCatalogElement.CostGroup("", List.of(new ShopCatalogElement.CostEntry(new ItemStack(Items.DIAMOND), "32")))),
                0.0
        ));

        list.add(new CatalogItemModel(
                UUID.fromString("00000000-0000-0000-0000-000000000005"),
                CAT_RESOURCES,
                "Netherite Ingot",
                List.of(new ItemStack(Items.NETHERITE_INGOT, 1)),
                List.of(new ShopCatalogElement.CostGroup("", List.of(new ShopCatalogElement.CostEntry(new ItemStack(Items.DIAMOND), "8")))),
                0.0
        ));

        list.add(new CatalogItemModel(
                UUID.fromString("00000000-0000-0000-0000-000000000006"),
                CAT_BUILDING,
                "Shulker Shell",
                List.of(new ItemStack(Items.SHULKER_SHELL, 2)),
                List.of(new ShopCatalogElement.CostGroup("", List.of(new ShopCatalogElement.CostEntry(new ItemStack(Items.DIAMOND), "4")))),
                0.0
        ));

        list.add(new CatalogItemModel(
                UUID.fromString("00000000-0000-0000-0000-000000000007"),
                CAT_RESOURCES,
                "Totem of Undying",
                List.of(new ItemStack(Items.TOTEM_OF_UNDYING, 1)),
                List.of(new ShopCatalogElement.CostGroup("", List.of(new ShopCatalogElement.CostEntry(new ItemStack(Items.DIAMOND), "12")))),
                0.0
        ));

        list.add(new CatalogItemModel(
                UUID.fromString("00000000-0000-0000-0000-000000000008"),
                CAT_RESOURCES,
                "Wither Skeleton Skull",
                List.of(new ItemStack(Items.WITHER_SKELETON_SKULL, 1)),
                List.of(new ShopCatalogElement.CostGroup("", List.of(new ShopCatalogElement.CostEntry(new ItemStack(Items.DIAMOND), "10")))),
                0.0
        ));

        return list;
    }

    public void refreshOffer(UUID offerId) {
        if (offerId == null) return;
        for (ShopCatalogElement card : currentCards) {
            if (offerId.equals(card.getOfferUuid())) {
                refreshOffers();
                break;
            }
        }
    }

    @Override
    public int priority() {
        return 0;
    }
}
