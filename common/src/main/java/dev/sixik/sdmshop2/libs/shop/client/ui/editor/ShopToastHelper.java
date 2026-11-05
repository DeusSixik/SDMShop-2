package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class ShopToastHelper {

    private ShopToastHelper() {}

    public static void info(Component message) {
        send(message);
    }

    public static void success(Component message) {
        send(Component.literal("§a✔ ").append(message));
    }

    public static void warning(Component message) {
        send(Component.literal("§e⚠ ").append(message));
    }

    public static void error(Component message) {
        send(Component.literal("§c✕ ").append(message));
    }

    private static void send(Component message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc != null && mc.player != null && message != null) {
            mc.player.displayClientMessage(message, false);
        }
    }
}
