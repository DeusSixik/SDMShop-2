package dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.entities;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.ShopUiElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.OfferElementContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.OfferElementRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ThemeApi;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ShopOfferElement extends WidgetGroup implements ShopUiElement, OfferElementContextRender {

    @Nullable
    private final ShopOffer shopEntity;

    protected final OfferElementRender render;

    public ShopOfferElement(@Nullable ShopOffer shopEntity) {
        this(shopEntity, ThemeApi.getDefaultTheme(ThemeApi.Category.Offers).get());
    }

    public ShopOfferElement(@Nullable ShopOffer shopEntity, OfferElementRender render) {
        this.shopEntity = shopEntity;
        this.render = Objects.requireNonNull(render, "render");
        this.render.constructor(this);
        this.render.addWidgets(this);
    }

    @Override
    public void alightWidget() {
        render.alightWidgets(this);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (render.mouseClicked(this, mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    @Nullable
    public ShopOffer getShopEntity() {
        return shopEntity;
    }

    @Override
    public Widget getOwner() {
        return this;
    }
}
