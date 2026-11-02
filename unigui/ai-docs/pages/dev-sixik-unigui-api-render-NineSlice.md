# `NineSlice`

Nine-patch scalable texture definition dividing an image into 4 unscaled corners, 4 single-axis edges, and 1 center area.

- **Purpose:** Enables scalable UI dialogues, buttons, and frames from a single source texture without distorting corner geometry.

### USE WHEN

- Define scalable window borders or dialogue plates for widgets via `NineSlice.of(texture, border)`.
- Draw scalable framed panels using `DrawScope#nineSlice(NineSlice, x, y, w, h, paint, fillCenter)`.
- Create hollow selection frames by specifying `fillCenter = false`.

### CRITICAL RULES & GOTCHAS:

- Border insets (`left`, `top`, `right`, `bottom`) are defined in source texture pixels, not screen UI points.
- DrawScope automatically scales down corner borders proportionally if widget dimensions are smaller than corner margins.
- Negative border insets are sanitized to 0.0.

- **Related:** `TextureHandle`, `SliceBorders`, `ImageFit`, `DrawScope`

### EXAMPLES

#### Create nine-slice window panel

```java
NineSlice panel = NineSlice.of(windowTex, 8.0f);
scope.nineSlice(panel, b.x(), b.y(), b.width(), b.height(), Paint.NONE, true);
```

