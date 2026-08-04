package dev.sixik.sdmshop2.libs.shop.base.limiter;

import com.google.gson.JsonObject;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Контейнер персональных данных лимитов для конкретного игрока.
 * Хранит ассоциативный массив (Map), где ключом является UUID товара,
 * а значением — данные о количестве покупок этого товара данным игроком.
 */
public class ShopLimiterPlayerData {

    @Getter
    private final UUID userId;

    private volatile ShopLimiterUpdate update = () -> {};

    private final Map<UUID, ShopLimiterOfferData> dataMap;

    public ShopLimiterPlayerData(UUID userId, FriendlyByteBuf buf) {
        this.userId = userId;
        int size = buf.readVarInt();
        dataMap = new ConcurrentHashMap<>();
        for (int i = 0; i < size; i++) {
            ShopLimiterOfferData data = new ShopLimiterOfferData(buf);
            data.setUpdate(update);
            dataMap.put(data.getOfferId(), data);
        }
    }

    public ShopLimiterPlayerData(JsonObject jsonObject) {
        this(readUserId(jsonObject));
        readData(readDataObject(jsonObject), false);
    }

    public ShopLimiterPlayerData(UUID userId) {
        this.userId = userId;
        this.dataMap = new ConcurrentHashMap<>();
    }

    public void setUpdate(ShopLimiterUpdate update) {
        this.update = Objects.requireNonNullElseGet(update, () -> () -> {});
        dataMap.values().forEach(data -> data.setUpdate(this.update));
    }

    public boolean add(UUID entityId) {
        return add(entityId, 0);
    }

    public boolean add(UUID entityId, int count) {
        final ShopLimiterOfferData newData = new ShopLimiterOfferData(entityId, count);
        newData.setUpdate(update);
        final ShopLimiterOfferData data = dataMap.putIfAbsent(entityId, newData);

        if(data != null) {
            if (count > 0) {
                data.add(count);
            } else {
                data.markPurchased();
            }
        } else {
            update.onUpdate();
        }

        return data == null;
    }

    public boolean remove(UUID entityId) {
       boolean removed = dataMap.remove(entityId) != null;
       if (removed) {
           update.onUpdate();
       }
       return removed;
    }

    public ShopLimiterOfferData getData(UUID entityId) {
        return dataMap.computeIfAbsent(entityId, id -> {
            ShopLimiterOfferData data = new ShopLimiterOfferData(id);
            data.setUpdate(update);
            return data;
        });
    }

    /**
     * Увеличивает счетчик покупок указанного товара на заданное значение
     * и возвращает текущее значение (ДО прибавления).
     *
     * @param userId UUID товара (обратите внимание: в вашем коде переменная названа userId, но логически это entityId товара)
     * @param count Количество добавляемых покупок
     * @return Количество покупок до выполнения операции
     */
    public int getAndUpdate(UUID userId, int count) {
        final ShopLimiterOfferData data = getData(userId);
        int value = data.getCount().getAndUpdate(i -> i + count);
        data.markPurchased();
        return value;
    }

    public int getAndAdd(UUID userId, int count) {
        final ShopLimiterOfferData data = getData(userId);
        int value = data.getCount().getAndAdd(count);
        data.markPurchased();
        return value;
    }

    /**
     * Возвращает текущее количество покупок для указанного товара.
     *
     * @param userId UUID товара
     * @return Текущее количество покупок
     */
    public int get(UUID userId) {
        return getData(userId).getCount().get();
    }

    public void clear() {
        if (dataMap.isEmpty()) {
            return;
        }

        dataMap.clear();
        update.onUpdate();
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("userId", userId.toString());

        JsonObject data = new JsonObject();
        dataMap.forEach((entityId, offerData) -> data.add(entityId.toString(), offerData.toJson()));
        json.add("data", data);
        return json;
    }

    public void fromJson(JsonObject json) {
        dataMap.clear();
        readData(readDataObject(json), true);
    }

    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeVarInt(dataMap.size());
        dataMap.forEach((entityId, data) -> data.toNetwork(buf));
    }

    private void readData(JsonObject json, boolean notify) {
        json.entrySet().forEach(entry -> {
            ShopLimiterOfferData data = new ShopLimiterOfferData(entry.getValue().getAsJsonObject());
            data.setUpdate(update);
            dataMap.put(UUID.fromString(entry.getKey()), data);
        });

        if (notify) {
            update.onUpdate();
        }
    }

    private static UUID readUserId(JsonObject jsonObject) {
        if (!jsonObject.has("userId")) {
            throw new IllegalArgumentException("Missing required limiter player field: userId");
        }

        return UUID.fromString(jsonObject.get("userId").getAsString());
    }

    private static JsonObject readDataObject(JsonObject jsonObject) {
        if (jsonObject.has("data") && jsonObject.get("data").isJsonObject()) {
            return jsonObject.getAsJsonObject("data");
        }

        return jsonObject;
    }
}
