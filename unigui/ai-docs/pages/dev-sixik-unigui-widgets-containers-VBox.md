# `VBox` (extends `LinearBox`)

Vertical linear container stacking children top-to-bottom with uniform spacing.

- **Purpose:** Convenient shorthand for LinearBox(Orientation.VERTICAL). Widely used for forms, sidebars, and vertical stacks.

### USE WHEN

- Stack items vertically: headers, list items, menus, and form rows.
- Use spacing(pixels) to separate items without configuring margins on each child.

### CRITICAL RULES & GOTCHAS:

- FlexDirection is fixed to COLUMN.
- Use child.layout(s -> s.expand()) on a child to consume remaining vertical free space.

- **Related:** `HBox`, `LinearBox`, `FlexBox`, `ScrollView`

### LIFECYCLE

- `VBox()` — Creates a vertical linear container.

### EXAMPLES

#### Vertical stack

```java
VBox panel = new VBox().spacing(8.0f);
panel.addChild(title);
panel.addChild(content);
```

