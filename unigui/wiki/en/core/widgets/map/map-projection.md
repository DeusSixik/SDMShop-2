---
title: "MapProjection"
slug: "map-projection"
category: "Core/Widgets/Map"
locale: "en"
order: 0
---

# MapProjection

Axis-aligned linear projection engine converting between external world coordinates and [MapCanvas](<../../../widgets/map/map-canvas.md>) internal map coordinates.

## Overview

`MapProjection` performs fast axis-aligned linear transformations between external game world coordinates and internal `MapCanvas` map coordinates.

Transformation model:
- `mapX = worldX * scaleX + offsetX`
- `mapY = worldY * scaleY + offsetY`

Calibration:
- Direct creation via explicit scale and offset values (`MapProjection(scaleX, offsetX, scaleY, offsetY)`).
- Calibration from two calibration point pairs using `MapProjection.axisAligned(...)` or the fluent builder `MapProjection.affine()`.

## Calibrating World to Map Coordinate Projection

```java
MapProjection projection = MapProjection.affine()
    .worldPoint(-1000, -1000).mapPoint(0, 0)
    .worldPoint(1000, 1000).mapPoint(2048, 2048)
    .build();
```

## Related links

- [MapCanvas](<../../../widgets/map/map-canvas.md>): Interactive 2D map widget extending [WorldCanvas](<../../../widgets/world/world-canvas.md>) with map sizing, texture and grid layers, coordinate projection, and marker handles.
- [WorldCanvas](<../../../widgets/world/world-canvas.md>): Infinite 2D/3D world canvas supporting camera pan and zoom, coordinate transformations, background world layers, and pinned screen-space anchor widgets.

## API

<div class="api-member-card" markdown="1">

### `MapProjection(float, float, float, float)` {: #mapprojectionfloat-float-float-float data-toc-label="MapProjection(float, float, float, float)" }

Constructs a MapProjection directly with explicit scale and offset values.

| Parameter | Type | Description |
| --- | --- | --- |
| `scaleX` | `float` | Horizontal scale factor. |
| `offsetX` | `float` | Horizontal translation offset. |
| `scaleY` | `float` | Vertical scale factor. |
| `offsetY` | `float` | Vertical translation offset. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`affine()` {: #affine data-toc-label="affine()" }

Starts fluent builder configuration of a projection via world-to-map calibration points.

Returns `Builder`: A new projection Builder instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`axisAligned(float, float, float, float, float, float, float, float)` {: #axisalignedfloat-float-float-float-float-float-float-float data-toc-label="axisAligned(float, float, float, float, float, float, float, float)" }

Constructs an axis-aligned projection calibrated from two world-to-map point pairs.

| Parameter | Type | Description |
| --- | --- | --- |
| `worldX1` | `float` | World X coordinate of the first calibration point. |
| `worldY1` | `float` | World Y coordinate of the first calibration point. |
| `mapX1` | `float` | Map X coordinate corresponding to worldX1. |
| `mapY1` | `float` | Map Y coordinate corresponding to worldY1. |
| `worldX2` | `float` | World X coordinate of the second calibration point. |
| `worldY2` | `float` | World Y coordinate of the second calibration point. |
| `mapX2` | `float` | Map X coordinate corresponding to worldX2. |
| `mapY2` | `float` | Map Y coordinate corresponding to worldY2. |

Returns `MapProjection`: The constructed MapProjection instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`identity()` {: #identity data-toc-label="identity()" }

Returns the identity projection where world coordinates map 1:1 to map coordinates.

Returns `MapProjection`: The constructed MapProjection instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`mapX(float)` {: #mapxfloat data-toc-label="mapX(float)" }

Transforms an external world X coordinate into map X coordinate space.

| Parameter | Type | Description |
| --- | --- | --- |
| `worldX` | `float` | World X coordinate. |

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`mapY(float)` {: #mapyfloat data-toc-label="mapY(float)" }

Transforms an external world Y coordinate into map Y coordinate space.

| Parameter | Type | Description |
| --- | --- | --- |
| `worldY` | `float` | World Y coordinate. |

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`offsetX()` {: #offsetx data-toc-label="offsetX()" }

Returns the horizontal X translation offset.

Returns `float`: The axis translation offset..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`offsetY()` {: #offsety data-toc-label="offsetY()" }

Returns the vertical Y translation offset.

Returns `float`: The axis translation offset..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`project(float, float)` {: #projectfloat-float data-toc-label="project(float, float)" }

Transforms external world coordinates into a 2D map point.

| Parameter | Type | Description |
| --- | --- | --- |
| `worldX` | `float` | World X coordinate. |
| `worldY` | `float` | World Y coordinate. |

Returns `ViewportPoint`: Transformed 2D coordinate point..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`scaleX()` {: #scalex data-toc-label="scaleX()" }

Returns the horizontal X scale factor.

Returns `float`: The axis scale factor..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`scaleY()` {: #scaley data-toc-label="scaleY()" }

Returns the vertical Y scale factor.

Returns `float`: The axis scale factor..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`unproject(float, float)` {: #unprojectfloat-float data-toc-label="unproject(float, float)" }

Inverse transforms a map coordinate point back into external world coordinates.

| Parameter | Type | Description |
| --- | --- | --- |
| `mapX` | `float` | Map X coordinate. |
| `mapY` | `float` | Map Y coordinate. |

Returns `ViewportPoint`: Transformed 2D coordinate point..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`worldX(float)` {: #worldxfloat data-toc-label="worldX(float)" }

Inverse transforms a map X coordinate back into external world X space.

| Parameter | Type | Description |
| --- | --- | --- |
| `mapX` | `float` | Map X coordinate. |

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapProjection::</span>`worldY(float)` {: #worldyfloat data-toc-label="worldY(float)" }

Inverse transforms a map Y coordinate back into external world Y space.

| Parameter | Type | Description |
| --- | --- | --- |
| `mapY` | `float` | Map Y coordinate. |

Returns `float`: The floating-point coordinate or dimension value..

</div>

