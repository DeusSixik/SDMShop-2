# `SplitPanel` (extends `PanelWidget`)

Two-pane container separated by an interactive draggable splitter for dynamic resizing.

- **Purpose:** Allows users to adjust relative dimensions of two adjacent panes along a horizontal or vertical axis.

### USE WHEN

- Building master-detail layouts, collapsible sidebars, or split code/preview windows.
- Let users interactively drag a divider handle to resize two adjacent content panes.

### CRITICAL RULES & GOTCHAS:

- Manages two child slots (first and second) with an internal Splitter widget between them.
- splitRatio is normalized between 0.0 and 1.0, respecting minFirstSize and minSecondSize limits.
- Orientation.HORIZONTAL places the first pane on the left; Orientation.VERTICAL places it on top.

- **Related:** `PanelWidget`, `DockPanel`, `HBox`

### PANES

- `first(Widget)` → SplitPanel — Sets widget in first slot (left or top).
- `second(Widget)` → SplitPanel — Sets widget in second slot (right or bottom).

### LAYOUT

- `orientation(Orientation)` → SplitPanel (default: `horizontal`) — Sets split orientation (HORIZONTAL or VERTICAL).
- `splitRatio(float)` → SplitPanel — Sets split fraction for first slot (0.0 to 1.0).

### CONSTRAINTS

- `minFirstSize(float)` → SplitPanel (default: `32`) — Sets minimum pixel size for first pane.
- `minSecondSize(float)` → SplitPanel (default: `32`) — Sets minimum pixel size for second pane.

### EXAMPLES

#### Horizontal split panel

```java
SplitPanel split = new SplitPanel(treeWidget, editorWidget)
        .orientation(Orientation.HORIZONTAL)
        .splitRatio(0.28f)
        .minFirstSize(120.0f);
```

