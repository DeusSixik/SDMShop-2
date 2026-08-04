package dev.sixik.sdmshop2.libs.shop.client.ui.widgets;

import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.misc.ItemStackTransfer;
import com.lowdragmc.lowdraglib.side.fluid.FluidTransferHelper;
import com.lowdragmc.lowdraglib.side.item.IItemTransfer;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.ButtonWidget;
import dev.sixik.sdmshop2.libs.shop_ldlib_extension.widgets.InputTextBox;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SDMBlockSelectorWidget extends WidgetGroup {

    private Consumer<BlockState> onBlockStateUpdate;
    private Block block;
    private final IItemTransfer handler;
    private final InputTextBox blockField;
    private final boolean stateSelector;
    private final Map<Property, Comparable> properties;

    public SDMBlockSelectorWidget(int x, int y, int width, boolean isState) {
        super(x, y, width, 20);
        this.stateSelector = isState;
        setClientSideWidget();
        properties = new HashMap<>();
        blockField = new InputTextBox(22, 0, getBlockFieldWidth(width), 20, null, s -> {
            if (s != null && !s.isEmpty()) {
                ResourceLocation location = ResourceLocation.tryParse(s);
                if (location == null) return;

                Block block = BuiltInRegistries.BLOCK.get(location);
                if (this.block != block) {
                    this.block = block;
                    onUpdate();
                }
            }
        });
        blockField.setResourceLocationOnly();
        blockField.setClientSideWidget();
        blockField.setHoverTooltips("ldlib.gui.tips.block_selector");

        addWidget(new PhantomSlotWidget(handler = new ItemStackTransfer(1), 0, 1, 1)
                .setClearSlotOnRightClick(true)
                .setChangeListener(() -> {
                    ItemStack stack = handler.getStackInSlot(0);
                    if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem itemBlock)) {
                        var fluidTransfer = FluidTransferHelper.getFluidTransfer(handler, 0);
                        if (fluidTransfer != null) {
                            if (fluidTransfer.getTanks() > 0) {
                                var fluid = fluidTransfer.getFluidInTank(0).getFluid();
                                setBlock(fluid.defaultFluidState().createLegacyBlock());
                                onUpdate();
                                return;
                            }
                        }
                        if (block != null) {
                            setBlock(null);
                            onUpdate();
                        }
                    } else {
                        setBlock(itemBlock.getBlock().defaultBlockState());
                        onUpdate();
                    }
                }).setBackgroundTexture(new ColorBorderTexture(1, -1)));
        addWidget(blockField);
        if (isState) {
            addWidget(new ButtonWidget(width - 21, 0, 20, 20, new ItemStackTexture(Items.BLACK_WOOL, Items.WHITE_WOOL, Items.BLUE_WOOL, Items.GREEN_WOOL, Items.YELLOW_WOOL), cd -> {
                DraggableScrollableWidgetGroup group;
                new DialogWidget(getGui().mainGroup, isClientSideWidget)
                        .setOnClosed(this::onUpdate)
                        .addWidget(group = new DraggableScrollableWidgetGroup(0, 0, getGui().mainGroup.getSize().width, getGui().mainGroup.getSize().height)
                                .setYScrollBarWidth(4).setYBarStyle(null, new ColorRectTexture(-1))
                                .setXScrollBarHeight(4).setXBarStyle(null, new ColorRectTexture(-1))
                                .setBackground(new ColorRectTexture(0x8f222222)));
                int i = properties.size() - 1;
                for (Map.Entry<Property, Comparable> entry : properties.entrySet()) {
                    Property property = entry.getKey();
                    Comparable value = entry.getValue();
                    group.addWidget(new SelectorWidget(3, 3 + i * 20, 100, 15, property.getPossibleValues().stream().map(v -> property.getName((Comparable) v)).toList(), -1)
                            .setValue(property.getName(value))
                            .setOnChanged(newValue -> properties.put(property, (Comparable) property.getValue(newValue).get()))
                            .setButtonBackground(ResourceBorderTexture.BUTTON_COMMON)
                            .setBackground(new ColorRectTexture(0xffaaaaaa)));
                    group.addWidget(new LabelWidget(105, 6 + i * 20, property.getName()));
                    i--;
                }
            }).setHoverBorderTexture(1, -1).setHoverTooltips("ldlib.gui.tips.block_meta"));
        }
    }

    @Override
    public void setSize(Size size) {
        super.setSize(size);
        blockField.setSelfPosition(22, 0);
        blockField.setSize(getBlockFieldWidth(size.width), 20);

        if (stateSelector && widgets.size() > 2) {
            Widget stateButton = widgets.get(2);
            stateButton.setSelfPosition(size.width - 21, 0);
            stateButton.setSize(20, 20);
        }
    }

    private int getBlockFieldWidth(int width) {
        return Math.max(1, width - (stateSelector ? 46 : 26));
    }

    public BlockState getBlock() {
        BlockState state;
        if (block == null) {
            state = null;
        } else {
            state = block.defaultBlockState();
            for (Map.Entry<Property, Comparable> entry : properties.entrySet()) {
                state = state.setValue(entry.getKey(), entry.getValue());
            }
        }
        return state;
    }

    public SDMBlockSelectorWidget setBlock(BlockState blockState) {
        properties.clear();
        if (blockState == null) {
            block = null;
            handler.setStackInSlot(0, ItemStack.EMPTY);
            blockField.setCurrentString("");
        } else {
            block = blockState.getBlock();
            new ItemStack(block);
            handler.setStackInSlot(0, new ItemStack(block));
            blockField.setCurrentString(BuiltInRegistries.BLOCK.getKey(block));
            for (Property<?> property : blockState.getBlock().getStateDefinition().getProperties()) {
                properties.put(property, blockState.getValue(property));
            }
        }
        return this;
    }

    public SDMBlockSelectorWidget setOnBlockStateUpdate(Consumer<BlockState> onBlockStateUpdate) {
        this.onBlockStateUpdate = onBlockStateUpdate;
        return this;
    }

    private void onUpdate() {
        handler.setStackInSlot(0, block == null ? ItemStack.EMPTY : new ItemStack(block));
        if (onBlockStateUpdate != null) {
            onBlockStateUpdate.accept(getBlock());
        }
    }
}
