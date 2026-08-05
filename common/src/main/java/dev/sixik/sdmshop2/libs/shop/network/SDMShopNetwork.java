package dev.sixik.sdmshop2.libs.shop.network;

import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import dev.architectury.platform.Platform;
import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.shop.network.async.AsyncClientTasks;
import dev.sixik.sdmshop2.libs.shop.network.async.AsyncServerTasks;
import dev.sixik.sdmshop2.libs.shop.network.packets.SendLimiterDataS2C;
import net.fabricmc.api.EnvType;

public class SDMShopNetwork {

    private static final SimpleNetworkManager NET = SimpleNetworkManager.create(SDMShop2.MODID);

    public static final MessageType SEND_LIMITER_DATA = NET.registerS2C("send_limiter_data", SendLimiterDataS2C::new);

    public static void init() {
        AsyncServerTasks.init();

        if (Platform.getEnv() == EnvType.CLIENT) {
            AsyncClientTasks.init();
        }
    }
}
