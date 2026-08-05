package dev.sixik.sdmshop2.libs.shop.config;

import ca.spottedleaf.yamlconfig.adapter.TypeAdapterRegistry;
import ca.spottedleaf.yamlconfig.config.YamlConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;

public final class ShopConfigHolder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShopConfigHolder.class);
    private static final File CONFIG_FILE = Paths.get(".")
            .resolve("config").resolve("sdm").resolve("shop").resolve("config.yaml").toFile();
    private static final TypeAdapterRegistry CONFIG_ADAPTER = new TypeAdapterRegistry();
    private static final YamlConfig<ShopConfig> CONFIG;

    private static boolean initialized;

    static {
        try {
            CONFIG = new YamlConfig<>(ShopConfig.class, new ShopConfig(), CONFIG_ADAPTER);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ShopConfigHolder() {
    }

    public static YamlConfig<ShopConfig> getConfigRaw() {
        if (!initialized && reloadConfig()) {
            initialized = true;
        }

        return CONFIG;
    }

    public static ShopConfig getConfig() {
        return getConfigRaw().config;
    }

    public static boolean reloadConfig() {
        synchronized (CONFIG) {
            if (CONFIG_FILE.exists()) {
                try {
                    CONFIG.load(CONFIG_FILE);
                } catch (Exception e) {
                    LOGGER.error("Failed to load SDM Shop config, using defaults", e);
                    return false;
                }
            }

            CONFIG.callInitialisers();
            migrateConfig(CONFIG.config);
            return saveConfig();
        }
    }

    private static void migrateConfig(ShopConfig config) {
        if (config == null) {
            return;
        }
    }

    public static boolean saveConfig() {
        synchronized (CONFIG) {
            try {
                CONFIG_FILE.getParentFile().mkdirs();
                CONFIG.save(CONFIG_FILE);
                return true;
            } catch (Exception e) {
                LOGGER.error("Failed to save SDM Shop config", e);
                return false;
            }
        }
    }

    public static File getConfigFile() {
        return CONFIG_FILE;
    }
}
