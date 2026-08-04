package dev.sixik.sdmshop2.libs.sdmeconomy.custom_currency;

import dev.sixik.sdmshop2.libs.sdmeconomy.CodecCurrencyType;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrencyType;
import dev.sixik.sdmshop2.libs.sdmeconomy.IExternalCurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.IconType;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import dev.sixik.sdmshop2.utils.ShopItemHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class ExternalItemCurrency implements IExternalCurrency {

    public static final ICurrencyType<ExternalItemCurrency> TYPE = CodecCurrencyType
            .builder(ExternalItemCurrency.class, ResourceLocation.tryBuild("minecraft", "item"))
            .field("item", FieldCodecs.ITEM_STACK_ID_NBT, currency -> currency.itemType, ItemStack.EMPTY)
            .build((id, values) -> fromSerializedItem(id, values.get(0)));

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
//        this.displayName = Component.translatable(id.toString().replace(":", "_"));
        this.displayName = Component.empty().append(itemType.getHoverName()).withStyle(itemType.getRarity().color);
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

    public ItemStack getItemType() {
        return itemType.copyWithCount(1);
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

    private static ExternalItemCurrency fromSerializedItem(ResourceLocation id, ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty() || itemStack.getItem() == Items.AIR) {
            throw new IllegalArgumentException("Item not found or empty in currency " + id);
        }

        return new ExternalItemCurrency(id, itemStack);
    }
}
