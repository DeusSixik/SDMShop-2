package dev.sixik.sdmshop2.libs.shop.components.api;

import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfig;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class PromoEffectComponent extends ShopComponent {

    private static final ComponentSerializer<PromoEffectComponent> ADDITIONAL_SERIALIZER = ComponentSerializer.<PromoEffectComponent>create()
            .addDefaultedString("target_promo_id", PromoEffectComponent::getTargetPromoId, PromoEffectComponent::setTargetPromoId, "")
            .addDefaultedInt("priority", PromoEffectComponent::getPriority, PromoEffectComponent::setPriority, 0)
            .add("apply_groups", FieldCodecs.list(FieldCodecs.STRING), PromoEffectComponent::getApplyGroupsList, PromoEffectComponent::setApplyGroupsList);

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.promo.effects.target_promo_id")
    private String targetPromoId = "";

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.promo.effects.priority")
    private int priority;

    @Getter
    @ComponentConfig(translationKey = "shop.component.promo.apply_groups")
    private final Set<String> applyGroups = new ObjectOpenHashSet<>();

    /**
     * Проверяет, применим ли данный эффект при текущих активных акциях и выбранной группе оплаты.
     */
    public boolean canApply(Set<String> activePromos, String chosenGroupId) {
        boolean hasActivePromo = activePromos != null && !activePromos.isEmpty();
        boolean matchPromo = targetPromoId == null || targetPromoId.isEmpty()
                ? hasActivePromo
                : hasActivePromo && activePromos.contains(targetPromoId);
        boolean matchGroup = applyGroups.isEmpty() || applyGroups.contains(chosenGroupId);
        return matchPromo && matchGroup;
    }

    public double applyPrice(PromoPriceContext context) {
        return applyPrice(context.currentPrice(), context.activePromos(), applyGroups);
    }

    @Deprecated
    public double applyPrice(double input, Set<String> activePromo, Set<String> activeGroups) {
        return input;
    }

    public int applyAmount(int input, Set<String> activePromo) {
        return input;
    }

    public final void applyGroup(String groupId) {
        applyGroups.add(groupId);
    }

    public final boolean removeGroup(String groupId) {
        return applyGroups.remove(groupId);
    }

    @Override
    public ComponentSerializer<PromoEffectComponent> additionalSerializer() {
        return ADDITIONAL_SERIALIZER;
    }

    @Override
    public ShopComponentCategory getCategory() {
        return ShopComponentCategory.PROMO_EFFECT;
    }

    private List<String> getApplyGroupsList() {
        return new ArrayList<>(applyGroups);
    }

    private void setApplyGroupsList(List<String> groups) {
        applyGroups.clear();
        if (groups != null) {
            applyGroups.addAll(groups);
        }
    }
}
