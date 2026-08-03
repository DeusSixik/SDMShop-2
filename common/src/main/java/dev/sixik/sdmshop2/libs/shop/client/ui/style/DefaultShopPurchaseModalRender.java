package dev.sixik.sdmshop2.libs.shop.client.ui.style;

import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.TransformTexture;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopEmptyWidget;
import dev.sixik.sdmshop2.libs.shop.client.ui.textures.ColorRectAndBorderTexture;
import dev.sixik.sdmshop2.libs.shop.client.ui.textures.PixelBevelTexture;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopPurchaseModalElement;
import dev.sixik.sdmshop2.libs.shop.components.api.CostComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.RewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponentCategory;
import dev.sixik.sdmshop2.libs.shop.components.utils.ShopComponentsUtils;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.InputTextBox;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.TextLabel;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.table.ScrollableInteractionTable;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class DefaultShopPurchaseModalRender implements WidgetRender {

    protected static final int PADDING = 6;
    protected static final int ROW_HEIGHT = 18;
    protected static final int BUTTON_HEIGHT = 20;
    protected static final int PREVIEW_SIZE = 86;
    protected static final int COST_ICON_SIZE = 14;

    @Nullable
    protected TextLabel limitLabel;
    @Nullable
    protected TextLabel totalLabel;
    @Nullable
    protected TextLabel statusLabel;
    @Nullable
    protected ButtonWidget minButton;
    @Nullable
    protected ButtonWidget minusButton;
    @Nullable
    protected ButtonWidget plusButton;
    @Nullable
    protected ButtonWidget maxButton;
    @Nullable
    protected ButtonWidget buyButton;
    @Nullable
    protected InputTextBox quantityInput;
    @Nullable
    protected WidgetGroup costsContainer;

    @Override
    public void constructor(WidgetContextRender ctx) {
        if (!(ctx instanceof ShopPurchaseModalElement modal)) {
            return;
        }

        modal.setTitle(Component.literal("Purchase"));
        modal.setPanelBackground(PixelBevelTexture.panel());
        modal.setCloseOnOutsideClick(true);
    }

    @Override
    public void addWidgets(WidgetContextRender ctx) {
        if (!(ctx instanceof ShopPurchaseModalElement modal)) {
            return;
        }

        clearWidgetRefs();

        int contentWidth = Math.max(1, modal.getContentWidth());

        addPreview(modal);
        addHeader(modal, contentWidth);
        addCostsBlock(modal, contentWidth);
        addActionBlock(modal, contentWidth);
    }

    @Override
    public void alightWidgets(WidgetContextRender ctx) {
        refreshState(ctx);
    }

    public void refreshState(WidgetContextRender ctx) {
        if (!(ctx instanceof ShopPurchaseModalElement modal)) {
            return;
        }

        refreshQuantityControls(modal);
        refreshCosts(modal);
        refreshTotal(modal);
        refreshActionState(modal);
    }

    protected void clearWidgetRefs() {
        limitLabel = null;
        totalLabel = null;
        statusLabel = null;
        minButton = null;
        minusButton = null;
        plusButton = null;
        maxButton = null;
        buyButton = null;
        quantityInput = null;
        costsContainer = null;
    }

    protected void addPreview(ShopPurchaseModalElement modal) {
        modal.addWidget(createOfferPreview(modal, PADDING, PADDING, PREVIEW_SIZE, PREVIEW_SIZE));
    }

    protected void addHeader(ShopPurchaseModalElement modal, int contentWidth) {
        int headerX = PADDING + PREVIEW_SIZE + PADDING;
        int headerWidth = contentWidth - PREVIEW_SIZE - PADDING * 3;

        TextLabel title = label(
                headerX,
                PADDING,
                headerWidth,
                18,
                Component.literal(ShopClientCache.describeEntity(modal.getOffer())),
                0xFFFFFFFF
        );
        title.setMaxLines(1).setOverflowMode(TextLabel.OverflowMode.ELLIPSIS);
        modal.addWidget(title);

        limitLabel = label(headerX, PADDING + 21, headerWidth, 16, Component.empty(), 0xFFAEB4C6);
        modal.addWidget(limitLabel);

        addQuantityControls(modal, headerX, PADDING + 42);
    }

    protected void addQuantityControls(ShopPurchaseModalElement modal, int x, int y) {
        minButton = button(x, y, 34, BUTTON_HEIGHT, Component.literal("Min"), ignored -> modal.setQuantity(1));
        minusButton = button(x + 38, y, 22, BUTTON_HEIGHT, Component.literal("-"), ignored -> modal.setQuantity(modal.getQuantity() - 1));

        quantityInput = new InputTextBox(x + 64, y, 42, BUTTON_HEIGHT, null, modal::setQuantityFromInput);
        styleQuantityInput(modal, quantityInput);

        plusButton = button(x + 110, y, 22, BUTTON_HEIGHT, Component.literal("+"), ignored -> modal.setQuantity(modal.getQuantity() + 1));
        maxButton = button(x + 136, y, 42, BUTTON_HEIGHT, Component.literal("Max"), ignored -> modal.setQuantity(modal.getMaxQuantity()));

        modal.addWidget(minButton);
        modal.addWidget(minusButton);
        modal.addWidget(quantityInput);
        modal.addWidget(plusButton);
        modal.addWidget(maxButton);
    }

    protected void addCostsBlock(ShopPurchaseModalElement modal, int contentWidth) {
        costsContainer = new WidgetGroup(
                PADDING,
                PADDING + PREVIEW_SIZE + PADDING,
                contentWidth - PADDING * 2,
                86
        );
        costsContainer.setBackground(new ColorRectAndBorderTexture(0xFF171B28, 0xFF3D4458, 1).setRadius(3));
        modal.addWidget(costsContainer);

        totalLabel = label(
                PADDING,
                costsContainer.getSelfPositionY() + costsContainer.getSizeHeight() + 4,
                contentWidth - PADDING * 2,
                18,
                Component.empty(),
                0xFFFFFFFF
        );
        totalLabel.setBackground(new ColorRectAndBorderTexture(0xFF1B1E2A, 0xFF3D4458, 1).setRadius(3));
        totalLabel.setPadding(5, 0);
        modal.addWidget(totalLabel);
    }

    protected void addActionBlock(ShopPurchaseModalElement modal, int contentWidth) {
        int buttonY = modal.getContentHeight() - BUTTON_HEIGHT + 4;
        int buttonWidth = contentWidth - PADDING * 2;

        statusLabel = label(PADDING, buttonY, buttonWidth, BUTTON_HEIGHT, Component.empty(), 0xFFFFD77A);
        statusLabel.setMaxLines(1).setOverflowMode(TextLabel.OverflowMode.ELLIPSIS);
        statusLabel.setPadding(5, 0);
        modal.addWidget(statusLabel);

        buyButton = button(
                PADDING,
                buttonY,
                buttonWidth,
                BUTTON_HEIGHT,
                Component.translatable("shop.ui.offer_element.button.buy"),
                ignored -> modal.confirmPurchase()
        );
        buyButton.setButtonTexture(new PixelBevelTexture(
                PixelBevelTexture.PANEL_COLOR,
                PixelBevelTexture.ACCENT_LOW_COLOR,
                PixelBevelTexture.ACCENT_HIGH_COLOR,
                0.7f
        ));
        buyButton.setHoverTexture(new PixelBevelTexture(
                0xFF111624,
                PixelBevelTexture.ACCENT_LOW_COLOR,
                PixelBevelTexture.ACCENT_HIGH_COLOR,
                0.7f
        ));
        buyButton.setClickedTexture(PixelBevelTexture.accent().pressed());
        modal.addWidget(buyButton);
    }

    protected void refreshQuantityControls(ShopPurchaseModalElement modal) {
        int maxQuantity = Math.max(0, modal.getMaxQuantity());
        int minQuantity = maxQuantity <= 0 ? 0 : 1;
        int quantity = modal.getQuantity();
        boolean editable = !modal.isPurchasing() && maxQuantity > 0;

        if (quantityInput != null) {
            quantityInput.setNumbersOnly(minQuantity, maxQuantity);
            quantityInput.setCurrentStringSilently(String.valueOf(quantity));
            quantityInput.setActive(editable);
        }

        if (limitLabel != null) {
            limitLabel.setText(Component.literal(modal.getAvailabilityText()));
        }

        if (minButton != null) {
            minButton.setActive(editable && quantity > 1);
        }
        if (minusButton != null) {
            minusButton.setActive(editable && quantity > 1);
        }
        if (plusButton != null) {
            plusButton.setActive(editable && quantity < maxQuantity);
        }
        if (maxButton != null) {
            maxButton.setActive(editable && quantity < maxQuantity);
        }
    }

    protected void refreshCosts(ShopPurchaseModalElement modal) {
        if (costsContainer == null) {
            return;
        }

        costsContainer.clearAllWidgets();

        int width = Math.max(1, costsContainer.getSizeWidth());

        if (modal.getCosts().isEmpty()) {
            TextLabel free = label(4, 4, width - 8, ROW_HEIGHT, Component.literal("Free"), 0xFFFFFFFF);
            free.alignMiddle();
            costsContainer.addWidget(free);
            return;
        }

        int y = 4;
        for (CostComponent cost : modal.getCosts()) {
            if (y + ROW_HEIGHT > costsContainer.getSizeHeight()) {
                break;
            }

            Widget icon = createCostIcon(cost);
            if (icon != null) {
                icon.setSelfPosition(5, y + 2);
                icon.setSize(COST_ICON_SIZE, COST_ICON_SIZE);
                costsContainer.addWidget(icon);
            }

            TextLabel label = label(24, y, width - 28, ROW_HEIGHT, modal.getCostLine(cost), modal.getCostLineColor(cost));
            label.setMaxLines(1).setOverflowMode(TextLabel.OverflowMode.ELLIPSIS).alignMiddle();
            costsContainer.addWidget(label);

            y += ROW_HEIGHT + 2;
        }
    }

    protected void refreshTotal(ShopPurchaseModalElement modal) {
        if (totalLabel != null) {
            totalLabel.setText(Component.literal("Total: " + modal.getTotalText()));
        }
    }

    protected void refreshActionState(ShopPurchaseModalElement modal) {
        Component status = modal.getStatusText();
        boolean hasStatus = !status.getString().isEmpty();

        if (statusLabel != null) {
            statusLabel.setText(status);
            statusLabel.setVisible(hasStatus);
            statusLabel.setActive(hasStatus);
        }

        if (buyButton != null) {
            boolean canBuy = !hasStatus
                    && modal.getQuantity() > 0
                    && modal.getQuantity() <= modal.getMaxQuantity();

            buyButton.setText(Component.translatable("shop.ui.offer_element.button.buy"));
            buyButton.setVisible(canBuy);
            buyButton.setActive(canBuy);
        }
    }

    protected WidgetGroup createOfferPreview(ShopPurchaseModalElement modal, int x, int y, int width, int height) {
        WidgetGroup preview = new WidgetGroup(x, y, width, height);
        preview.setBackground(PixelBevelTexture.panel());
        preview.setClientSideWidget();

        ObjectList<ShopComponent> rewardComponents = ShopComponentsUtils
                .getComponentsByCategory(modal.getOffer())
                .get(ShopComponentCategory.REWARD);

        if (rewardComponents == null || rewardComponents.isEmpty()) {
            TextLabel empty = label(4, 4, width - 8, height - 8, Component.literal("No reward"), 0xFFAEB4C6);
            empty.setAlignment(TextLabel.HorizontalAlignment.CENTER, TextLabel.VerticalAlignment.CENTER);
            preview.addWidget(empty);
            return preview;
        }

        if (rewardComponents.size() == 1) {
            addSingleRewardPreview(preview, (RewardComponent) rewardComponents.get(0), width, height);
            return preview;
        }

        addRewardTablePreview(preview, rewardComponents, width, height);
        return preview;
    }

    protected void addSingleRewardPreview(WidgetGroup preview, RewardComponent reward, int width, int height) {
        Widget widget = reward.createRender();
        if (widget == null) {
            return;
        }

        int iconSize = Math.max(1, Math.min(width, height) - 8);

        widget.setSelfPosition((width - iconSize) / 2, (height - iconSize) / 2);
        widget.setSize(iconSize, iconSize);
        widget.setBackground(new GuiTextureGroup(PixelBevelTexture.panel(), widget.getBackgroundTexture()));
        widget.setHoverTexture(new ColorBorderTexture(1, 0xFFFFFFFF));

        preview.addWidget(widget);
    }

    protected void addRewardTablePreview(
            WidgetGroup preview,
            ObjectList<ShopComponent> rewardComponents,
            int width,
            int height
    ) {
        ScrollableInteractionTable table = (ScrollableInteractionTable) new ScrollableInteractionTable()
                .setMaxRows(2)
                .setWheelRows(1)
                .reserveScrollBarSpace()
                .setBackground(PixelBevelTexture.panel());

        int tableWidth = Math.max(1, width - 6);
        int cellSize = Math.max(12, Math.min(26, tableWidth / 3));

        table.setSelfPosition(3, 3);
        table.setCellSize(cellSize, cellSize);
        table.setColumnsToFitWidth(tableWidth);
        table.setSize(tableWidth, Math.max(1, height - 6));

        for (ShopComponent component : rewardComponents) {
            Widget widget = ((RewardComponent) component).createRender();
            if (widget == null) {
                continue;
            }

            widget.setSize(cellSize, cellSize);
            widget.setHoverTexture(new ColorBorderTexture(1, 0xFFFFFFFF));
            table.addElement(widget);
        }

        preview.addWidget(table);
    }

    protected @Nullable Widget createCostIcon(CostComponent cost) {
        TransformTexture texture = cost.getRenderIcon();
        if (texture == null) {
            return null;
        }

        ShopEmptyWidget icon = new ShopEmptyWidget();
        icon.setBackground(texture);
        icon.setHoverTexture(new ColorBorderTexture(1, 0xFFFFFFFF));

        return icon;
    }

    protected void styleQuantityInput(ShopPurchaseModalElement modal, InputTextBox input) {
        input.setClientSideWidget();
        input.setNumbersOnly(modal.getMaxQuantity() <= 0 ? 0 : 1, Math.max(0, modal.getMaxQuantity()));
        input.setCurrentStringSilently(String.valueOf(modal.getQuantity()));
        input.setMaxLength(6);
        input.setPadding(4, 0);
        input.setTextColor(0xFFFFFFFF);
        input.setPlaceholder(Component.literal("1"));
        input.setBackground(new ColorRectAndBorderTexture(0xFF101016, 0xFF5C637A, 1).setRadius(2));
        input.setFocusedOutline(0xFF101016, 0xFFFFFFFF);
    }

    protected TextLabel label(int x, int y, int width, int height, Component text, int color) {
        return new TextLabel(x, y, Math.max(1, width), Math.max(1, height), text)
                .setAutoSize(false)
                .setColor(color)
                .setPadding(0)
                .setMaxLines(1)
                .setOverflowMode(TextLabel.OverflowMode.CLIP)
                .setAlignment(TextLabel.HorizontalAlignment.LEFT, TextLabel.VerticalAlignment.CENTER);
    }

    protected ButtonWidget button(
            int x,
            int y,
            int width,
            int height,
            Component text,
            java.util.function.Consumer<ClickData> onPress
    ) {
        ButtonWidget button = new ButtonWidget(
                x,
                y,
                Math.max(1, width),
                Math.max(1, height),
                text,
                ignored -> onPress.accept(ignored)
        );

        button.setClientSideWidget();
        button.setButtonTexture(PixelBevelTexture.panel());
        button.setHoverTexture(new PixelBevelTexture(
                0xFF111624,
                PixelBevelTexture.ACCENT_LOW_COLOR,
                PixelBevelTexture.ACCENT_HIGH_COLOR,
                0.7f
        ));
        button.setClickedTexture(PixelBevelTexture.panel().pressed());
        button.setTextColor(0xFFFFFFFF);
        button.setDisabledTextColor(0xFF8A8A8A);
        button.setTextPadding(3);
        button.setMinTextScale(0.35f);

        return button;
    }
}

