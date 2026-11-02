# `MapProjection`

Axis-aligned linear projection engine converting between external world coordinates and MapCanvas internal map coordinates.

### API

- `MapProjection(float, float, float, float)` — Constructs a MapProjection directly with explicit scale and offset values.
- `affine()` → Builder — Starts fluent builder configuration of a projection via world-to-map calibration points.
- `axisAligned(float, float, float, float, float, float, float, float)` → MapProjection — Constructs an axis-aligned projection calibrated from two world-to-map point pairs.
- `identity()` → MapProjection — Returns the identity projection where world coordinates map 1:1 to map coordinates.
- `mapX(float)` → float — Transforms an external world X coordinate into map X coordinate space.
- `mapY(float)` → float — Transforms an external world Y coordinate into map Y coordinate space.
- `offsetX()` → float — Returns the horizontal X translation offset.
- `offsetY()` → float — Returns the vertical Y translation offset.
- `project(float, float)` → ViewportPoint — Transforms external world coordinates into a 2D map point.
- `scaleX()` → float — Returns the horizontal X scale factor.
- `scaleY()` → float — Returns the vertical Y scale factor.
- `unproject(float, float)` → ViewportPoint — Inverse transforms a map coordinate point back into external world coordinates.
- `worldX(float)` → float — Inverse transforms a map X coordinate back into external world X space.
- `worldY(float)` → float — Inverse transforms a map Y coordinate back into external world Y space.

