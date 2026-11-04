package dev.sixik.sdmshop2.libs.shop.client.ui.editor.preset;

import com.google.gson.JsonObject;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.components.CommandRewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.ItemCostComponent;
import dev.sixik.sdmshop2.libs.shop.components.ItemRewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import dev.sixik.sdmshop2.libs.shop.components.money.MoneyCostComponent;
import dev.sixik.sdmshop2.libs.shop.components.money.MoneyRewardComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ShopOfferPresetManager {

    private static final List<ShopOfferPreset> BUILT_IN_PRESETS = new CopyOnWriteArrayList<>();

    private ShopOfferPresetManager() {
    }

    public static List<ShopOfferPreset> getBuiltInPresets() {
        if (BUILT_IN_PRESETS.isEmpty()) {
            synchronized (BUILT_IN_PRESETS) {
                if (BUILT_IN_PRESETS.isEmpty()) {
                    BUILT_IN_PRESETS.addAll(initBuiltInPresets());
                }
            }
        }
        return Collections.unmodifiableList(BUILT_IN_PRESETS);
    }

    public static List<ShopOfferPreset> getCustomPresets() {
        return ShopClientCache.getOfferPresets();
    }

    public static List<ShopOfferPreset> getAllPresets() {
        List<ShopOfferPreset> all = new ArrayList<>(getBuiltInPresets());
        all.addAll(getCustomPresets());
        return all;
    }

    public static ShopOfferPreset saveOfferAsPreset(String name, String description, @Nullable ShopOffer offer) {
        String presetName = (name != null && !name.isBlank()) ? name.trim() : "Custom Preset";
        String presetDesc = (description != null) ? description.trim() : "";

        JsonObject templateJson;
        if (offer != null) {
            try {
                templateJson = offer.serialize().getAsJsonObject().deepCopy();
            } catch (Exception e) {
                templateJson = createDefaultTemplate(presetName);
            }
        } else {
            templateJson = createDefaultTemplate(presetName);
        }

        String presetId = "custom_" + UUID.randomUUID();
        ShopOfferPreset preset = new ShopOfferPreset(presetId, presetName, presetDesc, false, templateJson);
        ShopClientCache.addOfferPreset(preset);
        return preset;
    }

    public static boolean deletePreset(String presetId) {
        return ShopClientCache.deleteOfferPreset(presetId);
    }

    private static List<ShopOfferPreset> initBuiltInPresets() {
        List<ShopOfferPreset> presets = new ArrayList<>();
        presets.add(createDiamondCoinPreset());
        presets.add(createItemTradePreset());
        presets.add(createSellItemPreset());
        presets.add(createCommandRewardPreset());
        presets.add(createEmptyOfferPreset());
        return presets;
    }

    private static ResourceLocation getPrimaryCurrencyId() {
        return SDMEconomyServiceClient.getAllCurrencies().isEmpty()
                ? new ResourceLocation("sdm", "coin")
                : SDMEconomyServiceClient.getAllCurrencies().values().iterator().next().getId();
    }

    private static ShopOfferPreset createDiamondCoinPreset() {
        ShopOffer offer = ShopOffer.create(UUID.randomUUID(), false);
        offer.addComponent(new NameComponent("Diamond Offer"));
        CatalogComponent cat = new CatalogComponent("resources");
        cat.setIconType(CatalogComponent.IconType.ITEM);
        cat.setIconItem(Items.DIAMOND.getDefaultInstance());
        offer.addComponent(cat);
        offer.addComponent(new ItemRewardComponent(Items.DIAMOND, 1));
        offer.addComponent(new MoneyCostComponent(getPrimaryCurrencyId(), 10.0));

        return new ShopOfferPreset(
                "builtin_diamond_coins",
                "Diamond for Coins",
                "Buy 1 Diamond for 10 Coins",
                true,
                offer.serialize().getAsJsonObject()
        );
    }

    private static ShopOfferPreset createItemTradePreset() {
        ShopOffer offer = ShopOffer.create(UUID.randomUUID(), false);
        offer.addComponent(new NameComponent("Gold Ingot Trade"));
        CatalogComponent cat = new CatalogComponent("resources");
        cat.setIconType(CatalogComponent.IconType.ITEM);
        cat.setIconItem(Items.GOLD_INGOT.getDefaultInstance());
        offer.addComponent(cat);
        offer.addComponent(new ItemRewardComponent(Items.GOLD_INGOT, 2));
        offer.addComponent(new ItemCostComponent(Items.IRON_INGOT, 8));

        return new ShopOfferPreset(
                "builtin_item_trade",
                "Item Trade (Iron for Gold)",
                "Trade 8 Iron Ingots for 2 Gold Ingots",
                true,
                offer.serialize().getAsJsonObject()
        );
    }

    private static ShopOfferPreset createSellItemPreset() {
        ShopOffer offer = ShopOffer.create(UUID.randomUUID(), false);
        offer.addComponent(new NameComponent("Sell Cobblestone"));
        CatalogComponent cat = new CatalogComponent("resources");
        cat.setIconType(CatalogComponent.IconType.ITEM);
        cat.setIconItem(Items.COBBLESTONE.getDefaultInstance());
        offer.addComponent(cat);
        offer.addComponent(new MoneyRewardComponent(getPrimaryCurrencyId(), 5.0));
        offer.addComponent(new ItemCostComponent(Items.COBBLESTONE, 64));

        return new ShopOfferPreset(
                "builtin_sell_item",
                "Sell Item for Coins",
                "Sell 64 Cobblestone for 5 Coins",
                true,
                offer.serialize().getAsJsonObject()
        );
    }

    private static ShopOfferPreset createCommandRewardPreset() {
        ShopOffer offer = ShopOffer.create(UUID.randomUUID(), false);
        offer.addComponent(new NameComponent("Day Time Command"));
        CatalogComponent cat = new CatalogComponent("specials");
        cat.setIconType(CatalogComponent.IconType.ITEM);
        cat.setIconItem(Items.CLOCK.getDefaultInstance());
        offer.addComponent(cat);
        offer.addComponent(new CommandRewardComponent("/time set day", "Day Time"));
        offer.addComponent(new MoneyCostComponent(getPrimaryCurrencyId(), 50.0));

        return new ShopOfferPreset(
                "builtin_command",
                "Command Reward",
                "Execute /time set day for 50 Coins",
                true,
                offer.serialize().getAsJsonObject()
        );
    }

    private static ShopOfferPreset createEmptyOfferPreset() {
        ShopOffer offer = ShopOffer.create(UUID.randomUUID(), false);
        offer.addComponent(new NameComponent("Empty Offer"));
        CatalogComponent cat = new CatalogComponent("resources");
        cat.setIconType(CatalogComponent.IconType.ITEM);
        cat.setIconItem(Items.CHEST.getDefaultInstance());
        offer.addComponent(cat);

        return new ShopOfferPreset(
                "builtin_empty",
                "Empty Offer",
                "Base offer skeleton without costs or rewards",
                true,
                offer.serialize().getAsJsonObject()
        );
    }

    private static JsonObject createDefaultTemplate(String name) {
        ShopOffer offer = ShopOffer.create(UUID.randomUUID(), false);
        offer.addComponent(new NameComponent(name));
        CatalogComponent cat = new CatalogComponent("resources");
        cat.setIconType(CatalogComponent.IconType.ITEM);
        cat.setIconItem(Items.DIAMOND.getDefaultInstance());
        offer.addComponent(cat);
        offer.addComponent(new ItemRewardComponent(Items.DIAMOND, 1));
        offer.addComponent(new MoneyCostComponent(getPrimaryCurrencyId(), 10.0));
        return offer.serialize().getAsJsonObject();
    }
}
