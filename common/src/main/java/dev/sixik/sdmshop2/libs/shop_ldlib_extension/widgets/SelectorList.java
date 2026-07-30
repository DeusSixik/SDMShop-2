package dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.shop.client.textures.ColorRectAndBorderTexture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.IntConsumer;

public class SelectorList extends Widget {

    public enum Direction {
        VERTICAL,
        HORIZONTAL
    }

    private static final int DEFAULT_WIDTH = 120;
    private static final int DEFAULT_HEIGHT = 20;
    private static final int DEFAULT_OPTION_HEIGHT = 14;
    private static final int DEFAULT_MARKER_SIZE = 8;
    private static final int DEFAULT_MARKER_GAP = 4;
    private static final int DEFAULT_OPTION_SPACING = 2;
    private static final int DEFAULT_PADDING = 2;
    private static final int DEFAULT_TEXT_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_DISABLED_TEXT_COLOR = 0xFF777777;
    private static final int DEFAULT_MARKER_FILL_COLOR = 0x00000000;
    private static final int DEFAULT_MARKER_BORDER_COLOR = 0xFF777777;
    private static final int DEFAULT_SELECTED_FILL_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_HOVER_FILL_COLOR = 0x22FFFFFF;

    protected final List<SelectorOption> options = new ArrayList<>();

    protected Direction direction = Direction.VERTICAL;
    protected int selectedIndex = -1;
    protected boolean allowEmptySelection = true;
    protected boolean deselectOnSelectedClick = true;

    protected int optionHeight = DEFAULT_OPTION_HEIGHT;
    protected int markerSize = DEFAULT_MARKER_SIZE;
    protected int markerGap = DEFAULT_MARKER_GAP;
    protected int optionSpacing = DEFAULT_OPTION_SPACING;
    protected int paddingLeft = DEFAULT_PADDING;
    protected int paddingTop = DEFAULT_PADDING;
    protected int paddingRight = DEFAULT_PADDING;
    protected int paddingBottom = DEFAULT_PADDING;

    protected int textColor = DEFAULT_TEXT_COLOR;
    protected int disabledTextColor = DEFAULT_DISABLED_TEXT_COLOR;
    protected int markerFillColor = DEFAULT_MARKER_FILL_COLOR;
    protected int markerBorderColor = DEFAULT_MARKER_BORDER_COLOR;
    protected int selectedFillColor = DEFAULT_SELECTED_FILL_COLOR;
    protected int hoverFillColor = DEFAULT_HOVER_FILL_COLOR;
    protected boolean textShadow;
    protected float scale = 1.0f;

    protected IntConsumer selectionChangedListener;

    public SelectorList() {
        this(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public SelectorList(Component... options) {
        this(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, options);
    }

    public SelectorList(String... options) {
        this();
        setOptions(options);
    }

    public SelectorList(Widget... options) {
        this();
        setOptions(options);
    }

    public SelectorList(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public SelectorList(Position position, Size size) {
        super(position, size);
    }

    public SelectorList(int x, int y, int width, int height, Component... options) {
        this(x, y, width, height);
        setOptions(options);
    }

    public SelectorList setOptions(Component... options) {
        this.options.clear();
        if (options != null) {
            for (Component option : options) {
                this.options.add(SelectorOption.text(option));
            }
        }
        clampSelectedIndex(true);
        return this;
    }

    public SelectorList setOptions(String... options) {
        this.options.clear();
        if (options != null) {
            for (String option : options) {
                this.options.add(SelectorOption.text(option == null ? Component.empty() : Component.literal(option)));
            }
        }
        clampSelectedIndex(true);
        return this;
    }

    public SelectorList setOptions(Widget... options) {
        this.options.clear();
        if (options != null) {
            for (Widget option : options) {
                this.options.add(SelectorOption.widget(option));
            }
        }
        clampSelectedIndex(true);
        return this;
    }

    public SelectorList addOption(Component option) {
        this.options.add(SelectorOption.text(option));
        clampSelectedIndex(true);
        return this;
    }

    public SelectorList addOption(String option) {
        return addOption(option == null ? Component.empty() : Component.literal(option));
    }

    public SelectorList addOption(Widget widget) {
        this.options.add(SelectorOption.widget(widget));
        clampSelectedIndex(true);
        return this;
    }

    public SelectorList addWidgetOption(Widget widget) {
        return addOption(widget);
    }

    public SelectorList clearOptions() {
        this.options.clear();
        int previousIndex = selectedIndex;
        selectedIndex = -1;
        if (previousIndex != selectedIndex) {
            notifySelectionChanged();
        }
        return this;
    }

    public List<Component> getOptions() {
        List<Component> textOptions = new ArrayList<>(options.size());
        for (SelectorOption option : options) {
            textOptions.add(option.text == null ? Component.empty() : option.text);
        }
        return Collections.unmodifiableList(textOptions);
    }

    public Widget getOptionWidget(int index) {
        return index >= 0 && index < options.size() ? options.get(index).widget : null;
    }

    public SelectorList setDirection(Direction direction) {
        this.direction = direction == null ? Direction.VERTICAL : direction;
        return this;
    }

    public SelectorList vertical() {
        return setDirection(Direction.VERTICAL);
    }

    public SelectorList horizontal() {
        return setDirection(Direction.HORIZONTAL);
    }

    public Direction getDirection() {
        return direction;
    }

    public SelectorList setSelectedIndex(int selectedIndex) {
        int nextIndex = normalizeSelectedIndex(selectedIndex);
        if (this.selectedIndex == nextIndex) {
            return this;
        }

        this.selectedIndex = nextIndex;
        notifySelectionChanged();
        return this;
    }

    public SelectorList select(int index) {
        return setSelectedIndex(index);
    }

    public SelectorList clearSelection() {
        return setSelectedIndex(-1);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public Component getSelectedOption() {
        if (selectedIndex < 0 || selectedIndex >= options.size()) return Component.empty();
        Component selectedText = options.get(selectedIndex).text;
        return selectedText == null ? Component.empty() : selectedText;
    }

    public Widget getSelectedOptionWidget() {
        return selectedIndex >= 0 && selectedIndex < options.size() ? options.get(selectedIndex).widget : null;
    }

    public boolean hasSelection() {
        return selectedIndex >= 0 && selectedIndex < options.size();
    }

    public SelectorList setAllowEmptySelection(boolean allowEmptySelection) {
        this.allowEmptySelection = allowEmptySelection;
        clampSelectedIndex(true);
        return this;
    }

    public SelectorList allowEmptySelection() {
        return setAllowEmptySelection(true);
    }

    public SelectorList requireSelection() {
        return setAllowEmptySelection(false);
    }

    public boolean isAllowEmptySelection() {
        return allowEmptySelection;
    }

    public SelectorList setDeselectOnSelectedClick(boolean deselectOnSelectedClick) {
        this.deselectOnSelectedClick = deselectOnSelectedClick;
        return this;
    }

    public SelectorList setAllowDeselect(boolean allowDeselect) {
        return setDeselectOnSelectedClick(allowDeselect);
    }

    public SelectorList allowDeselect() {
        return setAllowDeselect(true);
    }

    public SelectorList preventDeselect() {
        return setAllowDeselect(false);
    }

    public boolean isDeselectOnSelectedClick() {
        return deselectOnSelectedClick;
    }

    public boolean isAllowDeselect() {
        return deselectOnSelectedClick;
    }

    public SelectorList setSelectionChangedListener(IntConsumer selectionChangedListener) {
        this.selectionChangedListener = selectionChangedListener;
        return this;
    }

    public SelectorList onSelectionChanged(IntConsumer selectionChangedListener) {
        return setSelectionChangedListener(selectionChangedListener);
    }

    public SelectorList setOptionHeight(int optionHeight) {
        this.optionHeight = Math.max(1, optionHeight);
        return this;
    }

    public SelectorList setMarkerSize(int markerSize) {
        this.markerSize = Math.max(1, markerSize);
        return this;
    }

    public SelectorList setMarkerGap(int markerGap) {
        this.markerGap = Math.max(0, markerGap);
        return this;
    }

    public SelectorList setOptionSpacing(int optionSpacing) {
        this.optionSpacing = Math.max(0, optionSpacing);
        return this;
    }

    public SelectorList setPadding(int padding) {
        return setPadding(padding, padding, padding, padding);
    }

    public SelectorList setPadding(int horizontal, int vertical) {
        return setPadding(horizontal, vertical, horizontal, vertical);
    }

    public SelectorList setPadding(int left, int top, int right, int bottom) {
        this.paddingLeft = Math.max(0, left);
        this.paddingTop = Math.max(0, top);
        this.paddingRight = Math.max(0, right);
        this.paddingBottom = Math.max(0, bottom);
        return this;
    }

    public SelectorList setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public SelectorList setDisabledTextColor(int disabledTextColor) {
        this.disabledTextColor = disabledTextColor;
        return this;
    }

    public SelectorList setMarkerColors(int fillColor, int borderColor) {
        this.markerFillColor = fillColor;
        this.markerBorderColor = borderColor;
        return this;
    }

    public SelectorList setSelectedFillColor(int selectedFillColor) {
        this.selectedFillColor = selectedFillColor;
        return this;
    }

    public SelectorList setHoverFillColor(int hoverFillColor) {
        this.hoverFillColor = hoverFillColor;
        return this;
    }

    public SelectorList setTextShadow(boolean textShadow) {
        this.textShadow = textShadow;
        return this;
    }

    public SelectorList setScale(float scale) {
        this.scale = Math.max(0.01f, scale);
        return this;
    }

    public SelectorList scale(float scale) {
        return setScale(scale);
    }

    public float getScale() {
        return scale;
    }

    public int getContentHeightWithScale() {
        if (options.isEmpty()) {
            return getScaledPaddingTop() + getScaledPaddingBottom();
        }

        Font font = Minecraft.getInstance().font;
        return switch (direction) {
            case VERTICAL -> {
                int height = getScaledPaddingTop() + getScaledPaddingBottom();
                for (int i = 0; i < options.size(); i++) {
                    height += getOptionHeight(font, options.get(i));
                    if (i > 0) {
                        height += getScaledOptionSpacing();
                    }
                }
                yield height;
            }
            case HORIZONTAL -> getScaledPaddingTop() + getScaledPaddingBottom() + getMaxOptionHeight(font);
        };
    }

    public int getContentWidthWithScale() {
        Font font = Minecraft.getInstance().font;
        int width = getScaledPaddingLeft() + getScaledPaddingRight();
        if (options.isEmpty()) return width;

        if (direction == Direction.VERTICAL) {
            int maxOptionWidth = 0;
            for (SelectorOption option : options) {
                maxOptionWidth = Math.max(maxOptionWidth, getOptionWidth(font, option));
            }
            return width + maxOptionWidth;
        }

        int optionsWidth = 0;
        for (int i = 0; i < options.size(); i++) {
            optionsWidth += getOptionWidth(font, options.get(i));
            if (i > 0) {
                optionsWidth += getScaledOptionSpacing();
            }
        }
        return width + optionsWidth;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawInBackground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        if (options.isEmpty()) return;

        Font font = Minecraft.getInstance().font;
        for (int i = 0; i < options.size(); i++) {
            OptionBounds bounds = getOptionBounds(font, i);
            if (bounds == null) continue;

            boolean hovered = isActive() && isMouseOver(bounds.x, bounds.y, bounds.width, bounds.height, mouseX, mouseY);
            boolean selected = i == selectedIndex;

            if (hovered && hoverFillColor != 0) {
                graphics.fill(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, hoverFillColor);
            }

            drawMarker(graphics, mouseX, mouseY, bounds, selected);
            drawOptionContentBackground(graphics, font, options.get(i), bounds, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawInForeground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInForeground(graphics, mouseX, mouseY, partialTicks);
        if (options.isEmpty()) return;

        Font font = Minecraft.getInstance().font;
        for (int i = 0; i < options.size(); i++) {
            SelectorOption option = options.get(i);
            if (!option.hasWidget()) continue;

            OptionBounds bounds = getOptionBounds(font, i);
            if (bounds == null) continue;

            drawOptionWidget(graphics, option.widget, bounds, mouseX, mouseY, partialTicks, false);
        }
    }

    private void drawMarker(GuiGraphics graphics, int mouseX, int mouseY, OptionBounds bounds, boolean selected) {
        int marker = getScaledMarkerSize();
        int markerX = bounds.x;
        int markerY = bounds.y + Math.max(0, (bounds.height - marker) / 2);
        int radius = Math.max(1, marker / 2);

        new ColorRectAndBorderTexture(markerFillColor, markerBorderColor, 1)
                .setRadius(radius)
                .draw(graphics, mouseX, mouseY, markerX, markerY, marker, marker);

        if (!selected) return;

        int dotSize = Math.max(1, Math.round(marker * 0.5f));
        int dotX = markerX + Math.max(0, (marker - dotSize) / 2);
        int dotY = markerY + Math.max(0, (marker - dotSize) / 2);
        new ColorRectAndBorderTexture(selectedFillColor, selectedFillColor, 0)
                .setRadius(Math.max(1, dotSize / 2))
                .draw(graphics, mouseX, mouseY, dotX, dotY, dotSize, dotSize);
    }

    private void drawOptionContentBackground(GuiGraphics graphics, Font font, SelectorOption option, OptionBounds bounds, int mouseX, int mouseY, float partialTicks) {
        if (option.hasWidget()) {
            drawOptionWidget(graphics, option.widget, bounds, mouseX, mouseY, partialTicks, true);
            return;
        }

        drawOptionText(graphics, font, option.text == null ? Component.empty() : option.text, bounds);
    }

    private void drawOptionText(GuiGraphics graphics, Font font, Component option, OptionBounds bounds) {
        int marker = getScaledMarkerSize();
        int textX = bounds.x + marker + getScaledMarkerGap();
        int textY = bounds.y + Math.max(0, (bounds.height - getScaledFontHeight(font)) / 2);
        int textWidth = Math.max(0, bounds.x + bounds.width - textX);
        if (textWidth <= 0) return;

        float drawScale = Math.max(0.01f, scale);
        graphics.enableScissor(textX, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height);
        try {
            graphics.pose().pushPose();
            try {
                graphics.pose().translate(textX, textY, 0);
                graphics.pose().scale(drawScale, drawScale, 1.0f);
                graphics.drawString(font, option.getVisualOrderText(), 0, 0, isActive() ? textColor : disabledTextColor, textShadow);
            } finally {
                graphics.pose().popPose();
            }
        } finally {
            graphics.disableScissor();
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isActive() || !isVisible() || button != 0) {
            return false;
        }

        int optionIndex = getOptionIndexAt(mouseX, mouseY);
        if (optionIndex < 0) {
            return false;
        }

        int nextIndex = optionIndex;
        if (optionIndex == selectedIndex && allowEmptySelection && deselectOnSelectedClick) {
            nextIndex = -1;
        }

        if (nextIndex != selectedIndex) {
            selectedIndex = nextIndex;
            writeSelectionClientAction();
            notifySelectionChanged();
        }

        playButtonClickSound();
        return true;
    }

    @Override
    public void handleClientAction(int id, FriendlyByteBuf buffer) {
        super.handleClientAction(id, buffer);
        if (id == 1) {
            selectedIndex = normalizeSelectedIndex(buffer.readVarInt());
            notifySelectionChanged();
        }
    }

    private int getOptionIndexAt(double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        for (int i = 0; i < options.size(); i++) {
            OptionBounds bounds = getOptionBounds(font, i);
            if (bounds != null && isMouseOver(bounds.x, bounds.y, bounds.width, bounds.height, mouseX, mouseY)) {
                return i;
            }
        }
        return -1;
    }

    private OptionBounds getOptionBounds(Font font, int index) {
        if (index < 0 || index >= options.size()) return null;

        int x = getPositionX() + getScaledPaddingLeft();
        int y = getPositionY() + getScaledPaddingTop();

        if (direction == Direction.VERTICAL) {
            for (int i = 0; i < index; i++) {
                y += getOptionHeight(font, options.get(i)) + getScaledOptionSpacing();
            }
            int height = getOptionHeight(font, options.get(index));
            int width = Math.max(1, getSizeWidth() - getScaledPaddingLeft() - getScaledPaddingRight());
            return new OptionBounds(x, y, width, height);
        }

        int height = getMaxOptionHeight(font);
        int optionX = x;
        for (int i = 0; i < index; i++) {
            optionX += getOptionWidth(font, options.get(i)) + getScaledOptionSpacing();
        }

        return new OptionBounds(optionX, y, getOptionWidth(font, options.get(index)), height);
    }

    private int getOptionWidth(Font font, SelectorOption option) {
        return getScaledMarkerSize()
                + getScaledMarkerGap()
                + getContentWidth(font, option);
    }

    private int getOptionHeight(Font font, SelectorOption option) {
        return Math.max(getScaledOptionHeight(), getContentHeight(font, option));
    }

    private int getMaxOptionHeight(Font font) {
        int height = getScaledOptionHeight();
        for (SelectorOption option : options) {
            height = Math.max(height, getOptionHeight(font, option));
        }
        return height;
    }

    private int getContentWidth(Font font, SelectorOption option) {
        if (option == null) return 0;
        if (option.hasWidget()) {
            return Math.max(0, Math.round(option.widget.getSizeWidth() * scale));
        }
        return Math.round(font.width(option.text == null ? Component.empty() : option.text) * scale);
    }

    private int getContentHeight(Font font, SelectorOption option) {
        if (option == null) return 0;
        if (option.hasWidget()) {
            return Math.max(0, Math.round(option.widget.getSizeHeight() * scale));
        }
        return getScaledFontHeight(font);
    }

    private void drawOptionWidget(GuiGraphics graphics, Widget widget, OptionBounds bounds, int mouseX, int mouseY, float partialTicks, boolean background) {
        if (widget == null) return;

        Font font = Minecraft.getInstance().font;
        int marker = getScaledMarkerSize();
        int contentX = bounds.x + marker + getScaledMarkerGap();
        int contentWidth = Math.max(0, bounds.x + bounds.width - contentX);
        if (contentWidth <= 0) return;

        float drawScale = Math.max(0.01f, scale);
        int scaledWidgetHeight = getContentHeight(font, SelectorOption.widget(widget));
        int contentY = bounds.y + Math.max(0, (bounds.height - scaledWidgetHeight) / 2);

        WidgetRenderState state = captureWidgetState(widget);
        graphics.enableScissor(contentX, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height);
        try {
            widget.setVisible(true);
            widget.setSelfPosition(0, 0);

            graphics.pose().pushPose();
            try {
                graphics.pose().translate(contentX, contentY, 0);
                graphics.pose().scale(drawScale, drawScale, 1.0f);

                int scaledMouseX = Math.round((mouseX - contentX) / drawScale);
                int scaledMouseY = Math.round((mouseY - contentY) / drawScale);
                if (background) {
                    widget.drawInBackground(graphics, scaledMouseX, scaledMouseY, partialTicks);
                } else {
                    widget.drawInForeground(graphics, scaledMouseX, scaledMouseY, partialTicks);
                }
            } finally {
                graphics.pose().popPose();
            }
        } finally {
            restoreWidgetState(widget, state);
            graphics.disableScissor();
        }
    }

    private void clampSelectedIndex(boolean notify) {
        int previousIndex = selectedIndex;
        selectedIndex = normalizeSelectedIndex(selectedIndex);
        if (notify && previousIndex != selectedIndex) {
            notifySelectionChanged();
        }
    }

    private int normalizeSelectedIndex(int index) {
        if (options.isEmpty()) return -1;
        if (index >= 0 && index < options.size()) return index;
        return allowEmptySelection ? -1 : 0;
    }

    private void writeSelectionClientAction() {
        writeClientAction(1, buffer -> buffer.writeVarInt(selectedIndex));
    }

    private void notifySelectionChanged() {
        if (selectionChangedListener != null) {
            selectionChangedListener.accept(selectedIndex);
        }
    }

    private int getScaledOptionHeight() {
        return Math.max(1, Math.round(optionHeight * scale));
    }

    private int getScaledMarkerSize() {
        return Math.max(1, Math.round(markerSize * scale));
    }

    private int getScaledMarkerGap() {
        return Math.max(0, Math.round(markerGap * scale));
    }

    private int getScaledOptionSpacing() {
        return Math.max(0, Math.round(optionSpacing * scale));
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

    private int getScaledFontHeight(Font font) {
        return Math.max(1, Math.round(font.lineHeight * scale));
    }

    private WidgetRenderState captureWidgetState(Widget widget) {
        return new WidgetRenderState(
                widget.getSelfPositionX(),
                widget.getSelfPositionY(),
                widget.isVisible()
        );
    }

    private void restoreWidgetState(Widget widget, WidgetRenderState state) {
        widget.setSelfPosition(state.x, state.y);
        widget.setVisible(state.visible);
    }

    private static class SelectorOption {
        private final Component text;
        private final Widget widget;

        private SelectorOption(Component text, Widget widget) {
            this.text = text;
            this.widget = widget;
        }

        private static SelectorOption text(Component text) {
            return new SelectorOption(text == null ? Component.empty() : text, null);
        }

        private static SelectorOption widget(Widget widget) {
            return new SelectorOption(Component.empty(), widget);
        }

        private boolean hasWidget() {
            return widget != null;
        }
    }

    private static class WidgetRenderState {
        private final int x;
        private final int y;
        private final boolean visible;

        private WidgetRenderState(int x, int y, boolean visible) {
            this.x = x;
            this.y = y;
            this.visible = visible;
        }
    }

    private static class OptionBounds {
        private final int x;
        private final int y;
        private final int width;
        private final int height;

        private OptionBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }
}
