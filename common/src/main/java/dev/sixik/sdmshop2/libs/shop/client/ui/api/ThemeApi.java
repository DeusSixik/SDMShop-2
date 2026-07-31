package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.DefaultShopOfferElementRender;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public final class ThemeApi {

    private static final Map<Category, Map<ResourceLocation, Supplier<? extends OfferElementRender>>> REGISTER = new Object2ObjectOpenHashMap<>();

    public enum Category {
        Offers
    }

    public static void registerTheme(Category category, ResourceLocation id, Supplier<? extends OfferElementRender> render) {
        REGISTER.computeIfAbsent(category, (s1) -> new Object2ObjectOpenHashMap<>())
                .computeIfAbsent(id, (s2) -> render);
    }

    public static Optional<Supplier<? extends OfferElementRender>> getTheme(Category category, ResourceLocation id) {
        final Map<ResourceLocation, Supplier<? extends OfferElementRender>> data = REGISTER.getOrDefault(category, null);
        if(data == null) return Optional.empty();
        return Optional.ofNullable(data.getOrDefault(id, null));
    }

    public static Supplier<? extends OfferElementRender> getDefaultTheme(Category category) {
        return REGISTER.getOrDefault(category, null).getOrDefault(SDMShop2.resource("default"), null);
    }

    static {
        registerTheme(Category.Offers, SDMShop2.resource("default"), DefaultShopOfferElementRender::new);
    }
}
