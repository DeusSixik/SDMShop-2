---
title: "DragPayload"
slug: "drag-payload"
category: "Core/Widgets/DnD"
locale: "en"
order: 0
---

# DragPayload

Immutable transfer payload conveying identifier, type, preview text, and optional user object between drag source and drop target.

## Overview

`DragPayload` encapsulates data transferred between the drag initiator and candidate drop zones.

### Core Architecture

- **`id`**: Unique identifier for tracking the dragged instance or item.
- **`type`**: String MIME or semantic type (e.g. `item/inventory`, `dock/tab`). Supports wildcard prefix matching such as `item/*`.
- **`preview`**: Brief text representation displayed in default ghosts and tooltips.
- **`data`**: Optional attached in-memory Java object (`Object`), accessible via `data(Class<T>)`.

```java
DragPayload payload = DragPayload.of("item-42", "inventory/slot", "Diamond Sword", swordItem);
if (payload.matchesType("inventory/*")) {
    Sword item = payload.data(Sword.class);
}
```

## Creating and Checking Payloads

```java
DragPayload payload = DragPayload.of("item-42", "item.stack", "Iron Ingot", stackItem);
```

## Related links

- [DragController](<drag-controller.md>): Drag gesture controller attachable to any widget via composition to initiate drag-and-drop operations with ghost previews, threshold handling, and autoscrolling.
- [DropController](<drop-controller.md>): Drop target controller attachable to any widget via composition to filter payloads and handle drag enter, over, leave, and drop events.

## API

<div class="api-member-card" markdown="1">

### `DragPayload(String, String, String)` {: #dragpayloadstring-string-string data-toc-label="DragPayload(String, String, String)" }

Constructs a drag payload without extra object data.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique identifier for the payload. |
| `type` | `String` | MIME or semantic type string. |
| `preview` | `String` | Short preview text. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragPayload::</span>`data(Class)` {: #dataclass data-toc-label="data(Class)" }

Returns the attached data cast to the specified class, or null if absent or type mismatch.

| Parameter | Type | Description |
| --- | --- | --- |
| `type` | `Class` | Expected class of the data object. |

Returns `Object`: The payload data cast to the target type, or null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragPayload::</span>`hasData()` {: #hasdata data-toc-label="hasData()" }

Checks whether this payload carries attached data.

Returns `boolean`: True if a non-null payload data object is attached..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragPayload::</span>`matchesType(String)` {: #matchestypestring data-toc-label="matchesType(String)" }

Checks whether the payload type matches the expected filter.

| Parameter | Type | Description |
| --- | --- | --- |
| `expectedType` | `String` | Expected type string to match against. |

Returns `boolean`: True if the type matches the expected type string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragPayload::</span>`of(String, String, String)` {: #ofstring-string-string data-toc-label="of(String, String, String)" }

Factory method to create a payload without extra data.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique identifier for the payload. |
| `type` | `String` | MIME or semantic type string. |
| `preview` | `String` | Short preview text. |

Returns `DragPayload`: A newly created immutable DragPayload instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragPayload::</span>`of(String, String, String, Object)` {: #ofstring-string-string-object data-toc-label="of(String, String, String, Object)" }

Factory method to create a payload with arbitrary in-process data.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique identifier for the payload. |
| `type` | `String` | MIME or semantic type string. |
| `preview` | `String` | Short preview text. |
| `data` | `Object` | User data payload object. |

Returns `DragPayload`: A newly created immutable DragPayload instance..

</div>

