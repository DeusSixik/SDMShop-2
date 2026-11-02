# `TextureHandle`

Backend-neutral texture handle representing 2D image assets for UI drawing, batching, and sampling.

- **Purpose:** Decouples widget rendering code from low-level graphics handles (OpenGL texture IDs, Vulkan descriptors, Minecraft ResourceLocations).

### USE WHEN

- Pass texture references into `DrawScope#texture`, `DrawScope#nineSlice`, or `DrawScope#addTexturedQuad`.
- Wrap platform texture assets in cross-backend custom widgets.
- Query physical pixel dimensions (`width()`, `height()`) to compute aspect ratios and normalized UV coordinates.

### CRITICAL RULES & GOTCHAS:

- `id()` must return a stable, unique identifier string to maximize draw-call batching in render backends.
- Widgets must never cast `nativeHandle()` directly to maintain cross-platform compatibility.
- `options()` defines immutable sampling and wrapping parameters attached to the texture.

- **Related:** `SimpleTextureHandle`, `TextureRegion`, `AnimatedTexture`, `TextureClock`, `TextureOptions`, `TextureFilter`, `TextureWrap`, `NineSlice`, `DrawScope`

### EXAMPLES

#### Draw texture with normalized UV coordinates

```java
float u0 = 0.0f;
float v0 = 0.0f;
float uWidth = 1.0f;
float vHeight = 1.0f;
scope.texture(icon, x, y, width, height, u0, v0, uWidth, vHeight, paint);
```

