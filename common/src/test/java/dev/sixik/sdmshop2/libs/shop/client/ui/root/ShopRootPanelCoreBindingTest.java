package dev.sixik.sdmshop2.libs.shop.client.ui.root;

import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.SDMShopClient;
import dev.sixik.sdmshop2.libs.shop.client.cache.ClientCache;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.components.ItemRewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import dev.sixik.sdmshop2.libs.shop.register.ShopRegister;
import dev.sixik.unigui.widgets.containers.VBox;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ShopRootPanelCoreBindingTest {

    @BeforeAll
    static void initAll() {
        ShopRegister.init();
        Path testCachePath = Path.of("build/tmp/test_client_cache.json");
        ShopClientCache.setCache(ClientCache.load(testCachePath));
    }

    @AfterAll
    static void tearDownAll() {
        ShopClientCache.setCache(null);
    }

    @Test
    void testEmptyShopHasNoFakeOffersOrCategories() {
        ShopInstance emptyShop = ShopInstance.createManager(new ResourceLocation("sdm", "empty_test"), true);
        SDMShopClient.Shop = emptyShop;

        ShopRootPanel panel = new ShopRootPanel();
        VBox root = new VBox();
        panel.initWidget(root);

        // Categories must only have "ALL" (no fake Resources, Food, Building Blocks)
        assertEquals(1, panel.getCategoryButtons().size());
        assertTrue(panel.getCategoryButtons().containsKey(ShopRootPanel.CAT_ALL));

        // Offers must be completely empty (no fake Glowstone, Elytra, etc.)
        assertEquals(0, panel.getCurrentCards().size());
    }

    @Test
    void testShopBindsStrictlyToCoreOffersAndCategories() {
        ResourceLocation shopId = new ResourceLocation("sdm", "custom_test");
        ShopInstance shop = ShopInstance.createManager(shopId, true);

        // Add a custom category
        CatalogComponent customCat = new CatalogComponent("custom_category");
        customCat.setIconType(CatalogComponent.IconType.ITEM);
        customCat.setIconItem(new ItemStack(Items.GOLDEN_APPLE));
        shop.getCategories().addCategory(customCat);

        // Add a custom offer
        ShopOffer offer = ShopOffer.create(UUID.randomUUID(), false);
        offer.addComponent(new NameComponent("Custom Sword"));
        offer.addComponent(new ItemRewardComponent(Items.DIAMOND_SWORD, 1));
        offer.addComponent(new CatalogComponent("custom_category", customCat.getUuid()));
        shop.getEntries().addEntry(offer);
        shop.getCategories().reindex();

        SDMShopClient.Shop = shop;

        ShopRootPanel panel = new ShopRootPanel();
        VBox root = new VBox();
        panel.initWidget(root);

        // Must have "ALL" and "custom_category"
        assertEquals(2, panel.getCategoryButtons().size());
        assertTrue(panel.getCategoryButtons().containsKey(ShopRootPanel.CAT_ALL));
        assertTrue(panel.getCategoryButtons().containsKey("custom_category"));

        // Must have exactly 1 card corresponding to our offer
        assertEquals(1, panel.getCurrentCards().size());
        assertEquals(offer.getUUID(), panel.getCurrentCards().get(0).getOfferUuid());
    }
}
