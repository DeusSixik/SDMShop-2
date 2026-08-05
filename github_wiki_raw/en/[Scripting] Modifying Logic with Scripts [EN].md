# Shop Scripting

SDM Shop 2 provides extension points for KubeJS, CraftTweaker, and Java addons. Scripts can add conditions, rewards, modify the final price, and react to shop loading.

Supported JSON components:

- <code>sdm:condition_script</code> — server-side purchase condition.
- <code>sdm:reward_script</code> — server-side reward.

Transaction hooks are also available:

- <code>registerBeforePurchaseEvent</code> — can cancel a purchase before conditions/payment.
- <code>registerAfterPurchaseEvent</code> — called after a successful purchase, limiter updates, and balance sync.
- <code>registerPurchaseFailedEvent</code> — called when a purchase is rejected with a reason.

## Script Condition

Offer JSON:

~~~json
{
  "type": "sdm:condition_script",
  "script_id": "my_script_id"
}
~~~

If the same offer also has <code>sdm:hide_render</code>, the UI requests the condition result from the server and hides the offer when the script returns <code>false</code>.

### KubeJS

~~~js
SDMShop.register(event => {
    event.registerConditionEvent((player, component, scriptId) => {
        if (scriptId == "my_script_id") {
            return player.stages.has("one");
        }

        // For unknown script_id values, returning true is safer
        // so other script conditions are not broken.
        return true;
    });
});
~~~

### CraftTweaker

~~~ts
import mods.sdmshop.scripting.ShopScripting;

ShopScripting.registerConditionEvent((player, component, scriptId) => {
    if (scriptId == "my_script_id") {
        return (<item:minecraft:cobblestone> in player.inventory);
    }

    return true;
});
~~~

Important: if there are no listeners for script-condition at all, the core returns <code>false</code>. Therefore every used <code>script_id</code> must have a registered handler.

## Script Reward

Offer JSON:

~~~json
{
  "type": "sdm:reward_script",
  "script_id": "give_custom_reward"
}
~~~

The reward runs after successful payment. The callback receives <code>amount</code>, the number of purchased offer units.

### KubeJS

~~~js
SDMShop.register(event => {
    event.registerRewardEvent((player, amount, component, scriptId) => {
        if (scriptId == "give_custom_reward") {
            for (let i = 0; i < amount; i++) {
                player.give("minecraft:diamond");
            }
        }
    });
});
~~~

### CraftTweaker

~~~ts
import mods.sdmshop.scripting.ShopScripting;

ShopScripting.registerRewardEvent((player, amount, component, scriptId) => {
    if (scriptId == "give_custom_reward") {
        for i in 0 .. amount {
            player.give(<item:minecraft:diamond>);
        }
    }
});
~~~

## Price Modification

The price event runs after normal promo effects and before final price sanitization. The handler receives a map <code>CostComponent -> Double</code> for the selected payment group.

### KubeJS

~~~js
SDMShop.register(event => {
    event.registerPriceEvent((offer, server, chosenGroupId, prices) => {
        if (chosenGroupId == "vip") {
            // prices is a Java Map<CostComponent, Double>.
            // Change values using the method supported by your KubeJS bridge version.
        }
    });
});
~~~

### CraftTweaker

~~~ts
import mods.sdmshop.scripting.ShopScripting;

ShopScripting.registerPriceEvent((offer, server, chosenGroupId, prices) => {
    if (chosenGroupId == "vip") {
        // Change the map values using the method supported by your CT bridge version.
    }
});
~~~

## Purchase Hooks

### Before Purchase

If the callback returns <code>false</code>, the purchase is cancelled with reason <code>SCRIPT_CANCELLED</code>.

~~~js
SDMShop.register(event => {
    event.registerBeforePurchaseEvent((offer, player, chosenGroupId, amount) => {
        if (amount > 64) {
            return false;
        }

        return true;
    });
});
~~~

### After Successful Purchase

~~~js
SDMShop.register(event => {
    event.registerAfterPurchaseEvent((offer, player, chosenGroupId, amount) => {
        console.info("Player " + player.name.string + " bought " + amount + " item(s)");
    });
});
~~~

### Purchase Failure

~~~js
SDMShop.register(event => {
    event.registerPurchaseFailedEvent((offer, player, chosenGroupId, amount, reason) => {
        console.info("Purchase failed: " + reason);
    });
});
~~~

Failure reasons:

- <code>INVALID_INPUT</code>
- <code>SCRIPT_CANCELLED</code>
- <code>CONDITION_FAILED</code>
- <code>LIMIT_FAILED</code>
- <code>NO_COST_GROUP</code>
- <code>INVALID_COST</code>
- <code>CANNOT_PAY</code>
- <code>PAYMENT_FAILED</code>
- <code>REWARD_FAILED</code>

## Shop Loading

<code>ScriptShopLoadEvent</code> runs when the script manager reloads and lets addons create or modify shops programmatically. On reload, listeners are cleared and the KubeJS event is registered again, so handlers should be declared in the normal script loading location.

## Convenient API Methods

These methods are available in KubeJS through the <code>event</code> object inside <code>SDMShop.register</code> and in CraftTweaker through <code>ShopScripting</code>.

### Promo

~~~js
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
~~~

### Limits

~~~js
event.resetOfferLimits("a94d5c7c-efb0-4c7e-a527-9e11b609151d");
event.resetWorldLimits("a94d5c7c-efb0-4c7e-a527-9e11b609151d");
event.resetPlayerLimits(player, "a94d5c7c-efb0-4c7e-a527-9e11b609151d");

event.canPurchase(player, "a94d5c7c-efb0-4c7e-a527-9e11b609151d", 1);
event.getAvailableLimit(player, "a94d5c7c-efb0-4c7e-a527-9e11b609151d");
event.syncLimiterData(player);
~~~

Limit methods take the offer UUID as a string. If the UUID is invalid or the offer is not found, methods safely return <code>false</code>/<code>0</code>.

## Practical Tips

- Use unique <code>script_id</code> values, for example <code>my_mod:vip_stage</code>.
- For conditions, return <code>true</code> for unknown IDs if one listener handles multiple components.
- Script conditions are server-side: do not put client-only logic into them.
- For prices, try standard <code>sdm:discount</code> or <code>sdm:price_modifier</code> first; use script price events for logic that cannot be expressed through JSON components.

