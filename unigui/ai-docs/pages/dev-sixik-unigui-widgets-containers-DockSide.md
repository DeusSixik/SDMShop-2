# `DockSide` (extends `Enum`)

Specifies which outer edge (LEFT, RIGHT, TOP, BOTTOM) a child widget docks against inside a DockPanel container.

- **Purpose:** Used exclusively with DockPanel to attach child widgets to boundaries, shrinking the remaining central layout area with each docked element.

### USE WHEN

- Arranging peripheral elements like headers, toolbars, sidebars, or footers in a DockPanel.

### CRITICAL RULES & GOTCHAS:

- DockSide is only used by DockPanel; do not confuse with DockArea, which is used for full docking systems (DockingRoot/DockingManager).
- The order in which widgets are docked matters: earlier children take precedence along their respective edges.

- **Related:** `DockPanel`

