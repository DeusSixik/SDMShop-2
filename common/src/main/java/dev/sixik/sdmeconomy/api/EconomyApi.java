package dev.sixik.sdmeconomy.api;

import dev.sixik.sdmshop2.libs.sdmeconomy.BankAccount;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.ICurrencyType;
import dev.sixik.sdmshop2.libs.sdmeconomy.IExternalCurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.IStoredCurrency;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyCurrencyRegistry;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyService;
import dev.sixik.sdmshop2.libs.sdmeconomy.SDMEconomyServiceClient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Предоставляет доступ к счетам, балансам и реестру валют экономики.
 *
 * <p>Счета игроков доступны на сервере между запуском и остановкой сервиса. Для клиентского
 * отображения баланса используйте {@link #getBalanceOnClient(ICurrency)}. Хранимые валюты и
 * сериализаторы типов регистрируются при общей инициализации мода, сохранение динамических валют
 * выполняется на сервере. Параметр {@code client} выбирает логическую сторону, включая встроенный
 * сервер одиночной игры. Клиентские методы допустимы только на физическом клиенте.
 *
 * <p>Методы поиска без параметра стороны обращаются к локальному реестру, без добавления
 * синхронизированных клиентских валют.
 *
 * <p>Методы получения карт возвращают изменяемые копии: изменение состава карты не меняет реестр.
 * Экземпляры валют внутри этих карт остаются общими с реестром.
 */
public final class EconomyApi {

    /**
     * Сохраняет совместимость с кодом, создававшим экземпляры фасада.
     *
     * @deprecated Все методы статические; обращайтесь к ним через {@code EconomyApi}.
     */
    @Deprecated
    public EconomyApi() {}

    /**
     * Возвращает сервис указанной логической стороны.
     *
     * @param client {@code true} для клиентского сервиса
     * @return клиентский сервис либо серверный сервис; {@code null}, если серверный сервис не
     *     запущен
     */
    @Nullable
    public static SDMEconomyService getServices(final boolean client) {
        return client
                ? SDMEconomyServiceClient.getInstanceClient()
                : SDMEconomyService.getInstance();
    }

    /**
     * Получает или создаёт серверный счёт игрока по ID его игрового профиля.
     *
     * @param player игрок, не {@code null}
     * @return серверный счёт игрока
     * @throws NullPointerException если {@code player} равен {@code null}
     * @throws IllegalStateException если серверный сервис не запущен
     */
    public static BankAccount getAccount(Player player) {
        Objects.requireNonNull(player, "player");
        return getAccount(player.getGameProfile().getId());
    }

    /**
     * Получает или создаёт серверный счёт по ID игрового профиля.
     *
     * @param gameProfileId ID игрового профиля, не {@code null}
     * @return счёт, при необходимости загруженный из хранилища
     * @throws NullPointerException если {@code gameProfileId} равен {@code null}
     * @throws IllegalStateException если серверный сервис не запущен
     */
    public static BankAccount getAccount(UUID gameProfileId) {
        Objects.requireNonNull(gameProfileId, "gameProfileId");
        SDMEconomyService service = getServices(false);
        if (service == null) {
            throw new IllegalStateException(
                    "Server economy service is not running; access accounts after server startup"
                            + " and before shutdown");
        }
        return service.getAccount(gameProfileId);
    }

    /**
     * Возвращает баланс валюты через общий интерфейс.
     *
     * <p>Внешняя валюта запрашивается у её реализации, хранимая — у серверного счёта. Если валюта
     * реализует оба интерфейса, используется внешнее представление, как при поиске в реестре.
     *
     * @param currency внешняя или хранимая валюта, не {@code null}
     * @param player игрок, не {@code null}
     * @return баланс в единицах валюты без преобразования в {@code double}
     * @throws NullPointerException если любой аргумент равен {@code null}
     * @throws IllegalArgumentException если валюта не реализует ни один поддерживаемый интерфейс
     * @throws IllegalStateException если для хранимой валюты серверный сервис не запущен
     */
    public static BigDecimal getBalance(ICurrency currency, Player player) {
        Objects.requireNonNull(currency, "currency");
        Objects.requireNonNull(player, "player");
        if (currency instanceof IExternalCurrency externalCurrency) {
            return getBalance(externalCurrency, player);
        }
        if (currency instanceof IStoredCurrency storedCurrency) {
            return getBalance(storedCurrency, player);
        }
        throw new IllegalArgumentException(
                "Unsupported currency class: " + currency.getClass().getName());
    }

    /**
     * Возвращает баланс внешней валюты, делегируя запрос её реализации.
     *
     * @param currency внешняя валюта, не {@code null}
     * @param player игрок, не {@code null}
     * @return баланс в единицах валюты; серверный сервис для этого запроса не требуется
     * @throws NullPointerException если любой аргумент равен {@code null}
     */
    public static BigDecimal getBalance(IExternalCurrency currency, Player player) {
        Objects.requireNonNull(currency, "currency");
        Objects.requireNonNull(player, "player");
        return currency.getBalance(player);
    }

    /**
     * Возвращает баланс хранимой валюты на серверном счёте игрока.
     *
     * @param currency хранимая валюта, не {@code null}
     * @param player игрок, не {@code null}
     * @return сохранённый баланс либо {@link IStoredCurrency#getDefaultBalance()} для новой валюты
     * @throws NullPointerException если любой аргумент равен {@code null}
     * @throws IllegalStateException если серверный сервис не запущен
     */
    public static BigDecimal getBalance(IStoredCurrency currency, Player player) {
        Objects.requireNonNull(currency, "currency");
        return getAccount(player).getBalance(currency);
    }

    /**
     * Возвращает доступный клиенту баланс валюты.
     *
     * <p>Для хранимой валюты используется синхронизированный счёт, без обращения к экземпляру игры.
     * Для внешней валюты используется текущий клиентский игрок.
     *
     * @param currency валюта или {@code null}, если валюта ещё не определена
     * @return баланс; ноль для {@code null}, неподдерживаемой валюты или внешней валюты без игрока
     */
    @Environment(EnvType.CLIENT)
    public static BigDecimal getBalanceOnClient(@Nullable ICurrency currency) {
        if (!(currency instanceof IExternalCurrency)) {
            return SDMEconomyServiceClient.getBalance(currency, null);
        }
        Minecraft minecraft = Minecraft.getInstance();
        return SDMEconomyServiceClient.getBalance(
                currency, minecraft == null ? null : minecraft.player);
    }

    /**
     * Регистрирует сериализатор типа внешних валют.
     *
     * @param id уникальный ID типа, не {@code null}
     * @param type сериализатор, не {@code null}; регистрируется до загрузки его валют
     * @throws NullPointerException если любой аргумент равен {@code null}
     */
    public static void registerCurrencyType(ResourceLocation id, ICurrencyType<?> type) {
        SDMEconomyCurrencyRegistry.registerType(id, type);
    }

    /**
     * Регистрирует хранимую валюту в памяти, заменяя регистрацию с тем же ID.
     *
     * @param <T> тип хранимой валюты
     * @param currency валюта с ненулевым ID, не {@code null}
     * @return переданный экземпляр валюты
     * @throws NullPointerException если валюта или её ID равны {@code null}
     */
    public static <T extends IStoredCurrency> T registerCurrency(T currency) {
        return SDMEconomyCurrencyRegistry.registerStoredCurrency(currency);
    }

    /**
     * Сохраняет хранимую валюту в файловую конфигурацию и синхронизирует её с клиентами.
     *
     * <p>Сохраняются текстовое имя и значок; произвольное поведение реализации и начальный баланс
     * не сериализуются. Сохранённая конфигурация имеет приоритет над регистрацией с тем же ID.
     *
     * @param <T> тип хранимой валюты
     * @param currency валюта с ненулевым ID, не {@code null}
     * @return {@code true}, если сохранение и регистрация завершились успешно
     * @throws NullPointerException если {@code currency} равен {@code null}
     */
    public static <T extends IStoredCurrency> boolean registerAndSaveCurrency(T currency) {
        return SDMEconomyCurrencyRegistry.registerAndSaveStoredCurrency(currency);
    }

    /**
     * Сохраняет внешнюю валюту и синхронизирует её с клиентами.
     *
     * @param <T> тип внешней валюты
     * @param currency валюта, не {@code null}; её тип должен быть предварительно зарегистрирован
     * @return {@code false} при отсутствии сериализатора или ошибке сохранения; иначе {@code true}
     * @throws NullPointerException если {@code currency} равен {@code null}
     */
    public static <T extends IExternalCurrency> boolean registerAndSaveCurrency(T currency) {
        return SDMEconomyCurrencyRegistry.registerAndSaveCurrency(currency);
    }

    /**
     * Возвращает копию реестра внешних валют указанной стороны.
     *
     * @param client {@code true} для клиента с учётом синхронизации с сервером
     * @return изменяемая копия карты валют
     */
    public static Map<ResourceLocation, IExternalCurrency> getCurrencies(boolean client) {
        return client
                ? SDMEconomyServiceClient.getAllExternalCurrencies()
                : SDMEconomyCurrencyRegistry.getCurrenciesMap();
    }

    /**
     * Возвращает копию реестра хранимых валют указанной стороны.
     *
     * @param client {@code true} для клиента с учётом локальных регистраций и синхронизации с
     *     сервером
     * @return изменяемая копия карты валют; во встроенном сервере используется серверный реестр
     */
    public static Map<ResourceLocation, IStoredCurrency> getStoredCurrencies(boolean client) {
        return client
                ? SDMEconomyServiceClient.getAllStoredCurrencies()
                : SDMEconomyCurrencyRegistry.getStoredCurrenciesMap();
    }

    /**
     * Ищет валюту в локальном реестре, отдавая приоритет внешней валюте при совпадении ID.
     *
     * @param id ID валюты, не {@code null}
     * @return найденная валюта или {@code null}
     * @throws NullPointerException если {@code id} равен {@code null}
     */
    @Nullable
    public static ICurrency getAnyCurrency(ResourceLocation id) {
        return SDMEconomyCurrencyRegistry.getAnyCurrency(Objects.requireNonNull(id, "id"));
    }

    /**
     * Ищет хранимую валюту в локальном реестре.
     *
     * @param id ID валюты, не {@code null}
     * @return найденная валюта или {@code null}
     * @throws NullPointerException если {@code id} равен {@code null}
     */
    @Nullable
    public static IStoredCurrency getStoredCurrency(ResourceLocation id) {
        return SDMEconomyCurrencyRegistry.getStoredCurrency(Objects.requireNonNull(id, "id"));
    }

    /**
     * Ищет внешнюю валюту в локальном реестре.
     *
     * @param id ID валюты, не {@code null}
     * @return найденная валюта или {@code null}
     * @throws NullPointerException если {@code id} равен {@code null}
     */
    @Nullable
    public static IExternalCurrency getCurrency(ResourceLocation id) {
        return SDMEconomyCurrencyRegistry.getCurrency(Objects.requireNonNull(id, "id"));
    }

    /**
     * Ищет внешнюю валюту по строковому ID в локальном реестре.
     *
     * @param id ID валюты; при отсутствии пространства имён используется {@code sdm}, не {@code
     *     null}
     * @return найденная валюта или {@code null} для неизвестного либо некорректного ID
     * @throws NullPointerException если {@code id} равен {@code null}
     */
    @Nullable
    public static IExternalCurrency getCurrency(String id) {
        return SDMEconomyCurrencyRegistry.getCurrency(id);
    }

    /**
     * Возвращает копию серверного реестра всех валют.
     *
     * @return изменяемая копия карты; внешняя валюта имеет приоритет при совпадении ID
     */
    public static Map<ResourceLocation, ICurrency> getAllCurrencies() {
        return getAllCurrencies(false);
    }

    /**
     * Возвращает копию реестра всех валют указанной стороны.
     *
     * @param client {@code true} для клиента с учётом синхронизации с сервером
     * @return изменяемая копия карты доступных валют
     */
    public static Map<ResourceLocation, ICurrency> getAllCurrencies(boolean client) {
        return client
                ? SDMEconomyServiceClient.getAllCurrencies()
                : SDMEconomyCurrencyRegistry.getAllCurrenciesMap();
    }
}
