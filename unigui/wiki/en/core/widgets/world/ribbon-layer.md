---
title: "RibbonLayer"
slug: "ribbon-layer"
category: "Core/Widgets/World"
locale: "en"
order: 0
---

# RibbonLayer

World canvas layer rendering textured or solid ribbon lines, paths, and waypoint routes in world coordinates.

## Overview

`RibbonLayer` renders continuous textured or solid ribbon lines, waypoint routes, and geometric paths within a `WorldCanvas`.

Key features:
- Textured or solid color ribbons with configurable world width and texture repeat distance (`width`, `textureLength`).
- Mesh caching: geometry is cached until points, camera viewport, or styling options change.
- Advanced styling via `RibbonOptions`: closed paths, miter limits, UV swapping/flipping, and rounded joint dots.
- Point list manipulation: `path`, `addPoint`, `clear`.

## Drawing Textured Ribbon [Path](<../../../widgets/display/path.md>)

```java
RibbonLayer ribbon = new RibbonLayer()
    .width(16.0f)
    .addPoint(0, 0)
    .addPoint(100, 150);
canvas.addWorldLayer(ribbon);
```

## Related links

- [WorldCanvas](<../../../widgets/world/world-canvas.md>): Infinite 2D/3D world canvas supporting camera pan and zoom, coordinate transformations, background world layers, and pinned screen-space anchor widgets.
- [WorldLayer](<world-layer.md>): Functional contract for background or decorative rendering layers drawn inside [WorldCanvas](<../../../widgets/world/world-canvas.md>).

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`addPoint(float, float)` {: #addpointfloat-float data-toc-label="addPoint(float, float)" }

Appends a new point in world coordinates to the end of the ribbon path.

| Parameter | Type | Description |
| --- | --- | --- |
| `worldX` | `float` | World X coordinate. |
| `worldY` | `float` | World Y coordinate. |

Returns `RibbonLayer`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`cacheHits()` {: #cachehits data-toc-label="cacheHits()" }

Returns the count of successful mesh cache hits.

Returns `long`: The integer count value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`cacheRebuilds()` {: #cacherebuilds data-toc-label="cacheRebuilds()" }

Returns the count of mesh cache rebuilds.

Returns `long`: The integer count value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`clear()` {: #clear data-toc-label="clear()" }

Clears all points from the ribbon path.

Returns `RibbonLayer`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`closed()` {: #closed data-toc-label="closed()" }

Returns true if the ribbon path forms a closed loop.

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`closed(boolean)` {: #closedboolean data-toc-label="closed(boolean)" }

Configures whether the ribbon path connects its end back to its start.

| Parameter | Type | Description |
| --- | --- | --- |
| `closed` | `boolean` | Whether the ribbon forms a closed loop. |

Returns `RibbonLayer`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`flipU()` {: #flipu data-toc-label="flipU()" }

Returns true if the horizontal U texture coordinate is inverted.

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`flipU(boolean)` {: #flipuboolean data-toc-label="flipU(boolean)" }

Configures horizontal inversion of the texture U coordinate.

| Parameter | Type | Description |
| --- | --- | --- |
| `flipU` | `boolean` | Whether to flip the horizontal U texture coordinate. |

Returns `RibbonLayer`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`flipV()` {: #flipv data-toc-label="flipV()" }

Returns true if the vertical V texture coordinate is inverted.

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`flipV(boolean)` {: #flipvboolean data-toc-label="flipV(boolean)" }

Configures vertical inversion of the texture V coordinate.

| Parameter | Type | Description |
| --- | --- | --- |
| `flipV` | `boolean` | Whether to flip the vertical V texture coordinate. |

Returns `RibbonLayer`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`invalidateCache()` {: #invalidatecache data-toc-label="invalidateCache()" }

Forces invalidation of the cached mesh geometry.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`jointDots()` {: #jointdots data-toc-label="jointDots()" }

Returns true if rounded joint filler dots are rendered at path vertices.

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`jointDots(boolean)` {: #jointdotsboolean data-toc-label="jointDots(boolean)" }

Configures rendering of rounded joint filler dots at path vertices.

| Parameter | Type | Description |
| --- | --- | --- |
| `jointDots` | `boolean` | Whether to render rounded joint dots at vertices. |

Returns `RibbonLayer`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`jointSegments()` {: #jointsegments data-toc-label="jointSegments()" }

Returns the segment count used for rounded joint circles.

Returns `int`: The integer count value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`jointSegments(int)` {: #jointsegmentsint data-toc-label="jointSegments(int)" }

Sets the segment count used for rounded joint circles.

| Parameter | Type | Description |
| --- | --- | --- |
| `jointSegments` | `int` | Number of circle segments for rounded joint dots. |

Returns `RibbonLayer`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`miterLimit()` {: #miterlimit data-toc-label="miterLimit()" }

Returns the miter join bevel threshold.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`miterLimit(float)` {: #miterlimitfloat data-toc-label="miterLimit(float)" }

Sets the miter join bevel threshold.

| Parameter | Type | Description |
| --- | --- | --- |
| `miterLimit` | `float` | Miter join bevel limit threshold. |

Returns `RibbonLayer`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`options()` {: #options data-toc-label="options()" }

Returns the active ribbon generation options.

Returns `RibbonOptions`: The active [RibbonOptions](<../../render/ribbon-options.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`options(RibbonOptions)` {: #optionsribbonoptions data-toc-label="options(RibbonOptions)" }

Sets the ribbon generation options.

| Parameter | Type | Description |
| --- | --- | --- |
| `options` | `RibbonOptions` | Ribbon options controlling rendering and tessellation. |

Returns `RibbonLayer`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`path()` {: #path data-toc-label="path()" }

Returns an unmodifiable view of all world points along the ribbon path.

Returns `List`: Unmodifiable list of world points..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`path(List)` {: #pathlist data-toc-label="path(List)" }

Replaces the entire ribbon path with the given list of world points.

| Parameter | Type | Description |
| --- | --- | --- |
| `points` | `List` | List of world coordinate points defining the ribbon path. |

Returns `RibbonLayer`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`render(WorldCanvas, DrawScope)` {: #renderworldcanvas-drawscope data-toc-label="render(WorldCanvas, DrawScope)" }

Renders the ribbon mesh into the canvas draw scope.

| Parameter | Type | Description |
| --- | --- | --- |
| `canvas` | `WorldCanvas` | [WorldCanvas](<../../../widgets/world/world-canvas.md>) hosting this ribbon layer. |
| `draw` | `DrawScope` | [DrawScope](<../../render/draw-scope.md>) for recording ribbon mesh draw commands. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`resetCacheStats()` {: #resetcachestats data-toc-label="resetCacheStats()" }

Resets mesh caching diagnostic statistics.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`swapUv()` {: #swapuv data-toc-label="swapUv()" }

Returns true if U and V texture axes are swapped.

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`swapUv(boolean)` {: #swapuvboolean data-toc-label="swapUv(boolean)" }

Configures swapping of U and V texture axes.

| Parameter | Type | Description |
| --- | --- | --- |
| `swapUv` | `boolean` | Whether to swap U and V texture axes. |

Returns `RibbonLayer`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`texture()` {: #texture data-toc-label="texture()" }

Returns the texture handle applied to this ribbon, or null for solid color fill.

Returns `TextureHandle`: The texture handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`texture(TextureHandle)` {: #texturetexturehandle data-toc-label="texture(TextureHandle)" }

Sets the texture applied to this ribbon.

| Parameter | Type | Description |
| --- | --- | --- |
| `texture` | `TextureHandle` | Texture handle. |

Returns `RibbonLayer`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`textureLength()` {: #texturelength data-toc-label="textureLength()" }

Returns the world-space distance corresponding to one full texture tile repeat.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`textureLength(float)` {: #texturelengthfloat data-toc-label="textureLength(float)" }

Sets the world-space distance for one full texture tile repeat.

| Parameter | Type | Description |
| --- | --- | --- |
| `textureLength` | `float` | Distance in world units for one full texture tile repeat. |

Returns `RibbonLayer`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`tint()` {: #tint data-toc-label="tint()" }

Returns the mutable vertex tint color of the ribbon.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`tint(ColorView)` {: #tintcolorview data-toc-label="tint(ColorView)" }

Sets the vertex tint color of the ribbon.

| Parameter | Type | Description |
| --- | --- | --- |
| `tint` | `ColorView` | Tint color. |

Returns `RibbonLayer`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`width()` {: #width data-toc-label="width()" }

Returns the ribbon width in world units.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonLayer::</span>`width(float)` {: #widthfloat data-toc-label="width(float)" }

Sets the ribbon width in world units.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | Width dimension. |

Returns `RibbonLayer`: This instance for chaining..

</div>

