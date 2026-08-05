package dev.sixik.sdmshop2.libs.shop.base.limiter;

import com.google.gson.JsonObject;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShopLimiterDataTest {

    @Test
    void playerDataNetworkRoundTripKeepsEntriesAndBufferAlignment() {
        UUID playerId = UUID.randomUUID();
        UUID firstOffer = UUID.randomUUID();
        UUID secondOffer = UUID.randomUUID();

        ShopLimiterPlayerData playerData = new ShopLimiterPlayerData(playerId);
        playerData.getData(firstOffer).add(3);
        playerData.getData(secondOffer).add(7);

        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        try {
            playerData.toNetwork(buf);

            ShopLimiterPlayerData read = new ShopLimiterPlayerData(playerId, buf);

            assertEquals(0, buf.readableBytes());
            assertEquals(3, read.getData(firstOffer).get());
            assertEquals(7, read.getData(secondOffer).get());
        } finally {
            buf.release();
        }
    }

    @Test
    void playerDataJsonIncludesOwnerAndRoundTrips() {
        UUID playerId = UUID.randomUUID();
        UUID offerId = UUID.randomUUID();

        ShopLimiterPlayerData playerData = new ShopLimiterPlayerData(playerId);
        playerData.getData(offerId).add(5);

        JsonObject json = playerData.toJson();

        assertEquals(playerId.toString(), json.get("userId").getAsString());
        assertTrue(json.has("data"));
        assertTrue(json.getAsJsonObject("data").has(offerId.toString()));

        ShopLimiterPlayerData read = new ShopLimiterPlayerData(json);

        assertEquals(playerId, read.getUserId());
        assertEquals(5, read.getData(offerId).get());
    }

    @Test
    void setUpdatePropagatesToLoadedNestedOfferData() {
        UUID playerId = UUID.randomUUID();
        UUID offerId = UUID.randomUUID();

        ShopLimiterPlayerData original = new ShopLimiterPlayerData(playerId);
        original.getData(offerId).add(1);

        ShopLimiterPlayerData loaded = new ShopLimiterPlayerData(original.toJson());
        AtomicInteger updates = new AtomicInteger();
        loaded.setUpdate(updates::incrementAndGet);

        loaded.getData(offerId).add(1);

        assertEquals(1, updates.get());
        assertEquals(2, loaded.getData(offerId).get());
    }

    @Test
    void offerDataClampsNegativeCountsAndIgnoresNegativeDeltas() {
        UUID offerId = UUID.randomUUID();
        ShopLimiterOfferData data = new ShopLimiterOfferData(offerId, 5, -100L);

        assertEquals(5, data.get());
        assertEquals(0L, data.getLastPurchaseTime().get());

        assertEquals(5, data.add(-2));
        assertEquals(5, data.minus(-2));
        assertEquals(5, data.safeMinus(-2));

        data.set(-10);
        assertEquals(0, data.get());

        data.reset(-5, -50L);
        assertEquals(0, data.get());
        assertEquals(0L, data.getLastPurchaseTime().get());
    }
}
