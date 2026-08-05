package dev.sixik.sdmshop2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.platform.Platform;
import dev.sixik.sdmshop2.libs.platform.SDMPlatform;
import dev.sixik.sdmshop2.libs.platform.ServerOperation;
import dev.sixik.sdmshop2.libs.platform.utils.repositoryManager.JsonRepositoryManager;
import dev.sixik.sdmshop2.libs.platform.utils.repositoryManager.MongoRepositoryManager;
import dev.sixik.sdmshop2.libs.platform.utils.repositoryManager.RepositoryManager;
import dev.sixik.sdmshop2.libs.platform.utils.repositoryManager.RepositoryManagerRegistry;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyPlatform;
import dev.sixik.sdmshop2.libs.sdmeconomy.commands.SDMEconomyCommands;
import dev.sixik.sdmshop2.libs.shop.base.ShopTable;
import dev.sixik.sdmshop2.libs.shop.base.limiter.ShopLimiterTableServer;
import dev.sixik.sdmshop2.libs.shop.commands.SDMShopCommands;
import dev.sixik.sdmshop2.libs.shop.config.ShopConfig;
import dev.sixik.sdmshop2.libs.shop.config.ShopConfigHolder;
import dev.sixik.sdmshop2.libs.shop.network.SDMShopNetwork;
import dev.sixik.sdmshop2.libs.shop.promo.PromoStateStore;
import dev.sixik.sdmshop2.libs.shop.register.ShopRegister;
import dev.sixik.sdmshop2.libs.shop.sound.ShopSounds;
import dev.sixik.sdmshop2.libs.shop.scripting.events.ShopScriptEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;

public final class SDMShop2 {
    public static final String MODID = "sdmshop2";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final ShopTable.Manager SHOP_TABLE_MANAGER = new ShopTable.Manager();
    private static final ShopLimiterTableServer.Manager SHOP_LIMITER_TABLE_MANAGER = new ShopLimiterTableServer.Manager();
    private static final ShopScriptEvents.Manager SHOP_SCRIPTS_CONTAINER_MANAGER = new ShopScriptEvents.Manager();
    private static final PromoStateStore.Manager SHOP_PROMO_STATE_MANAGER = new PromoStateStore.Manager();

    private static RepositoryManager instance;

    public static RepositoryManager getRepositoryManager(MinecraftServer server) {
        if(instance == null) {
            final ShopConfig config = SDMShop2.getConfig();
            instance = switch (config.storageType) {
                case JSON -> new JsonRepositoryManager(SDMPlatform.resolveSdmDir(Platform.getConfigFolder(), "shop"));
                case MONGODB -> new MongoRepositoryManager(config.mongodb.uri, config.mongodb.database, config.mongodb.serverName);
                case CUSTOM -> RepositoryManagerRegistry.createOrDefault(
                        "sdm_shop",
                        server,
                        () -> new JsonRepositoryManager(SDMPlatform.resolveSdmDir(Platform.getConfigFolder(), "shop"))
                );
            };
        }

        return instance;
    }

    public static void init() {
        SDMPlatform.init();

        SDMPlatform.addOperation(new ServerOperation() {
            @Override
            public void onReload() {
                ShopConfigHolder.reloadConfig();
            }
        });
        SDMPlatform.addOperation(SHOP_PROMO_STATE_MANAGER);
        SDMPlatform.addOperation(SHOP_TABLE_MANAGER);
        SDMPlatform.addOperation(SHOP_LIMITER_TABLE_MANAGER);
        SDMPlatform.addOperation(SHOP_SCRIPTS_CONTAINER_MANAGER);

        SDMEconomyPlatform.init();
        ShopRegister.init();
        ShopSounds.init();

        SDMShopNetwork.init();

        CommandRegistrationEvent.EVENT.register((s1, s2, s3) -> {
            SDMEconomyCommands.registerCommands(s1, s2, s3);
            SDMShopCommands.registerCommands(s1, s2, s3);
        });

        ShopConfigHolder.getConfigRaw();
    }

    public static ShopConfig getConfig() {
        return ShopConfigHolder.getConfig();
    }

    public static ResourceLocation resource(String path) {
        return ResourceLocation.tryBuild(MODID, path);
    }

    public static ResourceLocation resourceTexture(String path) {
        return ResourceLocation.tryBuild(MODID, "textures/" + path);
    }
}
