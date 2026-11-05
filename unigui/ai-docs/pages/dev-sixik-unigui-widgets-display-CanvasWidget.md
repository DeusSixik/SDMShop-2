# `CanvasWidget` (extends `WidgetBase`)

Procedural drawing surface widget invoking custom CanvasDrawCallback painting routines.

- **Purpose:** Provides an immediate-mode 2D drawing canvas embedded directly in the widget tree.

### USE WHEN

- Custom rendering routines using DrawScope or graphics context.
- Custom animated dials, particle previews, or mini-games.

### CRITICAL RULES & GOTCHAS:

- onDraw() callbacks are invoked sequentially on each render pass.
- Remember to invalidate VISUAL when canvas state changes.

- **Related:** `CanvasDrawCallback`, `DrawScope`, `RenderContext`

