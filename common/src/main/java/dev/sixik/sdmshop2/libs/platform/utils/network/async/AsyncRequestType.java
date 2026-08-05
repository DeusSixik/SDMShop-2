package dev.sixik.sdmshop2.libs.platform.utils.network.async;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class AsyncRequestType<P extends AsyncRequestPacket<R>, R> {

    private final String subject;
    private final Function<FriendlyByteBuf, P> requestReader;
    private final BiConsumer<R, FriendlyByteBuf> responseWriter;
    private final Function<FriendlyByteBuf, R> responseReader;
    private final boolean hasResponse;

    private AsyncRequestType(
            String subject,
            Function<FriendlyByteBuf, P> requestReader,
            BiConsumer<R, FriendlyByteBuf> responseWriter,
            Function<FriendlyByteBuf, R> responseReader,
            boolean hasResponse
    ) {
        this.subject = Objects.requireNonNull(subject, "subject");
        this.requestReader = Objects.requireNonNull(requestReader, "requestReader");
        this.responseWriter = responseWriter;
        this.responseReader = responseReader;
        this.hasResponse = hasResponse;
    }

    public static <P extends AsyncRequestPacket<R>, R> AsyncRequestType<P, R> of(
            String subject,
            Function<FriendlyByteBuf, P> requestReader,
            BiConsumer<R, FriendlyByteBuf> responseWriter,
            Function<FriendlyByteBuf, R> responseReader
    ) {
        return new AsyncRequestType<>(
                subject,
                requestReader,
                Objects.requireNonNull(responseWriter, "responseWriter"),
                Objects.requireNonNull(responseReader, "responseReader"),
                true
        );
    }

    public static <P extends AsyncRequestPacket<Void>> AsyncRequestType<P, Void> noResponse(
            String subject,
            Function<FriendlyByteBuf, P> requestReader
    ) {
        return new AsyncRequestType<>(subject, requestReader, null, ignored -> null, false);
    }

    public String subject() {
        return subject;
    }

    public CompletableFuture<R> askServer(P packet) {
        return AsyncBridge.askServer(packet);
    }

    public CompletableFuture<R> askPlayer(ServerPlayer player, P packet) {
        return AsyncBridge.askPlayer(player, packet);
    }

    public void register(BiFunction<P, NetworkManager.PacketContext, R> processor) {
        AsyncBridge.registerHandler(this, processor);
    }

    public void register(BiConsumer<P, NetworkManager.PacketContext> processor) {
        Objects.requireNonNull(processor, "processor");
        if (hasResponse) {
            throw new IllegalStateException("Void handler cannot be used for AsyncRequestType with response: " + subject);
        }

        AsyncBridge.registerHandler(subject, (buf, context) -> {
            processor.accept(readRequest(buf), context);
            return null;
        });
    }

    public P readRequest(FriendlyByteBuf buf) {
        return requestReader.apply(buf);
    }

    public FriendlyByteBuf writeResponse(R response) {
        if (!hasResponse || response == null) {
            return null;
        }

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        responseWriter.accept(response, buf);
        return buf;
    }

    public R readResponse(FriendlyByteBuf buf) {
        if (!hasResponse) {
            return null;
        }

        return responseReader.apply(buf);
    }

    public boolean hasResponse() {
        return hasResponse;
    }
}
