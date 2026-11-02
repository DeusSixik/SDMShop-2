# `OverlayLayer` (extends `PanelWidget`)

Top-level overlay host managing modal and non-modal popups, tooltips, context menus, and floating WindowWidget instances.

### API

- `OverlayLayer()` — Constructs an empty OverlayLayer.
- `OverlayLayer(Widget)` — Constructs an OverlayLayer hosting the specified base content widget.
- `addOverlay(Widget)` → OverlayLayer — Adds a floating widget to the top of the overlay layer.
- `applyQueuedMutations()` → void — Flushes pending overlay additions and removals immediately.
- `clearOverlays()` → OverlayLayer — Removes all floating overlays currently displayed.
- `content()` → Widget — Returns the primary base content widget hosted underneath all overlays.
- `content(Widget)` → OverlayLayer — Replaces the primary base content widget.
- `modalScrimColor()` → MutableColor — Returns the mutable background scrim tint color displayed beneath modal dialogs.
- `modalScrimRenderer()` → WidgetRender — Returns the custom modal scrim background renderer, or null if default.
- `modalScrimRenderer(WidgetRender)` → OverlayLayer — Sets a custom modal scrim background renderer.
- `removeOverlay(Widget)` → OverlayLayer — Removes a floating widget from the overlay layer.
- `windowManager()` → WindowManager — Synonym returning the WindowManager controller.
- `windows()` → WindowManager — Returns the WindowManager controller governing floating windows.

