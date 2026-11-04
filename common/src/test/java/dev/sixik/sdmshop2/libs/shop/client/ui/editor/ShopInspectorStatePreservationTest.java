package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class ShopInspectorStatePreservationTest {

    @Test
    void testComponentSectionExpandedStateAndListener() {
        ShopOffer offer = ShopOffer.create(UUID.randomUUID(), false);
        NameComponent nameComp = new NameComponent("Test");
        offer.addComponent(nameComp);

        // По умолчанию секция создается развернутой
        ShopComponentSection section = new ShopComponentSection(offer, nameComp);
        assertTrue(section.isExpanded());

        AtomicBoolean notifiedState = new AtomicBoolean(true);
        section.onExpandChanged(notifiedState::set);

        // Сворачиваем секцию через toggle()
        section.toggle();
        assertFalse(section.isExpanded());
        assertFalse(notifiedState.get());

        // Разворачиваем снова
        section.toggle();
        assertTrue(section.isExpanded());
        assertTrue(notifiedState.get());

        // Создаем секцию с явно указанным свернутым состоянием
        ShopComponentSection collapsedSection = new ShopComponentSection(offer, nameComp, null, false);
        assertFalse(collapsedSection.isExpanded());
    }

    @Test
    void testPreserveComponentCollapsedStateAcrossRebuild() {
        ShopOffer offer = ShopOffer.create(UUID.randomUUID(), false);
        NameComponent nameComp = new NameComponent("Offer Name");
        CatalogComponent catComp = new CatalogComponent("cat_1");
        offer.addComponent(nameComp);
        offer.addComponent(catComp);

        Map<Object, Boolean> expandedMap = new HashMap<>();

        // 1. Создаем секцию для nameComp и сворачиваем ее
        ShopComponentSection sec1 = new ShopComponentSection(offer, nameComp, null,
                expandedMap.getOrDefault(nameComp, true));
        sec1.onExpandChanged(exp -> expandedMap.put(nameComp, exp));
        sec1.expanded(false);

        assertEquals(Boolean.FALSE, expandedMap.get(nameComp));

        // 2. Симулируем перестроение инспектора при добавлении нового компонента
        CatalogComponent newCat = new CatalogComponent("cat_2");
        offer.addComponent(newCat);

        // Воспроизводим секцию nameComp - она должна остаться свернутой
        ShopComponentSection rebuiltSec1 = new ShopComponentSection(offer, nameComp, null,
                expandedMap.getOrDefault(nameComp, true));
        assertFalse(rebuiltSec1.isExpanded(), "Ранее свернутый компонент должен остаться свернутым после перестроения инспектора");

        // Для нового компонента состояние должно быть по умолчанию развернутым
        ShopComponentSection newSec = new ShopComponentSection(offer, newCat, null,
                expandedMap.getOrDefault(newCat, true));
        assertTrue(newSec.isExpanded(), "Новый добавленный компонент должен быть развернут по умолчанию");
    }
}
