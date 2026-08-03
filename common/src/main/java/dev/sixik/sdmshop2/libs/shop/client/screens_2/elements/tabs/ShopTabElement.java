package dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.tabs;

import com.mojang.blaze3d.platform.InputConstants;
import dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.ShopUiElement;
import dev.sixik.sdmshop2.libs.shop.client.textures.PixelBevelTexture;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public class ShopTabElement extends ButtonWidget implements ShopUiElement {
    public static final Component DEFAULT_TITLE = Component.literal("No Title");
    public static final Component ALL_TITLE = Component.translatable("shop.ui.tabs.button.all");

    @Getter
    protected final @Nullable CatalogComponent component;
    protected boolean selected;

    public ShopTabElement(@Nullable CatalogComponent component) {
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
