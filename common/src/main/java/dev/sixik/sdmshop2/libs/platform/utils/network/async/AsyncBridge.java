package dev.sixik.sdmshop2.libs.platform.utils.network.async;

import dev.architectury.networking.NetworkManager;
import dev.sixik.sdmshop2.SDMShop2;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public final class AsyncBridge {

    public static final ResourceLocation CHANNEL = new ResourceLocation("sdm_platform_mod", "async_bridge");

    private static final int LARGE_RESPONSE_THRESHOLD = 500_000;
    private static final int REQUEST_TIMEOUT_SECONDS = 30;

    private static final Map<Long, PendingRequest> PENDING = new ConcurrentHashMap<>();
    private static final Map<String, BiFunction<FriendlyByteBuf, NetworkManager.PacketContext, FriendlyByteBuf>> HANDLERS = new ConcurrentHashMap<>();
    private static final AtomicLong ID_GEN = new AtomicLong();
    private static final AtomicBoolean SERVER_INITIALIZED = new AtomicBoolean();
    private static final AtomicBoolean CLIENT_INITIALIZED = new AtomicBoolean();
    private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor(daemonThreadFactory("sdm-async-bridge-timeout"));

    public static void initServer() {
        if (SERVER_INITIALIZED.compareAndSet(false, true)) {
            NetworkManager.registerReceiver(NetworkManager.Side.C2S, CHANNEL, AsyncBridge::onPacket);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        if (CLIENT_INITIALIZED.compareAndSet(false, true)) {
            NetworkManager.registerReceiver(NetworkManager.Side.S2C, CHANNEL, AsyncBridge::onPacket);
        }
    }

    public static CompletableFuture<FriendlyByteBuf> askServer(
            final String subject,
            final Function<FriendlyByteBuf, FriendlyByteBuf> writer
    ) {
        return sendInternal(subject, writer, buf -> NetworkManager.sendToServer(CHANNEL, buf));
    }

    public static <R> CompletableFuture<R> askServer(final AsyncRequestPacket<R> packet) {
        Objects.requireNonNull(packet, "packet");
        AsyncRequestType<? extends AsyncRequestPacket<R>, R> type = packet.type();
        return askServer(type.subject(), buf -> {
            packet.write(buf);
            return buf;
        }).thenApply(response -> readTypedResponse(packet, response));
    }

    public static <R> CompletableFuture<R> askServer(
            final String subject,
            final Function<FriendlyByteBuf, FriendlyByteBuf> writer,
            final Function<FriendlyByteBuf, R> responseReader
    ) {
        Objects.requireNonNull(responseReader, "responseReader");
        return askServer(subject, writer).thenApply(response -> readRawResponse(responseReader, response));
    }

    public static CompletableFuture<FriendlyByteBuf> askPlayer(
            final ServerPlayer player,
            final String subject,
            final Function<FriendlyByteBuf, FriendlyByteBuf> writer
    ) {
        Objects.requireNonNull(player, "player");
        return sendInternal(subject, writer, buf -> NetworkManager.sendToPlayer(player, CHANNEL, buf));
    }

    public static <R> CompletableFuture<R> askPlayer(final ServerPlayer player, final AsyncRequestPacket<R> packet) {
        Objects.requireNonNull(packet, "packet");
        AsyncRequestType<? extends AsyncRequestPacket<R>, R> type = packet.type();
        return askPlayer(player, type.subject(), buf -> {
            packet.write(buf);
            return buf;
        }).thenApply(response -> readTypedResponse(packet, response));
    }

    public static <R> CompletableFuture<R> askPlayer(
            final ServerPlayer player,
            final String subject,
            final Function<FriendlyByteBuf, FriendlyByteBuf> writer,
            final Function<FriendlyByteBuf, R> responseReader
    ) {
        Objects.requireNonNull(responseReader, "responseReader");
        return askPlayer(player, subject, writer).thenApply(response -> readRawResponse(responseReader, response));
    }

    public static void registerHandler(
            final String subject,
            final BiFunction<FriendlyByteBuf, NetworkManager.PacketContext, FriendlyByteBuf> processor
    ) {
        Objects.requireNonNull(subject, "subject");
        Objects.requireNonNull(processor, "processor");
        HANDLERS.put(subject, processor);
    }

    public static <P extends AsyncRequestPacket<R>, R> void registerHandler(
            final AsyncRequestType<P, R> type,
            final BiFunction<P, NetworkManager.PacketContext, R> processor
    ) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(processor, "processor");
        registerHandler(type.subject(), (buf, context) -> {
            P packet = type.readRequest(buf);
            R response = processor.apply(packet, context);
            return type.writeResponse(response);
        });
    }

    public static <P extends AsyncRequestPacket<Void>> void registerHandler(
            final AsyncRequestType<P, Void> type,
            final BiConsumer<P, NetworkManager.PacketContext> processor
    ) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(processor, "processor");
        registerHandler(type.subject(), (buf, context) -> {
            P packet = type.readRequest(buf);
            processor.accept(packet, context);
            return null;
        });
    }

    public static void completeExternal(final long id, final FriendlyByteBuf fullData) {
        PendingRequest pending = PENDING.remove(id);
        if (pending != null) {
            pending.complete(fullData);
        } else if (fullData != null) {
            fullData.release();
        }
    }

    public static void failExternal(final long id, final Throwable throwable) {
        PendingRequest pending = PENDING.remove(id);
        if (pending != null) {
            pending.completeExceptionally(throwable);
        }
    }

    private static CompletableFuture<FriendlyByteBuf> sendInternal(
            final String subject,
            final Function<FriendlyByteBuf, FriendlyByteBuf> writer,
            final Consumer<FriendlyByteBuf> sender
    ) {
        Objects.requireNonNull(subject, "subject");
        Objects.requireNonNull(writer, "writer");
        Objects.requireNonNull(sender, "sender");

        long requestId = ID_GEN.incrementAndGet();
        CompletableFuture<FriendlyByteBuf> future = new CompletableFuture<>();
        PendingRequest pending = new PendingRequest(future);
        PENDING.put(requestId, pending);

        ScheduledFuture<?> timeout = SCHEDULER.schedule(() -> {
            PendingRequest removed = PENDING.remove(requestId);
            if (removed != null) {
                removed.completeExceptionally(new TimeoutException("Packet timed out after " + REQUEST_TIMEOUT_SECONDS + "s: " + subject));
            }
        }, REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        pending.setTimeout(timeout);

        FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        try {
            packet.writeLong(requestId);
            packet.writeBoolean(true);
            packet.writeUtf(subject);
            writer.apply(packet);
            sender.accept(packet);
        } catch (Throwable throwable) {
            PENDING.remove(requestId);
            pending.completeExceptionally(throwable);
            packet.release();
        }

        return future;
    }

    private static void onPacket(final FriendlyByteBuf buf, final NetworkManager.PacketContext context) {
        if (buf.readableBytes() < Long.BYTES + 1) {
            SDMShop2.LOGGER.warn("Received malformed AsyncBridge packet: {} readable bytes", buf.readableBytes());
            return;
        }

        final long id = buf.readLong();
        final boolean isRequest = buf.readBoolean();

        if (isRequest) {
            handleRequest(id, buf, context);
        } else {
            handleResponse(id, buf);
        }
    }

    private static void handleRequest(
            final long id,
            final FriendlyByteBuf buf,
            final NetworkManager.PacketContext context
    ) {
        if (!buf.isReadable()) {
            sendErrorResponse(id, context, "AsyncBridge request missing subject");
            return;
        }

        final String subject = buf.readUtf();
        final BiFunction<FriendlyByteBuf, NetworkManager.PacketContext, FriendlyByteBuf> handler = HANDLERS.get(subject);
        if (handler == null) {
            sendErrorResponse(id, context, "No AsyncBridge handler registered for subject: " + subject);
            return;
        }

        final FriendlyByteBuf inputCopy = new FriendlyByteBuf(buf.copy());
        context.queue(() -> {
            FriendlyByteBuf responsePayload = null;
            try {
                responsePayload = handler.apply(inputCopy, context);
                if (responsePayload != null && responsePayload.readableBytes() > LARGE_RESPONSE_THRESHOLD) {
                    if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
                        BlobTransfer.sendToPlayer(serverPlayer, id, responsePayload);
                    } else {
                        BlobTransfer.sendToServer(id, responsePayload);
                    }
                } else {
                    sendSuccessResponse(id, context, responsePayload);
                }
            } catch (Throwable throwable) {
                SDMShop2.LOGGER.error("Error processing AsyncBridge request: {}", subject, throwable);
                sendErrorResponse(id, context, throwable.getClass().getSimpleName() + ": " + throwable.getMessage());
            } finally {
                inputCopy.release();
                if (responsePayload != null && responsePayload != inputCopy) {
                    responsePayload.release();
                }
            }
        });
    }

    private static void handleResponse(final long id, final FriendlyByteBuf buf) {
        PendingRequest pending = PENDING.remove(id);
        if (pending == null) {
            return;
        }

        if (!buf.isReadable()) {
            pending.completeExceptionally(new IllegalStateException("AsyncBridge response missing success flag"));
            return;
        }

        boolean success = buf.readBoolean();
        if (!success) {
            String message = buf.isReadable() ? buf.readUtf() : "Remote AsyncBridge request failed";
            pending.completeExceptionally(new IllegalStateException(message));
            return;
        }

        FriendlyByteBuf responseCopy = new FriendlyByteBuf(buf.copy());
        pending.complete(responseCopy);
    }

    private static void sendSuccessResponse(
            final long id,
            final NetworkManager.PacketContext context,
            final FriendlyByteBuf responsePayload
    ) {
        FriendlyByteBuf reply = new FriendlyByteBuf(Unpooled.buffer());
        reply.writeLong(id);
        reply.writeBoolean(false);
        reply.writeBoolean(true);

        if (responsePayload != null) {
            reply.writeBytes(responsePayload, responsePayload.readerIndex(), responsePayload.readableBytes());
        }

        sendReply(context, reply);
    }

    private static void sendErrorResponse(
            final long id,
            final NetworkManager.PacketContext context,
            final String message
    ) {
        FriendlyByteBuf reply = new FriendlyByteBuf(Unpooled.buffer());
        reply.writeLong(id);
        reply.writeBoolean(false);
        reply.writeBoolean(false);
        reply.writeUtf(message == null ? "Remote AsyncBridge request failed" : message);
        sendReply(context, reply);
    }

    private static void sendReply(final NetworkManager.PacketContext context, final FriendlyByteBuf reply) {
        if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
            NetworkManager.sendToPlayer(serverPlayer, CHANNEL, reply);
        } else {
            NetworkManager.sendToServer(CHANNEL, reply);
        }
    }

    private static <R> R readTypedResponse(AsyncRequestPacket<R> packet, FriendlyByteBuf response) {
        try {
            return packet.readResponse(response);
        } finally {
            response.release();
        }
    }

    private static <R> R readRawResponse(Function<FriendlyByteBuf, R> responseReader, FriendlyByteBuf response) {
        try {
            return responseReader.apply(response);
        } finally {
            response.release();
        }
    }

    private static ThreadFactory daemonThreadFactory(String name) {
        return runnable -> {
            Thread thread = new Thread(runnable, name);
            thread.setDaemon(true);
            return thread;
        };
    }

    private static final class PendingRequest {
        private final CompletableFuture<FriendlyByteBuf> future;
        private volatile ScheduledFuture<?> timeout;

        private PendingRequest(CompletableFuture<FriendlyByteBuf> future) {
            this.future = future;
        }

        private void setTimeout(ScheduledFuture<?> timeout) {
            this.timeout = timeout;
            if (future.isDone()) {
                timeout.cancel(false);
            }
        }

        private void complete(FriendlyByteBuf payload) {
            cancelTimeout();
            future.complete(payload);
        }

        private void completeExceptionally(Throwable throwable) {
            cancelTimeout();
            future.completeExceptionally(throwable);
        }

        private void cancelTimeout() {
            ScheduledFuture<?> currentTimeout = timeout;
            if (currentTimeout != null) {
                currentTimeout.cancel(false);
            }
        }
    }

    private AsyncBridge() {
    }
}
