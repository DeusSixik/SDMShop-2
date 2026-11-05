package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import dev.sixik.unigui.api.widget.Widget;

import org.jetbrains.annotations.Nullable;

/**
 * Factory for one component's presentation. A card and a purchase dialog each request a fresh
 * widget tree; implementations must never return an already mounted widget. Called on the client
 * thread. Returning null deliberately hides the element without hiding other components.
 */
@FunctionalInterface
public interface ShopRenderElement {
    /**
     * @return a fresh component-owned widget, or null when there is nothing to display.
     */
    @Nullable
    Widget createRender();
}
