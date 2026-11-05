package dev.sixik.sdmshop2.libs.shop.client.ui.editor;

import dev.sixik.unigui.api.layout.*;
import dev.sixik.unigui.widgets.containers.*;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Item data sources shared by picker views. */
public final class ShopItemSources {
    /** Registry snapshot, created lazily on the client thread. */
    private static List<ItemStack> CACHED_REGISTRY_ITEMS;

    /** Prevents instantiation. */
    private ShopItemSources() {}

    /**
     * Matches a stack against its registry identifier, display name or description key.
     *
     * @param stack item stack to search.
     * @param query search text; blank matches every stack.
     * @return true when the requested operation or check succeeds.
     */
    public static boolean matchesSearch(ItemStack stack, String query) {
        if (query == null || query.isBlank()) return true;
        if (stack == null || stack.isEmpty()) return false;

        String q = query.trim().toLowerCase(Locale.ROOT);

        // 1. Check Registry ID
        try {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            if (id != null) {
                if (id.toString().toLowerCase(Locale.ROOT).contains(q)) return true;
                if (id.getPath().toLowerCase(Locale.ROOT).contains(q)) return true;
                if (id.getNamespace().toLowerCase(Locale.ROOT).contains(q)) return true;
            }
        } catch (Throwable ignored) {
        }

        // 2. Check localized hover name (e.g. "Diamond Sword" / "Алмазный меч")
        try {
            String name = stack.getHoverName().getString().toLowerCase(Locale.ROOT);
            if (name.contains(q)) return true;
        } catch (Throwable ignored) {
        }

        // 3. Check description ID
        try {
            String descId = stack.getDescriptionId().toLowerCase(Locale.ROOT);
            if (descId.contains(q)) return true;
        } catch (Throwable ignored) {
        }

        return false;
    }

    /**
     * Returns the lazily loaded registry item snapshot, excluding air.
     *
     * @return ordered result list; never null.
     */
    public static List<ItemStack> getRegistryItems() {
        if (CACHED_REGISTRY_ITEMS == null) {
            List<ItemStack> list = new ArrayList<>();
            try {
                for (Item item : BuiltInRegistries.ITEM) {
                    if (item == null || item == Items.AIR) continue;
                    list.add(new ItemStack(item, 1));
                }
            } catch (Throwable ignored) {
            }
            CACHED_REGISTRY_ITEMS = List.copyOf(list);
        }
        return CACHED_REGISTRY_ITEMS;
    }

    /**
     * Copies the current player inventory, offhand and armor stacks.
     *
     * @return ordered result list; never null.
     */
    public static List<ItemStack> getInventoryItems() {
        List<ItemStack> list = new ArrayList<>();
        try {
            Minecraft mc = Minecraft.getInstance();
            if (mc != null && mc.player != null) {
                var inv = mc.player.getInventory();
                if (inv != null) {
                    if (inv.items != null) {
                        for (ItemStack s : inv.items) {
                            if (s != null && !s.isEmpty()) {
                                list.add(s.copy());
                            }
                        }
                    }
                    if (inv.offhand != null) {
                        for (ItemStack s : inv.offhand) {
                            if (s != null && !s.isEmpty()) {
                                list.add(s.copy());
                            }
                        }
                    }
                    if (inv.armor != null) {
                        for (ItemStack s : inv.armor) {
                            if (s != null && !s.isEmpty()) {
                                list.add(s.copy());
                            }
                        }
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return list;
    }
}
