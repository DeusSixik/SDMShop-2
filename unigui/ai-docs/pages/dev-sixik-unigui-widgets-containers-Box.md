# `Box` (extends `SurfaceWidget`)

Standard styled container combining background, texture, shader, border, radius, and child layout.

- **Purpose:** Primary concrete component for rectangular visual surfaces (cards, panels, containers) in UniGUI.

### USE WHEN

- Wrap elements with a visual background, border, or corner radius.
- Build cards, dialog backgrounds, and decorated containers.

### CRITICAL RULES & GOTCHAS:

- Inherits all SurfaceWidget chrome capabilities with surface rendering enabled by default.
- Children layout behaves as stack/overlay unless a specific layout is configured.

- **Related:** `SurfaceWidget`, `PanelWidget`, `Border`, `VBox`

### LIFECYCLE

- `Box()` — Creates an empty Box container with visual surface enabled.

### EXAMPLES

#### Create a card

```java
Box card = new Box()
        .background(ColorView.ofHex("#2F3136"))
        .radius(4.0f);
```

