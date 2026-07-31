package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.ShopUiElement;
import dev.sixik.sdmshop2.libs.shop.client.textures.ColorRectAndBorderTexture;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractOfferElementWidget extends WidgetGroup implements ShopUiElement {

    @Nullable
    protected final ShopOffer shopOffer;

    public AbstractOfferElementWidget(@Nullable ShopOffer shopOffer) {
        this.shopOffer = shopOffer;

        addWidgets();
    }

    protected void initializeBackground() {
        setBackground(new ColorRectAndBorderTexture());
    }

    protected abstract void addWidgets();
}
