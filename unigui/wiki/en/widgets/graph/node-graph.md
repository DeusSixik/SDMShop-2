---
title: "NodeGraph"
slug: "node-graph"
category: "Widgets/Graph"
locale: "en"
order: 0
---

# NodeGraph

Interactive 2D node graph canvas supporting movable nodes, pin connections, pan, zoom, grid snapping, and selection.

## Overview

Interactive 2D node graph canvas supporting movable nodes, pin connections, pan, zoom, grid snapping, and selection. Provides extensive layout configuration and interaction capabilities.

## Basic Usage

```java
NodeGraph graph = new NodeGraph();
NodeGraphItem itemA = graph.addItem("nodeA", new Label("A"), 50, 50);
itemA.addPort("out", NodeGraphPortKind.OUTPUT, NodeGraphPortSide.RIGHT, 10);
NodeGraphItem itemB = graph.addItem("nodeB", new Label("B"), 200, 50);
itemB.addPort("in", NodeGraphPortKind.INPUT, NodeGraphPortSide.LEFT, 10);
graph.addConnection("c1", "nodeA", "out", "nodeB", "in");
```

## Related links

- [GraphView](<graph-view.md>): Static or interactive diagram graph widget displaying connected nodes, edges, labels, and tooltips with custom renderers.
- [NodeGraphConnection](<../../core/widgets/graph/node-graph-connection.md>): Represents a directional or bidirectional connection between two ports in a NodeGraph.
- [NodeGraphItem](<../../core/widgets/graph/node-graph-item.md>): Node item container placed in a NodeGraph canvas hosting a content widget and pin ports.

## API

<div class="api-member-card" markdown="1">

### `NodeGraph()` {: #nodegraph data-toc-label="NodeGraph()" }

Constructs a new NodeGraph instance with default viewport, grid, and themes.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`addConnection(NodeGraphConnection)` {: #addconnectionnodegraphconnection data-toc-label="addConnection(NodeGraphConnection)" }

Adds an existing connection instance to the graph.

| Parameter | Type | Description |
| --- | --- | --- |
| `connection` | `NodeGraphConnection` | The connection parameter. |

Returns `NodeGraphConnection`: The created connection instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`addConnection(String, NodeGraphPortRef, NodeGraphPortRef)` {: #addconnectionstring-nodegraphportref-nodegraphportref data-toc-label="addConnection(String, NodeGraphPortRef, NodeGraphPortRef)" }

Creates and adds a connection between port references.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | The connection identifier. |
| `from` | `NodeGraphPortRef` | The source port reference. |
| `to` | `NodeGraphPortRef` | The destination port reference. |

Returns `NodeGraphConnection`: The created connection instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`addConnection(String, String, String, String, String)` {: #addconnectionstring-string-string-string-string data-toc-label="addConnection(String, String, String, String, String)" }

Creates and adds a connection between specified item and port IDs.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | The connection identifier. |
| `fromItemId` | `String` | The source item identifier. |
| `fromPortId` | `String` | The source port identifier. |
| `toItemId` | `String` | The destination item identifier. |
| `toPortId` | `String` | The destination port identifier. |

Returns `NodeGraphConnection`: The created connection instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`addItem(NodeGraphItem)` {: #additemnodegraphitem data-toc-label="addItem(NodeGraphItem)" }

Adds an existing node item to the graph.

| Parameter | Type | Description |
| --- | --- | --- |
| `item` | `NodeGraphItem` | The target item. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`addItem(String, Widget, float, float)` {: #additemstring-widget-float-float data-toc-label="addItem(String, Widget, float, float)" }

Creates and places a new node item with the given ID, content widget, and canvas coordinates.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | The item identifier. |
| `content` | `Widget` | The content widget. |
| `x` | `float` | The X coordinate position. |
| `y` | `float` | The Y coordinate position. |

Returns `NodeGraphItem`: The created node item instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`backgroundColor()` {: #backgroundcolor data-toc-label="backgroundColor()" }

Returns the background fill color of the canvas.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`bringToFrontOnSelect()` {: #bringtofrontonselect data-toc-label="bringToFrontOnSelect()" }

Returns true if selecting an item moves it to the top of the z-order.

Returns `boolean`: True if enabled; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`bringToFrontOnSelect(boolean)` {: #bringtofrontonselectboolean data-toc-label="bringToFrontOnSelect(boolean)" }

Sets whether selecting an item moves it to the front.

| Parameter | Type | Description |
| --- | --- | --- |
| `bringToFrontOnSelect` | `boolean` | Whether the feature or item is enabled. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`clearConnectionSelection()` {: #clearconnectionselection data-toc-label="clearConnectionSelection()" }

Deselects all currently selected connections.

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`clearItems()` {: #clearitems data-toc-label="clearItems()" }

Removes all items and connections from the graph.

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`clearSelection()` {: #clearselection data-toc-label="clearSelection()" }

Deselects all currently selected items.

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`clippingEnabled()` {: #clippingenabled data-toc-label="clippingEnabled()" }

Returns true if canvas rendering is clipped to widget bounds.

Returns `boolean`: True if clipping is enabled..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`clippingEnabled(boolean)` {: #clippingenabledboolean data-toc-label="clippingEnabled(boolean)" }

Sets whether canvas rendering is clipped to widget bounds.

| Parameter | Type | Description |
| --- | --- | --- |
| `clippingEnabled` | `boolean` | Whether canvas rendering is clipped. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`connection(String)` {: #connectionstring data-toc-label="connection(String)" }

Returns the connection with the given ID, or null if not found.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | The connection identifier. |

Returns `NodeGraphConnection`: The connection instance, or null if not found..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`connectionColor()` {: #connectioncolor data-toc-label="connectionColor()" }

Returns default line color for connections.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`connectionDragging()` {: #connectiondragging data-toc-label="connectionDragging()" }

Returns true if a new connection is currently being dragged from a port.

Returns `boolean`: True if enabled; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`connectionPolicy()` {: #connectionpolicy data-toc-label="connectionPolicy()" }

Returns the active connection validation policy.

Returns `NodeGraphConnectionPolicy`: The active connection policy..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`connectionPolicy(NodeGraphConnectionPolicy)` {: #connectionpolicynodegraphconnectionpolicy data-toc-label="connectionPolicy(NodeGraphConnectionPolicy)" }

Sets the connection validation policy.

| Parameter | Type | Description |
| --- | --- | --- |
| `connectionPolicy` | `NodeGraphConnectionPolicy` | The connection validation policy. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`connectionPreviewColor()` {: #connectionpreviewcolor data-toc-label="connectionPreviewColor()" }

Returns preview line color for valid drag connections.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`connections()` {: #connections data-toc-label="connections()" }

Returns an unmodifiable list of all connections.

Returns `List`: An unmodifiable list of connections..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`consumeWheelWhileHovered()` {: #consumewheelwhilehovered data-toc-label="consumeWheelWhileHovered()" }

Returns true if mouse wheel events are consumed while hovering the canvas.

Returns `boolean`: True if enabled; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`consumeWheelWhileHovered(boolean)` {: #consumewheelwhilehoveredboolean data-toc-label="consumeWheelWhileHovered(boolean)" }

Sets whether mouse wheel events are consumed when hovered.

| Parameter | Type | Description |
| --- | --- | --- |
| `consumeWheelWhileHovered` | `boolean` | Whether the feature or item is enabled. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`draggingItemId()` {: #draggingitemid data-toc-label="draggingItemId()" }

Returns the ID of the item currently being dragged, or empty string if none.

Returns `String`: The ID of the dragged item, or empty string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`gridColor()` {: #gridcolor data-toc-label="gridColor()" }

Returns the minor grid lines color.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`gridSize()` {: #gridsize data-toc-label="gridSize()" }

Returns the grid cell step size in pixels.

Returns `float`: The grid cell size in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`gridSize(float)` {: #gridsizefloat data-toc-label="gridSize(float)" }

Sets the grid cell step size in pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `gridSize` | `float` | The grid cell size in pixels. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`hasConnection(NodeGraphPortRef, NodeGraphPortRef)` {: #hasconnectionnodegraphportref-nodegraphportref data-toc-label="hasConnection(NodeGraphPortRef, NodeGraphPortRef)" }

Returns true if a connection already exists between the given ports.

| Parameter | Type | Description |
| --- | --- | --- |
| `from` | `NodeGraphPortRef` | The source port reference. |
| `to` | `NodeGraphPortRef` | The destination port reference. |

Returns `boolean`: True if connection exists; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`hoveredConnectionId()` {: #hoveredconnectionid data-toc-label="hoveredConnectionId()" }

Returns the ID of the currently hovered connection, or empty string if none.

Returns `String`: The ID of the hovered connection, or empty string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`hoveredItemBorderColor()` {: #hovereditembordercolor data-toc-label="hoveredItemBorderColor()" }

Returns hover border color for node items.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`hoveredItemId()` {: #hovereditemid data-toc-label="hoveredItemId()" }

Returns the ID of the currently hovered node item, or empty string if none.

Returns `String`: The ID of the hovered item, or empty string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`hoveredPort()` {: #hoveredport data-toc-label="hoveredPort()" }

Returns reference to the currently hovered port, or null if none.

Returns `NodeGraphPortRef`: The hovered port reference, or null if none..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`hoveredPortColor()` {: #hoveredportcolor data-toc-label="hoveredPortColor()" }

Returns hover fill color for pin ports.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`invalidConnectionPreviewColor()` {: #invalidconnectionpreviewcolor data-toc-label="invalidConnectionPreviewColor()" }

Returns preview line color for invalid drag connections.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`item(String)` {: #itemstring data-toc-label="item(String)" }

Returns the item with the given ID, or null if not found.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | The item identifier. |

Returns `NodeGraphItem`: The node item instance, or null if not found..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`itemBorderColor()` {: #itembordercolor data-toc-label="itemBorderColor()" }

Returns default border color for node items.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`itemContentPadding()` {: #itemcontentpadding data-toc-label="itemContentPadding()" }

Returns inner padding around item content widgets.

Returns `float`: The content padding in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`itemContentPadding(float)` {: #itemcontentpaddingfloat data-toc-label="itemContentPadding(float)" }

Sets inner padding around item content widgets.

| Parameter | Type | Description |
| --- | --- | --- |
| `padding` | `float` | The padding in pixels. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`itemDraggingEnabled()` {: #itemdraggingenabled data-toc-label="itemDraggingEnabled()" }

Returns true if item dragging is enabled.

Returns `boolean`: True if enabled; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`itemDraggingEnabled(boolean)` {: #itemdraggingenabledboolean data-toc-label="itemDraggingEnabled(boolean)" }

Sets whether item dragging is enabled.

| Parameter | Type | Description |
| --- | --- | --- |
| `itemDraggingEnabled` | `boolean` | Whether the feature or item is enabled. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`items()` {: #items data-toc-label="items()" }

Returns an unmodifiable list of all node items.

Returns `List`: An unmodifiable list of node items..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`keyboardEditingEnabled()` {: #keyboardeditingenabled data-toc-label="keyboardEditingEnabled()" }

Returns true if keyboard shortcuts (Delete, arrows) are enabled.

Returns `boolean`: True if enabled; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`keyboardEditingEnabled(boolean)` {: #keyboardeditingenabledboolean data-toc-label="keyboardEditingEnabled(boolean)" }

Sets whether keyboard editing shortcuts are enabled.

| Parameter | Type | Description |
| --- | --- | --- |
| `keyboardEditingEnabled` | `boolean` | Whether the feature or item is enabled. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`lassoSelecting()` {: #lassoselecting data-toc-label="lassoSelecting()" }

Returns true if a rubber-band lasso selection box is actively being dragged.

Returns `boolean`: True if enabled; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`lassoSelectionEnabled()` {: #lassoselectionenabled data-toc-label="lassoSelectionEnabled()" }

Returns true if rubber-band lasso box selection is enabled.

Returns `boolean`: True if enabled; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`lassoSelectionEnabled(boolean)` {: #lassoselectionenabledboolean data-toc-label="lassoSelectionEnabled(boolean)" }

Sets whether rubber-band lasso selection is enabled.

| Parameter | Type | Description |
| --- | --- | --- |
| `lassoSelectionEnabled` | `boolean` | Whether the feature or item is enabled. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`majorGridColor()` {: #majorgridcolor data-toc-label="majorGridColor()" }

Returns the major grid lines color.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`maxZoom()` {: #maxzoom data-toc-label="maxZoom()" }

Returns maximum zoom scale factor.

Returns `float`: The maximum zoom factor..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`minZoom()` {: #minzoom data-toc-label="minZoom()" }

Returns minimum zoom scale factor.

Returns `float`: The minimum zoom factor..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onConnectionCreated(EventListener)` {: #onconnectioncreatedeventlistener data-toc-label="onConnectionCreated(EventListener)" }

Registers a listener called when a new connection is created.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | The event listener callback. |

Returns `EventSubscription`: The event subscription handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onConnectionDragEnded(EventListener)` {: #onconnectiondragendedeventlistener data-toc-label="onConnectionDragEnded(EventListener)" }

Registers a listener called when connection drag interaction completes or cancels.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | The event listener callback. |

Returns `EventSubscription`: The event subscription handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onConnectionDragStarted(EventListener)` {: #onconnectiondragstartedeventlistener data-toc-label="onConnectionDragStarted(EventListener)" }

Registers a listener called when a connection drag starts from a port.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | The event listener callback. |

Returns `EventSubscription`: The event subscription handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onConnectionRemoved(EventListener)` {: #onconnectionremovedeventlistener data-toc-label="onConnectionRemoved(EventListener)" }

Registers a listener called when a connection is deleted.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | The event listener callback. |

Returns `EventSubscription`: The event subscription handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onConnectionSelectionChanged(EventListener)` {: #onconnectionselectionchangedeventlistener data-toc-label="onConnectionSelectionChanged(EventListener)" }

Registers a listener called when connection selection state changes.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | The event listener callback. |

Returns `EventSubscription`: The event subscription handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onItemMoveEnded(EventListener)` {: #onitemmoveendedeventlistener data-toc-label="onItemMoveEnded(EventListener)" }

Registers a listener called when item drag movement finishes.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | The event listener callback. |

Returns `EventSubscription`: The event subscription handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onItemMoveStarted(EventListener)` {: #onitemmovestartedeventlistener data-toc-label="onItemMoveStarted(EventListener)" }

Registers a listener called when item drag movement begins.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | The event listener callback. |

Returns `EventSubscription`: The event subscription handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onItemMoved(EventListener)` {: #onitemmovedeventlistener data-toc-label="onItemMoved(EventListener)" }

Registers a listener called as an item is dragged across the canvas.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | The event listener callback. |

Returns `EventSubscription`: The event subscription handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onItemRemoved(EventListener)` {: #onitemremovedeventlistener data-toc-label="onItemRemoved(EventListener)" }

Registers a listener called when a node item is removed.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | The event listener callback. |

Returns `EventSubscription`: The event subscription handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onItemResizeEnded(EventListener)` {: #onitemresizeendedeventlistener data-toc-label="onItemResizeEnded(EventListener)" }

Registers a listener called when item resizing ends.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | The event listener callback. |

Returns `EventSubscription`: The event subscription handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onItemResizeStarted(EventListener)` {: #onitemresizestartedeventlistener data-toc-label="onItemResizeStarted(EventListener)" }

Registers a listener called when item resizing begins.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | The event listener callback. |

Returns `EventSubscription`: The event subscription handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onItemResized(EventListener)` {: #onitemresizedeventlistener data-toc-label="onItemResized(EventListener)" }

Registers a listener called as an item is being resized.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | The event listener callback. |

Returns `EventSubscription`: The event subscription handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onSelectionChanged(EventListener)` {: #onselectionchangedeventlistener data-toc-label="onSelectionChanged(EventListener)" }

Registers a listener called when item selection changes.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | The event listener callback. |

Returns `EventSubscription`: The event subscription handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`onViewportChanged(EventListener)` {: #onviewportchangedeventlistener data-toc-label="onViewportChanged(EventListener)" }

Registers a listener called when viewport pan or zoom changes.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | The event listener callback. |

Returns `EventSubscription`: The event subscription handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`panning()` {: #panning data-toc-label="panning()" }

Returns true if the viewport canvas is currently being panned.

Returns `boolean`: True if enabled; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`panningEnabled()` {: #panningenabled data-toc-label="panningEnabled()" }

Returns true if canvas panning via mouse drag is enabled.

Returns `boolean`: True if enabled; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`panningEnabled(boolean)` {: #panningenabledboolean data-toc-label="panningEnabled(boolean)" }

Sets whether canvas panning is enabled.

| Parameter | Type | Description |
| --- | --- | --- |
| `panningEnabled` | `boolean` | Whether the feature or item is enabled. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`port(NodeGraphPortRef)` {: #portnodegraphportref data-toc-label="port(NodeGraphPortRef)" }

Resolves a port reference to its corresponding port object.

| Parameter | Type | Description |
| --- | --- | --- |
| `ref` | `NodeGraphPortRef` | The port reference. |

Returns `NodeGraphPort`: The port instance, or null if not found..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`portColor()` {: #portcolor data-toc-label="portColor()" }

Returns default fill color for pin ports.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`preferredHeight()` {: #preferredheight data-toc-label="preferredHeight()" }

Returns the preferred height of this graph.

Returns `float`: The preferred height in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`preferredHeight(float)` {: #preferredheightfloat data-toc-label="preferredHeight(float)" }

Sets the preferred height of this graph.

| Parameter | Type | Description |
| --- | --- | --- |
| `preferredHeight` | `float` | The preferred height in pixels. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`preferredSize(float, float)` {: #preferredsizefloat-float data-toc-label="preferredSize(float, float)" }

Sets both preferred width and height dimensions.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | The preferred width in pixels. |
| `height` | `float` | The preferred height in pixels. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`preferredWidth()` {: #preferredwidth data-toc-label="preferredWidth()" }

Returns the preferred width of this graph.

Returns `float`: The preferred width in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`preferredWidth(float)` {: #preferredwidthfloat data-toc-label="preferredWidth(float)" }

Sets the preferred width of this graph.

| Parameter | Type | Description |
| --- | --- | --- |
| `preferredWidth` | `float` | The preferred width in pixels. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`removeConnection(NodeGraphConnection)` {: #removeconnectionnodegraphconnection data-toc-label="removeConnection(NodeGraphConnection)" }

Removes the specified connection instance.

| Parameter | Type | Description |
| --- | --- | --- |
| `connection` | `NodeGraphConnection` | The connection parameter. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`removeConnection(String)` {: #removeconnectionstring data-toc-label="removeConnection(String)" }

Removes the connection with the given ID.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | The connection identifier. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`removeItem(NodeGraphItem)` {: #removeitemnodegraphitem data-toc-label="removeItem(NodeGraphItem)" }

Removes the specified node item and all its connected lines.

| Parameter | Type | Description |
| --- | --- | --- |
| `item` | `NodeGraphItem` | The target item. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`removeItem(String)` {: #removeitemstring data-toc-label="removeItem(String)" }

Removes the node item with the specified ID.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | The item identifier. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`removeSelectedConnections()` {: #removeselectedconnections data-toc-label="removeSelectedConnections()" }

Removes all currently selected connections.

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`resizeEnabled()` {: #resizeenabled data-toc-label="resizeEnabled()" }

Returns true if interactive resizing of resizable items is enabled.

Returns `boolean`: True if enabled; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`resizeEnabled(boolean)` {: #resizeenabledboolean data-toc-label="resizeEnabled(boolean)" }

Sets whether interactive item resizing is enabled.

| Parameter | Type | Description |
| --- | --- | --- |
| `resizeEnabled` | `boolean` | Whether the feature or item is enabled. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`resizeHandleColor()` {: #resizehandlecolor data-toc-label="resizeHandleColor()" }

Returns color of resize grip handles.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`restoreSnapshot(NodeGraphSnapshot, NodeGraphWidgetResolver)` {: #restoresnapshotnodegraphsnapshot-nodegraphwidgetresolver data-toc-label="restoreSnapshot(NodeGraphSnapshot, NodeGraphWidgetResolver)" }

Restores graph items, connections, and viewport from a serialized snapshot using a widget resolver.

| Parameter | Type | Description |
| --- | --- | --- |
| `snapshot` | `NodeGraphSnapshot` | The graph snapshot object. |
| `resolver` | `NodeGraphWidgetResolver` | The widget resolver callback. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`scaleContentWithZoom()` {: #scalecontentwithzoom data-toc-label="scaleContentWithZoom()" }

Returns true if node item content widgets scale with zoom.

Returns `boolean`: True if enabled; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`scaleContentWithZoom(boolean)` {: #scalecontentwithzoomboolean data-toc-label="scaleContentWithZoom(boolean)" }

Sets whether node item content widgets scale with zoom.

| Parameter | Type | Description |
| --- | --- | --- |
| `scaleContentWithZoom` | `boolean` | Whether the feature or item is enabled. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectConnection(NodeGraphConnection)` {: #selectconnectionnodegraphconnection data-toc-label="selectConnection(NodeGraphConnection)" }

Selects the specified connection.

| Parameter | Type | Description |
| --- | --- | --- |
| `connection` | `NodeGraphConnection` | The connection parameter. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectConnection(String)` {: #selectconnectionstring data-toc-label="selectConnection(String)" }

Selects the connection with the given ID.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | The connection identifier. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectItem(NodeGraphItem)` {: #selectitemnodegraphitem data-toc-label="selectItem(NodeGraphItem)" }

Selects the specified item exclusively.

| Parameter | Type | Description |
| --- | --- | --- |
| `item` | `NodeGraphItem` | The target item. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectItem(NodeGraphItem, boolean)` {: #selectitemnodegraphitem-boolean data-toc-label="selectItem(NodeGraphItem, boolean)" }

Selects the item with optional additive toggle behaviour.

| Parameter | Type | Description |
| --- | --- | --- |
| `item` | `NodeGraphItem` | The target item. |
| `additive` | `boolean` | Whether selection toggles additively. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectItem(String)` {: #selectitemstring data-toc-label="selectItem(String)" }

Selects the item with the given ID exclusively.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | The item identifier. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectedConnectionColor()` {: #selectedconnectioncolor data-toc-label="selectedConnectionColor()" }

Returns highlight line color for selected connections.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectedConnectionIds()` {: #selectedconnectionids data-toc-label="selectedConnectionIds()" }

Returns an unmodifiable list of IDs of currently selected connections.

Returns `List`: List of selected connection IDs..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectedItemBorderColor()` {: #selecteditembordercolor data-toc-label="selectedItemBorderColor()" }

Returns selected border color for node items.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectedItemIds()` {: #selecteditemids data-toc-label="selectedItemIds()" }

Returns an unmodifiable list of IDs of currently selected items.

Returns `List`: List of selected item IDs..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectionBoxBorderColor()` {: #selectionboxbordercolor data-toc-label="selectionBoxBorderColor()" }

Returns border color for rubber-band selection rectangles.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectionBoxFillColor()` {: #selectionboxfillcolor data-toc-label="selectionBoxFillColor()" }

Returns fill color for rubber-band selection rectangles.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectionMode()` {: #selectionmode data-toc-label="selectionMode()" }

Returns current selection mode: NONE, SINGLE, or MULTIPLE.

Returns `NodeGraphSelectionMode`: The active selection mode..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`selectionMode(NodeGraphSelectionMode)` {: #selectionmodenodegraphselectionmode data-toc-label="selectionMode(NodeGraphSelectionMode)" }

Sets selection mode.

| Parameter | Type | Description |
| --- | --- | --- |
| `selectionMode` | `NodeGraphSelectionMode` | The selection mode. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`snapshot()` {: #snapshot data-toc-label="snapshot()" }

Captures a full serializable snapshot of the node graph.

Returns `NodeGraphSnapshot`: The serialized snapshot object..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`validateConnection(NodeGraphPortRef, NodeGraphPortRef)` {: #validateconnectionnodegraphportref-nodegraphportref data-toc-label="validateConnection(NodeGraphPortRef, NodeGraphPortRef)" }

Validates whether a connection can be established according to the active connection policy.

| Parameter | Type | Description |
| --- | --- | --- |
| `from` | `NodeGraphPortRef` | The source port reference. |
| `to` | `NodeGraphPortRef` | The destination port reference. |

Returns `NodeGraphConnectionValidation`: The connection validation result..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`viewport()` {: #viewport data-toc-label="viewport()" }

Returns current viewport pan position and zoom factor.

Returns `NodeGraphViewport`: The current viewport transform..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`viewport(float, float)` {: #viewportfloat-float data-toc-label="viewport(float, float)" }

Sets viewport pan position without altering zoom.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | The viewport X pan offset. |
| `y` | `float` | The viewport Y pan offset. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`viewport(float, float, float)` {: #viewportfloat-float-float data-toc-label="viewport(float, float, float)" }

Sets viewport pan position and zoom factor.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | The viewport X pan offset. |
| `y` | `float` | The viewport Y pan offset. |
| `zoom` | `float` | The zoom factor. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`wheelPanStep()` {: #wheelpanstep data-toc-label="wheelPanStep()" }

Returns the pixel step distance per wheel tick during wheel panning.

Returns `float`: The pan distance per wheel tick in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`wheelPanStep(float)` {: #wheelpanstepfloat data-toc-label="wheelPanStep(float)" }

Sets the pixel step distance per wheel tick.

| Parameter | Type | Description |
| --- | --- | --- |
| `wheelPanStep` | `float` | The pan distance per wheel tick in pixels. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`wheelPanningEnabled()` {: #wheelpanningenabled data-toc-label="wheelPanningEnabled()" }

Returns true if mouse wheel pans instead of zooming.

Returns `boolean`: True if enabled; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`wheelPanningEnabled(boolean)` {: #wheelpanningenabledboolean data-toc-label="wheelPanningEnabled(boolean)" }

Sets whether mouse wheel pans instead of zooming.

| Parameter | Type | Description |
| --- | --- | --- |
| `wheelPanningEnabled` | `boolean` | Whether the feature or item is enabled. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`zoomEnabled()` {: #zoomenabled data-toc-label="zoomEnabled()" }

Returns true if canvas zooming via mouse wheel is enabled.

Returns `boolean`: True if enabled; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`zoomEnabled(boolean)` {: #zoomenabledboolean data-toc-label="zoomEnabled(boolean)" }

Sets whether canvas zooming is enabled.

| Parameter | Type | Description |
| --- | --- | --- |
| `zoomEnabled` | `boolean` | Whether the feature or item is enabled. |

Returns `NodeGraph`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NodeGraph::</span>`zoomRange(float, float)` {: #zoomrangefloat-float data-toc-label="zoomRange(float, float)" }

Configures minimum and maximum allowed zoom levels.

| Parameter | Type | Description |
| --- | --- | --- |
| `minZoom` | `float` | The minimum zoom factor. |
| `maxZoom` | `float` | The maximum zoom factor. |

Returns `NodeGraph`: This instance for chaining..

</div>

