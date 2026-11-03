package dev.sixik.sdmshop2.libs.shop.client.ui.root;

import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopScreenElement;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.layout.GridTrack;
import dev.sixik.unigui.api.layout.Justify;
import dev.sixik.unigui.api.layout.Overflow;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.api.widget.visual.BackgroundKind;
import dev.sixik.unigui.widgets.containers.*;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.interaction.Button;
import dev.sixik.unigui.widgets.interaction.ScrollBar;

public class ShopRootPanel implements ShopScreenElement {

    protected final ShopCategoryPanel categoryPanel = new ShopCategoryPanel();

    @Override
    public Widget initWidget(PanelWidget root) {
        HBox mainContainer = new HBox();
        mainContainer.layout(style -> style
                .fill()
                .padding(0.0f)
                .gap(6.0f)
        );

        VBox leftSidebar = new VBox();
        leftSidebar.layout(style -> style
                .width(140.0f)
                .flexNone()
                .gap(6.0f)
        );

        VBox categoriesPanel = new VBox();
        categoriesPanel.layout(style -> style
                .expand()
                .padding(4.0f)
                .gap(4.0f)
                .overflowY(Overflow.AUTO)
        );
        categoriesPanel.boxVisualEnabled(true);

        categoriesPanel.backgroundVisible(true);
        categoriesPanel.backgroundKind(BackgroundKind.COLOR);
        categoriesPanel.background().set(0,0,0,1);

        categoriesPanel.addChild(createCategoryBtn("All"));
        categoriesPanel.addChild(createCategoryBtn("Resources"));
        categoriesPanel.addChild(createCategoryBtn("Building Blocks"));
        categoriesPanel.addChild(createCategoryBtn("Farming & Food"));

        ScrollView categoriesPanelScroll = new ScrollView(categoriesPanel)
                .scrollStep(18.0f)
                .scrollbarGap(2.0f);
        categoriesPanelScroll.layout(layout ->
                layout.expand());
        categoriesPanelScroll.overlayScrollBars(false);

        VBox moneyPanel = new VBox();
        moneyPanel.layout(style -> style
                .expand()
                .flexNone()
                .padding(6.0f)
                .gap(2.0f)
        );
        moneyPanel.boxVisualEnabled(true);
        moneyPanel.backgroundVisible(true);
        moneyPanel.backgroundKind(BackgroundKind.COLOR);
        moneyPanel.background().set(0,0,0,1);

        moneyPanel.addChild(new Label("Your money"));
        moneyPanel.addChild(createMoneyRow("Coin", "0.0"));
        moneyPanel.addChild(createMoneyRow("Diamond", "0"));
        moneyPanel.addChild(createMoneyRow("Emerald", "0"));
        moneyPanel.addChild(createMoneyRow("Gold Ingot", "0"));
        moneyPanel.addChild(createMoneyRow("Netherite Ingot", "0"));

        ScrollView moneyPanelScroll = new ScrollView(moneyPanel)
                .scrollStep(18.0f)
                .scrollbarGap(2.0f);
        moneyPanelScroll.layout(layout ->
                layout.heightPercent(40.0f));
        moneyPanelScroll.overlayScrollBars(false);

        leftSidebar.addChild(categoriesPanelScroll);
        leftSidebar.addChild(moneyPanelScroll);

        VBox catalogPanel = new VBox();
        catalogPanel.layout(style -> style
                .expand()
                .padding(4.0f)
                .overflowY(Overflow.AUTO)
        );
        catalogPanel.boxVisualEnabled(true);
        catalogPanel.backgroundVisible(true);
        catalogPanel.backgroundKind(BackgroundKind.COLOR);
        catalogPanel.background().set(0,0,0,1);

        var itemsGrid = new GridBox();
        itemsGrid.layout(style -> style
                .gridTemplateColumns(
                        GridTrack.fr(1.0f),
                        GridTrack.fr(1.0f)
                )
                .gap(6.0f)
        );

        itemsGrid.addChild(createOfferCard("Elytra"));
        itemsGrid.addChild(createOfferCard("Enchanted Golden Apple"));
        itemsGrid.addChild(createOfferCard("Gold Ingot"));
        itemsGrid.addChild(createOfferCard("Netherite Ingot"));

        catalogPanel.addChild(itemsGrid);

        mainContainer.addChild(leftSidebar);
        mainContainer.addChild(catalogPanel);

        root.addChild(mainContainer);
        return mainContainer;
    }

    // Хелпер для кнопки категории
    private Button createCategoryBtn(String title) {
        Button btn = new Button(title);
        btn.layout(style -> style
                .height(24.0f)
                .flexNone()
        );
        return btn;
    }

    // Хелпер для строки баланса
    private Widget createMoneyRow(String name, String value) {
        HBox row = new HBox();
        row.layout(style -> style
                .alignItems(Align.CENTER)
                .justifyContent(Justify.SPACE_BETWEEN)
                .height(16.0f)
                .flexNone()
        );
        row.addChild(new Label(name));
        row.addChild(new Label(value));
        return row;
    }

    // Хелпер карточки товара
    private Widget createOfferCard(String title) {
        VBox card = new VBox();
        card.layout(style -> style
                .height(100.0f)
                .flexNone()
                .padding(4.0f)
        );
        card.boxVisualEnabled(true);
        card.addChild(new Label(title));
        return card;
    }
}
