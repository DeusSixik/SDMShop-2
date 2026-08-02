package dev.sixik.sdmshop2.libs.shop.client.ui.events;

import dev.sixik.sdmshop2.libs.platform.utils.eventbus.DODEventBus;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventPtr;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventPtrInvoker;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

public final class ShopUIEvents {

    private static final EventPtrInvoker<OnSearchUpdate> SEARCH_UPDATE_INVOKER =
            new EventPtrInvoker<>(new DODEventBus.EventType<>("UI.SEARCH_UPDATE"));
    private static final EventPtrInvoker<FavoritesChanged> FAVORITES_CHANGED_INVOKER =
            new EventPtrInvoker<>(new DODEventBus.EventType<>("UI.FAVORITES_CHANGED"));
    private static final EventPtrInvoker<OnBuyShopEntity> BUY_SHOP_ENTITY_INVOKER =
            new EventPtrInvoker<>(new DODEventBus.EventType<>("UI.BUY_ENTRY"));

    public static final EventPtr<OnSearchUpdate> SEARCH_UPDATE = SEARCH_UPDATE_INVOKER;
    public static final EventPtr<FavoritesChanged> FAVORITES_CHANGED = FAVORITES_CHANGED_INVOKER;
    public static final EventPtr<OnBuyShopEntity> BUY_SHOP_ENTITY = BUY_SHOP_ENTITY_INVOKER;

    public static void invokeSearchUpdate(String text) {
        SEARCH_UPDATE_INVOKER.invoke(new OnSearchUpdate(text));
    }

    public static void invokeFavoritesChanged(UUID offerId, boolean favorite, Set<UUID> favorites) {
        FAVORITES_CHANGED_INVOKER.invoke(new FavoritesChanged(offerId, favorite, Set.copyOf(favorites)));
    }

    public static void invokeBuyShopEntity(@NotNull ShopEntity entity, String money_group) {
        BUY_SHOP_ENTITY_INVOKER.invoke(new OnBuyShopEntity(entity, money_group));
    }

    public record OnSearchUpdate(String text) { }

    public record FavoritesChanged(UUID offerId, boolean favorite, Set<UUID> favorites) { }

    public record OnBuyShopEntity(@NotNull ShopEntity entity, String money_group) { }

    private ShopUIEvents() {
    }
}
