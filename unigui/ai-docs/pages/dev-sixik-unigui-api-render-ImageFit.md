# `ImageFit` (extends `Enum`)

Aspect-ratio and sizing strategies for fitting textures inside arbitrary target rectangle bounds.

- **Purpose:** Governs how source image content conforms to widget layout dimensions.

### USE WHEN

- `STRETCH`: Scale image to completely fill destination bounds without preserving aspect ratio.
- `CONTAIN`: Scale image preserving aspect ratio until it is completely visible inside destination bounds (letterboxing).
- `COVER`: Scale image preserving aspect ratio until destination bounds are completely filled (cropping overflow).
- `NINE_SLICE`: Deconstruct texture into 4 fixed corners, 4 stretching edges, and 1 center area using slice borders.

### CRITICAL RULES & GOTCHAS:

- `NINE_SLICE` requires valid `SliceBorders` defined on the widget visual surface.
- `CONTAIN` and `COVER` calculate aspect ratios using `TextureHandle#width()` and `height()`.

- **Related:** `TexturePlacement`, `TextureHandle`, `SliceBorders`, `NineSlice`

### EXAMPLES

#### Compute contained placement

```java
TexturePlacement p = TexturePlacement.fit(icon, widgetBounds, ImageFit.CONTAIN);
scope.texture(icon, p.x(), p.y(), p.width(), p.height(), Paint.NONE);
```

