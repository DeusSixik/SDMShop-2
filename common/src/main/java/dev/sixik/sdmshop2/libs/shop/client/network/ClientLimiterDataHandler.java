package dev.sixik.sdmshop2.libs.shop.client.network;

import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.shop.base.limiter.ShopLimiterTableClient;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;

@Environment(EnvType.CLIENT)
public final class ClientLimiterDataHandler {

    private ClientLimiterDataHandler() {
    }

    public static void handle(byte[] limiterData) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(limiterData));
        try {
            ShopLimiterTableClient.INSTANCE.fromNetwork(buf);
            ShopUIEvents.invokeRefreshOffers();
        } catch (Exception e) {
            SDMShop2.LOGGER.error("Failed to handle limiter synchronization packet!", e);
        } finally {
            buf.release();
        }
    }
}
