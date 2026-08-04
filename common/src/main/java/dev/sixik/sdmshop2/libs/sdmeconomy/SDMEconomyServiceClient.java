package dev.sixik.sdmshop2.libs.sdmeconomy;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public class SDMEconomyServiceClient extends SDMEconomyService {

    public static Object2ObjectOpenHashMap<ResourceLocation, IExternalCurrency> CURRENCIES = new Object2ObjectOpenHashMap<>();
    public static Object2ObjectOpenHashMap<ResourceLocation, IStoredCurrency> STORED_CURRENCIES = new Object2ObjectOpenHashMap<>();

    public static Object2ObjectOpenHashMap<ResourceLocation, ICurrency> getAllCurrencies() {
        Object2ObjectOpenHashMap<ResourceLocation, ICurrency> map = new Object2ObjectOpenHashMap<>(SDMEconomyCurrencyRegistry.getAllCurrenciesMap());
        if(SDMEconomyPlatform.server == null) {
            map.putAll(SDMEconomyServiceClient.STORED_CURRENCIES);
            map.putAll(SDMEconomyServiceClient.CURRENCIES);
        }
        return map;
    }

    public static Object2ObjectOpenHashMap<ResourceLocation, IExternalCurrency> getAllExternalCurrencies() {
        Object2ObjectOpenHashMap<ResourceLocation, IExternalCurrency> map = new Object2ObjectOpenHashMap<>(SDMEconomyCurrencyRegistry.getCurrenciesMap());
        if(SDMEconomyPlatform.server == null)
            map.putAll(SDMEconomyServiceClient.CURRENCIES);
        return map;
    }

    @Nullable
    public static ICurrency getCurrency(ResourceLocation id) {
        return getAllCurrencies().get(id);
    }

    public static BigDecimal getBalance(ICurrency currency, @Nullable Player player) {
        if (currency == null) {
            return BigDecimal.ZERO;
        }
        if (currency instanceof IExternalCurrency externalCurrency) {
            return player == null ? BigDecimal.ZERO : externalCurrency.getBalance(player);
        }
        if (currency instanceof IStoredCurrency storedCurrency) {
            return getInstanceClient().getBankAccount().getBalance(storedCurrency);
        }

        return BigDecimal.ZERO;
    }

    @Getter
    private static final SDMEconomyServiceClient InstanceClient = new SDMEconomyServiceClient();

    @Getter
    protected BankAccount bankAccount;

    public SDMEconomyServiceClient() {
        this.bankAccount = new BankAccount(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

    public SDMEconomyServiceClient(BankAccount account) {
        this.bankAccount = account;
    }

    @Override
    @Deprecated
    public BankAccount getAccount(UUID gameProfileId) {
        return bankAccount;
    }

    @Override
    public void unloadPlayer(UUID gameProfileId) { }

    @Override
    public void saveAllDirty() { }
}
