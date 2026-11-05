# `MapCanvas` (extends `WorldCanvas`)

Interactive 2D map widget extending WorldCanvas with map sizing, texture and grid layers, coordinate projection, and marker handles.

### API

- `MapCanvas()` — Constructs an empty MapCanvas with default 1024x512 map dimensions.
- `MapCanvas(TextureHandle)` — Constructs a MapCanvas displaying the given texture with dimensions derived from the texture.
- `MapCanvas(TextureHandle, float, float)` — Constructs a MapCanvas displaying a texture with explicit map width and height dimensions.
- `addMarker(String, float, float, MapMarker)` → MapMarkerHandle<MapMarker> — Adds a MapMarker shell at map coordinates.
- `addMarkerWidget(String, float, float, T)` → MapMarkerHandle<T> — Adds an arbitrary UI widget as a map marker at map coordinates.
- `addProjectedMarker(String, float, float, MapMarker)` → MapMarkerHandle<MapMarker> — Adds a MapMarker shell using external world coordinates projected to map space.
- `addProjectedMarkerWidget(String, float, float, T)` → MapMarkerHandle<T> — Adds an arbitrary UI widget as a marker using external world coordinates projected to map space.
- `backgroundColor()` → MutableColor — Returns the mutable background fill color rendered outside map bounds.
- `backgroundVisible()` → boolean — Returns true if the outer background canvas fill is rendered.
- `backgroundVisible(boolean)` → MapCanvas (default: `true`) — Enables or disables outer canvas background rendering.
- `borderColor()` → MutableColor — Returns the mutable border line color surrounding the map boundaries.
- `clampToMapBounds(boolean)` → MapCanvas (default: `false`) — Clamps pan navigation within the map area bounds.
- `externalToMapX(float)` → float — Converts an external world X coordinate to map X space using the current projection.
- `externalToMapY(float)` → float — Converts an external world Y coordinate to map Y space using the current projection.
- `gridColor()` → MutableColor — Returns the mutable line color of the coordinate grid overlay.
- `gridSize()` → float — Returns the distance between grid lines in map units.
- `gridSize(float)` → MapCanvas (default: `128`) — Sets the cell spacing distance for the coordinate grid overlay.
- `gridVisible()` → boolean — Returns true if the overlay grid lines are rendered.
- `gridVisible(boolean)` → MapCanvas (default: `true`) — Enables or disables coordinate grid lines across the map.
- `mapBorderVisible()` → boolean — Returns true if the border rectangle around map boundaries is rendered.
- `mapBorderVisible(boolean)` → MapCanvas (default: `true`) — Enables or disables the border outline around the map perimeter.
- `mapColor()` → MutableColor — Returns the mutable fallback fill color used when no texture is loaded.
- `mapHeight()` → float — Returns the height dimension of the map in map coordinate units.
- `mapSize(float, float)` → MapCanvas — Sets the dimensions of the map in map coordinate units.
- `mapWidth()` → float — Returns the width dimension of the map in map coordinate units.
- `marker(String)` → MapMarkerHandle<?> — Finds an existing marker handle by its string identifier, or returns null if not found.
- `moveMarker(String, float, float)` → boolean — Moves an existing marker to new map coordinates.
- `moveProjectedMarker(String, float, float)` → boolean — Moves an existing marker to new external world coordinates projected to map space.
- `projection()` → MapProjection — Returns the active MapProjection used for external coordinate conversion.
- `projection(MapProjection)` → MapCanvas — Sets the coordinate projection converting world points to map space.
- `removeMarker(String)` → boolean — Removes a marker by its string identifier.
- `source()` → MutableRect — Returns the normalized UV source rectangle of the map texture.
- `source(float, float, float, float)` → MapCanvas — Sets the normalized UV sub-region of the texture to display.
- `texture()` → TextureHandle — Returns the texture handle of the rendered map background.
- `texture(TextureHandle)` → MapCanvas — Sets the map background texture handle.
- `tint()` → MutableColor — Returns the mutable tint color applied to the map texture.
- `viewport(Viewport2D)` → MapCanvas — Sets the 2D pan and zoom viewport model.
- `viewport(float, float)` → MapCanvas — Sets viewport center coordinates while maintaining current zoom.
- `viewport(float, float, float)` → MapCanvas — Sets viewport center coordinates and zoom level simultaneously.
- `wheelPanStep(float)` → MapCanvas (default: `32`) — Sets the distance panned per mouse wheel click.
- `zoomRange(float, float)` → MapCanvas — Sets the allowed minimum and maximum zoom scale bounds.
- `zoomStep(float)` → MapCanvas (default: `1.1`) — Sets the zoom factor multiplier applied per mouse wheel step.

