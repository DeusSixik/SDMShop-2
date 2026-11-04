package dev.sixik.sdmshop2.libs.shop.client.ui;

import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopScreenElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopRootPanel;
import dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopToolPanel;
import dev.sixik.unigui.api.layout.FlexDirection;
import dev.sixik.unigui.api.screen.UniScreen;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.VBox;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.UUID;

public class ShopScreenController {

    private static OverlayLayer activeOverlay;

    private static final ShopRootPanel ROOT_PANEL = new ShopRootPanel();
    private static final ShopToolPanel TOOL_PANEL = new ShopToolPanel();

    private static final ShopScreenElement[] ELEMENTS = {
        ROOT_PANEL,
        TOOL_PANEL
    };

    public static ShopRootPanel getRootPanel() {
        return ROOT_PANEL;
    }

    public static ShopToolPanel getToolPanel() {
        return TOOL_PANEL;
    }

    public static void openShop() {
        UniScreen screen = new UniScreen(root());
        screen.openGui();
    }

    private static Widget root() {
        // VBox для вертикального размещения: шапка сверху, контент под ней
        final VBox mainLayout = new VBox();
        mainLayout.layout(style -> style
                .fill()                              // Растягивает корень на 100% экрана
                .flexDirection(FlexDirection.COLUMN) // Сверху вниз
                .padding(4.0f)
                .gap(4.0f)                           // Зазор между тулбаром и нижней панелью
        );

        // activeOverlay инициализируется ДО элементов, чтобы тултипы и оверлеи могли безопасно регистрироваться
        activeOverlay = new OverlayLayer(mainLayout);

        Arrays.stream(ELEMENTS)
                .sorted(Comparator.comparingInt(ShopScreenElement::priority))
                .forEach(element -> element.initWidget(mainLayout));

        return activeOverlay;
    }

    public static OverlayLayer getActiveOverlay() {
        return activeOverlay;
    }

    public static void openModal(Widget modal) {
        if (activeOverlay != null && modal != null) {
            activeOverlay.addOverlay(modal);
        }
    }

    public static void closeModal(Widget modal) {
        if (activeOverlay != null && modal != null) {
            activeOverlay.removeOverlay(modal);
        }
    }

    public static void onSearchQueryChanged(String query) {
        ROOT_PANEL.setSearchQuery(query);
    }

    public static void toggleFavoritesFirst() {
        boolean active = ROOT_PANEL.toggleFavoritesFirst();
        TOOL_PANEL.setFavoritesActive(active);
    }

    public static void invokeRefreshCurrencies() {
        ROOT_PANEL.refreshCurrencies();
    }

    public static void invokeFavoritesChanged(UUID offerId, boolean favorite, LinkedHashSet<UUID> uuids) {
        ROOT_PANEL.onFavoritesChanged(offerId, favorite);
    }

    public static void invokeRefreshOffers() {
        ROOT_PANEL.refreshOffers();
    }

    public static void invokeRefreshAll() {
        ROOT_PANEL.refreshOffers();
        ROOT_PANEL.refreshCurrencies();
    }

    public static void invokeRefreshOffer(UUID entityId) {
        ROOT_PANEL.refreshOffer(entityId);
    }
}
