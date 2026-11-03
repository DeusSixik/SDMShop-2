---
title: "RibbonOptions"
slug: "ribbon-options"
category: "Core/Render"
locale: "en"
order: 45
---

# RibbonOptions

Immutable value object configuring polyline ribbon mesh generation, corner miters, joint dots, and UV texture transforms.

## Overview

`RibbonOptions` groups ribbon generation parameters (closed loop, miter limit, UV swapping, axis flipping, and circular joint dots) into an immutable structure. It eliminates multi-parameter overload explosions across [DrawScope](<draw-scope.md>) and RenderContext.

## Immutability & Modification

Every modifier method returns a new `RibbonOptions` instance or returns the current instance if the requested value is unchanged. The shared baseline instance is retrieved via `RibbonOptions.defaults()`.

## Packed Bitfield & Caching

All configuration flags, quantized miter limit, and joint segment counts are packed into a single 32-bit integer via `packed()`. This allows zero-allocation cache lookups when caching precomputed ribbon meshes in widgets.

## Using Default Options

```java
RibbonOptions basic = RibbonOptions.defaults();
scope.ribbon(texture, points, 10.0f, 32.0f, ColorView.WHITE, basic);
```

## Configuring Vertical Cable UVs and Corner Dots

```java
RibbonOptions cableOptions = RibbonOptions.defaults()
    .swapUv(true)
    .jointDots(true)
    .jointSegments(12)
    .miterLimit(3.0f);
scope.ribbon(cableTexture, wirePoints, 6.0f, 24.0f, ColorView.rgb(0, 220, 255), cableOptions);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`defaults()` {: #defaults data-toc-label="defaults()" }

**defaults()**

Returns the shared default ribbon options: open path, default miter limit (4.0), no joint dots, standard UV.

Returns `RibbonOptions`: The shared default RibbonOptions instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`closed()` {: #closed data-toc-label="closed()" }

**closed()**

Checks whether the ribbon forms a closed loop.

Returns `boolean`: true if the contour is closed, connecting end to start..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`miterLimit()` {: #miterlimit data-toc-label="miterLimit()" }

**miterLimit()**

Gets the maximum miter length in half-widths of the ribbon.

Returns `float`: The miter limit, at least 1.0..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`swapUv()` {: #swapuv data-toc-label="swapUv()" }

**swapUv()**

Checks whether U and V texture coordinates are transposed.

Returns `boolean`: true if U runs along path and V across width..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`flipU()` {: #flipu data-toc-label="flipU()" }

**flipU()**

Checks whether horizontal U coordinates are inverted.

Returns `boolean`: true if U is flipped (1 - u)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`flipV()` {: #flipv data-toc-label="flipV()" }

**flipV()**

Checks whether vertical V coordinates are inverted.

Returns `boolean`: true if V is flipped (1 - v)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`jointDots()` {: #jointdots data-toc-label="jointDots()" }

**jointDots()**

Checks whether circular disks are placed at joints.

Returns `boolean`: true if joint disks are enabled..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`jointSegments()` {: #jointsegments data-toc-label="jointSegments()" }

**jointSegments()**

Gets the segment tessellation count for joint disks.

Returns `int`: Segment count, or 0 for automatic calculation..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`packed()` {: #packed data-toc-label="packed()" }

**packed()**

Returns a compact 32-bit integer encoding all boolean flags, quantized miter limit, and joint segments.

Returns `int`: Stable 32-bit cache key..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`closed(boolean)` {: #closedboolean data-toc-label="closed(boolean)" }

**closed(boolean)**

Returns options with the specified closed loop setting.

| Parameter | Type | Description |
| --- | --- | --- |
| `closed` | `boolean` | true to connect the last point to the first. |

Returns `RibbonOptions`: A new RibbonOptions instance, or this instance if unchanged..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`miterLimit(float)` {: #miterlimitfloat data-toc-label="miterLimit(float)" }

**miterLimit(float)**

Returns options with the specified miter limit.

| Parameter | Type | Description |
| --- | --- | --- |
| `miterLimit` | `float` | Miter limit in half-widths; values below 1.0 are clamped to 1.0. |

Returns `RibbonOptions`: A new RibbonOptions instance, or this instance if unchanged..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`swapUv(boolean)` {: #swapuvboolean data-toc-label="swapUv(boolean)" }

**swapUv(boolean)**

Returns options with UV coordinate swapping configured.

| Parameter | Type | Description |
| --- | --- | --- |
| `swapUv` | `boolean` | true to transpose U and V coordinates for vertical textures. |

Returns `RibbonOptions`: A new RibbonOptions instance, or this instance if unchanged..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`flipU(boolean)` {: #flipuboolean data-toc-label="flipU(boolean)" }

**flipU(boolean)**

Returns options with horizontal U inversion configured.

| Parameter | Type | Description |
| --- | --- | --- |
| `flipU` | `boolean` | true to invert U coordinates. |

Returns `RibbonOptions`: A new RibbonOptions instance, or this instance if unchanged..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`flipV(boolean)` {: #flipvboolean data-toc-label="flipV(boolean)" }

**flipV(boolean)**

Returns options with vertical V inversion configured.

| Parameter | Type | Description |
| --- | --- | --- |
| `flipV` | `boolean` | true to invert V coordinates. |

Returns `RibbonOptions`: A new RibbonOptions instance, or this instance if unchanged..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`jointDots(boolean)` {: #jointdotsboolean data-toc-label="jointDots(boolean)" }

**jointDots(boolean)**

Returns options with circular joint disks configured.

| Parameter | Type | Description |
| --- | --- | --- |
| `jointDots` | `boolean` | true to render circular disks at path joints. |

Returns `RibbonOptions`: A new RibbonOptions instance, or this instance if unchanged..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`jointSegments(int)` {: #jointsegmentsint data-toc-label="jointSegments(int)" }

**jointSegments(int)**

Returns options with the specified joint disk tessellation.

| Parameter | Type | Description |
| --- | --- | --- |
| `jointSegments` | `int` | Tessellation segments; 0 indicates auto-selection based on radius. |

Returns `RibbonOptions`: A new RibbonOptions instance, or this instance if unchanged..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`isDefault()` {: #isdefault data-toc-label="isDefault()" }

**isDefault()**

Checks whether this instance matches the default options configuration.

Returns `boolean`: true if all parameters match defaults..

</div>

