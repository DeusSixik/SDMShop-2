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
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class CatalogComponent extends ShopComponent {

    public static final IComponentType<CatalogComponent> TYPE = new Type();

    protected static final String NULL = "none";
    public static final ResourceLocation EMPTY_ICON_TEXTURE = new ResourceLocation("sdm", "empty_icon");

    public enum IconType {
        NONE,
        ITEM,
        TEXTURE
    }

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.misc.catalog.id")
    @ComponentConfigOptions(provider = "sdm:catalog_ids")
    private String id;

    @Getter
    @Setter
    private UUID uuid;

    @Getter
    @Setter
    private int order;

    @Getter
    @ComponentConfig(translationKey = "shop.component.misc.catalog.icon_type")
    private IconType iconType = IconType.NONE;

    @Getter
    @ComponentConfig(translationKey = "shop.component.misc.catalog.icon_item")
    private ItemStack iconItem = ItemStack.EMPTY;

    @Getter
    @ComponentConfig(translationKey = "shop.component.misc.catalog.icon_texture")
    private ResourceLocation iconTexture = EMPTY_ICON_TEXTURE;

    public CatalogComponent() {
        this(NULL);
    }

    public CatalogComponent(String id) {
        this(id, UUID.randomUUID());
    }

    public CatalogComponent(String id, UUID uuid) {
        this(id, uuid, 0);
    }

    public CatalogComponent(String id, UUID uuid, int order) {
        this(id, uuid, order, IconType.NONE, ItemStack.EMPTY, EMPTY_ICON_TEXTURE);
    }

    public CatalogComponent(String id, UUID uuid, int order, IconType iconType, ItemStack iconItem, ResourceLocation iconTexture) {
        this.id = id;
        this.uuid = uuid;
        this.order = order;
        setIconType(iconType);
        setIconItem(iconItem);
        setIconTexture(iconTexture);
    }

    public static CatalogComponent copyOf(CatalogComponent source) {
        if (source == null) {
            return new CatalogComponent();
        }

        return new CatalogComponent(
                source.getId(),
                source.getUuid(),
                source.getOrder(),
                source.getIconType(),
                source.getIconItem(),
                source.getIconTexture()
        );
    }

    public void copyIconFrom(CatalogComponent source) {
        if (source == null) {
            clearIcon();
            return;
        }

        setIconType(source.getIconType());
        setIconItem(source.getIconItem());
        setIconTexture(source.getIconTexture());
    }

    public void setIconType(IconType iconType) {
        this.iconType = iconType == null ? IconType.NONE : iconType;
    }

    public void setIconItem(ItemStack iconItem) {
        this.iconItem = iconItem == null ? ItemStack.EMPTY : iconItem.copyWithCount(1);
    }

    public void setIconTexture(ResourceLocation iconTexture) {
        this.iconTexture = iconTexture == null ? EMPTY_ICON_TEXTURE : iconTexture;
    }

    public void clearIcon() {
        setIconType(IconType.NONE);
        setIconItem(ItemStack.EMPTY);
        setIconTexture(EMPTY_ICON_TEXTURE);
    }

    public boolean hasItemIcon() {
        return iconType == IconType.ITEM && iconItem != null && !iconItem.isEmpty();
    }

    public boolean hasTextureIcon() {
        return iconType == IconType.TEXTURE && !isEmptyIconTexture(iconTexture);
    }

    public boolean hasIcon() {
        return hasItemIcon() || hasTextureIcon();
    }

    public static boolean isEmptyIconTexture(ResourceLocation texture) {
        return texture == null || EMPTY_ICON_TEXTURE.equals(texture);
    }

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    private static class Type extends SerializedComponentType<CatalogComponent> {

        public static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "catalog");
        private static final ComponentSerializer<CatalogComponent> SERIALIZER = ComponentSerializer.<CatalogComponent>create()
                .addString("catalog_id", CatalogComponent::getId, CatalogComponent::setId)
                .add("uuid", FieldCodecs.UUID_CODEC, CatalogComponent::getUuid, CatalogComponent::setUuid)
                .addDefaulted("order", FieldCodecs.INT, CatalogComponent::getOrder, CatalogComponent::setOrder, 0)
                .addDefaulted("icon_type", FieldCodecs.enumCodec(IconType.class), CatalogComponent::getIconType, CatalogComponent::setIconType, IconType.NONE)
                .addDefaulted("icon_item", FieldCodecs.ITEM_STACK, CatalogComponent::getIconItem, CatalogComponent::setIconItem, ItemStack.EMPTY)
                .addDefaultedResourceLocation("icon_texture", CatalogComponent::getIconTexture, CatalogComponent::setIconTexture, EMPTY_ICON_TEXTURE);

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
