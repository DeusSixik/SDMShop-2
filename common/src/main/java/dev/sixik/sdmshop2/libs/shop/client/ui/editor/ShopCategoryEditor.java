package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

import java.util.*;

/** Session-scoped commands for category editing. */
public final class ShopCategoryEditor {
    /**
     * Binds commands to an editor session.
     * @param state state owning this command service.
     */
    ShopCategoryEditor(ShopEditorState state) { this.state = state; }

    /** State owning the draft and notifications. */
    private final ShopEditorState state;

    /**
     * Adds a new category to the draft and selects it.
     *
     * @return resulting draft object, or null when no session or source is available.
     */
    public CatalogComponent createNewCategory() {
        if (state.editableShop() == null || state.getSession() == null) {
            return null;
        }

        String catId = "cat_" + System.currentTimeMillis() % 10000;
        CatalogComponent cat = new CatalogComponent(catId);
        cat.setIconType(CatalogComponent.IconType.ITEM);
        cat.setIconItem(Items.CHEST.getDefaultInstance());

        state.editableShop().getCategories().addCategory(cat);
        state.getSession()
                .recordHistory(
                        "category.create",
                        "CatalogComponent",
                        cat.getUuid(),
                        cat.getType().getId(),
                        "Created category " + catId);
        state.selectTarget(cat);
        state.markDirty();
        return cat;
    }

    /**
     * Deletes a category and reassigns affected offers to a fallback category.
     *
     * @param cat category belonging to the draft shop.
     */
    public void deleteCategory(CatalogComponent cat) {
        if (state.editableShop() == null || state.getSession() == null || cat == null) {
            return;
        }

        UUID catUuid = cat.getUuid();
        String catId = cat.getId();

        state.editableShop().getCategories().removeCategory(catUuid);

        if (state.editableShop().getEntries() != null) {
            String fallbackCatId = "resources";
            UUID fallbackCatUuid = null;
            if (state.editableShop().getCategories() != null
                    && !state.editableShop().getCategories().getCatalogsComponents().isEmpty()) {
                CatalogComponent fallback =
                        state.editableShop().getCategories().getCatalogsComponents().get(0);
                fallbackCatId = fallback.getId();
                fallbackCatUuid = fallback.getUuid();
            }

            for (ShopOffer offer : state.editableShop().getEntries().getEntryMap().values()) {
                CatalogComponent oCat = offer.getComponent(CatalogComponent.class).orElse(null);
                if (oCat != null) {
                    boolean match =
                            (catUuid != null && catUuid.equals(oCat.getUuid()))
                                    || (catId != null
                                            && !catId.isBlank()
                                            && catId.equalsIgnoreCase(oCat.getId()));
                    if (match) {
                        oCat.setId(fallbackCatId);
                        oCat.setUuid(fallbackCatUuid != null ? fallbackCatUuid : UUID.randomUUID());
                    }
                }
            }
        }

        state.editableShop().getCategories().reindex();
        state.getSession()
                .recordHistory(
                        "category.delete",
                        "CatalogComponent",
                        catUuid,
                        cat.getType().getId(),
                        "Deleted category " + catId);
        if (state.getSelectedTarget() == cat) {
            state.setSelectionSilently(null);
        }
        state.markDirty();
    }

    /**
     * Copies a category using a unique category identifier.
     *
     * @param cat category belonging to the draft shop.
     * @return resulting draft object, or null when no session or source is available.
     */
    public CatalogComponent duplicateCategory(CatalogComponent cat) {
        if (state.editableShop() == null || state.getSession() == null || cat == null) {
            return null;
        }

        CatalogComponent copy = CatalogComponent.copyOf(cat);
        copy.setUuid(UUID.randomUUID());
        String baseId = cat.getId() != null ? cat.getId() : "cat";
        String copyId = baseId + "_copy";
        int suffix = 2;
        while (isCategoryIdTaken(copyId)) {
            copyId = baseId + "_copy_" + suffix++;
        }
        copy.setId(copyId);

        state.editableShop().getCategories().addCategory(copy);
        state.getSession()
                .recordHistory(
                        "category.duplicate",
                        "CatalogComponent",
                        copy.getUuid(),
                        copy.getType().getId(),
                        "Duplicated category " + copy.getId());
        ShopToastHelper.success(Component.literal("Category duplicated"));
        state.selectTarget(copy);
        state.markDirty();
        return copy;
    }

    /**
     * Checks category identifiers without case sensitivity.
     *
     * @param id currency or category identifier to inspect.
     * @return true when the requested operation or check succeeds.
     */
    private boolean isCategoryIdTaken(String id) {
        if (id == null
                || state.editableShop() == null
                || state.editableShop().getCategories() == null) return false;
        for (CatalogComponent c : state.editableShop().getCategories().getCatalogsComponents()) {
            if (id.equalsIgnoreCase(c.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Moves a category relative to its current position.
     *
     * @param cat category belonging to the draft shop.
     * @param offset signed relative index change; negative moves toward the beginning.
     * @return true when the requested operation or check succeeds.
     */
    public boolean moveCategory(CatalogComponent cat, int offset) {
        if (state.editableShop() == null
                || state.getSession() == null
                || cat == null
                || state.editableShop().getCategories() == null) {
            return false;
        }
        boolean moved = state.editableShop().getCategories().moveCategory(cat.getUuid(), offset);
        if (moved) {
            state.getSession()
                    .recordHistory(
                            "category.move",
                            "CatalogComponent",
                            cat.getUuid(),
                            null,
                            "Moved category offset " + offset);
            state.markDirty();
            return true;
        }
        return false;
    }

    /**
     * Moves a category to an absolute position.
     *
     * @param cat category belonging to the draft shop.
     * @param targetIndex absolute zero-based destination index.
     * @return true when the requested operation or check succeeds.
     */
    public boolean moveCategoryToIndex(CatalogComponent cat, int targetIndex) {
        if (state.editableShop() == null
                || state.getSession() == null
                || cat == null
                || state.editableShop().getCategories() == null) {
            return false;
        }
        boolean moved =
                state.editableShop()
                        .getCategories()
                        .moveCategoryToIndex(cat.getUuid(), targetIndex);
        if (moved) {
            state.getSession()
                    .recordHistory(
                            "category.reorder",
                            "CatalogComponent",
                            cat.getUuid(),
                            null,
                            "Reordered category to index " + targetIndex);
            state.markDirty();
            return true;
        }
        return false;
    }

    // --- Управление валютами (Currencies) ---
}
