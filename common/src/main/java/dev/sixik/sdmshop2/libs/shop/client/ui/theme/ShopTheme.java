package dev.sixik.sdmshop2.libs.shop.client.ui.theme;

import dev.sixik.unigui.api.math.MutableColor;

/** Цветовые токены, скругления и размеры темы "Obsidian & Gold" для SDMShop-2. */
public final class ShopTheme {

    private ShopTheme() {}

    // --- Фоновые цвета ---
    public static final int BG_SCREEN = 0xF50B0D12;
    public static final int BG_SURFACE = 0xFF13161F;
    public static final int BG_CARD = 0xFF1A1E29;
    public static final int BG_CARD_HOVER = 0xFF222736;
    public static final int BG_SLOT = 0xFF11131B;
    public static final int BG_MODAL = 0xFF141722;
    public static final int BG_INPUT = 0xFF0D0F16;

    // --- Границы и разделители ---
    public static final int BORDER_SUBTLE = 0xFF282E3E;
    public static final int BORDER_HOVER = 0xFF3D465E;
    public static final int BORDER_SLOT = 0xFF333B50;
    public static final int BORDER_SEPARATOR = 0xFF232837;

    // --- Акцентные цвета (Золото) ---
    public static final int GOLD_PRIMARY = 0xFFD4AF37;
    public static final int GOLD_HOVER = 0xFFF6CF63;
    public static final int GOLD_MUTED = 0xFF7A6428;
    public static final int GOLD_ACTIVE = 0xFFFFD700;

    // --- Статусные цвета и бейджи ---
    public static final int DISCOUNT_BADGE = 0xFFE11D48;
    public static final int BUNDLE_BADGE = 0xFF6366F1;
    public static final int SUCCESS = 0xFF10B981;
    public static final int DANGER = 0xFFEF4444;

    // --- Текстовые цвета ---
    public static final int TEXT_PRIMARY = 0xFFF1F5F9;
    public static final int TEXT_SECONDARY = 0xFF94A3B8;
    public static final int TEXT_MUTED = 0xFF64748B;
    public static final int TEXT_GOLD = 0xFFFCD34D;

    // --- Скругления (Border Radius) ---
    public static final float RADIUS_CARD = 4.0f;
    public static final float RADIUS_PANEL = 4.0f;
    public static final float RADIUS_SLOT = 3.0f;
    public static final float RADIUS_BUTTON = 3.0f;
    public static final float RADIUS_BADGE = 3.0f;
    public static final float RADIUS_MODAL = 6.0f;

    // --- Размеры рамок ---
    public static final float BORDER_WIDTH_THIN = 1.0f;
    public static final float BORDER_WIDTH_ACCENT = 1.5f;

    public static MutableColor color(int argb) {
        return MutableColor.fromArgb(argb);
    }
}
