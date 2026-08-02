package dev.sixik.sdmshop2.libs.shop.client.cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodec;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * Generic client-side key-value cache backed by a flat JSON object.
 *
 * <p>Values are kept as raw {@link JsonElement}s and decoded lazily through the
 * caller-provided {@link FieldCodec}. Adding a new cached value only requires a
 * stable string key and a codec for that value type.</p>
 */
public final class ClientCache {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final ExecutorService IO_EXECUTOR = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable, "SDMShop2 Client Cache IO");
        thread.setDaemon(true);
        return thread;
    });

    private final Map<String, JsonElement> values = new LinkedHashMap<>();
    private final Object writeLock = new Object();
    private final Path file;
    private volatile boolean dirty;
    private long version;
    private long writtenVersion;

    private ClientCache(Path file) {
        this.file = Objects.requireNonNull(file, "file");
    }

    public static ClientCache load(Path file) {
        ClientCache cache = new ClientCache(file);
        if (!Files.exists(file)) {
            return cache;
        }

        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonElement parsed = JsonParser.parseReader(reader);
            if (parsed == null || !parsed.isJsonObject()) {
                SDMShop2.LOGGER.warn("Client cache at {} is not a JSON object, starting fresh", file);
                return cache;
            }

            JsonObject root = parsed.getAsJsonObject();
            synchronized (cache) {
                for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                    cache.values.put(entry.getKey(), entry.getValue() == null ? null : entry.getValue().deepCopy());
                }
            }
        } catch (Exception exception) {
            SDMShop2.LOGGER.error("Corrupted client cache at {}, starting fresh", file, exception);
        }

        return cache;
    }

    public Path getFile() {
        return file;
    }

    public synchronized boolean isDirty() {
        return dirty;
    }

    public <Value> Value get(String key, FieldCodec<Value> codec, Value defaultValue) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(codec, "codec");

        JsonElement element;
        synchronized (this) {
            element = values.get(key);
            element = element == null ? null : element.deepCopy();
        }

        try {
            return codec.fromJsonElement(element, defaultValue);
        } catch (Exception exception) {
            SDMShop2.LOGGER.warn("Failed to decode client cache key '{}', using default value", key, exception);
            return defaultValue;
        }
    }

    public <Value> void set(String key, FieldCodec<Value> codec, Value value) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(codec, "codec");

        JsonElement element = codec.toJsonElement(value);
        synchronized (this) {
            values.put(key, element == null ? null : element.deepCopy());
            markDirty();
        }
    }

    public synchronized boolean has(String key) {
        Objects.requireNonNull(key, "key");
        return values.containsKey(key);
    }

    public synchronized void remove(String key) {
        Objects.requireNonNull(key, "key");
        if (values.remove(key) != null) {
            markDirty();
        }
    }

    public synchronized void clear() {
        if (!values.isEmpty()) {
            values.clear();
            markDirty();
        }
    }

    public synchronized Map<String, JsonElement> valuesSnapshot() {
        Map<String, JsonElement> snapshot = new LinkedHashMap<>(values.size());
        for (Map.Entry<String, JsonElement> entry : values.entrySet()) {
            snapshot.put(entry.getKey(), entry.getValue() == null ? null : entry.getValue().deepCopy());
        }
        return snapshot;
    }

    public void saveAsync() {
        final Snapshot snapshot = snapshotJson(false);
        if (snapshot == null) {
            return;
        }

        try {
            IO_EXECUTOR.submit(() -> {
                try {
                    writeSnapshot(snapshot);
                } catch (IOException exception) {
                    markDirtyAfterFailedWrite(snapshot);
                    SDMShop2.LOGGER.error("Failed to save client cache to {}", file, exception);
                }
            });
        } catch (RejectedExecutionException exception) {
            markDirtyAfterFailedWrite(snapshot);
            SDMShop2.LOGGER.error("Failed to schedule client cache save to {}", file, exception);
        }
    }

    public void saveNow() {
        final Snapshot snapshot = snapshotJson(true);
        if (snapshot == null) {
            return;
        }

        try {
            writeSnapshot(snapshot);
        } catch (IOException exception) {
            markDirtyAfterFailedWrite(snapshot);
            SDMShop2.LOGGER.error("Failed to save client cache to {}", file, exception);
        }
    }

    private synchronized Snapshot snapshotJson(boolean force) {
        if (!dirty && (!force || version <= writtenVersion)) {
            return null;
        }

        dirty = false;
        JsonObject root = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : values.entrySet()) {
            root.add(entry.getKey(), entry.getValue() == null ? null : entry.getValue().deepCopy());
        }
        return new Snapshot(GSON.toJson(root), version);
    }

    private synchronized void markDirty() {
        dirty = true;
        version++;
    }

    private synchronized void markDirtyAfterFailedWrite(Snapshot snapshot) {
        if (snapshot.version >= writtenVersion) {
            dirty = true;
        }
    }

    private void writeSnapshot(Snapshot snapshot) throws IOException {
        synchronized (writeLock) {
            synchronized (this) {
                if (snapshot.version < writtenVersion) {
                    return;
                }
            }

            writeAtomic(snapshot.json);

            synchronized (this) {
                writtenVersion = Math.max(writtenVersion, snapshot.version);
            }
        }
    }

    private void writeAtomic(String json) throws IOException {
        Path parent = file.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        String fileName = file.getFileName() == null ? "client_cache.json" : file.getFileName().toString();
        Path temp = file.resolveSibling(fileName + ".tmp");
        Files.writeString(temp, json, StandardCharsets.UTF_8);

        try {
            Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException ignored) {
            Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private record Snapshot(String json, long version) {
    }
}
