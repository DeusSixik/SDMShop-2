# `EventType`

Strongly-typed event type marker used to register listeners and dispatch events.

- **Purpose:** Each concrete event class declares a `public static final EventType<T> TYPE` field. EventEmitter.on() uses it to match listeners to dispatched events with zero-map lookup.

### USE WHEN

- Declare a new event: `public static final EventType<MyEvent> TYPE = EventType.create("my.event");`.
- Subscribe: `widget.on(MyEvent.TYPE, listener)`.

### CRITICAL RULES & GOTCHAS:

- Create instances only via `EventType.create(id)` — the constructor is private.
- The `id` string must be non-null and is used for debugging and logging.
- Each EventType wraps a FastEventBus.EventType for zero-map dispatch.

- **Related:** `Event`, `EventEmitter`, `FastEventBus`

### EXAMPLES

#### Declare a custom event type

```java
public static final EventType<MyEvent> TYPE = EventType.create("my.event");
```

