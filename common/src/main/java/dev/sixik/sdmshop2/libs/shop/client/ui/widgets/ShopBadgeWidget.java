package dev.sixik.sdmshop2.libs.shop.client.ui.widgets;

import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.TextLabel;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ShopBadgeWidget extends WidgetGroup {

    private static final int DEFAULT_FILL_COLOR = 0xFFAA0000;
    private static final int DEFAULT_TEXT_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_RADIUS = 2;
    private static final int DEFAULT_TEXT_PADDING = 2;
    private static final int DEFAULT_CONTENT_GAP = 2;
    private static final int DEFAULT_LEADING_SIZE = 8;

    protected final ShopEmptyWidget backgroundWidget;
    protected final TextLabel textLabel;
    protected Widget leadingWidget;
    protected Widget contentWidget;

    protected int fillColor = DEFAULT_FILL_COLOR;
    protected int textColor = DEFAULT_TEXT_COLOR;
    protected int radius = DEFAULT_RADIUS;
    protected int textPadding = DEFAULT_TEXT_PADDING;
    protected int contentGap = DEFAULT_CONTENT_GAP;
    protected float scale = 1.0f;
    protected boolean autoSizeToContent;
    protected int leadingBaseWidth = DEFAULT_LEADING_SIZE;
    protected int leadingBaseHeight = DEFAULT_LEADING_SIZE;
    protected int contentBaseWidth;
    protected int contentBaseHeight;

    private boolean recomputingLayout;

    public ShopBadgeWidget(Component text) {
        this(0, 0, 26, 10, text);
    }

    public ShopBadgeWidget(Position selfPosition, Size size, Component text) {
        super(selfPosition, size);
        backgroundWidget = new ShopEmptyWidget();
        textLabel = new TextLabel(text);
        contentWidget = textLabel;
        super.addWidget(backgroundWidget);
        super.addWidget(textLabel);
        applyBackground();
        configureTextLabel();
        recomputeLayout();
    }

    public ShopBadgeWidget(int x, int y, int width, int height, Component text) {
        super(x, y, width, height);
        backgroundWidget = new ShopEmptyWidget();
        textLabel = new TextLabel(text);
        contentWidget = textLabel;
        super.addWidget(backgroundWidget);
        super.addWidget(textLabel);
        applyBackground();
        configureTextLabel();
        recomputeLayout();
    }

    public ShopBadgeWidget setText(Component text) {
        if (contentWidget != textLabel) {
            setContentWidget(textLabel);
        }
        textLabel.setText(text);
        updateSizeAndLayout();
        return this;
    }

    public ShopBadgeWidget setFillColor(int fillColor) {
        this.fillColor = fillColor;
        applyBackground();
        return this;
    }

    public ShopBadgeWidget setTextColor(int textColor) {
        this.textColor = textColor;
        textLabel.setColor(textColor);
        return this;
    }

    public ShopBadgeWidget setRadius(int radius) {
        this.radius = Math.max(0, radius);
        applyBackground();
        return this;
    }

    public ShopBadgeWidget setTextPadding(int textPadding) {
        this.textPadding = Math.max(0, textPadding);
        configureTextLabel();
        updateSizeAndLayout();
        return this;
    }

    public ShopBadgeWidget setContentGap(int contentGap) {
        this.contentGap = Math.max(0, contentGap);
        updateSizeAndLayout();
        return this;
    }

    public ShopBadgeWidget setScale(float scale) {
        this.scale = Math.max(0.01f, scale);
        configureTextLabel();
        updateSizeAndLayout();
        return this;
    }

    public float getScale() {
        return scale;
    }

    public ShopBadgeWidget setAutoSizeToContent(boolean autoSizeToContent) {
        this.autoSizeToContent = autoSizeToContent;
        setDynamicSized(autoSizeToContent);
        updateSizeAndLayout();
        return this;
    }

    public ShopBadgeWidget autoSizeToContent() {
        return setAutoSizeToContent(true);
    }

    public ShopBadgeWidget fixedSize() {
        return setAutoSizeToContent(false);
    }

    public ShopBadgeWidget setLeadingWidget(@Nullable Widget widget) {
        if (leadingWidget == widget) {
            updateSizeAndLayout();
            return this;
        }

        if (leadingWidget != null) {
            super.removeWidget(leadingWidget);
        }

        leadingWidget = widget;
        if (leadingWidget != null) {
            leadingBaseWidth = Math.max(0, leadingWidget.getSizeWidth());
            leadingBaseHeight = Math.max(0, leadingWidget.getSizeHeight());
            super.addWidget(leadingWidget);
        }

        updateSizeAndLayout();
        return this;
    }

    public ShopBadgeWidget clearLeadingWidget() {
        return setLeadingWidget(null);
    }

    public ShopBadgeWidget setLeadingTexture(IGuiTexture texture) {
        return setLeadingTexture(texture, DEFAULT_LEADING_SIZE, DEFAULT_LEADING_SIZE);
    }

    public ShopBadgeWidget setLeadingTexture(IGuiTexture texture, int width, int height) {
        Widget widget = new Widget(0, 0, Math.max(0, width), Math.max(0, height));
        widget.setBackground(texture);
        return setLeadingWidget(widget);
    }

    public ShopBadgeWidget setLeadingImage(IGuiTexture texture) {
        return setLeadingTexture(texture);
    }

    public ShopBadgeWidget setLeadingImage(IGuiTexture texture, int width, int height) {
        return setLeadingTexture(texture, width, height);
    }

    public ShopBadgeWidget setLeadingItem(ItemStack itemStack) {
        return setLeadingItem(itemStack, DEFAULT_LEADING_SIZE);
    }

    public ShopBadgeWidget setLeadingItem(ItemStack itemStack, int size) {
        return setLeadingTexture(new ItemStackTexture(itemStack), size, size);
    }

    public ShopBadgeWidget setContentWidget(@Nullable Widget widget) {
        Widget next = widget == null ? textLabel : widget;
        if (contentWidget == next) {
            updateContentBaseSize();
            updateSizeAndLayout();
            return this;
        }

        if (contentWidget != null) {
            super.removeWidget(contentWidget);
        }

        contentWidget = next;
        updateContentBaseSize();
        super.addWidget(contentWidget);
        configureTextLabel();
        updateSizeAndLayout();
        return this;
    }

    public ShopEmptyWidget getBackgroundWidget() {
        return backgroundWidget;
    }

    public TextLabel getTextLabel() {
        return textLabel;
    }

    @Nullable
    public Widget getLeadingWidget() {
        return leadingWidget;
    }

    public Widget getContentWidget() {
        return contentWidget;
    }

    @Override
    protected Size computeDynamicSize() {
        return computeContentSize();
    }

    @Override
    protected void recomputeLayout() {
        if (recomputingLayout) return;
        recomputingLayout = true;

        try {
            backgroundWidget.setSelfPosition(0, 0);
            backgroundWidget.setSize(getSizeWidth(), getSizeHeight());

            int padding = getScaledTextPadding();
            int gap = getScaledContentGap();
            int contentHeight = Math.max(0, getSizeHeight() - padding * 2);
            int x = padding;

            if (leadingWidget != null) {
                int leadingWidth = getScaledLeadingWidth();
                int leadingHeight = getScaledLeadingHeight();
                leadingWidget.setSelfPosition(x, padding + Math.max(0, (contentHeight - leadingHeight) / 2));
                leadingWidget.setSize(leadingWidth, leadingHeight);
                x += leadingWidth + gap;
            }

            if (contentWidget != null) {
                int availableWidth = Math.max(0, getSizeWidth() - x - padding);
                int contentWidth = autoSizeToContent ? getScaledContentWidth() : availableWidth;
                int labelHeight = autoSizeToContent ? getScaledContentHeight() : contentHeight;

                contentWidget.setSelfPosition(x, padding + Math.max(0, (contentHeight - labelHeight) / 2));
                contentWidget.setSize(contentWidth, labelHeight);
            }
        } finally {
            recomputingLayout = false;
        }
    }

    @Override
    protected void onSizeUpdate() {
        recomputeLayout();
    }

    @Override
    protected void onChildSelfPositionUpdate(Widget child) {
        if (!recomputingLayout) {
            recomputeLayout();
        }
    }

    @Override
    protected void onChildSizeUpdate(Widget child) {
        if (!recomputingLayout) {
            if (child == leadingWidget) {
                leadingBaseWidth = Math.max(0, leadingWidget.getSizeWidth());
                leadingBaseHeight = Math.max(0, leadingWidget.getSizeHeight());
            } else if (child == contentWidget && child != textLabel) {
                updateContentBaseSize();
            }
            updateSizeAndLayout();
        }
    }

    private void configureTextLabel() {
        textLabel.setPadding(0);
        textLabel.setScale(scale);
        textLabel.setAutoSize(false);
        textLabel.scaleToFit();
        textLabel.setColor(textColor);
    }

    private void applyBackground() {
        backgroundWidget.setBackground(new ColorRectTexture(fillColor).setRadius(radius));
    }

    private void updateSizeAndLayout() {
        if (autoSizeToContent) {
            recomputeSize();
        } else {
            recomputeLayout();
        }
    }

    private void updateContentBaseSize() {
        if (contentWidget != null && contentWidget != textLabel) {
            contentBaseWidth = Math.max(0, contentWidget.getSizeWidth());
            contentBaseHeight = Math.max(0, contentWidget.getSizeHeight());
        }
    }

    private Size computeContentSize() {
        int padding = getScaledTextPadding();
        int width = padding * 2;
        int height = padding * 2 + getScaledContentHeight();

        if (leadingWidget != null) {
            width += getScaledLeadingWidth() + getScaledContentGap();
            height = Math.max(height, padding * 2 + getScaledLeadingHeight());
        }

        width += getScaledContentWidth();
        return new Size(Math.max(1, width), Math.max(1, height));
    }

    private int getScaledTextPadding() {
        return Math.max(0, Math.round(textPadding * scale));
    }

    private int getScaledContentGap() {
        return leadingWidget == null ? 0 : Math.max(0, Math.round(contentGap * scale));
    }

    private int getScaledLeadingWidth() {
        return leadingWidget == null ? 0 : Math.max(0, Math.round(leadingBaseWidth * scale));
    }

    private int getScaledLeadingHeight() {
        return leadingWidget == null ? 0 : Math.max(0, Math.round(leadingBaseHeight * scale));
    }

    private int getScaledContentWidth() {
        if (contentWidget == null) return 0;
        if (contentWidget == textLabel) {
            return Math.max(0, Math.round(Minecraft.getInstance().font.width(textLabel.getText()) * scale));
        }
        return Math.max(0, Math.round(contentBaseWidth * scale));
    }

    private int getScaledContentHeight() {
        if (contentWidget == null) return 0;
        if (contentWidget == textLabel) {
            return Math.max(0, Math.round(Minecraft.getInstance().font.lineHeight * scale));
        }
        return Math.max(0, Math.round(contentBaseHeight * scale));
    }
}
