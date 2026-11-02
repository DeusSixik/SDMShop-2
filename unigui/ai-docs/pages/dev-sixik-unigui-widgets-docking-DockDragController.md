# `DockDragController`

Handles drag-and-drop interactions for docking tabs within DockingRoot, computing drop previews, live tear-offs, and docking intents.

### API

- `active()` → boolean — Returns true if a drag gesture is currently active or pressed.
- `dragThreshold()` → float — Returns the drag activation threshold in pixels.
- `dragThreshold(float)` → DockDragController — Sets the minimum movement threshold in pixels before drag initiates.
- `dragging()` → boolean — Returns true if the drag gesture has exceeded the threshold and is actively dragging.
- `paneId()` → String — Returns the identifier of the dock pane currently being dragged.
- `pointerId()` → int — Returns the active pointer identifier.
- `previewIntent()` → DockDropIntent — Returns the current drop preview intent.

