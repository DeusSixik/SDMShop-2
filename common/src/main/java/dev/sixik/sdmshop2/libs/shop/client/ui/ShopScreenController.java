package dev.sixik.sdmshop2.libs.shop.client.ui;

import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUiContext;
import dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopRootPanel;
import dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopToolPanel;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;

import java.util.LinkedHashSet;
import java.util.UUID;

/** Compatibility entry point for network/cache callbacks and the default shop window. */
public final class ShopScreenController {
    /** Default window; each open replaces it with a fresh instance. */
    private static ShopScreen current = new ShopScreen();

    /** Prevents instantiation. */
    private ShopScreenController() {}

    /**
     * @return actions for the default shop window.
     */
    public static ShopUiContext context() {
        return current;
    }

    /**
     * @return the default window's catalog.
     */
    public static ShopRootPanel getRootPanel() {
        return current.rootPanel();
    }

    /**
     * @return the default window's toolbar.
     */
    public static ShopToolPanel getToolPanel() {
        return current.toolPanel();
    }

    /** Opens a fresh default window. */
    public static void openShop() {
        current.close();
        current = new ShopScreen();
        current.open();
    }

    /**
     * @return default window's overlay, or null while unmounted.
     */
    public static OverlayLayer getActiveOverlay() {
        return current.overlay();
    }

    /**
     * @param modal widget to show; null is ignored.
     */
    public static void openModal(Widget modal) {
        current.openModal(modal);
    }

    /**
     * @param modal widget to remove; null is ignored.
     */
    public static void closeModal(Widget modal) {
        current.closeModal(modal);
    }

    /**
     * @param query search text; null clears search.
     */
    public static void onSearchQueryChanged(String query) {
        current.search(query);
    }

    /** Toggles favorite-first ordering and updates the toolbar. */
    public static void toggleFavoritesFirst() {
        current.toggleFavorites();
    }

    /** Refreshes visible currency balances. */
    public static void invokeRefreshCurrencies() {
        getRootPanel().refreshCurrencies();
    }

    /**
     * Updates the visible favorite state.
     *
     * @param offerId affected offer identifier.
     * @param favorite new favorite state.
     * @param uuids cache snapshot retained for compatibility.
     */
    public static void invokeFavoritesChanged(
            UUID offerId, boolean favorite, LinkedHashSet<UUID> uuids) {
        getRootPanel().onFavoritesChanged(offerId, favorite);
    }

    /** Refreshes the visible catalog. */
    public static void invokeRefreshOffers() {
        getRootPanel().refreshOffers();
    }

    /** Refreshes categories, catalog and balances. */
    public static void invokeRefreshAll() {
        getRootPanel().buildCategoriesList();
        getRootPanel().refreshOffers();
        getRootPanel().refreshCurrencies();
    }

    /**
     * @param entityId changed offer identifier.
     */
    public static void invokeRefreshOffer(UUID entityId) {
        getRootPanel().refreshOffer(entityId);
    }
}
