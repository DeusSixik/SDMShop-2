package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/** Catalog filtering with deterministic, stable favorite-first ordering. */
public final class ShopCatalogQuery {
    /** Prevents instantiation. */
    private ShopCatalogQuery() {}

    /**
     * Selects matching items without modifying the source list.
     *
     * @param items presentation snapshots in their original order.
     * @param category selected category, null or ALL for every category.
     * @param search search text, matched against titles, reward names and registry identifiers.
     * @param favoritesFirst whether favorite items precede other items, preserving order within
     *     each group.
     * @param <T> catalog snapshot type.
     * @return independent mutable result list.
     */
    public static <T extends ShopCatalogItem> List<T> select(
            List<T> items, String category, String search, boolean favoritesFirst) {
        String query = search != null ? search.trim().toLowerCase(Locale.ROOT) : "";
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (category != null
                    && !"ALL".equalsIgnoreCase(category)
                    && !category.equalsIgnoreCase(item.categoryId)) continue;
            if (matches(item, query)) result.add(item);
        }
        if (favoritesFirst)
            result.sort(Comparator.comparing(ShopCatalogItem::isFavorite).reversed());
        return result;
    }

    /**
     * Matches a snapshot against an already normalized search string.
     *
     * @param item snapshot to search.
     * @param query trimmed lowercase search string.
     * @return true if the title or any reward matches.
     */
    private static boolean matches(ShopCatalogItem item, String query) {
        if (query.isEmpty()) return true;
        if (item.title != null && item.title.toLowerCase(Locale.ROOT).contains(query)) return true;
        if (item.offer == null) return false;
        for (ItemStack reward : ShopOfferPresentation.resolveRewardItems(item.offer)) {
            if (reward.getHoverName().getString().toLowerCase(Locale.ROOT).contains(query))
                return true;
            if (BuiltInRegistries.ITEM
                    .getKey(reward.getItem())
                    .toString()
                    .toLowerCase(Locale.ROOT)
                    .contains(query)) return true;
        }
        return false;
    }
}
