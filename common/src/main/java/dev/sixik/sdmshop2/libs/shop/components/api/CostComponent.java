package dev.sixik.sdmshop2.libs.shop.components.api;

import com.lowdragmc.lowdraglib.gui.texture.TransformTexture;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfig;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

/**
 * Абстрактный компонент, представляющий стоимость покупки.
 * Отвечает за проверку наличия средств и процесс оплаты.
 */
public abstract class CostComponent extends ShopComponent {

    private static final ComponentSerializer<CostComponent> ADDITIONAL_SERIALIZER = ComponentSerializer.<CostComponent>create()
            .addDefaultedString("group_id", CostComponent::getGroupId, CostComponent::setGroupId, "");

    /**
     * Уникальный ID для группировки нескольких валют
     */
    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.cost.group_id")
    private String groupId = "";

    /**
     * Проверяет, может ли игрок оплатить данную стоимость.
     *
     * @param player Игрок для проверки
     * @return true, если средств достаточно, иначе false
     */
    public abstract boolean canPay(Player player, double actualPrice);

    public final void payInternal(Player player, double actualPrice) {
        tryPay(player, actualPrice);
    }

    public boolean tryPay(Player player, double actualPrice) {
        if (!canPay(player, actualPrice)) {
            return false;
        }

        pay(player, actualPrice);
        return true;
    }

    /**
     * Списывает стоимость у игрока.
     *
     * @param player Игрок, с которого списываются средства
     */
    public abstract void pay(Player player, double actualPrice);

    public void refund(Player player, double actualPrice) {
    }

    /**
     * Возвращает базовую стоимость до применения скидок.
     * Если компонент не поддерживает числовые скидки (например, квестовый предмет),
     * можно возвращать 0 или игнорировать его в скидках.
     */
    public abstract double getBaseAmount();

    @Override
    public ComponentSerializer<CostComponent> additionalSerializer() {
        return ADDITIONAL_SERIALIZER;
    }

    @Environment(EnvType.CLIENT)
    @Nullable
    public abstract TransformTexture getRenderIcon();

    public Component getDisplayName() {
        return Component.empty();
    }

    @Override
    public ShopComponentCategory getCategory() {
        return ShopComponentCategory.COST;
    }
}
