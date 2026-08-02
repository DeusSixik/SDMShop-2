package dev.sixik.sdmshop2.libs.shop.processors;

import com.lowdragmc.lowdraglib.gui.texture.TransformTexture;
import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.components.api.CostComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.IComponentType;
import dev.sixik.sdmshop2.libs.shop.components.api.PromoComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.PromoEffectComponent;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShopTransactionProcessorTest {

    @Test
    void nullChosenGroupMatchesDefaultCostGroup() {
        ShopOffer offer = ShopOffer.create(UUID.randomUUID(), false);
        TestCost defaultCost = new TestCost(null, 10.0D);
        TestCost otherCost = new TestCost("vip", 5.0D);
        offer.addComponent(defaultCost);
        offer.addComponent(otherCost);

        Map<CostComponent, Double> prices = ShopTransactionProcessor.calculateFinalCosts(offer, null, null);

        assertEquals(1, prices.size());
        assertEquals(10.0D, prices.get(defaultCost));
    }

    @Test
    void finalCostsKeepComponentOrder() {
        ShopOffer offer = ShopOffer.create(UUID.randomUUID(), false);
        TestCost first = new TestCost("", 1.0D);
        TestCost second = new TestCost("", 2.0D);
        TestCost third = new TestCost("", 3.0D);
        offer.addComponent(first);
        offer.addComponent(second);
        offer.addComponent(third);

        Map<CostComponent, Double> prices = ShopTransactionProcessor.calculateFinalCosts(offer, null, "");

        assertEquals(List.of(first, second, third), new ArrayList<>(prices.keySet()));
    }

    @Test
    void invalidPricesAreClampedToZero() {
        ShopOffer offer = ShopOffer.create(UUID.randomUUID(), false);
        TestCost negativeBase = new TestCost("", -10.0D);
        TestCost nanAfterEffect = new TestCost("vip", 10.0D);
        TestPromo promo = new TestPromo("promo");
        TestPromoEffect effect = new TestPromoEffect(null, Double.NaN);
        offer.addComponent(negativeBase);
        offer.addComponent(nanAfterEffect);
        offer.addComponent(promo);
        offer.addComponent(effect);

        Map<CostComponent, Double> defaultPrices = ShopTransactionProcessor.calculateFinalCosts(offer, null, "");
        Map<CostComponent, Double> vipPrices = ShopTransactionProcessor.calculateFinalCosts(offer, null, "vip");

        assertEquals(0.0D, defaultPrices.get(negativeBase));
        assertEquals(0.0D, vipPrices.get(nanAfterEffect));
    }

    private static final class TestCost extends CostComponent {
        private final double baseAmount;

        private TestCost(@Nullable String groupId, double baseAmount) {
            setGroupId(groupId);
            this.baseAmount = baseAmount;
        }

        @Override
        public boolean canPay(Player player, double actualPrice) {
            return true;
        }

        @Override
        public void pay(Player player, double actualPrice) {
        }

        @Override
        public double getBaseAmount() {
            return baseAmount;
        }

        @Override
        public @Nullable TransformTexture getRenderIcon() {
            return null;
        }

        @Override
        public IComponentType<?> getType() {
            return null;
        }
    }

    private static final class TestPromo extends PromoComponent {
        private TestPromo(String promoId) {
            setPromoId(promoId);
        }

        @Override
        public IComponentType<?> getType() {
            return null;
        }
    }

    private static final class TestPromoEffect extends PromoEffectComponent {
        private final double result;

        private TestPromoEffect(String groupId, double result) {
            if (groupId != null) {
                applyGroup(groupId);
            }
            this.result = result;
        }

        @Override
        public double applyPrice(double input, Set<String> activePromo, Set<String> activeGroups) {
            return result;
        }

        @Override
        public IComponentType<?> getType() {
            return null;
        }
    }
}
