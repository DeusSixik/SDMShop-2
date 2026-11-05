# `DockArea` (extends `Enum`)

Target destination zone for docking operations: edge splits (LEFT, RIGHT, TOP, BOTTOM), tab groupings (CENTER, TAB), or detached floating windows (FLOAT).

### API

- `insertsBeforeTarget()` → boolean — Returns true if the area places the new pane before the existing target node.
- `splitOrientation()` → DockSplitOrientation — Returns the split orientation corresponding to edge areas, or null for non-split areas.

