---
title: "DockingManager"
slug: "docking-manager"
category: "Core/Widgets/Docking"
locale: "en"
order: 0
---

# DockingManager

Non-visual layout controller managing the hierarchical docking tree, pane additions, splits, tab reordering, and snapshot serialization/restoration.

## Overview

`DockingManager` is the non-visual model and controller for docking layout trees.

Performs tree mutations:
- Adding panes to edge splits or tab groups (`addPane`, `tabPane`, `splitPane`).
- Reordering tabs within a leaf (`reorderPane`).
- Detaching panes to floating windows (`detachPaneForFloating`).
- Snapshot generation and restoration (`snapshot`, `restore`).

## Splitting and Tab-Grouping Panes

```java
dockingRoot.manager().splitPane("editor-1", DockArea.RIGHT, toolPane, 0.3f);
```

## Related links

- [DockLayoutSnapshot](<dock-layout-snapshot.md>): Immutable snapshot representing the recursive docking layout tree and active pane selection for serialization and layout persistence.
- [DockNode](<dock-node.md>): Hierarchical docking tree node: either a LEAF containing tabbed DockPanes or a SPLIT node dividing two children horizontally or vertically.
- [DockingRoot](<../../../widgets/docking/docking-root.md>): Root visual widget for the IDE docking system hosting dock tree nodes, tab strips, split handles, drop previews, and tear-off floating window integrations.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`addPane(DockPane)` {: #addpanedockpane data-toc-label="addPane(DockPane)" }

Adds a pane to the preferred leaf node.

| Parameter | Type | Description |
| --- | --- | --- |
| `pane` | `DockPane` | [DockPane](<dock-pane.md>) to add or manipulate. |

Returns `DockingManager`: This DockingManager for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`closeActivePane()` {: #closeactivepane data-toc-label="closeActivePane()" }

Closes the currently active pane if permitted by close guards.

Returns `boolean`: True if the pane was closed; false if cancelled or not found..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`closePane(String)` {: #closepanestring data-toc-label="closePane(String)" }

Closes and removes the pane with the specified ID.

| Parameter | Type | Description |
| --- | --- | --- |
| `paneId` | `String` | Identifier of the pane. |

Returns `DockingManager`: This DockingManager for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`containsPane(String)` {: #containspanestring data-toc-label="containsPane(String)" }

Checks whether a pane with the given ID exists in the docking tree.

| Parameter | Type | Description |
| --- | --- | --- |
| `paneId` | `String` | Identifier of the pane. |

Returns `boolean`: True if a pane with the specified ID exists..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`detachPaneForFloating(String)` {: #detachpaneforfloatingstring data-toc-label="detachPaneForFloating(String)" }

Detaches a pane from the dock tree for floating without creating the window directly.

| Parameter | Type | Description |
| --- | --- | --- |
| `paneId` | `String` | Identifier of the pane. |

Returns `DockPane`: The detached [DockPane](<dock-pane.md>) instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`dockPane(String, String, DockArea)` {: #dockpanestring-string-dockarea data-toc-label="dockPane(String, String, DockArea)" }

Moves an existing pane to a new dock area relative to targetPaneId.

| Parameter | Type | Description |
| --- | --- | --- |
| `paneId` | `String` | Identifier of the pane. |
| `targetPaneId` | `String` | Identifier of the reference target pane. |
| `area` | `DockArea` | Target dock area. |

Returns `boolean`: True if docking succeeded; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`empty()` {: #empty data-toc-label="empty()" }

Returns true if the docking manager contains no panes.

Returns `boolean`: True if the docking tree contains no panes..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`findPane(String)` {: #findpanestring data-toc-label="findPane(String)" }

Finds a [DockPane](<dock-pane.md>) by its identifier, or null if not found.

| Parameter | Type | Description |
| --- | --- | --- |
| `paneId` | `String` | Identifier of the pane. |

Returns `DockPane`: The matching [DockPane](<dock-pane.md>) or null if not found..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`floatPane(String)` {: #floatpanestring data-toc-label="floatPane(String)" }

Detaches the pane into a new independent [WindowWidget](<../../../widgets/feedback/window-widget.md>).

| Parameter | Type | Description |
| --- | --- | --- |
| `paneId` | `String` | Identifier of the pane. |

Returns `WindowWidget`: The created floating [WindowWidget](<../../../widgets/feedback/window-widget.md>) hosting the detached pane..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`paneCount()` {: #panecount data-toc-label="paneCount()" }

Returns the total number of registered dock panes.

Returns `int`: Total number of panes across the entire docking tree..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`panes()` {: #panes data-toc-label="panes()" }

Returns an immutable list of all panes across all leaves.

Returns `List`: Unmodifiable collection of all registered panes..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`reorderPane(String, String)` {: #reorderpanestring-string data-toc-label="reorderPane(String, String)" }

Reorders a pane before another tab in the same leaf during live drag reordering.

| Parameter | Type | Description |
| --- | --- | --- |
| `paneId` | `String` | Identifier of the pane. |
| `beforePaneId` | `String` | Identifier of the pane before which to insert. |

Returns `boolean`: True if tabs were reordered; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`restore(DockLayoutSnapshot, Map)` {: #restoredocklayoutsnapshot-map data-toc-label="restore(DockLayoutSnapshot, Map)" }

Restores docking layout from a snapshot using the provided pane registry.

| Parameter | Type | Description |
| --- | --- | --- |
| `snapshot` | `DockLayoutSnapshot` | Layout snapshot to restore. |
| `paneRegistry` | `Map` | Map of registered panes by ID. |

Returns `RestoreResult`: True if the snapshot was restored successfully..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`rootNode()` {: #rootnode data-toc-label="rootNode()" }

Returns the root [DockNode](<dock-node.md>) of the docking hierarchy.

Returns `DockNode`: The root node of the docking hierarchy..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`selectNextTab()` {: #selectnexttab data-toc-label="selectNextTab()" }

Selects the next tab in the active leaf.

Returns `boolean`: True if the active tab was changed; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`selectPane(String)` {: #selectpanestring data-toc-label="selectPane(String)" }

Selects and activates the pane with the specified ID.

| Parameter | Type | Description |
| --- | --- | --- |
| `paneId` | `String` | Identifier of the pane. |

Returns `DockingManager`: This DockingManager for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`selectPreviousTab()` {: #selectprevioustab data-toc-label="selectPreviousTab()" }

Selects the previous tab in the active leaf.

Returns `boolean`: True if the active tab was changed; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`selectedLeaf()` {: #selectedleaf data-toc-label="selectedLeaf()" }

Returns the leaf node hosting the currently active pane.

Returns `DockNode`: The currently selected leaf node..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`selectedPane()` {: #selectedpane data-toc-label="selectedPane()" }

Returns the currently active selected dock pane.

Returns `DockPane`: The currently selected active [DockPane](<dock-pane.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`snapshot()` {: #snapshot data-toc-label="snapshot()" }

Captures an immutable snapshot of the current docking layout.

Returns `DockLayoutSnapshot`: A captured snapshot of the current docking layout..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`splitPane(String, DockArea, DockPane)` {: #splitpanestring-dockarea-dockpane data-toc-label="splitPane(String, DockArea, DockPane)" }

Splits the leaf containing targetPaneId along the specified area with 0.5 ratio.

| Parameter | Type | Description |
| --- | --- | --- |
| `targetPaneId` | `String` | Identifier of the reference target pane. |
| `area` | `DockArea` | Target dock area. |
| `pane` | `DockPane` | [DockPane](<dock-pane.md>) to add or manipulate. |

Returns `DockingManager`: This DockingManager for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`splitPane(String, DockArea, DockPane, float)` {: #splitpanestring-dockarea-dockpane-float data-toc-label="splitPane(String, DockArea, DockPane, float)" }

Splits the leaf containing targetPaneId along the specified area with a custom split ratio.

| Parameter | Type | Description |
| --- | --- | --- |
| `targetPaneId` | `String` | Identifier of the reference target pane. |
| `area` | `DockArea` | Target dock area. |
| `pane` | `DockPane` | [DockPane](<dock-pane.md>) to add or manipulate. |
| `splitRatio` | `float` | Split ratio for dividing nodes. |

Returns `DockingManager`: This DockingManager for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingManager::</span>`tabPane(String, DockPane)` {: #tabpanestring-dockpane data-toc-label="tabPane(String, DockPane)" }

Adds a pane as a tab into the leaf containing targetPaneId.

| Parameter | Type | Description |
| --- | --- | --- |
| `targetPaneId` | `String` | Identifier of the reference target pane. |
| `pane` | `DockPane` | [DockPane](<dock-pane.md>) to add or manipulate. |

Returns `DockingManager`: This DockingManager for method chaining..

</div>

