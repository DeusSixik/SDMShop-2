package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ContextMenuWidget;

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

    /**
     * Минимальный размер карточки, при котором рендер ещё должен выглядеть приемлемо.
     * Используется внешними layout-контейнерами для расчёта количества колонок.
     */
    default Size getMinimumSize(WidgetContextRender ctx) {
        return new Size(120, 90);
    }

    /**
     * Предпочтительный размер карточки. Layout старается приблизиться к нему,
     * но может уменьшить или увеличить карточку под доступное место.
     */
    default Size getPreferredSize(WidgetContextRender ctx) {
        return new Size(160, 120);
    }

    /**
     * Максимальный размер карточки. Нужен, чтобы карточки не становились слишком большими
     * на широких экранах, когда можно просто добавить больше колонок или центрировать сетку.
     */
    default Size getMaximumSize(WidgetContextRender ctx) {
        return new Size(240, 180);
    }

    /**
     * Предпочтительная высота для уже выбранной ширины карточки.
     * По умолчанию сохраняет пропорцию из {@link #getPreferredSize(WidgetContextRender)}
     * и зажимает результат между min/max высотой.
     */
    default int getPreferredHeight(WidgetContextRender ctx, int width) {
        Size min = getMinimumSize(ctx);
        Size preferred = getPreferredSize(ctx);
        Size max = getMaximumSize(ctx);

        int preferredWidth = Math.max(1, preferred.width);
        int height = Math.round(width * (preferred.height / (float) preferredWidth));
        int maxHeight = max.height <= 0 ? Integer.MAX_VALUE : max.height;
        return Math.max(min.height, Math.min(height, maxHeight));
    }

    default boolean mouseClicked(WidgetContextRender ctx, double mouseX, double mouseY, int button) {
        return false;
    }

    /**
     * Метод для добавления своих пунктов контекстного меню
     */
    default void addContext(ContextMenuWidget widget) { }
}
