# `NodeModule`

Pluggable vertical strip of a node: owns widgets and ports, measures and arranges them, optionally paints decor.

- **Purpose:** Makes nodes composable so Blueprint or Shader Graph nodes are assembled from reusable parts.

### USE WHEN

- Add a custom row (preview tile, slider, color swatch) by implementing NodeModule.
- Hide detail when a node is collapsed via `visibleWhenCollapsed()`.

### CRITICAL RULES & GOTCHAS:

- All geometry is in world units relative to the node origin; the graph applies zoom.
- Only widgets returned by `widgets()` take part in layout, rendering and hit testing.
- Register ports through `NodeGraphItem.addPort(port, module)` inside `attach`; remove them in `detach`.
- Return a new list from `widgets()` only when it changes: the graph caches the visible widget list until `NodeGraphItem.refreshModules()`.

- **Related:** `NodeGraphItem`, `NodeBuilder`, `HeaderModule`, `PortRowModule`, `WidgetModule`, `SeparatorModule`

