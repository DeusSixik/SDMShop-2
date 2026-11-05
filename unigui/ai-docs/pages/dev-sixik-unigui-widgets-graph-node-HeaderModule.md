# `HeaderModule` (implements `NodeModule`)

Colored node header with icon, title and collapse arrow; stays visible when collapsed.

- **Purpose:** Gives every node the Blueprint/Shader Graph title bar and the collapse affordance.

### USE WHEN

- Start every node with a HeaderModule carrying the node name.
- Put execution-flow pins in a flow row right below the header, not in the header itself.

### CRITICAL RULES & GOTCHAS:

- The header owns no ports; exec pins belong to `PortRowModule.flow(...)` under it.
- Header geometry uses `NodeStyle.headerHeight()`; the arrow sits at the right padding.
- Disable the arrow with `collapsible(false)` for nodes that must stay expanded.

- **Related:** `NodeModule`, `NodeStyle`, `NodeGraphItem`, `NodeBuilder`

