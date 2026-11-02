# `Tooltip` (extends `Box`, implements `OverlayHostAware`)

Hover-activated informative tooltip box displaying text or RichText formatted content anchored to a widget.

### API

- `Tooltip()` — Constructs an empty Tooltip.
- `Tooltip(Widget, RichText)` — Constructs a Tooltip anchored to a widget with RichText.
- `Tooltip(Widget, String)` — Constructs a Tooltip anchored to a widget with plain text.
- `anchor()` → Widget — Returns the anchor widget this tooltip observes.
- `anchor(Widget)` → Tooltip — Sets the anchor widget this tooltip observes.
- `arrangeInHost(RectView)` → void — Clamps and positions the tooltip relative to pointer within host bounds.
- `maxWidth()` → float — Returns the maximum tooltip wrap width.
- `maxWidth(float)` → Tooltip (default: `220`) — Sets the maximum tooltip wrap width.
- `offset(float, float)` → Tooltip — Sets cursor displacement offset in UI pixels.
- `richText()` → RichText — Returns the formatted RichText content.
- `richText(RichText)` → Tooltip — Sets the formatted RichText content.
- `showing()` → boolean — Returns true if the tooltip is currently visible.
- `text()` → String — Returns the tooltip plain text.
- `text(String)` → Tooltip — Sets the tooltip plain text.
- `textColor()` → MutableColor — Returns the mutable text foreground color.

