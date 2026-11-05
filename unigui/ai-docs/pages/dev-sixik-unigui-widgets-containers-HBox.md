# `HBox` (extends `LinearBox`)

Horizontal linear container placing children left-to-right with uniform spacing.

- **Purpose:** Convenient shorthand for LinearBox(Orientation.HORIZONTAL). Used for toolbars, headers, and action rows.

### USE WHEN

- Place elements side by side horizontally in a single row.
- Build toolbars, action buttons, search bars, and breadcrumbs.

### CRITICAL RULES & GOTCHAS:

- FlexDirection is fixed to ROW.
- Use child.layout(s -> s.expand()) to consume remaining horizontal free space.

- **Related:** `VBox`, `LinearBox`, `FlexBox`, `PanelRowWidget`

### LIFECYCLE

- `HBox()` — Creates a horizontal linear container.

### EXAMPLES

#### Toolbar row

```java
HBox toolbar = new HBox().spacing(4.0f);
toolbar.addChild(saveBtn);
toolbar.addChild(cancelBtn);
```

