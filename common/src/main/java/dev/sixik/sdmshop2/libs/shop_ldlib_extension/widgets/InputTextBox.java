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

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class InputTextBox extends Widget {

    public enum HighlightMode {
        OUTLINE,
        FILL
    }

    public static final int DEFAULT_BACKGROUND_COLOR = 0xFF101016;
    public static final int DEFAULT_BORDER_COLOR = 0xFF5C637A;
    public static final int DEFAULT_FOCUSED_BORDER_COLOR = 0xFFFFFFFF;
    public static final int DEFAULT_TEXT_COLOR = 0xFFE8E8F0;
    public static final int DEFAULT_PLACEHOLDER_COLOR = 0xFF777D8D;
    public static final int DEFAULT_SELECTION_COLOR = 0xAA4C8DFF;
    public static final int DEFAULT_CURSOR_COLOR = 0xFFFFFFFF;

    protected String value = "";
    protected Component placeholder = Component.empty();

    protected Supplier<String> textSupplier;
    protected Consumer<String> textResponder;
    protected Function<String, String> textValidator = Function.identity();
    protected Predicate<Character> charFilter = character -> character != null && isAllowedInputCharacter(character);

    protected int cursorPosition;
    protected int selectionPosition;
    protected int displayOffset;
    protected int maxLength = Integer.MAX_VALUE;

    protected boolean editable = true;
    protected boolean bordered = true;
    protected boolean drawCursor = true;

    protected int textColor = DEFAULT_TEXT_COLOR;
    protected int placeholderColor = DEFAULT_PLACEHOLDER_COLOR;
    protected int selectionColor = DEFAULT_SELECTION_COLOR;
    protected int cursorColor = DEFAULT_CURSOR_COLOR;
    protected int focusedHighlightColor = DEFAULT_FOCUSED_BORDER_COLOR;
    protected HighlightMode focusedHighlightMode = HighlightMode.OUTLINE;

    protected int paddingLeft = 4;
    protected int paddingRight = 4;
    protected int paddingTop = 0;
    protected int paddingBottom = 0;

    protected IGuiTexture backgroundTexture = new ColorRectAndBorderTexture(DEFAULT_BACKGROUND_COLOR, DEFAULT_BORDER_COLOR, 1).setRadius(2);
    protected IGuiTexture focusedBackgroundTexture;

    public InputTextBox() {
        this(0, 0, 80, 18, null, null);
    }

    public InputTextBox(int x, int y, int width, int height) {
        this(x, y, width, height, null, null);
    }

    public InputTextBox(Position selfPosition, Size size) {
        this(selfPosition, size, null, null);
    }

    public InputTextBox(Position selfPosition, Size size, @Nullable Supplier<String> textSupplier, @Nullable Consumer<String> textResponder) {
        super(selfPosition, size);
        this.textSupplier = textSupplier;
        this.textResponder = textResponder;
        if (textSupplier != null) {
            setValueSilently(textSupplier.get());
        }
    }

    public InputTextBox(int x, int y, int width, int height, @Nullable Supplier<String> textSupplier, @Nullable Consumer<String> textResponder) {
        super(x, y, width, height);
        this.textSupplier = textSupplier;
        this.textResponder = textResponder;
        if (textSupplier != null) {
            setValueSilently(textSupplier.get());
        }
    }

    public InputTextBox setTextSupplier(@Nullable Supplier<String> textSupplier) {
        this.textSupplier = textSupplier;
        return this;
    }

    public InputTextBox setTextResponder(@Nullable Consumer<String> textResponder) {
        this.textResponder = textResponder;
        return this;
    }

    public InputTextBox setValidator(Function<String, String> textValidator) {
        this.textValidator = textValidator == null ? Function.identity() : textValidator;
        setValue(value);
        return this;
    }

    public InputTextBox setCharFilter(Predicate<Character> charFilter) {
        this.charFilter = charFilter == null ? character -> character != null && isAllowedInputCharacter(character) : charFilter;
        return this;
    }

    public InputTextBox setPlaceholder(Component placeholder) {
        this.placeholder = placeholder == null ? Component.empty() : placeholder;
        return this;
    }

    public InputTextBox setCurrentString(Object value) {
        setValue(value == null ? "" : value.toString());
        return this;
    }

    public InputTextBox setCurrentStringSilently(Object value) {
        setValueSilently(value == null ? "" : value.toString());
        return this;
    }

    public InputTextBox setValue(String value) {
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

    public InputTextBox setMaxLength(int maxLength) {
        this.maxLength = Math.max(0, maxLength);
        setValue(value);
        return this;
    }

    public InputTextBox setEditable(boolean editable) {
        this.editable = editable;
        return this;
    }

    public InputTextBox setBordered(boolean bordered) {
        this.bordered = bordered;
        return this;
    }

    public InputTextBox setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public InputTextBox setPlaceholderColor(int placeholderColor) {
        this.placeholderColor = placeholderColor;
        return this;
    }

    public InputTextBox setSelectionColor(int selectionColor) {
        this.selectionColor = selectionColor;
        return this;
    }

    public InputTextBox setCursorColor(int cursorColor) {
        this.cursorColor = cursorColor;
        return this;
    }

    public InputTextBox setPadding(int padding) {
        return setPadding(padding, padding, padding, padding);
    }

    public InputTextBox setPadding(int horizontal, int vertical) {
        return setPadding(horizontal, vertical, horizontal, vertical);
    }

    public InputTextBox setPadding(int left, int top, int right, int bottom) {
        this.paddingLeft = Math.max(0, left);
        this.paddingTop = Math.max(0, top);
        this.paddingRight = Math.max(0, right);
        this.paddingBottom = Math.max(0, bottom);
        ensureCursorVisible();
        return this;
    }

    @Override
    public InputTextBox setBackground(IGuiTexture... backgroundTexture) {
        super.setBackground(backgroundTexture);
        if (backgroundTexture != null && backgroundTexture.length > 0) {
            this.backgroundTexture = getBackgroundTexture();
        }
        return this;
    }

    public InputTextBox setFocusedBackground(IGuiTexture focusedBackgroundTexture) {
        this.focusedBackgroundTexture = focusedBackgroundTexture;
        this.focusedHighlightMode = HighlightMode.FILL;
        return this;
    }

    public InputTextBox setFocusedOutline(int borderColor) {
        this.focusedBackgroundTexture = null;
        this.focusedHighlightMode = HighlightMode.OUTLINE;
        this.focusedHighlightColor = borderColor;
        return this;
    }

    public InputTextBox setFocusedOutline(int backgroundColor, int borderColor) {
        this.focusedBackgroundTexture = new ColorRectAndBorderTexture(backgroundColor, DEFAULT_BORDER_COLOR, 1).setRadius(2);
        this.focusedHighlightMode = HighlightMode.OUTLINE;
        this.focusedHighlightColor = borderColor;
        return this;
    }

    public InputTextBox setFocusedFill(int fillColor) {
        this.focusedBackgroundTexture = new ColorRectAndBorderTexture(fillColor, DEFAULT_BORDER_COLOR, 1).setRadius(2);
        this.focusedHighlightMode = HighlightMode.FILL;
        this.focusedHighlightColor = fillColor;
        return this;
    }

    public InputTextBox setNumbersOnly(int minValue, int maxValue) {
        setCharFilter(character -> character != null && (Character.isDigit(character) || character == '-'));
        setValidator(text -> validateIntegralText(text, minValue, maxValue));
        return this;
    }

    public InputTextBox setNumbersOnly(long minValue, long maxValue) {
        setCharFilter(character -> character != null && (Character.isDigit(character) || character == '-'));
        setValidator(text -> validateIntegralText(text, minValue, maxValue));
        return this;
    }

    public InputTextBox setNumbersOnly(float minValue, float maxValue) {
        setCharFilter(character -> character != null && (Character.isDigit(character) || character == '-' || character == '.'));
        setValidator(text -> validateDecimalText(text, minValue, maxValue));
        return this;
    }

    public InputTextBox setNumbersOnly(double minValue, double maxValue) {
        setCharFilter(character -> character != null && (Character.isDigit(character) || character == '-' || character == '.'));
        setValidator(text -> validateDecimalText(text, minValue, maxValue));
        return this;
    }

    public InputTextBox setResourceLocationOnly() {
        setCharFilter(character -> character != null
                && (Character.isLetterOrDigit(character)
                || character == '_'
                || character == '-'
                || character == ':'
                || character == '/'
                || character == '.'
                || character == ' '));
        setValidator(text -> {
            String next = nullToEmpty(text).toLowerCase().replace(' ', '_');
            return next.matches("^[a-z0-9_\\-:/\\.]*$") ? next : value;
        });
        return this;
    }

    public InputTextBox setUuidOnly() {
        setMaxLength(36);
        setCharFilter(character -> character != null && (Character.digit(character, 16) >= 0 || character == '-'));
        setValidator(text -> {
            String next = nullToEmpty(text);
            return next.matches("^[0-9a-fA-F\\-]*$") ? next : value;
        });
        return this;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (isVisible() && isActive() && textSupplier != null && isClientSideWidget) {
            String supplied = nullToEmpty(textSupplier.get());
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

        int clickedCursor = getCursorAtMouse(mouseX);
        moveCursorTo(clickedCursor, Screen.hasShiftDown());
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
                moveCursorTo(Screen.hasControlDown() ? getWordPosition(-1) : cursorPosition - 1, Screen.hasShiftDown());
                return true;
            }
            case GLFW.GLFW_KEY_RIGHT -> {
                moveCursorTo(Screen.hasControlDown() ? getWordPosition(1) : cursorPosition + 1, Screen.hasShiftDown());
                return true;
            }
            case GLFW.GLFW_KEY_HOME -> {
                moveCursorTo(0, Screen.hasShiftDown());
                return true;
            }
            case GLFW.GLFW_KEY_END -> {
                moveCursorTo(value.length(), Screen.hasShiftDown());
                return true;
            }
            case GLFW.GLFW_KEY_ESCAPE, GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> {
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
        if (!isFocus() || !editable || !isActive() || !isVisible() || !charFilter.test(codePoint)) {
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
            String supplied = nullToEmpty(textSupplier.get());
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
            String nextValue = buffer.readUtf();
            setValueSilently(nextValue);
            if (textResponder != null) {
                textResponder.accept(value);
            }
        }
    }

    @Override
    public void drawInBackground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        drawOwnBackground(graphics, mouseX, mouseY);
        drawTextBox(graphics);
    }

    private void drawOwnBackground(GuiGraphics graphics, int mouseX, int mouseY) {
        if (!bordered) return;

        if (backgroundTexture != null) {
            backgroundTexture.draw(graphics, mouseX, mouseY, getPositionX(), getPositionY(), getSizeWidth(), getSizeHeight());
        }

        if (!isFocus()) return;

        if (focusedBackgroundTexture != null) {
            focusedBackgroundTexture.draw(graphics, mouseX, mouseY, getPositionX(), getPositionY(), getSizeWidth(), getSizeHeight());
        }

        if (focusedHighlightMode == HighlightMode.OUTLINE && focusedHighlightColor != 0) {
            drawOutline(graphics, getPositionX(), getPositionY(), getSizeWidth(), getSizeHeight(), focusedHighlightColor, 1);
        }
    }

    private void drawTextBox(GuiGraphics graphics) {
        Font font = Minecraft.getInstance().font;
        int textX = getTextAreaX();
        int textY = getTextAreaY(font);
        int textRight = getTextAreaRight();
        int textBottom = getPositionY() + getSizeHeight() - paddingBottom;

        graphics.enableScissor(textX, getPositionY() + paddingTop, textRight, textBottom);

        if (value.isEmpty() && !isFocus() && placeholder != null) {
            graphics.drawString(font, placeholder, textX, textY, placeholderColor, false);
            graphics.disableScissor();
            return;
        }

        int safeDisplayOffset = getSafeDisplayOffset();
        String visibleText = value.substring(safeDisplayOffset);
        int drawX = textX;

        drawSelection(graphics, font, textX, textY, safeDisplayOffset);
        graphics.drawString(font, visibleText, drawX, textY, textColor, false);

        if (drawCursor && isFocus() && System.currentTimeMillis() / 500L % 2L == 0L) {
            int cursorX = textX + font.width(value.substring(safeDisplayOffset, cursorPosition));
            graphics.fill(cursorX, textY - 1, cursorX + 1, textY + font.lineHeight + 1, cursorColor);
        }

        graphics.disableScissor();
    }

    private void drawSelection(GuiGraphics graphics, Font font, int textX, int textY, int safeDisplayOffset) {
        if (!hasSelection()) return;

        int start = Math.max(safeDisplayOffset, getSelectionStart());
        int end = Math.min(value.length(), Math.max(start, getSelectionEnd()));
        if (start >= value.length() && start == end) return;

        int selectionX1 = textX + font.width(value.substring(safeDisplayOffset, start));
        int selectionX2 = textX + font.width(value.substring(safeDisplayOffset, end));
        graphics.fill(selectionX1, textY - 1, selectionX2, textY + font.lineHeight + 1, selectionColor);
    }

    private void drawOutline(GuiGraphics graphics, int x, int y, int width, int height, int color, int thickness) {
        if (color == 0 || width <= 0 || height <= 0) return;

        int line = Math.max(1, Math.min(thickness, Math.min(width, height)));
        graphics.fill(x, y, x + width, y + line, color);
        graphics.fill(x, y + height - line, x + width, y + height, color);
        graphics.fill(x, y + line, x + line, y + height - line, color);
        graphics.fill(x + width - line, y + line, x + width, y + height - line, color);
    }

    private void insertText(String text) {
        if (text == null || text.isEmpty()) return;

        StringBuilder sanitized = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (charFilter.test(c)) {
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
        cursorPosition = clamp(prefix.length() + clippedReplacement.length(), 0, value.length());
        selectionPosition = cursorPosition;
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
        ensureCursorVisible();
    }

    private void moveCursorTo(int position, boolean keepSelection) {
        cursorPosition = clamp(position, 0, value.length());
        if (!keepSelection) {
            selectionPosition = cursorPosition;
        }
        ensureCursorVisible();
    }

    private int getCursorAtMouse(double mouseX) {
        Font font = Minecraft.getInstance().font;
        int relativeX = Math.max(0, (int) mouseX - getTextAreaX());
        int safeDisplayOffset = getSafeDisplayOffset();
        String visibleText = value.substring(safeDisplayOffset);
        return safeDisplayOffset + font.plainSubstrByWidth(visibleText, relativeX).length();
    }

    private int getWordPosition(int direction) {
        int position = cursorPosition;
        if (direction < 0) {
            while (position > 0 && value.charAt(position - 1) == ' ') {
                position--;
            }
            while (position > 0 && value.charAt(position - 1) != ' ') {
                position--;
            }
        } else {
            int length = value.length();
            while (position < length && value.charAt(position) != ' ') {
                position++;
            }
            while (position < length && value.charAt(position) == ' ') {
                position++;
            }
        }
        return position;
    }

    private void ensureCursorVisible() {
        Font font = Minecraft.getInstance().font;
        int innerWidth = getTextAreaWidth();

        cursorPosition = clamp(cursorPosition, 0, value.length());
        selectionPosition = clamp(selectionPosition, 0, value.length());
        displayOffset = clamp(displayOffset, 0, cursorPosition);
        if (font.width(value.substring(displayOffset, cursorPosition)) > innerWidth) {
            while (displayOffset < cursorPosition && font.width(value.substring(displayOffset, cursorPosition)) > innerWidth) {
                displayOffset++;
            }
        } else if (cursorPosition < displayOffset) {
            displayOffset = cursorPosition;
        }

        displayOffset = clamp(displayOffset, 0, value.length());
    }

    private int getSafeDisplayOffset() {
        displayOffset = clamp(displayOffset, 0, Math.min(cursorPosition, value.length()));
        return displayOffset;
    }

    private void setValueSilently(String nextValue) {
        String sanitized = textValidator.apply(clipToMaxLength(nullToEmpty(nextValue), 0));
        value = sanitized == null ? "" : clipToMaxLength(sanitized, 0);
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

    private int getTextAreaRight() {
        return getPositionX() + Math.max(paddingLeft, getSizeWidth() - paddingRight);
    }

    private int getTextAreaWidth() {
        return Math.max(0, getTextAreaRight() - getTextAreaX());
    }

    private int getTextAreaY(Font font) {
        int availableHeight = Math.max(0, getSizeHeight() - paddingTop - paddingBottom);
        return getPositionY() + paddingTop + Math.max(0, (availableHeight - font.lineHeight) / 2);
    }

    private static String nullToEmpty(String text) {
        return text == null ? "" : text;
    }

    private static boolean isAllowedInputCharacter(char character) {
        return character != 167 && character >= ' ' && character != 127;
    }

    private String validateIntegralText(String text, long minValue, long maxValue) {
        String next = nullToEmpty(text);
        if (next.isEmpty()) return "";
        if (next.equals("-")) return minValue < 0 ? next : value;

        try {
            long parsed = Long.parseLong(next);
            if (parsed < minValue) return String.valueOf(minValue);
            if (parsed > maxValue) return String.valueOf(maxValue);
            return next;
        } catch (NumberFormatException ignored) {
            return value;
        }
    }

    private String validateDecimalText(String text, double minValue, double maxValue) {
        String next = nullToEmpty(text);
        if (next.isEmpty()) return "";
        if (next.equals("-")) return minValue < 0 ? next : value;
        if (next.equals(".") || next.equals("-.")) return next;

        try {
            double parsed = Double.parseDouble(next);
            if (parsed < minValue) return trimDecimalBound(minValue);
            if (parsed > maxValue) return trimDecimalBound(maxValue);
            return next;
        } catch (NumberFormatException ignored) {
            return value;
        }
    }

    private static String trimDecimalBound(double value) {
        if (value == Math.rint(value) && value <= Long.MAX_VALUE && value >= Long.MIN_VALUE) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
