package dev.sixik.sdmshop2.libs.shop.client.ui.events;

import dev.sixik.sdmshop2.libs.platform.utils.eventbus.DODEventBus;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventPtr;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventPtrInvoker;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopOffersPanelElement;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    private static final EventPtrInvoker<RefreshUI> REFRESH_UI_INVOKER =
            new EventPtrInvoker<>(new DODEventBus.EventType<>("UI.REFRESH"));

    public static final EventPtr<OnSearchUpdate> SEARCH_UPDATE = SEARCH_UPDATE_INVOKER;
    public static final EventPtr<FavoritesChanged> FAVORITES_CHANGED = FAVORITES_CHANGED_INVOKER;
    public static final EventPtr<OnBuyShopEntity> BUY_SHOP_ENTITY = BUY_SHOP_ENTITY_INVOKER;
    public static final EventPtr<SortShopOffers> SORT_SHOP_OFFERS = SORT_SHOP_OFFERS_INVOKER;
    public static final EventPtr<SelectCategory> SELECT_CATEGORY = SELECT_CATEGORY_INVOKER;
    public static final EventPtr<RefreshUI> REFRESH_UI = REFRESH_UI_INVOKER;

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

    public static void invokeRefresh() {
        invokeRefreshAll();
    }

    public static void invokeRefreshAll() {
        invokeRefresh(RefreshTarget.ALL);
    }

    public static void invokeRefreshCurrencies() {
        invokeRefresh(RefreshTarget.CURRENCIES);
    }

    public static void invokeRefreshInventoryCurrencies() {
        invokeRefresh(RefreshTarget.INVENTORY_CURRENCIES);
    }

    public static void invokeRefreshPrices() {
        invokeRefreshOffers();
    }

    public static void invokeRefreshOffers() {
        invokeRefresh(RefreshTarget.OFFERS);
    }

    public static void invokeRefreshCategories() {
        invokeRefresh(RefreshTarget.CATEGORIES);
    }

    public static void invokeRefreshCategory(@Nullable CatalogComponent category) {
        invokeRefreshCategory(category == null ? null : category.getUuid());
    }

    public static void invokeRefreshCategory(@Nullable UUID categoryId) {
        invokeRefresh(new RefreshUI(RefreshTarget.CATEGORY, categoryId, null, true));
    }

    public static void invokeRefreshOffer(@Nullable UUID offerId) {
        invokeRefresh(new RefreshUI(RefreshTarget.OFFER, null, offerId, true));
    }

    public static void invokeRefresh(@NotNull RefreshTarget target) {
        invokeRefresh(new RefreshUI(target, null, null, true));
    }

    public static void invokeRefresh(@NotNull RefreshUI event) {
        REFRESH_UI_INVOKER.invoke(event);
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

    public enum RefreshTarget {
        ALL,
        CURRENCIES,
        INVENTORY_CURRENCIES,
        OFFERS,
        CATEGORIES,
        CATEGORY,
        OFFER
    }

    public record RefreshUI(
            @NotNull RefreshTarget target,
            @Nullable UUID categoryId,
            @Nullable UUID offerId,
            boolean relayout
    ) {
        public boolean affectsCurrencies() {
            return target == RefreshTarget.ALL
                    || target == RefreshTarget.CURRENCIES
                    || target == RefreshTarget.INVENTORY_CURRENCIES;
        }

        public boolean affectsPrices() {
            return affectsOffers();
        }

        public boolean affectsOffers() {
            return target == RefreshTarget.ALL
                    || target == RefreshTarget.INVENTORY_CURRENCIES
                    || target == RefreshTarget.OFFERS
                    || target == RefreshTarget.CATEGORIES
                    || target == RefreshTarget.CATEGORY
                    || target == RefreshTarget.OFFER;
        }

        public boolean affectsCategories() {
            return target == RefreshTarget.ALL
                    || target == RefreshTarget.CATEGORIES
                    || target == RefreshTarget.CATEGORY
                    || target == RefreshTarget.OFFER;
        }

        public boolean affectsOffer(@Nullable UUID id) {
            if (!affectsOffers()) {
                return false;
            }
            if (target != RefreshTarget.OFFER) {
                return true;
            }
            return id != null && id.equals(offerId);
        }

        public boolean affectsCategory(@Nullable CatalogComponent category) {
            if (target == RefreshTarget.ALL || target == RefreshTarget.OFFERS || target == RefreshTarget.CATEGORIES) {
                return true;
            }
            if (target != RefreshTarget.CATEGORY) {
                return false;
            }
            if (categoryId == null) {
                return category == null;
            }
            return category != null && categoryId.equals(category.getUuid());
        }
    }
}
