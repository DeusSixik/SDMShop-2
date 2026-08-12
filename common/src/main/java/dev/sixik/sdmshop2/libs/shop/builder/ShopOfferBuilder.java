package dev.sixik.sdmshop2.libs.shop.builder;

import dev.sixik.sdmshop2.libs.shop.components.ItemCostComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.money.MoneyCostComponent;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class ShopOfferBuilder implements ShopEntityBuilder<ShopOfferBuilder> {

    public static ShopOfferBuilder builder(String groupId) {
        return new ShopOfferBuilder(UUID.randomUUID(), groupId);
    }

    public static ShopOfferBuilder builder(UUID offerId, String groupId) {
        return new ShopOfferBuilder(offerId, groupId);
    }

    @Getter
    protected final UUID offerId;

    @Getter
    protected final String groupId;

    @Getter
    private final ObjectArrayList<ShopComponent> components = new ObjectArrayList<>();

    protected ShopOfferBuilder(UUID offerId, String groupId) {
        this.offerId = offerId;
        this.groupId = groupId;
    }

    public ShopOfferBuilder addPrice(String currencyId, double amount) {
        return addPrice(currencyId, amount, "");
    }

    public ShopOfferBuilder addPrice(String currencyId, double amount, String groupId) {
        if (currencyId == null || currencyId.isEmpty()) {
            throw new IllegalArgumentException("Currency ID cannot be null or empty");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        ResourceLocation id = currencyId.contains(":") ? ResourceLocation.tryParse(currencyId) : ResourceLocation.tryBuild("sdm", currencyId);
        if (id == null) {
            throw new IllegalArgumentException("Currency ID is invalid: " + currencyId);
        }

        MoneyCostComponent component = new MoneyCostComponent(id, amount);
        component.setGroupId(groupId == null ? "" : groupId);
        components.add(component);
        return this;
    }

    public ShopOfferBuilder addItemPrice(ItemStack itemStack, int amount) {
        return addItemPrice(itemStack, amount, "");
    }

    public ShopOfferBuilder addItemPrice(ItemStack itemStack, int amount, String groupId) {
        if (itemStack == null || itemStack.isEmpty()) {
            throw new IllegalArgumentException("ItemStack cannot be null or empty");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        ItemCostComponent component = new ItemCostComponent(itemStack, amount);
        component.setGroupId(groupId == null ? "" : groupId);
        components.add(component);
        return this;
    }

    public ShopOfferBuilder end() {
        return this;
    }
}
