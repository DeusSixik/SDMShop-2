package dev.sixik.sdmshop2.libs.shop.components.misc;

import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import net.minecraft.resources.ResourceLocation;

public final class RenderHideComponent extends ShopComponent {

    public static final IComponentType<RenderHideComponent> TYPE = new Type();

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    private static class Type extends SerializedComponentType<RenderHideComponent> {

        private static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "hide_render");
        private static final ComponentSerializer<RenderHideComponent> SERIALIZER = ComponentSerializer.create();

        private Type() {
            super(RenderHideComponent::new, SERIALIZER);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }
}
