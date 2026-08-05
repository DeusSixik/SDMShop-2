# SDM Shop 2

**SDM Shop 2** is a modern rewrite of the original SDM Shop mod: a flexible, configurable and server-authoritative shop system for Minecraft servers and modpacks.

Instead of hardcoding every offer into one fixed format, SDM Shop 2 uses an **Entity Component System (ECS)**. Shops and offers are assembled from reusable components such as prices, rewards, conditions, limits, categories and promos.

> **Current status:** `1.0.0-beta`
>
> The core shop flow is usable for testing and early server setups, but this release should still be treated as beta while dedicated-server and Fabric/Forge testing continues.

---

## Key Features

### Component-Based Shops

Mix and match components to build anything from a simple block shop to a scripted economy system:

- **Costs:** money/currency costs and grouped payment options.
- **Rewards:** items, money, commands and script rewards.
- **Conditions:** cooldowns, limiter-backed checks and script conditions.
- **Promos:** time-based, weekly, cooldown-based and externally triggered promos.
- **Categories:** organized shop tabs with an always-available **All** category.

### Server-Side Purchase Logic

The client can preview offers and request price data, but the final transaction is handled on the server.

During a purchase, the server:

1. checks conditions and limits;
2. calculates the final price, including active promos;
3. verifies all payment sources;
4. charges the player;
5. rolls back payment if reward delivery fails;
6. gives rewards and syncs updated limiter/currency data back to the client.

### Modern LDLib UI

The shop UI is built on LDLib and includes:

- category tabs;
- search and sorting;
- advanced currency filtering;
- favorite offers pinned before regular offers;
- purchase modal with quantity input, min/max controls and total price preview;
- toast notifications;
- client-side success/failure sounds.

### In-Game Editor Foundation

SDM Shop 2 includes an editor foundation for creating and adjusting shops in-game:

- draft shop and currency sessions;
- component search and category sorting;
- favorite components;
- generated component configuration widgets;
- editor history stored in the client cache;
- save/reset flow for changes.

### Default Shop

Fresh installs can generate a default `sdm:default` shop, so you can test the mod without writing configs first.

The default shop includes starter item-currency setup and categories such as:

- resources;
- building blocks;
- farming and food;
- utility;
- redstone;
- rare items;
- exchange.

### Flexible Storage

Supported storage modes:

- **JSON** — default local file storage;
- **MongoDB** — optional database-backed storage for larger setups;
- **CUSTOM** — hook for custom repository implementations.

---

## Commands

```text
/sdm_shop create_shop <shop_id>
/sdm_shop open_shop <targets> <shop_id>
/sdm_shop reload shops
/sdm_shop limiter sync
/sdm_shop limiter reset world <shop_id> <offer_id>
/sdm_shop limiter reset player <target> <shop_id> <offer_id>
/sdm_shop limiter reset offer <shop_id> <offer_id>
```

---

## Scripting and Events

SDM Shop 2 exposes hooks for:

- shop loading;
- price calculation;
- before purchase;
- after purchase;
- failed purchase;
- script conditions;
- script rewards;
- externally triggered promos.

KubeJS and CraftTweaker integration classes are present in the codebase.

---

## Requirements

- **Minecraft:** 1.20.1
- **Loaders:** Fabric / Forge
- **Required dependencies:** Architectury API, LDLib

---

## Beta Notice

This is a beta rewrite. The main shop flow is usable, but breaking changes and bug fixes may still happen while the API, editor and integration points are finalized.

Recommended test areas before using on a production server:

- dedicated server startup;
- opening shops from keybind and command;
- item-currency payments;
- stored/virtual currency payments;
- limiter synchronization;
- promo activation and expiration;
- editor save/reset flow;
- Fabric and Forge parity.
