package dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.sixik.sdmshop2.libs.shop.client.textures.ColorRectAndBorderTexture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;

public class DropDownBox extends WidgetGroup {

    public enum PopupDirection {
        DOWN,
        UP
    }

    public enum HighlightMode {
        OUTLINE,
        FILL
    }

    protected static final int ACTION_SELECT = 1;

    protected final List<Option> options = new ArrayList<>();

    protected int selectedIndex = -1;
    protected boolean expanded;
    protected PopupDirection popupDirection = PopupDirection.DOWN;

    protected int optionHeight = 22;
    protected int maxVisibleOptions = 5;
    protected int rowSpacing;
    protected int popupSpacing = 2;
    protected int paddingLeft = 4;
    protected int paddingTop = 2;
    protected int paddingRight = 4;
    protected int paddingBottom = 2;
    protected int arrowWidth = 14;
    protected int scrollIndex;
    protected int scrollBarWidth = 6;
    protected int scrollBarPadding = 2;
    protected int minScrollThumbSize = 12;
    protected boolean showScrollBar = true;

    protected boolean closeOnSelect = true;
    protected boolean resizeOptionsToRow = true;
    protected boolean selectedWidgetActiveWhenClosed;
    protected boolean draggingScrollThumb;
    protected double dragStartMouseY;
    protected int dragStartScrollIndex;

    protected int headerFill = 0xFF252733;
    protected int popupFill = 0xFF1E1F28;
    protected int borderColor = 0xFF4C5265;
    protected int hoverFill = 0xFF343746;
    protected int selectedHighlightColor = 0xFFFFFFFF;
    protected HighlightMode selectedHighlightMode = HighlightMode.OUTLINE;
    protected int disabledFill = 0x66202020;
    protected int arrowColor = 0xFFE8E8F0;
    protected int scrollTrackColor = 0x5530303A;
    protected int scrollThumbColor = 0xFF6D7485;
    protected int scrollThumbHoverColor = 0xFFAAB2C5;
    protected int radius = 3;

    protected IGuiTexture headerTexture;
    protected IGuiTexture popupTexture;
    protected IGuiTexture hoverOptionTexture;
    protected IGuiTexture selectedOptionTexture;
    protected IGuiTexture disabledOptionTexture;
    protected IGuiTexture scrollTrackTexture;
    protected IGuiTexture scrollThumbTexture;
    protected IGuiTexture scrollThumbHoverTexture;

    protected IntConsumer selectionChangedListener;
    protected BiConsumer<Integer, Widget> optionSelectedListener;

    private boolean layingOut;

    public DropDownBox() {
        this(0, 0, 120, 22);
    }

    public DropDownBox(int x, int y, int width, int height) {
        super(x, y, width, height);
        setDynamicSized(false);
    }

    public DropDownBox(Position selfPosition, Size size) {
        super(selfPosition, size);
        setDynamicSized(false);
    }

    public DropDownBox addOption(Widget widget) {
        return addOption(options.size(), widget);
    }

    public DropDownBox addOption(int index, Widget widget) {
        if (widget == null) return this;

        int safeIndex = Math.max(0, Math.min(index, options.size()));
        options.add(safeIndex, new Option(widget, true));
        super.addWidget(safeIndex, widget);

        if (selectedIndex < 0) {
            selectedIndex = safeIndex;
        } else if (safeIndex <= selectedIndex) {
            selectedIndex++;
        }

        clampScrollIndex();
        layoutOptions();
        return this;
    }

    @Override
    public DropDownBox addWidget(Widget widget) {
        return addOption(widget);
    }

    @Override
    public DropDownBox addWidget(int index, Widget widget) {
        return addOption(index, widget);
    }

    @Override
    public DropDownBox addWidgets(Widget... widgets) {
        if (widgets != null) {
            for (Widget widget : widgets) {
                addOption(widget);
            }
        }
        return this;
    }

    @Override
    public void removeWidget(Widget widget) {
        int index = indexOfOption(widget);
        if (index >= 0) {
            options.remove(index);
            super.removeWidget(widget);
            if (selectedIndex == index) {
                selectedIndex = findFirstEnabledOption();
            } else if (index < selectedIndex) {
                selectedIndex--;
            }
            clampScrollIndex();
            layoutOptions();
            return;
        }
        super.removeWidget(widget);
    }

    @Override
    public void clearAllWidgets() {
        super.clearAllWidgets();
        options.clear();
        selectedIndex = -1;
        scrollIndex = 0;
        expanded = false;
    }

    public DropDownBox clearOptions() {
        clearAllWidgets();
        return this;
    }

    public int getOptionCount() {
        return options.size();
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    @Nullable
    public Widget getSelectedWidget() {
        return selectedIndex >= 0 && selectedIndex < options.size() ? options.get(selectedIndex).widget : null;
    }

    public DropDownBox setSelectedIndex(int selectedIndex) {
        return setSelectedIndex(selectedIndex, true, false);
    }

    public DropDownBox setSelectedIndex(int selectedIndex, boolean notify) {
        return setSelectedIndex(selectedIndex, notify, false);
    }

    protected DropDownBox setSelectedIndex(int selectedIndex, boolean notify, boolean syncToServer) {
        int next = normalizeSelectableIndex(selectedIndex);
        if (next == this.selectedIndex) {
            layoutOptions();
            return this;
        }

        this.selectedIndex = next;
        ensureSelectedVisible();
        layoutOptions();

        if (notify) {
            if (selectionChangedListener != null) {
                selectionChangedListener.accept(this.selectedIndex);
            }
            if (optionSelectedListener != null && this.selectedIndex >= 0) {
                optionSelectedListener.accept(this.selectedIndex, options.get(this.selectedIndex).widget);
            }
        }

        if (syncToServer && this.selectedIndex >= 0) {
            writeClientAction(ACTION_SELECT, buffer -> buffer.writeVarInt(this.selectedIndex));
        }

        return this;
    }

    public DropDownBox setOptionEnabled(int index, boolean enabled) {
        if (index < 0 || index >= options.size()) return this;

        options.get(index).enabled = enabled;
        if (!enabled && selectedIndex == index) {
            setSelectedIndex(findFirstEnabledOption(), true, false);
        } else {
            layoutOptions();
        }
        return this;
    }

    public boolean isOptionEnabled(int index) {
        return index >= 0 && index < options.size() && options.get(index).enabled;
    }

    public boolean isExpanded() {
        return expanded;
    }

    @Environment(EnvType.CLIENT)
    public DropDownBox setExpanded(boolean expanded) {
        if (this.expanded == expanded) return this;

        this.expanded = expanded;
        if (expanded) {
            setFocus(true);
            ensureSelectedVisible();
        }
        layoutOptions();
        return this;
    }

    @Environment(EnvType.CLIENT)
    public DropDownBox toggleExpanded() {
        return setExpanded(!expanded);
    }

    public DropDownBox setPopupDirection(PopupDirection popupDirection) {
        this.popupDirection = popupDirection == null ? PopupDirection.DOWN : popupDirection;
        layoutOptions();
        return this;
    }

    public DropDownBox showUp() {
        return setPopupDirection(PopupDirection.UP);
    }

    public DropDownBox showDown() {
        return setPopupDirection(PopupDirection.DOWN);
    }

    public DropDownBox setOptionHeight(int optionHeight) {
        this.optionHeight = Math.max(1, optionHeight);
        layoutOptions();
        return this;
    }

    public DropDownBox setMaxVisibleOptions(int maxVisibleOptions) {
        this.maxVisibleOptions = Math.max(1, maxVisibleOptions);
        clampScrollIndex();
        layoutOptions();
        return this;
    }

    public DropDownBox setRowSpacing(int rowSpacing) {
        this.rowSpacing = Math.max(0, rowSpacing);
        layoutOptions();
        return this;
    }

    public DropDownBox setPopupSpacing(int popupSpacing) {
        this.popupSpacing = Math.max(0, popupSpacing);
        layoutOptions();
        return this;
    }

    public DropDownBox setPadding(int padding) {
        return setPadding(padding, padding, padding, padding);
    }

    public DropDownBox setPadding(int horizontal, int vertical) {
        return setPadding(horizontal, vertical, horizontal, vertical);
    }

    public DropDownBox setPadding(int left, int top, int right, int bottom) {
        this.paddingLeft = Math.max(0, left);
        this.paddingTop = Math.max(0, top);
        this.paddingRight = Math.max(0, right);
        this.paddingBottom = Math.max(0, bottom);
        layoutOptions();
        return this;
    }

    public DropDownBox setArrowWidth(int arrowWidth) {
        this.arrowWidth = Math.max(0, arrowWidth);
        layoutOptions();
        return this;
    }

    public DropDownBox setShowScrollBar(boolean showScrollBar) {
        this.showScrollBar = showScrollBar;
        layoutOptions();
        return this;
    }

    public DropDownBox setScrollBarWidth(int scrollBarWidth) {
        this.scrollBarWidth = Math.max(0, scrollBarWidth);
        layoutOptions();
        return this;
    }

    public DropDownBox setScrollBarPadding(int scrollBarPadding) {
        this.scrollBarPadding = Math.max(0, scrollBarPadding);
        layoutOptions();
        return this;
    }

    public DropDownBox setMinScrollThumbSize(int minScrollThumbSize) {
        this.minScrollThumbSize = Math.max(1, minScrollThumbSize);
        return this;
    }

    public DropDownBox setCloseOnSelect(boolean closeOnSelect) {
        this.closeOnSelect = closeOnSelect;
        return this;
    }

    public DropDownBox setResizeOptionsToRow(boolean resizeOptionsToRow) {
        this.resizeOptionsToRow = resizeOptionsToRow;
        layoutOptions();
        return this;
    }

    public DropDownBox setSelectedWidgetActiveWhenClosed(boolean selectedWidgetActiveWhenClosed) {
        this.selectedWidgetActiveWhenClosed = selectedWidgetActiveWhenClosed;
        layoutOptions();
        return this;
    }

    public DropDownBox setColors(int headerFill, int popupFill, int borderColor, int hoverFill, int selectedFill) {
        this.headerFill = headerFill;
        this.popupFill = popupFill;
        this.borderColor = borderColor;
        this.hoverFill = hoverFill;
        this.selectedHighlightColor = selectedFill;
        return this;
    }

    public DropDownBox setSelectedOptionOutline(int color) {
        this.selectedHighlightMode = HighlightMode.OUTLINE;
        this.selectedHighlightColor = color;
        return this;
    }

    public DropDownBox setSelectedOptionFill(int color) {
        this.selectedHighlightMode = HighlightMode.FILL;
        this.selectedHighlightColor = color;
        return this;
    }

    public DropDownBox setSelectedOptionHighlight(HighlightMode mode, int color) {
        this.selectedHighlightMode = mode == null ? HighlightMode.OUTLINE : mode;
        this.selectedHighlightColor = color;
        return this;
    }

    public DropDownBox setDisabledFill(int disabledFill) {
        this.disabledFill = disabledFill;
        return this;
    }

    public DropDownBox setArrowColor(int arrowColor) {
        this.arrowColor = arrowColor;
        return this;
    }

    public DropDownBox setScrollBarColors(int trackColor, int thumbColor, int thumbHoverColor) {
        this.scrollTrackColor = trackColor;
        this.scrollThumbColor = thumbColor;
        this.scrollThumbHoverColor = thumbHoverColor;
        return this;
    }

    public DropDownBox setRadius(int radius) {
        this.radius = Math.max(0, radius);
        return this;
    }

    public DropDownBox setHeaderTexture(IGuiTexture headerTexture) {
        this.headerTexture = headerTexture;
        return this;
    }

    public DropDownBox setPopupTexture(IGuiTexture popupTexture) {
        this.popupTexture = popupTexture;
        return this;
    }

    public DropDownBox setOptionTextures(IGuiTexture hoverTexture, IGuiTexture selectedTexture, IGuiTexture disabledTexture) {
        this.hoverOptionTexture = hoverTexture;
        this.selectedOptionTexture = selectedTexture;
        this.disabledOptionTexture = disabledTexture;
        return this;
    }

    public DropDownBox setScrollBarTextures(IGuiTexture trackTexture, IGuiTexture thumbTexture, IGuiTexture thumbHoverTexture) {
        this.scrollTrackTexture = trackTexture;
        this.scrollThumbTexture = thumbTexture;
        this.scrollThumbHoverTexture = thumbHoverTexture;
        return this;
    }

    public DropDownBox setSelectionChangedListener(IntConsumer selectionChangedListener) {
        this.selectionChangedListener = selectionChangedListener;
        return this;
    }

    public DropDownBox setOptionSelectedListener(BiConsumer<Integer, Widget> optionSelectedListener) {
        this.optionSelectedListener = optionSelectedListener;
        return this;
    }

    @Override
    public void setSize(Size size) {
        super.setSize(size);
        layoutOptions();
    }

    @Override
    protected void onPositionUpdate() {
        super.onPositionUpdate();
        layoutOptions();
    }

    @Override
    protected void onChildSelfPositionUpdate(Widget child) {
        if (!layingOut) {
            layoutOptions();
        }
    }

    @Override
    protected void onChildSizeUpdate(Widget child) {
        if (!layingOut) {
            layoutOptions();
        }
    }

    @Override
    public boolean isMouseOverElement(double mouseX, double mouseY) {
        return super.isMouseOverElement(mouseX, mouseY)
                || (expanded && isMouseOverPopup(mouseX, mouseY));
    }

    @Override
    public @Nullable Widget getHoverElement(double mouseX, double mouseY) {
        for (int i = widgets.size() - 1; i >= 0; i--) {
            Widget widget = widgets.get(i);
            if (widget.isVisible()) {
                Widget hovered = widget.getHoverElement(mouseX, mouseY);
                if (hovered != null) {
                    return hovered;
                }
            }
        }
        return isMouseOverElement(mouseX, mouseY) ? this : null;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) {
            return super.mouseClicked(mouseX, mouseY, button);
        }

        if (expanded) {
            if (isMouseOverScrollBar(mouseX, mouseY)) {
                setFocus(true);
                if (isMouseOverScrollThumb(mouseX, mouseY)) {
                    draggingScrollThumb = true;
                    dragStartMouseY = mouseY;
                    dragStartScrollIndex = scrollIndex;
                } else {
                    scrollToMouse(mouseY);
                }
                return true;
            }

            int optionIndex = getOptionAt(mouseX, mouseY);
            if (optionIndex >= 0) {
                Widget optionWidget = options.get(optionIndex).widget;
                if (optionWidget.isVisible() && optionWidget.isActive()) {
                    optionWidget.mouseClicked(mouseX, mouseY, button);
                }
                selectFromClient(optionIndex);
                if (closeOnSelect) {
                    setExpanded(false);
                }
                Widget.playButtonClickSound();
                return true;
            }

            if (!isMouseOverElement(mouseX, mouseY)) {
                setExpanded(false);
                setFocus(false);
                return false;
            }
        }

        if (isMouseOverHeader(mouseX, mouseY)) {
            toggleExpanded();
            Widget.playButtonClickSound();
            return true;
        }

        return false;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (draggingScrollThumb && button == 0) {
            Rect track = getScrollBarTrackRect();
            Rect thumb = getScrollBarThumbRect();
            int movable = Math.max(1, track.height - thumb.height);
            int next = dragStartScrollIndex + (int) Math.round((mouseY - dragStartMouseY) * getMaxScrollIndex() / (double) movable);
            setScrollIndex(next);
            return true;
        }

        return expanded && super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (draggingScrollThumb && button == 0) {
            draggingScrollThumb = false;
            return true;
        }

        return expanded && super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean mouseWheelMove(double mouseX, double mouseY, double wheelDelta) {
        if (!expanded || !isMouseOverPopup(mouseX, mouseY) || getVisibleOptionCount() >= options.size()) {
            return false;
        }

        int delta = wheelDelta < 0 ? 1 : -1;
        setScrollIndex(scrollIndex + delta);
        return true;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (expanded && super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }

        if (!isFocus()) return false;

        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            setExpanded(false);
            setFocus(false);
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER || keyCode == GLFW.GLFW_KEY_SPACE) {
            toggleExpanded();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_DOWN) {
            if (!expanded) {
                setExpanded(true);
            } else {
                selectFromClient(findNextSelectable(selectedIndex, 1));
            }
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_UP) {
            if (!expanded) {
                setExpanded(true);
            } else {
                selectFromClient(findNextSelectable(selectedIndex, -1));
            }
            return true;
        }

        return false;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void onFocusChanged(@Nullable Widget lastFocus, Widget focus) {
        if (expanded && focus != this && (focus == null || !focus.isParent(this))) {
            setExpanded(false);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawInBackground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        drawHeader(graphics, mouseX, mouseY);
        drawClosedSelectedWidget(graphics, mouseX, mouseY, partialTicks, true);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawInForeground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        drawTooltipTexts(mouseX, mouseY);
        drawClosedSelectedWidget(graphics, mouseX, mouseY, partialTicks, false);
        drawArrow(graphics);

        if (expanded) {
            graphics.pose().pushPose();
            graphics.pose().translate(0, 0, 240);
            drawPopup(graphics, mouseX, mouseY, partialTicks);
            graphics.pose().popPose();
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawOverlay(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (!expanded) {
            super.drawOverlay(graphics, mouseX, mouseY, partialTicks);
            return;
        }

        for (int i = scrollIndex; i < getEndVisibleIndex(); i++) {
            Widget widget = options.get(i).widget;
            if (widget.isVisible()) {
                widget.drawOverlay(graphics, mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    public void handleClientAction(int id, net.minecraft.network.FriendlyByteBuf buffer) {
        super.handleClientAction(id, buffer);
        if (id == ACTION_SELECT) {
            setSelectedIndex(buffer.readVarInt(), true, false);
        }
    }

    protected void selectFromClient(int optionIndex) {
        setSelectedIndex(optionIndex, true, true);
    }

    protected void layoutOptions() {
        if (layingOut) return;
        layingOut = true;

        try {
            clampScrollIndex();
            int headerContentWidth = getHeaderContentWidth();
            int headerContentHeight = getHeaderContentHeight();
            int popupY = getPopupRelativeY();
            int rowWidth = getOptionRowWidth();
            int visibleRow = 0;

            for (int i = 0; i < options.size(); i++) {
                Option option = options.get(i);
                Widget widget = option.widget;
                boolean selectedInHeader = !expanded && i == selectedIndex;
                boolean visibleInPopup = expanded && i >= scrollIndex && i < getEndVisibleIndex();

                widget.setVisible(selectedInHeader || visibleInPopup);
                widget.setActive(visibleInPopup && option.enabled || selectedInHeader && selectedWidgetActiveWhenClosed);

                if (selectedInHeader) {
                    if (resizeOptionsToRow) {
                        widget.setSize(headerContentWidth, headerContentHeight);
                    }
                    widget.setSelfPosition(
                            paddingLeft,
                            paddingTop + Math.max(0, (headerContentHeight - widget.getSizeHeight()) / 2)
                    );
                } else if (visibleInPopup) {
                    if (resizeOptionsToRow) {
                        widget.setSize(rowWidth, optionHeight);
                    }
                    widget.setSelfPosition(
                            0,
                            popupY + visibleRow * (optionHeight + rowSpacing) + Math.max(0, (optionHeight - widget.getSizeHeight()) / 2)
                    );
                    visibleRow++;
                }
            }
        } finally {
            layingOut = false;
        }
    }

    protected void drawHeader(GuiGraphics graphics, int mouseX, int mouseY) {
        int x = getPositionX();
        int y = getPositionY();
        if (headerTexture != null) {
            headerTexture.draw(graphics, mouseX, mouseY, x, y, getSizeWidth(), getSizeHeight());
        } else {
            int fill = isMouseOverHeader(mouseX, mouseY) && isActive() ? hoverFill : headerFill;
            new ColorRectAndBorderTexture(fill, borderColor, 1)
                    .setRadius(radius)
                    .draw(graphics, mouseX, mouseY, x, y, getSizeWidth(), getSizeHeight());
        }
    }

    protected void drawArrow(GuiGraphics graphics) {
        if (arrowWidth <= 0) return;

        Font font = Minecraft.getInstance().font;
        String arrow = expanded ? "▲" : "▼";
        int x = getPositionX() + getSizeWidth() - arrowWidth + Math.max(0, (arrowWidth - font.width(arrow)) / 2);
        int y = getPositionY() + Math.max(0, (getSizeHeight() - font.lineHeight) / 2);
        graphics.drawString(font, arrow, x, y, arrowColor, false);
    }

    protected void drawClosedSelectedWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, boolean background) {
        if (expanded || selectedIndex < 0 || selectedIndex >= options.size()) return;

        Widget selected = options.get(selectedIndex).widget;
        if (!selected.isVisible()) return;

        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        if (background) {
            selected.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        } else {
            selected.drawInForeground(graphics, mouseX, mouseY, partialTicks);
        }
    }

    protected void drawPopup(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int x = getPositionX();
        int y = getPositionY() + getPopupRelativeY();
        int width = getPopupWidth();
        int height = getPopupHeight();

        if (popupTexture != null) {
            popupTexture.draw(graphics, mouseX, mouseY, x, y, width, height);
        } else {
            new ColorRectAndBorderTexture(popupFill, borderColor, 1)
                    .setRadius(radius)
                    .draw(graphics, mouseX, mouseY, x, y, width, height);
        }

        for (int i = scrollIndex; i < getEndVisibleIndex(); i++) {
            drawOptionBackground(graphics, mouseX, mouseY, i);
        }

        for (int i = scrollIndex; i < getEndVisibleIndex(); i++) {
            Widget widget = options.get(i).widget;
            if (!widget.isVisible()) continue;

            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.enableBlend();
            widget.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        }

        for (int i = scrollIndex; i < getEndVisibleIndex(); i++) {
            Widget widget = options.get(i).widget;
            if (!widget.isVisible()) continue;

            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.enableBlend();
            widget.drawInForeground(graphics, mouseX, mouseY, partialTicks);
        }

        drawScrollBar(graphics, mouseX, mouseY);
    }

    protected void drawOptionBackground(GuiGraphics graphics, int mouseX, int mouseY, int index) {
        Rect rect = getOptionRect(index);
        Option option = options.get(index);
        boolean hovered = option.enabled && isMouseOver(rect.x, rect.y, rect.width, rect.height, mouseX, mouseY);
        boolean selected = index == selectedIndex;

        IGuiTexture texture = !option.enabled ? disabledOptionTexture : selected ? selectedOptionTexture : hovered ? hoverOptionTexture : null;
        if (texture != null) {
            texture.draw(graphics, mouseX, mouseY, rect.x, rect.y, rect.width, rect.height);
            return;
        }

        if (!option.enabled) {
            if (disabledFill != 0) {
                graphics.fill(rect.x, rect.y, rect.x + rect.width, rect.y + rect.height, disabledFill);
            }
            return;
        }

        if (selected) {
            if (selectedHighlightMode == HighlightMode.FILL) {
                if (selectedHighlightColor != 0) {
                    graphics.fill(rect.x, rect.y, rect.x + rect.width, rect.y + rect.height, selectedHighlightColor);
                }
                return;
            }

            if (hovered && hoverFill != 0) {
                graphics.fill(rect.x, rect.y, rect.x + rect.width, rect.y + rect.height, hoverFill);
            }
            if (selectedHighlightColor != 0) {
                drawOutline(graphics, rect.x, rect.y, rect.width, rect.height, selectedHighlightColor, 1);
            }
            return;
        }

        if (hovered && hoverFill != 0) {
            graphics.fill(rect.x, rect.y, rect.x + rect.width, rect.y + rect.height, hoverFill);
        }
    }

    protected void drawOutline(GuiGraphics graphics, int x, int y, int width, int height, int color, int thickness) {
        if (color == 0 || width <= 0 || height <= 0) return;

        int line = Math.max(1, Math.min(thickness, Math.min(width, height)));
        graphics.fill(x, y, x + width, y + line, color);
        graphics.fill(x, y + height - line, x + width, y + height, color);
        graphics.fill(x, y + line, x + line, y + height - line, color);
        graphics.fill(x + width - line, y + line, x + width, y + height - line, color);
    }

    protected boolean isMouseOverHeader(double mouseX, double mouseY) {
        return isMouseOver(getPositionX(), getPositionY(), getSizeWidth(), getSizeHeight(), mouseX, mouseY);
    }

    protected boolean isMouseOverPopup(double mouseX, double mouseY) {
        int x = getPositionX();
        int y = getPositionY() + getPopupRelativeY();
        return isMouseOver(x, y, getPopupWidth(), getPopupHeight(), mouseX, mouseY);
    }

    protected int getOptionAt(double mouseX, double mouseY) {
        if (!expanded) return -1;
        if (isMouseOverScrollBar(mouseX, mouseY)) return -1;

        for (int i = scrollIndex; i < getEndVisibleIndex(); i++) {
            if (!options.get(i).enabled) continue;

            Rect rect = getOptionRect(i);
            if (isMouseOver(rect.x, rect.y, rect.width, rect.height, mouseX, mouseY)) {
                return i;
            }
        }

        return -1;
    }

    protected Rect getOptionRect(int index) {
        int visibleRow = index - scrollIndex;
        int x = getPositionX();
        int y = getPositionY() + getPopupRelativeY() + visibleRow * (optionHeight + rowSpacing);
        return new Rect(x, y, getOptionRowWidth(), optionHeight);
    }

    protected int getHeaderContentWidth() {
        return Math.max(0, getSizeWidth() - paddingLeft - paddingRight - arrowWidth);
    }

    protected int getHeaderContentHeight() {
        return Math.max(0, getSizeHeight() - paddingTop - paddingBottom);
    }

    protected int getPopupWidth() {
        return Math.max(1, getSizeWidth());
    }

    protected int getOptionRowWidth() {
        int width = getPopupWidth();
        if (hasVisibleScrollBar()) {
            width -= scrollBarWidth + scrollBarPadding * 2;
        }
        return Math.max(1, width);
    }

    protected int getVisibleOptionCount() {
        return Math.min(maxVisibleOptions, options.size());
    }

    protected int getEndVisibleIndex() {
        return Math.min(options.size(), scrollIndex + getVisibleOptionCount());
    }

    protected int getPopupHeight() {
        int rows = getVisibleOptionCount();
        if (rows <= 0) return 0;
        return rows * optionHeight + Math.max(0, rows - 1) * rowSpacing;
    }

    protected int getPopupRelativeY() {
        int popupHeight = getPopupHeight();
        return popupDirection == PopupDirection.UP
                ? -popupHeight - popupSpacing
                : getSizeHeight() + popupSpacing;
    }

    protected int indexOfOption(Widget widget) {
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).widget == widget) return i;
        }
        return -1;
    }

    protected int normalizeSelectableIndex(int index) {
        if (index < 0 || index >= options.size()) return -1;
        return options.get(index).enabled ? index : findFirstEnabledOption();
    }

    protected int findFirstEnabledOption() {
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).enabled) return i;
        }
        return -1;
    }

    protected int findNextSelectable(int current, int direction) {
        if (options.isEmpty()) return -1;

        int step = direction < 0 ? -1 : 1;
        int start = current < 0 ? (step > 0 ? -1 : options.size()) : current;
        for (int i = 1; i <= options.size(); i++) {
            int next = Math.floorMod(start + step * i, options.size());
            if (options.get(next).enabled) {
                return next;
            }
        }
        return -1;
    }

    protected void ensureSelectedVisible() {
        if (selectedIndex < 0) return;

        int visibleCount = getVisibleOptionCount();
        if (selectedIndex < scrollIndex) {
            scrollIndex = selectedIndex;
        } else if (selectedIndex >= scrollIndex + visibleCount) {
            scrollIndex = selectedIndex - visibleCount + 1;
        }
        clampScrollIndex();
    }

    protected void clampScrollIndex() {
        scrollIndex = clamp(scrollIndex, 0, getMaxScrollIndex());
    }

    protected int getMaxScrollIndex() {
        return Math.max(0, options.size() - getVisibleOptionCount());
    }

    protected void setScrollIndex(int scrollIndex) {
        int previous = this.scrollIndex;
        this.scrollIndex = clamp(scrollIndex, 0, getMaxScrollIndex());
        if (previous != this.scrollIndex) {
            layoutOptions();
        }
    }

    protected boolean hasVisibleScrollBar() {
        return showScrollBar && scrollBarWidth > 0 && getMaxScrollIndex() > 0;
    }

    protected void drawScrollBar(GuiGraphics graphics, int mouseX, int mouseY) {
        if (!hasVisibleScrollBar()) return;

        Rect track = getScrollBarTrackRect();
        Rect thumb = getScrollBarThumbRect();

        if (scrollTrackTexture != null) {
            scrollTrackTexture.draw(graphics, mouseX, mouseY, track.x, track.y, track.width, track.height);
        } else {
            graphics.fill(track.x, track.y, track.x + track.width, track.y + track.height, scrollTrackColor);
        }

        IGuiTexture thumbTexture = (draggingScrollThumb || isMouseOverScrollThumb(mouseX, mouseY))
                ? scrollThumbHoverTexture
                : scrollThumbTexture;
        int thumbColor = (draggingScrollThumb || isMouseOverScrollThumb(mouseX, mouseY))
                ? scrollThumbHoverColor
                : scrollThumbColor;

        if (thumbTexture != null) {
            thumbTexture.draw(graphics, mouseX, mouseY, thumb.x, thumb.y, thumb.width, thumb.height);
        } else {
            graphics.fill(thumb.x, thumb.y, thumb.x + thumb.width, thumb.y + thumb.height, thumbColor);
        }
    }

    protected Rect getScrollBarTrackRect() {
        if (!hasVisibleScrollBar()) {
            return new Rect(0, 0, 0, 0);
        }

        int x = getPositionX() + getPopupWidth() - scrollBarPadding - scrollBarWidth;
        int y = getPositionY() + getPopupRelativeY() + scrollBarPadding;
        int height = Math.max(1, getPopupHeight() - scrollBarPadding * 2);
        return new Rect(x, y, scrollBarWidth, height);
    }

    protected Rect getScrollBarThumbRect() {
        Rect track = getScrollBarTrackRect();
        if (track.height <= 0) {
            return track;
        }

        int visible = getVisibleOptionCount();
        int total = Math.max(visible, options.size());
        int thumbHeight = clamp((int) Math.round(track.height * (visible / (double) total)), Math.min(track.height, minScrollThumbSize), track.height);
        int maxScroll = getMaxScrollIndex();
        int movable = Math.max(0, track.height - thumbHeight);
        int y = track.y + (maxScroll <= 0 ? 0 : (int) Math.round(movable * (scrollIndex / (double) maxScroll)));
        return new Rect(track.x, y, track.width, thumbHeight);
    }

    protected boolean isMouseOverScrollBar(double mouseX, double mouseY) {
        if (!hasVisibleScrollBar()) return false;
        Rect track = getScrollBarTrackRect();
        return isMouseOver(track.x, track.y, track.width, track.height, mouseX, mouseY);
    }

    protected boolean isMouseOverScrollThumb(double mouseX, double mouseY) {
        if (!hasVisibleScrollBar()) return false;
        Rect thumb = getScrollBarThumbRect();
        return isMouseOver(thumb.x, thumb.y, thumb.width, thumb.height, mouseX, mouseY);
    }

    protected void scrollToMouse(double mouseY) {
        Rect track = getScrollBarTrackRect();
        Rect thumb = getScrollBarThumbRect();
        int movable = Math.max(1, track.height - thumb.height);
        int relative = (int) Math.round(mouseY - track.y - thumb.height / 2.0);
        int next = (int) Math.round(clamp(relative, 0, movable) * getMaxScrollIndex() / (double) movable);
        setScrollIndex(next);
    }

    protected int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    protected static class Option {
        protected final Widget widget;
        protected boolean enabled;

        protected Option(Widget widget, boolean enabled) {
            this.widget = widget;
            this.enabled = enabled;
        }
    }

    protected record Rect(int x, int y, int width, int height) {
    }
}
