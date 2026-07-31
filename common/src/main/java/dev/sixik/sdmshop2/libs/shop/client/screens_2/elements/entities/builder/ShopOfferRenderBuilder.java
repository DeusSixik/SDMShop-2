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

    @Nullable
    private String title;
    private final ObjectArrayList<Badge> badges = new ObjectArrayList<>();
    private final ObjectArrayList<Price> prices = new ObjectArrayList<>();

    public ShopOfferRenderBuilder() { }

    public ShopOfferRenderBuilder title(String title) {
        this.title = title;
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

    /**
     * @param type {@code 0} is base price, {@code 1} is groups price
     * @param icon can be {@link ItemStack}, {@link Item}, {@link Ingredient}, {@link ResourceLocation}
     * @param parents if {@code type} == {@code 0} is always null if {@code type} == {@code 1} he contains others {@link Price}
     */
    public record Price(byte type, @Nullable Object icon, @Nullable String text, @Nullable Object... parents) { }
}
