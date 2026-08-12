package dev.sixik.sdmshop2.libs.shop.serializer.codec;

import com.google.gson.*;
import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.math.BigDecimal;
import java.util.*;

/**
 * Набор готовых кодеков для часто используемых типов полей.
 */
public final class FieldCodecs {

    private static final ResourceLocation EMPTY_TAG_LOCATION = new ResourceLocation("minecraft", "air");

    /**
     * Кодек для значений boolean.
     */
    public static final FieldCodec<Boolean> BOOL = FieldCodec.<Boolean>builder()
            .schema("bool")
            .json(
                    JsonObject::addProperty,
                    (json, key, defaultValue) -> json.has(key) ? json.get(key).getAsBoolean() : defaultValue
            )
            .jsonElement(
                    value -> value == null ? JsonNull.INSTANCE : new JsonPrimitive(value),
                    (element, defaultValue) -> element.getAsBoolean()
            )
            .network(FriendlyByteBuf::writeBoolean, FriendlyByteBuf::readBoolean)
            .build();

    /**
     * Кодек для значений int через VarInt в сети.
     */
    public static final FieldCodec<Integer> INT = FieldCodec.<Integer>builder()
            .schema("int")
            .json(
                    JsonObject::addProperty,
                    (json, key, defaultValue) -> json.has(key) ? json.get(key).getAsInt() : defaultValue
            )
            .jsonElement(
                    value -> value == null ? JsonNull.INSTANCE : new JsonPrimitive(value),
                    (element, defaultValue) -> element.getAsInt()
            )
            .network(FriendlyByteBuf::writeVarInt, FriendlyByteBuf::readVarInt)
            .build();

    /**
     * Кодек для значений long через VarLong в сети.
     */
    public static final FieldCodec<Long> LONG = FieldCodec.<Long>builder()
            .schema("long")
            .json(
                    JsonObject::addProperty,
                    (json, key, defaultValue) -> json.has(key) ? json.get(key).getAsLong() : defaultValue
            )
            .jsonElement(
                    value -> value == null ? JsonNull.INSTANCE : new JsonPrimitive(value),
                    (element, defaultValue) -> element.getAsLong()
            )
            .network(FriendlyByteBuf::writeVarLong, FriendlyByteBuf::readVarLong)
            .build();

    /**
     * Кодек для значений float.
     */
    public static final FieldCodec<Float> FLOAT = FieldCodec.<Float>builder()
            .schema("float")
            .json(
                    JsonObject::addProperty,
                    (json, key, defaultValue) -> json.has(key) ? json.get(key).getAsFloat() : defaultValue
            )
            .jsonElement(
                    value -> value == null ? JsonNull.INSTANCE : new JsonPrimitive(value),
                    (element, defaultValue) -> element.getAsFloat()
            )
            .network(FriendlyByteBuf::writeFloat, FriendlyByteBuf::readFloat)
            .build();

    /**
     * Кодек для значений double.
     */
    public static final FieldCodec<Double> DOUBLE = FieldCodec.<Double>builder()
            .schema("double")
            .json(
                    JsonObject::addProperty,
                    (json, key, defaultValue) -> json.has(key) ? json.get(key).getAsDouble() : defaultValue
            )
            .jsonElement(
                    value -> value == null ? JsonNull.INSTANCE : new JsonPrimitive(value),
                    (element, defaultValue) -> element.getAsDouble()
            )
            .network(FriendlyByteBuf::writeDouble, FriendlyByteBuf::readDouble)
            .build();

    public static final FieldCodec<BigDecimal> BIG_DECIMAL = FieldCodec.<BigDecimal>builder()
            .schema("big_decimal")
            .json(
                    (json, key, value) -> json.addProperty(key, value == null ? BigDecimal.ZERO.toPlainString() : value.toPlainString()),
                    (json, key, defaultValue) -> json.has(key) ? new BigDecimal(json.get(key).getAsString()) : defaultValue
            )
            .jsonElement(
                    value -> new JsonPrimitive(value == null ? BigDecimal.ZERO.toPlainString() : value.toPlainString()),
                    (element, defaultValue) -> element == null || element.isJsonNull() ? defaultValue : new BigDecimal(element.getAsString())
            )
            .network(
                    (buf, value) -> buf.writeUtf(value == null ? BigDecimal.ZERO.toPlainString() : value.toPlainString()),
                    buf -> new BigDecimal(buf.readUtf())
            )
            .build();

    /**
     * Кодек для строк. null записывается как пустая строка.
     */
    public static final FieldCodec<String> STRING = FieldCodec.<String>builder()
            .schema("string")
            .json(
                    (json, key, value) -> json.addProperty(key, value == null ? "" : value),
                    (json, key, defaultValue) -> json.has(key) ? json.get(key).getAsString() : defaultValue
            )
            .jsonElement(
                    value -> new JsonPrimitive(value == null ? "" : value),
                    (element, defaultValue) -> element.getAsString()
            )
            .network(
                    (buf, value) -> buf.writeUtf(value == null ? "" : value),
                    FriendlyByteBuf::readUtf
            )
            .build();

    /**
     * Кодек для ResourceLocation.
     */
    public static final FieldCodec<ResourceLocation> RESOURCE_LOCATION = FieldCodec.<ResourceLocation>builder()
            .schema("resource_location")
            .json(
                    (json, key, value) -> {
                        if (value != null) json.addProperty(key, value.toString());
                    },
                    (json, key, defaultValue) -> json.has(key) ? ResourceLocation.tryParse(json.get(key).getAsString()) : defaultValue
            )
            .jsonElement(
                    value -> value == null ? JsonNull.INSTANCE : new JsonPrimitive(value.toString()),
                    (element, defaultValue) -> ResourceLocation.tryParse(element.getAsString())
            )
            .network(FriendlyByteBuf::writeResourceLocation, FriendlyByteBuf::readResourceLocation)
            .build();

    /**
     * Кодек для TagKey<Item>.
     */
    public static final FieldCodec<TagKey<Item>> ITEM_TAG = tagKey("item_tag", Registries.ITEM);

    /**
     * Кодек для TagKey<Block>.
     */
    public static final FieldCodec<TagKey<Block>> BLOCK_TAG = tagKey("block_tag", Registries.BLOCK);

    /**
     * Кодек для UUID.
     */
    public static final FieldCodec<UUID> UUID_CODEC = FieldCodec.<UUID>builder()
            .schema("uuid")
            .json(
                    (json, key, value) -> {
                        if (value != null) json.addProperty(key, value.toString());
                    },
                    (json, key, defaultValue) -> json.has(key) ? UUID.fromString(json.get(key).getAsString()) : defaultValue
            )
            .jsonElement(
                    value -> value == null ? JsonNull.INSTANCE : new JsonPrimitive(value.toString()),
                    (element, defaultValue) -> UUID.fromString(element.getAsString())
            )
            .network(FriendlyByteBuf::writeUUID, FriendlyByteBuf::readUUID)
            .build();

    /**
     * Кодек для ItemStack в полном NBT-представлении.
     */
    public static final FieldCodec<ItemStack> ITEM_STACK = FieldCodec.<ItemStack>builder()
            .schema("item_stack")
            .json(FieldCodecs::writeItemStackJson, FieldCodecs::readItemStackJson)
            .jsonElement(FieldCodecs::writeItemStackJsonElement, FieldCodecs::readItemStackJsonElement)
            .network(
                    (buf, value) -> buf.writeItem(value == null ? ItemStack.EMPTY : value),
                    FriendlyByteBuf::readItem
            )
            .copy(value -> value == null ? null : value.copy())
            .equality((first, second) -> {
                if (first == second) return true;
                if (first == null || second == null) return false;
                return ItemStack.matches(first, second);
            })
            .build();

    /**
     * Кодек для ItemStack как id предмета + отдельный nbt, совместимый со старым JSON-форматом награды предметом.
     */
    public static final FieldCodec<ItemStack> ITEM_STACK_ID_NBT = FieldCodec.<ItemStack>builder()
            .schema("item_stack_id_nbt")
            .json(FieldCodecs::writeItemStackIdNbtJson, FieldCodecs::readItemStackIdNbtJson)
            .jsonElement(FieldCodecs::writeItemStackIdNbtJsonElement, FieldCodecs::readItemStackIdNbtJsonElement)
            .network(
                    (buf, value) -> buf.writeItem(value == null ? ItemStack.EMPTY : value),
                    FriendlyByteBuf::readItem
            )
            .copy(value -> value == null ? null : value.copy())
            .equality((first, second) -> {
                if (first == second) return true;
                if (first == null || second == null) return false;
                return ItemStack.matches(first, second);
            })
            .build();

    /**
     * Кодек для вложенного ShopOffer.
     */
    public static final FieldCodec<ShopOffer> SHOP_OFFER = FieldCodec.<ShopOffer>builder()
            .schema("shop_offer")
            .json(
                    (json, key, value) -> {
                        if (value != null) {
                            json.add(key, value.serialize());
                        }
                    },
                    (json, key, defaultValue) -> {
                        if (!json.has(key)) return defaultValue;
                        JsonObject object = json.getAsJsonObject(key);
                        ShopOffer offer = ShopOffer.create(object.has("uuid")
                                ? UUID.fromString(object.get("uuid").getAsString())
                                : UUID.randomUUID(), true);
                        offer.deserialize(object);
                        return offer;
                    }
            )
            .jsonElement(
                    value -> value == null ? JsonNull.INSTANCE : value.serialize(),
                    (element, defaultValue) -> {
                        JsonObject object = element.getAsJsonObject();
                        ShopOffer offer = ShopOffer.create(object.has("uuid")
                                ? UUID.fromString(object.get("uuid").getAsString())
                                : UUID.randomUUID(), true);
                        offer.deserialize(object);
                        return offer;
                    }
            )
            .network(
                    (buf, value) -> {
                        if (value == null) {
                            ShopOffer.create(UUID.randomUUID(), false).serializeNetwork(buf);
                        } else {
                            value.serializeNetwork(buf);
                        }
                    },
                    ShopOffer::fromNetwork
            )
            .build();

    /**
     * Кодек для вложенного ShopInstance.
     */
    public static final FieldCodec<ShopInstance> SHOP_INSTANCE = FieldCodec.<ShopInstance>builder()
            .schema("shop_instance")
            .json(
                    (json, key, value) -> {
                        if (value != null) {
                            json.add(key, value.serialize());
                        }
                    },
                    (json, key, defaultValue) -> json.has(key) ? ShopInstance.fromJson(json.get(key)) : defaultValue
            )
            .jsonElement(
                    value -> value == null ? JsonNull.INSTANCE : value.serialize(),
                    (element, defaultValue) -> ShopInstance.fromJson(element)
            )
            .network(
                    (buf, value) -> {
                        if (value == null) {
                            ShopInstance.createManager(ShopInstance.NULL_MANAGER, false).serializeNetwork(buf);
                        } else {
                            value.serializeNetwork(buf);
                        }
                    },
                    ShopInstance::fromNetwork
            )
            .build();

    /**
     * Делает кодек допускающим null: значение может быть null в JSON и сети.
     */
    public static <Value> FieldCodec<Value> nullable(FieldCodec<Value> codec) {
        Objects.requireNonNull(codec, "codec");
        return FieldCodec.<Value>builder()
                .schema("nullable(" + codec.schemaName() + ")")
                .json(
                        (json, key, value) -> json.add(key, value == null ? JsonNull.INSTANCE : codec.toJsonElement(value)),
                        (json, key, defaultValue) -> {
                            if (!json.has(key)) return defaultValue;
                            JsonElement element = json.get(key);
                            if (element == null || element.isJsonNull()) return null;
                            return codec.fromJsonElement(element, defaultValue);
                        }
                )
                .jsonElement(
                        value -> value == null ? JsonNull.INSTANCE : codec.toJsonElement(value),
                        (element, defaultValue) -> element == null || element.isJsonNull() ? null : codec.fromJsonElement(element, defaultValue)
                )
                .network(
                        (buf, value) -> {
                            buf.writeBoolean(value != null);
                            if (value != null) codec.toNetwork(buf, value);
                        },
                        buf -> buf.readBoolean() ? codec.fromNetwork(buf) : null
                )
                .copy(codec::copy)
                .equality(codec::areEqual)
                .build();
    }

    /**
     * Создает кодек для списка значений одного типа.
     */
    public static <Element> FieldCodec<List<Element>> list(FieldCodec<Element> elementCodec) {
        Objects.requireNonNull(elementCodec, "elementCodec");
        return FieldCodec.<List<Element>>builder()
                .schema("list(" + elementCodec.schemaName() + ")")
                .json(
                        (json, key, value) -> json.add(key, writeListJsonElement(value, elementCodec)),
                        (json, key, defaultValue) -> {
                            if (!json.has(key)) return defaultValue;
                            return readListJsonElement(json.get(key), defaultValue, elementCodec);
                        }
                )
                .jsonElement(
                        value -> writeListJsonElement(value, elementCodec),
                        (element, defaultValue) -> readListJsonElement(element, defaultValue, elementCodec)
                )
                .network(
                        (buf, value) -> {
                            int size = value == null ? 0 : value.size();
                            buf.writeVarInt(size);
                            if (value != null) {
                                for (Element element : value) {
                                    elementCodec.toNetwork(buf, element);
                                }
                            }
                        },
                        buf -> {
                            int size = buf.readVarInt();
                            List<Element> list = new ArrayList<>(size);
                            for (int i = 0; i < size; i++) {
                                list.add(elementCodec.fromNetwork(buf));
                            }
                            return list;
                        }
                )
                .copy(value -> {
                    if (value == null) return null;
                    List<Element> copy = new ArrayList<>(value.size());
                    for (Element element : value) {
                        copy.add(elementCodec.copy(element));
                    }
                    return copy;
                })
                .equality((first, second) -> listEquals(first, second, elementCodec))
                .build();
    }

    /**
     * Создает кодек для ассоциативной карты, где ключ и значение имеют свои кодеки.
     */
    public static <Key, Value> FieldCodec<Map<Key, Value>> map(FieldCodec<Key> keyCodec, FieldCodec<Value> valueCodec) {
        Objects.requireNonNull(keyCodec, "keyCodec");
        Objects.requireNonNull(valueCodec, "valueCodec");
        return FieldCodec.<Map<Key, Value>>builder()
                .schema("map(" + keyCodec.schemaName() + "," + valueCodec.schemaName() + ")")
                .json(
                        (json, key, value) -> json.add(key, writeMapJsonElement(value, keyCodec, valueCodec)),
                        (json, key, defaultValue) -> {
                            if (!json.has(key)) return defaultValue;
                            return readMapJsonElement(json.get(key), defaultValue, keyCodec, valueCodec);
                        }
                )
                .jsonElement(
                        value -> writeMapJsonElement(value, keyCodec, valueCodec),
                        (element, defaultValue) -> readMapJsonElement(element, defaultValue, keyCodec, valueCodec)
                )
                .network(
                        (buf, value) -> {
                            int size = value == null ? 0 : value.size();
                            buf.writeVarInt(size);
                            if (value != null) {
                                for (Map.Entry<Key, Value> entry : value.entrySet()) {
                                    keyCodec.toNetwork(buf, entry.getKey());
                                    valueCodec.toNetwork(buf, entry.getValue());
                                }
                            }
                        },
                        buf -> {
                            int size = buf.readVarInt();
                            Map<Key, Value> map = new Object2ObjectLinkedOpenHashMap<>(size);
                            for (int i = 0; i < size; i++) {
                                map.put(keyCodec.fromNetwork(buf), valueCodec.fromNetwork(buf));
                            }
                            return map;
                        }
                )
                .copy(value -> {
                    if (value == null) return null;
                    Map<Key, Value> copy = new Object2ObjectLinkedOpenHashMap<>(value.size());
                    for (Map.Entry<Key, Value> entry : value.entrySet()) {
                        copy.put(keyCodec.copy(entry.getKey()), valueCodec.copy(entry.getValue()));
                    }
                    return copy;
                })
                .equality((first, second) -> mapEquals(first, second, keyCodec, valueCodec))
                .build();
    }

    /**
     * Создает кодек для enum. JSON дополнительно принимает значение в другом регистре.
     */
    public static <EnumType extends Enum<EnumType>> FieldCodec<EnumType> enumCodec(Class<EnumType> enumClass) {
        Objects.requireNonNull(enumClass, "enumClass");
        return FieldCodec.<EnumType>builder()
                .schema("enum(" + enumClass.getName() + ")")
                .json(
                        (json, key, value) -> {
                            if (value != null) json.addProperty(key, value.name());
                        },
                        (json, key, defaultValue) -> {
                            if (!json.has(key)) return defaultValue;
                            String value = json.get(key).getAsString();
                            try {
                                return Enum.valueOf(enumClass, value);
                            } catch (IllegalArgumentException exception) {
                                return Enum.valueOf(enumClass, value.toUpperCase(Locale.ROOT));
                            }
                        }
                )
                .jsonElement(
                        value -> value == null ? JsonNull.INSTANCE : new JsonPrimitive(value.name()),
                        (element, defaultValue) -> {
                            String value = element.getAsString();
                            try {
                                return Enum.valueOf(enumClass, value);
                            } catch (IllegalArgumentException exception) {
                                return Enum.valueOf(enumClass, value.toUpperCase(Locale.ROOT));
                            }
                        }
                )
                .network(
                        FriendlyByteBuf::writeEnum,
                        buf -> buf.readEnum(enumClass)
                )
                .build();
    }

    public static <Value> FieldCodec<TagKey<Value>> tagKey(String schemaName, ResourceKey<? extends Registry<Value>> registryKey) {
        Objects.requireNonNull(schemaName, "schemaName");
        Objects.requireNonNull(registryKey, "registryKey");
        return FieldCodec.<TagKey<Value>>builder()
                .schema(schemaName)
                .json(
                        (json, key, value) -> {
                            if (value != null) json.addProperty(key, value.location().toString());
                        },
                        (json, key, defaultValue) -> json.has(key)
                                ? readTagKey(json.get(key), defaultValue, registryKey)
                                : defaultValue
                )
                .jsonElement(
                        value -> value == null ? JsonNull.INSTANCE : new JsonPrimitive(value.location().toString()),
                        (element, defaultValue) -> readTagKey(element, defaultValue, registryKey)
                )
                .network(
                        (buf, value) -> buf.writeResourceLocation(value == null ? EMPTY_TAG_LOCATION : value.location()),
                        buf -> TagKey.create(registryKey, buf.readResourceLocation())
                )
                .build();
    }

    private static <Value> TagKey<Value> readTagKey(
            JsonElement element,
            TagKey<Value> defaultValue,
            ResourceKey<? extends Registry<Value>> registryKey
    ) {
        if (element == null || element.isJsonNull()) return defaultValue;
        ResourceLocation location = ResourceLocation.tryParse(element.getAsString());
        return location == null ? defaultValue : TagKey.create(registryKey, location);
    }

    private static <Element> JsonArray writeListJsonElement(List<Element> value, FieldCodec<Element> elementCodec) {
        JsonArray array = new JsonArray();
        if (value != null) {
            for (Element element : value) {
                array.add(elementCodec.toJsonElement(element));
            }
        }
        return array;
    }

    private static <Element> List<Element> readListJsonElement(
            JsonElement element,
            List<Element> defaultValue,
            FieldCodec<Element> elementCodec
    ) {
        if (element == null) return defaultValue;

        JsonArray array = element.getAsJsonArray();
        List<Element> list = new ArrayList<>(array.size());
        for (JsonElement entry : array) {
            list.add(elementCodec.fromJsonElement(entry, null));
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private static <Key, Value> JsonElement writeMapJsonElement(
            Map<Key, Value> value,
            FieldCodec<Key> keyCodec,
            FieldCodec<Value> valueCodec
    ) {
        if (keyCodec == STRING) {
            return writeStringKeyMapJsonElement((Map<String, Value>) value, valueCodec);
        }

        JsonArray array = new JsonArray();
        if (value != null) {
            for (Map.Entry<Key, Value> entry : value.entrySet()) {
                JsonObject object = new JsonObject();
                object.add("key", keyCodec.toJsonElement(entry.getKey()));
                object.add("value", valueCodec.toJsonElement(entry.getValue()));
                array.add(object);
            }
        }
        return array;
    }

    private static <Value> JsonObject writeStringKeyMapJsonElement(Map<String, Value> value, FieldCodec<Value> valueCodec) {
        JsonObject object = new JsonObject();
        if (value != null) {
            for (Map.Entry<String, Value> entry : value.entrySet()) {
                object.add(entry.getKey(), valueCodec.toJsonElement(entry.getValue()));
            }
        }
        return object;
    }

    @SuppressWarnings("unchecked")
    private static <Key, Value> Map<Key, Value> readMapJsonElement(
            JsonElement element,
            Map<Key, Value> defaultValue,
            FieldCodec<Key> keyCodec,
            FieldCodec<Value> valueCodec
    ) {
        if (element == null) return defaultValue;
        if (keyCodec == STRING && element.isJsonObject()) {
            return (Map<Key, Value>) readStringKeyMapJsonElement(element.getAsJsonObject(), valueCodec);
        }

        JsonArray array = element.getAsJsonArray();
        Map<Key, Value> map = new Object2ObjectLinkedOpenHashMap<>(array.size());
        for (JsonElement entry : array) {
            JsonObject object = entry.getAsJsonObject();
            Key mapKey = keyCodec.fromJsonElement(object.get("key"), null);
            Value mapValue = valueCodec.fromJsonElement(object.get("value"), null);
            map.put(mapKey, mapValue);
        }
        return map;
    }

    private static <Value> Map<String, Value> readStringKeyMapJsonElement(JsonObject object, FieldCodec<Value> valueCodec) {
        Map<String, Value> map = new Object2ObjectLinkedOpenHashMap<>(object.size());
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            map.put(entry.getKey(), valueCodec.fromJsonElement(entry.getValue(), null));
        }
        return map;
    }

    private static void writeItemStackJson(JsonObject json, String key, ItemStack value) {
        CompoundTag tag = new CompoundTag();
        (value == null ? ItemStack.EMPTY : value).save(tag);
        json.addProperty(key, tag.toString());
    }

    private static JsonElement writeItemStackJsonElement(ItemStack value) {
        CompoundTag tag = new CompoundTag();
        (value == null ? ItemStack.EMPTY : value).save(tag);
        return new JsonPrimitive(tag.toString());
    }

    private static ItemStack readItemStackJson(JsonObject json, String key, ItemStack defaultValue) {
        if (!json.has(key)) return defaultValue;
        try {
            CompoundTag tag = TagParser.parseTag(json.get(key).getAsString());
            return ItemStack.of(tag);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid ItemStack json for key '" + key + "'", exception);
        }
    }

    private static ItemStack readItemStackJsonElement(JsonElement element, ItemStack defaultValue) {
        if (element == null) return defaultValue;
        try {
            CompoundTag tag = TagParser.parseTag(element.getAsString());
            return ItemStack.of(tag);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid ItemStack json element", exception);
        }
    }

    private static void writeItemStackIdNbtJson(JsonObject json, String key, ItemStack value) {
        ItemStack stack = value == null ? ItemStack.EMPTY : value;
        json.addProperty(key, BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
        if (stack.getTag() != null) {
            json.addProperty("nbt", stack.getTag().toString());
        }
    }

    private static JsonElement writeItemStackIdNbtJsonElement(ItemStack value) {
        ItemStack stack = value == null ? ItemStack.EMPTY : value;
        JsonObject object = new JsonObject();
        object.addProperty("item", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
        if (stack.getTag() != null) {
            object.addProperty("nbt", stack.getTag().toString());
        }
        return object;
    }

    private static ItemStack readItemStackIdNbtJson(JsonObject json, String key, ItemStack defaultValue) {
        if (!json.has(key)) return defaultValue;

        String itemIdString = json.get(key).getAsString();
        ResourceLocation itemId = ResourceLocation.tryParse(itemIdString);
        Item item = BuiltInRegistries.ITEM.get(itemId);
        if (item == null) {
            throw new IllegalArgumentException("Item not found: " + itemIdString);
        }

        ItemStack itemStack = item.getDefaultInstance();
        if (json.has("nbt")) {
            try {
                itemStack.setTag(TagParser.parseTag(json.get("nbt").getAsString()));
            } catch (Exception exception) {
                throw new IllegalArgumentException("Invalid ItemStack NBT for key '" + key + "'", exception);
            }
        }
        return itemStack;
    }

    private static ItemStack readItemStackIdNbtJsonElement(JsonElement element, ItemStack defaultValue) {
        if (element == null) return defaultValue;
        if (!element.isJsonObject()) {
            JsonObject object = new JsonObject();
            object.add("item", element);
            return readItemStackIdNbtJson(object, "item", defaultValue);
        }

        JsonObject object = element.getAsJsonObject();
        return readItemStackIdNbtJson(object, object.has("item") ? "item" : "value", defaultValue);
    }

    private static <Element> boolean listEquals(List<Element> first, List<Element> second, FieldCodec<Element> elementCodec) {
        if (first == second) return true;
        if (first == null || second == null || first.size() != second.size()) return false;
        for (int i = 0; i < first.size(); i++) {
            if (!elementCodec.areEqual(first.get(i), second.get(i))) return false;
        }
        return true;
    }

    private static <Key, Value> boolean mapEquals(
            Map<Key, Value> first,
            Map<Key, Value> second,
            FieldCodec<Key> keyCodec,
            FieldCodec<Value> valueCodec
    ) {
        if (first == second) return true;
        if (first == null || second == null || first.size() != second.size()) return false;
        for (Map.Entry<Key, Value> entry : first.entrySet()) {
            Key key = entry.getKey();
            if (second.containsKey(key)) {
                if (!valueCodec.areEqual(entry.getValue(), second.get(key))) return false;
                continue;
            }

            KeyMatch<Key> match = findMatchingKey(key, second, keyCodec);
            if (!match.found()) return false;
            if (!valueCodec.areEqual(entry.getValue(), second.get(match.key()))) return false;
        }
        return true;
    }

    private static <Key, Value> KeyMatch<Key> findMatchingKey(Key key, Map<Key, Value> map, FieldCodec<Key> keyCodec) {
        for (Key candidate : map.keySet()) {
            if (keyCodec.areEqual(key, candidate)) return new KeyMatch<>(true, candidate);
        }
        return new KeyMatch<>(false, null);
    }

    private record KeyMatch<Key>(boolean found, Key key) {
    }

    private FieldCodecs() {
    }
}
