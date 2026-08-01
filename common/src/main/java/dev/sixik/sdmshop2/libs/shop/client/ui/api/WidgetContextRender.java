package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ContextMenuWidget;
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
}
