---
title: "NodeGraphPort"
slug: "node-graph-port"
category: "Core/Widgets/Graph"
locale: "en"
order: 0
---

# NodeGraphPort

Pin/port descriptor on a [NodeGraphItem](<node-graph-item.md>) connecting lines to other nodes.

## Overview

Pin/port descriptor on a [NodeGraphItem](<node-graph-item.md>) connecting lines to other nodes. Provides extensive layout configuration and interaction capabilities.

## API

<div class="api-member-card" markdown="1">

### `NodeGraphPort(String, NodeGraphPortKind, NodeGraphPortSide, float)` {: #nodegraphportstring-nodegraphportkind-nodegraphportside-float data-toc-label="NodeGraphPort(String, NodeGraphPortKind, NodeGraphPortSide, float)" }

Constructs a NodeGraphPort with ID, direction kind, visual edge side, and pixel offset.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | The unique identifier. |
| `kind` | `NodeGraphPortKind` | The port connection direction kind. |
| `side` | `NodeGraphPortSide` | The port edge side. |
| `offset` | `float` | The port offset along the node edge in pixels. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`enabled()` {: #enabled data-toc-label="enabled()" }

Returns true if this port is interactive and can accept or initiate links.

Returns `boolean`: True if enabled; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`enabled(boolean)` {: #enabledboolean data-toc-label="enabled(boolean)" }

Sets whether this port is interactive.

| Parameter | Type | Description |
| --- | --- | --- |
| `enabled` | `boolean` | Whether the feature or item is enabled. |

Returns `NodeGraphPort`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`id()` {: #id data-toc-label="id()" }

Returns the port identifier unique within its owning item.

Returns `String`: The unique identifier string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`itemId()` {: #itemid data-toc-label="itemId()" }

Returns the ID of the item owning this port.

Returns `String`: The item identifier string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`kind()` {: #kind data-toc-label="kind()" }

Returns the connection direction kind.

Returns `NodeGraphPortKind`: The menu item kind..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`kind(NodeGraphPortKind)` {: #kindnodegraphportkind data-toc-label="kind(NodeGraphPortKind)" }

Sets the connection direction kind.

| Parameter | Type | Description |
| --- | --- | --- |
| `kind` | `NodeGraphPortKind` | The port connection direction kind. |

Returns `NodeGraphPort`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`offset()` {: #offset data-toc-label="offset()" }

Returns the offset along the node edge in pixels.

Returns `float`: The port offset in pixels along the node edge..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`offset(float)` {: #offsetfloat data-toc-label="offset(float)" }

Sets the offset along the node edge in pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `offset` | `float` | The port offset along the node edge in pixels. |

Returns `NodeGraphPort`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`ref()` {: #ref data-toc-label="ref()" }

Returns the compound [NodeGraphPortRef](<node-graph-port-ref.md>) pointing to this port.

Returns `NodeGraphPortRef`: The compound port reference..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`side()` {: #side data-toc-label="side()" }

Returns the visual edge side where the port is rendered.

Returns `NodeGraphPortSide`: The port visual edge side..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`side(NodeGraphPortSide)` {: #sidenodegraphportside data-toc-label="side(NodeGraphPortSide)" }

Sets the visual edge side where the port is rendered.

| Parameter | Type | Description |
| --- | --- | --- |
| `side` | `NodeGraphPortSide` | The port edge side. |

Returns `NodeGraphPort`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`type()` {: #type data-toc-label="type()" }

Returns the data type or compatibility label for connection validation.

Returns `String`: The type or compatibility category string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`type(String)` {: #typestring data-toc-label="type(String)" }

Sets the data type or compatibility label.

| Parameter | Type | Description |
| --- | --- | --- |
| `type` | `String` | The type or category string. |

Returns `NodeGraphPort`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`visible()` {: #visible data-toc-label="visible()" }

Returns true if this port is visible.

Returns `boolean`: True if visible; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphPort::</span>`visible(boolean)` {: #visibleboolean data-toc-label="visible(boolean)" }

Sets whether this port is visible.

| Parameter | Type | Description |
| --- | --- | --- |
| `visible` | `boolean` | Whether the item is visible. |

Returns `NodeGraphPort`: This instance for chaining..

</div>

