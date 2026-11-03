---
title: "WorldCanvas"
slug: "world-canvas"
category: "Widgets/World"
locale: "en"
order: 0
---

# WorldCanvas

Infinite 2D/3D world canvas supporting camera pan and zoom, coordinate transformations, background world layers, and pinned screen-space anchor widgets.

## Overview

`WorldCanvas` is an infinite 2D/3D camera canvas that maps world coordinates into screen-space UI pixels.

Key capabilities:
- Interactive camera panning and zooming via mouse dragging and wheel scroll (`panningEnabled`, `zoomEnabled`, `wheelPanningEnabled`).
- Viewport models and camera bounds (`viewport`, `worldBounds`, `clampToWorldBounds`).
- Coordinate transformation utilities: `worldToRoot`, `rootToWorld`, `worldToLocal`, `localToWorld`.
- Layered architecture hosting background `WorldLayer` renderers underneath interactive `AnchorLayer` widgets.

## Setting Up World Canvas with Viewport and Layers

```java
WorldCanvas canvas = new WorldCanvas()
    .viewport(0, 0, 1.0f)
    .zoomRange(0.1f, 5.0f);
canvas.anchorLayer().add("marker", 100, 200, new Button("POI"));
```

## Related links

- [AnchorLayer](<../../core/widgets/world/anchor-layer.md>): Layer managing screen-space UI widgets pinned to 3D/2D world coordinates inside WorldCanvas.
- [MapCanvas](<../map/map-canvas.md>): Interactive 2D map widget extending WorldCanvas with map sizing, texture and grid layers, coordinate projection, and marker handles.
- [RibbonLayer](<../../core/widgets/world/ribbon-layer.md>): World canvas layer rendering textured or solid ribbon lines, paths, and waypoint routes in world coordinates.
- [WorldLayer](<../../core/widgets/world/world-layer.md>): Functional contract for background or decorative rendering layers drawn inside WorldCanvas.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`addWorldLayer(WorldLayer)` {: #addworldlayerworldlayer data-toc-label="addWorldLayer(WorldLayer)" }

Registers a new world rendering layer.

| Parameter | Type | Description |
| --- | --- | --- |
| `layer` | `WorldLayer` | [WorldLayer](<../../core/widgets/world/world-layer.md>) instance to register or remove. |

Returns `WorldCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`anchorLayer()` {: #anchorlayer data-toc-label="anchorLayer()" }

Returns the [AnchorLayer](<../../core/widgets/world/anchor-layer.md>) managing world-pinned UI widgets.

Returns `AnchorLayer`: The [AnchorLayer](<../../core/widgets/world/anchor-layer.md>) managing world-pinned widgets..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`clampToWorldBounds(boolean)` {: #clamptoworldboundsboolean data-toc-label="clampToWorldBounds(boolean)" }

Enables or disables clamping of camera pan within world boundaries.

| Parameter | Type | Description |
| --- | --- | --- |
| `clampToWorldBounds` | `boolean` | Whether camera panning is clamped within world bounds. |

Returns `WorldCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`clearWorldBounds()` {: #clearworldbounds data-toc-label="clearWorldBounds()" }

Clears world boundaries allowing infinite camera panning.

Returns `WorldCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`clearWorldLayers()` {: #clearworldlayers data-toc-label="clearWorldLayers()" }

Clears all registered world rendering layers.

Returns `WorldCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`clippingEnabled()` {: #clippingenabled data-toc-label="clippingEnabled()" }

Returns true if canvas rendering is clipped to widget layout bounds.

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`clippingEnabled(boolean)` {: #clippingenabledboolean data-toc-label="clippingEnabled(boolean)" }

Enables or disables clipping of rendering to widget layout bounds.

| Parameter | Type | Description |
| --- | --- | --- |
| `clippingEnabled` | `boolean` | Whether rendering is clipped to canvas layout boundaries. |

Returns `WorldCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`consumeWheelWhileHovered()` {: #consumewheelwhilehovered data-toc-label="consumeWheelWhileHovered()" }

Returns true if mouse wheel events are consumed when hovered over this canvas.

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`consumeWheelWhileHovered(boolean)` {: #consumewheelwhilehoveredboolean data-toc-label="consumeWheelWhileHovered(boolean)" }

Sets whether mouse wheel events are swallowed when hovered.

| Parameter | Type | Description |
| --- | --- | --- |
| `consumeWheelWhileHovered` | `boolean` | Whether mouse wheel events are swallowed when hovered. |

Returns `WorldCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`localToWorldX(float)` {: #localtoworldxfloat data-toc-label="localToWorldX(float)" }

Transforms a widget local X coordinate back to world X coordinate space.

| Parameter | Type | Description |
| --- | --- | --- |
| `localX` | `float` | [Widget](<../widget.md>) local X coordinate. |

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`localToWorldY(float)` {: #localtoworldyfloat data-toc-label="localToWorldY(float)" }

Transforms a widget local Y coordinate back to world Y coordinate space.

| Parameter | Type | Description |
| --- | --- | --- |
| `localY` | `float` | [Widget](<../widget.md>) local Y coordinate. |

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`onViewportChanged(EventListener)` {: #onviewportchangedeventlistener data-toc-label="onViewportChanged(EventListener)" }

Registers a listener invoked whenever the viewport position or zoom changes.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | Listener invoked on viewport position or zoom changes. |

Returns `EventSubscription`: EventSubscription for unregistering the listener..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`panButton()` {: #panbutton data-toc-label="panButton()" }

Returns the mouse pointer button used to drag and pan.

Returns `PointerButton`: The pointer button used for drag panning..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`panButton(PointerButton)` {: #panbuttonpointerbutton data-toc-label="panButton(PointerButton)" }

Sets the mouse pointer button used to drag and pan.

| Parameter | Type | Description |
| --- | --- | --- |
| `panButton` | `PointerButton` | Pointer button used for drag panning. |

Returns `WorldCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`panningEnabled()` {: #panningenabled data-toc-label="panningEnabled()" }

Returns true if mouse dragging can pan the camera viewport.

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`panningEnabled(boolean)` {: #panningenabledboolean data-toc-label="panningEnabled(boolean)" }

Enables or disables pointer dragging to pan the viewport.

| Parameter | Type | Description |
| --- | --- | --- |
| `panningEnabled` | `boolean` | Whether pointer dragging pans the viewport. |

Returns `WorldCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`preferredHeight()` {: #preferredheight data-toc-label="preferredHeight()" }

Returns the preferred layout height of the canvas.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`preferredHeight(float)` {: #preferredheightfloat data-toc-label="preferredHeight(float)" }

Sets the preferred layout height of the canvas.

| Parameter | Type | Description |
| --- | --- | --- |
| `preferredHeight` | `float` | Preferred layout height. |

Returns `WorldCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`preferredSize(float, float)` {: #preferredsizefloat-float data-toc-label="preferredSize(float, float)" }

Sets the preferred width and height of the canvas simultaneously.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | Width dimension. |
| `height` | `float` | Height dimension. |

Returns `WorldCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`preferredWidth()` {: #preferredwidth data-toc-label="preferredWidth()" }

Returns the preferred layout width of the canvas.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`preferredWidth(float)` {: #preferredwidthfloat data-toc-label="preferredWidth(float)" }

Sets the preferred layout width of the canvas.

| Parameter | Type | Description |
| --- | --- | --- |
| `preferredWidth` | `float` | Preferred layout width. |

Returns `WorldCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`removeWorldLayer(WorldLayer)` {: #removeworldlayerworldlayer data-toc-label="removeWorldLayer(WorldLayer)" }

Removes a registered world rendering layer.

| Parameter | Type | Description |
| --- | --- | --- |
| `layer` | `WorldLayer` | [WorldLayer](<../../core/widgets/world/world-layer.md>) instance to register or remove. |

Returns `WorldCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`rootToWorldX(float)` {: #roottoworldxfloat data-toc-label="rootToWorldX(float)" }

Transforms a screen root X coordinate back to world X coordinate space.

| Parameter | Type | Description |
| --- | --- | --- |
| `rootX` | `float` | Screen root X coordinate. |

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`rootToWorldY(float)` {: #roottoworldyfloat data-toc-label="rootToWorldY(float)" }

Transforms a screen root Y coordinate back to world Y coordinate space.

| Parameter | Type | Description |
| --- | --- | --- |
| `rootY` | `float` | Screen root Y coordinate. |

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`viewport()` {: #viewport data-toc-label="viewport()" }

Returns the active 2D viewport model.

Returns `Viewport2D`: The active Viewport2D model..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`viewport(Viewport2D)` {: #viewportviewport2d data-toc-label="viewport(Viewport2D)" }

Sets the 2D pan and zoom viewport model.

| Parameter | Type | Description |
| --- | --- | --- |
| `viewport` | `Viewport2D` | 2D viewport model for pan and zoom. |

Returns `WorldCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`viewport(float, float)` {: #viewportfloat-float data-toc-label="viewport(float, float)" }

Sets viewport center coordinates while maintaining current zoom.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | Horizontal X coordinate. |
| `y` | `float` | Vertical Y coordinate. |

Returns `WorldCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`viewport(float, float, float)` {: #viewportfloat-float-float data-toc-label="viewport(float, float, float)" }

Sets viewport center coordinates and zoom level simultaneously.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | Horizontal X coordinate. |
| `y` | `float` | Vertical Y coordinate. |
| `zoom` | `float` | Zoom scale factor. |

Returns `WorldCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`wheelPanStep()` {: #wheelpanstep data-toc-label="wheelPanStep()" }

Returns the distance panned per wheel scroll click.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`wheelPanStep(float)` {: #wheelpanstepfloat data-toc-label="wheelPanStep(float)" }

Sets the distance panned per wheel scroll click.

| Parameter | Type | Description |
| --- | --- | --- |
| `wheelPanStep` | `float` | Distance panned per wheel scroll click. |

Returns `WorldCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`wheelPanningEnabled()` {: #wheelpanningenabled data-toc-label="wheelPanningEnabled()" }

Returns true if mouse wheel scrolling pans the camera.

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`wheelPanningEnabled(boolean)` {: #wheelpanningenabledboolean data-toc-label="wheelPanningEnabled(boolean)" }

Enables or disables mouse wheel scrolling to pan the camera.

| Parameter | Type | Description |
| --- | --- | --- |
| `wheelPanningEnabled` | `boolean` | Whether mouse wheel scrolling pans the viewport. |

Returns `WorldCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`worldBounds(float, float, float, float)` {: #worldboundsfloat-float-float-float data-toc-label="worldBounds(float, float, float, float)" }

Sets the rectangular world bounds constraining camera movement.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | Horizontal X coordinate. |
| `y` | `float` | Vertical Y coordinate. |
| `width` | `float` | Width dimension. |
| `height` | `float` | Height dimension. |

Returns `WorldCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`worldLayers()` {: #worldlayers data-toc-label="worldLayers()" }

Returns an unmodifiable list of all registered world rendering layers.

Returns `List`: Unmodifiable list of registered world layers..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`worldToLocalX(float)` {: #worldtolocalxfloat data-toc-label="worldToLocalX(float)" }

Transforms a world X coordinate to widget local X coordinate space.

| Parameter | Type | Description |
| --- | --- | --- |
| `worldX` | `float` | World X coordinate. |

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`worldToLocalY(float)` {: #worldtolocalyfloat data-toc-label="worldToLocalY(float)" }

Transforms a world Y coordinate to widget local Y coordinate space.

| Parameter | Type | Description |
| --- | --- | --- |
| `worldY` | `float` | World Y coordinate. |

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`worldToRootX(float)` {: #worldtorootxfloat data-toc-label="worldToRootX(float)" }

Transforms a world X coordinate to screen root X coordinate space.

| Parameter | Type | Description |
| --- | --- | --- |
| `worldX` | `float` | World X coordinate. |

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`worldToRootY(float)` {: #worldtorootyfloat data-toc-label="worldToRootY(float)" }

Transforms a world Y coordinate to screen root Y coordinate space.

| Parameter | Type | Description |
| --- | --- | --- |
| `worldY` | `float` | World Y coordinate. |

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`zoomEnabled()` {: #zoomenabled data-toc-label="zoomEnabled()" }

Returns true if mouse wheel zooming is enabled.

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`zoomEnabled(boolean)` {: #zoomenabledboolean data-toc-label="zoomEnabled(boolean)" }

Enables or disables mouse wheel zooming.

| Parameter | Type | Description |
| --- | --- | --- |
| `zoomEnabled` | `boolean` | Whether mouse wheel zooming is enabled. |

Returns `WorldCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`zoomRange(float, float)` {: #zoomrangefloat-float data-toc-label="zoomRange(float, float)" }

Sets the allowed minimum and maximum zoom scale bounds.

| Parameter | Type | Description |
| --- | --- | --- |
| `minZoom` | `float` | Minimum allowed zoom scale factor. |
| `maxZoom` | `float` | Maximum allowed zoom scale factor. |

Returns `WorldCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`zoomStep()` {: #zoomstep data-toc-label="zoomStep()" }

Returns the zoom multiplier applied per wheel tick.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WorldCanvas::</span>`zoomStep(float)` {: #zoomstepfloat data-toc-label="zoomStep(float)" }

Sets the zoom multiplier applied per wheel tick.

| Parameter | Type | Description |
| --- | --- | --- |
| `zoomStep` | `float` | Zoom multiplier applied per wheel tick. |

Returns `WorldCanvas`: This instance for chaining..

</div>

