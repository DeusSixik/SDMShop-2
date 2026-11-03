package dev.sixik.sdmshop2.libs.shop.client.ui.root;

import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopScreenElement;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.layout.FlexDirection;
import dev.sixik.unigui.api.layout.LayoutStyle;
import dev.sixik.unigui.api.math.MutableColor;
import dev.sixik.unigui.api.style.StyleKeys;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.api.widget.visual.BackgroundKind;
import dev.sixik.unigui.impl.widget.WidgetBase;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.containers.PanelWidget;
import dev.sixik.unigui.widgets.containers.WrapPanel;
import dev.sixik.unigui.widgets.interaction.Button;
import dev.sixik.unigui.widgets.interaction.TextField;

public class ShopToolPanel implements ShopScreenElement {
    @Override
    public Widget initWidget(PanelWidget root) {
        HBox toolbar = new HBox();
        toolbar.layout(style -> style
                .flexDirection(FlexDirection.ROW)
                .alignItems(Align.CENTER)
                .padding(4.0f, 6.0f)
                .gap(6.0f)
                .height(32.0f)
        );

        toolbar.boxVisualEnabled(true);
        toolbar.style(style -> style
                .put(StyleKeys.BACKGROUND_VISIBLE, true)
                .put(StyleKeys.BACKGROUND_KIND, BackgroundKind.COLOR)
                .put(StyleKeys.BACKGROUND_COLOR, new MutableColor(1f, 1f, 1f, 1f))
        );

        Button editBtn = new Button("Edit");
        editBtn.layout(style -> style
                .height(22.0f)
                .padding(0.0f, 10.0f)
                .flexNone()
        );
        Button editBtn2 = new Button("Reset Session");
        editBtn2.layout(style -> style
                .height(22.0f)
                .padding(0.0f, 10.0f)
                .flexNone()
        );

        WrapPanel spacer = new WrapPanel();
        spacer.layout(LayoutStyle::expand);
        WrapPanel spacer2 = new WrapPanel();
        spacer2.layout(LayoutStyle::expand);

        HBox searchGroup = new HBox();
        searchGroup.layout(style -> style
                .flexDirection(FlexDirection.ROW)
                .alignItems(Align.CENTER)
                .gap(4.0f)
                .flexNone()
        );

        TextField searchInput = new TextField();
        searchInput.placeholder("Search offers...");
        searchInput.layout(style -> style
                .width(220.0f)
                .height(22.0f)
                .flexNone()
        );

        Button favBtn = new Button("★");
        favBtn.layout(style -> style
                .fixed(22.0f, 22.0f)
        );

        searchGroup.addChild(searchInput);
        searchGroup.addChild(favBtn);

        Button editBtn3 = new Button("Send Changes");
        editBtn3.layout(style -> style
                .height(22.0f)
                .padding(0.0f, 10.0f)
                .flexNone()
        );

        toolbar.addChild(editBtn);
        toolbar.addChild(editBtn2);
        toolbar.addChild(spacer);
        toolbar.addChild(searchGroup);
        toolbar.addChild(spacer2);
        toolbar.addChild(editBtn3);

        root.addChild(toolbar);
        return toolbar;
    }

    @Override
    public int priority() {
        return -1;
    }
}
