package dev.sixik.sdmshop2.libs.shop.components.money;

import com.lowdragmc.lowdraglib.gui.texture.TransformTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.sdmeconomy.*;
import dev.sixik.sdmshop2.libs.shop.client.ui.widgets.ShopEmptyWidget;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.RewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfig;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfigOptions;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentNumberRange;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import dev.sixik.sdmshop2.utils.ShopUtils;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Map;

public class MoneyRewardComponent extends RewardComponent {

    public static final IComponentType<MoneyRewardComponent> TYPE = new Type();

    @Getter
    @ComponentConfig(translationKey = "shop.component.reward.money.money_id")
    @ComponentConfigOptions(provider = "sdm:money_ids")
    private ResourceLocation moneyId;

    @Getter
    @ComponentConfig(translationKey = "shop.component.reward.money.amount")
    @ComponentNumberRange(doubleMin = 0)
    private double amount;

    public MoneyRewardComponent() {
        this(EMPTY, 0);
    }

    public MoneyRewardComponent(ResourceLocation moneyId, double amount) {
        this.moneyId = moneyId;
        this.amount = amount;
    }

    @Override
    public void reward(ServerPlayer player, int inAmount) {
        final BigDecimal value = BigDecimal.valueOf(amount * inAmount);
        Map<ResourceLocation, IExternalCurrency> currencies = SDMEconomyCurrencyRegistry.getCurrenciesMap();

        if (currencies.containsKey(moneyId)) {
            currencies.get(moneyId).deposit(player, value);
            return;
        }

        SDMEconomyService.getInstance()
                .getAccount(player.getGameProfile().getId())
                .modify(MoneyCostComponent.storedCurrency(moneyId), value);
    }

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public @Nullable Widget createRender() {
        final ICurrency money = SDMEconomyServiceClient.getCurrency(moneyId);
        if(money == null) {
            SDMShop2.LOGGER.error("Can't find money with id '{}' and can't create render widget", moneyId);
            return null;
        }

        final ShopEmptyWidget widget = new ShopEmptyWidget();
        final TransformTexture texture = ShopUtils.getCurrencyTexture(money, amount);

        if(texture != null) {
            widget.setBackground(texture).setHoverTexture(texture);
        }

        return widget.setHoverTooltips(Component.translatable(
                "shop.component.reward.money.render.tooltip",
                money.getDisplayName(),
                money.format(BigDecimal.valueOf(amount))
        ));
    }

    private static class Type extends SerializedComponentType<MoneyRewardComponent> {

        private static final ResourceLocation ID = new ResourceLocation("sdm", "reward_money");
        private static final ComponentSerializer<MoneyRewardComponent> SERIALIZER = ComponentSerializer.<MoneyRewardComponent>create()
                .addRequired("money_id", FieldCodecs.RESOURCE_LOCATION, MoneyRewardComponent::getMoneyId, (component, value) -> component.moneyId = value)
                .addRequired("amount", FieldCodecs.DOUBLE, MoneyRewardComponent::getAmount, (component, value) -> component.amount = value);

        private Type() {
            super(MoneyRewardComponent::new, SERIALIZER);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public MoneyRewardComponent createFromBuilder(Object... args) {
            if(args.length != 2)
                throw new IllegalArgumentException("MoneyRewardComponent.createFromBuilder() takes 2 arguments (String/ResourceLocation, double)");

            Object obj = args[0];
            return new MoneyRewardComponent(obj instanceof ResourceLocation ? (ResourceLocation) obj : ResourceLocation.tryParse((String) obj), (double) args[1]);
        }
    }
}
