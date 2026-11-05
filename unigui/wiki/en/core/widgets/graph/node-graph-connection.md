---
title: "NodeGraphConnection"
slug: "node-graph-connection"
category: "Core/Widgets/Graph"
locale: "en"
order: 0
---

# NodeGraphConnection

Represents a directional or bidirectional connection between two ports in a [NodeGraph](<../../../widgets/graph/node-graph.md>).

## Overview

Represents a directional or bidirectional connection between two ports in a [NodeGraph](<../../../widgets/graph/node-graph.md>). Provides extensive layout configuration and interaction capabilities.

## API

<div class="api-member-card" markdown="1">

### `NodeGraphConnection(String, NodeGraphPortRef, NodeGraphPortRef)` {: #nodegraphconnectionstring-nodegraphportref-nodegraphportref data-toc-label="NodeGraphConnection(String, NodeGraphPortRef, NodeGraphPortRef)" }

Constructs a connection between ports specified by port references.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | The unique identifier. |
| `from` | `NodeGraphPortRef` | The source port reference. |
| `to` | `NodeGraphPortRef` | The destination port reference. |

</div>

<div class="api-member-card" markdown="1">

### `NodeGraphConnection(String, String, String, String, String)` {: #nodegraphconnectionstring-string-string-string-string data-toc-label="NodeGraphConnection(String, String, String, String, String)" }

Constructs a connection between ports by item and port IDs.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | The unique identifier. |
| `fromItemId` | `String` | The source item identifier. |
| `fromPortId` | `String` | The source port identifier. |
| `toItemId` | `String` | The destination item identifier. |
| `toPortId` | `String` | The destination port identifier. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`enabled()` {: #enabled data-toc-label="enabled()" }

Returns true if this connection is active and enabled.

Returns `boolean`: True if enabled; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`enabled(boolean)` {: #enabledboolean data-toc-label="enabled(boolean)" }

Sets whether this connection is active and enabled.

| Parameter | Type | Description |
| --- | --- | --- |
| `enabled` | `boolean` | Whether the feature or item is enabled. |

Returns `NodeGraphConnection`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`from()` {: #from data-toc-label="from()" }

Returns the source port reference.

Returns `NodeGraphPortRef`: The source port reference..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`fromItemId()` {: #fromitemid data-toc-label="fromItemId()" }

Returns the source node item ID.

Returns `String`: The item identifier string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`fromPortId()` {: #fromportid data-toc-label="fromPortId()" }

Returns the source port ID.

Returns `String`: The port identifier string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`id()` {: #id data-toc-label="id()" }

Returns the unique identifier of this connection.

Returns `String`: The unique identifier string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`selected()` {: #selected data-toc-label="selected()" }

Returns true if this connection is currently selected.

Returns `boolean`: True if currently selected; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`to()` {: #to data-toc-label="to()" }

Returns the destination port reference.

Returns `NodeGraphPortRef`: The destination port reference..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`toItemId()` {: #toitemid data-toc-label="toItemId()" }

Returns the destination node item ID.

Returns `String`: The item identifier string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`toPortId()` {: #toportid data-toc-label="toPortId()" }

Returns the destination port ID.

Returns `String`: The port identifier string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`type()` {: #type data-toc-label="type()" }

Returns the connection data type or compatibility category string.

Returns `String`: The type or compatibility category string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraphConnection::</span>`type(String)` {: #typestring data-toc-label="type(String)" }

Sets the connection data type or compatibility category string.

| Parameter | Type | Description |
| --- | --- | --- |
| `type` | `String` | The type or category string. |

Returns `NodeGraphConnection`: This instance for chaining..

</div>

