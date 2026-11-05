package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.cache.ClientCache;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.preset.ShopOfferPreset;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.preset.ShopOfferPresetManager;
import dev.sixik.sdmshop2.libs.shop.client.ui.root.ShopCatalogElement;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.sdmshop2.libs.shop.register.ShopRegister;
import dev.sixik.unigui.api.widget.Visibility;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ShopCatalogPreviewTest {

    private ShopInstance shop;
    private ShopOffer offer1;
    private ShopOffer offer2;
    private ShopOffer offer3;

    @BeforeAll
    static void initAll() {
        ShopRegister.init();
        Path testCachePath = Path.of("build/tmp/test_client_cache.json");
        ShopClientCache.setCache(ClientCache.load(testCachePath));
    }

    @AfterAll
    static void tearDownAll() {
        ShopClientCache.setCache(null);
    }

    @BeforeEach
    void setUp() {
        shop = ShopInstance.createManager(new ResourceLocation("sdm", "test_shop"), true);
        offer1 = ShopOffer.create(UUID.randomUUID(), false);
        offer1.addComponent(new NameComponent("Offer 1"));
        offer1.addComponent(new CatalogComponent("test_cat"));

        offer2 = ShopOffer.create(UUID.randomUUID(), false);
        offer2.addComponent(new NameComponent("Offer 2"));
        offer2.addComponent(new CatalogComponent("test_cat"));

        offer3 = ShopOffer.create(UUID.randomUUID(), false);
        offer3.addComponent(new NameComponent("Offer 3"));
        offer3.addComponent(new CatalogComponent("test_cat"));

        shop.getEntries().addEntry(offer1);
        shop.getEntries().addEntry(offer2);
        shop.getEntries().addEntry(offer3);

        ShopEditSession session = ShopEditSession.forShop(shop, UUID.randomUUID());
        ShopEditorState.get().startSession(session, shop);
    }

    @Test
    void testOfferReorderingInContainer() {
        List<ShopOffer> offers = new ArrayList<>(shop.getEntries().getEntryMap().values());
        assertEquals(3, offers.size());
        assertEquals(offer1.getUUID(), offers.get(0).getUUID());
        assertEquals(offer2.getUUID(), offers.get(1).getUUID());
        assertEquals(offer3.getUUID(), offers.get(2).getUUID());

        // Перемещаем offer3 на позицию 0 (Move Up / Drag to top)
        boolean moved = ShopEditorState.get().moveOfferToIndex(offer3, 0);
        assertTrue(moved);

        offers = new ArrayList<>(shop.getEntries().getEntryMap().values());
        assertEquals(offer3.getUUID(), offers.get(0).getUUID());
        assertEquals(offer1.getUUID(), offers.get(1).getUUID());
        assertEquals(offer2.getUUID(), offers.get(2).getUUID());

        // Перемещаем offer1 вниз на 1 (Move Down)
        moved = ShopEditorState.get().moveOffer(offer1, 1);
        assertTrue(moved);

        offers = new ArrayList<>(shop.getEntries().getEntryMap().values());
        assertEquals(offer3.getUUID(), offers.get(0).getUUID());
        assertEquals(offer2.getUUID(), offers.get(1).getUUID());
        assertEquals(offer1.getUUID(), offers.get(2).getUUID());
    }

    @Test
    void testDuplicateOffer() {
        int initialCount = shop.getEntries().getEntryMap().size();
        ShopOffer duplicate = ShopEditorState.get().duplicateOffer(offer2);

        assertNotNull(duplicate);
        assertNotEquals(offer2.getUUID(), duplicate.getUUID());
        assertEquals(initialCount + 1, shop.getEntries().getEntryMap().size());

        // Проверяем, что дубликат вставлен сразу после оригинала
        List<ShopOffer> offers = new ArrayList<>(shop.getEntries().getEntryMap().values());
        int offer2Idx = -1;
        int dupIdx = -1;
        for (int i = 0; i < offers.size(); i++) {
            if (offers.get(i).getUUID().equals(offer2.getUUID())) offer2Idx = i;
            if (offers.get(i).getUUID().equals(duplicate.getUUID())) dupIdx = i;
        }
        assertEquals(offer2Idx + 1, dupIdx);

        // Проверяем имя копии
        NameComponent nameComp = duplicate.getComponent(NameComponent.class).orElse(null);
        assertNotNull(nameComp);
        assertTrue(nameComp.getName().contains("(Copy)"));
    }

    @Test
    void testShowFavoriteToggle() {
        ShopCatalogElement card = new ShopCatalogElement(offer1);
        card.showFavorite(false);
        assertFalse(card.isShowFavorite());

        card.showFavorite(true);
        assertTrue(card.isShowFavorite());
    }

    @Test
    void testAddOfferAtBeginningAndEnd() {
        int initialCount = shop.getEntries().getEntryMap().size();
        assertEquals(3, initialCount);

        // Добавляем новый оффер в начало списка (atBeginning = true)
        ShopOffer offerStart = ShopEditorState.get().createNewOffer(true);
        assertNotNull(offerStart);

        List<ShopOffer> offers = new ArrayList<>(shop.getEntries().getEntryMap().values());
        assertEquals(initialCount + 1, offers.size());
        assertEquals(offerStart.getUUID(), offers.get(0).getUUID(), "Offer should be inserted at index 0");

        // Добавляем новый оффер в конец списка (atBeginning = false)
        ShopOffer offerEnd = ShopEditorState.get().createNewOffer(false);
        assertNotNull(offerEnd);

        offers = new ArrayList<>(shop.getEntries().getEntryMap().values());
        assertEquals(initialCount + 2, offers.size());
        assertEquals(offerEnd.getUUID(), offers.get(offers.size() - 1).getUUID(), "Offer should be inserted at end");
    }

    @Test
    void testBuiltInPresets() {
        List<ShopOfferPreset> builtIns = ShopOfferPresetManager.getBuiltInPresets();
        assertFalse(builtIns.isEmpty(), "Built-in presets must not be empty");
        assertTrue(builtIns.size() >= 5, "Should have at least 5 built-in presets");

        for (ShopOfferPreset preset : builtIns) {
            assertTrue(preset.builtIn());
            assertNotNull(preset.name());
            assertFalse(preset.name().isBlank());

            ShopOffer offer = preset.createOffer();
            assertNotNull(offer);
            assertNotNull(offer.getUUID());
            assertTrue(offer.getComponent(NameComponent.class).isPresent());
            assertTrue(offer.getComponent(CatalogComponent.class).isPresent());
        }
    }

    @Test
    void testCustomPresetLifecycle() {
        // 1. Создаем кастомный пресет на основе offer1
        String presetName = "Special Diamond Pack";
        String presetDesc = "Custom test description";
        ShopOfferPreset customPreset = ShopOfferPresetManager.saveOfferAsPreset(presetName, presetDesc, offer1);

        assertNotNull(customPreset);
        assertFalse(customPreset.builtIn());
        assertEquals(presetName, customPreset.name());
        assertEquals(presetDesc, customPreset.description());

        // 2. Проверяем, что он сохранился в кэше
        List<ShopOfferPreset> customPresets = ShopOfferPresetManager.getCustomPresets();
        assertTrue(customPresets.stream().anyMatch(p -> p.id().equals(customPreset.id())));

        // 3. Создаем оффер из кастомного пресета в начало (atBeginning = true)
        int beforeCount = shop.getEntries().getEntryMap().size();
        ShopOffer createdStart = ShopEditorState.get().createOfferFromPreset(customPreset, true);
        assertNotNull(createdStart);
        assertNotEquals(offer1.getUUID(), createdStart.getUUID(), "Created offer must have fresh UUID");

        List<ShopOffer> offers = new ArrayList<>(shop.getEntries().getEntryMap().values());
        assertEquals(beforeCount + 1, offers.size());
        assertEquals(createdStart.getUUID(), offers.get(0).getUUID());

        // 4. Создаем оффер из кастомного пресета в конец (atBeginning = false)
        ShopOffer createdEnd = ShopEditorState.get().createOfferFromPreset(customPreset, false);
        assertNotNull(createdEnd);

        offers = new ArrayList<>(shop.getEntries().getEntryMap().values());
        assertEquals(beforeCount + 2, offers.size());
        assertEquals(createdEnd.getUUID(), offers.get(offers.size() - 1).getUUID());

        // 5. Удаляем кастомный пресет
        boolean deleted = ShopOfferPresetManager.deletePreset(customPreset.id());
        assertTrue(deleted);
        assertFalse(ShopOfferPresetManager.getCustomPresets().stream().anyMatch(p -> p.id().equals(customPreset.id())));
    }

    @Test
    void testContainerAddEntryAt() {
        ShopOffer extra = ShopOffer.create(UUID.randomUUID(), false);
        extra.addComponent(new NameComponent("Extra"));
        extra.addComponent(new CatalogComponent("test_cat"));

        // Вставка в индекс 1
        shop.getEntries().addEntryAt(extra, 1);

        List<ShopOffer> offers = new ArrayList<>(shop.getEntries().getEntryMap().values());
        assertEquals(4, offers.size());
        assertEquals(offer1.getUUID(), offers.get(0).getUUID());
        assertEquals(extra.getUUID(), offers.get(1).getUUID());
        assertEquals(offer2.getUUID(), offers.get(2).getUUID());
        assertEquals(offer3.getUUID(), offers.get(3).getUUID());

        // Перемещение через addEntryAt в индекс 0
        shop.getEntries().addEntryAt(extra, 0);
        offers = new ArrayList<>(shop.getEntries().getEntryMap().values());
        assertEquals(4, offers.size());
        assertEquals(extra.getUUID(), offers.get(0).getUUID());
        assertEquals(offer1.getUUID(), offers.get(1).getUUID());
    }
}
