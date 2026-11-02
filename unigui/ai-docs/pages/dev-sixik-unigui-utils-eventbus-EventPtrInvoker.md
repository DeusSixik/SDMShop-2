# `EventPtrInvoker` (extends `EventPtr`)

EventPtr extension that enables firing/invoking the event on DEFAULT_BUS.

- **Purpose:** Used by event publishers to hold both subscription and invocation capabilities in a single handle.

### USE WHEN

- Define an event endpoint where the owning component needs to trigger the event via invoke().

- **Related:** `EventPtr`, `FastEventBus`, `EventScope`

