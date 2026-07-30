package dev.sixik.sdmshop2.libs.shop.client.screens.widgets;

import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import net.minecraft.network.chat.Component;

public class ShopBadgeWidget extends WidgetGroup {

    private static final int DEFAULT_FILL_COLOR = 0xFFAA0000;
    private static final int DEFAULT_TEXT_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_RADIUS = 2;
    private static final int DEFAULT_TEXT_PADDING = 2;

    protected final ShopEmptyWidget backgroundWidget;
    protected final SDMTextLabel textLabel;

    protected int fillColor = DEFAULT_FILL_COLOR;
    protected int textColor = DEFAULT_TEXT_COLOR;
    protected int radius = DEFAULT_RADIUS;
    protected int textPadding = DEFAULT_TEXT_PADDING;

    private boolean recomputingLayout;

    public ShopBadgeWidget(Component text) {
        this(0, 0, 26, 10, text);
    }

    public ShopBadgeWidget(Position selfPosition, Size size, Component text) {
        super(selfPosition, size);
        backgroundWidget = new ShopEmptyWidget();
        textLabel = new SDMTextLabel(text);
        addWidget(backgroundWidget);
        addWidget(textLabel);
        applyBackground();
        configureTextLabel();
        recomputeLayout();
    }

    public ShopBadgeWidget(int x, int y, int width, int height, Component text) {
        super(x, y, width, height);
        backgroundWidget = new ShopEmptyWidget();
        textLabel = new SDMTextLabel(text);
        addWidget(backgroundWidget);
        addWidget(textLabel);
        applyBackground();
        configureTextLabel();
        recomputeLayout();
    }

    public ShopBadgeWidget setText(Component text) {
        textLabel.setText(text);
        recomputeLayout();
        return this;
    }

    public ShopBadgeWidget setFillColor(int fillColor) {
        this.fillColor = fillColor;
        applyBackground();
        return this;
    }

    public ShopBadgeWidget setTextColor(int textColor) {
        this.textColor = textColor;
        textLabel.color = textColor;
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
        recomputeLayout();
        return this;
    }

    public ShopEmptyWidget getBackgroundWidget() {
        return backgroundWidget;
    }

    public SDMTextLabel getTextLabel() {
        return textLabel;
    }

    @Override
    protected void recomputeLayout() {
        if (recomputingLayout) return;
        recomputingLayout = true;

        try {
            backgroundWidget.setSelfPosition(0, 0);
            backgroundWidget.setSize(getSizeWidth(), getSizeHeight());

            textLabel.setSelfPosition(0, 0);
            textLabel.setSize(getSizeWidth(), getSizeHeight());
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
            recomputeLayout();
        }
    }

    private void configureTextLabel() {
        textLabel.setScale(1.0f);
        textLabel.setAutoScale(true);
        textLabel.setPadding(textPadding);
        textLabel.color = textColor;
    }

    private void applyBackground() {
        backgroundWidget.setBackground(new ColorRectTexture(fillColor).setRadius(radius));
    }
}
