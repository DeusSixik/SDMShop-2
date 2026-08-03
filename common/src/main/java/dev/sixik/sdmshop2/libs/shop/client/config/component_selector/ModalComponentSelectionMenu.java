package dev.sixik.sdmshop2.libs.shop.client.config.component_selector;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.layout.Layout;
import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import dev.sixik.sdmshop2.libs.shop.SDMShopConstants;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.textures.ColorRectAndBorderTexture;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopIcons;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponentRegistry;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.InputTextBox;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.TextLabel;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.DropDownBox;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ModalWidget;
import dev.sixik.sdmshop2.utils.ShopUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public final class ModalComponentSelectionMenu {

    private static final int MODAL_WIDTH = 380;
    private static final int MODAL_HEIGHT = 285;
    private static final int PADDING = 5;
    private static final int GAP = 4;
    private static final int SEARCH_HEIGHT = 20;
    private static final int CATEGORY_HEIGHT = 20;
    private static final int ACTION_BUTTON_WIDTH = 64;
    private static final int FAVORITES_BUTTON_WIDTH = 88;
    private static final int FOOTER_HEIGHT = 20;
    private static final int SCROLLBAR_WIDTH = 4;
    private static final int TILE_WIDTH = 104;
    private static final int TILE_HEIGHT = 64;
    private static final int TILE_SPACING = 5;
    private static final int CATEGORY_HEADER_HEIGHT = 14;
    private static final int CONTEXT_MENU_WIDTH = 120;
    private static final int CONTEXT_MENU_ROW_HEIGHT = 20;

    private static List<String> cachedCategories;
    private static List<IComponentType<?>> cachedSortedComponents;
    private static int cachedRegistrySignature = Integer.MIN_VALUE;
    private static String currentCategory = SDMShopConstants.ALL_GROUP;
    private static boolean onlyFavorites;

    private ModalComponentSelectionMenu() {
    }

    public static ModalWidget showComponentSelector(
            final Widget owner,
            final Consumer<ShopComponent> onSelected
    ) {
        return showComponentSelector(owner, Component.translatable("client.shop.component.editor.component_selector.title"), onSelected);
    }

    public static ModalWidget showComponentSelector(
            final Widget owner,
            final Component title,
            final Consumer<ShopComponent> onSelected
    ) {
        final ModalWidget modal = new ModalWidget(MODAL_WIDTH, MODAL_HEIGHT)
                .setTitle(title)
                .setCloseOnOutsideClick(true)
                .setCloseOnEsc(true);

        final ModalWidget opened = ModalWidget.openNested(owner, modal);
        if (opened == null) {
            return null;
        }

        build(opened, onSelected);
        return opened;
    }

    private static void build(ModalWidget modal, Consumer<ShopComponent> onSelected) {
        final int contentWidth = Math.max(1, modal.getContentWidth());
        final int contentHeight = Math.max(1, modal.getContentHeight());

        final int searchWidth = Math.max(1, contentWidth - ACTION_BUTTON_WIDTH - FAVORITES_BUTTON_WIDTH - GAP * 2);
        final int categoryY = SEARCH_HEIGHT + GAP;
        final int listY = categoryY + CATEGORY_HEIGHT + GAP;
        final int footerY = Math.max(listY + 1, contentHeight - FOOTER_HEIGHT);
        final int listHeight = Math.max(1, footerY - listY - GAP);
        final int listWidth = Math.max(1, contentWidth - SCROLLBAR_WIDTH - PADDING);

        final InputTextBox search = new InputTextBox(0, 0, searchWidth, SEARCH_HEIGHT);
        styleInput(search);
        search.setPlaceholder(Component.literal("Search component by name, id, category..."));
        search.setClientSideWidget();

        final ButtonWidget favoritesButton = createButton(
                searchWidth + GAP,
                0,
                FAVORITES_BUTTON_WIDTH,
                SEARCH_HEIGHT,
                favoritesButtonText(),
                ignored -> { }
        );

        final ButtonWidget wikiButton = createButton(
                searchWidth + FAVORITES_BUTTON_WIDTH + GAP * 2,
                0,
                ACTION_BUTTON_WIDTH,
                SEARCH_HEIGHT,
                Component.literal("Wiki"),
                ignored -> openWiki()
        );
        wikiButton.setHoverTooltips(Component.translatable("client.shop.component.editor.button.wiki.tooltip"));

        final List<String> categories = getCategories();
        final DropDownBox categorySelector = createCategoryDropDown(0, categoryY, contentWidth, CATEGORY_HEIGHT, categories);
        int selectedCategoryIndex = categories.indexOf(currentCategory);
        if (selectedCategoryIndex < 0) {
            currentCategory = SDMShopConstants.ALL_GROUP;
            selectedCategoryIndex = Math.max(0, categories.indexOf(currentCategory));
        }
        categorySelector.setSelectedIndex(selectedCategoryIndex, false);

        final DraggableScrollableWidgetGroup scroll = new DraggableScrollableWidgetGroup(0, listY, contentWidth, listHeight);
        scroll.setScrollWheelDirection(DraggableScrollableWidgetGroup.ScrollWheelDirection.VERTICAL);
        scroll.setLayout(Layout.NONE);
        scroll.setYScrollBarWidth(SCROLLBAR_WIDTH);
        scroll.setYBarStyle(null, ColorPattern.WHITE.rectTexture().setRadius(2));
        scroll.setClientSideWidget();

        final WidgetGroup grid = new ScrollClippedWidgetGroup(0, 0, listWidth, 1);
        grid.setLayout(Layout.NONE);
        grid.setDynamicSized(false);
        scroll.addWidget(grid);

        final ButtonWidget cancelButton = createButton(
                Math.max(0, (contentWidth - ACTION_BUTTON_WIDTH) / 2),
                footerY,
                ACTION_BUTTON_WIDTH,
                FOOTER_HEIGHT,
                Component.translatable("ldlib.gui.tips.cancel"),
                ignored -> {
                    modal.close();
                    if (onSelected != null) {
                        onSelected.accept(null);
                    }
                }
        );

        final Runnable[] rebuild = new Runnable[1];
        rebuild[0] = () -> rebuildGrid(modal, scroll, grid, listWidth, search.getCurrentString(), onSelected, rebuild[0]);

        search.setTextResponder(ignored -> rebuild[0].run());
        favoritesButton.setOnPressCallback(ignored -> {
            onlyFavorites = !onlyFavorites;
            favoritesButton.setText(favoritesButtonText());
            styleToggleButton(favoritesButton, onlyFavorites);
            rebuild[0].run();
        });
        categorySelector.setSelectionChangedListener(index -> {
            if (index < 0 || index >= categories.size()) {
                return;
            }

            currentCategory = categories.get(index);
            rebuild[0].run();
        });

        styleToggleButton(favoritesButton, onlyFavorites);

        modal.addWidget(search);
        modal.addWidget(favoritesButton);
        modal.addWidget(wikiButton);
        modal.addWidget(categorySelector);
        modal.addWidget(scroll);
        modal.addWidget(cancelButton);

        rebuild[0].run();
    }

    private static void rebuildGrid(
            ModalWidget modal,
            DraggableScrollableWidgetGroup scroll,
            WidgetGroup grid,
            int gridWidth,
            String query,
            Consumer<ShopComponent> onSelected,
            Runnable rebuild
    ) {
        final int previousScrollY = scroll == null ? 0 : scroll.getScrollYOffset();
        if (scroll != null) {
            scroll.setScrollYOffset(0);
        }

        grid.clearAllWidgets();
        grid.setSelfPosition(0, 0);

        final String filter = normalizeSearch(query);
        final Set<ResourceLocation> favorites = ShopClientCache.getFavoriteComponents();
        final List<IComponentType<?>> components = new ArrayList<>();

        for (IComponentType<?> type : getSortedComponents()) {
            if (!type.showInEditor()) {
                continue;
            }

            if (onlyFavorites && !favorites.contains(type.getId())) {
                continue;
            }

            String typeCategory = getComponentCategory(type);
            if (!currentCategory.equals(SDMShopConstants.ALL_GROUP) && !currentCategory.equals(typeCategory)) {
                continue;
            }

            if (!filter.isEmpty() && !matchesSearch(type, filter)) {
                continue;
            }

            components.add(type);
        }

        components.sort(componentComparator(favorites));

        if (components.isEmpty()) {
            TextLabel empty = new TextLabel(Component.literal("Nothing found"))
                    .setAutoSize(false)
                    .setPadding(4, 0)
                    .setOverflowMode(TextLabel.OverflowMode.ELLIPSIS)
                    .alignMiddle();
            empty.setSelfPosition(0, 0);
            empty.setSize(gridWidth, SEARCH_HEIGHT);
            grid.addWidget(empty);
            finishGridRebuild(scroll, grid, gridWidth, SEARCH_HEIGHT, previousScrollY);
            return;
        }

        final boolean showCategoryHeaders = currentCategory.equals(SDMShopConstants.ALL_GROUP);
        final int columns = Math.max(1, (gridWidth + TILE_SPACING) / (TILE_WIDTH + TILE_SPACING));
        int column = 0;
        int y = 0;
        String lastCategory = null;

        for (IComponentType<?> type : components) {
            final String category = getComponentCategory(type);
            if (showCategoryHeaders && !Objects.equals(lastCategory, category)) {
                if (column != 0) {
                    y += TILE_HEIGHT + TILE_SPACING;
                    column = 0;
                }

                grid.addWidget(createCategoryHeader(0, y, gridWidth, CATEGORY_HEADER_HEIGHT, category));
                y += CATEGORY_HEADER_HEIGHT + TILE_SPACING;
                lastCategory = category;
            }

            final int x = column * (TILE_WIDTH + TILE_SPACING);
            final int tileY = y;

            grid.addWidget(createTileWidget(x, tileY, TILE_WIDTH, TILE_HEIGHT, type, favorites.contains(type.getId()), () -> {
                modal.close();
                if (onSelected != null) {
                    onSelected.accept(type.createDefault());
                }
            }, rebuild));

            column++;
            if (column >= columns) {
                column = 0;
                y += TILE_HEIGHT + TILE_SPACING;
            }
        }

        int height = column == 0 ? y - TILE_SPACING : y + TILE_HEIGHT;
        finishGridRebuild(scroll, grid, gridWidth, Math.max(1, height), previousScrollY);
    }

    private static void finishGridRebuild(
            DraggableScrollableWidgetGroup scroll,
            WidgetGroup grid,
            int gridWidth,
            int gridHeight,
            int previousScrollY
    ) {
        grid.setSize(gridWidth, Math.max(1, gridHeight));

        if (scroll == null) {
            return;
        }

        int maxScrollY = Math.max(0, grid.getSizeHeight() - scroll.getSizeHeight());
        scroll.setScrollYOffset(Math.min(previousScrollY, maxScrollY));
    }

    private static Widget createCategoryHeader(int x, int y, int width, int height, String category) {
        TextLabel header = new TextLabel(x, y, width, height, Component.translatable(category))
                .setAutoSize(false)
                .setPadding(5, 0)
                .setColor(0xFFFFD77A)
                .setMaxLines(1)
                .setOverflowMode(TextLabel.OverflowMode.ELLIPSIS)
                .setAlignment(TextLabel.HorizontalAlignment.LEFT, TextLabel.VerticalAlignment.CENTER);
        header.setBackground(new ColorRectAndBorderTexture(0xFF1B1E2A, 0xFF3D4458, 1).setRadius(3));
        return header;
    }

    private static Widget createTileWidget(
            final int x,
            final int y,
            final int w,
            final int h,
            final IComponentType<?> type,
            final boolean favorite,
            final Runnable onClick,
            final Runnable rebuild
    ) {
        final WidgetGroup tile = new WidgetGroup(x, y, w, h) {
            @Override
            public Widget getHoverElement(double mouseX, double mouseY) {
                return isInsideScrollViewport(this, mouseX, mouseY) ? super.getHoverElement(mouseX, mouseY) : null;
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (!isInsideScrollViewport(this, mouseX, mouseY)) {
                    return false;
                }

                if (super.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }

                if (!isMouseOverElement(mouseX, mouseY)) {
                    return false;
                }

                Widget parentScroll = getParent() == null ? null : getParent().getParent();
                if (parentScroll != null && !parentScroll.isMouseOverElement(mouseX, mouseY)) {
                    return false;
                }

                if (button == 0) {
                    Widget.playButtonClickSound();
                    onClick.run();
                    return true;
                }

                if (button == 1) {
                    openContextMenu(this, (int) mouseX, (int) mouseY, type);
                    Widget.playButtonClickSound();
                    return true;
                }

                return false;
            }
        };

        tile.setBackground(new ColorRectAndBorderTexture(
                favorite ? 0xFF333020 : 0xFF252533,
                favorite ? 0xFFFFD77A : 0xFF5C637A,
                1f
        ).setRadius(4f));
        tile.setHoverTexture(new ColorRectAndBorderTexture(
                favorite ? 0xFF403A25 : 0xFF30364A,
                favorite ? 0xFFFFE08A : 0xFF7986CB,
                1f
        ).setRadius(4f));

        final Component tooltip = tooltip(type);
        if (tooltip != null) {
            tile.setHoverTooltips(tooltip);
        }

        final CurrencyIcon componentIcon = type.getIcon();
        IGuiTexture iconTexture = ShopUtils.getCurrencyTexture(componentIcon);
        if (iconTexture == null) {
            iconTexture = new ColorRectTexture(0x00000000);
        }

        final ImageWidget icon = new ImageWidget((w - 24) / 2, 5, 24, 24, iconTexture);
        if (tooltip != null) {
            icon.setHoverTooltips(tooltip);
        }
        tile.addWidget(icon);

        final ButtonWidget favoriteButton = new ButtonWidget(w - 16, 3, 12, 12, ignored -> {
            ShopClientCache.toggleFavoriteComponent(type.getId());
            if (rebuild != null) {
                rebuild.run();
            }
        });
        favoriteButton.setClientSideWidget();
        favoriteButton.setButtonTexture(favorite ? ShopIcons.STAR_FULL : ShopIcons.STAR_EMPTY);
        favoriteButton.setHoverTexture(new ColorBorderTexture(1, 0xFFFFFFFF));
        favoriteButton.setClickedTexture(new ColorBorderTexture(1, 0xFFFFD77A));
        favoriteButton.setHoverTooltips(Component.literal(favorite ? "Remove from favorites" : "Add to favorites"));
        tile.addWidget(favoriteButton);

        final TextLabel name = new TextLabel(2, 33, w - 4, 14, type.getTranslation())
                .setAutoSize(false)
                .setMaxLines(1)
                .setPadding(1, 0)
                .setOverflowMode(TextLabel.OverflowMode.ELLIPSIS)
                .setAlignment(TextLabel.HorizontalAlignment.CENTER, TextLabel.VerticalAlignment.CENTER);
        if (tooltip != null) {
            name.setHoverTooltips(tooltip);
        }
        tile.addWidget(name);

        final TextLabel id = new TextLabel(2, 49, w - 4, 10, Component.literal(shortId(type.getId())))
                .setAutoSize(false)
                .setScale(0.7f)
                .setMinScale(0.55f)
                .setColor(0xFFAEB4C6)
                .setMaxLines(1)
                .setPadding(1, 0)
                .setOverflowMode(TextLabel.OverflowMode.ELLIPSIS)
                .setAlignment(TextLabel.HorizontalAlignment.CENTER, TextLabel.VerticalAlignment.CENTER);
        if (tooltip != null) {
            id.setHoverTooltips(tooltip);
        }
        tile.addWidget(id);

        return tile;
    }

    private static Comparator<IComponentType<?>> componentComparator(Set<ResourceLocation> favorites) {
        return Comparator
                .comparing((IComponentType<?> type) -> getComponentCategory(type).toLowerCase(Locale.ROOT))
                .thenComparing(type -> !favorites.contains(type.getId()))
                .thenComparing(type -> type.getTranslation().getString().toLowerCase(Locale.ROOT))
                .thenComparing(type -> type.getId().toString());
    }

    private static boolean matchesSearch(IComponentType<?> type, String filter) {
        String id = type.getId() == null ? "" : type.getId().toString().toLowerCase(Locale.ROOT);
        String path = type.getId() == null ? "" : type.getId().getPath().replace('_', ' ').toLowerCase(Locale.ROOT);
        String name = type.getTranslation().getString().toLowerCase(Locale.ROOT);
        String category = getComponentCategory(type).toLowerCase(Locale.ROOT);
        String translationKey = type.getTranslationKey().toLowerCase(Locale.ROOT);

        return id.contains(filter)
                || path.contains(filter)
                || name.contains(filter)
                || category.contains(filter)
                || translationKey.contains(filter);
    }

    private static String normalizeSearch(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private static Component tooltip(IComponentType<?> type) {
        final String tooltipKey = type.getTranslationKey() + ".tooltip";
        return I18n.exists(tooltipKey) ? Component.translatable(tooltipKey) : null;
    }

    private static String shortId(ResourceLocation id) {
        if (id == null) {
            return "";
        }

        String path = id.getPath();
        return path == null || path.isBlank() ? id.toString() : path;
    }

    private static String getComponentCategory(final IComponentType<?> type) {
        String category = type.getCategory();
        return category == null || category.isBlank() ? SDMShopConstants.ALL_GROUP : category;
    }

    private static List<IComponentType<?>> getSortedComponents() {
        refreshCachesIfRegistryChanged();
        if (cachedSortedComponents != null) {
            return cachedSortedComponents;
        }

        List<IComponentType<?>> list = new ObjectArrayList<>(ShopComponentRegistry.getTypes().values());
        list.sort(Comparator
                .comparing((IComponentType<?> type) -> getComponentCategory(type).toLowerCase(Locale.ROOT))
                .thenComparing(type -> type.getTranslation().getString().toLowerCase(Locale.ROOT))
                .thenComparing(type -> type.getId().toString()));

        cachedSortedComponents = list;
        return list;
    }

    private static List<String> getCategories() {
        refreshCachesIfRegistryChanged();
        if (cachedCategories != null) {
            return cachedCategories;
        }

        Set<String> categories = new ObjectOpenHashSet<>();
        for (IComponentType<?> value : ShopComponentRegistry.getTypes().values()) {
            if (value.showInEditor()) {
                categories.add(getComponentCategory(value));
            }
        }

        List<String> sortedCategories = new ArrayList<>(categories);
        Collections.sort(sortedCategories);

        ObjectArrayList<String> result = new ObjectArrayList<>(sortedCategories.size() + 1);
        result.add(SDMShopConstants.ALL_GROUP);
        result.addAll(sortedCategories);

        cachedCategories = result;
        return result;
    }

    public static void invalidateCaches() {
        cachedRegistrySignature = Integer.MIN_VALUE;
        cachedCategories = null;
        cachedSortedComponents = null;
    }

    private static void refreshCachesIfRegistryChanged() {
        int registrySignature = Objects.hash(
                ShopComponentRegistry.getTypes().size(),
                ShopComponentRegistry.getTypes().keySet()
        );

        if (registrySignature == cachedRegistrySignature) {
            return;
        }

        cachedRegistrySignature = registrySignature;
        cachedCategories = null;
        cachedSortedComponents = null;
    }

    private static Component favoritesButtonText() {
        return Component.literal(onlyFavorites ? "Favorites only" : "Favorites");
    }

    private static DropDownBox createCategoryDropDown(int x, int y, int width, int height, List<String> categories) {
        DropDownBox dropDown = new DropDownBox(x, y, width, height)
                .setOptionHeight(height)
                .setMaxVisibleOptions(8)
                .setShowScrollBar(true)
                .setPadding(4, 1)
                .setRowSpacing(1)
                .setScrollBarWidth(SCROLLBAR_WIDTH)
                .setScrollBarPadding(1)
                .setColors(
                        0xFF101016,
                        0xFF1E1F28,
                        0xFF5C637A,
                        0xFF202638,
                        0xFF30364A
                )
                .setSelectedOptionFill(0xFF30364A)
                .setScrollBarColors(0x5530303A, 0xFF6D7485, 0xFFAAB2C5);
        dropDown.setClientSideWidget();
        dropDown.setHoverTexture(new ColorBorderTexture(1, 0xFF7986CB));

        for (String category : categories) {
            TextLabel option = new TextLabel(Component.translatable(category))
                    .setAutoSize(false)
                    .setPadding(4, 0)
                    .setOverflowMode(TextLabel.OverflowMode.ELLIPSIS)
                    .alignMiddle();
            option.setSize(Math.max(1, width - 16), height);
            dropDown.addOption(option);
        }

        return dropDown;
    }

    private static ButtonWidget createButton(int x, int y, int width, int height, Component text, Consumer<Object> onPress) {
        ButtonWidget button = new ButtonWidget(x, y, width, height, text, ignored -> onPress.accept(ignored));
        styleButton(button);
        button.setClientSideWidget();
        return button;
    }

    private static void styleInput(InputTextBox input) {
        input.setBackground(new ColorRectAndBorderTexture(0xFF101016, 0xFF5C637A, 1).setRadius(2));
        input.setFocusedOutline(0xFF101016, 0xFFFFFFFF);
        input.setTextColor(0xFFE8E8F0);
        input.setPadding(4, 1);
    }

    private static void styleButton(ButtonWidget button) {
        button.setButtonColors(0xFF2A2A36, 0xFF3D3D4E);
        button.setHoverColors(0xFF34384A, 0xFF7986CB);
        button.setClickedColors(0xFF202331, 0xFF7986CB);
        button.setTextColor(0xFFFFFFFF);
        button.setTextPadding(3);
        button.setMinTextScale(0.35f);
    }

    private static void styleToggleButton(ButtonWidget button, boolean enabled) {
        if (enabled) {
            button.setButtonColors(0xFF3B321C, 0xFFFFD77A);
            button.setHoverColors(0xFF4A3C22, 0xFFFFE08A);
            button.setClickedColors(0xFF2A2418, 0xFFFFD77A);
        } else {
            styleButton(button);
        }
    }

    private static void openWiki() {
        String url = I18n.get("client.shop.component.editor.button.wiki").trim();

        if (url.contains("http")) {
            url = url.substring(url.indexOf("http"));
        }

        try {
            Util.getPlatform().openUri(new java.net.URI(url));
        } catch (Exception e) {
            SDMShop2.LOGGER.error("Malformed Wiki URL: {}", url);
        }
    }

    private static void openContextMenu(Widget widget, int mouseX, int mouseY, IComponentType<?> type) {
        if (widget.getGui() == null) {
            return;
        }

        Widget root = widget;
        while (root.getParent() != null) {
            root = root.getParent();
        }

        if (!(root instanceof WidgetGroup mainGroup)) {
            return;
        }

        WidgetGroup contextMenu = new WidgetGroup(mouseX, mouseY, CONTEXT_MENU_WIDTH, 0) {
            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                boolean handled = super.mouseClicked(mouseX, mouseY, button);
                if (!isMouseOverElement(mouseX, mouseY)) {
                    mainGroup.removeWidget(this);
                }
                return handled;
            }

            @Override
            public void onFocusChanged(Widget lastFocus, Widget focus) {
                if (!isFocus()) {
                    mainGroup.removeWidget(this);
                }
            }
        };

        contextMenu.setDynamicSized(true);
        contextMenu.setLayout(Layout.VERTICAL_LEFT);
        contextMenu.setBackground(new ColorRectAndBorderTexture(0xFF1E1E1E, 0xFF555555, 1));

        final ButtonWidget copyId = new ButtonWidget(
                0,
                0,
                CONTEXT_MENU_WIDTH,
                CONTEXT_MENU_ROW_HEIGHT,
                Component.translatable("client.shop.component.editor.component_selector.widget.tile.copy_id"),
                ignored -> {
                    Minecraft.getInstance().keyboardHandler.setClipboard(type.getId().toString());
                    if (Minecraft.getInstance().player != null) {
                        Minecraft.getInstance().player.sendSystemMessage(Component.translatable("client.shop.component.editor.copied"));
                    }
                    mainGroup.removeWidget(contextMenu);
                }
        );
        styleButton(copyId);
        copyId.setClientSideWidget();
        contextMenu.addWidget(copyId);

        mainGroup.addWidget(contextMenu);
        contextMenu.setFocus(true);
    }

    private static boolean isInsideScrollViewport(Widget widget, double mouseX, double mouseY) {
        Widget current = widget;
        while (current != null) {
            if (current instanceof DraggableScrollableWidgetGroup scroll) {
                return mouseX >= scroll.getPositionX()
                        && mouseY >= scroll.getPositionY()
                        && mouseX < scroll.getPositionX() + scroll.getSizeWidth()
                        && mouseY < scroll.getPositionY() + scroll.getSizeHeight();
            }

            current = current.getParent();
        }

        return true;
    }

    private static class ScrollClippedWidgetGroup extends WidgetGroup {

        private ScrollClippedWidgetGroup(int x, int y, int width, int height) {
            super(x, y, width, height);
        }

        @Override
        public Widget getHoverElement(double mouseX, double mouseY) {
            return isInsideScrollViewport(this, mouseX, mouseY) ? super.getHoverElement(mouseX, mouseY) : null;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInsideScrollViewport(this, mouseX, mouseY) && super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
            return isInsideScrollViewport(this, mouseX, mouseY) && super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            return isInsideScrollViewport(this, mouseX, mouseY) && super.mouseReleased(mouseX, mouseY, button);
        }

        @Override
        public boolean mouseWheelMove(double mouseX, double mouseY, double wheelDelta) {
            return isInsideScrollViewport(this, mouseX, mouseY) && super.mouseWheelMove(mouseX, mouseY, wheelDelta);
        }
    }
}
