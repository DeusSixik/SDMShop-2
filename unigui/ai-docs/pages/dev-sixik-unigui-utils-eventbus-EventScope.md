# `EventScope` (implements `AutoCloseable`)

Scoped collector for EventSubscriptions that unregisters all listeners when closed or cleared.

- **Purpose:** Prevents listener leaks in screens, controllers, and short-lived UI components. Listeners registered via listen() are tracked and unsubscribed in reverse order when close() or clear() is called.

### USE WHEN

- Manage lifecycle of subscriptions in a screen, dialog, or controller.
- Unsubscribe multiple event listeners in bulk during widget teardown.
- Use EventScope.noop() to pass a disabled scope when subscriptions are optional.

### CRITICAL RULES & GOTCHAS:

- Calling close() marks the scope closed and unsubscribes all registered listeners.
- Registering listeners on a closed or noop scope returns EventSubscription.NOOP.
- All mutations (listen, clear, close) are synchronized and thread-safe.

- **Related:** `FastEventBus`, `EventPtr`, `EventSubscription`, `dev.sixik.unigui.api.event.EventEmitter`

### FACTORY

- `noop()` → EventScope — Returns a no-op scope that ignores all registrations.

### SUBSCRIPTION

- `listen(EventPtr<T>, EventListener<T>)` → EventSubscription — Subscribes a listener to the event and tracks the subscription for cleanup.

### LIFECYCLE

- `clear()` → void — Unsubscribes all currently tracked subscriptions without permanently closing the scope.
- `close()` → void — Closes the scope, unsubscribes all listeners, and rejects future registrations.

### EXAMPLES

#### Scoped subscriptions in a screen

```java
try (EventScope scope = new EventScope()) {
    scope.listen(USER_LOGGED_IN, user -> updateAvatar(user));
    scope.listen(SETTINGS_CHANGED, s -> applySettings(s));
    // when scope closes (e.g. on screen exit), all subscriptions are automatically removed
}
```

