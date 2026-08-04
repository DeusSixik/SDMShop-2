package dev.sixik.sdmshop2.libs.sdmeconomy.custom_currency;

import dev.sixik.sdmshop2.libs.sdmeconomy.IStoredCurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.IconType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.math.BigDecimal;
import java.util.Objects;

public class SimpleTextCurrency implements IStoredCurrency {

    private final ResourceLocation id;
    private final String displayName;
    private final String iconText;

    public SimpleTextCurrency(ResourceLocation id, String displayName, String iconText) {
        this.id = Objects.requireNonNull(id, "id");
        this.displayName = displayName == null || displayName.isBlank() ? id.toString() : displayName.trim();
        this.iconText = iconText == null || iconText.isBlank() ? "$" : iconText.trim();
    }

    @Override
    public BigDecimal getDefaultBalance() {
        return BigDecimal.ZERO;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public Component getDisplayName() {
        return isTranslationKey(displayName)
                ? Component.translatable(displayName)
                : Component.literal(displayName);
    }

    public String getDisplayNameValue() {
        return displayName;
    }

    @Override
    public String format(BigDecimal decimal) {
        return decimal == null ? "0" : decimal.stripTrailingZeros().toPlainString();
    }

    @Override
    public int getColor() {
        return 0;
    }

    @Override
    public CurrencyIcon getIcon() {
        return new CurrencyIcon(IconType.TEXTURE, iconText);
    }

    public String getIconText() {
        return iconText;
    }

    private static boolean isTranslationKey(String value) {
        if (value == null || value.isBlank() || value.indexOf('.') < 0 || value.indexOf(' ') >= 0) {
            return false;
        }

        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (!(c == '.'
                    || c == '_'
                    || c == '-'
                    || c == '/'
                    || c == ':'
                    || Character.isDigit(c)
                    || c >= 'a' && c <= 'z'
                    || c >= 'A' && c <= 'Z')) {
                return false;
            }
        }
        return true;
    }
}
