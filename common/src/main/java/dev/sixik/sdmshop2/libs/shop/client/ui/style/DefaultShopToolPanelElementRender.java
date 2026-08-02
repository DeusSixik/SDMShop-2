package dev.sixik.sdmshop2.libs.shop.client.ui.style;

import dev.sixik.sdmshop2.libs.shop.client.textures.PixelBevelTexture;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetRender;

public class DefaultShopToolPanelElementRender implements WidgetRender {

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

    }

    @Override
    public void alightWidgets(WidgetContextRender ctx) {

    }
}
