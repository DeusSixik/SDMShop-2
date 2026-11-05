---
title: "EventScope"
slug: "event-scope"
category: "Core/Events"
locale: "en"
order: 30
---

# EventScope

Scoped lifecycle manager for event subscriptions.

## Overview

`EventScope` is an `AutoCloseable` container that tracks event subscriptions created during a component's lifecycle. In interactive UIs, forgetting to unregister event listeners when navigating away from a screen or destroying a dialog is one of the most common causes of memory leaks and ghost updates.

By binding listener subscriptions to an `EventScope`, all registered listeners can be cleaned up in a single call.

## Lifecycle Management

- **`listen(event, listener)`**: Subscribes to the event and tracks the returned subscription.
- **`clear()`**: Unsubscribes all currently tracked listeners in reverse order, while leaving the scope open for future registrations.
- **`close()`**: Marks the scope as permanently closed and unsubscribes all listeners. Subsequent calls to `listen()` return `EventSubscription.NOOP`.
- **`EventScope.noop()`**: Factory creating a permanently disabled scope that ignores registrations, useful for optional subscription patterns.

## Screen lifecycle integration

```java
public class InventoryScreen extends UniScreen {
    private final EventScope scope = new EventScope();

    @Override
    public void onOpen() {
        scope.listen(ITEM_EQUIPPED, item -> updateSlot(item));
        scope.listen(CURRENCY_CHANGED, balance -> updateBalance(balance));
    }

    @Override
    public void onClose() {
        scope.close(); // all listeners unregister automatically
    }
}
```

## Related links

- [EventEmitter](<event-emitter.md>)
- [FastEventBus](<fast-event-bus.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">EventScope::</span>`noop()` {: #noop data-toc-label="noop()" }

**No-op scope**

Returns a disabled scope that ignores all registrations.

Returns `EventScope`: disabled EventScope instance.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">EventScope::</span>`listen(EventPtr, EventListener)` {: #listeneventptr-eventlistener data-toc-label="listen(EventPtr, EventListener)" }

**Tracked listen**

Subscribes a listener to the event and tracks it for cleanup.

| Parameter | Type | Description |
| --- | --- | --- |
| `event` | `EventPtr` | Event pointer handle |
| `listener` | `EventListener` | Listener callback |

Returns `EventSubscription`: EventSubscription handle.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">EventScope::</span>`clear()` {: #clear data-toc-label="clear()" }

**Clear subscriptions**

Unsubscribes all currently tracked listeners without closing the scope.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">EventScope::</span>`close()` {: #close data-toc-label="close()" }

**Close scope**

Closes the scope and unregisters all tracked listeners.

</div>

