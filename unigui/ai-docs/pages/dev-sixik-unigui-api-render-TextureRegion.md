# `TextureRegion` (implements `TextureHandle`)

Immutable UV sub-rectangle of a parent texture, used to draw sprites from texture atlases.

- **Purpose:** Lets widgets and draw calls address one sprite of an atlas as if it were a standalone texture.

### USE WHEN

- Draw a sprite from an atlas via `TextureRegion.ofPixels(atlas, x, y, w, h)`.
- Slice a uniform grid sprite sheet by cell index via `TextureRegion.ofCell(sheet, cellW, cellH, index)`.
- Pass a region anywhere a TextureHandle is accepted, including NineSlice.

### CRITICAL RULES & GOTCHAS:

- UV rectangle is normalized `0..1` relative to the parent; pixel factories convert using the parent size.
- `width()`/`height()` report the region size in pixels, `id()` and `nativeHandle()` come from the base texture.
- Regions nest: a region of a region or of an AnimatedTexture is composed against the physical texture.
- TextureWrap.REPEAT tiles the whole base texture, not the region.

- **Related:** `TextureHandle`, `AnimatedTexture`, `NineSlice`, `DrawList`

### API

- `TextureRegion(TextureHandle, float, float, float, float)` — Constructs a texture region with normalized UV coordinates relative to parent texture.
- `base()` → TextureHandle — Returns the underlying physical storage texture handle.
- `height()` → int — Returns the region height in physical pixels based on parent dimensions.
- `id()` → String — Returns the resource identifier delegated from the parent texture.
- `nativeHandle()` → Object — Returns low-level platform handle delegated from the parent texture.
- `ofCell(TextureHandle, int, int, int)` → TextureRegion — Factory method extracting a single cell from a uniform grid sprite sheet by cell index.
- `ofPixels(TextureHandle, int, int, int, int)` → TextureRegion — Factory method creating a texture region from pixel coordinates and dimensions on parent texture.
- `ofUv(TextureHandle, float, float, float, float)` → TextureRegion — Factory method creating a texture region from normalized UV coordinates.
- `options()` → TextureOptions — Returns texture sampling options delegated from the parent texture.
- `parent()` → TextureHandle — Returns the parent texture handle containing this sub-region.
- `resolveUv(MutableRect)` → boolean — Resolves compound UV coordinates for this region relative to root physical storage.
- `u()` → float — Returns the normalized start U coordinate within parent texture.
- `uWidth()` → float — Returns the normalized U width extent within parent texture.
- `v()` → float — Returns the normalized start V coordinate within parent texture.
- `vHeight()` → float — Returns the normalized V height extent within parent texture.
- `width()` → int — Returns the region width in physical pixels based on parent dimensions.

### EXAMPLES

#### Draw one sprite of an atlas

```java
TextureHandle sword = TextureRegion.ofPixels(atlas, 32, 0, 16, 16);
scope.texture(sword, x, y, 32.0f, 32.0f, Paint.NONE);
```

#### Pick a sprite from a grid by index

```java
TextureHandle icon = TextureRegion.ofCell(sheet, 8, 8, 35);
```

