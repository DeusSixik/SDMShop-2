package dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers;

import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.shop.client.textures.ColorRectAndBorderTexture;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class ContextMenuWidget extends WidgetGroup {

    private static final int DEFAULT_WIDTH = 120;
    private static final int DEFAULT_ROW_HEIGHT = 18;
    private static final int DEFAULT_SEPARATOR_HEIGHT = 3;
    private static final int DEFAULT_PADDING = 2;
    private static final int DEFAULT_FILL_COLOR = 0xFF1E1F28;
    private static final int DEFAULT_BORDER_COLOR = 0xFF555866;
    private static final int DEFAULT_ROW_FILL_COLOR = 0x00000000;
    private static final int DEFAULT_ROW_HOVER_FILL_COLOR = 0xFF34384A;
    private static final int DEFAULT_ROW_DISABLED_FILL_COLOR = 0x22000000;
    private static final int DEFAULT_TEXT_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_DISABLED_TEXT_COLOR = 0xFF777777;
    private static final int DEFAULT_SEPARATOR_COLOR = 0xFF444755;
    private static final int DEFAULT_RADIUS = 2;

    protected int menuWidth = DEFAULT_WIDTH;
    protected int rowHeight = DEFAULT_ROW_HEIGHT;
    protected int separatorHeight = DEFAULT_SEPARATOR_HEIGHT;
    protected int padding = DEFAULT_PADDING;
    protected int rowSpacing;
    protected int fillColor = DEFAULT_FILL_COLOR;
    protected int borderColor = DEFAULT_BORDER_COLOR;
    protected int rowFillColor = DEFAULT_ROW_FILL_COLOR;
    protected int rowHoverFillColor = DEFAULT_ROW_HOVER_FILL_COLOR;
    protected int rowDisabledFillColor = DEFAULT_ROW_DISABLED_FILL_COLOR;
    protected int textColor = DEFAULT_TEXT_COLOR;
    protected int disabledTextColor = DEFAULT_DISABLED_TEXT_COLOR;
    protected int separatorColor = DEFAULT_SEPARATOR_COLOR;
    protected int radius = DEFAULT_RADIUS;
    protected boolean closeOnSelect = true;
    protected boolean closeOnOutsideClick = true;
    protected boolean closeOnFocusLost = true;
    protected WidgetGroup attachedParent;

    private boolean recomputingLayout;
    private boolean closing;

    public ContextMenuWidget() {
        this(0, 0, DEFAULT_WIDTH);
    }

    public ContextMenuWidget(int x, int y) {
        this(x, y, DEFAULT_WIDTH);
    }

    public ContextMenuWidget(int x, int y, int width) {
        super(x, y, Math.max(1, width), 1);
        this.menuWidth = Math.max(1, width);
        applyBackground();
        recomputeLayoutAndSize();
    }

    public ContextMenuWidget(Position selfPosition, Size size) {
        super(selfPosition, size);
        this.menuWidth = Math.max(1, size.width);
        applyBackground();
        recomputeLayoutAndSize();
    }

    public static ContextMenuWidget open(Widget owner, int mouseX, int mouseY) {
        return open(owner, new ContextMenuWidget(mouseX, mouseY));
    }

    public static ContextMenuWidget open(Widget owner, int mouseX, int mouseY, int width) {
        return open(owner, new ContextMenuWidget(mouseX, mouseY, width));
    }

    public static ContextMenuWidget open(Widget owner, ContextMenuWidget menu) {
        if (owner == null || menu == null) return null;

        Widget root = owner;
        while (root.getParent() != null) {
            root = root.getParent();
        }

        if (!(root instanceof WidgetGroup mainGroup)) {
            return null;
        }

        closeExistingContextMenus(mainGroup);
        menu.attachTo(mainGroup);
        return menu;
    }

    public ContextMenuWidget attachTo(WidgetGroup parent) {
        if (parent == null) return this;

        attachedParent = parent;
        clampToScreen();
        parent.addWidget(this);
        setFocus(true);
        return this;
    }

    public ContextMenuWidget close() {
        if (closing) return this;
        closing = true;
        WidgetGroup parent = attachedParent != null ? attachedParent : getParent();
        if (parent != null) {
            parent.removeWidget(this);
        }
        attachedParent = null;
        setFocus(false);
        closing = false;
        return this;
    }

    public ContextMenuWidget addItem(Component text, Runnable action) {
        return addItem(text, true, action);
    }

    public ContextMenuWidget addItem(String text, Runnable action) {
        return addItem(text == null ? Component.empty() : Component.literal(text), action);
    }

    public ContextMenuWidget addItem(Component text, boolean enabled, Runnable action) {
        ButtonWidget button = createItemButton(text, action);
        button.setActive(enabled);
        addMenuWidget(button);
        return this;
    }

    public ContextMenuWidget addDisabledItem(Component text) {
        return addItem(text, false, null);
    }

    public ContextMenuWidget addDisabledItem(String text) {
        return addDisabledItem(text == null ? Component.empty() : Component.literal(text));
    }

    public ContextMenuWidget addSeparator() {
        Widget separator = new Widget(0, 0, Math.max(1, getContentWidth()), Math.max(1, separatorHeight));
        separator.setBackground(new ColorRectTexture(separatorColor));
        addMenuWidget(separator);
        return this;
    }

    public ContextMenuWidget addMenuWidget(Widget widget) {
        if (widget == null) return this;

        super.addWidget(widget);
        recomputeLayoutAndSize();
        return this;
    }

    @Override
    public ContextMenuWidget addWidget(Widget widget) {
        return addMenuWidget(widget);
    }

    @Override
    public ContextMenuWidget addWidget(int index, Widget widget) {
        if (widget == null) return this;

        super.addWidget(index, widget);
        recomputeLayoutAndSize();
        return this;
    }

    @Override
    public ContextMenuWidget addWidgets(Widget... widgets) {
        if (widgets != null) {
            for (Widget widget : widgets) {
                addMenuWidget(widget);
            }
        }
        return this;
    }

    @Override
    public void removeWidget(Widget widget) {
        super.removeWidget(widget);
        recomputeLayoutAndSize();
    }

    @Override
    public void clearAllWidgets() {
        super.clearAllWidgets();
        recomputeLayoutAndSize();
    }

    public ContextMenuWidget setMenuWidth(int menuWidth) {
        this.menuWidth = Math.max(1, menuWidth);
        recomputeLayoutAndSize();
        return this;
    }

    public ContextMenuWidget setRowHeight(int rowHeight) {
        this.rowHeight = Math.max(1, rowHeight);
        recomputeLayoutAndSize();
        return this;
    }

    public ContextMenuWidget setSeparatorHeight(int separatorHeight) {
        this.separatorHeight = Math.max(1, separatorHeight);
        return this;
    }

    public ContextMenuWidget setPadding(int padding) {
        this.padding = Math.max(0, padding);
        recomputeLayoutAndSize();
        return this;
    }

    public ContextMenuWidget setRowSpacing(int rowSpacing) {
        this.rowSpacing = Math.max(0, rowSpacing);
        recomputeLayoutAndSize();
        return this;
    }

    public ContextMenuWidget setColors(int fillColor, int borderColor, int rowHoverFillColor) {
        this.fillColor = fillColor;
        this.borderColor = borderColor;
        this.rowHoverFillColor = rowHoverFillColor;
        applyBackground();
        updateItemStyles();
        return this;
    }

    public ContextMenuWidget setRowColors(int rowFillColor, int rowHoverFillColor, int rowDisabledFillColor) {
        this.rowFillColor = rowFillColor;
        this.rowHoverFillColor = rowHoverFillColor;
        this.rowDisabledFillColor = rowDisabledFillColor;
        updateItemStyles();
        return this;
    }

    public ContextMenuWidget setTextColors(int textColor, int disabledTextColor) {
        this.textColor = textColor;
        this.disabledTextColor = disabledTextColor;
        updateItemStyles();
        return this;
    }

    public ContextMenuWidget setSeparatorColor(int separatorColor) {
        this.separatorColor = separatorColor;
        for (Widget widget : widgets) {
            if (!(widget instanceof ButtonWidget)) {
                widget.setBackground(new ColorRectTexture(separatorColor));
            }
        }
        return this;
    }

    public ContextMenuWidget setRadius(int radius) {
        this.radius = Math.max(0, radius);
        applyBackground();
        updateItemStyles();
        return this;
    }

    public ContextMenuWidget setCloseOnSelect(boolean closeOnSelect) {
        this.closeOnSelect = closeOnSelect;
        return this;
    }

    public ContextMenuWidget keepOpenOnSelect() {
        return setCloseOnSelect(false);
    }

    public ContextMenuWidget closeOnSelect() {
        return setCloseOnSelect(true);
    }

    public ContextMenuWidget setCloseOnOutsideClick(boolean closeOnOutsideClick) {
        this.closeOnOutsideClick = closeOnOutsideClick;
        return this;
    }

    public ContextMenuWidget setCloseOnFocusLost(boolean closeOnFocusLost) {
        this.closeOnFocusLost = closeOnFocusLost;
        return this;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean inside = isMouseOverElement(mouseX, mouseY);
        boolean handled = inside && super.mouseClicked(mouseX, mouseY, button);

        if (!inside && closeOnOutsideClick) {
            close();
            return true;
        }

        return handled || inside;
    }

    @Override
    public void onFocusChanged(Widget lastFocus, Widget focus) {
        if (!closing && closeOnFocusLost && !isFocus()) {
            close();
        }
    }

    @Override
    public void drawInBackground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
    }

    private ButtonWidget createItemButton(Component text, Runnable action) {
        ButtonWidget button = new ButtonWidget(0, 0, getContentWidth(), rowHeight, text, clickData -> {
            if (closeOnSelect) {
                close();
            }
            if (action != null) {
                action.run();
            }
        });
        button.setTextColor(textColor)
                .setDisabledTextColor(disabledTextColor)
                .setTextPadding(4)
                .setButtonColors(rowFillColor, rowFillColor)
                .setHoverColors(rowHoverFillColor, rowHoverFillColor)
                .setClickedColors(rowHoverFillColor, rowHoverFillColor)
                .setDisabledTexture(new ColorRectTexture(rowDisabledFillColor));
        return button;
    }

    private void recomputeLayoutAndSize() {
        recomputeLayout();
        recomputeSizeToContent();
        clampToScreen();
    }

    @Override
    protected void recomputeLayout() {
        if (recomputingLayout) return;
        recomputingLayout = true;

        try {
            int y = padding;
            int contentWidth = getContentWidth();

            for (Widget widget : widgets) {
                if (widget == null || !widget.isVisible()) continue;

                widget.setSelfPosition(padding, y);
                widget.setSize(contentWidth, widget instanceof ButtonWidget ? rowHeight : Math.max(1, widget.getSizeHeight()));
                y += widget.getSizeHeight() + rowSpacing;
            }
        } finally {
            recomputingLayout = false;
        }
    }

    @Override
    protected void onSizeUpdate() {
        if (!recomputingLayout) {
            recomputeLayout();
        }
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

    private void recomputeSizeToContent() {
        int height = padding * 2;
        int visibleWidgets = 0;

        for (Widget widget : widgets) {
            if (widget == null || !widget.isVisible()) continue;

            height += widget.getSizeHeight();
            visibleWidgets++;
        }

        if (visibleWidgets > 1) {
            height += rowSpacing * (visibleWidgets - 1);
        }

        setSize(menuWidth, Math.max(1, height));
    }

    private int getContentWidth() {
        return Math.max(1, menuWidth - padding * 2);
    }

    private void applyBackground() {
        setBackground(new ColorRectAndBorderTexture(fillColor, borderColor, 1).setRadius(radius));
    }

    private void updateItemStyles() {
        for (Widget widget : widgets) {
            if (widget instanceof ButtonWidget button) {
                button.setTextColor(textColor)
                        .setDisabledTextColor(disabledTextColor)
                        .setButtonColors(rowFillColor, rowFillColor)
                        .setHoverColors(rowHoverFillColor, rowHoverFillColor)
                        .setClickedColors(rowHoverFillColor, rowHoverFillColor)
                        .setDisabledTexture(new ColorRectTexture(rowDisabledFillColor));
            }
        }
    }

    private void clampToScreen() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null || minecraft.getWindow() == null) return;

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        int x = Math.max(0, Math.min(getSelfPositionX(), Math.max(0, screenWidth - getSizeWidth())));
        int y = Math.max(0, Math.min(getSelfPositionY(), Math.max(0, screenHeight - getSizeHeight())));
        setSelfPosition(x, y);
    }

    private static void closeExistingContextMenus(WidgetGroup mainGroup) {
        for (int i = mainGroup.widgets.size() - 1; i >= 0; i--) {
            Widget widget = mainGroup.widgets.get(i);
            if (widget instanceof ContextMenuWidget contextMenu) {
                contextMenu.close();
            }
        }
    }
}
