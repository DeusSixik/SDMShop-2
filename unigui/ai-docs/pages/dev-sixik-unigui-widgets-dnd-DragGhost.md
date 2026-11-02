# `DragGhost`

Renderer for visual ghost representation of dragged elements displayed in the overlay layer beneath the cursor.

### API

- `render(RenderContext, DragSession)` → void — Renders the ghost visualization into the overlay render context.
- `text()` → DragGhost — Creates a lightweight text badge ghost displaying the payload preview text.
- `widget(Widget, float)` → DragGhost — Creates a ghost that clones the cached rendering of a live widget with specified opacity.

