---
title: "NodeGraphItem"
slug: "node-graph-item"
category: "Core/Widgets/Graph"
locale: "en"
order: 0
---

# NodeGraphItem

Node item container placed in a [NodeGraph](<../../../widgets/graph/node-graph.md>) canvas hosting a content widget and pin ports.

## Overview

Node item container placed in a [NodeGraph](<../../../widgets/graph/node-graph.md>) canvas hosting a content widget and pin ports. Provides extensive layout configuration and interaction capabilities.

## API

<div class="api-member-card" markdown="1">

### `NodeGraphItem(String, Widget)` {: #nodegraphitemstring-widget data-toc-label="NodeGraphItem(String, Widget)" }

Constructs a NodeGraphItem with unique ID and content widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | The unique identifier. |
| `content` | `Widget` | The content widget. |

</div>

<div class="api-member-card" markdown="1">

### `NodeGraphItem(String, Widget, float, float)` {: #nodegraphitemstring-widget-float-float data-toc-label="NodeGraphItem(String, Widget, float, float)" }

Constructs a NodeGraphItem with unique ID, content widget, and initial coordinates.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | The unique identifier. |
| `content` | `Widget` | The content widget. |
| `x` | `float` | The X coordinate position. |
| `y` | `float` | The Y coordinate position. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`addPort(NodeGraphPort)` {: #addportnodegraphport data-toc-label="addPort(NodeGraphPort)" }

Attaches an existing port instance to this node.

| Parameter | Type | Description |
| --- | --- | --- |
| `port` | `NodeGraphPort` | The port parameter. |

Returns `NodeGraphItem`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`addPort(String, NodeGraphPortKind, NodeGraphPortSide, float)` {: #addportstring-nodegraphportkind-nodegraphportside-float data-toc-label="addPort(String, NodeGraphPortKind, NodeGraphPortSide, float)" }

Creates and attaches a port with specified ID, direction kind, side, and offset.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | The unique identifier. |
| `kind` | `NodeGraphPortKind` | The port connection direction kind. |
| `side` | `NodeGraphPortSide` | The port edge side. |
| `offset` | `float` | The port offset along the node edge in pixels. |

Returns `NodeGraphPort`: The created port instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`arrangedHeight()` {: #arrangedheight data-toc-label="arrangedHeight()" }

Returns the actual arranged layout height.

Returns `float`: The arranged height in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`arrangedWidth()` {: #arrangedwidth data-toc-label="arrangedWidth()" }

Returns the actual arranged layout width.

Returns `float`: The arranged width in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`autoHeight()` {: #autoheight data-toc-label="autoHeight()" }

Returns true if height is determined automatically by content layout.

Returns `boolean`: True if height is automatically sized..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`autoSize()` {: #autosize data-toc-label="autoSize()" }

Resets width and height to automatic sizing based on content.

Returns `NodeGraphItem`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`autoWidth()` {: #autowidth data-toc-label="autoWidth()" }

Returns true if width is determined automatically by content layout.

Returns `boolean`: True if width is automatically sized..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`content()` {: #content data-toc-label="content()" }

Returns the content widget hosted inside this node.

Returns `Widget`: The content widget..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`height()` {: #height data-toc-label="height()" }

Returns the explicit configured height of this node, or <= 0 for auto-sizing.

Returns `float`: The height in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`id()` {: #id data-toc-label="id()" }

Returns the unique identifier of this node item.

Returns `String`: The unique identifier string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`movable()` {: #movable data-toc-label="movable()" }

Returns true if this node can be dragged to move across the canvas.

Returns `boolean`: True if dragging is enabled..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`movable(boolean)` {: #movableboolean data-toc-label="movable(boolean)" }

Sets whether this node can be dragged to move.

| Parameter | Type | Description |
| --- | --- | --- |
| `movable` | `boolean` | Whether dragging and movement are allowed. |

Returns `NodeGraphItem`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`port(String)` {: #portstring data-toc-label="port(String)" }

Returns the port with the specified ID, or null if not found.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | The unique identifier. |

Returns `NodeGraphPort`: The port instance, or null if not found..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`ports()` {: #ports data-toc-label="ports()" }

Returns an unmodifiable list of all ports attached to this node.

Returns `List`: An unmodifiable list of ports..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`position(float, float)` {: #positionfloat-float data-toc-label="position(float, float)" }

Sets the (X, Y) world position of this node on the canvas.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | The X coordinate position. |
| `y` | `float` | The Y coordinate position. |

Returns `NodeGraphItem`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`removePort(String)` {: #removeportstring data-toc-label="removePort(String)" }

Removes the port with the specified ID.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | The unique identifier. |

Returns `NodeGraphItem`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`resizable()` {: #resizable data-toc-label="resizable()" }

Returns true if this node displays resize handles and can be interactively resized.

Returns `boolean`: True if resizing is enabled..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`resizable(boolean)` {: #resizableboolean data-toc-label="resizable(boolean)" }

Sets whether this node can be resized by dragging corner handles.

| Parameter | Type | Description |
| --- | --- | --- |
| `resizable` | `boolean` | Whether resizing is allowed. |

Returns `NodeGraphItem`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`selectable()` {: #selectable data-toc-label="selectable()" }

Returns true if this node can be selected by clicks or lasso selection.

Returns `boolean`: True if selection is enabled..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`selectable(boolean)` {: #selectableboolean data-toc-label="selectable(boolean)" }

Sets whether this node can be selected.

| Parameter | Type | Description |
| --- | --- | --- |
| `selectable` | `boolean` | Whether selection is allowed. |

Returns `NodeGraphItem`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`selected()` {: #selected data-toc-label="selected()" }

Returns true if this node is currently selected.

Returns `boolean`: True if currently selected; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`size(float, float)` {: #sizefloat-float data-toc-label="size(float, float)" }

Sets explicit width and height dimensions for this node.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | The width in pixels. |
| `height` | `float` | The height in pixels. |

Returns `NodeGraphItem`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`visible()` {: #visible data-toc-label="visible()" }

Returns true if this node is visible.

Returns `boolean`: True if visible; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`visible(boolean)` {: #visibleboolean data-toc-label="visible(boolean)" }

Sets whether this node is visible.

| Parameter | Type | Description |
| --- | --- | --- |
| `visible` | `boolean` | Whether the item is visible. |

Returns `NodeGraphItem`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`width()` {: #width data-toc-label="width()" }

Returns the explicit configured width of this node, or <= 0 for auto-sizing.

Returns `float`: The width in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`x()` {: #x data-toc-label="x()" }

Returns the X position of this node in canvas world coordinates.

Returns `float`: The X coordinate position..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphItem::</span>`y()` {: #y data-toc-label="y()" }

Returns the Y position of this node in canvas world coordinates.

Returns `float`: The Y coordinate position..

</div>

