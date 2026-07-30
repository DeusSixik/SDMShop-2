package dev.sixik.sdmshop2.libs.shop.client.screens.widgets;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.systems.RenderSystem;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import dev.sixik.sdmshop2.libs.shop.client.textures.ColorRectAndBorderTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopBadgeHBoxWidget extends WidgetGroup {

    public enum CrossAxisAlignment {
        START,
        CENTER,
        END
    }

    public enum MainAxisAlignment {
        START,
        CENTER,
        END
    }

    private static final int DEFAULT_SPACING = 2;
    private static final int DEFAULT_TOOLTIP_PADDING = 4;
    private static final int DEFAULT_TOOLTIP_SPACING = 2;
    private static final int DEFAULT_TOOLTIP_OFFSET_X = 8;
    private static final int DEFAULT_TOOLTIP_OFFSET_Y = 8;
    private static final int DEFAULT_TOOLTIP_BACKGROUND_COLOR = 0xEE1E1E2A;
    private static final int DEFAULT_TOOLTIP_BORDER_COLOR = 0xFF4B4B5D;

    protected final List<ShopBadgeWidget> badges = new ArrayList<>();
    protected final ShopBadgeWidget overflowBadge;

    protected int maxLength;
    protected int spacing = DEFAULT_SPACING;
    protected int paddingLeft;
    protected int paddingTop;
    protected int paddingRight;
    protected int paddingBottom;
    protected int tooltipPadding = DEFAULT_TOOLTIP_PADDING;
    protected int tooltipSpacing = DEFAULT_TOOLTIP_SPACING;
    protected int tooltipOffsetX = DEFAULT_TOOLTIP_OFFSET_X;
    protected int tooltipOffsetY = DEFAULT_TOOLTIP_OFFSET_Y;
    protected int tooltipBackgroundColor = DEFAULT_TOOLTIP_BACKGROUND_COLOR;
    protected int tooltipBorderColor = DEFAULT_TOOLTIP_BORDER_COLOR;
    protected MainAxisAlignment mainAxisAlignment = MainAxisAlignment.CENTER;
    protected CrossAxisAlignment crossAxisAlignment = CrossAxisAlignment.CENTER;
    protected float scale = 1.0f;
    protected boolean scaleTooltipWithHBox;

    protected int visibleBadgeCount;
    protected int hiddenBadgeCount;
    protected int overflowX;
    protected int overflowY;
    protected int overflowWidth;
    protected int overflowHeight;

    private boolean recomputingLayout;
    private boolean renderingTooltip;

    public ShopBadgeHBoxWidget() {
        this(Position.ORIGIN, Size.ZERO);
    }

    public ShopBadgeHBoxWidget(int maxLength) {
        this(0, 0, maxLength, 0);
        this.maxLength = Math.max(0, maxLength);
        setDynamicSized(true);
        recomputeLayoutAndSize();
    }

    public ShopBadgeHBoxWidget(Position selfPosition, Size size) {
        super(selfPosition, size);
        this.maxLength = Math.max(0, size.width);
        overflowBadge = new ShopBadgeWidget(Component.literal("(+0)"))
                .setFillColor(0xFF3D3D4E)
                .setTextColor(0xFFFFFFFF)
                .autoSizeToContent();
        overflowBadge.setVisible(false);
        super.addWidget(overflowBadge);
        setDynamicSized(false);
    }

    public ShopBadgeHBoxWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.maxLength = Math.max(0, width);
        overflowBadge = new ShopBadgeWidget(Component.literal("(+0)"))
                .setFillColor(0xFF3D3D4E)
                .setTextColor(0xFFFFFFFF)
                .autoSizeToContent();
        overflowBadge.setVisible(false);
        super.addWidget(overflowBadge);
        setDynamicSized(false);
    }

    public ShopBadgeHBoxWidget setMaxLength(int maxLength) {
        this.maxLength = Math.max(0, maxLength);
        recomputeLayoutAndSize();
        return this;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public ShopBadgeHBoxWidget setScale(float scale) {
        this.scale = Math.max(0.01f, scale);
        for (ShopBadgeWidget badge : badges) {
            badge.setScale(this.scale);
        }
        overflowBadge.setScale(this.scale);
        recomputeLayoutAndSize();
        return this;
    }

    public ShopBadgeHBoxWidget scale(float scale) {
        return setScale(scale);
    }

    public float getScale() {
        return scale;
    }

    public ShopBadgeHBoxWidget setScaleTooltipWithHBox(boolean scaleTooltipWithHBox) {
        this.scaleTooltipWithHBox = scaleTooltipWithHBox;
        return this;
    }

    public ShopBadgeHBoxWidget scaleTooltipWithHBox() {
        return setScaleTooltipWithHBox(true);
    }

    public ShopBadgeHBoxWidget fixedTooltipScale() {
        return setScaleTooltipWithHBox(false);
    }

    public boolean isScaleTooltipWithHBox() {
        return scaleTooltipWithHBox;
    }

    public ShopBadgeHBoxWidget setSpacing(int spacing) {
        this.spacing = Math.max(0, spacing);
        recomputeLayoutAndSize();
        return this;
    }

    public ShopBadgeHBoxWidget setPadding(int padding) {
        return setPadding(padding, padding, padding, padding);
    }

    public ShopBadgeHBoxWidget setPadding(int horizontal, int vertical) {
        return setPadding(horizontal, vertical, horizontal, vertical);
    }

    public ShopBadgeHBoxWidget setPadding(int left, int top, int right, int bottom) {
        this.paddingLeft = Math.max(0, left);
        this.paddingTop = Math.max(0, top);
        this.paddingRight = Math.max(0, right);
        this.paddingBottom = Math.max(0, bottom);
        recomputeLayoutAndSize();
        return this;
    }

    public ShopBadgeHBoxWidget setCrossAxisAlignment(CrossAxisAlignment alignment) {
        this.crossAxisAlignment = alignment == null ? CrossAxisAlignment.CENTER : alignment;
        recomputeLayout();
        return this;
    }

    public ShopBadgeHBoxWidget setMainAxisAlignment(MainAxisAlignment alignment) {
        this.mainAxisAlignment = alignment == null ? MainAxisAlignment.CENTER : alignment;
        recomputeLayout();
        return this;
    }

    public ShopBadgeHBoxWidget alignMainStart() {
        return setMainAxisAlignment(MainAxisAlignment.START);
    }

    public ShopBadgeHBoxWidget alignMainCenter() {
        return setMainAxisAlignment(MainAxisAlignment.CENTER);
    }

    public ShopBadgeHBoxWidget alignMainEnd() {
        return setMainAxisAlignment(MainAxisAlignment.END);
    }

    public ShopBadgeHBoxWidget alignStart() {
        return setCrossAxisAlignment(CrossAxisAlignment.START);
    }

    public ShopBadgeHBoxWidget alignCenter() {
        return setCrossAxisAlignment(CrossAxisAlignment.CENTER);
    }

    public ShopBadgeHBoxWidget alignEnd() {
        return setCrossAxisAlignment(CrossAxisAlignment.END);
    }

    public ShopBadgeHBoxWidget setTooltipPadding(int tooltipPadding) {
        this.tooltipPadding = Math.max(0, tooltipPadding);
        return this;
    }

    public ShopBadgeHBoxWidget setTooltipSpacing(int tooltipSpacing) {
        this.tooltipSpacing = Math.max(0, tooltipSpacing);
        return this;
    }

    public ShopBadgeHBoxWidget setTooltipOffset(int x, int y) {
        this.tooltipOffsetX = x;
        this.tooltipOffsetY = y;
        return this;
    }

    public ShopBadgeHBoxWidget setTooltipColors(int backgroundColor, int borderColor) {
        this.tooltipBackgroundColor = backgroundColor;
        this.tooltipBorderColor = borderColor;
        return this;
    }

    public ShopBadgeHBoxWidget addBadge(ShopBadgeWidget badge) {
        return addBadge(badges.size(), badge);
    }

    public ShopBadgeHBoxWidget addBadge(int index, ShopBadgeWidget badge) {
        if (badge == null || badge == overflowBadge) return this;

        int safeIndex = Math.max(0, Math.min(index, badges.size()));
        badge.setScale(scale);
        badges.add(safeIndex, badge);
        super.addWidget(Math.min(safeIndex, widgets.size()), badge);
        recomputeLayoutAndSize();
        return this;
    }

    @Override
    public ShopBadgeHBoxWidget addWidget(Widget widget) {
        if (widget instanceof ShopBadgeWidget badge) {
            return addBadge(badge);
        }

        throw new IllegalArgumentException("ShopBadgeHBoxWidget supports only ShopBadgeWidget children");
    }

    @Override
    public ShopBadgeHBoxWidget addWidget(int index, Widget widget) {
        if (widget instanceof ShopBadgeWidget badge) {
            return addBadge(index, badge);
        }

        throw new IllegalArgumentException("ShopBadgeHBoxWidget supports only ShopBadgeWidget children");
    }

    @Override
    public ShopBadgeHBoxWidget addWidgets(Widget... widgets) {
        for (Widget widget : widgets) {
            addWidget(widget);
        }
        return this;
    }

    @Override
    public void removeWidget(Widget widget) {
        if (widget == overflowBadge) return;

        if (widget instanceof ShopBadgeWidget badge) {
            badges.remove(badge);
        }
        super.removeWidget(widget);
        recomputeLayoutAndSize();
    }

    @Override
    public void clearAllWidgets() {
        badges.clear();
        super.clearAllWidgets();
        overflowBadge.setVisible(false);
        super.addWidget(overflowBadge);
        recomputeLayoutAndSize();
    }

    public List<ShopBadgeWidget> getBadges() {
        return Collections.unmodifiableList(badges);
    }

    public ShopBadgeWidget getOverflowBadge() {
        return overflowBadge;
    }

    public int getVisibleBadgeCount() {
        return visibleBadgeCount;
    }

    public int getHiddenBadgeCount() {
        return hiddenBadgeCount;
    }

    @Override
    protected Size computeDynamicSize() {
        int maxChildHeight = 0;
        int totalWidth = getScaledPaddingLeft() + getScaledPaddingRight();

        for (ShopBadgeWidget badge : badges) {
            totalWidth += badge.getSizeWidth();
            maxChildHeight = Math.max(maxChildHeight, badge.getSizeHeight());
        }

        if (badges.size() > 1) {
            totalWidth += getScaledSpacing() * (badges.size() - 1);
        }

        int width = maxLength > 0 ? Math.min(maxLength, totalWidth) : totalWidth;
        int height = getScaledPaddingTop() + getScaledPaddingBottom() + maxChildHeight;
        return new Size(Math.max(1, width), Math.max(1, height));
    }

    @Override
    protected void recomputeLayout() {
        if (recomputingLayout || renderingTooltip || overflowBadge == null) return;
        recomputingLayout = true;

        try {
            int limit = getContentLengthLimit();
            visibleBadgeCount = computeVisibleBadgeCount(limit);
            hiddenBadgeCount = Math.max(0, badges.size() - visibleBadgeCount);

            if (hiddenBadgeCount > 0) {
                overflowBadge.setText(Component.literal("(+" + hiddenBadgeCount + ")"));
            }

            int rowWidth = getRowWidth(visibleBadgeCount, hiddenBadgeCount);
            int extraSpace = limit == Integer.MAX_VALUE ? 0 : Math.max(0, limit - rowWidth);
            int x = getScaledPaddingLeft() + alignMainAxis(extraSpace);
            int availableHeight = Math.max(0, getSizeHeight() - getScaledPaddingTop() - getScaledPaddingBottom());

            for (int i = 0; i < badges.size(); i++) {
                ShopBadgeWidget badge = badges.get(i);
                boolean visible = i < visibleBadgeCount;
                badge.setVisible(visible);
                if (!visible) continue;

                badge.setSelfPosition(x, alignCrossAxis(availableHeight, badge.getSizeHeight()));
                x += badge.getSizeWidth() + getScaledSpacing();
            }

            overflowBadge.setVisible(hiddenBadgeCount > 0);
            if (hiddenBadgeCount > 0) {
                overflowWidth = computeOverflowWidth(hiddenBadgeCount);
                overflowHeight = computeOverflowHeight();
                overflowX = x;
                overflowY = alignCrossAxis(availableHeight, overflowHeight);
                overflowBadge.setSelfPosition(overflowX, overflowY);
                overflowBadge.setSize(overflowWidth, overflowHeight);
            } else {
                overflowWidth = 0;
                overflowHeight = 0;
            }
        } finally {
            recomputingLayout = false;
        }
    }

    @Override
    protected void onSizeUpdate() {
        recomputeLayout();
    }

    @Override
    protected void onChildSelfPositionUpdate(Widget child) {
        if (!recomputingLayout && !renderingTooltip) {
            recomputeLayoutAndSize();
        }
    }

    @Override
    protected void onChildSizeUpdate(Widget child) {
        if (!recomputingLayout && !renderingTooltip) {
            recomputeLayoutAndSize();
        }
    }

    @Override
    public void drawInBackground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        recomputeLayout();
        drawBackgroundTexture(graphics, mouseX, mouseY);
        drawRowBackground(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawInForeground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        drawTooltipTexts(mouseX, mouseY);
        drawRowForeground(graphics, mouseX, mouseY, partialTicks);
        drawOverflowTooltip(graphics, mouseX, mouseY, partialTicks);
    }

    private void drawRowBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        for (int i = 0; i < visibleBadgeCount && i < badges.size(); i++) {
            drawBadgeBackground(badges.get(i), graphics, mouseX, mouseY, partialTicks);
        }

        if (hiddenBadgeCount > 0) {
            drawOverflowBadge(graphics, mouseX, mouseY);
        }
    }

    private void drawRowForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        for (int i = 0; i < visibleBadgeCount && i < badges.size(); i++) {
            drawBadgeForeground(badges.get(i), graphics, mouseX, mouseY, partialTicks);
        }

        // Overflow text is drawn in the background phase together with its badge.
        // LDLib foreground rendering can be skipped/reordered when hover moves
        // across WidgetGroups or outside the game window, which made (+N) text flicker.
    }

    private void drawBadgeBackground(ShopBadgeWidget badge, GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        if (badge.inAnimate()) {
            badge.getAnimation().drawInBackground(graphics, mouseX, mouseY, partialTicks);
        } else {
            badge.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        }
    }

    private void drawBadgeForeground(ShopBadgeWidget badge, GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        if (badge.inAnimate()) {
            badge.getAnimation().drawInForeground(graphics, mouseX, mouseY, partialTicks);
        } else {
            badge.drawInForeground(graphics, mouseX, mouseY, partialTicks);
        }
    }

    private void drawOverflowBadge(GuiGraphics graphics, int mouseX, int mouseY) {
        drawOverflowBadgeBackground(graphics, mouseX, mouseY);
        drawOverflowBadgeText(graphics);
    }

    private void drawOverflowBadgeBackground(GuiGraphics graphics, int mouseX, int mouseY) {
        int x = getPositionX() + overflowX;
        int y = getPositionY() + overflowY;
        new ColorRectTexture(overflowBadge.fillColor)
                .setRadius(overflowBadge.radius)
                .draw(graphics, mouseX, mouseY, x, y, overflowWidth, overflowHeight);
    }

    private void drawOverflowBadgeText(GuiGraphics graphics) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();

        Font font = Minecraft.getInstance().font;
        String text = getOverflowText(hiddenBadgeCount);
        int padding = getOverflowPadding();
        float drawScale = Math.max(0.01f, scale);
        float x = getPositionX() + overflowX + padding;
        float y = getPositionY() + overflowY + Math.max(0, (overflowHeight - font.lineHeight * drawScale) / 2f);

        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);
        graphics.pose().scale(drawScale, drawScale, 1.0f);
        graphics.drawString(font, text, 0, 0, overflowBadge.textColor, false);
        graphics.pose().popPose();
    }

    private int computeVisibleBadgeCount(int limit) {
        int count = badges.size();
        if (count == 0) return 0;
        if (limit == Integer.MAX_VALUE || fits(count, 0, limit)) return count;

        for (int visible = count - 1; visible >= 0; visible--) {
            int hidden = count - visible;
            overflowBadge.setText(Component.literal("(+" + hidden + ")"));
            if (fits(visible, hidden, limit)) {
                return visible;
            }
        }

        return 0;
    }

    private boolean fits(int visibleCount, int hiddenCount, int limit) {
        return getRowWidth(visibleCount, hiddenCount) <= limit;
    }

    private int getRowWidth(int visibleCount, int hiddenCount) {
        int width = 0;
        int elementCount = 0;

        for (int i = 0; i < visibleCount && i < badges.size(); i++) {
            width += badges.get(i).getSizeWidth();
            elementCount++;
        }

        if (hiddenCount > 0) {
            width += computeOverflowWidth(hiddenCount);
            elementCount++;
        }

        if (elementCount > 1) {
            width += getScaledSpacing() * (elementCount - 1);
        }

        return width;
    }

    private String getOverflowText(int hiddenCount) {
        return "(+" + hiddenCount + ")";
    }

    private int computeOverflowWidth(int hiddenCount) {
        return Math.max(1, Math.round(Minecraft.getInstance().font.width(getOverflowText(hiddenCount)) * scale) + getOverflowPadding() * 2);
    }

    private int computeOverflowHeight() {
        return Math.max(1, Math.round(Minecraft.getInstance().font.lineHeight * scale) + getOverflowPadding() * 2);
    }

    private int getOverflowPadding() {
        return Math.max(0, Math.round(overflowBadge.textPadding * scale));
    }

    private int getContentLengthLimit() {
        int limit = maxLength > 0 ? maxLength : 0;
        if (limit <= 0 && getSizeWidth() > 0) {
            limit = getSizeWidth();
        }
        if (getParent() != null) {
            int parentAvailableWidth = getParent().getSizeWidth() - getSelfPositionX();
            if (parentAvailableWidth > 0) {
                limit = limit > 0 ? Math.min(limit, parentAvailableWidth) : parentAvailableWidth;
            }
        }
        if (limit <= 0) return Integer.MAX_VALUE;
        return Math.max(0, limit - getScaledPaddingLeft() - getScaledPaddingRight());
    }

    private int alignCrossAxis(int availableHeight, int childHeight) {
        return switch (crossAxisAlignment) {
            case START -> getScaledPaddingTop();
            case CENTER -> getScaledPaddingTop() + Math.max(0, (availableHeight - childHeight) / 2);
            case END -> getScaledPaddingTop() + Math.max(0, availableHeight - childHeight);
        };
    }

    private int alignMainAxis(int extraSpace) {
        return switch (mainAxisAlignment) {
            case START -> 0;
            case CENTER -> Math.max(0, extraSpace / 2);
            case END -> Math.max(0, extraSpace);
        };
    }

    private int getScaledSpacing() {
        return Math.max(0, Math.round(spacing * scale));
    }

    private int getScaledPaddingLeft() {
        return Math.max(0, Math.round(paddingLeft * scale));
    }

    private int getScaledPaddingTop() {
        return Math.max(0, Math.round(paddingTop * scale));
    }

    private int getScaledPaddingRight() {
        return Math.max(0, Math.round(paddingRight * scale));
    }

    private int getScaledPaddingBottom() {
        return Math.max(0, Math.round(paddingBottom * scale));
    }

    private int getScaledTooltipPadding() {
        return scaleTooltipDimension(tooltipPadding);
    }

    private int getScaledTooltipSpacing() {
        return scaleTooltipDimension(tooltipSpacing);
    }

    private int getScaledTooltipOffsetX() {
        return scaleTooltipWithHBox ? Math.round(tooltipOffsetX * scale) : tooltipOffsetX;
    }

    private int getScaledTooltipOffsetY() {
        return scaleTooltipWithHBox ? Math.round(tooltipOffsetY * scale) : tooltipOffsetY;
    }

    private int scaleTooltipDimension(int value) {
        return Math.max(0, scaleTooltipWithHBox ? Math.round(value * scale) : value);
    }

    private void recomputeLayoutAndSize() {
        recomputeLayout();
        recomputeSize();
    }

    private void drawOverflowTooltip(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (hiddenBadgeCount <= 0 || !isMouseOverOverflow(mouseX, mouseY)) {
            return;
        }

        TooltipBounds bounds = computeTooltipBounds(mouseX, mouseY);
        if (bounds == null) return;

        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 300);
        new ColorRectAndBorderTexture(tooltipBackgroundColor, tooltipBorderColor, 1)
                .setRadius(3)
                .draw(graphics, mouseX, mouseY, bounds.x, bounds.y, bounds.width, bounds.height);

        renderingTooltip = true;
        try {
            int y = bounds.y + getScaledTooltipPadding();
            for (int i = visibleBadgeCount; i < badges.size(); i++) {
                ShopBadgeWidget badge = badges.get(i);
                int tooltipBadgeHeight = getTooltipBadgeHeight(badge);
                BadgeRenderState state = captureState(badge);
                try {
                    if (!scaleTooltipWithHBox) {
                        badge.setScale(1.0f);
                    }
                    badge.setVisible(true);
                    badge.setSelfPosition(bounds.x + getScaledTooltipPadding() - getPositionX(), y - getPositionY());
                    badge.drawInBackground(graphics, mouseX, mouseY, partialTicks);
                    badge.drawInForeground(graphics, mouseX, mouseY, partialTicks);
                } finally {
                    restoreState(badge, state);
                }
                y += tooltipBadgeHeight + getScaledTooltipSpacing();
            }
        } finally {
            renderingTooltip = false;
            graphics.pose().popPose();
        }
    }

    private boolean isMouseOverOverflow(double mouseX, double mouseY) {
        return isMouseOver(
                getPositionX() + overflowX,
                getPositionY() + overflowY,
                overflowWidth,
                overflowHeight,
                mouseX,
                mouseY
        );
    }

    @Nullable
    private TooltipBounds computeTooltipBounds(int mouseX, int mouseY) {
        int contentWidth = 0;
        int contentHeight = 0;
        int tooltipBadgeCount = 0;

        for (int i = visibleBadgeCount; i < badges.size(); i++) {
            ShopBadgeWidget badge = badges.get(i);
            contentWidth = Math.max(contentWidth, getTooltipBadgeWidth(badge));
            contentHeight += getTooltipBadgeHeight(badge);
            tooltipBadgeCount++;
        }

        if (tooltipBadgeCount == 0) return null;
        if (tooltipBadgeCount > 1) {
            contentHeight += getScaledTooltipSpacing() * (tooltipBadgeCount - 1);
        }

        int width = contentWidth + getScaledTooltipPadding() * 2;
        int height = contentHeight + getScaledTooltipPadding() * 2;
        int x = mouseX + getScaledTooltipOffsetX();
        int y = mouseY + getScaledTooltipOffsetY();

        Minecraft minecraft = Minecraft.getInstance();
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        if (x + width > screenWidth) {
            x = Math.max(0, mouseX - getScaledTooltipOffsetX() - width);
        }
        if (y + height > screenHeight) {
            y = Math.max(0, screenHeight - height);
        }

        return new TooltipBounds(x, y, width, height);
    }

    private int getTooltipBadgeWidth(ShopBadgeWidget badge) {
        if (scaleTooltipWithHBox || scale == 0f) {
            return badge.getSizeWidth();
        }
        return Math.max(1, Math.round(badge.getSizeWidth() / scale));
    }

    private int getTooltipBadgeHeight(ShopBadgeWidget badge) {
        if (scaleTooltipWithHBox || scale == 0f) {
            return badge.getSizeHeight();
        }
        return Math.max(1, Math.round(badge.getSizeHeight() / scale));
    }

    private BadgeRenderState captureState(ShopBadgeWidget badge) {
        return new BadgeRenderState(
                badge.getSelfPositionX(),
                badge.getSelfPositionY(),
                badge.isVisible(),
                badge.getScale()
        );
    }

    private void restoreState(ShopBadgeWidget badge, BadgeRenderState state) {
        badge.setScale(state.scale);
        badge.setSelfPosition(state.x, state.y);
        badge.setVisible(state.visible);
    }

    private static class BadgeRenderState {
        private final int x;
        private final int y;
        private final boolean visible;
        private final float scale;

        private BadgeRenderState(int x, int y, boolean visible, float scale) {
            this.x = x;
            this.y = y;
            this.visible = visible;
            this.scale = scale;
        }
    }

    private static class TooltipBounds {
        private final int x;
        private final int y;
        private final int width;
        private final int height;

        private TooltipBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
}
