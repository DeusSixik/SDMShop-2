package dev.sixik.sdmshop2.libs.sdmeconomy.integration.ftbquests;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftbquests.net.DisplayRewardToastMessage;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.reward.Reward;
import dev.ftb.mods.ftbquests.quest.reward.RewardType;
import dev.sixik.sdmeconomy.api.EconomyApi;
import dev.sixik.sdmshop2.libs.sdmeconomy.IExternalCurrency;
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

import java.math.BigDecimal;
import java.util.Arrays;

public class SDMEconomyCurrencyReward extends Reward {

    public static RewardType TYPE;

    private ResourceLocation currencyId = SDMEconomyFTBQuestCurrencySupport.defaultCurrencyId();
    private long value = 1L;
    private int randomBonus = 0;

    public SDMEconomyCurrencyReward(long id, Quest quest) {
        super(id, quest);
    }

    @Override
    public RewardType getType() {
        return TYPE;
    }

    @Override
    public void writeData(CompoundTag nbt) {
        super.writeData(nbt);
        nbt.putString(SDMEconomyFTBQuestCurrencySupport.CURRENCY_ID_KEY, currencyId.toString());
        nbt.putLong(SDMEconomyFTBQuestCurrencySupport.VALUE_KEY, value);

        if (randomBonus > 0) {
            nbt.putInt(SDMEconomyFTBQuestCurrencySupport.RANDOM_BONUS_KEY, randomBonus);
        }
    }

    @Override
    public void readData(CompoundTag nbt) {
        super.readData(nbt);

        currencyId = nbt.contains(SDMEconomyFTBQuestCurrencySupport.CURRENCY_ID_KEY)
                ? SDMEconomyFTBQuestCurrencySupport.normalizeCurrencyId(
                ResourceLocation.tryParse(nbt.getString(SDMEconomyFTBQuestCurrencySupport.CURRENCY_ID_KEY))
        )
                : SDMEconomyFTBQuestCurrencySupport.defaultCurrencyId();

        if (nbt.contains(SDMEconomyFTBQuestCurrencySupport.VALUE_KEY)) {
            value = Math.max(1L, nbt.getLong(SDMEconomyFTBQuestCurrencySupport.VALUE_KEY));
        } else {
            value = Math.max(1L, nbt.getLong(SDMEconomyFTBQuestCurrencySupport.LEGACY_REWARD_VALUE_KEY));
        }
        randomBonus = normalizeRandomBonus(nbt.getInt(SDMEconomyFTBQuestCurrencySupport.RANDOM_BONUS_KEY));
    }

    @Override
    public void writeNetData(FriendlyByteBuf buffer) {
        super.writeNetData(buffer);
        buffer.writeResourceLocation(SDMEconomyFTBQuestCurrencySupport.normalizeCurrencyId(currencyId));
        buffer.writeVarLong(value);
        buffer.writeVarInt(randomBonus);
    }

    @Override
    public void readNetData(FriendlyByteBuf buffer) {
        super.readNetData(buffer);
        currencyId = SDMEconomyFTBQuestCurrencySupport.normalizeCurrencyId(buffer.readResourceLocation());
        value = Math.max(1L, buffer.readVarLong());
        randomBonus = normalizeRandomBonus(buffer.readVarInt());
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void fillConfigGroup(ConfigGroup config) {
        super.fillConfigGroup(config);
        SDMEconomyFTBQuestCurrencyClientSupport.writeCurrencyConfig(config, currencyId, value -> currencyId = value);
        SDMEconomyFTBQuestCurrencyClientSupport.writeValueConfig(config, value, v -> value = v);
        SDMEconomyFTBQuestCurrencyClientSupport.writeRandomBonusConfig(config, randomBonus, v -> randomBonus = v);
    }

    @Override
    public void claim(ServerPlayer player, boolean notify) {
        long bonus = randomBonus <= 0 ? 0L : player.serverLevel().random.nextInt(randomBonus + 1);
        long amount = safeAdd(value, bonus);
        BigDecimal rewardAmount = SDMEconomyFTBQuestCurrencySupport.amount(amount);

        if (!SDMEconomyFTBQuestCurrencySupport.deposit(player, currencyId, rewardAmount)) {
            return;
        }

        if (notify) {
            new DisplayRewardToastMessage(
                    id,
                    Component.literal(SDMEconomyFTBQuestCurrencySupport.formatAmount(currencyId, rewardAmount)),
                    getToastIcon()
            ).sendTo(player);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Component getAltTitle() {
        Component currencyName = SDMEconomyFTBQuestCurrencyClientSupport.clientCurrencyDisplayName(currencyId);
        if (randomBonus > 0) {
            return Component.empty()
                    .append(currencyName)
                    .append(": ")
                    .append(SDMEconomyFTBQuestCurrencyClientSupport.formatClientAmount(currencyId, value))
                    .append(" - ")
                    .append(SDMEconomyFTBQuestCurrencyClientSupport.formatClientAmount(currencyId, getMaxRewardValue()))
                    .withStyle(ChatFormatting.GOLD);
        }

        return Component.empty()
                .append(currencyName)
                .append(": ")
                .append(SDMEconomyFTBQuestCurrencyClientSupport.formatClientAmount(currencyId, value))
                .withStyle(ChatFormatting.GOLD);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Icon getAltIcon() {
        return SDMEconomyFTBQuestCurrencyClientSupport.clientCurrencyIcon(currencyId);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public String getButtonText() {
        if (randomBonus > 0) {
            return SDMEconomyFTBQuestCurrencyClientSupport.formatClientAmount(currencyId, value)
                    + "-"
                    + SDMEconomyFTBQuestCurrencyClientSupport.formatClientAmount(currencyId, getMaxRewardValue());
        }

        return SDMEconomyFTBQuestCurrencyClientSupport.formatClientAmount(currencyId, value);
    }

    private Icon getToastIcon() {
        final IExternalCurrency currency = EconomyApi.getCurrencies(true).get(currencyId);
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

    private long getMaxRewardValue() {
        return safeAdd(value, randomBonus);
    }

    private static long safeAdd(long left, long right) {
        if (right > 0L && left > Long.MAX_VALUE - right) {
            return Long.MAX_VALUE;
        }

        return left + right;
    }

    private static int normalizeRandomBonus(int value) {
        return Math.max(0, Math.min(Integer.MAX_VALUE - 1, value));
    }
}
