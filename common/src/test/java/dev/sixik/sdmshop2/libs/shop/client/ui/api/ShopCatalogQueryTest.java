package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShopCatalogQueryTest {
    @Test void select_categoryAndSearch_areCaseInsensitiveAndTrimmed() {
        ShopCatalogItem a = item("Diamond", "resources", false);
        ShopCatalogItem b = item("Diamond Sword", "weapons", true);
        assertEquals(List.of(a), ShopCatalogQuery.select(List.of(a, b), "RESOURCES", "  DIA  ", false));
    }

    @Test void select_favoritesFirst_isStableAndDoesNotMutateInput() {
        ShopCatalogItem a = item("a", "all", false);
        ShopCatalogItem b = item("b", "all", true);
        ShopCatalogItem c = item("c", "all", true);
        ShopCatalogItem d = item("d", "all", false);
        List<ShopCatalogItem> source = List.of(a, b, c, d);
        assertEquals(List.of(b, c, a, d), ShopCatalogQuery.select(source, null, null, true));
        assertEquals(List.of(a, b, c, d), source);
    }

    @Test void costGroup_sourceListMutated_keepsItsSnapshot() {
        List<ShopOfferView.CostEntry> costs = new ArrayList<>();
        costs.add(new ShopOfferView.CostEntry("$", "1"));
        ShopOfferView.CostGroup group = new ShopOfferView.CostGroup(null, costs);
        costs.clear();
        assertEquals("", group.groupId());
        assertEquals(1, group.costs().size());
        assertThrows(UnsupportedOperationException.class, () -> group.costs().clear());
    }

    private static ShopCatalogItem item(String title, String category, boolean favorite) {
        return new ShopCatalogItem(UUID.randomUUID(), category, title, List.of(), List.of(), 0) {
            @Override public boolean isFavorite() { return favorite; }
        };
    }
}
