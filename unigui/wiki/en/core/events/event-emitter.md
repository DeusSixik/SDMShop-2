---
title: "EventEmitter"
slug: "event-emitter"
category: "Core/Events"
locale: "en"
order: 10
---

# EventEmitter

Contract for objects that can subscribe to and dispatch typed events.

## Overview

`EventEmitter` is the foundational event subscription and dispatch contract implemented by `Widget` and other observable UniGUI components. It provides a strongly typed, zero-reflection interface for registering event listeners and emitting events synchronously.

Every event is keyed by an `EventType<T>`, eliminating string keys or reflection overhead.

## Subscription Lifecycle

Calling `on(EventType, listener)` registers a listener and returns an `EventSubscription` handle:

- **Unregistering**: Call `subscription.unsubscribe()` or `subscription.close()` to stop receiving events.
- **Scoped Cleanup**: Because `EventSubscription` implements `AutoCloseable`, it works seamlessly with `try-with-resources` or `EventScope` for automated unregistration during screen or widget teardown.
- **Thread Safety**: Implementations ensure that listener registration and unregistration during active dispatch passes do not throw concurrent modification errors.

## Event Cancellation

Events in UniGUI implement cooperative cancellation via `Event.cancel()` and `Event.isCancelled()`.

- **Standard Listener**: `on(type, listener)` automatically ignores events that have already been cancelled.
- **Audit / Monitor Listener**: Use `on(type, true, listener)` with `receiveCancelled = true` to receive events even after upstream handlers have called `cancel()`. Useful for diagnostics, telemetry, or catch-all handlers.

## Listening for events

```java
EventSubscription sub = widget.on(ButtonClickEvent.TYPE, e -> {
    System.out.println("Button clicked!");
});

// Later, unregister:
sub.unsubscribe();
```

## Dispatching events

```java
// Synchronously dispatches event to all matching listeners
widget.emit(new ButtonClickEvent(widget));
```

## Related links

- [FastEventBus](<fast-event-bus.md>)
- [Widget](<../../widgets/widget.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">EventEmitter::</span>`on(EventType, EventListener)` {: #oneventtype-eventlistener data-toc-label="on(EventType, EventListener)" }

**Subscribe to event**

Registers a listener for the specified event type.

| Parameter | Type | Description |
| --- | --- | --- |
| `type` | `EventType` | Event type token |
| `listener` | `EventListener` | Callback receiving event instances |

Returns `EventSubscription`: EventSubscription handle to unregister.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">EventEmitter::</span>`on(EventType, boolean, EventListener)` {: #oneventtype-boolean-eventlistener data-toc-label="on(EventType, boolean, EventListener)" }

**Subscribe including cancelled events**

Registers a listener with control over receiving cancelled events.

| Parameter | Type | Description |
| --- | --- | --- |
| `type` | `EventType` | Event type token |
| `receiveCancelled` | `boolean` | True to receive cancelled events |
| `listener` | `EventListener` | Callback receiving event instances |

Returns `EventSubscription`: EventSubscription handle to unregister.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">EventEmitter::</span>`emit(Event)` {: #emitevent data-toc-label="emit(Event)" }

**Emit event**

Dispatches an event synchronously to all registered listeners.

| Parameter | Type | Description |
| --- | --- | --- |
| `event` | `Event` | Event instance to dispatch |

</div>

