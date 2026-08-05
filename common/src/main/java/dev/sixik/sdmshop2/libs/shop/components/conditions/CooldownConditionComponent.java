package dev.sixik.sdmshop2.libs.shop.components.conditions;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.base.limiter.ShopLimiterTable;
import dev.sixik.sdmshop2.libs.shop.components.api.ConditionComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
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

public class CooldownConditionComponent extends ConditionComponent {

    public static final IComponentType<CooldownConditionComponent> TYPE = new Type();

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.condition.cooldown.cooldown_ms")
    @ComponentNumberRange(longMin = 0)
    private long cooldownMs;

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.condition.cooldown.limiter_type")
    private LimiterComponent.LimiterType limiterType;

    public CooldownConditionComponent() {
        this(0L, LimiterComponent.LimiterType.Player);
    }

    public CooldownConditionComponent(long cooldownMs, LimiterComponent.LimiterType limiterType) {
        this.cooldownMs = cooldownMs;
        this.limiterType = limiterType;
    }

    @Override
    public boolean isChecked(Player player) {
        return getAvailableAtMs(player) <= System.currentTimeMillis();
    }

    public long getAvailableAtMs(Player player) {
        if (player == null) {
            return Long.MAX_VALUE;
        }

        if (cooldownMs <= 0) {
            return 0L;
        }

        Optional<ShopLimiterTable> tableOpt = ShopUtils.getLimiterTable(player.isLocalPlayer());

        if (tableOpt.isEmpty()) {
            return Long.MAX_VALUE;
        }

        UUID offerId = ((ShopOffer) getRoots()).getUUID();
        ShopLimiterTable table = tableOpt.get();
        long lastTime;

        if (getResolvedLimiterType() == LimiterComponent.LimiterType.Player) {
            lastTime = table.getPlayerData(player).getData(offerId).getLastPurchaseTime().get();
        } else {
            lastTime = table.getOfferData(offerId).getLastPurchaseTime().get();
        }

        if (lastTime <= 0) {
            return 0L;
        }

        if (lastTime > Long.MAX_VALUE - cooldownMs) {
            return Long.MAX_VALUE;
        }

        return lastTime + cooldownMs;
    }

    public long getRemainingCooldownMs(Player player) {
        long availableAtMs = getAvailableAtMs(player);
        if (availableAtMs == Long.MAX_VALUE) {
            return Long.MAX_VALUE;
        }

        return Math.max(0L, availableAtMs - System.currentTimeMillis());
    }

    @Override
    public void recordPurchase(Player player, int amount) {
        if (player == null || amount <= 0) {
            return;
        }

        Optional<ShopLimiterTable> tableOpt = ShopUtils.getLimiterTable(false);
        if (tableOpt.isEmpty()) {
            return;
        }

        UUID offerId = ((ShopOffer) getRoots()).getUUID();
        ShopLimiterTable table = tableOpt.get();
        if (getResolvedLimiterType() == LimiterComponent.LimiterType.Player) {
            table.getPlayerData(player).getData(offerId).markPurchased();
        } else {
            table.getOfferData(offerId).markPurchased();
        }
    }

    private LimiterComponent.LimiterType getResolvedLimiterType() {
        return limiterType == null ? LimiterComponent.LimiterType.Player : limiterType;
    }

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    private static class Type extends SerializedComponentType<CooldownConditionComponent> {

        private static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "condition_cooldown");
        private static final ComponentSerializer<CooldownConditionComponent> SERIALIZER = ComponentSerializer.<CooldownConditionComponent>create()
                .addRequired("cooldown_ms", FieldCodecs.LONG, CooldownConditionComponent::getCooldownMs, (component, value) -> component.cooldownMs = value)
                .addDefaulted("limiter_type", FieldCodecs.enumCodec(LimiterComponent.LimiterType.class), CooldownConditionComponent::getLimiterType, (component, value) -> component.limiterType = value, LimiterComponent.LimiterType.Player);

        private Type() {
            super(CooldownConditionComponent::new, SERIALIZER);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public CooldownConditionComponent createFromBuilder(Object... args) {
            if (args.length != 2) {
                throw new IllegalArgumentException("CooldownConditionComponent.createFromBuilder() takes 2 arguments (long, String)");
            }

            return new CooldownConditionComponent((long) args[0], LimiterComponent.LimiterType.valueOf((String) args[1]));
        }
    }
}
