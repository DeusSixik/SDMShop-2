package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.DODEventBus;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventPtr;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventSubscription;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.*;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop.client.ui.style.DefaultShopOffersPanelRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.base.ShopDraggableScrollableWidgetGroup;
import dev.sixik.sdmshop2.libs.shop.components.api.ConditionComponent;
import dev.sixik.sdmshop2.libs.shop.components.conditions.CooldownConditionComponent;
import dev.sixik.sdmshop2.libs.shop.components.limiter.LimiterComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.shop.components.misc.RenderHideComponent;
import dev.sixik.sdmshop2.libs.shop.editor.ShopEditSession;
import dev.sixik.sdmshop2.libs.shop.network.ShopNetworkManager;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ShopOffersPanelElement extends ShopDraggableScrollableWidgetGroup implements
        ShopUiElement, WidgetContextRender, UIDisposable {

    private static final long CONDITION_REFRESH_THROTTLE_MS = 250L;

    @Getter
    protected final @NotNull ShopScreenElement shopScreen;
    protected final WidgetRender render;
    protected final UIEventScope eventScope = new UIEventScope();
    protected boolean defaultHandlersRegistered;
    @Getter
    protected String searchText = "";
    @Getter
    protected Set<ResourceLocation> selectedCurrencyFilters = Set.of();
    @Getter
    protected @Nullable CatalogComponent selectedCategory;
    protected final Map<UUID, Map<ConditionComponent, Boolean>> serverConditionCache = new HashMap<>();
    protected final Set<UUID> pendingConditionRequests = new HashSet<>();
    protected long nextConditionRefreshAtMs = Long.MAX_VALUE;
    protected long lastConditionAutoRefreshMs;

    public ShopOffersPanelElement(@NotNull ShopScreenElement shopScreen) {
        this(shopScreen, StyleApi.getDefaultStyle(StyleApi.Category.OffersPanel).get());
    }

    public ShopOffersPanelElement(@NotNull ShopScreenElement shopScreen, WidgetRender render) {
        this.shopScreen = shopScreen;
        this.render = Objects.requireNonNull(render, "render");

        this.render.constructor(this);
        if (shopScreen.getTabsPanel() != null) {
            this.selectedCategory = shopScreen.getTabsPanel().getSelectedCategory();
        }
        registerDefaultHandlers();
        refresh(false);
    }

    protected void registerDefaultHandlers() {
        if (defaultHandlersRegistered) {
            return;
        }

        defaultHandlersRegistered = true;
        listenScreen(ShopUIEvents.SEARCH_UPDATE, event -> setSearchText(event.text()));
        listenScreen(ShopUIEvents.CURRENCY_FILTER_UPDATE, event -> setSelectedCurrencyFilters(event.currencies()));
        listenScreen(ShopUIEvents.FAVORITES_CHANGED, event -> refresh(initialized));
        listenScreen(ShopUIEvents.SELECT_CATEGORY, event -> setSelectedCategory(event.selected()));
        listenScreen(ShopUIEvents.REFRESH_UI, event -> {
            if (shouldRefreshFor(event)) {
                refresh(initialized && event.relayout());
            }
        });
        listenScreen(ShopUIEvents.SORT_SHOP_OFFERS, event -> {
            if (event.panel() != this) {
                return;
            }

            event.sort((first, second) -> Boolean.compare(
                    !ShopClientCache.isFavorite(first.getUUID()),
                    !ShopClientCache.isFavorite(second.getUUID())
            ));
        });
    }

    @Override
    public void initWidget() {
        super.initWidget();
    }

    @Override
    public void alightWidget() {
        if (!initialized) return;
        render.alightWidgets(this);
    }

    @Override
    protected void alightWidgets() {
        alightWidget();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isInsideOffersViewport(mouseX, mouseY)) {
            return false;
        }

        if (render.mouseClicked(this, mouseX, mouseY, button)) {
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public Widget getHoverElement(double mouseX, double mouseY) {
        return isInsideOffersViewport(mouseX, mouseY) ? super.getHoverElement(mouseX, mouseY) : null;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return isInsideOffersViewport(mouseX, mouseY) && super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return isInsideOffersViewport(mouseX, mouseY) && super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseWheelMove(double mouseX, double mouseY, double wheelDelta) {
        return isInsideOffersViewport(mouseX, mouseY) && super.mouseWheelMove(mouseX, mouseY, wheelDelta);
    }

    protected boolean isInsideOffersViewport(double mouseX, double mouseY) {
        return mouseX >= getPositionX()
                && mouseY >= getPositionY()
                && mouseX < getPositionX() + getSizeWidth()
                && mouseY < getPositionY() + getSizeHeight();
    }

    @Override
    public void setSize(Size size) {
        super.setSize(size);
        if (!getContainedWidgets(true).isEmpty()) {
            alightWidget();
        }
    }

    public void rebuildOffers() {
        refresh(true);
    }

    public void setSearchText(String searchText) {
        String safeSearchText = searchText == null ? "" : searchText.trim();
        if (this.searchText.equals(safeSearchText)) {
            return;
        }

        this.searchText = safeSearchText;
        refresh(initialized);
    }

    public void setSelectedCategory(@Nullable CatalogComponent selectedCategory) {
        if (sameCategory(this.selectedCategory, selectedCategory)) {
            return;
        }

        this.selectedCategory = selectedCategory;
        refresh(initialized);
    }

    public void setSelectedCurrencyFilters(Set<ResourceLocation> selectedCurrencyFilters) {
        Set<ResourceLocation> safeFilters = selectedCurrencyFilters == null || selectedCurrencyFilters.isEmpty()
                ? Set.of()
                : Set.copyOf(selectedCurrencyFilters);
        if (this.selectedCurrencyFilters.equals(safeFilters)) {
            return;
        }

        this.selectedCurrencyFilters = safeFilters;
        refresh(initialized);
    }

    public boolean isSelectedCategory(@Nullable CatalogComponent category) {
        return sameCategory(selectedCategory, category);
    }

    public boolean isEditorMode() {
        return shopScreen.isEditorMode();
    }

    public void createDraftOffer() {
        ShopOffer offer = shopScreen.createDraftOffer(selectedCategory);
        if (offer != null) {
            ShopUIUtils.createEditMenu(this, getEditSession(), offer, this::rebuildOffers);
        }
    }

    public void duplicateDraftOffer(ShopOffer offer) {
        if (offer == null) {
            return;
        }

        ShopOffer duplicate = render instanceof DefaultShopOffersPanelRender offersRender
                ? offersRender.duplicateOffer(this, offer)
                : shopScreen.duplicateDraftOffer(offer.getUUID());
        if (duplicate != null) {
            rebuildOffers();
        }
    }

    public void requestServerConditions(Collection<ShopOffer> offers) {
        if (offers == null || offers.isEmpty() || isEditorMode()) {
            return;
        }

        List<ShopOffer> requestOffers = offers.stream()
                .filter(Objects::nonNull)
                .filter(offer -> offer.getComponents(ConditionComponent.class).stream().anyMatch(condition -> !condition.verifiedOnClient()))
                .filter(offer -> !serverConditionCache.containsKey(offer.getUUID()))
                .filter(offer -> pendingConditionRequests.add(offer.getUUID()))
                .toList();
        if (requestOffers.isEmpty()) {
            return;
        }

        ShopNetworkManager.fetchServerConditions(requestOffers).whenComplete((conditions, throwable) -> Minecraft.getInstance().execute(() -> {
            requestOffers.forEach(offer -> pendingConditionRequests.remove(offer.getUUID()));
            if (throwable != null || conditions == null) {
                return;
            }

            serverConditionCache.putAll(conditions);
            refresh(initialized);
        }));
    }

    public void scheduleConditionRefresh(Collection<ShopOffer> offers) {
        if (offers == null || offers.isEmpty() || isEditorMode()) {
            nextConditionRefreshAtMs = Long.MAX_VALUE;
            return;
        }

        if (Minecraft.getInstance().player == null) {
            nextConditionRefreshAtMs = Long.MAX_VALUE;
            return;
        }

        long now = System.currentTimeMillis();
        long nextRefreshAtMs = Long.MAX_VALUE;
        for (ShopOffer offer : offers) {
            if (offer == null || !offer.hasComponent(RenderHideComponent.class)) {
                continue;
            }

            nextRefreshAtMs = Math.min(nextRefreshAtMs, findNextCooldownRefreshAtMs(offer, now));
            nextRefreshAtMs = Math.min(nextRefreshAtMs, findNextLimiterRefreshAtMs(offer, now));
        }

        nextConditionRefreshAtMs = nextRefreshAtMs;
    }

    public boolean shouldRenderOffer(ShopOffer offer) {
        if (offer == null || isEditorMode() || !offer.hasComponent(RenderHideComponent.class)) {
            return true;
        }

        return isOfferAvailable(offer);
    }

    public boolean isOfferAvailable(ShopOffer offer) {
        if (offer == null) {
            return false;
        }

        for (ConditionComponent condition : offer.getComponents(ConditionComponent.class)) {
            if (condition.verifiedOnClient()) {
                if (!isClientConditionChecked(condition)) {
                    return false;
                }
                continue;
            }

            Map<ConditionComponent, Boolean> serverConditions = serverConditionCache.get(offer.getUUID());
            if (serverConditions != null && Boolean.FALSE.equals(serverConditions.get(condition))) {
                return false;
            }
        }

        return true;
    }

    private boolean isClientConditionChecked(ConditionComponent condition) {
        if (Minecraft.getInstance().player == null) {
            return false;
        }

        try {
            return condition.isChecked(Minecraft.getInstance().player);
        } catch (Exception ignored) {
            return false;
        }
    }

    private long findNextCooldownRefreshAtMs(ShopOffer offer, long now) {
        long nextRefreshAtMs = Long.MAX_VALUE;
        for (CooldownConditionComponent condition : offer.getComponents(CooldownConditionComponent.class)) {
            try {
                long availableAtMs = condition.getAvailableAtMs(Minecraft.getInstance().player);
                if (availableAtMs > now) {
                    nextRefreshAtMs = Math.min(nextRefreshAtMs, availableAtMs);
                }
            } catch (Exception ignored) {
            }
        }

        return nextRefreshAtMs;
    }

    private long findNextLimiterRefreshAtMs(ShopOffer offer, long now) {
        long nextRefreshAtMs = Long.MAX_VALUE;
        for (LimiterComponent limiter : offer.getComponents(LimiterComponent.class)) {
            try {
                long availableAtMs = limiter.getAvailableAtMs(Minecraft.getInstance().player);
                if (availableAtMs > now) {
                    nextRefreshAtMs = Math.min(nextRefreshAtMs, availableAtMs);
                }
            } catch (Exception ignored) {
            }
        }

        return nextRefreshAtMs;
    }

    protected boolean shouldRefreshFor(ShopUIEvents.RefreshUI event) {
        if (!event.affectsOffers()) {
            return false;
        }

        if (event.target() == ShopUIEvents.RefreshTarget.CATEGORY) {
            return selectedCategory == null || event.affectsCategory(selectedCategory);
        }

        return true;
    }

    protected static boolean sameCategory(@Nullable CatalogComponent first, @Nullable CatalogComponent second) {
        if (first == second) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        if (first.getUuid() != null && second.getUuid() != null) {
            return first.getUuid().equals(second.getUuid());
        }

        return Objects.equals(first.getId(), second.getId());
    }

    public List<ShopOffer> applyCustomSort(List<ShopOffer> offers) {
        ShopUIEvents.invokeSortShopOffers(this, offers, searchText);
        return offers;
    }

    public ShopOffersPanelElement refresh() {
        return refresh(true);
    }

    public ShopOffersPanelElement refresh(boolean relayout) {
        int previousScrollY = getScrollYOffset();
        eventScope.clear();
        ShopUIUtils.disposeChildren(this);
        clearAllWidgets();
        render.addWidgets(this);

        if (relayout) {
            alightWidget();
            restoreScrollY(previousScrollY);
        }

        return this;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        long nextRefreshAtMs = nextConditionRefreshAtMs;
        if (nextRefreshAtMs == Long.MAX_VALUE) {
            return;
        }

        long now = System.currentTimeMillis();
        if (now < nextRefreshAtMs || now - lastConditionAutoRefreshMs < CONDITION_REFRESH_THROTTLE_MS) {
            return;
        }

        lastConditionAutoRefreshMs = now;
        nextConditionRefreshAtMs = Long.MAX_VALUE;
        refresh(initialized);
    }

    protected void restoreScrollY(int previousScrollY) {
        int maxScrollY = Math.max(0, computeContentHeight() - getSizeHeight());
        setScrollYOffset(Math.min(Math.max(0, previousScrollY), maxScrollY));
    }

    protected int computeContentHeight() {
        int height = 0;
        for (Widget widget : widgets) {
            height = Math.max(height, widget.getSelfPositionY() + widget.getSizeHeight());
        }
        return height;
    }

    public void alightOffers() {
        alightWidget();
    }

    @Override
    @Nullable
    public ShopEntity getShopEntity() {
        return null;
    }

    @Override
    public Widget getOwner() {
        return this;
    }

    @Override
    public UIEventScope eventScope() {
        return eventScope;
    }

    @Override
    public UIEventScope screenEventScope() {
        return shopScreen.screenEventScope();
    }

    @Override
    public @Nullable ShopEditSession getEditSession() {
        return shopScreen.getEditSession();
    }

    public <Event> EventSubscription listenScreen(EventPtr<Event> event, DODEventBus.EventListener<Event> listener) {
        return shopScreen.listenScreen(event, listener);
    }

    @Override
    public void dispose() {
        eventScope.close();
        ShopUIUtils.disposeChildren(this);
    }
}
