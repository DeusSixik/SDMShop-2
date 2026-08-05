package dev.sixik.sdmshop2.libs.shop.editor;

import java.util.*;

/**
 * Хранилище черновиков объектов в рамках одной сессии.
 */
public final class ShopEditDraftStore {

    private final Map<ShopEditTarget<?>, Object> values = new LinkedHashMap<>();

    public <T> void put(ShopEditTarget<T> target, T value) {
        target.verify(value);
        values.put(target, value);
    }

    public <T> Optional<T> get(ShopEditTarget<T> target) {
        return Optional.ofNullable(target.cast(values.get(target)));
    }

    public <T> T getOrDefault(ShopEditTarget<T> target, T defaultValue) {
        return get(target).orElse(defaultValue);
    }

    public boolean contains(ShopEditTarget<?> target) {
        return values.containsKey(target);
    }

    public void remove(ShopEditTarget<?> target) {
        values.remove(target);
    }

    public void clear() {
        values.clear();
    }

    public Set<ShopEditTarget<?>> targets() {
        return Set.copyOf(values.keySet());
    }

    public Collection<Object> values() {
        return Collections.unmodifiableCollection(values.values());
    }

    public Map<ShopEditTarget<?>, Object> snapshot() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(values));
    }
}
