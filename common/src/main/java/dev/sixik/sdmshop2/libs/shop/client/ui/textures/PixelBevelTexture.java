package dev.sixik.sdmshop2.libs.shop.client.ui.textures;

/** Preset UniGUI rectangle renderers matching the previous shop palette. */
public class PixelBevelTexture extends ColorRectAndBorderTexture {
    public static final int PANEL_COLOR = 0xFF252833;
    public static final int LINE_LOW_COLOR = 0xFF151720;
    public static final int LINE_HIGH_COLOR = 0xFF565D70;
    public static final int ACCENT_COLOR = 0xFF3D74D8;
    public static final int ACCENT_LOW_COLOR = 0xFF24478C;
    public static final int ACCENT_HIGH_COLOR = 0xFF78A5FF;

    public PixelBevelTexture(int fill, int low, int high, float width) { super(fill, high, width); }
    public static PixelBevelTexture panel() { return new PixelBevelTexture(PANEL_COLOR, LINE_LOW_COLOR, LINE_HIGH_COLOR, 1); }
    public static PixelBevelTexture panelLow() { return new PixelBevelTexture(0xFF1B1D25, LINE_LOW_COLOR, LINE_HIGH_COLOR, 1); }
    public static PixelBevelTexture accent() { return new PixelBevelTexture(ACCENT_COLOR, ACCENT_LOW_COLOR, ACCENT_HIGH_COLOR, 1); }
    @Override public PixelBevelTexture pressed() { return new PixelBevelTexture(LINE_LOW_COLOR, LINE_HIGH_COLOR, LINE_LOW_COLOR, 1); }
}
