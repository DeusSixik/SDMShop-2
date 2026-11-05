# `EventEmitter`

Mixin interface for any object that can subscribe listeners to typed events and dispatch them.

- **Purpose:** Provides the subscribe/emit contract used by Widget and other observable components. Listeners receive strongly-typed events and can be unsubscribed via the returned EventSubscription.

### USE WHEN

- Subscribe to widget events: `widget.on(ButtonClickEvent.TYPE, e -> ...)`.
- Dispatch a custom event to registered listeners with `emit(event)`.
- Receive cancelled events by passing `receiveCancelled = true` in the three-arg overload.

### CRITICAL RULES & GOTCHAS:

- `on()` returns an EventSubscription; call `unsubscribe()` or `close()` to stop receiving events.
- `emit()` delivers the event to all matching listeners synchronously on the calling thread.
- The two-arg `on(type, listener)` skips cancelled events by default; use the three-arg overload to receive them.
- Implementations must be safe against listener removal during dispatch.

- **Related:** `Event`, `EventType`, `EventListener`, `EventSubscription`, `Widget`

### SUBSCRIPTION

- `on(EventType<T>, EventListener<? super T>)` → EventSubscription — Subscribes a listener to the specified event type.
- `on(EventType<T>, boolean, EventListener<? super T>)` → EventSubscription — Subscribes a listener with an option to receive cancelled events.

### DISPATCH

- `emit(Event)` → void — Emits an event to all registered listeners.

### EXAMPLES

#### Subscribe to a button click

```java
widget.on(ButtonClickEvent.TYPE, e -> {
    System.out.println("Clicked!");
});
```

#### Emit a custom event

```java
emitter.emit(new MyCustomEvent(target));
```

