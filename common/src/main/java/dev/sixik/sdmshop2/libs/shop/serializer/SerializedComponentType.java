package dev.sixik.sdmshop2.libs.shop.serializer;

import com.google.gson.JsonObject;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Базовая реализация типа компонента, которая делегирует сериализацию в {@link ComponentSerializer}.
 *
 * @param <T> тип компонента
 */
public abstract class SerializedComponentType<T extends ShopComponent> implements IComponentType<T> {

    private final Supplier<T> factory;
    private final ComponentSerializer<T> serializer;

    /**
     * Создает тип компонента с фабрикой дефолтного значения и serializer-схемой.
     */
    protected SerializedComponentType(Supplier<T> factory, ComponentSerializer<T> serializer) {
        this.factory = Objects.requireNonNull(factory, "factory");
        this.serializer = Objects.requireNonNull(serializer, "serializer");
    }

    /**
     * Возвращает serializer, описывающий поля компонента.
     */
    @Override
    public ComponentSerializer<T> serializer() {
        return serializer;
    }

    /**
     * Создает snapshot всех полей компонента.
     */
    public ComponentSerializer.Snapshot takeSnapshot(T component) {
        return serializer.takeSnapshot(component);
    }

    /**
     * Создает snapshot только сетевых полей компонента.
     */
    public ComponentSerializer.Snapshot takeNetworkSnapshot(T component) {
        return serializer.takeNetworkSnapshot(component);
    }

    /**
     * Создает tracker для отслеживания изменений сетевых полей.
     */
    public ComponentSerializer.SnapshotTracker<T> networkSnapshotTracker(T component) {
        return serializer.networkSnapshotTracker(component);
    }

    /**
     * Записывает в сеть только изменения относительно предыдущего snapshot.
     */
    public void writeNetworkDiff(FriendlyByteBuf buf, ComponentSerializer.Snapshot previousSnapshot, T component) {
        serializer.writeNetworkDiff(buf, previousSnapshot, component);
    }

    /**
     * Читает сетевой diff и применяет его к компоненту.
     */
    public boolean readNetworkDiff(FriendlyByteBuf buf, T component) {
        return serializer.readNetworkDiff(buf, component);
    }

    /**
     * Сериализует компонент в JSON через serializer.
     */
    @Override
    public JsonObject serialize(T component) {
        return serializer.serialize(component);
    }

    /**
     * Создает компонент из JSON через serializer и дефолтную фабрику.
     */
    @Override
    public T deserialize(JsonObject json) {
        return serializer.deserialize(json, createDefault());
    }

    /**
     * Записывает компонент в сетевой буфер через serializer.
     */
    @Override
    public void toNetwork(FriendlyByteBuf buf, T component) {
        serializer.toNetwork(buf, component);
    }

    /**
     * Читает компонент из сетевого буфера через serializer.
     */
    @Override
    public T fromNetwork(FriendlyByteBuf buf) {
        return serializer.fromNetwork(buf, createDefault());
    }

    /**
     * Создает компонент со значениями по умолчанию.
     */
    @Override
    public T createDefault() {
        return factory.get();
    }
}
