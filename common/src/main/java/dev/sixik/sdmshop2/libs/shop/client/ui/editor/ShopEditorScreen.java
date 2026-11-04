package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.client.SDMShopClient;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopScreenController;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityIcons;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityStyles;
import dev.sixik.unigui.api.core.UnityLikeUIScaleProvider;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.layout.Justify;
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
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.interaction.Button;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Полноэкранный редактор магазина SDMShop-2 в стиле Unity Editor.
 * Воспроизводит компоновку Unity:
 * - Слева: Hierarchy (поиск и дерево категорий, товаров, валют)
 * - По центру: 3 вкладки вместо Scene/Game — Catalog, Category, Money
 * - Справа: Inspector (компоненты, свойства и кнопка Add Component)
 */
public final class ShopEditorScreen {

    private static UniScreen currentScreen;
    private static OverlayLayer activeOverlay;
    private static ShopHierarchyView hierarchyView;

    private ShopEditorScreen() {}

    public static void open() {
        createScreen().openGui();
    }

    public static void close() {
        if (currentScreen != null) {
            currentScreen.closeGui();
            currentScreen = null;
        }
    }

    public static OverlayLayer getActiveOverlay() {
        return activeOverlay;
    }

    public static ShopHierarchyView getHierarchyView() {
        return hierarchyView;
    }

    public static UniScreen createScreen() {
        DefaultUIContext context = new DefaultUIContext();
        context.scaleProvider(new UnityLikeUIScaleProvider()
                .referenceResolution(1920.0f, 1080.0f)
                .matchBalanced()
                .scaleRange(0.75f, 6.0f)
                .userScale(1.8f));

        Widget root = buildRoot();
        currentScreen = new UniScreen("SDMShop Editor", root, context)
                .scaleWithGameGui(false)
                .renderPolicy(UiRenderPolicy.vsync())
                .defaultFont(Fonts.defaultFace());
        return currentScreen;
    }

    private static Widget buildRoot() {
        StackPanel viewport = new StackPanel();
        viewport.layout(style -> style.sizePercent(100.0f, 100.0f).overflow(Overflow.HIDDEN));

        // Фоновый слой #202020
        Box bg = new Box();
        bg.themeEnabled(false);
        bg.backgroundVisible(true);
        bg.background().set(UnityStyles.BG_WORKSPACE);
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
        ShopCatalogPreviewView catalogView = new ShopCatalogPreviewView();
        ShopCategoryWorkspaceView categoryView = new ShopCategoryWorkspaceView();
        ShopMoneyWorkspaceView moneyView = new ShopMoneyWorkspaceView();
        ShopInspectorView inspectorView = new ShopInspectorView(activeOverlay);

        // 2. Создание DockingRoot и настройка темы Unity
        DockingRoot dockingRoot = new DockingRoot();
        dockingRoot.layout(style -> style.widthPercent(100.0f).heightPercent(100.0f)
                .flexGrow(1.0f).flexShrink(1.0f));

        DockTheme theme = DockTheme.editor();
        theme.color(DockColor.STRIP_BACKGROUND).set(UnityStyles.BG_WORKSPACE);
        theme.color(DockColor.STRIP_BORDER).set(UnityStyles.BORDER_DEFAULT);
        theme.color(DockColor.TAB_BACKGROUND).set(UnityStyles.BG_TAB_INACTIVE);
        theme.color(DockColor.TAB_HOVER).set(UnityStyles.BG_TAB_ACTIVE);
        theme.color(DockColor.TAB_SELECTED).set(UnityStyles.BG_TAB_ACTIVE);
        theme.color(DockColor.TAB_SELECTED_INACTIVE).set(UnityStyles.BG_TAB_INACTIVE);
        theme.color(DockColor.TAB_ACCENT).set(UnityStyles.TAB_ACCENT);
        theme.color(DockColor.TAB_ACCENT_INACTIVE).set(UnityStyles.TAB_ACCENT);
        theme.color(DockColor.TAB_TEXT).set(UnityStyles.TEXT_PRIMARY);
        theme.color(DockColor.TAB_TEXT_MUTED).set(UnityStyles.TEXT_MUTED);
        theme.color(DockColor.CONTENT_BACKGROUND).set(UnityStyles.BG_WINDOW);
        theme.color(DockColor.PANE_BORDER).set(UnityStyles.BORDER_DEFAULT);
        theme.color(DockColor.SPLIT).set(UnityStyles.BORDER_DEFAULT);
        theme.color(DockColor.SPLIT_HOVER).set(UnityStyles.SELECTION_BLUE);
        theme.color(DockColor.SPLIT_PRESSED).set(UnityStyles.SELECTION_BLUE);
        theme.splitStyle(DockingRoot.SplitHandleStyle.SEAM);
        theme.splitThickness(3.0f);
        theme.tabHeight(22.0f);

        dockingRoot.theme(theme);
        dockingRoot.tabHeight(22.0f);
        dockingRoot.splitHandleThickness(3.0f);
        dockingRoot.splitHandleStyle(DockingRoot.SplitHandleStyle.SEAM);
        dockingRoot.tearOffOnDragOut(false);

        // Регистрация панелей
        DockPane hierarchyPane = DockPane.tool("hierarchy", "Hierarchy", hierarchyView)
                .icon(UnityIcons.HIERARCHY_TAB_ICON);
        DockPane catalogPane = DockPane.document("catalog", "Catalog", catalogView)
                .icon(UnityIcons.SCENE_TAB_ICON);
        DockPane categoryPane = DockPane.document("category", "Category", categoryView)
                .icon(UnityIcons.GAME_TAB_ICON);
        DockPane moneyPane = DockPane.document("money", "Money", moneyView)
                .icon(UnityIcons.CONSOLE_TAB_ICON);
        DockPane inspectorPane = DockPane.tool("inspector", "Inspector", inspectorView)
                .icon(UnityIcons.INSPECTOR_TAB_ICON);

        Map<String, DockPane> paneRegistry = new HashMap<>();
        paneRegistry.put("hierarchy", hierarchyPane);
        paneRegistry.put("catalog", catalogPane);
        paneRegistry.put("category", categoryPane);
        paneRegistry.put("money", moneyPane);
        paneRegistry.put("inspector", inspectorPane);

        dockingRoot.restoreLayout(buildDefaultLayoutSnapshot(), paneRegistry);

        // Связывание Hierarchy с переключением вкладок в центре
        hierarchyView.setOnTabSwitchRequested(tabId -> {
            if (dockingRoot.findPane(tabId) != null) {
                dockingRoot.selectPane(tabId);
            }
        });

        // 3. Верхний тулбар Unity
        editorRoot.addChild(buildTopToolbar());
        editorRoot.addChild(UnityStyles.separatorLine());

        // 4. Область докинга
        editorRoot.addChild(dockingRoot);

        activeOverlay.content(editorRoot);
        viewport.addChild(activeOverlay);

        return viewport;
    }

    private static DockLayoutSnapshot buildDefaultLayoutSnapshot() {
        DockLayoutSnapshot.Node centerDocs = new DockLayoutSnapshot.Node(
                DockNode.Kind.LEAF, null, 1.0f, List.of("catalog", "category", "money"), "catalog", null, null);
        DockLayoutSnapshot.Node inspectorLeaf = new DockLayoutSnapshot.Node(
                DockNode.Kind.LEAF, null, 1.0f, List.of("inspector"), "inspector", null, null);
        DockLayoutSnapshot.Node centerRightSplit = new DockLayoutSnapshot.Node(
                DockNode.Kind.SPLIT, DockSplitOrientation.HORIZONTAL, 0.75f, List.of(), "", centerDocs, inspectorLeaf);
        DockLayoutSnapshot.Node hierarchyLeaf = new DockLayoutSnapshot.Node(
                DockNode.Kind.LEAF, null, 1.0f, List.of("hierarchy"), "hierarchy", null, null);
        DockLayoutSnapshot.Node rootNode = new DockLayoutSnapshot.Node(
                DockNode.Kind.SPLIT, DockSplitOrientation.HORIZONTAL, 0.20f, List.of(), "", hierarchyLeaf, centerRightSplit);
        return new DockLayoutSnapshot(rootNode, "catalog");
    }

    private static HBox buildTopToolbar() {
        HBox toolbar = new HBox();
        toolbar.spacing(8.0f);
        toolbar.layout(style -> style.widthPercent(100.0f).height(24.0f)
                .padding(6.0f, 0.0f, 6.0f, 0.0f)
                .alignItems(Align.CENTER)
                .flexGrow(0.0f).flexShrink(0.0f));

        // Иконка куба Unity / лого магазина
        CanvasWidget cubeIcon = new CanvasWidget();
        cubeIcon.layout(style -> style.size(16.0f, 16.0f).flexNone());
        cubeIcon.onDraw(context -> {
            UnityIcons.drawProjectSceneCube(
                    new dev.sixik.unigui.api.render.DrawScope(context, cubeIcon.transform(), cubeIcon.layoutBounds()),
                    cubeIcon.layoutBounds().x(), cubeIcon.layoutBounds().y(), 16.0f);
        });
        toolbar.addChild(cubeIcon);

        ShopInstance shop = ShopEditorState.get().getDraftShop();
        String shopIdStr = shop != null && shop.getId() != null ? shop.getId().toString() : "Shop";
        Label titleLbl = new Label("SDMShop Editor — " + shopIdStr);
        titleLbl.color().set(UnityStyles.TEXT_BOLD);
        toolbar.addChild(titleLbl);

        Label changesBadge = new Label("");
        changesBadge.color().set(UnityStyles.WARNING_YELLOW);
        toolbar.addChild(changesBadge);

        ShopEditorState.get().addListener(() -> {
            int changes = ShopEditorState.get().getChangeCount();
            if (changes > 0) {
                changesBadge.text("• " + changes + " change" + (changes > 1 ? "s" : ""));
            } else {
                changesBadge.text("");
            }
        });

        // Распорка по центру
        Box spacer = new Box();
        spacer.layout(s -> s.expand());
        toolbar.addChild(spacer);

        // Кнопка Reset Session
        Button resetBtn = new Button("Reset Session");
        UnityStyles.styleButton(resetBtn);
        resetBtn.layout(style -> style.height(20.0f).flexNone());
        resetBtn.onClick(e -> {
            if (activeOverlay != null) {
                activeOverlay.addOverlay(new ShopResetSessionModal());
            } else {
                ShopScreenController.openModal(new ShopResetSessionModal());
            }
        });
        toolbar.addChild(resetBtn);

        // Кнопка Send Changes
        Button sendChangesBtn = new Button("Send Changes");
        UnityStyles.styleButton(sendChangesBtn);
        sendChangesBtn.textColor().set(UnityStyles.SELECTION_BLUE);
        sendChangesBtn.layout(style -> style.height(20.0f).flexNone());
        sendChangesBtn.onClick(e -> ShopEditorState.get().sendChanges());
        toolbar.addChild(sendChangesBtn);

        // Кнопка Back to Shop (View)
        Button backBtn = new Button("✕ Back to Shop");
        UnityStyles.styleButton(backBtn);
        backBtn.layout(style -> style.height(20.0f).flexNone());
        backBtn.onClick(e -> {
            close();
            ShopEditorState.get().exitEditor();
            ShopScreenController.openShop();
        });
        toolbar.addChild(backBtn);

        return toolbar;
    }
}
