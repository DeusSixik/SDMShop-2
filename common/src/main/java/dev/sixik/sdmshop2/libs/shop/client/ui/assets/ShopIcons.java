package dev.sixik.sdmshop2.libs.shop.client.ui.assets;

import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.unigui.api.render.SimpleTextureHandle;
import dev.sixik.unigui.api.render.TextureHandle;

/** Backend-neutral UniGUI textures used by the shop. */
public final class ShopIcons {
    private ShopIcons() { }
    public static final TextureHandle STAR_EMPTY = texture("gui/star_empty.png");
    public static final TextureHandle STAR_FULL = texture("gui/star.png");

    public static TextureHandle texture(String path) {
        String id = SDMShop2.resourceTexture(path).toString();
        return new SimpleTextureHandle(id, 16, 16, id);
    }
}
