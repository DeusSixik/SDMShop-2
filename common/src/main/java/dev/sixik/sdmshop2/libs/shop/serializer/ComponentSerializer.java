package dev.sixik.sdmshop2.libs.shop.serializer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodec;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.zip.CRC32;

public final class ComponentSerializer<T extends ShopComponent> {

    private final List<Entry<T, ?>> fields = new ArrayList<>();
    private final Map<String, Entry<T, ?>> fieldsByKey = new LinkedHashMap<>();
    private final Map<Integer, Entry<T, ?>> fieldsById = new LinkedHashMap<>();
    private int cachedNetworkSchemaHash;

    public static <T extends ShopComponent> ComponentSerializer<T> create() {
        return new ComponentSerializer<>();
    }

    public <Value> ComponentSerializer<T> add(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter
    ) {
        return add(key, codec, getter, setter, true);
    }

    public <Value> ComponentSerializer<T> add(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter,
            boolean shouldSyncNet
    ) {
        return addField(key, codec, getter, setter, shouldSyncNet, false, false, null, false);
    }

    public <Value> ComponentSerializer<T> addRequired(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter
    ) {
        return addRequired(key, codec, getter, setter, true);
    }

    public <Value> ComponentSerializer<T> addRequired(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter,
            boolean shouldSyncNet
    ) {
        return addField(key, codec, getter, setter, shouldSyncNet, false, true, null, false);
    }

    public <Value> ComponentSerializer<T> addOptional(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter
    ) {
        return addOptional(key, codec, getter, setter, true);
    }

    public <Value> ComponentSerializer<T> addOptional(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter,
            boolean shouldSyncNet
    ) {
        return addField(key, codec, getter, setter, shouldSyncNet, true, false, null, false);
    }

    public <Value> ComponentSerializer<T> addDefaulted(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter,
            Value defaultValue
    ) {
        return addDefaulted(key, codec, getter, setter, defaultValue, true);
    }

    public <Value> ComponentSerializer<T> addDefaulted(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter,
            Value defaultValue,
            boolean shouldSyncNet
    ) {
        return addField(key, codec, getter, setter, shouldSyncNet, false, false, defaultValue, true);
    }

    public <Value> ComponentSerializer<T> addDiskOnly(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter
    ) {
        return add(key, codec, getter, setter, false);
    }

    public <Value> ComponentSerializer<T> addLocalOnly(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter
    ) {
        return addDiskOnly(key, codec, getter, setter);
    }

    public <Value> ComponentSerializer<T> addOptionalDiskOnly(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter
    ) {
        return addOptional(key, codec, getter, setter, false);
    }

    public <Value> ComponentSerializer<T> addDefaultedDiskOnly(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter,
            Value defaultValue
    ) {
        return addDefaulted(key, codec, getter, setter, defaultValue, false);
    }

    public ComponentSerializer<T> addInt(String key, ToIntFunction<T> getter, ObjIntConsumer<T> setter) {
        return addInt(key, getter, setter, true);
    }

    public ComponentSerializer<T> addInt(String key, ToIntFunction<T> getter, ObjIntConsumer<T> setter, boolean shouldSyncNet) {
        return add(key, FieldCodecs.INT, component -> getter.applyAsInt(component), (component, value) -> setter.accept(component, value), shouldSyncNet);
    }

    public ComponentSerializer<T> addDefaultedInt(String key, ToIntFunction<T> getter, ObjIntConsumer<T> setter, int defaultValue) {
        return addDefaultedInt(key, getter, setter, defaultValue, true);
    }

    public ComponentSerializer<T> addDefaultedInt(String key, ToIntFunction<T> getter, ObjIntConsumer<T> setter, int defaultValue, boolean shouldSyncNet) {
        return addDefaulted(key, FieldCodecs.INT, component -> getter.applyAsInt(component), (component, value) -> setter.accept(component, value), defaultValue, shouldSyncNet);
    }

    public ComponentSerializer<T> addLong(String key, ToLongFunction<T> getter, ObjLongConsumer<T> setter) {
        return addLong(key, getter, setter, true);
    }

    public ComponentSerializer<T> addLong(String key, ToLongFunction<T> getter, ObjLongConsumer<T> setter, boolean shouldSyncNet) {
        return add(key, FieldCodecs.LONG, component -> getter.applyAsLong(component), (component, value) -> setter.accept(component, value), shouldSyncNet);
    }

    public ComponentSerializer<T> addDefaultedLong(String key, ToLongFunction<T> getter, ObjLongConsumer<T> setter, long defaultValue) {
        return addDefaultedLong(key, getter, setter, defaultValue, true);
    }

    public ComponentSerializer<T> addDefaultedLong(String key, ToLongFunction<T> getter, ObjLongConsumer<T> setter, long defaultValue, boolean shouldSyncNet) {
        return addDefaulted(key, FieldCodecs.LONG, component -> getter.applyAsLong(component), (component, value) -> setter.accept(component, value), defaultValue, shouldSyncNet);
    }

    public ComponentSerializer<T> addBool(String key, Function<T, Boolean> getter, BiConsumer<T, Boolean> setter) {
        return addBool(key, getter, setter, true);
    }

    public ComponentSerializer<T> addBool(String key, Function<T, Boolean> getter, BiConsumer<T, Boolean> setter, boolean shouldSyncNet) {
        return add(key, FieldCodecs.BOOL, getter, setter, shouldSyncNet);
    }

    public ComponentSerializer<T> addBoolean(String key, Function<T, Boolean> getter, BiConsumer<T, Boolean> setter) {
        return addBool(key, getter, setter);
    }

    public ComponentSerializer<T> addBoolean(String key, Function<T, Boolean> getter, BiConsumer<T, Boolean> setter, boolean shouldSyncNet) {
        return addBool(key, getter, setter, shouldSyncNet);
    }

    public ComponentSerializer<T> addDefaultedBool(String key, Function<T, Boolean> getter, BiConsumer<T, Boolean> setter, boolean defaultValue) {
        return addDefaultedBool(key, getter, setter, defaultValue, true);
    }

    public ComponentSerializer<T> addDefaultedBool(String key, Function<T, Boolean> getter, BiConsumer<T, Boolean> setter, boolean defaultValue, boolean shouldSyncNet) {
        return addDefaulted(key, FieldCodecs.BOOL, getter, setter, defaultValue, shouldSyncNet);
    }

    public ComponentSerializer<T> addDouble(String key, ToDoubleFunction<T> getter, ObjDoubleConsumer<T> setter) {
        return addDouble(key, getter, setter, true);
    }

    public ComponentSerializer<T> addDouble(String key, ToDoubleFunction<T> getter, ObjDoubleConsumer<T> setter, boolean shouldSyncNet) {
        return add(key, FieldCodecs.DOUBLE, component -> getter.applyAsDouble(component), (component, value) -> setter.accept(component, value), shouldSyncNet);
    }

    public ComponentSerializer<T> addDefaultedDouble(String key, ToDoubleFunction<T> getter, ObjDoubleConsumer<T> setter, double defaultValue) {
        return addDefaultedDouble(key, getter, setter, defaultValue, true);
    }

    public ComponentSerializer<T> addDefaultedDouble(String key, ToDoubleFunction<T> getter, ObjDoubleConsumer<T> setter, double defaultValue, boolean shouldSyncNet) {
        return addDefaulted(key, FieldCodecs.DOUBLE, component -> getter.applyAsDouble(component), (component, value) -> setter.accept(component, value), defaultValue, shouldSyncNet);
    }

    public ComponentSerializer<T> addFloat(String key, Function<T, Float> getter, BiConsumer<T, Float> setter) {
        return addFloat(key, getter, setter, true);
    }

    public ComponentSerializer<T> addFloat(String key, Function<T, Float> getter, BiConsumer<T, Float> setter, boolean shouldSyncNet) {
        return add(key, FieldCodecs.FLOAT, getter, setter, shouldSyncNet);
    }

    public ComponentSerializer<T> addDefaultedFloat(String key, Function<T, Float> getter, BiConsumer<T, Float> setter, float defaultValue) {
        return addDefaultedFloat(key, getter, setter, defaultValue, true);
    }

    public ComponentSerializer<T> addDefaultedFloat(String key, Function<T, Float> getter, BiConsumer<T, Float> setter, float defaultValue, boolean shouldSyncNet) {
        return addDefaulted(key, FieldCodecs.FLOAT, getter, setter, defaultValue, shouldSyncNet);
    }

    public ComponentSerializer<T> addString(String key, Function<T, String> getter, BiConsumer<T, String> setter) {
        return addString(key, getter, setter, true);
    }

    public ComponentSerializer<T> addString(String key, Function<T, String> getter, BiConsumer<T, String> setter, boolean shouldSyncNet) {
        return add(key, FieldCodecs.STRING, getter, setter, shouldSyncNet);
    }

    public ComponentSerializer<T> addDefaultedString(String key, Function<T, String> getter, BiConsumer<T, String> setter, String defaultValue) {
        return addDefaultedString(key, getter, setter, defaultValue, true);
    }

    public ComponentSerializer<T> addDefaultedString(String key, Function<T, String> getter, BiConsumer<T, String> setter, String defaultValue, boolean shouldSyncNet) {
        return addDefaulted(key, FieldCodecs.STRING, getter, setter, defaultValue, shouldSyncNet);
    }

    public ComponentSerializer<T> addResourceLocation(String key, Function<T, ResourceLocation> getter, BiConsumer<T, ResourceLocation> setter) {
        return addResourceLocation(key, getter, setter, true);
    }

    public ComponentSerializer<T> addResourceLocation(String key, Function<T, ResourceLocation> getter, BiConsumer<T, ResourceLocation> setter, boolean shouldSyncNet) {
        return add(key, FieldCodecs.RESOURCE_LOCATION, getter, setter, shouldSyncNet);
    }

    public ComponentSerializer<T> addDefaultedResourceLocation(String key, Function<T, ResourceLocation> getter, BiConsumer<T, ResourceLocation> setter, ResourceLocation defaultValue) {
        return addDefaultedResourceLocation(key, getter, setter, defaultValue, true);
    }

    public ComponentSerializer<T> addDefaultedResourceLocation(String key, Function<T, ResourceLocation> getter, BiConsumer<T, ResourceLocation> setter, ResourceLocation defaultValue, boolean shouldSyncNet) {
        return addDefaulted(key, FieldCodecs.RESOURCE_LOCATION, getter, setter, defaultValue, shouldSyncNet);
    }

    public ComponentSerializer<T> addUuid(String key, Function<T, UUID> getter, BiConsumer<T, UUID> setter) {
        return addUuid(key, getter, setter, true);
    }

    public ComponentSerializer<T> addUuid(String key, Function<T, UUID> getter, BiConsumer<T, UUID> setter, boolean shouldSyncNet) {
        return add(key, FieldCodecs.UUID_CODEC, getter, setter, shouldSyncNet);
    }

    public ComponentSerializer<T> addItemStack(String key, Function<T, ItemStack> getter, BiConsumer<T, ItemStack> setter) {
        return addItemStack(key, getter, setter, true);
    }

    public ComponentSerializer<T> addItemStack(String key, Function<T, ItemStack> getter, BiConsumer<T, ItemStack> setter, boolean shouldSyncNet) {
        return add(key, FieldCodecs.ITEM_STACK, getter, setter, shouldSyncNet);
    }

    public <Element> ComponentSerializer<T> addList(
            String key,
            Function<T, List<Element>> getter,
            BiConsumer<T, List<Element>> setter,
            FieldCodec<Element> elementCodec
    ) {
        return add(key, FieldCodecs.list(elementCodec), getter, setter);
    }

    public <Element> ComponentSerializer<T> addList(
            String key,
            Function<T, List<Element>> getter,
            BiConsumer<T, List<Element>> setter,
            FieldCodec<Element> elementCodec,
            boolean shouldSyncNet
    ) {
        return add(key, FieldCodecs.list(elementCodec), getter, setter, shouldSyncNet);
    }

    public <Key, Value> ComponentSerializer<T> addMap(
            String key,
            Function<T, Map<Key, Value>> getter,
            BiConsumer<T, Map<Key, Value>> setter,
            FieldCodec<Key> keyCodec,
            FieldCodec<Value> valueCodec
    ) {
        return add(key, FieldCodecs.map(keyCodec, valueCodec), getter, setter);
    }

    public <Key, Value> ComponentSerializer<T> addMap(
            String key,
            Function<T, Map<Key, Value>> getter,
            BiConsumer<T, Map<Key, Value>> setter,
            FieldCodec<Key> keyCodec,
            FieldCodec<Value> valueCodec,
            boolean shouldSyncNet
    ) {
        return add(key, FieldCodecs.map(keyCodec, valueCodec), getter, setter, shouldSyncNet);
    }

    public JsonObject serialize(T component) {
        JsonObject json = new JsonObject();
        serialize(json, component);
        return json;
    }

    public void serialize(JsonObject json, T component) {
        Objects.requireNonNull(json, "json");
        Objects.requireNonNull(component, "component");
        for (Entry<T, ?> field : fields) {
            field.toJson(json, component);
        }
    }

    public T deserialize(JsonObject json, T component) {
        Objects.requireNonNull(json, "json");
        Objects.requireNonNull(component, "component");
        for (Entry<T, ?> field : fields) {
            field.fromJson(json, component);
        }
        return component;
    }

    public T deserialize(JsonObject json, Supplier<T> factory) {
        return deserialize(json, factory.get());
    }

    public void toNetwork(FriendlyByteBuf buf, T component) {
        Objects.requireNonNull(buf, "buf");
        Objects.requireNonNull(component, "component");

        List<Entry<T, ?>> syncedFields = syncedFieldsById();
        buf.writeVarInt(networkSchemaHash());
        buf.writeVarInt(syncedFields.size());

        for (Entry<T, ?> field : syncedFields) {
            buf.writeVarInt(field.id);
            field.toNetwork(buf, component);
        }
    }

    public void toNetworkLegacy(FriendlyByteBuf buf, T component) {
        Objects.requireNonNull(buf, "buf");
        Objects.requireNonNull(component, "component");
        for (Entry<T, ?> field : fields) {
            if (field.shouldSyncNet) {
                field.toNetwork(buf, component);
            }
        }
    }

    public T fromNetwork(FriendlyByteBuf buf, T component) {
        Objects.requireNonNull(buf, "buf");
        Objects.requireNonNull(component, "component");

        int incomingSchemaHash = buf.readVarInt();
        validateNetworkSchemaHash(incomingSchemaHash);

        int count = buf.readVarInt();
        for (int i = 0; i < count; i++) {
            int id = buf.readVarInt();
            Entry<T, ?> field = fieldsById.get(id);
            if (field == null) {
                throw new IllegalArgumentException("Unknown serializer field id: " + id);
            }
            if (!field.shouldSyncNet) {
                throw new IllegalArgumentException("Field is not network synced: " + field.key);
            }
            field.fromNetwork(buf, component);
        }
        return component;
    }

    public T fromNetworkLegacy(FriendlyByteBuf buf, T component) {
        Objects.requireNonNull(buf, "buf");
        Objects.requireNonNull(component, "component");
        for (Entry<T, ?> field : fields) {
            if (field.shouldSyncNet) {
                field.fromNetwork(buf, component);
            }
        }
        return component;
    }

    public T fromNetwork(FriendlyByteBuf buf, Supplier<T> factory) {
        return fromNetwork(buf, factory.get());
    }

    public SnapshotTracker<T> snapshotTracker(T component) {
        return new SnapshotTracker<>(this, takeSnapshot(component), false);
    }

    public SnapshotTracker<T> networkSnapshotTracker(T component) {
        return new SnapshotTracker<>(this, takeNetworkSnapshot(component), true);
    }

    public Snapshot takeSnapshot(T component) {
        return takeSnapshot(component, false);
    }

    public Snapshot takeNetworkSnapshot(T component) {
        return takeSnapshot(component, true);
    }

    public boolean hasChanges(T component, Snapshot previousSnapshot) {
        return !diff(previousSnapshot, takeSnapshot(component)).isEmpty();
    }

    public Snapshot diff(Snapshot previousSnapshot, T component) {
        return diff(previousSnapshot, takeSnapshot(component));
    }

    public Snapshot diffIfDirty(T component, Snapshot previousSnapshot) {
        Objects.requireNonNull(component, "component");
        if (!component.consumeDirty()) return Snapshot.empty();
        return diff(previousSnapshot, component);
    }

    public Snapshot networkDiffIfDirty(T component, Snapshot previousSnapshot) {
        Objects.requireNonNull(component, "component");
        if (!component.consumeDirty()) return Snapshot.empty();
        return diff(previousSnapshot, takeNetworkSnapshot(component));
    }

    public Snapshot diff(Snapshot previousSnapshot, Snapshot nextSnapshot) {
        Snapshot previous = previousSnapshot == null ? Snapshot.empty() : previousSnapshot;
        Snapshot next = nextSnapshot == null ? Snapshot.empty() : nextSnapshot;
        Map<String, Object> changed = new LinkedHashMap<>();

        for (Entry<T, ?> field : fields) {
            if (!next.values.containsKey(field.key)) continue;
            Object previousValue = previous.values.get(field.key);
            Object nextValue = next.values.get(field.key);
            if (!field.areSnapshotValuesEqual(previousValue, nextValue)) {
                changed.put(field.key, field.copySnapshotValue(nextValue));
            }
        }

        return new Snapshot(changed);
    }

    public void writeNetworkDiff(FriendlyByteBuf buf, Snapshot previousSnapshot, T component) {
        writeNetworkDiff(buf, previousSnapshot, takeNetworkSnapshot(component));
    }

    public void writeNetworkDiff(FriendlyByteBuf buf, Snapshot previousSnapshot, Snapshot nextSnapshot) {
        Objects.requireNonNull(buf, "buf");

        Snapshot diff = diff(previousSnapshot, nextSnapshot).filter(this::isSyncedField);
        buf.writeVarInt(networkSchemaHash());
        buf.writeVarInt(diff.values.size());
        for (Map.Entry<String, Object> changed : diff.values.entrySet()) {
            Entry<T, ?> field = fieldsByKey.get(changed.getKey());
            if (field == null || !field.shouldSyncNet) continue;
            buf.writeVarInt(field.id);
            field.writeSnapshotValue(buf, changed.getValue());
        }
    }

    public boolean readNetworkDiff(FriendlyByteBuf buf, T component) {
        Objects.requireNonNull(buf, "buf");
        Objects.requireNonNull(component, "component");

        validateNetworkSchemaHash(buf.readVarInt());

        int count = buf.readVarInt();
        boolean changed = false;
        for (int i = 0; i < count; i++) {
            int id = buf.readVarInt();
            Entry<T, ?> field = fieldsById.get(id);
            if (field == null) {
                throw new IllegalArgumentException("Unknown serializer field id: " + id);
            }
            if (!field.shouldSyncNet) {
                throw new IllegalArgumentException("Field is not network synced: " + field.key);
            }

            field.readNetworkSnapshotValue(buf, component);
            changed = true;
        }
        return changed;
    }

    public void applySnapshot(T component, Snapshot snapshot) {
        Objects.requireNonNull(component, "component");
        if (snapshot == null || snapshot.isEmpty()) return;

        for (Map.Entry<String, Object> value : snapshot.values.entrySet()) {
            Entry<T, ?> field = fieldsByKey.get(value.getKey());
            if (field != null) {
                field.applySnapshotValue(component, value.getValue());
            }
        }
    }

    public List<String> keys() {
        return List.copyOf(fieldsByKey.keySet());
    }

    public List<String> networkKeys() {
        List<String> keys = new ArrayList<>();
        for (Entry<T, ?> field : fields) {
            if (field.shouldSyncNet) {
                keys.add(field.key);
            }
        }
        return Collections.unmodifiableList(keys);
    }

    public int networkSchemaHash() {
        if (cachedNetworkSchemaHash == 0) {
            cachedNetworkSchemaHash = computeNetworkSchemaHash();
        }
        return cachedNetworkSchemaHash;
    }

    public Integer fieldId(String key) {
        Entry<T, ?> field = fieldsByKey.get(key);
        return field == null ? null : field.id;
    }

    public static int stableFieldId(String key) {
        Objects.requireNonNull(key, "key");
        return deriveId(key);
    }

    private <Value> ComponentSerializer<T> addField(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter,
            boolean shouldSyncNet,
            boolean optional,
            boolean required,
            Value defaultValue,
            boolean omitDefaultFromJson
    ) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(codec, "codec");
        Objects.requireNonNull(getter, "getter");
        Objects.requireNonNull(setter, "setter");
        if (key.isBlank()) throw new IllegalArgumentException("Serializer field key cannot be blank");
        if (fieldsByKey.containsKey(key)) throw new IllegalArgumentException("Duplicate serializer field key: " + key);
        if (optional && required) throw new IllegalArgumentException("Serializer field cannot be optional and required at the same time: " + key);

        int id = deriveId(key);
        Entry<T, ?> collision = fieldsById.get(id);
        if (collision != null) {
            throw new IllegalStateException("Hash collision for serializer field id " + id + " between '" + key + "' and '" + collision.key + "'");
        }

        Entry<T, Value> entry = new Entry<>(id, key, codec, getter, setter, shouldSyncNet, optional, required, defaultValue, omitDefaultFromJson);
        fields.add(entry);
        fieldsByKey.put(key, entry);
        fieldsById.put(id, entry);
        cachedNetworkSchemaHash = 0;
        return this;
    }

    private Snapshot takeSnapshot(T component, boolean networkOnly) {
        Objects.requireNonNull(component, "component");
        Map<String, Object> values = new LinkedHashMap<>();
        for (Entry<T, ?> field : fields) {
            if (!networkOnly || field.shouldSyncNet) {
                values.put(field.key, field.copyValue(component));
            }
        }
        return new Snapshot(values);
    }

    private boolean isSyncedField(String key) {
        Entry<T, ?> field = fieldsByKey.get(key);
        return field != null && field.shouldSyncNet;
    }

    private int computeNetworkSchemaHash() {
        CRC32 crc = new CRC32();
        for (Entry<T, ?> field : syncedFieldsById()) {
            updateCrc(crc, field.id);
            updateCrc(crc, field.key);
            updateCrc(crc, field.codec.schemaName());
            updateCrc(crc, field.optional ? 1 : 0);
        }
        int hash = (int) crc.getValue();
        return hash == 0 ? 1 : hash;
    }

    private List<Entry<T, ?>> syncedFieldsById() {
        List<Entry<T, ?>> syncedFields = new ArrayList<>();
        for (Entry<T, ?> field : fieldsById.values()) {
            if (field.shouldSyncNet) {
                syncedFields.add(field);
            }
        }
        syncedFields.sort((first, second) -> Integer.compare(first.id, second.id));
        return syncedFields;
    }

    private void validateNetworkSchemaHash(int incomingSchemaHash) {
        int localSchemaHash = networkSchemaHash();
        if (incomingSchemaHash != localSchemaHash) {
            throw new IllegalArgumentException("Serializer network schema mismatch. Incoming: " + incomingSchemaHash + ", local: " + localSchemaHash);
        }
    }

    private static int deriveId(String key) {
        return key.hashCode() & 0x7FFFFFFF;
    }

    private static void updateCrc(CRC32 crc, int value) {
        crc.update(value);
        crc.update(value >>> 8);
        crc.update(value >>> 16);
        crc.update(value >>> 24);
    }

    private static void updateCrc(CRC32 crc, String value) {
        for (int i = 0; i < value.length(); i++) {
            char character = value.charAt(i);
            crc.update(character);
            crc.update(character >>> 8);
        }
    }

    private static final class Entry<ComponentType, Value> {
        private final int id;
        private final String key;
        private final FieldCodec<Value> codec;
        private final Function<ComponentType, Value> getter;
        private final BiConsumer<ComponentType, Value> setter;
        private final boolean shouldSyncNet;
        private final boolean optional;
        private final boolean required;
        private final Value defaultValue;
        private final boolean omitDefaultFromJson;

        private Entry(
                int id,
                String key,
                FieldCodec<Value> codec,
                Function<ComponentType, Value> getter,
                BiConsumer<ComponentType, Value> setter,
                boolean shouldSyncNet,
                boolean optional,
                boolean required,
                Value defaultValue,
                boolean omitDefaultFromJson
        ) {
            this.id = id;
            this.key = key;
            this.codec = codec;
            this.getter = getter;
            this.setter = setter;
            this.shouldSyncNet = shouldSyncNet;
            this.optional = optional;
            this.required = required;
            this.defaultValue = codec.copy(defaultValue);
            this.omitDefaultFromJson = omitDefaultFromJson;
        }

        private void toJson(JsonObject json, ComponentType component) {
            Value value = getter.apply(component);
            if (optional && value == null) return;
            if (omitDefaultFromJson && codec.areEqual(value, defaultValue)) return;
            codec.toJson(json, key, value);
        }

        private void fromJson(JsonObject json, ComponentType component) {
            if (!json.has(key)) {
                if (required) {
                    throw new JsonParseException("Missing required serializer field: '" + key + "'");
                }
                if (omitDefaultFromJson) {
                    setter.accept(component, codec.copy(defaultValue));
                }
                return;
            }
            setter.accept(component, codec.fromJson(json, key, getter.apply(component)));
        }

        private void toNetwork(FriendlyByteBuf buf, ComponentType component) {
            writeValue(buf, getter.apply(component));
        }

        private void fromNetwork(FriendlyByteBuf buf, ComponentType component) {
            setter.accept(component, readValue(buf));
        }

        private Object copyValue(ComponentType component) {
            return codec.copy(getter.apply(component));
        }

        private Object copySnapshotValue(Object value) {
            return codec.copy(cast(value));
        }

        private boolean areSnapshotValuesEqual(Object first, Object second) {
            return codec.areEqual(cast(first), cast(second));
        }

        private void applySnapshotValue(ComponentType component, Object value) {
            setter.accept(component, codec.copy(cast(value)));
        }

        private void writeSnapshotValue(FriendlyByteBuf buf, Object value) {
            writeValue(buf, cast(value));
        }

        private void readNetworkSnapshotValue(FriendlyByteBuf buf, ComponentType component) {
            setter.accept(component, readValue(buf));
        }

        private void writeValue(FriendlyByteBuf buf, Value value) {
            if (optional) {
                buf.writeBoolean(value != null);
                if (value == null) return;
            }
            codec.toNetwork(buf, value);
        }

        private Value readValue(FriendlyByteBuf buf) {
            if (optional && !buf.readBoolean()) {
                return null;
            }
            return codec.fromNetwork(buf);
        }

        @SuppressWarnings("unchecked")
        private Value cast(Object value) {
            return (Value) value;
        }
    }

    public static final class Snapshot {
        private static final Snapshot EMPTY = new Snapshot(Collections.emptyMap());

        private final Map<String, Object> values;

        private Snapshot(Map<String, Object> values) {
            this.values = Collections.unmodifiableMap(new LinkedHashMap<>(values));
        }

        public static Snapshot empty() {
            return EMPTY;
        }

        public boolean isEmpty() {
            return values.isEmpty();
        }

        public boolean contains(String key) {
            return values.containsKey(key);
        }

        @SuppressWarnings("unchecked")
        public <Value> Value get(String key) {
            return (Value) values.get(key);
        }

        public Map<String, Object> values() {
            return values;
        }

        private Snapshot filter(Predicate<String> keyFilter) {
            Map<String, Object> filtered = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                if (keyFilter.test(entry.getKey())) {
                    filtered.put(entry.getKey(), entry.getValue());
                }
            }
            return new Snapshot(filtered);
        }
    }

    public static final class SnapshotTracker<ComponentType extends ShopComponent> {
        private final ComponentSerializer<ComponentType> serializer;
        private final boolean networkOnly;
        private Snapshot snapshot;

        private SnapshotTracker(ComponentSerializer<ComponentType> serializer, Snapshot snapshot, boolean networkOnly) {
            this.serializer = serializer;
            this.snapshot = snapshot;
            this.networkOnly = networkOnly;
        }

        public Snapshot snapshot() {
            return snapshot;
        }

        public Snapshot diff(ComponentType component) {
            return serializer.diff(snapshot, take(component));
        }

        public boolean hasChanges(ComponentType component) {
            return !diff(component).isEmpty();
        }

        public Snapshot markClean(ComponentType component) {
            snapshot = take(component);
            return snapshot;
        }

        public void replace(Snapshot snapshot) {
            this.snapshot = snapshot == null ? Snapshot.empty() : snapshot;
        }

        public void writeNetworkDiff(FriendlyByteBuf buf, ComponentType component) {
            serializer.writeNetworkDiff(buf, snapshot, component);
            markClean(component);
        }

        public boolean readNetworkDiff(FriendlyByteBuf buf, ComponentType component) {
            boolean changed = serializer.readNetworkDiff(buf, component);
            markClean(component);
            return changed;
        }

        private Snapshot take(ComponentType component) {
            return networkOnly ? serializer.takeNetworkSnapshot(component) : serializer.takeSnapshot(component);
        }
    }
}
