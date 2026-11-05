# `NodeGraphItem`

Node item container placed in a NodeGraph canvas hosting a content widget and pin ports.

### API

- `NodeGraphItem(String, Widget)` — Constructs a NodeGraphItem with unique ID and content widget.
- `NodeGraphItem(String, Widget, float, float)` — Constructs a NodeGraphItem with unique ID, content widget, and initial coordinates.
- `addPort(NodeGraphPort)` → NodeGraphItem — Attaches an existing port instance to this node.
- `addPort(String, NodeGraphPortKind, NodeGraphPortSide, float)` → NodeGraphPort — Creates and attaches a port with specified ID, direction kind, side, and offset.
- `arrangedHeight()` → float — Returns the actual arranged layout height.
- `arrangedWidth()` → float — Returns the actual arranged layout width.
- `autoHeight()` → boolean — Returns true if height is determined automatically by content layout.
- `autoSize()` → NodeGraphItem — Resets width and height to automatic sizing based on content.
- `autoWidth()` → boolean — Returns true if width is determined automatically by content layout.
- `content()` → Widget — Returns the content widget hosted inside this node, or null for a modular node built from NodeModules.
- `height()` → float — Returns the explicit configured height of this node, or <= 0 for auto-sizing.
- `id()` → String — Returns the unique identifier of this node item.
- `movable()` → boolean — Returns true if this node can be dragged to move across the canvas.
- `movable(boolean)` → NodeGraphItem — Sets whether this node can be dragged to move.
- `port(String)` → NodeGraphPort — Returns the port with the specified ID, or null if not found.
- `ports()` → List<NodeGraphPort> — Returns an unmodifiable list of all ports attached to this node.
- `position(float, float)` → NodeGraphItem — Sets the (X, Y) world position of this node on the canvas.
- `removePort(String)` → NodeGraphItem — Removes the port with the specified ID.
- `resizable()` → boolean — Returns true if this node displays resize handles and can be interactively resized.
- `resizable(boolean)` → NodeGraphItem — Sets whether this node can be resized by dragging corner handles.
- `selectable()` → boolean — Returns true if this node can be selected by clicks or lasso selection.
- `selectable(boolean)` → NodeGraphItem — Sets whether this node can be selected.
- `selected()` → boolean — Returns true if this node is currently selected.
- `size(float, float)` → NodeGraphItem — Sets explicit width and height dimensions for this node.
- `visible()` → boolean — Returns true if this node is visible.
- `visible(boolean)` → NodeGraphItem — Sets whether this node is visible.
- `width()` → float — Returns the explicit configured width of this node, or <= 0 for auto-sizing.
- `x()` → float — Returns the X position of this node in canvas world coordinates.
- `y()` → float — Returns the Y position of this node in canvas world coordinates.

