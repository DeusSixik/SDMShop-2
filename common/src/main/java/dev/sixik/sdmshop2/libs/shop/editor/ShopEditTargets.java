package dev.sixik.sdmshop2.libs.shop.editor;

import dev.sixik.sdmshop2.libs.sdmeconomy.IExternalCurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.IStoredCurrency;
import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import net.minecraft.resources.ResourceLocation;

/**
 * Well-known draft targets used by the default editor modules.
 */
public final class ShopEditTargets {

    private ShopEditTargets() {
    }

    public static ShopEditTarget<ShopInstance> shop(ResourceLocation shopId) {
        return new ShopEditTarget<>(targetId("shop", shopId), ShopInstance.class);
    }

    public static ShopEditTarget<IExternalCurrency> externalCurrency(ResourceLocation currencyId) {
        return new ShopEditTarget<>(targetId("economy/external_currency", currencyId), IExternalCurrency.class);
    }

    public static ShopEditTarget<IStoredCurrency> storedCurrency(ResourceLocation currencyId) {
        return new ShopEditTarget<>(targetId("economy/stored_currency", currencyId), IStoredCurrency.class);
    }

    public static <T> ShopEditTarget<T> addon(ResourceLocation addonTargetId, Class<T> type) {
        return new ShopEditTarget<>(targetId("addon", addonTargetId), type);
    }

    private static ResourceLocation targetId(String group, ResourceLocation objectId) {
        return sdm("editor/" + group + "/" + objectId.getNamespace() + "/" + objectId.getPath());
    }

    private static ResourceLocation sdm(String path) {
        ResourceLocation id = ResourceLocation.tryBuild("sdm", path);
        if (id == null) {
            throw new IllegalArgumentException("Invalid shop editor target path: " + path);
        }
        return id;
    }
}
