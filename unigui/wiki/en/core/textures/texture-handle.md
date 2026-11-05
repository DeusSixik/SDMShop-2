---
title: "TextureHandle"
slug: "texture-handle"
category: "Core/Textures"
locale: "en"
order: 20
---

# TextureHandle

Backend-neutral texture resource interface providing dimensions, sampling options, and native object access.

## Architecture & Backend Decoupling

`TextureHandle` is UniGUI's core abstraction representing a 2D image asset.

### Backend-Neutral Design

Widgets and custom renderers in UniGUI never interact directly with low-level graphics handles such as OpenGL texture IDs, DirectX descriptors, or platform-specific objects like Minecraft's `ResourceLocation`. Instead, all rendering commands (`DrawScope#texture`, `DrawScope#nineSlice`) record a reference to `TextureHandle`.

| Characteristic | UniGUI Approach | Direct Graphics API Approach |
| --- | --- | --- |
| **Portability** | Runs unchanged across vanilla, Canvas, Iris/Oculus, or headless servers | Tied to specific OpenGL state or mod loaders |
| **Lifecycle** | Managed by UniGUI texture managers and asset providers | Manual `glGenTextures` and `glDeleteTextures` |
| **Sampling** | Immutable `TextureOptions` attached directly to the handle | Global sampler state changes in render loops |
| **Batching** | O(1) comparison via stable `id()` and packed options | Pointer or handle comparison with driver pipeline flushes |

!!! tip "Stable IDs for Pipeline Batching"
    UniGUI's render backends group consecutive textured quads into shared draw calls when they share the same `id()` and sampler state. Keep IDs consistent across frames to maximize draw-call batching efficiency.

## Dimensions & UV Mapping

`TextureHandle#width()` and `TextureHandle#height()` report the physical texel resolution of the texture resource.

### UV Normalization

When rendering sub-regions with `DrawScope#texture`, texture coordinates are expressed in normalized UV space from `0.0f` (top-left) to `1.0f` (bottom-right):

```java
// Convert pixel rectangle to normalized UV coordinates:
float u0 = srcPixelX / (float) texture.width();
float v0 = srcPixelY / (float) texture.height();
float uWidth = srcPixelWidth / (float) texture.width();
float vHeight = srcPixelHeight / (float) texture.height();

scope.texture(texture, screenX, screenY, screenW, screenH, u0, v0, uWidth, vHeight, paint);
```

For automatic aspect-ratio fitting and letterboxing without manual math, use `TexturePlacement#fit(TextureHandle, RectView, ImageFit)`.

## Sampler Options & Caching

Every `TextureHandle` exposes `options()`, returning an immutable `TextureOptions` instance.

- **Minification & Magnification Filtering**: `TextureFilter.NEAREST` for retro pixel-art or `TextureFilter.LINEAR` for smooth vector rasters.
- **UV Wrapping**: `TextureWrap.CLAMP_TO_EDGE` to eliminate border bleeding or `TextureWrap.REPEAT` for seamless wallpaper tiling.
- **Pre-computed State Key**: Options provide an integer key via `TextureOptions#packed()`, allowing backends to look up pre-created OpenGL sampler objects in O(1) time without allocating heap keys.

## Native Handle Access

`TextureHandle#nativeHandle()` returns the underlying graphics object used by the active platform:

- In Minecraft environments: Typically a `ResourceLocation` or `AbstractTexture`.
- In raw OpenGL / GLFW environments: Typically an `Integer` holding the OpenGL texture name.
- In mock / test environments: Returns `null` or a test identifier.

> [!WARNING]
> Custom widgets must avoid casting `nativeHandle()` directly. Rely exclusively on `DrawScope` methods to maintain cross-platform compatibility across client renderers and headless server environments.

## Rendering Custom Texture in [Widget](<../../widgets/widget.md>)

```java
public class IconBadgeWidget extends WidgetBase {
    private final TextureHandle icon;

    public IconBadgeWidget(TextureHandle icon) {
        this.icon = Objects.requireNonNull(icon, "icon");
    }

    @Override
    public void render(DrawScope scope) {
        RectView b = layoutBounds();
        // 1. Draw background panel
        scope.roundedRectFilled(b.x(), b.y(), b.width(), b.height(), 4.0f, ColorView.rgb(40, 44, 52));
        
        // 2. Render icon centered within widget bounds
        TexturePlacement placement = TexturePlacement.fit(icon, b, ImageFit.CONTAIN);
        scope.texture(icon, placement.x(), placement.y(), placement.width(), placement.height(), Paint.NONE);
    }
}
```

## API

### Texture Properties & Access

| Method | Description |
| --- | --- |
| [`id()`](#id) | Returns stable unique identifier of this texture asset. |
| [`width()`](#width) | Returns the physical width of the texture in texels. |
| [`height()`](#height) | Returns the physical height of the texture in texels. |
| [`options()`](#options) | Returns the sampling, filtering, and wrapping options configured for this texture. |
| [`nativeHandle()`](#nativehandle) | Returns the platform-specific or backend-specific underlying texture object, or null. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureHandle::</span>`id()` {: #id data-toc-label="id()" }

**Get Texture ID**

Returns stable unique identifier of this texture asset.

Returns `String`: Stable identifier string used for backend caching and batching..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureHandle::</span>`width()` {: #width data-toc-label="width()" }

**Get Width**

Returns the physical width of the texture in texels.

Returns `int`: Width in physical pixels/texels..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureHandle::</span>`height()` {: #height data-toc-label="height()" }

**Get Height**

Returns the physical height of the texture in texels.

Returns `int`: Height in physical pixels/texels..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureHandle::</span>`options()` {: #options data-toc-label="options()" }

**Get Options**

Returns the sampling, filtering, and wrapping options configured for this texture.

Returns `TextureOptions`: Immutable [TextureOptions](<texture-options.md>) instance..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureHandle::</span>`nativeHandle()` {: #nativehandle data-toc-label="nativeHandle()" }

**Get Native Handle**

Returns the platform-specific or backend-specific underlying texture object, or null.

Returns `Object`: Platform texture handle (e.g. GL texture ID or ResourceLocation), or null..

</div>

