# `EventPhase` (extends `Enum`)

Phase of a routed widget event: CAPTURE (root→target), TARGET, BUBBLE (target→root).

- **Purpose:** Used by WidgetEvent.phase() and RoutedEventDispatcher to distinguish the direction of event propagation through the widget tree.

### CRITICAL RULES & GOTCHAS:

- CAPTURE fires from root toward target; BUBBLE fires from target back to root.
- Most listeners subscribe at BUBBLE or TARGET phase.
- Cancelling an event during CAPTURE prevents it from reaching TARGET and BUBBLE.

- **Related:** `WidgetEvent`, `RoutableWidgetEvent`, `RoutedEventDispatcher`

