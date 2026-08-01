package dev.sixik.sdmshop2.libs.platform.utils.repository;

import com.google.gson.JsonObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import dev.sixik.sdmshop2.libs.platform.utils.repositoryManager.MongoRepositoryManager;
import dev.sixik.sdmshop2.libs.platform.utils.repositoryManager.RepoDefinition;
import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class MongoRepositoryIntegrationTest {

    private static final String RAW_MONGO_URI = System.getProperty(
            "sdmshop2.test.mongodb.uri",
            "mongodb://127.0.0.1:27017/?replicaSet=rs0"
    );
    private static final String MONGO_URI = withServerSelectionTimeout(RAW_MONGO_URI);

    private static boolean mongoAvailable;
    private static boolean changeStreamsAvailable;
    private static String mongoSkipReason;

    @BeforeAll
    static void checkMongo() {
        try (MongoClient client = MongoClients.create(clientSettings())) {
            Document ping = client.getDatabase("admin").runCommand(new Document("ping", 1));
            mongoAvailable = ping.getDouble("ok") == 1.0;

            Document hello = client.getDatabase("admin").runCommand(new Document("hello", 1));
            changeStreamsAvailable = hello.containsKey("setName") || "isdbgrid".equals(hello.getString("msg"));
            mongoSkipReason = changeStreamsAvailable
                    ? null
                    : "MongoDB доступна, но change streams не поддерживаются. Нужен replica set, например URI: " + RAW_MONGO_URI;
        } catch (Exception e) {
            mongoAvailable = false;
            changeStreamsAvailable = false;
            mongoSkipReason = "MongoDB недоступна по URI " + RAW_MONGO_URI + ": " + e.getMessage();
        }
    }

    @AfterAll
    static void cleanupLeftovers() {
        if (!mongoAvailable) {
            return;
        }

        try (MongoClient client = MongoClients.create(clientSettings())) {
            for (String databaseName : client.listDatabaseNames()) {
                if (databaseName.startsWith("sdmshop2_repository_test_")) {
                    client.getDatabase(databaseName).drop();
                }
            }
        }
    }

    @Test
    void createRepositoryRequiresInitializedManager() {
        assumeMongoAvailable();

        MongoRepositoryManager manager = new MongoRepositoryManager(MONGO_URI, newDatabaseName(), "server-a");
        try {
            IllegalStateException error = assertThrows(
                    IllegalStateException.class,
                    () -> manager.createRepository(null, "entities", definition())
            );
            assertTrue(error.getMessage().contains("init"));
        } finally {
            manager.close();
        }
    }

    @Test
    void managerInitAndCloseAreIdempotent() {
        assumeMongoAvailable();

        String databaseName = newDatabaseName();
        MongoRepositoryManager manager = new MongoRepositoryManager(MONGO_URI, databaseName, "server-a");
        try {
            manager.init();
            manager.init();

            Repository<String, TestEntity> repository = manager.createRepository(null, "lifecycle_entities", definition());
            TestEntity value = new TestEntity("idempotent", "Idempotent", 5, true, "lifecycle");

            repository.save(value.id(), value);
            assertEquals(value, repository.load(value.id()));

            manager.close();
            manager.close();
        } finally {
            manager.close();
            try (MongoClient cleanupClient = MongoClients.create(clientSettings())) {
                cleanupClient.getDatabase(databaseName).drop();
            }
        }
    }

    @Test
    void repositorySavesLoadsListsAndDeletesDocuments() {
        assumeMongoAvailable();

        try (MongoTestContext ctx = MongoTestContext.open("server-a")) {
            Repository<String, TestEntity> repository = ctx.repository("entities");

            TestEntity alpha = new TestEntity("alpha", "Alpha", 10, true, "deep-value");
            TestEntity beta = new TestEntity("beta", "Beta", 20, false, "other-value");

            repository.save(alpha.id(), alpha);
            repository.save(beta.id(), beta);

            assertEquals(alpha, repository.load("alpha"));
            assertEquals(beta, repository.load("beta"));
            assertNull(repository.load("missing"));

            Map<String, TestEntity> loaded = repository.loadAll();
            assertEquals(2, loaded.size());
            assertEquals(alpha, loaded.get("alpha"));
            assertEquals(beta, loaded.get("beta"));

            Document raw = ctx.collection("entities").find(Filters.eq("_id", "alpha")).first();
            assertNotNull(raw);
            assertEquals("server-a", raw.getString("last_updated_by"));
            assertNotNull(raw.get("_sdm_meta", Document.class));

            repository.delete("alpha");
            assertNull(repository.load("alpha"));
            assertEquals(beta, repository.load("beta"));
        }
    }

    @Test
    void repositorySupportsBulkSaveAndDeleteDefaults() {
        assumeMongoAvailable();

        try (MongoTestContext ctx = MongoTestContext.open("server-a")) {
            Repository<String, TestEntity> repository = ctx.repository("bulk_entities");

            repository.saveAll(Map.of(
                    "one", new TestEntity("one", "One", 1, true, "n1"),
                    "two", new TestEntity("two", "Two", 2, false, "n2"),
                    "three", new TestEntity("three", "Three", 3, true, "n3")
            ));

            assertEquals(3, repository.loadAll().size());

            repository.deleteAll(java.util.List.of("one", "three"));

            assertNull(repository.load("one"));
            assertEquals(new TestEntity("two", "Two", 2, false, "n2"), repository.load("two"));
            assertNull(repository.load("three"));
        }
    }

    @Test
    void repositoryStorageReceivesRemoteMongoUpdatesAndDeletes() {
        assumeMongoAvailable();
        assumeTrue(changeStreamsAvailable, mongoSkipReason);

        String databaseName = newDatabaseName();
        try (
                MongoTestContext serverA = MongoTestContext.open(databaseName, "server-a");
                MongoTestContext serverB = MongoTestContext.open(databaseName, "server-b")
        ) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            try {
                RepositoryStorage<String, TestEntity> storage = new RepositoryStorage<>(
                        serverA.repository("synced_entities"),
                        java.util.concurrent.ConcurrentHashMap::new,
                        executor
                );
                Repository<String, TestEntity> remoteRepository = serverB.repository("synced_entities");

                storage.loadAll();
                assertEquals(0, storage.size());

                TestEntity remote = new TestEntity("remote", "Remote", 42, true, "from-server-b");
                remoteRepository.save(remote.id(), remote);

                assertEventually(
                        () -> remote.equals(storage.getMap().get("remote")),
                        Duration.ofSeconds(8),
                        "RepositoryStorage не получил remote update из Mongo change stream"
                );

                remoteRepository.delete("remote");

                assertEventually(
                        () -> !storage.getMap().containsKey("remote"),
                        Duration.ofSeconds(8),
                        "RepositoryStorage не получил remote delete из Mongo change stream"
                );
            } finally {
                executor.shutdownNow();
            }
        }
    }

    @Test
    void sharedMongoClientSurvivesUntilLastManagerIsClosed() {
        assumeMongoAvailable();

        String databaseName = newDatabaseName();
        MongoTestContext serverA = MongoTestContext.open(databaseName, "server-a");
        MongoTestContext serverB = MongoTestContext.open(databaseName, "server-b");

        try {
            Repository<String, TestEntity> repositoryB = serverB.repository("shared_client_entities");
            serverA.close();

            TestEntity value = new TestEntity("alive", "Still Alive", 7, true, "after-close");
            repositoryB.save(value.id(), value);

            assertEquals(value, repositoryB.load(value.id()));
        } finally {
            serverB.close();
        }
    }

    private static void assumeMongoAvailable() {
        assumeTrue(mongoAvailable, mongoSkipReason);
    }

    private static RepoDefinition<String, TestEntity> definition() {
        return new RepoDefinition<>(
                key -> key,
                key -> key,
                TestEntity::id,
                TestEntity::toJson,
                TestEntity::fromJson
        );
    }

    private static MongoClientSettings clientSettings() {
        return MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(MONGO_URI))
                .applyToClusterSettings(builder -> builder.serverSelectionTimeout(2, TimeUnit.SECONDS))
                .build();
    }

    private static String withServerSelectionTimeout(String uri) {
        if (uri.contains("serverSelectionTimeoutMS=")) {
            return uri;
        }
        return uri + (uri.contains("?") ? "&" : "?") + "serverSelectionTimeoutMS=2000";
    }

    private static String newDatabaseName() {
        return "sdmshop2_repository_test_" + UUID.randomUUID().toString().replace("-", "");
    }

    private static void assertEventually(BooleanSupplier condition, Duration timeout, String message) {
        long deadline = System.nanoTime() + timeout.toNanos();
        AssertionError lastError = null;

        while (System.nanoTime() < deadline) {
            try {
                if (condition.getAsBoolean()) {
                    return;
                }
            } catch (AssertionError error) {
                lastError = error;
            }

            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new AssertionError(message, e);
            }
        }

        AssertionError error = new AssertionError(message);
        if (lastError != null) {
            error.addSuppressed(lastError);
        }
        throw error;
    }

    private static final class MongoTestContext implements AutoCloseable {

        private final String databaseName;
        private final MongoRepositoryManager manager;
        private final MongoClient cleanupClient;

        private MongoTestContext(String databaseName, String serverName) {
            this.databaseName = databaseName;
            this.manager = new MongoRepositoryManager(MONGO_URI, databaseName, serverName);
            this.manager.init();
            this.cleanupClient = MongoClients.create(clientSettings());
        }

        static MongoTestContext open(String serverName) {
            return open(newDatabaseName(), serverName);
        }

        static MongoTestContext open(String databaseName, String serverName) {
            return new MongoTestContext(databaseName, serverName);
        }

        Repository<String, TestEntity> repository(String collectionName) {
            return manager.createRepository(null, collectionName, definition());
        }

        MongoCollection<Document> collection(String collectionName) {
            MongoDatabase database = cleanupClient.getDatabase(databaseName);
            return database.getCollection(collectionName);
        }

        @Override
        public void close() {
            try {
                manager.close();
            } finally {
                try {
                    cleanupClient.getDatabase(databaseName).drop();
                } finally {
                    cleanupClient.close();
                }
            }
        }
    }

    private record TestEntity(String id, String name, int amount, boolean enabled, String nestedValue) {

        JsonObject toJson() {
            JsonObject json = new JsonObject();
            json.addProperty("id", id);
            json.addProperty("name", name);
            json.addProperty("amount", amount);
            json.addProperty("enabled", enabled);

            JsonObject nested = new JsonObject();
            nested.addProperty("value", nestedValue);
            json.add("nested", nested);
            return json;
        }

        static TestEntity fromJson(JsonObject json) {
            assertFalse(json.has("_id"), "Mongo _id не должен попадать в deserializer");
            assertFalse(json.has("last_updated_by"), "legacy metadata не должна попадать в deserializer");
            assertFalse(json.has("_sdm_meta"), "Mongo metadata не должна попадать в deserializer");

            return new TestEntity(
                    json.get("id").getAsString(),
                    json.get("name").getAsString(),
                    json.get("amount").getAsInt(),
                    json.get("enabled").getAsBoolean(),
                    json.getAsJsonObject("nested").get("value").getAsString()
            );
        }
    }
}
