---
title: "DragRoutedEvent"
slug: "drag-routed-event"
category: "Core/Widgets/DnD"
locale: "en"
order: 0
---

# DragRoutedEvent

Abstract base class for routed drag-and-drop events conveying the active [DragSession](<drag-session.md>) across widget hierarchies.

## Overview

`DragRoutedEvent` is the abstract base class for routed drag-and-drop notifications dispatched throughout the widget hierarchy.

### Routed Event Flow

| Phase | Description |
| --- | --- |
| `CAPTURING` | Dispatched from the root container downward toward the target widget under cursor. |
| `AT_TARGET` | Delivered directly to the candidate target widget beneath the pointer. |
| `BUBBLING` | Bubbles upwards from target back to the root widget. |

All drag events provide access to the active `DragSession`, transfer `DragPayload`, and pointer coordinates in root UI pixels.

## Related links

- [DragSession](<drag-session.md>): Live state and controller of an active drag gesture, managing pointer coordinates, active drop target, ghost positioning, insertion indicators, and autoscrolling.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragRoutedEvent::</span>`currentTarget()` {: #currenttarget data-toc-label="currentTarget()" }

Returns the current widget handling the event during dispatch.

Returns `Widget`: The current widget handling this routed event..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragRoutedEvent::</span>`payload()` {: #payload data-toc-label="payload()" }

Convenience shortcut returning the payload from the active drag session.

Returns `DragPayload`: The transfer payload of the drag session..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragRoutedEvent::</span>`phase()` {: #phase data-toc-label="phase()" }

Returns the current routing phase (capture, target, bubble).

Returns `EventPhase`: The dispatch phase (CAPTURING, AT_TARGET, BUBBLING)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragRoutedEvent::</span>`rootX()` {: #rootx data-toc-label="rootX()" }

Returns the pointer X coordinate in root UI space.

Returns `float`: The root pointer coordinate X..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragRoutedEvent::</span>`rootY()` {: #rooty data-toc-label="rootY()" }

Returns the pointer Y coordinate in root UI space.

Returns `float`: The root pointer coordinate Y..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragRoutedEvent::</span>`session()` {: #session data-toc-label="session()" }

Returns the active [DragSession](<drag-session.md>) associated with this event.

Returns `DragSession`: The active [DragSession](<drag-session.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragRoutedEvent::</span>`target()` {: #target data-toc-label="target()" }

Returns the original target widget where the event was initiated.

Returns `Widget`: The original target widget of the event..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragRoutedEvent::</span>`type()` {: #type data-toc-label="type()" }

Returns the event type token.

Returns `EventType`: The specific event type identifier..

</div>

