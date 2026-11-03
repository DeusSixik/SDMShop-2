---
title: "AnimatedTexture"
slug: "animated-texture"
category: "Core/Textures"
locale: "en"
order: 55
---

# AnimatedTexture

Mutable sprite-sheet texture whose frame advances with game ticks, selected from a uniform cell grid.

## Overview

AnimatedTexture divides a source sprite sheet into a uniform grid of cells, cycling through a defined range of frames based on game ticks without requiring widget update code.

## Grid & Sizing

Cells are sized using `setCellSize(width, height)`. Frames are numbered left-to-right, top-to-bottom across the sprite sheet columns.

## Playback & Timing

Use `setAnimation(from, to)` to select an inclusive frame range (playing backwards if `from > to`) and `setAnimation(ticksPerFrame)` to control frame duration.

## Basic Particle Sheet Animation

```java
AnimatedTexture particles = new AnimatedTexture(sheet)
        .setCellSize(8)
        .setAnimation(32, 44)
        .setAnimation(1);
scope.texture(particles, x, y, 32.0f, 32.0f, Paint.NONE);
```

## Related links

- [TextureHandle](<texture-handle.md>): Backend-neutral texture resource interface providing dimensions, sampling options, and native object access.
- [TextureRegion](<texture-region.md>): Immutable UV sub-rectangle of a parent texture, used to draw sprites from texture atlases.

## API

<div class="api-member-card" markdown="1">

### `AnimatedTexture(String, int, int)` {: #animatedtexturestring-int-int data-toc-label="AnimatedTexture(String, int, int)" }

Constructs an animated texture from a resource ID and known sheet dimensions.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | The resource identifier string. |
| `sheetWidth` | `int` | The sprite sheet width in pixels. |
| `sheetHeight` | `int` | The sprite sheet height in pixels. |

</div>

<div class="api-member-card" markdown="1">

### `AnimatedTexture(TextureHandle)` {: #animatedtexturetexturehandle data-toc-label="AnimatedTexture(TextureHandle)" }

Constructs an animated texture wrapping a sprite sheet texture handle.

| Parameter | Type | Description |
| --- | --- | --- |
| `sheet` | `TextureHandle` | The sprite sheet texture handle. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`base()` {: #base data-toc-label="base()" }

Returns the root physical texture handle resolving underlying sprite sheet storage.

Returns `TextureHandle`: The underlying base texture handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`cellHeight()` {: #cellheight data-toc-label="cellHeight()" }

Returns the configured cell height in pixels.

Returns `int`: The cell height in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`cellWidth()` {: #cellwidth data-toc-label="cellWidth()" }

Returns the configured cell width in pixels.

Returns `int`: The cell width in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`currentFrame()` {: #currentframe data-toc-label="currentFrame()" }

Returns the grid frame index corresponding to the current TextureClock tick.

Returns `int`: The calculated cell frame index..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`firstFrame()` {: #firstframe data-toc-label="firstFrame()" }

Returns the first frame index of the animation sequence.

Returns `int`: The first frame index..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`frameAt(long)` {: #frameatlong data-toc-label="frameAt(long)" }

Calculates the grid frame index for a specific absolute game tick.

| Parameter | Type | Description |
| --- | --- | --- |
| `ticks` | `long` | The absolute tick count. |

Returns `int`: The calculated cell frame index..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`frameCount()` {: #framecount data-toc-label="frameCount()" }

Returns the total number of frames in the active animation range.

Returns `int`: The total number of frames in range..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`frameUv(int, MutableRect)` {: #frameuvint-mutablerect data-toc-label="frameUv(int, MutableRect)" }

Computes UV sub-rectangle coordinates within the sprite sheet for a given frame index.

| Parameter | Type | Description |
| --- | --- | --- |
| `frame` | `int` | The cell frame index. |
| `out` | `MutableRect` | The mutable rectangle receiving UV bounds. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`height()` {: #height data-toc-label="height()" }

Returns the visual cell height in pixels.

Returns `int`: The cell height in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`id()` {: #id data-toc-label="id()" }

Returns the texture resource ID delegated from the base sprite sheet.

Returns `String`: The texture identifier string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`lastFrame()` {: #lastframe data-toc-label="lastFrame()" }

Returns the last frame index of the animation sequence.

Returns `int`: The last frame index..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`nativeHandle()` {: #nativehandle data-toc-label="nativeHandle()" }

Returns the low-level platform handle delegated from the parent sprite sheet.

Returns `Object`: The platform native texture handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`options()` {: #options data-toc-label="options()" }

Returns sampling options delegated from the parent sprite sheet.

Returns `TextureOptions`: The texture sampling options..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`resolveUv(MutableRect)` {: #resolveuvmutablerect data-toc-label="resolveUv(MutableRect)" }

Resolves normalized UV coordinates for the current animated frame composed against base storage.

| Parameter | Type | Description |
| --- | --- | --- |
| `out` | `MutableRect` | The mutable rectangle receiving UV bounds. |

Returns `boolean`: True if UV resolution succeeded..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`setAnimation(int)` {: #setanimationint data-toc-label="setAnimation(int)" }

Sets the duration of one animation frame in game ticks.

| Parameter | Type | Description |
| --- | --- | --- |
| `ticksPerFrame` | `int` | The tick duration per frame. |

Returns `AnimatedTexture`: This animated texture instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`setAnimation(int, int)` {: #setanimationint-int data-toc-label="setAnimation(int, int)" }

Sets the inclusive frame index range; playing backwards if from > to.

| Parameter | Type | Description |
| --- | --- | --- |
| `from` | `int` | The starting frame index. |
| `to` | `int` | The ending frame index. |

Returns `AnimatedTexture`: This animated texture instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`setCellSize(int)` {: #setcellsizeint data-toc-label="setCellSize(int)" }

Configures a square cell size in pixels for grid slicing.

| Parameter | Type | Description |
| --- | --- | --- |
| `size` | `int` | The cell size in pixels. |

Returns `AnimatedTexture`: This animated texture instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`setCellSize(int, int)` {: #setcellsizeint-int data-toc-label="setCellSize(int, int)" }

Configures width and height of each animation grid cell in pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `int` | The cell width in pixels. |
| `height` | `int` | The cell height in pixels. |

Returns `AnimatedTexture`: This animated texture instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`sheet()` {: #sheet data-toc-label="sheet()" }

Returns the underlying sprite sheet texture handle.

Returns `TextureHandle`: The underlying sprite sheet texture..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`ticksPerFrame()` {: #ticksperframe data-toc-label="ticksPerFrame()" }

Returns the number of game ticks each frame remains visible.

Returns `int`: The ticks per frame duration..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AnimatedTexture::</span>`width()` {: #width data-toc-label="width()" }

Returns the visual cell width in pixels.

Returns `int`: The cell width in pixels..

</div>

