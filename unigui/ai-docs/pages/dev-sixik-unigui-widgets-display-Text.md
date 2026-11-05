# `Text` (extends `TextWidget`)

Lightweight text display widget for static or dynamic strings and RichText.

- **Purpose:** Provides simple, non-interactive text rendering for headers, status messages, and read-only labels.

### USE WHEN

- Displaying titles, status readouts, or notifications without form focus semantics.
- Lightweight alternative to Label when no associated control exists.

### CRITICAL RULES & GOTCHAS:

- Does not delegate input focus on click.
- Inherits all styling capabilities from TextWidget.

- **Related:** `TextWidget`, `Label`, `TextBlock`, `RichTextView`

