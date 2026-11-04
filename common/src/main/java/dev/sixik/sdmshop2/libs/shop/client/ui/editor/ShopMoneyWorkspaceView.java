package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityStyles;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopTheme;
import dev.sixik.unigui.api.event.PointerPressedEvent;
import dev.sixik.unigui.api.input.PointerButton;
import dev.sixik.unigui.api.layout.*;
import dev.sixik.unigui.widgets.containers.GridBox;
import dev.sixik.unigui.widgets.containers.LinearBox;
import dev.sixik.unigui.widgets.containers.ScrollView;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.core.Orientation;
import dev.sixik.unigui.widgets.display.Label;

/**
 * Центральная рабочая панель «Money» (список валют магазина).
 */
public class ShopMoneyWorkspaceView extends LinearBox {

    private final GridBox grid = new GridBox();

    public ShopMoneyWorkspaceView() {
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

        // 1. Карточка "+ Add New Currency"
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
        Label addLbl = new Label("Add New Currency");
        addLbl.color(UnityStyles.TEXT_PRIMARY);

        addCard.addChild(plus);
        addCard.addChild(addLbl);
        addCard.on(PointerPressedEvent.TYPE, event -> {
            if (event.button() == PointerButton.PRIMARY) {
                CurrencyDraft draft = ShopEditorState.get().createNewCurrency();
                ShopEditorState.get().selectTarget(draft);
                refreshView();
                event.cancel();
            }
        });
        grid.addChild(addCard);

        Object currentTarget = ShopEditorState.get().getSelectedTarget();

        // 2. Черновики валют сессии
        if (ShopEditorState.get().getSession() != null) {
            for (CurrencyDraft draft : ShopEditorState.get().getSession().currencyDrafts()) {
                VBox card = new VBox();
                card.spacing(3.0f);
                card.layout(style -> style
                        .height(84.0f)
                        .padding(8.0f)
                        .alignItems(Align.CENTER)
                        .justifyContent(Justify.CENTER)
                        .flexNone()
                );

                boolean isSelected = draft == currentTarget;
                card.boxVisualEnabled(true);
                card.backgroundVisible(true);
                card.background(isSelected ? UnityStyles.BG_HEADER_HOVER : UnityStyles.BG_PANEL);
                card.borderVisible(true);
                card.border(isSelected ? UnityStyles.SELECTION_BLUE : UnityStyles.BORDER_DEFAULT);
                card.borderWidth(isSelected ? 2.0f : 1.0f);
                card.radius(ShopTheme.RADIUS_CARD);

                Label nameLbl = new Label(draft.displayName() + " [Draft]");
                nameLbl.color(isSelected ? UnityStyles.TEXT_BOLD : UnityStyles.TEXT_PRIMARY);
                Label idLbl = new Label(draft.id().toString() + " (" + draft.iconText() + ")");
                idLbl.color(UnityStyles.TEXT_MUTED);

                card.addChild(nameLbl);
                card.addChild(idLbl);

                card.on(PointerPressedEvent.TYPE, event -> {
                    if (event.button() == PointerButton.PRIMARY) {
                        ShopEditorState.get().selectTarget(draft);
                        refreshView();
                        event.cancel();
                    }
                });

                grid.addChild(card);
            }
        }

        // 3. Существующие валюты
        for (ICurrency cur : SDMEconomyServiceClient.getAllCurrencies().values()) {
            VBox card = new VBox();
            card.spacing(3.0f);
            card.layout(style -> style
                    .height(84.0f)
                    .padding(8.0f)
                    .alignItems(Align.CENTER)
                    .justifyContent(Justify.CENTER)
                    .flexNone()
            );

            boolean isSelected = cur == currentTarget;
            card.boxVisualEnabled(true);
            card.backgroundVisible(true);
            card.background(isSelected ? UnityStyles.BG_HEADER_HOVER : UnityStyles.BG_PANEL);
            card.borderVisible(true);
            card.border(isSelected ? UnityStyles.SELECTION_BLUE : UnityStyles.BORDER_DEFAULT);
            card.borderWidth(isSelected ? 2.0f : 1.0f);
            card.radius(ShopTheme.RADIUS_CARD);

            Label nameLbl = new Label(cur.getDisplayName().getString());
            nameLbl.color(isSelected ? UnityStyles.TEXT_BOLD : UnityStyles.TEXT_PRIMARY);
            Label idLbl = new Label(cur.getId().toString());
            idLbl.color(UnityStyles.TEXT_MUTED);

            card.addChild(nameLbl);
            card.addChild(idLbl);

            card.on(PointerPressedEvent.TYPE, event -> {
                if (event.button() == PointerButton.PRIMARY) {
                    ShopEditorState.get().selectTarget(cur);
                    refreshView();
                    event.cancel();
                }
            });

            grid.addChild(card);
        }
    }
}
