# `LinearBox` (extends `SurfaceWidget`)

One-dimensional flex container arranging children sequentially along a main axis without wrapping.

- **Purpose:** Synchronizes orientation and spacing into LayoutStyle flex properties. Supports opt-in Surface styling.

### USE WHEN

- Arrange widgets in a single row or column when orientation can change dynamically.
- Prefer VBox or HBox subclasses for static orientation.

### CRITICAL RULES & GOTCHAS:

- Surface rendering is disabled by default; calling background() or border() automatically enables it.
- Changing orientation or spacing updates LayoutStyle and invalidates layout.

- **Related:** `VBox`, `HBox`, `FlexBox`, `WrapPanel`, `SurfaceWidget`

### LIFECYCLE

- `LinearBox(Orientation)` — Creates a LinearBox with the specified orientation.

### LAYOUT

- `orientation(Orientation)` → LinearBox (default: `vertical`) — Sets orientation (HORIZONTAL or VERTICAL) and updates flex direction.
- `spacing(float)` → LinearBox (default: `0`) — Sets uniform gap between adjacent children along main axis.

### EXAMPLES

#### Create LinearBox

```java
LinearBox box = new LinearBox(Orientation.HORIZONTAL).spacing(6.0f);
```

