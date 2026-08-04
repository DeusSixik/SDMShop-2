package dev.sixik.sdmshop2.libs.shop.network.async;

import dev.sixik.sdmshop2.libs.platform.utils.network.async.AsyncBridge;
import dev.sixik.sdmshop2.libs.platform.utils.network.async.BlobTransfer;
import dev.sixik.sdmshop2.libs.shop.base.ShopInstance;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.client.SDMShopClient;
import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopScreenElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import dev.sixik.sdmshop2.libs.shop.client.ui.toast.ShopToasts;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponentRegistry;
import dev.sixik.sdmshop2.utils.ShopUtils;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

import static dev.sixik.sdmshop2.libs.shop.client.SDMShopClient.*;

public class AsyncClientTasks {

    public static final String GET_PRICES_FOR_OFFER = "get_prices_for_offer";
    public static final String GET_CONDITIONS_FOR_OFFER = "get_conditions_for_offer";
    public static final String PURCHASE_SHOP_OFFER = "purchase_shop_offer";
    public static final String REQUEST_SHOP = "request_shop";
    public static final String REQUEST_CONFIGURED_SHOP_OPEN = "request_configured_shop_open";
    public static final String SEND_SHOP_CHANGES = "send_shop_changes";
    public static final String SEND_CURRENCY_CHANGES = "send_currency_changes";

    public static void init() {
        AsyncBridge.initClient();
        BlobTransfer.initClient();

        AsyncBridge.registerHandler(AsyncServerTasks.SEND_SHOP_DATA, (buf, ctx) -> {
            ShopInstance previousShop = SDMShopClient.Shop;
            SDMShopClient.Shop = ShopInstance.fromNetwork(buf);
            UUID updater = readOptionalUpdater(buf);
            warnExternalShopUpdate(previousShop, SDMShopClient.Shop, updater);
            ACCEPT_SHOP_EVENT.invoker().onAcceptShopEvent(SDMShopClient.Shop);
            ShopUIEvents.invokeRefreshAll();
            return null;
        });

        AsyncBridge.registerHandler(AsyncServerTasks.SEND_SHOP_DATA_AND_OPEN, (buf, ctx) -> {
            SDMShopClient.Shop = ShopInstance.fromNetwork(buf);
            ACCEPT_SHOP_EVENT.invoker().onAcceptShopEvent(SDMShopClient.Shop);
            SDMShopClient.openShopGui();
            return null;
        });

        AsyncBridge.registerHandler(AsyncServerTasks.SEND_SHOP_LIMITER_DATA, (buf, ctx) -> {
            ShopUtils.getLimiterTable(true).ifPresent(limiterTable -> {
                limiterTable.fromNetwork(buf);
                ShopUIEvents.invokeRefreshOffers();
            });
            return null;
        });

        AsyncBridge.registerHandler(AsyncServerTasks.SEND_GET_CLIENT_SHOP_ID, (buf, ctx) -> {
            boolean result = false;

            if(buf.isReadable() && SDMShopClient.Shop != null) {
                ResourceLocation shopId = buf.readResourceLocation();
                result = SDMShopClient.Shop.getId().equals(shopId);
            }

            FriendlyByteBuf reply = new FriendlyByteBuf(Unpooled.buffer());
            reply.writeBoolean(result);
            return reply;
        });

        AsyncBridge.registerHandler(AsyncServerTasks.SEND_NEW_COMPONENT_DATA_TO_CLIENT, (buf, ctx) -> {
            if(!buf.isReadable()) return null;

            final UUID entityId = buf.readUUID();
            final ShopInstance shop = SDMShopClient.Shop;

            final ShopOffer entity = shop.getEntries().getEntry(entityId);
            if(entity == null) return null;

            final ShopComponent component = ShopComponentRegistry.fromNetwork(buf);
            entity.addComponent(component);
            component.init();
            ACCEPT_NEW_COMPONENT_DATA_EVENT.invoker().onAcceptNewComponentDataEvent(shop, entity, component);
            ShopUIEvents.invokeRefreshOffer(entityId);

            return null;
        });

    }

    private static UUID readOptionalUpdater(FriendlyByteBuf buf) {
        if (!buf.isReadable()) {
            return null;
        }

        boolean hasUpdater = buf.readBoolean();
        return hasUpdater && buf.isReadable() ? buf.readUUID() : null;
    }

    private static void warnExternalShopUpdate(ShopInstance previousShop, ShopInstance newShop, UUID updater) {
        if (updater == null || previousShop == null || newShop == null || previousShop.getId() == null || newShop.getId() == null) {
            return;
        }
        if (!previousShop.getId().equals(newShop.getId()) || ShopScreenElement.Instance == null) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && updater.equals(minecraft.player.getUUID())) {
            return;
        }

        ShopToasts.warning(Component.literal("Shop was updated by another editor. Your view has been refreshed."));
    }
}
