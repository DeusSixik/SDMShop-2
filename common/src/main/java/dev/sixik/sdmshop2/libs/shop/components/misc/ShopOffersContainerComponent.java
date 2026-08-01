package dev.sixik.sdmshop2.libs.shop.components.misc;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

    public void addEntry(ShopOffer entry) {
        entryMap.put(entry.getUUID(), entry);
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
            component.entryMap.clear();
            if (offers != null) {
                offers.forEach(component::addEntry);
            }
        }
    }
}
