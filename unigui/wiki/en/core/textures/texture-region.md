---
title: "TextureRegion"
slug: "texture-region"
category: "Core/Textures"
locale: "en"
order: 52
---

# TextureRegion

Immutable UV sub-rectangle of a parent texture, used to draw sprites from texture atlases.

## Overview

TextureRegion behaves as a standard [TextureHandle](<texture-handle.md>) while representing a sub-rectangle of an atlas or sheet, allowing widgets to render individual sprites with correct aspect ratios.

## Factory Methods

Regions can be created from normalized UV coordinates (`ofUv`), pixel coordinates (`ofPixels`), or uniform grid cell indices (`ofCell`).

## UV Composition & Nesting

Regions nest seamlessly: a region of a region or of an [AnimatedTexture](<animated-texture.md>) automatically composes UV coordinates against the root physical texture.

## Extract Sprite by Pixels

```java
TextureHandle sword = TextureRegion.ofPixels(atlas, 32, 0, 16, 16);
scope.texture(sword, x, y, 32.0f, 32.0f, Paint.NONE);
```

## Extract Cell by Grid Index

```java
TextureHandle icon = TextureRegion.ofCell(sheet, 8, 8, 35);
scope.texture(icon, x, y, 16.0f, 16.0f, Paint.NONE);
```

## Related links

- [AnimatedTexture](<animated-texture.md>): Mutable sprite-sheet texture whose frame advances with game ticks, selected from a uniform cell grid.
- [NineSlice](<nine-slice.md>): Nine-patch scalable texture definition dividing an image into 4 unscaled corners, 4 single-axis edges, and 1 center area.
- [TextureHandle](<texture-handle.md>): Backend-neutral texture resource interface providing dimensions, sampling options, and native object access.

## API

<div class="api-member-card" markdown="1">

### `TextureRegion(TextureHandle, float, float, float, float)` {: #textureregiontexturehandle-float-float-float-float data-toc-label="TextureRegion(TextureHandle, float, float, float, float)" }

Constructs a texture region with normalized UV coordinates relative to parent texture.

| Parameter | Type | Description |
| --- | --- | --- |
| `parent` | `TextureHandle` | The parent texture handle. |
| `u` | `float` | The start U coordinate (0..1). |
| `v` | `float` | The start V coordinate (0..1). |
| `uWidth` | `float` | The U width extent (0..1). |
| `vHeight` | `float` | The V height extent (0..1). |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`base()` {: #base data-toc-label="base()" }

Returns the underlying physical storage texture handle.

Returns `TextureHandle`: The underlying root texture handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`height()` {: #height data-toc-label="height()" }

Returns the region height in physical pixels based on parent dimensions.

Returns `int`: The region height in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`id()` {: #id data-toc-label="id()" }

Returns the resource identifier delegated from the parent texture.

Returns `String`: The texture identifier string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`nativeHandle()` {: #nativehandle data-toc-label="nativeHandle()" }

Returns low-level platform handle delegated from the parent texture.

Returns `Object`: The platform native handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`ofCell(TextureHandle, int, int, int)` {: #ofcelltexturehandle-int-int-int data-toc-label="ofCell(TextureHandle, int, int, int)" }

Factory method extracting a single cell from a uniform grid sprite sheet by cell index.

| Parameter | Type | Description |
| --- | --- | --- |
| `parent` | `TextureHandle` | The parent texture handle. |
| `cellWidth` | `int` | The grid cell width in pixels. |
| `cellHeight` | `int` | The grid cell height in pixels. |
| `index` | `int` | The grid cell index. |

Returns `TextureRegion`: The created TextureRegion instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`ofPixels(TextureHandle, int, int, int, int)` {: #ofpixelstexturehandle-int-int-int-int data-toc-label="ofPixels(TextureHandle, int, int, int, int)" }

Factory method creating a texture region from pixel coordinates and dimensions on parent texture.

| Parameter | Type | Description |
| --- | --- | --- |
| `parent` | `TextureHandle` | The parent texture handle. |
| `x` | `int` | The left pixel coordinate. |
| `y` | `int` | The top pixel coordinate. |
| `width` | `int` | The region pixel width. |
| `height` | `int` | The region pixel height. |

Returns `TextureRegion`: The created TextureRegion instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`ofUv(TextureHandle, float, float, float, float)` {: #ofuvtexturehandle-float-float-float-float data-toc-label="ofUv(TextureHandle, float, float, float, float)" }

Factory method creating a texture region from normalized UV coordinates.

| Parameter | Type | Description |
| --- | --- | --- |
| `parent` | `TextureHandle` | The parent texture handle. |
| `u` | `float` | The start U coordinate (0..1). |
| `v` | `float` | The start V coordinate (0..1). |
| `uWidth` | `float` | The U width extent (0..1). |
| `vHeight` | `float` | The V height extent (0..1). |

Returns `TextureRegion`: The created TextureRegion instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`options()` {: #options data-toc-label="options()" }

Returns texture sampling options delegated from the parent texture.

Returns `TextureOptions`: The texture sampling options..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`parent()` {: #parent data-toc-label="parent()" }

Returns the parent texture handle containing this sub-region.

Returns `TextureHandle`: The parent texture handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`resolveUv(MutableRect)` {: #resolveuvmutablerect data-toc-label="resolveUv(MutableRect)" }

Resolves compound UV coordinates for this region relative to root physical storage.

| Parameter | Type | Description |
| --- | --- | --- |
| `out` | `MutableRect` | The mutable rectangle receiving resolved UV bounds. |

Returns `boolean`: True if UV resolution succeeded..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`u()` {: #u data-toc-label="u()" }

Returns the normalized start U coordinate within parent texture.

Returns `float`: The normalized start U coordinate..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`uWidth()` {: #uwidth data-toc-label="uWidth()" }

Returns the normalized U width extent within parent texture.

Returns `float`: The normalized U width..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`v()` {: #v data-toc-label="v()" }

Returns the normalized start V coordinate within parent texture.

Returns `float`: The normalized start V coordinate..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`vHeight()` {: #vheight data-toc-label="vHeight()" }

Returns the normalized V height extent within parent texture.

Returns `float`: The normalized V height..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextureRegion::</span>`width()` {: #width data-toc-label="width()" }

Returns the region width in physical pixels based on parent dimensions.

Returns `int`: The region width in pixels..

</div>

