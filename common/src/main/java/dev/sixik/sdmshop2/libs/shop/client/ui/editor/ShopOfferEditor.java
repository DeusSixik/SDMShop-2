package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.preset.ShopOfferPreset;
import dev.sixik.sdmshop2.libs.shop.components.ItemRewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponentRegistry;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import dev.sixik.sdmshop2.libs.shop.components.money.MoneyCostComponent;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.*;

/** Session-scoped commands for offer editing. */
public final class ShopOfferEditor {
    /**
     * Binds commands to an editor session.
     * @param state state owning this command service.
     */
    ShopOfferEditor(ShopEditorState state) { this.state = state; }

    /** State owning the session and change notifications. */
    private final ShopEditorState state;

    /** Components protected in their original offer, tracked by identity. */
    private final Set<ShopComponent> defaultComponents =
            Collections.newSetFromMap(new IdentityHashMap<>());

    /** Offers whose original components have been recorded. */
    private final Set<UUID> initializedOfferDefaults = new HashSet<>();

    /** Resets protected component tracking for a replaced session. */
    public void resetDefaults() {
        defaultComponents.clear();
        initializedOfferDefaults.clear();
    }

    /**
     * Creates a draft offer with default name, category, reward and cost components.
     *
     * @return resulting draft object, or null when no session or source is available.
     */
    public ShopOffer createNewOffer() {
        return createNewOffer(false);
    }

    /**
     * Creates a draft offer with default name, category, reward and cost components.
     *
     * @param atBeginning true to insert before the existing offers.
     * @return resulting draft object, or null when no session or source is available.
     */
    public ShopOffer createNewOffer(boolean atBeginning) {
        if (state.editableShop() == null || state.getSession() == null) {
            return null;
        }

        UUID offerId = UUID.randomUUID();
        ShopOffer offer = ShopOffer.create(offerId, false);

        NameComponent nameComp = new NameComponent("New Offer");
        offer.addComponent(nameComp);

        // Категория по умолчанию
        String defaultCatId = "resources";
        if (state.editableShop().getCategories() != null
                && !state.editableShop().getCategories().getCatalogsComponents().isEmpty()) {
            defaultCatId =
                    state.editableShop().getCategories().getCatalogsComponents().get(0).getId();
        }
        CatalogComponent catalogComp = new CatalogComponent(defaultCatId);
        catalogComp.setIconType(CatalogComponent.IconType.ITEM);
        catalogComp.setIconItem(Items.DIAMOND.getDefaultInstance());
        offer.addComponent(catalogComp);

        // Регистрируем обязательные базовые компоненты как не удаляемые по умолчанию
        initializedOfferDefaults.add(offerId);
        defaultComponents.add(nameComp);
        defaultComponents.add(catalogComp);

        // Награда по умолчанию
        offer.addComponent(new ItemRewardComponent(Items.DIAMOND, 1));

        // Цена по умолчанию
        ResourceLocation defaultMoney =
                SDMEconomyServiceClient.getAllCurrencies().isEmpty()
                        ? new ResourceLocation("sdm", "coin")
                        : SDMEconomyServiceClient.getAllCurrencies()
                                .values()
                                .iterator()
                                .next()
                                .getId();
        offer.addComponent(new MoneyCostComponent(defaultMoney, 10.0));

        if (atBeginning) {
            state.editableShop().getEntries().addEntryAt(offer, 0);
        } else {
            state.editableShop().getEntries().addEntry(offer);
        }
        state.getSession()
                .recordHistory(
                        "offer.create", "ShopOffer", offerId, null, "Created offer " + offerId);
        state.selectTarget(offer);
        state.markDirty();
        return offer;
    }

    /**
     * Creates a draft offer from a saved preset.
     *
     * @param preset saved offer preset to instantiate.
     * @param atBeginning true to insert before the existing offers.
     * @return resulting draft object, or null when no session or source is available.
     */
    public ShopOffer createOfferFromPreset(ShopOfferPreset preset, boolean atBeginning) {
        if (state.editableShop() == null || state.getSession() == null || preset == null) {
            return null;
        }

        ShopOffer offer = preset.createOffer();
        if (offer == null) return null;

        if (atBeginning) {
            state.editableShop().getEntries().addEntryAt(offer, 0);
        } else {
            state.editableShop().getEntries().addEntry(offer);
        }

        ensureOfferDefaultsTracked(offer);
        state.getSession()
                .recordHistory(
                        "offer.create_preset",
                        "ShopOffer",
                        offer.getUUID(),
                        null,
                        "Created offer from preset: " + preset.name());
        state.selectTarget(offer);
        state.markDirty();
        ShopToastHelper.success(Component.literal("Offer created from preset: " + preset.name()));
        return offer;
    }

    /**
     * Removes an offer from the draft and records the change.
     *
     * @param offer draft offer to inspect or change; null makes editing commands a no-op.
     */
    public void deleteOffer(ShopOffer offer) {
        if (state.editableShop() == null || state.getSession() == null || offer == null) {
            return;
        }

        state.editableShop().getEntries().removeEntry(offer.getUUID());
        state.getSession()
                .recordHistory(
                        "offer.delete",
                        "ShopOffer",
                        offer.getUUID(),
                        null,
                        "Deleted offer " + offer.getUUID());
        if (state.getSelectedTarget() == offer) {
            state.setSelectionSilently(null);
        }
        state.markDirty();
    }

    /**
     * Moves an offer relative to its current position.
     *
     * @param offer draft offer to inspect or change; null makes editing commands a no-op.
     * @param offset signed relative index change; negative moves toward the beginning.
     * @return true when the requested operation or check succeeds.
     */
    public boolean moveOffer(ShopOffer offer, int offset) {
        if (state.editableShop() == null
                || state.getSession() == null
                || offer == null
                || state.editableShop().getEntries() == null) {
            return false;
        }
        boolean moved = state.editableShop().getEntries().moveEntry(offer.getUUID(), offset);
        if (moved) {
            state.getSession()
                    .recordHistory(
                            "offer.move",
                            "ShopOffer",
                            offer.getUUID(),
                            null,
                            "Moved offer offset " + offset);
            state.markDirty();
            return true;
        }
        return false;
    }

    /**
     * Moves an offer to an absolute position in the draft.
     *
     * @param offer draft offer to inspect or change; null makes editing commands a no-op.
     * @param targetIndex absolute zero-based destination index.
     * @return true when the requested operation or check succeeds.
     */
    public boolean moveOfferToIndex(ShopOffer offer, int targetIndex) {
        if (state.editableShop() == null
                || state.getSession() == null
                || offer == null
                || state.editableShop().getEntries() == null) {
            return false;
        }
        boolean moved =
                state.editableShop().getEntries().moveEntryToIndex(offer.getUUID(), targetIndex);
        if (moved) {
            state.getSession()
                    .recordHistory(
                            "offer.reorder",
                            "ShopOffer",
                            offer.getUUID(),
                            null,
                            "Reordered offer to index " + targetIndex);
            state.markDirty();
            return true;
        }
        return false;
    }

    /**
     * Clones an offer and inserts it after the original.
     *
     * @param offer draft offer to inspect or change; null makes editing commands a no-op.
     * @return resulting draft object, or null when no session or source is available.
     */
    public ShopOffer duplicateOffer(ShopOffer offer) {
        if (state.editableShop() == null
                || state.getSession() == null
                || offer == null
                || state.editableShop().getEntries() == null) {
            return null;
        }
        try {
            com.google.gson.JsonObject json = offer.serialize().getAsJsonObject();
            UUID newUuid = UUID.randomUUID();
            json.addProperty("uuid", newUuid.toString());
            ShopOffer clone = ShopOffer.create(newUuid, false);
            clone.deserialize(json);

            NameComponent nameComp = clone.getComponent(NameComponent.class).orElse(null);
            if (nameComp != null) {
                try {
                    var f = NameComponent.class.getDeclaredField("name");
                    f.setAccessible(true);
                    f.set(nameComp, nameComp.getName() + " (Copy)");
                } catch (Exception ignored) {
                }
            }

            state.editableShop().getEntries().addEntryAfter(clone, offer.getUUID());
            ensureOfferDefaultsTracked(clone);
            state.getSession()
                    .recordHistory(
                            "offer.duplicate",
                            "ShopOffer",
                            newUuid,
                            null,
                            "Duplicated offer " + offer.getUUID());
            state.selectTarget(clone);
            state.markDirty();
            ShopToastHelper.success(Component.literal("Offer duplicated"));
            return clone;
        } catch (Exception e) {
            e.printStackTrace();
            ShopToastHelper.error(
                    Component.literal("Failed to duplicate offer: " + e.getMessage()));
            return null;
        }
    }

    // --- Управление компонентами оффера ---

    /**
     * Adds a component of the requested type to an offer.
     *
     * @param offer draft offer to inspect or change; null makes editing commands a no-op.
     * @param type component type to instantiate.
     */
    public void addComponent(ShopOffer offer, IComponentType<?> type) {
        if (offer == null || type == null || state.getSession() == null) {
            return;
        }

        ShopComponent component = type.createDefault();
        offer.addComponent(component);
        state.getSession()
                .recordHistory(
                        "component.add",
                        "ShopOffer",
                        offer.getUUID(),
                        type.getId(),
                        "Added component " + type.getId());
        state.markDirty();
    }

    /**
     * Records the original mandatory components once per offer.
     *
     * @param offer draft offer to inspect or change; null makes editing commands a no-op.
     */
    public void ensureOfferDefaultsTracked(ShopOffer offer) {
        if (offer == null || !initializedOfferDefaults.add(offer.getUUID())) {
            return;
        }
        for (ShopComponent c : offer.getComponents()) {
            if (c instanceof CatalogComponent || c instanceof NameComponent) {
                defaultComponents.add(c);
            }
        }
    }

    /**
     * Checks whether a component belongs to the original mandatory set.
     *
     * @param offer draft offer to inspect or change; null makes editing commands a no-op.
     * @param component component belonging to the supplied offer.
     * @return true when the requested operation or check succeeds.
     */
    public boolean isDefaultComponent(ShopOffer offer, ShopComponent component) {
        if (offer == null || component == null) return false;
        ensureOfferDefaultsTracked(offer);
        return defaultComponents.contains(component);
    }

    /**
     * Checks whether deletion would preserve the mandatory offer components.
     *
     * @param offer draft offer to inspect or change; null makes editing commands a no-op.
     * @param component component belonging to the supplied offer.
     * @return true when the requested operation or check succeeds.
     */
    public boolean canDeleteComponent(ShopOffer offer, ShopComponent component) {
        if (offer == null || component == null) return false;
        // Компоненты по умолчанию sdm:catalog и sdm:name нельзя удалять.
        // Но если через редактор добавлены новые или дубликаты, их удалять можно!
        return !isDefaultComponent(offer, component);
    }

    /**
     * Removes an unprotected component and records the change.
     *
     * @param offer draft offer to inspect or change; null makes editing commands a no-op.
     * @param component component belonging to the supplied offer.
     */
    public void removeComponent(ShopOffer offer, ShopComponent component) {
        if (offer == null || component == null || state.getSession() == null) {
            return;
        }

        if (!canDeleteComponent(offer, component)) {
            ShopToastHelper.warning(
                    Component.literal(
                            "Default components (sdm:catalog, sdm:name) cannot be removed."));
            return;
        }

        defaultComponents.remove(component);
        offer.removeComponent(component);
        state.getSession()
                .recordHistory(
                        "component.delete",
                        "ShopOffer",
                        offer.getUUID(),
                        component.getType() != null ? component.getType().getId() : null,
                        "Removed component");
        state.markDirty();
    }

    /**
     * Moves a component relative to its current position.
     *
     * @param offer draft offer to inspect or change; null makes editing commands a no-op.
     * @param component component belonging to the supplied offer.
     * @param offset signed relative index change; negative moves toward the beginning.
     * @return true when the requested operation or check succeeds.
     */
    public boolean moveComponent(ShopOffer offer, ShopComponent component, int offset) {
        if (offer == null || component == null || state.getSession() == null) {
            return false;
        }

        if (offer.moveComponent(component, offset)) {
            state.getSession()
                    .recordHistory(
                            "component.move",
                            "ShopOffer",
                            offer.getUUID(),
                            component.getType() != null ? component.getType().getId() : null,
                            "Moved component offset " + offset);
            state.markDirty();
            return true;
        }
        return false;
    }

    /**
     * Moves a component to an absolute position.
     *
     * @param offer draft offer to inspect or change; null makes editing commands a no-op.
     * @param component component belonging to the supplied offer.
     * @param targetIndex absolute zero-based destination index.
     * @return true when the requested operation or check succeeds.
     */
    public boolean moveComponentToIndex(ShopOffer offer, ShopComponent component, int targetIndex) {
        if (offer == null || component == null || state.getSession() == null) {
            return false;
        }

        if (offer.moveComponentToIndex(component, targetIndex)) {
            state.getSession()
                    .recordHistory(
                            "component.reorder",
                            "ShopOffer",
                            offer.getUUID(),
                            component.getType() != null ? component.getType().getId() : null,
                            "Reordered component to index " + targetIndex);
            state.markDirty();
            return true;
        }
        return false;
    }

    /**
     * Clones a component and inserts it after the original.
     *
     * @param offer draft offer to inspect or change; null makes editing commands a no-op.
     * @param component component belonging to the supplied offer.
     * @return resulting draft object, or null when no session or source is available.
     */
    public ShopComponent duplicateComponent(ShopOffer offer, ShopComponent component) {
        if (offer == null || component == null || state.getSession() == null) {
            return null;
        }

        try {
            com.google.gson.JsonObject json = ShopComponentRegistry.toJson(component);
            ShopComponent clone = ShopComponentRegistry.fromJson(json);
            offer.addComponent(clone);
            offer.moveComponentAfter(clone, component);
            // Дубликат не является компонентом по умолчанию и может быть удален
            defaultComponents.remove(clone);
            state.getSession()
                    .recordHistory(
                            "component.duplicate",
                            "ShopOffer",
                            offer.getUUID(),
                            component.getType() != null ? component.getType().getId() : null,
                            "Duplicated component");
            state.markDirty();
            ShopToastHelper.success(Component.literal("Component duplicated"));
            return clone;
        } catch (Exception e) {
            e.printStackTrace();
            ShopToastHelper.error(
                    Component.literal("Failed to duplicate component: " + e.getMessage()));
            return null;
        }
    }

    // --- Управление категориями (Categories) ---
}
