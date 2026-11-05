---
title: "MapMarkerHandle"
slug: "map-marker-handle"
category: "Core/Widgets/Map"
locale: "en"
order: 0
---

# MapMarkerHandle

Handle for fluent configuration of marker positioning, screen sizing, zoom scaling, pivot, and visibility on [MapCanvas](<../../../widgets/map/map-canvas.md>).

## Overview

`MapMarkerHandle` provides a fluent configuration handle for widgets added as markers to a `MapCanvas`.

Key features:
- Repositioning in map coordinates (`position`) or projected external world coordinates (`projectedPosition`).
- Fixed screen-space sizing with min/max zoom clamping (`screenSize`, `minScreenSize`, `maxScreenSize`).
- Dynamic zoom scaling relative to a reference zoom factor (`zoomScaling`).
- Normalized pivot point adjustment (`pivot`) and viewport culling (`cullOutsideViewport`).
- Zoom-level LOD visibility ranges (`visibleZoom`).

## Fluent Marker Configuration with Handle

```java
MapMarkerHandle<MapMarker> handle = map.addMarker("home", 128.0f, 256.0f, "Base")
    .screenSize(32.0f, 32.0f)
    .pivot(0.5f, 1.0f);
```

## Related links

- [AnchorWidget](<../world/anchor-widget.md>): Spatial anchor pinning a screen-space UI widget to 3D/2D world coordinates with zoom scaling, pivot, and viewport culling.
- [MapCanvas](<../../../widgets/map/map-canvas.md>): Interactive 2D map widget extending [WorldCanvas](<../../../widgets/world/world-canvas.md>) with map sizing, texture and grid layers, coordinate projection, and marker handles.
- [MapMarker](<../../../widgets/map/map-marker.md>): Neutral base marker shell for [MapCanvas](<../../../widgets/map/map-canvas.md>) supporting custom content, text labels, selection, and highlighting.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`anchor()` {: #anchor data-toc-label="anchor()" }

Returns the underlying low-level [AnchorWidget](<../world/anchor-widget.md>) managing this marker.

Returns `AnchorWidget`: The managing [AnchorWidget](<../world/anchor-widget.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`cullOutsideViewport(boolean)` {: #culloutsideviewportboolean data-toc-label="cullOutsideViewport(boolean)" }

Enables or disables automatic culling when the marker is outside the visible viewport.

| Parameter | Type | Description |
| --- | --- | --- |
| `cullOutsideViewport` | `boolean` | Whether to automatically hide the marker when outside the viewport. |

Returns `MapMarkerHandle`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`id()` {: #id data-toc-label="id()" }

Returns the unique string identifier of this marker within the map.

Returns `String`: The string identifier or text..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`mapX()` {: #mapx data-toc-label="mapX()" }

Returns the X coordinate of the marker in map coordinate space.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`mapY()` {: #mapy data-toc-label="mapY()" }

Returns the Y coordinate of the marker in map coordinate space.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`maxScreenSize(float, float)` {: #maxscreensizefloat-float data-toc-label="maxScreenSize(float, float)" }

Sets maximum screen dimensions to prevent marker over-expanding at high zoom.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | Width dimension. |
| `height` | `float` | Height dimension. |

Returns `MapMarkerHandle`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`minScreenSize(float, float)` {: #minscreensizefloat-float data-toc-label="minScreenSize(float, float)" }

Sets minimum screen dimensions to prevent marker over-shrinking at low zoom.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | Width dimension. |
| `height` | `float` | Height dimension. |

Returns `MapMarkerHandle`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`pivot(float, float)` {: #pivotfloat-float data-toc-label="pivot(float, float)" }

Sets the normalized anchor pivot point (0.0 to 1.0) inside the marker bounds.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | Horizontal X coordinate. |
| `y` | `float` | Vertical Y coordinate. |

Returns `MapMarkerHandle`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`position(float, float)` {: #positionfloat-float data-toc-label="position(float, float)" }

Positions the marker in map coordinate space.

| Parameter | Type | Description |
| --- | --- | --- |
| `mapX` | `float` | Target X coordinate in map coordinate units. |
| `mapY` | `float` | Target Y coordinate in map coordinate units. |

Returns `MapMarkerHandle`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`priority()` {: #priority data-toc-label="priority()" }

Returns the user-defined priority value of this marker.

Returns `int`: The integer count value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`priority(int)` {: #priorityint data-toc-label="priority(int)" }

Sets the marker sorting priority value.

| Parameter | Type | Description |
| --- | --- | --- |
| `priority` | `int` | Marker sorting and clustering priority value. |

Returns `MapMarkerHandle`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`projectedPosition(float, float)` {: #projectedpositionfloat-float data-toc-label="projectedPosition(float, float)" }

Positions the marker in external world coordinates projected via [MapCanvas](<../../../widgets/map/map-canvas.md>) projection.

| Parameter | Type | Description |
| --- | --- | --- |
| `worldX` | `float` | Target X coordinate in external world units. |
| `worldY` | `float` | Target Y coordinate in external world units. |

Returns `MapMarkerHandle`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`screenSize(float, float)` {: #screensizefloat-float data-toc-label="screenSize(float, float)" }

Sets the base fixed screen dimensions of this marker in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | Width dimension. |
| `height` | `float` | Height dimension. |

Returns `MapMarkerHandle`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`visible(boolean)` {: #visibleboolean data-toc-label="visible(boolean)" }

Sets the manual visibility flag of this marker.

| Parameter | Type | Description |
| --- | --- | --- |
| `visible` | `boolean` | Visibility flag. |

Returns `MapMarkerHandle`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`visibleZoom(float, float)` {: #visiblezoomfloat-float data-toc-label="visibleZoom(float, float)" }

Restricts marker visibility to the specified zoom range.

| Parameter | Type | Description |
| --- | --- | --- |
| `minZoom` | `float` | Minimum allowed zoom scale factor. |
| `maxZoom` | `float` | Maximum allowed zoom scale factor. |

Returns `MapMarkerHandle`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`widget()` {: #widget data-toc-label="widget()" }

Returns the UI widget rendered as this marker.

Returns `Widget`: The T value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapMarkerHandle::</span>`zoomScaling(boolean, float)` {: #zoomscalingboolean-float data-toc-label="zoomScaling(boolean, float)" }

Configures dynamic scaling of the marker size relative to map zoom.

| Parameter | Type | Description |
| --- | --- | --- |
| `zoomScaling` | `boolean` | True to scale marker size dynamically with zoom. |
| `referenceZoom` | `float` | Zoom level at which marker displays at base screen dimensions. |

Returns `MapMarkerHandle`: This instance for chaining..

</div>

