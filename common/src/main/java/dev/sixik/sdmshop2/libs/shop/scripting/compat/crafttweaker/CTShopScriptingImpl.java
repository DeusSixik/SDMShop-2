package dev.sixik.sdmshop2.libs.shop.scripting.compat.crafttweaker;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import dev.sixik.sdmshop2.libs.shop.scripting.ShopScriptApi;
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
    public static void registerBeforePurchaseEvent(ShopScriptEvents.ScriptBeforePurchaseEvent event) {
        ShopScriptEvents.SCRIPT_BEFORE_PURCHASE_EVENT.register(event);
    }

    @ZenCodeType.Method
    public static void registerAfterPurchaseEvent(ShopScriptEvents.ScriptAfterPurchaseEvent event) {
        ShopScriptEvents.SCRIPT_AFTER_PURCHASE_EVENT.register(event);
    }

    @ZenCodeType.Method
    public static void registerPurchaseFailedEvent(ShopScriptEvents.ScriptPurchaseFailedEvent event) {
        ShopScriptEvents.SCRIPT_PURCHASE_FAILED_EVENT.register(event);
    }

    @ZenCodeType.Method
    public static int triggerGlobalPromo(String triggerId) {
        return ShopScriptApi.triggerGlobalPromo(triggerId);
    }

    @ZenCodeType.Method
    public static int triggerPlayerPromo(Player player, String triggerId) {
        return ShopScriptApi.triggerPlayerPromo(player, triggerId);
    }

    @ZenCodeType.Method
    public static int stopGlobalPromoTrigger(String triggerId) {
        return ShopScriptApi.stopGlobalPromoTrigger(triggerId);
    }

    @ZenCodeType.Method
    public static int stopPlayerPromoTrigger(Player player, String triggerId) {
        return ShopScriptApi.stopPlayerPromoTrigger(player, triggerId);
    }

    @ZenCodeType.Method
    public static void activateGlobalPromo(String promoId, long durationMillis) {
        ShopScriptApi.activateGlobalPromo(promoId, durationMillis);
    }

    @ZenCodeType.Method
    public static void activatePlayerPromo(Player player, String promoId, long durationMillis) {
        ShopScriptApi.activatePlayerPromo(player, promoId, durationMillis);
    }

    @ZenCodeType.Method
    public static void deactivateGlobalPromo(String promoId) {
        ShopScriptApi.deactivateGlobalPromo(promoId);
    }

    @ZenCodeType.Method
    public static void deactivatePlayerPromo(Player player, String promoId) {
        ShopScriptApi.deactivatePlayerPromo(player, promoId);
    }

    @ZenCodeType.Method
    public static boolean isGlobalPromoActive(String promoId) {
        return ShopScriptApi.isGlobalPromoActive(promoId);
    }

    @ZenCodeType.Method
    public static boolean isPlayerPromoActive(Player player, String promoId) {
        return ShopScriptApi.isPlayerPromoActive(player, promoId);
    }

    @ZenCodeType.Method
    public static boolean resetOfferLimits(String offerId) {
        return ShopScriptApi.resetOfferLimits(offerId);
    }

    @ZenCodeType.Method
    public static boolean resetWorldLimits(String offerId) {
        return ShopScriptApi.resetWorldLimits(offerId);
    }

    @ZenCodeType.Method
    public static boolean resetPlayerLimits(Player player, String offerId) {
        return ShopScriptApi.resetPlayerLimits(player, offerId);
    }

    @ZenCodeType.Method
    public static boolean canPurchase(Player player, String offerId, int amount) {
        return ShopScriptApi.canPurchase(player, offerId, amount);
    }

    @ZenCodeType.Method
    public static int getAvailableLimit(Player player, String offerId) {
        return ShopScriptApi.getAvailableLimit(player, offerId);
    }

    @ZenCodeType.Method
    public static void syncLimiterData(Player player) {
        ShopScriptApi.syncLimiterData(player);
    }
}
