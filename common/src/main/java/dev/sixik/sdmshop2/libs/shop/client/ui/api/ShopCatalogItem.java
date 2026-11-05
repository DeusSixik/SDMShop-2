package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopRenderWidgets;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;

import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import java.util.*;

/** Snapshot of catalog presentation data, independent of widget layout. */
public class ShopCatalogItem implements ShopOfferView {
    /** Source offer, or null for a standalone preview. */
    public final @Nullable ShopOffer offer;

    /** Offer or preview identifier, or null for anonymous previews. */
    public final @Nullable UUID uuid;

    /** Category identifier used by the catalog query. */
    public final String categoryId;

    /** Visible title independent of component widget layout. */
    public final String title;

    /** Component factories, independent of widget type or reward kind. */
    public final List<ShopRenderElement> renderElements;

    /** Alternative payment groups; each real cost supplies its own widget factory. */
    public final List<ShopOfferView.CostGroup> costGroups;

    /**
     * Captures an offer's metadata and component factories without creating any widgets.
     *
     * @param offer source offer; component order is preserved.
     */
    public ShopCatalogItem(ShopOffer offer) {
        this.offer = offer;
        this.uuid = offer.getUUID();
        CatalogComponent cat = offer.getComponent(CatalogComponent.class).orElse(null);
        this.categoryId = cat != null && cat.getId() != null ? cat.getId() : "ALL";
        this.title = ShopOfferPresentation.resolveTitle(offer);
        this.renderElements = ShopOfferPresentation.resolveRenderElements(offer);
        this.costGroups = ShopOfferPresentation.resolveCostGroups(offer);
    }

    /**
     * Adapts a legacy item-only preview to the component factory model.
     *
     * @param uuid preview identifier.
     * @param categoryId category filter identifier.
     * @param title visible title.
     * @param rewards legacy item previews.
     * @param costGroups alternative payment groups.
     * @param discountPercent legacy displayed discount, in percentage points.
     * @deprecated supply arbitrary render elements using the factory-based constructor.
     */
    @Deprecated
    public ShopCatalogItem(
            UUID uuid,
            String categoryId,
            String title,
            List<ItemStack> rewards,
            List<ShopOfferView.CostGroup> costGroups,
            double discountPercent) {
        this.offer = null;
        this.uuid = uuid;
        this.categoryId = categoryId;
        this.title = title;
        this.renderElements = legacyElements(rewards, discountPercent);
        this.costGroups = List.copyOf(costGroups);
    }

    /**
     * Creates a presentation from arbitrary factories, without a server offer.
     *
     * @param uuid preview identifier.
     * @param categoryId category filter identifier.
     * @param title visible title.
     * @param elements factories for arbitrary component widgets.
     * @param costs alternative payment groups.
     */
    public ShopCatalogItem(
            UUID uuid,
            String categoryId,
            String title,
            List<ShopRenderElement> elements,
            List<CostGroup> costs) {
        this.offer = null;
        this.uuid = uuid;
        this.categoryId = categoryId;
        this.title = title;
        this.renderElements = List.copyOf(elements);
        this.costGroups = List.copyOf(costs);
    }

    /** {@inheritDoc} */
    @Override
    public List<ShopRenderElement> getRenderElements() {
        return renderElements;
    }

    /**
     * Adapts the old item-only preview constructor to widget factories.
     *
     * @param rewards legacy item previews.
     * @param discountPercent legacy displayed percent, in percentage points.
     * @return independent factories; new code should supply its own render elements.
     */
    public static List<ShopRenderElement> legacyElements(
            List<ItemStack> rewards, double discountPercent) {
        List<ShopRenderElement> elements = new ArrayList<>();
        for (ItemStack reward : rewards) {
            ItemStack snapshot = reward.copy();
            elements.add(() -> ShopRenderWidgets.item(snapshot.copy()));
        }
        if (discountPercent > 0)
            elements.add(() -> ShopRenderWidgets.badge("-" + discountPercent + "%"));
        return List.copyOf(elements);
    }

    /**
     * @return true if the offer is marked as a favorite in the client cache.
     */
    public boolean isFavorite() {
        return uuid != null && ShopClientCache.isFavorite(uuid);
    }

    /** {@inheritDoc} */
    @Override
    public String getTitle() {
        return title;
    }

    /** Legacy typed accessor; rendering uses getRenderElements instead. */
    @Deprecated
    public List<ItemStack> getRewards() {
        return offer != null ? ShopOfferPresentation.resolveRewardItems(offer) : List.of();
    }

    /** {@inheritDoc} */
    @Override
    public List<ShopOfferView.CostGroup> getCostGroups() {
        return costGroups;
    }

    /** Legacy typed accessor; rendering uses getRenderElements instead. */
    @Deprecated
    public double getDiscountPercent() {
        return offer != null ? ShopOfferPresentation.resolveDiscount(offer) : 0;
    }
}
