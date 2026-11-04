package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;
import dev.sixik.sdmshop2.libs.shop.base.ObjectIdGetter;
import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.SDMShopClient;
import dev.sixik.sdmshop2.libs.shop.client.cache.PersistentEditSession;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopScreenController;
import dev.sixik.sdmshop2.libs.shop.client.ui.editor.preset.ShopOfferPreset;
import dev.sixik.sdmshop2.libs.shop.components.ItemRewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponentRegistry;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.ShopOffersContainerComponent;
import dev.sixik.sdmshop2.libs.shop.components.money.MoneyCostComponent;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditTargets;
import dev.sixik.sdmshop2.libs.shop.network.ShopNetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Глобальный диспетчер состояния внутриигрового редактора магазина SDMShop-2.
 * Управляет сессией редактирования (ShopEditSession / draftShop), активной вкладкой
 * (Offers / Categories / Currencies) и выбранным объектом для инспектора.
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

    private final Set<ShopComponent> defaultComponents = Collections.newSetFromMap(new IdentityHashMap<>());
    private final Set<UUID> initializedOfferDefaults = new HashSet<>();

    private ShopEditorState() {
    }

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

    /**
     * Запуск режима редактирования.
     * Проверяет сохраненную сессию в кэше.
     */
    public void startEditor(boolean clearOldSession) {
        ShopInstance liveShop = SDMShopClient.Shop;
        if (liveShop == null) {
            ShopToastHelper.error(Component.literal("No active shop found"));
            return;
        }

        UUID playerUUID = Minecraft.getInstance().player != null
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
        this.revision++;

        initializedOfferDefaults.clear();
        defaultComponents.clear();
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

    /**
     * Прямая инициализация сессии (для тестирования или внешнего внедрения).
     */
    public void startSession(ShopEditSession session, ShopInstance draftShop) {
        this.session = session;
        this.draftShop = draftShop;
        this.editorMode = true;
        this.activeTab = EditorTab.OFFERS;
        this.revision++;
        this.initializedOfferDefaults.clear();
        this.defaultComponents.clear();
        if (this.draftShop != null && this.draftShop.getEntries() != null) {
            for (ShopOffer offer : this.draftShop.getEntries().getEntryMap().values()) {
                ensureOfferDefaultsTracked(offer);
            }
        }
        notifyListeners();
    }

    /**
     * Выход из режима редактора (переход в режим обычного просмотра).
     */
    public void exitEditor() {
        this.editorMode = false;
        this.selectedTarget = null;
        saveToCache();
        notifyListeners();
        ShopScreenController.invokeRefreshAll();
    }

    /**
     * Сброс текущей сессии к актуальному состоянию с сервера.
     */
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
        initializedOfferDefaults.clear();
        defaultComponents.clear();
        if (this.draftShop != null && this.draftShop.getEntries() != null) {
            for (ShopOffer offer : this.draftShop.getEntries().getEntryMap().values()) {
                ensureOfferDefaultsTracked(offer);
            }
        }
        notifyListeners();
        ShopScreenController.invokeRefreshAll();
        ShopToastHelper.info(Component.literal("Edit session reset to server state"));
    }

    /**
     * Отправка изменений на сервер.
     */
    public void sendChanges() {
        if (draftShop == null || session == null) {
            return;
        }

        ShopToastHelper.info(Component.literal("Sending changes to server..."));

        ShopNetworkManager.sendShopChanges(draftShop).thenAccept(success -> {
            Minecraft.getInstance().execute(() -> {
                if (Boolean.TRUE.equals(success)) {
                    this.revision++;
        ShopClientCache.clearEditSession();
                    ShopToastHelper.success(Component.literal("Shop changes successfully saved on server!"));
                    exitEditor();
                } else {
                    ShopToastHelper.error(Component.literal("Server rejected shop changes!"));
                }
            });
        }).exceptionally(ex -> {
            Minecraft.getInstance().execute(() -> {
                ShopToastHelper.error(Component.literal("Network error while sending changes: " + ex.getMessage()));
            });
            return null;
        });

        // Если были изменения валют
        if (!session.currencyDrafts().isEmpty()) {
            ShopNetworkManager.sendCurrencyChanges(session.currencyDrafts());
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

    public ShopOffer createNewOffer() {
        return createNewOffer(false);
    }

    public ShopOffer createNewOffer(boolean atBeginning) {
        if (draftShop == null || session == null) {
            return null;
        }

        UUID offerId = UUID.randomUUID();
        ShopOffer offer = ShopOffer.create(offerId, false);

        NameComponent nameComp = new NameComponent("New Offer");
        offer.addComponent(nameComp);

        // Категория по умолчанию
        String defaultCatId = "resources";
        if (draftShop.getCategories() != null && !draftShop.getCategories().getCatalogsComponents().isEmpty()) {
            defaultCatId = draftShop.getCategories().getCatalogsComponents().get(0).getId();
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
        ResourceLocation defaultMoney = SDMEconomyServiceClient.getAllCurrencies().isEmpty()
                ? new ResourceLocation("sdm", "coin")
                : SDMEconomyServiceClient.getAllCurrencies().values().iterator().next().getId();
        offer.addComponent(new MoneyCostComponent(defaultMoney, 10.0));

        if (atBeginning) {
            draftShop.getEntries().addEntryAt(offer, 0);
        } else {
            draftShop.getEntries().addEntry(offer);
        }
        session.recordHistory("offer.create", "ShopOffer", offerId, null, "Created offer " + offerId);
        selectTarget(offer);
        markDirty();
        return offer;
    }

    public ShopOffer createOfferFromPreset(ShopOfferPreset preset, boolean atBeginning) {
        if (draftShop == null || session == null || preset == null) {
            return null;
        }

        ShopOffer offer = preset.createOffer();
        if (offer == null) return null;

        if (atBeginning) {
            draftShop.getEntries().addEntryAt(offer, 0);
        } else {
            draftShop.getEntries().addEntry(offer);
        }

        ensureOfferDefaultsTracked(offer);
        session.recordHistory("offer.create_preset", "ShopOffer", offer.getUUID(), null, "Created offer from preset: " + preset.name());
        selectTarget(offer);
        markDirty();
        ShopToastHelper.success(Component.literal("Offer created from preset: " + preset.name()));
        return offer;
    }

    public void deleteOffer(ShopOffer offer) {
        if (draftShop == null || session == null || offer == null) {
            return;
        }

        draftShop.getEntries().removeEntry(offer.getUUID());
        session.recordHistory("offer.delete", "ShopOffer", offer.getUUID(), null, "Deleted offer " + offer.getUUID());
        if (selectedTarget == offer) {
            selectedTarget = null;
        }
        markDirty();
    }

    public boolean moveOffer(ShopOffer offer, int offset) {
        if (draftShop == null || session == null || offer == null || draftShop.getEntries() == null) {
            return false;
        }
        boolean moved = draftShop.getEntries().moveEntry(offer.getUUID(), offset);
        if (moved) {
            session.recordHistory("offer.move", "ShopOffer", offer.getUUID(), null, "Moved offer offset " + offset);
            markDirty();
            return true;
        }
        return false;
    }

    public boolean moveOfferToIndex(ShopOffer offer, int targetIndex) {
        if (draftShop == null || session == null || offer == null || draftShop.getEntries() == null) {
            return false;
        }
        boolean moved = draftShop.getEntries().moveEntryToIndex(offer.getUUID(), targetIndex);
        if (moved) {
            session.recordHistory("offer.reorder", "ShopOffer", offer.getUUID(), null, "Reordered offer to index " + targetIndex);
            markDirty();
            return true;
        }
        return false;
    }

    public ShopOffer duplicateOffer(ShopOffer offer) {
        if (draftShop == null || session == null || offer == null || draftShop.getEntries() == null) {
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
                } catch (Exception ignored) {}
            }

            draftShop.getEntries().addEntryAfter(clone, offer.getUUID());
            ensureOfferDefaultsTracked(clone);
            session.recordHistory("offer.duplicate", "ShopOffer", newUuid, null, "Duplicated offer " + offer.getUUID());
            selectTarget(clone);
            markDirty();
            ShopToastHelper.success(Component.literal("Offer duplicated"));
            return clone;
        } catch (Exception e) {
            e.printStackTrace();
            ShopToastHelper.error(Component.literal("Failed to duplicate offer: " + e.getMessage()));
            return null;
        }
    }

    // --- Управление компонентами оффера ---

    public void addComponent(ShopOffer offer, IComponentType<?> type) {
        if (offer == null || type == null || session == null) {
            return;
        }

        ShopComponent component = type.createDefault();
        offer.addComponent(component);
        session.recordHistory("component.add", "ShopOffer", offer.getUUID(), type.getId(), "Added component " + type.getId());
        markDirty();
    }

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

    public boolean isDefaultComponent(ShopOffer offer, ShopComponent component) {
        if (offer == null || component == null) return false;
        ensureOfferDefaultsTracked(offer);
        return defaultComponents.contains(component);
    }

    public boolean canDeleteComponent(ShopOffer offer, ShopComponent component) {
        if (offer == null || component == null) return false;
        // Компоненты по умолчанию sdm:catalog и sdm:name нельзя удалять.
        // Но если через редактор добавлены новые или дубликаты, их удалять можно!
        return !isDefaultComponent(offer, component);
    }

    public void removeComponent(ShopOffer offer, ShopComponent component) {
        if (offer == null || component == null || session == null) {
            return;
        }

        if (!canDeleteComponent(offer, component)) {
            ShopToastHelper.warning(Component.literal("Default components (sdm:catalog, sdm:name) cannot be removed."));
            return;
        }

        defaultComponents.remove(component);
        offer.removeComponent(component);
        session.recordHistory("component.delete", "ShopOffer", offer.getUUID(), component.getType() != null ? component.getType().getId() : null, "Removed component");
        markDirty();
    }

    public boolean moveComponent(ShopOffer offer, ShopComponent component, int offset) {
        if (offer == null || component == null || session == null) {
            return false;
        }

        if (offer.moveComponent(component, offset)) {
            session.recordHistory("component.move", "ShopOffer", offer.getUUID(), component.getType() != null ? component.getType().getId() : null, "Moved component offset " + offset);
            markDirty();
            return true;
        }
        return false;
    }

    public boolean moveComponentToIndex(ShopOffer offer, ShopComponent component, int targetIndex) {
        if (offer == null || component == null || session == null) {
            return false;
        }

        if (offer.moveComponentToIndex(component, targetIndex)) {
            session.recordHistory("component.reorder", "ShopOffer", offer.getUUID(), component.getType() != null ? component.getType().getId() : null, "Reordered component to index " + targetIndex);
            markDirty();
            return true;
        }
        return false;
    }

    public ShopComponent duplicateComponent(ShopOffer offer, ShopComponent component) {
        if (offer == null || component == null || session == null) {
            return null;
        }

        try {
            com.google.gson.JsonObject json = ShopComponentRegistry.toJson(component);
            ShopComponent clone = ShopComponentRegistry.fromJson(json);
            offer.addComponent(clone);
            offer.moveComponentAfter(clone, component);
            // Дубликат не является компонентом по умолчанию и может быть удален
            defaultComponents.remove(clone);
            session.recordHistory("component.duplicate", "ShopOffer", offer.getUUID(), component.getType() != null ? component.getType().getId() : null, "Duplicated component");
            markDirty();
            ShopToastHelper.success(Component.literal("Component duplicated"));
            return clone;
        } catch (Exception e) {
            e.printStackTrace();
            ShopToastHelper.error(Component.literal("Failed to duplicate component: " + e.getMessage()));
            return null;
        }
    }

    // --- Управление категориями (Categories) ---

    public CatalogComponent createNewCategory() {
        if (draftShop == null || session == null) {
            return null;
        }

        String catId = "cat_" + System.currentTimeMillis() % 10000;
        CatalogComponent cat = new CatalogComponent(catId);
        cat.setIconType(CatalogComponent.IconType.ITEM);
        cat.setIconItem(Items.CHEST.getDefaultInstance());

        // Добавляем категорию в контейнер категорий
        draftShop.getCategories().getCatalogsComponents().add(cat);
        session.recordHistory("category.create", "CatalogComponent", cat.getUuid(), cat.getType().getId(), "Created category " + catId);
        selectTarget(cat);
        markDirty();
        return cat;
    }

    public void deleteCategory(CatalogComponent cat) {
        if (draftShop == null || session == null || cat == null) {
            return;
        }

        draftShop.getCategories().getCatalogsComponents().remove(cat);
        session.recordHistory("category.delete", "CatalogComponent", cat.getUuid(), cat.getType().getId(), "Deleted category " + cat.getId());
        if (selectedTarget == cat) {
            selectedTarget = null;
        }
        markDirty();
    }

    public CatalogComponent duplicateCategory(CatalogComponent cat) {
        if (draftShop == null || session == null || cat == null) {
            return null;
        }

        CatalogComponent copy = CatalogComponent.copyOf(cat);
        copy.setUuid(UUID.randomUUID());
        String baseId = cat.getId() != null ? cat.getId() : "cat";
        copy.setId(baseId + "_copy");
        copy.setOrder(cat.getOrder() + 1);

        draftShop.getCategories().getCatalogsComponents().add(copy);
        session.recordHistory("category.duplicate", "CatalogComponent", copy.getUuid(), copy.getType().getId(), "Duplicated category " + copy.getId());
        ShopToastHelper.success(Component.literal("Category duplicated"));
        selectTarget(copy);
        markDirty();
        return copy;
    }

    // --- Управление валютами (Currencies) ---

    public CurrencyDraft createNewCurrency() {
        if (session == null) {
            return null;
        }

        ResourceLocation id = new ResourceLocation("sdm", "currency_" + System.currentTimeMillis() % 10000);
        CurrencyDraft draft = CurrencyDraft.text(id, "New Currency", "$");

        List<CurrencyDraft> list = new ArrayList<>(session.currencyDrafts());
        list.add(draft);
        session.setCurrencyDrafts(list);
        session.recordHistory("currency.create", "CurrencyDraft", null, id, "Created currency draft " + id);
        selectTarget(draft);
        markDirty();
        return draft;
    }

    public void deleteCurrency(CurrencyDraft currency) {
        if (session == null || currency == null) {
            return;
        }

        List<CurrencyDraft> list = new ArrayList<>(session.currencyDrafts());
        list.remove(currency);
        session.setCurrencyDrafts(list);
        session.recordHistory("currency.delete", "CurrencyDraft", null, currency.id(), "Deleted currency draft " + currency.id());
        if (selectedTarget == currency) {
            selectedTarget = null;
        }
        markDirty();
    }

    public CurrencyDraft duplicateCurrency(CurrencyDraft currency) {
        if (session == null || currency == null) {
            return null;
        }

        ResourceLocation oldId = currency.id();
        ResourceLocation newId = new ResourceLocation(oldId.getNamespace(), oldId.getPath() + "_copy");
        CurrencyDraft copy = CurrencyDraft.text(newId, currency.displayName() + " (Copy)", currency.iconText());

        List<CurrencyDraft> list = new ArrayList<>(session.currencyDrafts());
        list.add(copy);
        session.setCurrencyDrafts(list);
        session.recordHistory("currency.duplicate", "CurrencyDraft", null, newId, "Duplicated currency " + newId);
        ShopToastHelper.success(Component.literal("Currency draft duplicated"));
        selectTarget(copy);
        markDirty();
        return copy;
    }

    public CurrencyDraft duplicateCurrency(dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency currency) {
        if (session == null || currency == null) {
            return null;
        }

        ResourceLocation oldId = currency.getId();
        ResourceLocation newId = new ResourceLocation(oldId != null ? oldId.getNamespace() : "sdm",
                (oldId != null ? oldId.getPath() : "cur") + "_copy");
        String name = currency.getDisplayName() != null ? currency.getDisplayName().getString() + " (Copy)" : "New Currency";
        String symbol = "$";
        try {
            symbol = currency.format(java.math.BigDecimal.ONE).replaceAll("[0-9.,\\s]", "");
            if (symbol.isEmpty()) symbol = "$";
        } catch (Exception ignored) {}
        CurrencyDraft copy = CurrencyDraft.text(newId, name, symbol);

        List<CurrencyDraft> list = new ArrayList<>(session.currencyDrafts());
        list.add(copy);
        session.setCurrencyDrafts(list);
        session.recordHistory("currency.duplicate", "CurrencyDraft", null, newId, "Duplicated currency " + newId);
        ShopToastHelper.success(Component.literal("Currency duplicated as draft"));
        selectTarget(copy);
        markDirty();
        return copy;
    }
}
