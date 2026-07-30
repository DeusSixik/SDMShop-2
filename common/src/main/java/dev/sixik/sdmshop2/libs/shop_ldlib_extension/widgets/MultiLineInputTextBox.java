package dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.shop.client.textures.ColorRectAndBorderTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class MultiLineInputTextBox extends Widget {

    public static final int DEFAULT_BACKGROUND_COLOR = InputTextBox.DEFAULT_BACKGROUND_COLOR;
    public static final int DEFAULT_BORDER_COLOR = InputTextBox.DEFAULT_BORDER_COLOR;
    public static final int DEFAULT_FOCUSED_BORDER_COLOR = InputTextBox.DEFAULT_FOCUSED_BORDER_COLOR;
    public static final int DEFAULT_TEXT_COLOR = InputTextBox.DEFAULT_TEXT_COLOR;
    public static final int DEFAULT_PLACEHOLDER_COLOR = InputTextBox.DEFAULT_PLACEHOLDER_COLOR;
    public static final int DEFAULT_SELECTION_COLOR = InputTextBox.DEFAULT_SELECTION_COLOR;
    public static final int DEFAULT_CURSOR_COLOR = InputTextBox.DEFAULT_CURSOR_COLOR;

    protected String value = "";
    protected Component placeholder = Component.empty();

    protected Supplier<String> textSupplier;
    protected Consumer<String> textResponder;
    protected Function<String, String> textValidator = Function.identity();
    protected Predicate<Character> charFilter = character -> character != null && isAllowedInputCharacter(character);

    protected int cursorPosition;
    protected int selectionPosition;
    protected int displayLine;
    protected int maxLength = Integer.MAX_VALUE;
    protected int lineSpacing = 1;
    protected int preferredCursorX = -1;

    protected boolean editable = true;
    protected boolean bordered = true;
    protected boolean drawCursor = true;
    protected boolean wrapText = true;

    protected int textColor = DEFAULT_TEXT_COLOR;
    protected int placeholderColor = DEFAULT_PLACEHOLDER_COLOR;
    protected int selectionColor = DEFAULT_SELECTION_COLOR;
    protected int cursorColor = DEFAULT_CURSOR_COLOR;

    protected int paddingLeft = 4;
    protected int paddingRight = 4;
    protected int paddingTop = 4;
    protected int paddingBottom = 4;
    protected int scrollBarWidth = 6;
    protected int scrollBarPadding = 2;
    protected int minScrollThumbSize = 12;

    protected boolean showScrollBar = true;
    protected boolean draggingScrollThumb;
    protected double dragStartMouseY;
    protected int dragStartDisplayLine;

    protected int scrollTrackColor = 0x5530303A;
    protected int scrollThumbColor = 0xFF6D7485;
    protected int scrollThumbHoverColor = 0xFFAAB2C5;

    protected IGuiTexture backgroundTexture = new ColorRectAndBorderTexture(DEFAULT_BACKGROUND_COLOR, DEFAULT_BORDER_COLOR, 1).setRadius(2);
    protected IGuiTexture focusedBackgroundTexture = new ColorRectAndBorderTexture(DEFAULT_BACKGROUND_COLOR, DEFAULT_FOCUSED_BORDER_COLOR, 1).setRadius(2);
    protected IGuiTexture scrollTrackTexture;
    protected IGuiTexture scrollThumbTexture;
    protected IGuiTexture scrollThumbHoverTexture;

    public MultiLineInputTextBox() {
        this(0, 0, 120, 64, null, null);
    }

    public MultiLineInputTextBox(int x, int y, int width, int height) {
        this(x, y, width, height, null, null);
    }

    public MultiLineInputTextBox(Position selfPosition, Size size) {
        this(selfPosition, size, null, null);
    }

    public MultiLineInputTextBox(Position selfPosition, Size size, @Nullable Supplier<String> textSupplier, @Nullable Consumer<String> textResponder) {
        super(selfPosition, size);
        this.textSupplier = textSupplier;
        this.textResponder = textResponder;
        if (textSupplier != null) {
            setValueSilently(textSupplier.get());
        }
    }

    public MultiLineInputTextBox(int x, int y, int width, int height, @Nullable Supplier<String> textSupplier, @Nullable Consumer<String> textResponder) {
        super(x, y, width, height);
        this.textSupplier = textSupplier;
        this.textResponder = textResponder;
        if (textSupplier != null) {
            setValueSilently(textSupplier.get());
        }
    }

    public MultiLineInputTextBox setTextSupplier(@Nullable Supplier<String> textSupplier) {
        this.textSupplier = textSupplier;
        return this;
    }

    public MultiLineInputTextBox setTextResponder(@Nullable Consumer<String> textResponder) {
        this.textResponder = textResponder;
        return this;
    }

    public MultiLineInputTextBox setValidator(Function<String, String> textValidator) {
        this.textValidator = textValidator == null ? Function.identity() : textValidator;
        setValue(value);
        return this;
    }

    public MultiLineInputTextBox setCharFilter(Predicate<Character> charFilter) {
        this.charFilter = charFilter == null ? character -> character != null && isAllowedInputCharacter(character) : charFilter;
        return this;
    }

    public MultiLineInputTextBox setPlaceholder(Component placeholder) {
        this.placeholder = placeholder == null ? Component.empty() : placeholder;
        return this;
    }

    public MultiLineInputTextBox setCurrentString(Object value) {
        setValue(value == null ? "" : value.toString());
        return this;
    }

    public MultiLineInputTextBox setValue(String value) {
        String oldValue = this.value;
        setValueSilently(value);
        if (!Objects.equals(oldValue, this.value) && textResponder != null && isClientSideWidget) {
            textResponder.accept(this.value);
        }
        return this;
    }

    public String getCurrentString() {
        return value;
    }

    public String getRawCurrentString() {
        return value;
    }

    public MultiLineInputTextBox setMaxLength(int maxLength) {
        this.maxLength = Math.max(0, maxLength);
        setValue(value);
        return this;
    }

    public MultiLineInputTextBox setEditable(boolean editable) {
        this.editable = editable;
        return this;
    }

    public MultiLineInputTextBox setBordered(boolean bordered) {
        this.bordered = bordered;
        return this;
    }

    public MultiLineInputTextBox setWrapText(boolean wrapText) {
        this.wrapText = wrapText;
        ensureCursorVisible();
        return this;
    }

    public MultiLineInputTextBox setLineSpacing(int lineSpacing) {
        this.lineSpacing = Math.max(0, lineSpacing);
        ensureCursorVisible();
        return this;
    }

    public MultiLineInputTextBox setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public MultiLineInputTextBox setPlaceholderColor(int placeholderColor) {
        this.placeholderColor = placeholderColor;
        return this;
    }

    public MultiLineInputTextBox setSelectionColor(int selectionColor) {
        this.selectionColor = selectionColor;
        return this;
    }

    public MultiLineInputTextBox setCursorColor(int cursorColor) {
        this.cursorColor = cursorColor;
        return this;
    }

    public MultiLineInputTextBox setPadding(int padding) {
        return setPadding(padding, padding, padding, padding);
    }

    public MultiLineInputTextBox setPadding(int horizontal, int vertical) {
        return setPadding(horizontal, vertical, horizontal, vertical);
    }

    public MultiLineInputTextBox setPadding(int left, int top, int right, int bottom) {
        this.paddingLeft = Math.max(0, left);
        this.paddingTop = Math.max(0, top);
        this.paddingRight = Math.max(0, right);
        this.paddingBottom = Math.max(0, bottom);
        ensureCursorVisible();
        return this;
    }

    public MultiLineInputTextBox setShowScrollBar(boolean showScrollBar) {
        this.showScrollBar = showScrollBar;
        ensureCursorVisible();
        return this;
    }

    public MultiLineInputTextBox setScrollBarWidth(int scrollBarWidth) {
        this.scrollBarWidth = Math.max(0, scrollBarWidth);
        ensureCursorVisible();
        return this;
    }

    public MultiLineInputTextBox setScrollBarPadding(int scrollBarPadding) {
        this.scrollBarPadding = Math.max(0, scrollBarPadding);
        ensureCursorVisible();
        return this;
    }

    public MultiLineInputTextBox setMinScrollThumbSize(int minScrollThumbSize) {
        this.minScrollThumbSize = Math.max(1, minScrollThumbSize);
        return this;
    }

    public MultiLineInputTextBox setScrollBarColors(int trackColor, int thumbColor, int thumbHoverColor) {
        this.scrollTrackColor = trackColor;
        this.scrollThumbColor = thumbColor;
        this.scrollThumbHoverColor = thumbHoverColor;
        return this;
    }

    public MultiLineInputTextBox setScrollBarTextures(IGuiTexture trackTexture, IGuiTexture thumbTexture, IGuiTexture thumbHoverTexture) {
        this.scrollTrackTexture = trackTexture;
        this.scrollThumbTexture = thumbTexture;
        this.scrollThumbHoverTexture = thumbHoverTexture;
        return this;
    }

    @Override
    public MultiLineInputTextBox setBackground(IGuiTexture... backgroundTexture) {
        super.setBackground(backgroundTexture);
        if (backgroundTexture != null && backgroundTexture.length > 0) {
            this.backgroundTexture = getBackgroundTexture();
        }
        return this;
    }

    public MultiLineInputTextBox setFocusedBackground(IGuiTexture focusedBackgroundTexture) {
        this.focusedBackgroundTexture = focusedBackgroundTexture;
        return this;
    }

    public MultiLineInputTextBox setFocusedOutline(int borderColor) {
        return setFocusedOutline(DEFAULT_BACKGROUND_COLOR, borderColor);
    }

    public MultiLineInputTextBox setFocusedOutline(int backgroundColor, int borderColor) {
        this.focusedBackgroundTexture = new ColorRectAndBorderTexture(backgroundColor, borderColor, 1).setRadius(2);
        return this;
    }

    public MultiLineInputTextBox setFocusedFill(int fillColor) {
        this.focusedBackgroundTexture = new ColorRectAndBorderTexture(fillColor, DEFAULT_BORDER_COLOR, 1).setRadius(2);
        return this;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (isVisible() && isActive() && textSupplier != null && isClientSideWidget) {
            String supplied = normalizeNewlines(nullToEmpty(textSupplier.get()));
            if (!supplied.equals(value)) {
                setValueSilently(supplied);
            }
        }
    }

    @Override
    public void onFocusChanged(@Nullable Widget lastFocus, Widget focus) {
        if (!isFocus()) {
            selectionPosition = cursorPosition;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean hovered = isMouseOverElement(mouseX, mouseY);
        setFocus(hovered);
        if (!hovered || button != 0) {
            return hovered;
        }

        if (isMouseOverScrollBar(mouseX, mouseY)) {
            if (isMouseOverScrollThumb(mouseX, mouseY)) {
                draggingScrollThumb = true;
                dragStartMouseY = mouseY;
                dragStartDisplayLine = displayLine;
            } else {
                scrollToMouse(mouseY);
            }
            return true;
        }

        moveCursorTo(getCursorAtMouse(mouseX, mouseY), Screen.hasShiftDown());
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (draggingScrollThumb && button == 0) {
            Rect track = getScrollBarTrackRect();
            Rect thumb = getScrollBarThumbRect();
            int movable = Math.max(1, track.height - thumb.height);
            int next = dragStartDisplayLine + (int) Math.round((mouseY - dragStartMouseY) * getMaxDisplayLine() / (double) movable);
            setDisplayLine(next);
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (draggingScrollThumb && button == 0) {
            draggingScrollThumb = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseWheelMove(double mouseX, double mouseY, double wheelDelta) {
        if (!isMouseOverElement(mouseX, mouseY)) {
            return false;
        }

        if (!hasVisibleScrollBar()) {
            return false;
        }

        setDisplayLine(displayLine + (wheelDelta < 0 ? 1 : -1));
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!isFocus() || !isActive() || !isVisible()) {
            return false;
        }

        if (Screen.isSelectAll(keyCode)) {
            cursorPosition = value.length();
            selectionPosition = 0;
            preferredCursorX = -1;
            ensureCursorVisible();
            return true;
        }

        if (Screen.isCopy(keyCode)) {
            Minecraft.getInstance().keyboardHandler.setClipboard(getSelectedText());
            return true;
        }

        if (Screen.isCut(keyCode)) {
            Minecraft.getInstance().keyboardHandler.setClipboard(getSelectedText());
            if (editable) {
                replaceSelection("");
            }
            return true;
        }

        if (Screen.isPaste(keyCode)) {
            if (editable) {
                insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
            }
            return true;
        }

        switch (keyCode) {
            case GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> {
                if (editable) {
                    insertText("\n");
                }
                return true;
            }
            case GLFW.GLFW_KEY_BACKSPACE -> {
                if (editable) {
                    deleteFromCursor(-1);
                }
                return true;
            }
            case GLFW.GLFW_KEY_DELETE -> {
                if (editable) {
                    deleteFromCursor(1);
                }
                return true;
            }
            case GLFW.GLFW_KEY_LEFT -> {
                preferredCursorX = -1;
                moveCursorTo(Screen.hasControlDown() ? getWordPosition(-1) : cursorPosition - 1, Screen.hasShiftDown());
                return true;
            }
            case GLFW.GLFW_KEY_RIGHT -> {
                preferredCursorX = -1;
                moveCursorTo(Screen.hasControlDown() ? getWordPosition(1) : cursorPosition + 1, Screen.hasShiftDown());
                return true;
            }
            case GLFW.GLFW_KEY_UP -> {
                moveCursorVertically(-1, Screen.hasShiftDown());
                return true;
            }
            case GLFW.GLFW_KEY_DOWN -> {
                moveCursorVertically(1, Screen.hasShiftDown());
                return true;
            }
            case GLFW.GLFW_KEY_HOME -> {
                preferredCursorX = -1;
                LineView line = getCursorLine();
                moveCursorTo(Screen.hasControlDown() ? 0 : line.start, Screen.hasShiftDown());
                return true;
            }
            case GLFW.GLFW_KEY_END -> {
                preferredCursorX = -1;
                LineView line = getCursorLine();
                moveCursorTo(Screen.hasControlDown() ? value.length() : line.end, Screen.hasShiftDown());
                return true;
            }
            case GLFW.GLFW_KEY_ESCAPE -> {
                setFocus(false);
                return true;
            }
            default -> {
                return true;
            }
        }
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (!isFocus() || !editable || !isActive() || !isVisible() || codePoint == '\n' || codePoint == '\r' || !charFilter.test(codePoint)) {
            return false;
        }

        insertText(Character.toString(codePoint));
        return true;
    }

    @Override
    public void writeInitialData(FriendlyByteBuf buffer) {
        super.writeInitialData(buffer);
        buffer.writeUtf(value);
    }

    @Override
    public void readInitialData(FriendlyByteBuf buffer) {
        super.readInitialData(buffer);
        setValueSilently(buffer.readUtf());
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (textSupplier != null) {
            String supplied = normalizeNewlines(nullToEmpty(textSupplier.get()));
            if (!supplied.equals(value)) {
                setValueSilently(supplied);
                writeUpdateInfo(1, buffer -> buffer.writeUtf(value));
            }
        }
    }

    @Override
    public void readUpdateInfo(int id, FriendlyByteBuf buffer) {
        super.readUpdateInfo(id, buffer);
        if (id == 1) {
            setValueSilently(buffer.readUtf());
        }
    }

    @Override
    public void handleClientAction(int id, FriendlyByteBuf buffer) {
        super.handleClientAction(id, buffer);
        if (id == 1) {
            setValueSilently(buffer.readUtf());
            if (textResponder != null) {
                textResponder.accept(value);
            }
        }
    }

    @Override
    public void drawInBackground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        drawOwnBackground(graphics, mouseX, mouseY);
        drawTextBox(graphics, mouseX, mouseY);
    }

    private void drawOwnBackground(GuiGraphics graphics, int mouseX, int mouseY) {
        if (!bordered) return;

        IGuiTexture texture = isFocus() && focusedBackgroundTexture != null ? focusedBackgroundTexture : backgroundTexture;
        if (texture != null) {
            texture.draw(graphics, mouseX, mouseY, getPositionX(), getPositionY(), getSizeWidth(), getSizeHeight());
        }
    }

    private void drawTextBox(GuiGraphics graphics, int mouseX, int mouseY) {
        Font font = Minecraft.getInstance().font;
        int textX = getTextAreaX();
        int textY = getTextAreaY();
        int textRight = getTextAreaRight();
        int textBottom = getTextAreaBottom();
        int lineHeight = getLineHeight(font);

        graphics.enableScissor(textX, textY, textRight, textBottom);

        if (value.isEmpty() && !isFocus() && placeholder != null) {
            graphics.drawString(font, placeholder, textX, textY, placeholderColor, false);
            graphics.disableScissor();
            return;
        }

        List<LineView> lines = computeLines(font);
        int visibleLineCount = getVisibleLineCount(font);
        int lastLine = Math.min(lines.size(), displayLine + visibleLineCount + 1);

        for (int i = displayLine; i < lastLine; i++) {
            LineView line = lines.get(i);
            int y = textY + (i - displayLine) * lineHeight;
            drawSelection(graphics, font, line, textX, y);
            graphics.drawString(font, line.text, textX, y, textColor, false);
        }

        if (drawCursor && isFocus() && System.currentTimeMillis() / 500L % 2L == 0L) {
            CursorView cursor = getCursorView(font, lines);
            int cursorLine = cursor.lineIndex;
            if (cursorLine >= displayLine && cursorLine < lastLine) {
                int cursorX = textX + cursor.x;
                int cursorY = textY + (cursorLine - displayLine) * lineHeight;
                graphics.fill(cursorX, cursorY - 1, cursorX + 1, cursorY + font.lineHeight + 1, cursorColor);
            }
        }

        graphics.disableScissor();
        drawScrollBar(graphics, mouseX, mouseY);
    }

    private void drawSelection(GuiGraphics graphics, Font font, LineView line, int textX, int textY) {
        if (!hasSelection()) return;

        int start = Math.max(line.start, getSelectionStart());
        int end = Math.min(line.end, getSelectionEnd());
        if (start >= end) return;

        int selectionX1 = textX + font.width(value.substring(line.start, start));
        int selectionX2 = textX + font.width(value.substring(line.start, end));
        graphics.fill(selectionX1, textY - 1, selectionX2, textY + font.lineHeight + 1, selectionColor);
    }

    private void insertText(String text) {
        if (text == null || text.isEmpty()) return;

        String normalized = normalizeNewlines(text);
        StringBuilder sanitized = new StringBuilder();
        for (int i = 0; i < normalized.length(); i++) {
            char c = normalized.charAt(i);
            if (c == '\n' || charFilter.test(c)) {
                sanitized.append(c);
            }
        }

        replaceSelection(sanitized.toString());
    }

    private void replaceSelection(String replacement) {
        int start = getSelectionStart();
        int end = getSelectionEnd();
        String prefix = value.substring(0, start);
        String suffix = value.substring(end);
        String clippedReplacement = clipToMaxLength(replacement, prefix.length() + suffix.length());
        setValue(prefix + clippedReplacement + suffix);
        cursorPosition = prefix.length() + clippedReplacement.length();
        selectionPosition = cursorPosition;
        preferredCursorX = -1;
        ensureCursorVisible();
    }

    private void deleteFromCursor(int direction) {
        if (hasSelection()) {
            replaceSelection("");
            return;
        }

        if (direction < 0 && cursorPosition > 0) {
            int target = Screen.hasControlDown() ? getWordPosition(-1) : cursorPosition - 1;
            deleteRange(target, cursorPosition);
        } else if (direction > 0 && cursorPosition < value.length()) {
            int target = Screen.hasControlDown() ? getWordPosition(1) : cursorPosition + 1;
            deleteRange(cursorPosition, target);
        }
    }

    private void deleteRange(int start, int end) {
        start = clamp(start, 0, value.length());
        end = clamp(end, 0, value.length());
        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }
        setValue(value.substring(0, start) + value.substring(end));
        cursorPosition = start;
        selectionPosition = cursorPosition;
        preferredCursorX = -1;
        ensureCursorVisible();
    }

    private void moveCursorTo(int position, boolean keepSelection) {
        cursorPosition = clamp(position, 0, value.length());
        if (!keepSelection) {
            selectionPosition = cursorPosition;
        }
        ensureCursorVisible();
    }

    private void moveCursorVertically(int direction, boolean keepSelection) {
        Font font = Minecraft.getInstance().font;
        List<LineView> lines = computeLines(font);
        CursorView cursor = getCursorView(font, lines);
        int targetLineIndex = clamp(cursor.lineIndex + direction, 0, Math.max(0, lines.size() - 1));
        LineView targetLine = lines.get(targetLineIndex);

        if (preferredCursorX < 0) {
            preferredCursorX = cursor.x;
        }

        int targetPosition = targetLine.start + font.plainSubstrByWidth(targetLine.text, preferredCursorX).length();
        moveCursorTo(targetPosition, keepSelection);
    }

    private int getCursorAtMouse(double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        List<LineView> lines = computeLines(font);
        int clickedLine = displayLine + Math.max(0, ((int) mouseY - getTextAreaY()) / getLineHeight(font));
        clickedLine = clamp(clickedLine, 0, Math.max(0, lines.size() - 1));

        LineView line = lines.get(clickedLine);
        int relativeX = Math.max(0, (int) mouseX - getTextAreaX());
        return line.start + font.plainSubstrByWidth(line.text, relativeX).length();
    }

    private int getWordPosition(int direction) {
        int position = cursorPosition;
        if (direction < 0) {
            while (position > 0 && isWordSeparator(value.charAt(position - 1))) {
                position--;
            }
            while (position > 0 && !isWordSeparator(value.charAt(position - 1))) {
                position--;
            }
        } else {
            int length = value.length();
            while (position < length && !isWordSeparator(value.charAt(position))) {
                position++;
            }
            while (position < length && isWordSeparator(value.charAt(position))) {
                position++;
            }
        }
        return position;
    }

    private void ensureCursorVisible() {
        Font font = Minecraft.getInstance().font;
        List<LineView> lines = computeLines(font);
        int cursorLine = getCursorView(font, lines).lineIndex;
        int visibleLineCount = getVisibleLineCount(font);
        int maxDisplayLine = Math.max(0, lines.size() - visibleLineCount);

        if (cursorLine < displayLine) {
            displayLine = cursorLine;
        } else if (cursorLine >= displayLine + visibleLineCount) {
            displayLine = cursorLine - visibleLineCount + 1;
        }

        displayLine = clamp(displayLine, 0, maxDisplayLine);
    }

    private LineView getCursorLine() {
        Font font = Minecraft.getInstance().font;
        List<LineView> lines = computeLines(font);
        return lines.get(getCursorView(font, lines).lineIndex);
    }

    private CursorView getCursorView(Font font, List<LineView> lines) {
        for (int i = 0; i < lines.size(); i++) {
            LineView line = lines.get(i);
            if (cursorPosition >= line.start && cursorPosition <= line.end) {
                int x = font.width(value.substring(line.start, cursorPosition));
                return new CursorView(i, x);
            }
        }

        LineView lastLine = lines.get(lines.size() - 1);
        return new CursorView(lines.size() - 1, font.width(lastLine.text));
    }

    private List<LineView> computeLines() {
        return computeLines(Minecraft.getInstance().font);
    }

    private List<LineView> computeLines(Font font) {
        return computeLines(font, getTextAreaWidth());
    }

    private List<LineView> computeLinesForScrollBar(Font font) {
        return computeLines(font, getBaseTextAreaWidth());
    }

    private List<LineView> computeLines(Font font, int maxWidth) {
        ArrayList<LineView> lines = new ArrayList<>();
        int lineStart = 0;

        for (int i = 0; i <= value.length(); i++) {
            if (i == value.length() || value.charAt(i) == '\n') {
                addWrappedLine(font, lines, lineStart, i, maxWidth);
                lineStart = i + 1;
            }
        }

        if (lines.isEmpty()) {
            lines.add(new LineView(0, 0, ""));
        }
        return lines;
    }

    private void addWrappedLine(Font font, List<LineView> lines, int start, int end, int maxWidth) {
        if (start == end) {
            lines.add(new LineView(start, end, ""));
            return;
        }

        if (!wrapText || maxWidth <= 0) {
            lines.add(new LineView(start, end, value.substring(start, end)));
            return;
        }

        int chunkStart = start;
        while (chunkStart < end) {
            String rest = value.substring(chunkStart, end);
            String chunk = font.plainSubstrByWidth(rest, maxWidth);
            int chunkLength = Math.max(1, chunk.length());
            int chunkEnd = Math.min(end, chunkStart + chunkLength);
            lines.add(new LineView(chunkStart, chunkEnd, value.substring(chunkStart, chunkEnd)));
            chunkStart = chunkEnd;
        }
    }

    private void setValueSilently(String nextValue) {
        String sanitized = textValidator.apply(clipToMaxLength(normalizeNewlines(nullToEmpty(nextValue)), 0));
        value = sanitized == null ? "" : clipToMaxLength(normalizeNewlines(sanitized), 0);
        cursorPosition = clamp(cursorPosition, 0, value.length());
        selectionPosition = clamp(selectionPosition, 0, value.length());
        ensureCursorVisible();
    }

    private String clipToMaxLength(String text, int existingLength) {
        String safeText = nullToEmpty(text);
        int available = Math.max(0, maxLength - existingLength);
        return safeText.length() <= available ? safeText : safeText.substring(0, available);
    }

    private boolean hasSelection() {
        return cursorPosition != selectionPosition;
    }

    private String getSelectedText() {
        return hasSelection() ? value.substring(getSelectionStart(), getSelectionEnd()) : "";
    }

    private int getSelectionStart() {
        return Math.min(cursorPosition, selectionPosition);
    }

    private int getSelectionEnd() {
        return Math.max(cursorPosition, selectionPosition);
    }

    private int getTextAreaX() {
        return getPositionX() + paddingLeft;
    }

    private int getTextAreaY() {
        return getPositionY() + paddingTop;
    }

    private int getTextAreaRight() {
        int right = getPositionX() + Math.max(paddingLeft, getSizeWidth() - paddingRight);
        if (hasVisibleScrollBar()) {
            right -= scrollBarWidth + scrollBarPadding * 2;
        }
        return Math.max(getTextAreaX(), right);
    }

    private int getTextAreaBottom() {
        return getPositionY() + Math.max(paddingTop, getSizeHeight() - paddingBottom);
    }

    private int getTextAreaWidth() {
        return Math.max(0, getTextAreaRight() - getTextAreaX());
    }

    private int getBaseTextAreaWidth() {
        int right = getPositionX() + Math.max(paddingLeft, getSizeWidth() - paddingRight);
        return Math.max(0, right - getTextAreaX());
    }

    private int getTextAreaHeight() {
        return Math.max(0, getTextAreaBottom() - getTextAreaY());
    }

    private int getVisibleLineCount() {
        return getVisibleLineCount(Minecraft.getInstance().font);
    }

    private int getVisibleLineCount(Font font) {
        return Math.max(1, getTextAreaHeight() / getLineHeight(font));
    }

    private int getMaxDisplayLine() {
        Font font = Minecraft.getInstance().font;
        List<LineView> lines = computeLines(font);
        return Math.max(0, lines.size() - getVisibleLineCount(font));
    }

    private int getMaxDisplayLineForScrollBar() {
        Font font = Minecraft.getInstance().font;
        List<LineView> lines = computeLinesForScrollBar(font);
        return Math.max(0, lines.size() - getVisibleLineCount(font));
    }

    private void setDisplayLine(int displayLine) {
        this.displayLine = clamp(displayLine, 0, getMaxDisplayLine());
    }

    private int getLineHeight(Font font) {
        return font.lineHeight + lineSpacing;
    }

    private boolean hasVisibleScrollBar() {
        return showScrollBar && scrollBarWidth > 0 && getMaxDisplayLineForScrollBar() > 0;
    }

    private void drawScrollBar(GuiGraphics graphics, int mouseX, int mouseY) {
        if (!hasVisibleScrollBar()) return;

        Rect track = getScrollBarTrackRect();
        Rect thumb = getScrollBarThumbRect();

        if (scrollTrackTexture != null) {
            scrollTrackTexture.draw(graphics, mouseX, mouseY, track.x, track.y, track.width, track.height);
        } else {
            graphics.fill(track.x, track.y, track.x + track.width, track.y + track.height, scrollTrackColor);
        }

        boolean thumbHovered = draggingScrollThumb || isMouseOverScrollThumb(mouseX, mouseY);
        IGuiTexture thumbTexture = thumbHovered ? scrollThumbHoverTexture : scrollThumbTexture;
        int thumbColor = thumbHovered ? scrollThumbHoverColor : scrollThumbColor;

        if (thumbTexture != null) {
            thumbTexture.draw(graphics, mouseX, mouseY, thumb.x, thumb.y, thumb.width, thumb.height);
        } else {
            graphics.fill(thumb.x, thumb.y, thumb.x + thumb.width, thumb.y + thumb.height, thumbColor);
        }
    }

    private Rect getScrollBarTrackRect() {
        if (!hasVisibleScrollBar()) {
            return new Rect(0, 0, 0, 0);
        }

        int x = getPositionX() + getSizeWidth() - paddingRight - scrollBarPadding - scrollBarWidth;
        int y = getPositionY() + paddingTop + scrollBarPadding;
        int height = Math.max(1, getSizeHeight() - paddingTop - paddingBottom - scrollBarPadding * 2);
        return new Rect(x, y, scrollBarWidth, height);
    }

    private Rect getScrollBarThumbRect() {
        Rect track = getScrollBarTrackRect();
        if (track.height <= 0) {
            return track;
        }

        Font font = Minecraft.getInstance().font;
        int totalLines = Math.max(1, computeLines(font).size());
        int visibleLines = Math.min(totalLines, getVisibleLineCount(font));
        int thumbHeight = clamp(
                (int) Math.round(track.height * (visibleLines / (double) totalLines)),
                Math.min(track.height, minScrollThumbSize),
                track.height
        );

        int maxDisplayLine = getMaxDisplayLine();
        int movable = Math.max(0, track.height - thumbHeight);
        int y = track.y + (maxDisplayLine <= 0 ? 0 : (int) Math.round(movable * (displayLine / (double) maxDisplayLine)));
        return new Rect(track.x, y, track.width, thumbHeight);
    }

    private boolean isMouseOverScrollBar(double mouseX, double mouseY) {
        if (!hasVisibleScrollBar()) return false;
        Rect track = getScrollBarTrackRect();
        return isMouseOver(track.x, track.y, track.width, track.height, mouseX, mouseY);
    }

    private boolean isMouseOverScrollThumb(double mouseX, double mouseY) {
        if (!hasVisibleScrollBar()) return false;
        Rect thumb = getScrollBarThumbRect();
        return isMouseOver(thumb.x, thumb.y, thumb.width, thumb.height, mouseX, mouseY);
    }

    private void scrollToMouse(double mouseY) {
        Rect track = getScrollBarTrackRect();
        Rect thumb = getScrollBarThumbRect();
        int movable = Math.max(1, track.height - thumb.height);
        int relative = (int) Math.round(mouseY - track.y - thumb.height / 2.0);
        int next = (int) Math.round(clamp(relative, 0, movable) * getMaxDisplayLine() / (double) movable);
        setDisplayLine(next);
    }

    private static String normalizeNewlines(String text) {
        return text.replace("\r\n", "\n").replace('\r', '\n');
    }

    private static String nullToEmpty(String text) {
        return text == null ? "" : text;
    }

    private static boolean isAllowedInputCharacter(char character) {
        return character != 167 && character >= ' ' && character != 127;
    }

    private static boolean isWordSeparator(char character) {
        return Character.isWhitespace(character);
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private record LineView(int start, int end, String text) {
    }

    private record CursorView(int lineIndex, int x) {
    }

    private record Rect(int x, int y, int width, int height) {
    }
}
