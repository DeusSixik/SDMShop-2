# `DragSession`

Live state and controller of an active drag gesture, managing pointer coordinates, active drop target, ghost positioning, insertion indicators, and autoscrolling.

### API

- `active()` → boolean — Returns true while the drag session is actively ongoing.
- `cancel()` → void — Cancels the active drag gesture.
- `clearIndicator()` → void — Hides the drop insertion indicator.
- `deltaX()` → float — Returns the pointer movement delta along the X axis.
- `deltaY()` → float — Returns the pointer movement delta along the Y axis.
- `dragged()` → Widget — Returns the widget that is logically moved.
- `dropTarget()` → Widget — Returns the currently hovered drop zone that accepted the payload, or null.
- `ghost()` → DragGhost — Returns the visual drag ghost used in this session.
- `ghostOffsetX()` → float — Returns the ghost X offset accounting for axis locking and container scroll compensation.
- `ghostOffsetX(Widget, float)` → float — Calculates the ghost X offset for a specific widget with initial coordinate compensation.
- `ghostOffsetY()` → float — Returns the ghost Y offset accounting for axis locking and container scroll compensation.
- `ghostOffsetY(Widget, float)` → float — Calculates the ghost Y offset for a specific widget with initial coordinate compensation.
- `handle()` → Widget — Returns the handle widget initiating the drag.
- `indicatorColor()` → ColorView — Returns the insertion indicator color.
- `indicatorHeight()` → float — Returns the insertion indicator height.
- `indicatorVisible()` → boolean — Returns true if the insertion indicator is currently visible.
- `indicatorWidth()` → float — Returns the insertion indicator width.
- `indicatorX()` → float — Returns the insertion indicator X coordinate.
- `indicatorY()` → float — Returns the insertion indicator Y coordinate.
- `payload()` → DragPayload — Returns the payload associated with this drag session.
- `pointerX()` → float — Returns the current pointer X coordinate in root space.
- `pointerY()` → float — Returns the current pointer Y coordinate in root space.
- `pressX()` → float — Returns the initial press X coordinate.
- `pressY()` → float — Returns the initial press Y coordinate.
- `root()` → Widget — Returns the topmost root widget of the hierarchy.
- `showIndicator(float, float, float, float, ColorView)` → void — Displays a drop insertion indicator rectangle in the overlay layer.
- `uiContext()` → UIContext — Returns the UI context for this session.

