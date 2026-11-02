package dev.sixik.sdmshop2.libs.shop.client.ui.textures;

import dev.sixik.unigui.api.math.MutableColor;
import dev.sixik.unigui.api.math.RectView;
import dev.sixik.unigui.api.widget.render.WidgetRender;

/** Four-corner UniGUI gradient renderer. */
public class GradientTexture implements WidgetRender {
    private int topLeft = 0xFFFFFFFF, topRight = 0xFFFFFFFF, bottomLeft = 0xFF000000, bottomRight = 0xFF000000;
    public GradientTexture setTopLeftColor(int color) { topLeft = color; return this; }
    public GradientTexture setTopRightColor(int color) { topRight = color; return this; }
    public GradientTexture setBottomLeftColor(int color) { bottomLeft = color; return this; }
    public GradientTexture setBottomRightColor(int color) { bottomRight = color; return this; }
    @Override public void render(dev.sixik.unigui.api.render.DrawScope scope, dev.sixik.unigui.api.widget.Widget widget) {
        RectView b = widget.layoutBounds();
        scope.addRectFilledMultiColor(b.x(), b.y(), b.width(), b.height(), MutableColor.fromArgb(topLeft), MutableColor.fromArgb(topRight), MutableColor.fromArgb(bottomRight), MutableColor.fromArgb(bottomLeft));
    }
}
