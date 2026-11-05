package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.IconType;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopOfferView.CostEntry;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopOfferView.CostGroup;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.ShopEditorState;
import dev.sixik.sdmshop2.libs.shop.components.ItemCostComponent;
import dev.sixik.sdmshop2.libs.shop.components.ItemRewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.ItemTagCostComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.CostComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.RewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import dev.sixik.sdmshop2.libs.shop.components.money.MoneyCostComponent;
import dev.sixik.sdmshop2.libs.shop.components.promo.effects.DiscountComponent;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.*;

/** Resolves display data without constructing widgets. */
public final class ShopOfferPresentation {
    /** Prevents instantiation. */
    private ShopOfferPresentation() {}

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
            return "Вариант " + (index + 1);
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
        List<ItemStack> list = new ArrayList<>();
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
     * Resolves the cost amount and its item, texture or text icon.
     *
     * @param cost display cost with its optional backing component.
     * @return resulting draft object, or null when no session or source is available.
     */
    public static CostEntry resolveCostEntry(CostComponent cost) {
        String formatted = formatAmount(cost.getBaseAmount());
        if (cost instanceof ItemCostComponent itemCost) {
            return new CostEntry(itemCost.getItem(), null, formatted, cost);
        } else if (cost instanceof MoneyCostComponent moneyCost) {
            if (ShopEditorState.get().isEditorMode()) {
                CurrencyDraft draft =
                        ShopEditorState.get().findCurrencyDraft(moneyCost.getMoneyId());
                if (draft != null) {
                    if (draft.kind() == CurrencyDraft.Kind.ITEM
                            && draft.itemStack() != null
                            && !draft.itemStack().isEmpty()) {
                        return new CostEntry(draft.itemStack(), null, null, formatted, cost);
                    } else if (draft.kind() == CurrencyDraft.Kind.TEXTURE
                            && draft.iconTexture() != null) {
                        return new CostEntry(null, draft.iconTexture(), null, formatted, cost);
                    } else if (draft.kind() == CurrencyDraft.Kind.TEXT) {
                        String sym = draft.iconText();
                        return new CostEntry(
                                null,
                                null,
                                sym != null && !sym.isBlank() ? sym : "$",
                                formatted,
                                cost);
                    }
                }
            }
            ICurrency currency = SDMEconomyServiceClient.getCurrency(moneyCost.getMoneyId());
            if (currency != null) {
                CurrencyIcon curIcon = currency.getIcon();
                if (curIcon != null) {
                    if (curIcon.type() == IconType.ITEM) {
                        if (curIcon.icon() instanceof ItemStack s)
                            return new CostEntry(s, null, null, formatted, cost);
                        if (curIcon.icon() instanceof Item item)
                            return new CostEntry(
                                    item.getDefaultInstance(), null, null, formatted, cost);
                    } else if (curIcon.type() == IconType.TEXTURE) {
                        if (curIcon.icon() instanceof ResourceLocation loc) {
                            return new CostEntry(null, loc, null, formatted, cost);
                        } else if (curIcon.icon() instanceof String str) {
                            if (str.contains(":") || str.contains("/") || str.endsWith(".png")) {
                                ResourceLocation loc = ResourceLocation.tryParse(str);
                                if (loc != null)
                                    return new CostEntry(null, loc, null, formatted, cost);
                            }
                            return new CostEntry(null, null, str, formatted, cost);
                        }
                    } else if (curIcon.type() == IconType.TEXT) {
                        if (curIcon.icon() instanceof String text) {
                            return new CostEntry(null, null, text, formatted, cost);
                        }
                    }
                }
            }
            return new CostEntry(new ItemStack(Items.GOLD_NUGGET), null, null, formatted, cost);
        } else if (cost instanceof ItemTagCostComponent tagCost) {
            ItemStack stack =
                    BuiltInRegistries.ITEM
                            .getTag(tagCost.getTagKey())
                            .flatMap(
                                    tag ->
                                            tag.stream()
                                                    .findFirst()
                                                    .map(h -> h.value().getDefaultInstance()))
                            .orElse(Items.NAME_TAG.getDefaultInstance());
            return new CostEntry(stack, null, formatted, cost);
        }
        return new CostEntry(Items.EMERALD.getDefaultInstance(), null, formatted, cost);
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
