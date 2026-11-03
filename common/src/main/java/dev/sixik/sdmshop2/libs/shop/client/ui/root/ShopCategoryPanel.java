package dev.sixik.sdmshop2.libs.shop.client.ui.root;

import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopScreenElement;
import dev.sixik.unigui.api.math.MutableColor;
import dev.sixik.unigui.api.style.StyleKeys;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.api.widget.visual.BackgroundKind;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.PanelWidget;
import dev.sixik.unigui.widgets.containers.VBox;

public class ShopCategoryPanel implements ShopScreenElement {

    @Override
    public Widget initWidget(PanelWidget root) {
        VBox panel = new VBox();
        panel.layout(layout -> layout.flexGrow(1));

        panel.boxVisualEnabled(true);
        panel.style(style -> style
                .put(StyleKeys.BACKGROUND_VISIBLE, true)
                .put(StyleKeys.BACKGROUND_KIND, BackgroundKind.COLOR)
                .put(StyleKeys.BACKGROUND_COLOR, new MutableColor(1f, 1f, 1f, 1f))
        );

        root.addChild(panel);
        return panel;
    }
}
