package dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.entities;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.ShopUiElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUIUtils;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ThemeApi;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ContextMenuWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ModalWidget;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ShopOfferElement extends WidgetGroup implements ShopUiElement, WidgetContextRender {

    @Nullable
    private final ShopOffer shopEntity;

    protected final WidgetRender render;

    public ShopOfferElement(@Nullable ShopOffer shopEntity) {
        this(shopEntity, ThemeApi.getDefaultTheme(ThemeApi.Category.Offers).get());
    }

    public ShopOfferElement(@Nullable ShopOffer shopEntity, WidgetRender render) {
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

        if (button != 1) {
            return super.mouseClicked(mouseX, mouseY, button);
        }

        if(!isMouseOverElement(mouseX, mouseY)) {
            return false;
        }

        ContextMenuWidget menuWidget = new ContextMenuWidget((int) mouseX, (int) mouseY, 120);
        menuWidget.setScale(0.7f);

        addDefaultContextMenu(menuWidget);
        render.addContext(menuWidget);

        if(!menuWidget.getContainedWidgets(true).isEmpty())
            ContextMenuWidget.open(this, menuWidget);

        return true;
    }

    protected void addDefaultContextMenu(ContextMenuWidget menuWidget) {
        menuWidget
                .addItem(Component.translatable("shop.ui.offer_element.context_menu.edit"), () -> {
                    ShopUIUtils.createEditMenu(this, shopEntity, () -> {});
                }).addSeparator()
                .addItem(Component.translatable("shop.ui.offer_element.context_menu.copy"), () -> {

                }).addSeparator()
                .addItem(Component.translatable("shop.ui.offer_element.context_menu.delete"), () -> {

                });
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
