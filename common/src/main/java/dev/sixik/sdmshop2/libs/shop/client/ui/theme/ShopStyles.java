package dev.sixik.sdmshop2.libs.shop.client.ui.theme;

import dev.sixik.unigui.widgets.containers.SurfaceWidget;
import dev.sixik.unigui.widgets.interaction.Button;

/** Explicit shop styles, insulated from UniGUI's default widget theme. */
public final class ShopStyles {
    /** Prevents instantiation. */
    private ShopStyles() {}

    /**
     * Applies a surface without changing its layout.
     *
     * @param widget target surface.
     * @param background ARGB fill color.
     * @param border ARGB border color.
     * @param radius corner radius in logical UI pixels.
     * @param <T> surface type.
     * @return the styled widget.
     */
    public static <T extends SurfaceWidget<?>> T surface(
            T widget, int background, int border, float radius) {
        widget.themeEnabled(false);
        widget.boxVisualEnabled(true);
        widget.backgroundVisible(true);
        widget.background(ShopTheme.color(background));
        widget.borderVisible(true);
        widget.border(ShopTheme.color(border));
        widget.borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        widget.radius(radius);
        return widget;
    }

    /**
     * Applies the shared shop button style, including hover and pressed states.
     *
     * @param button target button.
     * @return the styled button.
     */
    public static Button button(Button button) {
        surface(button, ShopTheme.BG_CARD, ShopTheme.BORDER_SUBTLE, ShopTheme.RADIUS_BUTTON);
        button.hoverBackground(ShopTheme.color(ShopTheme.BG_CARD_HOVER));
        button.pressedBackground(ShopTheme.color(ShopTheme.GOLD_MUTED));
        button.textColor(ShopTheme.color(ShopTheme.TEXT_PRIMARY));
        return button;
    }
}
