package dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets;

import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.sixik.sdmshop2.libs.shop.client.textures.ColorRectAndBorderTexture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class ButtonWidget extends Widget {

    private static final int DEFAULT_WIDTH = 40;
    private static final int DEFAULT_HEIGHT = 20;
    private static final int DEFAULT_FILL_COLOR = 0xFF2A2A36;
    private static final int DEFAULT_BORDER_COLOR = 0xFF3D3D4E;
    private static final int DEFAULT_HOVER_FILL_COLOR = 0xFF34384A;
    private static final int DEFAULT_HOVER_BORDER_COLOR = 0xFF7986CB;
    private static final int DEFAULT_PRESSED_FILL_COLOR = 0xFF202331;
    private static final int DEFAULT_DISABLED_OVERLAY_COLOR = 0x66000000;
    private static final int DEFAULT_TEXT_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_DISABLED_TEXT_COLOR = 0xFF8A8A8A;
    private static final int DEFAULT_TEXT_PADDING = 3;
    private static final float DEFAULT_MIN_TEXT_SCALE = 0.35f;

    protected IGuiTexture buttonTexture;
    protected IGuiTexture hoverTexture;
    protected IGuiTexture clickedTexture;
    protected IGuiTexture disabledTexture;

    protected Consumer<ClickData> onPressCallback;
    protected boolean isClicked;

    protected Component text = Component.empty();
    protected int textColor = DEFAULT_TEXT_COLOR;
    protected int disabledTextColor = DEFAULT_DISABLED_TEXT_COLOR;
    protected boolean textShadow;
    protected int textPadding = DEFAULT_TEXT_PADDING;
    protected float textScale = 1.0f;
    protected float minTextScale = DEFAULT_MIN_TEXT_SCALE;
    protected int textYOffset;

    /**
     * Left mouse button by default. Use {@link #allowAnyMouseButton()} if a button
     * should react to every mouse button, like LDLib's default ButtonWidget.
     */
    protected int acceptedMouseButton = 0;

    public ButtonWidget() {
        this(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, (Consumer<ClickData>) null);
    }

    public ButtonWidget(Component text) {
        this(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, text, null);
    }

    public ButtonWidget(String text) {
        this(Component.literal(text));
    }

    public ButtonWidget(int x, int y, int width, int height, Consumer<ClickData> onPressed) {
        this(x, y, width, height, defaultButtonTexture(), onPressed);
    }

    public ButtonWidget(int x, int y, int width, int height, Component text, Consumer<ClickData> onPressed) {
        this(x, y, width, height, defaultButtonTexture(), onPressed);
        setText(text);
    }

    public ButtonWidget(int x, int y, int width, int height, String text, Consumer<ClickData> onPressed) {
        this(x, y, width, height, Component.literal(text), onPressed);
    }

    public ButtonWidget(int x, int y, int width, int height, IGuiTexture buttonTexture, Consumer<ClickData> onPressed) {
        super(x, y, width, height);
        this.buttonTexture = buttonTexture == null ? defaultButtonTexture() : buttonTexture;
        this.hoverTexture = defaultHoverTexture();
        this.clickedTexture = defaultClickedTexture();
        this.onPressCallback = onPressed;
    }

    public ButtonWidget setOnPressCallback(Consumer<ClickData> onPressCallback) {
        this.onPressCallback = onPressCallback;
        return this;
    }

    public ButtonWidget onPress(Consumer<ClickData> onPressCallback) {
        return setOnPressCallback(onPressCallback);
    }

    public ButtonWidget setOnClick(Consumer<ClickData> onPressCallback) {
        return setOnPressCallback(onPressCallback);
    }

    public ButtonWidget setButtonTexture(IGuiTexture... textures) {
        this.buttonTexture = group(textures);
        return this;
    }

    @Override
    public ButtonWidget setBackground(IGuiTexture... textures) {
        return setButtonTexture(textures);
    }

    @Override
    public ButtonWidget setHoverTexture(IGuiTexture... textures) {
        this.hoverTexture = group(textures);
        return this;
    }

    public ButtonWidget kjs$setHoverTexture(IGuiTexture... textures) {
        return setHoverTexture(textures);
    }

    public ButtonWidget setClickedTexture(IGuiTexture... textures) {
        this.clickedTexture = group(textures);
        return this;
    }

    public ButtonWidget setDisabledTexture(IGuiTexture... textures) {
        this.disabledTexture = group(textures);
        return this;
    }

    public ButtonWidget setHoverBorderTexture(int border, int color) {
        return setHoverTexture(new ColorBorderTexture(border, color));
    }

    public ButtonWidget setButtonColors(int fillColor, int borderColor) {
        this.buttonTexture = new ColorRectAndBorderTexture(fillColor, borderColor, 1).setRadius(3);
        return this;
    }

    public ButtonWidget setHoverColors(int fillColor, int borderColor) {
        this.hoverTexture = new ColorRectAndBorderTexture(fillColor, borderColor, 1).setRadius(3);
        return this;
    }

    public ButtonWidget setClickedColors(int fillColor, int borderColor) {
        this.clickedTexture = new ColorRectAndBorderTexture(fillColor, borderColor, 1).setRadius(3);
        return this;
    }

    public ButtonWidget setText(Component text) {
        this.text = text == null ? Component.empty() : text;
        return this;
    }

    public ButtonWidget setText(String text) {
        return setText(text == null ? Component.empty() : Component.literal(text));
    }

    public Component getText() {
        return text;
    }

    public ButtonWidget setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public ButtonWidget setDisabledTextColor(int disabledTextColor) {
        this.disabledTextColor = disabledTextColor;
        return this;
    }

    public ButtonWidget setTextShadow(boolean textShadow) {
        this.textShadow = textShadow;
        return this;
    }

    public ButtonWidget setTextPadding(int textPadding) {
        this.textPadding = Math.max(0, textPadding);
        return this;
    }

    public ButtonWidget setTextScale(float textScale) {
        this.textScale = Math.max(0.01f, textScale);
        return this;
    }

    public ButtonWidget textScale(float textScale) {
        return setTextScale(textScale);
    }

    public ButtonWidget setMinTextScale(float minTextScale) {
        this.minTextScale = Math.max(0.01f, minTextScale);
        return this;
    }

    public ButtonWidget setTextYOffset(int textYOffset) {
        this.textYOffset = textYOffset;
        return this;
    }

    public ButtonWidget textYOffset(int textYOffset) {
        return setTextYOffset(textYOffset);
    }

    public ButtonWidget setAcceptedMouseButton(int acceptedMouseButton) {
        this.acceptedMouseButton = acceptedMouseButton;
        return this;
    }

    public ButtonWidget allowAnyMouseButton() {
        return setAcceptedMouseButton(-1);
    }

    public boolean isClicked() {
        return isClicked;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawInBackground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        drawText(graphics);
    }

    @Override
    @Environment(EnvType.CLIENT)
    protected void drawBackgroundTexture(@NonNull GuiGraphics graphics, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();

        drawTexture(buttonTexture, graphics, mouseX, mouseY);

        if (!isActive()) {
            if (disabledTexture != null) {
                drawTexture(disabledTexture, graphics, mouseX, mouseY);
            } else {
                graphics.fill(getPositionX(), getPositionY(), getPositionX() + getSizeWidth(), getPositionY() + getSizeHeight(), DEFAULT_DISABLED_OVERLAY_COLOR);
            }
            return;
        }

        if (isClicked && clickedTexture != null) {
            drawTexture(clickedTexture, graphics, mouseX, mouseY);
            return;
        }

        if (isMouseOverElement(mouseX, mouseY) && hoverTexture != null) {
            drawTexture(hoverTexture, graphics, mouseX, mouseY);
        }
    }

    private void drawTexture(IGuiTexture texture, GuiGraphics graphics, int mouseX, int mouseY) {
        if (texture == null || getSizeWidth() <= 0 || getSizeHeight() <= 0) return;
        texture.draw(graphics, mouseX, mouseY, getPositionX(), getPositionY(), getSizeWidth(), getSizeHeight());
    }

    private void drawText(GuiGraphics graphics) {
        if (text == null || text.getString().isEmpty() || getSizeWidth() <= 0 || getSizeHeight() <= 0) {
            return;
        }

        Font font = Minecraft.getInstance().font;
        FormattedCharSequence orderedText = text.getVisualOrderText();
        int rawWidth = font.width(orderedText);
        if (rawWidth <= 0) return;

        int contentX = getPositionX() + textPadding;
        int contentY = getPositionY() + textPadding;
        int contentWidth = Math.max(1, getSizeWidth() - textPadding * 2);
        int contentHeight = Math.max(1, getSizeHeight() - textPadding * 2);

        float widthScale = contentWidth / (float) rawWidth;
        float heightScale = contentHeight / (float) font.lineHeight;
        float drawScale = Math.min(textScale, Math.min(widthScale, heightScale));
        drawScale = Math.max(Math.min(textScale, minTextScale), drawScale);

        float scaledWidth = rawWidth * drawScale;
        float scaledHeight = font.lineHeight * drawScale;
        float x = contentX + Math.max(0, (contentWidth - scaledWidth) / 2f);
        float y = contentY + Math.max(0, (contentHeight - scaledHeight) / 2f) + textYOffset;

        graphics.enableScissor(contentX, contentY, contentX + contentWidth, contentY + contentHeight);
        try {
            graphics.pose().pushPose();
            try {
                graphics.pose().translate(x, y, 0);
                graphics.pose().scale(drawScale, drawScale, 1.0f);
                graphics.drawString(font, orderedText, 0, 0, isActive() ? textColor : disabledTextColor, textShadow);
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
        if (!isActive() || !isVisible() || !acceptsMouseButton(button) || !isMouseOverElement(mouseX, mouseY)) {
            return false;
        }

        isClicked = true;
        ClickData clickData = new ClickData();
        writeClientAction(1, clickData::writeToBuf);
        if (onPressCallback != null) {
            onPressCallback.accept(clickData);
        }
        playButtonClickSound();
        return true;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (acceptsMouseButton(button)) {
            isClicked = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void handleClientAction(int id, FriendlyByteBuf buffer) {
        super.handleClientAction(id, buffer);
        if (id == 1) {
            ClickData clickData = ClickData.readFromBuf(buffer);
            if (onPressCallback != null) {
                onPressCallback.accept(clickData);
            }
        }
    }

    private boolean acceptsMouseButton(int button) {
        return acceptedMouseButton < 0 || button == acceptedMouseButton;
    }

    private static IGuiTexture group(IGuiTexture... textures) {
        if (textures == null || textures.length == 0) return null;
        if (textures.length == 1) return textures[0];
        return new GuiTextureGroup(textures);
    }

    private static IGuiTexture defaultButtonTexture() {
        return new ColorRectAndBorderTexture(DEFAULT_FILL_COLOR, DEFAULT_BORDER_COLOR, 1).setRadius(3);
    }

    private static IGuiTexture defaultHoverTexture() {
        return new ColorRectAndBorderTexture(DEFAULT_HOVER_FILL_COLOR, DEFAULT_HOVER_BORDER_COLOR, 1).setRadius(3);
    }

    private static IGuiTexture defaultClickedTexture() {
        return new ColorRectAndBorderTexture(DEFAULT_PRESSED_FILL_COLOR, DEFAULT_HOVER_BORDER_COLOR, 1).setRadius(3);
    }
}
