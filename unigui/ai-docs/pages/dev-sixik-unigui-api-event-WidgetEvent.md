# `WidgetEvent` (extends `Event`)

Event bound to a Widget: carries target, currentTarget, and the dispatch phase.

- **Purpose:** Extended by all events that travel through the widget tree (pointer, focus, keyboard). The three accessors let listeners distinguish the originator from the current handler.

### CRITICAL RULES & GOTCHAS:

- `target()` is the widget where the event originated; `currentTarget()` changes during routing.
- `phase()` indicates CAPTURE, TARGET, or BUBBLE stage of dispatch.

- **Related:** `Event`, `Widget`, `EventPhase`, `RoutableWidgetEvent`, `PointerEvent`

