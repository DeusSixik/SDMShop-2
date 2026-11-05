---
title: "TextureOptions"
slug: "texture-options"
category: "Core/Textures"
locale: "en"
order: 40
---

# TextureOptions

Immutable texture sampling configuration defining min/mag filtering, UV wrapping, mipmaps, and premultiplied alpha.

## Overview & Immutable Design

`TextureOptions` defines how GPU hardware samples a texture when rendering quads, meshes, and nine-slice panels.

### Immutable Semantics

`TextureOptions` does not own the underlying GPU resource; it only describes the desired sampler behavior. Every modifier method returns a new `TextureOptions` instance (or returns `this` if the requested value is already present), preventing accidental side-effects:

```java
TextureOptions base = TextureOptions.defaults();
TextureOptions linearWithRepeat = base.sampling(TextureFilter.LINEAR).wrap(TextureWrap.REPEAT);
```

## Filtering Modes (Nearest vs Linear)

`TextureFilter` determines pixel interpolation when scaling textures:

| Mode | Behavior | Best Used For |
| --- | --- | --- |
| `NEAREST` | Samples nearest discrete pixel without interpolation. | Pixel-art Minecraft textures, crisp item icons, UI borders. |
| `LINEAR` | Bilinear interpolation smoothly blending 4 adjacent texels. | High-res art, vector rasters, rotating elements, smooth scales. |
| `NEAREST_MIPMAP_NEAREST` | Nearest-neighbor filtering using nearest precomputed mipmap. | Heavily downscaled pixel-art surfaces. |
| `LINEAR_MIPMAP_LINEAR` | Trilinear filtering smoothly blending between mipmap levels. | 3D world projections, distant UI planes, elimination of moiré. |

## UV Wrapping & Tiling (`TextureWrap`)

Controls sampling when UV coordinates fall outside the standard `0.0..1.0` range:

- `CLAMP_TO_EDGE`: Clamps coordinates to the outermost border pixels. Default in UniGUI; prevents bleeding of opposite edges on textured panels.
- `REPEAT`: Tiles the texture infinitely. Ideal for animated scrolling backgrounds, striped caution tape, and procedural wallpapers.
- `MIRRORED_REPEAT`: Tiles the texture, mirroring orientation on alternating tiles. Ideal for symmetric organic patterns.

## Premultiplied Alpha & Mipmaps

### Premultiplied Alpha
When textures with transparent edges undergo bilinear filtering, standard straight alpha can produce dark halo artifacts because RGB channels bleed into zero-alpha black. Setting `premultipliedAlpha(true)` instructs the backend shader to blend using premultiplied alpha math (`src + dst * (1 - srcAlpha)`).

### Mipmaps
Enables automatic selection of pre-filtered lower-resolution texture copies during downscaling, eliminating crawling aliasing and shimmer artifacts.

## 10-Bit Packed Key Architecture

All sampling and wrapping parameters fit cleanly into a compact 10-bit integer key (`packed()`):

```text
Bits 0-1: MinFilter (NEAREST=0, LINEAR=1, NEAREST_MIP=2, LINEAR_MIP=3)
Bits 2-3: MagFilter (NEAREST=0, LINEAR=1)
Bits 4-5: WrapS (CLAMP=0, REPEAT=1, MIRRORED=2)
Bits 6-7: WrapT (CLAMP=0, REPEAT=1, MIRRORED=2)
Bit 8:    Mipmaps flag (0/1)
Bit 9:    Premultiplied Alpha flag (0/1)
```

Because `packed()` is a 10-bit integer, backends use a direct primitive array table (`SamplerState[1024]`) or fast switch statements, achieving O(1) cache lookups with **zero heap allocations**.

## Seamless Tiling Wallpaper Pattern

```java
// Create repeating pattern options for background panel
TextureOptions tiledOptions = TextureOptions.defaults()
        .wrap(TextureWrap.REPEAT);

TextureHandle tiledTexture = new SimpleTextureHandle("custom:wallpaper", 32, 32, null, tiledOptions);
// Draws 5x3 repeated tiles across target rect
scope.texture(tiledTexture, x, y, width, height, 0.0f, 0.0f, 5.0f, 3.0f, Paint.NONE);
```

## Smooth Scaled Vector Icon with Mipmaps

```java
// Smooth trilinear filtered options for UI icons
TextureOptions smoothOptions = TextureOptions.linear()
        .mipmaps(true)
        .premultipliedAlpha(true);
```

## API

### Presets & Default Factories

| Method | Description |
| --- | --- |
| [`defaults()`](#defaults) | Returns standard UniGUI defaults: nearest filtering, clamp-to-edge wrap, no mipmaps. |
| [`nearest()`](#nearest) | Returns options configured for pixel-perfect nearest neighbor sampling. |
| [`linear()`](#linear) | Returns options configured for smooth linear sampling and clamp-to-edge wrap. |
| [`isDefault()`](#isdefault) | Returns true if this instance matches default nearest/clamp options. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`defaults()` {: #defaults data-toc-label="defaults()" }

**Get Default Options**

Returns standard UniGUI defaults: nearest filtering, clamp-to-edge wrap, no mipmaps.

Returns `TextureOptions`: Cached default TextureOptions instance..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`nearest()` {: #nearest data-toc-label="nearest()" }

**Get Nearest Options**

Returns options configured for pixel-perfect nearest neighbor sampling.

Returns `TextureOptions`: TextureOptions with nearest filtering..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`linear()` {: #linear data-toc-label="linear()" }

**Get Linear Options**

Returns options configured for smooth linear sampling and clamp-to-edge wrap.

Returns `TextureOptions`: TextureOptions with linear filtering..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`isDefault()` {: #isdefault data-toc-label="isDefault()" }

**Check Default Configuration**

Returns true if this instance matches default nearest/clamp options.

Returns `boolean`: True if identical to TextureOptions.defaults()..

</div>

### Sampler Properties

| Method | Description |
| --- | --- |
| [`minFilter()`](#minfilter) | Returns the texture filter used when scaling down below native dimensions. |
| [`magFilter()`](#magfilter) | Returns the texture filter used when scaling up above native dimensions. |
| [`wrapS()`](#wraps) | Returns horizontal wrapping mode along the U axis. |
| [`wrapT()`](#wrapt) | Returns vertical wrapping mode along the V axis. |
| [`mipmaps()`](#mipmaps) | Returns true if mipmaps are enabled for minification filtering. |
| [`premultipliedAlpha()`](#premultipliedalpha) | Returns true if texture data contains premultiplied alpha channels. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`minFilter()` {: #minfilter data-toc-label="minFilter()" }

**Get Minification Filter**

Returns the texture filter used when scaling down below native dimensions.

Returns `TextureFilter`: Active minification TextureFilter..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`magFilter()` {: #magfilter data-toc-label="magFilter()" }

**Get Magnification Filter**

Returns the texture filter used when scaling up above native dimensions.

Returns `TextureFilter`: Active magnification TextureFilter..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`wrapS()` {: #wraps data-toc-label="wrapS()" }

**Get Wrap S**

Returns horizontal wrapping mode along the U axis.

Returns `TextureWrap`: Horizontal TextureWrap mode..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`wrapT()` {: #wrapt data-toc-label="wrapT()" }

**Get Wrap T**

Returns vertical wrapping mode along the V axis.

Returns `TextureWrap`: Vertical TextureWrap mode..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`mipmaps()` {: #mipmaps data-toc-label="mipmaps()" }

**Check Mipmaps Enabled**

Returns true if mipmaps are enabled for minification filtering.

Returns `boolean`: True if mipmaps are enabled..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`premultipliedAlpha()` {: #premultipliedalpha data-toc-label="premultipliedAlpha()" }

**Check Premultiplied Alpha**

Returns true if texture data contains premultiplied alpha channels.

Returns `boolean`: True if premultiplied alpha is configured..

</div>

### Configuration Modifiers

| Method | Description |
| --- | --- |
| [`minFilter(TextureFilter)`](#minfiltertexturefilter) | Returns a TextureOptions copy configured with the specified minification filter. |
| [`magFilter(TextureFilter)`](#magfiltertexturefilter) | Returns a TextureOptions copy configured with the specified magnification filter. |
| [`sampling(TextureFilter)`](#samplingtexturefilter) | Sets both minification and magnification filters simultaneously. |
| [`wrap(TextureWrap)`](#wraptexturewrap) | Applies the specified wrap mode to both horizontal and vertical axes. |
| [`wrap(TextureWrap, TextureWrap)`](#wraptexturewrap-texturewrap) | Applies independent wrap modes along the U (S) and V (T) axes. |
| [`mipmaps(boolean)`](#mipmapsboolean) | Enables or disables mipmap generation and sampling. |
| [`premultipliedAlpha(boolean)`](#premultipliedalphaboolean) | Configures whether texture color data uses premultiplied alpha. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`minFilter(TextureFilter)` {: #minfiltertexturefilter data-toc-label="minFilter(TextureFilter)" }

**With Minification Filter**

Returns a TextureOptions copy configured with the specified minification filter.

| Parameter | Type | Description |
| --- | --- | --- |
| `filter` | `TextureFilter` | TextureFilter interpolation mode. |

Returns `TextureOptions`: Updated TextureOptions instance..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`magFilter(TextureFilter)` {: #magfiltertexturefilter data-toc-label="magFilter(TextureFilter)" }

**With Magnification Filter**

Returns a TextureOptions copy configured with the specified magnification filter.

| Parameter | Type | Description |
| --- | --- | --- |
| `filter` | `TextureFilter` | TextureFilter interpolation mode. |

Returns `TextureOptions`: Updated TextureOptions instance..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`sampling(TextureFilter)` {: #samplingtexturefilter data-toc-label="sampling(TextureFilter)" }

**With Unified Sampling Filter**

Sets both minification and magnification filters simultaneously.

| Parameter | Type | Description |
| --- | --- | --- |
| `filter` | `TextureFilter` | TextureFilter interpolation mode. |

Returns `TextureOptions`: Updated TextureOptions instance..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`wrap(TextureWrap)` {: #wraptexturewrap data-toc-label="wrap(TextureWrap)" }

**With Uniform Wrap**

Applies the specified wrap mode to both horizontal and vertical axes.

| Parameter | Type | Description |
| --- | --- | --- |
| `wrap` | `TextureWrap` | TextureWrap mode for UV coordinates. |

Returns `TextureOptions`: Updated TextureOptions instance..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`wrap(TextureWrap, TextureWrap)` {: #wraptexturewrap-texturewrap data-toc-label="wrap(TextureWrap, TextureWrap)" }

**With Independent Axes Wrap**

Applies independent wrap modes along the U (S) and V (T) axes.

| Parameter | Type | Description |
| --- | --- | --- |
| `s` | `TextureWrap` | Horizontal TextureWrap mode along U axis. |
| `t` | `TextureWrap` | Vertical TextureWrap mode along V axis. |

Returns `TextureOptions`: Updated TextureOptions instance..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`mipmaps(boolean)` {: #mipmapsboolean data-toc-label="mipmaps(boolean)" }

**With Mipmaps**

Enables or disables mipmap generation and sampling.

| Parameter | Type | Description |
| --- | --- | --- |
| `mipmaps` | `boolean` | True to enable mipmaps, false to disable. |

Returns `TextureOptions`: Updated TextureOptions instance..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`premultipliedAlpha(boolean)` {: #premultipliedalphaboolean data-toc-label="premultipliedAlpha(boolean)" }

**With Premultiplied Alpha**

Configures whether texture color data uses premultiplied alpha.

| Parameter | Type | Description |
| --- | --- | --- |
| `premultipliedAlpha` | `boolean` | True if texture RGB data is premultiplied with alpha. |

Returns `TextureOptions`: Updated TextureOptions instance..

</div>

### Packed Key & Caching

| Method | Description |
| --- | --- |
| [`packed()`](#packed) | Returns a compact 10-bit integer key encoding all sampler and wrap parameters. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureOptions::</span>`packed()` {: #packed data-toc-label="packed()" }

**Get Packed Key**

Returns a compact 10-bit integer key encoding all sampler and wrap parameters.

Returns `int`: 10-bit integer key for O(1) state caching..

</div>

