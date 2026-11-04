package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.shop.client.ui.ShopScreenController;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityIcons;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.unity.UnityStyles;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopItemWidget;
import dev.sixik.unigui.api.event.EventPhase;
import dev.sixik.unigui.api.event.PointerEnteredEvent;
import dev.sixik.unigui.api.event.PointerExitedEvent;
import dev.sixik.unigui.api.event.PointerPressedEvent;
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
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * Модальное окно выбора предмета (Select Item) в стиле Unity Dark.
 * Позволяет выбирать предмет как из общего реестра (All Items), так и из инвентаря игрока (Inventory).
 */
public class ShopItemPickerModal extends Box implements OverlayHostAware {

    private static final float MODAL_WIDTH = 560.0f;
    private static final float MAX_MODAL_HEIGHT = 440.0f;
    private static final int ITEMS_PER_PAGE = 126; // 14 колонок x 9 строк

    public enum ItemSourceTab {
        ALL_ITEMS,
        INVENTORY
    }

    private static List<ItemStack> CACHED_REGISTRY_ITEMS = null;

    private final ItemStack initialStack;
    private final Consumer<ItemStack> onSelect;
    private final VBox windowCard = new VBox();

    private ItemSourceTab currentTab = ItemSourceTab.ALL_ITEMS;
    private String searchQuery = "";
    private int currentPage = 0;
    private boolean closing = false;

    private Button tabAllBtn;
    private Button tabInvBtn;
    private VBox gridHost;
    private Label pageLabel;
    private Button prevPageBtn;
    private Button nextPageBtn;

    private List<ItemStack> currentFilteredList = new ArrayList<>();
    private @Nullable MinecraftItemTooltip activeTooltip = null;

    public ShopItemPickerModal(@Nullable ItemStack initialStack, Consumer<ItemStack> onSelect) {
        this.initialStack = initialStack != null ? initialStack : ItemStack.EMPTY;
        this.onSelect = onSelect;

        themeEnabled(false);
        boxVisualEnabled(true);
        backgroundVisible(true);
        background(new MutableColor(0.0f, 0.0f, 0.0f, 0.70f));

        layout(style -> style
                .position(PositionType.ABSOLUTE)
                .inset(0.0f)
                .fill()
        );

        this.on(PointerPressedEvent.TYPE, event -> {
            if (event.phase() == EventPhase.CAPTURE) return;
            if (event.target() == this) {
                closeSelf();
            }
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

        windowCard.layout(style -> style
                .fixed(modalW, modalH)
                .padding(10.0f)
                .overflow(Overflow.HIDDEN)
                .alignItems(Align.CENTER)
                .flexNone()
        );
        windowCard.arrange(new MutableRect(modalX, modalY, modalW, modalH));
    }

    private void buildModalWindow() {
        windowCard.themeEnabled(false);
        windowCard.boxVisualEnabled(true);
        windowCard.backgroundVisible(true);
        windowCard.background().set(UnityStyles.BG_WINDOW);
        windowCard.borderVisible(true);
        windowCard.borderColor().set(UnityStyles.BORDER_DEFAULT);
        windowCard.borderWidth(1.0f);
        windowCard.radius(4.0f);
        windowCard.spacing(6.0f);
        windowCard.layout(style -> style
                .fixed(MODAL_WIDTH, MAX_MODAL_HEIGHT)
                .padding(10.0f)
                .overflow(Overflow.HIDDEN)
                .alignItems(Align.CENTER)
                .flexNone()
        );

        // 1. Шапка: "Select Item" + превью текущего предмета + векторный ✕
        HBox header = new HBox();
        header.layout(style -> style
                .widthPercent(100.0f)
                .height(22.0f)
                .alignItems(Align.CENTER)
                .justifyContent(Justify.SPACE_BETWEEN)
                .flexNone()
        );

        HBox titleGroup = new HBox();
        titleGroup.spacing(8.0f);
        titleGroup.layout(style -> style.height(22.0f).alignItems(Align.CENTER).flexGrow(1.0f));

        Label title = new Label("Select Item");
        title.color().set(UnityStyles.TEXT_BOLD);
        titleGroup.addChild(title);

        if (!initialStack.isEmpty()) {
            ShopItemWidget curSlot = new ShopItemWidget(initialStack, 14.0f).asSlot(18.0f, 18.0f);
            curSlot.layout(style -> style.fixed(18.0f, 18.0f).flexNone());
            titleGroup.addChild(curSlot);

            String curName = initialStack.getHoverName().getString();
            Label curLabel = new Label("Current: " + curName);
            curLabel.color().set(UnityStyles.TEXT_MUTED);
            titleGroup.addChild(curLabel);
        }

        Button closeBtn = UnityIcons.createCloseButton(this::closeSelf);

        header.addChild(titleGroup);
        header.addChild(closeBtn);
        windowCard.addChild(header);

        // 2. Вкладки (All Items / Inventory)
        HBox tabsRow = new HBox();
        tabsRow.spacing(6.0f);
        tabsRow.layout(style -> style
                .widthPercent(100.0f)
                .height(22.0f)
                .alignItems(Align.CENTER)
                .flexNone()
        );

        tabAllBtn = new Button("All Items");
        UnityStyles.styleButton(tabAllBtn);
        tabAllBtn.layout(style -> style.height(22.0f).padding(12.0f, 0.0f).flexNone());
        tabAllBtn.onClick(e -> {
            if (currentTab != ItemSourceTab.ALL_ITEMS) {
                currentTab = ItemSourceTab.ALL_ITEMS;
                currentPage = 0;
                hideTooltip();
                updateTabStyles();
                filterAndRefresh();
            }
        });

        tabInvBtn = new Button("Inventory");
        UnityStyles.styleButton(tabInvBtn);
        tabInvBtn.layout(style -> style.height(22.0f).padding(12.0f, 0.0f).flexNone());
        tabInvBtn.onClick(e -> {
            if (currentTab != ItemSourceTab.INVENTORY) {
                currentTab = ItemSourceTab.INVENTORY;
                currentPage = 0;
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
        searchRow.layout(style -> style
                .widthPercent(100.0f)
                .height(22.0f)
                .alignItems(Align.CENTER)
                .flexNone()
        );

        TextField searchField = new TextField();
        searchField.placeholder("Search item by name or id (e.g. diamond, minecraft:apple)...");
        searchField.themeEnabled(false);
        searchField.boxVisualEnabled(true);
        searchField.backgroundVisible(true);
        searchField.background().set(UnityStyles.BG_INPUT);
        searchField.borderVisible(true);
        searchField.borderColor().set(UnityStyles.BORDER_INPUT);
        searchField.borderWidth(1.0f);
        searchField.radius(2.0f);
        searchField.textColor().set(UnityStyles.TEXT_PRIMARY);
        searchField.placeholderColor().set(UnityStyles.TEXT_MUTED);
        searchField.layout(style -> style.height(20.0f).flexGrow(1.0f));
        searchField.onTextChanged(event -> {
            this.searchQuery = event.newText() != null ? event.newText().trim() : "";
            this.currentPage = 0;
            hideTooltip();
            filterAndRefresh();
        });

        searchRow.addChild(searchField);
        windowCard.addChild(searchRow);

        // 4. Скроллируемая сетка предметов
        gridHost = new VBox();
        gridHost.spacing(4.0f);
        gridHost.layout(style -> style
                .widthPercent(100.0f)
                .alignItems(Align.CENTER)
                .padding(2.0f)
                .flexGrow(0.0f)
                .flexShrink(0.0f)
        );

        ScrollView scroll = new ScrollView(gridHost);
        scroll.scrollStep(20.0f);
        scroll.scrollbarGap(2.0f);
        scroll.scrollbarTrackColor().set(UnityStyles.BORDER_DEFAULT);
        scroll.scrollbarThumbColor().set(UnityStyles.TEXT_MUTED);
        scroll.overlayScrollBars(false);
        scroll.layout(layout -> layout
                .widthPercent(100.0f)
                .flexGrow(1.0f)
                .flexShrink(1.0f)
                .minHeight(0.0f)
                .overflowX(Overflow.HIDDEN)
                .overflowY(Overflow.AUTO)
        );
        windowCard.addChild(scroll);

        // 5. Подвал с пагинацией, кнопкой Clear и кнопкой Cancel
        HBox footer = new HBox();
        footer.layout(style -> style
                .widthPercent(100.0f)
                .height(24.0f)
                .alignItems(Align.CENTER)
                .justifyContent(Justify.SPACE_BETWEEN)
                .flexNone()
        );

        Button clearBtn = new Button("Clear (Air)");
        UnityStyles.styleButton(clearBtn);
        clearBtn.layout(style -> style.height(22.0f).padding(8.0f, 0.0f).flexNone());
        clearBtn.onClick(e -> {
            if (closing) return;
            closing = true;
            hideTooltip();
            if (onSelect != null) {
                onSelect.accept(ItemStack.EMPTY);
            }
            closeSelf();
        });

        HBox pageControls = new HBox();
        pageControls.spacing(6.0f);
        pageControls.layout(style -> style.height(22.0f).alignItems(Align.CENTER).flexNone());

        prevPageBtn = new Button("<");
        UnityStyles.styleButton(prevPageBtn);
        prevPageBtn.layout(style -> style.fixed(24.0f, 22.0f).flexNone());
        prevPageBtn.onClick(e -> {
            if (currentPage > 0) {
                currentPage--;
                hideTooltip();
                filterAndRefresh();
            }
        });

        pageLabel = new Label("Page 1 of 1 (0 items)");
        pageLabel.color().set(UnityStyles.TEXT_MUTED);
        pageLabel.layout(style -> style.alignSelf(Align.CENTER).flexNone());

        nextPageBtn = new Button(">");
        UnityStyles.styleButton(nextPageBtn);
        nextPageBtn.layout(style -> style.fixed(24.0f, 22.0f).flexNone());
        nextPageBtn.onClick(e -> {
            int totalPages = Math.max(1, (int) Math.ceil((double) currentFilteredList.size() / ITEMS_PER_PAGE));
            if (currentPage < totalPages - 1) {
                currentPage++;
                hideTooltip();
                filterAndRefresh();
            }
        });

        pageControls.addChild(prevPageBtn);
        pageControls.addChild(pageLabel);
        pageControls.addChild(nextPageBtn);

        Button cancelBtn = new Button("Cancel");
        UnityStyles.styleButton(cancelBtn);
        cancelBtn.layout(style -> style.height(22.0f).padding(10.0f, 0.0f).flexNone());
        cancelBtn.onClick(e -> closeSelf());

        footer.addChild(clearBtn);
        footer.addChild(pageControls);
        footer.addChild(cancelBtn);
        windowCard.addChild(footer);

        addChild(windowCard);

        filterAndRefresh();
    }

    private void updateTabStyles() {
        if (tabAllBtn == null || tabInvBtn == null) return;
        if (currentTab == ItemSourceTab.ALL_ITEMS) {
            tabAllBtn.background().set(new MutableColor(0.24f, 0.38f, 0.58f, 1.0f));
            tabAllBtn.textColor().set(UnityStyles.TEXT_BOLD);
            tabAllBtn.borderColor().set(UnityStyles.BORDER_INPUT_FOCUS);

            tabInvBtn.background().set(UnityStyles.BG_INPUT);
            tabInvBtn.textColor().set(UnityStyles.TEXT_MUTED);
            tabInvBtn.borderColor().set(UnityStyles.BORDER_INPUT);
        } else {
            tabInvBtn.background().set(new MutableColor(0.24f, 0.38f, 0.58f, 1.0f));
            tabInvBtn.textColor().set(UnityStyles.TEXT_BOLD);
            tabInvBtn.borderColor().set(UnityStyles.BORDER_INPUT_FOCUS);

            tabAllBtn.background().set(UnityStyles.BG_INPUT);
            tabAllBtn.textColor().set(UnityStyles.TEXT_MUTED);
            tabAllBtn.borderColor().set(UnityStyles.BORDER_INPUT);
        }
    }

    private void filterAndRefresh() {
        gridHost.clearChildren();

        List<ItemStack> source = currentTab == ItemSourceTab.ALL_ITEMS
                ? getRegistryItems()
                : getInventoryItems();

        currentFilteredList = new ArrayList<>();
        for (ItemStack stack : source) {
            if (matchesSearch(stack, searchQuery)) {
                currentFilteredList.add(stack);
            }
        }

        int total = currentFilteredList.size();
        int totalPages = Math.max(1, (int) Math.ceil((double) total / ITEMS_PER_PAGE));
        if (currentPage >= totalPages) {
            currentPage = Math.max(0, totalPages - 1);
        }

        pageLabel.text("Page " + (currentPage + 1) + " / " + totalPages + " (" + total + " items)");
        prevPageBtn.enabled(currentPage > 0);
        prevPageBtn.textColor().set(currentPage > 0 ? UnityStyles.TEXT_PRIMARY : UnityStyles.TEXT_MUTED);
        nextPageBtn.enabled(currentPage < totalPages - 1);
        nextPageBtn.textColor().set(currentPage < totalPages - 1 ? UnityStyles.TEXT_PRIMARY : UnityStyles.TEXT_MUTED);

        if (currentFilteredList.isEmpty()) {
            Label empty = new Label(currentTab == ItemSourceTab.INVENTORY
                    ? "No items in player inventory"
                    : "No matching items found");
            empty.color().set(UnityStyles.TEXT_MUTED);
            empty.layout(style -> style.margin(24.0f).alignSelf(Align.CENTER));
            gridHost.addChild(empty);
            return;
        }

        int from = currentPage * ITEMS_PER_PAGE;
        int to = Math.min(from + ITEMS_PER_PAGE, total);
        List<ItemStack> pageItems = currentFilteredList.subList(from, to);

        GridBox grid = new GridBox();
        grid.columns(14);
        grid.spacing(4.0f);
        grid.layout(style -> style
                .widthPercent(100.0f)
                .alignSelf(Align.CENTER)
        );

        for (ItemStack itemStack : pageItems) {
            final ItemStack stack = itemStack;
            ShopItemWidget slot = new ShopItemWidget(stack, 18.0f).asSlot(30.0f, 30.0f);
            slot.decorations(true);
            slot.background().set(UnityStyles.BG_INPUT);
            slot.borderColor().set(UnityStyles.BORDER_INPUT);
            slot.radius(3.0f);
            slot.layout(style -> style.fixed(30.0f, 30.0f).flexNone());

            slot.on(PointerEnteredEvent.TYPE, event -> {
                slot.borderColor().set(UnityStyles.BORDER_INPUT_FOCUS);
                slot.borderWidth(1.5f);
                slot.background().set(new MutableColor(0.22f, 0.24f, 0.28f, 1.0f));
                showTooltip(slot, stack);
            });

            slot.on(PointerExitedEvent.TYPE, event -> {
                slot.borderColor().set(UnityStyles.BORDER_INPUT);
                slot.borderWidth(1.0f);
                slot.background().set(UnityStyles.BG_INPUT);
                hideTooltip();
            });

            slot.on(PointerPressedEvent.TYPE, event -> {
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

    private static boolean matchesSearch(ItemStack stack, String query) {
        if (query == null || query.isBlank()) return true;
        if (stack == null || stack.isEmpty()) return false;

        String q = query.trim().toLowerCase(Locale.ROOT);

        // 1. Check Registry ID
        try {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            if (id != null) {
                if (id.toString().toLowerCase(Locale.ROOT).contains(q)) return true;
                if (id.getPath().toLowerCase(Locale.ROOT).contains(q)) return true;
                if (id.getNamespace().toLowerCase(Locale.ROOT).contains(q)) return true;
            }
        } catch (Throwable ignored) {
        }

        // 2. Check localized hover name (e.g. "Diamond Sword" / "Алмазный меч")
        try {
            String name = stack.getHoverName().getString().toLowerCase(Locale.ROOT);
            if (name.contains(q)) return true;
        } catch (Throwable ignored) {
        }

        // 3. Check description ID
        try {
            String descId = stack.getDescriptionId().toLowerCase(Locale.ROOT);
            if (descId.contains(q)) return true;
        } catch (Throwable ignored) {
        }

        return false;
    }

    private static List<ItemStack> getRegistryItems() {
        if (CACHED_REGISTRY_ITEMS == null) {
            List<ItemStack> list = new ArrayList<>();
            try {
                for (Item item : BuiltInRegistries.ITEM) {
                    if (item == null || item == Items.AIR) continue;
                    list.add(new ItemStack(item, 1));
                }
            } catch (Throwable ignored) {
            }
            CACHED_REGISTRY_ITEMS = list;
        }
        return CACHED_REGISTRY_ITEMS;
    }

    private static List<ItemStack> getInventoryItems() {
        List<ItemStack> list = new ArrayList<>();
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc != null && mc.player != null) {
                var inv = mc.player.getInventory();
                if (inv != null) {
                    if (inv.items != null) {
                        for (ItemStack s : inv.items) {
                            if (s != null && !s.isEmpty()) {
                                list.add(s.copy());
                            }
                        }
                    }
                    if (inv.offhand != null) {
                        for (ItemStack s : inv.offhand) {
                            if (s != null && !s.isEmpty()) {
                                list.add(s.copy());
                            }
                        }
                    }
                    if (inv.armor != null) {
                        for (ItemStack s : inv.armor) {
                            if (s != null && !s.isEmpty()) {
                                list.add(s.copy());
                            }
                        }
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return list;
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
