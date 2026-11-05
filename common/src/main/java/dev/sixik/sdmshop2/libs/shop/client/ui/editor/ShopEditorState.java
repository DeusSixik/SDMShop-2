package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft;
import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.SDMShopClient;
import dev.sixik.sdmshop2.libs.shop.client.cache.PersistentEditSession;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopScreenController;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUiSubscription;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.preset.ShopOfferPreset;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.sdmshop2.libs.shop.network.ShopNetworkManager;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Глобальный диспетчер состояния внутриигрового редактора магазина SDMShop-2. Управляет сессией
 * редактирования (ShopEditSession / draftShop), активной вкладкой (Offers / Categories /
 * Currencies) и выбранным объектом для инспектора.
 */
public final class ShopEditorState {

    public enum EditorTab {
        OFFERS,
        CATEGORIES,
        CURRENCIES
    }

    private static final ShopEditorState INSTANCE = new ShopEditorState();

    public static ShopEditorState get() {
        return INSTANCE;
    }

    private boolean editorMode = false;
    private @Nullable ShopEditSession session = null;
    private @Nullable ShopInstance draftShop = null;
    private EditorTab activeTab = EditorTab.OFFERS;
    private @Nullable Object selectedTarget = null;
    private long revision = 0;

    private final List<Runnable> listeners = new CopyOnWriteArrayList<>();

    /** Commands bound to this editor state. */
    private final ShopOfferEditor offerEditor = new ShopOfferEditor(this);

    /**
     * Returns session-scoped editing commands.
     *
     * @return commands for this state.
     */
    public ShopOfferEditor offerEditor() {
        return offerEditor;
    }

    /** Commands bound to this editor state. */
    private final ShopCategoryEditor categoryEditor = new ShopCategoryEditor(this);

    /**
     * Returns session-scoped editing commands.
     *
     * @return commands for this state.
     */
    public ShopCategoryEditor categoryEditor() {
        return categoryEditor;
    }

    /** Commands bound to this editor state. */
    private final ShopCurrencyEditor currencyEditor = new ShopCurrencyEditor(this);

    /**
     * Returns session-scoped editing commands.
     *
     * @return commands for this state.
     */
    public ShopCurrencyEditor currencyEditor() {
        return currencyEditor;
    }

    /**
     * Returns the actual draft, without falling back to the live shop.
     *
     * @return editable draft, or null outside a session.
     */
    @Nullable
    ShopInstance editableShop() {
        return draftShop;
    }

    /**
     * Updates selection before the command publishes a single change notification.
     *
     * @param target selected object, or null to clear selection.
     */
    void setSelectionSilently(@Nullable Object target) {
        selectedTarget = target;
    }

    /**
     * Creates independent editor state for embedding and tests.
     *
     * @return empty editor state with its own commands and subscriptions.
     */
    public static ShopEditorState create() {
        return new ShopEditorState();
    }

    private ShopEditorState() {}

    public boolean isEditorMode() {
        return editorMode;
    }

    public EditorTab getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(EditorTab tab) {
        if (this.activeTab != tab) {
            this.activeTab = tab != null ? tab : EditorTab.OFFERS;
            this.selectedTarget = null;
            notifyListeners();
        }
    }

    public @Nullable ShopEditSession getSession() {
        return session;
    }

    public @Nullable ShopInstance getDraftShop() {
        return draftShop != null ? draftShop : SDMShopClient.Shop;
    }

    public @Nullable Object getSelectedTarget() {
        return selectedTarget;
    }

    public long getRevision() {
        return revision;
    }

    public void selectTarget(@Nullable Object target) {
        if (Objects.equals(this.selectedTarget, target)) {
            return;
        }
        this.selectedTarget = target;
        notifyListeners();
    }

    /**
     * Registers a listener with an explicit disposal handle.
     *
     * @param listener callback invoked after editor state changes.
     * @return idempotent registration handle.
     * @throws NullPointerException if listener is null.
     */
    public ShopUiSubscription subscribe(Runnable listener) {
        Objects.requireNonNull(listener, "listener");
        // A distinct wrapper gives each registration independent ownership.
        Runnable registered = listener::run;
        listeners.add(registered);
        return () -> listeners.remove(registered);
    }

    public void addListener(Runnable listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(Runnable listener) {
        listeners.remove(listener);
    }

    public void notifyListeners() {
        for (Runnable listener : listeners) {
            try {
                listener.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /** Запуск режима редактирования. Проверяет сохраненную сессию в кэше. */
    public void startEditor(boolean clearOldSession) {
        ShopInstance liveShop = SDMShopClient.Shop;
        if (liveShop == null) {
            ShopToastHelper.error(Component.literal("No active shop found"));
            return;
        }

        UUID playerUUID =
                Minecraft.getInstance().player != null
                        ? Minecraft.getInstance().player.getUUID()
                        : UUID.randomUUID();

        PersistentEditSession saved = ShopClientCache.getEditSession();
        if (saved != null && !clearOldSession) {
            try {
                this.session = saved.restoreSession();
                this.draftShop = this.session.draftShop(liveShop.getId());
            } catch (Exception e) {
                this.session = ShopEditSession.forShop(liveShop, playerUUID);
                this.draftShop = this.session.draftShop(liveShop.getId());
            }
        } else {
            if (clearOldSession) {
                this.revision++;
                ShopClientCache.clearEditSession();
            }
            this.session = ShopEditSession.forShop(liveShop, playerUUID);
            this.draftShop = this.session.draftShop(liveShop.getId());
        }

        this.editorMode = true;
        this.activeTab = EditorTab.OFFERS;
        this.selectedTarget = null;
        this.revision++;

        offerEditor.resetDefaults();
        if (this.draftShop != null && this.draftShop.getEntries() != null) {
            for (ShopOffer offer : this.draftShop.getEntries().getEntryMap().values()) {
                ensureOfferDefaultsTracked(offer);
            }
        }

        // Выбираем первый оффер для инспектора, если есть
        if (this.draftShop != null && this.draftShop.getEntries() != null) {
            Map<UUID, ShopOffer> map = this.draftShop.getEntries().getEntryMap();
            if (map != null && !map.isEmpty()) {
                this.selectedTarget = map.values().iterator().next();
            }
        }

        saveToCache();
        notifyListeners();
        ShopScreenController.invokeRefreshAll();
    }

    /** Прямая инициализация сессии (для тестирования или внешнего внедрения). */
    public void startSession(ShopEditSession session, ShopInstance draftShop) {
        this.session = session;
        this.draftShop = draftShop;
        this.editorMode = true;
        this.activeTab = EditorTab.OFFERS;
        this.selectedTarget = null;
        this.revision++;
        offerEditor.resetDefaults();
        if (this.draftShop != null && this.draftShop.getEntries() != null) {
            for (ShopOffer offer : this.draftShop.getEntries().getEntryMap().values()) {
                ensureOfferDefaultsTracked(offer);
            }
        }
        notifyListeners();
    }

    /** Выход из режима редактора (переход в режим обычного просмотра). */
    public void exitEditor() {
        this.editorMode = false;
        this.selectedTarget = null;
        saveToCache();
        notifyListeners();
        ShopScreenController.invokeRefreshAll();
    }

    /** Сброс текущей сессии к актуальному состоянию с сервера. */
    public void resetSession() {
        ShopInstance liveShop = SDMShopClient.Shop;
        if (liveShop != null && session != null) {
            session.resetDraft(liveShop);
            session.clearHistory();
            this.draftShop = session.draftShop(liveShop.getId());
        }
        this.revision++;
        ShopClientCache.clearEditSession();
        this.selectedTarget = null;
        offerEditor.resetDefaults();
        if (this.draftShop != null && this.draftShop.getEntries() != null) {
            for (ShopOffer offer : this.draftShop.getEntries().getEntryMap().values()) {
                ensureOfferDefaultsTracked(offer);
            }
        }
        notifyListeners();
        ShopScreenController.invokeRefreshAll();
        ShopToastHelper.info(Component.literal("Edit session reset to server state"));
    }

    /** Отправка изменений на сервер. */
    public void sendChanges() {
        if (draftShop == null || session == null) {
            return;
        }

        ShopToastHelper.info(Component.literal("Sending changes to server..."));

        ShopNetworkManager.sendShopChanges(draftShop)
                .thenAccept(
                        success -> {
                            Minecraft.getInstance()
                                    .execute(
                                            () -> {
                                                if (Boolean.TRUE.equals(success)) {
                                                    this.revision++;
                                                    ShopClientCache.clearEditSession();
                                                    ShopToastHelper.success(
                                                            Component.literal(
                                                                    "Shop changes successfully"
                                                                        + " saved on server!"));
                                                    exitEditor();
                                                } else {
                                                    ShopToastHelper.error(
                                                            Component.literal(
                                                                    "Server rejected shop"
                                                                        + " changes!"));
                                                }
                                            });
                        })
                .exceptionally(
                        ex -> {
                            Minecraft.getInstance()
                                    .execute(
                                            () -> {
                                                ShopToastHelper.error(
                                                        Component.literal(
                                                                "Network error while sending"
                                                                    + " changes: "
                                                                        + ex.getMessage()));
                                            });
                            return null;
                        });

        // Если были изменения валют
        if (!session.currencyDrafts().isEmpty()) {
            List<CurrencyDraft> draftsToSend = List.copyOf(session.currencyDrafts());
            ShopNetworkManager.sendCurrencyChanges(draftsToSend)
                    .thenAccept(
                            curSuccess -> {
                                Minecraft.getInstance()
                                        .execute(
                                                () -> {
                                                    if (curSuccess) {
                                                        session.setCurrencyDrafts(
                                                                new ArrayList<>());
                                                        markDirty();
                                                        ShopToastHelper.success(
                                                                Component.literal(
                                                                        "Currency changes"
                                                                            + " successfully saved"
                                                                            + " on server!"));
                                                    } else {
                                                        ShopToastHelper.error(
                                                                Component.literal(
                                                                        "Server rejected currency"
                                                                            + " changes!"));
                                                    }
                                                });
                            })
                    .exceptionally(
                            ex -> {
                                Minecraft.getInstance()
                                        .execute(
                                                () -> {
                                                    ShopToastHelper.error(
                                                            Component.literal(
                                                                    "Network error while sending"
                                                                        + " currency changes: "
                                                                            + ex.getMessage()));
                                                });
                                return null;
                            });
        }
    }

    public int getChangeCount() {
        return session != null ? session.historySize() : 0;
    }

    public boolean isDirty() {
        return session != null && session.dirty();
    }

    public void markDirty() {
        if (session != null) {
            this.revision++;
            saveToCache();
            notifyListeners();
            ShopScreenController.invokeRefreshOffers();
        }
    }

    private void saveToCache() {
        if (session != null && draftShop != null) {
            ShopClientCache.saveEditSession(session, draftShop);
        }
    }

    // --- Управление товарами (Offers) ---

    /**
     * Delegates to {@link ShopOfferEditor}.
     *
     * @return the operation result; absent objects may be null.
     */
    public ShopOffer createNewOffer() {
        return offerEditor.createNewOffer();
    }

    /**
     * Delegates to {@link ShopOfferEditor}.
     *
     * @param atBeginning atBeginning used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public ShopOffer createNewOffer(boolean atBeginning) {
        return offerEditor.createNewOffer(atBeginning);
    }

    /**
     * Delegates to {@link ShopOfferEditor}.
     *
     * @param preset preset used by this operation.
     * @param atBeginning atBeginning used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public ShopOffer createOfferFromPreset(ShopOfferPreset preset, boolean atBeginning) {
        return offerEditor.createOfferFromPreset(preset, atBeginning);
    }

    /**
     * Delegates to {@link ShopOfferEditor}.
     *
     * @param offer offer used by this operation.
     */
    public void deleteOffer(ShopOffer offer) {
        offerEditor.deleteOffer(offer);
    }

    /**
     * Delegates to {@link ShopOfferEditor}.
     *
     * @param offer offer used by this operation.
     * @param offset offset used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public boolean moveOffer(ShopOffer offer, int offset) {
        return offerEditor.moveOffer(offer, offset);
    }

    /**
     * Delegates to {@link ShopOfferEditor}.
     *
     * @param offer offer used by this operation.
     * @param targetIndex targetIndex used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public boolean moveOfferToIndex(ShopOffer offer, int targetIndex) {
        return offerEditor.moveOfferToIndex(offer, targetIndex);
    }

    /**
     * Delegates to {@link ShopOfferEditor}.
     *
     * @param offer offer used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public ShopOffer duplicateOffer(ShopOffer offer) {
        return offerEditor.duplicateOffer(offer);
    }

    /**
     * Delegates to {@link ShopOfferEditor}.
     *
     * @param offer offer used by this operation.
     * @param type type used by this operation.
     */
    public void addComponent(ShopOffer offer, IComponentType<?> type) {
        offerEditor.addComponent(offer, type);
    }

    /**
     * Delegates to {@link ShopOfferEditor}.
     *
     * @param offer offer used by this operation.
     */
    public void ensureOfferDefaultsTracked(ShopOffer offer) {
        offerEditor.ensureOfferDefaultsTracked(offer);
    }

    /**
     * Delegates to {@link ShopOfferEditor}.
     *
     * @param offer offer used by this operation.
     * @param component component used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public boolean isDefaultComponent(ShopOffer offer, ShopComponent component) {
        return offerEditor.isDefaultComponent(offer, component);
    }

    /**
     * Delegates to {@link ShopOfferEditor}.
     *
     * @param offer offer used by this operation.
     * @param component component used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public boolean canDeleteComponent(ShopOffer offer, ShopComponent component) {
        return offerEditor.canDeleteComponent(offer, component);
    }

    /**
     * Delegates to {@link ShopOfferEditor}.
     *
     * @param offer offer used by this operation.
     * @param component component used by this operation.
     */
    public void removeComponent(ShopOffer offer, ShopComponent component) {
        offerEditor.removeComponent(offer, component);
    }

    /**
     * Delegates to {@link ShopOfferEditor}.
     *
     * @param offer offer used by this operation.
     * @param component component used by this operation.
     * @param offset offset used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public boolean moveComponent(ShopOffer offer, ShopComponent component, int offset) {
        return offerEditor.moveComponent(offer, component, offset);
    }

    /**
     * Delegates to {@link ShopOfferEditor}.
     *
     * @param offer offer used by this operation.
     * @param component component used by this operation.
     * @param targetIndex targetIndex used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public boolean moveComponentToIndex(ShopOffer offer, ShopComponent component, int targetIndex) {
        return offerEditor.moveComponentToIndex(offer, component, targetIndex);
    }

    /**
     * Delegates to {@link ShopOfferEditor}.
     *
     * @param offer offer used by this operation.
     * @param component component used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public ShopComponent duplicateComponent(ShopOffer offer, ShopComponent component) {
        return offerEditor.duplicateComponent(offer, component);
    }

    /**
     * Delegates to {@link ShopCategoryEditor}.
     *
     * @return the operation result; absent objects may be null.
     */
    public CatalogComponent createNewCategory() {
        return categoryEditor.createNewCategory();
    }

    /**
     * Delegates to {@link ShopCategoryEditor}.
     *
     * @param cat cat used by this operation.
     */
    public void deleteCategory(CatalogComponent cat) {
        categoryEditor.deleteCategory(cat);
    }

    /**
     * Delegates to {@link ShopCategoryEditor}.
     *
     * @param cat cat used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public CatalogComponent duplicateCategory(CatalogComponent cat) {
        return categoryEditor.duplicateCategory(cat);
    }

    /**
     * Delegates to {@link ShopCategoryEditor}.
     *
     * @param cat cat used by this operation.
     * @param offset offset used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public boolean moveCategory(CatalogComponent cat, int offset) {
        return categoryEditor.moveCategory(cat, offset);
    }

    /**
     * Delegates to {@link ShopCategoryEditor}.
     *
     * @param cat cat used by this operation.
     * @param targetIndex targetIndex used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public boolean moveCategoryToIndex(CatalogComponent cat, int targetIndex) {
        return categoryEditor.moveCategoryToIndex(cat, targetIndex);
    }

    /**
     * Delegates to {@link ShopCurrencyEditor}.
     *
     * @return the operation result; absent objects may be null.
     */
    public List<CurrencyDraft> getCurrencyDrafts() {
        return currencyEditor.getCurrencyDrafts();
    }

    /**
     * Delegates to {@link ShopCurrencyEditor}.
     *
     * @param id id used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public boolean isDefaultCurrency(@Nullable ResourceLocation id) {
        return currencyEditor.isDefaultCurrency(id);
    }

    /**
     * Delegates to {@link ShopCurrencyEditor}.
     *
     * @param id id used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public boolean isCurrencyDeleted(@Nullable ResourceLocation id) {
        return currencyEditor.isCurrencyDeleted(id);
    }

    /**
     * Delegates to {@link ShopCurrencyEditor}.
     *
     * @param id id used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public boolean hasCurrencyDraft(@Nullable ResourceLocation id) {
        return currencyEditor.hasCurrencyDraft(id);
    }

    /**
     * Delegates to {@link ShopCurrencyEditor}.
     *
     * @param id id used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public CurrencyDraft findCurrencyDraft(@Nullable ResourceLocation id) {
        return currencyEditor.findCurrencyDraft(id);
    }

    /**
     * Delegates to {@link ShopCurrencyEditor}.
     *
     * @param currency currency used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public CurrencyDraft getOrCreateCurrencyDraft(
            dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency currency) {
        return currencyEditor.getOrCreateCurrencyDraft(currency);
    }

    /**
     * Delegates to {@link ShopCurrencyEditor}.
     *
     * @param oldDraft oldDraft used by this operation.
     * @param newDraft newDraft used by this operation.
     */
    public void updateCurrencyDraft(CurrencyDraft oldDraft, CurrencyDraft newDraft) {
        currencyEditor.updateCurrencyDraft(oldDraft, newDraft);
    }

    /**
     * Delegates to {@link ShopCurrencyEditor}.
     *
     * @return the operation result; absent objects may be null.
     */
    public CurrencyDraft createNewCurrency() {
        return currencyEditor.createNewCurrency();
    }

    /**
     * Delegates to {@link ShopCurrencyEditor}.
     *
     * @param currency currency used by this operation.
     */
    public void deleteCurrency(CurrencyDraft currency) {
        currencyEditor.deleteCurrency(currency);
    }

    /**
     * Delegates to {@link ShopCurrencyEditor}.
     *
     * @param currency currency used by this operation.
     */
    public void deleteCurrency(dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency currency) {
        currencyEditor.deleteCurrency(currency);
    }

    /**
     * Delegates to {@link ShopCurrencyEditor}.
     *
     * @param currency currency used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public CurrencyDraft duplicateCurrency(CurrencyDraft currency) {
        return currencyEditor.duplicateCurrency(currency);
    }

    /**
     * Delegates to {@link ShopCurrencyEditor}.
     *
     * @param currency currency used by this operation.
     * @return the operation result; absent objects may be null.
     */
    public CurrencyDraft duplicateCurrency(dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency currency) {
        return currencyEditor.duplicateCurrency(currency);
    }
}
