---
title: "DockDragController"
slug: "dock-drag-controller"
category: "Core/Widgets/Docking"
locale: "en"
order: 0
---

# DockDragController

Handles drag-and-drop interactions for docking tabs within [DockingRoot](<../../../widgets/docking/docking-root.md>), computing drop previews, live tear-offs, and docking intents.

## Overview

`DockDragController` manages pointer drag gestures across docking tabs.

It computes hit tests against dock nodes, detects tear-offs when dragging outside dock bounds, evaluates cross-docking zones, and produces `DockDropIntent` previews.

## Related links

- [DockDropIntent](<dock-drop-intent.md>): Resolved docking drop intention capturing the target area, preview bounds, and source/target pane identifiers.
- [DockingRoot](<../../../widgets/docking/docking-root.md>): Root visual widget for the IDE docking system hosting dock tree nodes, tab strips, split handles, drop previews, and tear-off floating window integrations.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDragController::</span>`active()` {: #active data-toc-label="active()" }

Returns true if a drag gesture is currently active or pressed.

Returns `boolean`: True if a drag gesture is currently underway..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDragController::</span>`dragThreshold()` {: #dragthreshold data-toc-label="dragThreshold()" }

Returns the drag activation threshold in pixels.

Returns `float`: The movement threshold in pixels before drag starts..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDragController::</span>`dragThreshold(float)` {: #dragthresholdfloat data-toc-label="dragThreshold(float)" }

Sets the minimum movement threshold in pixels before drag initiates.

| Parameter | Type | Description |
| --- | --- | --- |
| `dragThreshold` | `float` | Drag threshold in pixels before initiating tab drag. |

Returns `DockDragController`: This controller for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDragController::</span>`dragging()` {: #dragging data-toc-label="dragging()" }

Returns true if the drag gesture has exceeded the threshold and is actively dragging.

Returns `boolean`: True if dragging beyond the movement threshold..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDragController::</span>`paneId()` {: #paneid data-toc-label="paneId()" }

Returns the identifier of the dock pane currently being dragged.

Returns `String`: The identifier of the pane being dragged..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDragController::</span>`pointerId()` {: #pointerid data-toc-label="pointerId()" }

Returns the active pointer identifier.

Returns `int`: The active pointer identifier..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDragController::</span>`previewIntent()` {: #previewintent data-toc-label="previewIntent()" }

Returns the current drop preview intent.

Returns `DockDropIntent`: The current resolved drop intent..

</div>

