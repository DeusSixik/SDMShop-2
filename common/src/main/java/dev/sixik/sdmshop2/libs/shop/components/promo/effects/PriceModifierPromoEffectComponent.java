package dev.sixik.sdmshop2.libs.shop.components.promo.effects;

import dev.sixik.sdmshop2.libs.shop.components.api.CostComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.PromoEffectComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.PromoPriceContext;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfig;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfigOptions;
import dev.sixik.sdmshop2.libs.shop.components.money.MoneyCostComponent;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.List;

public class PriceModifierPromoEffectComponent extends PromoEffectComponent {

    public static final IComponentType<PriceModifierPromoEffectComponent> TYPE = new Type();

    public enum Operation {
        ADD_FLAT,
        ADD_PERCENT,
        MULTIPLY,
        SET,
        MIN,
        MAX
    }

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.promo.effects.price_modifier.operation")
    private Operation operation = Operation.ADD_PERCENT;

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.promo.effects.price_modifier.value")
    private double value;

    @Getter
    @ComponentConfig(translationKey = "shop.component.promo.effects.price_modifier.target_money_ids")
    @ComponentConfigOptions(provider = "sdm:money_ids")
    private final List<ResourceLocation> targetMoneyIds = new ObjectArrayList<>();

    public PriceModifierPromoEffectComponent() {
    }

    public PriceModifierPromoEffectComponent(Operation operation, double value) {
        this.operation = operation == null ? Operation.ADD_PERCENT : operation;
        this.value = value;
    }

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    @Override
    public double applyPrice(PromoPriceContext context) {
        if (context == null || !canApplyToCost(context.cost())) {
            return context == null ? 0.0D : context.currentPrice();
        }

        double currentPrice = context.currentPrice();
        return switch (operation == null ? Operation.ADD_PERCENT : operation) {
            case ADD_FLAT -> currentPrice + value;
            case ADD_PERCENT -> currentPrice * (1.0D + value / 100.0D);
            case MULTIPLY -> currentPrice * value;
            case SET -> value;
            case MIN -> Math.min(currentPrice, value);
            case MAX -> Math.max(currentPrice, value);
        };
    }

    public void addTargetMoneyId(ResourceLocation moneyId) {
        if (moneyId != null && !targetMoneyIds.contains(moneyId)) {
            targetMoneyIds.add(moneyId);
        }
    }

    public boolean removeTargetMoneyId(ResourceLocation moneyId) {
        return targetMoneyIds.remove(moneyId);
    }

    private boolean canApplyToCost(CostComponent cost) {
        if (targetMoneyIds.isEmpty()) {
            return true;
        }

        return cost instanceof MoneyCostComponent moneyCost && targetMoneyIds.contains(moneyCost.getMoneyId());
    }

    private void setTargetMoneyIds(List<ResourceLocation> moneyIds) {
        targetMoneyIds.clear();
        if (moneyIds == null) {
            return;
        }

        for (ResourceLocation moneyId : moneyIds) {
            addTargetMoneyId(moneyId);
        }
    }

    private static class Type extends SerializedComponentType<PriceModifierPromoEffectComponent> {

        private static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "price_modifier");
        private static final ComponentSerializer<PriceModifierPromoEffectComponent> SERIALIZER = ComponentSerializer.<PriceModifierPromoEffectComponent>create()
                .addDefaulted("operation", FieldCodecs.enumCodec(Operation.class), PriceModifierPromoEffectComponent::getOperation, PriceModifierPromoEffectComponent::setOperation, Operation.ADD_PERCENT)
                .addDefaultedDouble("value", PriceModifierPromoEffectComponent::getValue, (component, value) -> component.value = value, 0.0D)
                .addDefaulted("target_money_ids", FieldCodecs.list(FieldCodecs.RESOURCE_LOCATION), PriceModifierPromoEffectComponent::getTargetMoneyIds, PriceModifierPromoEffectComponent::setTargetMoneyIds, List.of());

        private Type() {
            super(PriceModifierPromoEffectComponent::new, SERIALIZER);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public PriceModifierPromoEffectComponent createFromBuilder(Object... args) {
            if (args.length < 2) {
                throw new IllegalArgumentException("[PriceModifierPromoEffectComponent.createFromBuilder()] requires operation and value arguments.");
            }

            Operation operation = args[0] instanceof Operation enumValue
                    ? enumValue
                    : Operation.valueOf(String.valueOf(args[0]).toUpperCase());

            if (!(args[1] instanceof Number value)) {
                throw new IllegalArgumentException("[PriceModifierPromoEffectComponent.createFromBuilder()] value argument must be a number.");
            }

            PriceModifierPromoEffectComponent component = new PriceModifierPromoEffectComponent(operation, value.doubleValue());

            if (args.length >= 3 && args[2] instanceof String promoId) {
                component.setTargetPromoId(promoId);
            }

            if (args.length >= 4 && args[3] instanceof Number priority) {
                component.setPriority(priority.intValue());
            }

            for (int i = 4; i < args.length; i++) {
                Object arg = args[i];
                if (arg instanceof ResourceLocation moneyId) {
                    component.addTargetMoneyId(moneyId);
                } else if (arg instanceof String moneyId) {
                    component.addTargetMoneyId(ResourceLocation.tryParse(moneyId));
                } else if (arg instanceof Collection<?> moneyIds) {
                    moneyIds.forEach(entry -> {
                        if (entry instanceof ResourceLocation moneyId) {
                            component.addTargetMoneyId(moneyId);
                        } else {
                            component.addTargetMoneyId(ResourceLocation.tryParse(String.valueOf(entry)));
                        }
                    });
                } else {
                    throw new IllegalArgumentException("[PriceModifierPromoEffectComponent.createFromBuilder()] Invalid target money id format at index " + i);
                }
            }

            return component;
        }
    }
}
