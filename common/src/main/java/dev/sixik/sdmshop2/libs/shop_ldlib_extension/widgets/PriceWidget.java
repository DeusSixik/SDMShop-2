package dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class PriceWidget extends Widget {

    private static final int DEFAULT_WIDTH = 80;
    private static final int DEFAULT_HEIGHT = 12;
    private static final int DEFAULT_PRICE_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_OLD_PRICE_COLOR = 0xFF9A9A9A;
    private static final int DEFAULT_STRIKE_COLOR = 0xFFB0B0B0;
    private static final int DEFAULT_GAP = 3;

    protected Component oldPrice = Component.empty();
    protected Component price = Component.empty();

    protected int priceColor = DEFAULT_PRICE_COLOR;
    protected int oldPriceColor = DEFAULT_OLD_PRICE_COLOR;
    protected int strikeColor = DEFAULT_STRIKE_COLOR;
    protected boolean shadow;

    protected float scale = 1.0f;
    protected float priceScale = 1.0f;
    protected float oldPriceScale = 0.75f;

    protected int gap = DEFAULT_GAP;
    protected int paddingLeft;
    protected int paddingTop;
    protected int paddingRight;
    protected int paddingBottom;

    protected boolean autoSize = true;
    protected boolean showOldPrice = true;
    protected boolean strikeOldPrice = true;
    protected float strikeThickness = 0.5f;
    protected float strikeYRatio = 0.45f;
    protected float strikeYOffset;

    protected TextLabel.HorizontalAlignment horizontalAlignment = TextLabel.HorizontalAlignment.LEFT;
    protected TextLabel.VerticalAlignment verticalAlignment = TextLabel.VerticalAlignment.CENTER;

    private final PriceLayout layoutCache = new PriceLayout();
    private boolean layoutDirty = true;
    private float cachedLayoutExternalScale = Float.NaN;
    private boolean resizingToContent;

    public PriceWidget() {
        this(Component.empty());
    }

    public PriceWidget(Component price) {
        this(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, Component.empty(), price);
    }

    public PriceWidget(Component oldPrice, Component price) {
        this(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, oldPrice, price);
    }

    public PriceWidget(int x, int y, int width, int height) {
        this(x, y, width, height, Component.empty(), Component.empty());
    }

    public PriceWidget(int x, int y, int width, int height, Component oldPrice, Component price) {
        super(x, y, width, height);
        this.oldPrice = normalize(oldPrice);
        this.price = normalize(price);
        resizeToContentIfNeeded();
    }

    public PriceWidget(Position selfPosition, Size size, Component oldPrice, Component price) {
        super(selfPosition, size);
        this.oldPrice = normalize(oldPrice);
        this.price = normalize(price);
        resizeToContentIfNeeded();
    }

    public PriceWidget setPrice(Component price) {
        this.price = normalize(price);
        invalidateLayout();
        resizeToContentIfNeeded();
        return this;
    }

    public PriceWidget setPriceText(@Nullable String price) {
        return setPrice(price == null ? Component.empty() : Component.literal(price));
    }

    public PriceWidget setNewPrice(Component price) {
        return setPrice(price);
    }

    public PriceWidget setNewPriceText(@Nullable String price) {
        return setPriceText(price);
    }

    public PriceWidget setOldPrice(@Nullable Component oldPrice) {
        this.oldPrice = normalize(oldPrice);
        invalidateLayout();
        resizeToContentIfNeeded();
        return this;
    }

    public PriceWidget setOldPriceText(@Nullable String oldPrice) {
        return setOldPrice(oldPrice == null ? Component.empty() : Component.literal(oldPrice));
    }

    public PriceWidget setPrices(@Nullable Component oldPrice, Component price) {
        this.oldPrice = normalize(oldPrice);
        this.price = normalize(price);
        invalidateLayout();
        resizeToContentIfNeeded();
        return this;
    }

    public PriceWidget setPriceTexts(@Nullable String oldPrice, @Nullable String price) {
        return setPrices(
                oldPrice == null ? Component.empty() : Component.literal(oldPrice),
                price == null ? Component.empty() : Component.literal(price)
        );
    }

    public Component getPrice() {
        return price;
    }

    public Component getNewPrice() {
        return price;
    }

    public Component getOldPrice() {
        return oldPrice;
    }

    public PriceWidget setPriceColor(int priceColor) {
        this.priceColor = priceColor;
        return this;
    }

    public PriceWidget setNewPriceColor(int priceColor) {
        return setPriceColor(priceColor);
    }

    public PriceWidget setOldPriceColor(int oldPriceColor) {
        this.oldPriceColor = oldPriceColor;
        return this;
    }

    public PriceWidget setStrikeColor(int strikeColor) {
        this.strikeColor = strikeColor;
        return this;
    }

    public PriceWidget setColors(int oldPriceColor, int priceColor) {
        this.oldPriceColor = oldPriceColor;
        this.priceColor = priceColor;
        return this;
    }

    public PriceWidget setColors(int oldPriceColor, int priceColor, int strikeColor) {
        this.oldPriceColor = oldPriceColor;
        this.priceColor = priceColor;
        this.strikeColor = strikeColor;
        return this;
    }

    public PriceWidget setShadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    public PriceWidget setScale(float scale) {
        this.scale = Math.max(0.01f, scale);
        invalidateLayout();
        resizeToContentIfNeeded();
        return this;
    }

    public PriceWidget scale(float scale) {
        return setScale(scale);
    }

    public float getScale() {
        return scale;
    }

    public PriceWidget setPriceScale(float priceScale) {
        this.priceScale = Math.max(0.01f, priceScale);
        invalidateLayout();
        resizeToContentIfNeeded();
        return this;
    }

    public PriceWidget setNewPriceScale(float priceScale) {
        return setPriceScale(priceScale);
    }

    public PriceWidget setOldPriceScale(float oldPriceScale) {
        this.oldPriceScale = Math.max(0.01f, oldPriceScale);
        invalidateLayout();
        resizeToContentIfNeeded();
        return this;
    }

    public PriceWidget setGap(int gap) {
        this.gap = Math.max(0, gap);
        invalidateLayout();
        resizeToContentIfNeeded();
        return this;
    }

    public PriceWidget setPadding(int padding) {
        return setPadding(padding, padding, padding, padding);
    }

    public PriceWidget setPadding(int horizontal, int vertical) {
        return setPadding(horizontal, vertical, horizontal, vertical);
    }

    public PriceWidget setPadding(int left, int top, int right, int bottom) {
        this.paddingLeft = Math.max(0, left);
        this.paddingTop = Math.max(0, top);
        this.paddingRight = Math.max(0, right);
        this.paddingBottom = Math.max(0, bottom);
        resizeToContentIfNeeded();
        return this;
    }

    public PriceWidget setAutoSize(boolean autoSize) {
        this.autoSize = autoSize;
        resizeToContentIfNeeded();
        return this;
    }

    public PriceWidget autoSize() {
        return setAutoSize(true);
    }

    public PriceWidget fixedSize() {
        return setAutoSize(false);
    }

    public boolean isAutoSize() {
        return autoSize;
    }

    public PriceWidget setShowOldPrice(boolean showOldPrice) {
        this.showOldPrice = showOldPrice;
        invalidateLayout();
        resizeToContentIfNeeded();
        return this;
    }

    public PriceWidget showOldPrice() {
        return setShowOldPrice(true);
    }

    public PriceWidget hideOldPrice() {
        return setShowOldPrice(false);
    }

    public PriceWidget setStrikeOldPrice(boolean strikeOldPrice) {
        this.strikeOldPrice = strikeOldPrice;
        return this;
    }

    public PriceWidget strikeOldPrice() {
        return setStrikeOldPrice(true);
    }

    public PriceWidget noStrikeOldPrice() {
        return setStrikeOldPrice(false);
    }

    public PriceWidget setStrikeThickness(float strikeThickness) {
        this.strikeThickness = Math.max(0.1f, strikeThickness);
        return this;
    }

    public PriceWidget setStrikeYRatio(float strikeYRatio) {
        this.strikeYRatio = Math.max(0.0f, Math.min(1.0f, strikeYRatio));
        return this;
    }

    public PriceWidget setStrikeYOffset(float strikeYOffset) {
        this.strikeYOffset = strikeYOffset;
        return this;
    }

    public PriceWidget setHorizontalAlignment(TextLabel.HorizontalAlignment horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment == null ? TextLabel.HorizontalAlignment.LEFT : horizontalAlignment;
        return this;
    }

    public PriceWidget setVerticalAlignment(TextLabel.VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment == null ? TextLabel.VerticalAlignment.CENTER : verticalAlignment;
        return this;
    }

    public PriceWidget setAlignment(TextLabel.HorizontalAlignment horizontalAlignment, TextLabel.VerticalAlignment verticalAlignment) {
        return setHorizontalAlignment(horizontalAlignment).setVerticalAlignment(verticalAlignment);
    }

    public PriceWidget alignLeft() {
        return setHorizontalAlignment(TextLabel.HorizontalAlignment.LEFT);
    }

    public PriceWidget alignCenter() {
        return setHorizontalAlignment(TextLabel.HorizontalAlignment.CENTER);
    }

    public PriceWidget alignRight() {
        return setHorizontalAlignment(TextLabel.HorizontalAlignment.RIGHT);
    }

    public PriceWidget alignTop() {
        return setVerticalAlignment(TextLabel.VerticalAlignment.TOP);
    }

    public PriceWidget alignMiddle() {
        return setVerticalAlignment(TextLabel.VerticalAlignment.CENTER);
    }

    public PriceWidget alignBottom() {
        return setVerticalAlignment(TextLabel.VerticalAlignment.BOTTOM);
    }

    public int getContentWidthWithScale() {
        return getLayout(Minecraft.getInstance().font, 1.0f).width;
    }

    public int getContentHeightWithScale() {
        return getLayout(Minecraft.getInstance().font, 1.0f).height;
    }

    @Override
    public void setSize(Size size) {
        super.setSize(size);
        resizingToContent = false;
    }

    @Override
    public void drawInBackground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
        drawPrice(graphics, getPositionX(), getPositionY(), getSizeWidth(), getSizeHeight(), 1.0f);
    }

    public void drawInBackgroundAt(@NonNull GuiGraphics graphics, int x, int y, int width, int height, float externalScale) {
        drawPrice(graphics, x, y, width, height, externalScale);
    }

    private void drawPrice(GuiGraphics graphics, int x, int y, int width, int height, float externalScale) {
        Font font = Minecraft.getInstance().font;
        PriceLayout layout = getLayout(font, externalScale);
        if (layout.width <= 0 || layout.height <= 0) return;

        float safeScale = Math.max(0.01f, externalScale);
        int contentX = x + Math.round(paddingLeft * safeScale);
        int contentY = y + Math.round(paddingTop * safeScale);
        int contentWidth = Math.max(0, width - Math.round((paddingLeft + paddingRight) * safeScale));
        int contentHeight = Math.max(0, height - Math.round((paddingTop + paddingBottom) * safeScale));
        if (contentWidth <= 0 || contentHeight <= 0) return;

        int drawX = switch (horizontalAlignment) {
            case CENTER -> contentX + Math.max(0, (contentWidth - layout.width) / 2);
            case RIGHT -> contentX + Math.max(0, contentWidth - layout.width);
            case LEFT -> contentX;
        };
        int drawY = switch (verticalAlignment) {
            case TOP -> contentY;
            case CENTER -> contentY + Math.max(0, (contentHeight - layout.height) / 2);
            case BOTTOM -> contentY + Math.max(0, contentHeight - layout.height);
        };

        graphics.enableScissor(contentX, contentY, contentX + contentWidth, contentY + contentHeight);
        try {
            if (layout.hasOldPrice) {
                int oldY = drawY + Math.max(0, (layout.height - layout.oldHeight) / 2);
                drawScaledText(graphics, font, oldPrice, drawX, oldY, layout.oldScale, oldPriceColor);

                if (strikeOldPrice && strikeColor != 0) {
                    int strikeY = oldY + Math.round(font.lineHeight * layout.oldScale * strikeYRatio + strikeYOffset * scale * safeScale);
                    float thickness = Math.max(0.1f, strikeThickness * scale * safeScale);
                    drawStrikeLine(graphics, drawX, strikeY, layout.oldWidth, thickness);
                }
            }

            int priceX = drawX + (layout.hasOldPrice ? layout.oldWidth + layout.gap : 0);
            int priceY = drawY + Math.max(0, (layout.height - layout.priceHeight) / 2);
            drawScaledText(graphics, font, price, priceX, priceY, layout.priceScale, priceColor);
        } finally {
            graphics.disableScissor();
        }
    }

    private void drawScaledText(GuiGraphics graphics, Font font, Component text, int x, int y, float textScale, int color) {
        if (text == null || text.getString().isEmpty() || color == 0) return;

        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        poseStack.scale(textScale, textScale, 1.0f);
        graphics.drawString(font, text.getVisualOrderText(), 0, 0, color, shadow);
        poseStack.popPose();
    }

    private void drawStrikeLine(GuiGraphics graphics, int x, int y, int width, float thickness) {
        if (width <= 0 || thickness <= 0 || strikeColor == 0) return;

        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        poseStack.scale(1.0f, thickness, 1.0f);
        graphics.fill(0, 0, width, 1, strikeColor);
        poseStack.popPose();
    }

    private PriceLayout getLayout(Font font, float externalScale) {
        float safeScale = Math.max(0.01f, externalScale);
        if (layoutDirty || Float.compare(cachedLayoutExternalScale, safeScale) != 0) {
            rebuildLayout(font, safeScale);
        }

        return layoutCache;
    }

    private void rebuildLayout(Font font, float safeScale) {
        boolean hasOldPrice = hasOldPrice();
        float oldScale = getEffectiveOldPriceScale() * safeScale;
        float newScale = getEffectivePriceScale() * safeScale;

        int oldWidth = hasOldPrice ? Math.round(font.width(oldPrice) * oldScale) : 0;
        int oldHeight = hasOldPrice ? Math.max(1, Math.round(font.lineHeight * oldScale)) : 0;
        int priceWidth = price.getString().isEmpty() ? 0 : Math.round(font.width(price) * newScale);
        int priceHeight = price.getString().isEmpty() ? 0 : Math.max(1, Math.round(font.lineHeight * newScale));
        int scaledGap = hasOldPrice && priceWidth > 0 ? Math.max(0, Math.round(gap * scale * safeScale)) : 0;

        int width = oldWidth + scaledGap + priceWidth;
        int height = Math.max(oldHeight, priceHeight);
        layoutCache.set(hasOldPrice, oldScale, newScale, oldWidth, oldHeight, priceWidth, priceHeight, scaledGap, width, height);
        cachedLayoutExternalScale = safeScale;
        layoutDirty = false;
    }

    private boolean hasOldPrice() {
        return showOldPrice && oldPrice != null && !oldPrice.getString().isEmpty();
    }

    private float getEffectiveOldPriceScale() {
        return Math.max(0.01f, scale * oldPriceScale);
    }

    private float getEffectivePriceScale() {
        return Math.max(0.01f, scale * priceScale);
    }

    private void resizeToContentIfNeeded() {
        if (!autoSize || resizingToContent) return;

        resizingToContent = true;
        PriceLayout layout = getLayout(Minecraft.getInstance().font, 1.0f);
        setSize(
                Math.max(1, layout.width + paddingLeft + paddingRight),
                Math.max(1, layout.height + paddingTop + paddingBottom)
        );
        resizingToContent = false;
    }

    private Component normalize(@Nullable Component component) {
        return component == null ? Component.empty() : component;
    }

    private void invalidateLayout() {
        layoutDirty = true;
    }

    private static class PriceLayout {
        private boolean hasOldPrice;
        private float oldScale;
        private float priceScale;
        private int oldWidth;
        private int oldHeight;
        private int priceWidth;
        private int priceHeight;
        private int gap;
        private int width;
        private int height;

        private void set(
                boolean hasOldPrice,
                float oldScale,
                float priceScale,
                int oldWidth,
                int oldHeight,
                int priceWidth,
                int priceHeight,
                int gap,
                int width,
                int height
        ) {
            this.hasOldPrice = hasOldPrice;
            this.oldScale = oldScale;
            this.priceScale = priceScale;
            this.oldWidth = oldWidth;
            this.oldHeight = oldHeight;
            this.priceWidth = priceWidth;
            this.priceHeight = priceHeight;
            this.gap = gap;
            this.width = width;
            this.height = height;
        }
    }
}
