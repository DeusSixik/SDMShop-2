package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.shop.client.ui.style.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public final class StyleApi {

    private static final ResourceLocation DEFAULT = SDMShop2.resource("default");
    private static final Map<Category, Map<ResourceLocation, Supplier<? extends WidgetRender>>> REGISTER = new EnumMap<>(Category.class);

    public enum Category {
        Offers,
        OffersPanel,
        ToolPanel,
        TabsPanel,
        PurchaseModal,
        Editor
    }

    public static void registerStyle(Category category, ResourceLocation id, Supplier<? extends WidgetRender> render) {
        REGISTER.computeIfAbsent(category, (s1) -> new Object2ObjectOpenHashMap<>())
                .computeIfAbsent(id, (s2) -> render);
    }

    public static Optional<Supplier<? extends WidgetRender>> getStyle(Category category, ResourceLocation id) {
        final Map<ResourceLocation, Supplier<? extends WidgetRender>> data = REGISTER.getOrDefault(category, null);
        if(data == null) return Optional.empty();
        return Optional.ofNullable(data.getOrDefault(id, null));
    }

    public static Supplier<? extends WidgetRender> getDefaultStyle(Category category) {
        return REGISTER.getOrDefault(category, null).getOrDefault(SDMShop2.resource("default"), null);
    }

    static {
        registerStyle(Category.Offers, DEFAULT, DefaultShopOfferElementRender::new);
        registerStyle(Category.OffersPanel, DEFAULT, DefaultShopOffersPanelRender::new);
        registerStyle(Category.Editor, DEFAULT, DefaultEditMenuRender::new);
        registerStyle(Category.ToolPanel, DEFAULT, DefaultShopToolPanelElementRender::new);
        registerStyle(Category.TabsPanel, DEFAULT, DefaultShopTabsPanelRender::new);
        registerStyle(Category.PurchaseModal, DEFAULT, DefaultShopPurchaseModalRender::new);
    }
}
