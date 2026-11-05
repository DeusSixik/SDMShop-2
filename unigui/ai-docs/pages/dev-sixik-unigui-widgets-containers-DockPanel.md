# `DockPanel` (extends `PanelWidget`)

Edge-docking layout container that aligns child widgets along outer boundaries (left, right, top, bottom).

- **Purpose:** Creates classic desktop application shells with top menu/toolbars, sidebars, bottom status bars, and central expanding workspace.

### USE WHEN

- Arrange peripheral controls around a central main content area.
- Build application window chrome: header bar on top, footer at bottom, side navigation panels.

### CRITICAL RULES & GOTCHAS:

- Children are laid out sequentially along their assigned DockSide, each consuming available space along that edge.
- When lastChildFill is true (default), the final child widget occupies the entire remaining interior area.
- For complex IDE docking (tear-off windows, tabs, split docking zones), use DockingRoot and DockingManager instead of DockPanel.

- **Related:** `PanelWidget`, `SplitPanel`, `Box`

### LAYOUT

- `addChild(Widget, DockSide)` → void — Adds a child widget docked to a specific edge.
- `dockSide(Widget, DockSide)` → DockPanel — Sets dock side for an existing or future child.
- `lastChildFill(boolean)` → DockPanel (default: `true`) — Controls whether the last child expands to occupy remaining space.

### EXAMPLES

#### Docking application shell

```java
DockPanel shell = new DockPanel();
shell.addChild(toolbar, DockSide.TOP);
shell.addChild(sidebar, DockSide.LEFT);
shell.addChild(mainView);
```

