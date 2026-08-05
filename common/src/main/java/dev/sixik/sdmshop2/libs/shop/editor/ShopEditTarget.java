package dev.sixik.sdmshop2.libs.shop.editor;

import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

/**
 * Типизированный ключ одного черновика объекта внутри {@link ShopEditSession}.
 *
 * <p>Редактор не ограничен одним {@code ShopInstance}: валюты экономики,
 * настройки аддонов и пользовательские инструменты могут хранить свои черновики
 * объектов под собственными целями.</p>
 */
public final class ShopEditTarget<T> {

    private final ResourceLocation id;
    private final Class<T> type;

    public ShopEditTarget(ResourceLocation id, Class<T> type) {
        this.id = Objects.requireNonNull(id, "id");
        this.type = Objects.requireNonNull(type, "type");
    }

    public ResourceLocation id() {
        return id;
    }

    public Class<T> type() {
        return type;
    }

    public T cast(Object value) {
        if (value == null) {
            return null;
        }

        if (!type.isInstance(value)) {
            throw new ClassCastException("Draft target " + id + " expected " + type.getName() + " but got " + value.getClass().getName());
        }

        return type.cast(value);
    }

    public void verify(Object value) {
        if (value != null && !type.isInstance(value)) {
            throw new IllegalArgumentException("Draft target " + id + " expected " + type.getName() + " but got " + value.getClass().getName());
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ShopEditTarget<?> that)) return false;
        return id.equals(that.id) && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    @Override
    public String toString() {
        return id + "<" + type.getSimpleName() + ">";
    }
}
