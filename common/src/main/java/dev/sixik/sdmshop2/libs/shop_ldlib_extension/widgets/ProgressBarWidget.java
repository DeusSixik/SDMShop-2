package dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets;

import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import dev.sixik.sdmshop2.libs.shop.client.textures.ColorRectAndBorderTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleSupplier;

public class ProgressBarWidget extends Widget {

    public enum TextMode {
        NONE,
        CENTER,
        SPLIT
    }

    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 16;
    private static final int DEFAULT_BAR_HEIGHT = 4;
    private static final int DEFAULT_TEXT_GAP = 2;
    private static final int DEFAULT_SPLIT_TEXT_GAP = 4;
    private static final int DEFAULT_RADIUS = 2;
    private static final int DEFAULT_TRACK_COLOR = 0xFF2A2A36;
    private static final int DEFAULT_TRACK_BORDER_COLOR = 0xFF3D3D4E;
    private static final int DEFAULT_PROGRESS_COLOR = 0xFF5C6BC0;
    private static final int DEFAULT_TEXT_COLOR = 0xFFFFFFFF;

    protected double progress;
    protected DoubleSupplier progressSupplier;

    protected TextMode textMode = TextMode.NONE;
    protected Component centerText = Component.empty();
    protected Component leftText = Component.empty();
    protected Component rightText = Component.empty();

    protected int barHeight = DEFAULT_BAR_HEIGHT;
    protected int textGap = DEFAULT_TEXT_GAP;
    protected int splitTextGap = DEFAULT_SPLIT_TEXT_GAP;
    protected int radius = DEFAULT_RADIUS;
    protected int trackColor = DEFAULT_TRACK_COLOR;
    protected int trackBorderColor = DEFAULT_TRACK_BORDER_COLOR;
    protected int progressColor = DEFAULT_PROGRESS_COLOR;
    protected int textColor = DEFAULT_TEXT_COLOR;
    protected boolean textShadow;
    protected float scale = 1.0f;
    protected float textScale = 1.0f;
    protected int textYOffset;
    protected int textHorizontalPadding;
    protected boolean customTextScale;

    public ProgressBarWidget() {
        this(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public ProgressBarWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public ProgressBarWidget setProgress(double progress) {
        this.progress = clamp(progress);
        return this;
    }

    public double getProgress() {
        return progressSupplier == null ? progress : clamp(progressSupplier.getAsDouble());
    }

    public ProgressBarWidget setProgressSupplier(DoubleSupplier progressSupplier) {
        this.progressSupplier = progressSupplier;
        return this;
    }

    public ProgressBarWidget clearProgressSupplier() {
        this.progressSupplier = null;
        return this;
    }

    public ProgressBarWidget setTextMode(TextMode textMode) {
        this.textMode = textMode == null ? TextMode.NONE : textMode;
        return this;
    }

    public ProgressBarWidget setCenterText(Component text) {
        this.centerText = text == null ? Component.empty() : text;
        return setTextMode(TextMode.CENTER);
    }

    public ProgressBarWidget setSplitText(Component leftText, Component rightText) {
        this.leftText = leftText == null ? Component.empty() : leftText;
        this.rightText = rightText == null ? Component.empty() : rightText;
        return setTextMode(TextMode.SPLIT);
    }

    public ProgressBarWidget setLeftText(Component leftText) {
        this.leftText = leftText == null ? Component.empty() : leftText;
        if(this.rightText == null)
            this.rightText = Component.empty();
        return setTextMode(TextMode.SPLIT);
    }

    public ProgressBarWidget setRightText(Component rightText) {
        this.rightText = rightText == null ? Component.empty() : rightText;
        if(this.leftText == null)
            this.leftText = Component.empty();
        return setTextMode(TextMode.SPLIT);
    }

    public ProgressBarWidget clearText() {
        this.centerText = Component.empty();
        this.leftText = Component.empty();
        this.rightText = Component.empty();
        return setTextMode(TextMode.NONE);
    }

    public ProgressBarWidget setBarHeight(int barHeight) {
        this.barHeight = Math.max(1, barHeight);
        return this;
    }

    public ProgressBarWidget setTextGap(int textGap) {
        this.textGap = Math.max(0, textGap);
        return this;
    }

    public ProgressBarWidget setSplitTextGap(int splitTextGap) {
        this.splitTextGap = Math.max(0, splitTextGap);
        return this;
    }

    public ProgressBarWidget splitTextGap(int splitTextGap) {
        return setSplitTextGap(splitTextGap);
    }

    public int getSplitTextGap() {
        return splitTextGap;
    }

    public ProgressBarWidget setRadius(int radius) {
        this.radius = Math.max(0, radius);
        return this;
    }

    public ProgressBarWidget setTrackColors(int trackColor, int borderColor) {
        this.trackColor = trackColor;
        this.trackBorderColor = borderColor;
        return this;
    }

    public ProgressBarWidget setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        return this;
    }

    public ProgressBarWidget setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public ProgressBarWidget setTextShadow(boolean textShadow) {
        this.textShadow = textShadow;
        return this;
    }

    public ProgressBarWidget setScale(float scale) {
        this.scale = Math.max(0.01f, scale);
        return this;
    }

    public ProgressBarWidget scale(float scale) {
        return setScale(scale);
    }

    public float getScale() {
        return scale;
    }

    public ProgressBarWidget setTextScale(float textScale) {
        this.textScale = Math.max(0.01f, textScale);
        this.customTextScale = true;
        return this;
    }

    public ProgressBarWidget textScale(float textScale) {
        return setTextScale(textScale);
    }

    public ProgressBarWidget clearTextScale() {
        this.customTextScale = false;
        return this;
    }

    public float getTextScale() {
        return getEffectiveTextScale();
    }

    public boolean hasCustomTextScale() {
        return customTextScale;
    }

    public ProgressBarWidget setTextYOffset(int textYOffset) {
        this.textYOffset = textYOffset;
        return this;
    }

    public ProgressBarWidget textYOffset(int textYOffset) {
        return setTextYOffset(textYOffset);
    }

    public int getTextYOffset() {
        return textYOffset;
    }

    public ProgressBarWidget setTextHorizontalPadding(int textHorizontalPadding) {
        this.textHorizontalPadding = Math.max(0, textHorizontalPadding);
        return this;
    }

    public ProgressBarWidget textHorizontalPadding(int textHorizontalPadding) {
        return setTextHorizontalPadding(textHorizontalPadding);
    }

    public ProgressBarWidget setTextXPadding(int textHorizontalPadding) {
        return setTextHorizontalPadding(textHorizontalPadding);
    }

    public ProgressBarWidget textXPadding(int textHorizontalPadding) {
        return setTextHorizontalPadding(textHorizontalPadding);
    }

    public int getTextHorizontalPadding() {
        return textHorizontalPadding;
    }

    @Override
    public void drawInBackground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        drawText(graphics);
        drawBar(graphics, mouseX, mouseY);
    }

    private void drawText(GuiGraphics graphics) {
        if (textMode == TextMode.NONE) return;

        Font font = Minecraft.getInstance().font;
        TextLayout layout = computeTextLayout(font);
        int textY = getPositionY() + getScaledTextYOffset();
        int textX = getPositionX() + getScaledTextHorizontalPadding();
        int textWidth = Math.max(0, getSizeWidth() - getScaledTextHorizontalPadding() * 2);
        if (textWidth <= 0 || layout.height <= 0) return;

        graphics.enableScissor(textX, textY, textX + textWidth, textY + layout.height);
        try {
            for (DrawLine line : layout.lines) {
                drawScaledString(graphics, font, line.text, line.x, textY + line.y);
            }
        } finally {
            graphics.disableScissor();
        }
    }

    private void drawBar(GuiGraphics graphics, int mouseX, int mouseY) {
        int x = getPositionX();
        int y = getBarY();
        int width = getSizeWidth();
        int height = getScaledBarHeight();

        if (width <= 0 || height <= 0) return;

        new ColorRectAndBorderTexture(trackColor, trackBorderColor, 1)
                .setRadius(getScaledRadius())
                .draw(graphics, mouseX, mouseY, x, y, width, height);

        int progressWidth = Math.round(width * (float) getProgress());
        if (progressWidth <= 0) return;

        new ColorRectTexture(progressColor)
                .setRadius(getScaledRadius())
                .draw(graphics, mouseX, mouseY, x, y, Math.min(width, progressWidth), height);
    }

    private void drawScaledString(GuiGraphics graphics, Font font, FormattedCharSequence text, float x, float y) {
        if (text == null) return;

        float drawScale = getEffectiveTextScale();
        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);
        graphics.pose().scale(drawScale, drawScale, 1.0f);
        graphics.drawString(font, text, 0, 0, textColor, textShadow);
        graphics.pose().popPose();
    }

    private int getTextWidth(Font font, Component text) {
        return Math.round(font.width(text == null ? Component.empty() : text) * getEffectiveTextScale());
    }

    private int getTextWidth(Font font, FormattedCharSequence text) {
        return Math.round(font.width(text) * getEffectiveTextScale());
    }

    private int getBarY() {
        if (textMode == TextMode.NONE) {
            return getPositionY() + Math.max(0, (getSizeHeight() - getScaledBarHeight()) / 2);
        }

        return getPositionY() + getTextBlockHeight() + getScaledTextGap();
    }

    private int getScaledTextHeight() {
        return Math.max(1, Math.round(Minecraft.getInstance().font.lineHeight * getEffectiveTextScale()));
    }

    private int getScaledBarHeight() {
        return Math.max(1, Math.round(barHeight * scale));
    }

    private int getScaledTextGap() {
        return Math.max(0, Math.round(textGap * scale));
    }

    private int getScaledTextYOffset() {
        return Math.round(textYOffset * getEffectiveTextScale());
    }

    private int getScaledTextHorizontalPadding() {
        return Math.max(0, Math.round(textHorizontalPadding * getEffectiveTextScale()));
    }

    private int getScaledSplitTextGap() {
        return Math.max(0, Math.round(splitTextGap * getEffectiveTextScale()));
    }

    private int getScaledRadius() {
        return Math.max(0, Math.round(radius * scale));
    }

    private float getEffectiveTextScale() {
        return customTextScale ? textScale : scale;
    }

    private int getTextBlockHeight() {
        if (textMode == TextMode.NONE) return 0;
        return computeTextLayout(Minecraft.getInstance().font).height;
    }

    private TextLayout computeTextLayout(Font font) {
        int textX = getPositionX() + getScaledTextHorizontalPadding();
        int textWidth = Math.max(0, getSizeWidth() - getScaledTextHorizontalPadding() * 2);
        int lineHeight = getScaledTextHeight();
        List<DrawLine> lines = new ArrayList<>();

        if (textWidth <= 0 || textMode == TextMode.NONE) {
            return new TextLayout(lines, 0);
        }

        if (textMode == TextMode.CENTER) {
            int y = 0;
            for (FormattedCharSequence line : splitText(font, centerText, textWidth)) {
                int lineWidth = getTextWidth(font, line);
                lines.add(new DrawLine(line, textX + Math.max(0, (textWidth - lineWidth) / 2f), y));
                y += lineHeight;
            }
            return new TextLayout(lines, lines.isEmpty() ? 0 : y);
        }

        int leftWidth = getTextWidth(font, leftText);
        int rightWidth = getTextWidth(font, rightText);
        int splitGap = getScaledSplitTextGap();

        if (leftWidth + rightWidth + splitGap <= textWidth) {
            lines.add(new DrawLine(leftText.getVisualOrderText(), textX, 0));
            lines.add(new DrawLine(rightText.getVisualOrderText(), textX + textWidth - rightWidth, 0));
            return new TextLayout(lines, lineHeight);
        }

        int y = 0;
        for (FormattedCharSequence line : splitText(font, leftText, textWidth)) {
            lines.add(new DrawLine(line, textX, y));
            y += lineHeight;
        }

        for (FormattedCharSequence line : splitText(font, rightText, textWidth)) {
            int lineWidth = getTextWidth(font, line);
            lines.add(new DrawLine(line, textX + Math.max(0, textWidth - lineWidth), y));
            y += lineHeight;
        }

        return new TextLayout(lines, lines.isEmpty() ? 0 : y);
    }

    private List<FormattedCharSequence> splitText(Font font, Component text, int maxWidth) {
        if (text == null || text.getString().isEmpty() || maxWidth <= 0) {
            return List.of();
        }

        int unscaledMaxWidth = Math.max(1, (int) Math.floor(maxWidth / getEffectiveTextScale()));
        return font.split(text, unscaledMaxWidth);
    }

    private static class DrawLine {
        private final FormattedCharSequence text;
        private final float x;
        private final int y;

        private DrawLine(FormattedCharSequence text, float x, int y) {
            this.text = text;
            this.x = x;
            this.y = y;
        }
    }

    private static class TextLayout {
        private final List<DrawLine> lines;
        private final int height;

        private TextLayout(List<DrawLine> lines, int height) {
            this.lines = lines;
            this.height = height;
        }
    }

    private static double clamp(double value) {
        if (Double.isNaN(value)) return 0.0;
        return Math.max(0.0, Math.min(1.0, value));
    }
}
