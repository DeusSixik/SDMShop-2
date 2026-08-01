package dev.sixik.sdmshop2.libs.shop.components.misc;

import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfig;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;

public class NameComponent extends ShopComponent {

    public static final IComponentType<NameComponent> TYPE = new Type();

    @Getter
    @ComponentConfig(translationKey = "shop.component.misc.name.name")
    private String name;

    public NameComponent() {
        this.name = "";
    }

    public NameComponent(String name) {
        this.name = name;
    }


    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    private static class Type extends SerializedComponentType<NameComponent> {

        private static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "name");
        private static final ComponentSerializer<NameComponent> SERIALIZER = ComponentSerializer.<NameComponent>create()
                .addString("name", NameComponent::getName, (component, value) -> component.name = value);

        private Type() {
            super(NameComponent::new, SERIALIZER);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }
}
