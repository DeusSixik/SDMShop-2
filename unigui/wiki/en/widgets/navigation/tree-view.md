---
title: "TreeView"
slug: "tree-view"
category: "Widgets/Navigation"
locale: "en"
order: 0
---

# TreeView

Hierarchical tree container rendering collapsable nodes with custom icons, inline editing, and selection.

## Overview

Hierarchical tree container rendering collapsable nodes with custom icons, inline editing, and selection. Provides extensive layout configuration and interaction capabilities.

## Basic Usage

```java
TreeView tree = new TreeView();
TreeViewNode root = tree.addRoot("Project");
root.addChild("src");
root.addChild("build.gradle");
```

## Related links

- [TreeList](<tree-list.md>): Specialized TreeView for hierarchical path navigation and directory listing.

## API

<div class="api-member-card" markdown="1">

### `TreeView()` {: #treeview data-toc-label="TreeView()" }

Constructs an empty TreeView container.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`addRoot(RichText)` {: #addrootrichtext data-toc-label="addRoot(RichText)" }

Creates and appends a new root node with [RichText](<../../core/text/rich-text.md>) label.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `RichText` | The rich text label. |

Returns `TreeViewNode`: The tree node instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`addRoot(String)` {: #addrootstring data-toc-label="addRoot(String)" }

Creates and appends a new root node with plain text label.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | The text label. |

Returns `TreeViewNode`: The tree node instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`addRoot(TreeViewNode)` {: #addroottreeviewnode data-toc-label="addRoot(TreeViewNode)" }

Appends an existing node as a root.

| Parameter | Type | Description |
| --- | --- | --- |
| `node` | `TreeViewNode` | The tree node instance. |

Returns `TreeView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`beginBatch()` {: #beginbatch data-toc-label="beginBatch()" }

Begins a batch update session, suppressing layout recalculations until endBatch is called.

Returns `TreeView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`cancelEditing()` {: #cancelediting data-toc-label="cancelEditing()" }

Cancels the active inline node edit session without committing changes.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`clearRoots()` {: #clearroots data-toc-label="clearRoots()" }

Removes all root nodes from the tree.

Returns `TreeView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`clearSelection()` {: #clearselection data-toc-label="clearSelection()" }

Clears any current node selection.

Returns `TreeView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`editingNode()` {: #editingnode data-toc-label="editingNode()" }

Returns the node currently being edited in-place, or null if not editing.

Returns `TreeViewNode`: The node currently being edited, or null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`endBatch()` {: #endbatch data-toc-label="endBatch()" }

Ends a batch update session, triggering row rebuilding if mutations occurred.

Returns `TreeView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`hoverScrollSpeed()` {: #hoverscrollspeed data-toc-label="hoverScrollSpeed()" }

Returns the text hover scrolling speed.

Returns `float`: The hover scroll speed in pixels per second..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`hoverScrollSpeed(float)` {: #hoverscrollspeedfloat data-toc-label="hoverScrollSpeed(float)" }

Sets the text hover scrolling speed.

| Parameter | Type | Description |
| --- | --- | --- |
| `pixelsPerSecond` | `float` | The scroll speed in pixels per second. |

Returns `TreeView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`iconMode()` {: #iconmode data-toc-label="iconMode()" }

Returns the current tree icon display mode.

Returns `TreeIconMode`: The tree icon mode..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`iconMode(TreeIconMode)` {: #iconmodetreeiconmode data-toc-label="iconMode(TreeIconMode)" }

Sets the tree icon display mode.

| Parameter | Type | Description |
| --- | --- | --- |
| `iconMode` | `TreeIconMode` | The tree icon mode. |

Returns `TreeView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`insertRoot(int, TreeViewNode)` {: #insertrootint-treeviewnode data-toc-label="insertRoot(int, TreeViewNode)" }

Inserts a root node at the specified index.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | The index position. |
| `node` | `TreeViewNode` | The tree node instance. |

Returns `TreeView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`isBatching()` {: #isbatching data-toc-label="isBatching()" }

Returns true if batch mode is currently active.

Returns `boolean`: True if batch updates are active..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`isEditing()` {: #isediting data-toc-label="isEditing()" }

Returns true if an inline node label edit operation is active.

Returns `boolean`: True if inline editing is active..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`onRowCreated()` {: #onrowcreated data-toc-label="onRowCreated()" }

Returns the row creation callback listener.

Returns `Consumer`: The result value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`onRowCreated(Consumer)` {: #onrowcreatedconsumer data-toc-label="onRowCreated(Consumer)" }

Registers a listener invoked whenever a row button is instantiated or re-bound.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `Consumer` | The event listener callback. |

Returns `TreeView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`onSelectionChanged(EventListener)` {: #onselectionchangedeventlistener data-toc-label="onSelectionChanged(EventListener)" }

Registers a listener called when node selection changes.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | The event listener callback. |

Returns `EventSubscription`: The event subscription handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`removeRoot(TreeViewNode)` {: #removeroottreeviewnode data-toc-label="removeRoot(TreeViewNode)" }

Removes the specified root node from the tree.

| Parameter | Type | Description |
| --- | --- | --- |
| `node` | `TreeViewNode` | The tree node instance. |

Returns `TreeView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`root(int)` {: #rootint data-toc-label="root(int)" }

Returns the root node at the specified index.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | The index position. |

Returns `TreeViewNode`: The root node at the specified index..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`rootCount()` {: #rootcount data-toc-label="rootCount()" }

Returns the total number of root nodes.

Returns `int`: The number of root nodes..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`roots()` {: #roots data-toc-label="roots()" }

Returns an unmodifiable list of top-level root nodes.

Returns `List`: An unmodifiable list of root nodes..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`rowTextHoverScrollSpeed()` {: #rowtexthoverscrollspeed data-toc-label="rowTextHoverScrollSpeed()" }

Returns the text hover scrolling speed in pixels per second for overflowing node labels.

Returns `float`: The hover scroll speed in pixels per second..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`rowTextHoverScrollSpeed(float)` {: #rowtexthoverscrollspeedfloat data-toc-label="rowTextHoverScrollSpeed(float)" }

Sets the text hover scrolling speed in pixels per second.

| Parameter | Type | Description |
| --- | --- | --- |
| `pixelsPerSecond` | `float` | The scroll speed in pixels per second. |

Returns `TreeView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`rowsHost()` {: #rowshost data-toc-label="rowsHost()" }

Returns the vertical container hosting row button widgets.

Returns `VBox`: The container hosting tree row buttons..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`select(TreeViewNode)` {: #selecttreeviewnode data-toc-label="select(TreeViewNode)" }

Selects the specified node and fires a selection change event.

| Parameter | Type | Description |
| --- | --- | --- |
| `node` | `TreeViewNode` | The tree node instance. |

Returns `TreeView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`selectedIndex()` {: #selectedindex data-toc-label="selectedIndex()" }

Returns the visible index of the currently selected node.

Returns `int`: The selected index, or -1 if none..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`selectedNode()` {: #selectednode data-toc-label="selectedNode()" }

Returns the currently selected tree node, or null if none.

Returns `TreeViewNode`: The currently selected node, or null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`selectedPath()` {: #selectedpath data-toc-label="selectedPath()" }

Returns the hierarchical index path of the currently selected node.

Returns `List`: The index path of the selected node..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`silentSelect(TreeViewNode)` {: #silentselecttreeviewnode data-toc-label="silentSelect(TreeViewNode)" }

Selects the specified node without emitting a selection change event.

| Parameter | Type | Description |
| --- | --- | --- |
| `node` | `TreeViewNode` | The tree node instance. |

Returns `TreeView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`startEditing(TreeViewNode, Consumer)` {: #starteditingtreeviewnode-consumer data-toc-label="startEditing(TreeViewNode, Consumer)" }

Starts inline text editing for the specified node with default input configuration.

| Parameter | Type | Description |
| --- | --- | --- |
| `node` | `TreeViewNode` | The tree node instance. |
| `onCommit` | `Consumer` | The commit callback on editing completion. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`startEditing(TreeViewNode, Consumer, Consumer)` {: #starteditingtreeviewnode-consumer-consumer data-toc-label="startEditing(TreeViewNode, Consumer, Consumer)" }

Starts inline text editing for the specified node with custom text input configuration and commit callback.

| Parameter | Type | Description |
| --- | --- | --- |
| `node` | `TreeViewNode` | The tree node instance. |
| `config` | `Consumer` | The text input configuration callback. |
| `onCommit` | `Consumer` | The commit callback on editing completion. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`stopEditing()` {: #stopediting data-toc-label="stopEditing()" }

Commits and closes the active inline node edit session.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeView::</span>`visibleNodes()` {: #visiblenodes data-toc-label="visibleNodes()" }

Returns an unmodifiable list of currently visible (flattened) tree nodes.

Returns `List`: An unmodifiable list of visible nodes..

</div>

