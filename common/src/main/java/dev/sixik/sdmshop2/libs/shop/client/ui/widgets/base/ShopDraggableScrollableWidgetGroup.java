package dev.sixik.sdmshop2.libs.shop.client.ui.widgets.base;

import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUiElement;
import dev.sixik.unigui.api.core.InvalidationFlags;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.ScrollView;
import dev.sixik.unigui.widgets.containers.VBox;

/** UniGUI vertical scrolling host used by offer lists. */
public class ShopDraggableScrollableWidgetGroup extends ScrollView implements ShopUiElement {
    private final VBox contentBox = new VBox();

    public ShopDraggableScrollableWidgetGroup() {
        content(contentBox);
        overlayScrollBars(true);
    }

    public VBox contentBox() { return contentBox; }
    public void addWidget(Widget widget) { contentBox.addChild(widget); }
    public void clearWidgets() { contentBox.clearChildren(); }
    @Override public void alightWidget() { invalidate(InvalidationFlags.LAYOUT); }
}
