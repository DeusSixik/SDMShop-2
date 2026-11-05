# `DockLayoutSnapshot` (extends `Record`)

Immutable snapshot representing the recursive docking layout tree and active pane selection for serialization and layout persistence.

### API

- `DockLayoutSnapshot(Node)` — Creates a layout snapshot with empty active pane.
- `from(DockNode)` → DockLayoutSnapshot — Captures an immutable snapshot from a live DockNode hierarchy.
- `from(DockNode, String)` → DockLayoutSnapshot — Captures an immutable snapshot from a live DockNode hierarchy with active pane identifier.

