package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import dev.sixik.sdmshop2.libs.platform.utils.eventbus.DODEventBus;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventPtr;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventSubscription;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUIUtils;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIDisposable;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIEventScope;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.base.ShopDraggableScrollableWidgetGroup;
import dev.sixik.sdmshop2.libs.shop.components.api.ConditionComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.RewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponentCategory;
import dev.sixik.sdmshop2.libs.shop.components.conditions.CooldownConditionComponent;
import dev.sixik.sdmshop2.libs.shop.components.limiter.LimiterComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.NameComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.RenderHideComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.ShopOffersContainerComponent;
import dev.sixik.sdmshop2.libs.shop.components.money.MoneyCostComponent;
import dev.sixik.sdmshop2.libs.shop.components.utils.ShopComponentsUtils;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.sdmshop2.libs.shop.network.ShopNetworkManager;
import dev.sixik.unigui.api.core.FrameContext;
import dev.sixik.unigui.api.layout.EdgeInsets;
import dev.sixik.unigui.api.widget.Widget;
import it.unimi.dsi.fastutil.objects.ObjectList;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/** Filtered, scrollable UniGUI offer list. */
public class ShopOffersPanelElement extends ShopDraggableScrollableWidgetGroup implements WidgetContextRender, UIDisposable {
    private static final long CONDITION_REFRESH_THROTTLE_MS = 250L;

    @Getter private final @NotNull ShopScreenElement shopScreen;
    private final UIEventScope eventScope = new UIEventScope();
    @Getter private String searchText = "";
    @Getter private Set<ResourceLocation> selectedCurrencyFilters = Set.of();
    @Getter private @Nullable CatalogComponent selectedCategory;
    private final Map<UUID, Map<ConditionComponent, Boolean>> serverConditionCache = new HashMap<>();
    private final Set<UUID> pendingConditionRequests = new HashSet<>();
    private long nextConditionRefreshAtMs = Long.MAX_VALUE;
    private long lastConditionAutoRefreshMs;

    public ShopOffersPanelElement(@NotNull ShopScreenElement shopScreen) {
        this.shopScreen = shopScreen;
        contentBox().spacing(6.0f);
        contentBox().layout(style -> style.padding(EdgeInsets.all(6.0f)));
        registerHandlers();
        refresh(false);
    }

    private void registerHandlers() {
        listenScreen(ShopUIEvents.SEARCH_UPDATE, event -> setSearchText(event.text()));
        listenScreen(ShopUIEvents.CURRENCY_FILTER_UPDATE, event -> setSelectedCurrencyFilters(event.currencies()));
        listenScreen(ShopUIEvents.FAVORITES_CHANGED, event -> refresh(false));
        listenScreen(ShopUIEvents.SELECT_CATEGORY, event -> setSelectedCategory(event.selected()));
        listenScreen(ShopUIEvents.REFRESH_UI, event -> { if (shouldRefreshFor(event)) refresh(event.relayout()); });
        listenScreen(ShopUIEvents.SORT_SHOP_OFFERS, event -> {
            if (event.panel() == this) event.sort(Comparator.comparing(offer -> !ShopClientCache.isFavorite(offer.getUUID())));
        });
    }

    public void rebuildOffers() { refresh(true); }
    public void setSearchText(String value) { String safe = value == null ? "" : value.trim(); if (!searchText.equals(safe)) { searchText = safe; refresh(false); } }
    public void setSelectedCategory(@Nullable CatalogComponent value) { if (!sameCategory(selectedCategory, value)) { selectedCategory = value; refresh(false); } }
    public void setSelectedCurrencyFilters(Set<ResourceLocation> value) {
        Set<ResourceLocation> safe = value == null || value.isEmpty() ? Set.of() : Set.copyOf(value);
        if (!selectedCurrencyFilters.equals(safe)) { selectedCurrencyFilters = safe; refresh(false); }
    }
    public boolean isSelectedCategory(@Nullable CatalogComponent category) { return sameCategory(selectedCategory, category); }
    public boolean isEditorMode() { return shopScreen.isEditorMode(); }

    public void createDraftOffer() {
        ShopOffer offer = shopScreen.createDraftOffer(selectedCategory);
        if (offer != null) ShopUIUtils.createEditMenu(this, getEditSession(), offer, this::rebuildOffers);
    }

    public void duplicateDraftOffer(ShopOffer offer) {
        if (offer != null && shopScreen.duplicateDraftOffer(offer.getUUID()) != null) rebuildOffers();
    }

    public void requestServerConditions(Collection<ShopOffer> offers) {
        if (offers == null || offers.isEmpty() || isEditorMode()) return;
        List<ShopOffer> request = offers.stream().filter(Objects::nonNull)
                .filter(offer -> offer.getComponents(ConditionComponent.class).stream().anyMatch(condition -> !condition.verifiedOnClient()))
                .filter(offer -> !serverConditionCache.containsKey(offer.getUUID()))
                .filter(offer -> pendingConditionRequests.add(offer.getUUID())).toList();
        if (request.isEmpty()) return;
        ShopNetworkManager.fetchServerConditions(request).whenComplete((conditions, error) -> Minecraft.getInstance().execute(() -> {
            request.forEach(offer -> pendingConditionRequests.remove(offer.getUUID()));
            if (error == null && conditions != null) { serverConditionCache.putAll(conditions); refresh(false); }
        }));
    }

    public void scheduleConditionRefresh(Collection<ShopOffer> offers) {
        if (offers == null || offers.isEmpty() || isEditorMode() || Minecraft.getInstance().player == null) { nextConditionRefreshAtMs = Long.MAX_VALUE; return; }
        long now = System.currentTimeMillis();
        long next = Long.MAX_VALUE;
        for (ShopOffer offer : offers) {
            if (offer == null || !offer.hasComponent(RenderHideComponent.class)) continue;
            for (CooldownConditionComponent condition : offer.getComponents(CooldownConditionComponent.class)) {
                try { long at = condition.getAvailableAtMs(Minecraft.getInstance().player); if (at > now) next = Math.min(next, at); } catch (Exception ignored) { }
            }
            for (LimiterComponent limiter : offer.getComponents(LimiterComponent.class)) {
                try { long at = limiter.getAvailableAtMs(Minecraft.getInstance().player); if (at > now) next = Math.min(next, at); } catch (Exception ignored) { }
            }
        }
        nextConditionRefreshAtMs = next;
    }

    public boolean shouldRenderOffer(ShopOffer offer) { return offer == null || isEditorMode() || !offer.hasComponent(RenderHideComponent.class) || isOfferAvailable(offer); }
    public boolean isOfferAvailable(ShopOffer offer) {
        if (offer == null) return false;
        for (ConditionComponent condition : offer.getComponents(ConditionComponent.class)) {
            if (condition.verifiedOnClient()) {
                try { if (Minecraft.getInstance().player == null || !condition.isChecked(Minecraft.getInstance().player)) return false; }
                catch (Exception ignored) { return false; }
            } else {
                Map<ConditionComponent, Boolean> result = serverConditionCache.get(offer.getUUID());
                if (result != null && Boolean.FALSE.equals(result.get(condition))) return false;
            }
        }
        return true;
    }

    public List<ShopOffer> applyCustomSort(List<ShopOffer> offers) { ShopUIEvents.invokeSortShopOffers(this, offers, searchText); return offers; }
    public ShopOffersPanelElement refresh() { return refresh(true); }
    public ShopOffersPanelElement refresh(boolean relayout) {
        clearWidgets();
        ShopOffersContainerComponent entries = shopScreen.getEntriesContainer();
        if (entries == null) return this;
        List<ShopOffer> source = new ArrayList<>(entries.getEntryMap().values());
        requestServerConditions(source);
        scheduleConditionRefresh(source);
        String query = normalize(searchText);
        List<ShopOffer> visible = source.stream().map(OfferView::from)
                .filter(view -> shouldRenderOffer(view.offer()))
                .filter(view -> selectedCategory == null || isInCategory(view.offer(), selectedCategory))
                .filter(view -> selectedCurrencyFilters.isEmpty() || view.hasAnyCurrency(selectedCurrencyFilters))
                .filter(view -> query.isEmpty() || view.searchTitle().contains(query))
                .sorted(Comparator.comparing(OfferView::sortMoneyId).thenComparing(OfferView::sortTitle).thenComparing(view -> view.offer().getUUID()))
                .map(OfferView::offer).collect(Collectors.toCollection(ArrayList::new));
        applyCustomSort(visible);
        for (ShopOffer offer : visible) addWidget(new ShopOfferElement(getEditSession(), offer));
        if (isEditorMode()) addWidget(ShopOfferElement.editorAction(getEditSession(), Component.translatable("shop.ui.offers.button.add_offer"), this::createDraftOffer));
        return this;
    }

    @Override public void tick(FrameContext context) {
        super.tick(context);
        long now = System.currentTimeMillis();
        if (nextConditionRefreshAtMs != Long.MAX_VALUE && now >= nextConditionRefreshAtMs && now - lastConditionAutoRefreshMs >= CONDITION_REFRESH_THROTTLE_MS) {
            lastConditionAutoRefreshMs = now; nextConditionRefreshAtMs = Long.MAX_VALUE; refresh(false);
        }
    }

    protected boolean shouldRefreshFor(ShopUIEvents.RefreshUI event) {
        return event.affectsOffers() && (event.target() != ShopUIEvents.RefreshTarget.CATEGORY || selectedCategory == null || event.affectsCategory(selectedCategory));
    }

    protected static boolean sameCategory(@Nullable CatalogComponent first, @Nullable CatalogComponent second) {
        if (first == second) return true;
        if (first == null || second == null) return false;
        if (first.getUuid() != null && second.getUuid() != null) return first.getUuid().equals(second.getUuid());
        return Objects.equals(first.getId(), second.getId());
    }

    private static boolean isInCategory(ShopOffer offer, CatalogComponent selected) {
        return offer.getComponent(CatalogComponent.class).map(category -> sameCategory(category, selected)).orElse(false);
    }
    private static String normalize(String value) { return value == null ? "" : value.trim().toLowerCase(Locale.ROOT); }
    private static String resolveTitle(ShopOffer offer) {
        return offer.getComponent(NameComponent.class).map(NameComponent::getName).filter(name -> !name.isBlank())
                .map(name -> I18n.exists(name) ? Component.translatable(name).getString() : name).orElseGet(() -> resolveRewardTitle(offer));
    }
    private static String resolveRewardTitle(ShopOffer offer) {
        Map<ShopComponentCategory, ObjectList<ShopComponent>> grouped = ShopComponentsUtils.getComponentsByCategory(offer);
        ObjectList<ShopComponent> rewards = grouped.get(ShopComponentCategory.REWARD);
        if (rewards != null && !rewards.isEmpty() && rewards.get(0) instanceof RewardComponent reward) {
            Component title = reward.getDisplayTitle(); if (title != null && !title.getString().isBlank()) return title.getString();
        }
        return offer.getUUID().toString();
    }
    private static String currencySearch(ShopOffer offer) {
        return offer.getComponents(MoneyCostComponent.class).stream().map(MoneyCostComponent::getMoneyId).distinct().map(id -> {
            ICurrency currency = SDMEconomyServiceClient.getCurrency(id);
            return (id + " " + (currency == null ? "" : currency.getDisplayName().getString())).toLowerCase(Locale.ROOT);
        }).collect(Collectors.joining(" "));
    }

    private record OfferView(ShopOffer offer, String sortTitle, String searchTitle, String sortMoneyId, Set<ResourceLocation> moneyIds) {
        static OfferView from(ShopOffer offer) {
            String title = resolveTitle(offer).toLowerCase(Locale.ROOT);
            Set<ResourceLocation> ids = offer.getComponents(MoneyCostComponent.class).stream().map(MoneyCostComponent::getMoneyId).collect(Collectors.toUnmodifiableSet());
            String money = ids.stream().map(Object::toString).findFirst().orElse("~~~~");
            return new OfferView(offer, title, title + " " + currencySearch(offer), money, ids);
        }
        boolean hasAnyCurrency(Set<ResourceLocation> currencies) { return moneyIds.stream().anyMatch(currencies::contains); }
    }

    public void alightOffers() { alightWidget(); }
    @Override public @Nullable ShopEntity getShopEntity() { return null; }
    @Override public Widget getOwner() { return this; }
    @Override public UIEventScope eventScope() { return eventScope; }
    @Override public UIEventScope screenEventScope() { return shopScreen.screenEventScope(); }
    @Override public @Nullable ShopEditSession getEditSession() { return shopScreen.getEditSession(); }
    public <Event> EventSubscription listenScreen(EventPtr<Event> event, DODEventBus.EventListener<Event> listener) { return shopScreen.listenScreen(event, listener); }
    @Override public void dispose() { eventScope.close(); super.dispose(); }
}
