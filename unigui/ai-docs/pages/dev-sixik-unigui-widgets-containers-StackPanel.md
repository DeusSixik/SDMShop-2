# `StackPanel` (extends `PanelWidget`)

Overlay container stacking all children within a shared rectangular slot.

- **Purpose:** Lays out children layered on top of each other. Supports relative slot alignment and absolute positioning.

### USE WHEN

- Stack multiple widgets on top of each other: backdrops, images with badges, watermarks, or overlays.
- Align children independently within a shared bounding box.

### CRITICAL RULES & GOTCHAS:

- All non-collapsed children occupy the same slot and are drawn in insertion order.
- Child margin, alignment, and preferred/min/max constraints determine individual position inside the slot.

- **Related:** `PanelWidget`, `Box`, `WrapPanel`

### LAYOUT

- `arrangeChild(Widget, float, float, float, float)` → void — Arranges child in slot bounds applying margin and alignment.

### EXAMPLES

#### Stack overlay

```java
StackPanel stack = new StackPanel();
stack.addChild(background);
stack.addChild(content);
```

