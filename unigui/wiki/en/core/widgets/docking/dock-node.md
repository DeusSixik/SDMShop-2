---
title: "DockNode"
slug: "dock-node"
category: "Core/Widgets/Docking"
locale: "en"
order: 0
---

# DockNode

Hierarchical docking tree node: either a LEAF containing tabbed DockPanes or a SPLIT node dividing two children horizontally or vertically.

## Overview

`DockNode` is an immutable-like tree node structuring the docking hierarchy.

- **`LEAF` Node**: Holds a list of tabbed `DockPane`s with a `selectedIndex`.
- **`SPLIT` Node**: Divides two child nodes (`first`, `second`) along `HORIZONTAL` or `VERTICAL` orientation with a fractional `splitRatio`.

## Related links

- [DockPane](<dock-pane.md>): Descriptor representing an individual dockable pane hosting arbitrary widget content, tab title, icon, dirty state, and close guards.
- [DockingManager](<docking-manager.md>): Non-visual layout controller managing the hierarchical docking tree, pane additions, splits, tab reordering, and snapshot serialization/restoration.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`containsPane(String)` {: #containspanestring data-toc-label="containsPane(String)" }

Checks whether this node or its descendants contain a pane with the given ID.

| Parameter | Type | Description |
| --- | --- | --- |
| `paneId` | `String` | Identifier of the pane to search for. |

Returns `boolean`: True if this subtree contains a pane with the given ID..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`empty()` {: #empty data-toc-label="empty()" }

Returns true if this leaf contains zero panes.

Returns `boolean`: True if this node contains no panes..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`findPane(String)` {: #findpanestring data-toc-label="findPane(String)" }

Finds a [DockPane](<dock-pane.md>) by identifier within this node and its descendants.

| Parameter | Type | Description |
| --- | --- | --- |
| `paneId` | `String` | Identifier of the pane to search for. |

Returns `DockPane`: The matching [DockPane](<dock-pane.md>), or null if not found..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`first()` {: #first data-toc-label="first()" }

Returns the first child node of a split.

Returns `DockNode`: The requested child node..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`id()` {: #id data-toc-label="id()" }

Returns the unique identifier of this dock node.

Returns `String`: Unique node identifier..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`isLeaf()` {: #isleaf data-toc-label="isLeaf()" }

Returns true if this node is a leaf containing panes.

Returns `boolean`: True if this is a leaf node holding panes..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`isSplit()` {: #issplit data-toc-label="isSplit()" }

Returns true if this node is a split node containing two children.

Returns `boolean`: True if this is a split node holding two child nodes..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`kind()` {: #kind data-toc-label="kind()" }

Returns whether this node is a LEAF or SPLIT.

Returns `Kind`: Node classification kind (LEAF or SPLIT)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`leaf()` {: #leaf data-toc-label="leaf()" }

Creates an empty leaf dock node.

Returns `DockNode`: The created DockNode instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`leaf(DockPane)` {: #leafdockpane data-toc-label="leaf(DockPane)" }

Creates a leaf dock node populated with an initial pane.

| Parameter | Type | Description |
| --- | --- | --- |
| `pane` | `DockPane` | [DockPane](<dock-pane.md>) to host in the leaf node. |

Returns `DockNode`: The created DockNode instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`orientation()` {: #orientation data-toc-label="orientation()" }

Returns the split orientation (HORIZONTAL or VERTICAL) if this is a split node.

Returns `DockSplitOrientation`: Split orientation, or null for leaf nodes..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`panes()` {: #panes data-toc-label="panes()" }

Returns the unmodifiable list of panes hosted in this leaf node.

Returns `List`: Unmodifiable list of tabbed DockPanes in this leaf..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`second()` {: #second data-toc-label="second()" }

Returns the second child node of a split.

Returns `DockNode`: The requested child node..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`selectedIndex()` {: #selectedindex data-toc-label="selectedIndex()" }

Returns the index of the currently active tab in this leaf.

Returns `int`: Zero-based index of the active selected tab..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`selectedPane()` {: #selectedpane data-toc-label="selectedPane()" }

Returns the currently selected [DockPane](<dock-pane.md>) in this leaf, or null if empty.

Returns `DockPane`: The active selected [DockPane](<dock-pane.md>), or null if empty..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`split(DockSplitOrientation, DockNode, DockNode, float)` {: #splitdocksplitorientation-docknode-docknode-float data-toc-label="split(DockSplitOrientation, DockNode, DockNode, float)" }

Creates a split dock node dividing two children according to orientation and ratio.

| Parameter | Type | Description |
| --- | --- | --- |
| `orientation` | `DockSplitOrientation` | Split orientation dividing the two children. |
| `first` | `DockNode` | First child node. |
| `second` | `DockNode` | Second child node. |
| `splitRatio` | `float` | Ratio dividing space between children (0.0 to 1.0). |

Returns `DockNode`: The created DockNode instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockNode::</span>`splitRatio()` {: #splitratio data-toc-label="splitRatio()" }

Returns the fractional split ratio between first and second children (0.1 to 0.9).

Returns `float`: Split ratio dividing the two children..

</div>

