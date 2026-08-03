package dev.sixik.sdmshop2.libs.shop.client.ui.elements;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.utils.Size;
import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUiElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.base.ShopDraggableScrollableWidgetGroup;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUIUtils;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.StyleApi;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIDisposable;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.UIEventScope;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetContextRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.WidgetRender;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.DODEventBus;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventPtr;
import dev.sixik.sdmshop2.libs.platform.utils.eventbus.EventSubscription;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ShopOffersPanelElement extends ShopDraggableScrollableWidgetGroup implements
        ShopUiElement, WidgetContextRender, UIDisposable {

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
        if (render.mouseClicked(this, mouseX, mouseY, button)) {
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
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
        eventScope.clear();
        ShopUIUtils.disposeChildren(this);
        clearAllWidgets();
        render.addWidgets(this);

        if (relayout) {
            alightWidget();
        }

        return this;
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

    public <Event> EventSubscription listenScreen(EventPtr<Event> event, DODEventBus.EventListener<Event> listener) {
        return shopScreen.listenScreen(event, listener);
    }

    @Override
    public void dispose() {
        eventScope.close();
        ShopUIUtils.disposeChildren(this);
    }
}
