package dev.sixik.sdmshop2.libs.shop.client.ui.textures;

import dev.sixik.unigui.api.math.MutableColor;
import dev.sixik.unigui.api.math.RectView;
import dev.sixik.unigui.api.widget.render.WidgetRender;

/** UniGUI procedural rectangle renderer for shop surfaces. */
public class ColorRectAndBorderTexture implements WidgetRender {
    private int fillColor;
    private int borderColor;
    private float borderWidth;
    private float radius;

    public ColorRectAndBorderTexture() { this(0xCC1E2028, 0xFF555B6E, 1.0f); }
    public ColorRectAndBorderTexture(int fill, int border, float width) { this.fillColor = fill; this.borderColor = border; this.borderWidth = width; }
    public ColorRectAndBorderTexture(int fill, float width, int border) { this(fill, border, width); }
    public ColorRectAndBorderTexture setRadius(float radius) { this.radius = Math.max(0, radius); return this; }
    public ColorRectAndBorderTexture setFillColor(int color) { fillColor = color; return this; }
    public ColorRectAndBorderTexture setBorderColor(int color) { borderColor = color; return this; }
    public ColorRectAndBorderTexture setBorderWidth(float width) { borderWidth = Math.max(0, width); return this; }
    public ColorRectAndBorderTexture pressed() { return new ColorRectAndBorderTexture(borderColor, fillColor, borderWidth).setRadius(radius); }

    @Override public void render(dev.sixik.unigui.api.render.DrawScope scope, dev.sixik.unigui.api.widget.Widget widget) {
        RectView b = widget.layoutBounds();
        scope.roundedRectFilled(b.x(), b.y(), b.width(), b.height(), radius, MutableColor.fromArgb(fillColor));
        if (borderWidth > 0) scope.roundedRectStroke(b.x(), b.y(), b.width(), b.height(), radius, MutableColor.fromArgb(borderColor), borderWidth);
    }
}
