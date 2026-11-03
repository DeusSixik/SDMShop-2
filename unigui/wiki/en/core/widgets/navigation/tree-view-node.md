---
title: "TreeViewNode"
slug: "tree-view-node"
category: "Core/Widgets/Navigation"
locale: "en"
order: 0
---

# TreeViewNode

Hierarchical node item in a [TreeView](<../../../widgets/navigation/tree-view.md>) holding text, custom icon, expansion state, and child nodes.

## Overview

Hierarchical node item in a [TreeView](<../../../widgets/navigation/tree-view.md>) holding text, custom icon, expansion state, and child nodes. Provides extensive layout configuration and interaction capabilities.

## Basic Usage

```java
TreeViewNode node = new TreeViewNode("Folder");
node.folder(true).expanded(true);
node.addChild("file.txt");
```

## Related links

- [TreeView](<../../../widgets/navigation/tree-view.md>): Hierarchical tree container rendering collapsable nodes with custom icons, inline editing, and selection.

## API

<div class="api-member-card" markdown="1">

### `TreeViewNode()` {: #treeviewnode data-toc-label="TreeViewNode()" }

Constructs an empty TreeViewNode with empty label.

</div>

<div class="api-member-card" markdown="1">

### `TreeViewNode(RichText)` {: #treeviewnoderichtext data-toc-label="TreeViewNode(RichText)" }

Constructs a TreeViewNode with a [RichText](<../../text/rich-text.md>) label.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `RichText` | The rich text label. |

</div>

<div class="api-member-card" markdown="1">

### `TreeViewNode(String)` {: #treeviewnodestring data-toc-label="TreeViewNode(String)" }

Constructs a TreeViewNode with a plain text label.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | The text label. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`addChild(RichText)` {: #addchildrichtext data-toc-label="addChild(RichText)" }

Creates and appends a child node with [RichText](<../../text/rich-text.md>) label.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `RichText` | The rich text label. |

Returns `TreeViewNode`: The child tree node instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`addChild(String)` {: #addchildstring data-toc-label="addChild(String)" }

Creates and appends a child node with plain text label.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | The text label. |

Returns `TreeViewNode`: The child tree node instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`addChild(TreeViewNode)` {: #addchildtreeviewnode data-toc-label="addChild(TreeViewNode)" }

Appends an existing node as a child.

| Parameter | Type | Description |
| --- | --- | --- |
| `child` | `TreeViewNode` | The child node instance. |

Returns `TreeViewNode`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`child(int)` {: #childint data-toc-label="child(int)" }

Returns the child node at the specified index.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | The index position. |

Returns `TreeViewNode`: The child tree node instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`childCount()` {: #childcount data-toc-label="childCount()" }

Returns the number of child nodes under this node.

Returns `int`: The number of child nodes..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`children()` {: #children data-toc-label="children()" }

Returns an unmodifiable list of child nodes.

Returns `List`: An unmodifiable list of child nodes..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`clearChildren()` {: #clearchildren data-toc-label="clearChildren()" }

Removes all child nodes from this node.

Returns `TreeViewNode`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`depth()` {: #depth data-toc-label="depth()" }

Returns the nesting depth of this node from the root.

Returns `int`: The nesting depth level..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`emptyFolder()` {: #emptyfolder data-toc-label="emptyFolder()" }

Returns true if this node is configured to display as an empty folder.

Returns `boolean`: True if configured as an empty folder..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`emptyFolder(boolean)` {: #emptyfolderboolean data-toc-label="emptyFolder(boolean)" }

Sets whether this node displays folder semantics even without children.

| Parameter | Type | Description |
| --- | --- | --- |
| `emptyFolder` | `boolean` | Whether the node displays as an empty folder. |

Returns `TreeViewNode`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`expanded()` {: #expanded data-toc-label="expanded()" }

Returns true if this node is currently expanded.

Returns `boolean`: True if expanded; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`expanded(boolean)` {: #expandedboolean data-toc-label="expanded(boolean)" }

Expands or collapses this node, rebuilding tree rows.

| Parameter | Type | Description |
| --- | --- | --- |
| `expanded` | `boolean` | Whether the node or panel is expanded. |

Returns `TreeViewNode`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`folder()` {: #folder data-toc-label="folder()" }

Returns true if this node is explicitly designated as a folder.

Returns `boolean`: True if designated as a folder node..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`folder(boolean)` {: #folderboolean data-toc-label="folder(boolean)" }

Designates whether this node represents a folder item.

| Parameter | Type | Description |
| --- | --- | --- |
| `isFolder` | `boolean` | Whether the node is designated as a folder. |

Returns `TreeViewNode`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`hasChildren()` {: #haschildren data-toc-label="hasChildren()" }

Returns true if this node has one or more children.

Returns `boolean`: True if child nodes are present..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`icon()` {: #icon data-toc-label="icon()" }

Returns the texture handle icon for this node.

Returns `TextureHandle`: The texture icon handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`icon(TextureHandle)` {: #icontexturehandle data-toc-label="icon(TextureHandle)" }

Sets the texture icon for this node.

| Parameter | Type | Description |
| --- | --- | --- |
| `icon` | `TextureHandle` | The icon identifier or handle. |

Returns `TreeViewNode`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`iconDraw()` {: #icondraw data-toc-label="iconDraw()" }

Returns the custom icon draw callback.

Returns `IconDraw`: The custom icon draw callback..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`iconDraw(IconDraw)` {: #icondrawicondraw data-toc-label="iconDraw(IconDraw)" }

Sets a custom icon draw callback.

| Parameter | Type | Description |
| --- | --- | --- |
| `iconDraw` | `IconDraw` | The custom icon draw callback. |

Returns `TreeViewNode`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`iconSuppressed()` {: #iconsuppressed data-toc-label="iconSuppressed()" }

Returns true if icon rendering is explicitly suppressed for this node.

Returns `boolean`: True if icon display is suppressed..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`insertChild(int, TreeViewNode)` {: #insertchildint-treeviewnode data-toc-label="insertChild(int, TreeViewNode)" }

Inserts a child node at the given index.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | The index position. |
| `child` | `TreeViewNode` | The child node instance. |

Returns `TreeViewNode`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`isAncestor(TreeViewNode)` {: #isancestortreeviewnode data-toc-label="isAncestor(TreeViewNode)" }

Returns true if this node is an ancestor of the candidate node.

| Parameter | Type | Description |
| --- | --- | --- |
| `candidate` | `TreeViewNode` | The candidate tree node. |

Returns `boolean`: True if this node is an ancestor of the candidate..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`noIcon()` {: #noicon data-toc-label="noIcon()" }

Explicitly suppresses icon rendering on this node.

Returns `TreeViewNode`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`owner()` {: #owner data-toc-label="owner()" }

Returns the [TreeView](<../../../widgets/navigation/tree-view.md>) that owns this node.

Returns `TreeView`: The owning [TreeView](<../../../widgets/navigation/tree-view.md>) container..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`parent()` {: #parent data-toc-label="parent()" }

Returns the parent node in the hierarchy, or null if root.

Returns `TreeViewNode`: The parent tree node, or null if root..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`removeChild(TreeViewNode)` {: #removechildtreeviewnode data-toc-label="removeChild(TreeViewNode)" }

Removes the specified child node from this node.

| Parameter | Type | Description |
| --- | --- | --- |
| `child` | `TreeViewNode` | The child node instance. |

Returns `TreeViewNode`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`richText()` {: #richtext data-toc-label="richText()" }

Returns the [RichText](<../../text/rich-text.md>) representation of this node label.

Returns `RichText`: The rich text representation..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`richText(RichText)` {: #richtextrichtext data-toc-label="richText(RichText)" }

Sets the [RichText](<../../text/rich-text.md>) label of this node.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `RichText` | The rich text label. |

Returns `TreeViewNode`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`rowButton()` {: #rowbutton data-toc-label="rowButton()" }

Returns the button widget rendering this node row, or null if not currently visible.

Returns `Button`: The button widget rendering the row, or null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`selectable()` {: #selectable data-toc-label="selectable()" }

Returns true if this node can be selected by user interaction.

Returns `boolean`: True if selection is enabled..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`selectable(boolean)` {: #selectableboolean data-toc-label="selectable(boolean)" }

Sets whether this node can be selected.

| Parameter | Type | Description |
| --- | --- | --- |
| `selectable` | `boolean` | Whether selection is allowed. |

Returns `TreeViewNode`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`silentExpanded(boolean)` {: #silentexpandedboolean data-toc-label="silentExpanded(boolean)" }

Sets the expanded state without triggering row rebuilds.

| Parameter | Type | Description |
| --- | --- | --- |
| `expanded` | `boolean` | Whether the node or panel is expanded. |

Returns `TreeViewNode`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`text()` {: #text data-toc-label="text()" }

Returns the plain text representation of this node label.

Returns `String`: The plain text label string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`text(String)` {: #textstring data-toc-label="text(String)" }

Sets the plain text label of this node.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | The text label. |

Returns `TreeViewNode`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`toggleExpanded()` {: #toggleexpanded data-toc-label="toggleExpanded()" }

Toggles the expanded state of this node.

Returns `TreeViewNode`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`userData()` {: #userdata data-toc-label="userData()" }

Returns the arbitrary user data object attached to this node.

Returns `Object`: The attached user data object..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`userData(Class)` {: #userdataclass data-toc-label="userData(Class)" }

Returns typed user data attached to this node, or null if cast fails.

| Parameter | Type | Description |
| --- | --- | --- |
| `type` | `Class` | The type or category string. |

Returns `Object`: The typed user data, or null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`userData(Object)` {: #userdataobject data-toc-label="userData(Object)" }

Attaches arbitrary user data to this node.

| Parameter | Type | Description |
| --- | --- | --- |
| `userData` | `Object` | The arbitrary user data object. |

Returns `TreeViewNode`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`value()` {: #value data-toc-label="value()" }

Returns the semantic identifier string value of this node.

Returns `String`: The semantic identifier value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TreeViewNode::</span>`value(String)` {: #valuestring data-toc-label="value(String)" }

Sets the semantic identifier string value of this node.

| Parameter | Type | Description |
| --- | --- | --- |
| `value` | `String` | The semantic value. |

Returns `TreeViewNode`: This instance for chaining..

</div>

