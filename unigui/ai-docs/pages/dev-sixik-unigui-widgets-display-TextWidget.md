# `TextWidget` (extends `WidgetBase`)

Abstract foundation widget for text rendering components supporting plain strings, RichText, fonts, colors, and gradients.

- **Purpose:** Provides shared font selection, text measurement, color brushes, and gradient styling for text widgets.

### USE WHEN

- Base class for custom text rendering components.
- Applying common text styling (fonts, size, gradients) across text views.

### CRITICAL RULES & GOTCHAS:

- Supports both plain text String and styled RichText models.
- Automatically invalidates layout on font or content changes.

- **Related:** `Label`, `Text`, `TextBlock`, `RichTextView`, `FontFace`

