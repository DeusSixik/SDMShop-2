# `TextureWrap` (extends `Enum`)

UV coordinate wrapping behaviors when sampling textures beyond the normalized 0.0 to 1.0 UV boundary.

- **Purpose:** Defines edge tiling, repeating, or clamping behavior for UV texture coordinates.

### USE WHEN

- Use `CLAMP_TO_EDGE` for single UI icons, spritesheets, and standard 9-slice frames to prevent opposite edge bleeding.
- Use `REPEAT` for seamless scrolling backgrounds, tiled wallpaper panels, and pattern fills.
- Use `MIRRORED_REPEAT` for symmetric tiled textures where adjacent tiles mirror each other.

### CRITICAL RULES & GOTCHAS:

- Default in UniGUI is `CLAMP_TO_EDGE`.
- When using `REPEAT` or `MIRRORED_REPEAT` on atlas sub-regions, wrapping must be handled via custom shader math or standalone textures; raw GL wrap modes repeat the entire texture atlas.

- **Related:** `TextureOptions`, `TextureHandle`, `TextureFilter`, `DrawScope`

### EXAMPLES

#### Configure repeating wallpaper wrap

```java
TextureOptions tiled = TextureOptions.defaults().wrap(TextureWrap.REPEAT);
```

