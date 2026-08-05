package dev.sixik.sdmshop2.libs.shop.client.config.constructors;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;
import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.client.SDMShopClient;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopScreenElement;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.utils.ShopUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.Nullable;

import java.util.*;

public final class ComponentConfigOptionProviders {

    public static final ResourceLocation MONEY_IDS = new ResourceLocation("sdm", "money_ids");
    public static final ResourceLocation CATALOG_IDS = new ResourceLocation("sdm", "catalog_ids");

    private static final Map<ResourceLocation, Provider> PROVIDERS = new Object2ObjectOpenHashMap<>();

    static {
        register(MONEY_IDS, ComponentConfigOptionProviders::moneyOptions);
        register(CATALOG_IDS, ComponentConfigOptionProviders::catalogIdOptions);
    }

    private ComponentConfigOptionProviders() {
    }

    public static void register(ResourceLocation id, Provider provider) {
        if (id == null || provider == null) return;
        PROVIDERS.put(id, provider);
    }

    public static @Nullable Provider get(ResourceLocation id) {
        return PROVIDERS.get(id);
    }

    public static @Nullable Provider get(String id) {
        ResourceLocation location = parseProviderId(id);
        return location == null ? null : get(location);
    }

    public static List<Option> getOptions(ShopComponent component, ComponentConfigAccess.CachedField field) {
        if (field.options() == null) return List.of();

        Provider provider = get(field.options().provider());
        if (provider == null) {
            SDMShop2.LOGGER.warn("Unknown component config options provider '{}'", field.options().provider());
            return List.of();
        }

        List<Option> options = provider.getOptions(component, field);
        return options == null ? List.of() : options;
    }

    public static boolean applyOption(ShopComponent component, ComponentConfigAccess.CachedField field, Object value) {
        if (field.options() == null) return false;

        Provider provider = get(field.options().provider());
        return provider != null && provider.applyOption(component, field, value);
    }

    private static @Nullable ResourceLocation parseProviderId(String id) {
        if (id == null || id.isEmpty()) return null;
        return id.contains(":") ? ResourceLocation.tryParse(id) : ResourceLocation.tryBuild("sdm", id);
    }

    private static List<Option> moneyOptions(ShopComponent component, ComponentConfigAccess.CachedField field) {
        List<Option> options = new ArrayList<>();
        Map<ResourceLocation, ICurrency> currencies = ShopScreenElement.Instance != null && ShopScreenElement.Instance.isEditorMode()
                ? ShopScreenElement.Instance.getEditorCurrencies()
                : SDMEconomyServiceClient.getAllCurrencies();
        currencies.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(ResourceLocation::toString)))
                .forEach(entry -> {
                    ResourceLocation id = entry.getKey();
                    ICurrency currency = entry.getValue();
                    Component label = currency.getDisplayName().copy()
                            .append(Component.literal(" §8(" + id + ")"));
                    options.add(new Option(id, label, ShopUtils.getCurrencyTexture(currency)));
                });
        return options;
    }

    private static List<Option> catalogIdOptions(ShopComponent component, ComponentConfigAccess.CachedField field) {
        List<Option> options = new ArrayList<>();
        for (CatalogComponent catalog : getCatalogs()) {
            if (catalog.getId() == null) continue;
            options.add(new Option(catalog.getId(), catalogLabel(catalog)));
        }
        return sortCatalogOptions(options);
    }

    private static List<CatalogComponent> getCatalogs() {
        if (ShopScreenElement.Instance != null && ShopScreenElement.Instance.isEditorMode()) {
            return ShopScreenElement.Instance.getOrderedCategories();
        }

        ShopInstance shop = ShopScreenElement.Instance == null
                ? SDMShopClient.Shop
                : ShopScreenElement.Instance.getActiveShop();
        if (shop == null || shop.getCategories() == null) {
            return List.of();
        }
        return shop.getCategories().getCatalogsComponents();
    }

    private static Component catalogLabel(CatalogComponent catalog) {
        String id = catalog.getId() == null ? "none" : catalog.getId();
        UUID uuid = catalog.getUuid();
        return Component.literal(id + (uuid == null ? "" : " §8(" + uuid + ")"));
    }

    private static List<Option> sortCatalogOptions(List<Option> options) {
        options.sort(Comparator.comparing(option -> option.label().getString(), String.CASE_INSENSITIVE_ORDER));
        return options;
    }

    private static @Nullable CatalogComponent findCatalogByValue(Object value) {
        for (CatalogComponent catalog : getCatalogs()) {
            if (Objects.equals(catalog.getId(), value) || Objects.equals(catalog.getUuid(), value)) {
                return catalog;
            }
        }
        return null;
    }

    public interface Provider {

        List<Option> getOptions(ShopComponent component, ComponentConfigAccess.CachedField field);

        default boolean applyOption(ShopComponent component, ComponentConfigAccess.CachedField field, Object value) {
            if (component instanceof CatalogComponent catalog) {
                CatalogComponent selected = findCatalogByValue(value);
                if (selected != null) {
                    catalog.setId(selected.getId());
                    catalog.setUuid(selected.getUuid());
                    catalog.setOrder(selected.getOrder());
                    return true;
                }
            }
            return false;
        }
    }

    public record Option(Object value, Component label, @Nullable IGuiTexture icon) {

        public Option(Object value, Component label) {
            this(value, label, null);
        }
    }
}
