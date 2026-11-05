---
title: "DragSession"
slug: "drag-session"
category: "Core/Widgets/DnD"
locale: "en"
order: 0
---

# DragSession

Live state and controller of an active drag gesture, managing pointer coordinates, active drop target, ghost positioning, insertion indicators, and autoscrolling.

## Overview

`DragSession` represents an active drag-and-drop interaction.

### Session Responsibilities

- **Pointer Tracking**: Manages root coordinates (`pointerX`, `pointerY`), press coordinates, and delta displacements (`deltaX`, `deltaY`).
- **Drop Target Coordination**: Tracks the current accepting `dropTarget()` widget.
- **Overlay Rendering**: Coordinates `DragGhost` rendering and insertion indicator rectangles (`showIndicator`, `clearIndicator`).
- **Autoscroll Integration**: Coordinates container autoscrolling when the pointer hovers near scrollable container edges.

## Displaying a Drop Indicator

```java
session.showIndicator(x, y, width, 2.0f, MutableColor.rgba(0.2f, 0.6f, 1.0f, 1.0f));
```

## Related links

- [DragController](<drag-controller.md>): Drag gesture controller attachable to any widget via composition to initiate drag-and-drop operations with ghost previews, threshold handling, and autoscrolling.
- [DropController](<drop-controller.md>): Drop target controller attachable to any widget via composition to filter payloads and handle drag enter, over, leave, and drop events.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`active()` {: #active data-toc-label="active()" }

Returns true while the drag session is actively ongoing.

Returns `boolean`: True if the drag session is active and not cancelled..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`cancel()` {: #cancel data-toc-label="cancel()" }

Cancels the active drag gesture.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`clearIndicator()` {: #clearindicator data-toc-label="clearIndicator()" }

Hides the drop insertion indicator.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`deltaX()` {: #deltax data-toc-label="deltaX()" }

Returns the pointer movement delta along the X axis.

Returns `float`: Horizontal displacement from the start coordinate in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`deltaY()` {: #deltay data-toc-label="deltaY()" }

Returns the pointer movement delta along the Y axis.

Returns `float`: Vertical displacement from the start coordinate in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`dragged()` {: #dragged data-toc-label="dragged()" }

Returns the widget that is logically moved.

Returns `Widget`: The logical widget being dragged..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`dropTarget()` {: #droptarget data-toc-label="dropTarget()" }

Returns the currently hovered drop zone that accepted the payload, or null.

Returns `Widget`: The current candidate drop target widget, or null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`ghost()` {: #ghost data-toc-label="ghost()" }

Returns the visual drag ghost used in this session.

Returns `DragGhost`: The [DragGhost](<drag-ghost.md>) renderer used for overlay preview..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`ghostOffsetX()` {: #ghostoffsetx data-toc-label="ghostOffsetX()" }

Returns the ghost X offset accounting for axis locking and container scroll compensation.

Returns `float`: Horizontal offset for ghost placement..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`ghostOffsetX(Widget, float)` {: #ghostoffsetxwidget-float data-toc-label="ghostOffsetX(Widget, float)" }

Calculates the ghost X offset for a specific widget with initial coordinate compensation.

| Parameter | Type | Description |
| --- | --- | --- |
| `widget` | `Widget` | Reference widget for coordinate calculation. |
| `initialX` | `float` | Initial X coordinate before drag. |

Returns `float`: Horizontal offset for ghost placement..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`ghostOffsetY()` {: #ghostoffsety data-toc-label="ghostOffsetY()" }

Returns the ghost Y offset accounting for axis locking and container scroll compensation.

Returns `float`: Vertical offset for ghost placement..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`ghostOffsetY(Widget, float)` {: #ghostoffsetywidget-float data-toc-label="ghostOffsetY(Widget, float)" }

Calculates the ghost Y offset for a specific widget with initial coordinate compensation.

| Parameter | Type | Description |
| --- | --- | --- |
| `widget` | `Widget` | Reference widget for coordinate calculation. |
| `initialY` | `float` | Initial Y coordinate before drag. |

Returns `float`: Vertical offset for ghost placement..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`handle()` {: #handle data-toc-label="handle()" }

Returns the handle widget initiating the drag.

Returns `Widget`: The handle widget that initiated the drag..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`indicatorColor()` {: #indicatorcolor data-toc-label="indicatorColor()" }

Returns the insertion indicator color.

Returns `ColorView`: Color of the active indicator rectangle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`indicatorHeight()` {: #indicatorheight data-toc-label="indicatorHeight()" }

Returns the insertion indicator height.

Returns `float`: Height of the active indicator rectangle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`indicatorVisible()` {: #indicatorvisible data-toc-label="indicatorVisible()" }

Returns true if the insertion indicator is currently visible.

Returns `boolean`: True if the drop indicator is currently shown..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`indicatorWidth()` {: #indicatorwidth data-toc-label="indicatorWidth()" }

Returns the insertion indicator width.

Returns `float`: Width of the active indicator rectangle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`indicatorX()` {: #indicatorx data-toc-label="indicatorX()" }

Returns the insertion indicator X coordinate.

Returns `float`: X coordinate of the active indicator rectangle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`indicatorY()` {: #indicatory data-toc-label="indicatorY()" }

Returns the insertion indicator Y coordinate.

Returns `float`: Y coordinate of the active indicator rectangle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`payload()` {: #payload data-toc-label="payload()" }

Returns the payload associated with this drag session.

Returns `DragPayload`: The transfer payload..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`pointerX()` {: #pointerx data-toc-label="pointerX()" }

Returns the current pointer X coordinate in root space.

Returns `float`: Current root pointer X coordinate..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`pointerY()` {: #pointery data-toc-label="pointerY()" }

Returns the current pointer Y coordinate in root space.

Returns `float`: Current root pointer Y coordinate..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`pressX()` {: #pressx data-toc-label="pressX()" }

Returns the initial press X coordinate.

Returns `float`: Initial root pointer X coordinate at press time..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`pressY()` {: #pressy data-toc-label="pressY()" }

Returns the initial press Y coordinate.

Returns `float`: Initial root pointer Y coordinate at press time..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`root()` {: #root data-toc-label="root()" }

Returns the topmost root widget of the hierarchy.

Returns `Widget`: The root widget hosting the drag overlay..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`showIndicator(float, float, float, float, ColorView)` {: #showindicatorfloat-float-float-float-colorview data-toc-label="showIndicator(float, float, float, float, ColorView)" }

Displays a drop insertion indicator rectangle in the overlay layer.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | X coordinate of the indicator rectangle. |
| `y` | `float` | Y coordinate of the indicator rectangle. |
| `width` | `float` | Width of the indicator rectangle. |
| `height` | `float` | Height of the indicator rectangle. |
| `color` | `ColorView` | Color of the indicator rectangle. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragSession::</span>`uiContext()` {: #uicontext data-toc-label="uiContext()" }

Returns the UI context for this session.

Returns `UIContext`: The UI context associated with this session..

</div>

