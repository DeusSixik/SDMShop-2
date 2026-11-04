package dev.sixik.sdmshop2.libs.shop.components.misc;

import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ShopOffersContainerComponent extends ShopComponent {

    public static final IComponentType<ShopOffersContainerComponent> TYPE = new Type();

    @Getter
    protected final Map<UUID, ShopOffer> entryMap = Collections.synchronizedMap(new LinkedHashMap<>());

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    @Override
    public int priority() {
        return 1000;
    }

    @Override
    public void init() {
        for (ShopOffer entry : entryMap.values()) {
            attachEntry(entry);
        }
    }

    public void addEntry(ShopOffer entry) {
        Objects.requireNonNull(entry, "entry");

        ShopOffer previous = entryMap.put(entry.getUUID(), entry);
        if (previous == entry) {
            attachEntry(entry);
            return;
        }

        if (previous != null && previous != entry) {
            previous.setParentShop(null);
        }

        attachEntry(entry);
        notifyEntriesChanged();
    }

    @Nullable
    public ShopOffer removeEntry(UUID entryId) {
        ShopOffer removed = entryMap.remove(entryId);
        if (removed != null) {
            removed.setParentShop(null);
            notifyEntriesChanged();
        }
        return removed;
    }

    @Nullable
    public ShopOffer getEntry(UUID entryId) {
        return entryMap.get(entryId);
    }

    public void getEntries(UUID[] entryIds, ShopOffer[] outArray) {
        for (int i = 0; i < entryIds.length; i++) {
            outArray[i] = entryMap.get(entryIds[i]);
        }
    }

    public synchronized boolean moveEntryToIndex(UUID entryId, int targetIndex) {
        if (!entryMap.containsKey(entryId)) return false;
        List<ShopOffer> list = new ArrayList<>(entryMap.values());
        int currentIndex = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUUID().equals(entryId)) {
                currentIndex = i;
                break;
            }
        }
        if (currentIndex < 0) return false;
        int clampedIndex = Math.max(0, Math.min(list.size() - 1, targetIndex));
        if (currentIndex == clampedIndex) return false;

        ShopOffer item = list.remove(currentIndex);
        list.add(clampedIndex, item);

        entryMap.clear();
        for (ShopOffer offer : list) {
            entryMap.put(offer.getUUID(), offer);
        }
        notifyEntriesChanged();
        return true;
    }

    public synchronized boolean moveEntry(UUID entryId, int offset) {
        if (!entryMap.containsKey(entryId)) return false;
        List<ShopOffer> list = new ArrayList<>(entryMap.values());
        int currentIndex = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUUID().equals(entryId)) {
                currentIndex = i;
                break;
            }
        }
        if (currentIndex < 0) return false;
        return moveEntryToIndex(entryId, currentIndex + offset);
    }

    public synchronized void addEntryAfter(ShopOffer entry, UUID afterId) {
        Objects.requireNonNull(entry, "entry");
        List<ShopOffer> list = new ArrayList<>(entryMap.values());
        int afterIndex = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUUID().equals(afterId)) {
                afterIndex = i;
                break;
            }
        }
        if (afterIndex >= 0) {
            list.add(afterIndex + 1, entry);
            entryMap.clear();
            for (ShopOffer offer : list) {
                entryMap.put(offer.getUUID(), offer);
            }
            attachEntry(entry);
            notifyEntriesChanged();
        } else {
            addEntry(entry);
        }
    }

    private void attachEntry(ShopOffer entry) {
        if (getRoot() instanceof ShopInstance shop) {
            entry.setParentShop(shop);
        }
    }

    private void notifyEntriesChanged() {
        if (getRoot() instanceof ShopInstance shop) {
            shop.onOffersChanged(this);
        }
    }

    private static class Type extends SerializedComponentType<ShopOffersContainerComponent> {

        public static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "offers_container");
        private static final ComponentSerializer<ShopOffersContainerComponent> SERIALIZER = ComponentSerializer.<ShopOffersContainerComponent>create()
                .addRequired("offers", FieldCodecs.list(FieldCodecs.SHOP_OFFER), Type::getOffers, Type::setOffers);

        private Type() {
            super(ShopOffersContainerComponent::new, SERIALIZER);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public boolean showInEditor() {
            return false;
        }

        private static List<ShopOffer> getOffers(ShopOffersContainerComponent component) {
            return new ArrayList<>(component.entryMap.values());
        }

        private static void setOffers(ShopOffersContainerComponent component, List<ShopOffer> offers) {
            component.entryMap.values().forEach(entry -> entry.setParentShop(null));
            component.entryMap.clear();
            if (offers != null) {
                offers.forEach(component::addEntry);
            }
        }
    }
}
