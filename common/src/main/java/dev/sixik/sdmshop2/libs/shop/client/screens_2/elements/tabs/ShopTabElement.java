package dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.tabs;

import dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.ShopUiElement;
import dev.sixik.sdmshop2.libs.shop.client.textures.PixelBevelTexture;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public class ShopTabElement extends ButtonWidget implements ShopUiElement {
    public static final Component DEFAULT_TITLE = Component.literal("No Title");

    protected final @Nullable CatalogComponent component;

    public ShopTabElement(@Nullable CatalogComponent component) {
        this.component = component;

        setText(component == null ? DEFAULT_TITLE : Component.translatable(component.getId()));
        setButtonTexture(PixelBevelTexture.panel());
        setHoverTexture(new PixelBevelTexture(0xFF111624, PixelBevelTexture.ACCENT_LOW_COLOR, PixelBevelTexture.ACCENT_HIGH_COLOR, 0.7f));
        setClickedTexture(PixelBevelTexture.panel().pressed());
    }

    @Override
    public void alightWidget() {

    }

}
