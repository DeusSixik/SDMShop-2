package dev.sixik.sdmshop2.libs.shop.components.promo.conditions;

import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.PromoComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.PromoScope;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfig;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentNumberRange;
import dev.sixik.sdmshop2.libs.shop.promo.PromoStateStore;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

import java.util.Locale;

public class PromoTriggerComponent extends PromoComponent {

    public static final IComponentType<PromoTriggerComponent> TYPE = new Type();

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.promo.conditions.promo_trigger.trigger_id")
    private String triggerId = "";

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.promo.conditions.promo_trigger.duration_ms")
    @ComponentNumberRange(longMin = 0)
    private long durationMs;

    public PromoTriggerComponent() {
        this(0L);
    }

    public PromoTriggerComponent(long durationMs) {
        this.durationMs = durationMs;
    }

    public void triggerGlobal() {
        PromoStateStore.activateGlobal(getStateId(), durationMs);
    }

    public void trigger(Player player) {
        PromoStateStore.activate(getStateId(), getScope(), player, durationMs);
    }

    public void trigger(Player player, long durationMs) {
        PromoStateStore.activate(getStateId(), getScope(), player, durationMs);
    }

    public void stopGlobal() {
        PromoStateStore.deactivateGlobal(getStateId());
    }

    public void stop(Player player) {
        PromoStateStore.deactivate(getStateId(), getScope(), player);
    }

    @Override
    public boolean isActive(MinecraftServer server) {
        return getScope() == PromoScope.GLOBAL
                && (PromoStateStore.isGlobalActive(getStateId()) || PromoStateStore.isGlobalActive(getPromoId()));
    }

    @Override
    public boolean isActive(Player player) {
        if (getScope() == PromoScope.PLAYER) {
            return PromoStateStore.isPlayerActive(player, getStateId()) || PromoStateStore.isPlayerActive(player, getPromoId());
        }

        return PromoStateStore.isGlobalActive(getStateId()) || PromoStateStore.isGlobalActive(getPromoId());
    }

    public boolean matchesTrigger(String triggerId) {
        return normalize(getEffectiveTriggerId()).equals(normalize(triggerId));
    }

    public String getEffectiveTriggerId() {
        return isBlank(triggerId) ? getPromoId() : triggerId;
    }

    public String getStateId() {
        String promoId = getPromoId();
        if (isBlank(promoId)) {
            return getEffectiveTriggerId();
        }

        if (getRoot() instanceof ShopOffer offer) {
            ShopInstance shop = offer.getParentShop();
            String shopId = shop == null ? "unknown_shop" : shop.getId().toString();
            return shopId + "/" + offer.getUUID() + "/" + promoId;
        }

        return promoId;
    }

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static class Type extends SerializedComponentType<PromoTriggerComponent> {

        private static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "promo_trigger");
        private static final ComponentSerializer<PromoTriggerComponent> SERIALIZER = ComponentSerializer.<PromoTriggerComponent>create()
                .addDefaultedString("trigger_id", PromoTriggerComponent::getTriggerId, PromoTriggerComponent::setTriggerId, "")
                .addDefaultedLong("duration_ms", PromoTriggerComponent::getDurationMs, PromoTriggerComponent::setDurationMs, 0L);

        private Type() {
            super(PromoTriggerComponent::new, SERIALIZER);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public PromoTriggerComponent createFromBuilder(Object... args) {
            if (args.length > 4) {
                throw new IllegalArgumentException("PromoTriggerComponent.createFromBuilder() takes up to 4 arguments (trigger_id, promo_id, duration_ms, scope)");
            }

            PromoTriggerComponent component = new PromoTriggerComponent();

            boolean newStyle = args.length >= 2 && args[1] instanceof String;
            if (newStyle) {
                component.setTriggerId(String.valueOf(args[0]));
                component.setPromoId(String.valueOf(args[1]));
                if (args.length >= 3) {
                    if (!(args[2] instanceof Number duration)) {
                        throw new IllegalArgumentException("PromoTriggerComponent duration_ms must be a number");
                    }
                    component.setDurationMs(duration.longValue());
                }
                if (args.length >= 4) {
                    component.setScope(PromoScope.valueOf(String.valueOf(args[3]).toUpperCase(Locale.ROOT)));
                }
            } else {
                if (args.length >= 1) {
                    component.setPromoId(String.valueOf(args[0]));
                    component.setTriggerId(String.valueOf(args[0]));
                }
                if (args.length >= 2) {
                    if (!(args[1] instanceof Number duration)) {
                        throw new IllegalArgumentException("PromoTriggerComponent duration_ms must be a number");
                    }
                    component.setDurationMs(duration.longValue());
                }
                if (args.length >= 3) {
                    component.setScope(PromoScope.valueOf(String.valueOf(args[2]).toUpperCase(Locale.ROOT)));
                }
            }

            return component;
        }
    }
}
