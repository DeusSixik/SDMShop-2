package dev.sixik.sdmshop2.libs.sdmeconomy.integration.ftbquests;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftbquests.quest.reward.RewardTypes;
import dev.ftb.mods.ftbquests.quest.task.TaskTypes;
import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public final class SDMEconomyFTBQuestsIntegration {

    public static boolean loaded;

    private SDMEconomyFTBQuestsIntegration() {
    }

    public static void init() {
        if (loaded) {
            return;
        }

        loaded = true;
        try {
            SDMEconomyCurrencyTask.TYPE = TaskTypes.register(
                    new ResourceLocation(SDMEconomyPlatform.MODID, "currency"),
                    SDMEconomyCurrencyTask::new,
                    SDMEconomyFTBQuestsIntegration::defaultIcon
            );
            SDMEconomyCurrencyReward.TYPE = RewardTypes.register(
                    new ResourceLocation(SDMEconomyPlatform.MODID, "currency"),
                    SDMEconomyCurrencyReward::new,
                    SDMEconomyFTBQuestsIntegration::defaultIcon
            );
        } catch (NoClassDefFoundError error) {
            loaded = false;
            SDMShop2.LOGGER.error("Failed to register FTB Quests integration for SDM Economy", error);
        }
    }

    public static Icon defaultIcon() {
        return ItemIcon.getItemIcon(Items.GOLD_NUGGET.getDefaultInstance());
    }
}
