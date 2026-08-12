package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.mojang.blaze3d.platform.InputConstants;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUiElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop.client.ui.textures.PixelBevelTexture;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ContextMenuWidget;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class ShopTabElement extends ButtonWidget implements ShopUiElement {
    public static final Component DEFAULT_TITLE = Component.translatable("shop.ui.common.no_title");
    public static final Component ALL_TITLE = Component.translatable("shop.ui.tabs.button.all");
    protected static final int ICON_SIZE = 14;
    protected static final int ICON_PADDING = 4;
    protected static final int ICON_TEXT_PADDING = ICON_SIZE + ICON_PADDING * 2 + 2;
    protected static final int ICON_PLACEHOLDER_COLOR = 0x66FFFFFF;
    protected static final int ICON_HOVER_COLOR = 0xCCFFFFFF;
    protected static final int RIGHT_MOUSE_BUTTON = 1;

    @Getter
    protected final @Nullable CatalogComponent component;
    protected final @Nullable ShopTabsPanelElement panel;
    protected boolean selected;

    public ShopTabElement(@Nullable CatalogComponent component) {
        this(null, component);
    }

    public ShopTabElement(@Nullable ShopTabsPanelElement panel, @Nullable CatalogComponent component) {
        this.panel = panel;
        this.component = component;

        setText(component == null ? ALL_TITLE : Component.translatable(component.getId()));
        setHoverTexture(new PixelBevelTexture(0xFF111624, PixelBevelTexture.ACCENT_LOW_COLOR, PixelBevelTexture.ACCENT_HIGH_COLOR, 0.7f));
        setClickedTexture(PixelBevelTexture.panel().pressed());
        setSelected(false);
        setOnClick((clickData) -> {
            if(clickData.button != InputConstants.MOUSE_BUTTON_LEFT)
                return;
            ShopUIEvents.invokeSelectCategory(component);
        });
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (canOpenCategoryContextMenu() && isMouseOverElement(mouseX, mouseY) && isMouseOverIcon(mouseX, mouseY)) {
            if (button == InputConstants.MOUSE_BUTTON_LEFT) {
                panel.openCategoryItemIconModal(component);
                playButtonClickSound();
                return true;
            }

            if (button == RIGHT_MOUSE_BUTTON) {
                panel.openCategoryTextureIconModal(component);
                playButtonClickSound();
                return true;
            }
        }

        if (button == RIGHT_MOUSE_BUTTON && canOpenCategoryContextMenu() && isMouseOverElement(mouseX, mouseY)) {
            openCategoryContextMenu((int) mouseX, (int) mouseY);
            playButtonClickSound();
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void drawInBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        Component savedText = text;
        text = Component.empty();
        try {
            super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        } finally {
            text = savedText;
        }

        drawCatalogIcon(graphics, mouseX, mouseY);
        drawTabText(graphics);
    }

    protected boolean canOpenCategoryContextMenu() {
        return component != null
                && panel != null
                && panel.isEditorMode()
                && panel.getEditSession() != null
                && !panel.getEditSession().closed();
    }

    protected void openCategoryContextMenu(int mouseX, int mouseY) {
        if (panel == null || component == null) {
            return;
        }

        ContextMenuWidget menu = new ContextMenuWidget(mouseX, mouseY, 138);
        menu.setScale(0.75f);
        menu.addItem(Component.translatable("shop.ui.common.rename"), () -> panel.openRenameCategoryModal(component))
                .addSeparator()
                .addItem(Component.translatable("shop.ui.common.move_up"), panel.canMoveCategory(component, -1), () -> panel.moveCategory(component, -1))
                .addItem(Component.translatable("shop.ui.common.move_down"), panel.canMoveCategory(component, 1), () -> panel.moveCategory(component, 1))
                .addItem(Component.translatable("shop.ui.common.move_to"), panel.getCategoryCount() > 1, () -> panel.openMoveCategoryModal(component))
                .addSeparator()
                .addItem(Component.translatable("shop.ui.common.delete"), () -> panel.openDeleteCategoryModal(component));
        ContextMenuWidget.open(this, menu);
    }

    public ShopTabElement setSelected(boolean selected) {
        this.selected = selected;
        setButtonTexture(selected ? PixelBevelTexture.panel().pressed() : PixelBevelTexture.panel());
        return this;
    }

    public boolean isSelected() {
        return selected;
    }

    protected boolean hasIconSlot() {
        return component != null && (component.hasIcon() || canOpenCategoryContextMenu());
    }

    @Environment(EnvType.CLIENT)
    protected void drawCatalogIcon(GuiGraphics graphics, int mouseX, int mouseY) {
        if (!hasIconSlot()) {
            return;
        }

        int iconX = getIconX();
        int iconY = getIconY();
        IGuiTexture texture = getCatalogIconTexture();
        if (texture != null) {
            texture.draw(graphics, mouseX, mouseY, iconX, iconY, ICON_SIZE, ICON_SIZE);
        } else if (canOpenCategoryContextMenu()) {
            drawIconPlaceholder(graphics, iconX, iconY, isMouseOverIcon(mouseX, mouseY) ? ICON_HOVER_COLOR : ICON_PLACEHOLDER_COLOR);
        }

        if (canOpenCategoryContextMenu() && isMouseOverIcon(mouseX, mouseY)) {
            drawIconOutline(graphics, iconX, iconY, ICON_HOVER_COLOR);
        }
    }

    @Environment(EnvType.CLIENT)
    protected void drawTabText(GuiGraphics graphics) {
        if (text == null || text.getString().isEmpty() || getSizeWidth() <= 0 || getSizeHeight() <= 0) {
            return;
        }

        Font font = Minecraft.getInstance().font;
        FormattedCharSequence orderedText = text.getVisualOrderText();
        int rawWidth = font.width(orderedText);
        if (rawWidth <= 0) {
            return;
        }

        int leftPadding = hasIconSlot() ? ICON_TEXT_PADDING : textPadding;
        int rightPadding = textPadding;
        int contentX = getPositionX() + leftPadding;
        int contentY = getPositionY() + textPadding;
        int contentWidth = Math.max(1, getSizeWidth() - leftPadding - rightPadding);
        int contentHeight = Math.max(1, getSizeHeight() - textPadding * 2);

        float widthScale = contentWidth / (float) rawWidth;
        float heightScale = contentHeight / (float) font.lineHeight;
        float drawScale = Math.min(textScale, Math.min(1.0f, Math.min(widthScale, heightScale)));
        drawScale = Math.max(minTextScale, drawScale);
        drawScale = Math.min(textScale, drawScale);

        float drawWidth = rawWidth * drawScale;
        float drawHeight = font.lineHeight * drawScale;
        float drawX = contentX + (contentWidth - drawWidth) / 2.0f;
        float drawY = getPositionY() + (getSizeHeight() - drawHeight) / 2.0f + textYOffset;

        graphics.enableScissor(contentX, contentY, contentX + contentWidth, contentY + contentHeight);
        try {
            graphics.pose().pushPose();
            try {
                graphics.pose().translate(drawX, drawY, 0);
                graphics.pose().scale(drawScale, drawScale, 1.0f);
                graphics.drawString(font, orderedText, 0, 0, isActive() ? textColor : disabledTextColor, textShadow);
            } finally {
                graphics.pose().popPose();
            }
        } finally {
            graphics.disableScissor();
        }
    }

    @Nullable
    protected IGuiTexture getCatalogIconTexture() {
        if (component == null) {
            return null;
        }

        if (component.getIconType() == CatalogComponent.IconType.ITEM) {
            ItemStack stack = component.getIconItem();
            if (stack != null && !stack.isEmpty() && stack.getItem() != Items.AIR) {
                return new ItemStackTexture(stack);
            }
        }

        if (component.getIconType() == CatalogComponent.IconType.TEXTURE) {
            ResourceLocation texture = component.getIconTexture();
            if (!CatalogComponent.isEmptyIconTexture(texture)) {
                return new ResourceTexture(texture);
            }
        }

        return null;
    }

    protected boolean isMouseOverIcon(double mouseX, double mouseY) {
        if (!hasIconSlot()) {
            return false;
        }

        int iconX = getIconX();
        int iconY = getIconY();
        return mouseX >= iconX && mouseX < iconX + ICON_SIZE
                && mouseY >= iconY && mouseY < iconY + ICON_SIZE;
    }

    protected int getIconX() {
        return getPositionX() + ICON_PADDING;
    }

    protected int getIconY() {
        return getPositionY() + Math.max(0, (getSizeHeight() - ICON_SIZE) / 2);
    }

    @Environment(EnvType.CLIENT)
    protected void drawIconPlaceholder(GuiGraphics graphics, int x, int y, int color) {
        drawIconOutline(graphics, x, y, color);
        int centerX = x + ICON_SIZE / 2;
        int centerY = y + ICON_SIZE / 2;
        graphics.fill(centerX, y + 4, centerX + 1, y + ICON_SIZE - 4, color);
        graphics.fill(x + 4, centerY, x + ICON_SIZE - 4, centerY + 1, color);
    }

    @Environment(EnvType.CLIENT)
    protected void drawIconOutline(GuiGraphics graphics, int x, int y, int color) {
        graphics.fill(x, y, x + ICON_SIZE, y + 1, color);
        graphics.fill(x, y + ICON_SIZE - 1, x + ICON_SIZE, y + ICON_SIZE, color);
        graphics.fill(x, y, x + 1, y + ICON_SIZE, color);
        graphics.fill(x + ICON_SIZE - 1, y, x + ICON_SIZE, y + ICON_SIZE, color);
    }

    @Override
    public void alightWidget() {

    }

}
