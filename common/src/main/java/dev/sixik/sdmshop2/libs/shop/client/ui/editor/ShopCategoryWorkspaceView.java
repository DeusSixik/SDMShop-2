package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityStyles;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopTheme;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.unigui.api.event.PointerPressedEvent;
import dev.sixik.unigui.api.input.PointerButton;
import dev.sixik.unigui.api.layout.*;
import dev.sixik.unigui.widgets.containers.GridBox;
import dev.sixik.unigui.widgets.containers.LinearBox;
import dev.sixik.unigui.widgets.containers.ScrollView;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.core.Orientation;
import dev.sixik.unigui.widgets.display.Label;

import java.util.List;

/**
 * Центральная рабочая панель «Category» (список категорий магазина).
 */
public class ShopCategoryWorkspaceView extends LinearBox {

    private final GridBox grid = new GridBox();

    public ShopCategoryWorkspaceView() {
        super(Orientation.VERTICAL);
        spacing(0.0f);
        layout(style -> style.sizePercent(100.0f, 100.0f).flexGrow(1.0f).flexShrink(1.0f));

        grid.columns(3);
        grid.horizontalSpacing(8.0f);
        grid.verticalSpacing(8.0f);
        grid.layout(style -> style
                .gridTemplateColumns(GridTrack.fr(1.0f), GridTrack.fr(1.0f), GridTrack.fr(1.0f))
                .alignContent(AlignContent.START)
                .gap(8.0f)
                .widthPercent(100.0f)
                .padding(8.0f)
        );

        ScrollView scrollView = new ScrollView(grid);
        scrollView.scrollStep(24.0f);
        scrollView.scrollbarGap(2.0f);
        scrollView.scrollbarTrackColor().set(UnityStyles.BORDER_DEFAULT);
        scrollView.scrollbarThumbColor().set(UnityStyles.TEXT_MUTED);
        scrollView.layout(style -> style.sizePercent(100.0f, 100.0f)
                .overflowX(Overflow.HIDDEN).overflowY(Overflow.AUTO)
                .flexGrow(1.0f).flexShrink(1.0f));
        addChild(scrollView);

        ShopEditorState.get().addListener(this::refreshView);
        refreshView();
    }

    public void refreshView() {
        grid.clearChildren();

        // 1. Карточка "+ Add New Category"
        VBox addCard = new VBox();
        addCard.spacing(4.0f);
        addCard.layout(style -> style
                .height(84.0f)
                .padding(8.0f)
                .alignItems(Align.CENTER)
                .justifyContent(Justify.CENTER)
                .flexNone()
        );
        addCard.boxVisualEnabled(true);
        addCard.backgroundVisible(true);
        addCard.background(UnityStyles.BG_PANEL);
        addCard.borderVisible(true);
        addCard.border(UnityStyles.SELECTION_BLUE);
        addCard.borderWidth(1.0f);
        addCard.radius(ShopTheme.RADIUS_CARD);

        Label plus = new Label("+");
        plus.color(UnityStyles.SELECTION_BLUE);
        Label addLbl = new Label("Add New Category");
        addLbl.color(UnityStyles.TEXT_PRIMARY);

        addCard.addChild(plus);
        addCard.addChild(addLbl);
        addCard.on(PointerPressedEvent.TYPE, event -> {
            if (event.button() == PointerButton.PRIMARY) {
                CatalogComponent newCat = ShopEditorState.get().createNewCategory();
                ShopEditorState.get().selectTarget(newCat);
                refreshView();
                event.cancel();
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
            card.layout(style -> style
                    .height(84.0f)
                    .padding(8.0f)
                    .alignItems(Align.CENTER)
                    .justifyContent(Justify.CENTER)
                    .flexNone()
            );

            boolean isSelected = cat == currentTarget;
            card.boxVisualEnabled(true);
            card.backgroundVisible(true);
            card.background(isSelected ? UnityStyles.BG_HEADER_HOVER : UnityStyles.BG_PANEL);
            card.borderVisible(true);
            card.border(isSelected ? UnityStyles.SELECTION_BLUE : UnityStyles.BORDER_DEFAULT);
            card.borderWidth(isSelected ? 2.0f : 1.0f);
            card.radius(ShopTheme.RADIUS_CARD);

            Label idLbl = new Label(cat.getId() != null ? cat.getId() : "unnamed");
            idLbl.color(isSelected ? UnityStyles.TEXT_BOLD : UnityStyles.TEXT_PRIMARY);
            Label orderLbl = new Label("Order: " + cat.getOrder());
            orderLbl.color(UnityStyles.TEXT_MUTED);

            card.addChild(idLbl);
            card.addChild(orderLbl);

            card.on(PointerPressedEvent.TYPE, event -> {
                if (event.button() == PointerButton.PRIMARY) {
                    ShopEditorState.get().selectTarget(cat);
                    refreshView();
                    event.cancel();
                }
            });

            grid.addChild(card);
        }
    }
}
