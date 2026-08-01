package dev.sixik.sdmshop2.libs.platform.utils.repositoryManager;

import dev.sixik.sdmshop2.libs.platform.utils.repository.RepositoryType;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Lightweight registry for custom repository managers.
 *
 * <p>This replaces the old event-based creation hook. A module that wants to
 * provide custom storage should register a factory once during init and then
 * select {@link RepositoryType#CUSTOM} in config.</p>
 */
public final class RepositoryManagerRegistry {

    private static final Map<String, Factory> FACTORIES = new Object2ObjectOpenHashMap<>();

    private RepositoryManagerRegistry() {
    }

    public static void register(String id, Factory factory) {
        FACTORIES.put(normalizeId(id), Objects.requireNonNull(factory, "factory"));
    }

    public static boolean unregister(String id) {
        return FACTORIES.remove(normalizeId(id)) != null;
    }

    public static RepositoryManager createOrDefault(String id, MinecraftServer server, RepositoryManager defaultManager) {
        return createOrDefault(id, server, () -> defaultManager);
    }

    public static RepositoryManager createOrDefault(String id, MinecraftServer server, Supplier<RepositoryManager> defaultFactory) {
        Factory factory = FACTORIES.get(normalizeId(id));
        return factory == null ? defaultFactory.get() : factory.create(server, null);
    }

    private static String normalizeId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Repository manager registry id cannot be blank.");
        }
        return id.trim().toLowerCase();
    }

    @FunctionalInterface
    public interface Factory {
        RepositoryManager create(MinecraftServer server, @Nullable RepositoryManager defaultManager);
    }
}
