package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import dev.sixik.sdmshop2.libs.platform.utils.eventbus.DODEventBus;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventPtr;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventSubscription;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopScreenElement;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.PanelWidget;
import org.jetbrains.annotations.Nullable;

/** Context exposed to pluggable shop views. */
public interface WidgetContextRender {
    @Nullable ShopEntity getShopEntity();
    default @Nullable ShopEditSession getEditSession() { return null; }
    Widget getOwner();

    default void addWidget(Widget widget) {
        if (getOwner() instanceof PanelWidget panel) {
            panel.addChild(widget);
            return;
        }
        throw new IllegalStateException("The render owner is not a UniGUI container");
    }

    default void removeWidget(Widget widget) {
        if (getOwner() instanceof PanelWidget panel) panel.removeChild(widget);
    }

    default UIEventScope eventScope() { return UIEventScope.noop(); }

    default <Event> EventSubscription listen(EventPtr<Event> event, DODEventBus.EventListener<Event> listener) {
        return eventScope().listen(event, listener);
    }

    default UIEventScope screenEventScope() {
        Widget owner = getOwner();
        while (owner != null) {
            if (owner instanceof ShopScreenElement screen) return screen.screenEventScope();
            owner = owner.parent();
        }
        return ShopScreenElement.Instance == null ? eventScope() : ShopScreenElement.Instance.screenEventScope();
    }

    default <Event> EventSubscription listenScreen(EventPtr<Event> event, DODEventBus.EventListener<Event> listener) {
        return screenEventScope().listen(event, listener);
    }
}
