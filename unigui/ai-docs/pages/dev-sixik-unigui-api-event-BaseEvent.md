# `BaseEvent` (implements `Event`)

Default abstract Event implementation with a boolean cancelled flag.

- **Purpose:** Provides the standard `cancel()` / `isCancelled()` implementation so concrete event classes only need to add their payload fields and `type()`.

### USE WHEN

- Extend to create a concrete event: override `type()` and add payload fields.
- Use instead of implementing Event directly to get cancellation for free.

### CRITICAL RULES & GOTCHAS:

- Subclasses must override `type()` to return their static EventType constant.
- Cancellation is one-way and cannot be reversed.

- **Related:** `Event`, `EventType`, `WidgetEvent`, `PointerEvent`

