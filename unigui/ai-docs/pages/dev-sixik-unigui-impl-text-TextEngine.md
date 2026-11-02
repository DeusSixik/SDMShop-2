# `TextEngine`

Core text layout, line-wrapping, dimension measurement, and inline-content rendering engine.

- **Purpose:** Centralizes character metrics, word wrapping, alignment offsets, and atomic inline content positioning across all widgets.

### USE WHEN

- Calculating preferred or min-content size for text-containing widgets in layout phases.
- Wrapping multi-line rich text blocks to fit constrained widths.
- Drawing formatted text with alignment or mixed inline content into RenderContext or DrawScope.

### CRITICAL RULES & GOTCHAS:

- TextEngine is stateless; all state is passed via RenderContext, DrawScope, or parameter arguments.
- Plain rich text is dispatched directly to RenderBackend; rich text with inline content is decomposed into local atomic layout boxes.
- minContentWidth returns the length of the longest unbreakable word.

- **Related:** `RichText`, `FontFace`, `RenderContext`, `DrawScope`, `InlineContentSpan`

