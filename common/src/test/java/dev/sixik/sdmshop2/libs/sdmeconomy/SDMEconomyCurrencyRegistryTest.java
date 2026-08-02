package dev.sixik.sdmshop2.libs.sdmeconomy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.sixik.sdmshop2.libs.platform.utils.repositoryManager.JsonRepositoryManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SDMEconomyCurrencyRegistryTest {

    private static final ResourceLocation TEST_TYPE_ID = new ResourceLocation("sdm", "test_currency");
    private static final ICurrencyType<TestCurrency> TEST_TYPE = new TestCurrencyType();

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        SDMEconomyCurrencyRegistry.shutdownRepository();
        SDMEconomyPlatform.loadConfigDir(tempDir);
        SDMEconomyCurrencyRegistry.registerType(TEST_TYPE_ID, TEST_TYPE);
        SDMEconomyCurrencyRegistry.reload(currenciesDir());
    }

    @Test
    void registerAndSaveCurrencyWritesNamespacedJsonAndReloadsIt() throws Exception {
        ResourceLocation id = new ResourceLocation("test", "emerald_coin");
        TestCurrency currency = new TestCurrency(id, "emerald");

        assertTrue(SDMEconomyCurrencyRegistry.registerAndSaveCurrency(currency));

        Path file = currenciesDir().resolve("test").resolve("emerald_coin.json");
        assertTrue(Files.isRegularFile(file));

        JsonObject json = readJson(file);
        assertEquals("test:emerald_coin", json.get("id").getAsString());
        assertEquals("sdm:test_currency", json.get("type").getAsString());
        assertEquals("emerald", json.get("code").getAsString());

        SDMEconomyCurrencyRegistry.reload(currenciesDir());

        IExternalCurrency loaded = SDMEconomyCurrencyRegistry.getCurrency(id);
        assertNotNull(loaded);
        assertEquals(id, loaded.getId());
        assertInstanceOf(TestCurrency.class, loaded);
        assertEquals("emerald", ((TestCurrency) loaded).code());
    }

    @Test
    void reloadReadsLegacyFilesFromRootAndNamespacedFolders() throws Exception {
        writeLegacyCurrency(currenciesDir().resolve("emerald.json"), "emerald");
        writeLegacyCurrency(currenciesDir().resolve("custom").resolve("deep").resolve("diamond.json"), "diamond");

        SDMEconomyCurrencyRegistry.reload(currenciesDir());

        assertNotNull(SDMEconomyCurrencyRegistry.getCurrency(new ResourceLocation("sdm", "emerald")));
        assertNotNull(SDMEconomyCurrencyRegistry.getCurrency(new ResourceLocation("custom", "deep/diamond")));
    }

    @Test
    void repositoryBackedRegistrySavesAndMigratesLegacyJson() throws Exception {
        writeLegacyCurrency(currenciesDir().resolve("legacy_coin.json"), "legacy");

        JsonRepositoryManager manager = new JsonRepositoryManager(tempDir.resolve("repository"));
        try {
            SDMEconomyCurrencyRegistry.initRepository(manager);

            ResourceLocation legacyId = new ResourceLocation("sdm", "legacy_coin");
            assertNotNull(SDMEconomyCurrencyRegistry.getCurrency(legacyId));
            assertTrue(Files.isRegularFile(currenciesDir().resolve("sdm").resolve("legacy_coin.json")));

            ResourceLocation savedId = new ResourceLocation("test", "repo_coin");
            TestCurrency savedCurrency = new TestCurrency(savedId, "repo");
            assertTrue(SDMEconomyCurrencyRegistry.registerAndSaveCurrency(savedCurrency));

            SDMEconomyCurrencyRegistry.reload();

            assertNotNull(SDMEconomyCurrencyRegistry.getCurrency(legacyId));
            assertNotNull(SDMEconomyCurrencyRegistry.getCurrency(savedId));
            assertTrue(Files.isRegularFile(currenciesDir().resolve("test").resolve("repo_coin.json")));
        } finally {
            SDMEconomyCurrencyRegistry.shutdownRepository();
            manager.close();
        }
    }

    @Test
    void networkSerializationUsesStableTypeIdAndReadsLegacyOwnerFormat() {
        ResourceLocation id = new ResourceLocation("test", "network_coin");
        TestCurrency currency = new TestCurrency(id, "network");
        assertTrue(SDMEconomyCurrencyRegistry.registerAndSaveCurrency(currency));

        CompoundTag serialized = SDMEconomyCurrencyRegistry.serializeCurrencies();
        ListTag currencies = serialized.getList("currencies", 10);
        assertEquals(1, currencies.size());

        CompoundTag entry = currencies.getCompound(0);
        assertEquals("test:network_coin", entry.getString("key"));
        assertEquals("sdm:test_currency", entry.getString("type"));
        assertFalse(entry.contains("owner"));

        Object2ObjectOpenHashMap<ResourceLocation, IExternalCurrency> read =
                SDMEconomyCurrencyRegistry.deserializeCurrencies(serialized);
        assertNotNull(read.get(id));

        ResourceLocation legacyId = new ResourceLocation("test", "legacy_network_coin");
        CompoundTag legacyRoot = new CompoundTag();
        ListTag legacyCurrencies = new ListTag();
        CompoundTag legacyEntry = new CompoundTag();
        legacyEntry.putString("key", legacyId.toString());
        legacyEntry.putString("owner", TestCurrency.class.getName());
        legacyEntry.put("data", TEST_TYPE.serializeNbt(new TestCurrency(legacyId, "legacy-network")));
        legacyCurrencies.add(legacyEntry);
        legacyRoot.put("currencies", legacyCurrencies);

        Object2ObjectOpenHashMap<ResourceLocation, IExternalCurrency> legacyRead =
                SDMEconomyCurrencyRegistry.deserializeCurrencies(legacyRoot);
        assertNotNull(legacyRead.get(legacyId));
    }

    private Path currenciesDir() {
        return tempDir.resolve("sdm").resolve("economy").resolve("currencies");
    }

    private static JsonObject readJson(Path file) throws Exception {
        try (Reader reader = Files.newBufferedReader(file)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    private static void writeLegacyCurrency(Path file, String code) throws Exception {
        if (file.getParent() != null) {
            Files.createDirectories(file.getParent());
        }

        Files.writeString(file, """
                {
                  "type": "sdm:test_currency",
                  "code": "%s"
                }
                """.formatted(code));
    }

    private record TestCurrency(ResourceLocation id, String code) implements IExternalCurrency {

        @Override
        public boolean withdraw(ServerPlayer player, BigDecimal amount, boolean simulate) {
            return amount != null && amount.signum() > 0;
        }

        @Override
        public boolean deposit(ServerPlayer player, BigDecimal amount, boolean simulate) {
            return amount != null && amount.signum() > 0;
        }

        @Override
        public BigDecimal getBalance(Player player) {
            return BigDecimal.ZERO;
        }

        @Override
        public ICurrencyType<? extends IExternalCurrency> getType() {
            return TEST_TYPE;
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public Component getDisplayName() {
            return Component.literal(code);
        }

        @Override
        public String format(BigDecimal decimal) {
            return decimal.toPlainString();
        }

        @Override
        public int getColor() {
            return 0;
        }
    }

    private static final class TestCurrencyType implements ICurrencyType<TestCurrency> {

        @Override
        public Class<TestCurrency> getOwnerClass() {
            return TestCurrency.class;
        }

        @Override
        public TestCurrency deserialize(ResourceLocation id, JsonObject json) {
            return new TestCurrency(id, json.get("code").getAsString());
        }

        @Override
        public JsonObject serialize(TestCurrency currency) {
            JsonObject json = new JsonObject();
            serializeType(json, TEST_TYPE_ID.toString());
            json.addProperty("code", currency.code());
            return json;
        }
    }
}
