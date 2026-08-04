package dev.sixik.sdmshop2.libs.shop.scripting.compat.kubejs;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.sixik.sdmshop2.libs.shop.promo.ShopPromoTriggers;
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

    public int triggerGlobalPromo(String triggerId) {
        return ShopPromoTriggers.triggerGlobal(triggerId);
    }

    public int triggerPlayerPromo(Player player, String triggerId) {
        return ShopPromoTriggers.triggerPlayer(player, triggerId);
    }

    public int stopGlobalPromoTrigger(String triggerId) {
        return ShopPromoTriggers.stopGlobalTrigger(triggerId);
    }

    public int stopPlayerPromoTrigger(Player player, String triggerId) {
        return ShopPromoTriggers.stopPlayerTrigger(player, triggerId);
    }

    public void activateGlobalPromo(String promoId, long durationMillis) {
        ShopPromoTriggers.activateGlobal(promoId, durationMillis);
    }

    public void activatePlayerPromo(Player player, String promoId, long durationMillis) {
        ShopPromoTriggers.activatePlayer(player, promoId, durationMillis);
    }

    public void deactivateGlobalPromo(String promoId) {
        ShopPromoTriggers.deactivateGlobal(promoId);
    }

    public void deactivatePlayerPromo(Player player, String promoId) {
        ShopPromoTriggers.deactivatePlayer(player, promoId);
    }
}
