package dev.sixik.sdmshop2.libs.shop.components;

import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.util.DrawerHelper;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.IconType;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopEmptyWidget;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.RewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfig;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentNumberRange;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import dev.sixik.sdmshop2.utils.ShopItemHelper;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class ItemRewardComponent extends RewardComponent {

    public static final IComponentType<ItemRewardComponent> TYPE = new Type();

    @Getter
    @ComponentConfig(translationKey = "shop.component.reward.item.reward_item")
    private ItemStack rewardItem;

    @Getter
    @ComponentConfig(translationKey = "shop.component.reward.item.amount")
    @ComponentNumberRange(intMin = 1)
    private int amount;

    public ItemRewardComponent() {
        this(ItemStack.EMPTY, 1);
    }

    public ItemRewardComponent(Item item, int amount) {
        this(item.getDefaultInstance(), amount);
    }

    public ItemRewardComponent(ItemStack rewardItem, int amount) {
        this.rewardItem = rewardItem.getCount() > 1 ? rewardItem.copyWithCount(1) : rewardItem;
        this.amount = Math.max(1, amount);
    }

    @Override
    public void reward(ServerPlayer player, int inAmount) {
        ShopItemHelper.giveItems(player, rewardItem, (long) amount * inAmount);
    }

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public @Nullable Widget createRender() {
        final ItemStack item = rewardItem.copyWithCount(amount);
        return new ShopEmptyWidget().setBackground(
                new ItemStackTexture(item)
        ).setHoverTooltips(DrawerHelper.getItemToolTip(item));
    }

    @Override
    public @Nullable Component getDisplayTitle() {
        return Component.empty().append(rewardItem.getHoverName()).withStyle(rewardItem.getRarity().color);
    }

    private static class Type extends SerializedComponentType<ItemRewardComponent> {

        private static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "reward_item");
        private static final ComponentSerializer<ItemRewardComponent> SERIALIZER = ComponentSerializer.<ItemRewardComponent>create()
                .addRequired("item", FieldCodecs.ITEM_STACK_ID_NBT, ItemRewardComponent::getRewardItem, (component, value) -> component.rewardItem = value)
                .addDefaultedInt("amount", ItemRewardComponent::getAmount, (component, value) -> component.amount = Math.max(1, value), 1);

        private Type() {
            super(ItemRewardComponent::new, SERIALIZER);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public ItemRewardComponent createFromBuilder(Object... args) {
            if(args.length != 2)
                throw new IllegalArgumentException("ItemRewardComponent.createFromBuilder() takes 2 arguments (ItemStack, int)");

            return new ItemRewardComponent((ItemStack) args[0], (int) args[1]);
        }

        @Override
        public CurrencyIcon getIcon() {
           return new CurrencyIcon(IconType.ITEM, Items.DIAMOND);
        }
    }
}
