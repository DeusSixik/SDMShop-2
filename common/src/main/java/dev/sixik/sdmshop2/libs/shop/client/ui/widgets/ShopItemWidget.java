package dev.sixik.sdmshop2.libs.shop.client.ui.widgets;

import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopTheme;
import dev.sixik.unigui.api.layout.Align;
import dev.sixik.unigui.api.layout.LayoutContext;
import dev.sixik.unigui.api.layout.LayoutSize;
import dev.sixik.unigui.api.math.RectView;
import dev.sixik.unigui.api.render.RenderContext;
import dev.sixik.unigui.backend.minecraft_impl.MinecraftGuiRenderBackend;
import dev.sixik.unigui.widgets.containers.Box;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.minecraft.MinecraftItemTooltip;

import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

/**
 * Виджет отображения предмета и/или слота Minecraft. Наследует Box, что позволяет использовать его
 * как самостоятельный слот с рамкой и фоном, центрируя предмет математически точно внутри границ
 * слота (без смещений в угол).
 */
public class ShopItemWidget extends Box {

    private ItemStack stack = ItemStack.EMPTY;
    private float itemSize = 16.0f;
    private boolean decorations = false;
    private @Nullable MinecraftItemTooltip tooltip = null;

    /** Overlay that owns the current tooltip, independent of the active screen. */
    private @Nullable OverlayLayer tooltipOwner;

    public ShopItemWidget() {
        themeEnabled(false);
        boxVisualEnabled(false);
        backgroundVisible(false);
        borderVisible(false);
    }

    public ShopItemWidget(ItemStack stack, float itemSize) {
        this();
        this.stack = stack != null ? stack : ItemStack.EMPTY;
        this.itemSize = itemSize;
    }

    /** Конфигурирует виджет как полноценный слот инвентаря с фоном, рамкой и центрированием. */
    public ShopItemWidget asSlot(float slotWidth, float slotHeight) {
        boxVisualEnabled(true);
        backgroundVisible(true);
        background(ShopTheme.color(ShopTheme.BG_SLOT));
        borderVisible(true);
        border(ShopTheme.color(ShopTheme.BORDER_SLOT));
        borderWidth(ShopTheme.BORDER_WIDTH_THIN);
        radius(ShopTheme.RADIUS_SLOT);
        layout(style -> style.fixed(slotWidth, slotHeight).alignSelf(Align.CENTER).flexNone());
        return this;
    }

    public ShopItemWidget item(ItemStack stack) {
        this.stack = stack != null ? stack : ItemStack.EMPTY;
        invalidate(2);
        return this;
    }

    public ItemStack item() {
        return stack;
    }

    public ShopItemWidget size(float size) {
        this.itemSize = size;
        layout(s -> s.fixed(size, size));
        invalidate(1);
        return this;
    }

    public float size() {
        return itemSize;
    }

    public ShopItemWidget decorations(boolean decorations) {
        this.decorations = decorations;
        invalidate(2);
        return this;
    }

    public boolean decorations() {
        return decorations;
    }

    public ShopItemWidget attachTooltip(@Nullable OverlayLayer overlayLayer) {
        if (tooltipOwner != overlayLayer) detachTooltip(tooltipOwner);
        if (overlayLayer != null && tooltip == null && !stack.isEmpty()) {
            tooltipOwner = overlayLayer;
            tooltip = new MinecraftItemTooltip(this, () -> this.stack);
            overlayLayer.addOverlay(tooltip);
        }
        return this;
    }

    public ShopItemWidget detachTooltip(@Nullable OverlayLayer overlayLayer) {
        if (tooltip != null) {
            if (tooltipOwner != null) tooltipOwner.removeOverlay(tooltip);
            tooltip.dispose();
            tooltip = null;
        }
        tooltipOwner = null;
        return this;
    }

    /** Releases the item tooltip before disposing the widget tree. */
    @Override
    public void dispose() {
        detachTooltip(tooltipOwner);
        super.dispose();
    }

    public @Nullable MinecraftItemTooltip getTooltip() {
        return tooltip;
    }

    @Override
    public void measure(LayoutContext context) {
        super.measure(context);
        // Если размеры не заданы через layout style, берем размер предмета
        if (desiredSize() == null || desiredSize().width() <= 0.0f) {
            setDesiredSize(LayoutSize.of(itemSize, itemSize));
        }
    }

    @Override
    protected void renderContent(RenderContext context) {
        super.renderContent(context);
        if (stack == null || stack.isEmpty()) {
            return;
        }

        // Центрируем предмет строго внутри layoutBounds() слота/виджета
        RectView bounds = layoutBounds();
        float x = bounds.x() + Math.max(0.0f, (bounds.width() - itemSize) * 0.5f);
        float y = bounds.y() + Math.max(0.0f, (bounds.height() - itemSize) * 0.5f);
        float opacity = context.opacityMultiplier();
        float size = this.itemSize;
        ItemStack itemStack = this.stack;
        boolean deco = this.decorations;

        context.custom(
                backend -> {
                    if (backend instanceof MinecraftGuiRenderBackend mcBackend) {
                        mcBackend.renderItemPreview(itemStack, x, y, size, opacity, deco);
                    }
                });
    }
}
