package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.ShopScreen;
import dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.ShopUiElement;
import dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.base.ShopDraggableScrollableWidgetGroup;
import dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.base.ShopWidgetGroup;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUIUtils;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.StyleApi;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIDisposable;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIEventScope;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetRender;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.DODEventBus;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventPtr;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventSubscription;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ShopTabsPanelElement extends ShopWidgetGroup implements
        ShopUiElement, WidgetContextRender, UIDisposable {

    @Getter
    @Setter
    public int widgetSpace = 4;

    @Getter
    protected final @NotNull ShopScreen shopScreen;

    protected final WidgetRender render;
    protected final UIEventScope eventScope = new UIEventScope();

    public ShopTabsPanelElement(@NotNull ShopScreen screen) {
        this(screen, StyleApi.getDefaultStyle(StyleApi.Category.TabsPanel).get());
    }

    public ShopTabsPanelElement(@NotNull ShopScreen screen, WidgetRender render) {
        this.shopScreen = screen;
        this.render = Objects.requireNonNull(render, "render");

        this.render.constructor(this);
        refresh(false);
    }

    @Override
    public void initWidget() {
        super.initWidget();
    }

    @Override
    public void alightWidget() {
        if (!initialized) return;
        render.alightWidgets(this);
    }

    @Override
    protected void alightWidgets() {
        alightWidget();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (render.mouseClicked(this, mouseX, mouseY, button)) {
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void setSize(Size size) {
        super.setSize(size);
        if (!getContainedWidgets(true).isEmpty()) {
            alightWidget();
        }
    }

    public ShopTabsPanelElement refresh() {
        return refresh(true);
    }

    public ShopTabsPanelElement refresh(boolean relayout) {
        eventScope.clear();
        ShopUIUtils.disposeChildren(this);
        clearAllWidgets();
        render.addWidgets(this);

        if (relayout) {
            alightWidget();
        }

        return this;
    }

    @Override
    public @Nullable ShopEntity getShopEntity() {
        return null;
    }

    @Override
    public Widget getOwner() {
        return this;
    }

    @Override
    public UIEventScope eventScope() {
        return eventScope;
    }

    @Override
    public UIEventScope screenEventScope() {
        return shopScreen.screenEventScope();
    }

    public <Event> EventSubscription listenScreen(EventPtr<Event> event, DODEventBus.EventListener<Event> listener) {
        return shopScreen.listenScreen(event, listener);
    }

    @Override
    public void dispose() {
        eventScope.close();
        ShopUIUtils.disposeChildren(this);
    }
}
