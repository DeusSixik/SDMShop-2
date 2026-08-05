package dev.sixik.sdmshop2.libs.shop.promo;

import com.google.gson.*;
import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.platform.SDMPlatform;
import dev.sixik.sdmshop2.libs.platform.ServerOperation;
import dev.sixik.sdmshop2.libs.shop.components.api.PromoScope;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.LevelResource;
import org.jetbrains.annotations.Nullable;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Состояние времени выполнения для акций, которые активируются извне, например из скриптов.
 *
 * <p>Состояние хранится по id активации. Прямые акции могут использовать обычный {@code promo_id};
 * {@link dev.sixik.sdmshop2.libs.shop.components.promo.conditions.PromoTriggerComponent}
 * использует стабильный ключ для каждого товара, поэтому один и тот же {@code promo_id}
 * может иметь разные таймеры на разных товарах.</p>
 */
public final class PromoStateStore {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final long NEVER_EXPIRES = Long.MAX_VALUE;
    private static final String FILE_NAME = "promo_state.json";
    private static final Object IO_LOCK = new Object();
    private static final Map<PromoKey, PromoState> ACTIVE_PROMOS = new ConcurrentHashMap<>();
    private static volatile Path storageFile;

    public static void activateGlobal(String promoId, long durationMillis) {
        activate(promoId, PromoScope.GLOBAL, (UUID) null, durationMillis);
    }

    public static void activateGlobalUntil(String promoId, long expiresAtMillis) {
        activateUntil(promoId, PromoScope.GLOBAL, (UUID) null, expiresAtMillis);
    }

    public static void activatePlayer(Player player, String promoId, long durationMillis) {
        if (player == null) {
            throw new IllegalArgumentException("Player promo requires non-null player");
        }

        activatePlayer(player.getUUID(), promoId, durationMillis);
    }

    public static void activatePlayer(UUID playerId, String promoId, long durationMillis) {
        activate(promoId, PromoScope.PLAYER, playerId, durationMillis);
    }

    public static void activatePlayerUntil(Player player, String promoId, long expiresAtMillis) {
        if (player == null) {
            throw new IllegalArgumentException("Player promo requires non-null player");
        }

        activatePlayerUntil(player.getUUID(), promoId, expiresAtMillis);
    }

    public static void activatePlayerUntil(UUID playerId, String promoId, long expiresAtMillis) {
        activateUntil(promoId, PromoScope.PLAYER, playerId, expiresAtMillis);
    }

    public static void activate(String promoId, PromoScope scope, @Nullable Player player, long durationMillis) {
        activate(promoId, scope, player == null ? null : player.getUUID(), durationMillis);
    }

    public static void activate(String promoId, PromoScope scope, @Nullable UUID playerId, long durationMillis) {
        activateUntil(promoId, scope, playerId, expiresAtFromDuration(durationMillis));
    }

    public static void activateUntil(String promoId, PromoScope scope, @Nullable Player player, long expiresAtMillis) {
        activateUntil(promoId, scope, player == null ? null : player.getUUID(), expiresAtMillis);
    }

    public static void activateUntil(String promoId, PromoScope scope, @Nullable UUID playerId, long expiresAtMillis) {
        PromoKey key = keyOf(promoId, scope, playerId);
        ACTIVE_PROMOS.put(key, new PromoState(normalizeExpiresAt(expiresAtMillis)));
        saveNow();
    }

    public static void deactivateGlobal(String promoId) {
        deactivate(promoId, PromoScope.GLOBAL, (UUID) null);
    }

    public static void deactivatePlayer(Player player, String promoId) {
        if (player != null) {
            deactivatePlayer(player.getUUID(), promoId);
        }
    }

    public static void deactivatePlayer(UUID playerId, String promoId) {
        deactivate(promoId, PromoScope.PLAYER, playerId);
    }

    public static void deactivate(String promoId, PromoScope scope, @Nullable Player player) {
        deactivate(promoId, scope, player == null ? null : player.getUUID());
    }

    public static void deactivate(String promoId, PromoScope scope, @Nullable UUID playerId) {
        PromoKey key = keyOf(promoId, scope, playerId);
        if (ACTIVE_PROMOS.remove(key) != null) {
            saveNow();
        }
    }

    public static boolean isGlobalActive(String promoId) {
        return isActive(promoId, PromoScope.GLOBAL, (UUID) null);
    }

    public static boolean isPlayerActive(Player player, String promoId) {
        return player != null && isPlayerActive(player.getUUID(), promoId);
    }

    public static boolean isPlayerActive(UUID playerId, String promoId) {
        return isActive(promoId, PromoScope.PLAYER, playerId);
    }

    public static boolean isActive(String promoId, PromoScope scope, @Nullable Player player) {
        return isActive(promoId, scope, player == null ? null : player.getUUID());
    }

    public static boolean isActive(String promoId, PromoScope scope, @Nullable UUID playerId) {
        if (isBlank(promoId)) {
            return false;
        }

        PromoKey key = keyOf(promoId, scope, playerId);
        PromoState state = ACTIVE_PROMOS.get(key);
        if (state == null) {
            return false;
        }

        if (state.isExpired(System.currentTimeMillis())) {
            ACTIVE_PROMOS.remove(key, state);
            return false;
        }

        return true;
    }

    public static long remainingMillis(String promoId, PromoScope scope, @Nullable Player player) {
        return remainingMillis(promoId, scope, player == null ? null : player.getUUID());
    }

    public static long remainingMillis(String promoId, PromoScope scope, @Nullable UUID playerId) {
        if (isBlank(promoId)) {
            return 0L;
        }

        PromoKey key = keyOf(promoId, scope, playerId);
        PromoState state = ACTIVE_PROMOS.get(key);
        if (state == null) {
            return 0L;
        }

        long now = System.currentTimeMillis();
        if (state.isExpired(now)) {
            ACTIVE_PROMOS.remove(key, state);
            return 0L;
        }

        return state.expiresAtMillis == NEVER_EXPIRES ? NEVER_EXPIRES : Math.max(0L, state.expiresAtMillis - now);
    }

    public static void clearExpired() {
        long now = System.currentTimeMillis();
        if (ACTIVE_PROMOS.entrySet().removeIf(entry -> entry.getValue().isExpired(now))) {
            saveNow();
        }
    }

    public static void clearAll() {
        if (!ACTIVE_PROMOS.isEmpty()) {
            ACTIVE_PROMOS.clear();
            saveNow();
        }
    }

    public static void load(Path file) {
        synchronized (IO_LOCK) {
            storageFile = file;
            ACTIVE_PROMOS.clear();

            if (file == null || !Files.isRegularFile(file)) {
                return;
            }

            try (Reader reader = Files.newBufferedReader(file)) {
                JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                JsonArray promos = root.has("promos") && root.get("promos").isJsonArray()
                        ? root.getAsJsonArray("promos")
                        : new JsonArray();

                long now = System.currentTimeMillis();
                for (JsonElement element : promos) {
                    if (!element.isJsonObject()) {
                        continue;
                    }

                    PromoEntry entry = readEntry(element.getAsJsonObject());
                    if (entry == null || entry.state().isExpired(now)) {
                        continue;
                    }

                    ACTIVE_PROMOS.put(entry.key(), entry.state());
                }

                SDMShop2.LOGGER.info("Loaded {} active triggered promos from {}", ACTIVE_PROMOS.size(), file);
            } catch (Exception exception) {
                SDMShop2.LOGGER.error("Failed to load triggered promo state from {}, starting fresh", file, exception);
                ACTIVE_PROMOS.clear();
            }
        }
    }

    public static void saveNow() {
        synchronized (IO_LOCK) {
            Path file = storageFile;
            if (file == null) {
                return;
            }

            long now = System.currentTimeMillis();
            ACTIVE_PROMOS.entrySet().removeIf(entry -> entry.getValue().isExpired(now));

            try {
                if (file.getParent() != null) {
                    Files.createDirectories(file.getParent());
                }

                JsonObject root = new JsonObject();
                root.addProperty("version", 1);

                JsonArray promos = new JsonArray();
                for (Map.Entry<PromoKey, PromoState> entry : ACTIVE_PROMOS.entrySet()) {
                    promos.add(writeEntry(entry.getKey(), entry.getValue()));
                }
                root.add("promos", promos);

                Path tmpFile = file.resolveSibling(file.getFileName() + ".tmp");
                try (Writer writer = Files.newBufferedWriter(tmpFile)) {
                    GSON.toJson(root, writer);
                }

                try {
                    Files.move(tmpFile, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                } catch (AtomicMoveNotSupportedException ignored) {
                    Files.move(tmpFile, file, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (Exception exception) {
                SDMShop2.LOGGER.error("Failed to save triggered promo state to {}", file, exception);
            }
        }
    }

    private static JsonObject writeEntry(PromoKey key, PromoState state) {
        JsonObject object = new JsonObject();
        object.addProperty("state_id", key.promoId());
        object.addProperty("scope", key.scope().name());
        object.addProperty("expires_at", state.expiresAtMillis());
        if (key.playerId() != null) {
            object.addProperty("player_id", key.playerId().toString());
        }
        return object;
    }

    @Nullable
    private static PromoEntry readEntry(JsonObject object) {
        if ((!object.has("state_id") && !object.has("promo_id")) || !object.has("scope") || !object.has("expires_at")) {
            return null;
        }

        try {
            String promoId = object.has("state_id")
                    ? object.get("state_id").getAsString()
                    : object.get("promo_id").getAsString();
            PromoScope scope = PromoScope.valueOf(object.get("scope").getAsString().toUpperCase(Locale.ROOT));
            UUID playerId = null;
            if (scope == PromoScope.PLAYER) {
                if (!object.has("player_id")) {
                    return null;
                }
                playerId = UUID.fromString(object.get("player_id").getAsString());
            }

            PromoKey key = keyOf(promoId, scope, playerId);
            PromoState state = new PromoState(normalizeExpiresAt(object.get("expires_at").getAsLong()));
            return new PromoEntry(key, state);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static PromoKey keyOf(String promoId, PromoScope scope, @Nullable UUID playerId) {
        if (isBlank(promoId)) {
            throw new IllegalArgumentException("promoId can't be blank");
        }

        PromoScope normalizedScope = scope == null ? PromoScope.GLOBAL : scope;
        UUID normalizedPlayerId = normalizedScope == PromoScope.PLAYER ? playerId : null;
        if (normalizedScope == PromoScope.PLAYER && normalizedPlayerId == null) {
            throw new IllegalArgumentException("Player promo requires player UUID");
        }

        return new PromoKey(normalizedPromoId(promoId), normalizedScope, normalizedPlayerId);
    }

    private static String normalizedPromoId(String promoId) {
        return promoId.trim().toLowerCase(Locale.ROOT);
    }

    private static long expiresAtFromDuration(long durationMillis) {
        if (durationMillis <= 0L) {
            return NEVER_EXPIRES;
        }

        long now = System.currentTimeMillis();
        long expiresAt = now + durationMillis;
        return expiresAt < now ? NEVER_EXPIRES : expiresAt;
    }

    private static long normalizeExpiresAt(long expiresAtMillis) {
        return expiresAtMillis <= 0L ? NEVER_EXPIRES : expiresAtMillis;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private PromoStateStore() {
    }

    private record PromoKey(String promoId, PromoScope scope, @Nullable UUID playerId) {
    }

    private record PromoState(long expiresAtMillis) {

        private boolean isExpired(long now) {
            return expiresAtMillis != NEVER_EXPIRES && expiresAtMillis <= now;
        }
    }

    private record PromoEntry(PromoKey key, PromoState state) {
    }

    public static class Manager implements ServerOperation {

        @Override
        public void onServerStart(MinecraftServer server) {
            Path shopDir = SDMPlatform.resolveSdmDir(server.getWorldPath(LevelResource.ROOT), "shop");
            PromoStateStore.load(shopDir.resolve(FILE_NAME));
        }

        @Override
        public void onServerStop(MinecraftServer server) {
            PromoStateStore.saveNow();
            storageFile = null;
            ACTIVE_PROMOS.clear();
        }

        @Override
        public void onReload() {
            PromoStateStore.clearExpired();
        }
    }
}
