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
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.InputTextBox;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;
import java.util.function.Consumer;

public class SDMItemStackSelectorWidget extends WidgetGroup {

    protected Consumer<ItemStack> onItemStackUpdate;
    protected final IItemTransfer handler;
    protected final InputTextBox itemField;
    protected final boolean nbtEnabled;
    protected ItemStack item = ItemStack.EMPTY;

    public SDMItemStackSelectorWidget(int x, int y, int width) {
        this(x, y, width, true);
    }

    public SDMItemStackSelectorWidget(int x, int y, int width, boolean nbt) {
        super(x, y, width, 20);
        this.nbtEnabled = nbt;
        setClientSideWidget();
        itemField = new InputTextBox(22, 0, getItemFieldWidth(width), 20, null, s -> {
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

        addWidget(new PhantomSlotWidget(handler = new ItemStackTransfer(1), 0, 1, 1)
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
    public void setSize(Size size) {
        super.setSize(size);
        itemField.setSelfPosition(22, 0);
        itemField.setSize(getItemFieldWidth(size.width), 20);

        if (nbtEnabled && widgets.size() > 2) {
            Widget nbtButton = widgets.get(2);
            nbtButton.setSelfPosition(size.width - 21, 0);
            nbtButton.setSize(20, 20);
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
        itemField.setCurrentString(BuiltInRegistries.ITEM.getKey(item.getItem()).toString());
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
}
