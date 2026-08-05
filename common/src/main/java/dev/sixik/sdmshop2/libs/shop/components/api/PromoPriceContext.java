package dev.sixik.sdmshop2.libs.shop.components.api;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Контекст расчёта цены для promo-effect компонентов.
 *
 * <p>Эффект получает не только текущую цену, но и товар, cost-компонент,
 * выбранную группу оплаты и активные promo_id. За счёт этого один и тот же
 * эффект может быть как глобальным, так и таргетированным на конкретную валюту
 * или группу оплаты.</p>
 */
public record PromoPriceContext(
        ShopOffer offer,
        CostComponent cost,
        @Nullable MinecraftServer server,
        @Nullable Player player,
        String chosenGroupId,
        Set<String> activePromos,
        double basePrice,
        double currentPrice
) {
}
