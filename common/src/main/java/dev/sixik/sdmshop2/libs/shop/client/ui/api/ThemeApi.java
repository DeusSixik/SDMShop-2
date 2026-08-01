package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.DefaultEditMenuRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.DefaultShopOfferElementRender;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public final class ThemeApi {

    private static final ResourceLocation DEFAULT = SDMShop2.resource("default");
    private static final Map<Category, Map<ResourceLocation, Supplier<? extends WidgetRender>>> REGISTER = new Object2ObjectOpenHashMap<>();

    public enum Category {
        Offers,
        Editor
    }

    public static void registerTheme(Category category, ResourceLocation id, Supplier<? extends WidgetRender> render) {
        REGISTER.computeIfAbsent(category, (s1) -> new Object2ObjectOpenHashMap<>())
                .computeIfAbsent(id, (s2) -> render);
    }

    public static Optional<Supplier<? extends WidgetRender>> getTheme(Category category, ResourceLocation id) {
        final Map<ResourceLocation, Supplier<? extends WidgetRender>> data = REGISTER.getOrDefault(category, null);
        if(data == null) return Optional.empty();
        return Optional.ofNullable(data.getOrDefault(id, null));
    }

    public static Supplier<? extends WidgetRender> getDefaultTheme(Category category) {
        return REGISTER.getOrDefault(category, null).getOrDefault(SDMShop2.resource("default"), null);
    }

    static {
        registerTheme(Category.Offers, DEFAULT, DefaultShopOfferElementRender::new);
        registerTheme(Category.Editor, DEFAULT, DefaultEditMenuRender::new);
    }
}
