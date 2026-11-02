# `TextOverflowMode` (extends `Enum`)

Enumeration configuring visual clipping, truncation, shrinking, or marquee scrolling when text exceeds available bounds.

- **Purpose:** Dictates how renderers handle overflowing text lines in widgets without changing underlying layout measurements or plain text contents.

### USE WHEN

- Configuring text overflow handling on LabelWidget, ButtonWidget, or custom text renderers.
- Selecting ellipsis truncation (...) or hover-triggered marquee scrolling for constrained UI space.

### CRITICAL RULES & GOTCHAS:

- ELLIPSIS mode applies strictly to single-line text; for multi-line wrapped text it falls back to CLIP.
- Overflow settings affect visual rendering only; layout bounding boxes and text measurements remain unmodified.

- **Related:** `RichText`, `TextEngine`

