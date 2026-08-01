package dev.sixik.sdmshop2.libs.shop.components.api;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.sixik.sdmshop2.libs.shop.components.money.MoneyCostComponent;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Реестр типов компонентов магазина.
 * Используется для регистрации новых типов компонентов и обеспечения их
 * сериализации/десериализации при работе с JSON и сетью.
 */
public class ShopComponentRegistry {

    private static final Map<ResourceLocation, IComponentType<?>> TYPES = new HashMap<>();

    /**
     * Регистрирует новый тип компонента.
     *
     * @param type Тип компонента для регистрации
     */
    public static void register(IComponentType<?> type) {
        TYPES.put(type.getId(), type);
    }

    /**
     * Регистрирует тип компонента и его категорию за один вызов.
     * Удобно для аддонов, которым нужно добавить собственный компонент в кастомную категорию.
     *
     * @param type           Тип компонента для регистрации
     * @param componentClass Класс компонента, который будет попадать в категорию
     * @param category       Категория компонента
     * @param <T>            Тип компонента
     */
    public static <T extends ShopComponent> void register(IComponentType<T> type, Class<T> componentClass, ShopComponentCategory category) {
        register(type);
        ShopComponentCategory.registerComponent(componentClass, category);
    }

    /**
     * Возвращает тип компонента по его идентификатору.
     *
     * @param id Идентификатор типа
     * @return Optional, содержащий тип компонента, если он найден
     */
    public static Optional<IComponentType<?>> getType(ResourceLocation id) {
        return Optional.ofNullable(TYPES.get(id));
    }

    /**
     * Возвращает копию карты всех зарегистрированных типов компонентов.
     *
     * @return Карта типов компонентов
     */
    public static Map<ResourceLocation, IComponentType<?>> getTypes() {
        return Maps.newHashMap(TYPES);
    }

    /**
     * Сериализует компонент в JsonObject, автоматически добавляя поле "type".
     *
     * @param component Компонент для сериализации
     * @return JSON объект компонента
     */
    public static JsonObject toJson(ShopComponent component) {
        IComponentType type = component.getType();

        final JsonObject json = type.serialize(component);
        serializeAdditional(json, component);
        json.addProperty("type", type.getId().toString());
        return json;
    }

    /**
     * Создает компонент из JsonObject на основе поля "type".
     *
     * @param json JSON объект с данными компонента
     * @return Восстановленный экземпляр компонента
     * @throws JsonSyntaxException если поле "type" отсутствует или тип неизвестен
     */
    public static ShopComponent fromJson(JsonObject json) {
        if (!json.has("type")) throw new JsonSyntaxException("Component missing 'type'");
        ResourceLocation id = new ResourceLocation(json.get("type").getAsString());

        IComponentType<?> type = TYPES.get(id);
        if (type == null) throw new JsonSyntaxException("Unknown component type: " + id);
        final ShopComponent component = type.deserialize(json);
        deserializeAdditional(json, component);
        return component;
    }

    /**
     * Записывает компонент в сетевой буфер.
     * Сначала записывается идентификатор типа, затем данные компонента.
     *
     * @param buf       Сетевой буфер
     * @param component Компонент для записи
     */
    public static void toNetwork(FriendlyByteBuf buf, ShopComponent component) {
        IComponentType type = component.getType();
        buf.writeResourceLocation(type.getId());
        type.toNetwork(buf, component);
        toNetworkAdditional(buf, component);
    }

    /**
     * Считывает компонент из сетевого буфера.
     * Сначала считывается идентификатор типа для поиска соответствующего десериализатора.
     *
     * @param buf Сетевой буфер
     * @return Восстановленный экземпляр компонента
     */
    public static ShopComponent fromNetwork(FriendlyByteBuf buf) {
        ResourceLocation id = buf.readResourceLocation();
        IComponentType<?> type = TYPES.get(id);
        final ShopComponent component = type.fromNetwork(buf);
        fromNetworkAdditional(buf, component);
        return component;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void serializeAdditional(JsonObject json, ShopComponent component) {
        ComponentSerializer serializer = component.additionalSerializer();
        if (hasAdditionalFields(serializer)) {
            serializer.serialize(json, component);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void deserializeAdditional(JsonObject json, ShopComponent component) {
        ComponentSerializer serializer = component.additionalSerializer();
        if (hasAdditionalFields(serializer)) {
            serializer.deserialize(json, component);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void toNetworkAdditional(FriendlyByteBuf buf, ShopComponent component) {
        ComponentSerializer serializer = component.additionalSerializer();
        if (hasAdditionalNetworkFields(serializer)) {
            serializer.toNetwork(buf, component);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void fromNetworkAdditional(FriendlyByteBuf buf, ShopComponent component) {
        ComponentSerializer serializer = component.additionalSerializer();
        if (hasAdditionalNetworkFields(serializer)) {
            serializer.fromNetwork(buf, component);
        }
    }

    @SuppressWarnings("rawtypes")
    private static boolean hasAdditionalFields(ComponentSerializer serializer) {
        return serializer != null && !serializer.keys().isEmpty();
    }

    @SuppressWarnings("rawtypes")
    private static boolean hasAdditionalNetworkFields(ComponentSerializer serializer) {
        return serializer != null && !serializer.networkKeys().isEmpty();
    }
}
