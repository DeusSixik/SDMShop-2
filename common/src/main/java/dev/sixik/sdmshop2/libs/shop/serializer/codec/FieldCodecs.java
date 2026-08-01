package dev.sixik.sdmshop2.libs.shop.serializer.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Набор готовых codec-ов для часто используемых типов полей.
 */
public final class FieldCodecs {

    /** Codec для boolean значений. */
    public static final FieldCodec<Boolean> BOOL = FieldCodec.<Boolean>builder()
            .schema("bool")
            .json(
                    (json, key, value) -> json.addProperty(key, value),
                    (json, key, defaultValue) -> json.has(key) ? json.get(key).getAsBoolean() : defaultValue
            )
            .network(FriendlyByteBuf::writeBoolean, FriendlyByteBuf::readBoolean)
            .build();

    /** Codec для int значений через VarInt в сети. */
    public static final FieldCodec<Integer> INT = FieldCodec.<Integer>builder()
            .schema("int")
            .json(
                    (json, key, value) -> json.addProperty(key, value),
                    (json, key, defaultValue) -> json.has(key) ? json.get(key).getAsInt() : defaultValue
            )
            .network(FriendlyByteBuf::writeVarInt, FriendlyByteBuf::readVarInt)
            .build();

    /** Codec для long значений через VarLong в сети. */
    public static final FieldCodec<Long> LONG = FieldCodec.<Long>builder()
            .schema("long")
            .json(
                    (json, key, value) -> json.addProperty(key, value),
                    (json, key, defaultValue) -> json.has(key) ? json.get(key).getAsLong() : defaultValue
            )
            .network(FriendlyByteBuf::writeVarLong, FriendlyByteBuf::readVarLong)
            .build();

    /** Codec для float значений. */
    public static final FieldCodec<Float> FLOAT = FieldCodec.<Float>builder()
            .schema("float")
            .json(
                    (json, key, value) -> json.addProperty(key, value),
                    (json, key, defaultValue) -> json.has(key) ? json.get(key).getAsFloat() : defaultValue
            )
            .network(FriendlyByteBuf::writeFloat, FriendlyByteBuf::readFloat)
            .build();

    /** Codec для double значений. */
    public static final FieldCodec<Double> DOUBLE = FieldCodec.<Double>builder()
            .schema("double")
            .json(
                    (json, key, value) -> json.addProperty(key, value),
                    (json, key, defaultValue) -> json.has(key) ? json.get(key).getAsDouble() : defaultValue
            )
            .network(FriendlyByteBuf::writeDouble, FriendlyByteBuf::readDouble)
            .build();

    /** Codec для строк. null записывается как пустая строка. */
    public static final FieldCodec<String> STRING = FieldCodec.<String>builder()
            .schema("string")
            .json(
                    (json, key, value) -> json.addProperty(key, value == null ? "" : value),
                    (json, key, defaultValue) -> json.has(key) ? json.get(key).getAsString() : defaultValue
            )
            .network(
                    (buf, value) -> buf.writeUtf(value == null ? "" : value),
                    FriendlyByteBuf::readUtf
            )
            .build();

    /** Codec для ResourceLocation. */
    public static final FieldCodec<ResourceLocation> RESOURCE_LOCATION = FieldCodec.<ResourceLocation>builder()
            .schema("resource_location")
            .json(
                    (json, key, value) -> {
                        if (value != null) json.addProperty(key, value.toString());
                    },
                    (json, key, defaultValue) -> json.has(key) ? ResourceLocation.tryParse(json.get(key).getAsString()) : defaultValue
            )
            .network(FriendlyByteBuf::writeResourceLocation, FriendlyByteBuf::readResourceLocation)
            .build();

    /** Codec для UUID. */
    public static final FieldCodec<UUID> UUID_CODEC = FieldCodec.<UUID>builder()
            .schema("uuid")
            .json(
                    (json, key, value) -> {
                        if (value != null) json.addProperty(key, value.toString());
                    },
                    (json, key, defaultValue) -> json.has(key) ? UUID.fromString(json.get(key).getAsString()) : defaultValue
            )
            .network(FriendlyByteBuf::writeUUID, FriendlyByteBuf::readUUID)
            .build();

    /** Codec для ItemStack в полном NBT-представлении. */
    public static final FieldCodec<ItemStack> ITEM_STACK = FieldCodec.<ItemStack>builder()
            .schema("item_stack")
            .json(FieldCodecs::writeItemStackJson, FieldCodecs::readItemStackJson)
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

    /** Codec для ItemStack как id предмета + отдельный nbt, совместимый со старым JSON форматом item reward. */
    public static final FieldCodec<ItemStack> ITEM_STACK_ID_NBT = FieldCodec.<ItemStack>builder()
            .schema("item_stack_id_nbt")
            .json(FieldCodecs::writeItemStackIdNbtJson, FieldCodecs::readItemStackIdNbtJson)
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

    /** Codec для вложенного ShopOffer. */
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

    /** Codec для вложенного ShopInstance. */
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
     * Делает codec nullable: значение может быть null в JSON и сети.
     */
    public static <Value> FieldCodec<Value> nullable(FieldCodec<Value> codec) {
        Objects.requireNonNull(codec, "codec");
        return FieldCodec.<Value>builder()
                .schema("nullable(" + codec.schemaName() + ")")
                .json(
                        (json, key, value) -> {
                            if (value == null) {
                                json.add(key, JsonNull.INSTANCE);
                            } else {
                                JsonObject nested = new JsonObject();
                                codec.toJson(nested, "value", value);
                                json.add(key, nested.get("value"));
                            }
                        },
                        (json, key, defaultValue) -> {
                            if (!json.has(key)) return defaultValue;
                            JsonElement element = json.get(key);
                            if (element == null || element.isJsonNull()) return null;
                            JsonObject nested = new JsonObject();
                            nested.add("value", element);
                            return codec.fromJson(nested, "value", defaultValue);
                        }
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
     * Создает codec для списка значений одного типа.
     */
    public static <Element> FieldCodec<List<Element>> list(FieldCodec<Element> elementCodec) {
        Objects.requireNonNull(elementCodec, "elementCodec");
        return FieldCodec.<List<Element>>builder()
                .schema("list(" + elementCodec.schemaName() + ")")
                .json(
                        (json, key, value) -> {
                            JsonArray array = new JsonArray();
                            if (value != null) {
                                for (Element element : value) {
                                    JsonObject nested = new JsonObject();
                                    elementCodec.toJson(nested, "value", element);
                                    array.add(nested.get("value"));
                                }
                            }
                            json.add(key, array);
                        },
                        (json, key, defaultValue) -> {
                            if (!json.has(key)) return defaultValue;
                            JsonArray array = json.getAsJsonArray(key);
                            List<Element> list = new ArrayList<>(array.size());
                            for (JsonElement element : array) {
                                JsonObject nested = new JsonObject();
                                nested.add("value", element);
                                list.add(elementCodec.fromJson(nested, "value", null));
                            }
                            return list;
                        }
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
     * Создает codec для map, где ключ и значение имеют свои codec-и.
     */
    public static <Key, Value> FieldCodec<Map<Key, Value>> map(FieldCodec<Key> keyCodec, FieldCodec<Value> valueCodec) {
        Objects.requireNonNull(keyCodec, "keyCodec");
        Objects.requireNonNull(valueCodec, "valueCodec");
        return FieldCodec.<Map<Key, Value>>builder()
                .schema("map(" + keyCodec.schemaName() + "," + valueCodec.schemaName() + ")")
                .json(
                        (json, key, value) -> {
                            JsonArray array = new JsonArray();
                            if (value != null) {
                                for (Map.Entry<Key, Value> entry : value.entrySet()) {
                                    JsonObject object = new JsonObject();
                                    keyCodec.toJson(object, "key", entry.getKey());
                                    valueCodec.toJson(object, "value", entry.getValue());
                                    array.add(object);
                                }
                            }
                            json.add(key, array);
                        },
                        (json, key, defaultValue) -> {
                            if (!json.has(key)) return defaultValue;
                            JsonArray array = json.getAsJsonArray(key);
                            Map<Key, Value> map = new Object2ObjectLinkedOpenHashMap<>(array.size());
                            for (JsonElement element : array) {
                                JsonObject object = element.getAsJsonObject();
                                Key mapKey = keyCodec.fromJson(object, "key", null);
                                Value mapValue = valueCodec.fromJson(object, "value", null);
                                map.put(mapKey, mapValue);
                            }
                            return map;
                        }
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
     * Создает codec для enum. JSON дополнительно принимает значение в другом регистре.
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
                .network(
                        (buf, value) -> buf.writeEnum(value),
                        buf -> buf.readEnum(enumClass)
                )
                .build();
    }

    private static void writeItemStackJson(JsonObject json, String key, ItemStack value) {
        CompoundTag tag = new CompoundTag();
        (value == null ? ItemStack.EMPTY : value).save(tag);
        json.addProperty(key, tag.toString());
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

    private static void writeItemStackIdNbtJson(JsonObject json, String key, ItemStack value) {
        ItemStack stack = value == null ? ItemStack.EMPTY : value;
        json.addProperty(key, BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
        if (stack.getTag() != null) {
            json.addProperty("nbt", stack.getTag().toString());
        }
    }

    private static ItemStack readItemStackIdNbtJson(JsonObject json, String key, ItemStack defaultValue) {
        if (!json.has(key)) return defaultValue;

        String itemIdString = json.get(key).getAsString();
        ResourceLocation itemId = ResourceLocation.tryParse(itemIdString);
        Item item = BuiltInRegistries.ITEM.get(itemId);
        if (item == null || item == Items.AIR) {
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
