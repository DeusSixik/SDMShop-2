---
title: "NodeBuilder"
slug: "node-builder"
category: "Widgets/Graph"
locale: "en"
order: 0
---

# NodeBuilder

Fluent builder for assembling modular node items from headers, ports, editors, widgets, and custom modules.

## Overview

Use `NodeBuilder` when a node should have a reusable modular layout. Builder call order becomes top-to-bottom module order. Add flow and data ports with rows, provide default-value editors for inputs, and finish with `build()` before adding the item to a `NodeGraph`.

## Build a modular node

```java
NodeGraphItem node = NodeBuilder.node("spawn")
        .header("Create NPC")
        .flowRow("in", "out")
        .input("name", "Name", "string", nameEditor)
        .output("entity", "Entity", "entity")
        .build();
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`node(String)` {: #nodestring data-toc-label="node(String)" }

Creates a builder for a new modular node with the given item id.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique id assigned to the new node item. |

Returns `NodeBuilder`: New builder for the requested node id..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`at(float, float)` {: #atfloat-float data-toc-label="at(float, float)" }

Sets the node's initial position in graph world coordinates.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | Initial horizontal position in graph world coordinates. |
| `y` | `float` | Initial vertical position in graph world coordinates. |

Returns `NodeBuilder`: This builder instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`type(String)` {: #typestring data-toc-label="type(String)" }

Sets the registered node type id used for restore and palette operations.

| Parameter | Type | Description |
| --- | --- | --- |
| `typeId` | `String` | Registered node type id used to identify this node kind. |

Returns `NodeBuilder`: This builder instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`style(NodeStyle)` {: #stylenodestyle data-toc-label="style(NodeStyle)" }

Assigns the visual style used by the node.

| Parameter | Type | Description |
| --- | --- | --- |
| `style` | `NodeStyle` | Visual style to assign to the node. |

Returns `NodeBuilder`: This builder instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`color(int)` {: #colorint data-toc-label="color(int)" }

Sets the node header color using an ARGB value.

| Parameter | Type | Description |
| --- | --- | --- |
| `argb` | `int` | Header color in 0xAARRGGBB format. |

Returns `NodeBuilder`: This builder instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`header(String)` {: #headerstring data-toc-label="header(String)" }

Adds a title bar to the node.

| Parameter | Type | Description |
| --- | --- | --- |
| `title` | `String` | [Text](<../display/text.md>) displayed in the node header. |

Returns `NodeBuilder`: This builder instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`header(String, Consumer)` {: #headerstring-consumer data-toc-label="header(String, Consumer)" }

Adds a title bar and configures its icon or collapse behavior before adding it.

| Parameter | Type | Description |
| --- | --- | --- |
| `title` | `String` | [Text](<../display/text.md>) displayed in the node header. |
| `configure` | `Consumer` | Optional callback to configure the header before it is added. |

Returns `NodeBuilder`: This builder instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`flowRow(String, String)` {: #flowrowstring-string data-toc-label="flowRow(String, String)" }

Adds an execution-flow row with optional input and output port ids.

| Parameter | Type | Description |
| --- | --- | --- |
| `inPortId` | `String` | Input port id, or null to omit the input port. |
| `outPortId` | `String` | Output port id, or null to omit the output port. |

Returns `NodeBuilder`: This builder instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`input(String, String, String)` {: #inputstring-string-string data-toc-label="input(String, String, String)" }

Adds a data input row without an inline default-value editor.

| Parameter | Type | Description |
| --- | --- | --- |
| `portId` | `String` | Unique port id within the node. |
| `label` | `String` | Visible label for the port row. |
| `type` | `String` | Port data type id used for compatibility and presentation. |

Returns `NodeBuilder`: This builder instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`input(String, String, String, Widget)` {: #inputstring-string-string-widget data-toc-label="input(String, String, String, Widget)" }

Adds a data input row with a widget shown while the input is unconnected.

| Parameter | Type | Description |
| --- | --- | --- |
| `portId` | `String` | Unique port id within the node. |
| `label` | `String` | Visible label for the port row. |
| `type` | `String` | Port data type id used for compatibility and presentation. |
| `editor` | `Widget` | Optional inline widget shown while the input is unconnected. |

Returns `NodeBuilder`: This builder instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`input(String, String, String, ValueEditor)` {: #inputstring-string-string-valueeditor data-toc-label="input(String, String, String, ValueEditor)" }

Adds a data input row backed by a typed default-value editor.

| Parameter | Type | Description |
| --- | --- | --- |
| `portId` | `String` | Unique port id within the node. |
| `label` | `String` | Visible label for the port row. |
| `type` | `String` | Port data type id used for compatibility and presentation. |
| `editor` | `ValueEditor` | Typed editor that supplies the input's default value. |

Returns `NodeBuilder`: This builder instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`requiredInput(String, String, String)` {: #requiredinputstring-string-string data-toc-label="requiredInput(String, String, String)" }

Adds a required data input without a default value.

| Parameter | Type | Description |
| --- | --- | --- |
| `portId` | `String` | Unique port id within the node. |
| `label` | `String` | Visible label for the port row. |
| `type` | `String` | Port data type id used for compatibility and presentation. |

Returns `NodeBuilder`: This builder instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`output(String, String, String)` {: #outputstring-string-string data-toc-label="output(String, String, String)" }

Adds a data output row.

| Parameter | Type | Description |
| --- | --- | --- |
| `portId` | `String` | Unique port id within the node. |
| `label` | `String` | Visible label for the port row. |
| `type` | `String` | Port data type id used for compatibility and presentation. |

Returns `NodeBuilder`: This builder instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`row(String, String, String, String, String, String)` {: #rowstring-string-string-string-string-string data-toc-label="row(String, String, String, String, String, String)" }

Adds an input and output pair on the same row.

| Parameter | Type | Description |
| --- | --- | --- |
| `inPortId` | `String` | Input port id, or null to omit the input port. |
| `inLabel` | `String` | Visible label for the input port. |
| `inType` | `String` | Port data type id used for compatibility and presentation. |
| `outPortId` | `String` | Output port id, or null to omit the output port. |
| `outLabel` | `String` | Visible label for the output port. |
| `outType` | `String` | Port data type id used for compatibility and presentation. |

Returns `NodeBuilder`: This builder instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`widget(String, Widget)` {: #widgetstring-widget data-toc-label="widget(String, Widget)" }

Adds a full-width module row containing a widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Id unique among modules in the node. |
| `widget` | `Widget` | [Widget](<../widget.md>) embedded in the module row. |

Returns `NodeBuilder`: This builder instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`separator()` {: #separator data-toc-label="separator()" }

Adds a visual divider between node sections.

Returns `NodeBuilder`: This builder instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`module(NodeModule)` {: #modulenodemodule data-toc-label="module(NodeModule)" }

Adds a custom node module.

| Parameter | Type | Description |
| --- | --- | --- |
| `module` | `NodeModule` | Custom module to append to the node. |

Returns `NodeBuilder`: This builder instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`collapsed(boolean)` {: #collapsedboolean data-toc-label="collapsed(boolean)" }

Sets the node's initial collapsed state.

| Parameter | Type | Description |
| --- | --- | --- |
| `collapsed` | `boolean` | Whether the node starts collapsed. |

Returns `NodeBuilder`: This builder instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeBuilder::</span>`build()` {: #build data-toc-label="build()" }

Returns the completed node item.

Returns `NodeGraphItem`: The completed [NodeGraphItem](<../../core/widgets/graph/node-graph-item.md>)..

</div>

