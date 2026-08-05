package dev.sixik.sdmshop2.libs.shop.promo;

import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.base.ShopTable;
import dev.sixik.sdmshop2.libs.shop.components.api.PromoScope;
import dev.sixik.sdmshop2.libs.shop.components.promo.conditions.PromoTriggerComponent;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

/**
 * Небольшой удобный для скриптов фасад над {@link PromoStateStore}.
 */
public final class ShopPromoTriggers {

    public static int triggerGlobal(String triggerId) {
        int activated = 0;
        for (PromoTriggerComponent component : findTriggers(triggerId)) {
            if (component.getScope() == PromoScope.GLOBAL) {
                component.triggerGlobal();
                activated++;
            }
        }
        return activated;
    }

    public static int triggerPlayer(Player player, String triggerId) {
        if (player == null) {
            return 0;
        }

        int activated = 0;
        for (PromoTriggerComponent component : findTriggers(triggerId)) {
            component.trigger(player);
            activated++;
        }
        return activated;
    }

    public static int stopGlobalTrigger(String triggerId) {
        int stopped = 0;
        for (PromoTriggerComponent component : findTriggers(triggerId)) {
            if (component.getScope() == PromoScope.GLOBAL) {
                component.stopGlobal();
                stopped++;
            }
        }
        return stopped;
    }

    public static int stopPlayerTrigger(Player player, String triggerId) {
        if (player == null) {
            return 0;
        }

        int stopped = 0;
        for (PromoTriggerComponent component : findTriggers(triggerId)) {
            component.stop(player);
            stopped++;
        }
        return stopped;
    }

    public static void activateGlobal(String promoId, long durationMillis) {
        PromoStateStore.activateGlobal(promoId, durationMillis);
    }

    public static void activatePlayer(Player player, String promoId, long durationMillis) {
        PromoStateStore.activatePlayer(player, promoId, durationMillis);
    }

    public static void activatePlayer(UUID playerId, String promoId, long durationMillis) {
        PromoStateStore.activatePlayer(playerId, promoId, durationMillis);
    }

    public static void deactivateGlobal(String promoId) {
        PromoStateStore.deactivateGlobal(promoId);
    }

    public static void deactivatePlayer(Player player, String promoId) {
        PromoStateStore.deactivatePlayer(player, promoId);
    }

    public static void deactivatePlayer(UUID playerId, String promoId) {
        PromoStateStore.deactivatePlayer(playerId, promoId);
    }

    public static boolean isGlobalActive(String promoId) {
        return PromoStateStore.isGlobalActive(promoId);
    }

    public static boolean isPlayerActive(Player player, String promoId) {
        return PromoStateStore.isPlayerActive(player, promoId);
    }

    public static boolean isPlayerActive(UUID playerId, String promoId) {
        return PromoStateStore.isPlayerActive(playerId, promoId);
    }

    private static Iterable<PromoTriggerComponent> findTriggers(String triggerId) {
        java.util.List<PromoTriggerComponent> out = new java.util.ArrayList<>();
        ShopTable table = ShopTable.Instance;
        if (table == null || triggerId == null || triggerId.trim().isEmpty()) {
            return out;
        }

        for (ShopInstance shop : table.getAllShops()) {
            for (ShopOffer offer : shop.getEntries().getEntryMap().values()) {
                for (PromoTriggerComponent component : offer.getComponents(PromoTriggerComponent.class)) {
                    if (component.matchesTrigger(triggerId)) {
                        out.add(component);
                    }
                }
            }
        }

        return out;
    }

    private ShopPromoTriggers() {
    }
}
