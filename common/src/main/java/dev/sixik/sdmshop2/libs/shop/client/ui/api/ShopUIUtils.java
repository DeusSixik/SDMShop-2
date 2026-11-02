package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopEntityEditorElement;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.unigui.api.widget.Widget;
import org.jetbrains.annotations.Nullable;

public final class ShopUIUtils {
    private ShopUIUtils() { }

    public static ShopEntityEditorElement createEditMenu(Widget owner, ShopEntity editable, Runnable onEdit) {
        return ShopEntityEditorElement.open(owner, editable, onEdit);
    }
    public static ShopEntityEditorElement createEditMenu(Widget owner, ShopEntity editable, Runnable onEdit, boolean allowDelete) {
        return ShopEntityEditorElement.open(owner, editable, onEdit, allowDelete);
    }
    public static ShopEntityEditorElement createEditMenu(Widget owner, @Nullable ShopEditSession session, ShopEntity editable, Runnable onEdit) {
        return ShopEntityEditorElement.open(owner, session, editable, onEdit);
    }
    public static ShopEntityEditorElement createEditMenu(Widget owner, @Nullable ShopEditSession session, ShopEntity editable, Runnable onEdit, boolean allowDelete) {
        return ShopEntityEditorElement.open(owner, session, editable, onEdit, allowDelete);
    }

    public static void disposeTree(@Nullable Widget widget) { if (widget != null) widget.dispose(); }
    public static void disposeChildren(@Nullable Widget widget) {
        if (widget == null) return;
        for (Widget child : java.util.List.copyOf(widget.children())) child.dispose();
    }
}
