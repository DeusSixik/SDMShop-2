package dev.sixik.sdmshop2.libs.shop.components.misc;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ShopCategoriesContainerComponent extends ShopComponent {

    public static final IComponentType<ShopCategoriesContainerComponent> TYPE = new Type();

    protected final Map<UUID, List<ShopOffer>> indexedEntries = new ConcurrentHashMap<>();
    protected final Map<UUID, CatalogComponent> catalogComponentMap = Collections.synchronizedMap(new LinkedHashMap<>());

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

    public synchronized void reindex() {
        if (getRoot() == null) return;
        Optional<ShopOffersContainerComponent> opt = getRoot().getComponent(ShopOffersContainerComponent.class);
        if (opt.isEmpty()) return;

        ShopOffersContainerComponent container = opt.get();

        indexedEntries.clear();
        for (ShopOffer entry : container.getEntryMap().values()) {
            Optional<CatalogComponent> opt2 = entry.getComponent(CatalogComponent.class);
            if (opt2.isEmpty()) continue;

            CatalogComponent categoryComponent = opt2.get();

            indexedEntries.computeIfAbsent(categoryComponent.getUuid(), id -> new ObjectArrayList<>()).add(entry);
            catalogComponentMap.putIfAbsent(categoryComponent.getUuid(), categoryComponent);
        }
    }

    public synchronized void addCategory(CatalogComponent category) {
        Objects.requireNonNull(category, "category");
        if (category.getUuid() == null) {
            category.setUuid(UUID.randomUUID());
        }
        catalogComponentMap.put(category.getUuid(), category);
        syncCategoryOrders();
    }

    public synchronized boolean removeCategory(UUID categoryId) {
        if (categoryId == null) return false;
        boolean removed = catalogComponentMap.remove(categoryId) != null;
        indexedEntries.remove(categoryId);
        if (removed) {
            syncCategoryOrders();
        }
        return removed;
    }

    public synchronized boolean moveCategoryToIndex(UUID categoryId, int targetIndex) {
        if (!catalogComponentMap.containsKey(categoryId)) return false;
        List<CatalogComponent> list = new ArrayList<>(catalogComponentMap.values());
        int currentIndex = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUuid().equals(categoryId)) {
                currentIndex = i;
                break;
            }
        }
        if (currentIndex < 0) return false;
        int clamped = Math.max(0, Math.min(list.size() - 1, targetIndex));
        if (currentIndex == clamped) return false;

        CatalogComponent item = list.remove(currentIndex);
        list.add(clamped, item);

        catalogComponentMap.clear();
        for (int i = 0; i < list.size(); i++) {
            CatalogComponent c = list.get(i);
            c.setOrder(i);
            catalogComponentMap.put(c.getUuid(), c);
            List<ShopOffer> offers = indexedEntries.get(c.getUuid());
            if (offers != null) {
                for (ShopOffer offer : offers) {
                    offer.getComponent(CatalogComponent.class).ifPresent(oCat -> oCat.setOrder(c.getOrder()));
                }
            }
        }
        return true;
    }

    public synchronized boolean moveCategory(UUID categoryId, int offset) {
        if (!catalogComponentMap.containsKey(categoryId)) return false;
        List<CatalogComponent> list = new ArrayList<>(catalogComponentMap.values());
        int currentIndex = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUuid().equals(categoryId)) {
                currentIndex = i;
                break;
            }
        }
        if (currentIndex < 0) return false;
        return moveCategoryToIndex(categoryId, currentIndex + offset);
    }

    private void syncCategoryOrders() {
        int idx = 0;
        for (CatalogComponent c : catalogComponentMap.values()) {
            c.setOrder(idx++);
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
