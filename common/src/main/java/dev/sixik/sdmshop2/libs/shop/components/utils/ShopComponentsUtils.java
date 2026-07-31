package dev.sixik.sdmshop2.libs.shop.components.utils;

import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponentCategory;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.Map;

public final class ShopComponentsUtils {

    public static Map<ShopComponentCategory, ObjectList<ShopComponent>> getComponentsByCategory(ShopEntity entity) {
        Map<ShopComponentCategory, ObjectList<ShopComponent>> outMap = new Object2ObjectOpenHashMap<>();

        for (ShopComponent component : entity.getComponents()) {
            ShopComponentCategory category = ShopComponentCategory.getCategory(component);
            ObjectList<ShopComponent> components = outMap.get(category);
            if (components == null) {
                components = new ObjectArrayList<>();
                outMap.put(category, components);
            }

            components.add(component);
        }

        return outMap;
    }

    private ShopComponentsUtils() {
    }
}
