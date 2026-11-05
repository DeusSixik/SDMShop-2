package dev.sixik.sdmshop2.libs.shop.client.ui.root;

import dev.sixik.sdmshop2.libs.shop.client.ui.modal.ShopPurchaseModal;
import dev.sixik.sdmshop2.libs.shop.register.ShopRegister;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ShopCostChipsTest {

    @BeforeAll
    static void initAll() {
        ShopRegister.init();
    }

    @Test
    void testSingleCompoundCostGroup() {
        ShopCatalogElement.CostEntry cost1 = new ShopCatalogElement.CostEntry("🌲", "2");
        ShopCatalogElement.CostEntry cost2 = new ShopCatalogElement.CostEntry("🪵", "16");
        ShopCatalogElement.CostGroup compoundGroup = new ShopCatalogElement.CostGroup("group_a", List.of(cost1, cost2));

        ShopCatalogElement card = new ShopCatalogElement(
                UUID.randomUUID(),
                "Dragon Egg",
                List.of(new ItemStack(Items.DRAGON_EGG)),
                List.of(compoundGroup),
                0.0,
                false,
                null
        );

        assertEquals(1, card.getCostGroups().size());
        assertEquals(2, card.getCostGroups().get(0).costs().size());
        assertEquals(0, card.getActiveCostGroupIndex());
    }

    @Test
    void testAlternativeCostGroupsAndModalSync() {
        ShopCatalogElement.CostEntry opt1 = new ShopCatalogElement.CostEntry("🌲", "2");
        ShopCatalogElement.CostEntry opt2 = new ShopCatalogElement.CostEntry("🪵", "16");
        ShopCatalogElement.CostGroup group1 = new ShopCatalogElement.CostGroup("group_wood", List.of(opt1));
        ShopCatalogElement.CostGroup group2 = new ShopCatalogElement.CostGroup("group_mud", List.of(opt2));

        ShopCatalogElement card = new ShopCatalogElement(
                UUID.randomUUID(),
                "Dragon Egg",
                List.of(new ItemStack(Items.DRAGON_EGG)),
                List.of(group1, group2),
                0.0,
                false,
                null
        );

        assertEquals(2, card.getCostGroups().size());
        assertEquals(0, card.getActiveCostGroupIndex());

        // Verify switcher button exists in pricesBox and clicking it toggles variant
        assertEquals(2, card.getPricesBox().children().size());
        assertTrue(card.getPricesBox().children().get(1) instanceof dev.sixik.unigui.widgets.interaction.Button);
        dev.sixik.unigui.widgets.interaction.Button switchBtn = (dev.sixik.unigui.widgets.interaction.Button) card.getPricesBox().children().get(1);
        assertEquals("⇄ 1/2", switchBtn.text());

        // Click the button!
        switchBtn.click();
        assertEquals(1, card.getActiveCostGroupIndex());

        dev.sixik.unigui.widgets.interaction.Button updatedSwitchBtn = (dev.sixik.unigui.widgets.interaction.Button) card.getPricesBox().children().get(1);
        assertEquals("⇄ 2/2", updatedSwitchBtn.text());

        // Click again to toggle back to 0
        updatedSwitchBtn.click();
        assertEquals(0, card.getActiveCostGroupIndex());

        // Switch to alternative variant 1
        card.setActiveCostGroupIndex(1);
        assertEquals(1, card.getActiveCostGroupIndex());

        // Check ShopPurchaseModal synchronization
        ShopPurchaseModal modal = new ShopPurchaseModal(card);
        // Verify modal can be instantiated with preselected variant without error
        assertNotNull(modal);
    }

    @Test
    void testMultipleAlternativeGroupsCycling() {
        ShopCatalogElement.CostEntry opt1 = new ShopCatalogElement.CostEntry("🌲", "2");
        ShopCatalogElement.CostEntry opt2 = new ShopCatalogElement.CostEntry("🪵", "16");
        ShopCatalogElement.CostEntry opt3 = new ShopCatalogElement.CostEntry("💎", "1");
        ShopCatalogElement.CostGroup group1 = new ShopCatalogElement.CostGroup("g1", List.of(opt1));
        ShopCatalogElement.CostGroup group2 = new ShopCatalogElement.CostGroup("g2", List.of(opt2));
        ShopCatalogElement.CostGroup group3 = new ShopCatalogElement.CostGroup("g3", List.of(opt3));

        ShopCatalogElement card = new ShopCatalogElement(
                UUID.randomUUID(),
                "Dragon Egg",
                List.of(new ItemStack(Items.DRAGON_EGG)),
                List.of(group1, group2, group3),
                0.0,
                false,
                null
        );

        assertEquals(3, card.getCostGroups().size());
        assertEquals(0, card.getActiveCostGroupIndex());

        // Verify dropdown button exists in pricesBox
        assertEquals(2, card.getPricesBox().children().size());
        assertTrue(card.getPricesBox().children().get(1) instanceof dev.sixik.unigui.widgets.interaction.Button);
        dev.sixik.unigui.widgets.interaction.Button dropdownBtn = (dev.sixik.unigui.widgets.interaction.Button) card.getPricesBox().children().get(1);
        assertEquals("⇄ 1/3 ▾", dropdownBtn.text());

        card.setActiveCostGroupIndex(2);
        assertEquals(2, card.getActiveCostGroupIndex());

        dev.sixik.unigui.widgets.interaction.Button updatedDropdownBtn = (dev.sixik.unigui.widgets.interaction.Button) card.getPricesBox().children().get(1);
        assertEquals("⇄ 3/3 ▾", updatedDropdownBtn.text());

        ShopPurchaseModal modal = new ShopPurchaseModal(card);
        assertNotNull(modal);
    }

    @Test
    void testFormatCostGroupTitle() {
        ShopCatalogElement.CostEntry opt1 = new ShopCatalogElement.CostEntry("🌲", "2");
        ShopCatalogElement.CostEntry opt2 = new ShopCatalogElement.CostEntry("🪵", "16");
        ShopCatalogElement.CostGroup group = new ShopCatalogElement.CostGroup("g1", List.of(opt1, opt2));

        String title = ShopCatalogElement.formatCostGroupTitle(group, 0);
        assertEquals("1. 2x 🌲 + 16x 🪵", title);
    }

    @Test
    void testCostOptionButtonPreview() {
        dev.sixik.unigui.widgets.containers.HBox row = new dev.sixik.unigui.widgets.containers.HBox();
        dev.sixik.unigui.widgets.display.Label label = new dev.sixik.unigui.widgets.display.Label("🌲 2 + 🪵 16");
        row.addChild(label);

        ShopCatalogElement.CostOptionButton optionBtn = new ShopCatalogElement.CostOptionButton(row);
        assertNotNull(optionBtn.getContent());

        optionBtn.measure(new dev.sixik.unigui.api.layout.LayoutContext(500.0f, 500.0f));
        assertTrue(optionBtn.desiredSize().width() > 0);

        optionBtn.arrange(new dev.sixik.unigui.api.math.MutableRect(0, 0, 120, 20));
        assertEquals(120, (int) optionBtn.layoutBounds().width());
        assertEquals(20, (int) optionBtn.layoutBounds().height());
    }

    @Test
    void testCurrencyIconTypes() {
        // 1. Item cost entry
        ShopCatalogElement.CostEntry itemCost = new ShopCatalogElement.CostEntry(new ItemStack(Items.DIAMOND), null, null, "5", null);
        assertNotNull(itemCost.itemIcon());
        assertNull(itemCost.textureIcon());
        assertNull(itemCost.textIcon());

        // 2. Texture cost entry
        net.minecraft.resources.ResourceLocation texLoc = new net.minecraft.resources.ResourceLocation("sdmshop2", "textures/gui/coin.png");
        ShopCatalogElement.CostEntry textureCost = new ShopCatalogElement.CostEntry(null, texLoc, null, "10", null);
        assertNull(textureCost.itemIcon());
        assertEquals(texLoc, textureCost.textureIcon());
        assertNull(textureCost.textIcon());

        // 3. Text cost entry
        ShopCatalogElement.CostEntry textCost = new ShopCatalogElement.CostEntry(null, null, "$", "100", null);
        assertNull(textCost.itemIcon());
        assertNull(textCost.textureIcon());
        assertEquals("$", textCost.textIcon());
    }
}
