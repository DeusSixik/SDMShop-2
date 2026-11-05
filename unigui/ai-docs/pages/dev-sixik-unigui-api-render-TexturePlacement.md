# `TexturePlacement` (extends `Record`)

Immutable computed destination coordinates and normalized source UV boundaries resulting from an ImageFit calculation.

- **Purpose:** Pre-calculates final screen bounds and UV texture coordinates for immediate draw command dispatch.

### USE WHEN

- Position an image within widget bounds while honoring aspect ratio via `fit(TextureHandle, RectView, ImageFit)`.
- Extract fitted screen coordinates and UV rect to pass directly into `DrawScope#texture`.

### CRITICAL RULES & GOTCHAS:

- Screen coordinates (`x`, `y`, `width`, `height`) are in UI points, while UV parameters (`u`, `v`, `uWidth`, `vHeight`) are normalized `0.0..1.0`.
- `ImageFit.CONTAIN` centers the image inside destination bounds, letterboxing or pillarboxing empty space.
- `ImageFit.COVER` centers and crops the image UVs to completely fill destination bounds without distortion.

- **Related:** `ImageFit`, `TextureHandle`, `RectView`, `DrawScope`

### EXAMPLES

#### Compute letterboxed texture placement

```java
TexturePlacement p = TexturePlacement.fit(icon, widget.layoutBounds(), ImageFit.CONTAIN);
scope.texture(icon, p.x(), p.y(), p.width(), p.height(), p.u(), p.v(), p.uWidth(), p.vHeight(), Paint.NONE);
```

