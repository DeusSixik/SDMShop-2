package dev.sixik.sdmshop2.libs.shop.base;

import com.google.gson.JsonObject;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShopInstanceOfferPropagationTest {

    @Test
    void offerWithoutComponentsInitializesDefaultCatalog() {
        ShopOffer offer = ShopOffer.create(UUID.randomUUID(), false);

        offer.deserialize(new JsonObject());

        assertTrue(offer.hasComponent(CatalogComponent.class));
    }

    @Test
    void offerUpdateReindexesParentShopCategories() {
        ShopInstance shop = ShopInstance.createManager(new ResourceLocation("sdm", "propagation_test"), true);
        ShopOffer offer = ShopOffer.create(UUID.randomUUID(), true);
        CatalogComponent catalog = offer.getComponent(CatalogComponent.class).orElseThrow();
        UUID oldCatalogId = catalog.getUuid();
        UUID newCatalogId = UUID.randomUUID();

        shop.getEntries().addEntry(offer);

        assertSame(shop, offer.getParentShop());
        assertTrue(shop.getCategories().getCatalogsEntry(oldCatalogId).contains(offer));

        catalog.setUuid(newCatalogId);
        catalog.invokeUpdate();

        assertFalse(shop.getCategories().getCatalogsEntry(oldCatalogId).contains(offer));
        assertTrue(shop.getCategories().getCatalogsEntry(newCatalogId).contains(offer));
    }
}
