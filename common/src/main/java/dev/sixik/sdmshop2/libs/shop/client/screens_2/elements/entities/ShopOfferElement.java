package dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.entities;

import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.ShopBadgeHBoxWidget;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.ShopBadgeWidget;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.ShopEmptyWidget;
import dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.ShopUiElement;
import dev.sixik.sdmshop2.libs.shop.client.textures.ColorRectAndBorderTexture;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ProgressBarWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.TextLabel;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class ShopOfferElement extends WidgetGroup implements ShopUiElement {

    @Getter
    @Nullable
    private final ShopOffer shopEntity;

    @Nullable
    private TextLabel nameLabel;

//    private ShopEmptyWidget iconWidget;
//    private ShopEmptyWidget backgroundTitleWidget;
//    private ShopBadgeWidget badgeWidget;

    private ProgressBarWidget limitBar;
    private ShopBadgeHBoxWidget badgesBox;

    public ShopOfferElement(@Nullable ShopOffer shopEntity) {
        this.shopEntity = shopEntity;
        setBackground(new ColorRectAndBorderTexture());

//        addWidget(backgroundTitleWidget = new ShopEmptyWidget());
//        backgroundTitleWidget.setBackground(new ColorRectTexture(0xFF1E1E2A));
//
//        addWidget(iconWidget = new ShopEmptyWidget());
//        iconWidget.setBackground(new ItemStackTexture(new ItemStack(Items.DIAMOND, 4)));

//        addWidget(badgeWidget = new ShopBadgeWidget(Component.literal("Лим. 1")));

        badgesBox = new ShopBadgeHBoxWidget()
                .scale(0.5f)
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

        if (shopEntity != null) {
            NameComponent nameComponent = shopEntity.getComponent(NameComponent.class).orElse(null);
            if (nameComponent != null) {
                nameLabel = new TextLabel(0, 0, 10, 10, Component.translatable(nameComponent.getName()))
                        .setWrapText(true)
                        .scaleToFit()
                        .setAlignment(TextLabel.HorizontalAlignment.LEFT, TextLabel.VerticalAlignment.CENTER);
//                addWidget(nameLabel);
            }
        }
    }

    @Override
    public void alightWidget() {
        int space_x = 4;
        int space_y = 4;

        int headerWidth = this.getSizeWidth() - space_x * 2;
        int headerHeight = this.getSizeHeight() / 4;

//        backgroundTitleWidget.setSelfPosition(space_x, space_y);
//        backgroundTitleWidget.setSize(headerWidth, headerHeight);

        int iconPadding = 2;
        int iconSize = headerHeight - iconPadding * 2;

//        iconWidget.setSelfPosition(space_x + iconPadding, space_y + iconPadding);
//        iconWidget.setSize(iconSize, iconSize);

        int badgeWidth = 26;
        int badgeHeight = 10;
        int badgeX = (space_x + headerWidth) - badgeWidth - iconPadding;
        int badgeY = space_y + iconPadding;

        badgesBox.setSelfPositionY(-1);
        badgesBox.setSelfPositionX(2);
        int badgesBoxWidth = Math.max(0, getSizeWidth() - 4);
        badgesBox.setMaxLength(badgesBoxWidth);
        badgesBox.setSize(badgesBoxWidth, badgeHeight + 4);

        limitBar.setSelfPosition(2, getSize().height - 9);
        limitBar.setBarHeight(2);
        limitBar.setSize(getSize().width - 4, 6);

//        badgeWidget.setSelfPosition(badgeX, badgeY);
//        badgeWidget.setSize(badgeWidth, badgeHeight);

        if (nameLabel != null) {
//            int titleX = iconWidget.getSelfPositionX() + iconSize + 4;
//            int titleY = space_y + iconPadding;
//            int titleWidth = badgeX - titleX - 4;
//            int titleHeight = headerHeight - iconPadding * 2;
//
//            nameLabel.setSelfPosition(titleX, titleY);
//            nameLabel.setSize(titleWidth, titleHeight);
        }
    }
}
