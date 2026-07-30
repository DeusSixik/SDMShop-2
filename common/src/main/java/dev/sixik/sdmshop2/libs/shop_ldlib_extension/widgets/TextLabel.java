package dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class TextLabel extends Widget {

    public enum HorizontalAlignment {
        LEFT,
        CENTER,
        RIGHT
    }

    public enum VerticalAlignment {
        TOP,
        CENTER,
        BOTTOM
    }

    public enum OverflowMode {
        CLIP,
        ELLIPSIS,
        SCALE_TO_FIT
    }

    public enum HighlightMode {
        OUTLINE,
        FILL
    }

    protected Component text;

    protected int color = 0xFFFFFFFF;
    protected boolean shadow;
    protected boolean highlighted;
    protected int highlightColor = 0xFFFFFFFF;
    protected HighlightMode highlightMode = HighlightMode.OUTLINE;

    protected float scale = 1.0f;
    protected float minScale = 0.35f;

    protected boolean wrapText;
    protected boolean autoSize;
    protected int maxLines;
    protected int lineSpacing;

    protected int paddingLeft;
    protected int paddingTop;
    protected int paddingRight;
    protected int paddingBottom;

    protected HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;
    protected VerticalAlignment verticalAlignment = VerticalAlignment.TOP;
    protected OverflowMode overflowMode = OverflowMode.CLIP;

    private boolean layoutDirty = true;
    private boolean resizingToContent;

    private final List<FormattedCharSequence> renderLines = new ArrayList<>();
    private float renderScale = 1.0f;
    private int textBlockWidth;
    private int textBlockHeight;

    public TextLabel(Component text) {
        this(0, 0, text);
    }

    public TextLabel(int x, int y, Component text) {
        super(x, y, 1, 1);
        this.text = text == null ? Component.empty() : text;
        setAutoSize(true);
    }

    public TextLabel(Position selfPosition, Size size, Component text) {
        super(selfPosition, size);
        this.text = text == null ? Component.empty() : text;
    }

    public TextLabel(int x, int y, int width, int height, Component text) {
        super(x, y, width, height);
        this.text = text == null ? Component.empty() : text;
    }

    public TextLabel setText(Component text) {
        this.text = text == null ? Component.empty() : text;
        invalidateLayout();
        return this;
    }

    public Component getText() {
        return text;
    }

    public TextLabel setColor(int color) {
        this.color = color;
        return this;
    }

    public int getColor() {
        return color;
    }

    public TextLabel setShadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    public TextLabel setScale(float scale) {
        this.scale = Math.max(0.01f, scale);
        invalidateLayout();
        return this;
    }

    public TextLabel setMinScale(float minScale) {
        this.minScale = Math.max(0.01f, minScale);
        invalidateLayout();
        return this;
    }

    public TextLabel setWrapText(boolean wrapText) {
        this.wrapText = wrapText;
        invalidateLayout();
        return this;
    }

    public TextLabel setAutoSize(boolean autoSize) {
        this.autoSize = autoSize;
        invalidateLayout();
        return this;
    }

    public TextLabel setMaxLines(int maxLines) {
        this.maxLines = Math.max(0, maxLines);
        invalidateLayout();
        return this;
    }

    public TextLabel setLineSpacing(int lineSpacing) {
        this.lineSpacing = Math.max(0, lineSpacing);
        invalidateLayout();
        return this;
    }

    public TextLabel setPadding(int padding) {
        return setPadding(padding, padding, padding, padding);
    }

    public TextLabel setPadding(int horizontal, int vertical) {
        return setPadding(horizontal, vertical, horizontal, vertical);
    }

    public TextLabel setPadding(int left, int top, int right, int bottom) {
        this.paddingLeft = Math.max(0, left);
        this.paddingTop = Math.max(0, top);
        this.paddingRight = Math.max(0, right);
        this.paddingBottom = Math.max(0, bottom);
        invalidateLayout();
        return this;
    }

    public TextLabel setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment == null ? HorizontalAlignment.LEFT : horizontalAlignment;
        invalidateLayout();
        return this;
    }

    public TextLabel setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment == null ? VerticalAlignment.TOP : verticalAlignment;
        invalidateLayout();
        return this;
    }

    public TextLabel setAlignment(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
        return setHorizontalAlignment(horizontalAlignment).setVerticalAlignment(verticalAlignment);
    }

    public TextLabel alignLeft() {
        return setHorizontalAlignment(HorizontalAlignment.LEFT);
    }

    public TextLabel alignCenter() {
        return setHorizontalAlignment(HorizontalAlignment.CENTER);
    }

    public TextLabel alignRight() {
        return setHorizontalAlignment(HorizontalAlignment.RIGHT);
    }

    public TextLabel alignTop() {
        return setVerticalAlignment(VerticalAlignment.TOP);
    }

    public TextLabel alignMiddle() {
        return setVerticalAlignment(VerticalAlignment.CENTER);
    }

    public TextLabel alignBottom() {
        return setVerticalAlignment(VerticalAlignment.BOTTOM);
    }

    public TextLabel setOverflowMode(OverflowMode overflowMode) {
        this.overflowMode = overflowMode == null ? OverflowMode.CLIP : overflowMode;
        invalidateLayout();
        return this;
    }

    public TextLabel clipOverflow() {
        return setOverflowMode(OverflowMode.CLIP);
    }

    public TextLabel ellipsisOverflow() {
        return setOverflowMode(OverflowMode.ELLIPSIS);
    }

    public TextLabel scaleToFit() {
        return setOverflowMode(OverflowMode.SCALE_TO_FIT);
    }

    public TextLabel setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
        return this;
    }

    public TextLabel setHighlightOutline(int color) {
        this.highlighted = true;
        this.highlightMode = HighlightMode.OUTLINE;
        this.highlightColor = color;
        return this;
    }

    public TextLabel setHighlightFill(int color) {
        this.highlighted = true;
        this.highlightMode = HighlightMode.FILL;
        this.highlightColor = color;
        return this;
    }

    public TextLabel setHighlight(HighlightMode mode, int color) {
        this.highlighted = true;
        this.highlightMode = mode == null ? HighlightMode.OUTLINE : mode;
        this.highlightColor = color;
        return this;
    }

    public TextLabel clearHighlight() {
        this.highlighted = false;
        return this;
    }

    @Override
    public void setSize(Size size) {
        super.setSize(size);
        if (!resizingToContent) {
            invalidateLayout();
        }
    }

    @Override
    public void drawInBackground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        drawHighlight(graphics, mouseX, mouseY);
        ensureLayout();
        if (renderLines.isEmpty()) return;

        int contentX = getPositionX() + paddingLeft;
        int contentY = getPositionY() + paddingTop;
        int contentWidth = getContentWidth();
        int contentHeight = getContentHeight();

        int blockWidth = Math.round(textBlockWidth * renderScale);
        int blockHeight = Math.round(textBlockHeight * renderScale);
        int drawX = switch (horizontalAlignment) {
            case CENTER -> contentX + Math.max(0, (contentWidth - blockWidth) / 2);
            case RIGHT -> contentX + Math.max(0, contentWidth - blockWidth);
            case LEFT -> contentX;
        };
        int drawY = switch (verticalAlignment) {
            case CENTER -> contentY + Math.max(0, (contentHeight - blockHeight) / 2);
            case BOTTOM -> contentY + Math.max(0, contentHeight - blockHeight);
            case TOP -> contentY;
        };

        graphics.enableScissor(contentX, contentY, contentX + contentWidth, contentY + contentHeight);

        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(drawX, drawY, 0);
        poseStack.scale(renderScale, renderScale, 1.0f);

        Font font = Minecraft.getInstance().font;
        int lineStep = font.lineHeight + lineSpacing;
        for (int i = 0; i < renderLines.size(); i++) {
            FormattedCharSequence line = renderLines.get(i);
            int lineWidth = font.width(line);
            int lineX = switch (horizontalAlignment) {
                case CENTER -> Math.max(0, (textBlockWidth - lineWidth) / 2);
                case RIGHT -> Math.max(0, textBlockWidth - lineWidth);
                case LEFT -> 0;
            };
            graphics.drawString(font, line, lineX, i * lineStep, color, shadow);
        }

        poseStack.popPose();
        graphics.disableScissor();
    }

    private void drawHighlight(GuiGraphics graphics, int mouseX, int mouseY) {
        if (!highlighted || highlightColor == 0 || getSizeWidth() <= 0 || getSizeHeight() <= 0) return;

        if (highlightMode == HighlightMode.FILL) {
            graphics.fill(getPositionX(), getPositionY(), getPositionX() + getSizeWidth(), getPositionY() + getSizeHeight(), highlightColor);
            return;
        }

        drawOutline(graphics, getPositionX(), getPositionY(), getSizeWidth(), getSizeHeight(), highlightColor, 1);
    }

    private void drawOutline(GuiGraphics graphics, int x, int y, int width, int height, int color, int thickness) {
        if (color == 0 || width <= 0 || height <= 0) return;

        int line = Math.max(1, Math.min(thickness, Math.min(width, height)));
        graphics.fill(x, y, x + width, y + line, color);
        graphics.fill(x, y + height - line, x + width, y + height, color);
        graphics.fill(x, y + line, x + line, y + height - line, color);
        graphics.fill(x + width - line, y + line, x + width, y + height - line, color);
    }

    public int getTextWidth() {
        ensureLayout();
        return textBlockWidth;
    }

    public int getTextHeight() {
        ensureLayout();
        return textBlockHeight;
    }

    private void ensureLayout() {
        if (!layoutDirty) return;
        layoutDirty = false;

        Font font = Minecraft.getInstance().font;
        renderScale = scale;
        renderLines.clear();

        int contentWidth = Math.max(1, getContentWidth());
        int splitWidth = wrapText ? Math.max(1, Math.round(contentWidth / Math.max(0.01f, scale))) : Integer.MAX_VALUE;
        if (wrapText) {
            renderLines.addAll(font.split(text, splitWidth));
        } else {
            renderLines.add(text.getVisualOrderText());
        }

        applyMaxLinesAndOverflow(font, splitWidth);
        recalculateTextBlock(font);

        if (overflowMode == OverflowMode.SCALE_TO_FIT && !autoSize) {
            float widthScale = textBlockWidth <= 0 ? scale : getContentWidth() / (float) textBlockWidth;
            float heightScale = textBlockHeight <= 0 ? scale : getContentHeight() / (float) textBlockHeight;
            renderScale = Math.max(minScale, Math.min(scale, Math.min(widthScale, heightScale)));
        }

        if (autoSize) {
            resizingToContent = true;
            setSize(
                    Math.round(textBlockWidth * renderScale) + paddingLeft + paddingRight,
                    Math.round(textBlockHeight * renderScale) + paddingTop + paddingBottom
            );
            resizingToContent = false;
        }
    }

    private void applyMaxLinesAndOverflow(Font font, int splitWidth) {
        if (maxLines > 0 && renderLines.size() > maxLines) {
            while (renderLines.size() > maxLines) {
                renderLines.remove(renderLines.size() - 1);
            }
            if (overflowMode == OverflowMode.ELLIPSIS && !renderLines.isEmpty()) {
                renderLines.set(renderLines.size() - 1, makeEllipsisLine(font, text.getString(), splitWidth));
            }
            return;
        }

        if (!wrapText && overflowMode == OverflowMode.ELLIPSIS && !autoSize) {
            int availableWidth = Math.max(1, Math.round(getContentWidth() / Math.max(0.01f, scale)));
            String rawText = text.getString();
            if (font.width(rawText) > availableWidth) {
                renderLines.clear();
                renderLines.add(makeEllipsisLine(font, rawText, availableWidth));
            }
        }
    }

    private FormattedCharSequence makeEllipsisLine(Font font, String rawText, int availableWidth) {
        String ellipsis = "...";
        int ellipsisWidth = font.width(ellipsis);
        if (availableWidth <= ellipsisWidth) {
            return Component.literal(ellipsis).getVisualOrderText();
        }

        String prefix = font.plainSubstrByWidth(rawText, availableWidth - ellipsisWidth);
        return Component.literal(prefix + ellipsis).getVisualOrderText();
    }

    private void recalculateTextBlock(Font font) {
        textBlockWidth = 0;
        for (FormattedCharSequence line : renderLines) {
            textBlockWidth = Math.max(textBlockWidth, font.width(line));
        }

        if (renderLines.isEmpty()) {
            textBlockHeight = 0;
        } else {
            textBlockHeight = renderLines.size() * font.lineHeight + Math.max(0, renderLines.size() - 1) * lineSpacing;
        }
    }

    private int getContentWidth() {
        return Math.max(0, getSizeWidth() - paddingLeft - paddingRight);
    }

    private int getContentHeight() {
        return Math.max(0, getSizeHeight() - paddingTop - paddingBottom);
    }

    private void invalidateLayout() {
        layoutDirty = true;
    }
}
