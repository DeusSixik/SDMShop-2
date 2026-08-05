package dev.sixik.sdmshop2.libs.sdmeconomy.custom_currency;

import dev.sixik.sdmshop2.libs.sdmeconomy.IStoredCurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.IconType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.math.BigDecimal;

public class BasicCoinCurrency implements IStoredCurrency {

    public static final BasicCoinCurrency CURRENCY = new BasicCoinCurrency();

    public static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "coin");
    public static final Component DISPLAY_NAME = Component.translatable(ID.toString().replace(":", "_"));
    protected static final CurrencyIcon ICON = new CurrencyIcon(IconType.TEXTURE, "◎");

    protected BasicCoinCurrency() { }

    @Override
    public BigDecimal getDefaultBalance() {
        return BigDecimal.ZERO;
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Component getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public String format(BigDecimal decimal) {
        return String.valueOf(decimal.doubleValue());
    }

    @Override
    public CurrencyIcon getIcon() {
        return ICON;
    }

    @Override
    public int getColor() {
        return 0;
    }
}
