# `Event`

Base contract for all UI events: carries an EventType and supports cancellation.

- **Purpose:** Implemented by every event in the system. Listeners check `isCancelled()` to short-circuit processing; calling `cancel()` signals upstream handlers to stop dispatch.

### USE WHEN

- Define a new event type by implementing Event (or extending BaseEvent).
- Check or cancel an event inside a listener.

### CRITICAL RULES & GOTCHAS:

- Every Event must return a non-null EventType from type().
- Once cancelled, isCancelled() returns true for all subsequent listeners unless they opt in via receiveCancelled.
- Cancellation is one-way: there is no uncancel().

- **Related:** `BaseEvent`, `EventType`, `EventEmitter`, `EventListener`

