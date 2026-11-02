# `RoutableWidgetEvent` (extends `WidgetEvent`)

WidgetEvent that can create phase/current-target snapshots for routed dispatch.

- **Purpose:** RoutedEventDispatcher calls `routeTo()` to produce a shallow copy of the event with updated `currentTarget` and `phase` as it walks the widget tree.

### CRITICAL RULES & GOTCHAS:

- `routeTo()` must preserve cancellation state from the original event.
- The returned event shares the same `target()` but has a new `currentTarget` and `phase`.

- **Related:** `WidgetEvent`, `RoutedEventDispatcher`, `PointerEvent`, `EventPhase`

