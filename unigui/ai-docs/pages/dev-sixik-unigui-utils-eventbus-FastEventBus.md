# `FastEventBus`

Ultra-fast, lock-free, zero-allocation event bus without reflection or hash-maps.

- **Purpose:** Provides high-performance event dispatching for the entire UI runtime. Events are indexed by sequential integer IDs and listeners are stored in contiguous arrays for lock-free traversal.

### USE WHEN

- Dispatch high-frequency events where reflection or Map lookup would cause GC pressure or latency.
- Serve as the backend for UI event emitters and global system buses.

### CRITICAL RULES & GOTCHAS:

- Dispatches lock-free using volatile array reads: no synchronization or locking during fire().
- Subscribing and unsubscribing use Copy-On-Write and are synchronized.
- Listener exceptions are caught and logged so one failing listener does not break others.
- EventType IDs are assigned sequentially starting from 0; capacity expands automatically.

- **Related:** `EventScope`, `EventPtr`, `EventPtrInvoker`, `EventSubscription`, `dev.sixik.unigui.api.event.EventEmitter`

### SUBSCRIPTION

- `subscribe(EventType<T>, EventListener<T>)` → EventSubscription — Registers a listener for an event type using copy-on-write semantics.
- `unsubscribe(EventType<T>, EventListener<T>)` → void — Removes a listener using copy-on-write array shrinking.

### DISPATCH

- `fire(EventType<T>, T)` → void — Dispatches an event lock-free with zero garbage allocation.

### EXAMPLES

#### Subscribe and fire event

```java
FastEventBus.EventType<String> MESSAGE_EVENT = new FastEventBus.EventType<>("message");
FastEventBus bus = FastEventBus.DEFAULT_BUS;

EventSubscription sub = bus.subscribe(MESSAGE_EVENT, msg -> {
    System.out.println("Received: " + msg);
});

bus.fire(MESSAGE_EVENT, "Hello World!");
sub.unsubscribe();
```

