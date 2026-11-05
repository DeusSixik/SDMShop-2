package dev.sixik.sdmshop2.libs.shop.client.ui.api;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.cache.ShopClientCache;
import dev.sixik.sdmshop2.libs.shop.components.misc.CatalogComponent;

import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import java.util.*;

/** Snapshot of catalog presentation data, independent of widget layout. */
public class ShopCatalogItem implements ShopOfferView {
    public final @Nullable ShopOffer offer;
    public final @Nullable UUID uuid;
    public final String categoryId;
    public final String title;
    public final List<ItemStack> rewards;
    public final List<ShopOfferView.CostGroup> costGroups;
    public final double discountPercent;

    public ShopCatalogItem(ShopOffer offer) {
        this.offer = offer;
        this.uuid = offer.getUUID();
        CatalogComponent cat = offer.getComponent(CatalogComponent.class).orElse(null);
        this.categoryId = cat != null && cat.getId() != null ? cat.getId() : "ALL";
        this.title = ShopOfferPresentation.resolveTitle(offer);
        this.rewards = ShopOfferPresentation.resolveRewardItems(offer);
        this.costGroups = ShopOfferPresentation.resolveCostGroups(offer);
        this.discountPercent = ShopOfferPresentation.resolveDiscount(offer);
    }

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
        this.rewards = List.copyOf(rewards);
        this.costGroups = List.copyOf(costGroups);
        this.discountPercent = discountPercent;
    }

    public boolean isFavorite() {
        return uuid != null && ShopClientCache.isFavorite(uuid);
    }

    /** {@inheritDoc} */
    @Override
    public String getTitle() {
        return title;
    }

    /** {@inheritDoc} */
    @Override
    public List<ItemStack> getRewards() {
        return rewards;
    }

    /** {@inheritDoc} */
    @Override
    public List<ShopOfferView.CostGroup> getCostGroups() {
        return costGroups;
    }

    /** {@inheritDoc} */
    @Override
    public double getDiscountPercent() {
        return discountPercent;
    }
}
