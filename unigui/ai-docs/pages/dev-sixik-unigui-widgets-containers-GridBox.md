# `GridBox` (extends `PanelWidget`)

Multi-column grid container placing children into uniform cells or custom track templates.

- **Purpose:** Arranges children row by row into uniform columns or CSS Grid tracks with independent horizontal/vertical spacing.

### USE WHEN

- Display items in a grid: inventory slots, gallery tiles, or tabular control panels.
- Define custom column/row sizing tracks using GridTrack templates.

### CRITICAL RULES & GOTCHAS:

- columns() must be >= 1.
- Custom gridColumns(GridTrack...) overrides uniform column division.

- **Related:** `LayoutStyle`, `FlexBox`, `PanelWidget`

### LAYOUT

- `columns(int)` → GridBox (default: `1`) — Sets fixed number of columns for grid arrangement.
- `spacing(float)` → GridBox — Sets uniform row and column spacing in UI pixels.
- `horizontalSpacing(float)` → GridBox (default: `0`) — Sets horizontal gap between columns.
- `verticalSpacing(float)` → GridBox (default: `0`) — Sets vertical gap between rows.

### EXAMPLES

#### 3-column grid

```java
GridBox grid = new GridBox().columns(3).spacing(8.0f);
```

