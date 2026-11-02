# `AnchorWidget`

Spatial anchor pinning a screen-space UI widget to 3D/2D world coordinates with zoom scaling, pivot, and viewport culling.

### API

- `arrangedVisible()` → boolean — Returns true if the anchor is currently visible after zoom, culling, and layout passes.
- `cullOutsideViewport()` → boolean — Returns true if this anchor is automatically culled when outside the canvas viewport.
- `cullOutsideViewport(boolean)` → AnchorWidget — Enables or disables automatic culling when outside the visible viewport.
- `id()` → String — Returns the unique identifier of this anchor within its layer.
- `maxScreenHeight()` → float — Returns the maximum screen height constraint after zoom scaling.
- `maxScreenSize(float, float)` → AnchorWidget — Sets maximum screen dimensions to prevent over-expanding at high zoom.
- `maxScreenWidth()` → float — Returns the maximum screen width constraint after zoom scaling.
- `maxVisibleZoom()` → float — Returns the upper zoom threshold for anchor visibility.
- `minScreenHeight()` → float — Returns the minimum screen height constraint after zoom scaling.
- `minScreenSize(float, float)` → AnchorWidget — Sets minimum screen dimensions to prevent over-shrinking at low zoom.
- `minScreenWidth()` → float — Returns the minimum screen width constraint after zoom scaling.
- `minVisibleZoom()` → float — Returns the lower zoom threshold for anchor visibility.
- `pivot(float, float)` → AnchorWidget — Configures the normalized pivot point aligned to the anchor world position.
- `pivotX()` → float — Returns the normalized horizontal pivot coordinate (0.0=left, 0.5=center, 1.0=right).
- `pivotY()` → float — Returns the normalized vertical pivot coordinate (0.0=top, 0.5=center, 1.0=bottom).
- `projectedRootX()` → float — Returns the latest projected screen root X coordinate of the anchor point.
- `projectedRootY()` → float — Returns the latest projected screen root Y coordinate of the anchor point.
- `referenceZoom()` → float — Returns the reference zoom factor where widget renders at base screen size.
- `screenHeight()` → float — Returns the fixed screen height in pixels, or -1 if using desired size.
- `screenSize(float, float)` → AnchorWidget — Sets fixed screen dimensions in UI pixels for this anchor.
- `screenWidth()` → float — Returns the fixed screen width in pixels, or -1 if using desired size.
- `visible()` → boolean — Returns the manual visibility state of this anchor.
- `visible(boolean)` → AnchorWidget — Manually sets the visibility state of this anchor.
- `visibleZoomRange(float, float)` → AnchorWidget — Restricts anchor visibility to a specified viewport zoom range.
- `widget()` → Widget — Returns the underlying UI widget hosted and positioned by this anchor.
- `worldPosition(float, float)` → AnchorWidget — Updates the anchor position in world coordinates.
- `worldX()` → float — Returns the anchor world X coordinate.
- `worldY()` → float — Returns the anchor world Y coordinate.
- `zoomScaling()` → boolean — Returns true if widget dimensions scale proportionally with viewport zoom.
- `zoomScaling(boolean, float)` → AnchorWidget — Configures dynamic scaling of widget dimensions based on viewport zoom.

