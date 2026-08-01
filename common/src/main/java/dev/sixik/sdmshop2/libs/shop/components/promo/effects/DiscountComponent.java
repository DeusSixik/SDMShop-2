package dev.sixik.sdmshop2.libs.shop.components.promo.effects;

import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.PromoEffectComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfig;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentNumberRange;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Set;

public class DiscountComponent extends PromoEffectComponent {

    public static final IComponentType<DiscountComponent> TYPE = new Type();

    @Getter
    @ComponentConfig(translationKey = "shop.component.promo.effects.discount.discount")
    @ComponentNumberRange(doubleMin = 0)
    private double discount;

    public DiscountComponent() {
    }

    public DiscountComponent(double discount) {
        this.discount = discount;
    }

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    @Override
    public double applyPrice(double input, Set<String> activePromo, Set<String> activeGroups) {
        return input * (1 - discount);
    }

    private static class Type extends SerializedComponentType<DiscountComponent> {

        private static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "discount");
        private static final ComponentSerializer<DiscountComponent> SERIALIZER = ComponentSerializer.<DiscountComponent>create()
                .addRequired("discount", FieldCodecs.DOUBLE, DiscountComponent::getDiscount, (component, value) -> component.discount = value);

        private Type() {
            super(DiscountComponent::new, SERIALIZER);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public DiscountComponent createFromBuilder(Object... args) {
            if (args.length < 1 || !(args[0] instanceof Number value)) {
                throw new IllegalArgumentException("[DiscountComponent.createFromBuilder()] requires at least 1 number argument (discount amount).");
            }

            DiscountComponent component = new DiscountComponent(value.doubleValue());

            if (args.length >= 2 && args[1] instanceof String promoId) {
                component.setTargetPromoId(promoId);
            }

            for (int i = 2; i < args.length; i++) {
                Object arg = args[i];
                if (arg instanceof String groupId) {
                    component.applyGroup(groupId);
                } else if (arg instanceof Collection<?> groups) {
                    groups.forEach(group -> component.applyGroup(String.valueOf(group)));
                } else {
                    throw new IllegalArgumentException("[DiscountComponent.createFromBuilder()] Invalid group format at index " + i);
                }
            }

            return component;
        }
    }
}
