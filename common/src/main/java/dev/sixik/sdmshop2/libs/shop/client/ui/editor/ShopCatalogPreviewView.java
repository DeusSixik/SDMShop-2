package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityStyles;
import dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopCatalogElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopTheme;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.unigui.api.event.PointerPressedEvent;
import dev.sixik.unigui.api.input.PointerButton;
import dev.sixik.unigui.api.layout.*;
import dev.sixik.unigui.widgets.containers.GridBox;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.LinearBox;
import dev.sixik.unigui.widgets.containers.ScrollView;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.core.Orientation;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.interaction.TextInput;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Центральная рабочая панель «Catalog» (предпросмотр офферов магазина).
 */
public class ShopCatalogPreviewView extends LinearBox {

    private final GridBox itemsGrid = new GridBox();
    private final List<ShopCatalogElement> currentCards = new ArrayList<>();
    private String searchQuery = "";
    private String selectedCategoryId = "ALL";

    public ShopCatalogPreviewView() {
        super(Orientation.VERTICAL);
        spacing(0.0f);
        layout(style -> style.sizePercent(100.0f, 100.0f).flexGrow(1.0f).flexShrink(1.0f));

        // Верхняя панель фильтрации
        HBox topBar = new HBox();
        topBar.spacing(6.0f);
        topBar.layout(style -> style.widthPercent(100.0f).height(26.0f)
                .padding(4.0f, 6.0f).alignItems(Align.CENTER).flexGrow(0.0f).flexShrink(0.0f));

        TextInput filterInput = new TextInput("");
        filterInput.placeholder("Filter offers...");
        UnityStyles.styleTextInput(filterInput);
        filterInput.layout(style -> style.width(220.0f).height(20.0f).flexNone());
        filterInput.onTextChanged(e -> {
            this.searchQuery = e.newText().trim().toLowerCase(Locale.ROOT);
            refreshGrid();
        });
        topBar.addChild(filterInput);

        addChild(topBar);
        addChild(UnityStyles.separatorLine());

        // Сетка предложений
        itemsGrid.columns(3);
        itemsGrid.horizontalSpacing(8.0f);
        itemsGrid.verticalSpacing(8.0f);
        itemsGrid.layout(style -> style
                .gridTemplateColumns(GridTrack.fr(1.0f), GridTrack.fr(1.0f), GridTrack.fr(1.0f))
                .alignContent(AlignContent.START)
                .gap(8.0f)
                .widthPercent(100.0f)
                .padding(8.0f)
        );

        ScrollView scrollView = new ScrollView(itemsGrid);
        scrollView.scrollStep(24.0f);
        scrollView.scrollbarGap(2.0f);
        scrollView.scrollbarTrackColor().set(UnityStyles.BORDER_DEFAULT);
        scrollView.scrollbarThumbColor().set(UnityStyles.TEXT_MUTED);
        scrollView.layout(style -> style.sizePercent(100.0f, 100.0f)
                .overflowX(Overflow.HIDDEN).overflowY(Overflow.AUTO)
                .flexGrow(1.0f).flexShrink(1.0f));
        addChild(scrollView);

        ShopEditorState.get().addListener(this::refreshGrid);
        refreshGrid();
    }

    public void refreshGrid() {
        itemsGrid.clearChildren();
        currentCards.clear();

        // 1. Первая карточка — "+ Add New Offer"
        itemsGrid.addChild(createAddOfferCard());

        ShopInstance shop = ShopEditorState.get().getDraftShop();
        if (shop == null || shop.getEntries() == null) return;

        Map<UUID, ShopOffer> map = shop.getEntries().getEntryMap();
        if (map == null || map.isEmpty()) return;

        Object currentTarget = ShopEditorState.get().getSelectedTarget();

        for (ShopOffer offer : map.values()) {
            String title = ShopCatalogElement.resolveTitle(offer);
            if (!searchQuery.isEmpty()) {
                boolean matches = title.toLowerCase(Locale.ROOT).contains(searchQuery);
                if (!matches && offer.getUUID() != null) {
                    matches = offer.getUUID().toString().toLowerCase(Locale.ROOT).contains(searchQuery);
                }
                if (!matches) continue;
            }

            ShopCatalogElement card = new ShopCatalogElement(offer, c -> {
                ShopEditorState.get().selectTarget(offer);
            });

            boolean isSelected = offer == currentTarget;
            card.setSelected(isSelected);
            if (isSelected) {
                card.border(UnityStyles.SELECTION_BLUE);
                card.borderWidth(2.0f);
            }

            currentCards.add(card);
            itemsGrid.addChild(card);
        }
    }

    private VBox createAddOfferCard() {
        VBox card = new VBox();
        card.spacing(4.0f);
        card.layout(style -> style
                .height(96.0f)
                .flexNone()
                .padding(6.0f, 8.0f)
                .alignItems(Align.CENTER)
                .justifyContent(Justify.CENTER)
        );

        card.boxVisualEnabled(true);
        card.backgroundVisible(true);
        card.background(UnityStyles.BG_PANEL);
        card.borderVisible(true);
        card.border(UnityStyles.SELECTION_BLUE);
        card.borderWidth(1.0f);
        card.radius(ShopTheme.RADIUS_CARD);

        Label plus = new Label("+");
        plus.color(UnityStyles.SELECTION_BLUE);
        Label addLbl = new Label("Add New Offer");
        addLbl.color(UnityStyles.TEXT_PRIMARY);

        card.addChild(plus);
        card.addChild(addLbl);

        card.on(PointerPressedEvent.TYPE, event -> {
            if (event.button() == PointerButton.PRIMARY) {
                ShopOffer newOffer = ShopEditorState.get().createNewOffer();
                ShopEditorState.get().selectTarget(newOffer);
                refreshGrid();
                event.cancel();
            }
        });

        return card;
    }
}
