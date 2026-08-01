package dev.sixik.sdmshop2.libs.platform.utils.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Stream;

public class JsonGenericRepository<K, V> implements Repository<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonGenericRepository.class);
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final Path collectionDirectory;

    private final Function<K, String> keyToString;
    private final Function<V, K> keyExtractor;
    private final Function<V, JsonObject> serializer;
    private final Function<JsonObject, V> deserializer;

    public JsonGenericRepository(
            Path collectionDirectory,
            Function<K, String> keyToString,
            Function<V, K> keyExtractor,
            Function<V, JsonObject> serializer,
            Function<JsonObject, V> deserializer
    ) {
        this.collectionDirectory = collectionDirectory;
        this.keyToString = keyToString;
        this.keyExtractor = keyExtractor;
        this.serializer = serializer;
        this.deserializer = deserializer;

        try {
            if (!Files.exists(this.collectionDirectory)) {
                Files.createDirectories(this.collectionDirectory);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to create collection directory: {}", collectionDirectory, e);
        }
    }

    /**
     * Вычисляет точный путь к файлу.
     * Поддерживает вложенные папки, если keyToString возвращает строку со слэшами.
     */
    private Path getFilePath(K id) {
        return collectionDirectory.resolve(toStoragePath(keyToString.apply(id)) + ".json");
    }

    private Path getLegacyFilePath(K id) {
        final String string = keyToString.apply(id);
        final String[] spl = string.split(":");
        return collectionDirectory.resolve((spl.length > 1 ? spl[1] : spl[0]) + ".json");
    }

    private String toStoragePath(String rawKey) {
        final String normalized = rawKey == null ? "null" : rawKey.replace('\\', '/');
        final int namespaceSeparator = normalized.indexOf(':');
        final String path = namespaceSeparator >= 0
                ? normalized.substring(0, namespaceSeparator) + "/" + normalized.substring(namespaceSeparator + 1)
                : normalized;
        final String[] segments = path.split("/");
        final StringBuilder builder = new StringBuilder();

        for (String segment : segments) {
            if (segment == null || segment.isBlank() || ".".equals(segment) || "..".equals(segment)) {
                segment = "_";
            }

            segment = segment.replaceAll("[<>:\\\"|?*]", "_");

            if (!builder.isEmpty()) {
                builder.append('/');
            }
            builder.append(segment);
        }

        return builder.isEmpty() ? "_" : builder.toString();
    }

    @Override
    public void save(K id, V entity) {
        Path path = getFilePath(id);
        try {
            if (path.getParent() != null && !Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }

            final Path tmpPath = path.resolveSibling(path.getFileName() + ".tmp");
            try (Writer writer = Files.newBufferedWriter(tmpPath)) {
                gson.toJson(serializer.apply(entity), writer);
            }
            try {
                Files.move(tmpPath, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException ignored) {
                Files.move(tmpPath, path, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to save entity to JSON: {}", path, e);
        }
    }

    @Override
    public @Nullable V load(K id) {
        Path path = getFilePath(id);

        if (!Files.isRegularFile(path)) {
            path = getLegacyFilePath(id);
            if (!Files.isRegularFile(path)) {
                return null;
            }
        }

        try (Reader reader = Files.newBufferedReader(path)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            return deserializer.apply(json);
        } catch (Exception e) {
            LOGGER.error("Failed to load entity from JSON: {}", path, e);
            return null;
        }
    }

    @Override
    public Map<K, V> loadAll() {
        Map<K, V> map = new ConcurrentHashMap<>();

        try (Stream<Path> paths = Files.walk(collectionDirectory)) {
            paths.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".json"))
                    .forEach(path -> {
                        try (Reader reader = Files.newBufferedReader(path)) {
                            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                            V entity = deserializer.apply(json);

                            if (entity != null) {
                                K key = keyExtractor.apply(entity);
                                map.put(key, entity);
                            }
                        } catch (Exception e) {
                            LOGGER.error("Failed to parse file: {}", path, e);
                        }
                    });
        } catch (Exception e) {
            LOGGER.error("Failed to walk directory: {}", collectionDirectory, e);
        }
        return map;
    }

    @Override
    public void delete(K id) {
        try {
            Files.deleteIfExists(getFilePath(id));
            Files.deleteIfExists(getLegacyFilePath(id));
        } catch (IOException e) {
            LOGGER.error("Failed to delete file for entity: {}", id, e);
        }
    }
}
