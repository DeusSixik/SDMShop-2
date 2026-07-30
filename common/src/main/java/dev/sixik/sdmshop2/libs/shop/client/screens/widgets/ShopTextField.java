package dev.sixik.sdmshop2.libs.shop.client.screens.widgets;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.TextFieldWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ShopTextField extends TextFieldWidget {

    public ShopTextField() {
        super();
        isBordered = true;
        ensureShopEditBox();
    }

    public ShopTextField(int xPosition, int yPosition, int width, int height, Supplier<String> textSupplier, Consumer<String> textResponder) {
        super(xPosition, yPosition, width, height, textSupplier, textResponder);
        isBordered = true;
        ensureShopEditBox();
    }

    @Override
    public void initWidget() {
        super.initWidget();
        ensureShopEditBox();
    }

    @Override
    public ShopTextField setCurrentString(Object currentString) {
        this.currentString = currentString == null ? "" : currentString.toString();
        if (ensureShopEditBox() && !this.textField.getValue().equals(this.currentString)) {
            this.textField.setValue(this.currentString);
        }
        return this;
    }

    @Override
    public String getRawCurrentString() {
        return ensureShopEditBox() ? textField.getValue() : getCurrentString();
    }

    @Override
    public void onFocusChanged(Widget lastFocus, Widget focus) {
        if (ensureShopEditBox()) {
            this.textField.setFocused(isFocus());
        }
    }

    @Override
    public void drawInBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (ensureShopEditBox()) {
            super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        } else {
            drawBackgroundTexture(graphics, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return ensureShopEditBox() && super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return ensureShopEditBox() && super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return ensureShopEditBox() && super.charTyped(codePoint, modifiers);
    }

    @Override
    public ShopTextField setBordered(boolean bordered) {
        isBordered = bordered;
        if (ensureShopEditBox()) {
            this.textField.setBordered(bordered);
            onPositionUpdate();
            onSizeUpdate();
        }
        return this;
    }

    @Override
    public ShopTextField setTextColor(int textColor) {
        this.textColor = textColor;
        if (ensureShopEditBox()) {
            this.textField.setTextColor(textColor);
        }
        return this;
    }

    @Override
    public ShopTextField setMaxStringLength(int maxStringLength) {
        this.maxStringLength = maxStringLength;
        if (ensureShopEditBox()) {
            this.textField.setMaxLength(maxStringLength);
        }
        return this;
    }

    private boolean ensureShopEditBox() {
        if (textField instanceof ShopEditBox) {
            return true;
        }
        if (!isRemote()) {
            return false;
        }

        String value = textField == null ? getCurrentString() : textField.getValue();
        boolean focused = textField != null && textField.isFocused();

        Font fontRenderer = Minecraft.getInstance().font;
        this.textField = new ShopEditBox(fontRenderer, getPositionX(), getPositionY(), getSizeWidth(), getSizeHeight(), Component.literal("text field"), this);
        this.textField.setBordered(isBordered);
        this.textField.setMaxLength(this.maxStringLength);
        this.textField.setTextColor(this.textColor);
        this.textField.setResponder(this::onTextChanged);
        this.textField.setValue(value);
        this.textField.setFocused(focused);
        onPositionUpdate();
        onSizeUpdate();
        return true;
    }
}
