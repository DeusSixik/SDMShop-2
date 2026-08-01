package dev.sixik.sdmshop2.libs.shop.components.promo.conditions;

import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.PromoComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfig;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentNumberRange;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class PromoTimeComponent extends PromoComponent {

    public static final IComponentType<PromoTimeComponent> TYPE = new Type();

    public enum TimeMode {
        REAL_TIME_EPOCH,
        SERVER_TICKS,
        DAY_TIME
    }

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.promo.conditions.promo_timer.mode")
    private TimeMode mode;

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.promo.conditions.promo_timer.start_time")
    @ComponentNumberRange(longMin = 0)
    private long startTime;

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.promo.conditions.promo_timer.end_time")
    @ComponentNumberRange(longMin = 0)
    private long endTime;

    public PromoTimeComponent() {
        this(TimeMode.REAL_TIME_EPOCH, 0, 0);
    }

    public PromoTimeComponent(TimeMode mode, long startTime, long endTime) {
        this.mode = mode;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public boolean isActive(MinecraftServer server) {
        long currentTime = 0;

        switch (mode) {
            case REAL_TIME_EPOCH -> currentTime = System.currentTimeMillis();
            case SERVER_TICKS -> {
                ServerLevel level = server.getLevel(Level.OVERWORLD);
                if (level != null) currentTime = level.getGameTime();
            }
            case DAY_TIME -> {
                ServerLevel overworld = server.getLevel(Level.OVERWORLD);
                if (overworld != null) currentTime = overworld.getDayTime() % 24000;
            }
        }

        if (startTime == endTime) return true;
        if (mode == TimeMode.DAY_TIME && startTime > endTime) {
            return currentTime >= startTime || currentTime <= endTime;
        }

        return currentTime >= startTime && currentTime <= endTime;
    }

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    private static class Type extends SerializedComponentType<PromoTimeComponent> {

        private static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "promo_time");
        private static final ComponentSerializer<PromoTimeComponent> SERIALIZER = ComponentSerializer.<PromoTimeComponent>create()
                .addRequired("mode", FieldCodecs.enumCodec(TimeMode.class), PromoTimeComponent::getMode, (component, value) -> component.mode = value)
                .addRequired("start_time", FieldCodecs.LONG, PromoTimeComponent::getStartTime, (component, value) -> component.startTime = value)
                .addRequired("end_time", FieldCodecs.LONG, PromoTimeComponent::getEndTime, (component, value) -> component.endTime = value);

        private Type() {
            super(PromoTimeComponent::new, SERIALIZER);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public PromoTimeComponent createFromBuilder(Object... args) {
            if (args.length != 3 && args.length != 4) {
                throw new IllegalArgumentException("PromoTimeComponent.createFromBuilder() takes 3 or 4 arguments (String, long, long, (Optional) String)");
            }

            final var component = new PromoTimeComponent(TimeMode.valueOf((String) args[0]), (long) args[1], (long) args[2]);
            if (args.length == 4) {
                component.setPromoId((String) args[3]);
            }

            return component;
        }
    }
}
