---
title: "DockSplitOrientation"
slug: "dock-split-orientation"
category: "Core/Widgets/Docking"
locale: "en"
order: 0
---

# DockSplitOrientation

Split layout orientation between adjacent dock nodes: HORIZONTAL (side-by-side) or VERTICAL (stacked top/bottom).

## Overview

`DockSplitOrientation` defines the direction a split dock node divides space between its two children.

| Orientation | Layout | Child 1 | Child 2 |
| --- | --- | --- | --- |
| `HORIZONTAL` | Side-by-side | Left | Right |
| `VERTICAL` | Stacked | Top | Bottom |

## Related links

- [DockNode](<dock-node.md>): Hierarchical docking tree node: either a LEAF containing tabbed DockPanes or a SPLIT node dividing two children horizontally or vertically.
- [DockingRoot](<../../../widgets/docking/docking-root.md>): Root visual widget for the IDE docking system hosting dock tree nodes, tab strips, split handles, drop previews, and tear-off floating window integrations.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockSplitOrientation::</span>`toWidgetOrientation()` {: #towidgetorientation data-toc-label="toWidgetOrientation()" }

Converts the dock split orientation to the core widget Orientation enum.

Returns `Orientation`: Equivalent UniGUI Orientation value..

</div>

