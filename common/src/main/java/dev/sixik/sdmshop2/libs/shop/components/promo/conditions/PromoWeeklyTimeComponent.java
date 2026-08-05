package dev.sixik.sdmshop2.libs.shop.components.promo.conditions;

import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.PromoComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfig;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PromoWeeklyTimeComponent extends PromoComponent {

    public static final IComponentType<PromoWeeklyTimeComponent> TYPE = new Type();

    private static final int TIME_NOT_SET = -1;
    private static final int TIME_INVALID = -2;

    @Getter
    @ComponentConfig(translationKey = "shop.component.promo.conditions.promo_weekly_time.days")
    private final List<DayOfWeek> days = new ArrayList<>();

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.promo.conditions.promo_weekly_time.start_time")
    private String startTime = "";

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.promo.conditions.promo_weekly_time.end_time")
    private String endTime = "";

    public PromoWeeklyTimeComponent() {
    }

    public PromoWeeklyTimeComponent(Collection<DayOfWeek> days, String startTime, String endTime) {
        setDays(days == null ? List.of() : new ArrayList<>(days));
        this.startTime = startTime == null ? "" : startTime;
        this.endTime = endTime == null ? "" : endTime;
    }

    public void setDays(List<DayOfWeek> days) {
        this.days.clear();
        if (days == null) return;

        for (DayOfWeek day : days) {
            if (day != null && !this.days.contains(day)) {
                this.days.add(day);
            }
        }
    }

    @Override
    public boolean isActive(MinecraftServer server) {
        if (days.isEmpty()) {
            return false;
        }

        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        DayOfWeek currentDay = now.getDayOfWeek();
        int currentMinute = now.getHour() * 60 + now.getMinute();
        int startMinute = parseTime(startTime);
        int endMinute = parseTime(endTime);

        if (startMinute == TIME_INVALID || endMinute == TIME_INVALID) {
            return false;
        }

        if (startMinute == TIME_NOT_SET && endMinute == TIME_NOT_SET) {
            return days.contains(currentDay);
        }

        if (startMinute == TIME_NOT_SET || endMinute == TIME_NOT_SET) {
            return false;
        }

        if (startMinute == endMinute) {
            return days.contains(currentDay);
        }

        if (startMinute < endMinute) {
            return days.contains(currentDay)
                    && currentMinute >= startMinute
                    && currentMinute <= endMinute;
        }

        DayOfWeek previousDay = currentDay.minus(1);
        return (days.contains(currentDay) && currentMinute >= startMinute)
                || (days.contains(previousDay) && currentMinute <= endMinute);
    }

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    private static int parseTime(String value) {
        if (value == null || value.isBlank()) {
            return TIME_NOT_SET;
        }

        String normalized = value.trim();
        if (normalized.contains(":")) {
            String[] parts = normalized.split(":", -1);
            if (parts.length != 2) return TIME_INVALID;

            Integer hours = parseInteger(parts[0]);
            Integer minutes = parseInteger(parts[1]);
            if (hours == null || minutes == null) return TIME_INVALID;
            if (hours == 24 && minutes == 0) return 1439;
            if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) return TIME_INVALID;
            return hours * 60 + minutes;
        }

        Integer number = parseInteger(normalized);
        if (number == null || number < 0) return TIME_INVALID;
        if (number <= 23) return number * 60;
        if (number <= 1439) return number;
        return TIME_INVALID;
    }

    private static Integer parseInteger(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private static List<DayOfWeek> parseDays(Object value) {
        List<DayOfWeek> result = new ArrayList<>();
        if (value instanceof DayOfWeek day) {
            result.add(day);
        } else if (value instanceof String string) {
            result.add(DayOfWeek.valueOf(string.toUpperCase()));
        } else if (value instanceof Collection<?> collection) {
            for (Object entry : collection) {
                result.addAll(parseDays(entry));
            }
        } else {
            throw new IllegalArgumentException("Unsupported day value: " + value);
        }

        return result;
    }

    private static class Type extends SerializedComponentType<PromoWeeklyTimeComponent> {

        private static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "promo_weekly_time");
        private static final ComponentSerializer<PromoWeeklyTimeComponent> SERIALIZER = ComponentSerializer.<PromoWeeklyTimeComponent>create()
                .addRequired("days", FieldCodecs.list(FieldCodecs.enumCodec(DayOfWeek.class)), PromoWeeklyTimeComponent::getDays, PromoWeeklyTimeComponent::setDays)
                .addDefaultedString("start_time", PromoWeeklyTimeComponent::getStartTime, PromoWeeklyTimeComponent::setStartTime, "")
                .addDefaultedString("end_time", PromoWeeklyTimeComponent::getEndTime, PromoWeeklyTimeComponent::setEndTime, "");

        private Type() {
            super(PromoWeeklyTimeComponent::new, SERIALIZER);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public PromoWeeklyTimeComponent createFromBuilder(Object... args) {
            if (args.length != 1 && args.length != 2 && args.length != 3 && args.length != 4) {
                throw new IllegalArgumentException("PromoWeeklyTimeComponent.createFromBuilder() takes 1, 2, 3 or 4 arguments (day/days, (Optional) promoId) or (day/days, startTime, endTime, (Optional) promoId)");
            }

            PromoWeeklyTimeComponent component = new PromoWeeklyTimeComponent();
            component.setDays(parseDays(args[0]));

            if (args.length == 2) {
                component.setPromoId((String) args[1]);
            } else if (args.length >= 3) {
                component.setStartTime(String.valueOf(args[1]));
                component.setEndTime(String.valueOf(args[2]));

                if (args.length == 4) {
                    component.setPromoId((String) args[3]);
                }
            }

            return component;
        }
    }
}
