package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.Nullable;

import java.util.*;

/** Session-scoped commands for currency editing. */
public final class ShopCurrencyEditor {
    /**
     * Binds commands to an editor session.
     * @param state state owning this command service.
     */
    ShopCurrencyEditor(ShopEditorState state) { this.state = state; }

    /** State owning the draft and notifications. */
    private final ShopEditorState state;

    /**
     * Returns pending currency changes for the current session.
     *
     * @return ordered result list; never null.
     */
    public List<CurrencyDraft> getCurrencyDrafts() {
        if (state.getSession() == null) return List.of();
        return Collections.unmodifiableList(state.getSession().currencyDrafts());
    }

    /**
     * Checks whether an identifier refers to the protected default coin currency.
     *
     * @param id currency or category identifier to inspect.
     * @return true when the requested operation or check succeeds.
     */
    public boolean isDefaultCurrency(@Nullable ResourceLocation id) {
        if (id == null) return false;
        return "sdm".equals(id.getNamespace()) && "coin".equals(id.getPath());
    }

    /**
     * Checks whether a currency has a pending deletion.
     *
     * @param id currency or category identifier to inspect.
     * @return true when the requested operation or check succeeds.
     */
    public boolean isCurrencyDeleted(@Nullable ResourceLocation id) {
        if (state.getSession() == null || id == null) return false;
        for (CurrencyDraft d : state.getSession().currencyDrafts()) {
            if (d.kind() == CurrencyDraft.Kind.DELETE && d.id().equals(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether a currency has a pending non-deletion draft.
     *
     * @param id currency or category identifier to inspect.
     * @return true when the requested operation or check succeeds.
     */
    public boolean hasCurrencyDraft(@Nullable ResourceLocation id) {
        if (state.getSession() == null || id == null) return false;
        for (CurrencyDraft d : state.getSession().currencyDrafts()) {
            if (d.kind() != CurrencyDraft.Kind.DELETE && d.id().equals(id)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    /**
     * Finds a pending non-deletion currency draft.
     *
     * @param id currency or category identifier to inspect.
     * @return resulting draft object, or null when no session or source is available.
     */
    public CurrencyDraft findCurrencyDraft(@Nullable ResourceLocation id) {
        if (state.getSession() == null || id == null) return null;
        for (CurrencyDraft d : state.getSession().currencyDrafts()) {
            if (d.kind() != CurrencyDraft.Kind.DELETE && d.id().equals(id)) {
                return d;
            }
        }
        return null;
    }

    /**
     * Returns an existing draft or creates one from the live currency.
     *
     * @param currency source live currency or pending draft.
     * @return resulting draft object, or null when no session or source is available.
     */
    public CurrencyDraft getOrCreateCurrencyDraft(
            dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency currency) {
        if (state.getSession() == null || currency == null) return null;
        CurrencyDraft existing = findCurrencyDraft(currency.getId());
        if (existing != null) return existing;

        CurrencyDraft draft = CurrencyDraft.fromCurrency(currency);
        if (draft == null) {
            draft =
                    CurrencyDraft.text(
                            currency.getId(), currency.getDisplayName().getString(), "$");
        }
        List<CurrencyDraft> list = new ArrayList<>(state.getSession().currencyDrafts());
        list.add(draft);
        state.getSession().setCurrencyDrafts(list);
        state.getSession()
                .recordHistory(
                        "currency.edit",
                        "CurrencyDraft",
                        null,
                        draft.id(),
                        "Started editing currency " + draft.id());
        state.selectTarget(draft);
        state.markDirty();
        return draft;
    }

    /**
     * Replaces a pending currency draft and preserves the selected target.
     *
     * @param oldDraft draft being replaced.
     * @param newDraft replacement draft.
     */
    public void updateCurrencyDraft(CurrencyDraft oldDraft, CurrencyDraft newDraft) {
        if (state.getSession() == null || oldDraft == null || newDraft == null) return;
        List<CurrencyDraft> list = new ArrayList<>(state.getSession().currencyDrafts());
        int idx = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id().equals(oldDraft.id())) {
                idx = i;
                break;
            }
        }
        if (idx >= 0) {
            list.set(idx, newDraft);
        } else {
            list.add(newDraft);
        }
        state.getSession().setCurrencyDrafts(list);
        state.getSession()
                .recordHistory(
                        "currency.update",
                        "CurrencyDraft",
                        null,
                        newDraft.id(),
                        "Updated currency draft " + newDraft.id());
        if (state.getSelectedTarget() == oldDraft
                || (state.getSelectedTarget() instanceof CurrencyDraft cd
                        && cd.id().equals(oldDraft.id()))
                || (state.getSelectedTarget()
                                instanceof dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency ic
                        && ic.getId().equals(oldDraft.id()))) {
            state.setSelectionSilently(newDraft);
        }
        state.markDirty();
    }

    /**
     * Adds and selects a new text currency draft.
     *
     * @return resulting draft object, or null when no session or source is available.
     */
    public CurrencyDraft createNewCurrency() {
        if (state.getSession() == null) {
            return null;
        }

        ResourceLocation id =
                new ResourceLocation("sdm", "currency_" + System.currentTimeMillis() % 10000);
        CurrencyDraft draft = CurrencyDraft.text(id, "New Currency", "$");

        List<CurrencyDraft> list = new ArrayList<>(state.getSession().currencyDrafts());
        list.add(draft);
        state.getSession().setCurrencyDrafts(list);
        state.getSession()
                .recordHistory(
                        "currency.create",
                        "CurrencyDraft",
                        null,
                        id,
                        "Created currency draft " + id);
        state.selectTarget(draft);
        state.markDirty();
        return draft;
    }

    /**
     * Deletes a currency draft and records server deletion when needed.
     *
     * @param currency source live currency or pending draft.
     */
    public void deleteCurrency(CurrencyDraft currency) {
        if (state.getSession() == null || currency == null) {
            return;
        }
        if (isDefaultCurrency(currency.id())) {
            ShopToastHelper.warning(
                    Component.literal("Default currency (sdm:coin) cannot be deleted."));
            return;
        }

        List<CurrencyDraft> list = new ArrayList<>(state.getSession().currencyDrafts());
        list.remove(currency);

        boolean existsOnServer =
                dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient.getAllCurrencies()
                        .containsKey(currency.id());
        if (existsOnServer) {
            list.add(CurrencyDraft.delete(currency.id()));
        }

        state.getSession().setCurrencyDrafts(list);
        state.getSession()
                .recordHistory(
                        "currency.delete",
                        "CurrencyDraft",
                        null,
                        currency.id(),
                        "Deleted currency " + currency.id());
        if (state.getSelectedTarget() == currency) {
            state.setSelectionSilently(null);
        }
        state.markDirty();
        ShopToastHelper.info(Component.literal("Currency deleted"));
    }

    /**
     * Deletes a currency draft and records server deletion when needed.
     *
     * @param currency source live currency or pending draft.
     */
    public void deleteCurrency(dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency currency) {
        if (state.getSession() == null || currency == null) {
            return;
        }
        if (isDefaultCurrency(currency.getId())) {
            ShopToastHelper.warning(
                    Component.literal("Default currency (sdm:coin) cannot be deleted."));
            return;
        }

        List<CurrencyDraft> list = new ArrayList<>(state.getSession().currencyDrafts());
        list.removeIf(d -> d.id().equals(currency.getId()));
        list.add(CurrencyDraft.delete(currency.getId()));
        state.getSession().setCurrencyDrafts(list);
        state.getSession()
                .recordHistory(
                        "currency.delete",
                        "ICurrency",
                        null,
                        currency.getId(),
                        "Marked currency for deletion: " + currency.getId());
        if (state.getSelectedTarget() == currency) {
            state.setSelectionSilently(null);
        }
        state.markDirty();
        ShopToastHelper.info(Component.literal("Currency marked for deletion"));
    }

    /**
     * Creates a text currency draft based on an existing currency.
     *
     * @param currency source live currency or pending draft.
     * @return resulting draft object, or null when no session or source is available.
     */
    public CurrencyDraft duplicateCurrency(CurrencyDraft currency) {
        if (state.getSession() == null || currency == null) {
            return null;
        }

        ResourceLocation oldId = currency.id();
        ResourceLocation newId =
                new ResourceLocation(oldId.getNamespace(), oldId.getPath() + "_copy");
        CurrencyDraft copy =
                CurrencyDraft.text(newId, currency.displayName() + " (Copy)", currency.iconText());

        List<CurrencyDraft> list = new ArrayList<>(state.getSession().currencyDrafts());
        list.add(copy);
        state.getSession().setCurrencyDrafts(list);
        state.getSession()
                .recordHistory(
                        "currency.duplicate",
                        "CurrencyDraft",
                        null,
                        newId,
                        "Duplicated currency " + newId);
        ShopToastHelper.success(Component.literal("Currency draft duplicated"));
        state.selectTarget(copy);
        state.markDirty();
        return copy;
    }

    /**
     * Creates a text currency draft based on an existing currency.
     *
     * @param currency source live currency or pending draft.
     * @return resulting draft object, or null when no session or source is available.
     */
    public CurrencyDraft duplicateCurrency(dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency currency) {
        if (state.getSession() == null || currency == null) {
            return null;
        }

        ResourceLocation oldId = currency.getId();
        ResourceLocation newId =
                new ResourceLocation(
                        oldId != null ? oldId.getNamespace() : "sdm",
                        (oldId != null ? oldId.getPath() : "cur") + "_copy");
        String name =
                currency.getDisplayName() != null
                        ? currency.getDisplayName().getString() + " (Copy)"
                        : "New Currency";
        String symbol = "$";
        try {
            symbol = currency.format(java.math.BigDecimal.ONE).replaceAll("[0-9.,\\s]", "");
            if (symbol.isEmpty()) symbol = "$";
        } catch (Exception ignored) {
        }
        CurrencyDraft copy = CurrencyDraft.text(newId, name, symbol);

        List<CurrencyDraft> list = new ArrayList<>(state.getSession().currencyDrafts());
        list.add(copy);
        state.getSession().setCurrencyDrafts(list);
        state.getSession()
                .recordHistory(
                        "currency.duplicate",
                        "CurrencyDraft",
                        null,
                        newId,
                        "Duplicated currency " + newId);
        ShopToastHelper.success(Component.literal("Currency duplicated as draft"));
        state.selectTarget(copy);
        state.markDirty();
        return copy;
    }
}
