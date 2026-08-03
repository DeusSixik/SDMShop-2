package dev.sixik.sdmshop2.libs.shop.client.ui.toast;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopScreenElement;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public final class ShopToasts {

    private static final int MAX_TOASTS = 5;
    private static final Deque<ShopToast> TOASTS = new ArrayDeque<>();

    @Nullable
    private static ShopToastOverlayElement overlay;

    public static void info(String message) {
        info(Component.literal(message));
    }

    public static void info(Component message) {
        show(ShopToast.Type.INFO, Component.literal("Info"), message);
    }

    public static void success(String message) {
        success(Component.literal(message));
    }

    public static void success(Component message) {
        show(ShopToast.Type.SUCCESS, Component.literal("Success"), message);
    }

    public static void warning(String message) {
        warning(Component.literal(message));
    }

    public static void warning(Component message) {
        show(ShopToast.Type.WARNING, Component.literal("Warning"), message);
    }

    public static void error(String message) {
        error(Component.literal(message));
    }

    public static void error(Component message) {
        show(ShopToast.Type.ERROR, Component.literal("Error"), message);
    }

    public static void show(ShopToast.Type type, Component title, Component message) {
        show(type, title, message, ShopToast.DEFAULT_DURATION_MS);
    }

    public static void show(ShopToast.Type type, Component title, Component message, long durationMs) {
        synchronized (TOASTS) {
            while (TOASTS.size() >= MAX_TOASTS) {
                TOASTS.removeFirst();
            }

            TOASTS.addLast(new ShopToast(type, title, message, durationMs));
        }

        attachToActiveScreen();
    }

    public static List<ShopToast> snapshot() {
        long nowMs = System.currentTimeMillis();
        synchronized (TOASTS) {
            TOASTS.removeIf(toast -> toast.isExpired(nowMs));
            return new ArrayList<>(TOASTS);
        }
    }

    public static boolean hasToasts() {
        return !snapshot().isEmpty();
    }

    public static void clear() {
        synchronized (TOASTS) {
            TOASTS.clear();
        }
    }

    public static void attachToActiveScreen() {
        if (ShopScreenElement.Instance != null) {
            attachTo(ShopScreenElement.Instance);
        }
    }

    public static void attachTo(Widget anchor) {
        WidgetGroup root = findRootGroup(anchor);
        if (root == null) {
            return;
        }

        if (overlay == null) {
            overlay = new ShopToastOverlayElement();
            overlay.setClientSideWidget();
        }

        if (overlay.getParent() != root) {
            WidgetGroup oldParent = overlay.getParent();
            if (oldParent != null) {
                oldParent.removeWidget(overlay);
            }

            root.addWidget(overlay);
        } else {
            bringToFront(root, overlay);
        }

        overlay.syncToRoot(root);
    }

    public static void bringOverlayToFront() {
        if (overlay == null || overlay.getParent() == null) {
            return;
        }

        bringToFront(overlay.getParent(), overlay);
    }

    private static void bringToFront(WidgetGroup parent, Widget widget) {
        if (parent.widgets.isEmpty() || parent.widgets.get(parent.widgets.size() - 1) == widget) {
            return;
        }

        parent.removeWidget(widget);
        parent.addWidget(widget);
    }

    @Nullable
    private static WidgetGroup findRootGroup(Widget anchor) {
        if (anchor == null) {
            return null;
        }

        Widget root = anchor;
        while (root.getParent() != null) {
            root = root.getParent();
        }

        return root instanceof WidgetGroup group ? group : null;
    }

    private ShopToasts() {
    }
}

