package dev.sixik.sdmshop2.libs.shop.components.limiter;

import dev.sixik.sdmshop2.libs.shop.base.ShopEntity;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.base.limiter.ShopLimiterOfferData;
import dev.sixik.sdmshop2.libs.shop.base.limiter.ShopLimiterTable;
import dev.sixik.sdmshop2.libs.shop.components.api.ConditionComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentConfig;
import dev.sixik.sdmshop2.libs.shop.components.api.annotation.ComponentNumberRange;
import dev.sixik.sdmshop2.libs.shop.limiter.ShopLimiters;
import dev.sixik.sdmshop2.libs.shop.serializer.ComponentSerializer;
import dev.sixik.sdmshop2.libs.shop.serializer.SerializedComponentType;
import dev.sixik.sdmshop2.libs.shop.serializer.codec.FieldCodecs;
import dev.sixik.sdmshop2.utils.ShopUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class LimiterComponent extends ConditionComponent {

    public static final IComponentType<LimiterComponent> TYPE = new Type();

    public enum LimiterType {
        World,
        Player
    }

    private UUID rootId;
    private UUID storageId;

    @Getter
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

    @Getter
    @ComponentConfig(translationKey = "shop.component.limiter.limiter.limit_key")
    private String limitKey;

    public LimiterComponent() {
        this(LimiterType.World, 1, 0L, "");
    }

    public LimiterComponent(LimiterType type, int count) {
        this(type, count, 0L, "");
    }

    public LimiterComponent(LimiterType type, int count, long resetIntervalMs) {
        this(type, count, resetIntervalMs, "");
    }

    public LimiterComponent(LimiterType type, int count, long resetIntervalMs, String limitKey) {
        this.limiterType = type == null ? LimiterType.World : type;
        this.count = Math.max(1, count);
        this.resetIntervalMs = Math.max(0L, resetIntervalMs);
        this.limitKey = normalizeLimitKey(limitKey);
    }

    @Override
    public void init() {
        final ShopEntity root = getRoot();

        if (root instanceof ShopOffer offer) {
            this.rootId = offer.getUUID();
            this.storageId = resolveStorageId(offer);
            return;
        }

        throw new IllegalStateException("[LimiterComponent] Root entity must be ShopOffer!");
    }

    public boolean isChecked(Player player, int purchaseAmount) {
        if (player == null || purchaseAmount <= 0) {
            return false;
        }

        final ShopLimiterTable limiterTable = ShopUtils.getLimiterTable(player.isLocalPlayer()).orElse(null);
        if (limiterTable == null) return false;

        final UUID storageId = getStorageId();
        ShopLimiterOfferData data = (getResolvedLimiterType() == LimiterType.Player)
                ? limiterTable.getPlayerData(player).getData(storageId)
                : limiterTable.getOfferData(storageId);

        int currentPurchases = getActivePurchaseCount(data);

        return (currentPurchases + purchaseAmount) <= this.count;
    }

    @Override
    public boolean isChecked(Player player) {
        return isChecked(player, 1);
    }

    public long getAvailableAtMs(Player player) {
        if (player == null) {
            return Long.MAX_VALUE;
        }

        final ShopLimiterTable limiterTable = ShopUtils.getLimiterTable(player.isLocalPlayer()).orElse(null);
        if (limiterTable == null) {
            return Long.MAX_VALUE;
        }

        final UUID storageId = getStorageId();
        ShopLimiterOfferData data = (getResolvedLimiterType() == LimiterType.Player)
                ? limiterTable.getPlayerData(player).getData(storageId)
                : limiterTable.getOfferData(storageId);

        int currentPurchases = getActivePurchaseCount(data);
        if (currentPurchases < this.count) {
            return 0L;
        }

        if (this.resetIntervalMs <= 0) {
            return Long.MAX_VALUE;
        }

        long lastTime = data.getLastPurchaseTime().get();
        if (lastTime <= 0) {
            return Long.MAX_VALUE;
        }

        if (lastTime > Long.MAX_VALUE - this.resetIntervalMs) {
            return Long.MAX_VALUE;
        }

        return lastTime + this.resetIntervalMs;
    }

    public void addLimit(Player player, int amount) {
        if (player == null || amount <= 0) {
            return;
        }

        final ShopLimiterTable limiterTable = ShopUtils.getLimiterTable(false).get();

        final UUID storageId = getStorageId();
        ShopLimiterOfferData data = (getResolvedLimiterType() == LimiterType.Player)
                ? limiterTable.getPlayerData(player).getData(storageId)
                : limiterTable.getOfferData(storageId);

        long lastTime = data.getLastPurchaseTime().get();

        if (this.resetIntervalMs > 0 && lastTime > 0 && (System.currentTimeMillis() - lastTime) >= this.resetIntervalMs) {
            data.reset(amount, System.currentTimeMillis());
        } else {
            data.add(amount);
        }
    }

    public void minusLimit(Player player, int amount) {
        if (player == null || amount <= 0) {
            return;
        }

        final ShopLimiterTable limiterTable = ShopUtils.getLimiterTable(false).get();

        final UUID storageId = getStorageId();
        if (getResolvedLimiterType() == LimiterType.Player) {
            limiterTable.getPlayerData(player).getData(storageId).safeMinus(amount);
        } else {
            limiterTable.getOfferData(storageId).safeMinus(amount);
        }
    }

    public void setLimit(Player player, int amount) {
        if (player == null) {
            return;
        }

        final ShopLimiterTable limiterTable = ShopUtils.getLimiterTable(false).get();

        final UUID storageId = getStorageId();
        if (getResolvedLimiterType() == LimiterType.Player) {
            limiterTable.getPlayerData(player).getData(storageId).set(amount);
        } else {
            limiterTable.getOfferData(storageId).set(amount);
        }
    }

    public boolean resetAll() {
        return getRoot() instanceof ShopOffer offer && ShopLimiters.resetOffer(offer);
    }

    public boolean resetPlayer(Player player) {
        return getResolvedLimiterType() == LimiterType.Player && player != null && ShopLimiters.resetLimiter(this, player.getUUID());
    }

    public boolean resetWorld() {
        return getResolvedLimiterType() == LimiterType.World && ShopLimiters.resetLimiter(this, null);
    }

    public int getLimit(Player player) {
        if (player == null) {
            return count;
        }

        final ShopLimiterTable limiterTable = ShopUtils.getLimiterTable(player.isLocalPlayer()).orElse(null);
        if (limiterTable == null) {
            return count;
        }

        final UUID storageId = getStorageId();
        ShopLimiterOfferData data = getResolvedLimiterType() == LimiterType.Player
                ? limiterTable.getPlayerData(player).getData(storageId)
                : limiterTable.getOfferData(storageId);
        return Math.max(0, count - getActivePurchaseCount(data));
    }

    private int getActivePurchaseCount(ShopLimiterOfferData data) {
        long lastTime = data.getLastPurchaseTime().get();
        if (this.resetIntervalMs > 0 && lastTime > 0 && (System.currentTimeMillis() - lastTime) >= this.resetIntervalMs) {
            data.reset();
            return 0;
        }

        return Math.max(0, data.get());
    }

    public void setLimiterType(LimiterType limiterType) {
        this.limiterType = limiterType == null ? LimiterType.World : limiterType;
        this.storageId = null;
    }

    public void setLimitKey(String limitKey) {
        this.limitKey = normalizeLimitKey(limitKey);
        this.storageId = null;
    }

    public LimiterType getResolvedLimiterType() {
        return limiterType == null ? LimiterType.World : limiterType;
    }

    public UUID getStorageId() {
        if (storageId != null) {
            return storageId;
        }

        if (getRoot() instanceof ShopOffer offer) {
            this.rootId = offer.getUUID();
            this.storageId = resolveStorageId(offer);
            return storageId;
        }

        if (rootId != null) {
            return rootId;
        }

        throw new IllegalStateException("[LimiterComponent] Root entity must be ShopOffer!");
    }

    private UUID resolveStorageId(ShopOffer offer) {
        String explicitKey = normalizeLimitKey(this.limitKey);
        if (!explicitKey.isEmpty()) {
            return createStorageId(offer.getUUID(), explicitKey);
        }

        int ordinal = 0;
        for (LimiterComponent limiter : offer.getComponents(LimiterComponent.class)) {
            if (limiter == this) {
                break;
            }

            if (limiter.getResolvedLimiterType() == getResolvedLimiterType()) {
                ordinal++;
            }
        }

        if (ordinal == 0) {
            return offer.getUUID();
        }

        return createStorageId(offer.getUUID(), getResolvedLimiterType().name() + ":" + ordinal);
    }

    private static UUID createStorageId(UUID offerId, String key) {
        return UUID.nameUUIDFromBytes(("sdmshop2:limiter:" + offerId + ":" + key).getBytes(StandardCharsets.UTF_8));
    }

    private static String normalizeLimitKey(String limitKey) {
        return limitKey == null ? "" : limitKey.trim();
    }

    @Override
    public IComponentType<?> getType() {
        return TYPE;
    }

    private static class Type extends SerializedComponentType<LimiterComponent> {

        private static final ResourceLocation ID = ResourceLocation.tryBuild("sdm", "condition_limiter");
        private static final ComponentSerializer<LimiterComponent> SERIALIZER = ComponentSerializer.<LimiterComponent>create()
                .addRequired("limiter_type", FieldCodecs.enumCodec(LimiterType.class), LimiterComponent::getLimiterType, LimiterComponent::setLimiterType)
                .addRequired("count", FieldCodecs.INT, LimiterComponent::getCount, (component, value) -> component.count = Math.max(1, value))
                .addDefaultedLong("reset_interval_ms", LimiterComponent::getResetIntervalMs, (component, value) -> component.resetIntervalMs = Math.max(0L, value), 0L)
                .addDefaultedString("limit_key", LimiterComponent::getLimitKey, LimiterComponent::setLimitKey, "");

        private Type() {
            super(LimiterComponent::new, SERIALIZER);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }

        @Override
        public LimiterComponent createFromBuilder(Object... args) {
            if (args.length < 2 || args.length > 4) {
                throw new IllegalArgumentException("LimiterComponent.createFromBuilder() takes 2, 3 or 4 arguments (LimiterType/String, int, (Optional) long, (Optional) String)");
            }

            LimiterType type = args[0] instanceof LimiterType limiterType
                    ? limiterType
                    : LimiterType.valueOf((String) args[0]);
            int count = (int) args[1];

            if (args.length == 2) {
                return new LimiterComponent(type, count);
            }

            long resetIntervalMs = args[2] instanceof Number number ? number.longValue() : Long.parseLong(String.valueOf(args[2]));
            if (args.length == 3) {
                return new LimiterComponent(type, count, resetIntervalMs);
            }

            return new LimiterComponent(type, count, resetIntervalMs, String.valueOf(args[3]));
        }
    }
}
