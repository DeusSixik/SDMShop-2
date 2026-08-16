package dev.sixik.sdmshop2.utils;

import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.modular.ModularUIGuiContainer;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.texture.TransformTexture;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUIUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
public final class ShopClientUtils {

    private ShopClientUtils() {
    }

    public static Component getTranslation(String txt) {
        return getTranslation(txt, txt);
    }

    public static Component getTranslation(String txt, String or) {
        if (I18n.exists(txt)) {
            return Component.translatable(txt);
        }
        return Component.literal(or);
    }

    public static void openWidget(WidgetGroup panel) {
        RenderSystem.recordRenderCall(() -> {
            final var minecraft = Minecraft.getInstance();
            final var entityPlayer = minecraft.player;

            ModularUI ui = new ModularUI(panel, IUIHolder.EMPTY, entityPlayer);
            ui.setFullScreen();
            ui.initWidgets();
            ModularUIGuiContainer modularUIGuiContainer = new ModularUIGuiContainer(ui, entityPlayer.containerMenu.containerId) {
                @Override
                public void removed() {
                    ShopUIUtils.disposeTree(panel);
                    super.removed();
                }
            };

            minecraft.setScreen(modularUIGuiContainer);
        });
    }

    @Nullable
    public static TransformTexture getCurrencyTexture(ICurrency currency) {
        return getCurrencyTexture(currency.getIcon(), 0);
    }

    @Nullable
    public static TransformTexture getCurrencyTexture(ICurrency currency, double count) {
        return getCurrencyTexture(currency.getIcon(), count);
    }

    @Nullable
    public static TransformTexture getCurrencyTexture(CurrencyIcon icon) {
        return getCurrencyTexture(icon, 0);
    }

    @Nullable
    public static TransformTexture getCurrencyTexture(CurrencyIcon icon, double count) {
        final Object iconObject = icon.icon();

        TransformTexture texture = null;
        switch (icon.type()) {
            case NONE -> {
            }
            case ITEM -> {
                if (iconObject instanceof Item item) {
                    texture = new ItemStackTexture(item);
                } else if (iconObject instanceof ItemStack item) {
                    texture = new ItemStackTexture(item.copyWithCount(Math.max(1, (int) count)));
                } else if (iconObject instanceof Ingredient ingredient) {
                    texture = new ItemStackTexture((ItemStack[]) Arrays.stream(ingredient.getItems()).map(s -> s.copyWithCount(Math.max(1, (int) count))).toArray(ItemStack[]::new));
                }
            }
            case TEXTURE -> {
                if (iconObject instanceof ResourceLocation location) {
                    texture = new ResourceTexture(location);
                } else if (iconObject instanceof String location) {
                    if (ResourceLocation.isValidResourceLocation(location)) {
                        texture = new ResourceTexture(location);
                    } else {
                        texture = new TextTexture(location);
                    }
                }
            }
        }

        return texture;
    }
}
