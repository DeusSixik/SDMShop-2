package dev.sixik.sdmshop2.utils;

import dev.sixik.sdmshop2.libs.shop.base.limiter.ShopLimiterTable;
import dev.sixik.sdmshop2.libs.shop.base.limiter.ShopLimiterTableServer;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponentRegistry;
import io.netty.buffer.Unpooled;
import net.minecraft.commands.Commands;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class ShopUtils {

    private static final String CLIENT_LIMITER_TABLE_CLASS = "dev.sixik.sdmshop2.libs.shop.base.limiter.ShopLimiterTableClient";

    public static Optional<ShopLimiterTable> getLimiterTable(boolean isClient) {
        if (isClient) {
            return getClientLimiterTable();
        }

        return Optional.ofNullable(ShopLimiterTableServer.getInstance());
    }

    private static Optional<ShopLimiterTable> getClientLimiterTable() {
        try {
            Class<?> type = Class.forName(CLIENT_LIMITER_TABLE_CLASS);
            Object instance = type.getField("INSTANCE").get(null);
            return instance instanceof ShopLimiterTable table ? Optional.of(table) : Optional.empty();
        } catch (ReflectiveOperationException | LinkageError ignored) {
            return Optional.empty();
        }
    }

    public static Map<ResourceLocation, ShopComponent> createDefaultComponentsMap(Class<? extends ShopComponent> include) {
        Map<ResourceLocation, ShopComponent> componentMap = new HashMap<>();
        for (Map.Entry<ResourceLocation, IComponentType<?>> entry : ShopComponentRegistry.getTypes().entrySet()) {
            ShopComponent component = entry.getValue().createDefault();
            if(!include.isInstance(component)) continue;

            componentMap.put(entry.getKey(), component);
        }
        return componentMap;
    }

    public static Map<ResourceLocation, ShopComponent> createDefaultComponentsMap() {
        Map<ResourceLocation, ShopComponent> componentMap = new HashMap<>();
        for (Map.Entry<ResourceLocation, IComponentType<?>> entry : ShopComponentRegistry.getTypes().entrySet()) {
            componentMap.put(entry.getKey(), entry.getValue().createDefault());
        }
        return componentMap;
    }

    public static Optional<ShopComponent> createDefaultComponent(ResourceLocation typeId) {
        return ShopComponentRegistry.getType(typeId).map(IComponentType::createDefault);
    }

    public static <T extends ShopComponent> T createDefaultComponent(IComponentType<T> type) {
        return type.createDefault();
    }

    public static FriendlyByteBuf createResponse(Consumer<FriendlyByteBuf> writer) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        writer.accept(buf);
        return buf;
    }

    public static Component getTranslation(String txt) {
        return Component.translatable(txt);
    }

    public static Component getTranslation(String txt, String or) {
        return Component.translatable(txt);
    }

    public static boolean isPlayerAdmin(Player player) {
        return player.hasPermissions(Commands.LEVEL_GAMEMASTERS);
    }
}
