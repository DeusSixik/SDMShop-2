# `Border` (extends `WidgetBase`)

Render-only decorative outline widget without child widgets.

- **Purpose:** Draws a rectangular stroke with optional corner radius. Designed for overlay highlights and borders.

### USE WHEN

- Draw an outline overlay over another component (e.g. selection indicator, drag target).
- Highlight widget boundaries without modifying their internal layout.

### CRITICAL RULES & GOTCHAS:

- Border does not support child widgets: use Box when nested content is needed.
- color() returns a live MutableColor that automatically invalidates visual state upon modification.

- **Related:** `Box`, `SurfaceWidget`, `WidgetBase`

### LIFECYCLE

- `Border()` — Creates a border decorator with default 1px white border.

### APPEARANCE

- `color()` → MutableColor — Returns live mutable border color.
- `thickness(float)` → Border — Sets border stroke thickness in UI pixels.
- `radius(float)` → Border — Sets border corner rounding radius.

### EXAMPLES

#### Highlight border

```java
Border highlight = new Border().thickness(2.0f).radius(4.0f);
```

