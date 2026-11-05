# `EventPtr`

Handle wrapping a FastEventBus.EventType for convenient subscription on DEFAULT_BUS.

- **Purpose:** Provides a type-safe token that binds to FastEventBus.DEFAULT_BUS, simplifying listener subscription without explicitly referencing the global bus instance.

### USE WHEN

- Expose an event pointer constant in a module API.
- Subscribe directly via eventPtr.subscribe(listener) or pass to EventScope.listen().

- **Related:** `EventPtrInvoker`, `FastEventBus`, `EventScope`

