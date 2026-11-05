---
title: "TextureGroup"
slug: "texture-group"
category: "Core/Textures"
locale: "en"
order: 60
---

# TextureGroup

Ordered composite collection of TextureLayer instances rendered sequentially within shared base bounds.

## Overview & Layer Compositing

`TextureGroup` combines multiple `TextureLayer` descriptors into a single reusable visual asset.

### Multi-Layer Surface Architecture

Instead of managing multiple parallel texture fields inside a widget, `TextureGroup` bundles all layers together:
- Each layer manages its own `TextureHandle`, source UV rect, tint color, `ImageFit`, and nine-slice borders.
- Layers render in strict sequential order from index 0 to N-1 (background plates, frames, patterns, highlight overlays).
- Null textures in a layer are cleanly skipped without breaking the draw pipeline.

## Fluent Builder Construction

Construct a `TextureGroup` using static factories or the fluent `TextureGroup#builder()`:

```java
TextureGroup group = TextureGroup.builder()
        .add(backgroundTexture) // default layer settings
        .add(new TextureLayer(frameTexture).tint(ColorView.rgb(200, 200, 200)))
        .build();
```

## Composing Layered Card Surface

```java
// Create composite group with background plate and gold trim
TextureGroup cardSurface = TextureGroup.builder()
        .add(new TextureLayer(basePlate).fit(ImageFit.NINE_SLICE).sliceBorders(SliceBorders.all(8.0f)))
        .add(new TextureLayer(goldTrim).tint(ColorView.rgb(255, 215, 0)))
        .build();
```

## API

### Factory Methods & Builder

| Method | Description |
| --- | --- |
| [`create(TextureLayer[])`](#createtexturelayer) | Creates a TextureGroup from the specified layer array in render order. |
| [`TextureGroup(List)`](#texturegrouplist) | Creates a TextureGroup copying the provided list of layers. |
| [`builder()`](#builder) | Returns a new Builder for constructing a TextureGroup. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureGroup::</span>`create(TextureLayer[])` {: #createtexturelayer data-toc-label="create(TextureLayer[])" }

**Create from Layers Array**

Creates a TextureGroup from the specified layer array in render order.

| Parameter | Type | Description |
| --- | --- | --- |
| `layers` | `TextureLayer[]` | Sequential texture layers to composite. |

Returns `TextureGroup`: New immutable TextureGroup instance..

</div>

<div class="api-member-card" markdown="1">

#### `TextureGroup(List)` {: #texturegrouplist data-toc-label="TextureGroup(List)" }

**Construct with Layers List**

Creates a TextureGroup copying the provided list of layers.

| Parameter | Type | Description |
| --- | --- | --- |
| `layers` | `List` | Sequential texture layers to composite. |

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureGroup::</span>`builder()` {: #builder data-toc-label="builder()" }

**Create Builder**

Returns a new Builder for constructing a TextureGroup.

Returns `Builder`: New TextureGroup.Builder instance..

</div>

### Layer Access

| Method | Description |
| --- | --- |
| [`layers()`](#layers) | Returns an unmodifiable list of layers in rendering sequence. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">TextureGroup::</span>`layers()` {: #layers data-toc-label="layers()" }

**Get Layers List**

Returns an unmodifiable list of layers in rendering sequence.

Returns `List`: Unmodifiable list of TextureLayer instances..

</div>

