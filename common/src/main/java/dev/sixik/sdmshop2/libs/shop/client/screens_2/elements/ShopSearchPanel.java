package dev.sixik.sdmshop2.libs.shop.client.screens_2.elements;

import dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.base.ShopWidgetGroup;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.InputTextBox;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class ShopSearchPanel extends ShopWidgetGroup implements ShopUiElement {

    @Getter
    protected final @NotNull ShopScreen shopScreen;
    protected InputTextBox searchBox;

    public ShopSearchPanel(@NotNull ShopScreen screen) {
        this.shopScreen = screen;
    }

    @Override
    public void initWidget() {
        addWidget(searchBox = new InputTextBox());
        super.initWidget();
    }

    @Override
    protected void alightWidgets() {
        searchBox.setSize(100, 20);
    }
}
