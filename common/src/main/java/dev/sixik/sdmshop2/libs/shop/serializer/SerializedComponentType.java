package dev.sixik.sdmshop2.libs.shop.serializer;

import com.google.gson.JsonObject;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Objects;
import java.util.function.Supplier;

public abstract class SerializedComponentType<T extends ShopComponent> implements IComponentType<T> {

    private final Supplier<T> factory;
    private final ComponentSerializer<T> serializer;

    protected SerializedComponentType(Supplier<T> factory, ComponentSerializer<T> serializer) {
        this.factory = Objects.requireNonNull(factory, "factory");
        this.serializer = Objects.requireNonNull(serializer, "serializer");
    }

    @Override
    public ComponentSerializer<T> serializer() {
        return serializer;
    }

    public ComponentSerializer.Snapshot takeSnapshot(T component) {
        return serializer.takeSnapshot(component);
    }

    public ComponentSerializer.Snapshot takeNetworkSnapshot(T component) {
        return serializer.takeNetworkSnapshot(component);
    }

    public ComponentSerializer.SnapshotTracker<T> networkSnapshotTracker(T component) {
        return serializer.networkSnapshotTracker(component);
    }

    public void writeNetworkDiff(FriendlyByteBuf buf, ComponentSerializer.Snapshot previousSnapshot, T component) {
        serializer.writeNetworkDiff(buf, previousSnapshot, component);
    }

    public boolean readNetworkDiff(FriendlyByteBuf buf, T component) {
        return serializer.readNetworkDiff(buf, component);
    }

    @Override
    public JsonObject serialize(T component) {
        return serializer.serialize(component);
    }

    @Override
    public T deserialize(JsonObject json) {
        return serializer.deserialize(json, createDefault());
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, T component) {
        serializer.toNetwork(buf, component);
    }

    @Override
    public T fromNetwork(FriendlyByteBuf buf) {
        return serializer.fromNetwork(buf, createDefault());
    }

    @Override
    public T createDefault() {
        return factory.get();
    }
}
