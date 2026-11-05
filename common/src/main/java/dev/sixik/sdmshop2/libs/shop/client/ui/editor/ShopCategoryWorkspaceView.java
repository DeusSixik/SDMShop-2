package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUiSubscription;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityIcons;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopEditorStyles;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopTheme;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopItemWidget;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.unigui.api.core.FrameContext;
import dev.sixik.unigui.api.event.PointerPressedEvent;
import dev.sixik.unigui.api.input.PointerButton;
import dev.sixik.unigui.api.layout.*;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.GridBox;
import dev.sixik.unigui.widgets.containers.LinearBox;
import dev.sixik.unigui.widgets.containers.ScrollView;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.core.Orientation;
import dev.sixik.unigui.widgets.display.CanvasWidget;
import dev.sixik.unigui.widgets.display.ImageView;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.dnd.DragController;
import dev.sixik.unigui.widgets.dnd.DragPayload;
import dev.sixik.unigui.widgets.dnd.DropController;
import dev.sixik.unigui.widgets.feedback.ContextMenu;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.resources.language.I18n;

/** Центральная рабочая панель «Category» (список категорий магазина). */
public class ShopCategoryWorkspaceView extends LinearBox {
    /** Editor notification registration released with this view. */
    private final ShopUiSubscription stateSubscription;

    /** Releases editor notifications before disposing child widgets. */
    @Override
    public void dispose() {
        stateSubscription.close();
        super.dispose();
    }

    private final GridBox grid = new GridBox();
    private ScrollView scrollView;
    private final Map<CatalogComponent, Widget> categoryCards = new HashMap<>();
    private CatalogComponent pendingScrollCategory;
    private ContextMenu activeCategoryContextMenu;

    public ShopCategoryWorkspaceView() {
        super(Orientation.VERTICAL);
        spacing(0.0f);
        layout(style -> style.sizePercent(100.0f, 100.0f).flexGrow(1.0f).flexShrink(1.0f));

        grid.columns(3);
        grid.horizontalSpacing(8.0f);
        grid.verticalSpacing(8.0f);
        grid.layout(
                style ->
                        style.gridTemplateColumns(
                                        GridTrack.fr(1.0f), GridTrack.fr(1.0f), GridTrack.fr(1.0f))
                                .alignContent(AlignContent.START)
                                .gap(8.0f)
                                .widthPercent(100.0f)
                                .padding(8.0f));

        this.scrollView = new ScrollView(grid);
        this.scrollView.scrollStep(24.0f);
        this.scrollView.scrollbarGap(2.0f);
        this.scrollView.scrollbarTrackColor().set(ShopEditorStyles.BORDER_DEFAULT);
        this.scrollView.scrollbarThumbColor().set(ShopEditorStyles.TEXT_MUTED);
        this.scrollView.layout(
                style ->
                        style.sizePercent(100.0f, 100.0f)
                                .overflowX(Overflow.HIDDEN)
                                .overflowY(Overflow.AUTO)
                                .flexGrow(1.0f)
                                .flexShrink(1.0f));
        addChild(this.scrollView);

        stateSubscription = ShopEditorState.get().subscribe(this::refreshView);
        refreshView();
    }

    public void refreshView() {
        grid.clearChildren();
        categoryCards.clear();

        // 1. Карточка "+ Add New Category"
        VBox addCard = new VBox();
        addCard.spacing(4.0f);
        addCard.layout(
                style ->
                        style.height(96.0f)
                                .padding(6.0f)
                                .alignItems(Align.CENTER)
                                .justifyContent(Justify.CENTER)
                                .flexNone());
        addCard.themeEnabled(false);
        addCard.boxVisualEnabled(true);
        addCard.backgroundVisible(true);
        addCard.background(ShopEditorStyles.BG_PANEL);
        addCard.borderVisible(true);
        addCard.border(ShopEditorStyles.SELECTION_BLUE);
        addCard.borderWidth(1.0f);
        addCard.radius(ShopTheme.RADIUS_CARD);

        Label plus = new Label("+");
        plus.color(ShopEditorStyles.SELECTION_BLUE);
        Label addLbl = new Label(I18n.get("shop.ui.category_workspace.add"));
        addLbl.color(ShopEditorStyles.TEXT_PRIMARY);

        addCard.addChild(plus);
        addCard.addChild(addLbl);
        addCard.on(
                PointerPressedEvent.TYPE,
                event -> {
                    if (event.button() == PointerButton.PRIMARY) {
                        CatalogComponent newCat = ShopEditorState.get().createNewCategory();
                        ShopEditorState.get().selectTarget(newCat);
                        refreshView();
                        event.cancel();
                    }
                });

        // Drop на карточку добавления перемещает категорию на первое место (индекс 0)
        DropController.attach(addCard)
                .accepts("shop.category")
                .onDragEnter(
                        e -> {
                            addCard.border(ShopEditorStyles.SELECTION_BLUE);
                            addCard.borderWidth(2.0f);
                        })
                .onDragLeave(
                        e -> {
                            addCard.border(ShopEditorStyles.SELECTION_BLUE);
                            addCard.borderWidth(1.0f);
                        })
                .onDrop(
                        e -> {
                            DragPayload payload = e.payload();
                            if (payload != null && payload.matchesType("shop.category")) {
                                CatalogComponent draggedCat = payload.data(CatalogComponent.class);
                                if (draggedCat != null) {
                                    ShopEditorState.get().moveCategoryToIndex(draggedCat, 0);
                                    refreshView();
                                }
                            }
                        });

        grid.addChild(addCard);

        ShopInstance shop = ShopEditorState.get().getDraftShop();
        if (shop == null || shop.getCategories() == null) return;

        List<CatalogComponent> cats = shop.getCategories().getCatalogsComponents();
        if (cats == null) return;

        Object currentTarget = ShopEditorState.get().getSelectedTarget();

        for (CatalogComponent cat : cats) {
            VBox card = new VBox();
            card.spacing(3.0f);
            card.layout(
                    style ->
                            style.height(96.0f)
                                    .padding(6.0f)
                                    .alignItems(Align.CENTER)
                                    .justifyContent(Justify.CENTER)
                                    .flexNone());

            boolean isSelected = cat == currentTarget;
            card.themeEnabled(false);
            card.boxVisualEnabled(true);
            card.backgroundVisible(true);
            card.background(
                    isSelected ? ShopEditorStyles.BG_HEADER_HOVER : ShopEditorStyles.BG_PANEL);
            card.borderVisible(true);
            card.border(
                    isSelected ? ShopEditorStyles.SELECTION_BLUE : ShopEditorStyles.BORDER_DEFAULT);
            card.borderWidth(isSelected ? 2.0f : 1.0f);
            card.radius(ShopTheme.RADIUS_CARD);

            // Визуальная иконка категории
            if (cat.getIconType() == CatalogComponent.IconType.ITEM
                    && cat.getIconItem() != null
                    && !cat.getIconItem().isEmpty()) {
                ShopItemWidget itemSlot =
                        new ShopItemWidget(cat.getIconItem(), 20.0f).asSlot(24.0f, 24.0f);
                itemSlot.background().set(ShopEditorStyles.BG_INPUT);
                itemSlot.borderWidth(1.0f);
                itemSlot.radius(3.0f);
                itemSlot.layout(s -> s.fixed(24.0f, 24.0f).flexNone());
                card.addChild(itemSlot);
            } else if (cat.getIconType() == CatalogComponent.IconType.TEXTURE
                    && cat.getIconTexture() != null
                    && !CatalogComponent.EMPTY_ICON_TEXTURE.equals(cat.getIconTexture())) {
                String tex = cat.getIconTexture().toString();
                ImageView img =
                        new ImageView(
                                new dev.sixik.unigui.api.render.SimpleTextureHandle(
                                        tex, 24, 24, tex));
                img.layout(s -> s.size(24.0f, 24.0f).flexNone());
                card.addChild(img);
            } else {
                CanvasWidget cube =
                        UnityIcons.gameObjectIcon(20.0f, () -> UnityIcons.ShopObjectType.CATEGORY);
                cube.layout(s -> s.size(20.0f, 20.0f).flexNone());
                card.addChild(cube);
            }

            Label idLbl =
                    new Label(
                            cat.getId() != null && !cat.getId().isBlank()
                                    ? cat.getId()
                                    : "unnamed");
            idLbl.color(isSelected ? ShopEditorStyles.TEXT_BOLD : ShopEditorStyles.TEXT_PRIMARY);
            Label orderLbl = new Label(I18n.get("shop.ui.category_workspace.order", String.valueOf(cat.getOrder())));
            orderLbl.color(ShopEditorStyles.TEXT_MUTED);

            card.addChild(idLbl);
            card.addChild(orderLbl);

            // Drag and Drop для категорий
            String catName = cat.getId() != null ? cat.getId() : "Category";
            DragController.attach(card)
                    .dragged(card)
                    .payload(
                            () ->
                                    DragPayload.of(
                                            cat.getUuid() != null
                                                    ? cat.getUuid().toString()
                                                    : catName,
                                            "shop.category",
                                            catName,
                                            cat))
                    .threshold(4.0f)
                    .sourceOpacity(0.4f)
                    .ghost(card)
                    .ghostOpacity(0.8f)
                    .onDragStart(
                            session -> {
                                ShopEditorState.get().selectTarget(cat);
                            });

            DropController.attach(card)
                    .accepts("shop.category")
                    .onDragEnter(
                            e -> {
                                card.border(ShopEditorStyles.SELECTION_BLUE);
                                card.borderWidth(2.0f);
                            })
                    .onDragLeave(
                            e -> {
                                boolean selected = cat == ShopEditorState.get().getSelectedTarget();
                                if (selected) {
                                    card.border(ShopEditorStyles.SELECTION_BLUE);
                                    card.borderWidth(2.0f);
                                } else {
                                    card.border(ShopEditorStyles.BORDER_DEFAULT);
                                    card.borderWidth(1.0f);
                                }
                            })
                    .onDrop(
                            e -> {
                                DragPayload payload = e.payload();
                                if (payload != null && payload.matchesType("shop.category")) {
                                    CatalogComponent draggedCat =
                                            payload.data(CatalogComponent.class);
                                    if (draggedCat != null
                                            && !draggedCat.getUuid().equals(cat.getUuid())) {
                                        ShopInstance shopInstance =
                                                ShopEditorState.get().getDraftShop();
                                        if (shopInstance != null
                                                && shopInstance.getCategories() != null) {
                                            List<CatalogComponent> allCats =
                                                    shopInstance
                                                            .getCategories()
                                                            .getCatalogsComponents();
                                            int targetIdx = -1;
                                            for (int i = 0; i < allCats.size(); i++) {
                                                if (allCats.get(i)
                                                        .getUuid()
                                                        .equals(cat.getUuid())) {
                                                    targetIdx = i;
                                                    break;
                                                }
                                            }
                                            if (targetIdx >= 0) {
                                                ShopEditorState.get()
                                                        .moveCategoryToIndex(draggedCat, targetIdx);
                                                refreshView();
                                            }
                                        }
                                    }
                                }
                            });

            card.on(
                    PointerPressedEvent.TYPE,
                    event -> {
                        if (event.button() == PointerButton.PRIMARY) {
                            ShopEditorState.get().selectTarget(cat);
                            refreshView();
                            event.cancel();
                        } else if (event.button() == PointerButton.SECONDARY) {
                            ShopEditorState.get().selectTarget(cat);
                            openCategoryContextMenu(event.rootX(), event.rootY(), cat);
                            event.cancel();
                        }
                    });

            categoryCards.put(cat, card);
            grid.addChild(card);
        }
    }

    private void openCategoryContextMenu(float x, float y, CatalogComponent cat) {
        OverlayLayer layer = findOverlayLayer();
        if (layer == null) return;

        if (activeCategoryContextMenu != null) {
            layer.removeOverlay(activeCategoryContextMenu);
            activeCategoryContextMenu = null;
        }

        ShopInstance shop = ShopEditorState.get().getDraftShop();
        if (shop == null || shop.getCategories() == null) return;

        List<CatalogComponent> list = shop.getCategories().getCatalogsComponents();
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUuid().equals(cat.getUuid())) {
                index = i;
                break;
            }
        }
        int total = list.size();
        boolean canMoveUp = index > 0;
        boolean canMoveDown = index >= 0 && index < total - 1;

        float menuWidth = 140.0f;
        ContextMenu menu = new ContextMenu();
        ShopEditorStyles.styleContextMenu(menu);

        // 1. Move Up
        ShopEditorStyles.addContextMenuItem(
                menu,
                "Move Up",
                canMoveUp,
                menuWidth,
                () -> {
                    ShopEditorState.get().moveCategory(cat, -1);
                    refreshView();
                });

        // 2. Move Down
        ShopEditorStyles.addContextMenuItem(
                menu,
                "Move Down",
                canMoveDown,
                menuWidth,
                () -> {
                    ShopEditorState.get().moveCategory(cat, 1);
                    refreshView();
                });

        ShopEditorStyles.addContextMenuSeparator(menu, menuWidth);

        // 3. Duplicate
        ShopEditorStyles.addContextMenuItem(
                menu,
                "Duplicate",
                true,
                menuWidth,
                () -> {
                    ShopEditorState.get().duplicateCategory(cat);
                    refreshView();
                });

        ShopEditorStyles.addContextMenuSeparator(menu, menuWidth);

        // 4. Delete
        ShopEditorStyles.addContextMenuItem(
                menu,
                "Delete",
                true,
                menuWidth,
                () -> {
                    ShopEditorState.get().deleteCategory(cat);
                    refreshView();
                });

        menu.openAt(x, y);
        layer.addOverlay(menu);
        activeCategoryContextMenu = menu;
    }

    private OverlayLayer findOverlayLayer() {
        Widget current = this;
        while (current != null) {
            if (current instanceof OverlayLayer layer) return layer;
            current = current.parent();
        }
        return ShopEditorScreen.getActiveOverlay();
    }

    public void scrollToCategory(CatalogComponent cat) {
        if (cat == null) return;
        if (!scrollToCategoryInternal(cat)) {
            this.pendingScrollCategory = cat;
        }
    }

    private boolean scrollToCategoryInternal(CatalogComponent cat) {
        if (scrollView == null) return false;
        Widget card = categoryCards.get(cat);
        if (card != null) {
            float targetY = card.layoutBounds().y();
            if (targetY > 0.0f || card.layoutBounds().height() > 0.0f) {
                scrollView.scrollTo(0.0f, Math.max(0.0f, targetY - 16.0f));
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick(FrameContext context) {
        super.tick(context);
        if (pendingScrollCategory != null && scrollView != null) {
            if (scrollToCategoryInternal(pendingScrollCategory)) {
                pendingScrollCategory = null;
            }
        }
    }
}
