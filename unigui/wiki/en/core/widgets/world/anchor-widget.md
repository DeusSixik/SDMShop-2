---
title: "AnchorWidget"
slug: "anchor-widget"
category: "Core/Widgets/World"
locale: "en"
order: 0
---

# AnchorWidget

Spatial anchor pinning a screen-space UI widget to 3D/2D world coordinates with zoom scaling, pivot, and viewport culling.

## Overview

`AnchorWidget` is the spatial controller for a UI widget pinned to a world coordinate position inside `WorldCanvas`.

Capabilities:
- World coordinate positioning (`worldPosition`, `worldX`, `worldY`).
- Screen-space dimensions independent of zoom (`screenSize`) or zoom-scaled sizing (`zoomScaling`).
- Min/max screen size clamping to prevent markers becoming unreadable at extreme zoom levels.
- Normalized pivot offset (`pivot`), visibility zoom range (`visibleZoomRange`), and off-screen viewport culling (`cullOutsideViewport`).

## Configuring Spatial Anchor Properties

```java
AnchorWidget anchor = canvas.anchorLayer().add("pin", 100, 200, new Button("Marker"))
    .screenSize(64, 24)
    .pivot(0.5f, 1.0f);
```

## Related links

- [AnchorLayer](<anchor-layer.md>): Layer managing screen-space UI widgets pinned to 3D/2D world coordinates inside [WorldCanvas](<../../../widgets/world/world-canvas.md>).
- [WorldCanvas](<../../../widgets/world/world-canvas.md>): Infinite 2D/3D world canvas supporting camera pan and zoom, coordinate transformations, background world layers, and pinned screen-space anchor widgets.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`arrangedVisible()` {: #arrangedvisible data-toc-label="arrangedVisible()" }

Returns true if the anchor is currently visible after zoom, culling, and layout passes.

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`cullOutsideViewport()` {: #culloutsideviewport data-toc-label="cullOutsideViewport()" }

Returns true if this anchor is automatically culled when outside the canvas viewport.

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`cullOutsideViewport(boolean)` {: #culloutsideviewportboolean data-toc-label="cullOutsideViewport(boolean)" }

Enables or disables automatic culling when outside the visible viewport.

| Parameter | Type | Description |
| --- | --- | --- |
| `cullOutsideViewport` | `boolean` | Whether to automatically cull this anchor when outside the canvas viewport. |

Returns `AnchorWidget`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`id()` {: #id data-toc-label="id()" }

Returns the unique identifier of this anchor within its layer.

Returns `String`: The string identifier or text..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`maxScreenHeight()` {: #maxscreenheight data-toc-label="maxScreenHeight()" }

Returns the maximum screen height constraint after zoom scaling.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`maxScreenSize(float, float)` {: #maxscreensizefloat-float data-toc-label="maxScreenSize(float, float)" }

Sets maximum screen dimensions to prevent over-expanding at high zoom.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | Width dimension. |
| `height` | `float` | Height dimension. |

Returns `AnchorWidget`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`maxScreenWidth()` {: #maxscreenwidth data-toc-label="maxScreenWidth()" }

Returns the maximum screen width constraint after zoom scaling.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`maxVisibleZoom()` {: #maxvisiblezoom data-toc-label="maxVisibleZoom()" }

Returns the upper zoom threshold for anchor visibility.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`minScreenHeight()` {: #minscreenheight data-toc-label="minScreenHeight()" }

Returns the minimum screen height constraint after zoom scaling.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`minScreenSize(float, float)` {: #minscreensizefloat-float data-toc-label="minScreenSize(float, float)" }

Sets minimum screen dimensions to prevent over-shrinking at low zoom.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | Width dimension. |
| `height` | `float` | Height dimension. |

Returns `AnchorWidget`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`minScreenWidth()` {: #minscreenwidth data-toc-label="minScreenWidth()" }

Returns the minimum screen width constraint after zoom scaling.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`minVisibleZoom()` {: #minvisiblezoom data-toc-label="minVisibleZoom()" }

Returns the lower zoom threshold for anchor visibility.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`pivot(float, float)` {: #pivotfloat-float data-toc-label="pivot(float, float)" }

Configures the normalized pivot point aligned to the anchor world position.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | Horizontal X coordinate. |
| `y` | `float` | Vertical Y coordinate. |

Returns `AnchorWidget`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`pivotX()` {: #pivotx data-toc-label="pivotX()" }

Returns the normalized horizontal pivot coordinate (0.0=left, 0.5=center, 1.0=right).

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`pivotY()` {: #pivoty data-toc-label="pivotY()" }

Returns the normalized vertical pivot coordinate (0.0=top, 0.5=center, 1.0=bottom).

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`projectedRootX()` {: #projectedrootx data-toc-label="projectedRootX()" }

Returns the latest projected screen root X coordinate of the anchor point.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`projectedRootY()` {: #projectedrooty data-toc-label="projectedRootY()" }

Returns the latest projected screen root Y coordinate of the anchor point.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`referenceZoom()` {: #referencezoom data-toc-label="referenceZoom()" }

Returns the reference zoom factor where widget renders at base screen size.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`screenHeight()` {: #screenheight data-toc-label="screenHeight()" }

Returns the fixed screen height in pixels, or -1 if using desired size.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`screenSize(float, float)` {: #screensizefloat-float data-toc-label="screenSize(float, float)" }

Sets fixed screen dimensions in UI pixels for this anchor.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | Width dimension. |
| `height` | `float` | Height dimension. |

Returns `AnchorWidget`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`screenWidth()` {: #screenwidth data-toc-label="screenWidth()" }

Returns the fixed screen width in pixels, or -1 if using desired size.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`visible()` {: #visible data-toc-label="visible()" }

Returns the manual visibility state of this anchor.

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`visible(boolean)` {: #visibleboolean data-toc-label="visible(boolean)" }

Manually sets the visibility state of this anchor.

| Parameter | Type | Description |
| --- | --- | --- |
| `visible` | `boolean` | Visibility flag. |

Returns `AnchorWidget`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`visibleZoomRange(float, float)` {: #visiblezoomrangefloat-float data-toc-label="visibleZoomRange(float, float)" }

Restricts anchor visibility to a specified viewport zoom range.

| Parameter | Type | Description |
| --- | --- | --- |
| `minZoom` | `float` | Minimum allowed zoom scale factor. |
| `maxZoom` | `float` | Maximum allowed zoom scale factor. |

Returns `AnchorWidget`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`widget()` {: #widget data-toc-label="widget()" }

Returns the underlying UI widget hosted and positioned by this anchor.

Returns `Widget`: The hosted UI widget..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`worldPosition(float, float)` {: #worldpositionfloat-float data-toc-label="worldPosition(float, float)" }

Updates the anchor position in world coordinates.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | Horizontal X coordinate. |
| `y` | `float` | Vertical Y coordinate. |

Returns `AnchorWidget`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`worldX()` {: #worldx data-toc-label="worldX()" }

Returns the anchor world X coordinate.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`worldY()` {: #worldy data-toc-label="worldY()" }

Returns the anchor world Y coordinate.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`zoomScaling()` {: #zoomscaling data-toc-label="zoomScaling()" }

Returns true if widget dimensions scale proportionally with viewport zoom.

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnchorWidget::</span>`zoomScaling(boolean, float)` {: #zoomscalingboolean-float data-toc-label="zoomScaling(boolean, float)" }

Configures dynamic scaling of widget dimensions based on viewport zoom.

| Parameter | Type | Description |
| --- | --- | --- |
| `zoomScaling` | `boolean` | True to scale widget dimensions dynamically with zoom. |
| `referenceZoom` | `float` | Reference zoom factor where widget displays at base screen size. |

Returns `AnchorWidget`: This instance for chaining..

</div>

