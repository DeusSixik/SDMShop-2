package dev.sixik.sdmshop2.libs.shop.client.ui.style;

import com.lowdragmc.lowdraglib.gui.editor.ColorPattern;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.widget.DraggableScrollableWidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.gui.widget.layout.Layout;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopScreenController;
import dev.sixik.sdmshop2.libs.shop.client.ui.textures.PixelBevelTexture;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopToolPanelElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.InputTextBox;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.TextLabel;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ModalWidget;
import dev.sixik.sdmshop2.libs.shop.client.ui.toast.ShopToasts;
import dev.sixik.sdmshop2.utils.ShopUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DefaultShopToolPanelElementRender implements WidgetRender {

    protected static final int SEARCH_HEIGHT = 18;
    protected static final int ADVANCED_BUTTON_SIZE = 18;
    protected static final int EDIT_BUTTON_WIDTH = 48;
    protected static final int RESET_SESSION_BUTTON_WIDTH = 84;
    protected static final int SEND_CHANGES_BUTTON_WIDTH = 88;
    protected static final int TOOL_GAP = 4;
    protected static final int MODAL_WIDTH = 280;
    protected static final int MODAL_HEIGHT = 240;
    protected static final int MODAL_PADDING = 6;
    protected static final int ROW_HEIGHT = 20;
    protected static final int ROW_GAP = 4;
    protected static final int SCROLLBAR_WIDTH = 3;

    protected InputTextBox searchBox;
    protected ButtonWidget advancedButton;
    protected ButtonWidget editModeButton;
    protected ButtonWidget resetSessionButton;
    protected ButtonWidget sendChangesButton;
    protected final Set<ResourceLocation> selectedCurrencyFilters = new LinkedHashSet<>();

    public DefaultShopToolPanelElementRender() {

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
        Widget owner = ctx.getOwner();

        searchBox = new InputTextBox().setTextResponder(ShopUIEvents::invokeSearchUpdate);
        searchBox.setPlaceholder(Component.literal("Search by item or currency..."));

        advancedButton = new ButtonWidget(Component.literal("★"));
        advancedButton.setTextPadding(0);
        advancedButton.setHoverTooltips(Component.literal("Advanced search filters"));
        advancedButton.setOnPressCallback(ignored -> openAdvancedFilters(owner));
        updateAdvancedButton();

        if (shouldShowEditModeButton()) {
            editModeButton = createEditModeButton(ctx);
            ctx.addWidget(editModeButton);
        }

        if (ctx instanceof ShopToolPanelElement toolPanel && toolPanel.getScreen().isEditorMode()) {
            resetSessionButton = createResetSessionButton(toolPanel);
            ctx.addWidget(resetSessionButton);
            sendChangesButton = createSendChangesButton(toolPanel);
            ctx.addWidget(sendChangesButton);
        }

        ctx.addWidget(searchBox);
        ctx.addWidget(advancedButton);
    }

    @Override
    public void alightWidgets(WidgetContextRender ctx) {
        final Size size = ctx.getOwner().getSize();
        final int w_root = size.width;
        final int h_root = size.height;

        final int searchWidth = Math.max(80, w_root / 4);
        final int groupWidth = searchWidth + TOOL_GAP + ADVANCED_BUTTON_SIZE;
        final int x = Math.max(0, w_root / 2 - groupWidth / 2);
        final int y = Math.max(0, h_root / 2 - SEARCH_HEIGHT / 2);

        if (editModeButton != null) {
            editModeButton.setSize(EDIT_BUTTON_WIDTH, SEARCH_HEIGHT);
            editModeButton.setSelfPosition(TOOL_GAP, y);
        }

        if (resetSessionButton != null) {
            resetSessionButton.setSize(RESET_SESSION_BUTTON_WIDTH, SEARCH_HEIGHT);
            int xReset = editModeButton == null
                    ? TOOL_GAP
                    : editModeButton.getSelfPositionX() + EDIT_BUTTON_WIDTH + TOOL_GAP;
            resetSessionButton.setSelfPosition(xReset, y);
        }

        if (sendChangesButton != null) {
            sendChangesButton.setSize(SEND_CHANGES_BUTTON_WIDTH, SEARCH_HEIGHT);
            sendChangesButton.setSelfPosition(Math.max(0, w_root - SEND_CHANGES_BUTTON_WIDTH - TOOL_GAP), y);
        }

        searchBox.setSize(searchWidth, SEARCH_HEIGHT);
        searchBox.setSelfPosition(x, y);

        advancedButton.setSize(ADVANCED_BUTTON_SIZE, ADVANCED_BUTTON_SIZE);
        advancedButton.setSelfPosition(x + searchWidth + TOOL_GAP, y);
    }

    protected boolean shouldShowEditModeButton() {
        return Minecraft.getInstance().player != null && ShopUtils.isPlayerAdmin(Minecraft.getInstance().player);
    }

    protected ButtonWidget createEditModeButton(WidgetContextRender ctx) {
        boolean editorMode = ctx.getEditSession() != null;
        ButtonWidget button = new ButtonWidget(Component.literal(editorMode ? "View" : "Edit"));
        button.setTextPadding(4);
        button.setMinTextScale(0.45f);
        button.setHoverTooltips(Component.literal(editorMode ? "Exit edit mode" : "Open shop editor"));
        button.setOnPressCallback(ignored -> {
            if (ctx instanceof ShopToolPanelElement toolPanel && toolPanel.getScreen().isEditorMode()) {
                ShopScreenController.openShop();
            } else {
                ShopScreenController.openShopEditor(ctx.getOwner());
            }
        });
        button.setClientSideWidget();
        styleButton(button, editorMode);
        return button;
    }

    protected ButtonWidget createResetSessionButton(ShopToolPanelElement toolPanel) {
        ButtonWidget button = new ButtonWidget(Component.literal("Reset Session"));
        button.setTextPadding(4);
        button.setMinTextScale(0.35f);
        button.setHoverTooltips(Component.literal("Discard all unsent editor session changes"));
        button.setOnPressCallback(ignored -> openResetSessionConfirm(toolPanel));
        button.setClientSideWidget();
        styleButton(button, false);
        return button;
    }

    protected void openResetSessionConfirm(ShopToolPanelElement toolPanel) {
        ShopEditSession session = toolPanel.getScreen().getEditSession();
        int actionCount = session == null ? 0 : session.historySize();

        ModalWidget modal = new ModalWidget(260, 118)
                .setTitle(Component.literal("Reset Edit Session"))
                .setCloseOnEsc(true)
                .setCloseOnOutsideClick(false);
        ModalWidget opened = ModalWidget.openNested(toolPanel, modal);
        if (opened == null) {
            return;
        }

        TextLabel warning = new TextLabel(0, 4, opened.getContentWidth(), 34,
                Component.literal("This will discard all unsent editor changes."));
        warning.setAutoSize(false)
                .setWrapText(true)
                .setColor(0xFFFFC95A)
                .setAlignment(TextLabel.HorizontalAlignment.CENTER, TextLabel.VerticalAlignment.CENTER);
        opened.addWidget(warning);

        TextLabel details = new TextLabel(0, 38, opened.getContentWidth(), 18,
                Component.literal("Recorded actions: " + actionCount));
        details.setAutoSize(false)
                .setColor(0xFFAEB4C6)
                .setAlignment(TextLabel.HorizontalAlignment.CENTER, TextLabel.VerticalAlignment.CENTER);
        opened.addWidget(details);

        int buttonY = Math.max(60, opened.getContentHeight() - 24);
        int buttonWidth = Math.max(1, opened.getContentWidth() / 2 - 4);

        ButtonWidget cancel = createActionButton(0, buttonY, buttonWidth, 20, Component.literal("Cancel"), ignored -> opened.close());
        ButtonWidget reset = createActionButton(buttonWidth + 8, buttonY, buttonWidth, 20, Component.literal("Reset"), ignored -> {
            boolean resetDone = toolPanel.getScreen().resetEditorSession();
            opened.close();
            if (resetDone) {
                ShopToasts.success(Component.literal("Edit session reset"));
            } else {
                ShopToasts.error(Component.literal("Failed to reset session"));
            }
        });
        styleButton(reset, true);

        opened.addWidget(cancel);
        opened.addWidget(reset);
    }

    protected ButtonWidget createSendChangesButton(ShopToolPanelElement toolPanel) {
        ButtonWidget button = new ButtonWidget(Component.literal("Send Changes"));
        button.setTextPadding(4);
        button.setMinTextScale(0.4f);
        button.setHoverTooltips(Component.literal("Send editor changes to server"));
        button.setOnPressCallback(ignored -> {
            button.setActive(false);
            button.setText(Component.literal("Sending..."));
            toolPanel.getScreen().sendEditorChanges().whenComplete((success, throwable) ->
                    Minecraft.getInstance().execute(() -> {
                        button.setActive(true);
                        button.setText(Component.literal("Send Changes"));
                        if (throwable != null) {
                            ShopToasts.error(Component.literal("Failed to send changes"));
                            return;
                        }

                        if (Boolean.TRUE.equals(success)) {
                            ShopToasts.success(Component.literal("Changes sent"));
                        } else {
                            ShopToasts.error(Component.literal("Changes rejected"));
                        }
                    })
            );
        });
        button.setClientSideWidget();
        styleButton(button, true);
        return button;
    }

    protected void openAdvancedFilters(Widget owner) {
        ModalWidget modal = new ModalWidget(MODAL_WIDTH, MODAL_HEIGHT)
                .setTitle(Component.literal("Advanced Search"))
                .setCloseOnEsc(true)
                .setCloseOnOutsideClick(true);

        ModalWidget opened = ModalWidget.openNested(owner, modal);
        if (opened == null) {
            return;
        }

        buildAdvancedFilters(opened);
    }

    protected void buildAdvancedFilters(ModalWidget modal) {
        modal.clearContent();

        final int contentWidth = Math.max(1, modal.getContentWidth());
        final int contentHeight = Math.max(1, modal.getContentHeight());
        final int footerHeight = 22;
        final int listY = 22;
        final int listHeight = Math.max(1, contentHeight - listY - footerHeight - MODAL_PADDING);
        final int listWidth = Math.max(1, contentWidth - SCROLLBAR_WIDTH);

        TextLabel title = new TextLabel(0, 0, contentWidth, 16, Component.literal("Currencies"))
                .setAutoSize(false)
                .setColor(0xFFE8E8F0)
                .setAlignment(TextLabel.HorizontalAlignment.LEFT, TextLabel.VerticalAlignment.CENTER);
        modal.addWidget(title);

        DraggableScrollableWidgetGroup scroll = new DraggableScrollableWidgetGroup(0, listY, contentWidth, listHeight);
        scroll.setScrollWheelDirection(DraggableScrollableWidgetGroup.ScrollWheelDirection.VERTICAL);
        scroll.setLayout(Layout.NONE);
        scroll.setYScrollBarWidth(SCROLLBAR_WIDTH);
        scroll.setYBarStyle(null, ColorPattern.WHITE.rectTexture().setRadius(2));
        scroll.setClientSideWidget();

        WidgetGroup rows = new ScrollClippedWidgetGroup(0, 0, listWidth, 1);
        rows.setLayout(Layout.NONE);
        rows.setDynamicSized(false);
        scroll.addWidget(rows);
        modal.addWidget(scroll);

        List<Map.Entry<ResourceLocation, ICurrency>> currencies = new ArrayList<>(SDMEconomyServiceClient.getAllCurrencies().entrySet());
        currencies.sort(Map.Entry.comparingByKey(Comparator.comparing(ResourceLocation::toString)));

        if (currencies.isEmpty()) {
            rows.addWidget(new TextLabel(0, 0, listWidth, ROW_HEIGHT, Component.literal("No currencies available"))
                    .setAutoSize(false)
                    .setColor(0xFFAEB4C6)
                    .setAlignment(TextLabel.HorizontalAlignment.CENTER, TextLabel.VerticalAlignment.CENTER));
            rows.setSize(listWidth, ROW_HEIGHT);
        } else {
            int y = 0;
            for (Map.Entry<ResourceLocation, ICurrency> entry : currencies) {
                ResourceLocation id = entry.getKey();
                ICurrency currency = entry.getValue();
                ButtonWidget row = createCurrencyButton(id, currency, listWidth, ROW_HEIGHT);
                row.setSelfPosition(0, y);
                rows.addWidget(row);
                y += ROW_HEIGHT + ROW_GAP;
            }
            rows.setSize(listWidth, Math.max(listHeight, y));
        }

        ButtonWidget clearButton = createActionButton(
                0,
                contentHeight - footerHeight,
                Math.max(1, contentWidth / 2 - MODAL_PADDING / 2),
                footerHeight,
                Component.literal("Clear"),
                ignored -> {
                    selectedCurrencyFilters.clear();
                    ShopUIEvents.invokeCurrencyFilterUpdate(selectedCurrencyFilters);
                    updateAdvancedButton();
                    buildAdvancedFilters(modal);
                }
        );

        ButtonWidget doneButton = createActionButton(
                contentWidth / 2 + MODAL_PADDING / 2,
                contentHeight - footerHeight,
                Math.max(1, contentWidth - (contentWidth / 2 + MODAL_PADDING / 2)),
                footerHeight,
                Component.literal("Done"),
                ignored -> modal.close()
        );

        modal.addWidget(clearButton);
        modal.addWidget(doneButton);
    }

    protected ButtonWidget createCurrencyButton(ResourceLocation id, ICurrency currency, int width, int height) {
        ButtonWidget button = new ButtonWidget(0, 0, width, height, currencyLabel(id, currency), null);
        button.setOnPressCallback(ignored -> {
            if (!selectedCurrencyFilters.remove(id)) {
                selectedCurrencyFilters.add(id);
            }

            ShopUIEvents.invokeCurrencyFilterUpdate(selectedCurrencyFilters);
            updateAdvancedButton();
            updateCurrencyButton(button, id, currency);
        });
        button.setTextPadding(4);
        button.setMinTextScale(0.35f);
        button.setHoverTooltips(currency.getDisplayName().copy().append(Component.literal(" (" + id + ")")));
        button.setClientSideWidget();
        updateCurrencyButton(button, id, currency);

        return button;
    }

    protected Component currencyLabel(ResourceLocation id, ICurrency currency) {
        String prefix = selectedCurrencyFilters.contains(id) ? "✓ " : "";
        return currency.getDisplayName().copy().append(Component.literal("  §8" + prefix + id));
    }

    protected void updateCurrencyButton(ButtonWidget button, ResourceLocation id, ICurrency currency) {
        button.setText(currencyLabel(id, currency));
        styleButton(button, selectedCurrencyFilters.contains(id));
    }

    protected ButtonWidget createActionButton(int x, int y, int width, int height, Component text, java.util.function.Consumer<Object> onPress) {
        ButtonWidget button = new ButtonWidget(x, y, width, height, text, ignored -> onPress.accept(ignored));
        button.setClientSideWidget();
        styleButton(button, false);
        return button;
    }

    protected void updateAdvancedButton() {
        if (advancedButton == null) {
            return;
        }

        advancedButton.setText(Component.literal("★"));
        styleButton(advancedButton, !selectedCurrencyFilters.isEmpty());
        if (selectedCurrencyFilters.isEmpty()) {
            advancedButton.setHoverTooltips(Component.literal("Advanced search filters"));
        } else {
            advancedButton.setHoverTooltips(Component.literal("Currency filters: " + selectedCurrencyFilters.size()));
        }
    }

    protected void styleButton(ButtonWidget button, boolean active) {
        if (active) {
            button.setButtonTexture(PixelBevelTexture.accent());
            button.setHoverTexture(new PixelBevelTexture(0xFFFFC95A, PixelBevelTexture.ACCENT_LOW_COLOR, PixelBevelTexture.ACCENT_HIGH_COLOR, 0.7f));
            button.setClickedTexture(PixelBevelTexture.accent().pressed());
            button.setTextColor(0xFF1A1200);
            return;
        }

        button.setButtonTexture(PixelBevelTexture.panelLow());
        button.setHoverTexture(new PixelBevelTexture(0xFF252C3D, PixelBevelTexture.LINE_LOW_COLOR, PixelBevelTexture.LINE_HIGH_COLOR, 0.7f));
        button.setClickedTexture(PixelBevelTexture.panelLow().pressed());
        button.setTextColor(0xFFFFFFFF);
    }

    protected static boolean isInsideScrollViewport(Widget widget, double mouseX, double mouseY) {
        Widget current = widget;
        while (current != null) {
            if (current instanceof DraggableScrollableWidgetGroup scroll) {
                return mouseX >= scroll.getPositionX()
                        && mouseY >= scroll.getPositionY()
                        && mouseX < scroll.getPositionX() + scroll.getSizeWidth()
                        && mouseY < scroll.getPositionY() + scroll.getSizeHeight();
            }

            current = current.getParent();
        }

        return true;
    }

    protected static class ScrollClippedWidgetGroup extends WidgetGroup {

        protected ScrollClippedWidgetGroup(int x, int y, int width, int height) {
            super(x, y, width, height);
        }

        @Override
        public Widget getHoverElement(double mouseX, double mouseY) {
            return isInsideScrollViewport(this, mouseX, mouseY) ? super.getHoverElement(mouseX, mouseY) : null;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return isInsideScrollViewport(this, mouseX, mouseY) && super.mouseClicked(mouseX, mouseY, button);
        }
    }
}
