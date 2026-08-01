package dev.sixik.sdmshop2.libs.platform.utils.network.async;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public final class BlobTransfer {

    public static final ResourceLocation CHANNEL = new ResourceLocation("sdm_platform_mod", "blob_channel");

    private static final Logger LOGGER = LoggerFactory.getLogger(BlobTransfer.class);

    private static final int CHUNK_SIZE = 50 * 1024;
    private static final int MAX_BLOB_SIZE = 32 * 1024 * 1024;
    private static final long RECEIVER_TIMEOUT_MS = 30_000L;

    private static final Map<BlobKey, BlobReceiver> INCOMING_BUFFERS = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService CLEANUP_SCHEDULER = Executors.newSingleThreadScheduledExecutor(daemonThreadFactory("sdm-blob-cleanup"));
    private static final AtomicBoolean SERVER_INITIALIZED = new AtomicBoolean();
    private static final AtomicBoolean CLIENT_INITIALIZED = new AtomicBoolean();

    static {
        CLEANUP_SCHEDULER.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            INCOMING_BUFFERS.entrySet().removeIf(entry -> now - entry.getValue().lastUpdateTime > RECEIVER_TIMEOUT_MS);
        }, 10, 10, TimeUnit.SECONDS);
    }

    public static void initServer() {
        if (SERVER_INITIALIZED.compareAndSet(false, true)) {
            NetworkManager.registerReceiver(NetworkManager.Side.C2S, CHANNEL, BlobTransfer::onPacket);
        }
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        if (CLIENT_INITIALIZED.compareAndSet(false, true)) {
            NetworkManager.registerReceiver(NetworkManager.Side.S2C, CHANNEL, BlobTransfer::onPacket);
        }
    }

    public static void sendToPlayer(ServerPlayer player, long responseId, FriendlyByteBuf hugeData) {
        Objects.requireNonNull(player, "player");
        sendInternal(hugeData, responseId, buf -> NetworkManager.sendToPlayer(player, CHANNEL, buf));
    }

    public static void sendToServer(long requestId, FriendlyByteBuf hugeData) {
        sendInternal(hugeData, requestId, buf -> NetworkManager.sendToServer(CHANNEL, buf));
    }

    private static void sendInternal(ByteBuf data, long id, Consumer<FriendlyByteBuf> sender) {
        Objects.requireNonNull(data, "data");
        Objects.requireNonNull(sender, "sender");

        int totalSize = data.readableBytes();
        if (totalSize > MAX_BLOB_SIZE) {
            throw new IllegalArgumentException("Blob is too large: " + totalSize + " bytes, max " + MAX_BLOB_SIZE);
        }

        int totalChunks = Math.max(1, (int) Math.ceil((double) totalSize / CHUNK_SIZE));
        int baseReaderIndex = data.readerIndex();

        for (int chunkIndex = 0; chunkIndex < totalChunks; chunkIndex++) {
            int offset = chunkIndex * CHUNK_SIZE;
            int length = Math.min(CHUNK_SIZE, totalSize - offset);

            FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
            packet.writeLong(id);
            packet.writeInt(chunkIndex);
            packet.writeInt(totalChunks);
            packet.writeInt(totalSize);
            if (length > 0) {
                packet.writeBytes(data, baseReaderIndex + offset, length);
            }

            sender.accept(packet);
        }
    }

    private static void onPacket(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
        if (buf.readableBytes() < Long.BYTES + Integer.BYTES * 3) {
            LOGGER.warn("Received malformed BlobTransfer packet: {} readable bytes", buf.readableBytes());
            return;
        }

        long id = buf.readLong();
        int chunkIndex = buf.readInt();
        int totalChunks = buf.readInt();
        int totalSize = buf.readInt();
        BlobKey key = BlobKey.of(context, id);

        if (!isHeaderValid(id, chunkIndex, totalChunks, totalSize, buf.readableBytes())) {
            failTransfer(key, id, "Invalid BlobTransfer header");
            return;
        }

        BlobReceiver receiver = INCOMING_BUFFERS.computeIfAbsent(key, ignored -> new BlobReceiver(totalChunks, totalSize));
        FriendlyByteBuf completedData = null;
        String failure = null;

        synchronized (receiver) {
            if (receiver.totalChunks != totalChunks || receiver.totalSize != totalSize) {
                failure = "BlobTransfer metadata changed during transfer";
            } else if (receiver.chunks[chunkIndex] != null) {
                receiver.lastUpdateTime = System.currentTimeMillis();
                return;
            } else {
                int payloadSize = buf.readableBytes();
                byte[] payload = new byte[payloadSize];
                buf.readBytes(payload);
                receiver.chunks[chunkIndex] = payload;
                receiver.receivedChunks++;
                receiver.lastUpdateTime = System.currentTimeMillis();

                if (receiver.receivedChunks == receiver.totalChunks) {
                    completedData = receiver.assemble();
                }
            }
        }

        if (failure != null) {
            failTransfer(key, id, failure);
            return;
        }

        if (completedData != null) {
            INCOMING_BUFFERS.remove(key, receiver);
            FriendlyByteBuf finalData = completedData;
            context.queue(() -> AsyncBridge.completeExternal(id, finalData));
        }
    }

    private static boolean isHeaderValid(long id, int chunkIndex, int totalChunks, int totalSize, int payloadSize) {
        if (totalChunks <= 0 || chunkIndex < 0 || chunkIndex >= totalChunks) return false;
        if (totalSize < 0 || totalSize > MAX_BLOB_SIZE) return false;
        if (payloadSize > CHUNK_SIZE) return false;
        if (totalChunks != Math.max(1, (int) Math.ceil((double) totalSize / CHUNK_SIZE))) return false;

        int expectedPayloadSize = totalSize == 0
                ? 0
                : Math.min(CHUNK_SIZE, totalSize - chunkIndex * CHUNK_SIZE);
        return expectedPayloadSize == payloadSize;
    }

    private static void failTransfer(BlobKey key, long id, String message) {
        INCOMING_BUFFERS.remove(key);
        LOGGER.warn("{} for id {}", message, id);
        AsyncBridge.failExternal(id, new IllegalStateException(message));
    }

    private static ThreadFactory daemonThreadFactory(String name) {
        return runnable -> {
            Thread thread = new Thread(runnable, name);
            thread.setDaemon(true);
            return thread;
        };
    }

    private static final class BlobReceiver {
        private final byte[][] chunks;
        private final int totalChunks;
        private final int totalSize;
        private int receivedChunks;
        private long lastUpdateTime = System.currentTimeMillis();

        private BlobReceiver(int totalChunks, int totalSize) {
            this.totalChunks = totalChunks;
            this.totalSize = totalSize;
            this.chunks = new byte[totalChunks][];
        }

        private FriendlyByteBuf assemble() {
            ByteBuf byteBuf = Unpooled.buffer(totalSize);
            for (byte[] chunk : chunks) {
                if (chunk != null && chunk.length > 0) {
                    byteBuf.writeBytes(chunk);
                }
            }
            Arrays.fill(chunks, null);
            return new FriendlyByteBuf(byteBuf);
        }
    }

    private record BlobKey(UUID playerId, long id) {
        private static BlobKey of(NetworkManager.PacketContext context, long id) {
            if (context.getPlayer() instanceof ServerPlayer serverPlayer) {
                return new BlobKey(serverPlayer.getUUID(), id);
            }
            return new BlobKey(null, id);
        }
    }

    private BlobTransfer() {
    }
}
