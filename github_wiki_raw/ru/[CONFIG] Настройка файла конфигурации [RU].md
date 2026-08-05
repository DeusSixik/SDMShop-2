# Конфигурация SDM Shop 2

Конфиг управляет способом хранения магазинов, лимитов и статистики, а также серверной настройкой кнопки открытия магазина.

Пример:

```yaml
openShopKeybindShopId: sdm:default
storageType: JSON
mongodb:
  uri: mongodb://127.0.0.1:27017/?replicaSet=rs0
  database: sdm_shop
  shopsCollection: shops
  limiterOffersCollection: limiter_offers
  limiterPlayersCollection: limiter_players
  dailyStatsCollection: daily_stats
  serverName: server_1
```

## Основные поля

| Поле | Тип | Описание |
|---|---:|---|
| `openShopKeybindShopId` | resource location/string | Магазин, который открывается клиентской кнопкой “Open Shop”. Пустая строка отключает открытие через keybind на стороне сервера. Если namespace не указан, используется `sdm`. |
| `storageType` | enum | Тип хранилища: `JSON`, `MONGODB` или `CUSTOM`. |

## Типы хранилища

- `JSON` — магазины и данные хранятся локально в конфиг-папке сервера. Подходит для одного сервера.
- `MONGODB` — данные хранятся в MongoDB и могут синхронизироваться между несколькими серверами.
- `CUSTOM` — хранилище реализует аддон/другой мод.

## MongoDB

Блок `mongodb` используется только при `storageType: MONGODB`.

| Поле | Тип | Описание |
|---|---:|---|
| `uri` | string | Строка подключения. Локальный пример: `mongodb://127.0.0.1:27017/?replicaSet=rs0`. |
| `database` | string | Имя базы данных. |
| `shopsCollection` | string | Коллекция магазинов. |
| `limiterOffersCollection` | string | Коллекция world/offer-лимитов. |
| `limiterPlayersCollection` | string | Коллекция player-лимитов. |
| `dailyStatsCollection` | string | Коллекция дневной статистики. |
| `serverName` | string | Уникальное имя сервера, например `survival_1` или `lobby`. Используется для защиты от “эхо”, чтобы сервер не реагировал на собственные обновления в базе. |

Если `serverName` оставить пустым, ядро может сгенерировать случайный идентификатор. Для стабильной мультисерверной синхронизации лучше задавать понятное постоянное имя.
