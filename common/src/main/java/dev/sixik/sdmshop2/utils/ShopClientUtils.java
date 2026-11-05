package dev.sixik.sdmshop2.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.sixik.unigui.api.screen.UniScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public final class ShopClientUtils {
    private ShopClientUtils() { }
    public static Component getTranslation(String key) { return getTranslation(key, key); }
    public static Component getTranslation(String key, String fallback) {
        return I18n.exists(key) ? Component.translatable(key) : Component.literal(fallback);
    }
    public static void openUniGuiScreen(UniScreen screen) {
        if (screen == null) throw new IllegalArgumentException("screen cannot be null");
        RenderSystem.recordRenderCall(screen::openGui);
    }
}
