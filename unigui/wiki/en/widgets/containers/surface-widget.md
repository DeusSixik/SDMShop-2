---
title: "SurfaceWidget"
slug: "surface-widget"
category: "Widgets/Containers"
locale: "en"
order: 20
---

# SurfaceWidget

Container with a composable Surface: background colors, multi-layer textures, shaders, borders, and corner radius.

## Overview

`SurfaceWidget` is the foundation of UniGUI's modern styling system. Instead of hardcoding a specific renderer class, every `SurfaceWidget` embeds a composable `Surface`.

The background source can be a solid color, texture, or procedural GLSL shader, and can be switched dynamically via code, XML, or Theme/[StylePack](<../../core/styling/style-pack.md>) selectors without replacing the widget or renderer.

## Textures & Overlay Layers

- **Base Background**: Configured via `background(color)`, `backgroundTexture(handle)`, or `backgroundShader(handle)`.
- **Nine-Slice Slicing**: Enable via `backgroundTextureFit(ImageFit.NINE_SLICE)` and `sliceBorders(n)` for scalable panels without corner distortion.
- **Overlay Textures**: Add multiple layered textures over the background via `overlayTexture(handle)`. Each layer supports individual tint, source UVs, and fit modes.
- **Transitions & Animation**: Visual properties such as `backgroundColor`, `borderColor`, `borderWidth`, and `radius` can be smoothly transitioned using `animateBackgroundColor(...)`, `animateRadius(...)`, etc.

## Configured Surface Card

```java
Box card = new Box()
        .background(ColorView.ofHex("#1A1D24"))
        .border(ColorView.ofHex("#3A3F4D"))
        .borderWidth(1.5f)
        .radius(8.0f);
card.addChild(content);
```

## Related links

- [Box](<box.md>)
- [PanelWidget](<panel-widget.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`backgroundVisible(boolean)` {: #backgroundvisibleboolean data-toc-label="backgroundVisible(boolean)" }

**Set Background Visible**

Enables or disables background fill and texture rendering.

| Parameter | Type | Description |
| --- | --- | --- |
| `backgroundVisible` | `boolean` | true to render background |

Returns `SurfaceWidget`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`backgroundVisible()` {: #backgroundvisible data-toc-label="backgroundVisible()" }

**Get Background Visible**

Returns whether background rendering is active.

Returns `boolean`: true if background is visible.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`background(ColorView)` {: #backgroundcolorview data-toc-label="background(ColorView)" }

**Set Background**

Sets background fill color and automatically enables background visibility.

| Parameter | Type | Description |
| --- | --- | --- |
| `color` | `ColorView` | background fill color or null for transparent |

Returns `SurfaceWidget`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`borderColor(ColorView)` {: #bordercolorcolorview data-toc-label="borderColor(ColorView)" }

**Set [Border](<border.md>) Color**

Sets border stroke color and automatically enables border visibility.

| Parameter | Type | Description |
| --- | --- | --- |
| `color` | `ColorView` | border color view or hex |

Returns `SurfaceWidget`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`border(ColorView)` {: #bordercolorview data-toc-label="border(ColorView)" }

**Set [Border](<border.md>)**

Sets border stroke color and automatically enables border visibility.

| Parameter | Type | Description |
| --- | --- | --- |
| `color` | `ColorView` | border stroke color |

Returns `SurfaceWidget`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`borderVisible(boolean)` {: #bordervisibleboolean data-toc-label="borderVisible(boolean)" }

**Set [Border](<border.md>) Visible**

Enables or disables border stroke rendering.

| Parameter | Type | Description |
| --- | --- | --- |
| `borderVisible` | `boolean` | true to render border stroke |

Returns `SurfaceWidget`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`borderWidth(float)` {: #borderwidthfloat data-toc-label="borderWidth(float)" }

**Set border width**

Sets the thickness of the border stroke in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `borderWidth` | `float` | Stroke width in UI pixels |

Returns `SurfaceWidget`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`radius(float)` {: #radiusfloat data-toc-label="radius(float)" }

**Set corner radius**

Sets uniform corner radius in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `radius` | `float` | Corner radius in UI pixels |

Returns `SurfaceWidget`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`boxVisualEnabled(boolean)` {: #boxvisualenabledboolean data-toc-label="boxVisualEnabled(boolean)" }

**Set Visual Enabled**

Enables or disables all surface visual rendering (background, border, textures).

| Parameter | Type | Description |
| --- | --- | --- |
| `boxVisualEnabled` | `boolean` | true to enable surface rendering |

Returns `SurfaceWidget`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`themeEnabled(boolean)` {: #themeenabledboolean data-toc-label="themeEnabled(boolean)" }

**Set Theme Enabled**

Controls whether global Theme styles override surface properties.

| Parameter | Type | Description |
| --- | --- | --- |
| `themeEnabled` | `boolean` | true to allow global theme lookups |

Returns `SurfaceWidget`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SurfaceWidget::</span>`surface()` {: #surface data-toc-label="surface()" }

**Get Surface**

Returns the underlying composable Surface model.

Returns `Surface`: live Surface model instance.

</div>

