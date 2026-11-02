# `MapMarkerHandle`

Handle for fluent configuration of marker positioning, screen sizing, zoom scaling, pivot, and visibility on MapCanvas.

### API

- `anchor()` → AnchorWidget — Returns the underlying low-level AnchorWidget managing this marker.
- `cullOutsideViewport(boolean)` → MapMarkerHandle<T> — Enables or disables automatic culling when the marker is outside the visible viewport.
- `id()` → String — Returns the unique string identifier of this marker within the map.
- `mapX()` → float — Returns the X coordinate of the marker in map coordinate space.
- `mapY()` → float — Returns the Y coordinate of the marker in map coordinate space.
- `maxScreenSize(float, float)` → MapMarkerHandle<T> — Sets maximum screen dimensions to prevent marker over-expanding at high zoom.
- `minScreenSize(float, float)` → MapMarkerHandle<T> — Sets minimum screen dimensions to prevent marker over-shrinking at low zoom.
- `pivot(float, float)` → MapMarkerHandle<T> — Sets the normalized anchor pivot point (0.0 to 1.0) inside the marker bounds.
- `position(float, float)` → MapMarkerHandle<T> — Positions the marker in map coordinate space.
- `priority()` → int — Returns the user-defined priority value of this marker.
- `priority(int)` → MapMarkerHandle<T> — Sets the marker sorting priority value.
- `projectedPosition(float, float)` → MapMarkerHandle<T> — Positions the marker in external world coordinates projected via MapCanvas projection.
- `screenSize(float, float)` → MapMarkerHandle<T> — Sets the base fixed screen dimensions of this marker in UI pixels.
- `visible(boolean)` → MapMarkerHandle<T> — Sets the manual visibility flag of this marker.
- `visibleZoom(float, float)` → MapMarkerHandle<T> — Restricts marker visibility to the specified zoom range.
- `widget()` → T — Returns the UI widget rendered as this marker.
- `zoomScaling(boolean, float)` → MapMarkerHandle<T> — Configures dynamic scaling of the marker size relative to map zoom.

