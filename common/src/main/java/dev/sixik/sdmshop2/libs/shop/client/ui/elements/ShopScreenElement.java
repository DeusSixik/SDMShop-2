package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Size;
import com.mojang.blaze3d.platform.Window;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.DODEventBus;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventPtr;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventSubscription;
import dev.sixik.sdmshop2.libs.sdmeconomy.CurrencyDraft;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;
import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.SDMShopClient;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.ShopScreenController;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUIUtils;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIDisposable;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIEventScope;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop.client.ui.toast.ShopToasts;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.base.ShopWidgetGroup;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.ShopOffersContainerComponent;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditTargets;
import dev.sixik.sdmshop2.libs.shop.network.ShopNetworkManager;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ShopScreenElement extends ShopWidgetGroup implements UIDisposable {

    public static ShopScreenElement Instance;

    @Getter
    private final Minecraft minecraft;

    @Getter
    private final Window window;

    @Getter
    private ShopTabsPanelElement tabsPanel;

    @Getter
    private ShopOffersPanelElement shopOffersPanel;

    @Getter
    private ShopToolPanelElement toolPanel;

    @Getter
    @Nullable
    private final ShopEditSession editSession;
    @Nullable
    private final ResourceLocation editShopId;
    private final ObjectArrayList<CatalogComponent> editorDraftCategories = new ObjectArrayList<>();

    @Getter
    private ObjectArrayList<CatalogComponent> catalogComponents;
    @Getter
    private ShopOffersContainerComponent entriesContainer;

    private final UIEventScope screenEventScope = new UIEventScope();
    private final Map<String, State> states = new Object2ObjectOpenHashMap<>();

    public ShopScreenElement() {
        this(null, null);
    }

    public ShopScreenElement(@Nullable ShopEditSession editSession, @Nullable ResourceLocation editShopId) {
        this.editSession = editSession;
        this.editShopId = editShopId;
        this.minecraft = Minecraft.getInstance();
        this.window = minecraft.getWindow();
        Instance = this;

        if (editSession != null) {
            editSession.setOnChanged(() -> {
                ShopInstance activeShop = getActiveShop();
                if (activeShop != null) {
                    ShopClientCache.saveEditSession(editSession, activeShop);
                }
                if (toolPanel != null) {
                    toolPanel.alightWidget();
                }
            });
        }
    }

    @Override
    public void initWidget() {
//        setBackground(new ColorRectAndBorderTexture());

        reloadShopData();
        listenScreen(ShopUIEvents.REFRESH_UI, this::handleRefresh);

        alightWidget();
        addWidget(toolPanel = new ShopToolPanelElement(this));
        addWidget(tabsPanel = new ShopTabsPanelElement(this));
        addWidget(shopOffersPanel = new ShopOffersPanelElement(this));
        ShopToasts.attachTo(this);
        if (!isEditorMode()) {
            listenScreen(ShopUIEvents.BUY_SHOP_ENTITY, event -> ShopPurchaseModalElement.open(this, event.entity(), event.money_group()));
        }
        customInitWidget();
    }

    protected void handleRefresh(ShopUIEvents.RefreshUI event) {
        reloadShopData();

        if (!event.affectsCategories() || tabsPanel == null || tabsPanel.getSelectedCategory() == null) {
            return;
        }

        if (!hasCategory(tabsPanel.getSelectedCategory())) {
            ShopUIEvents.invokeSelectCategory(null);
        }
    }

    protected void reloadShopData() {
        ShopInstance shop = getActiveShop();
        if (shop == null) {
            catalogComponents = new ObjectArrayList<>();
            entriesContainer = null;
            return;
        }

        shop.getCategories().reindex();
        this.catalogComponents = mergeEditorCategories(shop.getCategories().getCatalogsComponents());
        this.entriesContainer = shop.getEntries();
    }

    @Nullable
    public ShopInstance getActiveShop() {
        if (editSession != null && editShopId != null && !editSession.closed()) {
            return editSession.draft(ShopEditTargets.shop(editShopId)).orElse(null);
        }

        return SDMShopClient.Shop;
    }

    public boolean isEditorMode() {
        return editSession != null;
    }

    public boolean hasEditorChanges() {
        return editSession != null && !editSession.closed() && editSession.dirty();
    }

    public CompletableFuture<Boolean> sendEditorChanges() {
        ShopInstance shop = getActiveShop();
        if (shop == null || editSession == null || editSession.closed()) {
            return CompletableFuture.completedFuture(false);
        }

        shop.setDirty();
        CompletableFuture<Boolean> shopChanges = ShopNetworkManager.sendShopChanges(shop);
        CompletableFuture<Boolean> currencyChanges = ShopNetworkManager.sendCurrencyChanges(editSession.currencyDrafts());

        return shopChanges.thenCombine(currencyChanges, (shopSuccess, currencySuccess) -> {
            boolean success = Boolean.TRUE.equals(shopSuccess) && Boolean.TRUE.equals(currencySuccess);
            if (success) {
                editSession.markClean();
                ShopClientCache.clearEditSession();
            }
            return success;
        });
    }

    public boolean resetEditorSession() {
        if (editSession == null || editSession.closed()) {
            return false;
        }

        if (SDMShopClient.Shop == null || SDMShopClient.Shop.isNull() || editShopId == null || !editShopId.equals(SDMShopClient.Shop.getId())) {
            ShopClientCache.clearEditSession();
            ShopScreenController.openShop();
            return true;
        }

        editSession.resetDraft(SDMShopClient.Shop);
        editorDraftCategories.clear();
        ShopClientCache.clearEditSession();
        reloadShopData();
        ShopUIEvents.invokeSelectCategory(null);
        ShopUIEvents.invokeRefreshAll();
        return true;
    }

    public Map<ResourceLocation, ICurrency> getEditorCurrencies() {
        Map<ResourceLocation, ICurrency> currencies = new LinkedHashMap<>(SDMEconomyServiceClient.getAllCurrencies());
        if (editSession == null || editSession.closed()) {
            return currencies;
        }

        for (CurrencyDraft draft : editSession.currencyDrafts()) {
            if (draft.kind() == CurrencyDraft.Kind.DELETE) {
                currencies.remove(draft.id());
            } else {
                currencies.put(draft.id(), draft.toCurrency());
            }
        }
        return currencies;
    }

    public boolean upsertTextCurrency(ResourceLocation id, String displayName, String iconText) {
        if (!canEditCurrencies() || id == null) {
            return false;
        }

        String safeName = displayName == null ? "" : displayName.trim();
        if (safeName.isEmpty()) {
            return false;
        }

        CurrencyDraft draft = CurrencyDraft.text(id, safeName, iconText);
        upsertCurrencyDraft(draft);
        recordEditorAction("currency.upsert_text", "currency", null, id, "Updated text currency " + id);
        ShopUIEvents.invokeRefreshCurrencies();
        return true;
    }

    public boolean upsertItemCurrency(ResourceLocation id, ItemStack itemStack) {
        if (!canEditCurrencies() || id == null || itemStack == null || itemStack.isEmpty() || itemStack.getItem() == Items.AIR) {
            return false;
        }

        CurrencyDraft draft = CurrencyDraft.item(id, itemStack);
        upsertCurrencyDraft(draft);
        recordEditorAction("currency.upsert_item", "currency", null, id, "Updated item currency " + id);
        ShopUIEvents.invokeRefreshCurrencies();
        return true;
    }

    public boolean deleteDraftCurrency(ResourceLocation id) {
        if (!canEditCurrencies() || id == null || isProtectedCurrency(id)) {
            return false;
        }

        List<CurrencyDraft> drafts = new ArrayList<>(editSession.currencyDrafts());
        boolean removedDraft = drafts.removeIf(current -> Objects.equals(current.id(), id));
        boolean existsOnServer = SDMEconomyServiceClient.getAllCurrencies().containsKey(id);
        if (!existsOnServer) {
            if (removedDraft) {
                editSession.setCurrencyDrafts(drafts);
                recordEditorAction("currency.delete_draft", "currency", null, id, "Deleted draft currency " + id);
                ShopUIEvents.invokeRefreshCurrencies();
                return true;
            }
            return false;
        }

        editSession.setCurrencyDrafts(drafts);
        upsertCurrencyDraft(CurrencyDraft.delete(id));
        recordEditorAction("currency.delete", "currency", null, id, "Deleted currency " + id);
        ShopUIEvents.invokeRefreshCurrencies();
        return true;
    }

    public boolean canDeleteCurrency(ResourceLocation id) {
        return canEditCurrencies() && id != null && !isProtectedCurrency(id);
    }

    private static boolean isProtectedCurrency(ResourceLocation id) {
        return ResourceLocation.tryBuild("sdm", "coin").equals(id);
    }

    private boolean canEditCurrencies() {
        return isEditorMode() && editSession != null && !editSession.closed();
    }

    private void upsertCurrencyDraft(CurrencyDraft draft) {
        if (editSession == null || editSession.closed() || draft == null) {
            return;
        }

        List<CurrencyDraft> drafts = new ArrayList<>(editSession.currencyDrafts());
        drafts.removeIf(current -> Objects.equals(current.id(), draft.id()));
        drafts.add(draft);
        editSession.setCurrencyDrafts(drafts);
    }

    public CatalogComponent createDraftCategory() {
        CatalogComponent category = new CatalogComponent(nextDraftCategoryId());
        category.setOrder(getOrderedCategories().size());
        editorDraftCategories.add(category);
        recordEditorAction("category.create", "category", null, null, "Created draft category " + category.getId());
        reloadShopData();
        ShopUIEvents.invokeSelectCategory(category);
        ShopUIEvents.invokeRefreshCategories();
        return category;
    }

    @Nullable
    public ShopOffer createDraftOffer(@Nullable CatalogComponent category) {
        ShopOffersContainerComponent entries = entriesContainer;
        if (entries == null) {
            return null;
        }

        CatalogComponent targetCategory = category == null ? firstOrDefaultCategory() : category;
        ShopOffer offer = ShopOffer.create(UUID.randomUUID(), false);
        offer.addComponent(new CatalogComponent(targetCategory.getId(), targetCategory.getUuid(), targetCategory.getOrder()));
        offer.addComponent(new NameComponent("New Offer"));
        entries.addEntry(offer);

        removeEditorDraftCategory(targetCategory);
        recordEditorAction("offer.create", "offer", offer.getUUID(), null, "Created draft offer " + offer.getUUID());
        reloadShopData();
        ShopUIEvents.invokeRefreshCategory(targetCategory);
        return offer;
    }

    public boolean deleteDraftOffer(UUID offerId) {
        if (offerId == null || entriesContainer == null) {
            return false;
        }

        ShopOffer removed = entriesContainer.removeEntry(offerId);
        if (removed == null) {
            return false;
        }

        recordEditorAction("offer.delete", "offer", offerId, null, "Deleted offer " + offerId);
        reloadShopData();
        ShopUIEvents.invokeRefreshOffers();
        return true;
    }

    @Nullable
    public ShopOffer duplicateDraftOffer(UUID offerId) {
        if (offerId == null || entriesContainer == null) {
            return null;
        }

        ShopOffer source = entriesContainer.getEntry(offerId);
        if (source == null) {
            return null;
        }

        ShopOffer duplicate = ShopOffer.create(UUID.randomUUID(), false);
        duplicate.deserialize(source.serialize().deepCopy());
        entriesContainer.addEntry(duplicate);

        recordEditorAction("offer.duplicate", "offer", duplicate.getUUID(), null,
                "Duplicated offer " + source.getUUID() + " as " + duplicate.getUUID());
        reloadShopData();
        ShopUIEvents.invokeRefreshOffers();
        return duplicate;
    }

    public List<CatalogComponent> getOrderedCategories() {
        if (catalogComponents == null || catalogComponents.isEmpty()) {
            return List.of();
        }

        return catalogComponents.stream()
                .sorted(categoryComparator())
                .toList();
    }

    public int countOffersInCategory(CatalogComponent category) {
        if (category == null || entriesContainer == null) {
            return 0;
        }

        int count = 0;
        for (ShopOffer offer : entriesContainer.getEntryMap().values()) {
            if (isOfferInCategory(offer, category)) {
                count++;
            }
        }
        return count;
    }

    public List<CatalogComponent> getMoveTargetCategories(CatalogComponent category) {
        List<CatalogComponent> targets = new ArrayList<>();
        for (CatalogComponent current : getOrderedCategories()) {
            if (!sameCategory(current, category)) {
                targets.add(current);
            }
        }
        return targets;
    }

    public boolean renameDraftCategory(CatalogComponent category, String newId) {
        if (!isEditableCategory(category) || newId == null) {
            return false;
        }

        String safeId = newId.trim();
        if (safeId.isEmpty() || Objects.equals(category.getId(), safeId)) {
            return false;
        }

        String oldId = category.getId();
        forEachMatchingCategory(category, current -> current.setId(safeId));

        recordEditorAction(
                "category.rename",
                "category",
                category.getUuid(),
                null,
                "Renamed category " + oldId + " to " + safeId
        );
        reloadShopData();
        ShopUIEvents.invokeRefreshCategories();
        return true;
    }

    public boolean moveDraftCategory(CatalogComponent category, int offset) {
        List<CatalogComponent> categories = new ArrayList<>(getOrderedCategories());
        int currentIndex = indexOfCategory(categories, category);
        if (currentIndex < 0) {
            return false;
        }

        return moveDraftCategoryTo(category, currentIndex + offset);
    }

    public boolean moveDraftCategoryTo(CatalogComponent category, int targetIndex) {
        if (!isEditableCategory(category)) {
            return false;
        }

        List<CatalogComponent> categories = new ArrayList<>(getOrderedCategories());
        int currentIndex = indexOfCategory(categories, category);
        if (currentIndex < 0 || categories.size() <= 1) {
            return false;
        }

        int safeTargetIndex = Math.max(0, Math.min(targetIndex, categories.size() - 1));
        if (safeTargetIndex == currentIndex) {
            return false;
        }

        CatalogComponent moved = categories.remove(currentIndex);
        categories.add(safeTargetIndex, moved);
        applyCategoryOrder(categories);

        recordEditorAction(
                "category.move",
                "category",
                category.getUuid(),
                null,
                "Moved category " + category.getId() + " to position " + (safeTargetIndex + 1)
        );
        reloadShopData();
        ShopUIEvents.invokeRefreshCategories();
        return true;
    }

    public boolean deleteDraftCategory(CatalogComponent category, @Nullable CatalogComponent moveTarget) {
        if (!isEditableCategory(category) || (moveTarget != null && sameCategory(category, moveTarget))) {
            return false;
        }

        int affectedOffers = 0;
        if (entriesContainer != null) {
            if (moveTarget == null) {
                List<UUID> removeIds = new ArrayList<>();
                for (ShopOffer offer : entriesContainer.getEntryMap().values()) {
                    if (isOfferInCategory(offer, category)) {
                        removeIds.add(offer.getUUID());
                    }
                }
                for (UUID offerId : removeIds) {
                    if (entriesContainer.removeEntry(offerId) != null) {
                        affectedOffers++;
                    }
                }
            } else {
                for (ShopOffer offer : entriesContainer.getEntryMap().values()) {
                    if (moveOfferToCategory(offer, category, moveTarget)) {
                        affectedOffers++;
                    }
                }
            }
        }

        removeEditorDraftCategory(category);
        reloadShopData();
        applyCategoryOrder(new ArrayList<>(getOrderedCategories()));

        recordEditorAction(
                moveTarget == null ? "category.delete" : "category.delete_move_offers",
                "category",
                category.getUuid(),
                null,
                moveTarget == null
                        ? "Deleted category " + category.getId() + " with " + affectedOffers + " offer(s)"
                        : "Deleted category " + category.getId() + " and moved " + affectedOffers + " offer(s) to " + moveTarget.getId()
        );
        reloadShopData();

        if (tabsPanel != null && tabsPanel.isSelectedCategory(category)) {
            ShopUIEvents.invokeSelectCategory(moveTarget);
        }
        ShopUIEvents.invokeRefreshAll();
        return true;
    }

    private boolean isEditableCategory(CatalogComponent category) {
        return isEditorMode()
                && editSession != null
                && !editSession.closed()
                && category != null
                && hasCategory(category);
    }

    private boolean isOfferInCategory(ShopOffer offer, CatalogComponent category) {
        if (offer == null || category == null) {
            return false;
        }

        return offer.getComponent(CatalogComponent.class)
                .map(offerCategory -> sameCategory(offerCategory, category))
                .orElse(false);
    }

    private boolean moveOfferToCategory(ShopOffer offer, CatalogComponent source, CatalogComponent target) {
        if (offer == null || source == null || target == null) {
            return false;
        }

        return offer.getComponent(CatalogComponent.class)
                .filter(offerCategory -> sameCategory(offerCategory, source))
                .map(offerCategory -> {
                    offerCategory.setId(target.getId());
                    offerCategory.setUuid(target.getUuid());
                    offerCategory.setOrder(target.getOrder());
                    return true;
                })
                .orElse(false);
    }

    private void forEachMatchingCategory(CatalogComponent category, java.util.function.Consumer<CatalogComponent> action) {
        if (category == null || action == null) {
            return;
        }

        boolean found = false;
        if (entriesContainer != null) {
            for (ShopOffer offer : entriesContainer.getEntryMap().values()) {
                for (CatalogComponent current : offer.getComponents(CatalogComponent.class)) {
                    if (sameCategory(current, category)) {
                        action.accept(current);
                        found = true;
                    }
                }
            }
        }

        for (CatalogComponent current : editorDraftCategories) {
            if (sameCategory(current, category)) {
                action.accept(current);
                found = true;
            }
        }

        if (!found) {
            action.accept(category);
        }
    }

    private void applyCategoryOrder(List<CatalogComponent> orderedCategories) {
        if (orderedCategories == null) {
            return;
        }

        for (int i = 0; i < orderedCategories.size(); i++) {
            int order = i;
            CatalogComponent category = orderedCategories.get(i);
            forEachMatchingCategory(category, current -> current.setOrder(order));
        }
    }

    private int indexOfCategory(List<CatalogComponent> categories, CatalogComponent category) {
        if (categories == null || category == null) {
            return -1;
        }

        for (int i = 0; i < categories.size(); i++) {
            if (sameCategory(categories.get(i), category)) {
                return i;
            }
        }
        return -1;
    }

    private static Comparator<CatalogComponent> categoryComparator() {
        return Comparator
                .comparingInt((CatalogComponent category) -> category == null ? 0 : category.getOrder())
                .thenComparing(category -> category == null || category.getId() == null ? "" : category.getId(), String.CASE_INSENSITIVE_ORDER);
    }

    private ObjectArrayList<CatalogComponent> mergeEditorCategories(ObjectArrayList<CatalogComponent> indexedCategories) {
        ObjectArrayList<CatalogComponent> merged = new ObjectArrayList<>();
        if (indexedCategories != null) {
            for (CatalogComponent category : indexedCategories) {
                addUniqueCategory(merged, category);
            }
        }

        for (CatalogComponent category : editorDraftCategories) {
            addUniqueCategory(merged, category);
        }

        return merged;
    }

    private void addUniqueCategory(ObjectArrayList<CatalogComponent> categories, CatalogComponent category) {
        for (CatalogComponent current : categories) {
            if (sameCategory(current, category)) {
                return;
            }
        }

        categories.add(category);
    }

    private CatalogComponent firstOrDefaultCategory() {
        if (catalogComponents != null && !catalogComponents.isEmpty()) {
            return catalogComponents.get(0);
        }

        CatalogComponent category = new CatalogComponent("default");
        editorDraftCategories.add(category);
        return category;
    }

    private void removeEditorDraftCategory(CatalogComponent category) {
        editorDraftCategories.removeIf(current -> sameCategory(current, category));
    }

    private String nextDraftCategoryId() {
        int index = 1;
        while (hasCategoryId("new_category_" + index)) {
            index++;
        }

        return "new_category_" + index;
    }

    private boolean hasCategoryId(String id) {
        if (catalogComponents == null) {
            return false;
        }

        for (CatalogComponent category : catalogComponents) {
            if (Objects.equals(category.getId(), id)) {
                return true;
            }
        }

        return false;
    }

    private void markEditorDirty() {
        if (editSession != null) {
            editSession.markDirty();
        }
    }

    private void recordEditorAction(String action, String entityType, UUID entityId, @Nullable ResourceLocation componentType, String summary) {
        if (editSession != null && !editSession.closed()) {
            editSession.recordHistory(action, entityType, entityId, componentType, summary);
        } else {
            markEditorDirty();
        }
    }

    protected boolean hasCategory(CatalogComponent category) {
        if (category == null || catalogComponents == null) {
            return false;
        }

        for (CatalogComponent current : catalogComponents) {
            if (sameCategory(current, category)) {
                return true;
            }
        }

        return false;
    }

    protected static boolean sameCategory(CatalogComponent first, CatalogComponent second) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        if (first.getUuid() != null && second.getUuid() != null) {
            return first.getUuid().equals(second.getUuid());
        }

        return java.util.Objects.equals(first.getId(), second.getId());
    }

    @Override
    public void alightWidget() {
        setSize(window.getGuiScaledWidth(), window.getGuiScaledHeight());
    }

    protected void alightWidgets() {
       /*
            cur_* = текущий N
            tsw_*  = виджет вкладок
        */
        final Size cur_size = getSize();
        final int cur_w     = cur_size.width;
        final int cur_h     = cur_size.height;
        final int x_w_space = 4;
        final int y_h_space = 4;

        toolPanel.setSize(
                cur_w - x_w_space * 2,
                25
        );
        toolPanel.setSelfPosition(
                x_w_space,
                y_h_space / 2
        );

        final int tsw_w         = cur_w  / 4;
        final int tsw_h_offset  = (cur_h / 4);
        final int tsw_h         = cur_h - tsw_h_offset * 2;

        final var sp_widgets    = toolPanel.getWidgets();
        final var sp_size       = toolPanel.getSize();
        for (int i = 0; i < sp_widgets.size(); i++) {
            final Widget sp_widget = sp_widgets.get(i);
            sp_widget.setSelfPositionY(
                    (sp_size.height - sp_widget.getSize().height) / 2
            );
        }

        /*
            ep_* = панель товаров
         */
        shopOffersPanel.setSize(
                cur_w  - tsw_w          - x_w_space  * 2,
                cur_h - sp_size.height - y_h_space  * 2
        );
        shopOffersPanel.setSelfPosition(
                tsw_w          + x_w_space,
                sp_size.height + y_h_space
        );

        final int tsw_x = 4;
        final int tsw_y = tsw_h_offset;
        tabsPanel.setSelfPosition(tsw_x, shopOffersPanel.getSelfPositionY());
        tabsPanel.setSize(
                tsw_w - 2,
                shopOffersPanel.getSizeHeight()
        );
    }

    @Override
    public void onScreenSizeUpdate(int screenWidth, int screenHeight) {
        super.onScreenSizeUpdate(screenWidth, screenHeight);
        alightWidget();
        alightWidgets();
        invokeAlightWidgets();
    }

    @Nullable
    public State getState(String id) {
        return states.get(id);
    }

    public void putState(String id, State state) {
        states.put(id, state);
    }

    public void updateState(String id, Object value) {
        State state = getState(id);
        if(state == null) return;
        state.setValue(value);
    }

    public void updateState(String id, State.Type type, Object defaultValue) {
        State state = getState(id);
        if(state == null) return;
        if(state.type != type)
            throw new IllegalArgumentException("Can't update state because '" + type.name() + " != " + state.type.name() + "' !");
        state.update(type, defaultValue);
    }

    public UIEventScope screenEventScope() {
        return screenEventScope;
    }

    public <Event> EventSubscription listenScreen(EventPtr<Event> event, DODEventBus.EventListener<Event> listener) {
        return screenEventScope.listen(event, listener);
    }

    @Override
    public void dispose() {
        screenEventScope.close();
        ShopUIUtils.disposeChildren(this);
        ShopToasts.clear();
        if (editSession != null) {
            editSession.close();
        }
        if (Instance == this) {
            Instance = null;
        }
    }

    public static class State {

        @Getter
        private Type type;

        @Getter
        private Object defaultValue;

        @Setter
        private @Nullable Object value;

        public State(Type type, Object defaultValue) {
            this(type, defaultValue, null);
        }

        public State(Type type, Object defaultValue, @Nullable Object value) {
            this.type = type;
            this.defaultValue = defaultValue;
            this.value = value;
        }

        public Object getValue() {
            return value == null ? defaultValue : value;
        }

        void update(Type type, Object defaultValue) {
            update(type, defaultValue, null);
        }

        void update(Type type, Object defaultValue, @Nullable Object value) {
            this.type = type;
            this.defaultValue = defaultValue;
            this.value = value;
        }

        public enum Type {
            String,
            Int,
            Bool,
            Double,
            Float,
            ShopEntity
        }
    }
}
