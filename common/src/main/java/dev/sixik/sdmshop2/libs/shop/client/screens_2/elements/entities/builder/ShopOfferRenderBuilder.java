package dev.sixik.sdmshop2.libs.shop.client.screens_2.elements.entities.builder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Getter
public final class ShopOfferRenderBuilder {

    private final ObjectArrayList<Badge> badges = new ObjectArrayList<>();

    public ShopOfferRenderBuilder() { }

    public ShopOfferRenderBuilder addBadge(@Nullable Object icon, @Nullable String text) {
        return addBadge(new Badge(icon, text));
    }

    public ShopOfferRenderBuilder addBadge(Badge badge) {
        this.badges.add(badge);
        return this;
    }

    /**
     * @param icon can be {@link ItemStack}, {@link Item}, {@link Ingredient}, {@link ResourceLocation}
     */
    public record Badge(@Nullable Object icon, @Nullable String text) {

        public Badge {
            if(icon == null && text == null)
                throw new NullPointerException("Can't 'icon' and 'text' be null. Someone one must have value");
        }
    }
}
