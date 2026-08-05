package dev.sixik.sdmshop2.mixin;

import dev.sixik.sdmshop2.libs.shop.client.ui.elements.ShopScreenElement;
import dev.sixik.sdmshop2.libs.shop.client.ui.events.ShopUIEvents;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {

    @Inject(method = "handleContainerSetSlot", at = @At("RETURN"))
    private void sdmshop2$refreshInventoryCurrenciesAfterSetSlot(
            ClientboundContainerSetSlotPacket packet,
            CallbackInfo ci
    ) {
        refreshShopInventoryCurrencies();
    }

    @Inject(method = "handleContainerContent", at = @At("RETURN"))
    private void sdmshop2$refreshInventoryCurrenciesAfterSetContent(
            ClientboundContainerSetContentPacket packet,
            CallbackInfo ci
    ) {
        refreshShopInventoryCurrencies();
    }

    private static void refreshShopInventoryCurrencies() {
        if (ShopScreenElement.Instance != null) {
            ShopUIEvents.invokeRefreshInventoryCurrencies();
        }
    }
}
