package dev.sixik.sdmshop2.libs.shop.client.ui.widgets.base;

import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUiElement;
import dev.sixik.unigui.api.core.InvalidationFlags;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.Box;

/** UniGUI base container shared by shop views. */
public class ShopWidgetGroup extends Box implements ShopUiElement {
    public void addWidget(Widget widget) { addChild(widget); }
    public void removeWidget(Widget widget) { removeChild(widget); }
    @Override public void alightWidget() { invalidate(InvalidationFlags.LAYOUT); }
}
