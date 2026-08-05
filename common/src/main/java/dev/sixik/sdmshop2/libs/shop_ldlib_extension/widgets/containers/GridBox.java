package dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;

public class GridBox extends WidgetGroup {

    public enum CellAlignment {
        START,
        CENTER,
        END,
        STRETCH
    }

    protected int columns;

    protected int fixedCellWidth = -1;
    protected int fixedCellHeight = -1;

    protected int spacingX;
    protected int spacingY;

    protected int paddingLeft;
    protected int paddingTop;
    protected int paddingRight;
    protected int paddingBottom;

    protected CellAlignment horizontalAlignment = CellAlignment.START;
    protected CellAlignment verticalAlignment = CellAlignment.START;

    private boolean recomputingLayout;

    public GridBox() {
        this(1);
    }

    public GridBox(int columns) {
        this(columns, Position.ORIGIN, Size.ZERO);
    }

    public GridBox(int columns, Position selfPosition) {
        this(columns, selfPosition, Size.ZERO);
    }

    public GridBox(int columns, Position selfPosition, Size size) {
        super(selfPosition, size);
        this.columns = Math.max(1, columns);
        setDynamicSized(size.equals(Size.ZERO));
    }

    public GridBox(int columns, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.columns = Math.max(1, columns);
        setDynamicSized(width == 0 && height == 0);
    }

    public GridBox setColumns(int columns) {
        this.columns = Math.max(1, columns);
        recomputeLayoutAndSize();
        return this;
    }

    public GridBox setCellSize(int cellWidth, int cellHeight) {
        this.fixedCellWidth = Math.max(0, cellWidth);
        this.fixedCellHeight = Math.max(0, cellHeight);
        recomputeLayoutAndSize();
        return this;
    }

    public GridBox setCellWidth(int cellWidth) {
        this.fixedCellWidth = Math.max(0, cellWidth);
        recomputeLayoutAndSize();
        return this;
    }

    public GridBox setCellHeight(int cellHeight) {
        this.fixedCellHeight = Math.max(0, cellHeight);
        recomputeLayoutAndSize();
        return this;
    }

    public GridBox autoCellSize() {
        this.fixedCellWidth = -1;
        this.fixedCellHeight = -1;
        recomputeLayoutAndSize();
        return this;
    }

    public GridBox setSpacing(int spacing) {
        return setSpacing(spacing, spacing);
    }

    public GridBox setSpacing(int spacingX, int spacingY) {
        this.spacingX = Math.max(0, spacingX);
        this.spacingY = Math.max(0, spacingY);
        recomputeLayoutAndSize();
        return this;
    }

    public GridBox setPadding(int padding) {
        return setPadding(padding, padding, padding, padding);
    }

    public GridBox setPadding(int horizontal, int vertical) {
        return setPadding(horizontal, vertical, horizontal, vertical);
    }

    public GridBox setPadding(int left, int top, int right, int bottom) {
        this.paddingLeft = Math.max(0, left);
        this.paddingTop = Math.max(0, top);
        this.paddingRight = Math.max(0, right);
        this.paddingBottom = Math.max(0, bottom);
        recomputeLayoutAndSize();
        return this;
    }

    public GridBox setAlignment(CellAlignment horizontalAlignment, CellAlignment verticalAlignment) {
        this.horizontalAlignment = horizontalAlignment == null ? CellAlignment.START : horizontalAlignment;
        this.verticalAlignment = verticalAlignment == null ? CellAlignment.START : verticalAlignment;
        recomputeLayout();
        return this;
    }

    public GridBox setHorizontalAlignment(CellAlignment alignment) {
        this.horizontalAlignment = alignment == null ? CellAlignment.START : alignment;
        recomputeLayout();
        return this;
    }

    public GridBox setVerticalAlignment(CellAlignment alignment) {
        this.verticalAlignment = alignment == null ? CellAlignment.START : alignment;
        recomputeLayout();
        return this;
    }

    public GridBox alignStart() {
        return setAlignment(CellAlignment.START, CellAlignment.START);
    }

    public GridBox alignCenter() {
        return setAlignment(CellAlignment.CENTER, CellAlignment.CENTER);
    }

    public GridBox alignEnd() {
        return setAlignment(CellAlignment.END, CellAlignment.END);
    }

    public GridBox stretchCells() {
        return setAlignment(CellAlignment.STRETCH, CellAlignment.STRETCH);
    }

    @Override
    public GridBox addWidget(Widget widget) {
        super.addWidget(widget);
        return this;
    }

    @Override
    public GridBox addWidget(int index, Widget widget) {
        super.addWidget(index, widget);
        return this;
    }

    @Override
    public GridBox addWidgets(Widget... widgets) {
        for (Widget widget : widgets) {
            addWidget(widget);
        }
        return this;
    }

    @Override
    protected void recomputeLayout() {
        if (recomputingLayout) return;
        recomputingLayout = true;

        try {
            int cellWidth = resolveCellWidth();
            int cellHeight = resolveCellHeight();
            int visibleIndex = 0;

            for (Widget widget : widgets) {
                if (!widget.isVisible()) continue;

                int col = visibleIndex % columns;
                int row = visibleIndex / columns;
                int cellX = paddingLeft + col * (cellWidth + spacingX);
                int cellY = paddingTop + row * (cellHeight + spacingY);

                if (horizontalAlignment == CellAlignment.STRETCH || verticalAlignment == CellAlignment.STRETCH) {
                    int targetWidth = horizontalAlignment == CellAlignment.STRETCH ? cellWidth : widget.getSizeWidth();
                    int targetHeight = verticalAlignment == CellAlignment.STRETCH ? cellHeight : widget.getSizeHeight();
                    widget.setSize(targetWidth, targetHeight);
                }

                widget.setSelfPosition(
                        alignInCell(cellX, cellWidth, widget.getSizeWidth(), horizontalAlignment),
                        alignInCell(cellY, cellHeight, widget.getSizeHeight(), verticalAlignment)
                );

                visibleIndex++;
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
        int visibleWidgets = getVisibleWidgets();
        int usedColumns = visibleWidgets == 0 ? 0 : Math.min(columns, visibleWidgets);
        int usedRows = visibleWidgets == 0 ? 0 : (visibleWidgets + columns - 1) / columns;
        int cellWidth = resolveCellWidth();
        int cellHeight = resolveCellHeight();

        int width = paddingLeft + paddingRight + usedColumns * cellWidth + Math.max(0, usedColumns - 1) * spacingX;
        int height = paddingTop + paddingBottom + usedRows * cellHeight + Math.max(0, usedRows - 1) * spacingY;
        return new Size(width, height);
    }

    private int resolveCellWidth() {
        if (fixedCellWidth >= 0) {
            return fixedCellWidth;
        }

        int maxWidth = 0;
        for (Widget widget : widgets) {
            if (widget.isVisible()) {
                maxWidth = Math.max(maxWidth, widget.getSizeWidth());
            }
        }
        return maxWidth;
    }

    private int resolveCellHeight() {
        if (fixedCellHeight >= 0) {
            return fixedCellHeight;
        }

        int maxHeight = 0;
        for (Widget widget : widgets) {
            if (widget.isVisible()) {
                maxHeight = Math.max(maxHeight, widget.getSizeHeight());
            }
        }
        return maxHeight;
    }

    private int getVisibleWidgets() {
        int visibleWidgets = 0;
        for (Widget widget : widgets) {
            if (widget.isVisible()) {
                visibleWidgets++;
            }
        }
        return visibleWidgets;
    }

    private int alignInCell(int cellStart, int cellSize, int widgetSize, CellAlignment alignment) {
        return switch (alignment) {
            case CENTER -> cellStart + Math.max(0, (cellSize - widgetSize) / 2);
            case END -> cellStart + Math.max(0, cellSize - widgetSize);
            case START, STRETCH -> cellStart;
        };
    }

    private void recomputeLayoutAndSize() {
        recomputeLayout();
        recomputeSize();
    }
}
