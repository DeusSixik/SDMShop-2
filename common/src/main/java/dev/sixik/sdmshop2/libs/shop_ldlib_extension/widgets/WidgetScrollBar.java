package dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets;

import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A standalone scrollbar controller for an existing {@link WidgetGroup}.
 * <p>
 * Unlike {@code DraggableScrollableWidgetGroup}, this widget does not require wrapping content
 * into a child scroll group. Add regular widgets directly to a panel, add this scrollbar as
 * another child, and attach it to the same panel.
 */
public class WidgetScrollBar extends Widget {

    public enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    public enum VisibilityMode {
        /**
         * Keep widgets visible while any part intersects the viewport.
         */
        INTERSECTING,

        /**
         * Hide widgets unless they are fully inside the viewport.
         * Useful when no parent scissor/clipping exists.
         */
        FULLY_INSIDE,

        /**
         * Do not change managed widgets visibility/active state.
         */
        NONE
    }

    private static final int DEFAULT_TRACK_COLOR = 0x5520202A;
    private static final int DEFAULT_THUMB_COLOR = 0xFF8E94A6;
    private static final int DEFAULT_THUMB_HOVER_COLOR = 0xFFC5CAD8;

    protected final Orientation orientation;

    protected WidgetGroup target;
    protected boolean customViewport;
    protected int viewportX;
    protected int viewportY;
    protected int viewportWidth;
    protected int viewportHeight;

    protected final List<Widget> explicitManagedWidgets = new ArrayList<>();
    protected final Set<Widget> ignoredWidgets = Collections.newSetFromMap(new IdentityHashMap<>());
    protected final Map<Widget, WidgetState> widgetStates = new IdentityHashMap<>();

    protected VisibilityMode visibilityMode = VisibilityMode.INTERSECTING;

    protected int scrollOffset;
    protected int maxScrollOffset;
    protected int manualContentLength = -1;
    protected int wheelStep = 13;
    protected int minThumbSize = 12;
    protected int pageStep = -1;
    protected boolean wheelOverViewport = true;
    protected boolean viewportWheelRequiresShift;

    protected boolean autoHide = false;
    protected boolean controlOnly = false;
    protected boolean draggingThumb;
    protected double dragStartMouse;
    protected int dragStartOffset;
    protected Consumer<Integer> scrollChangedListener;

    protected IGuiTexture trackTexture = new ColorRectTexture(DEFAULT_TRACK_COLOR);
    protected IGuiTexture thumbTexture = new ColorRectTexture(DEFAULT_THUMB_COLOR);
    protected IGuiTexture thumbHoverTexture = new ColorRectTexture(DEFAULT_THUMB_HOVER_COLOR);

    private boolean applyingScroll;

    public WidgetScrollBar() {
        this(Orientation.VERTICAL, 0, 0, 6, 100);
    }

    public WidgetScrollBar(int x, int y, int width, int height) {
        this(Orientation.VERTICAL, x, y, width, height);
    }

    public WidgetScrollBar(Orientation orientation, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.orientation = orientation == null ? Orientation.VERTICAL : orientation;
    }

    public static WidgetScrollBar vertical(int x, int y, int width, int height) {
        return new WidgetScrollBar(Orientation.VERTICAL, x, y, width, height);
    }

    public static WidgetScrollBar horizontal(int x, int y, int width, int height) {
        return new WidgetScrollBar(Orientation.HORIZONTAL, x, y, width, height);
    }

    public WidgetScrollBar attachTo(WidgetGroup target) {
        this.target = target;
        this.customViewport = false;
        rememberCurrentLayout();
        return this;
    }

    public WidgetScrollBar attachTo(WidgetGroup target, int viewportX, int viewportY, int viewportWidth, int viewportHeight) {
        this.target = target;
        setViewport(viewportX, viewportY, viewportWidth, viewportHeight);
        rememberCurrentLayout();
        return this;
    }

    public WidgetScrollBar detach() {
        restoreManagedWidgets();
        this.target = null;
        this.widgetStates.clear();
        this.scrollOffset = 0;
        this.maxScrollOffset = 0;
        this.draggingThumb = false;
        setFocus(false);
        return this;
    }

    public WidgetScrollBar setViewport(int x, int y, int width, int height) {
        this.customViewport = true;
        this.viewportX = x;
        this.viewportY = y;
        this.viewportWidth = Math.max(0, width);
        this.viewportHeight = Math.max(0, height);
        refreshContent();
        return this;
    }

    public WidgetScrollBar useTargetViewport() {
        this.customViewport = false;
        refreshContent();
        return this;
    }

    public WidgetScrollBar dockRight(int width) {
        if (target == null) {
            setSizeWidth(Math.max(1, width));
            return this;
        }

        int barWidth = Math.max(1, width);
        setSelfPosition(Math.max(0, target.getSizeWidth() - barWidth), 0);
        setSize(barWidth, target.getSizeHeight());
        setViewport(0, 0, Math.max(0, target.getSizeWidth() - barWidth), target.getSizeHeight());
        return this;
    }

    public WidgetScrollBar dockBottom(int height) {
        if (target == null) {
            setSizeHeight(Math.max(1, height));
            return this;
        }

        int barHeight = Math.max(1, height);
        setSelfPosition(0, Math.max(0, target.getSizeHeight() - barHeight));
        setSize(target.getSizeWidth(), barHeight);
        setViewport(0, 0, target.getSizeWidth(), Math.max(0, target.getSizeHeight() - barHeight));
        return this;
    }

    public WidgetScrollBar manage(Widget... widgets) {
        explicitManagedWidgets.clear();
        if (widgets != null) {
            for (Widget widget : widgets) {
                if (widget != null && widget != this && !explicitManagedWidgets.contains(widget)) {
                    explicitManagedWidgets.add(widget);
                }
            }
        }
        rememberCurrentLayout();
        return this;
    }

    public WidgetScrollBar addManagedWidget(Widget widget) {
        if (widget != null && widget != this && !explicitManagedWidgets.contains(widget)) {
            explicitManagedWidgets.add(widget);
            captureWidgetState(widget, true);
            refreshContent();
        }
        return this;
    }

    public WidgetScrollBar useAutoManagedWidgets() {
        explicitManagedWidgets.clear();
        rememberCurrentLayout();
        return this;
    }

    public WidgetScrollBar ignore(Widget... widgets) {
        if (widgets != null) {
            for (Widget widget : widgets) {
                if (widget != null) {
                    ignoredWidgets.add(widget);
                    widgetStates.remove(widget);
                }
            }
        }
        refreshContent();
        return this;
    }

    public WidgetScrollBar clearIgnoredWidgets() {
        ignoredWidgets.clear();
        rememberCurrentLayout();
        return this;
    }

    /**
     * Treat current child positions as unscrolled/base positions.
     * Call this after a panel performs its own layout pass.
     */
    public WidgetScrollBar rememberCurrentLayout() {
        widgetStates.clear();
        for (Widget widget : getManagedWidgets()) {
            captureWidgetState(widget, true);
        }
        refreshContent();
        return this;
    }

    /**
     * Re-scans target widgets, recomputes content bounds and reapplies the current scroll offset.
     */
    public WidgetScrollBar refreshContent() {
        if (target == null) {
            maxScrollOffset = 0;
            scrollOffset = 0;
            return this;
        }

        syncManagedWidgetStates();
        recomputeScrollRange();
        setScrollOffset(Mth.clamp(scrollOffset, 0, maxScrollOffset));
        applyScrollToWidgets();
        return this;
    }

    public WidgetScrollBar setScrollOffset(int scrollOffset) {
        int next = Mth.clamp(scrollOffset, 0, maxScrollOffset);
        if (next == this.scrollOffset) {
            return this;
        }
        this.scrollOffset = next;
        applyScrollToWidgets();
        if (scrollChangedListener != null) {
            scrollChangedListener.accept(this.scrollOffset);
        }
        return this;
    }

    public WidgetScrollBar scrollBy(int delta) {
        return setScrollOffset(scrollOffset + delta);
    }

    public WidgetScrollBar scrollToStart() {
        return setScrollOffset(0);
    }

    public WidgetScrollBar scrollToEnd() {
        return setScrollOffset(maxScrollOffset);
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public int getMaxScrollOffset() {
        refreshContent();
        return maxScrollOffset;
    }

    public boolean canScroll() {
        refreshContent();
        return maxScrollOffset > 0;
    }

    public WidgetScrollBar setContentLength(int contentLength) {
        this.manualContentLength = Math.max(0, contentLength);
        refreshContent();
        return this;
    }

    public WidgetScrollBar autoContentLength() {
        this.manualContentLength = -1;
        refreshContent();
        return this;
    }

    public WidgetScrollBar setWheelStep(int wheelStep) {
        this.wheelStep = Math.max(1, wheelStep);
        return this;
    }

    public WidgetScrollBar setPageStep(int pageStep) {
        this.pageStep = Math.max(1, pageStep);
        return this;
    }

    public WidgetScrollBar autoPageStep() {
        this.pageStep = -1;
        return this;
    }

    public WidgetScrollBar setMinThumbSize(int minThumbSize) {
        this.minThumbSize = Math.max(1, minThumbSize);
        return this;
    }

    public WidgetScrollBar setWheelOverViewport(boolean wheelOverViewport) {
        this.wheelOverViewport = wheelOverViewport;
        return this;
    }

    public WidgetScrollBar setViewportWheelRequiresShift(boolean viewportWheelRequiresShift) {
        this.viewportWheelRequiresShift = viewportWheelRequiresShift;
        return this;
    }

    public WidgetScrollBar setAutoHide(boolean autoHide) {
        this.autoHide = autoHide;
        return this;
    }

    /**
     * Makes this scrollbar act only as a visual/input controller.
     * It still computes thumb/offset, but it does not move or hide widgets in the target group.
     */
    public WidgetScrollBar setControlOnly(boolean controlOnly) {
        this.controlOnly = controlOnly;
        return this;
    }

    public WidgetScrollBar setScrollChangedListener(Consumer<Integer> scrollChangedListener) {
        this.scrollChangedListener = scrollChangedListener;
        return this;
    }

    public WidgetScrollBar setVisibilityMode(VisibilityMode visibilityMode) {
        this.visibilityMode = visibilityMode == null ? VisibilityMode.INTERSECTING : visibilityMode;
        applyScrollToWidgets();
        return this;
    }

    public WidgetScrollBar showPartiallyOutside() {
        return setVisibilityMode(VisibilityMode.INTERSECTING);
    }

    public WidgetScrollBar hidePartiallyOutside() {
        return setVisibilityMode(VisibilityMode.FULLY_INSIDE);
    }

    public WidgetScrollBar keepWidgetVisibility() {
        return setVisibilityMode(VisibilityMode.NONE);
    }

    public WidgetScrollBar setTrackTexture(IGuiTexture trackTexture) {
        this.trackTexture = trackTexture;
        return this;
    }

    public WidgetScrollBar setThumbTexture(IGuiTexture thumbTexture) {
        this.thumbTexture = thumbTexture;
        return this;
    }

    public WidgetScrollBar setThumbHoverTexture(IGuiTexture thumbHoverTexture) {
        this.thumbHoverTexture = thumbHoverTexture;
        return this;
    }

    public WidgetScrollBar setTextures(IGuiTexture trackTexture, IGuiTexture thumbTexture, IGuiTexture thumbHoverTexture) {
        this.trackTexture = trackTexture;
        this.thumbTexture = thumbTexture;
        this.thumbHoverTexture = thumbHoverTexture;
        return this;
    }

    @Override
    public void drawInBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        refreshContent();

        if (autoHide && maxScrollOffset <= 0) {
            return;
        }

        super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        int x = getPositionX();
        int y = getPositionY();
        int width = getSizeWidth();
        int height = getSizeHeight();

        if (trackTexture != null) {
            trackTexture.draw(graphics, mouseX, mouseY, x, y, width, height);
        }

        if (maxScrollOffset <= 0) {
            return;
        }

        ThumbRect thumb = getThumbRect();
        IGuiTexture texture = isMouseOver(thumb.x, thumb.y, thumb.width, thumb.height, mouseX, mouseY)
                ? thumbHoverTexture
                : thumbTexture;
        if (texture != null) {
            texture.draw(graphics, mouseX, mouseY, thumb.x, thumb.y, thumb.width, thumb.height);
        }
    }

    @Override
    public boolean mouseWheelMove(double mouseX, double mouseY, double wheelDelta) {
        refreshContent();
        if (maxScrollOffset <= 0 || !isMouseOverScrollArea(mouseX, mouseY)) {
            return false;
        }

        int direction = wheelDelta > 0 ? -1 : 1;
        scrollBy(direction * wheelStep);
        setFocus(true);
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        refreshContent();
        if (button != 0 || maxScrollOffset <= 0 || !isMouseOverElement(mouseX, mouseY)) {
            return false;
        }

        ThumbRect thumb = getThumbRect();
        if (isMouseOver(thumb.x, thumb.y, thumb.width, thumb.height, mouseX, mouseY)) {
            draggingThumb = true;
            dragStartMouse = orientation == Orientation.VERTICAL ? mouseY : mouseX;
            dragStartOffset = scrollOffset;
        } else {
            int mouseAxis = (int) (orientation == Orientation.VERTICAL ? mouseY : mouseX);
            int thumbAxis = orientation == Orientation.VERTICAL ? thumb.y : thumb.x;
            scrollBy(mouseAxis < thumbAxis ? -resolvePageStep() : resolvePageStep());
        }

        setFocus(true);
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (!draggingThumb || button != 0) {
            return false;
        }

        int availableTrack = getTrackLength() - getThumbLength();
        if (availableTrack <= 0) {
            setScrollOffset(0);
            return true;
        }

        double mouseAxis = orientation == Orientation.VERTICAL ? mouseY : mouseX;
        double delta = mouseAxis - dragStartMouse;
        int nextOffset = dragStartOffset + (int) Math.round(delta * maxScrollOffset / availableTrack);
        setScrollOffset(nextOffset);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!draggingThumb) {
            return false;
        }

        draggingThumb = false;
        return true;
    }

    private void syncManagedWidgetStates() {
        List<Widget> managedWidgets = getManagedWidgets();
        widgetStates.keySet().removeIf(widget -> !managedWidgets.contains(widget));

        for (Widget widget : managedWidgets) {
            captureWidgetState(widget, false);
        }
    }

    private void captureWidgetState(Widget widget, boolean overwrite) {
        if (widget == null || widget == this || ignoredWidgets.contains(widget)) {
            return;
        }
        if (!overwrite && widgetStates.containsKey(widget)) {
            return;
        }

        Position current = widget.getSelfPosition();
        if (!overwrite && scrollOffset != 0) {
            current = orientation == Orientation.VERTICAL
                    ? current.add(0, scrollOffset)
                    : current.add(scrollOffset, 0);
        }
        widgetStates.put(widget, new WidgetState(current, widget.isVisible(), widget.isActive()));
    }

    private List<Widget> getManagedWidgets() {
        List<Widget> managed = explicitManagedWidgets.isEmpty() && target != null
                ? target.widgets
                : explicitManagedWidgets;

        List<Widget> result = new ArrayList<>(managed.size());
        for (Widget widget : managed) {
            if (widget == null || widget == this || ignoredWidgets.contains(widget)) {
                continue;
            }
            result.add(widget);
        }
        return result;
    }

    private void recomputeScrollRange() {
        Viewport viewport = getViewport();
        int viewportLength = getViewportLength(viewport);
        int contentLength = manualContentLength >= 0 ? manualContentLength : computeContentLength(viewport);
        maxScrollOffset = Math.max(0, contentLength - viewportLength);
        scrollOffset = Mth.clamp(scrollOffset, 0, maxScrollOffset);
    }

    private int computeContentLength(Viewport viewport) {
        int start = orientation == Orientation.VERTICAL ? viewport.y : viewport.x;
        int end = start + getViewportLength(viewport);

        for (Widget widget : getManagedWidgets()) {
            WidgetState state = widgetStates.get(widget);
            if (state == null || !state.baseVisible) {
                continue;
            }

            int widgetEnd = orientation == Orientation.VERTICAL
                    ? state.basePosition.y + widget.getSizeHeight()
                    : state.basePosition.x + widget.getSizeWidth();
            end = Math.max(end, widgetEnd);
        }

        return Math.max(0, end - start);
    }

    private void applyScrollToWidgets() {
        if (target == null || applyingScroll || controlOnly) {
            return;
        }

        applyingScroll = true;
        try {
            Viewport viewport = getViewport();
            for (Widget widget : getManagedWidgets()) {
                WidgetState state = widgetStates.get(widget);
                if (state == null) {
                    continue;
                }

                Position currentPosition = widget.getSelfPosition();
                Position nextPosition = orientation == Orientation.VERTICAL
                        ? new Position(currentPosition.x, state.basePosition.y - scrollOffset)
                        : new Position(state.basePosition.x - scrollOffset, currentPosition.y);

                widget.setSelfPosition(nextPosition);

                if (visibilityMode != VisibilityMode.NONE) {
                    boolean inViewport = visibilityMode == VisibilityMode.FULLY_INSIDE
                            ? isFullyInsideViewport(nextPosition, widget.getSize(), viewport)
                            : intersectsViewport(nextPosition, widget.getSize(), viewport);
                    boolean visible = state.baseVisible && inViewport;
                    widget.setVisible(visible);
                    widget.setActive(state.baseActive && visible);
                }
            }
        } finally {
            applyingScroll = false;
        }
    }

    private void restoreManagedWidgets() {
        for (Map.Entry<Widget, WidgetState> entry : widgetStates.entrySet()) {
            Widget widget = entry.getKey();
            WidgetState state = entry.getValue();
            widget.setSelfPosition(state.basePosition);
            widget.setVisible(state.baseVisible);
            widget.setActive(state.baseActive);
        }
    }

    private boolean isMouseOverScrollArea(double mouseX, double mouseY) {
        if (isMouseOverElement(mouseX, mouseY)) {
            return true;
        }
        if (!wheelOverViewport) {
            return false;
        }
        return (!viewportWheelRequiresShift || isShiftDown()) && isMouseOverViewport(mouseX, mouseY);
    }

    private boolean isMouseOverViewport(double mouseX, double mouseY) {
        if (target == null) {
            return false;
        }
        Viewport viewport = getViewport();
        Position targetPosition = target.getPosition();
        return isMouseOver(
                targetPosition.x + viewport.x,
                targetPosition.y + viewport.y,
                viewport.width,
                viewport.height,
                mouseX,
                mouseY
        );
    }

    private boolean intersectsViewport(Position position, Size size, Viewport viewport) {
        return position.x < viewport.x + viewport.width
                && position.x + size.width > viewport.x
                && position.y < viewport.y + viewport.height
                && position.y + size.height > viewport.y;
    }

    private boolean isFullyInsideViewport(Position position, Size size, Viewport viewport) {
        return position.x >= viewport.x
                && position.y >= viewport.y
                && position.x + size.width <= viewport.x + viewport.width
                && position.y + size.height <= viewport.y + viewport.height;
    }

    private Viewport getViewport() {
        if (customViewport) {
            return new Viewport(viewportX, viewportY, viewportWidth, viewportHeight);
        }
        if (target != null) {
            return new Viewport(0, 0, target.getSizeWidth(), target.getSizeHeight());
        }
        return new Viewport(0, 0, getSizeWidth(), getSizeHeight());
    }

    private int getViewportLength(Viewport viewport) {
        return orientation == Orientation.VERTICAL ? viewport.height : viewport.width;
    }

    private int getTrackLength() {
        return Math.max(0, orientation == Orientation.VERTICAL ? getSizeHeight() : getSizeWidth());
    }

    private int getThumbLength() {
        int trackLength = getTrackLength();
        if (trackLength <= 0 || maxScrollOffset <= 0) {
            return trackLength;
        }

        Viewport viewport = getViewport();
        int viewportLength = Math.max(1, getViewportLength(viewport));
        int contentLength = viewportLength + maxScrollOffset;
        int thumbLength = (int) (trackLength * (viewportLength / (float) contentLength));
        return Mth.clamp(thumbLength, Math.min(trackLength, minThumbSize), trackLength);
    }

    private ThumbRect getThumbRect() {
        int trackLength = getTrackLength();
        int thumbLength = getThumbLength();
        int available = Math.max(0, trackLength - thumbLength);
        int offset = maxScrollOffset <= 0 ? 0 : Math.round(scrollOffset * available / (float) maxScrollOffset);

        if (orientation == Orientation.VERTICAL) {
            return new ThumbRect(getPositionX(), getPositionY() + offset, getSizeWidth(), thumbLength);
        }
        return new ThumbRect(getPositionX() + offset, getPositionY(), thumbLength, getSizeHeight());
    }

    private int resolvePageStep() {
        if (pageStep > 0) {
            return pageStep;
        }
        Viewport viewport = getViewport();
        return Math.max(wheelStep, getViewportLength(viewport) - wheelStep);
    }

    private record WidgetState(Position basePosition, boolean baseVisible, boolean baseActive) {
    }

    private record Viewport(int x, int y, int width, int height) {
    }

    private record ThumbRect(int x, int y, int width, int height) {
    }
}
