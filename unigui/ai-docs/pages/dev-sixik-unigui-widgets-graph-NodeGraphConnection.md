# `NodeGraphConnection`

Represents a directional or bidirectional connection between two ports in a NodeGraph.

### API

- `NodeGraphConnection(String, NodeGraphPortRef, NodeGraphPortRef)` — Constructs a connection between ports specified by port references.
- `NodeGraphConnection(String, String, String, String, String)` — Constructs a connection between ports by item and port IDs.
- `enabled()` → boolean — Returns true if this connection is active and enabled.
- `enabled(boolean)` → NodeGraphConnection — Sets whether this connection is active and enabled.
- `from()` → NodeGraphPortRef — Returns the source port reference.
- `fromItemId()` → String — Returns the source node item ID.
- `fromPortId()` → String — Returns the source port ID.
- `id()` → String — Returns the unique identifier of this connection.
- `selected()` → boolean — Returns true if this connection is currently selected.
- `to()` → NodeGraphPortRef — Returns the destination port reference.
- `toItemId()` → String — Returns the destination node item ID.
- `toPortId()` → String — Returns the destination port ID.
- `type()` → String — Returns the connection data type or compatibility category string.
- `type(String)` → NodeGraphConnection — Sets the connection data type or compatibility category string.

