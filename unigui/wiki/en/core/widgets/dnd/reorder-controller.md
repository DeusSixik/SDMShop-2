---
title: "ReorderController"
slug: "reorder-controller"
category: "Core/Widgets/DnD"
locale: "en"
order: 0
---

# ReorderController

Drag reordering controller attachable to container widgets (e.g. [VBox](<../../../widgets/containers/vbox.md>), [HBox](<../../../widgets/containers/hbox.md>), [PanelWidget](<../../../widgets/containers/panel-widget.md>)) supporting insertion lines and automated DOM reordering.

## Overview

`ReorderController` implements drag-to-reorder behavior for linear containers such as `VBox`, `HBox`, or `PanelWidget`.

### Features

- **Insertion Line Indicator**: Displays an animated insertion bar indicating where items will land.
- **Auto Apply**: Automatically mutates the container child list on drop when `autoApply(true)` is enabled.
- **Internal vs External**: Support reordering items from the same container only, or accepting foreign items matching payload filters.

```java
ReorderController.attach(vbox)
        .orientation(Orientation.VERTICAL)
        .accepts("task/*")
        .autoApply(true)
        .indicator(3.0f, 4.0f, ColorView.ofHex("#007ACC"))
        .onReorder((from, to) -> syncDatabaseOrder(from, to));
```

## Reordering Children within a Container

```java
ReorderController.attach(vbox)
        .orientation(Orientation.VERTICAL)
        .accepts("card")
        .onReorder((from, to) -> dataList.add(to, dataList.remove((int) from)));
```

## Related links

- [DragController](<drag-controller.md>): Drag gesture controller attachable to any widget via composition to initiate drag-and-drop operations with ghost previews, threshold handling, and autoscrolling.
- [DragSession](<drag-session.md>): Live state and controller of an active drag gesture, managing pointer coordinates, active drop target, ghost positioning, insertion indicators, and autoscrolling.
- [DropController](<drop-controller.md>): Drop target controller attachable to any widget via composition to filter payloads and handle drag enter, over, leave, and drop events.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`accepts(Predicate)` {: #acceptspredicate data-toc-label="accepts(Predicate)" }

Adds custom predicate validation rule for reorder payloads.

| Parameter | Type | Description |
| --- | --- | --- |
| `validator` | `Predicate` | Custom payload validator. |

Returns `ReorderController`: This ReorderController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`accepts(String)` {: #acceptsstring data-toc-label="accepts(String)" }

Restricts accepted payload types by comma/pipe/whitespace delimited string (* accepts all).

| Parameter | Type | Description |
| --- | --- | --- |
| `types` | `String` | Accepted payload types for reordering. |

Returns `ReorderController`: This ReorderController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`attach(Widget)` {: #attachwidget data-toc-label="attach(Widget)" }

Attaches a ReorderController to the specified container widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `container` | `Widget` | Container widget whose children will be reordered. |

Returns `ReorderController`: A newly attached ReorderController instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`autoApply(boolean)` {: #autoapplyboolean data-toc-label="autoApply(boolean)" }

Enables or disables automatic repositioning of children when container is a [PanelWidget](<../../../widgets/containers/panel-widget.md>).

| Parameter | Type | Description |
| --- | --- | --- |
| `autoApply` | `boolean` | Whether child list mutations are applied automatically. |

Returns `ReorderController`: This ReorderController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`container()` {: #container data-toc-label="container()" }

Returns the container widget managed by this controller.

Returns `Widget`: The container widget being reordered..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`detach()` {: #detach data-toc-label="detach()" }

Detaches event listeners from the container.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`enabled(boolean)` {: #enabledboolean data-toc-label="enabled(boolean)" }

Enables or disables this reorder controller.

| Parameter | Type | Description |
| --- | --- | --- |
| `enabled` | `boolean` | Whether the reorder controller is enabled. |

Returns `ReorderController`: This ReorderController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`indicator(ColorView, float, float)` {: #indicatorcolorview-float-float data-toc-label="indicator(ColorView, float, float)" }

Customizes appearance of the insertion line indicator.

| Parameter | Type | Description |
| --- | --- | --- |
| `color` | `ColorView` | Color of the reorder insertion line. |
| `thickness` | `float` | Thickness of the reorder insertion line. |
| `inset` | `float` | Inset padding for the insertion line. |

Returns `ReorderController`: This ReorderController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`internalOnly(boolean)` {: #internalonlyboolean data-toc-label="internalOnly(boolean)" }

Restricts reordering to internal children of this container only.

| Parameter | Type | Description |
| --- | --- | --- |
| `internalOnly` | `boolean` | Whether reordering only accepts items from this container. |

Returns `ReorderController`: This ReorderController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`itemFilter(Predicate)` {: #itemfilterpredicate data-toc-label="itemFilter(Predicate)" }

Configures a predicate filter selecting which child widgets participate in reordering.

| Parameter | Type | Description |
| --- | --- | --- |
| `itemFilter` | `Predicate` | Predicate determining if a child widget is draggable. |

Returns `ReorderController`: This ReorderController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`onReorder(BiConsumer)` {: #onreorderbiconsumer data-toc-label="onReorder(BiConsumer)" }

Registers a listener receiving indices: fromIndex and toIndex.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `BiConsumer` | Callback receiving fromIndex and toIndex. |

Returns `ReorderController`: This ReorderController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`onReorder(Consumer)` {: #onreorderconsumer data-toc-label="onReorder(Consumer)" }

Registers a listener receiving full ReorderEvent details.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `Consumer` | Callback invoked when items are reordered. |

Returns `ReorderController`: This ReorderController for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`orientation(Orientation)` {: #orientationorientation data-toc-label="orientation(Orientation)" }

Configures orientation (VERTICAL or HORIZONTAL) for reordering calculations.

| Parameter | Type | Description |
| --- | --- | --- |
| `orientation` | `Orientation` | Layout orientation of the container children. |

Returns `ReorderController`: This ReorderController for method chaining..

</div>

