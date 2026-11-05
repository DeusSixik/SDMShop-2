# `RibbonOptions`

Immutable value object configuring polyline ribbon mesh generation, corner miters, joint dots, and UV texture transforms.

- **Purpose:** Encapsulates ribbon generation parameters into an immutable structure with copy-with mutation and packed 32-bit caching keys.

### USE WHEN

- Pass customized ribbon generation settings to DrawScope#ribbon or RibbonMesh#builder.
- Rotate or flip UV textures along ribbon paths without modifying texture assets.
- Cache precomputed ribbon meshes using packed() as part of a compound cache key.

### CRITICAL RULES & GOTCHAS:

- RibbonOptions is immutable. Modifier methods return new instances or this if value unchanged.
- Miter limits less than 1.0 are automatically normalized to 1.0.
- Use swapUv(true) when ribbon texture pattern is oriented vertically.

- **Related:** `RibbonMesh`, `DrawScope`, `DrawMesh`

### EXAMPLES

#### Using Default Options

```java
RibbonOptions basic = RibbonOptions.defaults();
scope.ribbon(texture, points, 10.0f, 32.0f, ColorView.WHITE, basic);
```

#### Configuring Vertical Cable UVs and Corner Dots

```java
RibbonOptions cableOptions = RibbonOptions.defaults()
        .swapUv(true)
        .jointDots(true)
        .jointSegments(12)
        .miterLimit(3.0f);
scope.ribbon(cableTexture, wirePoints, 6.0f, 24.0f, ColorView.rgb(0, 220, 255), cableOptions);
```

