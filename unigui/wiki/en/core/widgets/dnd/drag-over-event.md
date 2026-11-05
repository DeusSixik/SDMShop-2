---
title: "DragOverEvent"
slug: "drag-over-event"
category: "Core/Widgets/DnD"
locale: "en"
order: 0
---

# DragOverEvent

Routed event probing candidate drop zones beneath the pointer during drag movement and animation ticks.

## Overview

`DragOverEvent` is dispatched continuously as the pointer hovers over candidate drop widgets.

### Target Validation

Drop zones inspect `payload()` and confirm acceptance by calling `event.accept(this)`:

```java
widget.addListener(DragOverEvent.class, event -> {
    if (event.payload().matchesType("inventory/*")) {
        event.accept(widget);
    }
});
```

When accepted, the widget becomes `session.dropTarget()`, enabling visual hover styling and indicator lines.

## Related links

- [DragSession](<drag-session.md>): Live state and controller of an active drag gesture, managing pointer coordinates, active drop target, ghost positioning, insertion indicators, and autoscrolling.
- [DropController](<drop-controller.md>): Drop target controller attachable to any widget via composition to filter payloads and handle drag enter, over, leave, and drop events.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragOverEvent::</span>`accept(Widget)` {: #acceptwidget data-toc-label="accept(Widget)" }

Designates the specified widget as the active drop target and stops further event bubbling.

| Parameter | Type | Description |
| --- | --- | --- |
| `zone` | `Widget` | [Widget](<../../../widgets/widget.md>) claiming drop target acceptance. |

</div>

