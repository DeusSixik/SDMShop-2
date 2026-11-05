package dev.sixik.sdmshop2.libs.shop.client.ui.widgets;

import dev.sixik.sdmshop2.libs.shop.register.ShopRegister;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import net.minecraft.world.item.Items;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShopItemWidgetLifecycleTest {
    @BeforeAll static void initialize() { ShopRegister.init(); }

    @Test void detach_afterActiveOverlayChanges_removesTooltipFromOriginalOwner() {
        OverlayLayer original = new OverlayLayer();
        OverlayLayer other = new OverlayLayer();
        ShopItemWidget item = new ShopItemWidget(Items.DIAMOND.getDefaultInstance(), 16);
        item.attachTooltip(original);
        original.applyQueuedMutations();
        assertNotNull(item.getTooltip());
        assertTrue(original.children().contains(item.getTooltip()));
        item.detachTooltip(other);
        original.applyQueuedMutations();
        assertNull(item.getTooltip());
        assertTrue(original.children().isEmpty());
    }

    @Test void attach_newOwner_transfersTooltipAndDisposeReleasesIt() {
        OverlayLayer first = new OverlayLayer();
        OverlayLayer second = new OverlayLayer();
        ShopItemWidget item = new ShopItemWidget(Items.DIAMOND.getDefaultInstance(), 16);
        item.attachTooltip(first);
        first.applyQueuedMutations();
        item.attachTooltip(second);
        first.applyQueuedMutations();
        second.applyQueuedMutations();
        assertTrue(first.children().isEmpty());
        assertTrue(second.children().contains(item.getTooltip()));
        item.dispose();
        second.applyQueuedMutations();
        assertNull(item.getTooltip());
        assertTrue(second.children().isEmpty());
    }
}
