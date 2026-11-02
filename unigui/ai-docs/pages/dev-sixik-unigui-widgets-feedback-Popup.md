# `Popup` (extends `Box`, implements `OverlayHostAware`)

Anchored floating popup panel positioned relative to a target anchor widget.

### API

- `Popup()` — Constructs an empty Popup.
- `Popup(Widget, Widget)` — Constructs a Popup bound to an anchor widget and displaying content.
- `anchor()` → Widget — Returns the anchor widget this popup is positioned relative to.
- `anchor(Widget)` → Popup — Sets the anchor widget this popup is positioned relative to.
- `arrangeInHost(RectView)` → void — Arranges the popup relative to its anchor within host bounds.
- `close()` → Popup — Closes the popup.
- `closeOnOutsideClick()` → boolean — Returns true if outside pointer clicks automatically dismiss the popup.
- `closeOnOutsideClick(boolean)` → Popup (default: `true`) — Sets whether outside clicks automatically close the popup.
- `content()` → Widget — Returns the content widget hosted inside this popup.
- `content(Widget)` → Popup — Sets the content widget displayed inside this popup.
- `offset(float, float)` → Popup — Sets additional displacement offsets from the computed placement in UI pixels.
- `onOpenChanged(Runnable)` → Popup — Registers a listener invoked when the open state changes.
- `open()` → Popup — Opens the popup.
- `open(boolean)` → Popup (default: `false`) — Sets the open state of the popup.
- `opened()` → boolean — Returns true if this popup is currently open.
- `padding(EdgeInsets)` → Popup (default: `6`) — Sets the interior padding around popup content.
- `placement()` → Placement — Returns the preferred anchor placement edge.
- `placement(Placement)` → Popup (default: `below`) — Sets the preferred anchor placement edge.
- `toggle()` → Popup — Toggles between open and closed states.

