package dev.sixik.sdmshop2.libs.shop.client.ui.theme;

import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopEditorStyles.*;
import dev.sixik.unigui.api.math.ColorView;
import dev.sixik.unigui.api.math.MutableColor;
import dev.sixik.unigui.api.math.RectView;
import dev.sixik.unigui.api.render.Paint;
import dev.sixik.unigui.api.text.RichText;
import dev.sixik.unigui.api.widget.render.WidgetRender;
import dev.sixik.unigui.impl.text.TextEngine;
import dev.sixik.unigui.widgets.interaction.Button;
import dev.sixik.unigui.widgets.interaction.Checkbox;
import dev.sixik.unigui.widgets.interaction.ToggleButton;

/** Custom draw callbacks shared by editor control styles. */
final class ShopEditorRenderers extends ShopEditorTheme {
    /** Prevents instantiation. */
    private ShopEditorRenderers() {}

    public static final WidgetRender CHECKBOX_RENDERER =
            WidgetRender.of(
                    Checkbox.class,
                    (draw, cb) -> {
                        RectView bounds = cb.layoutBounds();
                        float x = bounds.x();
                        float y = bounds.y();
                        float width = bounds.width();
                        float height = bounds.height();

                        float boxSize = cb.boxSize();
                        float indicatorX = x + Math.max(0.0f, width - boxSize) * 0.5f;
                        float indicatorY = y + Math.max(0.0f, height - boxSize) * 0.5f;

                        // Фон чекбокса: Unity тёмно-серый
                        draw.roundedRectFilled(
                                indicatorX, indicatorY, boxSize, boxSize, 2.0f, BG_INPUT);

                        // Тонкая рамка (1px): синяя при нажатии, белая при наведении, одинаково
                        // тёмная в покое (Checked и Unchecked)
                        ColorView borderCol;
                        if (!cb.enabled()) {
                            borderCol = BORDER_INPUT;
                        } else if (cb.pressed()) {
                            borderCol = BORDER_INPUT_FOCUS;
                        } else if (cb.hovered()) {
                            borderCol = TEXT_PRIMARY;
                        } else {
                            borderCol = BORDER_INPUT;
                        }
                        draw.roundedRectStroke(
                                indicatorX, indicatorY, boxSize, boxSize, 2.0f, borderCol, 1.0f);

                        // Галочка (✓) в стиле Unity (изящная тонкая линия 1.15px, острый угол)
                        if (cb.checked()) {
                            float ix = indicatorX;
                            float iy = indicatorY;
                            ColorView markCol = cb.enabled() ? TEXT_PRIMARY : TEXT_MUTED;
                            draw.pathClear();
                            draw.pathLineTo(ix + 2.8f, iy + 5.8f);
                            draw.pathLineTo(ix + 4.8f, iy + 8.2f);
                            draw.pathLineTo(ix + 9.2f, iy + 3.2f);
                            draw.pathStroke(markCol, false, 1.15f);
                        } else if (cb.indeterminate()) {
                            float dashW = boxSize * 0.5f;
                            float dashH = 2.0f;
                            ColorView markCol = cb.enabled() ? TEXT_PRIMARY : TEXT_MUTED;
                            draw.rectFilled(
                                    indicatorX + (boxSize - dashW) * 0.5f,
                                    indicatorY + (boxSize - dashH) * 0.5f,
                                    dashW,
                                    dashH,
                                    markCol);
                        }

                        // Текст чекбокса (если есть)
                        String text = cb.text();
                        if (text != null && !text.isEmpty()) {
                            float textX = indicatorX + boxSize + cb.textGap();
                            float textY = y + (height - 10.0f) * 0.5f;
                            draw.text(
                                    text,
                                    textX,
                                    textY,
                                    Math.max(0.0f, width - (textX - x)),
                                    height,
                                    Paint.fill(TEXT_PRIMARY));
                        }
                    });

    public static final WidgetRender TARGET_PICKER_RENDERER =
            WidgetRender.of(
                    Button.class,
                    (draw, btn) -> {
                        RectView b = btn.layoutBounds();
                        float x = b.x();
                        float y = b.y();
                        float w = b.width();
                        float h = b.height();

                        ColorView bg =
                                btn.pressed()
                                        ? BG_WORKSPACE
                                        : btn.hovered() ? BG_BUTTON_HOVER : BG_INPUT;
                        draw.roundedRectFilled(x, y, w, h, 2.0f, bg);

                        ColorView borderCol =
                                btn.pressed()
                                        ? BORDER_INPUT_FOCUS
                                        : btn.hovered() ? TEXT_PRIMARY : BORDER_INPUT;
                        draw.roundedRectStroke(x, y, w, h, 2.0f, borderCol, 1.0f);

                        float cx = x + w * 0.5f;
                        float cy = y + h * 0.5f;
                        float r = 4.2f;
                        ColorView iconCol = btn.hovered() ? TEXT_PRIMARY : TEXT_LABEL;
                        draw.circleStroke(cx - r, cy - r, r * 2.0f, r * 2.0f, iconCol, 1.0f);
                        draw.circleFilled(cx - 1.2f, cy - 1.2f, 2.4f, 2.4f, iconCol);
                    });

    public static final WidgetRender EYEDROPPER_RENDERER =
            WidgetRender.of(
                    Button.class,
                    (draw, btn) -> {
                        RectView b = btn.layoutBounds();
                        float x = b.x();
                        float y = b.y();
                        float w = b.width();
                        float h = b.height();

                        ColorView bg =
                                btn.pressed()
                                        ? BG_WORKSPACE
                                        : btn.hovered() ? BG_BUTTON_HOVER : BG_INPUT;
                        draw.roundedRectFilled(x, y, w, h, 2.0f, bg);

                        ColorView borderCol =
                                btn.pressed()
                                        ? BORDER_INPUT_FOCUS
                                        : btn.hovered() ? TEXT_PRIMARY : BORDER_INPUT;
                        draw.roundedRectStroke(x, y, w, h, 2.0f, borderCol, 1.0f);

                        float cx = x + w * 0.5f;
                        float cy = y + h * 0.5f;
                        ColorView col = btn.hovered() ? TEXT_PRIMARY : TEXT_LABEL;
                        draw.pathClear();
                        draw.pathLineTo(cx + 3.2f, cy - 3.2f);
                        draw.pathLineTo(cx - 0.8f, cy + 0.8f);
                        draw.pathLineTo(cx - 3.2f, cy + 3.2f);
                        draw.pathStroke(col, false, 1.4f);
                    });

    public static final WidgetRender DROPDOWN_OPTION_RENDERER =
            WidgetRender.of(
                    ToggleButton.class,
                    (draw, btn) -> {
                        RectView bounds = btn.layoutBounds();
                        float x = bounds.x();
                        float y = bounds.y();
                        float width = Math.max(0.0f, bounds.width());
                        float height = Math.max(0.0f, bounds.height());
                        if (width <= 0.0f || height <= 0.0f) return;

                        // Фон опции в палитре Unity Dark
                        ColorView bg;
                        if (btn.pressed()) {
                            bg = BG_DROPDOWN_ITEM_PRESS;
                        } else if (btn.hovered()) {
                            bg = BG_DROPDOWN_ITEM_HOVER;
                        } else if (btn.checked()) {
                            bg = BG_DROPDOWN_ITEM_CHECKED;
                        } else {
                            bg = BG_DROPDOWN_ITEM_NORMAL;
                        }
                        draw.rectFilled(x, y, width, height, bg);

                        // Тонкая рамка при наведении в стиле Unity Focus (1px)
                        if (btn.hovered() && btn.enabled()) {
                            draw.rectStroke(x, y, width, height, BORDER_DROPDOWN_ITEM_HOVER, 1.0f);
                        }

                        // Белый текст опции (без uppercase, сохраняя оригинальный регистр строк)
                        String plain = btn.text();
                        RichText rich = btn.richText();
                        float textX = x + 7.0f;
                        float textWidth = Math.max(0.0f, width - 14.0f);
                        if (textWidth > 0.0f) {
                            ColorView textCol =
                                    (btn.hovered() || btn.checked() || btn.pressed())
                                            ? TEXT_BOLD
                                            : TEXT_PRIMARY;
                            float textHeight =
                                    rich != null && !rich.isEmpty()
                                            ? TextEngine.measureTextHeight(rich)
                                            : 9.0f;
                            float drawY = y + Math.max(0.0f, height - textHeight) * 0.5f;
                            draw.pushClip(textX, y, textWidth, height);
                            try {
                                if (rich != null && !rich.isEmpty()) {
                                    draw.text(
                                            rich,
                                            textX,
                                            drawY,
                                            textWidth,
                                            textHeight,
                                            Paint.fill(textCol));
                                } else if (plain != null && !plain.isEmpty()) {
                                    draw.text(
                                            plain,
                                            textX,
                                            drawY,
                                            textWidth,
                                            textHeight,
                                            Paint.fill(textCol));
                                }
                            } finally {
                                draw.popClip();
                            }
                        }
                    });

    public static final WidgetRender CONTEXT_MENU_ITEM_RENDERER =
            WidgetRender.of(
                    Button.class,
                    (draw, btn) -> {
                        RectView b = btn.layoutBounds();
                        float x = b.x();
                        float y = b.y();
                        float w = b.width();
                        float h = b.height();
                        if (w <= 0.0f || h <= 0.0f) return;

                        boolean enabled = btn.enabled();
                        boolean hovered = btn.hovered() && enabled;
                        boolean pressed = btn.pressed() && enabled;

                        if (pressed) {
                            draw.roundedRectFilled(x, y, w, h, 2.0f, BG_DROPDOWN_ITEM_PRESS);
                        } else if (hovered) {
                            draw.roundedRectFilled(x, y, w, h, 2.0f, SELECTION_BLUE);
                        }

                        ColorView textColor;
                        if (!enabled) {
                            textColor = TEXT_MUTED;
                        } else if (hovered || pressed) {
                            textColor = TEXT_BOLD;
                        } else {
                            textColor = TEXT_PRIMARY;
                        }

                        RichText rt = RichText.plain(btn.text());
                        float padX = 14.0f;

                        String shortcut =
                                (btn instanceof UnityContextMenuItem item) ? item.shortcut() : null;
                        float shortcutWidth = 0.0f;
                        if (shortcut != null && !shortcut.isEmpty()) {
                            RichText srt = RichText.plain(shortcut);
                            shortcutWidth = TextEngine.measureLineWidth(draw.context(), srt) + 8.0f;
                            float sHeight = TextEngine.measureTextHeight(draw.context(), srt);
                            float sX = x + w - padX - (shortcutWidth - 8.0f);
                            float sY = y + Math.max(0.0f, h - sHeight) * 0.5f;
                            ColorView sColor =
                                    !enabled
                                            ? TEXT_MUTED
                                            : (hovered || pressed ? TEXT_BOLD : TEXT_MUTED);
                            TextEngine.drawInline(
                                    draw,
                                    srt,
                                    sX,
                                    sY,
                                    shortcutWidth - 8.0f,
                                    sHeight,
                                    Paint.fill(sColor));
                        }

                        boolean isCheckable =
                                (btn instanceof UnityContextMenuItem uItem) && uItem.isCheckable();
                        boolean isChecked = isCheckable && ((UnityContextMenuItem) btn).isChecked();

                        if (isChecked) {
                            float checkX = x + 4.0f;
                            float checkY = y + h * 0.5f;
                            ColorView checkColor =
                                    !enabled
                                            ? TEXT_MUTED
                                            : (hovered || pressed ? TEXT_BOLD : TEXT_PRIMARY);
                            draw.pathClear();
                            draw.pathLineTo(checkX + 2.0f, checkY - 0.5f);
                            draw.pathLineTo(checkX + 5.0f, checkY + 2.8f);
                            draw.pathLineTo(checkX + 10.5f, checkY - 3.8f);
                            draw.pathStroke(checkColor, false, 1.5f);
                        }

                        float textStartX = x + (isCheckable ? 20.0f : padX);
                        float contentWidth =
                                Math.max(
                                        0.0f,
                                        w
                                                - (isCheckable ? (20.0f + padX) : (padX * 2.0f))
                                                - shortcutWidth);
                        float textHeight = TextEngine.measureTextHeight(draw.context(), rt);
                        float drawY = y + Math.max(0.0f, h - textHeight) * 0.5f;

                        draw.pushTextClip(textStartX, y, contentWidth, h);
                        try {
                            TextEngine.drawInline(
                                    draw,
                                    rt,
                                    textStartX,
                                    drawY,
                                    contentWidth,
                                    textHeight,
                                    Paint.fill(textColor));
                        } finally {
                            draw.popClip();
                        }
                    });

    public static final WidgetRender BREADCRUMB_SEGMENT_RENDERER =
            WidgetRender.of(
                    Button.class,
                    (draw, btn) -> {
                        RectView b = btn.layoutBounds();
                        float x = b.x();
                        float y = b.y();
                        float w = b.width();
                        float h = b.height();
                        if (w <= 0.0f || h <= 0.0f) return;

                        boolean isCurrent =
                                (btn instanceof BreadcrumbSegmentButton sb) && sb.isCurrent();

                        if (btn.pressed()) {
                            draw.roundedRectFilled(
                                    x, y + 1.0f, w, h - 2.0f, 2.0f, BG_DROPDOWN_ITEM_PRESS);
                        } else if (btn.hovered()) {
                            draw.roundedRectFilled(
                                    x,
                                    y + 1.0f,
                                    w,
                                    h - 2.0f,
                                    2.0f,
                                    MutableColor.rgba(1.0f, 1.0f, 1.0f, 0.08f));
                        }

                        ColorView textColor;
                        if (isCurrent) {
                            textColor = TEXT_BOLD;
                        } else if (btn.hovered() || btn.pressed()) {
                            textColor = TEXT_BOLD;
                        } else {
                            textColor = TEXT_PRIMARY;
                        }

                        RichText rt = RichText.plain(btn.text());
                        float textWidth = TextEngine.measureLineWidth(draw.context(), rt);
                        float textHeight = TextEngine.measureTextHeight(draw.context(), rt);
                        float textX = x + Math.max(0.0f, w - textWidth) * 0.5f;
                        float textY = y + Math.max(0.0f, h - textHeight) * 0.5f;

                        draw.pushTextClip(x, y, w, h);
                        try {
                            TextEngine.drawInline(
                                    draw, rt, textX, textY, w, textHeight, Paint.fill(textColor));
                        } finally {
                            draw.popClip();
                        }
                    });

    public static final WidgetRender BREADCRUMB_CHEVRON_RENDERER =
            WidgetRender.of(
                    Button.class,
                    (draw, btn) -> {
                        RectView b = btn.layoutBounds();
                        float x = b.x();
                        float y = b.y();
                        float w = b.width();
                        float h = b.height();
                        if (w <= 0.0f || h <= 0.0f) return;

                        if (btn.pressed()) {
                            draw.roundedRectFilled(
                                    x, y + 1.0f, w, h - 2.0f, 2.0f, BG_DROPDOWN_ITEM_PRESS);
                        } else if (btn.hovered()) {
                            draw.roundedRectFilled(
                                    x,
                                    y + 1.0f,
                                    w,
                                    h - 2.0f,
                                    2.0f,
                                    MutableColor.rgba(1.0f, 1.0f, 1.0f, 0.08f));
                        }

                        ColorView chevronColor =
                                (btn.hovered() || btn.pressed()) ? TEXT_BOLD : TEXT_MUTED;
                        float cx = x + w * 0.5f;
                        float cy = y + h * 0.5f;

                        draw.pathClear();
                        draw.pathLineTo(cx - 1.5f, cy - 3.5f);
                        draw.pathLineTo(cx + 2.0f, cy);
                        draw.pathLineTo(cx - 1.5f, cy + 3.5f);
                        draw.pathStroke(chevronColor, false, 1.4f);
                    });

    public static final WidgetRender BREADCRUMB_DROPDOWN_ITEM_RENDERER =
            WidgetRender.of(
                    Button.class,
                    (draw, btn) -> {
                        RectView b = btn.layoutBounds();
                        float x = b.x();
                        float y = b.y();
                        float w = b.width();
                        float h = b.height();
                        if (w <= 0.0f || h <= 0.0f) return;

                        boolean enabled = btn.enabled();
                        boolean hovered = btn.hovered() && enabled;
                        boolean pressed = btn.pressed() && enabled;

                        if (pressed) {
                            draw.roundedRectFilled(x, y, w, h, 2.0f, BG_DROPDOWN_ITEM_PRESS);
                        } else if (hovered) {
                            draw.roundedRectFilled(x, y, w, h, 2.0f, SELECTION_BLUE);
                        }

                        boolean isChecked =
                                (btn instanceof BreadcrumbMenuItem item) && item.isChecked();
                        float slotWidth = 24.0f;

                        if (isChecked) {
                            float boxSize = 12.0f;
                            float boxX = x + 6.0f;
                            float boxY = y + Math.max(0.0f, h - boxSize) * 0.5f;

                            // Голубой квадрат (#90C8F6)
                            ColorView boxBg = MutableColor.rgba255(144, 200, 246, 255);
                            draw.roundedRectFilled(boxX, boxY, boxSize, boxSize, 2.0f, boxBg);

                            // Тёмная галочка внутри квадрата (✓)
                            ColorView checkColor = MutableColor.rgba255(32, 32, 32, 255);
                            draw.pathClear();
                            draw.pathLineTo(boxX + 2.8f, boxY + 5.8f);
                            draw.pathLineTo(boxX + 4.8f, boxY + 8.4f);
                            draw.pathLineTo(boxX + 9.2f, boxY + 3.2f);
                            draw.pathStroke(checkColor, false, 1.3f);
                        }

                        ColorView textColor;
                        if (!enabled) {
                            textColor = TEXT_MUTED;
                        } else if (hovered || pressed) {
                            textColor = TEXT_BOLD;
                        } else {
                            textColor = TEXT_PRIMARY;
                        }

                        RichText rt = RichText.plain(btn.text());
                        float textX = x + slotWidth;
                        float contentWidth = Math.max(0.0f, w - (slotWidth + 8.0f));
                        float textHeight = TextEngine.measureTextHeight(draw.context(), rt);
                        float drawY = y + Math.max(0.0f, h - textHeight) * 0.5f;

                        draw.pushTextClip(textX, y, contentWidth, h);
                        try {
                            TextEngine.drawInline(
                                    draw,
                                    rt,
                                    textX,
                                    drawY,
                                    contentWidth,
                                    textHeight,
                                    Paint.fill(textColor));
                        } finally {
                            draw.popClip();
                        }
                    });
}
