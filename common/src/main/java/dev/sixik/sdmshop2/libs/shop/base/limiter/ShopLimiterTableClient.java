package dev.sixik.sdmshop2.libs.shop.base.limiter;

import dev.sixik.sdmshop2.libs.shop.client.SDMShopClient;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Клиентская реализация таблицы лимитов магазина.
 * Хранит глобальные лимиты и персональные лимиты ТОЛЬКО локального игрока (клиента).
 * Методы сохранения и отправки данных на сервер в этой реализации не поддерживаются (бросают UnsupportedOperationException).
 */
public final class ShopLimiterTableClient implements ShopLimiterTable {

    private final Map<UUID, ShopLimiterOfferData> entitiesData = new ConcurrentHashMap<>();
    private ShopLimiterPlayerData localPlayerData;

    public static final ShopLimiterTableClient INSTANCE = new ShopLimiterTableClient();

    public ShopLimiterTableClient() { }

    @Override
    public ShopLimiterOfferData getOfferData(UUID entityId) {
        return entitiesData.computeIfAbsent(entityId, ShopLimiterOfferData::new);
    }

    public ShopLimiterPlayerData getPlayerData() {
        return getPlayerData(localPlayerId());
    }

    @Override
    public ShopLimiterPlayerData getPlayerData(Player player) {
        return getPlayerData(player.getGameProfile().getId());
    }

    /**
     * Возвращает данные локального игрока.
     * * @param playerId UUID игрока. Должен строго совпадать с UUID локального клиента.
     * @return Объект персональных данных локального игрока.
     * @throws IllegalArgumentException если переданный playerId не принадлежит локальному игроку.
     */
    @Override
    public ShopLimiterPlayerData getPlayerData(UUID playerId) {
        UUID localPlayerId = localPlayerId();
        if(playerId != null && localPlayerId != null && !playerId.equals(localPlayerId))
            throw new IllegalArgumentException("Player ID must be equal to local player ID");

        if (localPlayerData == null) {
            localPlayerData = new ShopLimiterPlayerData(playerId == null ? new UUID(0L, 0L) : playerId);
        }

        return localPlayerData;
    }

    @Override
    public boolean resetOfferData(UUID entityId) {
        return entitiesData.remove(entityId) != null;
    }

    @Override
    public boolean resetPlayerData(UUID playerId, UUID entityId) {
        UUID localPlayerId = localPlayerId();
        if (playerId != null && localPlayerId != null && !playerId.equals(localPlayerId)) {
            return false;
        }

        return localPlayerData != null && localPlayerData.remove(entityId);
    }

    @Override
    public int resetAllPlayerData(UUID entityId) {
        return localPlayerData != null && localPlayerData.remove(entityId) ? 1 : 0;
    }

    @Override
    public void toNetwork(UUID player, FriendlyByteBuf buf) {
        throw new UnsupportedOperationException();
    }

    /**
     * Читает данные, присланные сервером, и обновляет локальный кэш клиента.
     *
     * @param buf Сетевой буфер с данными от сервера
     */
    @Override
    public void fromNetwork(FriendlyByteBuf buf) {
        entitiesData.clear();
        localPlayerData = new ShopLimiterPlayerData(localPlayerId(), buf);

        int entitiesSize = buf.readVarInt();
        for (int i = 0; i < entitiesSize; i++) {
            ShopLimiterOfferData data = new ShopLimiterOfferData(buf);
            entitiesData.put(data.getOfferId(), data);
        }

        SDMShopClient.ACCEPT_LIMITER_DATA_EVENT.invoker().onAcceptLimiterDataEvent(this);
    }

    private static UUID localPlayerId() {
        if (Minecraft.getInstance().player == null) {
            return new UUID(0L, 0L);
        }

        return Minecraft.getInstance().player.getGameProfile().getId();
    }
}
