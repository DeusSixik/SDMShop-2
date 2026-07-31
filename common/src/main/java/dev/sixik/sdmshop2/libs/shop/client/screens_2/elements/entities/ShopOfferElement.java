package dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.entities;

import com.lowdragmc.lowdraglib.gui.texture.*;
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.ShopBadgeHBoxWidget;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.ShopBadgeWidget;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.ShopEmptyWidget;
import dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.ShopUiElement;
import dev.sixik.sdmshop2.libs.shop.client.textures.ColorRectAndBorderTexture;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopIcons;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.*;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ContextMenuWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.HorizontalContainer;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.table.ScrollableInteractionTable;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class ShopOfferElement extends WidgetGroup implements ShopUiElement {

    private static final int DEBUG_COUNTS_ELEMENTS = 50;
    private static final int DEBUG_SIZE_ELEMENT = 8;
    private static final int OFFER_ELEMENTS_TABLE_VISIBLE_ROWS = 2;
    private static final int OFFER_ELEMENTS_TABLE_PADDING = 4;
    private static final int OFFER_ELEMENTS_TABLE_TOP_GAP = 4;

    @Getter
    @Nullable
    private final ShopOffer shopEntity;

    private final TextLabel nameLabel;
    private final ButtonWidget buyButton;
    private final SelectorList moneyType;

    private final ScrollableInteractionTable offerElementsTable;
    private final ProgressBarWidget limitBar;
    private final ShopBadgeHBoxWidget badgesBox;

    private final ButtonWidget favoriteButton;

    public ShopOfferElement(@Nullable ShopOffer shopEntity) {
        this.shopEntity = shopEntity;
        setBackground(new ColorRectAndBorderTexture());

        addWidget(favoriteButton = new ButtonWidget());
        favoriteButton.setBackground(ShopIcons.STAR_EMPTY);
        favoriteButton.setSize(8, 8);

        badgesBox = new ShopBadgeHBoxWidget()
                .scale(0.45f)
                .scaleTooltipWithHBox()
                .setSpacing(2);
        for (int i = 0; i < 10; i++) {
            badgesBox.addBadge(new ShopBadgeWidget(Component.literal("Test: " + i)).autoSizeToContent());
        }
        addWidget(badgesBox);

        addWidget(limitBar = new ProgressBarWidget()
                .setLeftText(Component.literal("Limit"))
                .setRightText(Component.literal("1/5"))
                .setProgress(0.2f)
                .setScale(1.0f)
                .setTextScale(0.4f)
                .setTextYOffset(-1)
        );

        addWidget(nameLabel = (TextLabel) new TextLabel(Component.empty())
                .setAutoSize(false)
                .setWrapText(false)
                .setMaxLines(1)
                .setLineSpacing(0)
                .setPadding(0)
                .scaleToFit()
                .setAlignment(TextLabel.HorizontalAlignment.CENTER, TextLabel.VerticalAlignment.CENTER));

        if (shopEntity != null) {
            NameComponent nameComponent = shopEntity.getComponent(NameComponent.class).orElse(null);
            if (nameComponent != null) {
                final String name = nameComponent.getName();
                nameLabel.setText(I18n.exists(name) ? Component.translatable(name) : Component.literal(name));
            } else {
                nameLabel.setText(Component.literal("Hello fobos from Test"));
            }
        }

        addWidget(offerElementsTable = new ScrollableInteractionTable()
                .setMaxRows(OFFER_ELEMENTS_TABLE_VISIBLE_ROWS)
                .setWheelRows(1)
                .reserveScrollBarSpace());

        for (int i = 0; i < DEBUG_COUNTS_ELEMENTS; i++) {

            var item = Items.DIAMOND.getDefaultInstance();
            var widget = new ShopEmptyWidget()
                    .setBackground(new ItemStackTexture(Items.DIAMOND));
            widget.setSize(DEBUG_SIZE_ELEMENT, DEBUG_SIZE_ELEMENT);
            widget.setHoverTexture(new ColorBorderTexture(1, 0xFFFFFFFF));
            widget.setHoverTooltips(DrawerHelper.getItemToolTip(item));
            offerElementsTable.addElement(widget);
        }

        buyButton = new ButtonWidget(Component.literal("Buy"));
        addWidget(buyButton);

        moneyType = new SelectorList();

        for (int i = 0; i < 3; i++) {
            PriceWidget widget = new PriceWidget()
                    .setPriceTexts("100$", "75$")
                    .setOldPriceScale(0.75f)
                    .setNewPriceScale(1.0f)
                    .setGap(3)
                    .setColors(0xFF888888, 0xFFFFFFFF, 0xFFAAAAAA)
                    .autoSize()
                    .setStrikeYRatio(0.40f);

            HorizontalContainer container = new HorizontalContainer();
            container.setPadding(4, 2).setSpacing(2);

            var icon = new ShopEmptyWidget();
            icon.setSize(8, 8);
            icon.setBackground(ShopIcons.STAR_FULL);
            container.addWidget(icon);
            container.addWidget(widget);

            PriceWidget widget2 = new PriceWidget()
                    .setPriceTexts("100$", "75$")
                    .setOldPriceScale(0.75f)
                    .setNewPriceScale(1.0f)
                    .setGap(3)
                    .setColors(0xFF888888, 0xFFFFFFFF, 0xFFAAAAAA)
                    .autoSize()
                    .setStrikeYRatio(0.40f);
            var icon2 = new ShopEmptyWidget();
            icon2.setSize(8, 8);
            icon2.setBackground(ShopIcons.STAR_FULL);
            container.addWidget(icon2);
            container.addWidget(widget2);
            moneyType.addOption(container);
        }

        moneyType.allowEmptySelection()
                .vertical()
                .onSelectionChanged(index -> {
                    // -1 = ничего не выбрано
                });
        moneyType.scale(0.5f);
        moneyType.allowEmptySelection();
        moneyType.preventDeselect();
        addWidget(moneyType);
    }

    @Override
    public void alightWidget() {
        int space_x = 4;
        int space_y = 4;

        int headerWidth = this.getSizeWidth() - space_x * 2;
        int headerHeight = this.getSizeHeight() / 4;

        int iconPadding = 2;
        int iconSize = headerHeight - iconPadding * 2;

        int badgeWidth = 26;
        int badgeHeight = 10;
        int badgeX = (space_x + headerWidth) - badgeWidth - iconPadding;
        int badgeY = space_y + iconPadding;

        badgesBox.setSelfPositionY(-2);
        badgesBox.setSelfPositionX(2);
        int badgesBoxWidth = Math.max(0, getSizeWidth() - 4);
        badgesBox.setMaxLength(badgesBoxWidth);
        badgesBox.setSize(badgesBoxWidth, badgeHeight + 4);

        limitBar.setSelfPosition(2, getSize().height - 9);
        limitBar.setBarHeight(2);
        limitBar.setSize(getSize().width - 4, 6);
        if (nameLabel != null) {

            int screen_w = this.getSizeWidth();
            int title_y = 4 + badgesBox.getHeightWithScale();

            nameLabel.setSelfPosition(0, title_y);
            nameLabel.setSize(screen_w, Minecraft.getInstance().font.lineHeight);
        }

        alightOfferElementsTable();

        buyButton.setSize(getSizeWidth() - 4, 14);
        buyButton.setSelfPosition(2, offerElementsTable.getSelfPositionY() + offerElementsTable.getSizeHeight() + 2);

        moneyType.setSize(getSizeWidth() - 4, moneyType.getContentHeightWithScale());
        moneyType.setSelfPosition(2, buyButton.getSelfPositionY() + buyButton.getSizeHeight() + 2);

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
        int tableY = badgesBox.getHeightWithScale() + 4 + yOffset;

        offerElementsTable.setSelfPosition(tableX, tableY);
    }

    private int getOfferElementsTableTop() {
        if (nameLabel == null) {
            return 4 + badgesBox.getHeightWithScale() + OFFER_ELEMENTS_TABLE_TOP_GAP;
        }

        return nameLabel.getSelfPositionY() + nameLabel.getSizeHeight() + OFFER_ELEMENTS_TABLE_TOP_GAP;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(button == 1 && isMouseOverElement(mouseX, mouseY)) {
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
