# `MapMarker` (extends `Box`)

Neutral base marker shell for MapCanvas supporting custom content, text labels, selection, and highlighting.

### API

- `MapMarker()` — Constructs an empty marker shell without default surface styling.
- `MapMarker(String)` — Constructs a marker shell displaying a plain text label.
- `anchor()` → AnchorWidget — Returns the low-level AnchorWidget managing this marker positioning.
- `content()` → Widget — Returns the current content widget hosted inside this marker.
- `content(Widget)` → MapMarker — Replaces the content widget hosted inside this marker.
- `highlighted()` → boolean — Returns true if this marker is currently highlighted.
- `highlighted(boolean)` → MapMarker (default: `false`) — Sets the marker highlighted visual state.
- `label()` → Label — Returns the hosted Label widget if content is a label, or null otherwise.
- `label(String)` → MapMarker — Replaces marker content with a plain text Label widget.
- `selected()` → boolean — Returns true if this marker is currently in selected state.
- `selected(boolean)` → MapMarker (default: `false`) — Sets the marker selected visual state.

