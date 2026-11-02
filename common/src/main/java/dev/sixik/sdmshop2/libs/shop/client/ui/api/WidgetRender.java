package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import dev.sixik.unigui.api.layout.LayoutSize;
import dev.sixik.unigui.widgets.feedback.ContextMenu;

/** Shop view strategy expressed solely in UniGUI types. */
public interface WidgetRender {
    default void constructor(WidgetContextRender ctx) { }
    default void addWidgets(WidgetContextRender ctx) { }
    default void alightWidgets(WidgetContextRender ctx) { }
    default LayoutSize getMinimumSize(WidgetContextRender ctx) { return LayoutSize.of(120, 90); }
    default LayoutSize getPreferredSize(WidgetContextRender ctx) { return LayoutSize.of(160, 120); }
    default LayoutSize getMaximumSize(WidgetContextRender ctx) { return LayoutSize.of(240, 180); }

    default int getPreferredHeight(WidgetContextRender ctx, int width) {
        LayoutSize min = getMinimumSize(ctx);
        LayoutSize preferred = getPreferredSize(ctx);
        LayoutSize max = getMaximumSize(ctx);
        int height = Math.round(width * preferred.height() / Math.max(1.0f, preferred.width()));
        int maxHeight = max.height() <= 0 ? Integer.MAX_VALUE : Math.round(max.height());
        return Math.max(Math.round(min.height()), Math.min(height, maxHeight));
    }

    default void addContext(ContextMenu menu) { }
}
