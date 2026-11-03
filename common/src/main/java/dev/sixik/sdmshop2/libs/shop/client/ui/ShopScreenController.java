package dev.sixik.sdmshop2.libs.shop.client.ui;

import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopScreenElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopRootPanel;
import dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopToolPanel;
import dev.sixik.unigui.api.layout.FlexDirection;
import dev.sixik.unigui.api.screen.UniScreen;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.containers.WrapPanel;
import dev.sixik.unigui.widgets.core.Orientation;
import dev.sixik.unigui.widgets.interaction.Button;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.UUID;

public class ShopScreenController {
    private static ShopScreenElement[] ELEMENTS = {
        new ShopRootPanel(),
        new ShopToolPanel()
    };

    public static void openShop() {
        UniScreen screen = new UniScreen(root());
        screen.openGui();
    }

    private static Widget root() {
        // VBox для вертикального размещения: шапка сверху, контент под ней
        final VBox root = new VBox();
        root.layout(style -> style
                .fill()                              // Растягивает корень на 100% экрана
                .flexDirection(FlexDirection.COLUMN) // Сверху вниз
                .padding(4.0f)
                .gap(4.0f)                           // Зазор между тулбаром и нижней панелью
        );

        Arrays.stream(ELEMENTS)
                .sorted(Comparator.comparingInt(ShopScreenElement::priority))
                .forEach(element -> element.initWidget(root));

        return root;
    }

    public static void invokeRefreshCurrencies() {

    }

    public static void invokeFavoritesChanged(UUID offerId, boolean favorite, LinkedHashSet<UUID> uuids) { }

    public static void invokeRefreshOffers() {

    }

    public static void invokeRefreshAll() {

    }

    public static void invokeRefreshOffer(UUID entityId) {
    }
}
