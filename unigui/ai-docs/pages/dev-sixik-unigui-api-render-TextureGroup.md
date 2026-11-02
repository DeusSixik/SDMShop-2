# `TextureGroup`

Ordered composite group of TextureLayer instances rendered sequentially within shared base bounds.

- **Purpose:** Allows combining multiple texture layers (e.g. background plate, decorative trim, corner accents) into a single composite asset.

### USE WHEN

- Compose complex multi-layered widget surfaces using `TextureGroup.create(layers)` or `TextureGroup.builder()`.
- Build multi-textured UI cards with decoupled background, border, and highlight layers.

### CRITICAL RULES & GOTCHAS:

- Layers are rendered strictly in list order from index 0 upwards.
- The returned layers list is unmodifiable.

- **Related:** `TextureLayer`, `TextureHandle`, `Surface`

### EXAMPLES

#### Build composite card surface

```java
TextureGroup group = TextureGroup.builder()
        .add(bgTexture)
        .add(new TextureLayer(borderTexture).tint(ColorView.rgb(255, 215, 0)))
        .build();
```

