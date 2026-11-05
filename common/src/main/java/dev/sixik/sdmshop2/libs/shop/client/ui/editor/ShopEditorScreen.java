package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopScreenController;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityIcons;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopEditorStyles;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.utils.ShopUtils;
import dev.sixik.unigui.api.core.UnityLikeUIScaleProvider;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.layout.Overflow;
import dev.sixik.unigui.api.render.UiRenderPolicy;
import dev.sixik.unigui.api.screen.UniScreen;
import dev.sixik.unigui.api.text.Fonts;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.impl.core.DefaultUIContext;
import dev.sixik.unigui.widgets.containers.*;
import dev.sixik.unigui.widgets.display.CanvasWidget;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.docking.*;
import dev.sixik.unigui.widgets.feedback.ContextMenu;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.interaction.Button;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.resources.language.I18n;

/**
 * Полноэкранный редактор магазина SDMShop-2 в стиле Unity Editor. Воспроизводит компоновку Unity: -
 * Сверху: Toolbar с выпадающими меню (File, Window), заголовком и быстрыми действиями - Слева:
 * Hierarchy (поиск и дерево категорий, товаров, валют) - По центру: 3 вкладки вместо Scene/Game —
 * Catalog, Category, Money - Справа: Inspector (компоненты, свойства и кнопка Add Component)
 */
public final class ShopEditorScreen {

    private enum MenuId {
        FILE,
        WINDOW
    }

    /** Whether the current description was opened through the game backend. */
    private static boolean guiOpened;

    private static UniScreen currentScreen;
    private static OverlayLayer activeOverlay;
    private static ShopHierarchyView hierarchyView;
    private static ShopCatalogPreviewView catalogView;
    private static ShopCategoryWorkspaceView categoryView;
    private static ShopMoneyWorkspaceView moneyView;
    private static ShopInspectorView inspectorView;
    private static DockingRoot dockingRoot;
    private static final Map<String, DockPane> paneRegistry = new HashMap<>();

    /** Toolbar listener registration owned by the current editor window. */
    private static dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUiSubscription
            toolbarSubscription;

    private static ContextMenu activeMenu = null;
    private static MenuId lastOpenedMenuId = null;

    private ShopEditorScreen() {}

    public static void open() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc != null ? mc.player : null;
        if (!ShopUtils.isPlayerAdmin(player)) {
            return;
        }
        createScreen().openGui();
        guiOpened = true;
    }

    /** Closes the backend GUI and releases its UI resources. */
    public static void close() {
        UniScreen closing = currentScreen;
        if (closing != null) {
            closing.onClose(null);
            try {
                if (guiOpened) closing.closeGui();
            } finally {
                disposeUi();
            }
        } else {
            disposeUi();
        }
    }

    /** Releases subscriptions and widgets without invoking the game backend. */
    private static void disposeUi() {
        guiOpened = false;
        closeActiveMenu();
        if (toolbarSubscription != null) {
            toolbarSubscription.close();
            toolbarSubscription = null;
        }
        UniScreen closing = currentScreen;
        currentScreen = null;
        if (hierarchyView != null) hierarchyView.setOnTabSwitchRequested(null);
        // Hidden docking panes are not necessarily children of the mounted root.
        if (hierarchyView != null) hierarchyView.dispose();
        if (catalogView != null) catalogView.dispose();
        if (categoryView != null) categoryView.dispose();
        if (moneyView != null) moneyView.dispose();
        if (inspectorView != null) inspectorView.dispose();
        if (closing != null) {
            closing.onClose(null);
            closing.root().dispose();
        }
        activeOverlay = null;
        hierarchyView = null;
        catalogView = null;
        categoryView = null;
        moneyView = null;
        inspectorView = null;
        dockingRoot = null;
        paneRegistry.clear();
    }

    public static OverlayLayer getActiveOverlay() {
        return activeOverlay;
    }

    public static ShopHierarchyView getHierarchyView() {
        return hierarchyView;
    }

    public static ShopCatalogPreviewView getCatalogView() {
        return catalogView;
    }

    public static ShopCategoryWorkspaceView getCategoryView() {
        return categoryView;
    }

    public static ShopMoneyWorkspaceView getMoneyView() {
        return moneyView;
    }

    public static ShopInspectorView getInspectorView() {
        return inspectorView;
    }

    public static DockingRoot getDockingRoot() {
        return dockingRoot;
    }

    public static Map<String, DockPane> getPaneRegistry() {
        return java.util.Collections.unmodifiableMap(paneRegistry);
    }

    public static void closeActiveMenu() {
        if (activeMenu != null) {
            ContextMenu m = activeMenu;
            activeMenu = null;
            lastOpenedMenuId = null;
            try {
                m.close();
            } catch (Exception ignored) {
            }
            if (activeOverlay != null) {
                try {
                    activeOverlay.removeOverlay(m);
                } catch (Exception ignored) {
                }
            }
        }
    }

    public static void openResetSessionModal() {
        if (activeOverlay != null) {
            activeOverlay.addOverlay(new ShopResetSessionModal());
        } else {
            ShopScreenController.openModal(new ShopResetSessionModal());
        }
    }

    public static void closeEditor() {
        close();
        ShopEditorState.get().exitEditor();
        ShopScreenController.openShop();
    }

    public static UniScreen createScreen() {
        disposeUi();
        DefaultUIContext context = new DefaultUIContext();
        context.scaleProvider(
                new UnityLikeUIScaleProvider()
                        .referenceResolution(1920.0f, 1080.0f)
                        .matchBalanced()
                        .scaleRange(0.75f, 6.0f)
                        .userScale(1.8f));

        Widget root = buildRoot();
        currentScreen =
                new UniScreen("SDMShop Editor", root, context)
                        .scaleWithGameGui(false)
                        .renderPolicy(UiRenderPolicy.vsync())
                        .defaultFont(Fonts.defaultFace())
                        .onClose(ShopEditorScreen::disposeUi);
        return currentScreen;
    }

    private static Widget buildRoot() {
        StackPanel viewport = new StackPanel();
        viewport.layout(style -> style.sizePercent(100.0f, 100.0f).overflow(Overflow.HIDDEN));

        // Фоновый слой #202020
        Box bg = new Box();
        bg.themeEnabled(false);
        bg.backgroundVisible(true);
        bg.background().set(ShopEditorStyles.BG_WORKSPACE);
        bg.borderVisible(false);
        bg.layout(style -> style.sizePercent(100.0f, 100.0f));
        viewport.addChild(bg);

        // Оверлей для модальных окон
        activeOverlay = new OverlayLayer();
        activeOverlay.layout(style -> style.sizePercent(100.0f, 100.0f));

        VBox editorRoot = new VBox();
        editorRoot.spacing(0.0f);
        editorRoot.layout(style -> style.sizePercent(100.0f, 100.0f));

        // 1. Создание представлений
        hierarchyView = new ShopHierarchyView();
        catalogView = new ShopCatalogPreviewView();
        categoryView = new ShopCategoryWorkspaceView();
        moneyView = new ShopMoneyWorkspaceView();
        inspectorView = new ShopInspectorView(activeOverlay);

        // 2. Создание DockingRoot и настройка темы Unity
        DockingRoot root = new DockingRoot();
        root.layout(
                style ->
                        style.widthPercent(100.0f)
                                .heightPercent(100.0f)
                                .flexGrow(1.0f)
                                .flexShrink(1.0f));

        DockTheme theme = DockTheme.editor();
        theme.color(DockColor.STRIP_BACKGROUND).set(ShopEditorStyles.BG_WORKSPACE);
        theme.color(DockColor.STRIP_BORDER).set(ShopEditorStyles.BORDER_DEFAULT);
        theme.color(DockColor.TAB_BACKGROUND).set(ShopEditorStyles.BG_TAB_INACTIVE);
        theme.color(DockColor.TAB_HOVER).set(ShopEditorStyles.BG_TAB_ACTIVE);
        theme.color(DockColor.TAB_SELECTED).set(ShopEditorStyles.BG_TAB_ACTIVE);
        theme.color(DockColor.TAB_SELECTED_INACTIVE).set(ShopEditorStyles.BG_TAB_INACTIVE);
        theme.color(DockColor.TAB_ACCENT).set(ShopEditorStyles.TAB_ACCENT);
        theme.color(DockColor.TAB_ACCENT_INACTIVE).set(ShopEditorStyles.TAB_ACCENT);
        theme.color(DockColor.TAB_TEXT).set(ShopEditorStyles.TEXT_PRIMARY);
        theme.color(DockColor.TAB_TEXT_MUTED).set(ShopEditorStyles.TEXT_MUTED);
        theme.color(DockColor.CONTENT_BACKGROUND).set(ShopEditorStyles.BG_WINDOW);
        theme.color(DockColor.PANE_BORDER).set(ShopEditorStyles.BORDER_DEFAULT);
        theme.color(DockColor.SPLIT).set(ShopEditorStyles.BORDER_DEFAULT);
        theme.color(DockColor.SPLIT_HOVER).set(ShopEditorStyles.SELECTION_BLUE);
        theme.color(DockColor.SPLIT_PRESSED).set(ShopEditorStyles.SELECTION_BLUE);
        theme.splitStyle(DockingRoot.SplitHandleStyle.SEAM);
        theme.splitThickness(3.0f);
        theme.tabHeight(22.0f);

        root.theme(theme);
        root.tabHeight(22.0f);
        root.splitHandleThickness(3.0f);
        root.splitHandleStyle(DockingRoot.SplitHandleStyle.SEAM);
        root.tearOffOnDragOut(false);

        // Регистрация панелей
        DockPane hierarchyPane =
                DockPane.tool("hierarchy", "Hierarchy", hierarchyView)
                        .icon(UnityIcons.HIERARCHY_TAB_ICON);
        DockPane catalogPane =
                DockPane.document("catalog", "Catalog", catalogView)
                        .icon(UnityIcons.SCENE_TAB_ICON);
        DockPane categoryPane =
                DockPane.document("category", "Category", categoryView)
                        .icon(UnityIcons.GAME_TAB_ICON);
        DockPane moneyPane =
                DockPane.document("money", "Money", moneyView).icon(UnityIcons.CONSOLE_TAB_ICON);
        DockPane inspectorPane =
                DockPane.tool("inspector", "Inspector", inspectorView)
                        .icon(UnityIcons.INSPECTOR_TAB_ICON);

        dockingRoot = root;
        paneRegistry.clear();
        paneRegistry.put("hierarchy", hierarchyPane);
        paneRegistry.put("catalog", catalogPane);
        paneRegistry.put("category", categoryPane);
        paneRegistry.put("money", moneyPane);
        paneRegistry.put("inspector", inspectorPane);

        dockingRoot.restoreLayout(buildDefaultLayoutSnapshot(), paneRegistry);

        // Связывание Hierarchy с переключением вкладок в центре
        hierarchyView.setOnTabSwitchRequested(
                tabId -> {
                    if (dockingRoot != null && dockingRoot.findPane(tabId) != null) {
                        dockingRoot.selectPane(tabId);
                    }
                });

        // 3. Верхний тулбар Unity
        editorRoot.addChild(buildTopToolbar());
        editorRoot.addChild(ShopEditorStyles.separatorLine());

        // 4. Область докинга
        editorRoot.addChild(dockingRoot);

        activeOverlay.content(editorRoot);
        viewport.addChild(activeOverlay);

        return viewport;
    }

    public static DockLayoutSnapshot buildDefaultLayoutSnapshot() {
        DockLayoutSnapshot.Node centerDocs =
                new DockLayoutSnapshot.Node(
                        DockNode.Kind.LEAF,
                        null,
                        1.0f,
                        List.of("catalog", "category", "money"),
                        "catalog",
                        null,
                        null);
        DockLayoutSnapshot.Node inspectorLeaf =
                new DockLayoutSnapshot.Node(
                        DockNode.Kind.LEAF,
                        null,
                        1.0f,
                        List.of("inspector"),
                        "inspector",
                        null,
                        null);
        DockLayoutSnapshot.Node centerRightSplit =
                new DockLayoutSnapshot.Node(
                        DockNode.Kind.SPLIT,
                        DockSplitOrientation.HORIZONTAL,
                        0.75f,
                        List.of(),
                        "",
                        centerDocs,
                        inspectorLeaf);
        DockLayoutSnapshot.Node hierarchyLeaf =
                new DockLayoutSnapshot.Node(
                        DockNode.Kind.LEAF,
                        null,
                        1.0f,
                        List.of("hierarchy"),
                        "hierarchy",
                        null,
                        null);
        DockLayoutSnapshot.Node rootNode =
                new DockLayoutSnapshot.Node(
                        DockNode.Kind.SPLIT,
                        DockSplitOrientation.HORIZONTAL,
                        0.20f,
                        List.of(),
                        "",
                        hierarchyLeaf,
                        centerRightSplit);
        return new DockLayoutSnapshot(rootNode, "catalog");
    }

    private static HBox buildTopToolbar() {
        HBox toolbar = new HBox();
        toolbar.spacing(8.0f);
        toolbar.layout(
                style ->
                        style.widthPercent(100.0f)
                                .height(24.0f)
                                .padding(6.0f, 0.0f, 6.0f, 0.0f)
                                .alignItems(Align.CENTER)
                                .flexGrow(0.0f)
                                .flexShrink(0.0f));

        // Иконка куба Unity / лого магазина
        CanvasWidget cubeIcon = new CanvasWidget();
        cubeIcon.layout(style -> style.size(16.0f, 16.0f).flexNone());
        cubeIcon.onDraw(
                context -> {
                    UnityIcons.drawProjectSceneCube(
                            new dev.sixik.unigui.api.render.DrawScope(
                                    context, cubeIcon.transform(), cubeIcon.layoutBounds()),
                            cubeIcon.layoutBounds().x(),
                            cubeIcon.layoutBounds().y(),
                            16.0f);
                });
        toolbar.addChild(cubeIcon);

        // Кнопка меню File
        Button fileBtn = new Button(I18n.get("shop.ui.editor.menu.file"));
        styleToolbarMenuButton(fileBtn);
        fileBtn.layout(style -> style.height(20.0f).flexNone());
        fileBtn.onClick(e -> openFileMenu(fileBtn));
        toolbar.addChild(fileBtn);

        // Кнопка меню Window
        Button windowBtn = new Button(I18n.get("shop.ui.editor.menu.window"));
        styleToolbarMenuButton(windowBtn);
        windowBtn.layout(style -> style.height(20.0f).flexNone());
        windowBtn.onClick(e -> openWindowMenu(windowBtn));
        toolbar.addChild(windowBtn);

        // Разделитель между меню и заголовком
        Box menuDivider = new Box();
        menuDivider.themeEnabled(false);
        menuDivider.backgroundVisible(true);
        menuDivider.background().set(ShopEditorStyles.BORDER_DEFAULT);
        menuDivider.borderVisible(false);
        menuDivider.layout(style -> style.width(1.0f).height(14.0f).margin(2.0f, 0.0f).flexNone());
        toolbar.addChild(menuDivider);

        ShopInstance shop = ShopEditorState.get().getDraftShop();
        String shopIdStr = shop != null && shop.getId() != null ? shop.getId().toString() : I18n.get("shop.ui.editor.shop_fallback");
        Label titleLbl = new Label(I18n.get("shop.ui.editor.title", shopIdStr));
        titleLbl.color().set(ShopEditorStyles.TEXT_BOLD);
        toolbar.addChild(titleLbl);

        Label changesBadge = new Label("");
        changesBadge.color().set(ShopEditorStyles.WARNING_YELLOW);
        toolbar.addChild(changesBadge);

        toolbarSubscription =
                ShopEditorState.get()
                        .subscribe(
                                () -> {
                                    int changes = ShopEditorState.get().getChangeCount();
                                    if (changes > 0) {
                                        changesBadge.text(
                                                changes > 1 ? I18n.get("shop.ui.editor.changes.many", String.valueOf(changes)) : I18n.get("shop.ui.editor.changes.one", String.valueOf(changes)));
                                    } else {
                                        changesBadge.text("");
                                    }
                                });

        // Распорка по центру
        Box spacer = new Box();
        spacer.layout(s -> s.expand());
        toolbar.addChild(spacer);

        // Кнопка Reset Session
        Button resetBtn = new Button(I18n.get("shop.ui.tool_panel.button.reset_session"));
        ShopEditorStyles.styleButton(resetBtn);
        resetBtn.layout(style -> style.height(20.0f).flexNone());
        resetBtn.onClick(e -> openResetSessionModal());
        toolbar.addChild(resetBtn);

        // Кнопка Send Changes (обычный цвет текста)
        Button sendChangesBtn = new Button(I18n.get("shop.ui.tool_panel.button.send_changes"));
        ShopEditorStyles.styleButton(sendChangesBtn);
        sendChangesBtn.textColor().set(ShopEditorStyles.TEXT_PRIMARY);
        sendChangesBtn.layout(style -> style.height(20.0f).flexNone());
        sendChangesBtn.onClick(e -> ShopEditorState.get().sendChanges());
        toolbar.addChild(sendChangesBtn);

        // Кнопка Close
        Button closeBtn = new Button(I18n.get("shop.ui.common.close"));
        ShopEditorStyles.styleButton(closeBtn);
        closeBtn.layout(style -> style.height(20.0f).flexNone());
        closeBtn.onClick(e -> closeEditor());
        toolbar.addChild(closeBtn);

        return toolbar;
    }

    private static void styleToolbarMenuButton(Button button) {
        ShopEditorStyles.styleButton(button);
        button.borderVisible(false);
        button.background().set(ShopEditorStyles.BG_WORKSPACE);
        button.textColor().set(ShopEditorStyles.TEXT_PRIMARY);
    }

    private static void openFileMenu(Button anchor) {
        if (activeMenu != null && lastOpenedMenuId == MenuId.FILE && activeMenu.opened()) {
            closeActiveMenu();
            return;
        }
        closeActiveMenu();

        float menuWidth = 160.0f;
        ContextMenu menu = new ContextMenu();
        ShopEditorStyles.styleContextMenu(menu);

        ShopEditorStyles.addContextMenuItem(
                menu,
                "Reset Session",
                true,
                menuWidth,
                () -> {
                    closeActiveMenu();
                    openResetSessionModal();
                });

        ShopEditorStyles.addContextMenuItem(
                menu,
                "Send Changes",
                true,
                menuWidth,
                () -> {
                    closeActiveMenu();
                    ShopEditorState.get().sendChanges();
                });

        ShopEditorStyles.addContextMenuSeparator(menu, menuWidth);

        ShopEditorStyles.addContextMenuItem(
                menu,
                "Close",
                true,
                menuWidth,
                () -> {
                    closeActiveMenu();
                    closeEditor();
                });

        float rootX = getRootX(anchor);
        float rootY = getRootY(anchor) + anchor.layoutBounds().height() + 2.0f;
        menu.openAt(rootX, rootY);
        if (activeOverlay != null) {
            activeOverlay.addOverlay(menu);
        }
        activeMenu = menu;
        lastOpenedMenuId = MenuId.FILE;
    }

    private static void openWindowMenu(Button anchor) {
        if (activeMenu != null && lastOpenedMenuId == MenuId.WINDOW && activeMenu.opened()) {
            closeActiveMenu();
            return;
        }
        closeActiveMenu();

        float menuWidth = 190.0f;
        ContextMenu menu = new ContextMenu();
        ShopEditorStyles.styleContextMenu(menu);

        addWindowMenuItem(menu, "Hierarchy", "hierarchy", menuWidth);
        addWindowMenuItem(menu, "Catalog", "catalog", menuWidth);
        addWindowMenuItem(menu, "Category", "category", menuWidth);
        addWindowMenuItem(menu, "Money", "money", menuWidth);
        addWindowMenuItem(menu, "Inspector", "inspector", menuWidth);

        ShopEditorStyles.addContextMenuSeparator(menu, menuWidth);

        ShopEditorStyles.addContextMenuItem(
                menu,
                "Restore Default Layout",
                true,
                menuWidth,
                () -> {
                    closeActiveMenu();
                    restoreDefaultLayout();
                });

        float rootX = getRootX(anchor);
        float rootY = getRootY(anchor) + anchor.layoutBounds().height() + 2.0f;
        menu.openAt(rootX, rootY);
        if (activeOverlay != null) {
            activeOverlay.addOverlay(menu);
        }
        activeMenu = menu;
        lastOpenedMenuId = MenuId.WINDOW;
    }

    private static void addWindowMenuItem(
            ContextMenu menu, String title, String paneId, float menuWidth) {
        boolean isOpen = dockingRoot != null && dockingRoot.findPane(paneId) != null;
        ShopEditorStyles.addToggleContextMenuItem(
                menu,
                title,
                isOpen,
                true,
                menuWidth,
                () -> {
                    closeActiveMenu();
                    togglePane(paneId);
                });
    }

    public static void togglePane(String paneId) {
        if (dockingRoot == null) return;
        if (dockingRoot.findPane(paneId) != null) {
            dockingRoot.manager().closePane(paneId);
        } else {
            restoreOrFocusPane(paneId);
        }
    }

    public static void navigateTo(Object target) {
        if (target == null) return;
        ShopEditorState.get().selectTarget(target);

        String tabId = null;
        if (target instanceof ShopOffer) {
            tabId = "catalog";
        } else if (target instanceof CatalogComponent) {
            tabId = "category";
        } else if (target instanceof dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency
                || target instanceof dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft) {
            tabId = "money";
        }

        if (tabId != null) {
            restoreOrFocusPane(tabId);
        }

        if (target instanceof ShopOffer offer && catalogView != null) {
            catalogView.scrollToOffer(offer);
        } else if (target instanceof CatalogComponent cat && categoryView != null) {
            categoryView.scrollToCategory(cat);
        } else if (moneyView != null) {
            moneyView.scrollToCurrency(target);
        }
    }

    public static void restoreOrFocusPane(String paneId) {
        if (dockingRoot == null) return;
        DockPane pane = paneRegistry.get(paneId);
        if (pane == null) return;

        if (dockingRoot.findPane(paneId) != null) {
            dockingRoot.selectPane(paneId);
            return;
        }

        try {
            switch (paneId) {
                case "hierarchy" -> {
                    String centerId = findAnyOpenPane("catalog", "category", "money", "inspector");
                    if (centerId != null) {
                        dockingRoot.splitPane(centerId, DockArea.LEFT, pane, 0.20f);
                    } else {
                        dockingRoot.addPane(pane);
                    }
                }
                case "inspector" -> {
                    String centerId = findAnyOpenPane("catalog", "category", "money", "hierarchy");
                    if (centerId != null) {
                        dockingRoot.splitPane(centerId, DockArea.RIGHT, pane, 0.25f);
                    } else {
                        dockingRoot.addPane(pane);
                    }
                }
                case "catalog", "category", "money" -> {
                    String existingDoc = findAnyOpenPane("catalog", "category", "money");
                    if (existingDoc != null) {
                        dockingRoot.tabPane(existingDoc, pane);
                    } else if (dockingRoot.findPane("hierarchy") != null) {
                        dockingRoot.splitPane("hierarchy", DockArea.RIGHT, pane, 0.75f);
                    } else if (dockingRoot.findPane("inspector") != null) {
                        dockingRoot.splitPane("inspector", DockArea.LEFT, pane, 0.75f);
                    } else {
                        dockingRoot.addPane(pane);
                    }
                }
                default -> dockingRoot.addPane(pane);
            }
        } catch (Exception e) {
            restoreDefaultLayout();
        }
        dockingRoot.selectPane(paneId);
    }

    private static String findAnyOpenPane(String... candidateIds) {
        if (dockingRoot == null) return null;
        for (String id : candidateIds) {
            if (dockingRoot.findPane(id) != null) {
                return id;
            }
        }
        return null;
    }

    public static void restoreDefaultLayout() {
        if (dockingRoot != null && !paneRegistry.isEmpty()) {
            dockingRoot.restoreLayout(buildDefaultLayoutSnapshot(), paneRegistry);
        }
    }

    private static float getRootX(Widget widget) {
        float x = 0.0f;
        Widget cur = widget;
        while (cur != null) {
            if (cur instanceof OverlayLayer) break;
            x += cur.layoutBounds().x();
            cur = cur.parent();
        }
        return x;
    }

    private static float getRootY(Widget widget) {
        float y = 0.0f;
        Widget cur = widget;
        while (cur != null) {
            if (cur instanceof OverlayLayer) break;
            y += cur.layoutBounds().y();
            cur = cur.parent();
        }
        return y;
    }
}
