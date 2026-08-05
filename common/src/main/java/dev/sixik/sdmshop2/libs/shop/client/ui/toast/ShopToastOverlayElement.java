package dev.sixik.sdmshop2.libs.shop.client.ui.toast;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class ShopToastOverlayElement extends WidgetGroup implements ShopUiElement {

    private static final int TOAST_WIDTH = 236;
    private static final int MIN_TOAST_HEIGHT = 42;
    private static final int MAX_MESSAGE_LINES = 3;
    private static final int MARGIN = 10;
    private static final int GAP = 6;
    private static final int PADDING = 8;
    private static final int ACCENT_WIDTH = 3;
    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int MUTED_TEXT_COLOR = 0xFFD0D5E1;
    private static final int BORDER_COLOR = 0xAAFFFFFF;

    public ShopToastOverlayElement() {
        super(0, 0, 1, 1);
        setActive(false);
    }

    public void syncToRoot(WidgetGroup root) {
        if (root == null) {
            return;
        }

        setSelfPosition(0, 0);
        setSize(root.getSizeWidth(), root.getSizeHeight());
        setVisible(true);
        setActive(false);
    }

    @Override
    public void alightWidget() {
        WidgetGroup parent = getParent();
        if (parent != null) {
            syncToRoot(parent);
        }
    }

    @Override
    public void setSize(Size size) {
        super.setSize(size);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        WidgetGroup parent = getParent();
        if (parent != null) {
            syncToRoot(parent);
        }

        setVisible(ShopToasts.hasToasts());
    }

    @Override
    public void drawInForeground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        List<ShopToast> toasts = ShopToasts.snapshot();
        if (toasts.isEmpty()) {
            return;
        }

        Font font = Minecraft.getInstance().font;
        int x = getPositionX() + Math.max(MARGIN, getSizeWidth() - TOAST_WIDTH - MARGIN);
        int y = getPositionY() + MARGIN;

        for (int i = toasts.size() - 1; i >= 0; i--) {
            ShopToast toast = toasts.get(i);
            int height = computeToastHeight(font, toast);

            drawToast(graphics, font, toast, x, y, TOAST_WIDTH, height);
            y += height + GAP;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean mouseWheelMove(double mouseX, double mouseY, double wheelDelta) {
        return false;
    }

    @Override
    public Widget getHoverElement(double mouseX, double mouseY) {
        return null;
    }

    private void drawToast(GuiGraphics graphics, Font font, ShopToast toast, int x, int y, int width, int height) {
        int background = toast.getType().getBackgroundColor();
        int accent = toast.getType().getAccentColor();

        graphics.fill(x, y, x + width, y + height, background);
        graphics.fill(x, y, x + ACCENT_WIDTH, y + height, accent);
        drawBorder(graphics, x, y, width, height, BORDER_COLOR);

        int textX = x + PADDING + ACCENT_WIDTH;
        int titleY = y + 6;
        int textWidth = width - PADDING * 2 - ACCENT_WIDTH;

        graphics.drawString(font, trimToWidth(font, toast.getTitle().getString(), textWidth), textX, titleY, TEXT_COLOR, false);

        List<FormattedCharSequence> lines = font.split(toast.getMessage(), textWidth);
        int lineY = titleY + font.lineHeight + 3;
        int visibleLines = Math.min(MAX_MESSAGE_LINES, lines.size());

        for (int i = 0; i < visibleLines; i++) {
            graphics.drawString(font, lines.get(i), textX, lineY, MUTED_TEXT_COLOR, false);
            lineY += font.lineHeight;
        }

        drawLifetimeBar(graphics, toast, x, y + height - 2, width);
    }

    private void drawLifetimeBar(GuiGraphics graphics, ShopToast toast, int x, int y, int width) {
        float remaining = 1.0f - toast.progress(System.currentTimeMillis());
        int progressWidth = Math.max(0, Math.round(width * remaining));

        graphics.fill(x, y, x + width, y + 2, 0x66000000);
        graphics.fill(x, y, x + progressWidth, y + 2, toast.getType().getAccentColor());
    }

    private int computeToastHeight(Font font, ShopToast toast) {
        int textWidth = TOAST_WIDTH - PADDING * 2 - ACCENT_WIDTH;
        int messageLines = Math.min(MAX_MESSAGE_LINES, Math.max(1, font.split(toast.getMessage(), textWidth).size()));
        int textHeight = font.lineHeight + 3 + messageLines * font.lineHeight;

        return Math.max(MIN_TOAST_HEIGHT, textHeight + 12);
    }

    private void drawBorder(GuiGraphics graphics, int x, int y, int width, int height, int color) {
        graphics.fill(x, y, x + width, y + 1, color);
        graphics.fill(x, y + height - 1, x + width, y + height, color);
        graphics.fill(x, y, x + 1, y + height, color);
        graphics.fill(x + width - 1, y, x + width, y + height, color);
    }

    private String trimToWidth(Font font, String text, int width) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        if (font.width(text) <= width) {
            return text;
        }

        return font.plainSubstrByWidth(text, Math.max(0, width - font.width("..."))) + "...";
    }
}

