package dev.sixik.sdmshop2.libs.shop.client.ui.api;

/** Screen-local actions available to contributed UI elements. */
public interface ShopUiContext extends ShopModalHost {
    /**
     * Updates catalog search.
     *
     * @param query user input; null clears the search.
     */
    void search(String query);

    /** Toggles stable favorite-first ordering. */
    void toggleFavorites();
}
