package dev.sixik.sdmshop2.libs.shop.client.ui.widgets;

import dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.IconType;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.ShopEditorState;
import dev.sixik.unigui.api.render.SimpleTextureHandle;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.display.ImageView;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/** Currency icon factory used by currency components, including unsaved editor previews. */
public final class ShopCurrencyWidgets {
    /** Prevents instantiation. */
    private ShopCurrencyWidgets() {}

    /**
     * Resolves a currency's own icon, preserving the editor's pending currency changes.
     *
     * @param id currency identifier.
     * @return fresh item, texture or text icon; unknown currencies display their identifier.
     */
    public static Widget icon(ResourceLocation id) {
        ShopEditorState editor = ShopEditorState.get();
        CurrencyDraft draft = editor.isEditorMode() ? editor.findCurrencyDraft(id) : null;
        if (draft != null) {
            if (draft.kind() == CurrencyDraft.Kind.ITEM && draft.itemStack() != null)
                return item(draft.itemStack());
            if (draft.kind() == CurrencyDraft.Kind.TEXTURE && draft.iconTexture() != null)
                return texture(draft.iconTexture().toString());
            if (draft.kind() == CurrencyDraft.Kind.TEXT)
                return ShopRenderWidgets.text(draft.iconText() != null ? draft.iconText() : "$");
        }
        ICurrency currency = SDMEconomyServiceClient.getCurrency(id);
        CurrencyIcon icon = currency != null ? currency.getIcon() : null;
        if (icon != null) {
            Object value = icon.icon();
            if (icon.type() == IconType.ITEM) {
                if (value instanceof ItemStack stack) return item(stack);
                if (value instanceof Item item) return item(item.getDefaultInstance());
            }
            if (icon.type() == IconType.TEXTURE) {
                if (value instanceof ResourceLocation location) return texture(location.toString());
                if (value instanceof String text
                        && (text.contains(":") || text.contains("/") || text.endsWith(".png")))
                    return texture(text);
            }
            if (value instanceof String text) return ShopRenderWidgets.text(text);
        }
        return ShopRenderWidgets.text(id != null ? id.toString() : "$");
    }

    /**
     * Creates a small currency item icon.
     *
     * @param stack currency item.
     * @return fresh icon widget.
     */
    private static Widget item(ItemStack stack) {
        ShopItemWidget item = new ShopItemWidget(stack, 12);
        item.layout(style -> style.fixed(12, 12).flexNone());
        return item;
    }

    /**
     * Creates a small currency texture icon.
     *
     * @param texture texture resource identifier.
     * @return fresh icon widget.
     */
    private static Widget texture(String texture) {
        ImageView image = new ImageView(new SimpleTextureHandle(texture, 12, 12, texture));
        image.layout(style -> style.fixed(12, 12).flexNone());
        return image;
    }
}
