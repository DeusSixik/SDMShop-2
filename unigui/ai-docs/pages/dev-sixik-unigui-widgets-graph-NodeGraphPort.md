# `NodeGraphPort`

Pin/port descriptor on a NodeGraphItem connecting lines to other nodes.

### API

- `NodeGraphPort(String, NodeGraphPortKind, NodeGraphPortSide, float)` — Constructs a NodeGraphPort with ID, direction kind, visual edge side, and pixel offset.
- `enabled()` → boolean — Returns true if this port is interactive and can accept or initiate links.
- `enabled(boolean)` → NodeGraphPort — Sets whether this port is interactive.
- `id()` → String — Returns the port identifier unique within its owning item.
- `itemId()` → String — Returns the ID of the item owning this port.
- `kind()` → NodeGraphPortKind — Returns the connection direction kind.
- `kind(NodeGraphPortKind)` → NodeGraphPort — Sets the connection direction kind.
- `offset()` → float — Returns the offset along the node edge in pixels.
- `offset(float)` → NodeGraphPort — Sets the offset along the node edge in pixels.
- `ref()` → NodeGraphPortRef — Returns the compound NodeGraphPortRef pointing to this port.
- `side()` → NodeGraphPortSide — Returns the visual edge side where the port is rendered.
- `side(NodeGraphPortSide)` → NodeGraphPort — Sets the visual edge side where the port is rendered.
- `type()` → String — Returns the data type or compatibility label for connection validation.
- `type(String)` → NodeGraphPort — Sets the data type or compatibility label.
- `visible()` → boolean — Returns true if this port is visible.
- `visible(boolean)` → NodeGraphPort — Sets whether this port is visible.

