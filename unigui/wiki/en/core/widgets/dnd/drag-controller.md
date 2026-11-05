---
title: "DragController"
slug: "drag-controller"
category: "Core/Widgets/DnD"
locale: "en"
order: 0
---

# DragController

Drag gesture controller attachable to any widget via composition to initiate drag-and-drop operations with ghost previews, threshold handling, and autoscrolling.

## Overview

`DragController` attaches drag initiator capabilities to any widget using composition.

### Usage Pattern

```java
DragController.attach(headerWidget)
        .dragged(entireCardWidget)
        .payload(() -> DragPayload.of("card-1", "cards/item", "Card 1"))
        .threshold(4.0f)
        .ghostWidget(entireCardWidget, 0.75f)
        .onDragEnd(end -> System.out.println("Result: " + end.outcome()));
```

### Configuration Options

| Option | Method | Purpose |
| --- | --- | --- |
| Drag Handle | `attach(widget)` | The specific widget listening for pointer press events. |
| Dragged [Widget](<../../../widgets/widget.md>) | `dragged(widget)` | The logical component being moved (e.g. card container). |
| Lazy Payload | `payload(Supplier)` | Computes the payload only when the threshold is exceeded. |
| Threshold | `threshold(px)` | Deadzone in pixels before pointer movement triggers a drag. |
| Axis Lock | `lockGhostAxis(Orientation)` | Constrains ghost motion strictly horizontally or vertically. |
| Autoscroll | `autoScroll(edge, speed)` | Scrolls enclosing containers when dragging near boundaries. |

## Attaching Drag Controller to a [Widget](<../../../widgets/widget.md>)

```java
DragController.attach(header)
        .dragged(card)
        .payload(() -> DragPayload.of("card-1", "card", "Card #1", data))
        .threshold(4.0f)
        .onDragEnd(end -> System.out.println("Outcome: " + end.outcome()));
```

## Related links

- [DragSession](<drag-session.md>): Live state and controller of an active drag gesture, managing pointer coordinates, active drop target, ghost positioning, insertion indicators, and autoscrolling.
- [DropController](<drop-controller.md>): Drop target controller attachable to any widget via composition to filter payloads and handle drag enter, over, leave, and drop events.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`attach(Widget)` {: #attachwidget data-toc-label="attach(Widget)" }

Attaches a new DragController to the specified handle widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `handle` | `Widget` | [Widget](<../../../widgets/widget.md>) acting as the drag handle initiating the gesture. |

Returns `DragController`: A newly attached DragController instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`autoScroll(boolean)` {: #autoscrollboolean data-toc-label="autoScroll(boolean)" }

Enables or disables automatic scrolling near edges of parent ScrollViews.

| Parameter | Type | Description |
| --- | --- | --- |
| `autoScroll` | `boolean` | Whether auto-scrolling is enabled. |

Returns `DragController`: This DragController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`autoScroll(float, float)` {: #autoscrollfloat-float data-toc-label="autoScroll(float, float)" }

Configures autoscroll edge margin and maximum scroll speed in pixels per second.

| Parameter | Type | Description |
| --- | --- | --- |
| `edge` | `float` | Edge margin threshold in pixels for triggering auto-scroll. |
| `pixelsPerSecond` | `float` | Auto-scroll speed in pixels per second. |

Returns `DragController`: This DragController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`cancel()` {: #cancel data-toc-label="cancel()" }

Cancels the active gesture if one is in progress.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`detach()` {: #detach data-toc-label="detach()" }

Detaches event listeners from the handle widget and terminates active gestures.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`dragged(Widget)` {: #draggedwidget data-toc-label="dragged(Widget)" }

Specifies the logical widget being dragged (e.g. the entire card when dragged by its header).

| Parameter | Type | Description |
| --- | --- | --- |
| `dragged` | `Widget` | Logical widget being dragged. |

Returns `DragController`: This DragController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`dragging()` {: #dragging data-toc-label="dragging()" }

Returns true if currently in an active dragging state past the threshold.

Returns `boolean`: True if a drag gesture is currently active..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`enabled(boolean)` {: #enabledboolean data-toc-label="enabled(boolean)" }

Enables or disables this drag controller.

| Parameter | Type | Description |
| --- | --- | --- |
| `enabled` | `boolean` | Whether the drag controller is enabled. |

Returns `DragController`: This DragController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`filter(Predicate)` {: #filterpredicate data-toc-label="filter(Predicate)" }

Sets a predicate filter to selectively reject pointer pressed events.

| Parameter | Type | Description |
| --- | --- | --- |
| `filter` | `Predicate` | Predicate filter for pointer pressed events. |

Returns `DragController`: This DragController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`ghost(DragGhost)` {: #ghostdragghost data-toc-label="ghost(DragGhost)" }

Configures a custom drag ghost renderer.

| Parameter | Type | Description |
| --- | --- | --- |
| `ghost` | `DragGhost` | Drag ghost renderer. |

Returns `DragController`: This DragController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`ghost(Widget)` {: #ghostwidget data-toc-label="ghost(Widget)" }

Configures a ghost that duplicates the visual appearance of the specified widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `widget` | `Widget` | [Widget](<../../../widgets/widget.md>) whose visual appearance is cloned for ghost rendering. |

Returns `DragController`: This DragController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`ghostOpacity(float)` {: #ghostopacityfloat data-toc-label="ghostOpacity(float)" }

Sets the opacity level for the widget ghost preview.

| Parameter | Type | Description |
| --- | --- | --- |
| `opacity` | `float` | Opacity of the drag ghost preview. |

Returns `DragController`: This DragController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`handle()` {: #handle data-toc-label="handle()" }

Returns the handle widget.

Returns `Widget`: The widget configured as the drag handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`lockGhostAxis(Orientation)` {: #lockghostaxisorientation data-toc-label="lockGhostAxis(Orientation)" }

Locks ghost movement to a single axis (e.g. VERTICAL for lists) or null for free movement.

| Parameter | Type | Description |
| --- | --- | --- |
| `axis` | `Orientation` | Orientation axis constraint for ghost movement. |

Returns `DragController`: This DragController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`onDragEnd(Consumer)` {: #ondragendconsumer data-toc-label="onDragEnd(Consumer)" }

Registers a listener invoked when a drag gesture completes or is cancelled.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `Consumer` | Callback invoked when drag finishes. |

Returns `DragController`: This DragController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`onDragMove(Consumer)` {: #ondragmoveconsumer data-toc-label="onDragMove(Consumer)" }

Registers a listener invoked on each pointer movement during drag.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `Consumer` | Callback invoked on pointer movement during drag. |

Returns `DragController`: This DragController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`onDragStart(Consumer)` {: #ondragstartconsumer data-toc-label="onDragStart(Consumer)" }

Registers a listener invoked when a drag gesture starts.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `Consumer` | Callback invoked when drag session begins. |

Returns `DragController`: This DragController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`payload(DragPayload)` {: #payloaddragpayload data-toc-label="payload(DragPayload)" }

Configures a static constant payload for the drag gesture.

| Parameter | Type | Description |
| --- | --- | --- |
| `payload` | `DragPayload` | Static payload to transfer. |

Returns `DragController`: This DragController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`payload(Supplier)` {: #payloadsupplier data-toc-label="payload(Supplier)" }

Configures a lazy payload supplier invoked when the drag threshold is exceeded.

| Parameter | Type | Description |
| --- | --- | --- |
| `payload` | `Supplier` | Lazy supplier computing payload when drag begins. |

Returns `DragController`: This DragController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`session()` {: #session data-toc-label="session()" }

Returns the active [DragSession](<drag-session.md>), or null if idle.

Returns `DragSession`: The active [DragSession](<drag-session.md>) or null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`sourceOpacity(float)` {: #sourceopacityfloat data-toc-label="sourceOpacity(float)" }

Sets the opacity of the source widget while dragging (1.0 disables dimming).

| Parameter | Type | Description |
| --- | --- | --- |
| `opacity` | `float` | Opacity of the drag ghost preview. |

Returns `DragController`: This DragController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`threshold(float)` {: #thresholdfloat data-toc-label="threshold(float)" }

Sets the minimum movement threshold in pixels before the drag session starts.

| Parameter | Type | Description |
| --- | --- | --- |
| `threshold` | `float` | Movement threshold in pixels before drag starts. |

Returns `DragController`: This DragController for method chaining..

</div>

