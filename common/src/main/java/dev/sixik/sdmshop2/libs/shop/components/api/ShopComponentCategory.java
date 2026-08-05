package dev.sixik.sdmshop2.libs.shop.components.api;

import dev.sixik.sdmshop2.SDMShop2;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class ShopComponentCategory {

    private static final Map<ResourceLocation, ShopComponentCategory> REGISTRY = new ConcurrentHashMap<>();
    private static final Map<Class<? extends ShopComponent>, ShopComponentCategory> COMPONENT_CATEGORIES = new ConcurrentHashMap<>();
    private static final Map<Class<? extends ShopComponent>, ShopComponentCategory> COMPONENT_CATEGORY_CACHE = new ConcurrentHashMap<>();

    public static final ShopComponentCategory CONDITION = register("condition");
    public static final ShopComponentCategory COST = register("cost");
    public static final ShopComponentCategory PROMO = register("promo");
    public static final ShopComponentCategory PROMO_EFFECT = register("promo_effect");
    public static final ShopComponentCategory REWARD = register("reward");
    public static final ShopComponentCategory MISC = register("misc");

    static {
        registerComponent(ConditionComponent.class, CONDITION);
        registerComponent(CostComponent.class, COST);
        registerComponent(PromoComponent.class, PROMO);
        registerComponent(PromoEffectComponent.class, PROMO_EFFECT);
        registerComponent(RewardComponent.class, REWARD);
        registerComponent(ShopComponent.class, MISC);
    }

    private final ResourceLocation id;
    private final String name;
    private final int hashCode;

    private ShopComponentCategory(ResourceLocation id) {
        this.id = id;
        this.name = id.getPath().toUpperCase();
        this.hashCode = id.hashCode();
    }

    public static ShopComponentCategory register(String namespace, String path) {
        return register(ResourceLocation.tryBuild(namespace, path));
    }

    public static ShopComponentCategory register(String path) {
        return register(SDMShop2.resource(path));
    }

    public static ShopComponentCategory register(ResourceLocation id) {
        Objects.requireNonNull(id, "id");
        return REGISTRY.computeIfAbsent(id, ShopComponentCategory::new);
    }

    public static <T extends ShopComponent> void registerComponent(Class<T> componentClass, ShopComponentCategory category) {
        Objects.requireNonNull(componentClass, "componentClass");
        Objects.requireNonNull(category, "category");

        COMPONENT_CATEGORIES.put(componentClass, category);
        COMPONENT_CATEGORY_CACHE.clear();
    }

    public static <T extends ShopComponent> void registerComponent(Class<T> componentClass, ResourceLocation categoryId) {
        registerComponent(componentClass, register(categoryId));
    }

    public static <T extends ShopComponent> void registerComponent(Class<T> componentClass, String namespace, String path) {
        registerComponent(componentClass, register(namespace, path));
    }

    public static ShopComponentCategory getOrCreate(ResourceLocation id) {
        return register(id);
    }

    public static @Nullable ShopComponentCategory get(ResourceLocation id) {
        return REGISTRY.get(id);
    }

    public static Map<ResourceLocation, ShopComponentCategory> getRegisteredCategories() {
        return Collections.unmodifiableMap(new HashMap<>(REGISTRY));
    }

    public static ShopComponentCategory getCategory(ShopComponent component) {
        if (component == null) return MISC;
        return getCategory(component.getClass());
    }

    @SuppressWarnings("unchecked")
    public static ShopComponentCategory getCategory(Class<? extends ShopComponent> componentClass) {
        if (componentClass == null) return MISC;

        ShopComponentCategory cachedCategory = COMPONENT_CATEGORY_CACHE.get(componentClass);
        if (cachedCategory != null) {
            return cachedCategory;
        }

        ShopComponentCategory resolvedCategory = resolveCategory(componentClass);
        ShopComponentCategory previousCategory = COMPONENT_CATEGORY_CACHE.putIfAbsent(componentClass, resolvedCategory);
        return previousCategory == null ? resolvedCategory : previousCategory;
    }

    @SuppressWarnings("unchecked")
    private static ShopComponentCategory resolveCategory(Class<? extends ShopComponent> componentClass) {
        Class<?> current = componentClass;
        while (current != null && ShopComponent.class.isAssignableFrom(current)) {
            ShopComponentCategory category = COMPONENT_CATEGORIES.get(current);
            if (category != null) {
                return category;
            }

            current = current.getSuperclass();
        }

        return MISC;
    }

    public ResourceLocation getId() {
        return id;
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    @Override
    public boolean equals(Object object) {
        return this == object || object instanceof ShopComponentCategory category && id.equals(category.id);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
