---
title: "NineSlice"
slug: "nine-slice"
category: "Core/Textures"
locale: "en"
order: 50
---

# NineSlice

Nine-patch scalable texture definition dividing an image into 4 unscaled corners, 4 single-axis edges, and 1 center area.

## Overview & 9-Patch Grid

`NineSlice` is UniGUI's model for scalable rectangular GUI elements (window chrome, button backgrounds, dialogue plates, and tooltips).

### The 9-Slice Slicing Grid

An image is divided into nine rectangular regions based on four border inset measurements:

```text
+------------+--------------------+------------+
| Top-Left   | Top Edge           | Top-Right  |
| (fixed)    | (stretched horiz)  | (fixed)    |
+------------+--------------------+------------+
| Left Edge  | Center             | Right Edge |
| (stretch V)| (stretched both)   | (stretch V)|
+------------+--------------------+------------+
| Bottom-Left| Bottom Edge        | Bottom-Right|
| (fixed)    | (stretched horiz)  | (fixed)    |
+------------+--------------------+------------+
```

Because the 4 corners remain at their 1:1 pixel scale, visual corners never blur or distort regardless of the target widget dimensions.

## Texture Pixels vs UI Screen Points

All border inset parameters in `NineSlice` (`left`, `top`, `right`, `bottom`) are defined in **source texture pixels**, not in UI points.

```java
// Slices a 32x32 texture where corners are 6x6 pixels:
NineSlice panel = NineSlice.of(windowTexture, 6.0f);
```

When drawing via `DrawScope#nineSlice(panel, x, y, width, height, paint, fillCenter)`, UniGUI maps these pixel offsets directly to target UI screen dimensions.

## Proportional Scale-Down (`scaleToFit`)

If a widget's target width or height is smaller than the combined corner borders (`width < horizontal()` or `height < vertical()`), unadjusted rendering would cause corners to overlap or invert.

`NineSlice#scaleToFit(targetWidth, targetHeight)` computes a uniform scale factor between `0.0f` and `1.0f`:

```java
float scale = slice.scaleToFit(destWidth, destHeight);
// Automatically compresses corner dimensions when space is tight:
float effectiveLeft = slice.left() * scale;
```

This guarantees that small buttons or compact tabs remain visually crisp without corner overlap.

## Solid Panels vs Hollow Outlines

`DrawScope#nineSlice` accepts a boolean flag `fillCenter`:

- `fillCenter = true`: Renders all 9 quads (corners, edges, and center). Standard for solid dialogues and inventory panels.
- `fillCenter = false`: Omits the central quad (renders only the 8 border quads). Ideal for hollow selection rectangles, slot highlights, and transparent focus rings.

## Scalable Dialog Panel

```java
NineSlice dialogSlice = NineSlice.of(dialogTexture, 8.0f);

// Draw solid window background
scope.nineSlice(dialogSlice, b.x(), b.y(), b.width(), b.height(), Paint.NONE, true);
```

## Hollow Selection [Box](<../../widgets/containers/box.md>)

```java
NineSlice selectionSlice = NineSlice.of(selectionTexture, 3.0f);

// Draw hollow outline without filling center
scope.nineSlice(selectionSlice, slot.x() - 1, slot.y() - 1, 20.0f, 20.0f, Paint.NONE, false);
```

## API

### Factory Methods

| Method | Description |
| --- | --- |
| [`of(TextureHandle, float)`](#oftexturehandle-float) | Constructs a NineSlice instance with identical border insets on all four sides. |
| [`of(TextureHandle, float, float, float, float)`](#oftexturehandle-float-float-float-float) | Constructs a NineSlice with independent left, top, right, and bottom border insets. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NineSlice::</span>`of(TextureHandle, float)` {: #oftexturehandle-float data-toc-label="of(TextureHandle, float)" }

**Create Uniform NineSlice**

Constructs a NineSlice instance with identical border insets on all four sides.

| Parameter | Type | Description |
| --- | --- | --- |
| `texture` | `TextureHandle` | Source texture handle. |
| `border` | `float` | Uniform border inset width in texture pixels. |

Returns `NineSlice`: New NineSlice instance with uniform borders..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NineSlice::</span>`of(TextureHandle, float, float, float, float)` {: #oftexturehandle-float-float-float-float data-toc-label="of(TextureHandle, float, float, float, float)" }

**Create NineSlice with Custom Borders**

Constructs a NineSlice with independent left, top, right, and bottom border insets.

| Parameter | Type | Description |
| --- | --- | --- |
| `texture` | `TextureHandle` | Source texture handle. |
| `left` | `float` | Left border inset in texture pixels. |
| `top` | `float` | Top border inset in texture pixels. |
| `right` | `float` | Right border inset in texture pixels. |
| `bottom` | `float` | Bottom border inset in texture pixels. |

Returns `NineSlice`: New NineSlice instance with custom border insets..

</div>

### [Border](<../../widgets/containers/border.md>) Insets

| Method | Description |
| --- | --- |
| [`texture()`](#texture) | Returns the source [TextureHandle](<texture-handle.md>). |
| [`left()`](#left) | Returns left border width in texture pixels. |
| [`top()`](#top) | Returns top border height in texture pixels. |
| [`right()`](#right) | Returns right border width in texture pixels. |
| [`bottom()`](#bottom) | Returns bottom border height in texture pixels. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NineSlice::</span>`texture()` {: #texture data-toc-label="texture()" }

**Get Source Texture**

Returns the source [TextureHandle](<texture-handle.md>).

Returns `TextureHandle`: Source [TextureHandle](<texture-handle.md>) instance..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NineSlice::</span>`left()` {: #left data-toc-label="left()" }

**Get Left [Border](<../../widgets/containers/border.md>) Inset**

Returns left border width in texture pixels.

Returns `float`: Left border in texture pixels..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NineSlice::</span>`top()` {: #top data-toc-label="top()" }

**Get Top [Border](<../../widgets/containers/border.md>) Inset**

Returns top border height in texture pixels.

Returns `float`: Top border in texture pixels..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NineSlice::</span>`right()` {: #right data-toc-label="right()" }

**Get Right [Border](<../../widgets/containers/border.md>) Inset**

Returns right border width in texture pixels.

Returns `float`: Right border in texture pixels..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NineSlice::</span>`bottom()` {: #bottom data-toc-label="bottom()" }

**Get Bottom [Border](<../../widgets/containers/border.md>) Inset**

Returns bottom border height in texture pixels.

Returns `float`: Bottom border in texture pixels..

</div>

