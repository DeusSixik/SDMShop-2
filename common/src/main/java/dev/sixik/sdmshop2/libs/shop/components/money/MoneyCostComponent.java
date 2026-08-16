package dev.sixik.sdmshop2.libs.shop.components.money;

import com.lowdragmc.lowdraglib.gui.texture.TransformTexture;
import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.sdmeconomy.*;
import dev.sixik.sdmshop2.libs.shop.components.api.CostComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfig;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfigOptions;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentNumberRange;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import dev.sixik.sdmshop2.utils.ShopClientUtils;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.math.BigDecimal;
import java.util.Map;

public class MoneyCostComponent extends CostComponent {

    public static final IComponentType<MoneyCostComponent> TYPE = new Type();

    public static final ThreadLocal<DynamicStoredCurrency> DYNAMIC_CURRENCY =
            ThreadLocal.withInitial(() -> new DynamicStoredCurrency(EMPTY));

    @Getter
    @ComponentConfig(translationKey = "shop.component.cost.money.money_id")
    @ComponentConfigOptions(provider = "sdm:money_ids")
    private ResourceLocation moneyId;

    @Getter
    @ComponentConfig(translationKey = "shop.component.cost.money.amount")
    @ComponentNumberRange(doubleMin = 0)
    private double amount;

    public MoneyCostComponent() {
        this(EMPTY, 0);
    }

    public MoneyCostComponent(ResourceLocation moneyId, double amount) {
        this.moneyId = moneyId == null ? EMPTY : moneyId;
        this.amount = amount;
    }

    @Override
    public boolean canPay(Player player, double actualPrice) {
        if (actualPrice < 0 || !Double.isFinite(actualPrice)) {
            return false;
        }

        final Map<ResourceLocation, IExternalCurrency> currencies = player.isLocalPlayer() ?
                SDMEconomyServiceClient.getAllExternalCurrencies()
                : SDMEconomyCurrencyRegistry.getCurrenciesMap();

        if(currencies.containsKey(moneyId))
            /*
                Проверяем, хватает ли баланса на актуальную цену (actualPrice), а не на базовую (amount)
             */
            return currencies.get(moneyId).getBalance(player).doubleValue() >= actualPrice;

        final BankAccount account = player.isLocalPlayer()
                ? SDMEconomyServiceClient.getInstanceClient().getBankAccount()
                : SDMEconomyService.getInstance().getAccount(player.getGameProfile().getId());

        return account.getBalance(storedCurrency(moneyId)).doubleValue() >= actualPrice;
    }

    @Override
    public void pay(Player player, double actualPrice) {
        tryPay(player, actualPrice);
    }

    @Override
    public boolean tryPay(Player player, double actualPrice) {
        if (player.isLocalPlayer()) {
            SDMShop2.LOGGER.warn("Call Pay methods on client!");
            return false;
        }

        if (!canPay(player, actualPrice)) {
            return false;
        }

        if (actualPrice == 0) {
            return true;
        }

        /*
            Мы конвертируем в BigDecimal именно actualPrice,
            Чтобы списать ровно ту сумму, которую насчитал процессор скидок.
         */
        final BigDecimal value = BigDecimal.valueOf(actualPrice);
        Map<ResourceLocation, IExternalCurrency> currencies = SDMEconomyCurrencyRegistry.getCurrenciesMap();

        if (currencies.containsKey(moneyId)) {
            return currencies.get(moneyId).withdraw((ServerPlayer) player, value);
        }

        SDMEconomyService.getInstance()
                .getAccount(player.getGameProfile().getId())
                .modify(storedCurrency(moneyId), value.negate());
        return true;
    }

    @Override
    public void refund(Player player, double actualPrice) {
        if (player.isLocalPlayer() || actualPrice <= 0 || !Double.isFinite(actualPrice)) {
            return;
        }

        final BigDecimal value = BigDecimal.valueOf(actualPrice);
        Map<ResourceLocation, IExternalCurrency> currencies = SDMEconomyCurrencyRegistry.getCurrenciesMap();

        if (currencies.containsKey(moneyId)) {
            currencies.get(moneyId).deposit((ServerPlayer) player, value);
            return;
        }

        SDMEconomyService.getInstance()
                .getAccount(player.getGameProfile().getId())
                .modify(storedCurrency(moneyId), value);
    }

    @Override
    public double getBaseAmount() {
        return amount;
    }

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public TransformTexture getRenderIcon() {
        final ICurrency money = SDMEconomyServiceClient.getCurrency(moneyId);
        if(money == null) {
            SDMShop2.LOGGER.error("Can't find money with id '{}' and can't create render widget", moneyId);
            return null;
        }

        return ShopClientUtils.getCurrencyTexture(money);
    }

    @Override
    public Component getDisplayName() {
        final ICurrency money = SDMEconomyServiceClient.getCurrency(moneyId);
        if(money == null)
            return Component.empty();

        return money.getDisplayName();
    }

    public static IStoredCurrency storedCurrency(ResourceLocation moneyId) {
        IStoredCurrency storedCurrency = SDMEconomyCurrencyRegistry.getStoredCurrency(moneyId);
        return storedCurrency == null ? DYNAMIC_CURRENCY.get().setId(moneyId) : storedCurrency;
    }

    private static class Type extends SerializedComponentType<MoneyCostComponent> {

        private static final ResourceLocation ID = new ResourceLocation("sdm", "cost_money");
        private static final ComponentSerializer<MoneyCostComponent> SERIALIZER = ComponentSerializer.<MoneyCostComponent>create()
                .addRequired("money_id", dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs.RESOURCE_LOCATION, MoneyCostComponent::getMoneyId, (component, value) -> component.moneyId = value == null ? EMPTY : value)
                .addRequired("amount", dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs.DOUBLE, MoneyCostComponent::getAmount, (component, value) -> component.amount = value);

        private Type() {
            super(MoneyCostComponent::new, SERIALIZER);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public MoneyCostComponent createFromBuilder(Object... args) {
            if(args.length != 2 && args.length != 3)
                throw new IllegalArgumentException("MoneyCostComponent.createFromBuilder() takes 2 or 3 arguments (String/ResourceLocation, double, (Optional) String)");

            Object obj = args[0];
            final var component = new MoneyCostComponent(obj instanceof ResourceLocation ? (ResourceLocation) obj : ResourceLocation.tryParse((String) obj), (double) args[1]);
            if(args.length == 3)
                component.setGroupId((String) args[2]);

            return component;
        }
    }
}
