# `PanelRowWidget` (extends `PanelWidget`)

Two-sided row container that organizes widgets into left-aligned and right-aligned groups.

- **Purpose:** Creates title bars, action toolbars, and list item rows with clear left/right separation of titles and controls.

### USE WHEN

- A horizontal row needs left-aligned items (titles, icons) and right-aligned items (action buttons, badges).
- Creating toolbar strips or list item rows with left/right justification.

### CRITICAL RULES & GOTCHAS:

- Left widgets are laid out sequentially from left to right; right widgets are laid out from right to left.
- left(Widget) and right(Widget) replace the entire group on that side, while addLeft/addRight append items to the groups.
- Overall row height and gaps can be customized per-instance via rowHeight, gap, leftGap, rightGap.

- **Related:** `SettingRow`, `HBox`, `PanelWidget`

### GROUPS

- `left(Widget)` → PanelRowWidget — Replaces left group with a single widget.
- `right(Widget)` → PanelRowWidget — Replaces right group with a single widget.
- `addLeft(Widget)` → PanelRowWidget — Appends widget to left group.
- `addRight(Widget)` → PanelRowWidget — Appends widget to right group.

### SIZING

- `rowHeight(float)` → PanelRowWidget (default: `24`) — Sets preferred row height in UI pixels.
- `gap(float)` → PanelRowWidget (default: `8`) — Sets uniform spacing between elements and groups.

### EXAMPLES

#### Toolbar row with left/right groups

```java
PanelRowWidget row = new PanelRowWidget().rowHeight(24.0f).gap(8.0f);
row.left(titleLabel);
row.addRight(applyButton);
```

