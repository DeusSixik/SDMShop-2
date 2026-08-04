package dev.sixik.sdmshop2.libs.sdmeconomy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodec;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import dev.sixik.sdmshop2.utils.NbtExtern;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BankAccount {

    private static final FieldCodec<Map<ResourceLocation, BigDecimal>> BALANCES_CODEC =
            FieldCodec.<Map<ResourceLocation, BigDecimal>>builder()
                    .schema("sdmeconomy:bank_balances")
                    .json(
                            (json, key, value) -> json.add(key, writeBalances(value)),
                            (json, key, defaultValue) -> json.has(key) ? readBalances(json.get(key), defaultValue) : defaultValue
                    )
                    .jsonElement(BankAccount::writeBalances, BankAccount::readBalances)
                    .network(
                            (buf, value) -> {
                                Map<ResourceLocation, BigDecimal> safeBalances = value == null ? Map.of() : value;
                                buf.writeVarInt(safeBalances.size());
                                for (Map.Entry<ResourceLocation, BigDecimal> entry : safeBalances.entrySet()) {
                                    FieldCodecs.RESOURCE_LOCATION.toNetwork(buf, entry.getKey());
                                    FieldCodecs.BIG_DECIMAL.toNetwork(buf, entry.getValue());
                                }
                            },
                            buf -> {
                                int size = buf.readVarInt();
                                Map<ResourceLocation, BigDecimal> out = new ConcurrentHashMap<>();
                                for (int i = 0; i < size; i++) {
                                    ResourceLocation id = FieldCodecs.RESOURCE_LOCATION.fromNetwork(buf);
                                    BigDecimal value = FieldCodecs.BIG_DECIMAL.fromNetwork(buf);
                                    if (id != null && value != null) {
                                        out.put(id, value);
                                    }
                                }
                                return out;
                            }
                    )
                    .copy(value -> value == null ? null : new ConcurrentHashMap<>(value))
                    .build();

    /**
     * Владелец Аккаунта. Это всегда ID {@link GameProfile}
     */
    @Getter
    private final UUID gameProfileOwnerId;

    private final Map<ResourceLocation, BigDecimal> balances = new ConcurrentHashMap<>();

    @Getter
    private volatile boolean dirty = false;

    @Setter
    private Runnable onUpdate = () -> {};

    public BankAccount(Player player) {
        this(player.getGameProfile());
    }

    public BankAccount(GameProfile profile) {
        this(profile.getId());
    }

    public BankAccount(UUID gameProfileId) {
        this.gameProfileOwnerId = gameProfileId;
    }

    /**
     * Возвращает баланс игрока или стандартное значение валюты {@link IStoredCurrency#getDefaultBalance()} если у игрока нет этой валюты
     */
    public BigDecimal getBalance(IStoredCurrency currency) {
        return balances.getOrDefault(currency.getId(), currency.getDefaultBalance());
    }

    /**
     * Устанавливает количество конкретной валюты
     */
    public void setBalance(IStoredCurrency currency, BigDecimal amount) {
        try {
            balances.put(currency.getId(), amount);
            markDirty();
        } finally {
            onUpdate.run();
        }
    }

    /**
     * Изменяет количество конкретной валюты
     * @param amount Сколько добавить или убавить
     */
    public void modify(IStoredCurrency currency, BigDecimal amount) {
        try {
            balances.merge(currency.getId(), amount, BigDecimal::add);
            markDirty();
        } finally {
            onUpdate.run();
        }
    }

    public boolean hasMoney(ResourceLocation moneyId) {
        return balances.containsKey(moneyId);
    }

    public boolean hasMoney(IStoredCurrency currency) {
        return hasMoney(currency.getId());
    }

    @Nullable
    public BigDecimal removeBalance(IStoredCurrency currency) {
        try {
            BigDecimal removed = balances.remove(currency.getId());
            if (removed != null) {
                markDirty();
            }
            return removed;
        } finally {
            onUpdate.run();
        }
    }

    public List<ResourceLocation> getCurrenciesIds() {
        return new ArrayList<>(balances.keySet());
    }

    /**
     * Помечает то что данные изменились и их нужно сохранить
     */
    public void markDirty() {
        this.dirty = true;
    }

    /**
     * Помечает то что данные сохранять не нужно
     */
    public void markClean() {
        this.dirty = false;
    }

    public CompoundTag serializeNbt() {
        final CompoundTag nbt = new CompoundTag();
        final ListTag list = new ListTag();
        balances.forEach((id, val) -> {
            final CompoundTag entry = new CompoundTag();
            entry.putString("id", id.toString());
            entry.putString("val", val.toString());
            list.add(entry);
        });
        nbt.put("balances", list);
        return nbt;
    }

    public void deserializeNbt(final CompoundTag nbt) {
        final ListTag list = NbtExtern.getOrThrow(nbt, "balances");

        balances.clear();

        for (int i = 0; i < list.size(); i++) {
            final CompoundTag entry = list.getCompound(i);

            balances.put(
                    ResourceLocation.tryParse(entry.getString("id")),
                    new BigDecimal(entry.getString("val"))
            );
        }
    }

    public JsonObject serializeJson() {
        final JsonObject json = new JsonObject();

        json.addProperty("owner", this.gameProfileOwnerId.toString());
        BALANCES_CODEC.toJson(json, "balances", balances);

        return json;
    }

    public static BankAccount deserializeJson(JsonObject json) {
        UUID ownerId = UUID.fromString(json.get("owner").getAsString());
        BankAccount account = new BankAccount(ownerId);

        account.balances.putAll(BALANCES_CODEC.fromJson(json, "balances", Map.of()));
        return account;
    }

    private static JsonObject writeBalances(Map<ResourceLocation, BigDecimal> balances) {
        JsonObject object = new JsonObject();
        if (balances == null) {
            return object;
        }

        for (Map.Entry<ResourceLocation, BigDecimal> entry : balances.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }

            object.add(entry.getKey().toString(), FieldCodecs.BIG_DECIMAL.toJsonElement(entry.getValue()));
        }

        return object;
    }

    private static Map<ResourceLocation, BigDecimal> readBalances(JsonElement element, Map<ResourceLocation, BigDecimal> defaultValue) {
        if (element == null || !element.isJsonObject()) {
            return defaultValue;
        }

        Map<ResourceLocation, BigDecimal> out = new ConcurrentHashMap<>();
        for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
            ResourceLocation id = ResourceLocation.tryParse(entry.getKey());
            if (id == null) {
                continue;
            }

            try {
                BigDecimal value = FieldCodecs.BIG_DECIMAL.fromJsonElement(entry.getValue(), BigDecimal.ZERO);
                out.put(id, value);
            } catch (RuntimeException exception) {
                SDMEconomyService.LOGGER.error("Failed to parse balance for currency {}", id, exception);
            }
        }

        return out;
    }
}
