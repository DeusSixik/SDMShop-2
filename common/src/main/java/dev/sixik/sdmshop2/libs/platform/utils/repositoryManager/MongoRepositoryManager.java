package dev.sixik.sdmshop2.libs.platform.utils.repositoryManager;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.mongodb.client.model.changestream.OperationType;
import dev.sixik.sdmshop2.libs.platform.utils.repository.MongoGenericRepository;
import dev.sixik.sdmshop2.libs.platform.utils.repository.Repository;
import lombok.Getter;
import org.bson.BsonDocument;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MongoRepositoryManager extends RepositoryManager{

    protected static final Logger LOGGER = LoggerFactory.getLogger(MongoRepositoryManager.class);

    private static final Map<String, MongoClient> SHARED_CLIENTS = new ConcurrentHashMap<>();
    private static final Map<String, AtomicInteger> CLIENT_REFERENCES = new ConcurrentHashMap<>();

    protected MongoClient client;
    private MongoDatabase database;

    protected final String connectionString;
    protected final String dbName;

    @Getter
    protected final String serverIdentifier;

    private final Map<String, List<MongoChangeListener>> listeners = new ConcurrentHashMap<>();

    private Thread watchThread;
    private volatile boolean initialized;

    public MongoRepositoryManager(String uri, String db, String name) {
        this.connectionString = uri;
        this.dbName = db;

        /*
            Если имя сервера в конфиге пустое - генерим UUID, иначе берем из конфига
         */
        this.serverIdentifier = (name == null || name.isEmpty())
                ? UUID.randomUUID().toString()
                : name;
    }

    /**
     * Создает Ref для конкретной коллекции и автоматически регистрирует слушателя.
     */
    public MongoRef createRef(String collectionName, MongoChangeListener listener) {
        if (!initialized || database == null) {
            throw new IllegalStateException("MongoRepositoryManager is not initialized. Call init() before createRepository().");
        }

        MongoCollection<Document> collection = database.getCollection(collectionName);
        listeners.computeIfAbsent(collectionName, k -> new CopyOnWriteArrayList<>()).add(listener);
        return new MongoRef(this, collection, collectionName);
    }

    @Override
    public synchronized void init() {
        if (initialized) {
            return;
        }

        /*
            Получаем или создаем общий MongoClient для этого URI
         */
        this.client = SHARED_CLIENTS.computeIfAbsent(connectionString, uri -> {
            LOGGER.info("Creating new MongoDB Client for URI: {}", uri);
            ServerApi serverApi = ServerApi.builder().version(ServerApiVersion.V1).build();
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(uri))
                    .serverApi(serverApi)
                    .build();
            return MongoClients.create(settings);
        });

        // Увеличиваем счетчик использований
        CLIENT_REFERENCES.computeIfAbsent(connectionString, k -> new AtomicInteger(0)).incrementAndGet();
        this.database = client.getDatabase(dbName);
        initialized = true;
        startWatchingDatabase();
    }

    private void startWatchingDatabase() {
        if (watchThread != null && watchThread.isAlive()) {
            return;
        }

        watchThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try (MongoCursor<ChangeStreamDocument<Document>> cursor = database.watch()
                        .fullDocument(FullDocument.UPDATE_LOOKUP)
                        .cursor()) {

                    LOGGER.info("✅ SUCCESS: Global DB Watcher started for database '{}'", dbName);

                    while (cursor.hasNext() && !Thread.currentThread().isInterrupted()) {
                        ChangeStreamDocument<Document> change = cursor.next();

                        if (change.getNamespace() == null) continue;
                        String changedCollection = change.getNamespace().getCollectionName();

                        List<MongoChangeListener> targets = listeners.get(changedCollection);
                        if (targets == null || targets.isEmpty()) continue;

                        BsonDocument documentKey = change.getDocumentKey();
                        if (documentKey == null || !documentKey.containsKey("_id")) continue;
                        String rawId = documentKey.getString("_id").getValue().toString();

                        Document fullDoc = change.getFullDocument();
                        if (isOwnChange(fullDoc)) {
                            continue;
                        }

                        OperationType opType = change.getOperationType();
                        for (MongoChangeListener listener : targets) {
                            if (opType == OperationType.INSERT || opType == OperationType.REPLACE || opType == OperationType.UPDATE) {
                                listener.onRemoteUpdate(rawId);
                            } else if (opType == OperationType.DELETE) {
                                listener.onRemoteDelete(rawId);
                            }
                        }
                    }
                } catch (Exception e) {
                    if (Thread.currentThread().isInterrupted() || e instanceof InterruptedException) {
                        break;
                    }

                    if (isUnsupportedChangeStream(e)) {
                        LOGGER.warn("MongoDB change streams are not available for database '{}'. Repository storage will still work, but remote live-sync is disabled. Use a replica set if you need multi-server sync.", dbName);
                        break;
                    }

                    LOGGER.error("MongoDB Watcher disconnected! Retrying in 5 seconds...", e);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            LOGGER.info("🛑 Global MongoDB Watcher Thread stopped cleanly.");
        }, "SDM-Global-Watcher-" + dbName);

        watchThread.setDaemon(true);
        watchThread.start();
    }

    private boolean isOwnChange(@Nullable Document fullDoc) {
        if (fullDoc == null) {
            return false;
        }

        if (serverIdentifier.equals(fullDoc.getString("last_updated_by"))) {
            return true;
        }

        Object meta = fullDoc.get("_sdm_meta");
        if (meta instanceof Document metaDocument) {
            return serverIdentifier.equals(metaDocument.getString("updated_by"));
        }

        return false;
    }

    private boolean isUnsupportedChangeStream(Exception e) {
        if (e instanceof MongoCommandException commandException) {
            String codeName = commandException.getErrorCodeName();
            return commandException.getErrorCode() == 40573
                    || "IllegalOperation".equals(codeName)
                    || "CommandNotSupported".equals(codeName);
        }

        return false;
    }

    @Override
    public synchronized void close() {
        if (!initialized) {
            return;
        }

        if (watchThread != null && watchThread.isAlive()) {
            watchThread.interrupt();
        }
        watchThread = null;
        listeners.clear();
        initialized = false;

        AtomicInteger refs = CLIENT_REFERENCES.get(connectionString);
        if (refs != null) {
            int remaining = refs.decrementAndGet();

            /*
                Если это был последний мод, использующий этот URI - физически закрываем клиент
             */
            if (remaining <= 0) {
                LOGGER.info("Closing MongoDB Client for URI: {}", connectionString);
                MongoClient sharedClient = SHARED_CLIENTS.remove(connectionString);
                CLIENT_REFERENCES.remove(connectionString);

                if (sharedClient != null) {
                    sharedClient.close();
                }
            }
        }
    }

    @Override
    public <K, V> Repository<K, V> createRepository(@Nullable Path custom, @NonNull String collectionName, RepoDefinition<K, V> def) {
        return new MongoGenericRepository<>(
                this,
                collectionName,
                def.keyToString(),
                def.stringToKey(),
                def.extractKey(),
                def.serializer(),
                def.deserializer()
        );
    }

    public record MongoRef(MongoRepositoryManager manager, MongoCollection<Document> collection, String collectionName) { }

    public interface MongoChangeListener {
        void onRemoteUpdate(String rawId);
        void onRemoteDelete(String rawId);
    }
}
