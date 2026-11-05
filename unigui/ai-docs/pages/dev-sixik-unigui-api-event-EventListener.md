# `EventListener`

Functional listener interface for handling a single typed Event.

- **Purpose:** Passed to `EventEmitter.on()` to receive events. Being a @FunctionalInterface, it can be expressed as a lambda or method reference.

### CRITICAL RULES & GOTCHAS:

- Listeners run synchronously on the thread that calls emit().
- Throwing from handle() does not prevent other listeners from running — FastEventBus catches and logs exceptions.

- **Related:** `EventEmitter`, `Event`, `EventSubscription`

