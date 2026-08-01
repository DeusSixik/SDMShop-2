package dev.sixik.sdmshop2.libs.shop.components.promo.conditions;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.base.limiter.ShopLimiterTable;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.PromoComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfig;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentNumberRange;
import dev.sixik.sdmshop2.libs.shop.components.limiter.LimiterComponent;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import dev.sixik.sdmshop2.utils.ShopUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;
import java.util.UUID;

public class PromoCooldownComponent extends PromoComponent {

    public static final IComponentType<PromoCooldownComponent> TYPE = new Type();

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.promo.conditions.promo_cooldown.cooldown_ms")
    @ComponentNumberRange(longMin = 0)
    private long cooldownMs;

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.promo.conditions.promo_cooldown.limiter_type")
    private LimiterComponent.LimiterType limiterType;

    public PromoCooldownComponent() {
        this(0L, LimiterComponent.LimiterType.Player);
    }

    public PromoCooldownComponent(long cooldownMs, LimiterComponent.LimiterType limiterType) {
        this.cooldownMs = cooldownMs;
        this.limiterType = limiterType;
    }

    @Override
    public boolean isActive(Player player) {
        Optional<ShopLimiterTable> tableOpt = ShopUtils.getLimiterTable(player.isLocalPlayer());

        if (tableOpt.isEmpty()) {
            return false;
        }

        UUID offerId = ((ShopOffer) getRoots()).getUUID();
        long lastTime;

        if (limiterType == LimiterComponent.LimiterType.Player) {
            lastTime = tableOpt.get().getPlayerData(player).getData(offerId).getLastPurchaseTime().get();
        } else {
            lastTime = tableOpt.get().getOfferDatga(offerId).getLastPurchaseTime().get();
        }

        return lastTime == 0 || (System.currentTimeMillis() - lastTime) >= cooldownMs;
    }

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    private static class Type extends SerializedComponentType<PromoCooldownComponent> {

        private static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "promo_cooldown");
        private static final ComponentSerializer<PromoCooldownComponent> SERIALIZER = ComponentSerializer.<PromoCooldownComponent>create()
                .addRequired("cooldown_ms", FieldCodecs.LONG, PromoCooldownComponent::getCooldownMs, (component, value) -> component.cooldownMs = value)
                .addRequired("side", FieldCodecs.enumCodec(LimiterComponent.LimiterType.class), PromoCooldownComponent::getLimiterType, (component, value) -> component.limiterType = value);

        private Type() {
            super(PromoCooldownComponent::new, SERIALIZER);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public PromoCooldownComponent createFromBuilder(Object... args) {
            if (args.length != 2 && args.length != 3) {
                throw new IllegalArgumentException("PromoCooldownComponent.createFromBuilder() takes 2 or 3 arguments (long, String, (Optional) String)");
            }

            final var promo = new PromoCooldownComponent((long) args[0], LimiterComponent.LimiterType.valueOf((String) args[1]));
            if (args.length == 3) {
                promo.setPromoId((String) args[2]);
            }

            return promo;
        }
    }
}
