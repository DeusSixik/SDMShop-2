package dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers;

import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.shop.client.ui.textures.PixelBevelTexture;
import dev.sixik.sdmshop2.libs.shop.client.ui.toast.ShopToasts;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.TextLabel;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class ModalWidget extends WidgetGroup {

    private static final int DEFAULT_WIDTH = 180;
    private static final int DEFAULT_HEIGHT = 120;
    private static final int DEFAULT_PANEL_PADDING = 6;
    private static final int DEFAULT_TITLE_HEIGHT = 18;
    private static final int DEFAULT_CLOSE_BUTTON_SIZE = 12;
    private static final int DEFAULT_OVERLAY_COLOR = 0xAA000000;

    protected final WidgetGroup panel;
    protected final WidgetGroup content;
    @Nullable
    @Getter
    protected TextLabel titleLabel;
    @Nullable
    @Getter
    protected ButtonWidget closeButton;
    @Nullable
    @Getter
    protected WidgetGroup attachedParent;
    protected final Map<Widget, Boolean> blockedWidgets = new IdentityHashMap<>();
    protected final Map<Widget, List<Component>> blockedWidgetTooltips = new IdentityHashMap<>();

    protected int panelWidth;
    protected int panelHeight;
    protected int panelPadding = DEFAULT_PANEL_PADDING;
    protected int titleHeight = DEFAULT_TITLE_HEIGHT;
    protected int overlayColor = DEFAULT_OVERLAY_COLOR;
    protected boolean closeOnEsc = true;
    protected boolean closeOnOutsideClick = true;
    protected boolean closeButtonVisible = true;
    protected boolean blockWidgetsBehind = true;
    protected boolean blockHoverBehind = true;
    protected boolean closing;
    protected boolean initialized;

    public ModalWidget() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public ModalWidget(int width, int height) {
        super(0, 0, 1, 1);
        initializeSize(width, height);

        setBackground(new ColorRectTexture(overlayColor));

        this.panel = new WidgetGroup(Position.ORIGIN, new Size(panelWidth, panelHeight));
        this.panel.setBackground(PixelBevelTexture.panel());
        super.addWidget(panel);

        this.content = new WidgetGroup(Position.ORIGIN, Size.ZERO);
        panel.addWidget(content);

        setTitle(Component.empty());
        setCloseButtonVisible(true);
        initialized = true;
        recomputeLayout();
    }

    protected void initializeSize(int width, int height) {
        this.panelWidth = Math.max(1, width);
        this.panelHeight = Math.max(1, height);
    }

    public static ModalWidget open(Widget owner) {
        return open(owner, new ModalWidget());
    }

    public static ModalWidget open(Widget owner, int width, int height) {
        return open(owner, new ModalWidget(width, height));
    }

    public static ModalWidget open(Widget owner, Component title, int width, int height) {
        return open(owner, new ModalWidget(width, height).setTitle(title));
    }

    public static ModalWidget open(Widget owner, String title, int width, int height) {
        return open(owner, title == null ? Component.empty() : Component.literal(title), width, height);
    }

    public static <MODAL extends ModalWidget> MODAL open(Widget owner, MODAL modal) {
        return open(owner, modal, true);
    }

    public static <MODAL extends ModalWidget> MODAL openNested(Widget owner, MODAL modal) {
        return open(owner, modal, false);
    }

    private static <MODAL extends ModalWidget> MODAL open(Widget owner, MODAL modal, boolean closeExisting) {
        if (owner == null || modal == null) return null;

        Widget root = owner;
        while (root.getParent() != null) {
            root = root.getParent();
        }

        if (!(root instanceof WidgetGroup mainGroup)) {
            return null;
        }

        if (closeExisting) {
            closeExistingModals(mainGroup);
        }
        DropDownBox.closeActivePopup();
        modal.attachTo(mainGroup);
        return modal;
    }

    public static boolean isCoveredByHigherModal(Widget widget, double mouseX, double mouseY) {
        if (widget == null) return false;

        Widget root = widget;
        while (root.getParent() != null) {
            root = root.getParent();
        }

        if (!(root instanceof WidgetGroup mainGroup)) {
            return false;
        }

        for (int i = mainGroup.widgets.size() - 1; i >= 0; i--) {
            Widget candidate = mainGroup.widgets.get(i);
            if (!(candidate instanceof ModalWidget modal) || !modal.isVisible()) {
                continue;
            }

            if (isAncestorOrSelf(modal, widget)) {
                return false;
            }

            if (modal.isMouseOverElement(mouseX, mouseY)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isAncestorOrSelf(Widget ancestor, Widget widget) {
        Widget current = widget;
        while (current != null) {
            if (current == ancestor) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }

    public ModalWidget attachTo(WidgetGroup parent) {
        if (parent == null) return this;

        attachedParent = parent;
        resizeToScreen();
        centerPanel();
        blockWidgetsBehind();
        parent.addWidget(this);
        ShopToasts.bringOverlayToFront();
        setFocus(true);
        return this;
    }

    public ModalWidget close() {
        if (closing) return this;
        closing = true;
        WidgetGroup parent = attachedParent != null ? attachedParent : getParent();
        if (parent != null) {
            parent.removeWidget(this);
        }
        restoreHoverBehind();
        restoreWidgetsBehind();
        attachedParent = null;
        setFocus(false);
        closing = false;
        return this;
    }

    public ModalWidget setTitle(Component title) {
        Component safeTitle = title == null ? Component.empty() : title;
        boolean hasTitle = !safeTitle.getString().isEmpty();

        if (!hasTitle) {
            if (titleLabel != null) {
                panel.removeWidget(titleLabel);
                titleLabel = null;
            }
            recomputeLayout();
            return this;
        }

        if (titleLabel == null) {
            titleLabel = (TextLabel) new TextLabel(safeTitle)
                    .setAutoSize(false)
                    .setWrapText(false)
                    .setMaxLines(1)
                    .setPadding(0)
                    .setAlignment(TextLabel.HorizontalAlignment.CENTER, TextLabel.VerticalAlignment.CENTER);
            panel.addWidget(titleLabel);
        } else {
            titleLabel.setText(safeTitle);
        }

        recomputeLayout();
        return this;
    }

    public ModalWidget setTitle(String title) {
        return setTitle(title == null ? Component.empty() : Component.literal(title));
    }

    public ModalWidget setPanelSize(int width, int height) {
        this.panelWidth = Math.max(1, width);
        this.panelHeight = Math.max(1, height);
        panel.setSize(panelWidth, panelHeight);
        centerPanel();
        recomputeLayout();
        return this;
    }

    public ModalWidget setPanelPadding(int panelPadding) {
        this.panelPadding = Math.max(0, panelPadding);
        recomputeLayout();
        return this;
    }

    public ModalWidget setTitleHeight(int titleHeight) {
        this.titleHeight = Math.max(0, titleHeight);
        recomputeLayout();
        return this;
    }

    public ModalWidget setOverlayColor(int overlayColor) {
        this.overlayColor = overlayColor;
        setBackground(new ColorRectTexture(overlayColor));
        return this;
    }

    public ModalWidget setPanelBackground(IGuiTexture... textures) {
        panel.setBackground(textures);
        return this;
    }

    public ModalWidget setCloseOnEsc(boolean closeOnEsc) {
        this.closeOnEsc = closeOnEsc;
        return this;
    }

    public ModalWidget setCloseOnOutsideClick(boolean closeOnOutsideClick) {
        this.closeOnOutsideClick = closeOnOutsideClick;
        return this;
    }

    public ModalWidget setCloseButtonVisible(boolean closeButtonVisible) {
        this.closeButtonVisible = closeButtonVisible;

        if (!closeButtonVisible) {
            if (closeButton != null) {
                panel.removeWidget(closeButton);
                closeButton = null;
            }
            recomputeLayout();
            return this;
        }

        if (closeButton == null) {
            closeButton = new ButtonWidget(Component.literal("×"));
            closeButton.setTextPadding(0)
                    .setTextScale(0.75f)
                    .setButtonTexture(PixelBevelTexture.panelLow())
                    .setHoverTexture(PixelBevelTexture.accent())
                    .setClickedTexture(PixelBevelTexture.panelLow().pressed())
                    .onPress(clickData -> close());
            panel.addWidget(closeButton);
        }

        recomputeLayout();
        return this;
    }

    public ModalWidget setBlockWidgetsBehind(boolean blockWidgetsBehind) {
        this.blockWidgetsBehind = blockWidgetsBehind;
        if (blockWidgetsBehind) {
            blockWidgetsBehind();
        } else {
            restoreWidgetsBehind();
        }
        return this;
    }

    public ModalWidget setBlockHoverBehind(boolean blockHoverBehind) {
        this.blockHoverBehind = blockHoverBehind;
        if (blockHoverBehind) {
            blockHoverBehind();
        } else {
            restoreHoverBehind();
        }
        return this;
    }

    public ModalWidget hideCloseButton() {
        return setCloseButtonVisible(false);
    }

    public WidgetGroup getPanel() {
        return panel;
    }

    public WidgetGroup getContent() {
        return content;
    }

    public int getContentWidth() {
        return content.getSizeWidth();
    }

    public int getContentHeight() {
        return content.getSizeHeight();
    }

    @Override
    public ModalWidget addWidget(Widget widget) {
        if (!initialized) {
            super.addWidget(widget);
            return this;
        }

        content.addWidget(widget);
        return this;
    }

    @Override
    public ModalWidget addWidget(int index, Widget widget) {
        if (!initialized) {
            super.addWidget(index, widget);
            return this;
        }

        content.addWidget(index, widget);
        return this;
    }

    @Override
    public ModalWidget addWidgets(Widget... widgets) {
        if (widgets != null) {
            for (Widget widget : widgets) {
                addWidget(widget);
            }
        }
        return this;
    }

    public ModalWidget addContentWidget(Widget widget) {
        return addWidget(widget);
    }

    public ModalWidget addContentWidget(int index, Widget widget) {
        return addWidget(index, widget);
    }

    public void clearContent() {
        content.clearAllWidgets();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        blockWidgetsBehind();

        boolean insidePanel = isMouseOverPanel(mouseX, mouseY);
        if (insidePanel) {
            boolean handled = super.mouseClicked(mouseX, mouseY, button);
            if (!handled) {
                setFocus(true);
            }
            return true;
        }

        if (closeOnOutsideClick) {
            close();
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        blockWidgetsBehind();
        if (isMouseOverPanel(mouseX, mouseY)) {
            super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        blockWidgetsBehind();
        super.mouseReleased(mouseX, mouseY, button);
        return true;
    }

    @Override
    public boolean mouseWheelMove(double mouseX, double mouseY, double wheelDelta) {
        blockWidgetsBehind();
        if (isMouseOverPanel(mouseX, mouseY)) {
            super.mouseWheelMove(mouseX, mouseY, wheelDelta);
        }
        return true;
    }

    @Override
    public boolean mouseMoved(double mouseX, double mouseY) {
        blockWidgetsBehind();
        if (isMouseOverPanel(mouseX, mouseY)) {
            super.mouseMoved(mouseX, mouseY);
        }
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        blockWidgetsBehind();
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }

        if (closeOnEsc && keyCode == GLFW.GLFW_KEY_ESCAPE) {
            close();
            return true;
        }

        return true;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        blockWidgetsBehind();
        super.charTyped(codePoint, modifiers);
        return true;
    }

    @Override
    public boolean isMouseOverElement(double mouseX, double mouseY) {
        return isVisible()
                && mouseX >= getPositionX()
                && mouseY >= getPositionY()
                && mouseX < getPositionX() + getSizeWidth()
                && mouseY < getPositionY() + getSizeHeight();
    }

    @Override
    public @Nullable Widget getHoverElement(double mouseX, double mouseY) {
        blockHoverBehind();
        if (!isVisible() || !isMouseOverElement(mouseX, mouseY)) return null;
        if (isMouseOverPanel(mouseX, mouseY)) {
            Widget hovered = super.getHoverElement(mouseX, mouseY);
            return hovered == null ? panel : hovered;
        }
        return this;
    }

    @Override
    public void drawInForeground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        blockHoverBehind();
        clearHoverTooltip();
        super.drawInForeground(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawInBackground(@NonNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        resizeToScreen();
        centerPanel();
        blockWidgetsBehind();
        blockHoverBehind();
        super.drawInBackground(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void onSizeUpdate() {
        centerPanel();
        recomputeLayout();
    }

    protected void recomputeLayout() {
        if (panel == null || content == null) {
            return;
        }

        panel.setSize(panelWidth, panelHeight);

        int titleBlockHeight = titleLabel == null ? 0 : titleHeight;
        int contentX = panelPadding;
        int contentY = panelPadding + titleBlockHeight;
        int contentWidth = Math.max(1, panelWidth - panelPadding * 2);
        int contentHeight = Math.max(1, panelHeight - panelPadding * 2 - titleBlockHeight);

        if (titleLabel != null) {
            int titleRightPadding = closeButton == null ? 0 : DEFAULT_CLOSE_BUTTON_SIZE + panelPadding;
            titleLabel.setSelfPosition(panelPadding + titleRightPadding, panelPadding);
            titleLabel.setSize(Math.max(1, panelWidth - panelPadding * 2 - titleRightPadding * 2), titleHeight);
        }

        if (closeButton != null) {
            closeButton.setSelfPosition(
                    Math.max(panelPadding, panelWidth - panelPadding - DEFAULT_CLOSE_BUTTON_SIZE),
                    panelPadding
            );
            closeButton.setSize(DEFAULT_CLOSE_BUTTON_SIZE, DEFAULT_CLOSE_BUTTON_SIZE);
        }

        content.setSelfPosition(contentX, contentY);
        content.setSize(contentWidth, contentHeight);
    }

    protected void resizeToScreen() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null || minecraft.getWindow() == null) return;

        int screenWidth = Math.max(1, minecraft.getWindow().getGuiScaledWidth());
        int screenHeight = Math.max(1, minecraft.getWindow().getGuiScaledHeight());
        if (getSizeWidth() != screenWidth || getSizeHeight() != screenHeight) {
            setSelfPosition(0, 0);
            setSize(screenWidth, screenHeight);
        }
    }

    protected void centerPanel() {
        int x = Math.max(0, (getSizeWidth() - panelWidth) / 2);
        int y = Math.max(0, (getSizeHeight() - panelHeight) / 2);
        panel.setSelfPosition(x, y);
    }

    protected boolean isMouseOverPanel(double mouseX, double mouseY) {
        int x = panel.getPositionX();
        int y = panel.getPositionY();
        return mouseX >= x
                && mouseY >= y
                && mouseX < x + panel.getSizeWidth()
                && mouseY < y + panel.getSizeHeight();
    }

    protected void blockWidgetsBehind() {
        if (!blockWidgetsBehind || closing || attachedParent == null) return;

        for (Widget widget : attachedParent.widgets) {
            if (widget == this || widget instanceof ModalWidget) continue;
            if (isModalOverlayWidget(widget)) {
                restoreBlockedWidget(widget);
                continue;
            }

            blockedWidgets.putIfAbsent(widget, widget.isActive());
            if (widget.isActive()) {
                widget.setActive(false);
            }
        }
    }

    protected void blockHoverBehind() {
        if (!blockHoverBehind || closing || attachedParent == null) return;

        for (Widget widget : attachedParent.widgets) {
            if (widget == this || widget instanceof ModalWidget) continue;
            if (isModalOverlayWidget(widget)) {
                restoreBlockedHover(widget);
                continue;
            }
            blockHover(widget);
        }
    }

    protected boolean isModalOverlayWidget(Widget widget) {
        return widget instanceof DropDownBox.PopupInputLayer;
    }

    protected void restoreBlockedWidget(Widget widget) {
        Boolean active = blockedWidgets.remove(widget);
        if (active != null) {
            widget.setActive(active);
        }
    }

    protected void restoreBlockedHover(Widget widget) {
        List<Component> tooltipTexts = blockedWidgetTooltips.remove(widget);
        if (tooltipTexts != null) {
            widget.getTooltipTexts().clear();
            widget.getTooltipTexts().addAll(tooltipTexts);
        }
    }

    protected void blockHover(Widget widget) {
        List<Component> tooltipTexts = widget.getTooltipTexts();
        if (!tooltipTexts.isEmpty()) {
            blockedWidgetTooltips.putIfAbsent(widget, new ArrayList<>(tooltipTexts));
            tooltipTexts.clear();
        }

        if (widget instanceof WidgetGroup group) {
            for (Widget child : group.widgets) {
                blockHover(child);
            }
        }
    }

    protected void restoreWidgetsBehind() {
        if (blockedWidgets.isEmpty()) return;

        for (Map.Entry<Widget, Boolean> entry : blockedWidgets.entrySet()) {
            entry.getKey().setActive(entry.getValue());
        }
        blockedWidgets.clear();
    }

    protected void restoreHoverBehind() {
        if (blockedWidgetTooltips.isEmpty()) return;

        for (Map.Entry<Widget, List<Component>> entry : blockedWidgetTooltips.entrySet()) {
            List<Component> tooltipTexts = entry.getKey().getTooltipTexts();
            tooltipTexts.clear();
            tooltipTexts.addAll(entry.getValue());
        }
        blockedWidgetTooltips.clear();
    }

    protected void clearHoverTooltip() {
        if (gui != null && gui.getModularUIGui() != null) {
            gui.getModularUIGui().setHoverTooltip(Collections.emptyList(), null, null, null);
        }
    }

    protected static void closeExistingModals(WidgetGroup mainGroup) {
        for (int i = mainGroup.widgets.size() - 1; i >= 0; i--) {
            Widget widget = mainGroup.widgets.get(i);
            if (widget instanceof ModalWidget modal) {
                modal.close();
            }
        }
    }
}
