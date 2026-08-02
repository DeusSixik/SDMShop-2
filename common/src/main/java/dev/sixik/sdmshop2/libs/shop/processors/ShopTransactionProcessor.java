package dev.sixik.sdmshop2.libs.shop.processors;

import dev.sixik.sdmshop2.libs.shop.base.ShopOffer;
import dev.sixik.sdmshop2.libs.shop.components.api.ConditionComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.CostComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.PromoComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.PromoEffectComponent;
import dev.sixik.sdmshop2.libs.shop.components.api.RewardComponent;
import dev.sixik.sdmshop2.libs.shop.components.limiter.LimiterComponent;
import dev.sixik.sdmshop2.libs.shop.events.ShopServerEvents;
import dev.sixik.sdmshop2.libs.shop.network.ShopNetworkManager;
import dev.sixik.sdmshop2.libs.shop.scripting.events.ShopScriptEvents;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

/**
 * Выполняет серверную логику покупки товара в магазине.
 *
 * <p>Процессор отвечает за полный цикл транзакции: проверку условий, проверку лимитов,
 * расчёт итоговой цены, списание средств, выдачу наград и обновление лимитов. Логика
 * специально собрана в одном месте, чтобы покупка проходила предсказуемо и не оставляла
 * частично списанные средства при ошибке на этапе выдачи наград.</p>
 */
public class ShopTransactionProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShopTransactionProcessor.class);

    /**
     * Пытается выполнить покупку указанного {@link ShopOffer} для игрока.
     *
     * <p>Метод возвращает {@code false}, если проверка условий, лимитов, оплаты или выдачи
     * наград не прошла. Если ошибка возникает после частичного списания средств, уже
     * списанные {@link CostComponent} откатываются через {@link CostComponent#refund(Player, double)}.</p>
     *
     * @param offer товар, который покупает игрок
     * @param player игрок-покупатель
     * @param chosenGroupId выбранная группа оплаты; {@code null} считается дефолтной группой {@code ""}
     * @param amount количество покупаемых единиц товара
     * @return {@code true}, если покупка полностью завершилась успешно
     */
    public static boolean executePlayerPurchase(
            final ShopOffer offer,
            final ServerPlayer player,
            final String chosenGroupId,
            final int amount
    ) {
        if (offer == null || player == null || amount <= 0) {
            return false;
        }

        final MinecraftServer server = player.getServer();

        // Фаза 1: обычные условия доступа к товару.
        for (ConditionComponent condition : offer.getComponents(ConditionComponent.class)) {
            if (!condition.isChecked(player)) {
                return false;
            }
        }

        // Фаза 2: лимиты проверяются до оплаты, чтобы не списывать средства зря.
        final List<LimiterComponent> limiters = offer.getComponents(LimiterComponent.class);
        for (LimiterComponent limiter : limiters) {
            if (!limiter.isChecked(player, amount)) {
                return false;
            }
        }

        // Фаза 3: выбираем cost-компоненты нужной группы и применяем активные скидки.
        final Object2DoubleMap<CostComponent> finalCosts = calculateFinalCosts(offer, server, chosenGroupId);
        if (finalCosts.isEmpty() && !offer.getComponents(CostComponent.class).isEmpty()) {
            return false;
        }

        // Цена в finalCosts указана за 1 единицу товара, поэтому умножаем её на amount.
        final List<CostCharge> totalCosts = multiplyCosts(finalCosts, amount);
        if (totalCosts.isEmpty() && !finalCosts.isEmpty()) {
            return false;
        }

        // Фаза 4: предварительная проверка всех источников оплаты.
        for (CostCharge charge : totalCosts) {
            if (!charge.cost().canPay(player, charge.amount())) {
                return false;
            }
        }

        // Фаза 5: реальное списание. Если один cost не смог списаться — откатываем предыдущие.
        final List<PaidCost> paidCosts = new ObjectArrayList<>();
        for (CostCharge charge : totalCosts) {
            final CostComponent cost = charge.cost();
            final double totalCost = charge.amount();

            if (!cost.tryPay(player, totalCost)) {
                rollbackCosts(player, paidCosts);
                return false;
            }

            paidCosts.add(new PaidCost(cost, totalCost));
        }

        // Фаза 6: выдача наград. При ошибке откатываем оплату и не считаем лимиты.
        try {
            for (RewardComponent reward : offer.getComponents(RewardComponent.class)) {
                reward.reward(player, amount);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to give rewards for offer {}", offer.getUUID(), e);
            rollbackCosts(player, paidCosts);
            return false;
        }

        // Фаза 7: лимиты обновляются только после успешной оплаты и выдачи наград.
        for (LimiterComponent limiter : limiters) {
            limiter.addLimit(player, amount);
        }

        syncLimitersNetwork(limiters, player);
        return true;
    }

    /**
     * Синхронизирует клиенту новые данные лимитеров после успешной покупки.
     */
    private static void syncLimitersNetwork(List<LimiterComponent> limiters, ServerPlayer player) {
        if (!limiters.isEmpty()) {
            ShopNetworkManager.sendLimiterData(player);
        }
    }

    /**
     * Рассчитывает итоговую цену каждого {@link CostComponent} в выбранной группе оплаты.
     *
     * <p>Метод сохраняет порядок cost-компонентов из товара. Это важно, потому что порядок
     * списания должен быть стабильным: если в будущем у cost-компонентов появятся побочные
     * эффекты или разные валюты, результат не будет зависеть от порядка в hash map.</p>
     *
     * <p>После применения скидок и внешних event-хуков цена проходит через
     * {@link #sanitizePrice(Double)}: отрицательные, {@code NaN} и бесконечные значения
     * превращаются в {@code 0}.</p>
     *
     * @param offer товар, для которого считается цена
     * @param server текущий сервер; может быть {@code null} в тестах/клиентских preview-сценариях
     * @param chosenGroupId выбранная группа оплаты; {@code null} считается дефолтной группой {@code ""}
     * @return map cost-компонентов и итоговых цен за одну единицу товара
     */
    public static Object2DoubleMap<CostComponent> calculateFinalCosts(
            final ShopOffer offer,
            final MinecraftServer server,
            final String chosenGroupId
    ) {
        final Object2DoubleMap<CostComponent> finalCosts = new Object2DoubleLinkedOpenHashMap<>();
        if (offer == null) {
            return finalCosts;
        }

        final String selectedGroupId = normalizeGroupId(chosenGroupId);
        final List<CostComponent> groupCosts = new ObjectArrayList<>();

        // Берём только cost-компоненты выбранной группы оплаты.
        for (CostComponent cost : offer.getComponents(CostComponent.class)) {
            if (normalizeGroupId(cost.getGroupId()).equals(selectedGroupId)) {
                groupCosts.add(cost);
            }
        }

        if (groupCosts.isEmpty()) {
            return finalCosts;
        }

        final Set<String> activePromos = new ObjectOpenHashSet<>();

        // Сначала собираем активные promo_id, затем по ним выбираем применимые эффекты.
        for (PromoComponent promo : offer.getComponents(PromoComponent.class)) {
            if (promo.isActive(server)) {
                activePromos.add(normalizeGroupId(promo.getPromoId()));
            }
        }

        final List<PromoEffectComponent> activeEffects = new ObjectArrayList<>();
        if (!activePromos.isEmpty()) {
            for (PromoEffectComponent effect : offer.getComponents(PromoEffectComponent.class)) {
                if (effect.canApply(activePromos, selectedGroupId)) {
                    activeEffects.add(effect);
                }
            }
        }

        for (CostComponent cost : groupCosts) {
            double currentPrice = sanitizePrice(cost.getBaseAmount());

            // Эффекты применяются последовательно в порядке компонентов товара.
            for (PromoEffectComponent effect : activeEffects) {
                currentPrice = sanitizePrice(effect.applyPrice(currentPrice, activePromos, effect.getApplyGroups()));
            }

            finalCosts.put(cost, currentPrice);
        }

        ShopScriptEvents.SCRIPT_CALCULATE_PRICE_EVENT.invoker().invoke(offer, server, selectedGroupId, finalCosts);
        ShopServerEvents.CALCULATE_PRICE_EVENT.invoker().invoke(offer, server, selectedGroupId, finalCosts);
        finalCosts.replaceAll((cost, price) -> sanitizePrice(price));

        return finalCosts;
    }

    /**
     * Преобразует цену за одну единицу товара в итоговую сумму списания.
     *
     * @return список списаний или пустой список, если найдено некорректное значение
     */
    private static ObjectList<CostCharge> multiplyCosts(Object2DoubleMap<CostComponent> costs, int amount) {
        final ObjectList<CostCharge> out = new ObjectArrayList<>(costs.size());
        for (Object2DoubleMap.Entry<CostComponent> entry : costs.object2DoubleEntrySet()) {
            final double totalCost = entry.getDoubleValue() * amount;
            if (!Double.isFinite(totalCost) || totalCost < 0) {
                return ObjectLists.emptyList();
            }

            out.add(new CostCharge(entry.getKey(), totalCost));
        }

        return out;
    }

    /**
     * Откатывает уже списанные средства в обратном порядке.
     *
     * <p>Обратный порядок нужен на случай, если компоненты оплаты зависят друг от друга:
     * сначала возвращаем последнее списание, затем предыдущие.</p>
     */
    private static void rollbackCosts(ServerPlayer player, List<PaidCost> paidCosts) {
        for (int i = paidCosts.size() - 1; i >= 0; i--) {
            PaidCost paidCost = paidCosts.get(i);
            try {
                paidCost.cost().refund(player, paidCost.amount());
            } catch (Exception e) {
                LOGGER.error("Failed to rollback cost component {}", paidCost.cost().getType().getId(), e);
            }
        }
    }

    /**
     * Нормализует цену после скидок и внешних хуков.
     */
    private static double sanitizePrice(Double price) {
        if (price == null || !Double.isFinite(price) || price < 0) {
            return 0.0D;
        }

        return price;
    }

    /**
     * Приводит отсутствующую группу оплаты к дефолтной группе.
     */
    private static String normalizeGroupId(String groupId) {
        return groupId == null ? "" : groupId;
    }

    /**
     * Уже успешно списанный cost-компонент. Используется только для rollback.
     */
    private record PaidCost(CostComponent cost, double amount) {
    }

    /**
     * Планируемое списание cost-компонента после умножения цены на количество товара.
     */
    private record CostCharge(CostComponent cost, double amount) {
    }
}
