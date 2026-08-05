package dev.sixik.sdmshop2.fabric.client;

import dev.sixik.sdmshop2.libs.shop.client.SDMShopClientKeybinds;
import net.fabricmc.api.ClientModInitializer;

public final class SDMShop2FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SDMShopClientKeybinds.init();
    }
}
