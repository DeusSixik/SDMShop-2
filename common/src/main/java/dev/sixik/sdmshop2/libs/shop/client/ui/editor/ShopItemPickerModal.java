package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.shop.client.ui.ShopScreenController;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityIcons;
import dev.sixik.sdmshop2.libs.shop.client.ui.theme.ShopEditorStyles;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopItemWidget;
import dev.sixik.unigui.api.event.EventPhase;
import dev.sixik.unigui.api.event.PointerEnteredEvent;
import dev.sixik.unigui.api.event.PointerExitedEvent;
import dev.sixik.unigui.api.event.PointerPressedEvent;
import dev.sixik.unigui.api.event.PointerReleasedEvent;
import dev.sixik.unigui.api.input.PointerButton;
import dev.sixik.unigui.api.layout.*;
import dev.sixik.unigui.api.math.MutableColor;
import dev.sixik.unigui.api.math.MutableRect;
import dev.sixik.unigui.api.math.RectView;
import dev.sixik.unigui.api.widget.Widget;
import dev.sixik.unigui.widgets.containers.*;
import dev.sixik.unigui.widgets.display.Label;
import dev.sixik.unigui.widgets.feedback.OverlayHostAware;
import dev.sixik.unigui.widgets.feedback.OverlayLayer;
import dev.sixik.unigui.widgets.interaction.Button;
import dev.sixik.unigui.widgets.interaction.TextField;
import dev.sixik.unigui.widgets.minecraft.MinecraftItemTooltip;

import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.resources.language.I18n;

/**
 * Модальное окно выбора предмета (Select Item) в стиле Unity Dark со скроллбаром. Позволяет
 * выбирать предмет как из общего реестра (All Items), так и из инвентаря игрока (Inventory).
 */
public class ShopItemPickerModal extends Box implements OverlayHostAware {

    private static final float MODAL_WIDTH = 560.0f;
    private static final float MAX_MODAL_HEIGHT = 440.0f;

    public enum ItemSourceTab {
        ALL_ITEMS,
        INVENTORY
    }

    private final ItemStack initialStack;
    private final Consumer<ItemStack> onSelect;
    private final VBox windowCard = new VBox();

    private ItemSourceTab currentTab = ItemSourceTab.ALL_ITEMS;
    private String searchQuery = "";
    private boolean closing = false;

    private Button tabAllBtn;
    private Button tabInvBtn;
    private VBox gridHost;
    private ScrollView scroll;
    private Label itemsCountLabel;

    private List<ItemStack> currentFilteredList = new ArrayList<>();
    private @Nullable MinecraftItemTooltip activeTooltip = null;

    public ShopItemPickerModal(@Nullable ItemStack initialStack, Consumer<ItemStack> onSelect) {
        this.initialStack = initialStack != null ? initialStack : ItemStack.EMPTY;
        this.onSelect = onSelect;

        themeEnabled(false);
        boxVisualEnabled(true);
        backgroundVisible(true);
        background(new MutableColor(0.0f, 0.0f, 0.0f, 0.70f));

        layout(style -> style.position(PositionType.ABSOLUTE).inset(0.0f).fill());

        this.on(
                PointerPressedEvent.TYPE,
                event -> {
                    if (event.phase() == EventPhase.CAPTURE) return;
                    if (event.target() == this) {
                        closeSelf();
                    }
                    event.cancel();
                });

        this.on(
                PointerReleasedEvent.TYPE,
                event -> {
                    if (event.phase() == EventPhase.CAPTURE) return;
                    event.cancel();
                });

        buildModalWindow();
    }

    @Override
    public void measure(LayoutContext context) {
        super.measure(context);
        float modalH = Math.min(context.availableHeight() * 0.88f, MAX_MODAL_HEIGHT);
        windowCard.measure(new LayoutContext(MODAL_WIDTH, modalH));
    }

    @Override
    public void arrangeInHost(RectView hostBounds) {
        this.arrange(hostBounds);

        float hostW = hostBounds.width();
        float hostH = hostBounds.height();
        float modalW = Math.min(hostW * 0.94f, MODAL_WIDTH);
        float modalH = Math.min(hostH * 0.88f, MAX_MODAL_HEIGHT);
        float modalX = hostBounds.x() + (hostW - modalW) * 0.5f;
        float modalY = hostBounds.y() + (hostH - modalH) * 0.5f;

        windowCard.layout(
                style ->
                        style.fixed(modalW, modalH)
                                .padding(10.0f)
                                .overflow(Overflow.HIDDEN)
                                .alignItems(Align.CENTER)
                                .flexNone());
        windowCard.arrange(new MutableRect(modalX, modalY, modalW, modalH));
    }

    private void buildModalWindow() {
        windowCard.themeEnabled(false);
        windowCard.boxVisualEnabled(true);
        windowCard.backgroundVisible(true);
        windowCard.background().set(ShopEditorStyles.BG_WINDOW);
        windowCard.borderVisible(true);
        windowCard.borderColor().set(ShopEditorStyles.BORDER_DEFAULT);
        windowCard.borderWidth(1.0f);
        windowCard.radius(4.0f);
        windowCard.spacing(6.0f);
        windowCard.layout(
                style ->
                        style.fixed(MODAL_WIDTH, MAX_MODAL_HEIGHT)
                                .padding(10.0f)
                                .overflow(Overflow.HIDDEN)
                                .alignItems(Align.CENTER)
                                .flexNone());

        // 1. Шапка: "Select Item" + превью текущего предмета + векторный ✕
        HBox header = new HBox();
        header.layout(
                style ->
                        style.widthPercent(100.0f)
                                .height(22.0f)
                                .alignItems(Align.CENTER)
                                .justifyContent(Justify.SPACE_BETWEEN)
                                .flexNone());

        HBox titleGroup = new HBox();
        titleGroup.spacing(8.0f);
        titleGroup.layout(style -> style.height(22.0f).alignItems(Align.CENTER).flexGrow(1.0f));

        Label title = new Label(I18n.get("shop.ui.item_selector.title"));
        title.color().set(ShopEditorStyles.TEXT_BOLD);
        titleGroup.addChild(title);

        if (!initialStack.isEmpty()) {
            ShopItemWidget curSlot = new ShopItemWidget(initialStack, 14.0f).asSlot(18.0f, 18.0f);
            curSlot.layout(style -> style.fixed(18.0f, 18.0f).flexNone());
            titleGroup.addChild(curSlot);

            String curName = initialStack.getHoverName().getString();
            Label curLabel = new Label(I18n.get("shop.ui.item_selector.current", curName));
            curLabel.color().set(ShopEditorStyles.TEXT_MUTED);
            titleGroup.addChild(curLabel);
        }

        Button closeBtn = UnityIcons.createCloseButton(this::closeSelf);

        header.addChild(titleGroup);
        header.addChild(closeBtn);
        windowCard.addChild(header);

        // 2. Вкладки (All Items / Inventory)
        HBox tabsRow = new HBox();
        tabsRow.spacing(6.0f);
        tabsRow.layout(
                style ->
                        style.widthPercent(100.0f)
                                .height(22.0f)
                                .alignItems(Align.CENTER)
                                .flexNone());

        tabAllBtn = new Button(I18n.get("shop.ui.item_selector.all"));
        ShopEditorStyles.styleButton(tabAllBtn);
        tabAllBtn.layout(style -> style.height(22.0f).padding(12.0f, 0.0f).flexNone());
        tabAllBtn.onClick(
                e -> {
                    if (currentTab != ItemSourceTab.ALL_ITEMS) {
                        currentTab = ItemSourceTab.ALL_ITEMS;
                        hideTooltip();
                        updateTabStyles();
                        filterAndRefresh();
                    }
                });

        tabInvBtn = new Button(I18n.get("shop.ui.item_selector.inventory"));
        ShopEditorStyles.styleButton(tabInvBtn);
        tabInvBtn.layout(style -> style.height(22.0f).padding(12.0f, 0.0f).flexNone());
        tabInvBtn.onClick(
                e -> {
                    if (currentTab != ItemSourceTab.INVENTORY) {
                        currentTab = ItemSourceTab.INVENTORY;
                        hideTooltip();
                        updateTabStyles();
                        filterAndRefresh();
                    }
                });

        tabsRow.addChild(tabAllBtn);
        tabsRow.addChild(tabInvBtn);
        updateTabStyles();
        windowCard.addChild(tabsRow);

        // 3. Поисковая строка
        HBox searchRow = new HBox();
        searchRow.spacing(6.0f);
        searchRow.layout(
                style ->
                        style.widthPercent(100.0f)
                                .height(22.0f)
                                .alignItems(Align.CENTER)
                                .flexNone());

        TextField searchField = new TextField();
        searchField.placeholder(I18n.get("shop.ui.item_selector.search.long"));
        searchField.themeEnabled(false);
        searchField.boxVisualEnabled(true);
        searchField.backgroundVisible(true);
        searchField.background().set(ShopEditorStyles.BG_INPUT);
        searchField.borderVisible(true);
        searchField.borderColor().set(ShopEditorStyles.BORDER_INPUT);
        searchField.borderWidth(1.0f);
        searchField.radius(2.0f);
        searchField.textColor().set(ShopEditorStyles.TEXT_PRIMARY);
        searchField.placeholderColor().set(ShopEditorStyles.TEXT_MUTED);
        searchField.layout(style -> style.height(20.0f).flexGrow(1.0f));
        searchField.onTextChanged(
                event -> {
                    this.searchQuery = event.newText() != null ? event.newText().trim() : "";
                    hideTooltip();
                    filterAndRefresh();
                });

        searchRow.addChild(searchField);
        windowCard.addChild(searchRow);

        // 4. Скроллируемая сетка всех предметов со скроллбаром Unity
        gridHost = new VBox();
        gridHost.spacing(4.0f);
        gridHost.layout(
                style ->
                        style.widthPercent(100.0f)
                                .alignItems(Align.CENTER)
                                .padding(2.0f)
                                .flexGrow(0.0f)
                                .flexShrink(0.0f));

        scroll = new ScrollView(gridHost);
        scroll.scrollStep(34.0f);
        scroll.scrollbarSize(6.0f);
        scroll.scrollbarGap(2.0f);
        scroll.scrollbarTrackColor().set(ShopEditorStyles.BG_INPUT);
        scroll.scrollbarThumbColor().set(ShopEditorStyles.TEXT_MUTED);
        scroll.scrollbarThumbHoverColor().set(ShopEditorStyles.TEXT_PRIMARY);
        scroll.overlayScrollBars(false);
        scroll.tickCullingEnabled(true);
        scroll.layout(
                layout ->
                        layout.widthPercent(100.0f)
                                .flexGrow(1.0f)
                                .flexShrink(1.0f)
                                .minHeight(0.0f)
                                .overflowX(Overflow.HIDDEN)
                                .overflowY(Overflow.AUTO));
        windowCard.addChild(scroll);

        // 5. Подвал со счётчиком элементов, кнопкой Clear и кнопкой Cancel
        HBox footer = new HBox();
        footer.layout(
                style ->
                        style.widthPercent(100.0f)
                                .height(24.0f)
                                .alignItems(Align.CENTER)
                                .justifyContent(Justify.SPACE_BETWEEN)
                                .flexNone());

        Button clearBtn = new Button(I18n.get("shop.ui.item_selector.clear"));
        ShopEditorStyles.styleButton(clearBtn);
        clearBtn.layout(style -> style.height(22.0f).padding(8.0f, 0.0f).flexNone());
        clearBtn.onClick(
                e -> {
                    if (closing) return;
                    closing = true;
                    hideTooltip();
                    if (onSelect != null) {
                        onSelect.accept(ItemStack.EMPTY);
                    }
                    closeSelf();
                });

        itemsCountLabel = new Label(I18n.get("shop.ui.item_selector.count", "0"));
        itemsCountLabel.color().set(ShopEditorStyles.TEXT_MUTED);
        itemsCountLabel.layout(style -> style.alignSelf(Align.CENTER).flexNone());

        Button cancelBtn = new Button(I18n.get("shop.ui.common.cancel"));
        ShopEditorStyles.styleButton(cancelBtn);
        cancelBtn.layout(style -> style.height(22.0f).padding(10.0f, 0.0f).flexNone());
        cancelBtn.onClick(e -> closeSelf());

        footer.addChild(clearBtn);
        footer.addChild(itemsCountLabel);
        footer.addChild(cancelBtn);
        windowCard.addChild(footer);

        addChild(windowCard);

        filterAndRefresh();
    }

    private void updateTabStyles() {
        if (tabAllBtn == null || tabInvBtn == null) return;
        if (currentTab == ItemSourceTab.ALL_ITEMS) {
            tabAllBtn.background().set(new MutableColor(0.24f, 0.38f, 0.58f, 1.0f));
            tabAllBtn.textColor().set(ShopEditorStyles.TEXT_BOLD);
            tabAllBtn.borderColor().set(ShopEditorStyles.BORDER_INPUT_FOCUS);

            tabInvBtn.background().set(ShopEditorStyles.BG_INPUT);
            tabInvBtn.textColor().set(ShopEditorStyles.TEXT_MUTED);
            tabInvBtn.borderColor().set(ShopEditorStyles.BORDER_INPUT);
        } else {
            tabInvBtn.background().set(new MutableColor(0.24f, 0.38f, 0.58f, 1.0f));
            tabInvBtn.textColor().set(ShopEditorStyles.TEXT_BOLD);
            tabInvBtn.borderColor().set(ShopEditorStyles.BORDER_INPUT_FOCUS);

            tabAllBtn.background().set(ShopEditorStyles.BG_INPUT);
            tabAllBtn.textColor().set(ShopEditorStyles.TEXT_MUTED);
            tabAllBtn.borderColor().set(ShopEditorStyles.BORDER_INPUT);
        }
    }

    private void filterAndRefresh() {
        gridHost.clearChildren();

        List<ItemStack> source =
                currentTab == ItemSourceTab.ALL_ITEMS
                        ? ShopItemSources.getRegistryItems()
                        : ShopItemSources.getInventoryItems();

        currentFilteredList = new ArrayList<>();
        for (ItemStack stack : source) {
            if (ShopItemSources.matchesSearch(stack, searchQuery)) {
                currentFilteredList.add(stack);
            }
        }

        int total = currentFilteredList.size();
        if (itemsCountLabel != null) {
            itemsCountLabel.text(I18n.get("shop.ui.item_selector.count", String.valueOf(total)));
        }

        if (scroll != null) {
            scroll.scrollTo(0.0f, 0.0f);
        }

        if (currentFilteredList.isEmpty()) {
            Label empty =
                    new Label(
                            currentTab == ItemSourceTab.INVENTORY
                                    ? "No items in player inventory"
                                    : "No matching items found");
            empty.color().set(ShopEditorStyles.TEXT_MUTED);
            empty.layout(style -> style.margin(24.0f).alignSelf(Align.CENTER));
            gridHost.addChild(empty);
            return;
        }

        GridBox grid = new GridBox();
        grid.columns(14);
        grid.spacing(4.0f);
        grid.layout(style -> style.widthPercent(100.0f).alignSelf(Align.CENTER));

        for (ItemStack itemStack : currentFilteredList) {
            final ItemStack stack = itemStack;
            ShopItemWidget slot = new ShopItemWidget(stack, 18.0f).asSlot(30.0f, 30.0f);
            slot.decorations(true);
            slot.background().set(ShopEditorStyles.BG_INPUT);
            slot.borderColor().set(ShopEditorStyles.BORDER_INPUT);
            slot.radius(3.0f);
            slot.layout(style -> style.fixed(30.0f, 30.0f).flexNone());

            slot.on(
                    PointerEnteredEvent.TYPE,
                    event -> {
                        slot.borderColor().set(ShopEditorStyles.BORDER_INPUT_FOCUS);
                        slot.borderWidth(1.5f);
                        slot.background().set(new MutableColor(0.22f, 0.24f, 0.28f, 1.0f));
                        showTooltip(slot, stack);
                    });

            slot.on(
                    PointerExitedEvent.TYPE,
                    event -> {
                        slot.borderColor().set(ShopEditorStyles.BORDER_INPUT);
                        slot.borderWidth(1.0f);
                        slot.background().set(ShopEditorStyles.BG_INPUT);
                        hideTooltip();
                    });

            slot.on(
                    PointerPressedEvent.TYPE,
                    event -> {
                        if (event.phase() == EventPhase.CAPTURE) return;
                        if (event.button() == PointerButton.PRIMARY) {
                            event.cancel();
                        }
                    });

            slot.on(
                    PointerReleasedEvent.TYPE,
                    event -> {
                        if (event.phase() == EventPhase.CAPTURE) return;
                        if (event.button() != PointerButton.PRIMARY) return;
                        event.cancel();
                        if (closing) return;
                        closing = true;
                        hideTooltip();
                        ItemStack chosen = stack.copy();
                        chosen.setCount(1);
                        if (onSelect != null) {
                            onSelect.accept(chosen);
                        }
                        closeSelf();
                    });

            grid.addChild(slot);
        }

        gridHost.addChild(grid);
    }

    private OverlayLayer getHostOverlay() {
        if (parent() instanceof OverlayLayer ol) {
            return ol;
        }
        OverlayLayer active = ShopEditorScreen.getActiveOverlay();
        if (active != null) return active;
        return ShopScreenController.getActiveOverlay();
    }

    private void showTooltip(Widget anchor, ItemStack stack) {
        hideTooltip();
        if (stack == null || stack.isEmpty()) return;
        OverlayLayer overlay = getHostOverlay();
        if (overlay != null) {
            activeTooltip = new MinecraftItemTooltip(anchor, stack);
            overlay.addOverlay(activeTooltip);
        }
    }

    private void hideTooltip() {
        if (activeTooltip != null) {
            OverlayLayer overlay = getHostOverlay();
            if (overlay != null) {
                overlay.removeOverlay(activeTooltip);
            }
            activeTooltip.dispose();
            activeTooltip = null;
        }
    }

    private void closeSelf() {
        hideTooltip();
        if (parent() instanceof OverlayLayer ol) {
            ol.removeOverlay(this);
        } else {
            ShopScreenController.closeModal(this);
        }
    }
}
