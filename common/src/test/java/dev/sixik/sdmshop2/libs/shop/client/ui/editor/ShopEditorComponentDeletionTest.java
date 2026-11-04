package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ShopEditorComponentDeletionTest {

    @Test
    void defaultComponentsCannotBeDeletedButNewlyAddedOnesCan() {
        ShopOffer offer = ShopOffer.create(UUID.randomUUID(), false);
        NameComponent defaultName = new NameComponent("Initial Name");
        CatalogComponent defaultCatalog = new CatalogComponent("initial_cat");

        offer.addComponent(defaultName);
        offer.addComponent(defaultCatalog);

        ShopEditorState editorState = ShopEditorState.get();
        editorState.ensureOfferDefaultsTracked(offer);

        // Базовые компоненты по умолчанию нельзя удалить
        assertFalse(editorState.canDeleteComponent(offer, defaultName));
        assertFalse(editorState.canDeleteComponent(offer, defaultCatalog));

        // Добавляем новые компоненты таких же типов через редактор
        NameComponent addedName = new NameComponent("Added Name");
        CatalogComponent addedCatalog = new CatalogComponent("added_cat");
        offer.addComponent(addedName);
        offer.addComponent(addedCatalog);

        // Новые добавленные компоненты можно удалить
        assertTrue(editorState.canDeleteComponent(offer, addedName));
        assertTrue(editorState.canDeleteComponent(offer, addedCatalog));

        // Старые компоненты по-прежнему нельзя удалить
        assertFalse(editorState.canDeleteComponent(offer, defaultName));
        assertFalse(editorState.canDeleteComponent(offer, defaultCatalog));
    }
}
