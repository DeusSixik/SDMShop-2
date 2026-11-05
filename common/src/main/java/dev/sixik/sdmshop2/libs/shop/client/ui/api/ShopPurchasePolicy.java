package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopOfferView.CostEntry;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopOfferView.CostGroup;
import dev.sixik.sdmshop2.libs.shop.components.ItemCostComponent;
import dev.sixik.sdmshop2.libs.shop.components.ItemTagCostComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ConditionComponent;
import dev.sixik.sdmshop2.libs.shop.components.money.MoneyCostComponent;
import dev.sixik.sdmshop2.libs.shop.limiter.ShopLimiters;
import dev.sixik.sdmshop2.utils.ShopItemHelper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.Nullable;

import java.util.*;

/** Client-side balance and eligibility checks shared by purchase views. */
public final class ShopPurchasePolicy {
    /** Prevents instantiation. */
    private ShopPurchasePolicy() {}

    /**
     * Checks the player, limiter, client conditions and selected payment group.
     *
     * @param offer draft offer to inspect or change; null makes editing commands a no-op.
     * @param costGroups alternative payment groups in display order.
     * @param activeCostGroupIndex selected group index, clamped to the available groups.
     * @param player local player; null yields no balance and prevents purchase.
     * @return true when the requested operation or check succeeds.
     */
    public static boolean canPurchase(
            @Nullable ShopOffer offer,
            List<CostGroup> costGroups,
            int activeCostGroupIndex,
            @Nullable Player player) {
        if (player == null) return false;
        // 1. Проверка лимитера
        if (offer != null && !ShopLimiters.canPurchase(offer, player, 1)) {
            return false;
        }
        // 2. Проверка условий (conditions)
        if (offer != null) {
            for (ConditionComponent condition : offer.getComponents(ConditionComponent.class)) {
                if (condition.verifiedOnClient() && !condition.isChecked(player)) {
                    return false;
                }
            }
        }
        // 3. Проверка стоимости текущего способа оплаты
        if (costGroups.isEmpty()) {
            return true;
        }
        int currentGroupIdx = Math.min(Math.max(0, activeCostGroupIndex), costGroups.size() - 1);
        CostGroup currentGroup = costGroups.get(currentGroupIdx);
        for (CostEntry cost : currentGroup.costs()) {
            if (cost.component() != null) {
                if (!cost.component().canPay(player, cost.component().getBaseAmount()))
                    return false;
                continue;
            }
            double needed = parseAmount(cost.amount());
            double balance = checkCostBalance(cost, player);
            if (balance < needed) {
                return false;
            }
        }
        return true;
    }

    /**
     * Reads the player balance or inventory count represented by a cost.
     *
     * @param cost display cost with its optional backing component.
     * @param player local player; null yields no balance and prevents purchase.
     * @return resolved numeric value.
     */
    public static double checkCostBalance(CostEntry cost, @Nullable Player player) {
        if (player == null) return 0.0;
        if (cost.component() != null) {
            if (cost.component() instanceof MoneyCostComponent moneyCost) {
                ICurrency currency = SDMEconomyServiceClient.getCurrency(moneyCost.getMoneyId());
                if (currency != null) {
                    java.math.BigDecimal bal = SDMEconomyServiceClient.getBalance(currency, player);
                    return bal != null ? bal.doubleValue() : 0.0;
                }
            } else if (cost.component() instanceof ItemCostComponent itemCost) {
                return ShopItemHelper.countItem(
                        player.getInventory(), itemCost.getItem(), true, true);
            } else if (cost.component() instanceof ItemTagCostComponent tagCost) {
                return ShopItemHelper.countItem(player.getInventory(), tagCost.getTagKey());
            }
        }
        if (cost.itemIcon() != null && !cost.itemIcon().isEmpty()) {
            return ShopItemHelper.countItem(player.getInventory(), cost.itemIcon(), true, true);
        }
        ICurrency defaultCur =
                SDMEconomyServiceClient.getCurrency(new ResourceLocation("sdm", "coin"));
        if (defaultCur != null) {
            java.math.BigDecimal bal = SDMEconomyServiceClient.getBalance(defaultCur, player);
            return bal != null ? bal.doubleValue() : 0.0;
        }
        return 0.0;
    }

    /**
     * Parses a preview amount, accepting decimal commas and falling back to one.
     *
     * @param str formatted preview amount, or null to use the fallback.
     * @return resolved numeric value.
     */
    public static double parseAmount(String str) {
        if (str == null) return 1.0;
        try {
            return Double.parseDouble(str.replace(",", "."));
        } catch (Exception e) {
            return 1.0;
        }
    }
}
