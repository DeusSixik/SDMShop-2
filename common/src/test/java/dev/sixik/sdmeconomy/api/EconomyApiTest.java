package dev.sixik.sdmeconomy.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mojang.authlib.GameProfile;

import dev.sixik.sdmshop2.libs.sdmeconomy.BankAccount;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.IExternalCurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.IStoredCurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyCurrencyRegistry;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyPlatform;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyService;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class EconomyApiTest {
    private SDMEconomyService previousService;
    private MinecraftServer previousServer;
    private Object2ObjectOpenHashMap<ResourceLocation, IExternalCurrency>
            previousExternalCurrencies;
    private Object2ObjectOpenHashMap<ResourceLocation, IStoredCurrency> previousStoredCurrencies;

    @BeforeAll
    static void initializeMinecraft() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @BeforeEach
    void isolateServices() {
        previousService = SDMEconomyService.getInstance();
        previousServer = SDMEconomyPlatform.server;
        previousExternalCurrencies = SDMEconomyServiceClient.CURRENCIES;
        previousStoredCurrencies = SDMEconomyServiceClient.STORED_CURRENCIES;
        SDMEconomyService.init(null);
        SDMEconomyPlatform.server = null;
        SDMEconomyServiceClient.CURRENCIES = new Object2ObjectOpenHashMap<>();
        SDMEconomyServiceClient.STORED_CURRENCIES = new Object2ObjectOpenHashMap<>();
    }

    @AfterEach
    void restoreServices() {
        SDMEconomyService.init(previousService);
        SDMEconomyPlatform.server = previousServer;
        SDMEconomyServiceClient.CURRENCIES = previousExternalCurrencies;
        SDMEconomyServiceClient.STORED_CURRENCIES = previousStoredCurrencies;
    }

    @Test
    void getAccount_serviceNotRunning_reportsLifecycleError() {
        assertNull(EconomyApi.getServices(false));
        IllegalStateException error =
                assertThrows(
                        IllegalStateException.class,
                        () -> EconomyApi.getAccount(UUID.randomUUID()));
        assertTrue(error.getMessage().contains("Server economy service"));
        assertSame(SDMEconomyServiceClient.getInstanceClient(), EconomyApi.getServices(true));
    }

    @Test
    void getAccount_afterShutdown_reportsLifecycleError() {
        SDMEconomyService service = SDMEconomyService.init(new SDMEconomyService());
        try {
            assertSame(service, EconomyApi.getServices(false));
        } finally {
            service.shutdown();
        }
        assertNull(EconomyApi.getServices(false));
        assertThrows(IllegalStateException.class, () -> EconomyApi.getAccount(UUID.randomUUID()));
    }

    @Test
    void getBalance_storedCurrency_resolvesProfileAndPreservesDecimalPrecision() {
        UUID profileId = UUID.randomUUID();
        Player player = mock(Player.class);
        when(player.getGameProfile()).thenReturn(new GameProfile(profileId, "account-owner"));
        IStoredCurrency currency = storedCurrency("stored", new BigDecimal("25.50"));
        BankAccount account = new BankAccount(profileId);
        SDMEconomyService service = mock(SDMEconomyService.class);
        when(service.getAccount(profileId)).thenReturn(account);
        SDMEconomyService.init(service);

        assertSame(account, EconomyApi.getAccount(player));
        assertEquals(new BigDecimal("25.50"), EconomyApi.getBalance((ICurrency) currency, player));
        BigDecimal amount = new BigDecimal("12345678901234567890.12345678901234567890");
        account.setBalance(currency, amount);
        assertEquals(amount, EconomyApi.getBalance(currency, player));
        assertEquals(amount, EconomyApi.getBalance((ICurrency) currency, player));
    }

    @Test
    void getBalance_externalCurrency_doesNotRequireServerService() {
        Player player = mock(Player.class);
        IExternalCurrency currency = mock(IExternalCurrency.class);
        BigDecimal amount = new BigDecimal("98765432109876543210.000000000000000001");
        when(currency.getBalance(player)).thenReturn(amount);

        assertEquals(amount, EconomyApi.getBalance((ICurrency) currency, player));
        assertEquals(amount, EconomyApi.getBalance(currency, player));
        assertNull(EconomyApi.getServices(false));
    }

    @Test
    void getBalance_currencyWithBothInterfaces_prefersExternalBalance() {
        Player player = mock(Player.class);
        IExternalCurrency currency =
                mock(
                        IExternalCurrency.class,
                        withSettings().extraInterfaces(IStoredCurrency.class));
        when(currency.getBalance(player)).thenReturn(new BigDecimal("12.34"));

        assertEquals(new BigDecimal("12.34"), EconomyApi.getBalance((ICurrency) currency, player));
    }

    @Test
    void getBalance_unsupportedCurrency_reportsUnsupportedType() {
        assertThrows(
                IllegalArgumentException.class,
                () -> EconomyApi.getBalance(mock(ICurrency.class), mock(Player.class)));
    }

    @Test
    void getAccountAndBalance_nullArguments_failBeforeServiceAccess() {
        assertEquals(
                "gameProfileId",
                assertThrows(NullPointerException.class, () -> EconomyApi.getAccount((UUID) null))
                        .getMessage());
        assertEquals(
                "player",
                assertThrows(NullPointerException.class, () -> EconomyApi.getAccount((Player) null))
                        .getMessage());
        assertEquals(
                "currency",
                assertThrows(
                                NullPointerException.class,
                                () -> EconomyApi.getBalance((ICurrency) null, null))
                        .getMessage());
        assertEquals(
                "player",
                assertThrows(
                                NullPointerException.class,
                                () -> EconomyApi.getBalance(mock(ICurrency.class), null))
                        .getMessage());
    }

    @Test
    void getBalanceOnClient_storedCurrencyWithoutGame_readsSyncedAccount() {
        IStoredCurrency currency = storedCurrency("client_stored", new BigDecimal("19.99"));
        BankAccount account = SDMEconomyServiceClient.getInstanceClient().getBankAccount();
        boolean wasDirty = account.isDirty();

        assertEquals(new BigDecimal("19.99"), EconomyApi.getBalanceOnClient(currency));
        BigDecimal amount = new BigDecimal("1234567890.000000000000000001");
        try {
            account.setBalance(currency, amount);
            assertEquals(amount, EconomyApi.getBalanceOnClient(currency));
        } finally {
            account.removeBalance(currency);
            if (!wasDirty) {
                account.markClean();
            }
        }
    }

    @Test
    void getBalanceOnClient_missingCurrencyOrPlayer_returnsZero() {
        assertEquals(BigDecimal.ZERO, EconomyApi.getBalanceOnClient(null));
        assertEquals(BigDecimal.ZERO, EconomyApi.getBalanceOnClient(mock(ICurrency.class)));
        assertEquals(BigDecimal.ZERO, EconomyApi.getBalanceOnClient(mock(IExternalCurrency.class)));
    }

    @Test
    void getStoredCurrencies_remoteServer_mergesLocalAndSyncedCurrenciesIntoSnapshot() {
        IStoredCurrency local = storedCurrency("local", BigDecimal.ZERO);
        IStoredCurrency localOverridden = storedCurrency("shared", BigDecimal.ONE);
        IStoredCurrency synced = storedCurrency("shared", BigDecimal.TEN);
        Map<ResourceLocation, IStoredCurrency> registered =
                Map.of(local.getId(), local, localOverridden.getId(), localOverridden);
        SDMEconomyServiceClient.STORED_CURRENCIES.put(synced.getId(), synced);

        try (MockedStatic<SDMEconomyCurrencyRegistry> registry =
                mockStatic(SDMEconomyCurrencyRegistry.class)) {
            registry.when(SDMEconomyCurrencyRegistry::getStoredCurrenciesMap)
                    .thenAnswer(ignored -> new HashMap<>(registered));
            Map<ResourceLocation, IStoredCurrency> snapshot = EconomyApi.getStoredCurrencies(true);

            assertEquals(Map.of(local.getId(), local, synced.getId(), synced), snapshot);
            assertEquals(registered, EconomyApi.getStoredCurrencies(false));
            snapshot.clear();
            assertEquals(2, EconomyApi.getStoredCurrencies(true).size());
            assertSame(synced, SDMEconomyServiceClient.STORED_CURRENCIES.get(synced.getId()));
        }
    }

    @Test
    void getStoredCurrencies_integratedServer_ignoresStaleSyncedCurrencies() {
        IStoredCurrency local = storedCurrency("shared", BigDecimal.ONE);
        IStoredCurrency stale = storedCurrency("shared", BigDecimal.TEN);
        IStoredCurrency remoteOnly = storedCurrency("remote_only", BigDecimal.ZERO);
        SDMEconomyServiceClient.STORED_CURRENCIES.put(stale.getId(), stale);
        SDMEconomyServiceClient.STORED_CURRENCIES.put(remoteOnly.getId(), remoteOnly);
        SDMEconomyPlatform.server = mock(MinecraftServer.class);

        try (MockedStatic<SDMEconomyCurrencyRegistry> registry =
                mockStatic(SDMEconomyCurrencyRegistry.class)) {
            registry.when(SDMEconomyCurrencyRegistry::getStoredCurrenciesMap)
                    .thenAnswer(ignored -> new HashMap<>(Map.of(local.getId(), local)));

            assertEquals(Map.of(local.getId(), local), EconomyApi.getStoredCurrencies(true));
        }
    }

    @Test
    void getAllCurrencies_clientAndServer_keepTheirOwnCurrencySets() {
        IStoredCurrency local = storedCurrency("local", BigDecimal.ZERO);
        IStoredCurrency remote = storedCurrency("remote", BigDecimal.ONE);
        IExternalCurrency external = mock(IExternalCurrency.class);
        ResourceLocation externalId = new ResourceLocation("test", "external");
        SDMEconomyServiceClient.STORED_CURRENCIES.put(remote.getId(), remote);
        SDMEconomyServiceClient.CURRENCIES.put(externalId, external);

        try (MockedStatic<SDMEconomyCurrencyRegistry> registry =
                mockStatic(SDMEconomyCurrencyRegistry.class)) {
            registry.when(SDMEconomyCurrencyRegistry::getAllCurrenciesMap)
                    .thenAnswer(ignored -> new HashMap<>(Map.of(local.getId(), local)));

            assertEquals(Map.of(local.getId(), local), EconomyApi.getAllCurrencies());
            assertEquals(Map.of(local.getId(), local), EconomyApi.getAllCurrencies(false));
            Map<ResourceLocation, ICurrency> snapshot = EconomyApi.getAllCurrencies(true);
            assertEquals(
                    Map.of(local.getId(), local, remote.getId(), remote, externalId, external),
                    snapshot);
            snapshot.clear();
            assertEquals(3, EconomyApi.getAllCurrencies(true).size());
        }
    }

    @ParameterizedTest
    @ValueSource(
            strings = {"Bad ID", "test:Uppercase", "bad namespace:coin", "test:bad path", "a:b:c"})
    void getCurrency_malformedId_returnsNull(String id) {
        assertNull(EconomyApi.getCurrency(id));
    }

    @ParameterizedTest
    @ValueSource(strings = {"coin", "sdm:coin", "addon:token"})
    void getCurrency_validId_usesSdmDefaultNamespace(String id) {
        ResourceLocation expected =
                id.equals("coin") ? new ResourceLocation("sdm", "coin") : new ResourceLocation(id);
        IExternalCurrency currency = mock(IExternalCurrency.class);
        try (MockedStatic<SDMEconomyCurrencyRegistry> registry =
                mockStatic(SDMEconomyCurrencyRegistry.class, CALLS_REAL_METHODS)) {
            registry.when(() -> SDMEconomyCurrencyRegistry.getCurrency(expected))
                    .thenReturn(currency);

            assertSame(currency, EconomyApi.getCurrency(id));
        }
    }

    private static IStoredCurrency storedCurrency(String path, BigDecimal defaultBalance) {
        IStoredCurrency currency = mock(IStoredCurrency.class);
        when(currency.getId()).thenReturn(new ResourceLocation("economy_api_test", path));
        when(currency.getDefaultBalance()).thenReturn(defaultBalance);
        return currency;
    }
}
