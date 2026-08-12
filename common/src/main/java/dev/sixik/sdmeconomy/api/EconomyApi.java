package dev.sixik.sdmeconomy.api;

import dev.sixik.sdmshop2.libs.sdmeconomy.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public final class EconomyApi {

    public static SDMEconomyService getServices(final boolean client) {
        return client ? SDMEconomyServiceClient.getInstanceClient() : SDMEconomyService.getInstance();
    }

    public static BankAccount getAccount(Player player) {
        return getAccount(player.getGameProfile().getId());
    }

    public static BankAccount getAccount(UUID gameProfileId) {
        return getServices(false).getAccount(gameProfileId);
    }

    public static BigDecimal getBalance(IExternalCurrency currency, Player player) {
        return currency.getBalance(player);
    }

    public static BigDecimal getBalance(IStoredCurrency currency, Player player) {
        return getAccount(player).getBalance(currency);
    }

    @Environment(EnvType.CLIENT)
    public static BigDecimal getBalanceOnClient(ICurrency currency) {
        return SDMEconomyServiceClient.getBalance(currency, net.minecraft.client.Minecraft.getInstance().player);
    }

    public static void registerCurrencyType(ResourceLocation id, ICurrencyType<?> type) {
        SDMEconomyCurrencyRegistry.registerType(id, type);
    }

    public static <T extends IStoredCurrency> T registerCurrency(T currency) {
        return SDMEconomyCurrencyRegistry.registerStoredCurrency(currency);
    }

    public static <T extends IStoredCurrency> boolean registerAndSaveCurrency(T currency) {
        return SDMEconomyCurrencyRegistry.registerAndSaveStoredCurrency(currency);
    }

    public static <T extends IExternalCurrency> boolean registerAndSaveCurrency(T currency) {
        return SDMEconomyCurrencyRegistry.registerAndSaveCurrency(currency);
    }

    public static Map<ResourceLocation, IExternalCurrency> getCurrencies(boolean client) {
        return client ? SDMEconomyServiceClient.getAllExternalCurrencies()
                      : SDMEconomyCurrencyRegistry.getCurrenciesMap();
    }

    public static Map<ResourceLocation, IStoredCurrency> getStoredCurrencies(boolean client) {
        return client ? new Object2ObjectOpenHashMap<>(SDMEconomyServiceClient.STORED_CURRENCIES)
                      : SDMEconomyCurrencyRegistry.getStoredCurrenciesMap();
    }

    @Nullable
    public static ICurrency getAnyCurrency(ResourceLocation id) {
        return SDMEconomyCurrencyRegistry.getAnyCurrency(id);
    }

    @Nullable
    public static IStoredCurrency getStoredCurrency(ResourceLocation id)  {
        return SDMEconomyCurrencyRegistry.getStoredCurrency(id);
    }

    @Nullable
    public static IExternalCurrency getCurrency(ResourceLocation id) {
        return SDMEconomyCurrencyRegistry.getCurrency(id);
    }

    @Nullable
    public static IExternalCurrency getCurrency(String id) {
        return SDMEconomyCurrencyRegistry.getCurrency(id);
    }

    public static Map<ResourceLocation, ICurrency> getAllCurrencies() {
        return SDMEconomyCurrencyRegistry.getAllCurrenciesMap();
    }
}
