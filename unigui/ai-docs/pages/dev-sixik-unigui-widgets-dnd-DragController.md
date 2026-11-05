# `DragController`

Drag gesture controller attachable to any widget via composition to initiate drag-and-drop operations with ghost previews, threshold handling, and autoscrolling.

### API

- `attach(Widget)` → DragController — Attaches a new DragController to the specified handle widget.
- `autoScroll(boolean)` → DragController — Enables or disables automatic scrolling near edges of parent ScrollViews.
- `autoScroll(float, float)` → DragController — Configures autoscroll edge margin and maximum scroll speed in pixels per second.
- `cancel()` → void — Cancels the active gesture if one is in progress.
- `detach()` → void — Detaches event listeners from the handle widget and terminates active gestures.
- `dragged(Widget)` → DragController — Specifies the logical widget being dragged (e.g. the entire card when dragged by its header).
- `dragging()` → boolean — Returns true if currently in an active dragging state past the threshold.
- `enabled(boolean)` → DragController — Enables or disables this drag controller.
- `filter(Predicate<PointerPressedEvent>)` → DragController — Sets a predicate filter to selectively reject pointer pressed events.
- `ghost(DragGhost)` → DragController — Configures a custom drag ghost renderer.
- `ghost(Widget)` → DragController — Configures a ghost that duplicates the visual appearance of the specified widget.
- `ghostOpacity(float)` → DragController — Sets the opacity level for the widget ghost preview.
- `handle()` → Widget — Returns the handle widget.
- `lockGhostAxis(Orientation)` → DragController — Locks ghost movement to a single axis (e.g. VERTICAL for lists) or null for free movement.
- `onDragEnd(Consumer<DragEnd>)` → DragController — Registers a listener invoked when a drag gesture completes or is cancelled.
- `onDragMove(Consumer<DragSession>)` → DragController — Registers a listener invoked on each pointer movement during drag.
- `onDragStart(Consumer<DragSession>)` → DragController — Registers a listener invoked when a drag gesture starts.
- `payload(DragPayload)` → DragController — Configures a static constant payload for the drag gesture.
- `payload(Supplier<DragPayload>)` → DragController — Configures a lazy payload supplier invoked when the drag threshold is exceeded.
- `session()` → DragSession — Returns the active DragSession, or null if idle.
- `sourceOpacity(float)` → DragController — Sets the opacity of the source widget while dragging (1.0 disables dimming).
- `threshold(float)` → DragController — Sets the minimum movement threshold in pixels before the drag session starts.

