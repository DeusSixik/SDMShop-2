package dev.sixik.sdmshop2.libs.shop.components.api;

import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfig;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

public abstract class PromoComponent extends ShopComponent {

    private static final ComponentSerializer<PromoComponent> ADDITIONAL_SERIALIZER = ComponentSerializer.<PromoComponent>create()
            .addDefaultedString("promo_id", PromoComponent::getPromoId, PromoComponent::setPromoId, "")
            .addDefaulted("scope", FieldCodecs.enumCodec(PromoScope.class), PromoComponent::getScope, PromoComponent::setScope, PromoScope.GLOBAL);

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.promo.conditions.promo_id")
    private String promoId = "";

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.promo.scope")
    private PromoScope scope = PromoScope.GLOBAL;

    /**
     * Главный метод проверки. Возвращает true, если акция активна прямо сейчас.
     */
    public boolean isActive(MinecraftServer server) {
        return true;
    }

    public boolean isActive(Player player) {
        return isActive(player.getServer());
    }

    @Override
    public ComponentSerializer<PromoComponent> additionalSerializer() {
        return ADDITIONAL_SERIALIZER;
    }

    @Override
    public ShopComponentCategory getCategory() {
        return ShopComponentCategory.PROMO;
    }
}
