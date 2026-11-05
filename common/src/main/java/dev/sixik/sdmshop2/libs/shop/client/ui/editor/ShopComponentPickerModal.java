package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.IconType;
import dev.sixik.sdmshop2.libs.shop.SDMShopConstants;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopScreenController;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityIcons;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopEditorStyles;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopItemWidget;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponentRegistry;
import dev.sixik.unigui.api.event.EventPhase;
import dev.sixik.unigui.api.event.PointerEnteredEvent;
import dev.sixik.unigui.api.event.PointerExitedEvent;
import dev.sixik.unigui.api.event.PointerPressedEvent;
import dev.sixik.unigui.api.event.PointerReleasedEvent;
import dev.sixik.unigui.api.input.PointerButton;
import dev.sixik.unigui.api.layout.*;
import dev.sixik.unigui.api.math.ColorView;
import dev.sixik.unigui.api.math.MutableColor;
import dev.sixik.unigui.api.math.MutableRect;
import dev.sixik.unigui.api.math.RectView;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.api.widget.render.WidgetRender;
import dev.sixik.unigui.widgets.containers.*;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.feedback.OverlayHostAware;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.interaction.Button;
import dev.sixik.unigui.widgets.interaction.ComboBox;
import dev.sixik.unigui.widgets.interaction.TextField;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.*;

/**
 * Модальное окно выбора компонента (Select Component) в стиле Unity Dark. Реализует
 * OverlayHostAware для корректного центрирования на экране.
 */
public class ShopComponentPickerModal extends Box implements OverlayHostAware {

    private static final float MODAL_WIDTH = 540.0f;
    private static final float MAX_MODAL_HEIGHT = 420.0f;

    private final ShopOffer targetOffer;
    private final VBox windowCard = new VBox();

    private String searchQuery = "";
    private String selectedCategoryKey = "all";
    private boolean favoritesOnly = false;
    private boolean closing = false;

    private VBox listContainer;
    private Button favoritesBtn;

    private record CategoryFilter(String key, String displayName) {}

    private final List<CategoryFilter> categoryFilters = new ArrayList<>();

    public ShopComponentPickerModal(ShopOffer targetOffer) {
        this.targetOffer = targetOffer;

        // Полноэкранный фон-затемнение
        themeEnabled(false);
        boxVisualEnabled(true);
        backgroundVisible(true);
        background(new MutableColor(0.0f, 0.0f, 0.0f, 0.70f));

        layout(style -> style.position(PositionType.ABSOLUTE).inset(0.0f).fill());

        on(
                PointerPressedEvent.TYPE,
                event -> {
                    if (event.phase() == EventPhase.CAPTURE) return;
                    if (event.target() == this) {
                        closeSelf();
                    }
                    event.cancel();
                });

        on(
                PointerReleasedEvent.TYPE,
                event -> {
                    if (event.phase() == EventPhase.CAPTURE) return;
                    event.cancel();
                });

        initCategoryFilters();
        buildModalWindow();
    }

    private void initCategoryFilters() {
        categoryFilters.clear();
        categoryFilters.add(
                new CategoryFilter("all", translateOrFallback(SDMShopConstants.ALL_GROUP, "All")));
        categoryFilters.add(
                new CategoryFilter(
                        SDMShopConstants.CONDITION_GROUP,
                        translateOrFallback(SDMShopConstants.CONDITION_GROUP, "Conditions")));
        categoryFilters.add(
                new CategoryFilter(
                        SDMShopConstants.COST_GROUP,
                        translateOrFallback(SDMShopConstants.COST_GROUP, "Costs")));
        categoryFilters.add(
                new CategoryFilter(
                        SDMShopConstants.REWARD_GROUP,
                        translateOrFallback(SDMShopConstants.REWARD_GROUP, "Rewards")));
        categoryFilters.add(
                new CategoryFilter(
                        SDMShopConstants.PROMO_GROUP,
                        translateOrFallback(SDMShopConstants.PROMO_GROUP, "Promo")));
        categoryFilters.add(
                new CategoryFilter(
                        SDMShopConstants.PROMO_EFFECT_GROUP,
                        translateOrFallback(SDMShopConstants.PROMO_EFFECT_GROUP, "Promo Effects")));
        categoryFilters.add(
                new CategoryFilter(
                        SDMShopConstants.MISC_GROUP,
                        translateOrFallback(SDMShopConstants.MISC_GROUP, "Misc")));
    }

    private static String translateOrFallback(String key, String fallback) {
        if (key != null && I18n.exists(key)) {
            return Component.translatable(key).getString();
        }
        return fallback;
    }

    @Override
    public void measure(LayoutContext context) {
        super.measure(context);
        float modalH = Math.min(context.availableHeight() * 0.85f, MAX_MODAL_HEIGHT);
        windowCard.measure(new LayoutContext(MODAL_WIDTH, modalH));
    }

    @Override
    public void arrangeInHost(RectView hostBounds) {
        this.arrange(hostBounds);

        float hostW = hostBounds.width();
        float hostH = hostBounds.height();
        float modalW = Math.min(hostW * 0.92f, MODAL_WIDTH);
        float modalH = Math.min(hostH * 0.85f, MAX_MODAL_HEIGHT);
        float modalX = hostBounds.x() + (hostW - modalW) * 0.5f;
        float modalY = hostBounds.y() + (hostH - modalH) * 0.5f;

        windowCard.layout(
                style ->
                        style.fixed(modalW, modalH)
                                .padding(10.0f)
                                .overflow(Overflow.HIDDEN)
                                .alignItems(Align.CENTER)
                                .flexNone());
        windowCard.arrange(new MutableRect(modalX, modalY, modalW, modalH));
    }

    private void buildModalWindow() {
        windowCard.themeEnabled(false);
        windowCard.boxVisualEnabled(true);
        windowCard.backgroundVisible(true);
        windowCard.background().set(ShopEditorStyles.BG_WINDOW);
        windowCard.borderVisible(true);
        windowCard.borderColor().set(ShopEditorStyles.BORDER_DEFAULT);
        windowCard.borderWidth(1.0f);
        windowCard.radius(4.0f);
        windowCard.spacing(6.0f);
        windowCard.layout(
                style ->
                        style.fixed(MODAL_WIDTH, MAX_MODAL_HEIGHT)
                                .padding(10.0f)
                                .overflow(Overflow.HIDDEN)
                                .alignItems(Align.CENTER)
                                .flexNone());

        // 1. Шапка: "Select Component" + векторный ✕
        HBox header = new HBox();
        header.layout(
                style ->
                        style.widthPercent(100.0f)
                                .height(22.0f)
                                .alignItems(Align.CENTER)
                                .justifyContent(Justify.SPACE_BETWEEN)
                                .flexNone());

        Label title = new Label(I18n.get("shop.ui.component_picker.title"));
        title.color().set(ShopEditorStyles.TEXT_BOLD);

        Button closeBtn = UnityIcons.createCloseButton(this::closeSelf);

        header.addChild(title);
        header.addChild(closeBtn);
        windowCard.addChild(header);

        // 2. Строка поиска и фильтра избранного
        HBox searchRow = new HBox();
        searchRow.spacing(6.0f);
        searchRow.layout(
                style ->
                        style.widthPercent(100.0f)
                                .height(22.0f)
                                .alignItems(Align.CENTER)
                                .flexNone());

        TextField searchField = new TextField();
        searchField.placeholder(I18n.get("shop.ui.component_picker.search"));
        searchField.themeEnabled(false);
        searchField.boxVisualEnabled(true);
        searchField.backgroundVisible(true);
        searchField.background().set(ShopEditorStyles.BG_INPUT);
        searchField.borderVisible(true);
        searchField.borderColor().set(ShopEditorStyles.BORDER_INPUT);
        searchField.borderWidth(1.0f);
        searchField.radius(2.0f);
        searchField.textColor().set(ShopEditorStyles.TEXT_PRIMARY);
        searchField.placeholderColor().set(ShopEditorStyles.TEXT_MUTED);
        searchField.layout(style -> style.height(20.0f).flexGrow(1.0f));
        searchField.onTextChanged(
                event -> {
                    this.searchQuery =
                            event.newText() != null
                                    ? event.newText().trim().toLowerCase(Locale.ROOT)
                                    : "";
                    refreshGrid();
                });

        favoritesBtn = new Button(I18n.get("shop.ui.component_picker.favorites"));
        ShopEditorStyles.styleButton(favoritesBtn);
        favoritesBtn.layout(style -> style.height(20.0f).padding(6.0f, 0.0f).flexNone());
        favoritesBtn.onClick(
                event -> {
                    this.favoritesOnly = !this.favoritesOnly;
                    favoritesBtn
                            .textColor()
                            .set(
                                    this.favoritesOnly
                                            ? ShopEditorStyles.AUDIO_GOLD
                                            : ShopEditorStyles.TEXT_PRIMARY);
                    refreshGrid();
                });

        searchRow.addChild(searchField);
        searchRow.addChild(favoritesBtn);
        windowCard.addChild(searchRow);

        // 3. Выпадающий список категорий
        List<String> categoryDisplayNames = new ArrayList<>();
        for (CategoryFilter cf : categoryFilters) {
            categoryDisplayNames.add(cf.displayName());
        }

        ComboBox categoryCombo = new ComboBox();
        categoryCombo.items(categoryDisplayNames);
        categoryCombo.selectedIndex(0);
        ShopEditorStyles.styleComboBox(categoryCombo);
        categoryCombo.layout(style -> style.widthPercent(100.0f).height(20.0f).flexNone());
        categoryCombo.onSelectionChanged(
                event -> {
                    int idx = categoryCombo.selectedIndex();
                    if (idx >= 0 && idx < categoryFilters.size()) {
                        this.selectedCategoryKey = categoryFilters.get(idx).key();
                    } else {
                        this.selectedCategoryKey = "all";
                    }
                    refreshGrid();
                });
        windowCard.addChild(categoryCombo);

        // 4. Скроллируемая область карточек компонентов
        listContainer = new VBox();
        listContainer.spacing(8.0f);
        listContainer.layout(
                style -> style.widthPercent(100.0f).padding(2.0f).flexGrow(0.0f).flexShrink(0.0f));

        ScrollView scroll = new ScrollView(listContainer);
        scroll.scrollStep(24.0f);
        scroll.scrollbarGap(2.0f);
        scroll.scrollbarTrackColor().set(ShopEditorStyles.BORDER_DEFAULT);
        scroll.scrollbarThumbColor().set(ShopEditorStyles.TEXT_MUTED);
        scroll.overlayScrollBars(false);
        scroll.layout(
                layout ->
                        layout.widthPercent(100.0f)
                                .flexGrow(1.0f)
                                .flexShrink(1.0f)
                                .minHeight(0.0f)
                                .overflowX(Overflow.HIDDEN)
                                .overflowY(Overflow.AUTO));
        windowCard.addChild(scroll);

        // 5. Подвал с кнопкой Cancel
        HBox footer = new HBox();
        footer.layout(
                style ->
                        style.widthPercent(100.0f)
                                .height(24.0f)
                                .justifyContent(Justify.CENTER)
                                .alignItems(Align.CENTER)
                                .flexGrow(0.0f)
                                .flexShrink(0.0f));

        Button cancelBtn = new Button(I18n.get("shop.ui.common.cancel"));
        ShopEditorStyles.styleButton(cancelBtn);
        cancelBtn.layout(style -> style.fixed(80.0f, 22.0f));
        cancelBtn.onClick(event -> closeSelf());
        footer.addChild(cancelBtn);

        windowCard.addChild(footer);
        addChild(windowCard);

        refreshGrid();
    }

    private void refreshGrid() {
        listContainer.clearChildren();

        Map<String, List<IComponentType<?>>> categorized = new LinkedHashMap<>();

        for (IComponentType<?> type : ShopComponentRegistry.getTypes().values()) {
            if (!type.showInEditor()) {
                continue;
            }

            ResourceLocation id = type.getId();
            String name = type.getTranslation().getString().toLowerCase(Locale.ROOT);
            String idStr = id.toString().toLowerCase(Locale.ROOT);
            String category =
                    type.getCategory() != null ? type.getCategory() : SDMShopConstants.MISC_GROUP;

            // Фильтр по категории
            if (!"all".equalsIgnoreCase(selectedCategoryKey)
                    && !category.equalsIgnoreCase(selectedCategoryKey)) {
                continue;
            }

            // Фильтр по избранному
            if (favoritesOnly && !ShopClientCache.isFavoriteComponent(id)) {
                continue;
            }

            // Фильтр по поисковому запросу
            if (!searchQuery.isEmpty()) {
                String translatedCat =
                        translateOrFallback(category, category).toLowerCase(Locale.ROOT);
                if (!name.contains(searchQuery)
                        && !idStr.contains(searchQuery)
                        && !translatedCat.contains(searchQuery)) {
                    continue;
                }
            }

            categorized.computeIfAbsent(category, k -> new ArrayList<>()).add(type);
        }

        if (categorized.isEmpty()) {
            Label empty = new Label(I18n.get("shop.ui.component_picker.empty"));
            empty.color().set(ShopEditorStyles.TEXT_MUTED);
            empty.layout(style -> style.margin(12.0f).alignSelf(Align.CENTER));
            listContainer.addChild(empty);
            return;
        }

        for (Map.Entry<String, List<IComponentType<?>>> entry : categorized.entrySet()) {
            String catKey = entry.getKey();
            List<IComponentType<?>> types = entry.getValue();

            // Переведенное человекочитаемое имя группы компонентов
            String catTitle = translateOrFallback(catKey, catKey);
            Label sectionTitle = new Label(catTitle);
            sectionTitle.color().set(ShopEditorStyles.TEXT_BOLD);
            sectionTitle.layout(style -> style.margin(4.0f, 2.0f));
            listContainer.addChild(sectionTitle);

            GridBox grid = new GridBox();
            grid.columns(3);
            grid.horizontalSpacing(6.0f);
            grid.verticalSpacing(6.0f);
            grid.layout(
                    style ->
                            style.gridTemplateColumns(
                                            GridTrack.fr(1.0f),
                                            GridTrack.fr(1.0f),
                                            GridTrack.fr(1.0f))
                                    .widthPercent(100.0f)
                                    .gap(6.0f));

            for (IComponentType<?> type : types) {
                grid.addChild(createComponentCard(type));
            }

            listContainer.addChild(grid);
        }
    }

    private Widget createComponentCard(IComponentType<?> type) {
        VBox card = new VBox();
        card.spacing(2.0f);
        card.layout(
                style ->
                        style.height(64.0f)
                                .padding(4.0f)
                                .alignItems(Align.CENTER)
                                .justifyContent(Justify.CENTER)
                                .flexNone());

        card.themeEnabled(false);
        card.boxVisualEnabled(true);
        card.backgroundVisible(true);
        card.background().set(ShopEditorStyles.BG_WORKSPACE);
        card.borderVisible(true);
        card.borderColor().set(ShopEditorStyles.BORDER_DEFAULT);
        card.borderWidth(1.0f);
        card.radius(3.0f);

        card.on(
                PointerEnteredEvent.TYPE,
                e -> {
                    card.background().set(ShopEditorStyles.BG_HEADER_HOVER);
                    card.borderColor().set(ShopEditorStyles.BORDER_INPUT_FOCUS);
                });
        card.on(
                PointerExitedEvent.TYPE,
                e -> {
                    card.background().set(ShopEditorStyles.BG_WORKSPACE);
                    card.borderColor().set(ShopEditorStyles.BORDER_DEFAULT);
                });

        // Верхний ряд: спейсер слева, иконка по центру, векторная звезда избранного справа
        HBox topRow = new HBox();
        topRow.layout(
                style ->
                        style.widthPercent(100.0f)
                                .height(24.0f)
                                .alignItems(Align.CENTER)
                                .justifyContent(Justify.SPACE_BETWEEN)
                                .flexNone());

        Box leftSpacer = new Box();
        leftSpacer.layout(style -> style.fixed(20.0f, 20.0f).flexNone());
        topRow.addChild(leftSpacer);

        // Иконка
        ItemStack iconStack = resolveIconStack(type.getIcon());
        ShopItemWidget itemWidget = new ShopItemWidget(iconStack, 20.0f);
        topRow.addChild(itemWidget);

        // Векторная кнопка избранного (без использования отсутствующих юникод-символов)
        Button starBtn = new Button("");
        starBtn.themeEnabled(false);
        starBtn.boxVisualEnabled(false);
        starBtn.focusRingVisible(false);
        starBtn.focusable(false);
        starBtn.layout(style -> style.fixed(20.0f, 20.0f).flexNone());
        starBtn.renderer(
                WidgetRender.of(
                        Button.class,
                        (draw, b) -> {
                            RectView bounds = b.layoutBounds();
                            float cx = bounds.x() + bounds.width() * 0.5f;
                            float cy = bounds.y() + bounds.height() * 0.5f;
                            boolean isFav = ShopClientCache.isFavoriteComponent(type.getId());
                            ColorView color =
                                    isFav
                                            ? ShopEditorStyles.AUDIO_GOLD
                                            : (b.hovered()
                                                    ? ShopEditorStyles.TEXT_PRIMARY
                                                    : ShopEditorStyles.TEXT_MUTED);
                            UnityIcons.drawStar(draw, cx, cy, 6.0f, color, isFav);
                        }));
        starBtn.on(
                PointerPressedEvent.TYPE,
                event -> {
                    if (event.phase() == EventPhase.CAPTURE) return;
                    if (event.button() == PointerButton.PRIMARY) {
                        ShopClientCache.toggleFavoriteComponent(type.getId());
                        if (favoritesOnly) {
                            refreshGrid();
                        } else {
                            starBtn.invalidate(2);
                        }
                        event.cancel();
                    }
                });
        starBtn.on(
                PointerReleasedEvent.TYPE,
                event -> {
                    if (event.phase() == EventPhase.CAPTURE) return;
                    if (event.button() == PointerButton.PRIMARY) {
                        event.cancel();
                    }
                });
        topRow.addChild(starBtn);
        card.addChild(topRow);

        // Заголовок компонента
        Label nameLbl = new Label(type.getTranslation().getString());
        nameLbl.color().set(ShopEditorStyles.TEXT_PRIMARY);
        nameLbl.layout(style -> style.alignSelf(Align.CENTER));
        card.addChild(nameLbl);

        // ID компонента (subtitle)
        String path = type.getId().getPath();
        Label idLbl = new Label(path);
        idLbl.color().set(ShopEditorStyles.TEXT_MUTED);
        idLbl.layout(style -> style.alignSelf(Align.CENTER));
        card.addChild(idLbl);

        // Клик по карточке — добавление компонента в оффер
        card.on(
                PointerPressedEvent.TYPE,
                event -> {
                    if (event.phase() == EventPhase.CAPTURE) return;
                    if (event.button() == PointerButton.PRIMARY) {
                        event.cancel();
                    }
                });

        card.on(
                PointerReleasedEvent.TYPE,
                event -> {
                    if (event.phase() == EventPhase.CAPTURE) return;
                    if (event.isCancelled()) return;
                    if (event.button() != PointerButton.PRIMARY) return;

                    Widget target = event.target();
                    if (target == starBtn || isDescendantOf(target, starBtn)) {
                        return;
                    }

                    RectView sb = starBtn.layoutBounds();
                    if (event.rootX() >= sb.x()
                            && event.rootX() <= sb.x() + sb.width()
                            && event.rootY() >= sb.y()
                            && event.rootY() <= sb.y() + sb.height()) {
                        return;
                    }

                    event.cancel();
                    if (closing) return;
                    closing = true;

                    ShopEditorState.get().addComponent(targetOffer, type);
                    closeSelf();
                });

        return card;
    }

    private static boolean isDescendantOf(Widget child, Widget ancestor) {
        if (child == null || ancestor == null) return false;
        Widget current = child;
        while (current != null) {
            if (current == ancestor) return true;
            current = current.parent();
        }
        return false;
    }

    private ItemStack resolveIconStack(CurrencyIcon icon) {
        if (icon != null) {
            if (icon.type() == IconType.ITEM) {
                if (icon.icon() instanceof ItemStack s) return s;
                if (icon.icon() instanceof Item item) return item.getDefaultInstance();
            }
        }
        return Items.ICE.getDefaultInstance();
    }

    private void closeSelf() {
        if (parent() instanceof OverlayLayer ol) {
            ol.removeOverlay(this);
        } else {
            ShopScreenController.closeModal(this);
        }
    }
}
