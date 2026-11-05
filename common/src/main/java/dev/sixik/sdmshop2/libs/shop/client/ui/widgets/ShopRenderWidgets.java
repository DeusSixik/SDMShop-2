package dev.sixik.sdmshop2.libs.shop.client.ui.widgets;

import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopOfferView.CostEntry;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopTheme;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.render.SimpleTextureHandle;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.HBox;
import dev.sixik.unigui.widgets.display.ImageView;
import dev.sixik.unigui.widgets.display.Label;

import net.minecraft.world.item.ItemStack;

/** Optional building blocks for built-in components and add-ons; cards do not select these. */
public final class ShopRenderWidgets {
    /** Prevents instantiation. */
    private ShopRenderWidgets() {}

    /**
     * Creates a decorated item preview whose tooltip is managed by the containing component panel.
     *
     * @param stack displayed item and amount.
     * @return fresh item widget.
     */
    public static ShopItemWidget item(ItemStack stack) {
        return new ShopItemWidget(stack, 28).asSlot(36, 36).decorations(true);
    }

    /**
     * Creates a themed text presentation without imposing a fixed height.
     *
     * @param text component-owned display text.
     * @return fresh label.
     */
    public static Label text(String text) {
        Label label = new Label(text);
        label.color(ShopTheme.color(ShopTheme.TEXT_PRIMARY));
        return label;
    }

    /**
     * Creates a rounded badge, placed above the separator below the products by the component host.
     *
     * @param text promotion text.
     * @return fresh badge containing a readable foreground label.
     */
    public static Widget badge(String text) {
        ShopBadge badge = new ShopBadge();
        badge.addChild(text(text));
        return badge;
    }

    /**
     * Creates a payment row from a component-owned icon and formatted amount.
     *
     * @param icon arbitrary fresh icon widget, or null for text-only payment.
     * @param amount displayed amount.
     * @return fresh row.
     */
    public static Widget cost(Widget icon, String amount) {
        HBox row = new HBox();
        row.layout(style -> style.alignItems(Align.CENTER).gap(3).flexNone());
        if (icon != null) row.addChild(icon);
        row.addChild(text(amount));
        return row;
    }

    /**
     * Adapts the former icon DTO for standalone previews that have no backing component.
     *
     * @param entry legacy preview data; real costs use CostComponent.createRender instead.
     * @return fresh payment row.
     */
    public static Widget legacyCost(CostEntry entry) {
        Widget icon = null;
        if (entry.itemIcon() != null && !entry.itemIcon().isEmpty()) {
            icon = new ShopItemWidget(entry.itemIcon(), 12);
            ((ShopItemWidget) icon).layout(style -> style.fixed(12, 12).flexNone());
        } else if (entry.textureIcon() != null) {
            String texture = entry.textureIcon().toString();
            ImageView image = new ImageView(new SimpleTextureHandle(texture, 12, 12, texture));
            image.layout(style -> style.fixed(12, 12).flexNone());
            icon = image;
        } else if (entry.textIcon() != null) {
            icon = text(entry.textIcon());
        }
        return cost(icon, entry.amount());
    }
}
