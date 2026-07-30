package dev.sixik.sdmshop2.libs.shop_ldlib_extension;

import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.ButtonWidget;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.ProgressWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.ButtonWidgetGroup;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.SDMTextLabel;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.ShopEmptyWidget;
import dev.sixik.sdmshop2.libs.shop.client.textures.ColorRectAndBorderTexture;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.InputTextBox;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.MultiLineInputTextBox;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.TextLabel;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.GridBox;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.HorizontalContainer;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.TabBox;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.VerticalContainer;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.table.IconsTable;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.table.InteractionTable;
import dev.sixik.sdmshop2.utils.ShopUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class ShopRenderLibExtension {

    private static final int TEXT_MAIN = 0xFFE8E8F0;
    private static final int TEXT_MUTED = 0xFF9DA3B0;
    private static final int PANEL_FILL = 0xFF20202A;
    private static final int PANEL_BORDER = 0xFF4C5265;
    private static final int HOVER_BORDER = 0xFFFFD166;

    public static void openUi() {
        ShopUtils.openWidget(debug());
    }

    private static WidgetGroup debug() {
        WidgetGroup group = new WidgetGroup(0, 0, 1920, 1080);
        group.setClientSideWidget();
        group.setBackground(new ColorRectTexture(0xDD101018));

        group.addWidget(label(40, 28, "SDMShop2 LDLib widgets debug", 0xFFFFFFFF));
        group.addWidget(label(40, 44, "IconsTable / InteractionTable / GridBox / HorizontalContainer / VerticalContainer", TEXT_MUTED));

        MultiLineInputTextBox multiLineInput = createMultiLineInputDemo();
        multiLineInput.setSelfPosition(40, 78);
        group.addWidget(section(28, 64, 330, 160, "MultiLineInputTextBox"));
        group.addWidget(multiLineInput);

        InteractionTable interactionTable = createInteractionTable();
        interactionTable.setSelfPosition(400, 78);
        group.addWidget(section(388, 64, 226, 160, "InteractionTable"));
        group.addWidget(interactionTable);

        VerticalContainer containerDemo = createContainerDemo();
        containerDemo.setSelfPosition(660, 78);
        group.addWidget(section(648, 64, 330, 210, "HBox / VBox + our widgets"));
        group.addWidget(containerDemo);

        HorizontalContainer shopWidgetsRow = createShopWidgetsRow();
        shopWidgetsRow.setSelfPosition(40, 270);
        group.addWidget(section(28, 256, 500, 96, "Shop widgets row"));
        group.addWidget(shopWidgetsRow);

        GridBox gridBox = createGridDemo();
        gridBox.setSelfPosition(40, 390);
        group.addWidget(section(28, 376, 330, 126, "GridBox"));
        group.addWidget(gridBox);

        IconsTable iconsTable = createIconsTable();
        iconsTable.setSelfPosition(390, 318);
        group.addWidget(section(378, 304, 138, 160, "IconsTable"));
        group.addWidget(iconsTable);

        TabBox tabBox = createTabBoxDemo();
        tabBox.setSelfPosition(600, 78);
        group.addWidget(section(588, 64, 360, 190, "TabBox"));
        group.addWidget(tabBox);

        WidgetGroup textLabelDemo = createTextLabelDemo();
        textLabelDemo.setSelfPosition(980, 78);
        group.addWidget(section(968, 64, 330, 190, "TextLabel"));
        group.addWidget(textLabelDemo);

        return group;
    }

    private static IconsTable createIconsTable() {
        IconsTable table = new IconsTable(4, 3);
        table.setCellSize(20, 20);
        table.setSpacing(4, 4);

        table.addElement(new ItemStackTexture(Items.DIAMOND)::draw);
        table.addElement(new ItemStackTexture(Items.EMERALD)::draw);
        table.addElement(new ItemStackTexture(Items.GOLD_INGOT)::draw);
        table.addElement(new ItemStackTexture(Items.IRON_INGOT)::draw);
        table.addElement(new ItemStackTexture(Items.NETHERITE_INGOT)::draw);
        table.addElement(new ItemStackTexture(Items.REDSTONE)::draw);
        table.addElement(new ItemStackTexture(Items.LAPIS_LAZULI)::draw);
        table.addElement(new ItemStackTexture(Items.AMETHYST_SHARD)::draw);

        table.setElementScale(4, 0.65f);
        table.setElementScale(7, 1.25f);
        return table;
    }

    private static String text = "diamond";
    private static String multilineText = "Unity-like multiline input\nEnter adds new lines\nMouse wheel scrolls";

    private static InteractionTable createInteractionTable() {
        InteractionTable table = new InteractionTable(2, 3)
                .setCellSize(96, 28)
                .setSpacing(8, 8);

        table.addElement(button("Buy", "Clickable ButtonWidget inside InteractionTable"));
        table.addElement(button("Sell", "Another clickable cell; hover should show tooltip"));

        InputTextBox search = new InputTextBox(0, 0, 96, 28, () -> text, text -> ShopRenderLibExtension.text = text)
                .setPlaceholder(Component.literal("search"));
        search.setCurrentString(text);
        search.setHoverTooltips(Component.literal("InputTextBox receives focus/click/keyboard events here"));
        table.addElement(search);

        ProgressWidget progress = new ProgressWidget(ProgressWidget.JEIProgress, 0, 0, 96, 28);
        progress.setHoverTooltips(Component.literal("Animated ProgressWidget cell"));
        table.addElement(progress);

        table.addElement(iconCell(Items.CHEST, "Plain WidgetGroup cell with item icon"));
        table.addElement(iconCell(Items.BARRIER, "Scaled cell example"), 0.8f);

        return table;
    }

    private static VerticalContainer createContainerDemo() {
        VerticalContainer box = new VerticalContainer(Position.ORIGIN)
                .setPadding(8)
                .setSpacing(8)
                .alignStart();

        box.addWidget(new SDMTextLabel(0, 0, 250, 18, Component.literal("VerticalContainer: stacks children"))
                .setAutoScale(true)
                .setHoverTooltips(Component.literal("This is our SDMTextLabel inside VBox")));

        HorizontalContainer row = new HorizontalContainer(Position.ORIGIN)
                .setPadding(4, 2)
                .setSpacing(6)
                .alignCenter();
        row.setBackground(new ColorRectAndBorderTexture(0xFF282A36, 0xFF5C637A, 1));

        row.addWidget(button("One", "First ButtonWidget in HorizontalContainer"));
        row.addWidget(button("Two", "Second ButtonWidget in HorizontalContainer"));
        row.addWidget(button("Three", "Third ButtonWidget in HorizontalContainer"));

        box.addWidget(row);

        InputTextBox field = new InputTextBox(0, 0, 230, 22, () -> "editable text", text -> { })
                .setPlaceholder(Component.literal("type here"));
        field.setCurrentString("editable text");
        field.setHoverTooltips(Component.literal("Editable InputTextBox in VBox"));
        box.addWidget(field);

        ButtonWidgetGroup buttonGroup = new ButtonWidgetGroup(0, 0, 230, 28,
                new GuiTextureGroup(ResourceBorderTexture.BUTTON_COMMON, new TextTexture("ButtonWidgetGroup")),
                click -> { });
        buttonGroup.setHoverTexture(new ColorBorderTexture(1, HOVER_BORDER));
        buttonGroup.setHoverTooltips(Component.literal("Our ButtonWidgetGroup with a nested label"));
        buttonGroup.addWidget(new SDMTextLabel(8, 9, Component.literal("nested label")));
        box.addWidget(buttonGroup);

        return box;
    }

    private static HorizontalContainer createShopWidgetsRow() {
        HorizontalContainer row = new HorizontalContainer(Position.ORIGIN)
                .setPadding(8)
                .setSpacing(10)
                .alignCenter();

        row.addWidget(coloredBox("ShopEmptyWidget", 0xFF2E3440));
        row.addWidget(coloredBox("Hover texture", 0xFF3B4252).setHoverTexture(new ColorBorderTexture(2, HOVER_BORDER)));
        row.addWidget(new SDMTextLabel(0, 0, 120, 36, Component.literal("Auto-scale label"))
                .setAutoScale(true)
                .setHoverTooltips(Component.literal("SDMTextLabel autoScale demo")));

        ProgressWidget progress = new ProgressWidget(ProgressWidget.JEIProgress, 0, 0, 120, 18);
        progress.setHoverTooltips(Component.literal("ProgressWidget next to custom shop widgets"));
        row.addWidget(progress);

        return row;
    }

    private static GridBox createGridDemo() {
        GridBox grid = new GridBox(3, Position.ORIGIN)
                .setPadding(8)
                .setSpacing(8, 8)
                .setCellSize(96, 28)
                .alignCenter();

        grid.addWidget(button("Grid 1", "GridBox cell 1"));
        grid.addWidget(button("Grid 2", "GridBox cell 2"));
        grid.addWidget(button("Grid 3", "GridBox cell 3"));
        grid.addWidget(iconCell(Items.DIAMOND, "WidgetGroup item cell in GridBox"));
        grid.addWidget(iconCell(Items.EMERALD, "Second item cell in GridBox"));
        grid.addWidget(iconCell(Items.GOLD_INGOT, "Third item cell in GridBox"));

        return grid;
    }

    private static MultiLineInputTextBox createMultiLineInputDemo() {
        MultiLineInputTextBox input = new MultiLineInputTextBox(0, 0, 306, 86, () -> multilineText, text -> ShopRenderLibExtension.multilineText = text)
                .setPlaceholder(Component.literal("type multiline text..."))
                .setMaxLength(512)
                .setPadding(6, 5)
                .setLineSpacing(2);
        input.setCurrentString(multilineText);
        input.setHoverTooltips(Component.literal("Enter inserts newline; Up/Down and mouse wheel work here"));
        return input;
    }

    private static TabBox createTabBoxDemo() {
        TabBox tabBox = new TabBox(0, 0, 336, 150)
                .setTabHeight(20)
                .setTabWidth(74)
                .setContentPadding(6);

        WidgetGroup infoPage = new WidgetGroup(0, 0, 320, 112);
        infoPage.addWidget(new SDMTextLabel(0, 0, 300, 18, Component.literal("TabBox: switches visible pages"))
                .setAutoScale(true)
                .setHoverTooltips(Component.literal("This page is a plain WidgetGroup")));
        infoPage.addWidget(new SDMTextLabel(0, 24, 300, 18, Component.literal("Inactive pages are hidden/deactivated"))
                .setAutoScale(true));

        WidgetGroup inputPage = new WidgetGroup(0, 0, 320, 112);
        InputTextBox input = new InputTextBox(0, 0, 180, 24, () -> text, text -> ShopRenderLibExtension.text = text)
                .setPlaceholder(Component.literal("tab input"));
        input.setCurrentString(text);
        inputPage.addWidget(input);
        ButtonWidget applyButton = button("Apply", "Button inside TabBox page");
        applyButton.setSelfPosition(190, 0);
        inputPage.addWidget(applyButton);

        WidgetGroup gridPage = new WidgetGroup(0, 0, 320, 112);
        GridBox grid = new GridBox(2, Position.ORIGIN)
                .setCellSize(96, 28)
                .setSpacing(8)
                .alignCenter();
        grid.addWidget(button("A", "Grid in TabBox"));
        grid.addWidget(button("B", "Grid in TabBox"));
        grid.addWidget(iconCell(Items.CHEST, "Icon page cell"));
        grid.addWidget(iconCell(Items.BARRIER, "Icon page cell"));
        gridPage.addWidget(grid);

        tabBox.addTab("Info", infoPage);
        tabBox.addTab("Input", inputPage);
        tabBox.addTab("Grid", gridPage);
        return tabBox;
    }

    private static WidgetGroup createTextLabelDemo() {
        WidgetGroup group = new WidgetGroup(0, 0, 306, 150);

        group.addWidget(new TextLabel(0, 0, 290, 36,
                Component.literal("Wrapped TextLabel: long text stays inside the assigned box."))
                .setWrapText(true)
                .setLineSpacing(1)
                .setPadding(3)
                .setBackground(new ColorRectAndBorderTexture(0xFF252733, PANEL_BORDER, 1)));

        group.addWidget(new TextLabel(0, 46, 140, 22,
                Component.literal("Ellipsis overflow example"))
                .ellipsisOverflow()
                .setPadding(3)
                .setBackground(new ColorRectAndBorderTexture(0xFF252733, PANEL_BORDER, 1)));

        group.addWidget(new TextLabel(150, 46, 140, 22,
                Component.literal("Centered"))
                .setAlignment(TextLabel.HorizontalAlignment.CENTER, TextLabel.VerticalAlignment.CENTER)
                .setPadding(3)
                .setBackground(new ColorRectAndBorderTexture(0xFF252733, PANEL_BORDER, 1)));

        group.addWidget(new TextLabel(0, 78, 290, 42,
                Component.literal("Scale to fit keeps large labels readable"))
                .scaleToFit()
                .setAlignment(TextLabel.HorizontalAlignment.CENTER, TextLabel.VerticalAlignment.CENTER)
                .setPadding(4)
                .setBackground(new ColorRectAndBorderTexture(0xFF252733, PANEL_BORDER, 1)));

        return group;
    }

    private static ButtonWidget button(String text, String tooltip) {
        ButtonWidget button = new ButtonWidget(0, 0, 96, 28,
                new GuiTextureGroup(ResourceBorderTexture.BUTTON_COMMON, new TextTexture(text)),
                click -> { });
        button.setHoverBorderTexture(1, HOVER_BORDER);
        button.setHoverTooltips(Component.literal(tooltip));
        return button;
    }

    private static WidgetGroup iconCell(Item item, String tooltip) {
        WidgetGroup cell = new WidgetGroup(0, 0, 96, 28);
        cell.setBackground(new ColorRectAndBorderTexture(0xFF2A2A36, PANEL_BORDER, 1));
        cell.setHoverTexture(new ColorBorderTexture(1, HOVER_BORDER));
        cell.setHoverTooltips(Component.literal(tooltip));
        cell.addWidget(new Widget(6, 6, 16, 16).setBackground(new ItemStackTexture(item)));
        cell.addWidget(new LabelWidget(28, 10, item.getDescriptionId()).setTextColor(TEXT_MUTED));
        return cell;
    }

    private static ShopEmptyWidget coloredBox(String text, int fillColor) {
        ShopEmptyWidget box = new ShopEmptyWidget(0, 0, 120, 36);
        box.setBackground(new GuiTextureGroup(
                new ColorRectAndBorderTexture(fillColor, PANEL_BORDER, 1),
                new TextTexture(text).setColor(TEXT_MAIN)
        ));
        box.setHoverTooltips(Component.literal(text));
        return box;
    }

    private static WidgetGroup section(int x, int y, int width, int height, String title) {
        WidgetGroup section = new WidgetGroup(x, y, width, height);
        section.setBackground(new ColorRectAndBorderTexture(PANEL_FILL, PANEL_BORDER, 1).setRadius(4));
        section.addWidget(label(8, 6, title, TEXT_MAIN));
        section.setActive(false);
        section.setClientSideWidget();
        return section;
    }

    private static LabelWidget label(int x, int y, String text, int color) {
        return new LabelWidget(x, y, text).setTextColor(color);
    }
}
