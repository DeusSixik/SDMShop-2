# `GraphView` (extends `WidgetBase`)

Static or interactive diagram graph widget displaying connected nodes, edges, labels, and tooltips with custom renderers.

### API

- `GraphView()` — Constructs a GraphView diagram widget with default styles.
- `addEdge(String, String)` → GraphView — Adds a directed edge connection between two node IDs; the edge waits for a missing node to be added.
- `addNode(String, float, float)` → GraphView — Adds a node with ID and normalized (0..1) coordinates; an existing ID is moved instead of duplicated.
- `clearGraph()` → GraphView — Clears all nodes and edges from the diagram.
- `edgeColor()` → MutableColor — Returns the connection edge line color.
- `hoveredNodeColor()` → MutableColor — Returns the hover fill color for nodes.
- `labelColor()` → MutableColor — Returns the node label text color.
- `nodeColor()` → MutableColor — Returns the default node circle fill color.
- `nodeLabelPlacement(NodeLabelPlacement)` → GraphView (default: `right`) — Sets relative placement of text labels around node points.
- `nodeLabelProvider(Function<NodePoint, String>)` → GraphView — Configures a function returning the text label for a given node point.
- `nodeLabelRenderer(GraphNodeLabelRenderer)` → GraphView — Sets a custom renderer callback for drawing node labels.
- `nodeLabelsVisible(boolean)` → GraphView (default: `true`) — Sets whether text labels are rendered alongside node points.
- `nodeRenderer(GraphNodeRenderer)` → GraphView — Sets a custom renderer callback for drawing node points.
- `nodeTooltipProvider(Function<NodePoint, String>)` → GraphView — Configures a function returning tooltip text for hovered nodes.
- `nodeTooltipRenderer(GraphNodeTooltipRenderer)` → GraphView — Sets a custom renderer callback for drawing node hover tooltips.
- `onNodeClick(EventListener<? super GraphNodeClickEvent>)` → EventSubscription — Registers a listener called when a node point is clicked.
- `preferredHeight()` → float — Returns the preferred height of this graph view.
- `preferredHeight(float)` → GraphView (default: `130`) — Sets the preferred height of this graph view.
- `preferredSize(float, float)` → GraphView — Sets both preferred width and height dimensions.
- `preferredWidth()` → float — Returns the preferred width of this graph view.
- `preferredWidth(float)` → GraphView (default: `240`) — Sets the preferred width of this graph view.
- `tooltipBackground()` → MutableColor — Returns the background fill color of node tooltip popups.
- `tooltipBorder()` → MutableColor — Returns the border outline color of node tooltip popups.

### GRAPH DATA

- `removeNode(String)` → GraphView — Removes a node and every edge that references its identifier.

### APPEARANCE

- `directed(boolean)` → GraphView (default: `true`) — Enables or disables target arrowheads for edges.

