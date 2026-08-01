package dev.sixik.sdmshop2.libs.shop.components.misc;

import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfig;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfigOptions;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class CatalogComponent extends ShopComponent {

    public static final IComponentType<CatalogComponent> TYPE = new Type();

    protected static final String NULL = "none";

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.misc.catalog.id")
    @ComponentConfigOptions(provider = "sdm:catalog_ids")
    private String id;

    @Getter
    @Setter
    private UUID uuid;

    public CatalogComponent() {
        this(NULL);
    }

    public CatalogComponent(String id) {
        this(id, UUID.randomUUID());
    }

    public CatalogComponent(String id, UUID uuid) {
        this.id = id;
        this.uuid = uuid;
    }

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    private static class Type extends SerializedComponentType<CatalogComponent> {

        public static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "catalog");
        private static final ComponentSerializer<CatalogComponent> SERIALIZER = ComponentSerializer.<CatalogComponent>create()
                .addString("catalog_id", CatalogComponent::getId, CatalogComponent::setId)
                .add("uuid", FieldCodecs.UUID_CODEC, CatalogComponent::getUuid, CatalogComponent::setUuid);

        private Type() {
            super(CatalogComponent::new, SERIALIZER);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public CatalogComponent createFromBuilder(Object... args) {
            if (args.length != 1 && args.length != 2) {
                throw new IllegalArgumentException("CatalogComponent.createFromBuilder() takes 1 or 2 arguments (String, (Optional) UUID/String)");
            }

            if (args.length == 1) {
                return new CatalogComponent((String) args[0]);
            }

            Object id = args[1];
            return new CatalogComponent((String) args[0], id instanceof UUID ? (UUID) id : UUID.fromString((String) id));
        }
    }
}
