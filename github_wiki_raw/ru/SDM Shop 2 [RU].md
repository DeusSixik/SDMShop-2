# SDM Shop 2

SDM Shop 2 — компонентный магазин на ECS-архитектуре. Магазины и товары собираются из компонентов: стоимость, награды, условия, лимиты, акции, категории и скриптовая логика.

## Документация

- [JSON: Компоненты магазина](https://github.com/DeusSixik/SDMShop2/wiki/%5BJSON%5D-%D0%9A%D0%BE%D0%BC%D0%BF%D0%BE%D0%BD%D0%B5%D0%BD%D1%82%D1%8B-%D0%BC%D0%B0%D0%B3%D0%B0%D0%B7%D0%B8%D0%BD%D0%B0-(ECS)-%5BRU%5D)
- [Java: ECS System](https://github.com/DeusSixik/SDMShop2/wiki/%5BJava%5D-ECS-System-%5BRU%5D)
- [Scripting: модификация логики через скрипты](https://github.com/DeusSixik/SDMShop2/wiki/%5BScripting%5D-%D0%9C%D0%BE%D0%B4%D0%B8%D1%84%D0%B8%D0%BA%D0%B0%D1%86%D0%B8%D1%8F-%D0%BB%D0%BE%D0%B3%D0%B8%D0%BA%D0%B8-%D1%87%D0%B5%D1%80%D0%B5%D0%B7-%D1%81%D0%BA%D1%80%D0%B8%D0%BF%D1%82%D1%8B-%5BRU%5D)
- [CONFIG: настройка файла конфигурации](https://github.com/DeusSixik/SDMShop2/wiki/%5BCONFIG%5D-%D0%9D%D0%B0%D1%81%D1%82%D1%80%D0%BE%D0%B9%D0%BA%D0%B0-%D1%84%D0%B0%D0%B9%D0%BB%D0%B0-%D0%BA%D0%BE%D0%BD%D1%84%D0%B8%D0%B3%D1%83%D1%80%D0%B0%D1%86%D0%B8%D0%B8-%5BRU%5D)

## Что важно в текущем ядре

- Лимитер `sdm:condition_limiter` теперь является `ConditionComponent` и работает с `sdm:hide_render`.
- Несколько лимитеров на одном товаре поддерживаются через `limit_key`.
- UI получает sync лимитов отдельным пакетом и обновляется после покупки/сброса.
- Cooldown и лимитеры с reset interval могут вернуть скрытый товар в открытом UI без переоткрытия магазина.
- Скриптовые условия проверяются на сервере, а UI запрашивает их состояние при необходимости.

## Базовые команды

```mcfunction
/sdm_shop create_shop <shop_id>
/sdm_shop open_shop <targets> <shop_id>
/sdm_shop reload shops
/sdm_shop synchronization limiter_data
```

## Команды лимитов

```mcfunction
/sdm_shop limiter reset world <shop_id> <offer_id>
/sdm_shop limiter reset player <target> <shop_id> <offer_id>
/sdm_shop limiter reset offer <shop_id> <offer_id>
```

После успешного сброса сервер отправляет sync клиентам, поэтому открытый UI обновляется без перезахода.

## Встроенные интеграции

- SDM Economy 2
- SDM Data Lib
- SDM Events
- KubeJS / CraftTweaker bridge для script-компонентов
