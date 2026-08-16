package dev.sixik.sdmshop2.libs.shop_ldlib_extension;

import com.lowdragmc.lowdraglib.gui.texture.*;
import com.lowdragmc.lowdraglib.gui.widget.LabelWidget;
import com.lowdragmc.lowdraglib.gui.widget.ProgressWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.sixik.sdmshop2.libs.shop.client.ui.textures.ColorRectAndBorderTexture;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ButtonWidgetGroup;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopEmptyWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.*;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.*;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.table.IconsTable;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.table.InteractionTable;
import dev.sixik.sdmshop2.utils.ShopClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.joml.Vector4f;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public final class ShopRenderLibExtension {

    private static final int DEBUG_CONTENT_WIDTH = 1320;
    private static final int DEBUG_CONTENT_HEIGHT = 660;
    private static final int DEBUG_SCROLL_BAR_SIZE = 8;
    private static final int DEBUG_MIN_VIEWPORT_WIDTH = 360;
    private static final int DEBUG_MIN_VIEWPORT_HEIGHT = 260;

    private static final int TEXT_MAIN = 0xFFE8E8F0;
    private static final int TEXT_MUTED = 0xFF9DA3B0;
    private static final int PANEL_FILL = 0xFF20202A;
    private static final int PANEL_BORDER = 0xFF4C5265;
    private static final int HOVER_BORDER = 0xFFFFFFFF;

    public static void openUi() {
        ShopClientUtils.openWidget(debug());
    }

    private static WidgetGroup debug() {
        int viewportWidth = resolveDebugViewportWidth();
        int viewportHeight = resolveDebugViewportHeight();
        int contentViewportWidth = Math.max(1, viewportWidth - DEBUG_SCROLL_BAR_SIZE);
        int contentViewportHeight = Math.max(1, viewportHeight - DEBUG_SCROLL_BAR_SIZE);

        DebugScrollableWidgetGroup group = new DebugScrollableWidgetGroup(0, 0, viewportWidth, viewportHeight)
                .setViewport(0, 0, contentViewportWidth, contentViewportHeight);
        group.setClientSideWidget();
        group.setBackground(new ColorRectTexture(0xDD101018));

        group.addWidget(label(40, 28, "SDMShop2 LDLib widgets debug", 0xFFFFFFFF));
        group.addWidget(label(40, 44, "Inputs / Tables / Containers / DropDownBox / ScrollBar", TEXT_MUTED));

        MultiLineInputTextBox multiLineInput = createMultiLineInputDemo();
        multiLineInput.setSelfPosition(40, 94);
        group.addWidget(section(28, 64, 330, 150, "MultiLineInputTextBox"));
        group.addWidget(multiLineInput);

        InteractionTable interactionTable = createInteractionTable();
        interactionTable.setSelfPosition(400, 94);
        group.addWidget(section(388, 64, 250, 160, "InteractionTable widgets"));
        group.addWidget(interactionTable);

        VerticalContainer containerDemo = createContainerDemo();
        containerDemo.setSelfPosition(680, 94);
        group.addWidget(section(668, 64, 280, 220, "HBox / VBox"));
        group.addWidget(containerDemo);

        WidgetGroup textLabelDemo = createTextLabelDemo();
        textLabelDemo.setSelfPosition(990, 94);
        group.addWidget(section(978, 64, 330, 190, "TextLabel"));
        group.addWidget(textLabelDemo);

        IconsTable iconsTable = createIconsTable();
        iconsTable.setSelfPosition(40, 282);
        group.addWidget(section(28, 252, 170, 160, "IconsTable"));
        group.addWidget(iconsTable);

        InteractionTable interactionItemsTable = createInteractionItemsTable();
        interactionItemsTable.setSelfPosition(230, 282);
        group.addWidget(section(218, 252, 210, 160, "InteractionTable items"));
        group.addWidget(interactionItemsTable);

        HorizontalContainer shopWidgetsRow = createShopWidgetsRow();
        shopWidgetsRow.setSelfPosition(470, 282);
        group.addWidget(section(458, 252, 490, 96, "Shop widgets row"));
        group.addWidget(shopWidgetsRow);

        DropDownBox dropDownBox = createDropDownBoxDemo();
        dropDownBox.setSelfPosition(990, 306);
        group.addWidget(section(978, 276, 330, 130, "DropDownBox"));
        group.addWidget(dropDownBox);

        GridBox gridBox = createGridDemo();
        gridBox.setSelfPosition(40, 468);
        group.addWidget(section(28, 438, 330, 126, "GridBox"));
        group.addWidget(gridBox);

        TabBox tabBox = createTabBoxDemo();
        tabBox.setSelfPosition(400, 468);
        group.addWidget(section(388, 438, 360, 190, "TabBox"));
        group.addWidget(tabBox);

        WidgetGroup scrollBarDemo = createScrollBarDemo();
        scrollBarDemo.setSelfPosition(790, 468);
        group.addWidget(section(778, 438, 330, 190, "WidgetScrollBar"));
        group.addWidget(scrollBarDemo);

        group.rememberCanvasLayout();

        WidgetScrollBar verticalScrollBar = WidgetScrollBar.vertical(
                        contentViewportWidth,
                        0,
                        DEBUG_SCROLL_BAR_SIZE,
                        contentViewportHeight
                )
                .setControlOnly(true)
                .attachTo(group, 0, 0, contentViewportWidth, contentViewportHeight)
                .keepWidgetVisibility()
                .setContentLength(DEBUG_CONTENT_HEIGHT)
                .setWheelStep(28)
                .setScrollChangedListener(group::setContentScrollY)
                .setTrackTexture(new ColorRectTexture(0x6630303A))
                .setThumbTexture(new ColorRectTexture(0xFF6D7485))
                .setThumbHoverTexture(new ColorRectTexture(0xFFAAB2C5));
        verticalScrollBar.setHoverTooltips(Component.literal("Mouse wheel / drag to scroll debug UI vertically"));

        WidgetScrollBar horizontalScrollBar = WidgetScrollBar.horizontal(
                        0,
                        contentViewportHeight,
                        contentViewportWidth,
                        DEBUG_SCROLL_BAR_SIZE
                )
                .setControlOnly(true)
                .attachTo(group, 0, 0, contentViewportWidth, contentViewportHeight)
                .keepWidgetVisibility()
                .setContentLength(DEBUG_CONTENT_WIDTH)
                .setWheelStep(36)
                .setViewportWheelRequiresShift(true)
                .setScrollChangedListener(group::setContentScrollX)
                .setTrackTexture(new ColorRectTexture(0x6630303A))
                .setThumbTexture(new ColorRectTexture(0xFF6D7485))
                .setThumbHoverTexture(new ColorRectTexture(0xFFAAB2C5));
        horizontalScrollBar.setHoverTooltips(Component.literal("Shift + mouse wheel / drag to scroll debug UI horizontally"));

        Widget scrollCorner = new Widget(contentViewportWidth, contentViewportHeight, DEBUG_SCROLL_BAR_SIZE, DEBUG_SCROLL_BAR_SIZE)
                .setBackground(new ColorRectTexture(0xFF30303A));
        scrollCorner.setActive(false);

        verticalScrollBar.ignore(verticalScrollBar, horizontalScrollBar, scrollCorner);
        horizontalScrollBar.ignore(verticalScrollBar, horizontalScrollBar, scrollCorner);

        group.addFixedWidget(verticalScrollBar);
        group.addFixedWidget(horizontalScrollBar);
        group.addFixedWidget(scrollCorner);

        return group;
    }

    private static int resolveDebugViewportWidth() {
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        return Math.max(DEBUG_MIN_VIEWPORT_WIDTH, Math.min(DEBUG_CONTENT_WIDTH, screenWidth - 24));
    }

    private static int resolveDebugViewportHeight() {
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        return Math.max(DEBUG_MIN_VIEWPORT_HEIGHT, Math.min(DEBUG_CONTENT_HEIGHT, screenHeight - 24));
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

    private static InteractionTable createInteractionItemsTable() {
        InteractionTable table = new InteractionTable(4, 3)
                .setCellSize(32, 32)
                .setSpacing(6, 6);

        table.addElement(itemTableCell(Items.DIAMOND, "Diamond cell; white hover border + tooltip"));
        table.addElement(itemTableCell(Items.EMERALD, "Emerald cell; real Widget inside InteractionTable"));
        table.addElement(itemTableCell(Items.GOLD_INGOT, "Gold cell"));
        table.addElement(itemTableCell(Items.IRON_INGOT, "Iron cell"));
        table.addElement(itemTableCell(Items.NETHERITE_INGOT, "Scaled down Netherite cell"), 0.75f);
        table.addElement(itemTableCell(Items.REDSTONE, "Redstone cell"));
        table.addElement(itemTableCell(Items.LAPIS_LAZULI, "Lapis cell"));
        table.addElement(itemTableCell(Items.AMETHYST_SHARD, "Scaled up Amethyst cell"), 1.25f);
        table.addElement(itemTableCell(Items.CHEST, "Chest cell"));
        table.addElement(itemTableCell(Items.BARRIER, "Barrier cell"));

        table.setElementScale(1, 1.1f);
        table.setElementScale(6, 0.9f);
        return table;
    }

    private static String text = "diamond";
    private static String multilineText = "Unity-like multiline input\nEnter adds new lines\nMouse wheel scrolls\nScrollbar appears only when needed\nDrag thumb on the right\nLine 6\nLine 7\nLine 8";

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

        box.addWidget(new TextLabel(0, 0, 250, 18, Component.literal("VerticalContainer: stacks children"))
                .scaleToFit()
                .setHoverTooltips(Component.literal("This is our TextLabel inside VBox")));

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
        buttonGroup.addWidget(new TextLabel(8, 9, Component.literal("nested label")));
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
        row.addWidget(new TextLabel(0, 0, 120, 36, Component.literal("Auto-scale label"))
                .scaleToFit()
                .setHoverTooltips(Component.literal("TextLabel scaleToFit demo")));

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
        infoPage.addWidget(new TextLabel(0, 0, 300, 18, Component.literal("TabBox: switches visible pages"))
                .scaleToFit()
                .setHoverTooltips(Component.literal("This page is a plain WidgetGroup")));
        infoPage.addWidget(new TextLabel(0, 24, 300, 18, Component.literal("Inactive pages are hidden/deactivated"))
                .scaleToFit());

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

    private static WidgetGroup createScrollBarDemo() {
        WidgetGroup panel = new WidgetGroup(0, 0, 306, 150);
        panel.setBackground(new ColorRectAndBorderTexture(0xFF252733, PANEL_BORDER, 1));

        for (int i = 0; i < 10; i++) {
            ButtonWidget row = button("Scroll row " + (i + 1), "Direct child of this WidgetGroup; no wrapper content group");
            row.setSelfPosition(8, 8 + i * 32);
            row.setSize(270, 24);
            panel.addWidget(row);
        }

        WidgetScrollBar scrollBar = new WidgetScrollBar(WidgetScrollBar.Orientation.VERTICAL, 292, 8, 6, 130)
                .attachTo(panel, 8, 8, 278, 130)
                .hidePartiallyOutside()
                .setWheelStep(18)
                .setTrackTexture(new ColorRectTexture(0x6630303A))
                .setThumbTexture(new ColorRectTexture(0xFF6D7485))
                .setThumbHoverTexture(new ColorRectTexture(0xFFAAB2C5));
        scrollBar.setHoverTooltips(Component.literal("WidgetScrollBar controls existing panel children directly"));
        panel.addWidget(scrollBar);

        return panel;
    }

    private static DropDownBox createDropDownBoxDemo() {
        DropDownBox dropDown = new DropDownBox(0, 0, 292, 28)
                .setOptionHeight(30)
                .setMaxVisibleOptions(4)
                .setPadding(6, 4)
                .setRowSpacing(1)
                .setScrollBarWidth(6)
                .setScrollBarPadding(2);
        dropDown.setHoverTexture(new ColorBorderTexture(1, HOVER_BORDER));
        dropDown.setHoverTooltips(Component.literal("DropDownBox options are real Widget instances; wheel or drag scrollbar"));

        dropDown.addOption(new TextLabel(0, 0, 250, 30, Component.literal("TextLabel option"))
                .setPadding(6, 0)
                .setAlignment(TextLabel.HorizontalAlignment.LEFT, TextLabel.VerticalAlignment.CENTER)
                .setBackground(new ColorRectAndBorderTexture(0xFF262936, PANEL_BORDER, 1)));

        WidgetGroup diamondOption = iconCell(Items.DIAMOND, "WidgetGroup option with item icon");
        diamondOption.addWidget(new TextLabel(148, 0, 90, 28, Component.literal("selected row"))
                .setColor(TEXT_MUTED)
                .setAlignment(TextLabel.HorizontalAlignment.RIGHT, TextLabel.VerticalAlignment.CENTER));
        dropDown.addOption(diamondOption);

        dropDown.addOption(button("Button row", "ButtonWidget can be used as a DropDownBox option"));
        dropDown.addOption(iconCell(Items.EMERALD, "Another widget option"));
        dropDown.addOption(iconCell(Items.GOLD_INGOT, "Mouse wheel scrolls dropdown popup"));
        dropDown.addOption(new TextLabel(0, 0, 250, 30, Component.literal("Disabled option"))
                .setPadding(6, 0)
                .setAlignment(TextLabel.HorizontalAlignment.LEFT, TextLabel.VerticalAlignment.CENTER)
                .setColor(0xFF777777)
                .setBackground(new ColorRectAndBorderTexture(0xFF20222A, PANEL_BORDER, 1)));
        dropDown.setOptionEnabled(5, false);
        dropDown.setSelectedIndex(1);

        return dropDown;
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

    private static WidgetGroup itemTableCell(Item item, String tooltip) {
        WidgetGroup cell = new WidgetGroup(0, 0, 32, 32);
        cell.setBackground(new ColorRectAndBorderTexture(0xFF2A2A36, PANEL_BORDER, 1).setRadius(3));
        cell.setHoverTexture(new ColorBorderTexture(1, 0xFFFFFFFF));
        cell.setHoverTooltips(Component.literal(tooltip));
        cell.addWidget(new Widget(8, 8, 16, 16).setBackground(new ItemStackTexture(item)));
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

    private static final class DebugScrollableWidgetGroup extends WidgetGroup {

        private final Set<Widget> fixedWidgets = Collections.newSetFromMap(new IdentityHashMap<>());
        private final Map<Widget, Position> canvasBasePositions = new IdentityHashMap<>();

        private int viewportX;
        private int viewportY;
        private int viewportWidth;
        private int viewportHeight;
        private int contentScrollX;
        private int contentScrollY;
        private boolean applyingCanvasScroll;

        private DebugScrollableWidgetGroup(int x, int y, int width, int height) {
            super(x, y, width, height);
            setViewport(0, 0, width, height);
        }

        private DebugScrollableWidgetGroup setViewport(int x, int y, int width, int height) {
            this.viewportX = x;
            this.viewportY = y;
            this.viewportWidth = Math.max(0, width);
            this.viewportHeight = Math.max(0, height);
            return this;
        }

        private DebugScrollableWidgetGroup addFixedWidget(Widget widget) {
            if (widget != null) {
                fixedWidgets.add(widget);
                addWidget(widget);
            }
            return this;
        }

        private DebugScrollableWidgetGroup rememberCanvasLayout() {
            canvasBasePositions.clear();
            for (Widget widget : widgets) {
                if (!fixedWidgets.contains(widget)) {
                    canvasBasePositions.put(widget, widget.getSelfPosition());
                    widget.setVisible(true);
                }
            }
            applyContentScroll();
            return this;
        }

        private void setContentScrollX(int contentScrollX) {
            if (this.contentScrollX == contentScrollX) {
                return;
            }
            this.contentScrollX = Math.max(0, contentScrollX);
            applyContentScroll();
        }

        private void setContentScrollY(int contentScrollY) {
            if (this.contentScrollY == contentScrollY) {
                return;
            }
            this.contentScrollY = Math.max(0, contentScrollY);
            applyContentScroll();
        }

        private void applyContentScroll() {
            if (applyingCanvasScroll) {
                return;
            }

            applyingCanvasScroll = true;
            try {
                for (Map.Entry<Widget, Position> entry : canvasBasePositions.entrySet()) {
                    Widget widget = entry.getKey();
                    if (fixedWidgets.contains(widget)) {
                        continue;
                    }

                    Position base = entry.getValue();
                    widget.setSelfPosition(base.x - contentScrollX, base.y - contentScrollY);
                    widget.setVisible(true);
                }
            } finally {
                applyingCanvasScroll = false;
            }
        }

        @Override
        public void drawInBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            drawBackgroundTexture(graphics, mouseX, mouseY);

            withViewportScissor(graphics, () -> {
                for (Widget widget : widgets) {
                    if (!fixedWidgets.contains(widget) && widget.isVisible()) {
                        RenderSystem.setShaderColor(1, 1, 1, 1);
                        RenderSystem.enableBlend();
                        if (widget.inAnimate()) {
                            widget.getAnimation().drawInBackground(graphics, mouseX, mouseY, partialTicks);
                        } else {
                            widget.drawInBackground(graphics, mouseX, mouseY, partialTicks);
                        }
                    }
                }
            });

            drawFixedBackground(graphics, mouseX, mouseY, partialTicks);
        }

        @Override
        public void drawInForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            drawTooltipTexts(mouseX, mouseY);

            withViewportScissor(graphics, () -> {
                for (Widget widget : widgets) {
                    if (!fixedWidgets.contains(widget) && widget.isVisible()) {
                        RenderSystem.setShaderColor(1, 1, 1, 1);
                        RenderSystem.enableBlend();
                        if (widget.inAnimate()) {
                            widget.getAnimation().drawInForeground(graphics, mouseX, mouseY, partialTicks);
                        } else {
                            widget.drawInForeground(graphics, mouseX, mouseY, partialTicks);
                        }
                    }
                }
            });

            for (Widget widget : fixedWidgets) {
                if (widget.isVisible()) {
                    RenderSystem.setShaderColor(1, 1, 1, 1);
                    RenderSystem.enableBlend();
                    widget.drawInForeground(graphics, mouseX, mouseY, partialTicks);
                }
            }
        }

        @Override
        public void drawOverlay(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            if (overlay != null) {
                overlay.draw(graphics, mouseX, mouseY, getPositionX(), getPositionY(), getSizeWidth(), getSizeHeight());
            }

            withViewportScissor(graphics, () -> {
                for (Widget widget : widgets) {
                    if (!fixedWidgets.contains(widget) && widget.isVisible()) {
                        RenderSystem.setShaderColor(1, 1, 1, 1);
                        RenderSystem.enableBlend();
                        widget.drawOverlay(graphics, mouseX, mouseY, partialTicks);
                    }
                }
            });

            for (Widget widget : fixedWidgets) {
                if (widget.isVisible()) {
                    RenderSystem.setShaderColor(1, 1, 1, 1);
                    RenderSystem.enableBlend();
                    widget.drawOverlay(graphics, mouseX, mouseY, partialTicks);
                }
            }
        }

        @Override
        public boolean mouseWheelMove(double mouseX, double mouseY, double wheelDelta) {
            // Scrollbar directly under the cursor has the highest priority.
            // This lets users wheel over the visible bar/track itself without child widgets stealing it.
            for (int i = widgets.size() - 1; i >= 0; i--) {
                Widget widget = widgets.get(i);
                if (fixedWidgets.contains(widget) && widget.isVisible() && widget.isActive()
                        && widget.isMouseOverElement(mouseX, mouseY)
                        && widget.mouseWheelMove(mouseX, mouseY, wheelDelta)) {
                    return true;
                }
            }

            if (!isMouseOverViewport(mouseX, mouseY)) {
                return false;
            }

            for (int i = widgets.size() - 1; i >= 0; i--) {
                Widget widget = widgets.get(i);
                if (!fixedWidgets.contains(widget) && widget.isVisible() && widget.isActive()
                        && widget.isMouseOverElement(mouseX, mouseY)
                        && widget.mouseWheelMove(mouseX, mouseY, wheelDelta)) {
                    return true;
                }
            }

            // If no hovered child consumed the wheel, fall back to the global debug canvas scrollbars.
            // These bars can still treat the whole viewport as their wheel area.
            for (int i = widgets.size() - 1; i >= 0; i--) {
                Widget widget = widgets.get(i);
                if (fixedWidgets.contains(widget) && widget.isVisible() && widget.isActive()
                        && !widget.isMouseOverElement(mouseX, mouseY)
                        && widget.mouseWheelMove(mouseX, mouseY, wheelDelta)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            for (int i = widgets.size() - 1; i >= 0; i--) {
                Widget widget = widgets.get(i);
                if (fixedWidgets.contains(widget) && widget.isVisible() && widget.isActive()
                        && widget.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }

            if (!isMouseOverViewport(mouseX, mouseY)) {
                return false;
            }

            for (int i = widgets.size() - 1; i >= 0; i--) {
                Widget widget = widgets.get(i);
                if (!fixedWidgets.contains(widget) && widget.isVisible() && widget.isActive()
                        && widget.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
            for (int i = widgets.size() - 1; i >= 0; i--) {
                Widget widget = widgets.get(i);
                if (fixedWidgets.contains(widget) && widget.isVisible() && widget.isActive()
                        && widget.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
                    return true;
                }
            }

            if (!isMouseOverViewport(mouseX, mouseY)) {
                return false;
            }

            for (int i = widgets.size() - 1; i >= 0; i--) {
                Widget widget = widgets.get(i);
                if (!fixedWidgets.contains(widget) && widget.isVisible() && widget.isActive()
                        && widget.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            for (int i = widgets.size() - 1; i >= 0; i--) {
                Widget widget = widgets.get(i);
                if (widget.isVisible() && widget.isActive() && widget.mouseReleased(mouseX, mouseY, button)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public boolean mouseMoved(double mouseX, double mouseY) {
            for (int i = widgets.size() - 1; i >= 0; i--) {
                Widget widget = widgets.get(i);
                if (fixedWidgets.contains(widget) && widget.isVisible() && widget.isActive()
                        && widget.mouseMoved(mouseX, mouseY)) {
                    return true;
                }
            }

            if (!isMouseOverViewport(mouseX, mouseY)) {
                return false;
            }

            for (int i = widgets.size() - 1; i >= 0; i--) {
                Widget widget = widgets.get(i);
                if (!fixedWidgets.contains(widget) && widget.isVisible() && widget.isActive()
                        && widget.mouseMoved(mouseX, mouseY)) {
                    return true;
                }
            }

            return false;
        }

        private void drawFixedBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            for (Widget widget : fixedWidgets) {
                if (widget.isVisible()) {
                    RenderSystem.setShaderColor(1, 1, 1, 1);
                    RenderSystem.enableBlend();
                    widget.drawInBackground(graphics, mouseX, mouseY, partialTicks);
                }
            }
        }

        private void withViewportScissor(GuiGraphics graphics, Runnable draw) {
            int x = getPositionX() + viewportX;
            int y = getPositionY() + viewportY;
            int width = viewportWidth;
            int height = viewportHeight;

            var transform = graphics.pose().last().pose();
            var realPos = transform.transform(new Vector4f(x, y, 0, 1));
            var realPos2 = transform.transform(new Vector4f(x + width, y + height, 0, 1));

            graphics.enableScissor((int) realPos.x, (int) realPos.y, (int) realPos2.x, (int) realPos2.y);
            draw.run();
            graphics.disableScissor();
        }

        private boolean isMouseOverViewport(double mouseX, double mouseY) {
            return isMouseOver(
                    getPositionX() + viewportX,
                    getPositionY() + viewportY,
                    viewportWidth,
                    viewportHeight,
                    mouseX,
                    mouseY
            );
        }
    }
}
