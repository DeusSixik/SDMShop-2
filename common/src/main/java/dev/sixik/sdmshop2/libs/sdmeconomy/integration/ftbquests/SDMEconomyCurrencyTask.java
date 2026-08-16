package dev.sixik.sdmshop2.libs.sdmeconomy.integration.ftbquests;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.ISingleLongValueTask;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import dev.sixik.sdmshop2.libs.sdmeconomy.IExternalCurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;

public class SDMEconomyCurrencyTask extends Task implements ISingleLongValueTask {

    public static TaskType TYPE;

    private ResourceLocation currencyId = SDMEconomyFTBQuestCurrencySupport.defaultCurrencyId();
    private long value = 1L;

    public SDMEconomyCurrencyTask(long id, Quest quest) {
        super(id, quest);
    }

    @Override
    public TaskType getType() {
        return TYPE;
    }

    @Override
    public long getMaxProgress() {
        return value;
    }

    @Override
    public String formatMaxProgress() {
        return SDMEconomyFTBQuestCurrencySupport.formatAmount(currencyId, value);
    }

    @Override
    public String formatProgress(TeamData teamData, long progress) {
        return SDMEconomyFTBQuestCurrencySupport.formatAmount(currencyId, progress);
    }

    @Override
    public void setValue(long value) {
        this.value = Math.max(1L, value);
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString(SDMEconomyFTBQuestCurrencySupport.CURRENCY_ID_KEY, currencyId.toString());
        nbt.putLong(SDMEconomyFTBQuestCurrencySupport.VALUE_KEY, value);
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);

        currencyId = nbt.contains(SDMEconomyFTBQuestCurrencySupport.CURRENCY_ID_KEY)
                ? SDMEconomyFTBQuestCurrencySupport.normalizeCurrencyId(
                ResourceLocation.tryParse(nbt.getString(SDMEconomyFTBQuestCurrencySupport.CURRENCY_ID_KEY))
        )
                : SDMEconomyFTBQuestCurrencySupport.defaultCurrencyId();

        value = Math.max(1L, nbt.getLong(SDMEconomyFTBQuestCurrencySupport.VALUE_KEY));
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeResourceLocation(SDMEconomyFTBQuestCurrencySupport.normalizeCurrencyId(currencyId));
        buffer.writeVarLong(value);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        currencyId = SDMEconomyFTBQuestCurrencySupport.normalizeCurrencyId(buffer.readResourceLocation());
        value = Math.max(1L, buffer.readVarLong());
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        SDMEconomyFTBQuestCurrencyClientSupport.writeCurrencyConfig(config, currencyId, value -> currencyId = value);
        SDMEconomyFTBQuestCurrencyClientSupport.writeValueConfig(config, value, this::setValue);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Component getAltTitle() {
        return Component.empty()
                .append(SDMEconomyFTBQuestCurrencyClientSupport.clientCurrencyDisplayName(currencyId))
                .append(": ")
                .append(SDMEconomyFTBQuestCurrencyClientSupport.formatClientAmount(currencyId, value))
                .withStyle(ChatFormatting.GOLD);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Icon getAltIcon() {
        final IExternalCurrency currency = SDMEconomyServiceClient.getAllExternalCurrencies().get(currencyId);
        if(currency == null)
            return SDMEconomyFTBQuestsIntegration.defaultIcon();

        final CurrencyIcon icon = currency.getIcon();
        final Object icon_object = icon.icon();
        return switch (icon.type()) {
            case TEXTURE -> {
                if(icon_object instanceof ResourceLocation location) {
                    yield Icon.getIcon(location);
                } else if(icon_object instanceof String location) {
                    if(ResourceLocation.isValidResourceLocation(location))
                        yield Icon.getIcon(location);
                }

                yield SDMEconomyFTBQuestsIntegration.defaultIcon();
            }
            case ITEM -> {
                if(icon_object instanceof Item item) {
                    yield ItemIcon.getItemIcon(item);
                } else if(icon_object instanceof ItemStack item) {
                    yield ItemIcon.getItemIcon(item.copyWithCount(1));
                } else if(icon_object instanceof Ingredient ingredient) {
                    yield ItemIcon.getItemIcon(((ItemStack[]) Arrays.stream(ingredient.getItems()).map(s -> s.copyWithCount(1)).toArray(ItemStack[]::new))[0]);
                }
                yield SDMEconomyFTBQuestsIntegration.defaultIcon();
            }
            default -> SDMEconomyFTBQuestsIntegration.defaultIcon();
        };
    }

    @Override
    public boolean consumesResources() {
        return true;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void addMouseOverText(TooltipList list, TeamData teamData) {
        super.addMouseOverText(list, teamData);
        list.add(Component.translatable(SDMEconomyFTBQuestCurrencySupport.BALANCE_NAME_KEY)
                .append(": ")
                .append(Component.literal(SDMEconomyFTBQuestCurrencyClientSupport.formatClientAmount(
                        currencyId,
                        SDMEconomyFTBQuestCurrencyClientSupport.getClientBalance(currencyId)
                )))
                .withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void submitTask(TeamData teamData, ServerPlayer player, ItemStack craftedItem) {
        if (!checkTaskSequence(teamData)) {
            return;
        }

        long remaining = value - teamData.getProgress(this);
        long amount = SDMEconomyFTBQuestCurrencySupport.payableWholeAmount(player, currencyId, remaining);

        if (amount <= 0L) {
            return;
        }

        if (SDMEconomyFTBQuestCurrencySupport.withdraw(player, currencyId, SDMEconomyFTBQuestCurrencySupport.amount(amount))) {
            teamData.addProgress(this, amount);
        }
    }
}
