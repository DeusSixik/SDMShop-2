package dev.sixik.sdmshop2.libs.sdmeconomy.custom_currency;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrencyType;
import dev.sixik.sdmshop2.libs.sdmeconomy.IExternalCurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.IconType;
import dev.sixik.sdmshop2.utils.ShopItemHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class ExternalItemCurrency implements IExternalCurrency {

    public static final ICurrencyType<ExternalItemCurrency> TYPE = new ExternalItemCurrencyType();

    private final ResourceLocation id;
    private final Component displayName;
    private final ItemStack itemType;
    private final CurrencyIcon currencyIcon;

    public ExternalItemCurrency(String id, ItemStack itemType) {
        this(id.contains(":") ? ResourceLocation.tryParse(id) : ResourceLocation.tryBuild("sdm", id), itemType);
    }

    public ExternalItemCurrency(ResourceLocation id, ItemStack itemType) {
        Objects.requireNonNull(id, "Id cannot be null!");
        this.id = id;
        this.displayName = Component.translatable(id.toString().replace(":", "_"));
        this.itemType = itemType;
        this.currencyIcon = new CurrencyIcon(IconType.ITEM, itemType);
    }

    @Override
    public boolean withdraw(ServerPlayer player, BigDecimal amount, boolean simulate) {
        long itemAmount = toItemAmount(amount);
        if (itemAmount <= 0 || itemAmount > Integer.MAX_VALUE) return false;

        BigDecimal currentBalance = getBalance(player);
        if (currentBalance.compareTo(BigDecimal.valueOf(itemAmount)) < 0) return false;

        if (!simulate) {
            return ShopItemHelper.shrinkItem(player.getInventory(), itemType, (int) itemAmount, true, false);
        }
        return true;
    }

    @Override
    public boolean deposit(ServerPlayer player, BigDecimal amount, boolean simulate) {
        long itemAmount = toItemAmount(amount);
        if (itemAmount <= 0) return false;

        if (!simulate) {
            ItemStack stack = itemType.copyWithCount(1);
            return ShopItemHelper.giveItems(player, stack, itemAmount);
        }
        return true;
    }

    @Override
    public BigDecimal getBalance(Player player) {
        return new BigDecimal(ShopItemHelper.countItem(player.getInventory(), itemType, true, false));
    }

    @Override
    public ICurrencyType<? extends IExternalCurrency> getType() {
        return TYPE;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public Component getDisplayName() {
        return displayName;
    }

    @Override
    public String format(BigDecimal decimal) {
        return decimal.toString();
    }

    @Override
    public int getColor() {
        return 0;
    }

    @Override
    public CurrencyIcon getIcon() {
        return currencyIcon;
    }

    private static long toItemAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) return -1L;

        try {
            BigInteger integer = amount.toBigIntegerExact();
            return integer.longValueExact();
        } catch (ArithmeticException e) {
            return -1L;
        }
    }

    private static class ExternalItemCurrencyType implements ICurrencyType<ExternalItemCurrency> {

        @Override
        public Class<ExternalItemCurrency> getOwnerClass() {
            return ExternalItemCurrency.class;
        }

        @Override
        public ExternalItemCurrency deserialize(ResourceLocation id, JsonObject json) {

            if(!json.has("item"))
                throw new NullPointerException("Param with id 'item' not exists!");

            String itemIdStr = json.get("item").getAsString();
            ResourceLocation itemId = ResourceLocation.tryParse(itemIdStr);

            Item item = BuiltInRegistries.ITEM.get(itemId);

            if (item == null || item == Items.AIR) {
                throw new IllegalArgumentException("Item not found: " + itemIdStr);
            }

            CompoundTag nbt = null;
            if (json.has("nbt")) {
                String nbtString = json.get("nbt").getAsString();
                try {
                    nbt = TagParser.parseTag(nbtString);
                } catch (Exception e) {
                    throw new JsonSyntaxException("Invalid NBT in currency " + id + ": " + e.getMessage());
                }
            }


            ItemStack itemStack = item.getDefaultInstance();

            if(nbt != null)
                itemStack.setTag(nbt);

            return new ExternalItemCurrency(id, itemStack);
        }

        @Override
        public JsonObject serialize(ExternalItemCurrency currency) {
            final ItemStack item = currency.itemType;

            JsonObject json = new JsonObject();
            serializeType(json, "minecraft:item");
            json.addProperty("item", BuiltInRegistries.ITEM.getKey(item.getItem()).toString());

            if(item.getTag() != null) {
                json.addProperty("nbt", item.getTag().toString());
            }

            return json;
        }
    }
}
