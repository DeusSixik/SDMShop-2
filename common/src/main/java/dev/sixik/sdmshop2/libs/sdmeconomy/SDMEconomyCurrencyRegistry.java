package dev.sixik.sdmshop2.libs.sdmeconomy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.sixik.sdmshop2.libs.platform.utils.repository.Repository;
import dev.sixik.sdmshop2.libs.platform.utils.repositoryManager.RepoDefinition;
import dev.sixik.sdmshop2.libs.platform.utils.repositoryManager.RepositoryManager;
import dev.sixik.sdmshop2.libs.sdmeconomy.custom_currency.SimpleTextCurrency;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class SDMEconomyCurrencyRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(SDMEconomyCurrencyRegistry.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final String ID_FIELD = "id";
    private static final String TYPE_FIELD = "type";
    private static final String LEGACY_OWNER_FIELD = "owner";
    private static final ResourceLocation TEXT_TYPE_ID = ResourceLocation.tryBuild("sdm", "text");
    private static final String DISPLAY_NAME_FIELD = "display_name";
    private static final String ICON_TEXT_FIELD = "icon_text";

    private static final Map<ResourceLocation, ICurrencyType<?>> TYPES = new ConcurrentHashMap<>();
    private static final Map<Class<?>, ICurrencyType<?>> TYPES_BY_CLASS = new ConcurrentHashMap<>();
    private static final Map<Class<?>, ResourceLocation> TYPE_IDS_BY_CLASS = new ConcurrentHashMap<>();

    private static final Map<ResourceLocation, IExternalCurrency> CURRENCIES = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, IStoredCurrency> STORED_CURRENCIES = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, IStoredCurrency> CUSTOM_STORED_CURRENCIES = new ConcurrentHashMap<>();

    @Nullable
    private static volatile Repository<ResourceLocation, IExternalCurrency> repository;

    public static void registerType(ResourceLocation id, ICurrencyType<?> type) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(type, "type");
        TYPES.put(id, type);
        TYPES_BY_CLASS.put(type.getOwnerClass(), type);
        TYPE_IDS_BY_CLASS.put(type.getOwnerClass(), id);
    }

    public static <T extends IStoredCurrency> T registerStoredCurrency(T currency) {
        Objects.requireNonNull(currency, "currency");
        STORED_CURRENCIES.put(currency.getId(), currency);
        return currency;
    }

    public static void initRepository(RepositoryManager manager) {
        Objects.requireNonNull(manager, "manager");
        manager.init();

        repository = manager.createRepository(
                SDMEconomyPlatform.getCurrenciesDir(),
                currenciesCollectionName(),
                new RepoDefinition<>(
                        SDMEconomyCurrencyRegistry::currencyStorageKey,
                        SDMEconomyCurrencyRegistry::currencyStorageId,
                        IExternalCurrency::getId,
                        SDMEconomyCurrencyRegistry::serializeStoredCurrency,
                        SDMEconomyCurrencyRegistry::deserializeStoredCurrency
                )
        );

        repository.setSyncCallbacks(
                SDMEconomyCurrencyRegistry::reloadRemoteCurrency,
                SDMEconomyCurrencyRegistry::deleteRemoteCurrency
        );

        reload();
    }

    public static void shutdownRepository() {
        repository = null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends IExternalCurrency> boolean registerAndSaveCurrency(T currency) {
        Objects.requireNonNull(currency, "currency");
        ResourceLocation id = currency.getId();

        ICurrencyType<T> type = (ICurrencyType<T>) TYPES_BY_CLASS.get(currency.getClass());
        if (type == null) {
            LOGGER.error("Cannot save currency {}: No ICurrencyType registered for class {}", id, currency.getClass().getName());
            return false;
        }

        try {
            Repository<ResourceLocation, IExternalCurrency> repo = repository;
            if (repo != null) {
                repo.save(id, currency);
            } else {
                saveCurrencyToFile(currency, type);
            }

            CURRENCIES.put(id, currency);
            SDMEconomyPlatform.broadcastCurrencies();
            LOGGER.info("Registered new currency with id: '{}'", id);
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to save currency {}", id, e);
            return false;
        }
    }

    public static boolean registerAndSaveStoredCurrency(IStoredCurrency currency) {
        Objects.requireNonNull(currency, "currency");
        ResourceLocation id = currency.getId();
        SimpleTextCurrency textCurrency = toSimpleTextCurrency(currency);

        try {
            saveStoredCurrencyToFile(textCurrency);
            CUSTOM_STORED_CURRENCIES.put(id, textCurrency);
            SDMEconomyPlatform.broadcastCurrencies();
            LOGGER.info("Registered stored currency with id: '{}'", id);
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to save stored currency {}", id, e);
            return false;
        }
    }

    public static boolean deleteCurrency(ResourceLocation id) {
        if (id == null || ResourceLocation.tryBuild("sdm", "coin").equals(id)) {
            return false;
        }

        try {
            Repository<ResourceLocation, IExternalCurrency> repo = repository;
            if (repo != null) {
                repo.delete(id);
            }

            deleteCurrencyFile(id);
            boolean removed = CURRENCIES.remove(id) != null;
            removed |= CUSTOM_STORED_CURRENCIES.remove(id) != null;
            SDMEconomyPlatform.broadcastCurrencies();
            LOGGER.info("Deleted currency with id: '{}'", id);
            return removed;
        } catch (Exception e) {
            LOGGER.error("Failed to delete currency {}", id, e);
            return false;
        }
    }

    @Nullable
    public static IExternalCurrency getCurrency(String id) {
        return getCurrency(
                id.contains(":")
                        ? ResourceLocation.tryParse(id)
                        : ResourceLocation.tryBuild("sdm", id)
        );
    }

    @Nullable
    public static IExternalCurrency getCurrency(ResourceLocation id) {
        return CURRENCIES.get(id);
    }

    @Nullable
    public static IStoredCurrency getStoredCurrency(ResourceLocation id) {
        IStoredCurrency custom = CUSTOM_STORED_CURRENCIES.get(id);
        return custom == null ? STORED_CURRENCIES.get(id) : custom;
    }

    @Nullable
    public static ICurrency getAnyCurrency(ResourceLocation id) {
        IExternalCurrency external = getCurrency(id);
        if (external != null) {
            return external;
        }

        return getStoredCurrency(id);
    }

    public static Map<ResourceLocation, IExternalCurrency> getCurrenciesMap() {
        return new Object2ObjectOpenHashMap<>(CURRENCIES);
    }

    public static Map<ResourceLocation, IStoredCurrency> getStoredCurrenciesMap() {
        Object2ObjectOpenHashMap<ResourceLocation, IStoredCurrency> currencies = new Object2ObjectOpenHashMap<>(STORED_CURRENCIES);
        currencies.putAll(CUSTOM_STORED_CURRENCIES);
        return currencies;
    }

    public static Map<ResourceLocation, ICurrency> getAllCurrenciesMap() {
        Object2ObjectOpenHashMap<ResourceLocation, ICurrency> currencies = new Object2ObjectOpenHashMap<>();
        currencies.putAll(STORED_CURRENCIES);
        currencies.putAll(CUSTOM_STORED_CURRENCIES);
        currencies.putAll(CURRENCIES);
        return currencies;
    }

    public static Collection<IExternalCurrency> getCurrencies() {
        return new ArrayList<>(CURRENCIES.values());
    }

    public static void forEachCurrencies(BiConsumer<ResourceLocation, IExternalCurrency> iterator) {
        getCurrenciesMap().forEach(iterator);
    }

    public static void reload() {
        Repository<ResourceLocation, IExternalCurrency> repo = repository;
        CURRENCIES.clear();
        CUSTOM_STORED_CURRENCIES.clear();

        if (repo != null) {
            CURRENCIES.putAll(repo.loadAll());
            migrateLegacyCurrencyFiles(repo);
        } else {
            CURRENCIES.putAll(loadCurrencyFiles(SDMEconomyPlatform.getCurrenciesDir(), false));
        }
    }

    public static void reload(Path configDir) {
        CURRENCIES.clear();
        CUSTOM_STORED_CURRENCIES.clear();
        CURRENCIES.putAll(loadCurrencyFiles(configDir, false));
    }

    private static void reloadRemoteCurrency(ResourceLocation id) {
        Repository<ResourceLocation, IExternalCurrency> repo = repository;
        if (repo == null) return;

        IExternalCurrency currency = repo.load(id);
        if (currency == null) {
            CURRENCIES.remove(id);
        } else {
            CURRENCIES.put(id, currency);
        }

        SDMEconomyPlatform.broadcastCurrencies();
    }

    private static void deleteRemoteCurrency(ResourceLocation id) {
        CURRENCIES.remove(id);
        SDMEconomyPlatform.broadcastCurrencies();
    }

    private static void migrateLegacyCurrencyFiles(Repository<ResourceLocation, IExternalCurrency> repo) {
        Map<ResourceLocation, IExternalCurrency> legacy = loadCurrencyFiles(SDMEconomyPlatform.getCurrenciesDir(), true);
        legacy.forEach((id, currency) -> {
            if (CURRENCIES.putIfAbsent(id, currency) == null) {
                repo.save(id, currency);
            }
        });
    }

    private static String currenciesCollectionName() {
        if (SDMEconomyPlatform.getDataStorageConfig() == null) {
            return "currencies";
        }

        return SDMEconomyPlatform.getDataStorageConfig().getCurrentConfig().mongodb.currenciesCollection;
    }

    private static String currencyStorageKey(ResourceLocation id) {
        if ("sdm".equals(id.getNamespace())) {
            return id.getPath();
        }

        return id.toString();
    }

    private static ResourceLocation currencyStorageId(String id) {
        if (id.contains(":")) {
            return ResourceLocation.tryParse(id);
        }

        return ResourceLocation.tryBuild("sdm", id);
    }

    private static Map<ResourceLocation, IExternalCurrency> loadCurrencyFiles(Path configDir, boolean legacyOnly) {
        Map<ResourceLocation, IExternalCurrency> out = new Object2ObjectOpenHashMap<>();

        if(!Files.exists(configDir)) {
            LOGGER.error("Can't reload currencies because, currencies folder not exists!");
            return out;
        }

        try (Stream<Path> files = Files.walk(configDir)) {
            files
                    .filter(Files::isRegularFile)
                    .filter(path -> FilenameUtils.getExtension(path.getFileName().toString()).equals("json"))
                    .forEach(path -> loadCurrencyFile(configDir, path.toFile(), legacyOnly, out));
        } catch (IOException e) {
            LOGGER.error("Can't reload currencies from {}", configDir, e);
        }

        return out;
    }

    private static void loadCurrencyFile(Path root, File file, boolean legacyOnly, Map<ResourceLocation, IExternalCurrency> out) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            final JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            if (legacyOnly && json.has(ID_FIELD)) {
                return;
            }

            if (isTextCurrencyJson(json)) {
                IStoredCurrency currency = deserializeTextCurrency(root, file.toPath(), json);
                if (currency != null) {
                    CUSTOM_STORED_CURRENCIES.put(currency.getId(), currency);
                    LOGGER.info("Loaded stored currency: {}", currency.getId());
                }
                return;
            }

            IExternalCurrency currency = deserializeCurrency(root, file.toPath(), json);
            if (currency != null) {
                out.put(currency.getId(), currency);
                LOGGER.info("Loaded currency: {}", currency.getId());
            }
        } catch (Exception e) {
            LOGGER.error("Error loading currency from file '{}'", file.getName(), e);
        }
    }

    public static CompoundTag serializeCurrencies() {
        CompoundTag nbt = new CompoundTag();

        ListTag nbtCurrencies = new ListTag();
        for (Map.Entry<ResourceLocation, IExternalCurrency> entry : CURRENCIES.entrySet()) {
            ResourceLocation key = entry.getKey();
            IExternalCurrency value = entry.getValue();
            final ICurrencyType<?> type = TYPES_BY_CLASS.get(value.getClass());
            if (type == null) continue;
            final ResourceLocation typeId = TYPE_IDS_BY_CLASS.get(type.getOwnerClass());
            if (typeId == null) continue;

            CompoundTag data = new CompoundTag();
            data.putString("key", key.toString());
            data.putString(TYPE_FIELD, typeId.toString());
            data.put("data", serializeNbtUnchecked(type, value));
            nbtCurrencies.add(data);
        }

        for (Map.Entry<ResourceLocation, IStoredCurrency> entry : CUSTOM_STORED_CURRENCIES.entrySet()) {
            CompoundTag data = new CompoundTag();
            data.putString("key", entry.getKey().toString());
            data.putString(TYPE_FIELD, TEXT_TYPE_ID.toString());
            data.put("data", serializeTextCurrency(entry.getValue()));
            nbtCurrencies.add(data);
        }

        nbt.put("currencies", nbtCurrencies);
        return nbt;
    }

    public static Object2ObjectOpenHashMap<ResourceLocation, IExternalCurrency> deserializeCurrencies(CompoundTag nbt) {
        if(!nbt.contains("currencies")) return new Object2ObjectOpenHashMap<>();

        Object2ObjectOpenHashMap<ResourceLocation, IExternalCurrency> out = new Object2ObjectOpenHashMap<>();

        ListTag nbtCurrencies = (ListTag) nbt.get("currencies");
        for (Tag nbtCurrency : nbtCurrencies) {
            CompoundTag data = (CompoundTag) nbtCurrency;

            ResourceLocation id = ResourceLocation.tryParse(data.getString("key"));
            if (id == null) {
                LOGGER.error("Invalid currency id in network data: {}", data.getString("key"));
                continue;
            }

            if (isTextCurrencyType(data)) {
                continue;
            }

            ICurrencyType<?> type = readNetworkCurrencyType(data);
            if(type == null) {
                continue;
            }

            IExternalCurrency obj = deserializeNbtUnchecked(type, id, data.get("data"));
            out.put(id, obj);
        }

        return out;
    }

    public static Object2ObjectOpenHashMap<ResourceLocation, IStoredCurrency> deserializeStoredCurrencies(CompoundTag nbt) {
        if(!nbt.contains("currencies")) return new Object2ObjectOpenHashMap<>();

        Object2ObjectOpenHashMap<ResourceLocation, IStoredCurrency> out = new Object2ObjectOpenHashMap<>();

        ListTag nbtCurrencies = (ListTag) nbt.get("currencies");
        for (Tag nbtCurrency : nbtCurrencies) {
            CompoundTag data = (CompoundTag) nbtCurrency;
            if (!isTextCurrencyType(data)) {
                continue;
            }

            ResourceLocation id = ResourceLocation.tryParse(data.getString("key"));
            if (id == null) {
                LOGGER.error("Invalid stored currency id in network data: {}", data.getString("key"));
                continue;
            }

            IStoredCurrency currency = deserializeTextCurrency(id, data.getCompound("data"));
            if (currency != null) {
                out.put(id, currency);
            }
        }

        return out;
    }

    @SuppressWarnings("unchecked")
    private static <T extends IExternalCurrency> JsonObject serializeStoredCurrency(T currency) {
        ICurrencyType<T> type = (ICurrencyType<T>) TYPES_BY_CLASS.get(currency.getClass());
        if (type == null) {
            throw new IllegalStateException("No ICurrencyType registered for class " + currency.getClass().getName());
        }

        JsonObject json = type.serialize(currency);
        json.addProperty(ID_FIELD, currency.getId().toString());
        return json;
    }

    @Nullable
    private static IExternalCurrency deserializeStoredCurrency(JsonObject json) {
        if (isTextCurrencyJson(json)) {
            IStoredCurrency currency = deserializeTextCurrency(null, null, json);
            if (currency != null) {
                CUSTOM_STORED_CURRENCIES.put(currency.getId(), currency);
            }
            return null;
        }
        return deserializeCurrency(null, null, json);
    }

    @Nullable
    private static IExternalCurrency deserializeCurrency(@Nullable Path root, @Nullable Path file, JsonObject json) {
        ResourceLocation currencyId = readCurrencyId(root, file, json);
        if (currencyId == null) {
            LOGGER.error("Failed to load currency: invalid or missing currency id");
            return null;
        }

        if (!json.has(TYPE_FIELD)) {
            LOGGER.error("Failed to load currency '{}': missing '{}' field", currencyId, TYPE_FIELD);
            return null;
        }

        ResourceLocation typeId = ResourceLocation.tryParse(json.get(TYPE_FIELD).getAsString());
        ICurrencyType<?> typeFactory = TYPES.get(typeId);
        if (typeFactory == null) {
            LOGGER.error("Unknown currency type '{}' for currency '{}'", typeId, currencyId);
            return null;
        }

        return typeFactory.deserialize(currencyId, json);
    }

    private static Path currencyFile(Path root, ResourceLocation id) {
        if ("sdm".equals(id.getNamespace())) {
            return root.resolve(id.getPath() + ".json");
        }

        return root.resolve(id.getNamespace()).resolve(id.getPath() + ".json");
    }

    @SuppressWarnings("unchecked")
    private static <T extends IExternalCurrency> void saveCurrencyToFile(T currency, ICurrencyType<T> type) throws IOException {
        ResourceLocation id = currency.getId();
        JsonObject json = type.serialize(currency);
        json.addProperty(ID_FIELD, id.toString());

        File file = currencyFile(SDMEconomyPlatform.getCurrenciesDir(), id).toFile();
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Failed to create currency directory: " + parent);
        }

        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(json, writer);
        }
    }

    private static void saveStoredCurrencyToFile(SimpleTextCurrency currency) throws IOException {
        ResourceLocation id = currency.getId();
        JsonObject json = serializeTextCurrencyJson(currency);
        json.addProperty(ID_FIELD, id.toString());

        File file = currencyFile(SDMEconomyPlatform.getCurrenciesDir(), id).toFile();
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Failed to create currency directory: " + parent);
        }

        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(json, writer);
        }
    }

    private static void deleteCurrencyFile(ResourceLocation id) throws IOException {
        Files.deleteIfExists(currencyFile(SDMEconomyPlatform.getCurrenciesDir(), id));
    }

    @Nullable
    private static ResourceLocation readCurrencyId(@Nullable Path root, @Nullable Path file, JsonObject json) {
        if (json.has(ID_FIELD)) {
            return ResourceLocation.tryParse(json.get(ID_FIELD).getAsString());
        }

        if (root == null || file == null) {
            return null;
        }

        Path relative = root.relativize(file);
        int nameCount = relative.getNameCount();
        if (nameCount <= 1) {
            return ResourceLocation.tryBuild("sdm", FilenameUtils.removeExtension(file.getFileName().toString()));
        }

        String namespace = relative.getName(0).toString();
        StringBuilder path = new StringBuilder();
        for (int i = 1; i < nameCount; i++) {
            if (path.length() > 0) {
                path.append('/');
            }
            path.append(relative.getName(i));
        }

        return ResourceLocation.tryBuild(namespace, FilenameUtils.removeExtension(path.toString()).replace('\\', '/'));
    }

    private static boolean isTextCurrencyJson(JsonObject json) {
        if (json == null || !json.has(TYPE_FIELD)) {
            return false;
        }

        ResourceLocation typeId = ResourceLocation.tryParse(json.get(TYPE_FIELD).getAsString());
        return TEXT_TYPE_ID.equals(typeId);
    }

    private static boolean isTextCurrencyType(CompoundTag data) {
        if (data == null || !data.contains(TYPE_FIELD)) {
            return false;
        }

        return TEXT_TYPE_ID.equals(ResourceLocation.tryParse(data.getString(TYPE_FIELD)));
    }

    @Nullable
    private static IStoredCurrency deserializeTextCurrency(@Nullable Path root, @Nullable Path file, JsonObject json) {
        ResourceLocation id = readCurrencyId(root, file, json);
        if (id == null) {
            LOGGER.error("Failed to load stored currency: invalid or missing currency id");
            return null;
        }

        String displayName = json.has(DISPLAY_NAME_FIELD) ? json.get(DISPLAY_NAME_FIELD).getAsString() : id.toString();
        String iconText = json.has(ICON_TEXT_FIELD) ? json.get(ICON_TEXT_FIELD).getAsString() : "$";
        return new SimpleTextCurrency(id, displayName, iconText);
    }

    @Nullable
    private static IStoredCurrency deserializeTextCurrency(ResourceLocation id, CompoundTag tag) {
        if (id == null) {
            return null;
        }

        String displayName = tag == null || !tag.contains(DISPLAY_NAME_FIELD) ? id.toString() : tag.getString(DISPLAY_NAME_FIELD);
        String iconText = tag == null || !tag.contains(ICON_TEXT_FIELD) ? "$" : tag.getString(ICON_TEXT_FIELD);
        return new SimpleTextCurrency(id, displayName, iconText);
    }

    private static JsonObject serializeTextCurrencyJson(IStoredCurrency currency) {
        SimpleTextCurrency textCurrency = toSimpleTextCurrency(currency);
        JsonObject json = new JsonObject();
        json.addProperty(TYPE_FIELD, TEXT_TYPE_ID.toString());
        json.addProperty(DISPLAY_NAME_FIELD, textCurrency.getDisplayNameValue());
        json.addProperty(ICON_TEXT_FIELD, textCurrency.getIconText());
        return json;
    }

    private static CompoundTag serializeTextCurrency(IStoredCurrency currency) {
        SimpleTextCurrency textCurrency = toSimpleTextCurrency(currency);
        CompoundTag tag = new CompoundTag();
        tag.putString(DISPLAY_NAME_FIELD, textCurrency.getDisplayNameValue());
        tag.putString(ICON_TEXT_FIELD, textCurrency.getIconText());
        return tag;
    }

    private static SimpleTextCurrency toSimpleTextCurrency(IStoredCurrency currency) {
        if (currency instanceof SimpleTextCurrency textCurrency) {
            return textCurrency;
        }

        String displayName = currency.getDisplayName() == null
                ? currency.getId().toString()
                : currency.getDisplayName().getString();
        String iconText = "$";
        if (currency.getIcon() != null
                && currency.getIcon().type() == dev.sixik.sdmshop2.libs.sdmeconomy.icons.IconType.TEXTURE
                && currency.getIcon().icon() instanceof String text) {
            iconText = text;
        }
        return new SimpleTextCurrency(currency.getId(), displayName, iconText);
    }

    @Nullable
    private static ICurrencyType<?> readNetworkCurrencyType(CompoundTag data) {
        if (data.contains(TYPE_FIELD)) {
            ResourceLocation typeId = ResourceLocation.tryParse(data.getString(TYPE_FIELD));
            ICurrencyType<?> type = TYPES.get(typeId);
            if (type == null) {
                LOGGER.error("Unknown currency type: {}", typeId);
            }
            return type;
        }

        if (data.contains(LEGACY_OWNER_FIELD)) {
            try {
                Class<?> clz = Class.forName(data.getString(LEGACY_OWNER_FIELD));
                ICurrencyType<?> type = TYPES_BY_CLASS.get(clz);
                if (type == null) {
                    LOGGER.error("Unknown legacy currency owner type: {}", clz.getName());
                }
                return type;
            } catch (ClassNotFoundException e) {
                LOGGER.error("Unknown legacy currency owner type: {}", data.getString(LEGACY_OWNER_FIELD));
            }
        }

        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Tag serializeNbtUnchecked(ICurrencyType type, IExternalCurrency currency) {
        return type.serializeNbt(currency);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static IExternalCurrency deserializeNbtUnchecked(ICurrencyType type, ResourceLocation id, Tag tag) {
        return (IExternalCurrency) type.deserializeNbt(id, tag);
    }
}
