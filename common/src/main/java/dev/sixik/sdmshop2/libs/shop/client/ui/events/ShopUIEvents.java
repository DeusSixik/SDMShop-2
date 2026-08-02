package dev.sixik.sdmshop2.libs.shop.client.ui.events;

import dev.sixik.sdmshop2.libs.platform.utils.eventbus.DODEventBus;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventPtr;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventPtrInvoker;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopOffersPanelElement;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class ShopUIEvents {

    private static final EventPtrInvoker<OnSearchUpdate> SEARCH_UPDATE_INVOKER =
            new EventPtrInvoker<>(new DODEventBus.EventType<>("UI.SEARCH_UPDATE"));
    private static final EventPtrInvoker<FavoritesChanged> FAVORITES_CHANGED_INVOKER =
            new EventPtrInvoker<>(new DODEventBus.EventType<>("UI.FAVORITES_CHANGED"));
    private static final EventPtrInvoker<OnBuyShopEntity> BUY_SHOP_ENTITY_INVOKER =
            new EventPtrInvoker<>(new DODEventBus.EventType<>("UI.BUY_ENTRY"));
    private static final EventPtrInvoker<SortShopOffers> SORT_SHOP_OFFERS_INVOKER =
            new EventPtrInvoker<>(new DODEventBus.EventType<>("UI.SORT_SHOP_OFFERS"));
    private static final EventPtrInvoker<SelectCategory> SELECT_CATEGORY_INVOKER =
            new EventPtrInvoker<>(new DODEventBus.EventType<>("UI.SELECT_CATEGORY"));

    public static final EventPtr<OnSearchUpdate> SEARCH_UPDATE = SEARCH_UPDATE_INVOKER;
    public static final EventPtr<FavoritesChanged> FAVORITES_CHANGED = FAVORITES_CHANGED_INVOKER;
    public static final EventPtr<OnBuyShopEntity> BUY_SHOP_ENTITY = BUY_SHOP_ENTITY_INVOKER;
    public static final EventPtr<SortShopOffers> SORT_SHOP_OFFERS = SORT_SHOP_OFFERS_INVOKER;
    public static final EventPtr<SelectCategory> SELECT_CATEGORY = SELECT_CATEGORY_INVOKER;

    public static void invokeSearchUpdate(String text) {
        SEARCH_UPDATE_INVOKER.invoke(new OnSearchUpdate(text));
    }

    public static void invokeFavoritesChanged(UUID offerId, boolean favorite, Set<UUID> favorites) {
        FAVORITES_CHANGED_INVOKER.invoke(new FavoritesChanged(offerId, favorite, Set.copyOf(favorites)));
    }

    public static void invokeBuyShopEntity(@NotNull ShopEntity entity, String money_group) {
        BUY_SHOP_ENTITY_INVOKER.invoke(new OnBuyShopEntity(entity, money_group));
    }

    public static void invokeSortShopOffers(
            @NotNull ShopOffersPanelElement panel,
            @NotNull List<ShopOffer> offers,
            String searchText
    ) {
        SORT_SHOP_OFFERS_INVOKER.invoke(new SortShopOffers(panel, offers, searchText == null ? "" : searchText));
    }

    public static void invokeSelectCategory(CatalogComponent selected) {
        SELECT_CATEGORY_INVOKER.invoke(new SelectCategory(selected));
    }

    public record OnSearchUpdate(String text) { }

    public record FavoritesChanged(UUID offerId, boolean favorite, Set<UUID> favorites) { }

    public record OnBuyShopEntity(@NotNull ShopEntity entity, String money_group) { }

    /**
     * Mutable event fired after the default offers panel filtering and title sorting.
     * Listeners can reorder {@link #offers()} in-place before widgets are created.
     */
    public record SortShopOffers(
            @NotNull ShopOffersPanelElement panel,
            @NotNull List<ShopOffer> offers,
            String searchText
    ) {
        public void sort(Comparator<ShopOffer> comparator) {
            offers.sort(comparator);
        }
    }

    private ShopUIEvents() {
    }

    public record SelectCategory(CatalogComponent selected) {

    }
}
