# Java: ECS System

SDM Shop 2 использует ECS-подход: магазин, категория и товар — это `ShopEntity`, а поведение задаётся набором компонентов. Вместо жёсткой иерархии классов товар собирается из маленьких независимых блоков: цена, награда, условие, акция, визуальная настройка.

## Ключевые идеи

- **Композиция вместо наследования** — один offer может иметь несколько цен, наград, условий, лимитеров и promo effects.
- **Компоненты одного типа можно повторять** — например, `World` + `Player` лимитер или несколько `MoneyCostComponent` в разных `group_id`.
- **Условия едины для покупки и UI** — всё, что наследует `ConditionComponent`, участвует в проверке покупки; `RenderHideComponent` использует эти же условия для скрытия недоступных товаров.
- **Smart Sync** — `shouldSync()` решает, отправлять ли компонент клиенту. Серверные проверки вроде script-condition могут оставаться только на сервере.
- **Декларативная сериализация** — компонент сам описывает свои JSON/network поля через `ComponentSerializer`.

## ShopEntity

`ShopEntity` хранит компоненты и управляет их жизненным циклом.

| Метод | Возвращает | Описание |
|---|---:|---|
| `addComponent(T component)` | `T` | Добавляет компонент, выставляет root, сортирует по `priority()` и вызывает update. |
| `hasComponent(Class<?> type)` | `boolean` | Проверяет наличие компонента указанного класса или наследника. |
| `getComponent(Class<T> type)` | `Optional<T>` | Возвращает первый компонент подходящего типа. |
| `getComponents()` | `ObjectList<ShopComponent>` | Возвращает неизменяемый список всех компонентов. |
| `getComponents(Class<T> type)` | `ObjectList<T>` | Возвращает кэшированный неизменяемый список компонентов подходящего типа. Используется `Class#isInstance`, поэтому наследники тоже попадают в результат. |
| `serialize()` / `deserialize(...)` | JSON | Сохраняет/читает компоненты. |
| `serializeNetwork(...)` / `deserializeNetwork(...)` | network | Передаёт только компоненты, у которых `shouldSync() == true`. |

Практический эффект: после того как `LimiterComponent` стал наследником `ConditionComponent`, `offer.getComponents(ConditionComponent.class)` автоматически включает лимитеры.

## ShopComponent

Базовый класс для всех компонентов.

| Метод | Возвращает | Описание |
|---|---:|---|
| `init()` | `void` | Вызывается после привязки к root и загрузки всех компонентов. |
| `priority()` | `int` | Порядок компонента внутри entity. Чем меньше значение, тем раньше компонент. |
| `getType()` | `IComponentType<?>` | Тип компонента для реестра и сериализации. |
| `getCategory()` | `ShopComponentCategory` | Категория для редактора/группировки. |
| `shouldSync()` | `boolean` | Если `false`, компонент не отправляется клиенту. |
| `getRoot()` | `ShopEntity` | Entity, к которому привязан компонент. |
| `getRoots<T>()` | `T` | То же, но с приведением типа. |
| `invokeUpdate()` | `void` | Помечает компонент dirty и сообщает root об изменении. |
| `additionalSerializer()` | `ComponentSerializer<?>` | Дополнительные поля, общие для семейства компонентов. Например, promo-компоненты добавляют `promo_id` и `scope`. |

## Категории компонентов

Категория влияет на UI/редактор и помогает группировать компоненты.

| Категория | Базовый класс | Примеры |
|---|---|---|
| `CONDITION` | `ConditionComponent` | `condition_limiter`, `condition_cooldown`, `condition_script` |
| `COST` | `CostComponent` | `cost_money` |
| `REWARD` | `RewardComponent` | `reward_item`, `reward_money`, `reward_command`, `reward_script` |
| `PROMO` | `PromoComponent` | `promo_time`, `promo_cooldown`, `promo_trigger` |
| `PROMO_EFFECT` | `PromoEffectComponent` | `discount`, `price_modifier` |
| `MISC` | `ShopComponent` | `name`, `catalog`, `hide_render`, контейнеры |

## ConditionComponent

Условия используются серверным процессором покупки и клиентским UI.

| Метод | Возвращает | Описание |
|---|---:|---|
| `isChecked(Player player)` | `boolean` | Главная проверка условия. |
| `verifiedOnClient()` | `boolean` | `true`, если условие можно проверить на клиенте. `false` — UI запросит состояние у сервера. |
| `recordPurchase(Player player, int amount)` | `void` | Hook после успешной покупки. Используется, например, cooldown-компонентом для записи `lastPurchaseTime`. |

Если на offer есть `RenderHideComponent`, UI скрывает offer, когда любое условие возвращает `false`.

## CostComponent

Базовый класс цены. Общий serializer добавляет поле `group_id`.

Покупка выбирает только компоненты цены из выбранной группы. Если `group_id` пустой, это группа по умолчанию. Несколько цен внутри одной группы списываются вместе.

## RewardComponent

Награды выдаются после успешной оплаты. Если выдача награды выбросила ошибку, процессор откатывает уже списанные `CostComponent` в обратном порядке.

## PromoComponent и PromoEffectComponent

Promo pipeline работает так:

1. `PromoComponent` собирают активные `promo_id`.
2. `PromoEffectComponent` выбираются по `target_promo_id` и `apply_groups`.
3. Effects сортируются по `priority`.
4. Каждый effect последовательно меняет цену через `PromoPriceContext`.
5. После effects вызываются script/server price events.
6. Цена санитизируется перед списанием.

Общие поля promo:

- `promo_id` — ID активной акции.
- `scope` — `GLOBAL` или `PLAYER`.

Общие поля effects:

- `target_promo_id` — к какой акции привязан effect.
- `priority` — порядок применения.
- `apply_groups` — группы оплаты, к которым применяется effect.

## Регистрация компонента

Регистрация выполняется один раз при инициализации:

```java
ShopComponentRegistry.register(MoneyCostComponent.TYPE);
ShopComponentRegistry.register(LimiterComponent.TYPE);
```

Тип компонента обычно наследует `SerializedComponentType<T>` и объявляет `ComponentSerializer`.

```java
private static final ComponentSerializer<MyComponent> SERIALIZER =
        ComponentSerializer.<MyComponent>create()
                .addRequired("value", FieldCodecs.INT, MyComponent::getValue, MyComponent::setValue);
```

## Пример работы с offer

```java
ShopOffer offer = ShopOffer.create(UUID.randomUUID(), true);

offer.addComponent(new NameComponent("Epic Sword"));
offer.addComponent(new MoneyCostComponent(ResourceLocation.tryBuild("sdm", "coins"), 1000.0D));
offer.addComponent(new ItemRewardComponent(Items.DIAMOND_SWORD.getDefaultInstance(), 1));
offer.addComponent(new LimiterComponent(LimiterComponent.LimiterType.Player, 1, 86_400_000L, "daily_player"));

offer.initializeServerOnlyComponents();

for (ConditionComponent condition : offer.getComponents(ConditionComponent.class)) {
    if (!condition.isChecked(player)) {
        return;
    }
}
```

Для реальной покупки лучше использовать `ShopTransactionProcessor`, потому что он атомарно проверяет условия, лимиты, стоимость, награды, откат оплаты и сетевой sync.

## Лимитеры

`LimiterComponent` остаётся отдельным компонентом с API лимитов, но теперь наследует `ConditionComponent`. Это значит:

- покупка по-прежнему проверяет лимиты на выбранное `amount`;
- UI может скрывать товар через `RenderHideComponent`;
- несколько лимитеров одного типа можно разделять через `limit_key`;
- reset-команды и `ShopLimiters` сбрасывают storage и отправляют sync клиентам.

Для внешнего кода используйте facade `ShopLimiters`:

```java
ShopLimiters.canPurchase(offer, player, amount);
ShopLimiters.recordPurchase(offer, player, amount);
ShopLimiters.getAvailable(offer, player);
ShopLimiters.getSnapshot(offer, player);
ShopLimiters.resetOffer(offer);
ShopLimiters.resetWorld(offer);
ShopLimiters.resetPlayer(offer, player);
```
