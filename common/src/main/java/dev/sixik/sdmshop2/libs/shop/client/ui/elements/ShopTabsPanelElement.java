package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUiElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.base.ShopWidgetGroup;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUIUtils;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.StyleApi;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIDisposable;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIEventScope;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop.client.ui.style.DefaultShopTabsPanelRender;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
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
    protected final @NotNull ShopScreenElement shopScreen;

    protected final WidgetRender render;
    protected final UIEventScope eventScope = new UIEventScope();
    protected boolean defaultHandlersRegistered;
    @Getter
    protected @Nullable CatalogComponent selectedCategory;

    public ShopTabsPanelElement(@NotNull ShopScreenElement screen) {
        this(screen, StyleApi.getDefaultStyle(StyleApi.Category.TabsPanel).get());
    }

    public ShopTabsPanelElement(@NotNull ShopScreenElement screen, WidgetRender render) {
        this.shopScreen = screen;
        this.render = Objects.requireNonNull(render, "render");

        this.render.constructor(this);
        registerDefaultHandlers();
        refresh(false);
    }

    protected void registerDefaultHandlers() {
        if (defaultHandlersRegistered) {
            return;
        }

        defaultHandlersRegistered = true;
        listenScreen(ShopUIEvents.REFRESH_UI, event -> {
            if (event.affectsCategories()) {
                refresh(initialized && event.relayout());
                return;
            }

            if (event.affectsCurrencies()) {
                if (render instanceof DefaultShopTabsPanelRender tabsRender && tabsRender.refreshCurrencies()) {
                    return;
                }

                refresh(initialized && event.relayout());
            }
        });
        listenScreen(ShopUIEvents.SELECT_CATEGORY, event -> {
            if (!setSelectedCategory(event.selected())) {
                return;
            }

            if (render instanceof DefaultShopTabsPanelRender tabsRender) {
                tabsRender.updateSelectedTabs(this);
            } else {
                refresh(initialized);
            }
        });
    }

    public boolean setSelectedCategory(@Nullable CatalogComponent selectedCategory) {
        if (sameCategory(this.selectedCategory, selectedCategory)) {
            return false;
        }

        this.selectedCategory = selectedCategory;
        return true;
    }

    public boolean isSelectedCategory(@Nullable CatalogComponent category) {
        return sameCategory(selectedCategory, category);
    }

    protected static boolean sameCategory(@Nullable CatalogComponent first, @Nullable CatalogComponent second) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        if (first.getUuid() != null && second.getUuid() != null) {
            return first.getUuid().equals(second.getUuid());
        }

        return Objects.equals(first.getId(), second.getId());
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
