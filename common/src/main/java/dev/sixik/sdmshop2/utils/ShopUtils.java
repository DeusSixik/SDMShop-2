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
import dev.sixik.sdmshop2.libs.shop.base.limiter.ShopLimiterTable;
import dev.sixik.sdmshop2.libs.shop.base.limiter.ShopLimiterTableClient;
import dev.sixik.sdmshop2.libs.shop.base.limiter.ShopLimiterTableServer;
import dev.sixik.sdmshop2.libs.shop.client.ui.api.ShopUIUtils;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponentRegistry;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.Commands;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class ShopUtils {

    public static Optional<ShopLimiterTable> getLimiterTable(boolean isClient) {
        return Optional.ofNullable(isClient ? ShopLimiterTableClient.INSTANCE : ShopLimiterTableServer.getInstance());
    }

    public static Map<ResourceLocation, ShopComponent> createDefaultComponentsMap(Class<? extends ShopComponent> include) {
        Map<ResourceLocation, ShopComponent> componentMap = new HashMap<>();
        for (Map.Entry<ResourceLocation, IComponentType<?>> entry : ShopComponentRegistry.getTypes().entrySet()) {
            ShopComponent component = entry.getValue().createDefault();
            if(!include.isInstance(component)) continue;

            componentMap.put(entry.getKey(), component);
        }
        return componentMap;
    }

    public static Map<ResourceLocation, ShopComponent> createDefaultComponentsMap() {
        Map<ResourceLocation, ShopComponent> componentMap = new HashMap<>();
        for (Map.Entry<ResourceLocation, IComponentType<?>> entry : ShopComponentRegistry.getTypes().entrySet()) {
            componentMap.put(entry.getKey(), entry.getValue().createDefault());
        }
        return componentMap;
    }

    public static Optional<ShopComponent> createDefaultComponent(ResourceLocation typeId) {
        return ShopComponentRegistry.getType(typeId).map(IComponentType::createDefault);
    }

    public static <T extends ShopComponent> T createDefaultComponent(IComponentType<T> type) {
        return type.createDefault();
    }

    public static FriendlyByteBuf createResponse(Consumer<FriendlyByteBuf> writer) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        writer.accept(buf);
        return buf;
    }

    public static Component getTranslation(String txt) {
        return getTranslation(txt, txt);
    }

    public static Component getTranslation(String txt, String or) {
        if(I18n.exists(txt))
            return Component.translatable(txt);
        return Component.literal(or);
    }

    @Environment(EnvType.CLIENT)
    public static void openWidget(WidgetGroup panel) {
        RenderSystem.recordRenderCall(() -> {
            final var minecraft = Minecraft.getInstance();
            final var entityPlayer = minecraft.player;

            ModularUI ui = new ModularUI(panel, IUIHolder.EMPTY, entityPlayer);
            ui.setFullScreen();
            ui.initWidgets();
            ModularUIGuiContainer ModularUIGuiContainer = new ModularUIGuiContainer(ui, entityPlayer.containerMenu.containerId) {
                @Override
                public void removed() {
                    ShopUIUtils.disposeTree(panel);
                    super.removed();
                }
            };

            minecraft.setScreen(ModularUIGuiContainer);
        });
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public static TransformTexture getCurrencyTexture(ICurrency currency) {
        return getCurrencyTexture(currency.getIcon(), 0);
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public static TransformTexture getCurrencyTexture(ICurrency currency, double count) {
        return getCurrencyTexture(currency.getIcon(), count);
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public static TransformTexture getCurrencyTexture(CurrencyIcon icon) {
        return getCurrencyTexture(icon, 0);
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public static TransformTexture getCurrencyTexture(CurrencyIcon icon, double count) {
        final Object icon_object = icon.icon();

        TransformTexture texture = null;
        switch (icon.type()) {
            case NONE -> {
            }
            case ITEM -> {
                if(icon_object instanceof Item item) {
                    texture = new ItemStackTexture(item);
                } else if(icon_object instanceof ItemStack item) {
                    texture = new ItemStackTexture(item.copyWithCount(Math.max(1, (int) count)));
                } else if(icon_object instanceof Ingredient ingredient) {
                    texture = new ItemStackTexture((ItemStack[]) Arrays.stream(ingredient.getItems()).map(s -> s.copyWithCount(Math.max(1, (int) count))).toArray(ItemStack[]::new));
                }
            }
            case TEXTURE -> {
                if(icon_object instanceof ResourceLocation location) {
                    texture = new ResourceTexture(location);
                } else if(icon_object instanceof String location) {
                    if(ResourceLocation.isValidResourceLocation(location))
                        texture = new ResourceTexture(location);
                    else texture = new TextTexture(location);
                }
            }
        }

        return texture;
    }

    public static boolean isPlayerAdmin(Player player) {
        return player.hasPermissions(Commands.LEVEL_GAMEMASTERS);
    }
}
