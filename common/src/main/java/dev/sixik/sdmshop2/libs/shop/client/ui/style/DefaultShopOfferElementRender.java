package dev.sixik.sdmshop2.libs.shop.client.ui.style;

import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.TransformTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.platform.InputConstants;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.ShopBadgeHBoxWidget;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.ShopEmptyWidget;
import dev.sixik.sdmshop2.libs.shop.client.textures.PixelBevelTexture;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopIcons;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop.components.api.CostComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.RewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponentCategory;
import dev.sixik.sdmshop2.libs.shop.components.limiter.LimiterComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import dev.sixik.sdmshop2.libs.shop.components.utils.ShopComponentsUtils;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.PriceWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ProgressBarWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.TextLabel;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.HorizontalContainer;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.VerticalContainer;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.table.ScrollableInteractionTable;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DefaultShopOfferElementRender implements WidgetRender {

    protected static final int DEBUG_SIZE_ELEMENT = 8;
    protected static final int MAX_REWARD_CELL_SIZE = 18;
    protected static final int OFFER_ELEMENTS_TABLE_VISIBLE_ROWS = 2;
    protected static final int OFFER_ELEMENTS_TABLE_PADDING = 4;
    protected static final int OFFER_ELEMENTS_TABLE_TOP_GAP = 4;
    protected static final int CONTENT_PADDING = 4;
    protected static final int MONEY_TYPES_TOP_GAP = 2;
    protected static final int MAX_VISIBLE_MONEY_ROWS = 3;
    protected static final int LIMIT_BAR_HEIGHT = 14;

    protected static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    @Nullable
    protected TextLabel titleLabel;
    @Nullable
    protected VerticalContainer moneyTypesContainer;
    protected final List<HorizontalContainer> moneyTypeRows = new ArrayList<>();
    @Nullable
    protected ScrollableInteractionTable offerElementsTable;
    @Nullable
    protected Widget offerElement;
    @Nullable
    protected ProgressBarWidget limitBar;
    @Nullable
    protected ShopBadgeHBoxWidget badgesBox;
    @Nullable
    protected ButtonWidget favoriteButton;

    protected boolean oneOfferElement = false;
    protected int lastOfferElementWidth = -1;
    protected int lastOfferElementHeight = -1;

    public DefaultShopOfferElementRender() {
    }

    @Override
    public void constructor(WidgetContextRender ctx) {
        ctx.getOwner().setBackground(new PixelBevelTexture(
                PixelBevelTexture.PANEL_COLOR,
                PixelBevelTexture.LINE_LOW_COLOR,
                PixelBevelTexture.LINE_HIGH_COLOR,
                1.5f
        ));
    }

    @Override
    public void addWidgets(WidgetContextRender ctx) {
        clearWidgetsState();

        final ShopEntity entity = ctx.getShopEntity();
        if (!(entity instanceof ShopOffer shopEntity)) {
            return;
        }

        favoriteButton = new ButtonWidget();
        updateFavoriteButton(ShopClientCache.isFavorite(shopEntity.getUUID()));
        favoriteButton.setClientSideWidget();
        favoriteButton.setHoverTexture(new ColorBorderTexture(1, 0xFFFFFFFF));
        favoriteButton.setClickedTexture(new ColorBorderTexture(1, 0xFFFFD77A));
        favoriteButton.setOnClick(ignored -> updateFavoriteButton(ShopClientCache.toggleFavorite(shopEntity.getUUID())));
        favoriteButton.setSize(8, 8);
        ctx.addWidget(favoriteButton);
        ctx.listen(ShopUIEvents.FAVORITES_CHANGED, event -> {
            if (shopEntity.getUUID().equals(event.offerId())) {
                updateFavoriteButton(event.favorite());
            }
        });

        final Map<ShopComponentCategory, ObjectList<ShopComponent>> offerComponentsMap =
                ShopComponentsUtils.getComponentsByCategory(shopEntity);

        addNameLabel(ctx, shopEntity, offerComponentsMap);
        addOfferElementsTable(ctx, offerComponentsMap);
        addMoneyTypes(ctx, offerComponentsMap);
        addLimitBar(ctx, shopEntity);

    }

    @Override
    public void alightWidgets(WidgetContextRender ctx) {
        final Widget owner = ctx.getOwner();

        int contentPadding = CONTENT_PADDING;
        int currentY = CONTENT_PADDING;
        int contentWidth = Math.max(1, owner.getSizeWidth() - contentPadding * 2);

        if (badgesBox != null) {
            badgesBox.setSelfPositionY(-2);
            badgesBox.setSelfPositionX(contentPadding / 2);
            int badgesBoxWidth = Math.max(1, owner.getSizeWidth() - contentPadding);
            badgesBox.setMaxLength(badgesBoxWidth);
            badgesBox.setSize(badgesBoxWidth, 14);
            currentY = Math.max(currentY, badgesBox.getSelfPositionY() + badgesBox.getHeightWithScale() + OFFER_ELEMENTS_TABLE_TOP_GAP);
        }

        if (limitBar != null) {
            limitBar.setBarHeight(4);
            limitBar.setSelfPosition(contentPadding / 2, owner.getSize().height - LIMIT_BAR_HEIGHT);
            limitBar.setSize(owner.getSize().width - contentPadding, LIMIT_BAR_HEIGHT);
        }

        if (titleLabel != null) {
            titleLabel.setSelfPosition(0, currentY);
            titleLabel.setSize(owner.getSizeWidth(), Minecraft.getInstance().font.lineHeight);
            currentY = titleLabel.getSelfPositionY() + titleLabel.getSizeHeight() + OFFER_ELEMENTS_TABLE_TOP_GAP;
        }

        currentY = alightOfferElementsTable(owner, currentY, contentPadding, contentWidth);

        if (moneyTypesContainer != null) {
            int moneyTypesVisualWidth = Math.max(1, owner.getSizeWidth() - contentPadding);
            int moneyTypesLogicalWidth = Math.max(1, Math.round(moneyTypesVisualWidth / moneyTypesContainer.getScale()));
            int moneyTypesY = currentY + MONEY_TYPES_TOP_GAP;
            int availableBottom = limitBar == null
                    ? owner.getSizeHeight() - contentPadding
                    : limitBar.getSelfPositionY() - contentPadding;
            int maxMoneyTypesVisualHeight = getVisibleMoneyRows() * getMoneyRowHeight();
            int moneyTypesVisualHeight = Math.max(1, Math.min(maxMoneyTypesVisualHeight, availableBottom - moneyTypesY));
            int moneyTypesLogicalHeight = Math.max(1, Math.round(moneyTypesVisualHeight / moneyTypesContainer.getScale()));

            moneyTypesContainer.setSize(moneyTypesLogicalWidth, moneyTypesLogicalHeight);
            moneyTypesContainer.setSelfPosition(contentPadding / 2, moneyTypesY);
            for (HorizontalContainer row : moneyTypeRows) {
                row.setSize(moneyTypesLogicalWidth, Math.max(getMoneyRowHeight(), row.getSizeHeight()));
            }
        }

        if (favoriteButton != null) {
            favoriteButton.setSelfPosition(owner.getSizeWidth() - favoriteButton.getSizeWidth() / 2, -favoriteButton.getSizeHeight() / 2);
        }

    }

    @Override
    public Size getMinimumSize(WidgetContextRender ctx) {
        return new Size(118, 96);
    }

    @Override
    public Size getPreferredSize(WidgetContextRender ctx) {
        return new Size(160, getPreferredHeight(ctx, 160));
    }

    @Override
    public Size getMaximumSize(WidgetContextRender ctx) {
        return new Size(220, 220);
    }

    @Override
    public int getPreferredHeight(WidgetContextRender ctx, int width) {
        Size min = getMinimumSize(ctx);
        Size max = getMaximumSize(ctx);
        int height = calculatePreferredContentHeight(width);
        return Math.max(min.height, Math.min(height, max.height));
    }

    protected int calculatePreferredContentHeight(int width) {
        int contentWidth = Math.max(1, width - CONTENT_PADDING * 2);
        int height = CONTENT_PADDING;

        if (badgesBox != null) {
            height = Math.max(height, -2 + 14 + OFFER_ELEMENTS_TABLE_TOP_GAP);
        }

        if (titleLabel != null) {
            height += Minecraft.getInstance().font.lineHeight + OFFER_ELEMENTS_TABLE_TOP_GAP;
        }

        if (offerElementsTable != null || offerElement != null) {
            height += getPreferredRewardBlockHeight(contentWidth);
        }

        if (moneyTypesContainer != null && !moneyTypeRows.isEmpty()) {
            height += MONEY_TYPES_TOP_GAP + getVisibleMoneyRows() * getMoneyRowHeight();
        }

        if (limitBar != null) {
            height += CONTENT_PADDING + LIMIT_BAR_HEIGHT;
        }

        return height + CONTENT_PADDING;
    }

    protected int getPreferredRewardBlockHeight(int contentWidth) {
        int cellSize = Math.max(DEBUG_SIZE_ELEMENT, Math.min(MAX_REWARD_CELL_SIZE, Math.max(1, contentWidth / 4)));
        return cellSize * OFFER_ELEMENTS_TABLE_VISIBLE_ROWS;
    }

    protected int getVisibleMoneyRows() {
        return Math.min(MAX_VISIBLE_MONEY_ROWS, Math.max(1, moneyTypeRows.size()));
    }

    protected int getMoneyRowHeight() {
        return Minecraft.getInstance().font.lineHeight + 7;
    }

    protected void clearWidgetsState() {
        if (offerElement != null) {
            lastOfferElementWidth = offerElement.getSizeWidth();
            lastOfferElementHeight = offerElement.getSizeHeight();
        }

        titleLabel = null;
        moneyTypesContainer = null;
        moneyTypeRows.clear();
        offerElementsTable = null;
        offerElement = null;
        limitBar = null;
        badgesBox = null;
        favoriteButton = null;
        oneOfferElement = false;
    }

    protected void updateFavoriteButton(boolean favorite) {
        if (favoriteButton != null) {
            favoriteButton.setBackground(favorite ? ShopIcons.STAR_FULL : ShopIcons.STAR_EMPTY);
        }
    }

    protected void addNameLabel(
            WidgetContextRender ctx,
            ShopOffer shopEntity,
            Map<ShopComponentCategory, ObjectList<ShopComponent>> offerComponentsMap
    ) {
        final Optional<NameComponent> nameComponentOpt = shopEntity.getComponent(NameComponent.class);
        if (nameComponentOpt.isPresent()) {
            final String name = nameComponentOpt.get().getName();
            titleLabel = createNameLabel(I18n.exists(name) ? Component.translatable(name) : Component.literal(name));
            ctx.addWidget(titleLabel);
            return;
        }

        final ObjectList<ShopComponent> rewardComponents = offerComponentsMap.get(ShopComponentCategory.REWARD);
        if (rewardComponents == null || rewardComponents.isEmpty()) {
            return;
        }

        final RewardComponent rewardComponent = (RewardComponent) rewardComponents.get(0);
        final Component display = rewardComponent.getDisplayTitle();
        if (display != null) {
            titleLabel = createNameLabel(display);
            ctx.addWidget(titleLabel);
        }
    }

    protected TextLabel createNameLabel(Component text) {
        return (TextLabel) new TextLabel(text)
                .setAutoSize(false)
                .setWrapText(false)
                .setMaxLines(1)
                .setLineSpacing(0)
                .setPadding(0)
                .scaleToFit()
                .setAlignment(TextLabel.HorizontalAlignment.CENTER, TextLabel.VerticalAlignment.CENTER);
    }

    protected void addOfferElementsTable(
            WidgetContextRender ctx,
            Map<ShopComponentCategory, ObjectList<ShopComponent>> offerComponentsMap
    ) {
        final ObjectList<ShopComponent> rewardComponents = offerComponentsMap.get(ShopComponentCategory.REWARD);
        if (rewardComponents == null || rewardComponents.isEmpty()) {
            return;
        }

        if (rewardComponents.size() == 1) {
            oneOfferElement = true;
            addOfferElement(ctx, (RewardComponent) rewardComponents.get(0));
            return;
        }

        offerElementsTable = (ScrollableInteractionTable) new ScrollableInteractionTable()
                .setMaxRows(OFFER_ELEMENTS_TABLE_VISIBLE_ROWS)
                .setWheelRows(1)
                .reserveScrollBarSpace()
                .setBackground(PixelBevelTexture.panel());
        offerElementsTable.setBackground(PixelBevelTexture.panel());
        ctx.addWidget(offerElementsTable);

        for (final ShopComponent rewardComponent : rewardComponents) {
            final @Nullable Widget widget = rewardComponent.createRender();
            if (widget == null) {
                continue;
            }

            widget.setSize(DEBUG_SIZE_ELEMENT, DEBUG_SIZE_ELEMENT);
            widget.setHoverTexture(new ColorBorderTexture(1, 0xFFFFFFFF));
            offerElementsTable.addElement(widget);
        }
    }

    protected void addOfferElement(
            WidgetContextRender ctx,
            RewardComponent component
    ) {
        if (component == null)
            return;

        final Widget widget = component.createRender();
        if (widget == null)
            return;

        if (lastOfferElementWidth > 0 && lastOfferElementHeight > 0) {
            widget.setSize(lastOfferElementWidth, lastOfferElementHeight);
        } else {
            final int size_w = ctx.getOwner().getSizeWidth();
            final int icon_size = Math.max(size_w / 2, DEBUG_SIZE_ELEMENT);
            widget.setSize(icon_size, icon_size);
        }
        widget.setBackground(new GuiTextureGroup(PixelBevelTexture.panel(), widget.getBackgroundTexture()));
        widget.setHoverTexture(new ColorBorderTexture(1, 0xFFFFFFFF));
        ctx.addWidget(offerElement = widget);
    }

    protected void addMoneyTypes(
            WidgetContextRender ctx,
            Map<ShopComponentCategory, ObjectList<ShopComponent>> offerComponentsMap
    ) {
        if (!offerComponentsMap.containsKey(ShopComponentCategory.COST)) {
            return;
        }

        final Map<String, List<CostComponent>> costComponents = ShopComponentsUtils.getCostComponentsByGroups(offerComponentsMap);

        moneyTypesContainer = new VerticalContainer();
        moneyTypesContainer.setDynamicSized(false);
        moneyTypesContainer.setSpacing(0);
        ctx.addWidget(moneyTypesContainer);


        final int test_size = Minecraft.getInstance().font.lineHeight + 3;
        for (Map.Entry<String, List<CostComponent>> entry : costComponents.entrySet()) {
            final List<CostComponent> entries = entry.getValue();
            final HorizontalContainer container = new HorizontalContainer();
            container.setBackground(PixelBevelTexture.panel());
            container.setPadding(4, 2).setSpacing(4);
            container.alignBottom();
            container.pushLastElementToEnd();

            for (CostComponent component : entries) {
                final @Nullable TransformTexture texture = component.getRenderIcon();

                if (texture != null) {
                    final ShopEmptyWidget icon = new ShopEmptyWidget();
                    icon.setSize(test_size, test_size);
                    icon.setBackground(texture);
                    container.addWidget(icon);
                }
                final PriceWidget widget = new PriceWidget()
                        .setPriceTexts(null, DECIMAL_FORMAT.format(component.getBaseAmount()))
                        .setOldPriceScale(0.75f)
                        .setNewPriceScale(1.0f)
                        .setGap(3)
                        .setColors(0xFF888888, 0xFFFFFFFF, 0xFFAAAAAA)
                        .autoSize()
                        .setStrikeYRatio(0.40f);
                container.addWidget(widget);
            }

            final ButtonWidget buyButton = new ButtonWidget();
            buyButton.setText(Component.translatable("shop.ui.offer_element.button.buy"));
            buyButton.setSizeHeight(test_size);
            buyButton.setButtonTexture(new PixelBevelTexture(PixelBevelTexture.PANEL_COLOR, PixelBevelTexture.ACCENT_LOW_COLOR, PixelBevelTexture.ACCENT_HIGH_COLOR, 0.7f));
            buyButton.setHoverTexture(new PixelBevelTexture(0xFF111624, PixelBevelTexture.ACCENT_LOW_COLOR, PixelBevelTexture.ACCENT_HIGH_COLOR, 0.7f));
            buyButton.setClickedTexture(PixelBevelTexture.accent().pressed());
            buyButton.setOnPressCallback((b) -> {
                if(b.button == InputConstants.MOUSE_BUTTON_LEFT)
                    ShopUIEvents.invokeBuyShopEntity(ctx.getShopEntity(), entry.getKey());
            });
//            buyButton.setTextColor(PixelBevelTexture.PAGE_COLOR);
            container.addWidget(buyButton);
            container.setDynamicSized(false);
            moneyTypeRows.add(container);
            moneyTypesContainer.addWidget(container);
        }
    }

    protected void addLimitBar(WidgetContextRender ctx, ShopOffer shopEntity) {
        final @Nullable LimiterComponent limiterComponent = shopEntity.getComponent(LimiterComponent.class).orElse(null);
        if (limiterComponent == null) {
            return;
        }

        final int offerLimitCount = limiterComponent.getCount();
        final int playerLimit = Math.max(0, limiterComponent.getLimit(Minecraft.getInstance().player));

        limitBar = new ProgressBarWidget()
                .setLeftText(Component.literal("Limit"))
                .setRightText(Component.literal(playerLimit + " / " + offerLimitCount))
                .setProgress(Mth.clamp((float) playerLimit / offerLimitCount, 0.0f, 1.0f))
                .setSegmentCount(offerLimitCount)
                .setScale(1.0f)
                .setTextScale(0.4f)
                .setBarBevelThickness(1f)
                .setDividerThickness(0.4f)
                .setTextYOffset(-1);
        if(offerLimitCount <= 50) {
            limitBar.setDividersEnabled(true);
        }

        ctx.addWidget(limitBar);
    }

    protected int alightOfferElementsTable(Widget owner, int currentY, int contentPadding, int contentWidth) {
        if (offerElementsTable == null) {
            if (offerElement != null) {
                return alightOfferElement(owner, currentY, contentPadding, contentWidth);
            }

            return currentY;
        }

        int tableAvailableWidth = Math.max(1, Math.min(contentWidth, owner.getSizeWidth() - OFFER_ELEMENTS_TABLE_PADDING * 2));
        int bottomLimit = limitBar == null
                ? owner.getSizeHeight() - contentPadding
                : limitBar.getSelfPositionY() - OFFER_ELEMENTS_TABLE_PADDING;
        int reservedMoneyHeight = moneyTypesContainer == null
                ? 0
                : getVisibleMoneyRows() * getMoneyRowHeight();
        bottomLimit -= reservedMoneyHeight;
        int cellSize = Math.max(DEBUG_SIZE_ELEMENT, Math.min(MAX_REWARD_CELL_SIZE, Math.min(tableAvailableWidth / 4, Math.max(1, bottomLimit - currentY))));
        int tableMaxRows = Math.max(1, Math.min(
                OFFER_ELEMENTS_TABLE_VISIBLE_ROWS,
                Math.max(1, (bottomLimit - currentY) / Math.max(1, cellSize))
        ));

        offerElementsTable
                .setCellSize(cellSize, cellSize)
                .setColumnsToFitWidth(tableAvailableWidth)
                .setMaxRows(tableMaxRows);

        int tableX = Math.max(0, (owner.getSizeWidth() - offerElementsTable.getSizeWidth()) / 2);
        offerElementsTable.setSelfPosition(tableX, currentY);

        return offerElementsTable.getSelfPositionY() + offerElementsTable.getSizeHeight();
    }

    protected int alightOfferElement(Widget owner, int currentY, int contentPadding, int contentWidth) {
        if (offerElement == null)
            return currentY;

        final int space_y = 2;
        final int titleBottom = titleLabel == null ? currentY : titleLabel.getSelfPositionY() + titleLabel.getSizeHeight();
        int bottomLimit = limitBar == null
                ? owner.getSizeHeight() - contentPadding
                : limitBar.getSelfPositionY() - contentPadding;
        int reservedMoneyHeight = moneyTypesContainer == null
                ? 0
                : getVisibleMoneyRows() * getMoneyRowHeight();
        int availableIconHeight = Math.max(DEBUG_SIZE_ELEMENT, bottomLimit - titleBottom - space_y - reservedMoneyHeight);
        int maxSingleRewardSize = MAX_REWARD_CELL_SIZE * OFFER_ELEMENTS_TABLE_VISIBLE_ROWS;
        int iconSize = Math.max(DEBUG_SIZE_ELEMENT, Math.min(maxSingleRewardSize, Math.min(contentWidth, availableIconHeight)));
        offerElement.setSize(iconSize, iconSize);
        final int offer_pos_x = Math.max(0, (owner.getSizeWidth() - offerElement.getSizeWidth()) / 2);
        offerElement.setSelfPosition(offer_pos_x, titleBottom + space_y);

        return offerElement.getSelfPositionY() + offerElement.getSizeHeight();
    }
}
