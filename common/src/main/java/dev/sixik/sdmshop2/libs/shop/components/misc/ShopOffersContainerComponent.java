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
    protected final Map<UUID, ShopOffer> entryMap = new ConcurrentHashMap<>();

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
