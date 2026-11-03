---
title: "FastEventBus"
slug: "fast-event-bus"
category: "Core/Events"
locale: "en"
order: 20
---

# FastEventBus

Ultra-fast, lock-free, zero-allocation event bus without reflection or hash-maps.

## Overview

`FastEventBus` is the high-performance event bus engine powering UniGUI. Designed specifically for performance-critical scenarios such as game rendering loops and input dispatching, it eliminates runtime reflection, hash map lookups, and allocation garbage during dispatch.

## Zero-Garbage Architecture

Traditional event buses use class-to-method reflection maps or string identifiers, which generate iterator allocations, boxing overhead, and hash lookups on every dispatch.

`FastEventBus` solves this through dense integer indexing:

- **`EventType<T>`**: Each event type receives an automatically assigned sequential integer index (`id = 0, 1, 2...`).
- **AtomicReferenceArray**: Listeners are stored in contiguous typed arrays indexed directly by event ID.
- **Direct Iteration**: `fire()` directly indexes into the array and loops through listeners with zero intermediate objects.

## Concurrency & Thread Safety

The bus uses lock-free reads combined with copy-on-write writes:

- **`fire()`**: Entirely lock-free. Reads the current listener array atomically and iterates over it.
- **`subscribe()` / `unsubscribe()`**: Synchronized and use Copy-On-Write array duplication, keeping reader threads completely unblocked.
- **Exception Isolation**: Exceptions thrown inside a listener callback are caught and logged via SLF4J, ensuring one broken handler never halts dispatch to other listeners.

## Subscribe and fire

```java
FastEventBus.EventType<String> CHAT_EVENT = new FastEventBus.EventType<>("chat");
FastEventBus bus = FastEventBus.DEFAULT_BUS;

EventSubscription sub = bus.subscribe(CHAT_EVENT, msg -> {
    System.out.println("Received: " + msg);
});

bus.fire(CHAT_EVENT, "Hello World!");
sub.unsubscribe();
```

## Custom record event

```java
public record PlayerScore(String player, int score) {}
public static final FastEventBus.EventType<PlayerScore> SCORE = new FastEventBus.EventType<>("score");

FastEventBus.DEFAULT_BUS.subscribe(SCORE, e -> {
    System.out.printf("%s scored %d points%n", e.player(), e.score());
});

FastEventBus.DEFAULT_BUS.fire(SCORE, new PlayerScore("Alex", 100));
```

## Related links

- [EventEmitter](<event-emitter.md>)
- [EventScope](<event-scope.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">FastEventBus::</span>`subscribe(EventType, EventListener)` {: #subscribeeventtype-eventlistener data-toc-label="subscribe(EventType, EventListener)" }

**Subscribe listener**

Registers a listener for an event type using copy-on-write.

| Parameter | Type | Description |
| --- | --- | --- |
| `type` | `EventType` | Unique event type token |
| `listener` | `EventListener` | Listener callback |

Returns `EventSubscription`: EventSubscription handle to unregister.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">FastEventBus::</span>`unsubscribe(EventType, EventListener)` {: #unsubscribeeventtype-eventlistener data-toc-label="unsubscribe(EventType, EventListener)" }

**Unsubscribe listener**

Removes a listener using copy-on-write array shrinking.

| Parameter | Type | Description |
| --- | --- | --- |
| `type` | `EventType` | Event type token |
| `listener` | `EventListener` | Listener to remove |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">FastEventBus::</span>`fire(EventType, Object)` {: #fireeventtype-object data-toc-label="fire(EventType, Object)" }

**Fire event**

Dispatches an event lock-free with zero garbage allocation.

| Parameter | Type | Description |
| --- | --- | --- |
| `type` | `EventType` | Event type token |
| `event` | `Object` | Event payload data |

</div>

