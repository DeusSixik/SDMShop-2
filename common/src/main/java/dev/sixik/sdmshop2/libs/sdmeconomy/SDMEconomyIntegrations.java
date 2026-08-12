package dev.sixik.sdmshop2.libs.sdmeconomy;

import dev.architectury.platform.Platform;
import dev.sixik.sdmshop2.SDMShop2;

public final class SDMEconomyIntegrations {

    private SDMEconomyIntegrations() {
    }

    public static void init() {
        if (Platform.isModLoaded("ftbquests")) {
            initFTBQuests();
        }
    }

    private static void initFTBQuests() {
        try {
            Class.forName("dev.sixik.sdmshop2.libs.sdmeconomy.integration.ftbquests.SDMEconomyFTBQuestsIntegration")
                    .getMethod("init")
                    .invoke(null);
        } catch (ReflectiveOperationException | LinkageError exception) {
            SDMShop2.LOGGER.error("Failed to load FTB Quests integration for SDM Economy", exception);
        }
    }
}
