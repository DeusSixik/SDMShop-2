package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopEntityEditorElement;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ModalWidget;

public final class ShopUIUtils {

    public static ModalWidget createEditMenu(Widget owner, ShopEntity editable, Runnable onEdit) {
       return ShopEntityEditorElement.open(owner, editable, onEdit);
    }
}
