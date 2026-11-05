package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.shop.client.cache.ClientCache;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.unigui.api.layout.LayoutContext;
import dev.sixik.unigui.api.math.MutableRect;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.interaction.TextInput;
import net.minecraft.world.item.ItemStack;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class ShopItemPickerModalTest {

    @BeforeAll
    static void initCache() {
        dev.sixik.sdmshop2.libs.shop.register.ShopRegister.init();
        Path testCachePath = Path.of("build/tmp/test_client_cache_item_picker.json");
        ShopClientCache.setCache(ClientCache.load(testCachePath));
    }

    @AfterAll
    static void cleanup() {
        ShopClientCache.setCache(null);
    }

    @Test
    void testModalLayoutAndMeasure() {
        AtomicReference<ItemStack> chosen = new AtomicReference<>(null);
        ShopItemPickerModal modal = new ShopItemPickerModal(ItemStack.EMPTY, chosen::set);

        modal.measure(new LayoutContext(800.0f, 600.0f));
        modal.arrangeInHost(new MutableRect(0.0f, 0.0f, 800.0f, 600.0f));

        assertNotNull(modal.layoutBounds());
        assertTrue(modal.children().size() > 0);
    }

    @Test
    void testModalWithInitialStack() {
        AtomicReference<ItemStack> chosen = new AtomicReference<>(null);
        ItemStack diamondStack = net.minecraft.world.item.Items.DIAMOND.getDefaultInstance();
        ShopItemPickerModal modal = new ShopItemPickerModal(diamondStack, chosen::set);

        modal.measure(new LayoutContext(800.0f, 600.0f));
        modal.arrangeInHost(new MutableRect(0.0f, 0.0f, 800.0f, 600.0f));

        assertNotNull(modal.layoutBounds());
        assertTrue(modal.children().size() > 0);
    }

    @Test
    void testItemStackRowCreation() {
        AtomicReference<ItemStack> changed = new AtomicReference<>(null);
        HBox row = ShopPropertyRows.itemStackRow("Reward Item", ItemStack.EMPTY, changed::set);

        assertNotNull(row);
        row.applyQueuedMutations();
        assertEquals(3, row.children().size()); // Label, ShopItemWidget (slot), TextInput

        assertTrue(row.children().get(0) instanceof Label);
        assertTrue(row.children().get(2) instanceof TextInput);
    }
}
