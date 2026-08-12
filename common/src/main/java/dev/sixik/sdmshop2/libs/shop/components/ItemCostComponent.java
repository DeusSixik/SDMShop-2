package dev.sixik.sdmshop2.libs.shop.components;

import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.texture.TransformTexture;
import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.IconType;
import dev.sixik.sdmshop2.libs.shop.components.api.CostComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfig;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentNumberRange;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import dev.sixik.sdmshop2.utils.ShopItemHelper;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class ItemCostComponent extends CostComponent {

    public static final IComponentType<ItemCostComponent> TYPE = new Type();

    @Getter
    @ComponentConfig(translationKey = "shop.component.cost.item.item")
    private ItemStack item;

    @Getter
    @ComponentConfig(translationKey = "shop.component.cost.item.amount")
    @ComponentNumberRange(intMin = 1)
    private int amount;

    public ItemCostComponent() {
        this(Items.DIAMOND, 1);
    }

    public ItemCostComponent(Item item, int amount) {
        this(item == null ? ItemStack.EMPTY : item.getDefaultInstance(), amount);
    }

    public ItemCostComponent(ItemStack item, int amount) {
        this.item = normalizeItem(item);
        this.amount = Math.max(1, amount);
    }

    @Override
    public boolean canPay(Player player, double actualPrice) {
        int required = requiredAmount(actualPrice);
        return player != null
                && required >= 0
                && (required == 0 || (!item.isEmpty() && countAvailable(player) >= required));
    }

    @Override
    public void pay(Player player, double actualPrice) {
        tryPay(player, actualPrice);
    }

    @Override
    public boolean tryPay(Player player, double actualPrice) {
        if (player == null) return false;
        if (player.isLocalPlayer()) {
            SDMShop2.LOGGER.warn("Call Pay methods on client!");
            return false;
        }

        int required = requiredAmount(actualPrice);
        if (required < 0 || !canPay(player, actualPrice)) return false;
        if (required == 0) return true;

        return ShopItemHelper.shrinkItem(player.getInventory(), item, required, true, true);
    }

    @Override
    public void refund(Player player, double actualPrice) {
        if (player == null || player.isLocalPlayer()) return;

        int required = requiredAmount(actualPrice);
        if (required > 0 && !item.isEmpty()) {
            ShopItemHelper.giveItems(player, item, required);
        }
    }

    @Override
    public double getBaseAmount() {
        return amount;
    }

    public int requiredAmount(double actualPrice) {
        if (!Double.isFinite(actualPrice) || actualPrice < 0 || actualPrice > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) Math.ceil(actualPrice);
    }

    public int countAvailable(Player player) {
        return player == null || item.isEmpty()
                ? 0
                : ShopItemHelper.countItem(player.getInventory(), item, true, true);
    }

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public @Nullable TransformTexture getRenderIcon() {
        return new ItemStackTexture(item.isEmpty() ? Items.BARRIER.getDefaultInstance() : item.copyWithCount(amount));
    }

    @Override
    public Component getDisplayName() {
        return item.isEmpty()
                ? Component.empty()
                : Component.empty().append(item.getHoverName()).withStyle(item.getRarity().color);
    }

    private static ItemStack normalizeItem(ItemStack item) {
        return item == null || item.isEmpty() ? ItemStack.EMPTY : item.copyWithCount(1);
    }

    private static class Type extends SerializedComponentType<ItemCostComponent> {

        private static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "cost_item");
        private static final ComponentSerializer<ItemCostComponent> SERIALIZER = ComponentSerializer.<ItemCostComponent>create()
                .addRequired("item", FieldCodecs.ITEM_STACK_ID_NBT, ItemCostComponent::getItem, (component, value) -> component.item = normalizeItem(value))
                .addDefaultedInt("amount", ItemCostComponent::getAmount, (component, value) -> component.amount = Math.max(1, value), 1);

        private Type() {
            super(ItemCostComponent::new, SERIALIZER);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public ItemCostComponent createFromBuilder(Object... args) {
            if (args.length != 2 && args.length != 3) {
                throw new IllegalArgumentException("ItemCostComponent.createFromBuilder() takes 2 or 3 arguments (ItemStack/Item/ResourceLocation/String, int, (Optional) String)");
            }

            ItemStack itemStack = parseItemStack(args[0]);
            ItemCostComponent component = new ItemCostComponent(itemStack, (int) args[1]);
            if (args.length == 3) {
                component.setGroupId((String) args[2]);
            }

            return component;
        }

        @Override
        public CurrencyIcon getIcon() {
            return new CurrencyIcon(IconType.ITEM, Items.DIAMOND);
        }

        private ItemStack parseItemStack(Object value) {
            if (value instanceof ItemStack stack) {
                return stack;
            }
            if (value instanceof Item item) {
                return item.getDefaultInstance();
            }

            ResourceLocation id = value instanceof ResourceLocation location
                    ? location
                    : ResourceLocation.tryParse(String.valueOf(value));
            if (id == null) {
                return ItemStack.EMPTY;
            }

            return BuiltInRegistries.ITEM.get(id).getDefaultInstance();
        }
    }
}
