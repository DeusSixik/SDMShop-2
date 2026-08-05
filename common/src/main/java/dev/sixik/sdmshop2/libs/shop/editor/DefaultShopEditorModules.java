package dev.sixik.sdmshop2.libs.shop.editor;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class DefaultShopEditorModules {

    public static final ResourceLocation SHOP_ENTITIES_ID = sdm("shop_entities");
    public static final ResourceLocation ECONOMY_CURRENCIES_ID = sdm("economy_currencies");

    public static final ShopEditorModule SHOP_ENTITIES = new SimpleModule(
            SHOP_ENTITIES_ID,
            Component.literal("Shop"),
            0
    );

    public static final ShopEditorModule ECONOMY_CURRENCIES = new SimpleModule(
            ECONOMY_CURRENCIES_ID,
            Component.literal("Economy"),
            100
    );

    private DefaultShopEditorModules() {
    }

    private static ResourceLocation sdm(String path) {
        ResourceLocation id = ResourceLocation.tryBuild("sdm", path);
        if (id == null) {
            throw new IllegalArgumentException("Invalid default editor module id: " + path);
        }
        return id;
    }

    private record SimpleModule(ResourceLocation id, Component title, int priority) implements ShopEditorModule {
    }
}
