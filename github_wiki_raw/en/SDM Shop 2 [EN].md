# SDM Shop 2

SDM Shop 2 is a component-based shop built around an ECS-style architecture. Shops and offers are assembled from components: costs, rewards, conditions, limits, promos, categories, rendering settings, and script-driven logic.

## Documentation

- [JSON: Shop Components](JSON-Shop-Components-ECS-EN)
- [Java: ECS System](Java-ECS-System-EN)
- [Scripting: Modifying Logic with Scripts](Scripting-Modifying-Logic-with-Scripts-EN)
- [CONFIG: Configuration File](CONFIG-Configuration-File-EN)

## Important Notes About the Current Core

- The <code>sdm:condition_limiter</code> component is now a <code>ConditionComponent</code> and works with <code>sdm:hide_render</code>.
- Multiple limiters on the same offer are supported through <code>limit_key</code>.
- The UI receives limiter sync through a dedicated packet and refreshes after purchases/resets.
- Cooldowns and resettable limiters can return a hidden offer to an open UI without reopening the shop.
- Script conditions are checked on the server, while the UI requests their state only when needed.

## Basic Commands

~~~mcfunction
/sdm_shop create_shop <shop_id>
/sdm_shop open_shop <targets> <shop_id>
/sdm_shop reload shops
~~~

## Limit Commands

~~~mcfunction
/sdm_shop limiter sync
/sdm_shop limiter reset world <shop_id> <offer_id>
/sdm_shop limiter reset player <target> <shop_id> <offer_id>
/sdm_shop limiter reset offer <shop_id> <offer_id>
~~~

After a successful reset, the server sends a sync packet to clients, so an open UI updates without reopening the shop.

## Built-in Integrations

- SDM Economy 2
- SDM Data Lib
- SDM Events
- KubeJS / CraftTweaker bridge for script components

