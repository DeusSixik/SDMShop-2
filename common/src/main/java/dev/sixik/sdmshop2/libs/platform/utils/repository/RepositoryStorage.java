package dev.sixik.sdmshop2.libs.platform.utils.repository;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * In-memory cache over a repository backend.
 *
 * <p>All local cache mutations are synchronized because Mongo change streams,
 * game thread reads and async save callbacks can touch this storage from
 * different threads.</p>
 */
public final class RepositoryStorage<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryStorage.class);

    private final Repository<K, V> repository;
    private final Supplier<Map<K, V>> mapFactory;
    private final ExecutorService ioExecutor;

    private final Object writeLock = new Object();

    private volatile Map<K, V> storage;

    public RepositoryStorage(Repository<K, V> repository, Supplier<Map<K, V>> createMap, ExecutorService ioExecutor) {
        this.repository = repository;
        this.mapFactory = createMap;
        this.storage = createMap.get();
        this.ioExecutor = ioExecutor;

        this.repository.setSyncCallbacks(
                this::reloadFromRemote,
                this::removeLocalCache
        );
    }

    public void load(K key) {
        final V value = repository.load(key);
        if (value == null) return;

        synchronized (writeLock) {
            storage.put(key, value);
        }
    }

    /**
     * Fully reloads the cache and swaps the map atomically after load completes.
     */
    public void loadAll() {
        Map<K, V> newMap = mapFactory.get();
        newMap.putAll(repository.loadAll());

        synchronized (writeLock) {
            this.storage = newMap;
        }
    }

    /**
     * Updates local cache immediately and persists the value asynchronously.
     */
    public void putValue(K key, V value) {
        synchronized (writeLock) {
            storage.put(key, value);
        }

        ioExecutor.submit(() -> {
            try {
                repository.save(key, value);
            } catch (Exception e) {
                LOGGER.error("Failed to async save value for key: {}", key, e);
            }
        });
    }

    public void update(K key) {
        final V value;
        synchronized (writeLock) {
            value = storage.get(key);
        }

        if (value != null) {
            ioExecutor.submit(() -> {
                try {
                    repository.save(key, value);
                } catch (Exception e) {
                    LOGGER.error("Failed to async update value for key: {}", key, e);
                }
            });
        }
    }

    /**
     * Called by remote sync backends, for example MongoDB change streams.
     */
    public void reloadFromRemote(K key) {
        V updatedValue = repository.load(key);

        synchronized (writeLock) {
            if (updatedValue != null) {
                storage.put(key, updatedValue);
            } else {
                storage.remove(key);
            }
        }
    }

    public void removeLocalCache(K key) {
        synchronized (writeLock) {
            storage.remove(key);
        }
    }

    @Nullable
    public V getValue(K key) {
        synchronized (writeLock) {
            final V cached = storage.get(key);
            if (cached != null) {
                return cached;
            }
        }

        final V loaded = repository.load(key);
        if (loaded == null) {
            return null;
        }

        synchronized (writeLock) {
            final V cached = storage.get(key);
            if (cached != null) {
                return cached;
            }

            storage.put(key, loaded);
            return loaded;
        }
    }

    public V getOrCreate(K key, Function<K, V> entityFactory) {
        final V cached = getValue(key);
        if (cached != null) {
            return cached;
        }

        final V newEntity = entityFactory.apply(key);
        synchronized (writeLock) {
            final V existing = storage.get(key);
            if (existing != null) {
                return existing;
            }
            storage.put(key, newEntity);
        }

        ioExecutor.submit(() -> {
            try {
                repository.save(key, newEntity);
            } catch (Exception e) {
                LOGGER.error("Failed to async save new entity for key: {}", key, e);
            }
        });

        return newEntity;
    }

    public Collection<V> getAllValues() {
        synchronized (writeLock) {
            return new ArrayList<>(storage.values());
        }
    }

    public Collection<K> getAllKeys() {
        synchronized (writeLock) {
            return new ArrayList<>(storage.keySet());
        }
    }

    public Map<K, V> getMap() {
        synchronized (writeLock) {
            return new Object2ObjectOpenHashMap<>(storage);
        }
    }

    /**
     * Removes local cache immediately and deletes the value asynchronously.
     */
    public void delete(K key) {
        final V oldData;
        synchronized (writeLock) {
            oldData = storage.remove(key);
        }

        ioExecutor.submit(() -> {
            try {
                repository.delete(key);
            } catch (Exception e) {
                LOGGER.error("Failed to async delete value for key: {}", key, e);

                if (oldData != null) {
                    synchronized (writeLock) {
                        storage.put(key, oldData);
                    }
                }
            }
        });
    }

    public int size() {
        synchronized (writeLock) {
            return storage.size();
        }
    }

    public void forEach(BiConsumer<? super K, ? super V> action) {
        getMap().forEach(action);
    }

    public void save(K key, V value) {
        synchronized (writeLock) {
            if (!storage.containsKey(key)) {
                return;
            }
        }

        try {
            repository.save(key, value);
        } catch (Exception e) {
            LOGGER.error("Failed to save value for key: {}", key, e);
        }
    }

    public boolean unload(K key) {
        synchronized (writeLock) {
            return storage.remove(key) != null;
        }
    }
}
