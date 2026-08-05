# SDM Shop 2 Configuration

The config controls how shops, limiters, and statistics are stored, and also defines the server-side behavior of the shop keybind.

Example:

~~~yaml
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
~~~

## Main Fields

| Field | Type | Description |
|---|---:|---|
| <code>openShopKeybindShopId</code> | resource location/string | The shop opened by the client “Open Shop” keybind. An empty string disables keybind opening on the server side. If no namespace is specified, <code>sdm</code> is used. |
| <code>storageType</code> | enum | Storage type: <code>JSON</code>, <code>MONGODB</code>, or <code>CUSTOM</code>. |

## Storage Types

- <code>JSON</code> — shops and data are stored locally in the server config folder. Best for a single server.
- <code>MONGODB</code> — data is stored in MongoDB and can be synchronized across multiple servers.
- <code>CUSTOM</code> — storage is provided by an addon or another mod.

## MongoDB

The <code>mongodb</code> block is used only when <code>storageType: MONGODB</code>.

| Field | Type | Description |
|---|---:|---|
| <code>uri</code> | string | Connection string. Local example: <code>mongodb://127.0.0.1:27017/?replicaSet=rs0</code>. |
| <code>database</code> | string | Database name. |
| <code>shopsCollection</code> | string | Shop collection. |
| <code>limiterOffersCollection</code> | string | World/offer limiter collection. |
| <code>limiterPlayersCollection</code> | string | Player limiter collection. |
| <code>dailyStatsCollection</code> | string | Daily statistics collection. |
| <code>serverName</code> | string | Unique server name, such as <code>survival_1</code> or <code>lobby</code>. Used to prevent “echo” updates where a server reacts to its own database writes. |

If <code>serverName</code> is left empty, the core may generate a random identifier. For stable multi-server synchronization, set a clear persistent name.

