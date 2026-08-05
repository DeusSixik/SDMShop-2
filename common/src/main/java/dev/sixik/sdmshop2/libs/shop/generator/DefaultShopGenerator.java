package dev.sixik.sdmshop2.libs.shop.generator;

import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.sdmeconomy.IExternalCurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyCurrencyRegistry;
import dev.sixik.sdmshop2.libs.sdmeconomy.custom_currency.ExternalItemCurrency;
import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.base.ShopTable;
import dev.sixik.sdmshop2.libs.shop.builder.ShopBuilder;
import dev.sixik.sdmshop2.libs.shop.builder.ShopOfferBuilder;
import dev.sixik.sdmshop2.libs.shop.components.ItemRewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.LinkedHashMap;
import java.util.Map;

public final class DefaultShopGenerator {

    public static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "default");

    private static final String EMERALD_MONEY_ID = "emerald";
    private static final String GOLD_MONEY_ID = "gold";
    private static final String DIAMOND_MONEY_ID = "diamond";
    private static final String NETHERITE_MONEY_ID = "netherite";

    private static final Category RESOURCES = category("shop.default.category.resources", 10);
    private static final Category BUILDING_BLOCKS = category("shop.default.category.blocks", 20);
    private static final Category FARMING_FOOD = category("shop.default.category.farming_food", 30);
    private static final Category UTILITY = category("shop.default.category.utility", 40);
    private static final Category REDSTONE = category("shop.default.category.redstone", 50);
    private static final Category RARES = category("shop.default.category.rares", 60);
    private static final Category EXCHANGE = category("shop.default.category.exchange", 70);

    private static final Map<String, Integer> CATEGORY_ORDER = new LinkedHashMap<>();

    static {
        registerCategory(RESOURCES);
        registerCategory(BUILDING_BLOCKS);
        registerCategory(FARMING_FOOD);
        registerCategory(UTILITY);
        registerCategory(REDSTONE);
        registerCategory(RARES);
        registerCategory(EXCHANGE);
    }

    private DefaultShopGenerator() {
    }

    public static void registerDefault(ShopTable table) {
        if (table == null) {
            throw new IllegalStateException("ShopTable is not initialized");
        }

        try {
            registerUnknownCurrencies();

            if (table.getShop(ID) != null) {
                return;
            }

            table.addShop(generate());
            SDMShop2.LOGGER.info("Generated default shop '{}'.", ID);
        } catch (Exception e) {
            SDMShop2.LOGGER.error("Failed to register default shop", e);
        }
    }

    public static void registerDefaultIfEmpty() {
        registerDefaultIfEmpty(ShopTable.Instance);
    }

    public static void registerDefaultIfEmpty(ShopTable table) {
        if (table == null || table.getShopsCount() > 0) {
            return;
        }

        registerDefault(table);
    }

    private static void checkOrRegisterCurrency(IExternalCurrency currency) {
        IExternalCurrency existing = SDMEconomyCurrencyRegistry.getCurrency(currency.getId());
        if (existing == null) {
            SDMEconomyCurrencyRegistry.registerAndSaveCurrency(currency);
        }
    }

    private static void registerUnknownCurrencies() {
        checkOrRegisterCurrency(new ExternalItemCurrency(EMERALD_MONEY_ID, new ItemStack(Items.EMERALD)));
        checkOrRegisterCurrency(new ExternalItemCurrency(GOLD_MONEY_ID, new ItemStack(Items.GOLD_INGOT)));
        checkOrRegisterCurrency(new ExternalItemCurrency(DIAMOND_MONEY_ID, new ItemStack(Items.DIAMOND)));
        checkOrRegisterCurrency(new ExternalItemCurrency(NETHERITE_MONEY_ID, new ItemStack(Items.NETHERITE_INGOT)));
    }

    private static ShopInstance generate() {
        ShopBuilder shop = ShopBuilder.builder(ID);

        addResources(shop);
        addBuildingBlocks(shop);
        addFarmingAndFood(shop);
        addUtility(shop);
        addRedstone(shop);
        addRares(shop);
        addExchange(shop);

        ShopInstance instance = shop.mustSave().end();
        applyCategoryOrder(instance);
        return instance;
    }

    private static void addResources(ShopBuilder shop) {
        addItem(shop, RESOURCES, Items.COAL, 16, EMERALD_MONEY_ID, 1);
        addItem(shop, RESOURCES, Items.COPPER_INGOT, 8, EMERALD_MONEY_ID, 1);
        addItem(shop, RESOURCES, Items.IRON_INGOT, 4, EMERALD_MONEY_ID, 2);
        addItem(shop, RESOURCES, Items.GOLD_INGOT, 2, EMERALD_MONEY_ID, 3);
        addItem(shop, RESOURCES, Items.REDSTONE, 16, EMERALD_MONEY_ID, 2);
        addItem(shop, RESOURCES, Items.LAPIS_LAZULI, 12, EMERALD_MONEY_ID, 2);
        addItem(shop, RESOURCES, Items.QUARTZ, 12, EMERALD_MONEY_ID, 2);
        addItem(shop, RESOURCES, Items.AMETHYST_SHARD, 8, EMERALD_MONEY_ID, 2);
        addItem(shop, RESOURCES, Items.DIAMOND, 1, GOLD_MONEY_ID, 4);
    }

    private static void addBuildingBlocks(ShopBuilder shop) {
        addItem(shop, BUILDING_BLOCKS, Items.GLASS, 16, EMERALD_MONEY_ID, 2);
        addItem(shop, BUILDING_BLOCKS, Items.STONE_BRICKS, 16, EMERALD_MONEY_ID, 2);
        addItem(shop, BUILDING_BLOCKS, Items.DEEPSLATE_BRICKS, 16, EMERALD_MONEY_ID, 3);
        addItem(shop, BUILDING_BLOCKS, Items.TERRACOTTA, 16, EMERALD_MONEY_ID, 3);
        addItem(shop, BUILDING_BLOCKS, Items.WHITE_WOOL, 16, EMERALD_MONEY_ID, 2);
        addItem(shop, BUILDING_BLOCKS, Items.OBSIDIAN, 8, EMERALD_MONEY_ID, 6);
        addItem(shop, BUILDING_BLOCKS, Items.GLOWSTONE, 8, EMERALD_MONEY_ID, 4);
        addItem(shop, BUILDING_BLOCKS, Items.SEA_LANTERN, 4, GOLD_MONEY_ID, 2);
        addItem(shop, BUILDING_BLOCKS, Items.END_STONE, 16, EMERALD_MONEY_ID, 3);
    }

    private static void addFarmingAndFood(ShopBuilder shop) {
        addItem(shop, FARMING_FOOD, Items.OAK_SAPLING, 4, EMERALD_MONEY_ID, 1);
        addItem(shop, FARMING_FOOD, Items.SPRUCE_SAPLING, 4, EMERALD_MONEY_ID, 1);
        addItem(shop, FARMING_FOOD, Items.WHEAT_SEEDS, 16, EMERALD_MONEY_ID, 1);
        addItem(shop, FARMING_FOOD, Items.CARROT, 8, EMERALD_MONEY_ID, 1);
        addItem(shop, FARMING_FOOD, Items.POTATO, 8, EMERALD_MONEY_ID, 1);
        addItem(shop, FARMING_FOOD, Items.MELON_SEEDS, 8, EMERALD_MONEY_ID, 1);
        addItem(shop, FARMING_FOOD, Items.PUMPKIN_SEEDS, 8, EMERALD_MONEY_ID, 1);
        addItem(shop, FARMING_FOOD, Items.BREAD, 16, EMERALD_MONEY_ID, 2);
        addItem(shop, FARMING_FOOD, Items.COOKED_BEEF, 8, EMERALD_MONEY_ID, 3);
        addItem(shop, FARMING_FOOD, Items.GOLDEN_CARROT, 8, GOLD_MONEY_ID, 2);
    }

    private static void addUtility(ShopBuilder shop) {
        addItem(shop, UTILITY, Items.TORCH, 32, EMERALD_MONEY_ID, 1);
        addItem(shop, UTILITY, Items.LANTERN, 8, EMERALD_MONEY_ID, 4);
        addItem(shop, UTILITY, Items.ARROW, 32, EMERALD_MONEY_ID, 2);
        addItem(shop, UTILITY, Items.BONE, 16, EMERALD_MONEY_ID, 2);
        addItem(shop, UTILITY, Items.STRING, 16, EMERALD_MONEY_ID, 2);
        addItem(shop, UTILITY, Items.GUNPOWDER, 8, EMERALD_MONEY_ID, 3);
        addItem(shop, UTILITY, Items.ENDER_PEARL, 4, GOLD_MONEY_ID, 2);
        addItem(shop, UTILITY, Items.SLIME_BALL, 8, GOLD_MONEY_ID, 2);
        addItem(shop, UTILITY, Items.BLAZE_ROD, 4, GOLD_MONEY_ID, 3);
        addItem(shop, UTILITY, Items.SADDLE, 1, GOLD_MONEY_ID, 8);
        addItem(shop, UTILITY, Items.NAME_TAG, 1, GOLD_MONEY_ID, 10);
    }

    private static void addRedstone(ShopBuilder shop) {
        addItem(shop, REDSTONE, Items.REPEATER, 4, EMERALD_MONEY_ID, 2);
        addItem(shop, REDSTONE, Items.COMPARATOR, 2, EMERALD_MONEY_ID, 4);
        addItem(shop, REDSTONE, Items.OBSERVER, 4, GOLD_MONEY_ID, 2);
        addItem(shop, REDSTONE, Items.PISTON, 4, EMERALD_MONEY_ID, 4);
        addItem(shop, REDSTONE, Items.STICKY_PISTON, 4, GOLD_MONEY_ID, 3);
        addItem(shop, REDSTONE, Items.HOPPER, 2, GOLD_MONEY_ID, 4);
        addItem(shop, REDSTONE, Items.RAIL, 16, EMERALD_MONEY_ID, 3);
        addItem(shop, REDSTONE, Items.POWERED_RAIL, 8, GOLD_MONEY_ID, 3);
    }

    private static void addRares(ShopBuilder shop) {
        addItem(shop, RARES, Items.EXPERIENCE_BOTTLE, 16, GOLD_MONEY_ID, 3);
        addItem(shop, RARES, Items.PHANTOM_MEMBRANE, 4, GOLD_MONEY_ID, 4);
        addItem(shop, RARES, Items.SHULKER_SHELL, 2, DIAMOND_MONEY_ID, 4);
        addItem(shop, RARES, Items.TOTEM_OF_UNDYING, 1, DIAMOND_MONEY_ID, 12);
        addItem(shop, RARES, Items.WITHER_SKELETON_SKULL, 1, DIAMOND_MONEY_ID, 10);
        addItem(shop, RARES, Items.ENCHANTED_GOLDEN_APPLE, 1, DIAMOND_MONEY_ID, 32);
        addItem(shop, RARES, Items.ELYTRA, 1, DIAMOND_MONEY_ID, 64)
                .addPrice(NETHERITE_MONEY_ID, 4);
        addItem(shop, RARES, Items.DRAGON_EGG, 1, NETHERITE_MONEY_ID, 2)
                .addPrice(DIAMOND_MONEY_ID, 16);
    }

    private static void addExchange(ShopBuilder shop) {
        addItem(shop, EXCHANGE, Items.GOLD_INGOT, 1, EMERALD_MONEY_ID, 4);
        addItem(shop, EXCHANGE, Items.EMERALD, 4, GOLD_MONEY_ID, 1);

        addItem(shop, EXCHANGE, Items.DIAMOND, 1, GOLD_MONEY_ID, 4);
        addItem(shop, EXCHANGE, Items.GOLD_INGOT, 4, DIAMOND_MONEY_ID, 1);

        addItem(shop, EXCHANGE, Items.NETHERITE_INGOT, 1, DIAMOND_MONEY_ID, 8);
        addItem(shop, EXCHANGE, Items.DIAMOND, 8, NETHERITE_MONEY_ID, 1);
    }

    private static ShopOfferBuilder addItem(
            ShopBuilder shop,
            Category category,
            Item item,
            int amount,
            String currencyId,
            double price
    ) {
        ShopOfferBuilder builder = ShopOfferBuilder.builder(category.id())
                .addComponent(new NameComponent(item.getDescriptionId()))
                .addComponent(new ItemRewardComponent(item, amount))
                .addPrice(currencyId, price);
        shop.addOffer(builder);
        return builder;
    }

    private static void applyCategoryOrder(ShopInstance instance) {
        for (ShopOffer offer : instance.getEntries().getEntryMap().values()) {
            for (CatalogComponent catalog : offer.getComponents(CatalogComponent.class)) {
                catalog.setOrder(CATEGORY_ORDER.getOrDefault(catalog.getId(), 1_000));
            }
        }

        instance.getCategories().reindex();
    }

    private static Category category(String id, int order) {
        return new Category(id, order);
    }

    private static void registerCategory(Category category) {
        CATEGORY_ORDER.put(category.id(), category.order());
    }

    private record Category(String id, int order) {
    }
}
