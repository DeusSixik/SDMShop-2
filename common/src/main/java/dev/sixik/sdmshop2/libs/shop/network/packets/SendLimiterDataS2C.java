package dev.sixik.sdmshop2.libs.shop.network.packets;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.shop.base.limiter.ShopLimiterTable;
import dev.sixik.sdmshop2.libs.shop.network.SDMShopNetwork;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

/**
 * Синхронизирует счётчики лимитеров с клиентом и обновляет видимый UI товаров.
 */
public class SendLimiterDataS2C extends BaseS2CMessage {

    private static final String CLIENT_HANDLER_CLASS = "dev.sixik.sdmshop2.libs.shop.client.network.ClientLimiterDataHandler";

    private final byte[] limiterData;

    public SendLimiterDataS2C(ServerPlayer player, ShopLimiterTable limiterTable) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        try {
            limiterTable.toNetwork(player.getGameProfile().getId(), buf);
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
        try {
            Class<?> handlerClass = Class.forName(CLIENT_HANDLER_CLASS);
            handlerClass.getMethod("handle", byte[].class).invoke(null, (Object) limiterData);
        } catch (ReflectiveOperationException | LinkageError exception) {
            SDMShop2.LOGGER.error("Failed to dispatch limiter synchronization packet to client handler!", exception);
        }
    }
}
