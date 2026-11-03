package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.impl.widget.WidgetBase;
import dev.sixik.unigui.widgets.containers.PanelWidget;

public interface ShopScreenElement {

    Widget initWidget(PanelWidget root);

    default int priority() {
        return 0;
    }
}
