package dev.sixik.sdmshop2.libs.shop.client.ui.widgets;

import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopTheme;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.widgets.containers.LinearBox;
import dev.sixik.unigui.widgets.core.Orientation;

/**
 * Component-owned badge placed above the separator below the products. Add-ons may add decorative
 * widgets to this container; the UI uses the container's role without inspecting the component
 * type.
 */
public final class ShopBadge extends LinearBox {
    /** Creates a compact badge with the shop accent background and rounded corners. */
    public ShopBadge() {
        super(Orientation.HORIZONTAL);
        themeEnabled(false);
        boxVisualEnabled(true);
        backgroundVisible(true);
        background(ShopTheme.color(ShopTheme.DISCOUNT_BADGE));
        borderVisible(false);
        radius(ShopTheme.RADIUS_BADGE);
        layout(style -> style.flexNone().alignItems(Align.CENTER).padding(3, 1).gap(2));
    }
}
