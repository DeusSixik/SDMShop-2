package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ContextMenuWidget;

import java.util.Collections;
import java.util.Map;

/**
 * Абстрактный интерфейс для реализации собственого рендера и поведения.
 */
public interface WidgetRender {

    /**
     * Вызывается в конструкторе класса
     */
    void constructor(WidgetContextRender ctx);

    /**
     * Вызывается на этапе добавления виджетов
     */
    void addWidgets(WidgetContextRender ctx);

    /**
     * Вызывается на этапе установки параметрова (размер, позиция и т.п)
     */
    void alightWidgets(WidgetContextRender ctx);

    default boolean mouseClicked(WidgetContextRender ctx, double mouseX, double mouseY, int button) {
        return false;
    }

    /**
     * Метод для добавления своих пунктов контекстного меню
     */
    default void addContext(ContextMenuWidget widget) { }
}
