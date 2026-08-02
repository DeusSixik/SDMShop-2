package dev.sixik.sdmshop2.libs.shop.base.limiter;

import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.platform.SDMPlatform;
import dev.sixik.sdmshop2.libs.platform.ServerOperation;
import dev.sixik.sdmshop2.libs.platform.utils.repository.RepositoryStorage;
import dev.sixik.sdmshop2.libs.platform.utils.repositoryManager.RepoDefinition;
import dev.sixik.sdmshop2.libs.platform.utils.repositoryManager.RepositoryManager;
import dev.sixik.sdmshop2.libs.shop.config.ShopDataStorageConfig;
import dev.sixik.sdmshop2.utils.exceptions.NotInitializedException;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Серверная реализация таблицы лимитов магазина.
 * Хранит полную базу данных: глобальные лимиты всех товаров и персональные лимиты всех игроков.
 * Обеспечивает потокобезопасное асинхронное сохранение данных в JSON файл.
 */
public final class ShopLimiterTableServer implements ShopLimiterTable {

    private static ShopLimiterTableServer Instance;

    public static ShopLimiterTableServer getInstance() {
        if(Instance == null)
            throw new NotInitializedException("ShopLimiterTableServer is not initialized yet.");

        return Instance;
    }

    @Getter
    private final MinecraftServer server;
    private final ExecutorService ioExecutor;

    private RepositoryStorage<UUID, ShopLimiterOfferData> offersRepository;
    private RepositoryStorage<UUID, ShopLimiterPlayerData> playersRepository;
    private RepositoryStorage<String, DailyOfferStats> dailyStatsRepository;

    public ShopLimiterTableServer(MinecraftServer server, Path shopDirWorld) {
        this(server, shopDirWorld, false);
    }

    public ShopLimiterTableServer(MinecraftServer server) {
        this(server, false);
    }

    public ShopLimiterTableServer(MinecraftServer server, boolean isInstance) {
        this(server, SDMPlatform.resolveSdmDir(server.getWorldPath(LevelResource.ROOT), "shop"), isInstance);
    }

    public ShopLimiterTableServer(MinecraftServer server, Path shopDirWorld, boolean isInstance) {
        this.server = server;
        this.ioExecutor = Executors.newSingleThreadExecutor();

        if(isInstance)
            Instance = this;

        final ShopDataStorageConfig.MongoConfig config = SDMShop2.getDataStorageConfig().getCurrentConfig().mongodb;
        final RepositoryManager repositoryManager = SDMShop2.getRepositoryManager(server);
        offersRepository = new RepositoryStorage<>(repositoryManager.createRepository(
                shopDirWorld.resolve("limiter").resolve("offers"),
                config.limiterOffersCollection,
                new RepoDefinition<>(
                        UUID::toString,
                        UUID::fromString,
                        ShopLimiterOfferData::getOfferId,
                        ShopLimiterOfferData::toJson,
                        this::readOfferData)
        ), ConcurrentHashMap::new, ioExecutor);

        playersRepository = new RepositoryStorage<>(repositoryManager.createRepository(
                shopDirWorld.resolve("limiter").resolve("players"),
                config.limiterPlayersCollection,
                new RepoDefinition<>(
                        UUID::toString,
                        UUID::fromString,
                        ShopLimiterPlayerData::getUserId,
                        ShopLimiterPlayerData::toJson,
                        this::readPlayerData)
        ), ConcurrentHashMap::new, ioExecutor);

        dailyStatsRepository = new RepositoryStorage<>(repositoryManager.createRepository(
                shopDirWorld,
                config.dailyStatsCollection,
                new RepoDefinition<>(
                        s -> s,
                        s -> s,
                        DailyOfferStats::getDate,
                        DailyOfferStats::toJson,
                        this::readDailyOfferStats
                 )
        ), ConcurrentHashMap::new, ioExecutor);

        offersRepository.loadAll();
        playersRepository.loadAll();
    }

    public ShopLimiterOfferData getOfferData(UUID entityId) {
        return offersRepository.getOrCreate(entityId, this::createOfferData);
    }

    public ShopLimiterPlayerData getPlayerData(Player player) {
        return getPlayerData(player.getGameProfile().getId());
    }

    public ShopLimiterPlayerData getPlayerData(UUID playerId) {
        return playersRepository.getOrCreate(playerId, this::createPlayerData);
    }

    public DailyOfferStats getDailyOfferStats() {
        return getDailyOfferStats(LocalDate.now().toString());
    }

    public DailyOfferStats getDailyOfferStats(String timeData) {
        return dailyStatsRepository.getOrCreate(timeData, this::createDailyOfferStats);
    }

    /**
     * Сериализует персональные данные конкретного игрока и общие глобальные лимиты
     * в сетевой буфер для отправки на клиент.
     *
     * @param player UUID игрока, данные которого необходимо отправить
     * @param buf Сетевой буфер для записи
     */
    @Override
    public void toNetwork(UUID player, FriendlyByteBuf buf) {
        getPlayerData(player).toNetwork(buf);

        buf.writeVarInt(offersRepository.size());
        offersRepository.forEach((entityId, data) -> data.toNetwork(buf));
    }

    @Override
    public void fromNetwork(FriendlyByteBuf buf) {
        throw new UnsupportedOperationException();
    }

    /**
     * Синхронно сохраняет данные и корректно завершает работу пула потоков.
     * Этот метод должен обязательно вызываться при остановке сервера (ServerStoppingEvent),
     * чтобы предотвратить потерю данных о покупках.
     */
    @Override
    public void shutdown() {
        ioExecutor.shutdown();
        try {
            if (!ioExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                ioExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            ioExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        if (Instance == this) {
            Instance = null;
        }
    }

    private ShopLimiterOfferData readOfferData(com.google.gson.JsonObject json) {
        ShopLimiterOfferData data = new ShopLimiterOfferData(json);
        data.setUpdate(() -> offersRepository.update(data.getOfferId()));
        return data;
    }

    private ShopLimiterOfferData createOfferData(UUID offerId) {
        ShopLimiterOfferData data = new ShopLimiterOfferData(offerId);
        data.setUpdate(() -> offersRepository.update(offerId));
        return data;
    }

    private ShopLimiterPlayerData readPlayerData(com.google.gson.JsonObject json) {
        ShopLimiterPlayerData data = new ShopLimiterPlayerData(json);
        data.setUpdate(() -> playersRepository.update(data.getUserId()));
        return data;
    }

    private ShopLimiterPlayerData createPlayerData(UUID playerId) {
        ShopLimiterPlayerData data = new ShopLimiterPlayerData(playerId);
        data.setUpdate(() -> playersRepository.update(playerId));
        return data;
    }

    private DailyOfferStats readDailyOfferStats(com.google.gson.JsonObject json) {
        DailyOfferStats stats = new DailyOfferStats(json);
        stats.setUpdate(() -> dailyStatsRepository.update(stats.getDate()));
        return stats;
    }

    private DailyOfferStats createDailyOfferStats(String date) {
        DailyOfferStats stats = new DailyOfferStats(date);
        stats.setUpdate(() -> dailyStatsRepository.update(stats.getDate()));
        return stats;
    }

    public static class Manager implements ServerOperation {
        @Override
        public void onServerStart(MinecraftServer server) {
            new ShopLimiterTableServer(server, true);
        }

        @Override
        public void onServerStop(MinecraftServer server) {
            getInstance().shutdown();
        }

        @Override
        public void onReload() { }

    }
}
