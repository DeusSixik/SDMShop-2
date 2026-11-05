# `RoutedEventDispatcher`

Dispatches routed widget events across the UI tree or directly to targets.

- **Purpose:** Coordinates tree traversal during routed event dispatch (capture down to target, and bubble back up to root) or delivers directly via the DIRECT constant.

### USE WHEN

- Dispatch input/pointer/focus events across the hierarchy using routed phases.
- Use RoutedEventDispatcher.DIRECT for simple point-to-point dispatch without bubbling.

### CRITICAL RULES & GOTCHAS:

- Returns true if the event was cancelled during dispatch, false otherwise.
- Null events or events with null targets are ignored safely.

- **Related:** `WidgetEvent`, `RoutableWidgetEvent`, `EventPhase`, `Widget`

