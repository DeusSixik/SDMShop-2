package dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.table;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import org.jspecify.annotations.NonNull;

/**
 * Interaction table with row based vertical scrolling.
 * <p>
 * The table keeps all widgets in the same grid, but only renders/interacts with
 * {@link #maxVisibleRows} rows at a time. Extra rows are reachable with mouse wheel
 * or by dragging/clicking the optional scrollbar.
 */
public class ScrollableInteractionTable extends InteractionTable {

    protected int maxVisibleRows;
    protected int scrollRow;
    protected int wheelRows = 1;
    protected boolean fitHeightToContent;

    protected boolean showScrollBar = true;
    protected boolean reserveScrollBarSpace = true;
    protected int scrollBarWidth = 4;
    protected int scrollBarPadding = 1;
    protected int minScrollThumbSize = 8;
    protected int scrollTrackColor = 0x5520202A;
    protected int scrollThumbColor = 0xFF8E94A6;
    protected int scrollThumbHoverColor = 0xFFC5CAD8;

    private boolean recomputingScrollableLayout;
    private boolean draggingScrollThumb;
    private double dragStartMouseY;
    private int dragStartScrollRow;

    public ScrollableInteractionTable() {
        this(1, 1);
    }

    public ScrollableInteractionTable(int coll, int maxVisibleRows) {
        this(coll, maxVisibleRows, Position.ORIGIN, Size.ZERO);
    }

    public ScrollableInteractionTable(int coll, int maxVisibleRows, Position selfPosition, Size size) {
        super(coll, maxVisibleRows, selfPosition, size);
        this.maxVisibleRows = Math.max(1, maxVisibleRows);
        updateTableBounds();
    }

    public ScrollableInteractionTable(int coll, int maxVisibleRows, int x, int y, int width, int height) {
        super(coll, maxVisibleRows, x, y, width, height);
        this.maxVisibleRows = Math.max(1, maxVisibleRows);
        updateTableBounds();
    }

    @Override
    protected ScrollableInteractionTable self() {
        return this;
    }

    @Override
    public ScrollableInteractionTable setCollAndRow(int coll, int row) {
        return setColumnsAndMaxVisibleRows(coll, row);
    }

    public ScrollableInteractionTable setColumnsAndMaxVisibleRows(int coll, int maxVisibleRows) {
        this.coll = Math.max(1, coll);
        this.maxVisibleRows = Math.max(1, maxVisibleRows);
        this.row = this.maxVisibleRows;
        updateTableBounds();
        return this;
    }

    public ScrollableInteractionTable setColumns(int coll) {
        this.coll = Math.max(1, coll);
        updateTableBounds();
        return this;
    }

    public ScrollableInteractionTable setColumnsToFitWidth(int availableWidth) {
        return setColumns(calculateColumnsToFitWidth(availableWidth));
    }

    public int calculateColumnsToFitWidth(int availableWidth) {
        int columns = calculateColumnsForContentWidth(availableWidth);
        if (needsScrollBar(columns) && reserveScrollBarSpace) {
            columns = calculateColumnsForContentWidth(Math.max(1, availableWidth - getReservedScrollBarWidth()));
        }
        return Math.max(1, columns);
    }

    public ScrollableInteractionTable setMaxVisibleRows(int maxVisibleRows) {
        this.maxVisibleRows = Math.max(1, maxVisibleRows);
        this.row = this.maxVisibleRows;
        updateTableBounds();
        return this;
    }

    public ScrollableInteractionTable setMaxRows(int maxRows) {
        return setMaxVisibleRows(maxRows);
    }

    public int getMaxVisibleRows() {
        return maxVisibleRows;
    }

    public int getMaxRows() {
        return getMaxVisibleRows();
    }

    public int getVisibleRows() {
        if (!fitHeightToContent) {
            return maxVisibleRows;
        }

        return clamp(getContentRows(), 1, maxVisibleRows);
    }

    public ScrollableInteractionTable setFitHeightToContent(boolean fitHeightToContent) {
        this.fitHeightToContent = fitHeightToContent;
        updateTableBounds();
        return this;
    }

    public ScrollableInteractionTable fitHeightToContent() {
        return setFitHeightToContent(true);
    }

    public ScrollableInteractionTable reserveMaxVisibleHeight() {
        return setFitHeightToContent(false);
    }

    public boolean isFitHeightToContent() {
        return fitHeightToContent;
    }

    public ScrollableInteractionTable setWheelRows(int wheelRows) {
        this.wheelRows = Math.max(1, wheelRows);
        return this;
    }

    public int getWheelRows() {
        return wheelRows;
    }

    public ScrollableInteractionTable setShowScrollBar(boolean showScrollBar) {
        this.showScrollBar = showScrollBar;
        updateTableBounds();
        return this;
    }

    public ScrollableInteractionTable showScrollBar() {
        return setShowScrollBar(true);
    }

    public ScrollableInteractionTable hideScrollBar() {
        return setShowScrollBar(false);
    }

    public ScrollableInteractionTable setReserveScrollBarSpace(boolean reserveScrollBarSpace) {
        this.reserveScrollBarSpace = reserveScrollBarSpace;
        updateTableBounds();
        return this;
    }

    public ScrollableInteractionTable reserveScrollBarSpace() {
        return setReserveScrollBarSpace(true);
    }

    public ScrollableInteractionTable overlayScrollBar() {
        return setReserveScrollBarSpace(false);
    }

    public ScrollableInteractionTable setScrollBarWidth(int scrollBarWidth) {
        this.scrollBarWidth = Math.max(0, scrollBarWidth);
        updateTableBounds();
        return this;
    }

    public ScrollableInteractionTable setScrollBarPadding(int scrollBarPadding) {
        this.scrollBarPadding = Math.max(0, scrollBarPadding);
        updateTableBounds();
        return this;
    }

    public ScrollableInteractionTable setMinScrollThumbSize(int minScrollThumbSize) {
        this.minScrollThumbSize = Math.max(1, minScrollThumbSize);
        return this;
    }

    public ScrollableInteractionTable setScrollBarColors(int trackColor, int thumbColor, int thumbHoverColor) {
        this.scrollTrackColor = trackColor;
        this.scrollThumbColor = thumbColor;
        this.scrollThumbHoverColor = thumbHoverColor;
        return this;
    }

    public int getScrollRow() {
        return scrollRow;
    }

    public int getMaxScrollRow() {
        return Math.max(0, getContentRows() - getVisibleRows());
    }

    public boolean canScroll() {
        return getMaxScrollRow() > 0;
    }

    public ScrollableInteractionTable setScrollRow(int scrollRow) {
        int next = clamp(scrollRow, 0, getMaxScrollRow());
        if (this.scrollRow == next) {
            return this;
        }

        this.scrollRow = next;
        updateTableBounds();
        return this;
    }

    public ScrollableInteractionTable scrollToRow(int row) {
        return setScrollRow(row);
    }

    public ScrollableInteractionTable scrollByRows(int rows) {
        return setScrollRow(scrollRow + rows);
    }

    public ScrollableInteractionTable scrollToStart() {
        return setScrollRow(0);
    }

    public ScrollableInteractionTable scrollToEnd() {
        return setScrollRow(getMaxScrollRow());
    }

    public ScrollableInteractionTable ensureRowVisible(int row) {
        if (row < scrollRow) {
            return setScrollRow(row);
        }

        int visibleRows = getVisibleRows();
        if (row >= scrollRow + visibleRows) {
            return setScrollRow(row - visibleRows + 1);
        }

        return this;
    }

    public ScrollableInteractionTable ensureElementVisible(int index) {
        if (index < 0) return this;
        return ensureRowVisible(index / Math.max(1, coll));
    }

    @Override
    public ScrollableInteractionTable setCellSize(int cellWidth, int cellHeight) {
        super.setCellSize(cellWidth, cellHeight);
        return this;
    }

    @Override
    public ScrollableInteractionTable setSpacing(int spacingX, int spacingY) {
        super.setSpacing(spacingX, spacingY);
        return this;
    }

    @Override
    public ScrollableInteractionTable setElementScale(int index, float scale) {
        super.setElementScale(index, scale);
        return this;
    }

    @Override
    public ScrollableInteractionTable addElement(Widget widget) {
        return addWidget(widget);
    }

    @Override
    public ScrollableInteractionTable addElement(Widget widget, float scale) {
        return addWidget(widgets.size(), widget, scale);
    }

    @Override
    public ScrollableInteractionTable addWidget(Widget widget) {
        return addWidget(widgets.size(), widget, 1f);
    }

    @Override
    public ScrollableInteractionTable addWidget(int index, Widget widget) {
        return addWidget(index, widget, 1f);
    }

    @Override
    public ScrollableInteractionTable addWidget(int index, Widget widget, float scale) {
        super.addWidget(index, widget, scale);
        return this;
    }

    @Override
    public ScrollableInteractionTable addWidgets(Widget... widgets) {
        super.addWidgets(widgets);
        return this;
    }

    @Override
    public void removeWidget(Widget widget) {
        super.removeWidget(widget);
        clampScrollRow();
        updateTableBounds();
    }

    @Override
    public void clearAllWidgets() {
        super.clearAllWidgets();
        scrollRow = 0;
        updateTableBounds();
    }

    @Override
    protected Position getCellSelfPosition(int index) {
        int col = index % coll;
        int row = index / coll - scrollRow;
        return new Position(col * (cellWidth + spacingX), row * (cellHeight + spacingY));
    }

    @Override
    protected Size computeDynamicSize() {
        return computeScrollableTableSize();
    }

    @Override
    protected void updateTableBounds() {
        clampScrollRow();

        onTableBoundsChanged();
        if (isDynamicSized()) {
            recomputeSize();
        } else {
            setSize(computeScrollableTableSize());
        }
    }

    @Override
    protected void onTableBoundsChanged() {
        if (recomputingScrollableLayout) return;
        recomputingScrollableLayout = true;

        try {
            for (int i = 0; i < widgets.size(); i++) {
                Widget widget = widgets.get(i);
                widget.setSelfPosition(getCellSelfPosition(i));
                widget.setSize(cellWidth, cellHeight);
            }
        } finally {
            recomputingScrollableLayout = false;
        }
    }

    @Override
    protected void onChildSelfPositionUpdate(Widget child) {
        if (!recomputingScrollableLayout) {
            updateTableBounds();
        }
    }

    @Override
    protected void onChildSizeUpdate(Widget child) {
        if (!recomputingScrollableLayout) {
            updateTableBounds();
        }
    }

    @Override
    public Widget getHoverElement(double mouseX, double mouseY) {
        if (isMouseOverScrollBar(mouseX, mouseY)) {
            return this;
        }

        if (!isMouseOverViewport(mouseX, mouseY)) {
            return null;
        }

        for (int i = getVisibleEndIndex() - 1; i >= getVisibleStartIndex(); i--) {
            Widget widget = widgets.get(i);
            if (!widget.isVisible()) continue;

            Widget hovered = widget.getHoverElement(getScaledMouseX(i, mouseX), getScaledMouseY(i, mouseY));
            if (hovered != null) {
                return hovered;
            }
        }

        return isMouseOverElement(mouseX, mouseY) ? this : null;
    }

    @Override
    public boolean mouseWheelMove(double mouseX, double mouseY, double wheelDelta) {
        if (!isMouseOverViewport(mouseX, mouseY) && !isMouseOverScrollBar(mouseX, mouseY)) {
            return false;
        }

        for (int i = getVisibleEndIndex() - 1; i >= getVisibleStartIndex(); i--) {
            Widget widget = widgets.get(i);
            if (widget.isVisible() && widget.isActive()
                    && widget.mouseWheelMove(getScaledMouseX(i, mouseX), getScaledMouseY(i, mouseY), wheelDelta)) {
                return true;
            }
        }

        if (!canScroll()) {
            return false;
        }

        scrollByRows(wheelDelta > 0 ? -wheelRows : wheelRows);
        setFocus(true);
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && canScroll() && isMouseOverScrollBar(mouseX, mouseY)) {
            ScrollThumb thumb = getScrollThumb();
            if (isMouseOver(thumb.x, thumb.y, thumb.width, thumb.height, mouseX, mouseY)) {
                draggingScrollThumb = true;
                dragStartMouseY = mouseY;
                dragStartScrollRow = scrollRow;
            } else {
                scrollByRows(mouseY < thumb.y ? -getVisibleRows() : getVisibleRows());
            }

            setFocus(true);
            return true;
        }

        if (!isMouseOverViewport(mouseX, mouseY)) {
            return false;
        }

        for (int i = getVisibleEndIndex() - 1; i >= getVisibleStartIndex(); i--) {
            Widget widget = widgets.get(i);
            if (widget.isVisible() && widget.isActive()
                    && widget.mouseClicked(getScaledMouseX(i, mouseX), getScaledMouseY(i, mouseY), button)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (draggingScrollThumb && button == 0) {
            int movable = getScrollTrackHeight() - getScrollThumbHeight();
            if (movable <= 0) {
                setScrollRow(0);
                return true;
            }

            int next = dragStartScrollRow + (int) Math.round((mouseY - dragStartMouseY) * getMaxScrollRow() / (double) movable);
            setScrollRow(next);
            return true;
        }

        if (!isMouseOverViewport(mouseX, mouseY)) {
            return false;
        }

        for (int i = getVisibleEndIndex() - 1; i >= getVisibleStartIndex(); i--) {
            Widget widget = widgets.get(i);
            if (!widget.isVisible() || !widget.isActive()) continue;

            float scale = getElementScale(i);
            double scaledDragX = scale == 0f ? dragX : dragX / scale;
            double scaledDragY = scale == 0f ? dragY : dragY / scale;
            if (widget.mouseDragged(getScaledMouseX(i, mouseX), getScaledMouseY(i, mouseY), button, scaledDragX, scaledDragY)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (draggingScrollThumb && button == 0) {
            draggingScrollThumb = false;
            return true;
        }

        for (int i = getVisibleEndIndex() - 1; i >= getVisibleStartIndex(); i--) {
            Widget widget = widgets.get(i);
            if (widget.isVisible() && widget.isActive()
                    && widget.mouseReleased(getScaledMouseX(i, mouseX), getScaledMouseY(i, mouseY), button)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean mouseMoved(double mouseX, double mouseY) {
        if (!isMouseOverViewport(mouseX, mouseY)) {
            return false;
        }

        for (int i = getVisibleEndIndex() - 1; i >= getVisibleStartIndex(); i--) {
            Widget widget = widgets.get(i);
            if (widget.isVisible() && widget.isActive()
                    && widget.mouseMoved(getScaledMouseX(i, mouseX), getScaledMouseY(i, mouseY))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void drawInBackground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        drawTableBackground(graphics, mouseX, mouseY);

        enableViewportScissor(graphics);
        try {
            drawVisibleWidgetsBackgroundScaled(graphics, mouseX, mouseY, partialTicks);
        } finally {
            graphics.disableScissor();
        }

        drawScrollBar(graphics, mouseX, mouseY);
    }

    @Override
    public void drawInForeground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        drawTooltipTexts(mouseX, mouseY);

        enableViewportScissor(graphics);
        try {
            drawVisibleWidgetsForegroundScaled(graphics, mouseX, mouseY, partialTicks);
        } finally {
            graphics.disableScissor();
        }
    }

    @Override
    public void drawOverlay(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (overlay != null) {
            overlay.draw(graphics, mouseX, mouseY, getPositionX(), getPositionY(), getSizeWidth(), getSizeHeight());
        }

        enableViewportScissor(graphics);
        try {
            for (int i = getVisibleStartIndex(); i < getVisibleEndIndex(); i++) {
                Widget widget = widgets.get(i);
                if (!widget.isVisible()) continue;

                RenderSystem.setShaderColor(1, 1, 1, 1);
                RenderSystem.enableBlend();
                int index = i;
                withCellScale(graphics, index, () -> widget.drawOverlay(graphics, getScaledMouseX(index, mouseX), getScaledMouseY(index, mouseY), partialTicks));
            }
        } finally {
            graphics.disableScissor();
        }
    }

    protected Size computeScrollableTableSize() {
        Size base = computeTableSize(getVisibleRows());
        int width = base.width;
        if (reserveScrollBarSpace && hasVisibleScrollBar()) {
            width += getReservedScrollBarWidth();
        }
        return new Size(width, base.height);
    }

    protected int getContentRows() {
        if (widgets.isEmpty()) return 0;
        return (widgets.size() + coll - 1) / coll;
    }

    protected int getVisibleStartIndex() {
        return clamp(scrollRow * coll, 0, widgets.size());
    }

    protected int getVisibleEndIndex() {
        int visibleCells = Math.max(0, getVisibleRows() * coll);
        return clamp(getVisibleStartIndex() + visibleCells, 0, widgets.size());
    }

    protected void clampScrollRow() {
        scrollRow = clamp(scrollRow, 0, getMaxScrollRow());
    }

    protected boolean hasVisibleScrollBar() {
        return showScrollBar && scrollBarWidth > 0 && getMaxScrollRow() > 0;
    }

    public int getReservedScrollBarWidth() {
        return Math.max(0, scrollBarWidth + scrollBarPadding * 2);
    }

    protected void drawScrollBar(GuiGraphics graphics, int mouseX, int mouseY) {
        if (!hasVisibleScrollBar()) return;

        ScrollTrack track = getScrollTrack();
        graphics.fill(track.x, track.y, track.x + track.width, track.y + track.height, scrollTrackColor);

        ScrollThumb thumb = getScrollThumb();
        int thumbColor = draggingScrollThumb || isMouseOver(thumb.x, thumb.y, thumb.width, thumb.height, mouseX, mouseY)
                ? scrollThumbHoverColor
                : scrollThumbColor;
        graphics.fill(thumb.x, thumb.y, thumb.x + thumb.width, thumb.y + thumb.height, thumbColor);
    }

    protected boolean isMouseOverViewport(double mouseX, double mouseY) {
        return isMouseOver(
                getPositionX(),
                getPositionY(),
                getViewportWidth(),
                getSizeHeight(),
                mouseX,
                mouseY
        );
    }

    protected boolean isMouseOverScrollBar(double mouseX, double mouseY) {
        if (!hasVisibleScrollBar()) return false;
        ScrollTrack track = getScrollTrack();
        return isMouseOver(track.x, track.y, track.width, track.height, mouseX, mouseY);
    }

    protected int getViewportWidth() {
        if (reserveScrollBarSpace && hasVisibleScrollBar()) {
            return Math.max(0, getSizeWidth() - getReservedScrollBarWidth());
        }

        return getSizeWidth();
    }

    protected int calculateColumnsForContentWidth(int width) {
        int cellWidth = Math.max(1, this.cellWidth);
        int spacing = Math.max(0, spacingX);
        return Math.max(1, (Math.max(1, width) + spacing) / (cellWidth + spacing));
    }

    protected boolean needsScrollBar(int columns) {
        if (!showScrollBar || scrollBarWidth <= 0) return false;
        int safeColumns = Math.max(1, columns);
        int contentRows = widgets.isEmpty() ? 0 : (widgets.size() + safeColumns - 1) / safeColumns;
        return contentRows > maxVisibleRows;
    }

    protected ScrollTrack getScrollTrack() {
        int width = Math.max(1, scrollBarWidth);
        int x = getPositionX() + getSizeWidth() - scrollBarPadding - width;
        int y = getPositionY() + scrollBarPadding;
        int height = Math.max(1, getSizeHeight() - scrollBarPadding * 2);
        return new ScrollTrack(x, y, width, height);
    }

    protected ScrollThumb getScrollThumb() {
        ScrollTrack track = getScrollTrack();
        int thumbHeight = getScrollThumbHeight();
        int movable = Math.max(0, track.height - thumbHeight);
        int maxScroll = getMaxScrollRow();
        int y = track.y + (maxScroll <= 0 ? 0 : Math.round(movable * (scrollRow / (float) maxScroll)));
        return new ScrollThumb(track.x, y, track.width, thumbHeight);
    }

    protected int getScrollTrackHeight() {
        return getScrollTrack().height;
    }

    protected int getScrollThumbHeight() {
        ScrollTrack track = getScrollTrack();
        int totalRows = Math.max(1, getContentRows());
        int visibleRows = Math.max(1, getVisibleRows());
        int height = Math.round(track.height * (visibleRows / (float) totalRows));
        return clamp(height, Math.min(track.height, minScrollThumbSize), track.height);
    }

    protected void enableViewportScissor(GuiGraphics graphics) {
        graphics.enableScissor(
                getPositionX(),
                getPositionY(),
                getPositionX() + getViewportWidth(),
                getPositionY() + getSizeHeight()
        );
    }

    protected void drawVisibleWidgetsBackgroundScaled(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        for (int i = getVisibleStartIndex(); i < getVisibleEndIndex(); i++) {
            Widget widget = widgets.get(i);
            if (!widget.isVisible()) continue;

            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.enableBlend();
            int index = i;
            withCellScale(graphics, index, () -> {
                if (widget.inAnimate()) {
                    widget.getAnimation().drawInBackground(graphics, getScaledMouseX(index, mouseX), getScaledMouseY(index, mouseY), partialTicks);
                } else {
                    widget.drawInBackground(graphics, getScaledMouseX(index, mouseX), getScaledMouseY(index, mouseY), partialTicks);
                }
            });
        }
    }

    protected void drawVisibleWidgetsForegroundScaled(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        for (int i = getVisibleStartIndex(); i < getVisibleEndIndex(); i++) {
            Widget widget = widgets.get(i);
            if (!widget.isVisible()) continue;

            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.enableBlend();
            int index = i;
            withCellScale(graphics, index, () -> {
                if (widget.inAnimate()) {
                    widget.getAnimation().drawInForeground(graphics, getScaledMouseX(index, mouseX), getScaledMouseY(index, mouseY), partialTicks);
                } else {
                    widget.drawInForeground(graphics, getScaledMouseX(index, mouseX), getScaledMouseY(index, mouseY), partialTicks);
                }
            });
        }
    }

    protected int getScaledMouseX(int index, double mouseX) {
        float scale = getElementScale(index);
        if (scale == 1f || scale == 0f) {
            return (int) mouseX;
        }

        float pivotX = getCellPositionX(index) + cellWidth * 0.5f;
        return (int) (pivotX + (mouseX - pivotX) / scale);
    }

    protected int getScaledMouseY(int index, double mouseY) {
        float scale = getElementScale(index);
        if (scale == 1f || scale == 0f) {
            return (int) mouseY;
        }

        float pivotY = getCellPositionY(index) + cellHeight * 0.5f;
        return (int) (pivotY + (mouseY - pivotY) / scale);
    }

    private static int clamp(int value, int min, int max) {
        if (max < min) {
            return min;
        }
        return Math.max(min, Math.min(max, value));
    }

    protected record ScrollTrack(int x, int y, int width, int height) {
    }

    protected record ScrollThumb(int x, int y, int width, int height) {
    }
}
