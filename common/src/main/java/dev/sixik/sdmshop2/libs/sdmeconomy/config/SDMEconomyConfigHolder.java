package dev.sixik.sdmshop2.libs.sdmeconomy.config;

import ca.spottedleaf.yamlconfig.adapter.TypeAdapterRegistry;
import ca.spottedleaf.yamlconfig.config.YamlConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;

public final class SDMEconomyConfigHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(SDMEconomyConfigHolder.class);
    private static final File CONFIG_FILE = Paths.get(".")
            .resolve("config").resolve("sdm").resolve("economy").resolve("config.yaml").toFile();
    private static final TypeAdapterRegistry CONFIG_ADAPTER = new TypeAdapterRegistry();
    private static final YamlConfig<SDMEconomyDataStorageConfig> CONFIG;

    private static boolean initialized;

    static {
        try {
            CONFIG = new YamlConfig<>(SDMEconomyDataStorageConfig.class, new SDMEconomyDataStorageConfig(), CONFIG_ADAPTER);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private SDMEconomyConfigHolder() {
    }

    public static YamlConfig<SDMEconomyDataStorageConfig> getConfigRaw() {
        if (!initialized && reloadConfig()) {
            initialized = true;
        }

        return CONFIG;
    }

    public static SDMEconomyDataStorageConfig getConfig() {
        return getConfigRaw().config;
    }

    public static boolean reloadConfig() {
        synchronized (CONFIG) {
            if (CONFIG_FILE.exists()) {
                try {
                    CONFIG.load(CONFIG_FILE);
                } catch (Exception e) {
                    LOGGER.error("Failed to load SDM Economy config, using defaults", e);
                    return false;
                }
            }

            CONFIG.callInitialisers();
            migrateConfig(CONFIG.config);
            return saveConfig();
        }
    }

    private static void migrateConfig(SDMEconomyDataStorageConfig config) {
        if (config == null) {
            return;
        }
        if (config.mongodb == null) {
            config.mongodb = new SDMEconomyDataStorageConfig.MongoConfig();
        }
    }

    public static boolean saveConfig() {
        synchronized (CONFIG) {
            try {
                CONFIG_FILE.getParentFile().mkdirs();
                CONFIG.save(CONFIG_FILE);
                return true;
            } catch (Exception e) {
                LOGGER.error("Failed to save SDM Economy config", e);
                return false;
            }
        }
    }

    public static File getConfigFile() {
        return CONFIG_FILE;
    }
}
