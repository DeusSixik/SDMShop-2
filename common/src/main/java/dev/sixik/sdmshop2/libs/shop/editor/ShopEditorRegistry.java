package dev.sixik.sdmshop2.libs.shop.editor;

import net.minecraft.resources.ResourceLocation;

import java.util.*;

public final class ShopEditorRegistry {

    private static final Map<ResourceLocation, ShopEditorModule> MODULES = new LinkedHashMap<>();

    static {
        register(DefaultShopEditorModules.SHOP_ENTITIES);
        register(DefaultShopEditorModules.ECONOMY_CURRENCIES);
    }

    private ShopEditorRegistry() {
    }

    public static synchronized void register(ShopEditorModule module) {
        Objects.requireNonNull(module, "module");
        ResourceLocation id = Objects.requireNonNull(module.id(), "module id");
        ShopEditorModule old = MODULES.putIfAbsent(id, module);
        if (old != null && old != module) {
            throw new IllegalArgumentException("Shop editor module already registered: " + id);
        }
    }

    public static synchronized Optional<ShopEditorModule> get(ResourceLocation id) {
        return Optional.ofNullable(MODULES.get(id));
    }

    public static synchronized List<ShopEditorModule> modules() {
        List<ShopEditorModule> modules = new ArrayList<>(MODULES.values());
        modules.sort(Comparator
                .comparingInt(ShopEditorModule::priority)
                .thenComparing(module -> module.id().toString()));
        return List.copyOf(modules);
    }
}
