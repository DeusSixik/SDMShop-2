# SDM Shop 2

SDM Shop 2 — компонентный магазин на ECS-архитектуре. Магазины и товары собираются из компонентов: стоимость, награды, условия, лимиты, акции, категории и скриптовая логика.

## Документация

- [JSON: Компоненты магазина](JSON-Shop-Components-ECS-RU)
- [Java: ECS System](Java-ECS-System-RU)
- [Scripting: модификация логики через скрипты](Scripting-Modifying-Logic-with-Scripts-RU)
- [CONFIG: настройка файла конфигурации](CONFIG-Configuration-File-RU)

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
```

## Команды лимитов

```mcfunction
/sdm_shop limiter sync
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
