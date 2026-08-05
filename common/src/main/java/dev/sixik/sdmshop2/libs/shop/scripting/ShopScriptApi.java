package dev.sixik.sdmshop2.libs.shop.scripting;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.limiter.ShopLimiters;
import dev.sixik.sdmshop2.libs.shop.network.ShopNetworkManager;
import dev.sixik.sdmshop2.libs.shop.promo.ShopPromoTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Удобный для скриптов фасад для основных операций магазина.
 *
 * <p>Методы должны быть защитными: скрипты должны получать false/0 вместо шумных
 * исключений при отсутствующих игроках, некорректных строках UUID или незагруженных товарах.</p>
 */
public final class ShopScriptApi {

    public static int triggerGlobalPromo(String triggerId) {
        return ShopPromoTriggers.triggerGlobal(triggerId);
    }

    public static int triggerPlayerPromo(Player player, String triggerId) {
        return ShopPromoTriggers.triggerPlayer(player, triggerId);
    }

    public static int stopGlobalPromoTrigger(String triggerId) {
        return ShopPromoTriggers.stopGlobalTrigger(triggerId);
    }

    public static int stopPlayerPromoTrigger(Player player, String triggerId) {
        return ShopPromoTriggers.stopPlayerTrigger(player, triggerId);
    }

    public static void activateGlobalPromo(String promoId, long durationMillis) {
        ShopPromoTriggers.activateGlobal(promoId, durationMillis);
    }

    public static void activatePlayerPromo(Player player, String promoId, long durationMillis) {
        ShopPromoTriggers.activatePlayer(player, promoId, durationMillis);
    }

    public static void deactivateGlobalPromo(String promoId) {
        ShopPromoTriggers.deactivateGlobal(promoId);
    }

    public static void deactivatePlayerPromo(Player player, String promoId) {
        ShopPromoTriggers.deactivatePlayer(player, promoId);
    }

    public static boolean isGlobalPromoActive(String promoId) {
        return ShopPromoTriggers.isGlobalActive(promoId);
    }

    public static boolean isPlayerPromoActive(Player player, String promoId) {
        return ShopPromoTriggers.isPlayerActive(player, promoId);
    }

    public static boolean resetOfferLimits(String offerId) {
        UUID uuid = parseUuid(offerId);
        return uuid != null && ShopLimiters.resetOffer(uuid);
    }

    public static boolean resetWorldLimits(String offerId) {
        UUID uuid = parseUuid(offerId);
        return uuid != null && ShopLimiters.resetWorld(uuid);
    }

    public static boolean resetPlayerLimits(Player player, String offerId) {
        UUID uuid = parseUuid(offerId);
        return player != null && uuid != null && ShopLimiters.resetPlayer(uuid, player.getUUID());
    }

    public static boolean canPurchase(Player player, String offerId, int amount) {
        ShopOffer offer = findOffer(offerId);
        return ShopLimiters.canPurchase(offer, player, amount);
    }

    public static int getAvailableLimit(Player player, String offerId) {
        ShopOffer offer = findOffer(offerId);
        return offer == null || player == null ? 0 : ShopLimiters.getAvailable(offer, player);
    }

    public static void syncLimiterData(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            ShopNetworkManager.sendLimiterData(serverPlayer);
        }
    }

    @Nullable
    public static ShopOffer findOffer(String offerId) {
        UUID uuid = parseUuid(offerId);
        return uuid == null ? null : ShopLimiters.findOffer(uuid);
    }

    @Nullable
    private static UUID parseUuid(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return UUID.fromString(value.trim());
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private ShopScriptApi() {
    }
}
