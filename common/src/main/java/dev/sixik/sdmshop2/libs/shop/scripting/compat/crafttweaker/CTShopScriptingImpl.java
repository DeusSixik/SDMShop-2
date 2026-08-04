package dev.sixik.sdmshop2.libs.shop.scripting.compat.crafttweaker;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import dev.sixik.sdmshop2.libs.shop.promo.ShopPromoTriggers;
import dev.sixik.sdmshop2.libs.shop.scripting.events.ShopScriptEvents;
import net.minecraft.world.entity.player.Player;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.sdmshop.scripting.ShopScripting")
public class CTShopScriptingImpl {

    @ZenCodeType.Method
    public static void registerConditionEvent(ShopScriptEvents.ScripConditionEvent event) {
        ShopScriptEvents.SCRIP_CONDITION_EVENT.register(event);
    }

    @ZenCodeType.Method
    public static void registerRewardEvent(ShopScriptEvents.ScripRewardEvent event) {
        ShopScriptEvents.SCRIP_REWARD_EVENT.register(event);
    }

    @ZenCodeType.Method
    public static void registerPriceEvent(ShopScriptEvents.ScriptCalculatePriceEvent event) {
        ShopScriptEvents.SCRIPT_CALCULATE_PRICE_EVENT.register(event);
    }

    @ZenCodeType.Method
    public static void registerShopLoadEvent(ShopScriptEvents.ScriptShopLoadEvent event) {
        ShopScriptEvents.SCRIPT_SHOP_LOAD_EVENT.register(event);
    }

    @ZenCodeType.Method
    public static int triggerGlobalPromo(String triggerId) {
        return ShopPromoTriggers.triggerGlobal(triggerId);
    }

    @ZenCodeType.Method
    public static int triggerPlayerPromo(Player player, String triggerId) {
        return ShopPromoTriggers.triggerPlayer(player, triggerId);
    }

    @ZenCodeType.Method
    public static int stopGlobalPromoTrigger(String triggerId) {
        return ShopPromoTriggers.stopGlobalTrigger(triggerId);
    }

    @ZenCodeType.Method
    public static int stopPlayerPromoTrigger(Player player, String triggerId) {
        return ShopPromoTriggers.stopPlayerTrigger(player, triggerId);
    }

    @ZenCodeType.Method
    public static void activateGlobalPromo(String promoId, long durationMillis) {
        ShopPromoTriggers.activateGlobal(promoId, durationMillis);
    }

    @ZenCodeType.Method
    public static void activatePlayerPromo(Player player, String promoId, long durationMillis) {
        ShopPromoTriggers.activatePlayer(player, promoId, durationMillis);
    }

    @ZenCodeType.Method
    public static void deactivateGlobalPromo(String promoId) {
        ShopPromoTriggers.deactivateGlobal(promoId);
    }

    @ZenCodeType.Method
    public static void deactivatePlayerPromo(Player player, String promoId) {
        ShopPromoTriggers.deactivatePlayer(player, promoId);
    }
}
