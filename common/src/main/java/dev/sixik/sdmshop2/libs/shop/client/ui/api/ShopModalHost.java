package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;

import org.jetbrains.annotations.Nullable;

/** Owns modals and tooltips for one UI surface. Use only on the client thread. */
public interface ShopModalHost {
    /**
     * @return mounted overlay, or null before mounting and after disposal.
     */
    @Nullable
    OverlayLayer overlay();

    /**
     * Adds a modal to this surface when mounted.
     *
     * @param modal modal widget; null is ignored.
     */
    default void openModal(@Nullable Widget modal) {
        OverlayLayer layer = overlay();
        if (layer != null && modal != null) layer.addOverlay(modal);
    }

    /**
     * Removes a modal from this surface.
     *
     * @param modal modal widget; null is ignored.
     */
    default void closeModal(@Nullable Widget modal) {
        OverlayLayer layer = overlay();
        if (layer != null && modal != null) layer.removeOverlay(modal);
    }
}
