# `Splitter` (extends `Box`)

Interactive draggable divider widget embedded between the two panes of a SplitPanel.

- **Purpose:** Provides mouse hover feedback, pointer capture, and drag-to-resize behavior for SplitPanel while visually rendering a split handle.

### USE WHEN

- Managed internally by SplitPanel; not intended for standalone instantiation in UI hierarchies.

### CRITICAL RULES & GOTCHAS:

- Do not instantiate Splitter directly or add it manually to arbitrary containers; it is managed by SplitPanel.
- Captures pointer events during dragging and computes new splitRatio values delegated to its owning SplitPanel.

- **Related:** `SplitPanel`

