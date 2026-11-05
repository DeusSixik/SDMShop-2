---
title: "DockArea"
slug: "dock-area"
category: "Core/Widgets/Docking"
locale: "en"
order: 0
---

# DockArea

Target destination zone for docking operations: edge splits (LEFT, RIGHT, TOP, BOTTOM), tab groupings (CENTER, TAB), or detached floating windows (FLOAT).

## Overview

`DockArea` specifies target drop zones when docking a pane into an existing layout.

### Area Types

| DockArea | Action | Node Transformation |
| --- | --- | --- |
| `CENTER` / `TAB` | Tab Grouping | Adds pane into the target leaf node as an additional tab. |
| `LEFT` | Split Horizontal | Divides target space: new pane on Left, existing on Right. |
| `RIGHT` | Split Horizontal | Divides target space: existing pane on Left, new on Right. |
| `TOP` | Split Vertical | Divides target space: new pane on Top, existing on Bottom. |
| `BOTTOM` | Split Vertical | Divides target space: existing pane on Top, new on Bottom. |
| `FLOAT` | Detach Window | Tears off pane into a separate floating window. |

## Related links

- [DockingManager](<docking-manager.md>): Non-visual layout controller managing the hierarchical docking tree, pane additions, splits, tab reordering, and snapshot serialization/restoration.
- [DockingRoot](<../../../widgets/docking/docking-root.md>): Root visual widget for the IDE docking system hosting dock tree nodes, tab strips, split handles, drop previews, and tear-off floating window integrations.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockArea::</span>`insertsBeforeTarget()` {: #insertsbeforetarget data-toc-label="insertsBeforeTarget()" }

Returns true if the area places the new pane before the existing target node.

Returns `boolean`: True if docking in this area splits before the target pane; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockArea::</span>`splitOrientation()` {: #splitorientation data-toc-label="splitOrientation()" }

Returns the split orientation corresponding to edge areas, or null for non-split areas.

Returns `DockSplitOrientation`: The split layout orientation for edge areas, or null for non-split areas..

</div>

