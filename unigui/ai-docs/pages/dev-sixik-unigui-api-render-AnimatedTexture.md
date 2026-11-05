# `AnimatedTexture` (implements `TextureHandle`)

Mutable sprite-sheet texture whose frame advances with game ticks, selected from a uniform cell grid.

- **Purpose:** Provides animated icons, spinners and particles from a single sheet without any per-widget update code.

### USE WHEN

- Show a looping frame animation from a sprite sheet in TextureWidget, ImageView or DrawScope#texture.
- Play a frame range backwards by passing `from > to` to `setAnimation(from, to)`.
- Drive animation from the game tick counter by installing it with `TextureClock.source`.

### CRITICAL RULES & GOTCHAS:

- Frames are numbered left-to-right, top-to-bottom over `columns = sheetWidth / cellWidth` columns.
- Without `setCellSize` the whole sheet is one frame; without `setAnimation` frames `0..0` play.
- Frame indices beyond the grid are clamped to the last cell instead of failing at render time.
- The sheet needs known pixel dimensions: the grid cannot be computed from an id alone.

- **Related:** `TextureHandle`, `TextureRegion`, `TextureClock`, `SimpleTextureHandle`

### API

- `AnimatedTexture(String, int, int)` — Constructs an animated texture from a resource ID and known sheet dimensions.
- `AnimatedTexture(TextureHandle)` — Constructs an animated texture wrapping a sprite sheet texture handle.
- `base()` → TextureHandle — Returns the root physical texture handle resolving underlying sprite sheet storage.
- `cellHeight()` → int — Returns the configured cell height in pixels.
- `cellWidth()` → int — Returns the configured cell width in pixels.
- `currentFrame()` → int — Returns the grid frame index corresponding to the current TextureClock tick.
- `firstFrame()` → int — Returns the first frame index of the animation sequence.
- `frameAt(long)` → int — Calculates the grid frame index for a specific absolute game tick.
- `frameCount()` → int — Returns the total number of frames in the active animation range.
- `frameUv(int, MutableRect)` → void — Computes UV sub-rectangle coordinates within the sprite sheet for a given frame index.
- `height()` → int — Returns the visual cell height in pixels.
- `id()` → String — Returns the texture resource ID delegated from the base sprite sheet.
- `lastFrame()` → int — Returns the last frame index of the animation sequence.
- `nativeHandle()` → Object — Returns the low-level platform handle delegated from the parent sprite sheet.
- `options()` → TextureOptions — Returns sampling options delegated from the parent sprite sheet.
- `resolveUv(MutableRect)` → boolean — Resolves normalized UV coordinates for the current animated frame composed against base storage.
- `setAnimation(int)` → AnimatedTexture — Sets the duration of one animation frame in game ticks.
- `setAnimation(int, int)` → AnimatedTexture — Sets the inclusive frame index range; playing backwards if from > to.
- `setCellSize(int)` → AnimatedTexture — Configures a square cell size in pixels for the grid slicing.
- `setCellSize(int, int)` → AnimatedTexture — Configures width and height of each animation grid cell in pixels.
- `sheet()` → TextureHandle — Returns the underlying sprite sheet texture handle.
- `ticksPerFrame()` → int — Returns the number of game ticks each frame remains visible.
- `width()` → int — Returns the visual cell width in pixels.

### EXAMPLES

#### Animate frames 32..44 of a particle sheet, 1 tick per frame

```java
AnimatedTexture particles = new AnimatedTexture(sheet)
        .setCellSize(8)
        .setAnimation(32, 44)
        .setAnimation(1);
scope.texture(particles, x, y, 32.0f, 32.0f, Paint.NONE);
```

