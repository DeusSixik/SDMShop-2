package dev.sixik.sdmshop2.libs.shop.components.misc;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ShopCategoriesContainerComponent extends ShopComponent {

    public static final IComponentType<ShopCategoriesContainerComponent> TYPE = new Type();

    protected final Map<UUID, List<ShopOffer>> indexedEntries = new ConcurrentHashMap<>();
    protected final Map<UUID, CatalogComponent> catalogComponentMap = new Object2ObjectOpenHashMap<>();

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    @Override
    public boolean shouldSync() {
        return false;
    }

    @Override
    public void init() {
        reindex();
    }

    public void reindex() {
        Optional<ShopOffersContainerComponent> opt = getRoot().getComponent(ShopOffersContainerComponent.class);
        if (opt.isEmpty()) return;

        ShopOffersContainerComponent container = opt.get();

        indexedEntries.clear();
        catalogComponentMap.clear();
        for (ShopOffer entry : container.getEntryMap().values()) {
            Optional<CatalogComponent> opt2 = entry.getComponent(CatalogComponent.class);

            if (opt2.isEmpty()) throw new NullPointerException("ShopEntry didn't have 'CategoryComponent'!");

            CatalogComponent categoryComponent = opt2.get();

            indexedEntries.computeIfAbsent(categoryComponent.getUuid(), id -> new ObjectArrayList<>()).add(entry);
            catalogComponentMap.computeIfAbsent(categoryComponent.getUuid(), id -> categoryComponent);
        }
    }

    public ObjectArrayList<UUID> getCatalogsEntry() {
        return new ObjectArrayList<>(indexedEntries.keySet());
    }

    public ObjectArrayList<CatalogComponent> getCatalogsComponents() {
        return new ObjectArrayList<>(catalogComponentMap.values());
    }

    public ObjectArrayList<ShopOffer> getCatalogsEntry(UUID categoryId) {
        return new ObjectArrayList<>(indexedEntries.getOrDefault(categoryId, new ObjectArrayList<>()));
    }

    private static class Type extends SerializedComponentType<ShopCategoriesContainerComponent> {

        public static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "categories_manager");
        private static final ComponentSerializer<ShopCategoriesContainerComponent> SERIALIZER = ComponentSerializer.create();

        private Type() {
            super(ShopCategoriesContainerComponent::new, SERIALIZER);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public boolean showInEditor() {
            return false;
        }
    }
}
