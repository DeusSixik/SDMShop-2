package dev.sixik.sdmshop2.libs.shop.client.ui.root;

import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;

import dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft;
import dev.sixik.sdmshop2.libs.sdmeconomy.custom_currency.ExternalItemCurrency;

import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.IconType;

import dev.sixik.sdmshop2.libs.shop.client.ui.editor.ShopEditorState;
import dev.sixik.unigui.api.render.SimpleTextureHandle;

import dev.sixik.unigui.widgets.containers.Box;
import dev.sixik.unigui.widgets.display.ImageView;

import net.minecraft.resources.ResourceLocation;
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
import dev.sixik.unigui.api.layout.LayoutContext;
import dev.sixik.unigui.api.math.MutableRect;
import dev.sixik.unigui.api.math.RectView;
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

    public record CategoryEntry(
            String id,
            String displayName,
            CatalogComponent.IconType iconType,
            @Nullable ItemStack iconItem,
            @Nullable ResourceLocation iconTexture
    ) {
        public CategoryEntry(String id, String displayName) {
            this(id, displayName, CatalogComponent.IconType.NONE, null, null);
        }
    }

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

    public static class CategoryButton extends Button {
        private final Widget content;
        private final Label label;

        public CategoryButton(Widget content, Label label) {
            super("");
            this.content = content;
            this.label = label;
            if (content != null) {
                addChild(content);
            }
        }

        public Label getLabel() {
            return label;
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
                            String name = I18n.exists(comp.getId()) ? Component.translatable(comp.getId()).getString() : comp.getId();
                            CatalogComponent.IconType iconType = comp.getIconType() != null ? comp.getIconType() : CatalogComponent.IconType.NONE;
                            ItemStack iconItem = comp.getIconItem();
                            ResourceLocation iconTex = comp.getIconTexture();

                            categories.add(new CategoryEntry(comp.getId(), name, iconType, iconItem, iconTex));
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
        row.layout(style -> style
                .alignItems(Align.CENTER)
                .gap(6.0f)
        );

        if (entry.iconType() == CatalogComponent.IconType.ITEM && entry.iconItem() != null && !entry.iconItem().isEmpty()) {
            ShopItemWidget itemWidget = new ShopItemWidget(entry.iconItem(), 14.0f);
            itemWidget.decorations(false);
            itemWidget.layout(s -> s.fixed(14.0f, 14.0f).flexNone());
            row.addChild(itemWidget);
        } else if (entry.iconType() == CatalogComponent.IconType.TEXTURE && entry.iconTexture() != null
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
        btn.boxVisualEnabled(true);
        btn.backgroundVisible(true);
        btn.borderVisible(true);
        btn.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        btn.radius(ShopTheme.RADIUS_BUTTON);
        btn.textPadding(6.0f, 2.0f);
        btn.layout(style -> style
                .height(24.0f)
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

    public void resetViewState() {
        this.selectedCategoryId = CAT_ALL;
        this.currentSearchQuery = "";
        this.favoritesFirst = false;
        this.currentCards.clear();
    }

    private void updateCategoryButtonStyle(Button btn, boolean isSelected) {
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

    public void refreshCurrencies() {
        if (moneyPanel == null) return;

        moneyPanel.clearChildren();

        Label yourMoneyLabel = new Label(Component.translatable("shop.ui.tabs_panel.your_money").getString());

        yourMoneyLabel.color(ShopTheme.color(ShopTheme.GOLD_PRIMARY));

        moneyPanel.addChild(yourMoneyLabel);

        Minecraft mc = Minecraft.getInstance();
        Player player = mc != null ? mc.player : null;
        if (player == null) return;

        boolean isEditor = ShopEditorState.get().isEditorMode();

        Set<ResourceLocation> processedIds = new HashSet<>();

        if (isEditor) {
            for (CurrencyDraft draft : ShopEditorState.get().getCurrencyDrafts()) {
                if (draft.kind() == CurrencyDraft.Kind.DELETE) continue;

                processedIds.add(draft.id());

                ICurrency liveCur = SDMEconomyServiceClient.getCurrency(draft.id());

                addCurrencyRow(player, draft.id(), draft.displayName(), draft, liveCur);

            }

        }

        for (ICurrency currency : SDMEconomyServiceClient.getAllCurrencies().values()) {
            if (currency == null) continue;

            ResourceLocation id = currency.getId();

            if (isEditor) {
                if (processedIds.contains(id)) continue;

                if (ShopEditorState.get().isCurrencyDeleted(id)) continue;

            }

            addCurrencyRow(player, id, currency.getDisplayName().getString(), null, currency);

        }

        for (ShopCatalogElement card : currentCards) {
            card.updateBuyButtonState();

        }

    }

    private void addCurrencyRow(

            Player player,

            ResourceLocation currencyId,

            String displayName,

            @Nullable CurrencyDraft draft,

            @Nullable ICurrency currency

    ) {
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

        Widget iconWidget = buildCurrencyIconWidget(draft, currency);

        if (iconWidget != null) {
            leftPart.addChild(iconWidget);

        }

        Label nameLabel = new Label(displayName);

        nameLabel.color(ShopTheme.color(ShopTheme.TEXT_SECONDARY));

        leftPart.addChild(nameLabel);

        java.math.BigDecimal balance = java.math.BigDecimal.ZERO;

        if (currency != null) {
            java.math.BigDecimal b = SDMEconomyServiceClient.getBalance(currency, player);

            if (b != null) balance = b;

        }

        String balanceStr = balance.stripTrailingZeros().toPlainString();

        Label balanceLabel = new Label(balanceStr);

        balanceLabel.color(ShopTheme.color(ShopTheme.TEXT_PRIMARY));

        row.addChild(leftPart);

        row.addChild(balanceLabel);

        moneyPanel.addChild(row);

    }

    private Widget buildCurrencyIconWidget(@Nullable CurrencyDraft draft, @Nullable ICurrency currency) {
        // 1. Check draft if present

        if (draft != null) {
            if (draft.kind() == CurrencyDraft.Kind.ITEM && draft.itemStack() != null && !draft.itemStack().isEmpty()) {
                ShopItemWidget itemWidget = new ShopItemWidget(draft.itemStack(), 14.0f);

                itemWidget.decorations(false);

                itemWidget.layout(s -> s.fixed(14.0f, 14.0f).flexNone());

                return itemWidget;

            } else if (draft.kind() == CurrencyDraft.Kind.TEXTURE && draft.iconTexture() != null) {
                String tex = draft.iconTexture().toString();

                ImageView img = new ImageView(new SimpleTextureHandle(tex, 14, 14, tex));

                img.layout(s -> s.size(14.0f, 14.0f).flexNone());

                return img;

            } else if (draft.kind() == CurrencyDraft.Kind.TEXT) {
                String sym = draft.iconText();

                if (sym == null || sym.isBlank()) sym = "$";

                return createTextIconBadge(sym);

            }

        }

        // 2. Check external item currency

        if (currency instanceof ExternalItemCurrency eic && !eic.getItemType().isEmpty()) {
            ShopItemWidget itemWidget = new ShopItemWidget(eic.getItemType(), 14.0f);

            itemWidget.decorations(false);

            itemWidget.layout(s -> s.fixed(14.0f, 14.0f).flexNone());

            return itemWidget;

        }

        // 3. Check registered currency icon

        if (currency != null) {
            CurrencyIcon curIcon = currency.getIcon();

            if (curIcon != null) {
                if (curIcon.type() == IconType.ITEM) {
                    ItemStack iconStack = curIcon.icon() instanceof ItemStack s ? s :

                            (curIcon.icon() instanceof Item it ? it.getDefaultInstance() : null);

                    if (iconStack != null && !iconStack.isEmpty()) {
                        ShopItemWidget itemWidget = new ShopItemWidget(iconStack, 14.0f);

                        itemWidget.decorations(false);

                        itemWidget.layout(s -> s.fixed(14.0f, 14.0f).flexNone());

                        return itemWidget;

                    }

                } else if (curIcon.type() == IconType.TEXTURE) {
                    if (curIcon.icon() instanceof ResourceLocation loc) {
                        String tex = loc.toString();

                        ImageView img = new ImageView(new SimpleTextureHandle(tex, 14, 14, tex));

                        img.layout(s -> s.size(14.0f, 14.0f).flexNone());

                        return img;

                    } else if (curIcon.icon() instanceof String str) {
                        if (str.contains(":") || str.contains("/") || str.endsWith(".png")) {
                            ImageView img = new ImageView(new SimpleTextureHandle(str, 14, 14, str));

                            img.layout(s -> s.size(14.0f, 14.0f).flexNone());

                            return img;

                        } else {
                            return createTextIconBadge(str);

                        }

                    }

                } else if (curIcon.type() == IconType.TEXT) {
                    String sym = curIcon.icon() instanceof String str ? str : "$";

                    return createTextIconBadge(sym);

                }

            }

            // Fallback: extract symbol from currency formatting

            String sym = "$";

            try {
                sym = currency.format(java.math.BigDecimal.ONE).replaceAll("[0-9.,\\s]", "");

                if (sym.isBlank()) sym = "$";

            } catch (Exception ignored) {}

            return createTextIconBadge(sym);

        }

        // 4. Default fallback: Gold Nugget

        ShopItemWidget itemWidget = new ShopItemWidget(new ItemStack(Items.GOLD_NUGGET), 14.0f);

        itemWidget.decorations(false);

        itemWidget.layout(s -> s.fixed(14.0f, 14.0f).flexNone());

        return itemWidget;

    }

    private Box createTextIconBadge(String symbol) {
        Box badge = new Box();

        badge.layout(s -> s

                .size(14.0f, 14.0f)

                .alignItems(Align.CENTER)

                .justifyContent(Justify.CENTER)

                .flexNone()

        );

        Label iconLbl = new Label(symbol);

        iconLbl.color(ShopTheme.color(ShopTheme.GOLD_PRIMARY));

        badge.addChild(iconLbl);

        return badge;

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

    @Override
    public int priority() {
        return 0;
    }
}
