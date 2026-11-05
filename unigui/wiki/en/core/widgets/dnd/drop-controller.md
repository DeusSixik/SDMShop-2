---
title: "DropController"
slug: "drop-controller"
category: "Core/Widgets/DnD"
locale: "en"
order: 0
---

# DropController

Drop target controller attachable to any widget via composition to filter payloads and handle drag enter, over, leave, and drop events.

## Overview

`DropController` provides drop target validation and lifecycle management for container widgets.

### Usage Pattern

```java
DropController.attach(targetContainer)
        .accepts("items/weapon", "items/armor")
        .onDragEnter(event -> targetContainer.style().border(ColorView.GREEN, 2.0f))
        .onDragLeave(event -> targetContainer.style().clearBorder())
        .onDrop(event -> addItem(event.payload().data(Item.class)));
```

### Features

- **Declarative Filtering**: Set allowed types via `accepts(String...)` or custom logic via `accepts(Predicate<DragPayload>)`.
- **Automatic Claiming**: Automatically calls `event.accept(zone)` when payload matches.
- **Lifecycle Callbacks**: Convenient chained handlers for `onDragEnter`, `onDragOver`, `onDragLeave`, and `onDrop`.

## Attaching Drop Controller with Validation

```java
DropController.attach(targetPanel)
        .accepts("card")
        .onDrop(event -> System.out.println("Dropped: " + event.payload().id()));
```

## Related links

- [DragController](<drag-controller.md>): Drag gesture controller attachable to any widget via composition to initiate drag-and-drop operations with ghost previews, threshold handling, and autoscrolling.
- [DragSession](<drag-session.md>): Live state and controller of an active drag gesture, managing pointer coordinates, active drop target, ghost positioning, insertion indicators, and autoscrolling.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`accepts(DragPayload)` {: #acceptsdragpayload data-toc-label="accepts(DragPayload)" }

Tests whether the specified payload matches type and validator requirements.

| Parameter | Type | Description |
| --- | --- | --- |
| `payload` | `DragPayload` | Payload to validate. |

Returns `boolean`: True if the payload is accepted by this drop zone..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`accepts(Predicate)` {: #acceptspredicate data-toc-label="accepts(Predicate)" }

Adds a custom predicate validation rule for candidate payloads.

| Parameter | Type | Description |
| --- | --- | --- |
| `validator` | `Predicate` | Predicate validator for payloads. |

Returns `DropController`: This DropController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`accepts(String)` {: #acceptsstring data-toc-label="accepts(String)" }

Restricts accepted payload types by comma/pipe/whitespace delimited string (* accepts all).

| Parameter | Type | Description |
| --- | --- | --- |
| `types` | `String` | Accepted payload types. |

Returns `DropController`: This DropController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`attach(Widget)` {: #attachwidget data-toc-label="attach(Widget)" }

Attaches a DropController to the specified widget zone.

| Parameter | Type | Description |
| --- | --- | --- |
| `zone` | `Widget` | [Widget](<../../../widgets/widget.md>) acting as the drop target zone. |

Returns `DropController`: A newly attached DropController instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`detach()` {: #detach data-toc-label="detach()" }

Detaches event listeners from the host drop zone widget.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`enabled(boolean)` {: #enabledboolean data-toc-label="enabled(boolean)" }

Enables or disables this drop controller.

| Parameter | Type | Description |
| --- | --- | --- |
| `enabled` | `boolean` | Whether the drop target is enabled. |

Returns `DropController`: This DropController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`hovering()` {: #hovering data-toc-label="hovering()" }

Returns true while an accepted drag payload is hovering over the zone.

Returns `boolean`: True if a drag gesture is hovering over this drop zone..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`onDragEnter(Consumer)` {: #ondragenterconsumer data-toc-label="onDragEnter(Consumer)" }

Registers a listener called when an accepted payload enters the drop zone.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `Consumer` | Callback for drag enter event. |

Returns `DropController`: This DropController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`onDragLeave(Consumer)` {: #ondragleaveconsumer data-toc-label="onDragLeave(Consumer)" }

Registers a listener called when the drag pointer leaves the drop zone or is cancelled.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `Consumer` | Callback for drag leave event. |

Returns `DropController`: This DropController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`onDragOver(Consumer)` {: #ondragoverconsumer data-toc-label="onDragOver(Consumer)" }

Registers a listener called on each movement and frame tick over the drop zone.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `Consumer` | Callback for drag over event. |

Returns `DropController`: This DropController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`onDrop(Consumer)` {: #ondropconsumer data-toc-label="onDrop(Consumer)" }

Registers a listener called when an accepted payload is dropped over the zone.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `Consumer` | Callback for drop event. |

Returns `DropController`: This DropController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`zone()` {: #zone data-toc-label="zone()" }

Returns the host drop zone widget.

Returns `Widget`: The widget acting as the drop target zone..

</div>

