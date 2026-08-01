package dev.sixik.sdmshop2.libs.shop.serializer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodec;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
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

/**
 * Описывает набор полей компонента и умеет сериализовать их в JSON, сеть и snapshot.
 *
 * @param <T> тип компонента
 */
public final class ComponentSerializer<T extends ShopComponent> {

    private final List<Entry<T, ?>> fields = new ArrayList<>();
    private final Map<String, Entry<T, ?>> fieldsByKey = new LinkedHashMap<>();
    private final Int2ObjectMap<Entry<T, ?>> fieldsById = new Int2ObjectOpenHashMap<>();
    private int cachedNetworkSchemaHash;

    /**
     * Создает пустой serializer, в который дальше добавляются поля.
     */
    public static <T extends ShopComponent> ComponentSerializer<T> create() {
        return new ComponentSerializer<>();
    }

    /**
     * Добавляет обычное поле, которое пишется в JSON и синхронизируется по сети.
     */
    public <Value> ComponentSerializer<T> add(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter
    ) {
        return add(key, codec, getter, setter, true);
    }

    /**
     * Добавляет обычное поле с ручным выбором, нужно ли синхронизировать его по сети.
     */
    public <Value> ComponentSerializer<T> add(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter,
            boolean shouldSyncNet
    ) {
        return addField(key, codec, getter, setter, shouldSyncNet, false, false, null, false);
    }

    /**
     * Добавляет обязательное поле. Если его нет в JSON, будет ошибка.
     */
    public <Value> ComponentSerializer<T> addRequired(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter
    ) {
        return addRequired(key, codec, getter, setter, true);
    }

    /**
     * Добавляет обязательное поле с ручным выбором сетевой синхронизации.
     */
    public <Value> ComponentSerializer<T> addRequired(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter,
            boolean shouldSyncNet
    ) {
        return addField(key, codec, getter, setter, shouldSyncNet, false, true, null, false);
    }

    /**
     * Добавляет optional-поле, которое может быть null.
     */
    public <Value> ComponentSerializer<T> addOptional(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter
    ) {
        return addOptional(key, codec, getter, setter, true);
    }

    /**
     * Добавляет optional-поле с ручным выбором сетевой синхронизации.
     */
    public <Value> ComponentSerializer<T> addOptional(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter,
            boolean shouldSyncNet
    ) {
        return addField(key, codec, getter, setter, shouldSyncNet, true, false, null, false);
    }

    /**
     * Добавляет поле со значением по умолчанию. В JSON default-значение не записывается.
     */
    public <Value> ComponentSerializer<T> addDefaulted(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter,
            Value defaultValue
    ) {
        return addDefaulted(key, codec, getter, setter, defaultValue, true);
    }

    /**
     * Добавляет defaulted-поле с ручным выбором сетевой синхронизации.
     */
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

    /**
     * Добавляет поле только для сохранения на диск, без сетевой синхронизации.
     */
    public <Value> ComponentSerializer<T> addDiskOnly(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter
    ) {
        return add(key, codec, getter, setter, false);
    }

    /**
     * Алиас для addDiskOnly: поле существует только локально/на диске.
     */
    public <Value> ComponentSerializer<T> addLocalOnly(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter
    ) {
        return addDiskOnly(key, codec, getter, setter);
    }

    /**
     * Добавляет nullable-поле только для диска.
     */
    public <Value> ComponentSerializer<T> addOptionalDiskOnly(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter
    ) {
        return addOptional(key, codec, getter, setter, false);
    }

    /**
     * Добавляет defaulted-поле только для диска.
     */
    public <Value> ComponentSerializer<T> addDefaultedDiskOnly(
            String key,
            FieldCodec<Value> codec,
            Function<T, Value> getter,
            BiConsumer<T, Value> setter,
            Value defaultValue
    ) {
        return addDefaulted(key, codec, getter, setter, defaultValue, false);
    }

    /**
     * Добавляет int-поле.
     */
    public ComponentSerializer<T> addInt(String key, ToIntFunction<T> getter, ObjIntConsumer<T> setter) {
        return addInt(key, getter, setter, true);
    }

    /**
     * Добавляет int-поле с ручным выбором сетевой синхронизации.
     */
    public ComponentSerializer<T> addInt(String key, ToIntFunction<T> getter, ObjIntConsumer<T> setter, boolean shouldSyncNet) {
        return add(key, FieldCodecs.INT, getter::applyAsInt, setter::accept, shouldSyncNet);
    }

    /**
     * Добавляет int-поле со значением по умолчанию.
     */
    public ComponentSerializer<T> addDefaultedInt(String key, ToIntFunction<T> getter, ObjIntConsumer<T> setter, int defaultValue) {
        return addDefaultedInt(key, getter, setter, defaultValue, true);
    }

    /**
     * Добавляет defaulted int-поле с ручным выбором сетевой синхронизации.
     */
    public ComponentSerializer<T> addDefaultedInt(String key, ToIntFunction<T> getter, ObjIntConsumer<T> setter, int defaultValue, boolean shouldSyncNet) {
        return addDefaulted(key, FieldCodecs.INT, getter::applyAsInt, setter::accept, defaultValue, shouldSyncNet);
    }

    /**
     * Добавляет long-поле.
     */
    public ComponentSerializer<T> addLong(String key, ToLongFunction<T> getter, ObjLongConsumer<T> setter) {
        return addLong(key, getter, setter, true);
    }

    /**
     * Добавляет long-поле с ручным выбором сетевой синхронизации.
     */
    public ComponentSerializer<T> addLong(String key, ToLongFunction<T> getter, ObjLongConsumer<T> setter, boolean shouldSyncNet) {
        return add(key, FieldCodecs.LONG, getter::applyAsLong, setter::accept, shouldSyncNet);
    }

    /**
     * Добавляет long-поле со значением по умолчанию.
     */
    public ComponentSerializer<T> addDefaultedLong(String key, ToLongFunction<T> getter, ObjLongConsumer<T> setter, long defaultValue) {
        return addDefaultedLong(key, getter, setter, defaultValue, true);
    }

    /**
     * Добавляет defaulted long-поле с ручным выбором сетевой синхронизации.
     */
    public ComponentSerializer<T> addDefaultedLong(String key, ToLongFunction<T> getter, ObjLongConsumer<T> setter, long defaultValue, boolean shouldSyncNet) {
        return addDefaulted(key, FieldCodecs.LONG, getter::applyAsLong, setter::accept, defaultValue, shouldSyncNet);
    }

    /**
     * Добавляет boolean-поле.
     */
    public ComponentSerializer<T> addBool(String key, Function<T, Boolean> getter, BiConsumer<T, Boolean> setter) {
        return addBool(key, getter, setter, true);
    }

    /**
     * Добавляет boolean-поле с ручным выбором сетевой синхронизации.
     */
    public ComponentSerializer<T> addBool(String key, Function<T, Boolean> getter, BiConsumer<T, Boolean> setter, boolean shouldSyncNet) {
        return add(key, FieldCodecs.BOOL, getter, setter, shouldSyncNet);
    }

    /**
     * Алиас для addBool.
     */
    public ComponentSerializer<T> addBoolean(String key, Function<T, Boolean> getter, BiConsumer<T, Boolean> setter) {
        return addBool(key, getter, setter);
    }

    /**
     * Алиас для addBool с ручным выбором сетевой синхронизации.
     */
    public ComponentSerializer<T> addBoolean(String key, Function<T, Boolean> getter, BiConsumer<T, Boolean> setter, boolean shouldSyncNet) {
        return addBool(key, getter, setter, shouldSyncNet);
    }

    /**
     * Добавляет boolean-поле со значением по умолчанию.
     */
    public ComponentSerializer<T> addDefaultedBool(String key, Function<T, Boolean> getter, BiConsumer<T, Boolean> setter, boolean defaultValue) {
        return addDefaultedBool(key, getter, setter, defaultValue, true);
    }

    /**
     * Добавляет defaulted boolean-поле с ручным выбором сетевой синхронизации.
     */
    public ComponentSerializer<T> addDefaultedBool(String key, Function<T, Boolean> getter, BiConsumer<T, Boolean> setter, boolean defaultValue, boolean shouldSyncNet) {
        return addDefaulted(key, FieldCodecs.BOOL, getter, setter, defaultValue, shouldSyncNet);
    }

    /**
     * Добавляет double-поле.
     */
    public ComponentSerializer<T> addDouble(String key, ToDoubleFunction<T> getter, ObjDoubleConsumer<T> setter) {
        return addDouble(key, getter, setter, true);
    }

    /**
     * Добавляет double-поле с ручным выбором сетевой синхронизации.
     */
    public ComponentSerializer<T> addDouble(String key, ToDoubleFunction<T> getter, ObjDoubleConsumer<T> setter, boolean shouldSyncNet) {
        return add(key, FieldCodecs.DOUBLE, getter::applyAsDouble, setter::accept, shouldSyncNet);
    }

    /**
     * Добавляет double-поле со значением по умолчанию.
     */
    public ComponentSerializer<T> addDefaultedDouble(String key, ToDoubleFunction<T> getter, ObjDoubleConsumer<T> setter, double defaultValue) {
        return addDefaultedDouble(key, getter, setter, defaultValue, true);
    }

    /**
     * Добавляет defaulted double-поле с ручным выбором сетевой синхронизации.
     */
    public ComponentSerializer<T> addDefaultedDouble(String key, ToDoubleFunction<T> getter, ObjDoubleConsumer<T> setter, double defaultValue, boolean shouldSyncNet) {
        return addDefaulted(key, FieldCodecs.DOUBLE, getter::applyAsDouble, setter::accept, defaultValue, shouldSyncNet);
    }

    /**
     * Добавляет float-поле.
     */
    public ComponentSerializer<T> addFloat(String key, Function<T, Float> getter, BiConsumer<T, Float> setter) {
        return addFloat(key, getter, setter, true);
    }

    /**
     * Добавляет float-поле с ручным выбором сетевой синхронизации.
     */
    public ComponentSerializer<T> addFloat(String key, Function<T, Float> getter, BiConsumer<T, Float> setter, boolean shouldSyncNet) {
        return add(key, FieldCodecs.FLOAT, getter, setter, shouldSyncNet);
    }

    /**
     * Добавляет float-поле со значением по умолчанию.
     */
    public ComponentSerializer<T> addDefaultedFloat(String key, Function<T, Float> getter, BiConsumer<T, Float> setter, float defaultValue) {
        return addDefaultedFloat(key, getter, setter, defaultValue, true);
    }

    /**
     * Добавляет defaulted float-поле с ручным выбором сетевой синхронизации.
     */
    public ComponentSerializer<T> addDefaultedFloat(String key, Function<T, Float> getter, BiConsumer<T, Float> setter, float defaultValue, boolean shouldSyncNet) {
        return addDefaulted(key, FieldCodecs.FLOAT, getter, setter, defaultValue, shouldSyncNet);
    }

    /**
     * Добавляет string-поле.
     */
    public ComponentSerializer<T> addString(String key, Function<T, String> getter, BiConsumer<T, String> setter) {
        return addString(key, getter, setter, true);
    }

    /**
     * Добавляет string-поле с ручным выбором сетевой синхронизации.
     */
    public ComponentSerializer<T> addString(String key, Function<T, String> getter, BiConsumer<T, String> setter, boolean shouldSyncNet) {
        return add(key, FieldCodecs.STRING, getter, setter, shouldSyncNet);
    }

    /**
     * Добавляет string-поле со значением по умолчанию.
     */
    public ComponentSerializer<T> addDefaultedString(String key, Function<T, String> getter, BiConsumer<T, String> setter, String defaultValue) {
        return addDefaultedString(key, getter, setter, defaultValue, true);
    }

    /**
     * Добавляет defaulted string-поле с ручным выбором сетевой синхронизации.
     */
    public ComponentSerializer<T> addDefaultedString(String key, Function<T, String> getter, BiConsumer<T, String> setter, String defaultValue, boolean shouldSyncNet) {
        return addDefaulted(key, FieldCodecs.STRING, getter, setter, defaultValue, shouldSyncNet);
    }

    /**
     * Добавляет ResourceLocation-поле.
     */
    public ComponentSerializer<T> addResourceLocation(String key, Function<T, ResourceLocation> getter, BiConsumer<T, ResourceLocation> setter) {
        return addResourceLocation(key, getter, setter, true);
    }

    /**
     * Добавляет ResourceLocation-поле с ручным выбором сетевой синхронизации.
     */
    public ComponentSerializer<T> addResourceLocation(String key, Function<T, ResourceLocation> getter, BiConsumer<T, ResourceLocation> setter, boolean shouldSyncNet) {
        return add(key, FieldCodecs.RESOURCE_LOCATION, getter, setter, shouldSyncNet);
    }

    /**
     * Добавляет ResourceLocation-поле со значением по умолчанию.
     */
    public ComponentSerializer<T> addDefaultedResourceLocation(String key, Function<T, ResourceLocation> getter, BiConsumer<T, ResourceLocation> setter, ResourceLocation defaultValue) {
        return addDefaultedResourceLocation(key, getter, setter, defaultValue, true);
    }

    /**
     * Добавляет defaulted ResourceLocation-поле с ручным выбором сетевой синхронизации.
     */
    public ComponentSerializer<T> addDefaultedResourceLocation(String key, Function<T, ResourceLocation> getter, BiConsumer<T, ResourceLocation> setter, ResourceLocation defaultValue, boolean shouldSyncNet) {
        return addDefaulted(key, FieldCodecs.RESOURCE_LOCATION, getter, setter, defaultValue, shouldSyncNet);
    }

    /**
     * Добавляет UUID-поле.
     */
    public ComponentSerializer<T> addUuid(String key, Function<T, UUID> getter, BiConsumer<T, UUID> setter) {
        return addUuid(key, getter, setter, true);
    }

    /**
     * Добавляет UUID-поле с ручным выбором сетевой синхронизации.
     */
    public ComponentSerializer<T> addUuid(String key, Function<T, UUID> getter, BiConsumer<T, UUID> setter, boolean shouldSyncNet) {
        return add(key, FieldCodecs.UUID_CODEC, getter, setter, shouldSyncNet);
    }

    /**
     * Добавляет ItemStack-поле.
     */
    public ComponentSerializer<T> addItemStack(String key, Function<T, ItemStack> getter, BiConsumer<T, ItemStack> setter) {
        return addItemStack(key, getter, setter, true);
    }

    /**
     * Добавляет ItemStack-поле с ручным выбором сетевой синхронизации.
     */
    public ComponentSerializer<T> addItemStack(String key, Function<T, ItemStack> getter, BiConsumer<T, ItemStack> setter, boolean shouldSyncNet) {
        return add(key, FieldCodecs.ITEM_STACK, getter, setter, shouldSyncNet);
    }

    /**
     * Добавляет поле-список.
     */
    public <Element> ComponentSerializer<T> addList(
            String key,
            Function<T, List<Element>> getter,
            BiConsumer<T, List<Element>> setter,
            FieldCodec<Element> elementCodec
    ) {
        return add(key, FieldCodecs.list(elementCodec), getter, setter);
    }

    /**
     * Добавляет поле-список с ручным выбором сетевой синхронизации.
     */
    public <Element> ComponentSerializer<T> addList(
            String key,
            Function<T, List<Element>> getter,
            BiConsumer<T, List<Element>> setter,
            FieldCodec<Element> elementCodec,
            boolean shouldSyncNet
    ) {
        return add(key, FieldCodecs.list(elementCodec), getter, setter, shouldSyncNet);
    }

    /**
     * Добавляет map-поле.
     */
    public <Key, Value> ComponentSerializer<T> addMap(
            String key,
            Function<T, Map<Key, Value>> getter,
            BiConsumer<T, Map<Key, Value>> setter,
            FieldCodec<Key> keyCodec,
            FieldCodec<Value> valueCodec
    ) {
        return add(key, FieldCodecs.map(keyCodec, valueCodec), getter, setter);
    }

    /**
     * Добавляет map-поле с ручным выбором сетевой синхронизации.
     */
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

    /**
     * Сериализует компонент в новый JSON объект.
     */
    public JsonObject serialize(T component) {
        JsonObject json = new JsonObject();
        serialize(json, component);
        return json;
    }

    /**
     * Дописывает поля компонента в уже существующий JSON объект.
     */
    public void serialize(JsonObject json, T component) {
        Objects.requireNonNull(json, "json");
        Objects.requireNonNull(component, "component");
        for (Entry<T, ?> field : fields) {
            field.toJson(json, component);
        }
    }

    /**
     * Читает поля из JSON и применяет их к переданному компоненту.
     */
    public T deserialize(JsonObject json, T component) {
        Objects.requireNonNull(json, "json");
        Objects.requireNonNull(component, "component");
        for (Entry<T, ?> field : fields) {
            field.fromJson(json, component);
        }
        return component;
    }

    /**
     * Создает компонент через factory и заполняет его из JSON.
     */
    public T deserialize(JsonObject json, Supplier<T> factory) {
        return deserialize(json, factory.get());
    }

    /**
     * Записывает сетевую схему и все синхронизируемые поля в буфер.
     */
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

    /**
     * Старый сетевой формат без schema hash и id полей.
     */
    public void toNetworkLegacy(FriendlyByteBuf buf, T component) {
        Objects.requireNonNull(buf, "buf");
        Objects.requireNonNull(component, "component");
        for (Entry<T, ?> field : fields) {
            if (field.shouldSyncNet) {
                field.toNetwork(buf, component);
            }
        }
    }

    /**
     * Читает сетевую схему и применяет полученные поля к компоненту.
     */
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

    /**
     * Читает старый сетевой формат без schema hash и id полей.
     */
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

    /**
     * Создает компонент через factory и заполняет его из сетевого буфера.
     */
    public T fromNetwork(FriendlyByteBuf buf, Supplier<T> factory) {
        return fromNetwork(buf, factory.get());
    }

    /**
     * Создает tracker для отслеживания изменений всех полей.
     */
    public SnapshotTracker<T> snapshotTracker(T component) {
        return new SnapshotTracker<>(this, takeSnapshot(component), false);
    }

    /**
     * Создает tracker для отслеживания изменений только сетевых полей.
     */
    public SnapshotTracker<T> networkSnapshotTracker(T component) {
        return new SnapshotTracker<>(this, takeNetworkSnapshot(component), true);
    }

    /**
     * Создает snapshot всех полей компонента.
     */
    public Snapshot takeSnapshot(T component) {
        return takeSnapshot(component, false);
    }

    /**
     * Создает snapshot только сетевых полей компонента.
     */
    public Snapshot takeNetworkSnapshot(T component) {
        return takeSnapshot(component, true);
    }

    /**
     * Проверяет, отличаются ли текущие поля от предыдущего snapshot.
     */
    public boolean hasChanges(T component, Snapshot previousSnapshot) {
        return !diff(previousSnapshot, takeSnapshot(component)).isEmpty();
    }

    /**
     * Возвращает snapshot только изменившихся полей.
     */
    public Snapshot diff(Snapshot previousSnapshot, T component) {
        return diff(previousSnapshot, takeSnapshot(component));
    }

    /**
     * Возвращает diff только если компонент помечен dirty.
     */
    public Snapshot diffIfDirty(T component, Snapshot previousSnapshot) {
        Objects.requireNonNull(component, "component");
        if (!component.consumeDirty()) return Snapshot.empty();
        return diff(previousSnapshot, component);
    }

    /**
     * Возвращает сетевой diff только если компонент помечен dirty.
     */
    public Snapshot networkDiffIfDirty(T component, Snapshot previousSnapshot) {
        Objects.requireNonNull(component, "component");
        if (!component.consumeDirty()) return Snapshot.empty();
        return diff(previousSnapshot, takeNetworkSnapshot(component));
    }

    /**
     * Сравнивает два snapshot и возвращает изменившиеся поля.
     */
    public Snapshot diff(Snapshot previousSnapshot, Snapshot nextSnapshot) {
        Snapshot previous = previousSnapshot == null ? Snapshot.empty() : previousSnapshot;
        Snapshot next = nextSnapshot == null ? Snapshot.empty() : nextSnapshot;
        Map<String, Object> changed = new Object2ObjectLinkedOpenHashMap<>();

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

    /**
     * Записывает сетевой diff между предыдущим snapshot и текущим компонентом.
     */
    public void writeNetworkDiff(FriendlyByteBuf buf, Snapshot previousSnapshot, T component) {
        writeNetworkDiff(buf, previousSnapshot, takeNetworkSnapshot(component));
    }

    /**
     * Записывает сетевой diff между двумя snapshot.
     */
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

    /**
     * Читает сетевой diff и применяет его к компоненту.
     */
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

    /**
     * Применяет значения из snapshot к компоненту.
     */
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

    /**
     * Возвращает ключи всех зарегистрированных полей.
     */
    public List<String> keys() {
        return List.copyOf(fieldsByKey.keySet());
    }

    /**
     * Возвращает ключи только сетевых полей.
     */
    public List<String> networkKeys() {
        List<String> keys = new ArrayList<>();
        for (Entry<T, ?> field : fields) {
            if (field.shouldSyncNet) {
                keys.add(field.key);
            }
        }
        return Collections.unmodifiableList(keys);
    }

    /**
     * Возвращает hash сетевой схемы для проверки совместимости.
     */
    public int networkSchemaHash() {
        if (cachedNetworkSchemaHash == 0) {
            cachedNetworkSchemaHash = computeNetworkSchemaHash();
        }
        return cachedNetworkSchemaHash;
    }

    /**
     * Возвращает стабильный id поля по его ключу или null, если поля нет.
     */
    public Integer fieldId(String key) {
        Entry<T, ?> field = fieldsByKey.get(key);
        return field == null ? null : field.id;
    }

    /**
     * Считает стабильный id поля по ключу.
     */
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
        Map<String, Object> values = new Object2ObjectLinkedOpenHashMap<>();
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

    /**
     * Снимок значений полей компонента на определенный момент.
     */
    public static final class Snapshot {
        private static final Snapshot EMPTY = new Snapshot(Collections.emptyMap());

        private final Map<String, Object> values;

        private Snapshot(Map<String, Object> values) {
            this.values = Collections.unmodifiableMap(values);
        }

        /**
         * Возвращает пустой snapshot без полей.
         */
        public static Snapshot empty() {
            return EMPTY;
        }

        /**
         * Проверяет, нет ли в snapshot значений.
         */
        public boolean isEmpty() {
            return values.isEmpty();
        }

        /**
         * Проверяет, содержит ли snapshot поле с указанным ключом.
         */
        public boolean contains(String key) {
            return values.containsKey(key);
        }

        /**
         * Возвращает значение поля из snapshot.
         */
        @SuppressWarnings("unchecked")
        public <Value> Value get(String key) {
            return (Value) values.get(key);
        }

        /**
         * Возвращает все значения snapshot как неизменяемую map.
         */
        public Map<String, Object> values() {
            return values;
        }

        private Snapshot filter(Predicate<String> keyFilter) {
            Map<String, Object> filtered = new Object2ObjectLinkedOpenHashMap<>();
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                if (keyFilter.test(entry.getKey())) {
                    filtered.put(entry.getKey(), entry.getValue());
                }
            }
            return new Snapshot(filtered);
        }
    }

    /**
     * Хранит предыдущий snapshot и помогает получать diff после изменений.
     */
    public static final class SnapshotTracker<ComponentType extends ShopComponent> {
        private final ComponentSerializer<ComponentType> serializer;
        private final boolean networkOnly;
        private Snapshot snapshot;

        private SnapshotTracker(ComponentSerializer<ComponentType> serializer, Snapshot snapshot, boolean networkOnly) {
            this.serializer = serializer;
            this.snapshot = snapshot;
            this.networkOnly = networkOnly;
        }

        /**
         * Возвращает текущий сохраненный snapshot.
         */
        public Snapshot snapshot() {
            return snapshot;
        }

        /**
         * Сравнивает компонент с сохраненным snapshot.
         */
        public Snapshot diff(ComponentType component) {
            return serializer.diff(snapshot, take(component));
        }

        /**
         * Проверяет, есть ли изменения относительно сохраненного snapshot.
         */
        public boolean hasChanges(ComponentType component) {
            return !diff(component).isEmpty();
        }

        /**
         * Обновляет сохраненный snapshot текущим состоянием компонента.
         */
        public Snapshot markClean(ComponentType component) {
            snapshot = take(component);
            return snapshot;
        }

        /**
         * Заменяет сохраненный snapshot вручную.
         */
        public void replace(Snapshot snapshot) {
            this.snapshot = snapshot == null ? Snapshot.empty() : snapshot;
        }

        /**
         * Записывает сетевой diff и сразу обновляет snapshot.
         */
        public void writeNetworkDiff(FriendlyByteBuf buf, ComponentType component) {
            serializer.writeNetworkDiff(buf, snapshot, component);
            markClean(component);
        }

        /**
         * Читает сетевой diff, применяет его и обновляет snapshot.
         */
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
