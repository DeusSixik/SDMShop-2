# `TextureFilter` (extends `Enum`)

Texture sampling interpolation filter governing minification and magnification algorithms.

- **Purpose:** Controls pixel interpolation when textures are rendered at scales different from their native resolution.

### USE WHEN

- Use `NEAREST` for retro pixel-art UI, crisp Minecraft textures, and 1:1 pixel alignments.
- Use `LINEAR` for smoothly scaled icons, vector rasters, photographic backdrops, and rotated badges.
- Use `NEAREST_MIPMAP_NEAREST` or `LINEAR_MIPMAP_LINEAR` for 3D world projections or heavily downscaled surfaces to eliminate moire aliasing artifacts.

### CRITICAL RULES & GOTCHAS:

- Default in UniGUI is `NEAREST` to preserve crisp Minecraft pixel-art aesthetics.
- Mipmap modes require mipmaps to be generated or provided by the graphics asset loader; if mipmaps are missing, backends fall back to standard minification.
- Magnification filter (`magFilter`) cannot use mipmaps because textures are only enlarged; `TextureOptions` automatically maps mipmap modes to non-mipmap filters for mag.

- **Related:** `TextureOptions`, `TextureHandle`, `TextureWrap`, `DrawScope`

### EXAMPLES

#### Configure linear sampling with mipmaps

```java
TextureOptions options = TextureOptions.defaults()
        .minFilter(TextureFilter.LINEAR_MIPMAP_LINEAR)
        .magFilter(TextureFilter.LINEAR);
```

