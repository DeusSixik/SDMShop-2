# `InlineContentSpan` (extends `Record`, implements `RichTextSpan`)

Atomic non-text layout item (icon, texture, badge) embedded in a RichText sequence.

- **Purpose:** Participates in line breaking and text alignment, providing fallback plain text.

### USE WHEN

- Embedding inline graphics into text without creating separate widget nodes.

- **Related:** `RichText`, `InlineContentRenderer`, `InlineContentAlignment`

