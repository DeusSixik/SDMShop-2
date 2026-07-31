package dev.sixik.sdmshop2.libs.shop.client.ui.theme;

import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.TransformTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.ShopBadgeHBoxWidget;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.ShopEmptyWidget;
import dev.sixik.sdmshop2.libs.shop.client.textures.PixelBevelTexture;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopIcons;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.OfferElementContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.OfferElementRender;
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
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ContextMenuWidget;
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

public class DefaultShopOfferElementRender implements OfferElementRender {

    private static final int DEBUG_SIZE_ELEMENT = 8;
    private static final int OFFER_ELEMENTS_TABLE_VISIBLE_ROWS = 2;
    private static final int OFFER_ELEMENTS_TABLE_PADDING = 4;
    private static final int OFFER_ELEMENTS_TABLE_TOP_GAP = 4;

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    @Nullable
    private TextLabel titleLabel;
    @Nullable
    private VerticalContainer moneyTypesContainer;
    private final List<HorizontalContainer> moneyTypeRows = new ArrayList<>();
    @Nullable
    private ScrollableInteractionTable offerElementsTable;
    @Nullable
    private Widget offerElement;
    @Nullable
    private ProgressBarWidget limitBar;
    @Nullable
    private ShopBadgeHBoxWidget badgesBox;
    @Nullable
    private ButtonWidget favoriteButton;

    private boolean oneOfferElement = false;

    public DefaultShopOfferElementRender() {
    }

    @Override
    public void constructor(OfferElementContextRender ctx) {
        ctx.getOwner().setBackground(new PixelBevelTexture(
                PixelBevelTexture.PANEL_COLOR,
                PixelBevelTexture.LINE_LOW_COLOR,
                PixelBevelTexture.LINE_HIGH_COLOR,
                1.5f
        ));
    }

    @Override
    public void addWidgets(OfferElementContextRender ctx) {
        clearWidgetsState();

        final ShopEntity entity = ctx.getShopEntity();
        if (!(entity instanceof ShopOffer shopEntity)) {
            return;
        }

        favoriteButton = new ButtonWidget();
        favoriteButton.setBackground(ShopIcons.STAR_EMPTY);
        favoriteButton.setSize(8, 8);
        ctx.addWidget(favoriteButton);

        final Map<ShopComponentCategory, ObjectList<ShopComponent>> offerComponentsMap =
                ShopComponentsUtils.getComponentsByCategory(shopEntity);

        addNameLabel(ctx, shopEntity, offerComponentsMap);
        addOfferElementsTable(ctx, offerComponentsMap);
        addMoneyTypes(ctx, offerComponentsMap);
        addLimitBar(ctx, shopEntity);

    }

    @Override
    public void alightWidgets(OfferElementContextRender ctx) {
        final Widget owner = ctx.getOwner();

        int contentPadding = 4;
        int currentY = 4;
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
            limitBar.setSelfPosition(contentPadding / 2, owner.getSize().height - limitBar.getSizeHeight());
            limitBar.setSize(owner.getSize().width - contentPadding, 14);
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
            int moneyTypesY = currentY + 2;
            int availableBottom = limitBar == null
                    ? owner.getSizeHeight() - contentPadding
                    : limitBar.getSelfPositionY() - contentPadding;
            int moneyTypesVisualHeight = Math.max(1, availableBottom - moneyTypesY);
            int moneyTypesLogicalHeight = Math.max(1, Math.round(moneyTypesVisualHeight / moneyTypesContainer.getScale()));

            moneyTypesContainer.setSize(moneyTypesLogicalWidth, moneyTypesLogicalHeight);
            moneyTypesContainer.setSelfPosition(contentPadding / 2, moneyTypesY);
            for (HorizontalContainer row : moneyTypeRows) {
                row.setSize(moneyTypesLogicalWidth, Math.max(1, row.getSizeHeight()));
            }
        }

        if (favoriteButton != null) {
            favoriteButton.setSelfPosition(owner.getSizeWidth() - favoriteButton.getSizeWidth() / 2, -favoriteButton.getSizeHeight() / 2);
        }

    }

    @Override
    public boolean mouseClicked(OfferElementContextRender ctx, double mouseX, double mouseY, int button) {
        if (button != 1 || !ctx.getOwner().isMouseOverElement(mouseX, mouseY)) {
            return false;
        }

        ContextMenuWidget.open(ctx.getOwner(), (int) mouseX, (int) mouseY, 120)
                .addItem("Copy", () -> {
                    // copy action
                })
                .addSeparator()
                .addItem("Delete", () -> {
                    // delete action
                })
                .addSeparator()
                .addItem("Edit", () -> {

                }).scale(0.7f);

        return true;
    }

    private void clearWidgetsState() {
        titleLabel = null;
        moneyTypesContainer = null;
        moneyTypeRows.clear();
        offerElementsTable = null;
        limitBar = null;
        badgesBox = null;
        favoriteButton = null;
    }

    private void addNameLabel(
            OfferElementContextRender ctx,
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

    private TextLabel createNameLabel(Component text) {
        return (TextLabel) new TextLabel(text)
                .setAutoSize(false)
                .setWrapText(false)
                .setMaxLines(1)
                .setLineSpacing(0)
                .setPadding(0)
                .scaleToFit()
                .setAlignment(TextLabel.HorizontalAlignment.CENTER, TextLabel.VerticalAlignment.CENTER);
    }

    private void addOfferElementsTable(
            OfferElementContextRender ctx,
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

    private void addOfferElement(
            OfferElementContextRender ctx,
            RewardComponent component
    ) {
        if (component == null)
            return;

        final Widget widget = component.createRender();
        if (widget == null)
            return;

        final int size_w = ctx.getOwner().getSizeWidth();
        final int icon_size = Math.max(size_w / 2, DEBUG_SIZE_ELEMENT);

        widget.setSize(icon_size, icon_size);
        widget.setBackground(new GuiTextureGroup(PixelBevelTexture.panel(), widget.getBackgroundTexture()));
        widget.setHoverTexture(new ColorBorderTexture(1, 0xFFFFFFFF));
        ctx.addWidget(offerElement = widget);
    }

    private void addMoneyTypes(
            OfferElementContextRender ctx,
            Map<ShopComponentCategory, ObjectList<ShopComponent>> offerComponentsMap
    ) {
        if (!offerComponentsMap.containsKey(ShopComponentCategory.COST)) {
            return;
        }

        final Map<String, List<CostComponent>> costComponents = ShopComponentsUtils.getCostComponentsByGroups(offerComponentsMap);

        moneyTypesContainer = new VerticalContainer();
        moneyTypesContainer.setDynamicSized(false);
        moneyTypesContainer.setSpacing(0);
        moneyTypesContainer.setScale(oneOfferElement ? 1f : 0.7f);
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
            buyButton.setButtonTexture(PixelBevelTexture.accent());
            buyButton.setHoverTexture(new PixelBevelTexture(0xFFF0BC50, PixelBevelTexture.ACCENT_LOW_COLOR, PixelBevelTexture.ACCENT_HIGH_COLOR, 0.7f));
            buyButton.setClickedTexture(PixelBevelTexture.accent().pressed());
            buyButton.setTextColor(PixelBevelTexture.PAGE_COLOR);
            container.addWidget(buyButton);
            container.setDynamicSized(false);
            moneyTypeRows.add(container);
            moneyTypesContainer.addWidget(container);
        }
    }

    private void addLimitBar(OfferElementContextRender ctx, ShopOffer shopEntity) {
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

    private int alightOfferElementsTable(Widget owner, int currentY, int contentPadding, int contentWidth) {
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
        int tableMaxRows = Math.max(1, Math.min(
                OFFER_ELEMENTS_TABLE_VISIBLE_ROWS,
                Math.max(1, (bottomLimit - currentY) / Math.max(1, DEBUG_SIZE_ELEMENT))
        ));

        offerElementsTable
                .setCellSize(DEBUG_SIZE_ELEMENT, DEBUG_SIZE_ELEMENT)
                .setColumnsToFitWidth(tableAvailableWidth)
                .setMaxRows(tableMaxRows);

        int tableX = Math.max(0, (owner.getSizeWidth() - offerElementsTable.getSizeWidth()) / 2);
        offerElementsTable.setSelfPosition(tableX, currentY);

        return offerElementsTable.getSelfPositionY() + offerElementsTable.getSizeHeight();
    }

    private int alightOfferElement(Widget owner, int currentY, int contentPadding, int contentWidth) {
        if (offerElement == null)
            return currentY;

        final int offer_pos_x = Math.max(0, (owner.getSizeWidth() - offerElement.getSizeWidth()) / 2);
        final int space_y = 2;
        offerElement.setSelfPosition(offer_pos_x, titleLabel.getSelfPositionY() + titleLabel.getSizeHeight() + space_y);

        return offerElement.getSelfPositionY() + offerElement.getSizeHeight();
    }
}
