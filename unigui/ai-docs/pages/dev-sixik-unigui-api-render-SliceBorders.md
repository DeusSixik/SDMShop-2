# `SliceBorders` (extends `Record`)

Immutable nine-slice border inset metrics measured in native texture pixels.

- **Purpose:** Specifies unscaled corner dimensions for nine-slice panel rendering.

### USE WHEN

- Define nine-slice border insets for widget visual surfaces via `SliceBorders.all(border)` or `SliceBorders.symmetric(h, v)`.
- Build a NineSlice directly from borders using `slice(TextureHandle)`.

### CRITICAL RULES & GOTCHAS:

- Values are in raw texture pixels, not screen UI pixels.
- Negative insets are sanitized to 0.0.
- Zero borders (`isZero()`) degrade nine-slice rendering to a simple stretch.

- **Related:** `NineSlice`, `ImageFit`, `TextureHandle`, `DrawScope`

### EXAMPLES

#### Create uniform slice borders

```java
SliceBorders borders = SliceBorders.all(6.0f);
NineSlice slice = borders.slice(texture);
```

