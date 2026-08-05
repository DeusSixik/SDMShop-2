# Java: ECS System

SDM Shop 2 uses an ECS-style approach: a shop, category, and offer are <code>ShopEntity</code> instances, and behavior is defined by a set of components. Instead of a rigid class hierarchy, an offer is assembled from small independent blocks: cost, reward, condition, promo, and rendering settings.

## Key Ideas

- **Composition instead of inheritance** — one offer can have multiple costs, rewards, conditions, limiters, and promo effects.
- **Components of the same type can repeat** — for example, a <code>World</code> limiter plus a <code>Player</code> limiter, or several <code>MoneyCostComponent</code> entries in different <code>group_id</code> groups.
- **Conditions are shared by purchasing and UI** — everything that extends <code>ConditionComponent</code> participates in purchase checks; <code>RenderHideComponent</code> uses the same conditions to hide unavailable offers.
- **Smart Sync** — <code>shouldSync()</code> decides whether a component is sent to the client. Server checks such as script conditions can remain server-only.
- **Declarative serialization** — each component declares its JSON/network fields through <code>ComponentSerializer</code>.

## ShopEntity

<code>ShopEntity</code> stores components and manages their lifecycle.

| Method | Returns | Description |
|---|---:|---|
| <code>addComponent(T component)</code> | <code>T</code> | Adds a component, assigns its root, sorts by <code>priority()</code>, and triggers an update. |
| <code>hasComponent(Class<?> type)</code> | <code>boolean</code> | Checks whether a component of the given class or subclass exists. |
| <code>getComponent(Class<T> type)</code> | <code>Optional<T></code> | Returns the first matching component. |
| <code>getComponents()</code> | <code>ObjectList&lt;ShopComponent&gt;</code> | Returns an immutable list of all components. |
| <code>getComponents(Class<T> type)</code> | <code>ObjectList&lt;T&gt;</code> | Returns a cached immutable list of matching components. Uses <code>Class#isInstance</code>, so subclasses are included. |
| <code>serialize()</code> / <code>deserialize(...)</code> | JSON | Saves/loads components. |
| <code>serializeNetwork(...)</code> / <code>deserializeNetwork(...)</code> | network | Sends only components where <code>shouldSync() == true</code>. |

Practical result: after <code>LimiterComponent</code> became a subclass of <code>ConditionComponent</code>, <code>offer.getComponents(ConditionComponent.class)</code> automatically includes limiters.

## ShopComponent

Base class for every component.

| Method | Returns | Description |
|---|---:|---|
| <code>init()</code> | <code>void</code> | Called after the component is attached to a root and all components are loaded. |
| <code>priority()</code> | <code>int</code> | Component order inside the entity. Lower values run earlier. |
| <code>getType()</code> | <code>IComponentType&lt;?&gt;</code> | Component type for registry and serialization. |
| <code>getCategory()</code> | <code>ShopComponentCategory</code> | Category used by the editor/grouping UI. |
| <code>shouldSync()</code> | <code>boolean</code> | If <code>false</code>, the component is not sent to clients. |
| <code>getRoot()</code> | <code>ShopEntity</code> | Entity this component is attached to. |
| <code>getRoots&lt;T&gt;()</code> | <code>T</code> | Same as <code>getRoot()</code>, but cast to the requested type. |
| <code>invokeUpdate()</code> | <code>void</code> | Marks the component dirty and notifies the root. |
| <code>additionalSerializer()</code> | <code>ComponentSerializer&lt;?&gt;</code> | Additional fields shared by a component family. For example, promo components add <code>promo_id</code> and <code>scope</code>. |

## Component Categories

Categories affect the editor UI and help group components.

| Category | Base Class | Examples |
|---|---|---|
| <code>CONDITION</code> | <code>ConditionComponent</code> | <code>condition_limiter</code>, <code>condition_cooldown</code>, <code>condition_script</code> |
| <code>COST</code> | <code>CostComponent</code> | <code>cost_money</code> |
| <code>REWARD</code> | <code>RewardComponent</code> | <code>reward_item</code>, <code>reward_money</code>, <code>reward_command</code>, <code>reward_script</code> |
| <code>PROMO</code> | <code>PromoComponent</code> | <code>promo_time</code>, <code>promo_weekly_time</code>, <code>promo_cooldown</code>, <code>promo_trigger</code> |
| <code>PROMO_EFFECT</code> | <code>PromoEffectComponent</code> | <code>discount</code>, <code>price_modifier</code> |
| <code>MISC</code> | <code>ShopComponent</code> | <code>name</code>, <code>catalog</code>, <code>hide_render</code>, containers |

## ConditionComponent

Conditions are used by the server purchase processor and the client UI.

| Method | Returns | Description |
|---|---:|---|
| <code>isChecked(Player player)</code> | <code>boolean</code> | Main condition check. |
| <code>verifiedOnClient()</code> | <code>boolean</code> | <code>true</code> if the condition can be checked on the client. <code>false</code> means the UI asks the server for the state. |
| <code>recordPurchase(Player player, int amount)</code> | <code>void</code> | Hook after a successful purchase. Used by cooldown components to store <code>lastPurchaseTime</code>. |

If an offer has <code>RenderHideComponent</code>, the UI hides the offer when any condition returns <code>false</code>.

## CostComponent

Base class for costs. The shared serializer adds the <code>group_id</code> field.

Purchasing selects only cost components from the chosen group. If <code>group_id</code> is empty, it belongs to the default group. Multiple costs in the same group are charged together.

## RewardComponent

Rewards are granted after successful payment. If granting a reward throws an error, the processor rolls back already charged <code>CostComponent</code> entries in reverse order.

## PromoComponent and PromoEffectComponent

The promo pipeline works like this:

1. <code>PromoComponent</code> collects active <code>promo_id</code> values.
2. <code>PromoEffectComponent</code> entries are selected by <code>target_promo_id</code> and <code>apply_groups</code>.
3. Effects are sorted by <code>priority</code>.
4. Each effect changes the price through <code>PromoPriceContext</code>.
5. Script/server price events run after effects.
6. The price is sanitized before charging.

Shared promo fields:

- <code>promo_id</code> — active promo ID.
- <code>scope</code> — <code>GLOBAL</code> or <code>PLAYER</code>.

Shared effect fields:

- <code>target_promo_id</code> — the promo this effect is bound to.
- <code>priority</code> — application order.
- <code>apply_groups</code> — payment groups affected by the effect.

## Registering a Component

Registration is performed once during initialization:

~~~java
ShopComponentRegistry.register(MoneyCostComponent.TYPE);
ShopComponentRegistry.register(LimiterComponent.TYPE);
~~~

A component type usually extends <code>SerializedComponentType&lt;T&gt;</code> and declares a <code>ComponentSerializer</code>.

~~~java
private static final ComponentSerializer<MyComponent> SERIALIZER =
        ComponentSerializer.<MyComponent>create()
                .addRequired("value", FieldCodecs.INT, MyComponent::getValue, MyComponent::setValue);
~~~

## Offer Example

~~~java
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
~~~

For real purchases, prefer <code>ShopTransactionProcessor</code>, because it atomically checks conditions, limits, costs, rewards, payment rollback, and network sync.

## Limiters

<code>LimiterComponent</code> remains a separate component with a limiter API, but now extends <code>ConditionComponent</code>. This means:

- purchasing still checks limits for the requested <code>amount</code>;
- the UI can hide the offer through <code>RenderHideComponent</code>;
- multiple limiters of the same type can be separated through <code>limit_key</code>;
- reset commands and <code>ShopLimiters</code> reset storage and sync clients.

Use the <code>ShopLimiters</code> facade from external code:

~~~java
ShopLimiters.canPurchase(offer, player, amount);
ShopLimiters.recordPurchase(offer, player, amount);
ShopLimiters.getAvailable(offer, player);
ShopLimiters.getSnapshot(offer, player);
ShopLimiters.resetOffer(offer);
ShopLimiters.resetWorld(offer);
ShopLimiters.resetPlayer(offer, player);
~~~

