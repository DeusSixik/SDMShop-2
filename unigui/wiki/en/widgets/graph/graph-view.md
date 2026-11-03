---
title: "GraphView"
slug: "graph-view"
category: "Widgets/Graph"
locale: "en"
order: 0
---

# GraphView

Static or interactive diagram graph widget displaying connected nodes, edges, labels, and tooltips with custom renderers.

## Overview

Static or interactive diagram graph widget displaying connected nodes, edges, labels, and tooltips with custom renderers. Provides extensive layout configuration and interaction capabilities.

## Basic Usage

```java
GraphView graph = new GraphView();
graph.addNode("a", 0.2f, 0.5f);
graph.addNode("b", 0.8f, 0.5f);
graph.addEdge("a", "b");
```

## Related links

- [NodeGraph](<node-graph.md>): Interactive 2D node graph canvas supporting movable nodes, pin connections, pan, zoom, grid snapping, and selection.

## API

### API

| Method | Description |
| --- | --- |
| [`GraphView()`](#graphview) | Constructs a GraphView diagram widget with default styles. |
| [`addEdge(String, String)`](#addedgestring-string) | Adds a directed edge connection between two node IDs. |
| [`addNode(String, float, float)`](#addnodestring-float-float) | Adds a node with ID and normalized (0..1) or absolute coordinates. |
| [`clearGraph()`](#cleargraph) | Clears all nodes and edges from the diagram. |
| [`edgeColor()`](#edgecolor) | Returns the connection edge line color. |
| [`hoveredNodeColor()`](#hoverednodecolor) | Returns the hover fill color for nodes. |
| [`labelColor()`](#labelcolor) | Returns the node label text color. |
| [`nodeColor()`](#nodecolor) | Returns the default node circle fill color. |
| [`nodeLabelPlacement(NodeLabelPlacement)`](#nodelabelplacementnodelabelplacement) | Sets relative placement of text labels around node points. |
| [`nodeLabelProvider(Function)`](#nodelabelproviderfunction) | Configures a function returning the text label for a given node point. |
| [`nodeLabelRenderer(GraphNodeLabelRenderer)`](#nodelabelrenderergraphnodelabelrenderer) | Sets a custom renderer callback for drawing node labels. |
| [`nodeLabelsVisible(boolean)`](#nodelabelsvisibleboolean) | Sets whether text labels are rendered alongside node points. |
| [`nodeRenderer(GraphNodeRenderer)`](#noderenderergraphnoderenderer) | Sets a custom renderer callback for drawing node points. |
| [`nodeTooltipProvider(Function)`](#nodetooltipproviderfunction) | Configures a function returning tooltip text for hovered nodes. |
| [`nodeTooltipRenderer(GraphNodeTooltipRenderer)`](#nodetooltiprenderergraphnodetooltiprenderer) | Sets a custom renderer callback for drawing node hover tooltips. |
| [`onNodeClick(EventListener)`](#onnodeclickeventlistener) | Registers a listener called when a node point is clicked. |
| [`preferredHeight()`](#preferredheight) | Returns the preferred height of this graph view. |
| [`preferredHeight(float)`](#preferredheightfloat) | Sets the preferred height of this graph view. |
| [`preferredSize(float, float)`](#preferredsizefloat-float) | Sets both preferred width and height dimensions. |
| [`preferredWidth()`](#preferredwidth) | Returns the preferred width of this graph view. |
| [`preferredWidth(float)`](#preferredwidthfloat) | Sets the preferred width of this graph view. |
| [`tooltipBackground()`](#tooltipbackground) | Returns the background fill color of node tooltip popups. |
| [`tooltipBorder()`](#tooltipborder) | Returns the border outline color of node tooltip popups. |

<div class="api-member-card" markdown="1">

#### `GraphView()` {: #graphview data-toc-label="GraphView()" }

Constructs a GraphView diagram widget with default styles.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`addEdge(String, String)` {: #addedgestring-string data-toc-label="addEdge(String, String)" }

Adds a directed edge connection between two node IDs.

| Parameter | Type | Description |
| --- | --- | --- |
| `from` | `String` | The source node identifier. |
| `to` | `String` | The destination node identifier. |

Returns `GraphView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`addNode(String, float, float)` {: #addnodestring-float-float data-toc-label="addNode(String, float, float)" }

Adds a node with ID and normalized (0..1) or absolute coordinates.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | The node identifier. |
| `x` | `float` | The X coordinate position. |
| `y` | `float` | The Y coordinate position. |

Returns `GraphView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`clearGraph()` {: #cleargraph data-toc-label="clearGraph()" }

Clears all nodes and edges from the diagram.

Returns `GraphView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`edgeColor()` {: #edgecolor data-toc-label="edgeColor()" }

Returns the connection edge line color.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`hoveredNodeColor()` {: #hoverednodecolor data-toc-label="hoveredNodeColor()" }

Returns the hover fill color for nodes.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`labelColor()` {: #labelcolor data-toc-label="labelColor()" }

Returns the node label text color.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`nodeColor()` {: #nodecolor data-toc-label="nodeColor()" }

Returns the default node circle fill color.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`nodeLabelPlacement(NodeLabelPlacement)` {: #nodelabelplacementnodelabelplacement data-toc-label="nodeLabelPlacement(NodeLabelPlacement)" }

Sets relative placement of text labels around node points.

| Parameter | Type | Description |
| --- | --- | --- |
| `nodeLabelPlacement` | `NodeLabelPlacement` | The relative label placement around nodes. |

Returns `GraphView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`nodeLabelProvider(Function)` {: #nodelabelproviderfunction data-toc-label="nodeLabelProvider(Function)" }

Configures a function returning the text label for a given node point.

| Parameter | Type | Description |
| --- | --- | --- |
| `nodeLabelProvider` | `Function` | The node label provider function. |

Returns `GraphView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`nodeLabelRenderer(GraphNodeLabelRenderer)` {: #nodelabelrenderergraphnodelabelrenderer data-toc-label="nodeLabelRenderer(GraphNodeLabelRenderer)" }

Sets a custom renderer callback for drawing node labels.

| Parameter | Type | Description |
| --- | --- | --- |
| `nodeLabelRenderer` | `GraphNodeLabelRenderer` | The custom node label renderer callback. |

Returns `GraphView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`nodeLabelsVisible(boolean)` {: #nodelabelsvisibleboolean data-toc-label="nodeLabelsVisible(boolean)" }

Sets whether text labels are rendered alongside node points.

| Parameter | Type | Description |
| --- | --- | --- |
| `nodeLabelsVisible` | `boolean` | Whether node text labels are rendered. |

Returns `GraphView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`nodeRenderer(GraphNodeRenderer)` {: #noderenderergraphnoderenderer data-toc-label="nodeRenderer(GraphNodeRenderer)" }

Sets a custom renderer callback for drawing node points.

| Parameter | Type | Description |
| --- | --- | --- |
| `nodeRenderer` | `GraphNodeRenderer` | The custom node renderer callback. |

Returns `GraphView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`nodeTooltipProvider(Function)` {: #nodetooltipproviderfunction data-toc-label="nodeTooltipProvider(Function)" }

Configures a function returning tooltip text for hovered nodes.

| Parameter | Type | Description |
| --- | --- | --- |
| `nodeTooltipProvider` | `Function` | The node tooltip provider function. |

Returns `GraphView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`nodeTooltipRenderer(GraphNodeTooltipRenderer)` {: #nodetooltiprenderergraphnodetooltiprenderer data-toc-label="nodeTooltipRenderer(GraphNodeTooltipRenderer)" }

Sets a custom renderer callback for drawing node hover tooltips.

| Parameter | Type | Description |
| --- | --- | --- |
| `nodeTooltipRenderer` | `GraphNodeTooltipRenderer` | The custom node tooltip renderer callback. |

Returns `GraphView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`onNodeClick(EventListener)` {: #onnodeclickeventlistener data-toc-label="onNodeClick(EventListener)" }

Registers a listener called when a node point is clicked.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | The event listener callback. |

Returns `EventSubscription`: The event subscription handle..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`preferredHeight()` {: #preferredheight data-toc-label="preferredHeight()" }

Returns the preferred height of this graph view.

Returns `float`: The preferred height in pixels..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`preferredHeight(float)` {: #preferredheightfloat data-toc-label="preferredHeight(float)" }

Sets the preferred height of this graph view.

| Parameter | Type | Description |
| --- | --- | --- |
| `preferredHeight` | `float` | The preferred height in pixels. |

Returns `GraphView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`preferredSize(float, float)` {: #preferredsizefloat-float data-toc-label="preferredSize(float, float)" }

Sets both preferred width and height dimensions.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | The preferred width in pixels. |
| `height` | `float` | The preferred height in pixels. |

Returns `GraphView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`preferredWidth()` {: #preferredwidth data-toc-label="preferredWidth()" }

Returns the preferred width of this graph view.

Returns `float`: The preferred width in pixels..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`preferredWidth(float)` {: #preferredwidthfloat data-toc-label="preferredWidth(float)" }

Sets the preferred width of this graph view.

| Parameter | Type | Description |
| --- | --- | --- |
| `preferredWidth` | `float` | The preferred width in pixels. |

Returns `GraphView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`tooltipBackground()` {: #tooltipbackground data-toc-label="tooltipBackground()" }

Returns the background fill color of node tooltip popups.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`tooltipBorder()` {: #tooltipborder data-toc-label="tooltipBorder()" }

Returns the border outline color of node tooltip popups.

Returns `MutableColor`: The mutable color instance..

</div>

### Graph Data

| Method | Description |
| --- | --- |
| [`removeNode(String)`](#removenodestring) | Removes a node and all incident edges. |
| [`nodeCount()`](#nodecount) | Returns the number of nodes currently in the graph. |
| [`edgeCount()`](#edgecount) | Returns the number of edges, including edges waiting for missing endpoints. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`removeNode(String)` {: #removenodestring data-toc-label="removeNode(String)" }

Removes a node and all incident edges.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Node identifier to remove; null or unknown identifiers are ignored. |

Returns `GraphView`: This graph view for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`nodeCount()` {: #nodecount data-toc-label="nodeCount()" }

Returns the number of nodes currently in the graph.

Returns `int`: The current number of nodes..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`edgeCount()` {: #edgecount data-toc-label="edgeCount()" }

Returns the number of edges, including edges waiting for missing endpoints.

Returns `int`: The current number of stored edges..

</div>

### Appearance

| Method | Description |
| --- | --- |
| [`nodeBorderColor()`](#nodebordercolor) | Returns the mutable color used to outline graph nodes. |
| [`hoveredEdgeColor()`](#hoverededgecolor) | Returns the mutable color used for edges connected to the hovered node. |
| [`directed(boolean)`](#directedboolean) | Enables or disables arrowheads at edge targets. |
| [`directed()`](#directed) | Returns whether edges are rendered as directed connections. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`nodeBorderColor()` {: #nodebordercolor data-toc-label="nodeBorderColor()" }

Returns the mutable color used to outline graph nodes.

Returns `MutableColor`: The mutable RGBA node-border color..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`hoveredEdgeColor()` {: #hoverededgecolor data-toc-label="hoveredEdgeColor()" }

Returns the mutable color used for edges connected to the hovered node.

Returns `MutableColor`: The mutable RGBA hovered-edge color..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`directed(boolean)` {: #directedboolean data-toc-label="directed(boolean)" }

Enables or disables arrowheads at edge targets.

| Parameter | Type | Description |
| --- | --- | --- |
| `directed` | `boolean` | True to draw an arrowhead at each target node. |

Returns `GraphView`: This graph view for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">GraphView::</span>`directed()` {: #directed data-toc-label="directed()" }

Returns whether edges are rendered as directed connections.

Returns `boolean`: True when edge target arrowheads are enabled..

</div>

