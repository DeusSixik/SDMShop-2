# `CanvasDrawCallback`

Functional interface for drawing custom 2D graphics into a CanvasWidget.

- **Purpose:** Supplies custom drawing commands executed during the render pass of a CanvasWidget.

### USE WHEN

- Supplying a lambda or method reference to CanvasWidget.onDraw().

### CRITICAL RULES & GOTCHAS:

- RenderContext provides layout bounds and transformation matrix.

- **Related:** `CanvasWidget`, `RenderContext`

