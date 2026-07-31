package dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.entities;

import com.lowdragmc.lowdraglib.gui.texture.*;
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.ShopBadgeHBoxWidget;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.ShopBadgeWidget;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.ShopEmptyWidget;
import dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.ShopUiElement;
import dev.sixik.sdmshop2.libs.shop.client.textures.ColorRectAndBorderTexture;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopIcons;
import dev.sixik.sdmshop2.libs.shop.components.api.CostComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponentCategory;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import dev.sixik.sdmshop2.libs.shop.components.utils.ShopComponentsUtils;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.*;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ContextMenuWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.HorizontalContainer;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.VerticalContainer;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.table.ScrollableInteractionTable;
import it.unimi.dsi.fastutil.objects.ObjectList;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ShopOfferElement extends WidgetGroup implements ShopUiElement {

    private static final int DEBUG_COUNTS_ELEMENTS = 50;
    private static final int DEBUG_SIZE_ELEMENT = 8;
    private static final int OFFER_ELEMENTS_TABLE_VISIBLE_ROWS = 2;
    private static final int OFFER_ELEMENTS_TABLE_PADDING = 4;
    private static final int OFFER_ELEMENTS_TABLE_TOP_GAP = 4;

    @Getter
    @Nullable
    private final ShopOffer shopEntity;

    @Nullable
    private final TextLabel nameLabel;
    @Nullable
    private final VerticalContainer moneyTypesContainer;
    private final List<HorizontalContainer> moneyTypeRows = new ArrayList<>();
    @Nullable
    private final ScrollableInteractionTable offerElementsTable;
    @Nullable
    private final ProgressBarWidget limitBar;
    @Nullable
    private final ShopBadgeHBoxWidget badgesBox;

    private final ButtonWidget favoriteButton;

    public ShopOfferElement(@Nullable ShopOffer shopEntity) {
        this.shopEntity = shopEntity;
        setBackground(new ColorRectAndBorderTexture());

        if (shopEntity == null) {
            this.nameLabel = null;
            this.moneyTypesContainer = null;
            this.offerElementsTable = null;
            this.limitBar = null;
            this.badgesBox = null;
            this.favoriteButton = null;
            return;
        }

        addWidget(favoriteButton = new ButtonWidget());
        favoriteButton.setBackground(ShopIcons.STAR_EMPTY);
        favoriteButton.setSize(8, 8);

        final Map<ShopComponentCategory, ObjectList<ShopComponent>> offer_components_map =
                ShopComponentsUtils.getComponentsByCategory(shopEntity);

        final Optional<NameComponent> name_component_opt = shopEntity.getComponent(NameComponent.class);
        if (name_component_opt.isPresent()) {
            final NameComponent name_component = name_component_opt.get();
            final String name = name_component.getName();
            addWidget(nameLabel = (TextLabel) new TextLabel(I18n.exists(name) ? Component.translatable(name) : Component.literal(name))
                    .setAutoSize(false)
                    .setWrapText(false)
                    .setMaxLines(1)
                    .setLineSpacing(0)
                    .setPadding(0)
                    .scaleToFit()
                    .setAlignment(TextLabel.HorizontalAlignment.CENTER, TextLabel.VerticalAlignment.CENTER));
        } else
            nameLabel = null;

        if (offer_components_map.containsKey(ShopComponentCategory.REWARD)) {
            addWidget(offerElementsTable = new ScrollableInteractionTable()
                    .setMaxRows(OFFER_ELEMENTS_TABLE_VISIBLE_ROWS)
                    .setWheelRows(1)
                    .reserveScrollBarSpace());

            final ObjectList<ShopComponent> reward_components = offer_components_map.get(ShopComponentCategory.REWARD);
            for (final ShopComponent rewardComponent : reward_components) {
                final @Nullable Widget widget = rewardComponent.createRender();
                if (widget == null)
                    continue;

                widget.setSize(DEBUG_SIZE_ELEMENT, DEBUG_SIZE_ELEMENT);
                widget.setHoverTexture(new ColorBorderTexture(1, 0xFFFFFFFF));
                offerElementsTable.addElement(widget);
            }
        } else
            offerElementsTable = null;

        if (offer_components_map.containsKey(ShopComponentCategory.COST)) {
            final Map<String, List<CostComponent>> cost_components = ShopComponentsUtils.getCostComponentsByGroups(offer_components_map);

            moneyTypesContainer = new VerticalContainer();
            moneyTypesContainer.setDynamicSized(false);
            moneyTypesContainer.setSpacing(0);
            moneyTypesContainer.setScale(0.7f);
            addWidget(moneyTypesContainer);

            for (Map.Entry<String, List<CostComponent>> entry : cost_components.entrySet()) {
                final HorizontalContainer container = new HorizontalContainer();
                container.setPadding(4, 2).setSpacing(4);
                container.alignBottom();
                container.pushLastElementToEnd();

                for (CostComponent component : entry.getValue()) {
                    final @Nullable TransformTexture texture = component.getRenderIcon();

                    if (texture != null) {
                        var icon = new ShopEmptyWidget();
                        icon.setSize(8, 8);
                        icon.setBackground(texture);
                        container.addWidget(icon);
                    }

                    final PriceWidget widget = new PriceWidget()
                            .setPriceTexts(null, String.valueOf(component.getBaseAmount()))
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
                buyButton.setSizeHeight(Minecraft.getInstance().font.lineHeight + 3);
                container.addWidget(buyButton);
                container.setDynamicSized(false);
                moneyTypeRows.add(container);
                moneyTypesContainer.addWidget(container);
            }
        } else
            moneyTypesContainer = null;

        badgesBox = null;
        limitBar = null;

//        for (int i = 0; i < 10; i++) {
//            badgesBox.addBadge(new ShopBadgeWidget(Component.literal("Test: " + i)).autoSizeToContent());
//        }
//        addWidget(badgesBox = new ShopBadgeHBoxWidget()
//                .scale(0.45f)
//                .scaleTooltipWithHBox()
//                .setSpacing(2));
//
//        addWidget(limitBar = new ProgressBarWidget()
//                .setLeftText(Component.literal("Limit"))
//                .setRightText(Component.literal("1/5"))
//                .setProgress(0.2f)
//                .setScale(1.0f)
//                .setTextScale(0.4f)
//                .setTextYOffset(-1)
//        );
    }

    @Override
    public void alightWidget() {
        int space_x = 4;
        int space_y = 4;

        int headerWidth = this.getSizeWidth() - space_x * 2;
        int headerHeight = this.getSizeHeight() / 4;

        int badgeWidth = 26;
        int badgeHeight = 10;

        if (badgesBox != null) {
            badgesBox.setSelfPositionY(-2);
            badgesBox.setSelfPositionX(2);
            int badgesBoxWidth = Math.max(0, getSizeWidth() - 4);
            badgesBox.setMaxLength(badgesBoxWidth);
            badgesBox.setSize(badgesBoxWidth, badgeHeight + 4);
        }

        if(limitBar != null) {
            limitBar.setSelfPosition(2, getSize().height - 9);
            limitBar.setBarHeight(2);
            limitBar.setSize(getSize().width - 4, 6);
        }
        if (nameLabel != null) {

            int screen_w = this.getSizeWidth();
            int title_y = 4 + (badgesBox == null ? 0 : badgesBox.getHeightWithScale());

            nameLabel.setSelfPosition(0, title_y);
            nameLabel.setSize(screen_w, Minecraft.getInstance().font.lineHeight);
        }

        alightOfferElementsTable();

        int moneyTypesVisualWidth = Math.max(1, getSizeWidth() - 4);
        int moneyTypesLogicalWidth = Math.max(1, Math.round(moneyTypesVisualWidth / moneyTypesContainer.getScale()));
        moneyTypesContainer.setSize(moneyTypesLogicalWidth, 100);
        moneyTypesContainer.setSelfPosition(2, offerElementsTable.getSelfPositionY() + offerElementsTable.getSizeHeight() + 2);
        for (HorizontalContainer row : moneyTypeRows) {
            row.setSize(moneyTypesLogicalWidth, Math.max(1, row.getSizeHeight()));
        }

        favoriteButton.setSelfPosition(getSizeWidth() - 4, -4);
    }

    private void alightOfferElementsTable() {
        int tableAvailableWidth = Math.max(1, getSizeWidth() - OFFER_ELEMENTS_TABLE_PADDING * 2);
        int tableTop = getOfferElementsTableTop();
        int tableBottom = Math.max(tableTop, limitBar.getSelfPositionY() - OFFER_ELEMENTS_TABLE_PADDING);

        int yOffset = nameLabel == null ? 0 : nameLabel.getSizeHeight() + 4;

        offerElementsTable
                .setCellSize(DEBUG_SIZE_ELEMENT, DEBUG_SIZE_ELEMENT)
                .setColumnsToFitWidth(tableAvailableWidth)
                .setMaxRows(OFFER_ELEMENTS_TABLE_VISIBLE_ROWS);

        int tableX = Math.max(0, (getSizeWidth() - offerElementsTable.getSizeWidth()) / 2);
        int tableY = (badgesBox != null ? badgesBox.getHeightWithScale() : 0) + 4 + yOffset;

        offerElementsTable.setSelfPosition(tableX, tableY);
    }

    private int getOfferElementsTableTop() {
        if (nameLabel == null) {
            return 4 + (badgesBox != null ? badgesBox.getHeightWithScale() : 0) + OFFER_ELEMENTS_TABLE_TOP_GAP;
        }

        return nameLabel.getSelfPositionY() + nameLabel.getSizeHeight() + OFFER_ELEMENTS_TABLE_TOP_GAP;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 1 && isMouseOverElement(mouseX, mouseY)) {
            ContextMenuWidget.open(this, (int) mouseX, (int) mouseY, 120)
                    .addItem("Copy", () -> {
                        // copy action
                    })
                    .addSeparator()
                    .addItem("Delete", () -> {
                        // delete action
                    }).scale(0.7f);

            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
}
