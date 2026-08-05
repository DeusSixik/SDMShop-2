package dev.sixik.sdmshop2.libs.shop.scripting.events;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.sixik.sdmshop2.libs.platform.ServerOperation;
import dev.sixik.sdmshop2.libs.shop.SDMShopPlatform;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.base.ShopTable;
import dev.sixik.sdmshop2.libs.shop.components.api.ConditionComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.CostComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.RewardComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ShopScriptEvents {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShopScriptEvents.class);

    public static final Event<ScripConditionEvent> SCRIP_CONDITION_EVENT =
            EventFactory.of(listeners -> (player, component, scriptId) -> {
                if (listeners.isEmpty()) return false;

                boolean finalResult = true;
                for (ScripConditionEvent listener : listeners) {
                    boolean checked;
                    try {
                        checked = listener.invoke(player, component, scriptId);
                    } catch (Throwable throwable) {
                        LOGGER.error("Script condition '{}' failed", scriptId, throwable);
                        return false;
                    }

                    if (!checked) {
                        return false;
                    }
                }
                return finalResult;
            });

    public static final Event<ScripConditionEvent> SCRIPT_CONDITION_EVENT = SCRIP_CONDITION_EVENT;

    public static final Event<ScripRewardEvent> SCRIP_REWARD_EVENT =
            EventFactory.createLoop(new ScripRewardEvent[0]);

    public static final Event<ScripRewardEvent> SCRIPT_REWARD_EVENT = SCRIP_REWARD_EVENT;

    public static final Event<ScriptCalculatePriceEvent> SCRIPT_CALCULATE_PRICE_EVENT =
            EventFactory.of(listeners -> (offer, server, chosenGroupId, prices) -> {
                for (ScriptCalculatePriceEvent listener : listeners) {
                    try {
                        listener.invoke(offer, server, chosenGroupId, prices);
                    } catch (Throwable throwable) {
                        LOGGER.error("Script price hook failed for offer {}", offer == null ? null : offer.getUUID(), throwable);
                    }
                }
            });

    public static final Event<ScriptShopLoadEvent> SCRIPT_SHOP_LOAD_EVENT =
            EventFactory.of(listeners -> (server, table) -> {
                for (ScriptShopLoadEvent listener : listeners) {
                    try {
                        listener.invoke(server, table);
                    } catch (Throwable throwable) {
                        LOGGER.error("Script shop load hook failed", throwable);
                    }
                }
            });

    public static final Event<ScriptBeforePurchaseEvent> SCRIPT_BEFORE_PURCHASE_EVENT =
            EventFactory.of(listeners -> (offer, player, chosenGroupId, amount) -> {
                for (ScriptBeforePurchaseEvent listener : listeners) {
                    boolean allowed;
                    try {
                        allowed = listener.invoke(offer, player, chosenGroupId, amount);
                    } catch (Throwable throwable) {
                        LOGGER.error("Script before-purchase hook failed for offer {}", offer == null ? null : offer.getUUID(), throwable);
                        return false;
                    }

                    if (!allowed) {
                        return false;
                    }
                }
                return true;
            });

    public static final Event<ScriptAfterPurchaseEvent> SCRIPT_AFTER_PURCHASE_EVENT =
            EventFactory.of(listeners -> (offer, player, chosenGroupId, amount) -> {
                for (ScriptAfterPurchaseEvent listener : listeners) {
                    try {
                        listener.invoke(offer, player, chosenGroupId, amount);
                    } catch (Throwable throwable) {
                        LOGGER.error("Script after-purchase hook failed for offer {}", offer == null ? null : offer.getUUID(), throwable);
                    }
                }
            });

    public static final Event<ScriptPurchaseFailedEvent> SCRIPT_PURCHASE_FAILED_EVENT =
            EventFactory.of(listeners -> (offer, player, chosenGroupId, amount, reason) -> {
                for (ScriptPurchaseFailedEvent listener : listeners) {
                    try {
                        listener.invoke(offer, player, chosenGroupId, amount, reason);
                    } catch (Throwable throwable) {
                        LOGGER.error("Script purchase-failed hook failed for offer {}", offer == null ? null : offer.getUUID(), throwable);
                    }
                }
            });

    @FunctionalInterface
    public interface ScripConditionEvent {

        boolean invoke(Player player, ConditionComponent component, String scriptId);
    }

    @FunctionalInterface
    public interface ScripRewardEvent {

        void invoke(Player player, int amount, RewardComponent component, String scriptId);
    }

    @FunctionalInterface
    public interface ScriptCalculatePriceEvent {

        void invoke(ShopOffer offer, MinecraftServer server, String chosenGroupId, Map<CostComponent, Double> prices);
    }

    @FunctionalInterface
    public interface ScriptShopLoadEvent {

        void invoke(MinecraftServer server, ShopTable table);
    }

    @FunctionalInterface
    public interface ScriptBeforePurchaseEvent {

        boolean invoke(ShopOffer offer, ServerPlayer player, String chosenGroupId, int amount);
    }

    @FunctionalInterface
    public interface ScriptAfterPurchaseEvent {

        void invoke(ShopOffer offer, ServerPlayer player, String chosenGroupId, int amount);
    }

    @FunctionalInterface
    public interface ScriptPurchaseFailedEvent {

        void invoke(ShopOffer offer, ServerPlayer player, String chosenGroupId, int amount, PurchaseFailureReason reason);
    }

    public enum PurchaseFailureReason {
        INVALID_INPUT,
        SCRIPT_CANCELLED,
        CONDITION_FAILED,
        LIMIT_FAILED,
        NO_COST_GROUP,
        INVALID_COST,
        CANNOT_PAY,
        PAYMENT_FAILED,
        REWARD_FAILED
    }

    public static class Manager implements ServerOperation {

        @Override
        public void onReload() {
            ShopScriptEvents.SCRIP_CONDITION_EVENT.clearListeners();
            ShopScriptEvents.SCRIP_REWARD_EVENT.clearListeners();
            ShopScriptEvents.SCRIPT_CALCULATE_PRICE_EVENT.clearListeners();
            ShopScriptEvents.SCRIPT_SHOP_LOAD_EVENT.clearListeners();
            ShopScriptEvents.SCRIPT_BEFORE_PURCHASE_EVENT.clearListeners();
            ShopScriptEvents.SCRIPT_AFTER_PURCHASE_EVENT.clearListeners();
            ShopScriptEvents.SCRIPT_PURCHASE_FAILED_EVENT.clearListeners();

            SDMShopPlatform.invokeKubeJSEvent.run();
        }
    }
}
