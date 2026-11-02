package dev.sixik.sdmshop2.libs.shop.client.ui.toast;

import net.minecraft.network.chat.Component;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShopToastTest {
    @Test
    void constructor_shortDuration_clampsToMinimum() {
        ShopToast toast = new ShopToast(ShopToast.Type.INFO, Component.literal("title"), Component.literal("message"), 1L);
        assertEquals(500L, toast.getDurationMs());
    }

    @Test
    void progress_beforeAndAfterLifetime_isClamped() {
        ShopToast toast = new ShopToast(ShopToast.Type.SUCCESS, Component.empty(), Component.empty(), 1_000L);
        assertEquals(0.0f, toast.progress(toast.getCreatedAtMs() - 100L));
        assertEquals(1.0f, toast.progress(toast.getCreatedAtMs() + 2_000L));
        assertTrue(toast.isExpired(toast.getCreatedAtMs() + 1_000L));
    }
}
