# `WrapPanel` (extends `PanelWidget`)

Flow container wrapping children to a new row or column when space is exceeded.

- **Purpose:** Implements multi-line flex wrapping. Distinguishes item spacing from line spacing.

### USE WHEN

- Layout tag clouds, chips, media galleries, or dynamic tool palettes that must wrap.

### CRITICAL RULES & GOTCHAS:

- FlexWrap is set to WRAP.
- spacing() governs item-to-item gap; lineSpacing() governs line-to-line gap.

- **Related:** `LinearBox`, `FlexBox`, `PanelWidget`

### LIFECYCLE

- `WrapPanel()` — Creates a WrapPanel with HORIZONTAL orientation.

### LAYOUT

- `orientation(Orientation)` → WrapPanel (default: `horizontal`) — Sets flow orientation (HORIZONTAL rows or VERTICAL columns).
- `spacing(float)` → WrapPanel (default: `0`) — Sets spacing between items along flow line.
- `lineSpacing(float)` → WrapPanel (default: `0`) — Sets spacing between wrapped lines/columns.

### EXAMPLES

#### Tag cloud

```java
WrapPanel tags = new WrapPanel().spacing(4.0f).lineSpacing(4.0f);
```

