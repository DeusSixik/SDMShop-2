package dev.sixik.sdmshop2.libs.shop.network.packets;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop.network.SDMShopNetwork;
import dev.sixik.sdmshop2.utils.ShopUtils;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

/**
 * Synchronizes limiter counters to a client and refreshes visible offer UI.
 */
public class SendLimiterDataS2C extends BaseS2CMessage {

    private final byte[] limiterData;

    public SendLimiterDataS2C(ServerPlayer player) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        try {
            ShopUtils.getLimiterTable(false).ifPresent(limiterTable ->
                    limiterTable.toNetwork(player.getGameProfile().getId(), buf)
            );
            this.limiterData = new byte[buf.readableBytes()];
            buf.readBytes(this.limiterData);
        } finally {
            buf.release();
        }
    }

    public SendLimiterDataS2C(FriendlyByteBuf buf) {
        this.limiterData = buf.readByteArray();
    }

    @Override
    public MessageType getType() {
        return SDMShopNetwork.SEND_LIMITER_DATA;
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeByteArray(limiterData);
    }

    @Override
    public void handle(NetworkManager.PacketContext packetContext) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(limiterData));
        try {
            ShopUtils.getLimiterTable(true).ifPresent(limiterTable -> {
                limiterTable.fromNetwork(buf);
                ShopUIEvents.invokeRefreshOffers();
            });
        } catch (Exception e) {
            SDMShop2.LOGGER.error("Failed to handle limiter synchronization packet!", e);
        } finally {
            buf.release();
        }
    }
}
