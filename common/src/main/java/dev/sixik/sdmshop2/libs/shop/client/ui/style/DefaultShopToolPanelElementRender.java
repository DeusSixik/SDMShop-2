package dev.sixik.sdmshop2.libs.shop.client.ui.style;

import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.shop.client.ui.textures.PixelBevelTexture;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.InputTextBox;

public class DefaultShopToolPanelElementRender implements WidgetRender {

    protected InputTextBox searchBox;

    public DefaultShopToolPanelElementRender() {

    }


    @Override
    public void constructor(WidgetContextRender ctx) {
        ctx.getOwner().setBackground(new PixelBevelTexture(
                PixelBevelTexture.PANEL_COLOR,
                PixelBevelTexture.LINE_LOW_COLOR,
                PixelBevelTexture.LINE_HIGH_COLOR,
                1.5f
        ));
    }

    @Override
    public void addWidgets(WidgetContextRender ctx) {
        ctx.addWidget(searchBox = new InputTextBox().setTextResponder(ShopUIEvents::invokeSearchUpdate));
    }

    @Override
    public void alightWidgets(WidgetContextRender ctx) {
        final Size size = ctx.getOwner().getSize();
        final int w_root = size.width;
        final int h_root = size.height;

        searchBox.setSizeWidth(w_root / 4);
        searchBox.setSelfPosition(w_root / 2 - (searchBox.getSizeWidth() / 2), h_root / 2 - searchBox.getSizeHeight() / 2);
    }
}
