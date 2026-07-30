package dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;

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

    @Override
    public T addWidget(Widget widget) {
        super.addWidget(widget);
        return self();
    }

    @Override
    public T addWidget(int index, Widget widget) {
        super.addWidget(index, widget);
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

    private void recomputeHorizontalLayout() {
        int x = paddingLeft;
        int availableHeight = Math.max(0, getSizeHeight() - paddingTop - paddingBottom);

        for (Widget widget : widgets) {
            if (!widget.isVisible()) continue;

            int y = alignCrossAxis(paddingTop, availableHeight, widget.getSizeHeight());
            widget.setSelfPosition(x, y);
            x += widget.getSizeWidth() + spacing;
        }
    }

    private void recomputeVerticalLayout() {
        int y = paddingTop;
        int availableWidth = Math.max(0, getSizeWidth() - paddingLeft - paddingRight);

        for (Widget widget : widgets) {
            if (!widget.isVisible()) continue;

            int x = alignCrossAxis(paddingLeft, availableWidth, widget.getSizeWidth());
            widget.setSelfPosition(x, y);
            y += widget.getSizeHeight() + spacing;
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
        int visibleWidgets = 0;
        int maxChildHeight = 0;

        for (Widget widget : widgets) {
            if (!widget.isVisible()) continue;

            width += widget.getSizeWidth();
            maxChildHeight = Math.max(maxChildHeight, widget.getSizeHeight());
            visibleWidgets++;
        }

        if (visibleWidgets > 1) {
            width += spacing * (visibleWidgets - 1);
        }

        height += maxChildHeight;
        return new Size(width, height);
    }

    private Size computeVerticalSize() {
        int width = paddingLeft + paddingRight;
        int height = paddingTop + paddingBottom;
        int visibleWidgets = 0;
        int maxChildWidth = 0;

        for (Widget widget : widgets) {
            if (!widget.isVisible()) continue;

            height += widget.getSizeHeight();
            maxChildWidth = Math.max(maxChildWidth, widget.getSizeWidth());
            visibleWidgets++;
        }

        if (visibleWidgets > 1) {
            height += spacing * (visibleWidgets - 1);
        }

        width += maxChildWidth;
        return new Size(width, height);
    }

    private void recomputeLayoutAndSize() {
        recomputeLayout();
        recomputeSize();
    }
}
