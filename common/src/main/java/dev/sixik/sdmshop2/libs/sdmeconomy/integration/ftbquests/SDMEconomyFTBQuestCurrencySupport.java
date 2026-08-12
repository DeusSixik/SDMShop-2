package dev.sixik.sdmshop2.libs.sdmeconomy.integration.ftbquests;

import dev.sixik.sdmshop2.libs.sdmeconomy.BankAccount;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.IExternalCurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.IStoredCurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyCurrencyRegistry;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyPlatform;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyService;
import dev.sixik.sdmshop2.libs.sdmeconomy.custom_currency.BasicCoinCurrency;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

final class SDMEconomyFTBQuestCurrencySupport {

    static final String CURRENCY_ID_KEY = "currency_id";
    static final String VALUE_KEY = "value";
    static final String RANDOM_BONUS_KEY = "random_bonus";
    static final String LEGACY_REWARD_VALUE_KEY = "ftb_money";

    static final String CURRENCY_ID_NAME_KEY = "ftbquests.sdmeconomy.currency_id";
    static final String VALUE_NAME_KEY = "ftbquests.sdmeconomy.currency_value";
    static final String RANDOM_BONUS_NAME_KEY = "ftbquests.sdmeconomy.currency_random_bonus";
    static final String BALANCE_NAME_KEY = "ftbquests.sdmeconomy.balance";
    static final String MISSING_CURRENCY_KEY = "ftbquests.sdmeconomy.currency_missing";

    private SDMEconomyFTBQuestCurrencySupport() {
    }

    static ResourceLocation defaultCurrencyId() {
        return BasicCoinCurrency.ID;
    }

    static ResourceLocation normalizeCurrencyId(ResourceLocation currencyId) {
        return currencyId == null ? defaultCurrencyId() : currencyId;
    }

    static ICurrency getCurrency(ResourceLocation currencyId) {
        ResourceLocation id = normalizeCurrencyId(currencyId);
        ICurrency currency = SDMEconomyCurrencyRegistry.getAnyCurrency(id);
        return currency == null && BasicCoinCurrency.ID.equals(id) ? BasicCoinCurrency.CURRENCY : currency;
    }

    static boolean deposit(ServerPlayer player, ResourceLocation currencyId, BigDecimal amount) {
        if (player == null || amount == null || amount.signum() <= 0) {
            return false;
        }

        ICurrency currency = getCurrency(currencyId);
        if (currency instanceof IStoredCurrency storedCurrency) {
            BankAccount account = SDMEconomyService.getInstance().getAccount(player.getGameProfile().getId());
            account.setBalance(storedCurrency, account.getBalance(storedCurrency).add(amount));
            SDMEconomyPlatform.syncPlayerAccount(player);
            return true;
        }

        if (currency instanceof IExternalCurrency externalCurrency) {
            return externalCurrency.deposit(player, amount, false);
        }

        player.sendSystemMessage(Component.translatable(MISSING_CURRENCY_KEY, normalizeCurrencyId(currencyId)).withStyle(ChatFormatting.RED));
        return false;
    }

    static boolean withdraw(ServerPlayer player, ResourceLocation currencyId, BigDecimal amount) {
        if (player == null || amount == null || amount.signum() <= 0) {
            return false;
        }

        ICurrency currency = getCurrency(currencyId);
        if (currency instanceof IStoredCurrency storedCurrency) {
            BankAccount account = SDMEconomyService.getInstance().getAccount(player.getGameProfile().getId());
            BigDecimal balance = account.getBalance(storedCurrency);
            if (balance.compareTo(amount) < 0) {
                return false;
            }

            account.setBalance(storedCurrency, balance.subtract(amount));
            SDMEconomyPlatform.syncPlayerAccount(player);
            return true;
        }

        if (currency instanceof IExternalCurrency externalCurrency) {
            return externalCurrency.withdraw(player, amount, false);
        }

        player.sendSystemMessage(Component.translatable(MISSING_CURRENCY_KEY, normalizeCurrencyId(currencyId)).withStyle(ChatFormatting.RED));
        return false;
    }

    static BigDecimal getBalance(ServerPlayer player, ResourceLocation currencyId) {
        if (player == null) {
            return BigDecimal.ZERO;
        }

        ICurrency currency = getCurrency(currencyId);
        if (currency instanceof IStoredCurrency storedCurrency) {
            return SDMEconomyService.getInstance().getAccount(player.getGameProfile().getId()).getBalance(storedCurrency);
        }

        if (currency instanceof IExternalCurrency externalCurrency) {
            BigDecimal balance = externalCurrency.getBalance(player);
            return balance == null ? BigDecimal.ZERO : balance;
        }

        return BigDecimal.ZERO;
    }

    static long payableWholeAmount(ServerPlayer player, ResourceLocation currencyId, long remaining) {
        if (remaining <= 0L) {
            return 0L;
        }

        BigDecimal balance = getBalance(player, currencyId);
        if (balance.signum() <= 0) {
            return 0L;
        }

        BigInteger whole = balance.setScale(0, RoundingMode.DOWN).toBigInteger();
        BigInteger capped = whole.min(BigInteger.valueOf(remaining)).min(BigInteger.valueOf(Long.MAX_VALUE));
        return Math.max(0L, capped.longValue());
    }

    static BigDecimal amount(long value) {
        return BigDecimal.valueOf(Math.max(0L, value));
    }

    static String formatAmount(ResourceLocation currencyId, long amount) {
        return formatAmount(currencyId, amount(amount));
    }

    static String formatAmount(ResourceLocation currencyId, BigDecimal amount) {
        ICurrency currency = getCurrency(currencyId);
        if (currency == null) {
            return amount.toPlainString() + " " + normalizeCurrencyId(currencyId);
        }

        return currency.format(amount);
    }

    static Component currencyDisplayName(ResourceLocation currencyId) {
        ICurrency currency = getCurrency(currencyId);
        if (currency == null) {
            return Component.literal(normalizeCurrencyId(currencyId).toString());
        }

        return currency.getDisplayName() == null ? Component.literal(currency.getId().toString()) : currency.getDisplayName();
    }
}
