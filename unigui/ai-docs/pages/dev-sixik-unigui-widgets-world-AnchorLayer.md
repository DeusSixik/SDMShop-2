# `AnchorLayer`

Layer managing screen-space UI widgets pinned to 3D/2D world coordinates inside WorldCanvas.

### API

- `add(String, float, float, Widget)` → AnchorWidget — Adds a UI widget anchored to the specified world coordinates.
- `anchor(String)` → AnchorWidget — Finds an AnchorWidget by its string identifier, or returns null if not found.
- `anchors()` → List<AnchorWidget> — Returns an unmodifiable list of all active anchors in this layer.
- `clear()` → void — Removes and detaches all anchors from this layer.
- `remove(AnchorWidget)` → boolean — Removes the specified AnchorWidget from this layer.
- `remove(String)` → boolean — Removes an anchored widget by its string identifier.
- `size()` → int — Returns the count of active anchors in this layer.

