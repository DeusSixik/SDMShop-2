package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.PanelWidget;

/** Screen contribution created once per window and ordered by ascending priority. */
public interface ShopScreenElement {
    /**
     * Creates and attaches this element's widget.
     *
     * @param root parent owned by the window.
     * @return created widget.
     */
    Widget initWidget(PanelWidget root);

    /**
     * Mounts a contribution with screen-local actions.
     *
     * @param root parent owned by the window.
     * @param context actions and modal host belonging to this window.
     * @return created widget.
     */
    default Widget initWidget(PanelWidget root, ShopUiContext context) {
        return initWidget(root);
    }

    /** Releases subscriptions and overlays before disposal; must be safe after partial mounting. */
    default void dispose() {}

    /**
     * @return mounting order; ties preserve registration order.
     */
    default int priority() {
        return 0;
    }
}
