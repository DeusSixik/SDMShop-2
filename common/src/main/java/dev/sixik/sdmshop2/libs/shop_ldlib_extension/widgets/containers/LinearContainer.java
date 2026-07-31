package dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.DialogWidget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.PriceWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.TextLabel;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.IdentityHashMap;
import java.util.Map;

public abstract class LinearContainer<T extends LinearContainer<T>> extends WidgetGroup {

    public enum CrossAxisAlignment {
        START,
        CENTER,
        END
    }

    protected final boolean horizontal;

    protected int spacing;
    protected int paddingLeft;
    protected int paddingTop;
    protected int paddingRight;
    protected int paddingBottom;

    protected CrossAxisAlignment crossAxisAlignment = CrossAxisAlignment.START;
    protected float scale = 1.0f;
    protected final Map<Widget, Integer> spacingAfterOverrides = new IdentityHashMap<>();
    protected boolean pushLastElementToEnd;

    private boolean recomputingLayout;

    protected LinearContainer(boolean horizontal) {
        this(horizontal, Position.ORIGIN, Size.ZERO);
    }

    protected LinearContainer(boolean horizontal, Position selfPosition) {
        this(horizontal, selfPosition, Size.ZERO);
    }

    protected LinearContainer(boolean horizontal, Position selfPosition, Size size) {
        super(selfPosition, size);
        this.horizontal = horizontal;
        setDynamicSized(size.equals(Size.ZERO));
    }

    protected LinearContainer(boolean horizontal, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.horizontal = horizontal;
        setDynamicSized(width == 0 && height == 0);
    }

    protected abstract T self();

    public T setSpacing(int spacing) {
        this.spacing = Math.max(0, spacing);
        recomputeLayoutAndSize();
        return self();
    }

    public T setSpacingAfter(Widget widget, int spacing) {
        if (widget == null) return self();

        spacingAfterOverrides.put(widget, Math.max(0, spacing));
        recomputeLayoutAndSize();
        return self();
    }

    public T setSpacingAfter(int index, int spacing) {
        if (index < 0 || index >= widgets.size()) return self();
        return setSpacingAfter(widgets.get(index), spacing);
    }

    public T setSpacingAfterEach(int... spacingByIndex) {
        spacingAfterOverrides.clear();
        if (spacingByIndex != null) {
            int limit = Math.min(spacingByIndex.length, widgets.size());
            for (int i = 0; i < limit; i++) {
                spacingAfterOverrides.put(widgets.get(i), Math.max(0, spacingByIndex[i]));
            }
        }
        recomputeLayoutAndSize();
        return self();
    }

    public T setGapAfterEach(int... gapByIndex) {
        return setSpacingAfterEach(gapByIndex);
    }

    public T setGapAfter(Widget widget, int gap) {
        return setSpacingAfter(widget, gap);
    }

    public T setGapAfter(int index, int gap) {
        return setSpacingAfter(index, gap);
    }

    public T clearSpacingAfter(Widget widget) {
        if (widget == null) return self();

        spacingAfterOverrides.remove(widget);
        recomputeLayoutAndSize();
        return self();
    }

    public T clearSpacingAfter() {
        spacingAfterOverrides.clear();
        recomputeLayoutAndSize();
        return self();
    }

    public T clearGapAfter() {
        return clearSpacingAfter();
    }

    public T setPushLastElementToEnd(boolean pushLastElementToEnd) {
        this.pushLastElementToEnd = pushLastElementToEnd;
        recomputeLayout();
        return self();
    }

    public T pushLastElementToEnd() {
        return setPushLastElementToEnd(true);
    }

    public T keepLastElementInFlow() {
        return setPushLastElementToEnd(false);
    }

    public T setLastElementToEnd(boolean lastElementToEnd) {
        return setPushLastElementToEnd(lastElementToEnd);
    }

    public T lastElementToEnd() {
        return pushLastElementToEnd();
    }

    public boolean isPushLastElementToEnd() {
        return pushLastElementToEnd;
    }

    public T setPadding(int padding) {
        return setPadding(padding, padding, padding, padding);
    }

    public T setPadding(int horizontal, int vertical) {
        return setPadding(horizontal, vertical, horizontal, vertical);
    }

    public T setPadding(int left, int top, int right, int bottom) {
        this.paddingLeft = Math.max(0, left);
        this.paddingTop = Math.max(0, top);
        this.paddingRight = Math.max(0, right);
        this.paddingBottom = Math.max(0, bottom);
        recomputeLayoutAndSize();
        return self();
    }

    public T setCrossAxisAlignment(CrossAxisAlignment alignment) {
        this.crossAxisAlignment = alignment == null ? CrossAxisAlignment.START : alignment;
        recomputeLayout();
        return self();
    }

    public T alignStart() {
        return setCrossAxisAlignment(CrossAxisAlignment.START);
    }

    public T alignCenter() {
        return setCrossAxisAlignment(CrossAxisAlignment.CENTER);
    }

    public T alignEnd() {
        return setCrossAxisAlignment(CrossAxisAlignment.END);
    }

    public T setScale(float scale) {
        this.scale = Math.max(0.01f, scale);
        return self();
    }

    public T scale(float scale) {
        return setScale(scale);
    }

    public float getScale() {
        return scale;
    }

    public int getScaledWidth() {
        return Math.max(1, Math.round(getSizeWidth() * scale));
    }

    public int getScaledHeight() {
        return Math.max(1, Math.round(getSizeHeight() * scale));
    }

    @Override
    public T addWidget(Widget widget) {
        super.addWidget(widget);
        recomputeLayoutAndSize();
        return self();
    }

    @Override
    public T addWidget(int index, Widget widget) {
        super.addWidget(index, widget);
        recomputeLayoutAndSize();
        return self();
    }

    @Override
    public T addWidgets(Widget... widgets) {
        for (Widget widget : widgets) {
            addWidget(widget);
        }
        return self();
    }

    @Override
    public void removeWidget(Widget widget) {
        spacingAfterOverrides.remove(widget);
        super.removeWidget(widget);
        recomputeLayoutAndSize();
    }

    @Override
    public void clearAllWidgets() {
        spacingAfterOverrides.clear();
        super.clearAllWidgets();
        recomputeLayoutAndSize();
    }

    @Override
    protected void recomputeLayout() {
        if (recomputingLayout) return;
        recomputingLayout = true;

        try {
            if (horizontal) {
                recomputeHorizontalLayout();
            } else {
                recomputeVerticalLayout();
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
        if (!recomputingLayout) {
            recomputeLayoutAndSize();
        }
    }

    @Override
    protected void onChildSizeUpdate(Widget child) {
        if (!recomputingLayout) {
            recomputeLayoutAndSize();
        }
    }

    @Override
    protected Size computeDynamicSize() {
        return horizontal ? computeHorizontalSize() : computeVerticalSize();
    }

    @Override
    public boolean isMouseOverElement(double mouseX, double mouseY) {
        int x = getPositionX();
        int y = getPositionY();
        return mouseX >= x
                && mouseY >= y
                && mouseX < x + getScaledWidth()
                && mouseY < y + getScaledHeight();
    }

    @Override
    public @Nullable Widget getHoverElement(double mouseX, double mouseY) {
        if (!isVisible() || !isMouseOverElement(mouseX, mouseY)) return null;

        double scaledMouseX = toUnscaledMouseX(mouseX);
        double scaledMouseY = toUnscaledMouseY(mouseY);
        for (int i = widgets.size() - 1; i >= 0; i--) {
            Widget widget = widgets.get(i);
            if (!widget.isVisible()) continue;

            Widget hovered = widget.getHoverElement(scaledMouseX, scaledMouseY);
            if (hovered != null) {
                return hovered;
            }
        }

        return this;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return isMouseOverElement(mouseX, mouseY)
                && super.mouseClicked(toUnscaledMouseX(mouseX), toUnscaledMouseY(mouseY), button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (!isMouseOverElement(mouseX, mouseY)) return false;
        return super.mouseDragged(
                toUnscaledMouseX(mouseX),
                toUnscaledMouseY(mouseY),
                button,
                dragX / scale,
                dragY / scale
        );
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(toUnscaledMouseX(mouseX), toUnscaledMouseY(mouseY), button);
    }

    @Override
    public boolean mouseWheelMove(double mouseX, double mouseY, double wheelDelta) {
        return isMouseOverElement(mouseX, mouseY)
                && super.mouseWheelMove(toUnscaledMouseX(mouseX), toUnscaledMouseY(mouseY), wheelDelta);
    }

    @Override
    public boolean mouseMoved(double mouseX, double mouseY) {
        return isMouseOverElement(mouseX, mouseY)
                && super.mouseMoved(toUnscaledMouseX(mouseX), toUnscaledMouseY(mouseY));
    }

    @Override
    public void drawInBackground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (scale == 1.0f) {
            super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
            return;
        }

        drawOwnBackgroundScaled(graphics, mouseX, mouseY);
        drawWidgetsScaled(graphics, mouseX, mouseY, partialTicks, getPositionX(), getPositionY(), scale, DrawPass.BACKGROUND);
    }

    @Override
    public void drawInForeground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (scale == 1.0f) {
            super.drawInForeground(graphics, mouseX, mouseY, partialTicks);
            return;
        }

        drawTooltipTexts(mouseX, mouseY);
        drawWidgetsScaled(graphics, mouseX, mouseY, partialTicks, getPositionX(), getPositionY(), scale, DrawPass.FOREGROUND);
    }

    @Override
    public void drawOverlay(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (scale == 1.0f) {
            super.drawOverlay(graphics, mouseX, mouseY, partialTicks);
            return;
        }

        drawOwnOverlayScaled(graphics, mouseX, mouseY);
        drawWidgetsScaled(graphics, mouseX, mouseY, partialTicks, getPositionX(), getPositionY(), scale, DrawPass.OVERLAY);
    }

    private void recomputeHorizontalLayout() {
        int x = paddingLeft;
        int availableHeight = Math.max(0, getSizeHeight() - paddingTop - paddingBottom);
        Widget lastVisibleWidget = getLastVisibleWidget();

        for (Widget widget : widgets) {
            if (!widget.isVisible()) continue;

            if (pushLastElementToEnd && widget == lastVisibleWidget) {
                x = Math.max(x, getSizeWidth() - paddingRight - widget.getSizeWidth());
            }

            int y = alignCrossAxis(paddingTop, availableHeight, widget.getSizeHeight());
            widget.setSelfPosition(x, y);
            x += widget.getSizeWidth() + getSpacingAfter(widget);
        }
    }

    private void recomputeVerticalLayout() {
        int y = paddingTop;
        int availableWidth = Math.max(0, getSizeWidth() - paddingLeft - paddingRight);
        Widget lastVisibleWidget = getLastVisibleWidget();

        for (Widget widget : widgets) {
            if (!widget.isVisible()) continue;

            if (pushLastElementToEnd && widget == lastVisibleWidget) {
                y = Math.max(y, getSizeHeight() - paddingBottom - widget.getSizeHeight());
            }

            int x = alignCrossAxis(paddingLeft, availableWidth, widget.getSizeWidth());
            widget.setSelfPosition(x, y);
            y += widget.getSizeHeight() + getSpacingAfter(widget);
        }
    }

    private int alignCrossAxis(int start, int availableSize, int widgetSize) {
        return switch (crossAxisAlignment) {
            case CENTER -> start + Math.max(0, (availableSize - widgetSize) / 2);
            case END -> start + Math.max(0, availableSize - widgetSize);
            case START -> start;
        };
    }

    private Size computeHorizontalSize() {
        int width = paddingLeft + paddingRight;
        int height = paddingTop + paddingBottom;
        int maxChildHeight = 0;
        Widget previousVisibleWidget = null;

        for (Widget widget : widgets) {
            if (!widget.isVisible()) continue;

            if (previousVisibleWidget != null) {
                width += getSpacingAfter(previousVisibleWidget);
            }
            width += widget.getSizeWidth();
            maxChildHeight = Math.max(maxChildHeight, widget.getSizeHeight());
            previousVisibleWidget = widget;
        }

        height += maxChildHeight;
        return new Size(width, height);
    }

    private Size computeVerticalSize() {
        int width = paddingLeft + paddingRight;
        int height = paddingTop + paddingBottom;
        int maxChildWidth = 0;
        Widget previousVisibleWidget = null;

        for (Widget widget : widgets) {
            if (!widget.isVisible()) continue;

            if (previousVisibleWidget != null) {
                height += getSpacingAfter(previousVisibleWidget);
            }
            height += widget.getSizeHeight();
            maxChildWidth = Math.max(maxChildWidth, widget.getSizeWidth());
            previousVisibleWidget = widget;
        }

        width += maxChildWidth;
        return new Size(width, height);
    }

    private int getSpacingAfter(Widget widget) {
        return spacingAfterOverrides.getOrDefault(widget, spacing);
    }

    private @Nullable Widget getLastVisibleWidget() {
        for (int i = widgets.size() - 1; i >= 0; i--) {
            Widget widget = widgets.get(i);
            if (widget.isVisible()) {
                return widget;
            }
        }
        return null;
    }

    private void recomputeLayoutAndSize() {
        recomputeLayout();
        recomputeSize();
    }

    private void drawOwnBackgroundScaled(@NonNull GuiGraphics graphics, int mouseX, int mouseY) {
        drawOwnBackgroundAt(graphics, mouseX, mouseY, getPositionX(), getPositionY(), scale);
    }

    private void drawOwnOverlayScaled(@NonNull GuiGraphics graphics, int mouseX, int mouseY) {
        drawOwnOverlayAt(graphics, mouseX, mouseY, getPositionX(), getPositionY(), scale);
    }

    private void drawWidgetsScaled(
            @NonNull GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTicks,
            int baseVisualX,
            int baseVisualY,
            float effectiveScale,
            DrawPass pass
    ) {
        DialogWidget dialogWidget = pass == DrawPass.FOREGROUND ? findTopDialogWidget() : null;

        for (Widget widget : widgets) {
            if (!widget.isVisible()) continue;
            if (dialogWidget != null && widget != dialogWidget) continue;

            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.enableBlend();
            drawWidgetScaled(graphics, widget, mouseX, mouseY, partialTicks, baseVisualX, baseVisualY, effectiveScale, pass);
        }
    }

    private @Nullable DialogWidget findTopDialogWidget() {
        for (int i = widgets.size() - 1; i >= 0; i--) {
            if (widgets.get(i) instanceof DialogWidget dialogWidget) {
                return dialogWidget;
            }
        }
        return null;
    }

    private void drawWidgetScaled(
            @NonNull GuiGraphics graphics,
            Widget widget,
            int mouseX,
            int mouseY,
            float partialTicks,
            int baseVisualX,
            int baseVisualY,
            float effectiveScale,
            DrawPass pass
    ) {
        int scaledX = baseVisualX + Math.round(widget.getSelfPositionX() * effectiveScale);
        int scaledY = baseVisualY + Math.round(widget.getSelfPositionY() * effectiveScale);
        int scaledWidth = Math.max(1, Math.round(widget.getSizeWidth() * effectiveScale));
        int scaledHeight = Math.max(1, Math.round(widget.getSizeHeight() * effectiveScale));

        if (widget instanceof LinearContainer<?> linearContainer) {
            linearContainer.drawLinearContainerAt(graphics, mouseX, mouseY, partialTicks, scaledX, scaledY, effectiveScale, pass);
            return;
        }

        if (pass == DrawPass.BACKGROUND && widget instanceof TextLabel textLabel) {
            textLabel.drawInBackgroundAt(graphics, mouseX, mouseY, scaledX, scaledY, scaledWidth, scaledHeight, effectiveScale);
            return;
        }

        if (pass == DrawPass.BACKGROUND && widget instanceof PriceWidget priceWidget) {
            priceWidget.drawInBackgroundAt(graphics, scaledX, scaledY, scaledWidth, scaledHeight, effectiveScale);
            return;
        }

        if (pass == DrawPass.BACKGROUND && widget instanceof ButtonWidget buttonWidget) {
            buttonWidget.drawInBackgroundAt(graphics, mouseX, mouseY, scaledX, scaledY, scaledWidth, scaledHeight, effectiveScale);
            return;
        }

        withScaledBounds(graphics, scaledX, scaledY, widget.getPositionX(), widget.getPositionY(), effectiveScale, () -> {
            int scaledMouseX = toWidgetMouseX(widget, scaledX, effectiveScale, mouseX);
            int scaledMouseY = toWidgetMouseY(widget, scaledY, effectiveScale, mouseY);

            drawWidgetPass(widget, graphics, scaledMouseX, scaledMouseY, partialTicks, pass);
        });
    }

    private void drawLinearContainerAt(
            @NonNull GuiGraphics graphics,
            int mouseX,
            int mouseY,
            float partialTicks,
            int visualX,
            int visualY,
            float parentScale,
            DrawPass pass
    ) {
        float effectiveScale = parentScale * scale;

        switch (pass) {
            case BACKGROUND -> drawOwnBackgroundAt(graphics, mouseX, mouseY, visualX, visualY, effectiveScale);
            case FOREGROUND -> {
                // Tooltip routing still uses the logical widget tree; children are drawn below with the same effective scale.
            }
            case OVERLAY -> drawOwnOverlayAt(graphics, mouseX, mouseY, visualX, visualY, effectiveScale);
        }

        drawWidgetsScaled(graphics, mouseX, mouseY, partialTicks, visualX, visualY, effectiveScale, pass);
    }

    private void drawWidgetPass(Widget widget, @NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, DrawPass pass) {
        switch (pass) {
            case BACKGROUND -> {
                if (widget.inAnimate()) {
                    widget.getAnimation().drawInBackground(graphics, mouseX, mouseY, partialTicks);
                } else {
                    widget.drawInBackground(graphics, mouseX, mouseY, partialTicks);
                }
            }
            case FOREGROUND -> {
                if (widget.inAnimate()) {
                    widget.getAnimation().drawInForeground(graphics, mouseX, mouseY, partialTicks);
                } else {
                    widget.drawInForeground(graphics, mouseX, mouseY, partialTicks);
                }
            }
            case OVERLAY -> widget.drawOverlay(graphics, mouseX, mouseY, partialTicks);
        }
    }

    private void drawOwnBackgroundAt(@NonNull GuiGraphics graphics, int mouseX, int mouseY, int visualX, int visualY, float effectiveScale) {
        int visualWidth = Math.max(1, Math.round(getSizeWidth() * effectiveScale));
        int visualHeight = Math.max(1, Math.round(getSizeHeight() * effectiveScale));
        boolean hovered = isMouseOver(visualX, visualY, visualWidth, visualHeight, mouseX, mouseY);

        if (backgroundTexture != null && (!hovered || drawBackgroundWhenHover)) {
            backgroundTexture.draw(graphics, mouseX, mouseY, visualX, visualY, visualWidth, visualHeight);
        }
        if (hoverTexture != null && hovered && isActive()) {
            hoverTexture.draw(graphics, mouseX, mouseY, visualX, visualY, visualWidth, visualHeight);
        }
    }

    private void drawOwnOverlayAt(@NonNull GuiGraphics graphics, int mouseX, int mouseY, int visualX, int visualY, float effectiveScale) {
        if (overlay == null) return;

        int visualWidth = Math.max(1, Math.round(getSizeWidth() * effectiveScale));
        int visualHeight = Math.max(1, Math.round(getSizeHeight() * effectiveScale));
        overlay.draw(graphics, mouseX, mouseY, visualX, visualY, visualWidth, visualHeight);
    }

    private void withScaledBounds(@NonNull GuiGraphics graphics, int visualX, int visualY, int logicalX, int logicalY, float effectiveScale, Runnable renderer) {
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        try {
            poseStack.translate(visualX - logicalX * effectiveScale, visualY - logicalY * effectiveScale, 0);
            poseStack.scale(effectiveScale, effectiveScale, 1.0f);
            renderer.run();
        } finally {
            poseStack.popPose();
        }
    }

    private int toWidgetMouseX(Widget widget, int visualX, float effectiveScale, double mouseX) {
        return (int) Math.floor(widget.getPositionX() + (mouseX - visualX) / effectiveScale);
    }

    private int toWidgetMouseY(Widget widget, int visualY, float effectiveScale, double mouseY) {
        return (int) Math.floor(widget.getPositionY() + (mouseY - visualY) / effectiveScale);
    }

    private int toUnscaledMouseX(double mouseX) {
        if (scale == 1.0f) return (int) mouseX;
        return (int) Math.floor(getPositionX() + (mouseX - getPositionX()) / scale);
    }

    private int toUnscaledMouseY(double mouseY) {
        if (scale == 1.0f) return (int) mouseY;
        return (int) Math.floor(getPositionY() + (mouseY - getPositionY()) / scale);
    }

    private enum DrawPass {
        BACKGROUND,
        FOREGROUND,
        OVERLAY
    }
}
