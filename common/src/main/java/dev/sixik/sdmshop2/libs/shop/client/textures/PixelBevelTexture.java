package dev.sixik.sdmshop2.libs.shop.client.textures;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberColor;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.gui.texture.TransformTexture;
import com.lowdragmc.lowdraglib.utils.Rect;
import dev.sixik.sdmshop2.libs.shop.client.SDMShaders;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;

import java.awt.Color;

@LDLRegister(name = "pixel_bevel_texture", group = "texture")
public class PixelBevelTexture extends TransformTexture {

    public static final int PAGE_COLOR = 0xFF0B0D12;
    public static final int PANEL_COLOR = 0xFF1B2030;
    public static final int PANEL_LOW_COLOR = 0xFF151925;
    public static final int LINE_LOW_COLOR = 0xFF0A0C12;
    public static final int LINE_HIGH_COLOR = 0xFF3A4258;
    public static final int ACCENT_COLOR = 0xFFE8B039;
    public static final int ACCENT_LOW_COLOR = 0xFF7A5A12;
    public static final int ACCENT_HIGH_COLOR = 0xFFFFD580;

    @Configurable
    @NumberColor
    public int fillColor;

    @Configurable
    @NumberColor
    public int highlightColor;

    @Configurable
    @NumberColor
    public int shadowColor;

    @Configurable
    @NumberRange(range = {0, 16}, wheel = 1)
    public float bevelThickness;

    @Configurable
    public boolean pressed;

    public PixelBevelTexture() {
        this(PANEL_COLOR, LINE_HIGH_COLOR, LINE_LOW_COLOR, 2.0f);
    }

    public PixelBevelTexture(int fillColor, int highlightColor, int shadowColor, float bevelThickness) {
        this.fillColor = fillColor;
        this.highlightColor = highlightColor;
        this.shadowColor = shadowColor;
        this.bevelThickness = Math.max(0.0f, bevelThickness);
    }

    public PixelBevelTexture(Color fillColor, Color highlightColor, Color shadowColor, float bevelThickness) {
        this(fillColor.getRGB(), highlightColor.getRGB(), shadowColor.getRGB(), bevelThickness);
    }

    public static PixelBevelTexture panel() {
        return new PixelBevelTexture(PANEL_COLOR, LINE_LOW_COLOR, LINE_HIGH_COLOR, 0.7f);
    }

    public static PixelBevelTexture panelLow() {
        return new PixelBevelTexture(PANEL_LOW_COLOR, LINE_LOW_COLOR, LINE_HIGH_COLOR, 0.7f);
    }

    public static PixelBevelTexture accent() {
        return new PixelBevelTexture(ACCENT_COLOR, ACCENT_LOW_COLOR, ACCENT_HIGH_COLOR, 0.7f);
    }

    public static PixelBevelTexture dark() {
        return new PixelBevelTexture(PAGE_COLOR, LINE_LOW_COLOR, LINE_HIGH_COLOR, 0.7f);
    }

    public PixelBevelTexture setFillColor(int fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public PixelBevelTexture setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
        return this;
    }

    public PixelBevelTexture setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
        return this;
    }

    public PixelBevelTexture setBevelColors(int highlightColor, int shadowColor) {
        this.highlightColor = highlightColor;
        this.shadowColor = shadowColor;
        return this;
    }

    public PixelBevelTexture setBevelThickness(float bevelThickness) {
        this.bevelThickness = Math.max(0.0f, bevelThickness);
        return this;
    }

    public PixelBevelTexture thickness(float bevelThickness) {
        return setBevelThickness(bevelThickness);
    }

    public PixelBevelTexture setPressed(boolean pressed) {
        this.pressed = pressed;
        return this;
    }

    public PixelBevelTexture pressed() {
        return setPressed(true);
    }

    @Environment(EnvType.CLIENT)
    @Override
    protected void drawInternal(GuiGraphics graphics, int mouseX, int mouseY, float x, float y, int width, int height) {
        if (width <= 0 || height <= 0) {
            return;
        }

        int high = pressed ? shadowColor : highlightColor;
        int low = pressed ? highlightColor : shadowColor;
        SDMShaders.drawPixelBevelBox(
                graphics,
                Rect.ofRelative((int) x, width, (int) y, height),
                bevelThickness,
                fillColor,
                high,
                low
        );
    }
}
