package dev.sixik.sdmshop2.libs.shop.client.screens.widgets;

import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.DialogWidget;
import com.lowdragmc.lowdraglib.gui.widget.PhantomSlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Size;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.side.item.IItemTransfer;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.sixik.sdmshop2.libs.shop.client.textures.ColorRectAndBorderTexture;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.InputTextBox;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.containers.ModalWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

public class SDMItemStackSelectorWidget extends WidgetGroup {

    private static final int HEIGHT = 20;
    private static final int PICKER_WIDTH = 320;
    private static final int PICKER_HEIGHT = 260;
    private static final int PICKER_PADDING = 4;
    private static final int PICKER_SEARCH_HEIGHT = 20;
    private static final int PICKER_SOURCE_BUTTON_WIDTH = 76;
    private static final int PICKER_GRID_ITEM_SIZE = 18;
    private static final int PICKER_GRID_GAP = 2;
    private static final int PICKER_SCROLLBAR_WIDTH = 4;
    private static final int PICKER_SCROLLBAR_PADDING = 2;

    protected Consumer<ItemStack> onItemStackUpdate;
    protected final IItemTransfer handler;
    protected final PhantomSlotWidget itemSlot;
    protected final InputTextBox itemField;
    protected final boolean nbtEnabled;
    protected ItemStack item = ItemStack.EMPTY;

    public SDMItemStackSelectorWidget(int x, int y, int width) {
        this(x, y, width, true);
    }

    public SDMItemStackSelectorWidget(int x, int y, int width, boolean nbt) {
        super(x, y, width, HEIGHT);
        this.nbtEnabled = nbt;
        setClientSideWidget();
        itemField = new InputTextBox(22, 0, getItemFieldWidth(width), HEIGHT, null, s -> {
            if (s != null && !s.isEmpty()) {
                ResourceLocation location = ResourceLocation.tryParse(s);
                if (location == null) return;

                Item item = BuiltInRegistries.ITEM.get(location);
                if (!ItemStack.isSameItemSameTags(item.getDefaultInstance(), this.item)) {
                    this.item = item.getDefaultInstance();
                    onUpdate();
                }
            }
        });
        itemField.setResourceLocationOnly();
        itemField.setClientSideWidget();
        itemField.setHoverTooltips("ldlib.gui.tips.item_selector");

        addWidget(itemSlot = (PhantomSlotWidget) new PhantomSlotWidget(handler = new ItemStackTransfer(1), 0, 1, 1)
                .setClearSlotOnRightClick(true)
                .setChangeListener(() -> {
                    setItemStack(handler.getStackInSlot(0));
                    onUpdate();
                }).setBackgroundTexture(new ColorBorderTexture(1, -1)));
        addWidget(itemField);

        if (nbt) {
            addWidget(new ButtonWidget(width - 21, 0, 20, 20, cd -> {
                if (item.isEmpty()) return;
                InputTextBox nbtField;
                new DialogWidget(getGui().mainGroup, isClientSideWidget)
                        .setOnClosed(this::onUpdate)
                        .addWidget(nbtField = new InputTextBox(10, 10, getGui().mainGroup.getSize().width - 50, 20, null, s -> {
                            try {
                                item.setTag(TagParser.parseTag(s));
                                onUpdate();
                            } catch (CommandSyntaxException ignored) {

                            }
                        }));
                nbtField.setClientSideWidget();
                if (item.hasTag()) {
                    nbtField.setCurrentString(item.getTag().toString());
                }
            })
                    .setButtonTexture(ResourceBorderTexture.BUTTON_COMMON, new TextTexture("NBT", -1).setDropShadow(true))
                    .setHoverBorderTexture(1, -1).setHoverTooltips("ldlib.gui.tips.item_tag"));
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && itemSlot.isMouseOverElement(mouseX, mouseY)) {
            openItemPicker();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void setSize(Size size) {
        super.setSize(size);
        itemField.setSelfPosition(22, 0);
        itemField.setSize(getItemFieldWidth(size.width), HEIGHT);

        if (nbtEnabled && widgets.size() > 2) {
            Widget nbtButton = widgets.get(2);
            nbtButton.setSelfPosition(size.width - 21, 0);
            nbtButton.setSize(20, HEIGHT);
        }
    }

    private int getItemFieldWidth(int width) {
        return Math.max(1, width - (nbtEnabled ? 43 : 22));
    }

    public ItemStack getItemStack() {
        return item;
    }

    public SDMItemStackSelectorWidget setItemStack(ItemStack itemStack) {
        item = Objects.requireNonNullElse(itemStack, ItemStack.EMPTY).copy();
        handler.setStackInSlot(0, item);
        itemField.setCurrentStringSilently(BuiltInRegistries.ITEM.getKey(item.getItem()).toString());
        return this;
    }

    public SDMItemStackSelectorWidget setOnItemStackUpdate(Consumer<ItemStack> onItemStackUpdate) {
        this.onItemStackUpdate = onItemStackUpdate;
        return this;
    }

    private void onUpdate() {
        handler.setStackInSlot(0, item);
        if (onItemStackUpdate != null) {
            onItemStackUpdate.accept(item);
        }
    }

    private void openItemPicker() {
        ModalWidget modal = new ModalWidget(PICKER_WIDTH, PICKER_HEIGHT)
                .setTitle(Component.literal("Выбор предмета"));
        ModalWidget opened = ModalWidget.openNested(this, modal);
        if (opened == null) return;

        int contentWidth = Math.max(1, opened.getContentWidth());
        int contentHeight = Math.max(1, opened.getContentHeight());
        int searchWidth = Math.max(1, contentWidth - PICKER_SOURCE_BUTTON_WIDTH - PICKER_PADDING);
        int gridY = PICKER_SEARCH_HEIGHT + PICKER_PADDING;
        int gridHeight = Math.max(1, contentHeight - gridY);
        int gridWidth = Math.max(1, contentWidth);

        InputTextBox search = new InputTextBox(0, 0, searchWidth, PICKER_SEARCH_HEIGHT);
        styleInput(search);
        search.setPlaceholder(Component.literal("Поиск по id или названию..."));
        search.setClientSideWidget();

        ItemPickerState state = new ItemPickerState();
        ButtonWidget sourceButton = createPickerButton(
                searchWidth + PICKER_PADDING,
                0,
                PICKER_SOURCE_BUTTON_WIDTH,
                PICKER_SEARCH_HEIGHT,
                sourceButtonText(state.source),
                ignored -> { }
        );

        ItemPickerGrid grid = new ItemPickerGrid(0, gridY, gridWidth, gridHeight, selected -> {
            setItemStack(selected);
            onUpdate();
            opened.close();
        });
        grid.setClientSideWidget();

        final Runnable[] rebuild = new Runnable[1];
        rebuild[0] = () -> rebuildItemPickerGrid(grid, state.source, search.getCurrentString());
        search.setTextResponder(ignored -> rebuild[0].run());
        sourceButton.setOnPressCallback(ignored -> {
            state.source = state.source == PickerSource.REGISTRY ? PickerSource.INVENTORY : PickerSource.REGISTRY;
            sourceButton.setText(sourceButtonText(state.source));
            rebuild[0].run();
        });

        opened.addWidget(search);
        opened.addWidget(sourceButton);
        opened.addWidget(grid);
        rebuild[0].run();
    }

    private void rebuildItemPickerGrid(
            ItemPickerGrid grid,
            PickerSource source,
            String query
    ) {
        String filter = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        List<ItemStack> stacks = collectPickerItems(source);
        if (!filter.isEmpty()) {
            stacks.removeIf(stack -> !matchesItem(stack, filter));
        }
        grid.setItems(stacks);
    }

    private List<ItemStack> collectPickerItems(PickerSource source) {
        List<ItemStack> stacks = new ArrayList<>();
        if (source == PickerSource.INVENTORY) {
            if (Minecraft.getInstance().player == null) return stacks;

            Inventory inventory = Minecraft.getInstance().player.getInventory();
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack stack = inventory.getItem(i);
                if (!stack.isEmpty()) {
                    stacks.add(stack.copy());
                }
            }
            return stacks;
        }

        for (Item registryItem : BuiltInRegistries.ITEM) {
            if (registryItem == Items.AIR) continue;
            stacks.add(registryItem.getDefaultInstance());
        }
        return stacks;
    }

    private boolean matchesItem(ItemStack stack, String filter) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String idText = id == null ? "" : id.toString().toLowerCase(Locale.ROOT);
        String nameText = stack.getHoverName().getString().toLowerCase(Locale.ROOT);
        String tagText = stack.hasTag() ? stack.getTag().toString().toLowerCase(Locale.ROOT) : "";
        return idText.contains(filter) || nameText.contains(filter) || tagText.contains(filter);
    }

    private ButtonWidget createPickerButton(int x, int y, int width, int height, Component text, Consumer<Object> onPress) {
        ButtonWidget button = new ButtonWidget(x, y, width, height, text, ignored -> onPress.accept(ignored));
        styleButton(button);
        button.setClientSideWidget();
        return button;
    }

    private Component sourceButtonText(PickerSource source) {
        return source == PickerSource.REGISTRY
                ? Component.literal("Инвентарь")
                : Component.literal("Регистр");
    }

    private void styleInput(InputTextBox input) {
        input.setBackground(new ColorRectAndBorderTexture(0xFF101016, 0xFF5C637A, 1).setRadius(2));
        input.setFocusedOutline(0xFF101016, 0xFFFFFFFF);
        input.setTextColor(0xFFE8E8F0);
        input.setPadding(4, 1);
    }

    private void styleButton(ButtonWidget button) {
        button.setButtonColors(0xFF2A2A36, 0xFF3D3D4E);
        button.setHoverColors(0xFF34384A, 0xFF7986CB);
        button.setClickedColors(0xFF202331, 0xFF7986CB);
        button.setTextColor(0xFFFFFFFF);
        button.setTextPadding(3);
        button.setMinTextScale(0.35f);
    }

    private enum PickerSource {
        REGISTRY,
        INVENTORY
    }

    private static class ItemPickerState {
        private PickerSource source = PickerSource.REGISTRY;
    }

    private class ItemPickerGrid extends Widget {

        private final List<ItemStack> items = new ArrayList<>();
        private final Consumer<ItemStack> onSelect;
        private int scrollOffset;
        private boolean draggingScrollThumb;
        private double dragStartMouseY;
        private int dragStartScrollOffset;

        private ItemPickerGrid(int x, int y, int width, int height, Consumer<ItemStack> onSelect) {
            super(x, y, width, height);
            this.onSelect = onSelect;
        }

        private void setItems(List<ItemStack> stacks) {
            items.clear();
            if (stacks != null) {
                for (ItemStack stack : stacks) {
                    if (!stack.isEmpty()) {
                        items.add(stack.copy());
                    }
                }
            }
            scrollOffset = 0;
            draggingScrollThumb = false;
            clampScrollOffset();
        }

        @Override
        public void drawInBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            int x = getPositionX();
            int y = getPositionY();
            int width = getSizeWidth();
            int height = getSizeHeight();

            graphics.fill(x, y, x + width, y + height, 0xFF14151C);
            graphics.fill(x, y, x + width, y + 1, 0xFF3D3D4E);
            graphics.fill(x, y + height - 1, x + width, y + height, 0xFF3D3D4E);
            graphics.fill(x, y, x + 1, y + height, 0xFF3D3D4E);
            graphics.fill(x + width - 1, y, x + width, y + height, 0xFF3D3D4E);

            graphics.enableScissor(x + 1, y + 1, x + width - 1, y + height - 1);
            try {
                drawVisibleItems(graphics, mouseX, mouseY);
                if (items.isEmpty()) {
                    graphics.drawString(
                            Minecraft.getInstance().font,
                            "Ничего не найдено",
                            x + 6,
                            y + 6,
                            0xFFAAAAAA,
                            false
                    );
                }
            } finally {
                graphics.disableScissor();
            }

            drawScrollBar(graphics, mouseX, mouseY);
        }

        @Override
        public void drawInForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            ItemStack hovered = getItemAt(mouseX, mouseY);
            if (!hovered.isEmpty()) {
                graphics.renderTooltip(Minecraft.getInstance().font, hovered, mouseX, mouseY);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button != 0 || !isMouseOverElement(mouseX, mouseY)) {
                return false;
            }

            if (isMouseOverScrollBar(mouseX, mouseY)) {
                if (isMouseOverScrollThumb(mouseX, mouseY)) {
                    draggingScrollThumb = true;
                    dragStartMouseY = mouseY;
                    dragStartScrollOffset = scrollOffset;
                } else {
                    scrollToMouse(mouseY);
                }
                return true;
            }

            ItemStack clicked = getItemAt(mouseX, mouseY);
            if (!clicked.isEmpty()) {
                Widget.playButtonClickSound();
                onSelect.accept(clicked.copy());
                return true;
            }

            return true;
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
            if (!draggingScrollThumb || button != 0) {
                return false;
            }

            int maxScroll = getMaxScrollOffset();
            if (maxScroll <= 0) {
                scrollOffset = 0;
                return true;
            }

            int trackHeight = getScrollTrackHeight();
            int thumbHeight = getScrollThumbHeight();
            int movable = Math.max(1, trackHeight - thumbHeight);
            scrollOffset = dragStartScrollOffset + (int) Math.round((mouseY - dragStartMouseY) * maxScroll / (double) movable);
            clampScrollOffset();
            return true;
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            if (button == 0 && draggingScrollThumb) {
                draggingScrollThumb = false;
                return true;
            }
            return false;
        }

        @Override
        public boolean mouseWheelMove(double mouseX, double mouseY, double wheelDelta) {
            if (!isMouseOverElement(mouseX, mouseY) || getMaxScrollOffset() <= 0) {
                return false;
            }

            scrollOffset += wheelDelta < 0 ? rowPitch() * 2 : -rowPitch() * 2;
            clampScrollOffset();
            return true;
        }

        private void drawVisibleItems(GuiGraphics graphics, int mouseX, int mouseY) {
            if (items.isEmpty()) return;

            int columns = getColumns();
            int pitch = rowPitch();
            int firstRow = Math.max(0, scrollOffset / pitch);
            int lastRow = Math.min(getRows() - 1, (scrollOffset + getSizeHeight() + pitch - 1) / pitch);
            int startX = getGridStartX(columns);
            int baseX = getPositionX();
            int baseY = getPositionY();

            for (int row = firstRow; row <= lastRow; row++) {
                int itemY = baseY + row * pitch - scrollOffset;
                for (int col = 0; col < columns; col++) {
                    int index = row * columns + col;
                    if (index >= items.size()) return;

                    int itemX = baseX + startX + col * pitch;
                    ItemStack stack = items.get(index);
                    boolean hovered = isMouseOverItem(mouseX, mouseY, itemX, itemY);

                    graphics.fill(itemX, itemY, itemX + PICKER_GRID_ITEM_SIZE, itemY + PICKER_GRID_ITEM_SIZE, hovered ? 0xFF34384A : 0xFF20222B);
                    graphics.fill(itemX, itemY, itemX + PICKER_GRID_ITEM_SIZE, itemY + 1, hovered ? 0xFF7986CB : 0xFF3D3D4E);
                    graphics.fill(itemX, itemY + PICKER_GRID_ITEM_SIZE - 1, itemX + PICKER_GRID_ITEM_SIZE, itemY + PICKER_GRID_ITEM_SIZE, hovered ? 0xFF7986CB : 0xFF3D3D4E);
                    graphics.fill(itemX, itemY, itemX + 1, itemY + PICKER_GRID_ITEM_SIZE, hovered ? 0xFF7986CB : 0xFF3D3D4E);
                    graphics.fill(itemX + PICKER_GRID_ITEM_SIZE - 1, itemY, itemX + PICKER_GRID_ITEM_SIZE, itemY + PICKER_GRID_ITEM_SIZE, hovered ? 0xFF7986CB : 0xFF3D3D4E);
                    graphics.renderItem(stack, itemX + 1, itemY + 1);
                    graphics.renderItemDecorations(Minecraft.getInstance().font, stack, itemX + 1, itemY + 1);
                }
            }
        }

        private void drawScrollBar(GuiGraphics graphics, int mouseX, int mouseY) {
            if (!hasScrollBar()) return;

            int trackX = getScrollTrackX();
            int trackY = getPositionY();
            int trackHeight = getScrollTrackHeight();
            int thumbY = getScrollThumbY();
            int thumbHeight = getScrollThumbHeight();
            int thumbColor = isMouseOverScrollThumb(mouseX, mouseY) || draggingScrollThumb ? 0xFFAAB2C5 : 0xFF6D7485;

            graphics.fill(trackX, trackY, trackX + PICKER_SCROLLBAR_WIDTH, trackY + trackHeight, 0x5530303A);
            graphics.fill(trackX, thumbY, trackX + PICKER_SCROLLBAR_WIDTH, thumbY + thumbHeight, thumbColor);
        }

        private ItemStack getItemAt(double mouseX, double mouseY) {
            if (!isMouseOverElement(mouseX, mouseY) || items.isEmpty() || isMouseOverScrollBar(mouseX, mouseY)) {
                return ItemStack.EMPTY;
            }

            int columns = getColumns();
            int pitch = rowPitch();
            int startX = getGridStartX(columns);
            int localX = (int) Math.floor(mouseX - getPositionX() - startX);
            int localY = (int) Math.floor(mouseY - getPositionY() + scrollOffset);
            if (localX < 0 || localY < 0) return ItemStack.EMPTY;

            int col = localX / pitch;
            int row = localY / pitch;
            if (col < 0 || col >= columns || localX % pitch >= PICKER_GRID_ITEM_SIZE || localY % pitch >= PICKER_GRID_ITEM_SIZE) {
                return ItemStack.EMPTY;
            }

            int index = row * columns + col;
            return index >= 0 && index < items.size() ? items.get(index) : ItemStack.EMPTY;
        }

        private boolean isMouseOverItem(double mouseX, double mouseY, int itemX, int itemY) {
            return mouseX >= itemX
                    && mouseY >= itemY
                    && mouseX < itemX + PICKER_GRID_ITEM_SIZE
                    && mouseY < itemY + PICKER_GRID_ITEM_SIZE;
        }

        private void scrollToMouse(double mouseY) {
            int maxScroll = getMaxScrollOffset();
            int thumbHeight = getScrollThumbHeight();
            int movable = Math.max(1, getScrollTrackHeight() - thumbHeight);
            scrollOffset = (int) Math.round((mouseY - getPositionY() - thumbHeight / 2.0) * maxScroll / movable);
            clampScrollOffset();
        }

        private int getColumns() {
            int availableWidth = Math.max(1, getSizeWidth() - PICKER_SCROLLBAR_WIDTH - PICKER_SCROLLBAR_PADDING);
            return Math.max(1, availableWidth / rowPitch());
        }

        private int getRows() {
            int columns = getColumns();
            return items.isEmpty() ? 0 : (int) Math.ceil(items.size() / (double) columns);
        }

        private int getContentHeight() {
            int rows = getRows();
            return rows <= 0 ? 0 : rows * rowPitch() - PICKER_GRID_GAP;
        }

        private int getMaxScrollOffset() {
            return Math.max(0, getContentHeight() - getSizeHeight());
        }

        private void clampScrollOffset() {
            scrollOffset = Math.max(0, Math.min(scrollOffset, getMaxScrollOffset()));
        }

        private boolean hasScrollBar() {
            return getMaxScrollOffset() > 0;
        }

        private int getGridStartX(int columns) {
            int availableWidth = Math.max(1, getSizeWidth() - PICKER_SCROLLBAR_WIDTH - PICKER_SCROLLBAR_PADDING);
            int contentWidth = columns * rowPitch() - PICKER_GRID_GAP;
            return Math.max(1, (availableWidth - contentWidth) / 2);
        }

        private int rowPitch() {
            return PICKER_GRID_ITEM_SIZE + PICKER_GRID_GAP;
        }

        private int getScrollTrackX() {
            return getPositionX() + getSizeWidth() - PICKER_SCROLLBAR_WIDTH;
        }

        private int getScrollTrackHeight() {
            return getSizeHeight();
        }

        private int getScrollThumbHeight() {
            int contentHeight = Math.max(1, getContentHeight());
            int trackHeight = getScrollTrackHeight();
            return Math.max(12, Math.min(trackHeight, (int) Math.round(trackHeight * (trackHeight / (double) contentHeight))));
        }

        private int getScrollThumbY() {
            int maxScroll = getMaxScrollOffset();
            if (maxScroll <= 0) return getPositionY();

            int movable = Math.max(1, getScrollTrackHeight() - getScrollThumbHeight());
            return getPositionY() + (int) Math.round(scrollOffset * movable / (double) maxScroll);
        }

        private boolean isMouseOverScrollBar(double mouseX, double mouseY) {
            return hasScrollBar()
                    && mouseX >= getScrollTrackX()
                    && mouseY >= getPositionY()
                    && mouseX < getScrollTrackX() + PICKER_SCROLLBAR_WIDTH
                    && mouseY < getPositionY() + getScrollTrackHeight();
        }

        private boolean isMouseOverScrollThumb(double mouseX, double mouseY) {
            if (!hasScrollBar()) return false;
            int thumbY = getScrollThumbY();
            return mouseX >= getScrollTrackX()
                    && mouseY >= thumbY
                    && mouseX < getScrollTrackX() + PICKER_SCROLLBAR_WIDTH
                    && mouseY < thumbY + getScrollThumbHeight();
        }
    }
}
