package dev.sixik.sdmshop2.libs.shop.client.ui.toast;

import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopScreenElement;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.feedback.Toast;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/** Notification facade backed by UniGUI Toast overlays. */
public final class ShopToasts {
    private static final int MAX_TOASTS = 5;
    private static final Deque<ShopToast> TOASTS = new ArrayDeque<>();
    private static final List<Toast> ACTIVE = new ArrayList<>();
    private static @Nullable OverlayLayer overlay;

    private ShopToasts() { }
    public static void info(String message) { info(Component.literal(message)); }
    public static void info(Component message) { show(ShopToast.Type.INFO, Component.literal("Info"), message); }
    public static void success(String message) { success(Component.literal(message)); }
    public static void success(Component message) { show(ShopToast.Type.SUCCESS, Component.literal("Success"), message); }
    public static void warning(String message) { warning(Component.literal(message)); }
    public static void warning(Component message) { show(ShopToast.Type.WARNING, Component.literal("Warning"), message); }
    public static void error(String message) { error(Component.literal(message)); }
    public static void error(Component message) { show(ShopToast.Type.ERROR, Component.literal("Error"), message); }
    public static void show(ShopToast.Type type, Component title, Component message) { show(type, title, message, ShopToast.DEFAULT_DURATION_MS); }

    public static void show(ShopToast.Type type, Component title, Component message, long durationMs) {
        ShopToast data = new ShopToast(type, title, message, durationMs);
        synchronized (TOASTS) {
            while (TOASTS.size() >= MAX_TOASTS) TOASTS.removeFirst();
            TOASTS.addLast(data);
        }
        attachToActiveScreen();
        if (overlay != null) {
            Toast toast = new Toast(title.getString() + ": " + message.getString()).duration(durationMs / 1000.0f);
            overlay.addOverlay(toast);
            ACTIVE.add(toast);
            toast.show();
        }
    }

    public static List<ShopToast> snapshot() {
        long now = System.currentTimeMillis();
        synchronized (TOASTS) { TOASTS.removeIf(toast -> toast.isExpired(now)); return new ArrayList<>(TOASTS); }
    }
    public static boolean hasToasts() { return !snapshot().isEmpty(); }
    public static void clear() {
        synchronized (TOASTS) { TOASTS.clear(); }
        for (Toast toast : List.copyOf(ACTIVE)) toast.hide();
        ACTIVE.clear();
    }
    public static void attachToActiveScreen() { if (ShopScreenElement.Instance != null) attachTo(ShopScreenElement.Instance); }
    public static void attachTo(Widget anchor) { overlay = findOverlay(anchor); }
    public static void bringOverlayToFront() { }

    private static @Nullable OverlayLayer findOverlay(Widget anchor) {
        Widget current = anchor;
        while (current != null) { if (current instanceof OverlayLayer layer) return layer; current = current.parent(); }
        if (anchor instanceof ShopScreenElement screen) return screen.getOverlay();
        return ShopScreenElement.Instance == null ? null : ShopScreenElement.Instance.getOverlay();
    }
}
