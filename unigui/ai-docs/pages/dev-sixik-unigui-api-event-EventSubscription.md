# `EventSubscription` (extends `AutoCloseable`)

Handle returned by EventEmitter.on() to unsubscribe a listener.

- **Purpose:** Allows the caller to remove a specific listener registration. Implements AutoCloseable so subscriptions can be managed with try-with-resources or EventScope.

### USE WHEN

- Store the returned subscription and call `unsubscribe()` when the listener is no longer needed.
- Use in try-with-resources: `try (var sub = emitter.on(...)) { ... }`.

### CRITICAL RULES & GOTCHAS:

- Calling `unsubscribe()` or `close()` more than once is safe and idempotent.
- After unsubscribe, the listener will not receive any further events.

- **Related:** `EventEmitter`, `EventListener`, `EventScope`

