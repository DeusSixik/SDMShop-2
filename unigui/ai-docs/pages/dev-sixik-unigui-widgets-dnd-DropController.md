# `DropController`

Drop target controller attachable to any widget via composition to filter payloads and handle drag enter, over, leave, and drop events.

### API

- `accepts(DragPayload)` → boolean — Tests whether the specified payload matches type and validator requirements.
- `accepts(Predicate<DragPayload>)` → DropController — Adds a custom predicate validation rule for candidate payloads.
- `accepts(String)` → DropController — Restricts accepted payload types by comma/pipe/whitespace delimited string (* accepts all).
- `attach(Widget)` → DropController — Attaches a DropController to the specified widget zone.
- `detach()` → void — Detaches event listeners from the host drop zone widget.
- `enabled(boolean)` → DropController — Enables or disables this drop controller.
- `hovering()` → boolean — Returns true while an accepted drag payload is hovering over the zone.
- `onDragEnter(Consumer<DragOverEvent>)` → DropController — Registers a listener called when an accepted payload enters the drop zone.
- `onDragLeave(Consumer<DragLeaveEvent>)` → DropController — Registers a listener called when the drag pointer leaves the drop zone or is cancelled.
- `onDragOver(Consumer<DragOverEvent>)` → DropController — Registers a listener called on each movement and frame tick over the drop zone.
- `onDrop(Consumer<DragDropEvent>)` → DropController — Registers a listener called when an accepted payload is dropped over the zone.
- `zone()` → Widget — Returns the host drop zone widget.

