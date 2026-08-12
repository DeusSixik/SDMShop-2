package dev.sixik.sdmshop2.libs.sdmeconomy.integration.ftbquests;

import dev.ftb.mods.ftblibrary.config.ConfigGroup;
import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyCurrencyRegistry;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyPlatform;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;
import dev.sixik.sdmshop2.libs.sdmeconomy.custom_currency.BasicCoinCurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.IconType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
final class SDMEconomyFTBQuestCurrencyClientSupport {

    private SDMEconomyFTBQuestCurrencyClientSupport() {
    }

    static void writeCurrencyConfig(ConfigGroup config, ResourceLocation currencyId, Consumer<ResourceLocation> setter) {
        ResourceLocation normalizedCurrencyId = SDMEconomyFTBQuestCurrencySupport.normalizeCurrencyId(currencyId);
        NameMap<ResourceLocation> currencies = currencyNameMap(normalizedCurrencyId);
        config.addEnum(
                        SDMEconomyFTBQuestCurrencySupport.CURRENCY_ID_KEY,
                        normalizedCurrencyId,
                        value -> setter.accept(SDMEconomyFTBQuestCurrencySupport.normalizeCurrencyId(value)),
                        currencies,
                        normalizedCurrencyId
                )
                .setNameKey(SDMEconomyFTBQuestCurrencySupport.CURRENCY_ID_NAME_KEY);
    }

    static void writeValueConfig(ConfigGroup config, long value, Consumer<Long> setter) {
        config.addLong(
                        SDMEconomyFTBQuestCurrencySupport.VALUE_KEY,
                        value,
                        v -> setter.accept(Math.max(1L, v)),
                        1L,
                        1L,
                        Long.MAX_VALUE
                )
                .setNameKey(SDMEconomyFTBQuestCurrencySupport.VALUE_NAME_KEY);
    }

    static void writeRandomBonusConfig(ConfigGroup config, int randomBonus, Consumer<Integer> setter) {
        config.addInt(
                        SDMEconomyFTBQuestCurrencySupport.RANDOM_BONUS_KEY,
                        randomBonus,
                        v -> setter.accept(Math.max(0, Math.min(Integer.MAX_VALUE - 1, v))),
                        0,
                        0,
                        Integer.MAX_VALUE - 1
                )
                .setNameKey(SDMEconomyFTBQuestCurrencySupport.RANDOM_BONUS_NAME_KEY);
    }

    static ICurrency getClientCurrency(ResourceLocation currencyId) {
        ResourceLocation id = SDMEconomyFTBQuestCurrencySupport.normalizeCurrencyId(currencyId);
        ICurrency currency = SDMEconomyServiceClient.getCurrency(id);
        if (currency != null) {
            return currency;
        }

        return SDMEconomyFTBQuestCurrencySupport.getCurrency(id);
    }

    static BigDecimal getClientBalance(ResourceLocation currencyId) {
        ICurrency currency = getClientCurrency(currencyId);
        return SDMEconomyServiceClient.getBalance(currency, Minecraft.getInstance().player);
    }

    static String formatClientAmount(ResourceLocation currencyId, long amount) {
        return formatClientAmount(currencyId, SDMEconomyFTBQuestCurrencySupport.amount(amount));
    }

    static String formatClientAmount(ResourceLocation currencyId, BigDecimal amount) {
        ICurrency currency = getClientCurrency(currencyId);
        if (currency == null) {
            return amount.toPlainString() + " " + SDMEconomyFTBQuestCurrencySupport.normalizeCurrencyId(currencyId);
        }

        return currency.format(amount);
    }

    static Component clientCurrencyDisplayName(ResourceLocation currencyId) {
        ICurrency currency = getClientCurrency(currencyId);
        if (currency == null) {
            return Component.literal(SDMEconomyFTBQuestCurrencySupport.normalizeCurrencyId(currencyId).toString());
        }

        return currency.getDisplayName() == null ? Component.literal(currency.getId().toString()) : currency.getDisplayName();
    }

    static Icon clientCurrencyIcon(ResourceLocation currencyId) {
        ICurrency currency = getClientCurrency(currencyId);
        if (currency != null) {
            CurrencyIcon icon = currency.getIcon();
            if (icon != null && icon.type() == IconType.ITEM) {
                Object value = icon.icon();
                if (value instanceof ItemStack stack && !stack.isEmpty()) {
                    return ItemIcon.getItemIcon(stack.copyWithCount(1));
                }

                if (value instanceof Item item) {
                    return ItemIcon.getItemIcon(item.getDefaultInstance());
                }
            }

            if (icon != null && icon.type() == IconType.TEXTURE) {
                Object value = icon.icon();
                if (value instanceof ResourceLocation location) {
                    return Icon.getIcon(location.toString());
                }

                if (value instanceof String texture && texture.contains(":")) {
                    return Icon.getIcon(texture);
                }
            }
        }

        return SDMEconomyFTBQuestsIntegration.defaultIcon();
    }

    private static NameMap<ResourceLocation> currencyNameMap(ResourceLocation selectedCurrencyId) {
        ResourceLocation normalizedCurrencyId = SDMEconomyFTBQuestCurrencySupport.normalizeCurrencyId(selectedCurrencyId);
        List<ResourceLocation> ids = clientCurrencyIds(normalizedCurrencyId);
        return NameMap.of(normalizedCurrencyId, ids)
                .id(ResourceLocation::toString)
                .name(id -> Component.empty()
                        .append(clientCurrencyDisplayName(id))
                        .append(Component.literal(" (" + id + ")").withStyle(ChatFormatting.DARK_GRAY)))
                .icon(SDMEconomyFTBQuestCurrencyClientSupport::clientCurrencyIcon)
                .create();
    }

    private static List<ResourceLocation> clientCurrencyIds(ResourceLocation selectedCurrencyId) {
        Set<ResourceLocation> ids = new LinkedHashSet<>();
        ids.add(SDMEconomyFTBQuestCurrencySupport.normalizeCurrencyId(selectedCurrencyId));
        ids.add(BasicCoinCurrency.ID);
        ids.addAll(sortedCurrencyIds(SDMEconomyServiceClient.getAllCurrencies()));
        if (SDMEconomyPlatform.server != null) {
            ids.addAll(sortedCurrencyIds(SDMEconomyCurrencyRegistry.getAllCurrenciesMap()));
        }

        return new ArrayList<>(ids);
    }

    private static List<ResourceLocation> sortedCurrencyIds(Map<ResourceLocation, ? extends ICurrency> currencies) {
        List<ResourceLocation> ids = new ArrayList<>();
        if (currencies != null) {
            ids.addAll(currencies.keySet());
        }
        ids.removeIf(id -> id == null);
        ids.sort(Comparator.comparing(ResourceLocation::toString));
        return ids;
    }
}
