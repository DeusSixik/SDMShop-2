# `NodeStyle`

Per-node look: header/body/border/title colors plus header and row geometry in world units.

- **Purpose:** Lets each node type carry its own accent color (event red, function blue, variable green) like Blueprint.

### USE WHEN

- Color a node header by category with `headerColor(int)`.
- Tune row height, padding and corner radius for a denser or looser node.

### CRITICAL RULES & GOTCHAS:

- All lengths are world units and are scaled by the graph zoom.
- Use `copy()` before customizing a shared template.

- **Related:** `NodeBuilder`, `HeaderModule`, `NodeModule`

