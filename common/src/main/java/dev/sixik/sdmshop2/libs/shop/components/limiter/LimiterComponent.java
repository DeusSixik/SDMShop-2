package dev.sixik.sdmshop2.libs.shop.components.limiter;

import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.base.limiter.ShopLimiterOfferData;
import dev.sixik.sdmshop2.libs.shop.base.limiter.ShopLimiterTable;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.ShopComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfig;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentNumberRange;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import dev.sixik.sdmshop2.utils.ShopUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class LimiterComponent extends ShopComponent {

    public static final IComponentType<LimiterComponent> TYPE = new Type();

    public enum LimiterType {
        World,
        Player
    }

    private UUID rootId;

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.limiter.limiter.limiter_type")
    private LimiterType limiterType;

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.limiter.limiter.count")
    @ComponentNumberRange(intMin = 1)
    private int count;

    @Getter
    @Setter
    @ComponentConfig(translationKey = "shop.component.limiter.limiter.reset_interval_ms")
    @ComponentNumberRange(longMin = 0L)
    private long resetIntervalMs;

    public LimiterComponent() {
        this(LimiterType.World, 1, 0L);
    }

    public LimiterComponent(LimiterType type, int count) {
        this(type, count, 0L);
    }

    public LimiterComponent(LimiterType type, int count, long resetIntervalMs) {
        this.limiterType = type;
        this.count = Math.max(1, count);
        this.resetIntervalMs = Math.max(0L, resetIntervalMs);
    }

    @Override
    public void init() {
        final ShopEntity root = getRoot();

        if (root != null) {
            this.rootId = ((ShopOffer) root).getUUID();
        } else {
            throw new RuntimeException("[LimiterComponent] Root entity is missing!");
        }
    }

    public boolean isChecked(Player player, int purchaseAmount) {
        final ShopLimiterTable limiterTable = ShopUtils.getLimiterTable(player.isLocalPlayer()).orElse(null);
        if (limiterTable == null) return false;

        ShopLimiterOfferData data = (limiterType == LimiterType.Player)
                ? limiterTable.getPlayerData(player).getData(this.rootId)
                : limiterTable.getOfferData(this.rootId);

        int currentPurchases = getActivePurchaseCount(data);

        return (currentPurchases + purchaseAmount) <= this.count;
    }

    public void addLimit(Player player, int amount) {
        final ShopLimiterTable limiterTable = ShopUtils.getLimiterTable(false).get();

        ShopLimiterOfferData data = (limiterType == LimiterType.Player)
                ? limiterTable.getPlayerData(player).getData(this.rootId)
                : limiterTable.getOfferData(this.rootId);

        long lastTime = data.getLastPurchaseTime().get();

        if (this.resetIntervalMs > 0 && lastTime > 0 && (System.currentTimeMillis() - lastTime) >= this.resetIntervalMs) {
            data.reset(amount, System.currentTimeMillis());
        } else {
            data.add(amount);
        }
    }

    public void minusLimit(Player player, int amount) {
        final ShopLimiterTable limiterTable = ShopUtils.getLimiterTable(false).get();

        if (limiterType == LimiterType.Player) {
            limiterTable.getPlayerData(player).getData(this.rootId).safeMinus(amount);
        } else {
            limiterTable.getOfferData(this.rootId).safeMinus(amount);
        }
    }

    public void setLimit(Player player, int amount) {
        final ShopLimiterTable limiterTable = ShopUtils.getLimiterTable(false).get();

        if (limiterType == LimiterType.Player) {
            limiterTable.getPlayerData(player).getData(this.rootId).set(amount);
        } else {
            limiterTable.getOfferData(this.rootId).set(amount);
        }
    }

    public int getLimit(Player player) {
        final ShopLimiterTable limiterTable = ShopUtils.getLimiterTable(player.isLocalPlayer()).orElse(null);
        if (limiterTable == null) {
            return count;
        }

        ShopLimiterOfferData data = limiterType == LimiterType.Player
                ? limiterTable.getPlayerData(player).getData(this.rootId)
                : limiterTable.getOfferData(this.rootId);
        return Math.max(0, count - getActivePurchaseCount(data));
    }

    private int getActivePurchaseCount(ShopLimiterOfferData data) {
        long lastTime = data.getLastPurchaseTime().get();
        if (this.resetIntervalMs > 0 && lastTime > 0 && (System.currentTimeMillis() - lastTime) >= this.resetIntervalMs) {
            return 0;
        }

        return Math.max(0, data.get());
    }

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    private static class Type extends SerializedComponentType<LimiterComponent> {

        private static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "condition_limiter");
        private static final ComponentSerializer<LimiterComponent> SERIALIZER = ComponentSerializer.<LimiterComponent>create()
                .addRequired("limiter_type", FieldCodecs.enumCodec(LimiterType.class), LimiterComponent::getLimiterType, (component, value) -> component.limiterType = value)
                .addRequired("count", FieldCodecs.INT, LimiterComponent::getCount, (component, value) -> component.count = Math.max(1, value))
                .addDefaultedLong("reset_interval_ms", LimiterComponent::getResetIntervalMs, (component, value) -> component.resetIntervalMs = Math.max(0L, value), 0L);

        private Type() {
            super(LimiterComponent::new, SERIALIZER);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public LimiterComponent createFromBuilder(Object... args) {
            if (args.length != 2 && args.length != 3) {
                throw new IllegalArgumentException("LimiterComponent.createFromBuilder() takes 2 or 3 arguments (LimiterType/String, int, (Optional) long)");
            }

            LimiterType type = args[0] instanceof LimiterType limiterType
                    ? limiterType
                    : LimiterType.valueOf((String) args[0]);
            int count = (int) args[1];

            if (args.length == 2) {
                return new LimiterComponent(type, count);
            }

            return new LimiterComponent(type, count, (long) args[2]);
        }
    }
}
