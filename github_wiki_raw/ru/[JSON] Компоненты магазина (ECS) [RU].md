# JSON: компоненты магазина (ECS)

В SDM Shop 2 магазин и каждый товар собираются из компонентов. Компоненты можно комбинировать: несколько цен, несколько условий, несколько наград, акции, визуальные настройки и скриптовую логику.

Базовая форма товара:

```json
{
  "uuid": "a94d5c7c-efb0-4c7e-a527-9e11b609151d",
  "components": [
    {
      "type": "sdm:name",
      "name": "Эпический меч"
    }
  ]
}
```

Если `uuid` не указан, он будет создан автоматически. Для стабильных лимитов и админ-команд лучше задавать UUID явно.

## Как выполняется покупка

1. Проверяются все `ConditionComponent`.
2. Проверяются лимиты на нужное количество покупки.
3. Выбирается группа стоимости `group_id`.
4. Считаются активные promo и применяются promo effects по `priority`.
5. Списывается стоимость.
6. Выдаются награды.
7. Обновляются cooldown/лимиты и отправляется sync UI.

Если хотя бы одно условие не прошло, покупка отменяется до списания средств.

## Условия

Условия решают, доступна ли покупка. Если на товаре есть `sdm:hide_render`, товар скрывается в UI, когда хотя бы одно условие не выполнено.

### Лимит покупок: `sdm:condition_limiter`

Ограничивает количество покупок. Сейчас лимитер является полноценным `ConditionComponent`, поэтому работает и как серверная защита покупки, и как условие для `sdm:hide_render`.

```json
{
  "type": "sdm:condition_limiter",
  "limiter_type": "Player",
  "count": 5,
  "reset_interval_ms": 86400000,
  "limit_key": "daily_player_limit"
}
```

| Параметр | Тип | Обязательно | Описание |
|---|---:|:---:|---|
| `limiter_type` | string | Да | `Player` — отдельный счётчик для каждого игрока. `World` — общий счётчик на сервер/мир. |
| `count` | int | Да | Максимальное количество покупок до блокировки. Минимум — 1. |
| `reset_interval_ms` | long | Нет | Интервал автосброса в миллисекундах. `0` — лимит не сбрасывается сам. |
| `limit_key` | string | Нет | Стабильный ключ хранилища конкретного лимитера. Нужен, если на одном товаре несколько лимитеров одного типа или вы не хотите терять историю при перестановке компонентов. |

Важно:

- Можно ставить несколько лимитеров на один товар, например `World` + `Player`.
- Покупка проходит только если прошли все лимитеры.
- UI карточки и модалка используют минимально доступное значение по всем лимитерам.
- При `reset_interval_ms > 0` скрытый через `sdm:hide_render` товар возвращается в UI автоматически после сброса лимита.
- Без `limit_key` первый лимитер старого типа использует UUID товара для совместимости, а дополнительные лимитеры получают вычисленный ключ по порядку. Для новых сложных товаров лучше явно задавать `limit_key`.

### Кулдаун покупки: `sdm:condition_cooldown`

Запрещает повторную покупку до истечения таймера после успешной покупки.

```json
{
  "type": "sdm:condition_cooldown",
  "limiter_type": "Player",
  "cooldown_ms": 3600000
}
```

| Параметр | Тип | Обязательно | Описание |
|---|---:|:---:|---|
| `cooldown_ms` | long | Да | Время ожидания до следующей покупки в миллисекундах. |
| `limiter_type` | string | Нет | `Player` или `World`. По умолчанию `Player`. |

Кулдаун хранит время последней покупки в limiter storage. Отдельный `sdm:condition_limiter` для него не нужен. Если товар скрыт через `sdm:hide_render`, UI вернёт карточку автоматически после истечения cooldown.

### Скриптовое условие: `sdm:condition_script`

Проверка выполняется на сервере через KubeJS/CraftTweaker/Java listener.

```json
{
  "type": "sdm:condition_script",
  "script_id": "has_stage_vip"
}
```

| Параметр | Тип | Обязательно | Описание |
|---|---:|:---:|---|
| `script_id` | string | Нет | ID проверки, по которому ваш скрипт понимает, какую логику выполнить. |

Скриптовое условие не проверяется на клиенте. UI запрашивает результат с сервера и может скрыть товар через `sdm:hide_render`.

## Стоимость

Стоимость определяет, что игрок должен отдать. Несколько `CostComponent` с одинаковым `group_id` списываются вместе. Разные `group_id` используются как альтернативные варианты оплаты.

### Денежная стоимость: `sdm:cost_money`

```json
{
  "type": "sdm:cost_money",
  "money_id": "sdm:coins",
  "amount": 150.5,
  "group_id": "default"
}
```

| Параметр | Тип | Обязательно | Описание |
|---|---:|:---:|---|
| `money_id` | resource location | Да | ID валюты. |
| `amount` | double | Да | Цена за 1 единицу товара. При покупке нескольких штук умножается на количество. |
| `group_id` | string | Нет | Группа оплаты. Пустая строка — группа по умолчанию. |

Пример альтернативной оплаты: один товар можно купить либо за монеты, либо за алмазы, если UI/логика покупки передаст выбранный `group_id`.

## Награды

Награды выдаются после успешной проверки условий и списания стоимости. При ошибке выдачи награды уже списанная стоимость откатывается.

### Выдача валюты: `sdm:reward_money`

```json
{
  "type": "sdm:reward_money",
  "money_id": "sdm:coins",
  "amount": 100.0
}
```

| Параметр | Тип | Обязательно | Описание |
|---|---:|:---:|---|
| `money_id` | resource location | Да | ID валюты. |
| `amount` | double | Да | Сколько начислить за 1 единицу товара. |

### Выдача предмета: `sdm:reward_item`

```json
{
  "type": "sdm:reward_item",
  "item": "minecraft:diamond_sword",
  "amount": 1,
  "nbt": "{Damage:0,Enchantments:[{id:\"minecraft:sharpness\",lvl:5s}]}"
}
```

| Параметр | Тип | Обязательно | Описание |
|---|---:|:---:|---|
| `item` | resource location | Да | ID предмета. |
| `amount` | int | Нет | Количество предметов. По умолчанию `1`. |
| `nbt` | string | Нет | SNBT-теги предмета. |

### Выполнение команды: `sdm:reward_command`

```json
{
  "type": "sdm:reward_command",
  "name": "Выдать VIP",
  "command": "lp user {player} parent add vip"
}
```

| Параметр | Тип | Обязательно | Описание |
|---|---:|:---:|---|
| `name` | string | Нет | Название награды в UI. |
| `command` | string | Нет | Команда сервера. Поддерживается `{player}` — имя игрока-покупателя. |

Команда выполняется на сервере. Обычно начальный `/` не нужен.

### Скриптовая награда: `sdm:reward_script`

```json
{
  "type": "sdm:reward_script",
  "script_id": "give_custom_bundle"
}
```

| Параметр | Тип | Обязательно | Описание |
|---|---:|:---:|---|
| `script_id` | string | Нет | ID награды, по которому скрипт выбирает действие. |

## Акции

Promo-компоненты сами цену не меняют. Они только объявляют активный `promo_id`. Изменение цены делают `PromoEffectComponent`.

У всех promo-компонентов есть общие поля:

| Параметр | Тип | Обязательно | Описание |
|---|---:|:---:|---|
| `promo_id` | string | Нет | ID акции. По нему эффекты выбирают, к какой акции применяться. |
| `scope` | string | Нет | `GLOBAL` или `PLAYER`. По умолчанию `GLOBAL`. Используется компонентами с состоянием, например `promo_trigger`. |

### Акция по времени: `sdm:promo_time`

```json
{
  "type": "sdm:promo_time",
  "promo_id": "weekend_sale",
  "mode": "REAL_TIME_EPOCH",
  "start_time": 1713000000000,
  "end_time": 1714000000000
}
```

| Параметр | Тип | Обязательно | Описание |
|---|---:|:---:|---|
| `mode` | string | Да | `REAL_TIME_EPOCH`, `SERVER_TICKS` или `DAY_TIME`. |
| `start_time` | long | Да | Начало окна. |
| `end_time` | long | Да | Конец окна. Если равно `start_time`, акция активна всегда. |
| `promo_id` | string | Нет | ID акции. |
| `scope` | string | Нет | Наследуется от promo, но для простого time-окна обычно достаточно `GLOBAL`. |

Для `DAY_TIME` поддерживается переход через ночь: если `start_time > end_time`, активным считается интервал через 24000.

### Акция после простоя: `sdm:promo_cooldown`

Акция активна, если товар давно не покупали.

```json
{
  "type": "sdm:promo_cooldown",
  "promo_id": "return_discount",
  "side": "Player",
  "cooldown_ms": 604800000
}
```

| Параметр | Тип | Обязательно | Описание |
|---|---:|:---:|---|
| `cooldown_ms` | long | Да | Сколько времени должно пройти с последней покупки. |
| `side` | string | Да | `Player` — отдельно для игрока. `World` — общий простой товара. |
| `promo_id` | string | Нет | ID акции. |
| `scope` | string | Нет | Общее promo-поле. |

### Триггерная акция: `sdm:promo_trigger`

Акция активируется кодом/скриптом/аддоном на ограниченное время.

```json
{
  "type": "sdm:promo_trigger",
  "trigger_id": "boss_killed",
  "promo_id": "boss_sale",
  "duration_ms": 1800000,
  "scope": "GLOBAL"
}
```

| Параметр | Тип | Обязательно | Описание |
|---|---:|:---:|---|
| `trigger_id` | string | Нет | Внешний ID события. Если пусто, используется `promo_id`. |
| `duration_ms` | long | Нет | Длительность активности после trigger. `0` — зависит от реализации хранилища/активации. |
| `promo_id` | string | Нет | ID акции для effects. |
| `scope` | string | Нет | `GLOBAL` или `PLAYER`. |

## Эффекты акций

У всех promo effects есть общие поля:

| Параметр | Тип | Обязательно | Описание |
|---|---:|:---:|---|
| `target_promo_id` | string | Нет | Если указан, эффект работает только при активной акции с таким ID. Если пустой — при любой активной акции. |
| `priority` | int | Нет | Порядок применения. Меньшее значение применяется раньше. |
| `apply_groups` | string[] | Нет | Список `group_id`, к которым применяется эффект. Пустой список — ко всем группам. |

### Скидка: `sdm:discount`

Уменьшает цену на долю от текущей цены.

```json
{
  "type": "sdm:discount",
  "target_promo_id": "weekend_sale",
  "discount": 0.5,
  "priority": 0,
  "apply_groups": ["default"]
}
```

| Параметр | Тип | Обязательно | Описание |
|---|---:|:---:|---|
| `discount` | double | Да | Доля скидки: `0.3` = 30%, `0.5` = 50%. |

### Модификатор цены: `sdm:price_modifier`

Универсальный эффект для изменения цены.

```json
{
  "type": "sdm:price_modifier",
  "target_promo_id": "weekend_sale",
  "operation": "ADD_PERCENT",
  "value": -25.0,
  "target_money_ids": ["sdm:coins"],
  "priority": 10
}
```

| Параметр | Тип | Обязательно | Описание |
|---|---:|:---:|---|
| `operation` | string | Нет | `ADD_FLAT`, `ADD_PERCENT`, `MULTIPLY`, `SET`, `MIN`, `MAX`. По умолчанию `ADD_PERCENT`. |
| `value` | double | Нет | Значение операции. |
| `target_money_ids` | resource location[] | Нет | Если пусто, применяется ко всем money-cost. Если заполнено — только к указанным валютам. |

Операции:

- `ADD_FLAT`: `price + value`
- `ADD_PERCENT`: `price * (1 + value / 100)`; для скидки 25% используйте `-25`
- `MULTIPLY`: `price * value`
- `SET`: заменить цену на `value`
- `MIN`: взять меньшее из текущей цены и `value`
- `MAX`: взять большее из текущей цены и `value`

После всех эффектов цена проходит санитизацию: отрицательные, бесконечные и NaN-значения становятся безопасными для транзакции.

## Прочие компоненты

### Название: `sdm:name`

```json
{
  "type": "sdm:name",
  "name": "Эпический меч"
}
```

| Параметр | Тип | Обязательно | Описание |
|---|---:|:---:|---|
| `name` | string | Нет | Отображаемое имя товара. Если строка является ключом локализации, UI может показать перевод. |

### Каталог/категория: `sdm:catalog`

```json
{
  "type": "sdm:catalog",
  "catalog_id": "weapons",
  "uuid": "0b2dfdcc-e9a8-4d6f-97c4-3ae18f711111",
  "order": 10
}
```

| Параметр | Тип | Обязательно | Описание |
|---|---:|:---:|---|
| `catalog_id` | string | Нет | ID категории. |
| `uuid` | uuid | Нет | Стабильный UUID категории. |
| `order` | int | Нет | Порядок сортировки категории. |

### Скрывать при недоступности: `sdm:hide_render`

```json
{
  "type": "sdm:hide_render"
}
```

Компонент без параметров. Если он есть на товаре, UI не рисует товар, пока не выполнены все условия товара. Работает с:

- `sdm:condition_limiter`
- `sdm:condition_cooldown`
- `sdm:condition_script`
- любыми другими компонентами, наследующими `ConditionComponent`

Для cooldown и лимитеров с reset interval UI умеет вернуть товар автоматически без переоткрытия магазина.

### Контейнер товаров: `sdm:offers_container`

Внутренний компонент магазина, который хранит список offer.

```json
{
  "type": "sdm:offers_container",
  "offers": []
}
```

Обычно его создаёт ядро при инициализации магазина, вручную добавлять не требуется.

### Контейнер категорий: `sdm:categories_manager`

Внутренний компонент магазина для категорий. Обычно добавляется автоматически.

## Админ-команды лимитов

```mcfunction
/sdm_shop limiter reset world <shop_id> <offer_id>
/sdm_shop limiter reset player <target> <shop_id> <offer_id>
/sdm_shop limiter reset offer <shop_id> <offer_id>
```

- `world` сбрасывает world-лимиты товара.
- `player` сбрасывает player-лимиты товара для выбранного игрока.
- `offer` сбрасывает и world, и player-лимиты товара.
- После успешного сброса сервер отправляет sync клиентам, поэтому UI обновляется без перезахода.

## Пример сложного товара

Товар стоит 1000 монет, выдаёт меч, имеет личный лимит 1 покупка в день, глобальный лимит 100 покупок, скрывается при недоступности и получает скидку 50% на выходных.

```json
{
  "uuid": "a94d5c7c-efb0-4c7e-a527-9e11b609151d",
  "components": [
    {
      "type": "sdm:name",
      "name": "shop.offer.epic_sword"
    },
    {
      "type": "sdm:catalog",
      "catalog_id": "weapons",
      "order": 10
    },
    {
      "type": "sdm:cost_money",
      "money_id": "sdm:coins",
      "amount": 1000.0,
      "group_id": "default"
    },
    {
      "type": "sdm:reward_item",
      "item": "minecraft:diamond_sword",
      "amount": 1,
      "nbt": "{Damage:0,Enchantments:[{id:\"minecraft:sharpness\",lvl:5s}]}"
    },
    {
      "type": "sdm:condition_limiter",
      "limiter_type": "Player",
      "count": 1,
      "reset_interval_ms": 86400000,
      "limit_key": "daily_player"
    },
    {
      "type": "sdm:condition_limiter",
      "limiter_type": "World",
      "count": 100,
      "limit_key": "global_stock"
    },
    {
      "type": "sdm:hide_render"
    },
    {
      "type": "sdm:promo_time",
      "promo_id": "weekend_sale",
      "mode": "DAY_TIME",
      "start_time": 0,
      "end_time": 24000
    },
    {
      "type": "sdm:discount",
      "target_promo_id": "weekend_sale",
      "discount": 0.5,
      "apply_groups": ["default"]
    }
  ]
}
```
