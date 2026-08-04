package dev.sixik.sdmshop2.libs.shop.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.sixik.sdmshop2.libs.shop.network.ShopNetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public final class SDMShopClientKeybinds {

    public static final String CATEGORY = "key.categories.sdmshop2";

    private static final KeyMapping OPEN_SHOP = new KeyMapping(
            "key.sdmshop2.open_shop",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            CATEGORY
    );

    private static boolean initialized;

    private SDMShopClientKeybinds() {
    }

    public static void init() {
        if (initialized) {
            return;
        }

        initialized = true;
        KeyMappingRegistry.register(OPEN_SHOP);
        ClientTickEvent.CLIENT_POST.register(SDMShopClientKeybinds::onClientTick);
    }

    private static void onClientTick(Minecraft minecraft) {
        while (OPEN_SHOP.consumeClick()) {
            if (minecraft.player == null || minecraft.getConnection() == null) {
                continue;
            }

            ShopNetworkManager.requestConfiguredShopOpen();
        }
    }
}
