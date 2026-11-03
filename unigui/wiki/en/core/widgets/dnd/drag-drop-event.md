---
title: "DragDropEvent"
slug: "drag-drop-event"
category: "Core/Widgets/DnD"
locale: "en"
order: 0
---

# DragDropEvent

Routed event delivered to the target drop zone when the pointer is released with an accepted payload.

## Overview

`DragDropEvent` is delivered to the accepted drop target when the user releases the pointer.

### Handling Drops

```java
widget.addListener(DragDropEvent.class, event -> {
    MyData item = event.payload().data(MyData.class);
    insertItem(item);
});
```

Delivering `DragDropEvent` successfully marks the drag session outcome as `DragOutcome.DROPPED`.

## Related links

- [DragSession](<drag-session.md>): Live state and controller of an active drag gesture, managing pointer coordinates, active drop target, ghost positioning, insertion indicators, and autoscrolling.
- [DropController](<drop-controller.md>): Drop target controller attachable to any widget via composition to filter payloads and handle drag enter, over, leave, and drop events.

