# `WorldCanvas` (extends `WidgetBase`)

Infinite 2D/3D world canvas supporting camera pan and zoom, coordinate transformations, background world layers, and pinned screen-space anchor widgets.

### API

- `addWorldLayer(WorldLayer)` → WorldCanvas — Registers a new world rendering layer.
- `anchorLayer()` → AnchorLayer — Returns the AnchorLayer managing world-pinned UI widgets.
- `clampToWorldBounds(boolean)` → WorldCanvas (default: `false`) — Enables or disables clamping of camera pan within world boundaries.
- `clearWorldBounds()` → WorldCanvas — Clears world boundaries allowing infinite camera panning.
- `clearWorldLayers()` → WorldCanvas — Clears all registered world rendering layers.
- `clippingEnabled()` → boolean — Returns true if canvas rendering is clipped to widget layout bounds.
- `clippingEnabled(boolean)` → WorldCanvas (default: `true`) — Enables or disables clipping of rendering to widget layout bounds.
- `consumeWheelWhileHovered()` → boolean — Returns true if mouse wheel events are consumed when hovered over this canvas.
- `consumeWheelWhileHovered(boolean)` → WorldCanvas (default: `true`) — Sets whether mouse wheel events are swallowed when hovered.
- `localToWorldX(float)` → float — Transforms a widget local X coordinate back to world X coordinate space.
- `localToWorldY(float)` → float — Transforms a widget local Y coordinate back to world Y coordinate space.
- `onViewportChanged(EventListener<? super ViewportChangedEvent>)` → EventSubscription — Registers a listener invoked whenever the viewport position or zoom changes.
- `panButton()` → PointerButton — Returns the mouse pointer button used to drag and pan.
- `panButton(PointerButton)` → WorldCanvas (default: `primary`) — Sets the mouse pointer button used to drag and pan.
- `panningEnabled()` → boolean — Returns true if mouse dragging can pan the camera viewport.
- `panningEnabled(boolean)` → WorldCanvas (default: `true`) — Enables or disables pointer dragging to pan the viewport.
- `preferredHeight()` → float — Returns the preferred layout height of the canvas.
- `preferredHeight(float)` → WorldCanvas (default: `240`) — Sets the preferred layout height of the canvas.
- `preferredSize(float, float)` → WorldCanvas — Sets the preferred width and height of the canvas simultaneously.
- `preferredWidth()` → float — Returns the preferred layout width of the canvas.
- `preferredWidth(float)` → WorldCanvas (default: `320`) — Sets the preferred layout width of the canvas.
- `removeWorldLayer(WorldLayer)` → WorldCanvas — Removes a registered world rendering layer.
- `rootToWorldX(float)` → float — Transforms a screen root X coordinate back to world X coordinate space.
- `rootToWorldY(float)` → float — Transforms a screen root Y coordinate back to world Y coordinate space.
- `viewport()` → Viewport2D — Returns the active 2D viewport model.
- `viewport(Viewport2D)` → WorldCanvas — Sets the 2D pan and zoom viewport model.
- `viewport(float, float)` → WorldCanvas — Sets viewport center coordinates while maintaining current zoom.
- `viewport(float, float, float)` → WorldCanvas — Sets viewport center coordinates and zoom level simultaneously.
- `wheelPanStep()` → float — Returns the distance panned per wheel scroll click.
- `wheelPanStep(float)` → WorldCanvas (default: `32`) — Sets the distance panned per wheel scroll click.
- `wheelPanningEnabled()` → boolean — Returns true if mouse wheel scrolling pans the camera.
- `wheelPanningEnabled(boolean)` → WorldCanvas (default: `true`) — Enables or disables mouse wheel scrolling to pan the camera.
- `worldBounds(float, float, float, float)` → WorldCanvas — Sets the rectangular world bounds constraining camera movement.
- `worldLayers()` → List<WorldLayer> — Returns an unmodifiable list of all registered world rendering layers.
- `worldToLocalX(float)` → float — Transforms a world X coordinate to widget local X coordinate space.
- `worldToLocalY(float)` → float — Transforms a world Y coordinate to widget local Y coordinate space.
- `worldToRootX(float)` → float — Transforms a world X coordinate to screen root X coordinate space.
- `worldToRootY(float)` → float — Transforms a world Y coordinate to screen root Y coordinate space.
- `zoomEnabled()` → boolean — Returns true if mouse wheel zooming is enabled.
- `zoomEnabled(boolean)` → WorldCanvas (default: `true`) — Enables or disables mouse wheel zooming.
- `zoomRange(float, float)` → WorldCanvas — Sets the allowed minimum and maximum zoom scale bounds.
- `zoomStep()` → float — Returns the zoom multiplier applied per wheel tick.
- `zoomStep(float)` → WorldCanvas (default: `1.1`) — Sets the zoom multiplier applied per wheel tick.

