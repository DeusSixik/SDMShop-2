package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShopOffersPanelElementTest {
    @Test
    void columnsForWidth_desktopWidth_usesThreeColumns() {
        assertEquals(3, ShopOffersPanelElement.columnsForWidth(900.0f));
    }

    @Test
    void columnsForWidth_mediumWidth_usesTwoColumns() {
        assertEquals(2, ShopOffersPanelElement.columnsForWidth(500.0f));
    }

    @Test
    void columnsForWidth_narrowWidth_usesSingleColumn() {
        assertEquals(1, ShopOffersPanelElement.columnsForWidth(320.0f));
    }
}
