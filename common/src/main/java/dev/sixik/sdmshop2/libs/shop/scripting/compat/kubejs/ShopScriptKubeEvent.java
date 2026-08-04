package dev.sixik.sdmshop2.libs.shop.scripting.compat.kubejs;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.sixik.sdmshop2.libs.shop.scripting.ShopScriptApi;
import dev.sixik.sdmshop2.libs.shop.scripting.events.ShopScriptEvents;
import net.minecraft.world.entity.player.Player;

public class ShopScriptKubeEvent extends EventJS {

    public void registerConditionEvent(ShopScriptEvents.ScripConditionEvent event) {
        ShopScriptEvents.SCRIP_CONDITION_EVENT.register(event);
    }

    public void registerRewardEvent(ShopScriptEvents.ScripRewardEvent event) {
        ShopScriptEvents.SCRIP_REWARD_EVENT.register(event);
    }

    public void registerPriceEvent(ShopScriptEvents.ScriptCalculatePriceEvent event) {
        ShopScriptEvents.SCRIPT_CALCULATE_PRICE_EVENT.register(event);
    }

    public void registerShopLoadEvent(ShopScriptEvents.ScriptShopLoadEvent event) {
        ShopScriptEvents.SCRIPT_SHOP_LOAD_EVENT.register(event);
    }

    public void registerBeforePurchaseEvent(ShopScriptEvents.ScriptBeforePurchaseEvent event) {
        ShopScriptEvents.SCRIPT_BEFORE_PURCHASE_EVENT.register(event);
    }

    public void registerAfterPurchaseEvent(ShopScriptEvents.ScriptAfterPurchaseEvent event) {
        ShopScriptEvents.SCRIPT_AFTER_PURCHASE_EVENT.register(event);
    }

    public void registerPurchaseFailedEvent(ShopScriptEvents.ScriptPurchaseFailedEvent event) {
        ShopScriptEvents.SCRIPT_PURCHASE_FAILED_EVENT.register(event);
    }

    public int triggerGlobalPromo(String triggerId) {
        return ShopScriptApi.triggerGlobalPromo(triggerId);
    }

    public int triggerPlayerPromo(Player player, String triggerId) {
        return ShopScriptApi.triggerPlayerPromo(player, triggerId);
    }

    public int stopGlobalPromoTrigger(String triggerId) {
        return ShopScriptApi.stopGlobalPromoTrigger(triggerId);
    }

    public int stopPlayerPromoTrigger(Player player, String triggerId) {
        return ShopScriptApi.stopPlayerPromoTrigger(player, triggerId);
    }

    public void activateGlobalPromo(String promoId, long durationMillis) {
        ShopScriptApi.activateGlobalPromo(promoId, durationMillis);
    }

    public void activatePlayerPromo(Player player, String promoId, long durationMillis) {
        ShopScriptApi.activatePlayerPromo(player, promoId, durationMillis);
    }

    public void deactivateGlobalPromo(String promoId) {
        ShopScriptApi.deactivateGlobalPromo(promoId);
    }

    public void deactivatePlayerPromo(Player player, String promoId) {
        ShopScriptApi.deactivatePlayerPromo(player, promoId);
    }

    public boolean isGlobalPromoActive(String promoId) {
        return ShopScriptApi.isGlobalPromoActive(promoId);
    }

    public boolean isPlayerPromoActive(Player player, String promoId) {
        return ShopScriptApi.isPlayerPromoActive(player, promoId);
    }

    public boolean resetOfferLimits(String offerId) {
        return ShopScriptApi.resetOfferLimits(offerId);
    }

    public boolean resetWorldLimits(String offerId) {
        return ShopScriptApi.resetWorldLimits(offerId);
    }

    public boolean resetPlayerLimits(Player player, String offerId) {
        return ShopScriptApi.resetPlayerLimits(player, offerId);
    }

    public boolean canPurchase(Player player, String offerId, int amount) {
        return ShopScriptApi.canPurchase(player, offerId, amount);
    }

    public int getAvailableLimit(Player player, String offerId) {
        return ShopScriptApi.getAvailableLimit(player, offerId);
    }

    public void syncLimiterData(Player player) {
        ShopScriptApi.syncLimiterData(player);
    }
}
