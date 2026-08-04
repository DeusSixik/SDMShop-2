package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import com.mojang.blaze3d.platform.InputConstants;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUiElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop.client.ui.textures.PixelBevelTexture;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ContextMenuWidget;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public class ShopTabElement extends ButtonWidget implements ShopUiElement {
    public static final Component DEFAULT_TITLE = Component.translatable("shop.ui.common.no_title");
    public static final Component ALL_TITLE = Component.translatable("shop.ui.tabs.button.all");

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
        if (button == 1 && canOpenCategoryContextMenu() && isMouseOverElement(mouseX, mouseY)) {
            openCategoryContextMenu((int) mouseX, (int) mouseY);
            playButtonClickSound();
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
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

    @Override
    public void alightWidget() {

    }

}
