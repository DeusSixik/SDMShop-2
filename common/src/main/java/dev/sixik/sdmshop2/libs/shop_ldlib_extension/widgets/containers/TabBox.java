package dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.shop.client.textures.ColorRectAndBorderTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

public class TabBox extends WidgetGroup {

    public enum TabPlacement {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }

    public enum HighlightMode {
        OUTLINE,
        FILL
    }

    protected final List<Tab> tabs = new ArrayList<>();

    protected int currentTab = -1;
    protected TabPlacement tabPlacement = TabPlacement.TOP;

    protected int tabHeight = 20;
    protected int tabWidth = 76;
    protected int tabSpacing = 2;
    protected int tabPaddingX = 8;
    protected int contentSpacing = 2;
    protected int contentPadding = 4;

    protected int backgroundColor = 0xFF15151D;
    protected int contentColor = 0xFF1D1D27;
    protected int tabColor = 0xFF2A2A36;
    protected int activeTabColor = 0xFF3B4252;
    protected int disabledTabColor = 0xFF20202A;
    protected int borderColor = 0xFF5C637A;
    protected int activeBorderColor = 0xFFFFFFFF;
    protected int activeHighlightColor = 0xFFFFFFFF;
    protected HighlightMode activeHighlightMode = HighlightMode.OUTLINE;
    protected int textColor = 0xFFE8E8F0;
    protected int disabledTextColor = 0xFF777D8D;

    protected IntConsumer tabChangedListener;

    private boolean recomputingLayout;

    public TabBox() {
        this(Position.ORIGIN, Size.ZERO);
    }

    public TabBox(Position selfPosition) {
        this(selfPosition, Size.ZERO);
    }

    public TabBox(Position selfPosition, Size size) {
        super(selfPosition, size);
        setDynamicSized(size.equals(Size.ZERO));
    }

    public TabBox(int x, int y, int width, int height) {
        super(x, y, width, height);
        setDynamicSized(width == 0 && height == 0);
    }

    public int addTab(String title, Widget page) {
        return addTab(Component.literal(title), page);
    }

    public int addTab(Component title, Widget page) {
        return insertTab(tabs.size(), title, page);
    }

    public int insertTab(int index, String title, Widget page) {
        return insertTab(index, Component.literal(title), page);
    }

    public int insertTab(int index, Component title, Widget page) {
        if (page == null) return -1;

        int safeIndex = Math.max(0, Math.min(index, tabs.size()));
        tabs.add(safeIndex, new Tab(title == null ? Component.empty() : title, page, true));
        super.addWidget(safeIndex, page);

        if (currentTab < 0) {
            currentTab = safeIndex;
        } else if (safeIndex <= currentTab) {
            currentTab++;
        }

        updateTabVisibility();
        recomputeLayoutAndSize();
        return safeIndex;
    }

    public TabBox removeTab(int index) {
        if (index < 0 || index >= tabs.size()) return this;

        Tab removed = tabs.remove(index);
        super.removeWidget(removed.page);

        if (tabs.isEmpty()) {
            currentTab = -1;
        } else if (currentTab == index) {
            currentTab = Math.min(index, tabs.size() - 1);
        } else if (index < currentTab) {
            currentTab--;
        }

        updateTabVisibility();
        recomputeLayoutAndSize();
        return this;
    }

    public TabBox clearTabs() {
        for (Tab tab : new ArrayList<>(tabs)) {
            super.removeWidget(tab.page);
        }
        tabs.clear();
        currentTab = -1;
        recomputeLayoutAndSize();
        return this;
    }

    public TabBox setCurrentTab(int index) {
        if (index < 0 || index >= tabs.size() || !tabs.get(index).enabled || currentTab == index) {
            return this;
        }

        currentTab = index;
        updateTabVisibility();
        recomputeLayout();
        if (tabChangedListener != null) {
            tabChangedListener.accept(currentTab);
        }
        return this;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public Widget getCurrentPage() {
        return currentTab >= 0 && currentTab < tabs.size() ? tabs.get(currentTab).page : null;
    }

    public Widget getPage(int index) {
        return index >= 0 && index < tabs.size() ? tabs.get(index).page : null;
    }

    public TabBox setTabTitle(int index, Component title) {
        if (index >= 0 && index < tabs.size()) {
            tabs.get(index).title = title == null ? Component.empty() : title;
            recomputeLayoutAndSize();
        }
        return this;
    }

    public TabBox setTabEnabled(int index, boolean enabled) {
        if (index < 0 || index >= tabs.size()) return this;

        tabs.get(index).enabled = enabled;
        if (!enabled && currentTab == index) {
            currentTab = findFirstEnabledTab();
        }
        updateTabVisibility();
        recomputeLayout();
        return this;
    }

    public TabBox setTabPlacement(TabPlacement tabPlacement) {
        this.tabPlacement = tabPlacement == null ? TabPlacement.TOP : tabPlacement;
        recomputeLayoutAndSize();
        return this;
    }

    public TabBox setTabHeight(int tabHeight) {
        this.tabHeight = Math.max(0, tabHeight);
        recomputeLayoutAndSize();
        return this;
    }

    public TabBox setTabWidth(int tabWidth) {
        this.tabWidth = Math.max(0, tabWidth);
        recomputeLayoutAndSize();
        return this;
    }

    public TabBox setTabSpacing(int tabSpacing) {
        this.tabSpacing = Math.max(0, tabSpacing);
        recomputeLayoutAndSize();
        return this;
    }

    public TabBox setTabPaddingX(int tabPaddingX) {
        this.tabPaddingX = Math.max(0, tabPaddingX);
        recomputeLayoutAndSize();
        return this;
    }

    public TabBox setContentSpacing(int contentSpacing) {
        this.contentSpacing = Math.max(0, contentSpacing);
        recomputeLayoutAndSize();
        return this;
    }

    public TabBox setContentPadding(int contentPadding) {
        this.contentPadding = Math.max(0, contentPadding);
        recomputeLayoutAndSize();
        return this;
    }

    public TabBox setColors(int backgroundColor, int contentColor, int tabColor, int activeTabColor, int borderColor) {
        this.backgroundColor = backgroundColor;
        this.contentColor = contentColor;
        this.tabColor = tabColor;
        this.activeTabColor = activeTabColor;
        this.borderColor = borderColor;
        return this;
    }

    public TabBox setActiveTabOutline(int color) {
        this.activeHighlightMode = HighlightMode.OUTLINE;
        this.activeHighlightColor = color;
        this.activeBorderColor = color;
        return this;
    }

    public TabBox setActiveTabFill(int color) {
        this.activeHighlightMode = HighlightMode.FILL;
        this.activeHighlightColor = color;
        return this;
    }

    public TabBox setActiveTabHighlight(HighlightMode mode, int color) {
        this.activeHighlightMode = mode == null ? HighlightMode.OUTLINE : mode;
        this.activeHighlightColor = color;
        if (this.activeHighlightMode == HighlightMode.OUTLINE) {
            this.activeBorderColor = color;
        }
        return this;
    }

    public TabBox setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public TabBox setDisabledTextColor(int disabledTextColor) {
        this.disabledTextColor = disabledTextColor;
        return this;
    }

    public TabBox setTabChangedListener(IntConsumer tabChangedListener) {
        this.tabChangedListener = tabChangedListener;
        return this;
    }

    @Override
    protected void recomputeLayout() {
        if (recomputingLayout) return;
        recomputingLayout = true;

        try {
            Rect contentRect = getContentRect();
            for (Tab tab : tabs) {
                tab.page.setSelfPosition(contentRect.x + contentPadding, contentRect.y + contentPadding);
                tab.page.setSize(
                        Math.max(0, contentRect.width - contentPadding * 2),
                        Math.max(0, contentRect.height - contentPadding * 2)
                );
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
            recomputeLayoutAndSize();
        }
    }

    @Override
    protected void onChildSizeUpdate(Widget child) {
        if (!recomputingLayout) {
            recomputeLayoutAndSize();
        }
    }

    @Override
    protected Size computeDynamicSize() {
        Size pageSize = computeMaxPageSize();
        int tabBarWidth = computeTabBarWidth();
        int tabBarHeight = computeTabBarHeight();

        if (isHorizontalTabs()) {
            int width = Math.max(tabBarWidth, pageSize.width + contentPadding * 2);
            int height = tabBarHeight + contentSpacing + pageSize.height + contentPadding * 2;
            return new Size(width, height);
        }

        int width = tabBarWidth + contentSpacing + pageSize.width + contentPadding * 2;
        int height = Math.max(tabBarHeight, pageSize.height + contentPadding * 2);
        return new Size(width, height);
    }

    @Override
    public void drawInBackground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        drawBox(graphics, mouseX, mouseY);
        drawTabs(graphics);
        drawWidgetsBackground(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawInForeground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        drawTooltipTexts(mouseX, mouseY);
        drawWidgetsForeground(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int tabIndex = getTabAt(mouseX, mouseY);
            if (tabIndex >= 0) {
                setCurrentTab(tabIndex);
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void drawBox(GuiGraphics graphics, int mouseX, int mouseY) {
        new ColorRectAndBorderTexture(backgroundColor, borderColor, 1)
                .setRadius(3)
                .draw(graphics, mouseX, mouseY, getPositionX(), getPositionY(), getSizeWidth(), getSizeHeight());

        Rect contentRect = getContentRect();
        new ColorRectAndBorderTexture(contentColor, borderColor, 1)
                .setRadius(2)
                .draw(graphics, mouseX, mouseY, getPositionX() + contentRect.x, getPositionY() + contentRect.y, contentRect.width, contentRect.height);
    }

    private void drawTabs(GuiGraphics graphics) {
        Font font = Minecraft.getInstance().font;
        for (int i = 0; i < tabs.size(); i++) {
            Tab tab = tabs.get(i);
            Rect rect = getTabRect(i);
            boolean active = i == currentTab;
            int fill = !tab.enabled
                    ? disabledTabColor
                    : active && activeHighlightMode == HighlightMode.FILL ? activeHighlightColor : tabColor;

            new ColorRectAndBorderTexture(fill, borderColor, 1)
                    .setRadius(2)
                    .draw(graphics, 0, 0, getPositionX() + rect.x, getPositionY() + rect.y, rect.width, rect.height);

            if (active && activeHighlightMode == HighlightMode.OUTLINE) {
                drawOutline(graphics, getPositionX() + rect.x, getPositionY() + rect.y, rect.width, rect.height, activeHighlightColor, 1);
            }

            String text = tab.title.getString();
            int textX = getPositionX() + rect.x + Math.max(2, (rect.width - font.width(text)) / 2);
            int textY = getPositionY() + rect.y + Math.max(1, (rect.height - font.lineHeight) / 2);
            graphics.drawString(font, text, textX, textY, tab.enabled ? textColor : disabledTextColor, false);
        }
    }

    private void drawOutline(GuiGraphics graphics, int x, int y, int width, int height, int color, int thickness) {
        if (color == 0 || width <= 0 || height <= 0) return;

        int line = Math.max(1, Math.min(thickness, Math.min(width, height)));
        graphics.fill(x, y, x + width, y + line, color);
        graphics.fill(x, y + height - line, x + width, y + height, color);
        graphics.fill(x, y + line, x + line, y + height - line, color);
        graphics.fill(x + width - line, y + line, x + width, y + height - line, color);
    }

    private int getTabAt(double mouseX, double mouseY) {
        for (int i = 0; i < tabs.size(); i++) {
            if (!tabs.get(i).enabled) continue;

            Rect rect = getTabRect(i);
            int x = getPositionX() + rect.x;
            int y = getPositionY() + rect.y;
            if (mouseX >= x && mouseY >= y && mouseX < x + rect.width && mouseY < y + rect.height) {
                return i;
            }
        }
        return -1;
    }

    private Rect getTabRect(int index) {
        if (isHorizontalTabs()) {
            int x = 0;
            for (int i = 0; i < index; i++) {
                x += getHorizontalTabWidth(i) + tabSpacing;
            }

            int y = tabPlacement == TabPlacement.TOP ? 0 : Math.max(0, getSizeHeight() - tabHeight);
            return new Rect(x, y, getHorizontalTabWidth(index), tabHeight);
        }

        int y = index * (tabHeight + tabSpacing);
        int x = tabPlacement == TabPlacement.LEFT ? 0 : Math.max(0, getSizeWidth() - tabWidth);
        return new Rect(x, y, tabWidth, tabHeight);
    }

    private Rect getContentRect() {
        if (tabPlacement == TabPlacement.TOP) {
            int y = tabHeight + contentSpacing;
            return new Rect(0, y, getSizeWidth(), Math.max(0, getSizeHeight() - y));
        }
        if (tabPlacement == TabPlacement.BOTTOM) {
            int height = Math.max(0, getSizeHeight() - tabHeight - contentSpacing);
            return new Rect(0, 0, getSizeWidth(), height);
        }
        if (tabPlacement == TabPlacement.LEFT) {
            int x = tabWidth + contentSpacing;
            return new Rect(x, 0, Math.max(0, getSizeWidth() - x), getSizeHeight());
        }

        int width = Math.max(0, getSizeWidth() - tabWidth - contentSpacing);
        return new Rect(0, 0, width, getSizeHeight());
    }

    private int getHorizontalTabWidth(int index) {
        if (index < 0 || index >= tabs.size()) return 0;

        int labelWidth = Minecraft.getInstance().font.width(tabs.get(index).title);
        return Math.max(tabWidth, labelWidth + tabPaddingX * 2);
    }

    private int computeTabBarWidth() {
        if (tabs.isEmpty()) return 0;
        if (!isHorizontalTabs()) return tabWidth;

        int width = 0;
        for (int i = 0; i < tabs.size(); i++) {
            width += getHorizontalTabWidth(i);
        }
        width += tabSpacing * Math.max(0, tabs.size() - 1);
        return width;
    }

    private int computeTabBarHeight() {
        if (tabs.isEmpty()) return 0;
        if (isHorizontalTabs()) return tabHeight;

        return tabs.size() * tabHeight + tabSpacing * Math.max(0, tabs.size() - 1);
    }

    private Size computeMaxPageSize() {
        int width = 0;
        int height = 0;
        for (Tab tab : tabs) {
            width = Math.max(width, tab.page.getSizeWidth());
            height = Math.max(height, tab.page.getSizeHeight());
        }
        return new Size(width, height);
    }

    private boolean isHorizontalTabs() {
        return tabPlacement == TabPlacement.TOP || tabPlacement == TabPlacement.BOTTOM;
    }

    private void updateTabVisibility() {
        for (int i = 0; i < tabs.size(); i++) {
            boolean current = i == currentTab && tabs.get(i).enabled;
            tabs.get(i).page.setVisible(current);
            tabs.get(i).page.setActive(current);
        }
    }

    private int findFirstEnabledTab() {
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).enabled) return i;
        }
        return -1;
    }

    private void recomputeLayoutAndSize() {
        recomputeLayout();
        recomputeSize();
    }

    protected static class Tab {
        protected Component title;
        protected final Widget page;
        protected boolean enabled;

        protected Tab(Component title, Widget page, boolean enabled) {
            this.title = title;
            this.page = page;
            this.enabled = enabled;
        }
    }

    protected record Rect(int x, int y, int width, int height) {
    }
}
