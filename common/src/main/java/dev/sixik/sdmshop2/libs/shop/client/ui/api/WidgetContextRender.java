package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.DODEventBus;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventPtr;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventSubscription;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopScreenElement;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import org.jetbrains.annotations.Nullable;

/**
 * Контекст для тем и отрисовки.
 */
public interface WidgetContextRender {

    /**
     * Возвращает {@link ShopEntity} который используется в элементе. Так же он может быть {@code null} это может означать
     * что это {@code Admin} элемент.
     */
    @Nullable
    ShopEntity getShopEntity();

    @Nullable
    default ShopEditSession getEditSession() {
        return null;
    }

    /**
     * Виджет у которого вызываеться этот контекст
     */
    Widget getOwner();

    /**
     * Метод для добавления собственного виджета
     */
    WidgetGroup addWidget(Widget widget);

    /**
     * Метод для удаления собственного виджета
     */
    void removeWidget(Widget widget);

    default UIEventScope eventScope() {
        return UIEventScope.noop();
    }

    default <Event> EventSubscription listen(EventPtr<Event> event, DODEventBus.EventListener<Event> listener) {
        return eventScope().listen(event, listener);
    }

    default UIEventScope screenEventScope() {
        Widget owner = getOwner();
        while (owner != null) {
            if (owner instanceof ShopScreenElement screen) {
                return screen.screenEventScope();
            }
            owner = owner.getParent();
        }

        if (ShopScreenElement.Instance != null) {
            return ShopScreenElement.Instance.screenEventScope();
        }

        return eventScope();
    }

    default <Event> EventSubscription listenScreen(EventPtr<Event> event, DODEventBus.EventListener<Event> listener) {
        return screenEventScope().listen(event, listener);
    }
}
