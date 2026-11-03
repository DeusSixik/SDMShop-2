---
title: "DockDropIntent"
slug: "dock-drop-intent"
category: "Core/Widgets/Docking"
locale: "en"
order: 0
---

# DockDropIntent

Resolved docking drop intention capturing the target area, preview bounds, and source/target pane identifiers.

## Overview

`DockDropIntent` represents a resolved target drop location during dock tab dragging.

Contains target `DockArea`, target pane ID, preview bounding rectangle, and whether the intent represents a split, tab grouping, or floating window tear-off.

## Related links

- [DockArea](<dock-area.md>): Target destination zone for docking operations: edge splits (LEFT, RIGHT, TOP, BOTTOM), tab groupings (CENTER, TAB), or detached floating windows (FLOAT).
- [DockingRoot](<../../../widgets/docking/docking-root.md>): Root visual widget for the IDE docking system hosting dock tree nodes, tab strips, split handles, drop previews, and tear-off floating window integrations.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDropIntent::</span>`floating()` {: #floating data-toc-label="floating()" }

Returns true if this intent detaches the pane into a floating window.

Returns `boolean`: True if this intent requests floating window tear-off..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDropIntent::</span>`floating(String, float, float)` {: #floatingstring-float-float data-toc-label="floating(String, float, float)" }

Creates a floating tear-off drop intent around the pointer.

| Parameter | Type | Description |
| --- | --- | --- |
| `sourcePaneId` | `String` | Identifier of the dragged source pane. |
| `pointerX` | `float` | Pointer X coordinate. |
| `pointerY` | `float` | Pointer Y coordinate. |

Returns `DockDropIntent`: A resolved dock drop intent instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDropIntent::</span>`none()` {: #none data-toc-label="none()" }

Returns an invalid empty drop intent representing no drop target.

Returns `DockDropIntent`: A resolved dock drop intent instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDropIntent::</span>`of(String, String, DockArea, RectView, float, float)` {: #ofstring-string-dockarea-rectview-float-float data-toc-label="of(String, String, DockArea, RectView, float, float)" }

Creates a valid dock drop intent for the specified area and bounds.

| Parameter | Type | Description |
| --- | --- | --- |
| `sourcePaneId` | `String` | Identifier of the dragged source pane. |
| `targetPaneId` | `String` | Identifier of the target pane under cursor. |
| `area` | `DockArea` | Target dock area. |
| `previewBounds` | `RectView` | Visual preview rectangle bounds. |
| `pointerX` | `float` | Pointer X coordinate. |
| `pointerY` | `float` | Pointer Y coordinate. |

Returns `DockDropIntent`: A resolved dock drop intent instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDropIntent::</span>`split()` {: #split data-toc-label="split()" }

Returns true if this intent splits an existing node.

Returns `boolean`: True if this intent requests edge split docking..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockDropIntent::</span>`tabbed()` {: #tabbed data-toc-label="tabbed()" }

Returns true if this intent adds the pane as a tab.

Returns `boolean`: True if this intent requests tab grouping into an existing pane..

</div>

