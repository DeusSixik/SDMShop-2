# `RichTextSpan`

Base interface for atoms in a RichText stream (TextRun or InlineContentSpan).

- **Purpose:** Unifies text runs and non-text inline items into a single layout sequence.

### USE WHEN

- Iterating over all atoms in a RichText via RichText#spans().

- **Related:** `RichText`, `TextRun`, `InlineContentSpan`

