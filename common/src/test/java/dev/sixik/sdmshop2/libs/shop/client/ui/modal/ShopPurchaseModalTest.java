package dev.sixik.sdmshop2.libs.shop.client.ui.modal;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.cache.ClientCache;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopCatalogElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopToolPanel;
import dev.sixik.sdmshop2.libs.shop.register.ShopRegister;
import dev.sixik.unigui.widgets.containers.VBox;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import net.minecraft.client.resources.language.I18n;

public class ShopPurchaseModalTest {

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
    void testCostStatusBadgeFormattingAndCalculation() {
        // Enough
        ShopPurchaseModal.CostStatusBadge badgeEnough = new ShopPurchaseModal.CostStatusBadge(true, 0.0);
        assertTrue(badgeEnough.isEnough());
        assertEquals(I18n.get("shop.ui.purchase.badge.enough"), badgeEnough.getText());

        // Not enough - integer missing amount (e.g. 50 coins missing)
        ShopPurchaseModal.CostStatusBadge badgeMissing50 = new ShopPurchaseModal.CostStatusBadge(false, 50.0);
        assertFalse(badgeMissing50.isEnough());
        assertEquals(I18n.get("shop.ui.purchase.badge.missing", "50"), badgeMissing50.getText());

        // Not enough - decimal missing amount
        ShopPurchaseModal.CostStatusBadge badgeMissingDecimal = new ShopPurchaseModal.CostStatusBadge(false, 15.5);
        assertFalse(badgeMissingDecimal.isEnough());
        assertEquals(I18n.get("shop.ui.purchase.badge.missing", "15.50"), badgeMissingDecimal.getText());
    }

    @Test
    void testNonAdminCannotSeeEditButton() {
        ShopToolPanel toolPanel = new ShopToolPanel();
        VBox root = new VBox();
        toolPanel.initWidget(root);

        // Minecraft.getInstance() is null in headless tests, so player is null -> not admin
        assertNull(toolPanel.getEditBtn(), "Edit button must not be visible/created for non-admin");
    }

    @Test
    void testPurchaseModalTabsConsistentNaming() {
        ShopCatalogElement.CostEntry opt1 = new ShopCatalogElement.CostEntry("🌲", "2");
        ShopCatalogElement.CostEntry opt2 = new ShopCatalogElement.CostEntry("💎", "1");
        ShopCatalogElement.CostGroup group1 = new ShopCatalogElement.CostGroup("group_1", List.of(opt1));
        ShopCatalogElement.CostGroup group2 = new ShopCatalogElement.CostGroup("group_2", List.of(opt2));

        ShopCatalogElement card = new ShopCatalogElement(
                UUID.randomUUID(),
                "Dragon Egg",
                List.of(new ItemStack(Items.DRAGON_EGG)),
                List.of(group1, group2),
                0.0,
                false,
                null
        );

        ShopPurchaseModal modal = new ShopPurchaseModal(card);
        assertNotNull(modal);
    }
}
