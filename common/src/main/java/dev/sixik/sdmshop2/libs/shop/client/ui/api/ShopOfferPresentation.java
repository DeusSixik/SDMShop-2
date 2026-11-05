package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopOfferView.CostEntry;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopOfferView.CostGroup;
import dev.sixik.sdmshop2.libs.shop.components.ItemRewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.CostComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.RewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import dev.sixik.sdmshop2.libs.shop.components.promo.effects.DiscountComponent;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.*;

/** Resolves display data without constructing widgets. */
public final class ShopOfferPresentation {
    /** Prevents instantiation. */
    private ShopOfferPresentation() {}

    /**
     * Captures factories for all non-payment components, including add-on types. Payment components
     * are presented inside their respective payment groups.
     *
     * @param offer offer whose components determine the display order.
     * @return immutable factory list; factories create widgets only when a view is mounted.
     */
    public static List<ShopRenderElement> resolveRenderElements(ShopOffer offer) {
        List<ShopRenderElement> elements = new ArrayList<>();
        for (ShopComponent component : offer.getComponents()) {
            if (!(component instanceof CostComponent)) elements.add(component::createRender);
        }
        return List.copyOf(elements);
    }

    /**
     * Formats a numbered summary of the payment group.
     *
     * @param group payment group to describe.
     * @param index zero-based group position.
     * @return formatted display text; never null.
     */
    public static String formatCostGroupTitle(CostGroup group, int index) {
        List<String> parts = new ArrayList<>();
        for (CostEntry cost : group.costs()) {
            String name;
            if (cost.itemIcon() != null && !cost.itemIcon().isEmpty()) {
                name = cost.itemIcon().getHoverName().getString();
            } else if (cost.textureIcon() != null) {
                name = cost.textureIcon().getPath();
            } else if (cost.textIcon() != null && !cost.textIcon().isBlank()) {
                name = cost.textIcon();
            } else {
                name = "Coins";
            }
            parts.add(cost.amount() + "x " + name);
        }
        if (parts.isEmpty()) {
            return I18n.get("shop.ui.purchase.variant", String.valueOf(index + 1));
        }
        return (index + 1) + ". " + String.join(" + ", parts);
    }

    /**
     * Resolves the explicit offer name, reward title or identifier in fallback order.
     *
     * @param offer draft offer to inspect or change; null makes editing commands a no-op.
     * @return formatted display text; never null.
     */
    public static String resolveTitle(ShopOffer offer) {
        return offer.getComponent(NameComponent.class)
                .map(NameComponent::getName)
                .filter(name -> !name.isBlank())
                .map(name -> I18n.exists(name) ? Component.translatable(name).getString() : name)
                .orElseGet(
                        () ->
                                offer.getComponents(RewardComponent.class).stream()
                                        .map(RewardComponent::getDisplayTitle)
                                        .filter(Objects::nonNull)
                                        .map(Component::getString)
                                        .filter(title -> !title.isBlank())
                                        .findFirst()
                                        .orElse(offer.getUUID().toString()));
    }

    /**
     * Resolves item reward previews, using a barrier when no item reward exists.
     *
     * @param offer draft offer to inspect or change; null makes editing commands a no-op.
     * @return ordered result list; never null.
     */
    public static List<ItemStack> resolveRewardItems(ShopOffer offer) {
        List<ItemStack> list = new ObjectArrayList<>();
        for (RewardComponent reward : offer.getComponents(RewardComponent.class)) {
            if (reward instanceof ItemRewardComponent itemReward) {
                list.add(itemReward.getRewardItem().copyWithCount(itemReward.getAmount()));
            }
        }
        if (list.isEmpty()) {
            list.add(Items.BARRIER.getDefaultInstance());
        }
        return list;
    }

    /**
     * Groups costs by their server payment-group identifier in encounter order.
     *
     * @param offer draft offer to inspect or change; null makes editing commands a no-op.
     * @return ordered result list; never null.
     */
    public static List<CostGroup> resolveCostGroups(ShopOffer offer) {
        Map<String, List<CostEntry>> groups = new LinkedHashMap<>();
        for (CostComponent cost : offer.getComponents(CostComponent.class)) {
            String groupId = cost.getGroupId() == null ? "" : cost.getGroupId();
            CostEntry entry = resolveCostEntry(cost);
            groups.computeIfAbsent(groupId, k -> new ArrayList<>()).add(entry);
        }
        List<CostGroup> result = new ArrayList<>();
        for (Map.Entry<String, List<CostEntry>> entry : groups.entrySet()) {
            result.add(new CostGroup(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    /**
     * Captures payment metadata without interpreting the component's visual representation.
     *
     * @param cost component owning the payment presentation and numeric amount.
     * @return payment metadata retaining the component's render factory; never null.
     */
    public static CostEntry resolveCostEntry(CostComponent cost) {
        return new CostEntry(null, null, null, formatAmount(cost.getBaseAmount()), cost);
    }

    /**
     * Resolves the displayed discount percentage, or zero when absent.
     *
     * @param offer draft offer to inspect or change; null makes editing commands a no-op.
     * @return resolved numeric value.
     */
    public static double resolveDiscount(ShopOffer offer) {
        return offer.getComponent(DiscountComponent.class)
                .map(DiscountComponent::getDiscount)
                .orElse(0.0);
    }

    /**
     * Formats an amount with no decimal places for integers and two otherwise.
     *
     * @param amount numeric amount to display.
     * @return formatted display text; never null.
     */
    public static String formatAmount(double amount) {
        if (amount == (long) amount) {
            return String.valueOf((long) amount);
        }
        return String.format(Locale.ROOT, "%.2f", amount).replaceAll("\\.00$", "");
    }
}
