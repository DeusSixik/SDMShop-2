package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopEntityEditorElement;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ModalWidget;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ShopUIUtils {

    public static ModalWidget createEditMenu(Widget owner, ShopEntity editable, Runnable onEdit) {
       return ShopEntityEditorElement.open(owner, editable, onEdit);
    }

    public static ModalWidget createEditMenu(Widget owner, ShopEntity editable, Runnable onEdit, boolean allowComponentDelete) {
       return ShopEntityEditorElement.open(owner, editable, onEdit, allowComponentDelete);
    }

    public static ModalWidget createEditMenu(Widget owner, @Nullable ShopEditSession editSession, ShopEntity editable, Runnable onEdit) {
       return ShopEntityEditorElement.open(owner, editSession, editable, onEdit);
    }

    public static ModalWidget createEditMenu(Widget owner, @Nullable ShopEditSession editSession, ShopEntity editable, Runnable onEdit, boolean allowComponentDelete) {
       return ShopEntityEditorElement.open(owner, editSession, editable, onEdit, allowComponentDelete);
    }

    public static void disposeTree(Widget widget) {
        if (widget == null) {
            return;
        }

        if (widget instanceof UIDisposable disposable) {
            disposable.dispose();
            return;
        }

        if (widget instanceof WidgetGroup group) {
            disposeChildren(group);
        }
    }

    public static void disposeChildren(WidgetGroup group) {
        if (group == null || group.widgets.isEmpty()) {
            return;
        }

        for (Widget child : List.copyOf(group.widgets)) {
            disposeTree(child);
        }
    }
}
