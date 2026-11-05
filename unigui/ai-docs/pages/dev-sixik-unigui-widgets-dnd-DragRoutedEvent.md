# `DragRoutedEvent` (extends `BaseEvent`, implements `RoutableWidgetEvent`)

Abstract base class for routed drag-and-drop events conveying the active DragSession across widget hierarchies.

### API

- `currentTarget()` → Widget — Returns the current widget handling the event during dispatch.
- `payload()` → DragPayload — Convenience shortcut returning the payload from the active drag session.
- `phase()` → EventPhase — Returns the current routing phase (capture, target, bubble).
- `rootX()` → float — Returns the pointer X coordinate in root UI space.
- `rootY()` → float — Returns the pointer Y coordinate in root UI space.
- `session()` → DragSession — Returns the active DragSession associated with this event.
- `target()` → Widget — Returns the original target widget where the event was initiated.
- `type()` → EventType<? extends Event> — Returns the event type token.

