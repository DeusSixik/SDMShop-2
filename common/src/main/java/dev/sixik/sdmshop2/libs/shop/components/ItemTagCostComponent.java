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
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ItemTagCostComponent extends CostComponent {

    public static final IComponentType<ItemTagCostComponent> TYPE = new Type();
    public static final TagKey<Item> DEFAULT_TAG = TagKey.create(Registries.ITEM, new ResourceLocation("minecraft", "planks"));

    @Getter
    @ComponentConfig(translationKey = "shop.component.cost.item_tag.tag")
    private TagKey<Item> tagKey;

    @Getter
    @ComponentConfig(translationKey = "shop.component.cost.item_tag.amount")
    @ComponentNumberRange(intMin = 1)
    private int amount;

    public ItemTagCostComponent() {
        this(DEFAULT_TAG, 1);
    }

    public ItemTagCostComponent(TagKey<Item> tagKey, int amount) {
        this.tagKey = tagKey == null ? DEFAULT_TAG : tagKey;
        this.amount = Math.max(1, amount);
    }

    public ItemTagCostComponent(ResourceLocation tagId, int amount) {
        this(TagKey.create(Registries.ITEM, tagId == null ? DEFAULT_TAG.location() : tagId), amount);
    }

    @Override
    public boolean canPay(Player player, double actualPrice) {
        int required = requiredAmount(actualPrice);
        return player != null && required >= 0 && ShopItemHelper.countItem(player.getInventory(), tagKey) >= required;
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

        return ShopItemHelper.shrinkItemByPredicate(player.getInventory(), stack -> stack.is(tagKey), required);
    }

    @Override
    public void refund(Player player, double actualPrice) {
        if (player == null || player.isLocalPlayer()) return;

        int required = requiredAmount(actualPrice);
        ItemStack refundStack = firstTagItem();
        if (required > 0 && !refundStack.isEmpty()) {
            ShopItemHelper.giveItems(player, refundStack, required);
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

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public @Nullable TransformTexture getRenderIcon() {
        ItemStack[] stacks = tagItems();
        return stacks.length == 0 ? new ItemStackTexture(Items.NAME_TAG) : new ItemStackTexture(stacks);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("#" + tagKey.location());
    }

    private ItemStack firstTagItem() {
        ItemStack[] stacks = tagItems();
        return stacks.length == 0 ? ItemStack.EMPTY : stacks[0].copy();
    }

    private ItemStack[] tagItems() {
        Optional<HolderSet.Named<Item>> optionalTag = BuiltInRegistries.ITEM.getTag(tagKey);
        if (optionalTag.isEmpty() || optionalTag.get().size() <= 0) {
            return new ItemStack[0];
        }

        HolderSet.Named<Item> tag = optionalTag.get();
        ItemStack[] stacks = new ItemStack[tag.size()];
        for (int i = 0; i < tag.size(); i++) {
            stacks[i] = tag.get(i).value().getDefaultInstance();
        }
        return stacks;
    }

    private static class Type extends SerializedComponentType<ItemTagCostComponent> {

        private static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "cost_item_tag");
        private static final ComponentSerializer<ItemTagCostComponent> SERIALIZER = ComponentSerializer.<ItemTagCostComponent>create()
                .addRequired("tagKey", FieldCodecs.ITEM_TAG, ItemTagCostComponent::getTagKey, (component, value) -> component.tagKey = value == null ? DEFAULT_TAG : value)
                .addDefaultedInt("amount", ItemTagCostComponent::getAmount, (component, value) -> component.amount = Math.max(1, value), 1);

        private Type() {
            super(ItemTagCostComponent::new, SERIALIZER);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        @SuppressWarnings("unchecked")
        public ItemTagCostComponent createFromBuilder(Object... args) {
            if (args.length != 2 && args.length != 3) {
                throw new IllegalArgumentException("ItemTagCostComponent.createFromBuilder() takes 2 or 3 arguments (TagKey/ResourceLocation/String, int, (Optional) String)");
            }

            Object tag = args[0];
            int amount = (int) args[1];
            ItemTagCostComponent component;
            if (tag instanceof TagKey<?> tagKey) {
                component = new ItemTagCostComponent((TagKey<Item>) tagKey, amount);
            } else if (tag instanceof ResourceLocation location) {
                component = new ItemTagCostComponent(location, amount);
            } else {
                component = new ItemTagCostComponent(ResourceLocation.tryParse(String.valueOf(tag)), amount);
            }

            if (args.length == 3) {
                component.setGroupId((String) args[2]);
            }

            return component;
        }

        @Override
        public CurrencyIcon getIcon() {
            return new CurrencyIcon(IconType.ITEM, Items.NAME_TAG);
        }
    }
}