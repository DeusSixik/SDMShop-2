package dev.sixik.sdmshop2.libs.shop.components.money;

import com.lowdragmc.lowdraglib.gui.editor.Icons;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.texture.TransformTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import dev.sixik.sdmshop2.SDMShop2;
import dev.sixik.sdmshop2.libs.sdmeconomy.IExternalCurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyCurrencyRegistry;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyService;
import dev.sixik.sdmshop2.libs.sdmeconomy.icons.CurrencyIcon;
import dev.sixik.sdmshop2.libs.shop.client.screens.widgets.ShopEmptyWidget;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.RewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfig;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentNumberRange;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class MoneyRewardComponent extends RewardComponent {

    public static final IComponentType<MoneyRewardComponent> TYPE = new Type();

    @Getter
    @ComponentConfig(translationKey = "shop.component.reward.money.money_id")
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
                .modify(MoneyCostComponent.DYNAMIC_CURRENCY.get().setId(moneyId), value);
    }

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public @Nullable Widget createRender() {
        final Map<ResourceLocation, IExternalCurrency> cur_map = SDMEconomyCurrencyRegistry.getCurrenciesMap();
        if(!cur_map.containsKey(moneyId)) {
            SDMShop2.LOGGER.error("Can't find money with id '{}' and can't create render widget", moneyId);
            return null;
        }

        final IExternalCurrency money = cur_map.get(moneyId);
        final CurrencyIcon icon = money.getIcon();
        final Object icon_object = icon.icon();
        final ShopEmptyWidget widget = new ShopEmptyWidget();

        TransformTexture texture = null;
        switch (icon.type()) {
            case NONE -> {
            }
            case ITEM -> {
                if(icon_object instanceof Item item) {
                    texture = new ItemStackTexture(item);
                } else if(icon_object instanceof ItemStack item) {
                    texture = new ItemStackTexture(item);
                } else if(icon_object instanceof Ingredient ingredient) {
                    texture = new ItemStackTexture(ingredient.getItems());
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

        if(texture != null) {
            widget.setBackground(texture).setHoverTexture(texture);
        }

        return widget.setHoverTooltips(money.getDisplayName().copy().append(" : ").append(money.format(BigDecimal.valueOf(amount))));
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
