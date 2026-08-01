package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

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

    default Map<CallbackType, Consumer<?>> getCallbacks() {
        return Collections.emptyMap();
    }

    enum CallbackType {
        /**
         * Доступен только в {@link ThemeApi.Category#Offers} <br>
         * Принимает {@link String} {@code groupId} в качестве аргумента
         */
        InvokeBuy
    }
}
