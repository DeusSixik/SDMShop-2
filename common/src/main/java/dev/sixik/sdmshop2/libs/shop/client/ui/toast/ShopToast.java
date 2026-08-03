package dev.sixik.sdmshop2.libs.shop.client.ui.toast;

import net.minecraft.network.chat.Component;

public final class ShopToast {

    public static final long DEFAULT_DURATION_MS = 4_000L;

    private final Type type;
    private final Component title;
    private final Component message;
    private final long createdAtMs;
    private final long durationMs;

    public ShopToast(Type type, Component title, Component message, long durationMs) {
        this.type = type == null ? Type.INFO : type;
        this.title = title == null ? Component.empty() : title;
        this.message = message == null ? Component.empty() : message;
        this.createdAtMs = System.currentTimeMillis();
        this.durationMs = Math.max(500L, durationMs);
    }

    public boolean isExpired(long nowMs) {
        return nowMs - createdAtMs >= durationMs;
    }

    public float progress(long nowMs) {
        return Math.min(1.0f, Math.max(0.0f, (nowMs - createdAtMs) / (float) durationMs));
    }

    public Type getType() {
        return type;
    }

    public Component getTitle() {
        return title;
    }

    public Component getMessage() {
        return message;
    }

    public long getCreatedAtMs() {
        return createdAtMs;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public enum Type {
        INFO(0xFF2F6FED, 0xFF10203D),
        SUCCESS(0xFF28A745, 0xFF102818),
        WARNING(0xFFFFB020, 0xFF33250A),
        ERROR(0xFFE55353, 0xFF331316);

        private final int accentColor;
        private final int backgroundColor;

        Type(int accentColor, int backgroundColor) {
            this.accentColor = accentColor;
            this.backgroundColor = backgroundColor;
        }

        public int getAccentColor() {
            return accentColor;
        }

        public int getBackgroundColor() {
            return backgroundColor;
        }
    }
}

