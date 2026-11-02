package dev.sixik.sdmshop2.libs.shop.client.ui.toast;

import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUiElement;
import dev.sixik.unigui.widgets.containers.PanelWidget;

/** @deprecated UniGUI's OverlayLayer now owns shop toasts directly. */
@Deprecated
public final class ShopToastOverlayElement extends PanelWidget implements ShopUiElement {
    @Override public void alightWidget() { }
}
