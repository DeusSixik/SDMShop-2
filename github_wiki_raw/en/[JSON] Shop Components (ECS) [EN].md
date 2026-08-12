# JSON: Shop Components (ECS)

In SDM Shop 2, a shop and each offer are assembled from components. Components can be combined: multiple costs, multiple conditions, multiple rewards, promos, visual settings, and script logic.

Basic offer shape:

~~~json
{
  "uuid": "a94d5c7c-efb0-4c7e-a527-9e11b609151d",
  "components": [
    {
      "type": "sdm:name",
      "name": "Epic Sword"
    }
  ]
}
~~~

If <code>uuid</code> is omitted, it is generated automatically. For stable limits and admin commands, it is better to set UUIDs explicitly.

## How a Purchase Is Processed

1. All <code>ConditionComponent</code> entries are checked.
2. Limits are checked for the requested purchase amount.
3. A cost group <code>group_id</code> is selected.
4. Active promos are collected and promo effects are applied by <code>priority</code>.
5. The cost is charged.
6. Rewards are granted.
7. Cooldowns/limits are updated and the UI is synced.

If at least one condition fails, the purchase is cancelled before any money is charged.

## Conditions

Conditions decide whether a purchase is available. If an offer has <code>sdm:hide_render</code>, the offer is hidden in the UI whenever at least one condition fails.

### Purchase Limit: <code>sdm:condition_limiter</code>

Limits the number of purchases. The limiter is now a full <code>ConditionComponent</code>, so it works both as server-side purchase protection and as a condition for <code>sdm:hide_render</code>.

~~~json
{
  "type": "sdm:condition_limiter",
  "limiter_type": "Player",
  "count": 5,
  "reset_interval_ms": 86400000,
  "limit_key": "daily_player_limit"
}
~~~

| Parameter | Type | Required | Description |
|---|---:|:---:|---|
| <code>limiter_type</code> | string | Yes | <code>Player</code> — separate counter per player. <code>World</code> — shared counter for the server/world. |
| <code>count</code> | int | Yes | Maximum purchases before blocking. Minimum is <code>1</code>. |
| <code>reset_interval_ms</code> | long | No | Auto-reset interval in milliseconds. <code>0</code> disables automatic reset. |
| <code>limit_key</code> | string | No | Stable storage key for this limiter. Needed when one offer has multiple limiters of the same type or when you do not want history to be lost after reordering components. |

Important:

- You can attach multiple limiters to one offer, for example <code>World</code> + <code>Player</code>.
- A purchase succeeds only when all limiters pass.
- UI cards and the purchase modal use the minimum available value across all limiters.
- If <code>reset_interval_ms > 0</code>, an offer hidden by <code>sdm:hide_render</code> returns to the UI automatically after the limit resets.
- Without <code>limit_key</code>, the first limiter of an old type uses the offer UUID for compatibility, and additional limiters receive a computed key based on order. For new complex offers, explicitly set <code>limit_key</code>.

### Purchase Cooldown: <code>sdm:condition_cooldown</code>

Prevents repeat purchases until the timer expires after a successful purchase.

~~~json
{
  "type": "sdm:condition_cooldown",
  "limiter_type": "Player",
  "cooldown_ms": 3600000
}
~~~

| Parameter | Type | Required | Description |
|---|---:|:---:|---|
| <code>cooldown_ms</code> | long | Yes | Time to wait before the next purchase, in milliseconds. |
| <code>limiter_type</code> | string | No | <code>Player</code> or <code>World</code>. Defaults to <code>Player</code>. |

Cooldown stores the last purchase time in limiter storage. A separate <code>sdm:condition_limiter</code> is not required for it. If the offer is hidden with <code>sdm:hide_render</code>, the UI returns the card automatically when the cooldown expires.

### Script Condition: <code>sdm:condition_script</code>

The check runs on the server through a KubeJS/CraftTweaker/Java listener.

~~~json
{
  "type": "sdm:condition_script",
  "script_id": "has_stage_vip"
}
~~~

| Parameter | Type | Required | Description |
|---|---:|:---:|---|
| <code>script_id</code> | string | No | Check ID used by your script to select the logic to run. |

Script conditions are not checked on the client. The UI requests the result from the server and can hide the offer through <code>sdm:hide_render</code>.

## Costs

Costs define what the player must pay. Multiple <code>CostComponent</code> entries with the same <code>group_id</code> are charged together. Different <code>group_id</code> values are alternative payment options.

### Money Cost: <code>sdm:cost_money</code>

~~~json
{
  "type": "sdm:cost_money",
  "money_id": "sdm:coins",
  "amount": 150.5,
  "group_id": "default"
}
~~~

| Parameter | Type | Required | Description |
|---|---:|:---:|---|
| <code>money_id</code> | resource location | Yes | Currency ID. |
| <code>amount</code> | double | Yes | Price for one item. Multiplied by amount when buying multiple items. |
| <code>group_id</code> | string | No | Payment group. Empty string means the default group. |

Example of alternative payment: one offer can be bought either with coins or diamonds if the UI/purchase logic passes the selected <code>group_id</code>.

### Item Cost: <code>sdm:cost_item</code>

Charges a specific item stack from the player's inventory.

~~~json
{
  "type": "sdm:cost_item",
  "item": "minecraft:diamond",
  "amount": 3,
  "group_id": "default"
}
~~~

With NBT matching:

~~~json
{
  "type": "sdm:cost_item",
  "item": "minecraft:diamond_sword",
  "amount": 1,
  "nbt": "{Damage:0}"
}
~~~

| Parameter | Type | Required | Description |
|---|---:|:---:|---|
| <code>item</code> | resource location | Yes | Item ID to charge. |
| <code>amount</code> | int | No | Base item amount before promo modifiers. Defaults to <code>1</code>. The final charged count is rounded up after modifiers. |
| <code>nbt</code> | string | No | Optional SNBT that must match the charged item stack. |
| <code>group_id</code> | string | No | Payment group. Empty string means the default group. |

### Item Tag Cost: <code>sdm:cost_item_tag</code>

Charges any items matching an item tag.

~~~json
{
  "type": "sdm:cost_item_tag",
  "tagKey": "minecraft:planks",
  "amount": 16,
  "group_id": "default"
}
~~~

| Parameter | Type | Required | Description |
|---|---:|:---:|---|
| <code>tagKey</code> | resource location | Yes | Item tag ID without the leading <code>#</code>. |
| <code>amount</code> | int | No | Base item amount before promo modifiers. Defaults to <code>1</code>. The final charged count is rounded up after modifiers. |
| <code>group_id</code> | string | No | Payment group. Empty string means the default group. |

The editor provides a searchable TagKey picker. This component uses item tags; block tags are only shown for fields typed as <code>TagKey&lt;Block&gt;</code>.


## Rewards

Rewards are granted after conditions pass and costs are charged. If granting a reward fails, already charged costs are rolled back.

### Money Reward: <code>sdm:reward_money</code>

~~~json
{
  "type": "sdm:reward_money",
  "money_id": "sdm:coins",
  "amount": 100.0
}
~~~

| Parameter | Type | Required | Description |
|---|---:|:---:|---|
| <code>money_id</code> | resource location | Yes | Currency ID. |
| <code>amount</code> | double | Yes | Amount granted per purchased item. |

### Item Reward: <code>sdm:reward_item</code>

~~~json
{
  "type": "sdm:reward_item",
  "item": "minecraft:diamond_sword",
  "amount": 1,
  "nbt": "{Damage:0}"
}
~~~

| Parameter | Type | Required | Description |
|---|---:|:---:|---|
| <code>item</code> | resource location | Yes | Item ID. |
| <code>amount</code> | int | No | Number of items. Defaults to <code>1</code>. |
| <code>nbt</code> | string | No | Item SNBT tags. |

### Command Reward: <code>sdm:reward_command</code>

~~~json
{
  "type": "sdm:reward_command",
  "name": "Grant VIP",
  "command": "lp user {player} parent add vip"
}
~~~

| Parameter | Type | Required | Description |
|---|---:|:---:|---|
| <code>name</code> | string | No | Reward name shown in the UI. |
| <code>command</code> | string | No | Server command. Supports <code>{player}</code> as the buyer name. |

The command is executed on the server. Usually the leading <code>/</code> is not needed.

### Script Reward: <code>sdm:reward_script</code>

~~~json
{
  "type": "sdm:reward_script",
  "script_id": "give_custom_bundle"
}
~~~

| Parameter | Type | Required | Description |
|---|---:|:---:|---|
| <code>script_id</code> | string | No | Reward ID used by the script to select the action. |

## Promos

Promo components do not change prices by themselves. They only declare an active <code>promo_id</code>. Price changes are performed by <code>PromoEffectComponent</code>.

All promo components share these fields:

| Parameter | Type | Required | Description |
|---|---:|:---:|---|
| <code>promo_id</code> | string | No | Promo ID. Effects use it to decide which promo they apply to. |
| <code>scope</code> | string | No | <code>GLOBAL</code> or <code>PLAYER</code>. Defaults to <code>GLOBAL</code>. Used by stateful components such as <code>promo_trigger</code>. |

### Time Promo: <code>sdm:promo_time</code>

~~~json
{
  "type": "sdm:promo_time",
  "promo_id": "weekend_sale",
  "mode": "REAL_TIME_EPOCH",
  "start_time": 1713000000000,
  "end_time": 1714000000000
}
~~~

| Parameter | Type | Required | Description |
|---|---:|:---:|---|
| <code>mode</code> | string | Yes | <code>REAL_TIME_EPOCH</code>, <code>SERVER_TICKS</code>, or <code>DAY_TIME</code>. |
| <code>start_time</code> | long | Yes | Window start. |
| <code>end_time</code> | long | Yes | Window end. If it equals <code>start_time</code>, the promo is always active. |
| <code>promo_id</code> | string | No | Promo ID. |
| <code>scope</code> | string | No | Inherited from promo. For a simple time window, <code>GLOBAL</code> is usually enough. |

For <code>DAY_TIME</code>, crossing midnight is supported: if <code>start_time > end_time</code>, the active interval wraps through 24000.

### Weekly Time Promo: <code>sdm:promo_weekly_time</code>

The promo is active on selected weekdays. The time inside the day is optional — if omitted, the promo works for the entire selected day.

~~~json
{
  "type": "sdm:promo_weekly_time",
  "promo_id": "monday_morning_sale",
  "days": ["MONDAY"],
  "start_time": "6",
  "end_time": "8"
}
~~~

Full-day Monday promo:

~~~json
{
  "type": "sdm:promo_weekly_time",
  "promo_id": "monday_sale",
  "days": ["MONDAY"]
}
~~~

| Parameter | Type | Required | Description |
|---|---:|:---:|---|
| <code>days</code> | string[] | Yes | Weekdays when the promo can be active: <code>MONDAY</code>, <code>TUESDAY</code>, <code>WEDNESDAY</code>, <code>THURSDAY</code>, <code>FRIDAY</code>, <code>SATURDAY</code>, <code>SUNDAY</code>. Multiple days are supported. |
| <code>start_time</code> | string | No | Daily window start. Supports hour (<code>"6"</code>), time (<code>"06:00"</code>), or minutes from midnight (<code>"360"</code>). If both <code>start_time</code> and <code>end_time</code> are omitted, the promo works for the entire day. |
| <code>end_time</code> | string | No | Daily window end. Uses the same format as <code>start_time</code>. |
| <code>promo_id</code> | string | No | Promo ID. |
| <code>scope</code> | string | No | Shared promo field. <code>GLOBAL</code> is usually enough when the promo depends only on the calendar. |

If <code>start_time</code> is greater than <code>end_time</code>, the interval crosses midnight. For example, <code>22:00</code> → <code>02:00</code> activates on the evening of the selected day and continues into the next night.

### Cooldown Promo: <code>sdm:promo_cooldown</code>

The promo is active when the offer has not been purchased for a long enough time.

~~~json
{
  "type": "sdm:promo_cooldown",
  "promo_id": "return_discount",
  "side": "Player",
  "cooldown_ms": 604800000
}
~~~

| Parameter | Type | Required | Description |
|---|---:|:---:|---|
| <code>cooldown_ms</code> | long | Yes | Time that must pass since the last purchase. |
| <code>side</code> | string | Yes | <code>Player</code> — per player. <code>World</code> — shared idle time for the offer. |
| <code>promo_id</code> | string | No | Promo ID. |
| <code>scope</code> | string | No | Shared promo field. |

### Triggered Promo: <code>sdm:promo_trigger</code>

The promo is activated by code/script/addon for a limited duration.

~~~json
{
  "type": "sdm:promo_trigger",
  "trigger_id": "boss_killed",
  "promo_id": "boss_sale",
  "duration_ms": 1800000,
  "scope": "GLOBAL"
}
~~~

| Parameter | Type | Required | Description |
|---|---:|:---:|---|
| <code>trigger_id</code> | string | No | External event ID. If empty, <code>promo_id</code> is used. |
| <code>duration_ms</code> | long | No | Active duration after trigger. <code>0</code> depends on storage/activation implementation. |
| <code>promo_id</code> | string | No | Promo ID for effects. |
| <code>scope</code> | string | No | <code>GLOBAL</code> or <code>PLAYER</code>. |

## Promo Effects

All promo effects share these fields:

| Parameter | Type | Required | Description |
|---|---:|:---:|---|
| <code>target_promo_id</code> | string | No | If set, the effect works only when a promo with this ID is active. If empty, it works with any active promo. |
| <code>priority</code> | int | No | Application order. Lower values are applied earlier. |
| <code>apply_groups</code> | string[] | No | List of <code>group_id</code> values affected by the effect. Empty list means all groups. |

### Discount: <code>sdm:discount</code>

Reduces the price by a fraction of the current price.

~~~json
{
  "type": "sdm:discount",
  "target_promo_id": "weekend_sale",
  "discount": 0.5,
  "priority": 0,
  "apply_groups": ["default"]
}
~~~

| Parameter | Type | Required | Description |
|---|---:|:---:|---|
| <code>discount</code> | double | Yes | Discount fraction: <code>0.3</code> = 30%, <code>0.5</code> = 50%. |

### Price Modifier: <code>sdm:price_modifier</code>

Universal effect for modifying prices.

~~~json
{
  "type": "sdm:price_modifier",
  "target_promo_id": "weekend_sale",
  "operation": "ADD_PERCENT",
  "value": -25.0,
  "target_money_ids": ["sdm:coins"],
  "priority": 10
}
~~~

| Parameter | Type | Required | Description |
|---|---:|:---:|---|
| <code>operation</code> | string | No | <code>ADD_FLAT</code>, <code>ADD_PERCENT</code>, <code>MULTIPLY</code>, <code>SET</code>, <code>MIN</code>, <code>MAX</code>. Defaults to <code>ADD_PERCENT</code>. |
| <code>value</code> | double | No | Operation value. |
| <code>target_money_ids</code> | resource location[] | No | If empty, applies to all money costs. If filled, applies only to the specified currencies. |

Operations:

- <code>ADD_FLAT</code>: <code>price + value</code>
- <code>ADD_PERCENT</code>: <code>price * (1 + value / 100)</code>; use <code>-25</code> for a 25% discount
- <code>MULTIPLY</code>: <code>price * value</code>
- <code>SET</code>: replace the price with <code>value</code>
- <code>MIN</code>: use the smaller value between current price and <code>value</code>
- <code>MAX</code>: use the larger value between current price and <code>value</code>

After all effects, the price is sanitized: negative, infinite, and NaN values become safe for the transaction.

## Misc Components

### Name: <code>sdm:name</code>

~~~json
{
  "type": "sdm:name",
  "name": "Epic Sword"
}
~~~

| Parameter | Type | Required | Description |
|---|---:|:---:|---|
| <code>name</code> | string | No | Visible offer name. If the string is a localization key, the UI can show the translated text. |

### Catalog/Category: <code>sdm:catalog</code>

~~~json
{
  "type": "sdm:catalog",
  "catalog_id": "weapons",
  "uuid": "0b2dfdcc-e9a8-4d6f-97c4-3ae18f711111",
  "order": 10,
  "icon_type": "TEXTURE",
  "icon_texture": "sdmshop2:textures/gui/category/weapons.png"
}
~~~

| Parameter | Type | Required | Description |
|---|---:|:---:|---|
| <code>catalog_id</code> | string | No | Category ID. |
| <code>uuid</code> | uuid | No | Stable category UUID. |
| <code>order</code> | int | No | Category sort order. |
| <code>icon_type</code> | string | No | Icon mode: <code>NONE</code>, <code>ITEM</code>, or <code>TEXTURE</code>. Defaults to <code>NONE</code>. |
| <code>icon_item</code> | item stack SNBT | No | ItemStack icon used when <code>icon_type</code> is <code>ITEM</code>. |
| <code>icon_texture</code> | resource location | No | Texture used when <code>icon_type</code> is <code>TEXTURE</code>. Example: <code>modid:textures/gui/icon.png</code>. |

Icon notes:

- <code>icon_item</code> uses the full ItemStack SNBT format, for example <code>{id:"minecraft:diamond",Count:1b}</code>.
- In editor mode, left-click the category icon slot to choose an item icon and right-click it to choose a texture icon. The texture picker shows loaded textures in a searchable grid.

### Hide When Unavailable: <code>sdm:hide_render</code>

~~~json
{
  "type": "sdm:hide_render"
}
~~~

Component without parameters. If present on an offer, the UI does not render the offer until all offer conditions pass. Works with:

- <code>sdm:condition_limiter</code>
- <code>sdm:condition_cooldown</code>
- <code>sdm:condition_script</code>
- any other component extending <code>ConditionComponent</code>

For cooldowns and limiters with reset intervals, the UI can return the offer automatically without reopening the shop.

### Offers Container: <code>sdm:offers_container</code>

Internal shop component that stores the offer list.

~~~json
{
  "type": "sdm:offers_container",
  "offers": []
}
~~~

Usually created by the core during shop initialization; manual creation is not required.

### Categories Container: <code>sdm:categories_manager</code>

Internal shop component for categories. Usually added automatically.

## Admin Limit Commands

~~~mcfunction
/sdm_shop limiter reset world <shop_id> <offer_id>
/sdm_shop limiter reset player <target> <shop_id> <offer_id>
/sdm_shop limiter reset offer <shop_id> <offer_id>
~~~

- <code>world</code> resets world limits for the offer.
- <code>player</code> resets player limits for the selected player.
- <code>offer</code> resets both world and player limits for the offer.
- After a successful reset, the server sends sync to clients, so the UI updates without reopening.

## Complex Offer Example

The offer costs 1000 coins, grants a sword, has a personal limit of 1 purchase per day, a global limit of 100 purchases, hides when unavailable, and receives a 50% weekend discount.

~~~json
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
      "nbt": "{Damage:0}"
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
~~~

