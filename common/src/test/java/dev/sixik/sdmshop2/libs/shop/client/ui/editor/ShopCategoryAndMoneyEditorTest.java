package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import static org.junit.jupiter.api.Assertions.*;

import dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyCurrencyRegistry;
import dev.sixik.sdmshop2.libs.sdmeconomy.custom_currency.SimpleTextCurrency;
import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.client.cache.ClientCache;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopItemWidget;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.ShopCategoriesContainerComponent;
import dev.sixik.sdmshop2.libs.shop.components.money.MoneyCostComponent;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.sdmshop2.libs.shop.register.ShopRegister;

import net.minecraft.resources.ResourceLocation;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public class ShopCategoryAndMoneyEditorTest {

    private ShopInstance shop;
    private CatalogComponent cat1;
    private CatalogComponent cat2;
    private CatalogComponent cat3;

    @BeforeAll
    static void initAll() {
        ShopRegister.init();
        Path testCachePath = Path.of("build/tmp/test_client_cache_cat_money.json");
        ShopClientCache.setCache(ClientCache.load(testCachePath));
    }

    @AfterAll
    static void tearDownAll() {
        ShopClientCache.setCache(null);
    }

    @BeforeEach
    void setUp() {
        shop = ShopInstance.createManager(new ResourceLocation("sdm", "test_shop_cat_money"), true);

        cat1 = new CatalogComponent("cat_1");
        cat1.setUuid(UUID.randomUUID());
        cat2 = new CatalogComponent("cat_2");
        cat2.setUuid(UUID.randomUUID());
        cat3 = new CatalogComponent("cat_3");
        cat3.setUuid(UUID.randomUUID());

        ShopCategoriesContainerComponent categories = shop.getCategories();
        categories.addCategory(cat1);
        categories.addCategory(cat2);
        categories.addCategory(cat3);

        ShopEditSession session = ShopEditSession.forShop(shop, UUID.randomUUID());
        ShopEditorState.get().startSession(session, shop);
    }

    @Test
    void testCategoryReorderingAndOrderSync() {
        ShopCategoriesContainerComponent container = shop.getCategories();
        List<CatalogComponent> list = container.getCatalogsComponents();
        assertEquals(3, list.size());
        assertEquals("cat_1", list.get(0).getId());
        assertEquals(0, list.get(0).getOrder());
        assertEquals("cat_2", list.get(1).getId());
        assertEquals(1, list.get(1).getOrder());
        assertEquals("cat_3", list.get(2).getId());
        assertEquals(2, list.get(2).getOrder());

        // Move cat3 to position 0 (Drag & Drop to top or addCard drop)
        boolean moved = ShopEditorState.get().moveCategoryToIndex(cat3, 0);
        assertTrue(moved);

        List<CatalogComponent> reordered = container.getCatalogsComponents();
        assertEquals("cat_3", reordered.get(0).getId());
        assertEquals(0, reordered.get(0).getOrder());
        assertEquals("cat_1", reordered.get(1).getId());
        assertEquals(1, reordered.get(1).getOrder());
        assertEquals("cat_2", reordered.get(2).getId());
        assertEquals(2, reordered.get(2).getOrder());

        // Move cat1 down by offset 1
        boolean movedOffset = ShopEditorState.get().moveCategory(cat1, 1);
        assertTrue(movedOffset);

        List<CatalogComponent> offsetReordered = container.getCatalogsComponents();
        assertEquals("cat_3", offsetReordered.get(0).getId());
        assertEquals("cat_2", offsetReordered.get(1).getId());
        assertEquals("cat_1", offsetReordered.get(2).getId());
        assertEquals(0, offsetReordered.get(0).getOrder());
        assertEquals(1, offsetReordered.get(1).getOrder());
        assertEquals(2, offsetReordered.get(2).getOrder());
    }

    @Test
    void testCategoryDuplicateAndDelete() {
        ShopCategoriesContainerComponent container = shop.getCategories();

        // Duplicate cat1
        CatalogComponent dup = ShopEditorState.get().duplicateCategory(cat1);
        assertNotNull(dup);
        assertEquals("cat_1_copy", dup.getId());
        assertEquals(4, container.getCatalogsComponents().size());

        // Delete duplicated category
        ShopEditorState.get().deleteCategory(dup);
        assertEquals(3, container.getCatalogsComponents().size());

        // Delete cat2
        ShopEditorState.get().deleteCategory(cat2);
        List<CatalogComponent> remaining = container.getCatalogsComponents();
        assertEquals(2, remaining.size());
        assertEquals("cat_1", remaining.get(0).getId());
        assertEquals(0, remaining.get(0).getOrder());
        assertEquals("cat_3", remaining.get(1).getId());
        assertEquals(1, remaining.get(1).getOrder());
    }

    @Test
    void testCategoryMultipleDuplicatesHaveUniqueIds() {
        ShopCategoriesContainerComponent container = shop.getCategories();
        CatalogComponent dup1 = ShopEditorState.get().duplicateCategory(cat1);
        assertNotNull(dup1);
        assertEquals("cat_1_copy", dup1.getId());

        CatalogComponent dup2 = ShopEditorState.get().duplicateCategory(cat1);
        assertNotNull(dup2);
        assertEquals("cat_1_copy_2", dup2.getId());

        // Reindex does not create duplicate categories for matching IDs
        container.reindex();
        long copyCount =
                container.getCatalogsComponents().stream()
                        .filter(c -> "cat_1_copy".equalsIgnoreCase(c.getId()))
                        .count();
        assertEquals(1, copyCount);
    }

    @Test
    void testDefaultCurrencyCannotBeDeleted() {
        ResourceLocation coinId = new ResourceLocation("sdm", "coin");
        assertTrue(ShopEditorState.get().isDefaultCurrency(coinId));

        CurrencyDraft coinDraft = CurrencyDraft.text(coinId, "Coin", "$");
        ShopEditorState.get().deleteCurrency(coinDraft);

        assertFalse(ShopEditorState.get().isCurrencyDeleted(coinId));
    }

    @Test
    void testLocalCurrencyDraftCreationAndDeletion() {
        CurrencyDraft newDraft = ShopEditorState.get().createNewCurrency();
        assertNotNull(newDraft);
        assertTrue(ShopEditorState.get().getSession().currencyDrafts().contains(newDraft));
        assertFalse(ShopEditorState.get().isCurrencyDeleted(newDraft.id()));

        // Deleting a local draft removes it without creating a DELETE kind draft
        ShopEditorState.get().deleteCurrency(newDraft);
        assertFalse(ShopEditorState.get().getSession().currencyDrafts().contains(newDraft));
        assertFalse(ShopEditorState.get().isCurrencyDeleted(newDraft.id()));
    }

    @Test
    void testServerCurrencyDeletionQueuesDeleteDraft() {
        ResourceLocation customId = new ResourceLocation("sdm", "silver_coins");
        SimpleTextCurrency serverCurrency = new SimpleTextCurrency(customId, "Silver Coins", "S");
        SDMEconomyCurrencyRegistry.registerStoredCurrency(serverCurrency);

        assertFalse(ShopEditorState.get().isDefaultCurrency(customId));
        assertFalse(ShopEditorState.get().isCurrencyDeleted(customId));

        // Delete the server currency
        ShopEditorState.get().deleteCurrency(serverCurrency);

        assertTrue(ShopEditorState.get().isCurrencyDeleted(customId));
        boolean hasDeleteDraft =
                ShopEditorState.get().getSession().currencyDrafts().stream()
                        .anyMatch(
                                d ->
                                        d.kind() == CurrencyDraft.Kind.DELETE
                                                && d.id().equals(customId));
        assertTrue(hasDeleteDraft);
    }

    @Test
    void testCurrencyEditingPromotesToDraftAndUpdates() {
        ResourceLocation bronzeId = new ResourceLocation("sdm", "bronze_coins");
        SimpleTextCurrency bronzeCurrency = new SimpleTextCurrency(bronzeId, "Bronze Coins", "B");
        SDMEconomyCurrencyRegistry.registerStoredCurrency(bronzeCurrency);

        // Not yet a draft
        assertFalse(ShopEditorState.get().hasCurrencyDraft(bronzeId));

        // Promote to draft
        CurrencyDraft draft = ShopEditorState.get().getOrCreateCurrencyDraft(bronzeCurrency);
        assertNotNull(draft);
        assertEquals(bronzeId, draft.id());
        assertEquals("Bronze Coins", draft.displayName());
        assertTrue(ShopEditorState.get().hasCurrencyDraft(bronzeId));

        // Update draft
        CurrencyDraft updated =
                new CurrencyDraft(bronzeId, CurrencyDraft.Kind.TEXT, "Updated Bronze", "UB", null);
        ShopEditorState.get().updateCurrencyDraft(draft, updated);

        CurrencyDraft fetched = ShopEditorState.get().findCurrencyDraft(bronzeId);
        assertNotNull(fetched);
        assertEquals("Updated Bronze", fetched.displayName());
        assertEquals("UB", fetched.iconText());
    }

    @Test
    void testSequentialCurrencyDraftFieldUpdatesPreserveValues() {
        CurrencyDraft draft = ShopEditorState.get().createNewCurrency();
        assertNotNull(draft);
        ResourceLocation id = draft.id();

        // 1. Update display name
        CurrencyDraft step1 =
                new CurrencyDraft(
                        id, draft.kind(), "Renamed Currency", draft.iconText(), draft.itemStack());
        ShopEditorState.get().updateCurrencyDraft(draft, step1);

        CurrencyDraft fetched1 = ShopEditorState.get().findCurrencyDraft(id);
        assertNotNull(fetched1);
        assertEquals("Renamed Currency", fetched1.displayName());

        // 2. Update symbol using latest draft
        CurrencyDraft step2 =
                new CurrencyDraft(
                        id, fetched1.kind(), fetched1.displayName(), "€", fetched1.itemStack());
        ShopEditorState.get().updateCurrencyDraft(fetched1, step2);

        CurrencyDraft fetched2 = ShopEditorState.get().findCurrencyDraft(id);
        assertNotNull(fetched2);
        assertEquals("Renamed Currency", fetched2.displayName());
        assertEquals("€", fetched2.iconText());

        // 3. Switch to ITEM kind with diamond
        net.minecraft.world.item.ItemStack diamond =
                net.minecraft.world.item.Items.DIAMOND.getDefaultInstance();
        CurrencyDraft step3 =
                new CurrencyDraft(
                        id,
                        CurrencyDraft.Kind.ITEM,
                        fetched2.displayName(),
                        fetched2.iconText(),
                        diamond);
        ShopEditorState.get().updateCurrencyDraft(fetched2, step3);

        CurrencyDraft fetched3 = ShopEditorState.get().findCurrencyDraft(id);
        assertNotNull(fetched3);
        assertEquals(CurrencyDraft.Kind.ITEM, fetched3.kind());
        assertEquals("Renamed Currency", fetched3.displayName());
        assertEquals(diamond.getItem(), fetched3.itemStack().getItem());
    }

    @Test
    void testOfferCostResolvesCurrencyDraftVisual() {
        CurrencyDraft draft = ShopEditorState.get().createNewCurrency();
        assertNotNull(draft);
        net.minecraft.world.item.ItemStack emerald =
                net.minecraft.world.item.Items.EMERALD.getDefaultInstance();
        CurrencyDraft itemDraft =
                new CurrencyDraft(
                        draft.id(), CurrencyDraft.Kind.ITEM, "Emerald Money", "", emerald);
        ShopEditorState.get().updateCurrencyDraft(draft, itemDraft);

        MoneyCostComponent costComp = new MoneyCostComponent(draft.id(), 10.0);

        dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopCatalogElement.CostEntry costEntry =
                dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopCatalogElement.resolveCostEntry(
                        costComp);

        assertNotNull(costEntry);
        assertEquals("10", costEntry.amount());
        // The currency component owns its visual; the DTO no longer reduces it to an item icon.
        var rendered =
                new dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopComponentPanel(
                        List.of(costEntry::createRender));
        var row = (dev.sixik.unigui.widgets.containers.PanelWidget) rendered.children().get(0);
        row.applyQueuedMutations();
        row = (dev.sixik.unigui.widgets.containers.PanelWidget) row.children().get(0);
        row.applyQueuedMutations();
        var icon = (ShopItemWidget) row.children().get(0);
        assertEquals(emerald.getItem(), icon.item().getItem());
        rendered.dispose();
    }
}
