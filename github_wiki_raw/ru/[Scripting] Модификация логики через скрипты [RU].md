# Скриптинг магазина

SDM Shop 2 предоставляет точки расширения для KubeJS, CraftTweaker и Java-аддонов. Через скрипты можно добавлять условия, награды, править итоговую цену и реагировать на загрузку магазинов.

Поддерживаемые JSON-компоненты:

- `sdm:condition_script` — серверное условие покупки.
- `sdm:reward_script` — серверная награда.

Также доступны transaction hooks:

- `registerBeforePurchaseEvent` — можно отменить покупку до условий/оплаты.
- `registerAfterPurchaseEvent` — вызывается после успешной покупки, обновления лимитов и sync баланса.
- `registerPurchaseFailedEvent` — вызывается при отказе покупки с причиной.

## Скриптовое условие

JSON товара:

```json
{
  "type": "sdm:condition_script",
  "script_id": "my_script_id"
}
```

Если на этом же товаре есть `sdm:hide_render`, UI запросит результат условия с сервера и скроет товар, когда скрипт вернёт `false`.

### KubeJS

```js
SDMShop.register(event => {
    event.registerConditionEvent((player, component, scriptId) => {
        if (scriptId == "my_script_id") {
            return player.stages.has("one");
        }

        // Для чужих script_id лучше возвращать true,
        // чтобы не ломать другие скриптовые условия.
        return true;
    });
});
```

### CraftTweaker

```ts
import mods.sdmshop.scripting.ShopScripting;

ShopScripting.registerConditionEvent((player, component, scriptId) => {
    if (scriptId == "my_script_id") {
        return (<item:minecraft:cobblestone> in player.inventory);
    }

    return true;
});
```

Важно: если вообще нет listeners для script-condition, ядро возвращает `false`. Поэтому для каждого используемого `script_id` должен быть зарегистрирован обработчик.

## Скриптовая награда

JSON товара:

```json
{
  "type": "sdm:reward_script",
  "script_id": "give_custom_reward"
}
```

Награда вызывается после успешной оплаты. В callback приходит `amount` — количество купленных единиц товара.

### KubeJS

```js
SDMShop.register(event => {
    event.registerRewardEvent((player, amount, component, scriptId) => {
        if (scriptId == "give_custom_reward") {
            for (let i = 0; i < amount; i++) {
                player.give("minecraft:diamond");
            }
        }
    });
});
```

### CraftTweaker

```ts
import mods.sdmshop.scripting.ShopScripting;

ShopScripting.registerRewardEvent((player, amount, component, scriptId) => {
    if (scriptId == "give_custom_reward") {
        for i in 0 .. amount {
            player.give(<item:minecraft:diamond>);
        }
    }
});
```

## Изменение цены

Price event вызывается после обычных promo effects и до финальной санитизации цены. В обработчик приходит map `CostComponent -> Double` для выбранной группы оплаты.

### KubeJS

```js
SDMShop.register(event => {
    event.registerPriceEvent((offer, server, chosenGroupId, prices) => {
        if (chosenGroupId == "vip") {
            // prices — Java Map<CostComponent, Double>.
            // Меняйте значения способом, который поддерживает ваша версия KubeJS bridge.
        }
    });
});
```

### CraftTweaker

```ts
import mods.sdmshop.scripting.ShopScripting;

ShopScripting.registerPriceEvent((offer, server, chosenGroupId, prices) => {
    if (chosenGroupId == "vip") {
        // Измените значения map способом, который поддерживает ваша версия CT bridge.
    }
});
```

## Hooks покупки

### До покупки

Если callback возвращает `false`, покупка отменяется с причиной `SCRIPT_CANCELLED`.

```js
SDMShop.register(event => {
    event.registerBeforePurchaseEvent((offer, player, chosenGroupId, amount) => {
        if (amount > 64) {
            return false;
        }

        return true;
    });
});
```

### После успешной покупки

```js
SDMShop.register(event => {
    event.registerAfterPurchaseEvent((offer, player, chosenGroupId, amount) => {
        console.info("Player " + player.name.string + " bought " + amount + " item(s)");
    });
});
```

### Ошибка покупки

```js
SDMShop.register(event => {
    event.registerPurchaseFailedEvent((offer, player, chosenGroupId, amount, reason) => {
        console.info("Purchase failed: " + reason);
    });
});
```

Причины отказа:

- `INVALID_INPUT`
- `SCRIPT_CANCELLED`
- `CONDITION_FAILED`
- `LIMIT_FAILED`
- `NO_COST_GROUP`
- `INVALID_COST`
- `CANNOT_PAY`
- `PAYMENT_FAILED`
- `REWARD_FAILED`

## Загрузка магазинов

`ScriptShopLoadEvent` вызывается при reload скриптового manager и позволяет аддонам создавать/изменять магазины программно. На reload listeners очищаются и KubeJS-событие регистрируется заново, поэтому обработчики нужно объявлять в обычном месте загрузки скриптов.

## Удобные методы API

Эти методы доступны в KubeJS через объект `event` внутри `SDMShop.register` и в CraftTweaker через `ShopScripting`.

### Promo

```js
event.triggerGlobalPromo("boss_killed");
event.triggerPlayerPromo(player, "personal_bonus");
event.stopGlobalPromoTrigger("boss_killed");
event.stopPlayerPromoTrigger(player, "personal_bonus");

event.activateGlobalPromo("manual_sale", 60000);
event.activatePlayerPromo(player, "personal_sale", 60000);
event.deactivateGlobalPromo("manual_sale");
event.deactivatePlayerPromo(player, "personal_sale");

event.isGlobalPromoActive("manual_sale");
event.isPlayerPromoActive(player, "personal_sale");
```

### Лимиты

```js
event.resetOfferLimits("a94d5c7c-efb0-4c7e-a527-9e11b609151d");
event.resetWorldLimits("a94d5c7c-efb0-4c7e-a527-9e11b609151d");
event.resetPlayerLimits(player, "a94d5c7c-efb0-4c7e-a527-9e11b609151d");

event.canPurchase(player, "a94d5c7c-efb0-4c7e-a527-9e11b609151d", 1);
event.getAvailableLimit(player, "a94d5c7c-efb0-4c7e-a527-9e11b609151d");
event.syncLimiterData(player);
```

Методы лимитов принимают UUID offer строкой. Если UUID неверный или offer не найден, методы безопасно возвращают `false`/`0`.

## Практические советы

- Используйте уникальные `script_id`, например `my_mod:vip_stage`.
- Для условий возвращайте `true` для неизвестных ID, если один listener обслуживает несколько компонентов.
- Скриптовое условие — серверное: не храните в нём только клиентскую логику.
- Для цен сначала попробуйте стандартные `sdm:discount` или `sdm:price_modifier`; script price event используйте для логики, которую нельзя выразить JSON-компонентами.
