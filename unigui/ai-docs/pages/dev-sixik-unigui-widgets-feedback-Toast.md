# `Toast` (extends `Box`, implements `OverlayHostAware`)

Lightweight transient message overlay that auto-dismisses after a configurable duration.

### API

- `Toast()` — Constructs an empty Toast widget initialized with default styling.
- `Toast(String)` — Constructs a Toast with the specified message text.
- `arrangeInHost(RectView)` → void — Positions the toast anchored to the bottom-right corner of the host container.
- `duration()` → float — Returns the toast lifetime in seconds.
- `duration(float)` → Toast (default: `2.5`) — Sets the toast lifetime in seconds before auto-dismissal (0 keeps it open indefinitely).
- `hide()` → Toast — Hides the toast immediately.
- `margin()` → float — Returns the margin distance from host edges in UI pixels.
- `margin(float)` → Toast (default: `10`) — Sets the margin distance from host edges in UI pixels.
- `opened()` → boolean — Returns true if the toast is currently visible and active.
- `preferredSize(float, float)` → Toast — Sets the preferred dimensions of the toast card in UI pixels.
- `show()` → Toast — Shows the toast and resets its elapsed time counter.
- `text()` → String — Returns the toast message text.
- `text(String)` → Toast — Sets the toast message text.
- `toast(String)` → Toast — Updates message text and immediately displays the toast with animation.

