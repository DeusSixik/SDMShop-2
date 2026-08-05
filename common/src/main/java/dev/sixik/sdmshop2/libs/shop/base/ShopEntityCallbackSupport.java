package dev.sixik.sdmshop2.libs.shop.base;

import dev.sixik.sdmshop2.libs.shop.base.callbacks.ShopEntityCallbacks;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.Nullable;

public interface ShopEntityCallbackSupport {
    
    @Nullable
    ObjectArrayList<ShopEntityCallbacks.OnAddComponent> getAddComponentListeners();

    @Nullable
    ObjectArrayList<ShopEntityCallbacks.OnRemoveComponent> getRemoveComponentListeners();

    @Nullable
    ObjectArrayList<ShopEntityCallbacks.OnComponentUpdate> getUpdateComponentListeners();

    @Nullable
    ObjectArrayList<ShopEntityCallbacks.OnUpdate> getUpdateListeners();

    void subscribeAddComponent(ShopEntityCallbacks.OnAddComponent callback);

    void subscribeRemoveComponent(ShopEntityCallbacks.OnRemoveComponent callback);

    void subscribeUpdateComponent(ShopEntityCallbacks.OnComponentUpdate callback);

    void subscribeUpdate(ShopEntityCallbacks.OnUpdate callback);

    void unsubscribeAddComponent(ShopEntityCallbacks.OnAddComponent callback);

    void unsubscribeRemoveComponent(ShopEntityCallbacks.OnRemoveComponent callback);

    void unsubscribeUpdateComponent(ShopEntityCallbacks.OnComponentUpdate callback);

    void unsubscribeUpdate(ShopEntityCallbacks.OnUpdate callback);

    default void invokeAddComponent(ShopEntity entity, ShopComponent component) {
        final var list = getAddComponentListeners();
        if (list == null || list.isEmpty()) return;
        final ShopEntityCallbacks.OnAddComponent[] snapshot = list.toArray(new ShopEntityCallbacks.OnAddComponent[0]);
        for (int i = snapshot.length - 1; i >= 0; i--) {
            snapshot[i].onComponentAdd(entity, component);
        }
    }    
    
    default void invokeRemoveComponent(ShopEntity entity, ShopComponent component) {
        final var list = getRemoveComponentListeners();
        if (list == null || list.isEmpty()) return;
        final ShopEntityCallbacks.OnRemoveComponent[] snapshot = list.toArray(new ShopEntityCallbacks.OnRemoveComponent[0]);
        for (int i = snapshot.length - 1; i >= 0; i--) {
            snapshot[i].onComponentRemove(entity, component);
        }
    }

    default void invokeUpdateComponent(ShopEntity entity, ShopComponent component) {
        final var list = getUpdateComponentListeners();
        if (list == null || list.isEmpty()) return;
        final ShopEntityCallbacks.OnComponentUpdate[] snapshot = list.toArray(new ShopEntityCallbacks.OnComponentUpdate[0]);
        for (int i = snapshot.length - 1; i >= 0; i--) {
            snapshot[i].onComponentUpdate(entity, component);
        }
    }

    default void invokeUpdate(ShopEntity entity) {
        entity.onUpdate();
        final var list = getUpdateListeners();
        if (list == null || list.isEmpty()) return;
        final ShopEntityCallbacks.OnUpdate[] snapshot = list.toArray(new ShopEntityCallbacks.OnUpdate[0]);
        for (int i = snapshot.length - 1; i >= 0; i--) {
            snapshot[i].onUpdate(entity);
        }
    }


}
