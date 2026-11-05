package dev.sixik.sdmshop2.libs.shop.client.ui.root;

import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.SDMShopClient;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopScreenController;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopCatalogItem;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopCatalogQuery;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopScreenElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUiContext;
import dev.sixik.sdmshop2.libs.shop.client.ui.modal.ShopPurchaseModal;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopTheme;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopContentButton;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopItemWidget;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.layout.AlignContent;
import dev.sixik.unigui.api.layout.GridTrack;
import dev.sixik.unigui.api.layout.Overflow;
import dev.sixik.unigui.api.render.SimpleTextureHandle;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.api.widget.visual.BackgroundKind;
import dev.sixik.unigui.widgets.containers.GridBox;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.PanelWidget;
import dev.sixik.unigui.widgets.containers.ScrollView;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.display.ImageView;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.interaction.Button;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Основная панель магазина: - Левая колонка: Категории (сверху) и Баланс игрока (снизу, 40% высоты)
 * - Центральная область: Каталог предложений магазина (сетка предложений)
 */
public class ShopRootPanel implements ShopScreenElement {
    /** Actions and overlays of the owning window; null uses the compatibility facade. */
    private final ShopUiContext context;

    /** Creates a standalone panel using the default window actions. */
    public ShopRootPanel() {
        this(null);
    }

    /**
     * Creates a panel for a specific window.
     *
     * @param context owning window, or null for legacy integration.
     */
    public ShopRootPanel(ShopUiContext context) {
        this.context = context;
    }

    /**
     * @return the owning window's actions.
     */
    private ShopUiContext context() {
        return context != null ? context : ShopScreenController.context();
    }

    public static final String CAT_ALL = "ALL";

    public record CategoryEntry(
            String id,
            String displayName,
            CatalogComponent.IconType iconType,
            @Nullable ItemStack iconItem,
            @Nullable ResourceLocation iconTexture) {
        public CategoryEntry(String id, String displayName) {
            this(id, displayName, CatalogComponent.IconType.NONE, null, null);
        }
    }

    /** Compatibility model; new integrations may use {@link ShopCatalogItem}. */
    public static class CatalogItemModel extends ShopCatalogItem {
        /**
         * Snapshots offer presentation data.
         *
         * @param offer source offer.
         */
        public CatalogItemModel(ShopOffer offer) {
            super(offer);
        }

        /**
         * Creates an offer preview without a backing server offer.
         *
         * @param uuid preview identifier.
         * @param categoryId category filter identifier.
         * @param title display title.
         * @param rewards reward previews.
         * @param costGroups alternative payment groups.
         * @param discountPercent displayed discount percentage.
         */
        public CatalogItemModel(
                UUID uuid,
                String categoryId,
                String title,
                List<ItemStack> rewards,
                List<ShopCatalogElement.CostGroup> costGroups,
                double discountPercent) {
            super(uuid, categoryId, title, rewards, costGroups, discountPercent);
        }
    }

    private String selectedCategoryId = CAT_ALL;
    private String currentSearchQuery = "";
    private boolean favoritesFirst = false;
    private VBox categoriesPanel;
    private final Map<String, Button> categoryButtons = new LinkedHashMap<>();
    private ShopCurrencyPanel moneyPanel;
    private ScrollView catalogScroll;
    private GridBox itemsGrid;
    private final List<ShopCatalogElement> currentCards = new ArrayList<>();

    @Override
    public Widget initWidget(PanelWidget root) {
        currentCards.clear();
        categoryButtons.clear();
        HBox mainContainer = new HBox();
        mainContainer.layout(style -> style.expand().padding(0.0f).gap(6.0f));
        // --- 1. Левая колонка (Категории + Баланс) ---
        VBox leftSidebar = new VBox();
        leftSidebar.layout(style -> style.width(140.0f).flexNone().gap(6.0f));
        VBox categoriesCard = new VBox();
        categoriesCard.layout(style -> style.expand());
        categoriesCard.themeEnabled(false);
        categoriesCard.boxVisualEnabled(true);
        categoriesCard.backgroundVisible(true);
        categoriesCard.backgroundKind(BackgroundKind.COLOR);
        categoriesCard.background(ShopTheme.color(ShopTheme.BG_SURFACE));
        categoriesCard.borderVisible(true);
        categoriesCard.border(ShopTheme.color(ShopTheme.BORDER_SUBTLE));
        categoriesCard.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        categoriesCard.radius(ShopTheme.RADIUS_CARD);
        categoriesPanel = new VBox();
        categoriesPanel.layout(style -> style.padding(4.0f).gap(4.0f));
        buildCategoriesList();
        ScrollView categoriesPanelScroll =
                new ScrollView(categoriesPanel).scrollStep(18.0f).scrollbarGap(2.0f);
        categoriesPanelScroll.layout(
                layout -> layout.fill().overflowX(Overflow.HIDDEN).overflowY(Overflow.AUTO));
        categoriesPanelScroll.overlayScrollBars(false);
        categoriesCard.addChild(categoriesPanelScroll);
        VBox moneyCard = new VBox();
        moneyCard.layout(style -> style.heightPercent(40.0f));
        moneyCard.themeEnabled(false);
        moneyCard.boxVisualEnabled(true);
        moneyCard.backgroundVisible(true);
        moneyCard.backgroundKind(BackgroundKind.COLOR);
        moneyCard.background(ShopTheme.color(ShopTheme.BG_SURFACE));
        moneyCard.borderVisible(true);
        moneyCard.border(ShopTheme.color(ShopTheme.BORDER_SUBTLE));
        moneyCard.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        moneyCard.radius(ShopTheme.RADIUS_CARD);
        moneyPanel = new ShopCurrencyPanel();
        moneyPanel.layout(style -> style.padding(8.0f, 6.0f).gap(4.0f));
        refreshCurrencies();
        ScrollView moneyPanelScroll =
                new ScrollView(moneyPanel).scrollStep(18.0f).scrollbarGap(2.0f);
        moneyPanelScroll.layout(
                layout -> layout.fill().overflowX(Overflow.HIDDEN).overflowY(Overflow.AUTO));
        moneyPanelScroll.overlayScrollBars(false);
        moneyCard.addChild(moneyPanelScroll);
        leftSidebar.addChild(categoriesCard);
        leftSidebar.addChild(moneyCard);
        // --- 2. Центральная панель (Каталог товаров) ---
        VBox catalogPanel = new VBox();
        catalogPanel.layout(style -> style.expand().padding(6.0f).gap(6.0f));
        catalogPanel.themeEnabled(false);
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
        itemsGrid.layout(
                style ->
                        style.gridTemplateColumns(
                                        GridTrack.fr(1.0f), GridTrack.fr(1.0f), GridTrack.fr(1.0f))
                                .alignContent(AlignContent.START)
                                .gap(8.0f)
                                .widthPercent(100.0f));
        catalogScroll = new ScrollView(itemsGrid).scrollStep(24.0f).scrollbarGap(2.0f);
        catalogScroll.layout(
                layout -> layout.fill().overflowX(Overflow.HIDDEN).overflowY(Overflow.AUTO));
        catalogScroll.overlayScrollBars(false);
        catalogPanel.addChild(catalogScroll);
        mainContainer.addChild(leftSidebar);
        mainContainer.addChild(catalogPanel);
        refreshOffers();
        root.addChild(mainContainer);
        return mainContainer;
    }

    /** Построение кнопок категорий в левой боковой панели. */
    /** Category row with decorative content and one pointer target. */
    public static class CategoryButton extends ShopContentButton {
        /** Label whose color follows selection state. */
        private final Label label;

        /**
         * Creates a category button.
         *
         * @param content decorative icon and text row.
         * @param label label used for selection styling.
         */
        public CategoryButton(Widget content, Label label) {
            super(content);
            this.label = label;
        }

        /**
         * @return category label.
         */
        public Label getLabel() {
            return label;
        }
    }

    public void buildCategoriesList() {
        if (categoriesPanel == null) return;
        categoriesPanel.clearChildren();
        categoryButtons.clear();
        List<CategoryEntry> categories = new ArrayList<>();
        categories.add(new CategoryEntry(CAT_ALL, "All"));
        Set<String> seenIds = new HashSet<>();
        seenIds.add(CAT_ALL.toLowerCase(Locale.ROOT));
        ShopInstance shop = SDMShopClient.Shop;
        if (shop != null && shop.getCategories() != null) {
            List<CatalogComponent> comps = shop.getCategories().getCatalogsComponents();
            if (comps != null && !comps.isEmpty()) {
                for (CatalogComponent comp : comps) {
                    if (comp.getId() != null && !comp.getId().isBlank()) {
                        String idKey = comp.getId().toLowerCase(Locale.ROOT);
                        if (seenIds.add(idKey)) {
                            String name =
                                    I18n.exists(comp.getId())
                                            ? Component.translatable(comp.getId()).getString()
                                            : comp.getId();
                            CatalogComponent.IconType iconType =
                                    comp.getIconType() != null
                                            ? comp.getIconType()
                                            : CatalogComponent.IconType.NONE;
                            ItemStack iconItem = comp.getIconItem();
                            ResourceLocation iconTex = comp.getIconTexture();
                            categories.add(
                                    new CategoryEntry(
                                            comp.getId(), name, iconType, iconItem, iconTex));
                        }
                    }
                }
            }
        }
        if (!seenIds.contains(this.selectedCategoryId.toLowerCase(Locale.ROOT))) {
            this.selectedCategoryId = CAT_ALL;
        }
        for (CategoryEntry cat : categories) {
            Button btn = createCategoryBtn(cat);
            categoryButtons.put(cat.id(), btn);
            categoriesPanel.addChild(btn);
        }
    }

    private Button createCategoryBtn(CategoryEntry entry) {
        HBox row = new HBox();
        row.layout(style -> style.alignItems(Align.CENTER).gap(6.0f));
        if (entry.iconType() == CatalogComponent.IconType.ITEM
                && entry.iconItem() != null
                && !entry.iconItem().isEmpty()) {
            ShopItemWidget itemWidget = new ShopItemWidget(entry.iconItem(), 14.0f);
            itemWidget.decorations(false);
            itemWidget.layout(s -> s.fixed(14.0f, 14.0f).flexNone());
            row.addChild(itemWidget);
        } else if (entry.iconType() == CatalogComponent.IconType.TEXTURE
                && entry.iconTexture() != null
                && !CatalogComponent.isEmptyIconTexture(entry.iconTexture())) {
            String tex = entry.iconTexture().toString();
            ImageView img = new ImageView(new SimpleTextureHandle(tex, 14, 14, tex));
            img.layout(s -> s.fixed(14.0f, 14.0f).flexNone());
            row.addChild(img);
        }
        Label label = new Label(entry.displayName());
        label.layout(s -> s.alignSelf(Align.CENTER));
        row.addChild(label);
        row.applyQueuedMutations();
        CategoryButton btn = new CategoryButton(row, label);
        btn.themeEnabled(false);
        btn.boxVisualEnabled(true);
        btn.backgroundVisible(true);
        btn.borderVisible(true);
        btn.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        btn.radius(ShopTheme.RADIUS_BUTTON);
        btn.textPadding(6.0f, 2.0f);
        btn.layout(style -> style.height(24.0f).flexNone());
        updateCategoryButtonStyle(btn, entry.id().equalsIgnoreCase(selectedCategoryId));
        btn.onClick(event -> selectCategory(entry.id()));
        return btn;
    }

    public void selectCategory(String categoryId) {
        if (categoryId == null || categoryId.isBlank()) categoryId = CAT_ALL;
        this.selectedCategoryId = categoryId;
        for (Map.Entry<String, Button> e : categoryButtons.entrySet()) {
            updateCategoryButtonStyle(
                    e.getValue(), e.getKey().equalsIgnoreCase(this.selectedCategoryId));
        }
        refreshOffers();
    }

    public void resetViewState() {
        this.selectedCategoryId = CAT_ALL;
        this.currentSearchQuery = "";
        this.favoritesFirst = false;
        refreshOffers();
    }

    private void updateCategoryButtonStyle(Button btn, boolean isSelected) {
        btn.hoverBackground(
                ShopTheme.color(isSelected ? ShopTheme.GOLD_MUTED : ShopTheme.BG_CARD_HOVER));
        btn.pressedBackground(ShopTheme.color(ShopTheme.GOLD_MUTED));
        if (isSelected) {
            btn.background(ShopTheme.color(ShopTheme.BG_CARD_HOVER));
            btn.border(ShopTheme.color(ShopTheme.GOLD_PRIMARY));
            btn.textColor(ShopTheme.color(ShopTheme.TEXT_GOLD));
            if (btn instanceof CategoryButton catBtn && catBtn.getLabel() != null) {
                catBtn.getLabel().color(ShopTheme.color(ShopTheme.TEXT_GOLD));
            }
        } else {
            btn.background(ShopTheme.color(ShopTheme.BG_CARD));
            btn.hoverBackground(ShopTheme.color(ShopTheme.BG_CARD_HOVER));
            btn.border(ShopTheme.color(ShopTheme.BORDER_SUBTLE));
            btn.textColor(ShopTheme.color(ShopTheme.TEXT_PRIMARY));
            if (btn instanceof CategoryButton catBtn && catBtn.getLabel() != null) {
                catBtn.getLabel().color(ShopTheme.color(ShopTheme.TEXT_PRIMARY));
            }
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

    /** Refreshes the reusable currency balance view when mounted. */
    public void refreshCurrencies() {
        if (moneyPanel == null) return;
        moneyPanel.refreshCurrencies();
        for (ShopCatalogElement card : currentCards) card.updateBuyButtonState();
    }

    public void refreshOffers() {
        if (itemsGrid == null) return;
        OverlayLayer overlay = context().overlay();
        for (ShopCatalogElement card : currentCards) {
            card.detachTooltips(overlay);
        }
        currentCards.clear();
        itemsGrid.clearChildren();
        List<CatalogItemModel> allItems = getAllItems();
        List<CatalogItemModel> filtered =
                ShopCatalogQuery.select(
                        allItems, selectedCategoryId, currentSearchQuery, favoritesFirst);
        if (filtered.isEmpty()) {
            VBox emptyBox = new VBox();
            emptyBox.layout(
                    s ->
                            s.gridColumn(1, 3)
                                    .alignSelf(Align.CENTER)
                                    .alignItems(Align.CENTER)
                                    .padding(20.0f));
            Label emptyLbl = new Label("No offers found");
            emptyLbl.color(ShopTheme.color(ShopTheme.TEXT_MUTED));
            emptyBox.addChild(emptyLbl);
            itemsGrid.addChild(emptyBox);
        } else {
            for (CatalogItemModel item : filtered) {
                ShopCatalogElement card;
                if (item.offer != null) {
                    card =
                            new ShopCatalogElement(
                                    item.offer,
                                    c -> {
                                        context().openModal(new ShopPurchaseModal(c, context()));
                                    });
                } else {
                    card =
                            new ShopCatalogElement(
                                    item.uuid,
                                    item.title,
                                    item.rewards,
                                    item.costGroups,
                                    item.discountPercent,
                                    item.isFavorite(),
                                    c -> {
                                        context().openModal(new ShopPurchaseModal(c, context()));
                                    });
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
        return Collections.emptyList();
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

    public List<ShopCatalogElement> getCurrentCards() {
        return Collections.unmodifiableList(currentCards);
    }

    public Map<String, Button> getCategoryButtons() {
        return Collections.unmodifiableMap(categoryButtons);
    }

    /** Releases all card tooltips before the window overlay is discarded. */
    @Override
    public void dispose() {
        for (ShopCatalogElement card : currentCards) card.detachTooltips(context().overlay());
        currentCards.clear();
        categoryButtons.clear();
        categoriesPanel = null;
        moneyPanel = null;
        catalogScroll = null;
        itemsGrid = null;
    }

    public int priority() {
        return 0;
    }
}
