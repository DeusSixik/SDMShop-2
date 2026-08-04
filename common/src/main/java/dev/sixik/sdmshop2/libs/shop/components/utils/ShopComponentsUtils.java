package dev.sixik.sdmshop2.libs.shop.components.utils;

import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.components.api.CostComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponentCategory;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

import java.util.List;
import java.util.Map;

public final class ShopComponentsUtils {

    public static Map<ShopComponentCategory, ObjectList<ShopComponent>> getComponentsByCategory(ShopEntity entity) {
        Map<ShopComponentCategory, ObjectList<ShopComponent>> outMap = new Object2ObjectOpenHashMap<>();

        for (ShopComponent component : entity.getComponents()) {
            ShopComponentCategory category = ShopComponentCategory.getCategory(component);
            outMap.computeIfAbsent(category, k -> new ObjectArrayList<>())
                    .add(component);
        }

        return outMap;
    }

    public static Map<String, List<CostComponent>> getCostComponentsByGroups(ShopEntity entity) {
        return getCostComponentsByGroups(entity.getComponents(CostComponent.class));
    }

    public static Map<String, List<CostComponent>> getCostComponentsByGroups(Map<ShopComponentCategory, ObjectList<ShopComponent>> map) {
        return getCostComponentsByGroups(map.getOrDefault(ShopComponentCategory.COST, new ObjectArrayList<>()));
    }

    public static Map<String, List<CostComponent>> getCostComponentsByGroups(List<? extends ShopComponent> components) {
        final Object2ObjectMap<String, List<CostComponent>> out_map = new Object2ObjectOpenHashMap<>();

        for (ShopComponent component : components) {
            if(!(component instanceof CostComponent costComponent))
                continue;

            out_map.computeIfAbsent(costComponent.getGroupId(), (c) -> new ObjectArrayList<>())
                    .add(costComponent);
        }
        return out_map;
    }

    private ShopComponentsUtils() {
    }
}
