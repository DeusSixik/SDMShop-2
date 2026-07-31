package dev.sixik.sdmshop2.libs.platform.utils.network.async;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.concurrent.CompletableFuture;

public interface AsyncRequestPacket<R> {

    AsyncRequestType<? extends AsyncRequestPacket<R>, R> type();

    void write(FriendlyByteBuf buf);

    default R readResponse(FriendlyByteBuf buf) {
        return type().readResponse(buf);
    }

    default CompletableFuture<R> askServer() {
        return AsyncBridge.askServer(this);
    }

    default CompletableFuture<R> sendToServer() {
        return askServer();
    }

    default CompletableFuture<R> askPlayer(ServerPlayer player) {
        return AsyncBridge.askPlayer(player, this);
    }

    default CompletableFuture<R> sendToPlayer(ServerPlayer player) {
        return askPlayer(player);
    }
}
