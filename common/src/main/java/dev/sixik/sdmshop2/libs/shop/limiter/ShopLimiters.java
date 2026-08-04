package dev.sixik.sdmshop2.libs.shop.limiter;

import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.base.ShopTable;
import dev.sixik.sdmshop2.libs.shop.base.limiter.ShopLimiterTable;
import dev.sixik.sdmshop2.libs.shop.base.limiter.ShopLimiterTableServer;
import dev.sixik.sdmshop2.libs.shop.components.limiter.LimiterComponent;
import dev.sixik.sdmshop2.libs.shop.network.ShopNetworkManager;
import dev.sixik.sdmshop2.utils.ShopUtils;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Small facade for offer purchase limits.
 *
 * <p>Limiters are combined as constraints: if an offer has both World and Player limits,
 * purchase availability is the lowest remaining value and a successful purchase increments
 * every limiter.</p>
 */
public final class ShopLimiters {

    public static final int NO_LIMIT = Integer.MAX_VALUE;

    public static boolean hasLimiters(@Nullable ShopOffer offer) {
        return offer != null && !offer.getComponents(LimiterComponent.class).isEmpty();
    }

    public static boolean canPurchase(@Nullable ShopOffer offer, @Nullable Player player, int amount) {
        if (offer == null || player == null || amount <= 0) {
            return false;
        }

        if (!hasLimiters(offer)) {
            return true;
        }

        return getAvailable(offer, player) >= amount;
    }

    public static void recordPurchase(@Nullable ShopOffer offer, @Nullable Player player, int amount) {
        if (offer == null || player == null || amount <= 0) {
            return;
        }

        for (LimiterComponent limiter : offer.getComponents(LimiterComponent.class)) {
            limiter.addLimit(player, amount);
        }
    }

    public static int getAvailable(@Nullable ShopOffer offer, @Nullable Player player) {
        return getSnapshot(offer, player).getAvailable();
    }

    public static LimitSnapshot getSnapshot(@Nullable ShopOffer offer, @Nullable Player player) {
        if (offer == null || player == null) {
            return LimitSnapshot.noLimit();
        }

        int available = NO_LIMIT;
        int capacity = NO_LIMIT;
        LimiterComponent.LimiterType type = null;
        boolean limited = false;

        for (LimiterComponent limiter : offer.getComponents(LimiterComponent.class)) {
            int limiterAvailable = Math.max(0, limiter.getLimit(player));
            int limiterCapacity = Math.max(1, limiter.getCount());

            if (!limited || limiterAvailable < available) {
                available = limiterAvailable;
                capacity = limiterCapacity;
                type = limiter.getLimiterType();
            }

            limited = true;
        }

        return limited ? new LimitSnapshot(available, capacity, type) : LimitSnapshot.noLimit();
    }

    public static boolean resetOffer(@Nullable ShopOffer offer) {
        if (offer == null) {
            return false;
        }

        Optional<ShopLimiterTable> table = ShopUtils.getLimiterTable(false);
        if (table.isEmpty()) {
            return false;
        }

        boolean changed = false;
        for (UUID storageId : collectStorageIds(offer, LimiterComponent.LimiterType.World)) {
            changed |= table.get().resetOfferData(storageId);
        }
        for (UUID storageId : collectStorageIds(offer, LimiterComponent.LimiterType.Player)) {
            changed |= table.get().resetAllPlayerData(storageId) > 0;
        }

        if (changed) {
            syncAllPlayers(table.get());
        }
        return changed;
    }

    public static boolean resetOffer(UUID offerId) {
        ShopOffer offer = findOffer(offerId);
        if (offer != null) {
            return resetOffer(offer);
        }

        Optional<ShopLimiterTable> table = ShopUtils.getLimiterTable(false);
        if (offerId == null || table.isEmpty()) {
            return false;
        }

        boolean changed = table.get().resetOfferData(offerId);
        changed |= table.get().resetAllPlayerData(offerId) > 0;
        if (changed) {
            syncAllPlayers(table.get());
        }
        return changed;
    }

    public static boolean resetOfferLimit(@Nullable ShopOffer offer) {
        return resetOffer(offer);
    }

    public static boolean resetOfferLimit(UUID offerId) {
        return resetOffer(offerId);
    }

    public static boolean resetWorld(@Nullable ShopOffer offer) {
        if (offer == null) {
            return false;
        }

        Optional<ShopLimiterTable> table = ShopUtils.getLimiterTable(false);
        if (table.isEmpty()) {
            return false;
        }

        boolean changed = false;
        for (UUID storageId : collectStorageIds(offer, LimiterComponent.LimiterType.World)) {
            changed |= table.get().resetOfferData(storageId);
        }

        if (changed) {
            syncAllPlayers(table.get());
        }
        return changed;
    }

    public static boolean resetWorld(UUID offerId) {
        ShopOffer offer = findOffer(offerId);
        if (offer != null) {
            return resetWorld(offer);
        }

        Optional<ShopLimiterTable> table = ShopUtils.getLimiterTable(false);
        if (offerId == null || table.isEmpty()) {
            return false;
        }

        boolean changed = table.get().resetOfferData(offerId);
        if (changed) {
            syncAllPlayers(table.get());
        }
        return changed;
    }

    public static boolean resetPlayer(@Nullable ShopOffer offer, @Nullable Player player) {
        return offer != null && player != null && resetPlayer(offer, player.getUUID());
    }

    public static boolean resetPlayer(UUID offerId, UUID playerId) {
        ShopOffer offer = findOffer(offerId);
        if (offer != null) {
            return resetPlayer(offer, playerId);
        }

        Optional<ShopLimiterTable> table = ShopUtils.getLimiterTable(false);
        if (offerId == null || playerId == null || table.isEmpty()) {
            return false;
        }

        boolean changed = table.get().resetPlayerData(playerId, offerId);
        if (changed) {
            syncAllPlayers(table.get());
        }
        return changed;
    }

    public static boolean resetOfferByLoadedTable(UUID offerId) {
        return resetOffer(offerId);
    }

    public static boolean resetLimiter(@Nullable LimiterComponent limiter, @Nullable UUID playerId) {
        if (limiter == null) {
            return false;
        }

        Optional<ShopLimiterTable> table = ShopUtils.getLimiterTable(false);
        if (table.isEmpty()) {
            return false;
        }

        boolean changed;
        UUID storageId = limiter.getStorageId();
        if (limiter.getLimiterType() == LimiterComponent.LimiterType.Player) {
            changed = playerId == null
                    ? table.get().resetAllPlayerData(storageId) > 0
                    : table.get().resetPlayerData(playerId, storageId);
        } else {
            changed = table.get().resetOfferData(storageId);
        }

        if (changed) {
            syncAllPlayers(table.get());
        }
        return changed;
    }

    @Nullable
    public static ShopOffer findOffer(UUID offerId) {
        ShopTable table = ShopTable.Instance;
        if (table == null || offerId == null) {
            return null;
        }

        for (ShopInstance shop : table.getAllShops()) {
            ShopOffer offer = shop.getEntries().getEntry(offerId);
            if (offer != null) {
                return offer;
            }
        }

        return null;
    }

    private static void syncAllPlayers(ShopLimiterTable table) {
        if (table instanceof ShopLimiterTableServer serverTable && serverTable.getServer() != null) {
            ShopNetworkManager.sendLimiterData(serverTable.getServer().getPlayerList().getPlayers());
        }
    }

    private static boolean resetPlayer(ShopOffer offer, UUID playerId) {
        Optional<ShopLimiterTable> table = ShopUtils.getLimiterTable(false);
        if (offer == null || playerId == null || table.isEmpty()) {
            return false;
        }

        boolean changed = false;
        for (UUID storageId : collectStorageIds(offer, LimiterComponent.LimiterType.Player)) {
            changed |= table.get().resetPlayerData(playerId, storageId);
        }

        if (changed) {
            syncAllPlayers(table.get());
        }
        return changed;
    }

    private static Set<UUID> collectStorageIds(ShopOffer offer, LimiterComponent.LimiterType type) {
        Set<UUID> storageIds = new LinkedHashSet<>();
        storageIds.add(offer.getUUID());

        for (LimiterComponent limiter : offer.getComponents(LimiterComponent.class)) {
            if (limiter.getLimiterType() == type) {
                storageIds.add(limiter.getStorageId());
            }
        }

        return storageIds;
    }

    private ShopLimiters() {
    }

    public static final class LimitSnapshot {
        private static final LimitSnapshot NO_LIMIT_SNAPSHOT = new LimitSnapshot(NO_LIMIT, NO_LIMIT, null);

        private final int available;
        private final int capacity;
        private final LimiterComponent.LimiterType limiterType;

        private LimitSnapshot(int available, int capacity, @Nullable LimiterComponent.LimiterType limiterType) {
            this.available = Math.max(0, available);
            this.capacity = Math.max(1, capacity);
            this.limiterType = limiterType;
        }

        public static LimitSnapshot noLimit() {
            return NO_LIMIT_SNAPSHOT;
        }

        public boolean isLimited() {
            return limiterType != null;
        }

        public int getAvailable() {
            return available;
        }

        public int getCapacity() {
            return capacity;
        }

        @Nullable
        public LimiterComponent.LimiterType getLimiterType() {
            return limiterType;
        }
    }
}
