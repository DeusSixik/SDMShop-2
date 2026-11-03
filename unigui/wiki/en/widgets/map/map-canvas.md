---
title: "MapCanvas"
slug: "map-canvas"
category: "Widgets/Map"
locale: "en"
order: 0
---

# MapCanvas

Interactive 2D map widget extending [WorldCanvas](<../world/world-canvas.md>) with map sizing, texture and grid layers, coordinate projection, and marker handles.

## Overview

`MapCanvas` is a high-level cartographic visualization widget built on top of `WorldCanvas`.

Key features:
- Map dimensions in dedicated map coordinates (`mapSize`, `mapWidth`, `mapHeight`).
- Texture and fallback color rendering with UV sub-region selection (`texture`, `source`).
- Coordinate grid and boundary outlines (`gridVisible`, `gridSize`, `mapBorderVisible`).
- Coordinate transformation between game world and map coordinates via `MapProjection`.
- Interactive marker management using `MapMarkerHandle` and `MapMarker`.

## Configuring Map Canvas and Adding Markers

```java
MapCanvas map = new MapCanvas()
    .mapSize(2048, 1024)
    .viewport(0, 0, 1.0f);
map.addMarker("spawn", 512, 256, "Spawn");
```

## Related links

- [MapMarker](<map-marker.md>): Neutral base marker shell for MapCanvas supporting custom content, text labels, selection, and highlighting.
- [MapMarkerHandle](<../../core/widgets/map/map-marker-handle.md>): Handle for fluent configuration of marker positioning, screen sizing, zoom scaling, pivot, and visibility on MapCanvas.
- [MapProjection](<../../core/widgets/map/map-projection.md>): Axis-aligned linear projection engine converting between external world coordinates and MapCanvas internal map coordinates.
- [WorldCanvas](<../world/world-canvas.md>): Infinite 2D/3D world canvas supporting camera pan and zoom, coordinate transformations, background world layers, and pinned screen-space anchor widgets.

## API

<div class="api-member-card" markdown="1">

### `MapCanvas()` {: #mapcanvas data-toc-label="MapCanvas()" }

Constructs an empty MapCanvas with default 1024x512 map dimensions.

</div>

<div class="api-member-card" markdown="1">

### `MapCanvas(TextureHandle)` {: #mapcanvastexturehandle data-toc-label="MapCanvas(TextureHandle)" }

Constructs a MapCanvas displaying the given texture with dimensions derived from the texture.

| Parameter | Type | Description |
| --- | --- | --- |
| `texture` | `TextureHandle` | Texture handle. |

</div>

<div class="api-member-card" markdown="1">

### `MapCanvas(TextureHandle, float, float)` {: #mapcanvastexturehandle-float-float data-toc-label="MapCanvas(TextureHandle, float, float)" }

Constructs a MapCanvas displaying a texture with explicit map width and height dimensions.

| Parameter | Type | Description |
| --- | --- | --- |
| `texture` | `TextureHandle` | Texture handle. |
| `mapWidth` | `float` | Width dimension. |
| `mapHeight` | `float` | Height dimension. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`addMarker(String, float, float, MapMarker)` {: #addmarkerstring-float-float-mapmarker data-toc-label="addMarker(String, float, float, MapMarker)" }

Adds a [MapMarker](<map-marker.md>) shell at map coordinates.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique string identifier. |
| `mapX` | `float` | Map X coordinate. |
| `mapY` | `float` | Map Y coordinate. |
| `marker` | `MapMarker` | [MapMarker](<map-marker.md>) instance. |

Returns `MapMarkerHandle`: The created or found [MapMarkerHandle](<../../core/widgets/map/map-marker-handle.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`addMarkerWidget(String, float, float, Widget)` {: #addmarkerwidgetstring-float-float-widget data-toc-label="addMarkerWidget(String, float, float, Widget)" }

Adds an arbitrary UI widget as a map marker at map coordinates.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique string identifier. |
| `mapX` | `float` | Map X coordinate. |
| `mapY` | `float` | Map Y coordinate. |
| `widget` | `Widget` | Hosted content widget. |

Returns `MapMarkerHandle`: The created or found [MapMarkerHandle](<../../core/widgets/map/map-marker-handle.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`addProjectedMarker(String, float, float, MapMarker)` {: #addprojectedmarkerstring-float-float-mapmarker data-toc-label="addProjectedMarker(String, float, float, MapMarker)" }

Adds a [MapMarker](<map-marker.md>) shell using external world coordinates projected to map space.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique string identifier. |
| `worldX` | `float` | World X coordinate. |
| `worldY` | `float` | World Y coordinate. |
| `marker` | `MapMarker` | [MapMarker](<map-marker.md>) instance. |

Returns `MapMarkerHandle`: The created or found [MapMarkerHandle](<../../core/widgets/map/map-marker-handle.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`addProjectedMarkerWidget(String, float, float, Widget)` {: #addprojectedmarkerwidgetstring-float-float-widget data-toc-label="addProjectedMarkerWidget(String, float, float, Widget)" }

Adds an arbitrary UI widget as a marker using external world coordinates projected to map space.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique string identifier. |
| `worldX` | `float` | World X coordinate. |
| `worldY` | `float` | World Y coordinate. |
| `widget` | `Widget` | Hosted content widget. |

Returns `MapMarkerHandle`: The created or found [MapMarkerHandle](<../../core/widgets/map/map-marker-handle.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`backgroundColor()` {: #backgroundcolor data-toc-label="backgroundColor()" }

Returns the mutable background fill color rendered outside map bounds.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`backgroundVisible()` {: #backgroundvisible data-toc-label="backgroundVisible()" }

Returns true if the outer background canvas fill is rendered.

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`backgroundVisible(boolean)` {: #backgroundvisibleboolean data-toc-label="backgroundVisible(boolean)" }

Enables or disables outer canvas background rendering.

| Parameter | Type | Description |
| --- | --- | --- |
| `backgroundVisible` | `boolean` | Parameter value for gridVisible. |

Returns `MapCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`borderColor()` {: #bordercolor data-toc-label="borderColor()" }

Returns the mutable border line color surrounding the map boundaries.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`clampToMapBounds(boolean)` {: #clamptomapboundsboolean data-toc-label="clampToMapBounds(boolean)" }

Clamps pan navigation within the map area bounds.

| Parameter | Type | Description |
| --- | --- | --- |
| `clamp` | `boolean` | Clamping flag. |

Returns `MapCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`externalToMapX(float)` {: #externaltomapxfloat data-toc-label="externalToMapX(float)" }

Converts an external world X coordinate to map X space using the current projection.

| Parameter | Type | Description |
| --- | --- | --- |
| `worldX` | `float` | World X coordinate. |

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`externalToMapY(float)` {: #externaltomapyfloat data-toc-label="externalToMapY(float)" }

Converts an external world Y coordinate to map Y space using the current projection.

| Parameter | Type | Description |
| --- | --- | --- |
| `worldY` | `float` | World Y coordinate. |

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`gridColor()` {: #gridcolor data-toc-label="gridColor()" }

Returns the mutable line color of the coordinate grid overlay.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`gridSize()` {: #gridsize data-toc-label="gridSize()" }

Returns the distance between grid lines in map units.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`gridSize(float)` {: #gridsizefloat data-toc-label="gridSize(float)" }

Sets the cell spacing distance for the coordinate grid overlay.

| Parameter | Type | Description |
| --- | --- | --- |
| `gridSize` | `float` | Cell spacing distance for the grid overlay. |

Returns `MapCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`gridVisible()` {: #gridvisible data-toc-label="gridVisible()" }

Returns true if the overlay grid lines are rendered.

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`gridVisible(boolean)` {: #gridvisibleboolean data-toc-label="gridVisible(boolean)" }

Enables or disables coordinate grid lines across the map.

| Parameter | Type | Description |
| --- | --- | --- |
| `gridVisible` | `boolean` | Parameter value for gridVisible. |

Returns `MapCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`mapBorderVisible()` {: #mapbordervisible data-toc-label="mapBorderVisible()" }

Returns true if the border rectangle around map boundaries is rendered.

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`mapBorderVisible(boolean)` {: #mapbordervisibleboolean data-toc-label="mapBorderVisible(boolean)" }

Enables or disables the border outline around the map perimeter.

| Parameter | Type | Description |
| --- | --- | --- |
| `mapBorderVisible` | `boolean` | Parameter value for gridVisible. |

Returns `MapCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`mapColor()` {: #mapcolor data-toc-label="mapColor()" }

Returns the mutable fallback fill color used when no texture is loaded.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`mapHeight()` {: #mapheight data-toc-label="mapHeight()" }

Returns the height dimension of the map in map coordinate units.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`mapSize(float, float)` {: #mapsizefloat-float data-toc-label="mapSize(float, float)" }

Sets the dimensions of the map in map coordinate units.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | Width dimension. |
| `height` | `float` | Height dimension. |

Returns `MapCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`mapWidth()` {: #mapwidth data-toc-label="mapWidth()" }

Returns the width dimension of the map in map coordinate units.

Returns `float`: The floating-point coordinate or dimension value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`marker(String)` {: #markerstring data-toc-label="marker(String)" }

Finds an existing marker handle by its string identifier, or returns null if not found.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique string identifier. |

Returns `MapMarkerHandle`: The created or found [MapMarkerHandle](<../../core/widgets/map/map-marker-handle.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`moveMarker(String, float, float)` {: #movemarkerstring-float-float data-toc-label="moveMarker(String, float, float)" }

Moves an existing marker to new map coordinates.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique string identifier. |
| `mapX` | `float` | Map X coordinate. |
| `mapY` | `float` | Map Y coordinate. |

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`moveProjectedMarker(String, float, float)` {: #moveprojectedmarkerstring-float-float data-toc-label="moveProjectedMarker(String, float, float)" }

Moves an existing marker to new external world coordinates projected to map space.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique string identifier. |
| `worldX` | `float` | World X coordinate. |
| `worldY` | `float` | World Y coordinate. |

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`projection()` {: #projection data-toc-label="projection()" }

Returns the active [MapProjection](<../../core/widgets/map/map-projection.md>) used for external coordinate conversion.

Returns `MapProjection`: The active [MapProjection](<../../core/widgets/map/map-projection.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`projection(MapProjection)` {: #projectionmapprojection data-toc-label="projection(MapProjection)" }

Sets the coordinate projection converting world points to map space.

| Parameter | Type | Description |
| --- | --- | --- |
| `projection` | `MapProjection` | Coordinate projection engine. |

Returns `MapCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`removeMarker(String)` {: #removemarkerstring data-toc-label="removeMarker(String)" }

Removes a marker by its string identifier.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique string identifier. |

Returns `boolean`: True if active, enabled, or successful..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`source()` {: #source data-toc-label="source()" }

Returns the normalized UV source rectangle of the map texture.

Returns `MutableRect`: The mutable UV source rectangle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`source(float, float, float, float)` {: #sourcefloat-float-float-float data-toc-label="source(float, float, float, float)" }

Sets the normalized UV sub-region of the texture to display.

| Parameter | Type | Description |
| --- | --- | --- |
| `u` | `float` | Normalized horizontal UV texture coordinate. |
| `v` | `float` | Normalized vertical UV texture coordinate. |
| `width` | `float` | Width dimension. |
| `height` | `float` | Height dimension. |

Returns `MapCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`texture()` {: #texture data-toc-label="texture()" }

Returns the texture handle of the rendered map background.

Returns `TextureHandle`: The texture handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`texture(TextureHandle)` {: #texturetexturehandle data-toc-label="texture(TextureHandle)" }

Sets the map background texture handle.

| Parameter | Type | Description |
| --- | --- | --- |
| `texture` | `TextureHandle` | Texture handle. |

Returns `MapCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`tint()` {: #tint data-toc-label="tint()" }

Returns the mutable tint color applied to the map texture.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`viewport(Viewport2D)` {: #viewportviewport2d data-toc-label="viewport(Viewport2D)" }

Sets the 2D pan and zoom viewport model.

| Parameter | Type | Description |
| --- | --- | --- |
| `viewport` | `Viewport2D` | Parameter value for viewport. |

Returns `MapCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`viewport(float, float)` {: #viewportfloat-float data-toc-label="viewport(float, float)" }

Sets viewport center coordinates while maintaining current zoom.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | Horizontal X coordinate. |
| `y` | `float` | Vertical Y coordinate. |

Returns `MapCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`viewport(float, float, float)` {: #viewportfloat-float-float data-toc-label="viewport(float, float, float)" }

Sets viewport center coordinates and zoom level simultaneously.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | Horizontal X coordinate. |
| `y` | `float` | Vertical Y coordinate. |
| `zoom` | `float` | Zoom scale factor. |

Returns `MapCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`wheelPanStep(float)` {: #wheelpanstepfloat data-toc-label="wheelPanStep(float)" }

Sets the distance panned per mouse wheel click.

| Parameter | Type | Description |
| --- | --- | --- |
| `wheelPanStep` | `float` | Parameter value for wheelPanStep. |

Returns `MapCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`zoomRange(float, float)` {: #zoomrangefloat-float data-toc-label="zoomRange(float, float)" }

Sets the allowed minimum and maximum zoom scale bounds.

| Parameter | Type | Description |
| --- | --- | --- |
| `minZoom` | `float` | Minimum allowed zoom scale factor. |
| `maxZoom` | `float` | Maximum allowed zoom scale factor. |

Returns `MapCanvas`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MapCanvas::</span>`zoomStep(float)` {: #zoomstepfloat data-toc-label="zoomStep(float)" }

Sets the zoom factor multiplier applied per mouse wheel step.

| Parameter | Type | Description |
| --- | --- | --- |
| `zoomStep` | `float` | Parameter value for zoomStep. |

Returns `MapCanvas`: This instance for chaining..

</div>

