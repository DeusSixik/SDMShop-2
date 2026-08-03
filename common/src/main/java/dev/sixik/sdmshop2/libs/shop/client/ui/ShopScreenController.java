package dev.sixik.sdmshop2.libs.shop.client.ui;

import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopScreenElement;
import dev.sixik.sdmshop2.utils.ShopUtils;

public final class ShopScreenController {

    public static void openShop() {
        ShopUtils.openWidget(defaultGui());
    }

    private static WidgetGroup defaultGui() {
        return new ShopScreenElement();
    }
}
