---
title: "RichText"
slug: "rich-text"
category: "Core/Text"
locale: "en"
order: 10
---

# RichText

Immutable multi-style rich text model supporting styled runs, fonts, brushes, gradients, and inline content spans.

## Overview

`RichText` represents formatted text composed of multiple style runs and optional inline content atoms (such as icons or custom graphics). It exposes a dual projection: `spans()` for complete layout atoms (including inline spans) and `runs()` for legacy text-only consumers.

## Spans and Projections

[Text](<../../widgets/display/text.md>) is stored as an immutable list of `RichTextSpan` instances. [Text](<../../widgets/display/text.md>) runs (`TextRun`) contain character sequences with dedicated font faces, pixel sizes, colors, tracking, and transforms. Inline content spans (`InlineContentSpan`) describe atomic layout boxes (with fallback text) rendered by procedural callbacks.

## Inline Content and Icons

Inline atoms flow seamlessly within the text stream. They participate in line wrapping, measurement, and alignment without creating separate widget hierarchy nodes. Icons and custom renderers inherit parent scissor clipping and transforms automatically.

## [Text](<../../widgets/display/text.md>) Brushes and Gradients

RichText supports procedural brush fills via `TextBrush`. The `withBrush()` and `withLinearGradient()` methods apply solid colors or multi-color gradients across all text runs while preserving existing color tints.

## [Text](<../../widgets/display/text.md>) Transforms and Slicing

Use `slice(start, end)` to extract substring ranges using plain-text character offsets. Whole inline spans within the range are preserved, while partial overlaps degrade safely to fallback plain text. `withTextStyle()` allows batch customization of font sizes, letter spacing (tracking), and case transformations.

## Builder API

Construct mixed text and icon flows fluently using `RichText.builder()`. Set style attributes (`font`, `size`, `color`, `brush`, `tracking`, `transform`) that apply to subsequent `append()` calls, or insert inline graphics via `icon()` and `inline()`.

## Building Rich [Text](<../../widgets/display/text.md>) with [Text](<../../widgets/display/text.md>) and Inline Icons

```java
RichText text = RichText.builder()
    .append("Status: ")
    .icon("icon:ok", checkTexture, 12.0f)
    .color(ColorView.rgb(80, 220, 100))
    .append(" All systems online")
    .build();
```

## Applying Linear Gradient Across [Text](<../../widgets/display/text.md>)

```java
RichText gradientTitle = RichText.gradient(
    "UniGUI Engine",
    Fonts.TITLE,
    18.0f,
    ColorView.rgb(255, 100, 100),
    ColorView.rgb(100, 150, 255),
    45.0f
);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`plain(String)` {: #plainstring data-toc-label="plain(String)" }

**plain(String)**

Creates a single-run rich text without explicit font face or styling.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | Plain text string. |

Returns `RichText`: Single-run RichText instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`resolve(String)` {: #resolvestring data-toc-label="resolve(String)" }

**resolve(String)**

Parses a string through the active InlineContentResolver, resolving marker placeholders into inline content.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | Raw text potentially containing inline markers. |

Returns `RichText`: Resolved RichText instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`of(String, FontFace, float)` {: #ofstring-fontface-float data-toc-label="of(String, FontFace, float)" }

**of(String, FontFace, float)**

Creates a rich text with specified font face and pixel size.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | [Text](<../../widgets/display/text.md>) string. |
| `font` | `FontFace` | Font face or null for default. |
| `pixelSize` | `float` | Font size in UI pixels. |

Returns `RichText`: Single-run RichText instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`of(String, FontFace, float, ColorView)` {: #ofstring-fontface-float-colorview data-toc-label="of(String, FontFace, float, ColorView)" }

**of(String, FontFace, float, ColorView)**

Creates a rich text with specified font, pixel size, and vertex color.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | [Text](<../../widgets/display/text.md>) string. |
| `font` | `FontFace` | Font face or null for default. |
| `pixelSize` | `float` | Font size in UI pixels. |
| `color` | `ColorView` | Tint color or null for widget paint color. |

Returns `RichText`: Single-run RichText instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`brushed(String, FontFace, float, TextBrush)` {: #brushedstring-fontface-float-textbrush data-toc-label="brushed(String, FontFace, float, TextBrush)" }

**brushed(String, FontFace, float, TextBrush)**

Creates a rich text with specified font, size, and procedural brush fill.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | [Text](<../../widgets/display/text.md>) string. |
| `font` | `FontFace` | Font face or null for default. |
| `pixelSize` | `float` | Font size in UI pixels. |
| `brush` | `TextBrush` | [Text](<../../widgets/display/text.md>) brush or null for solid fill. |

Returns `RichText`: Single-run RichText instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`gradient(String, FontFace, float, ColorView, ColorView, float)` {: #gradientstring-fontface-float-colorview-colorview-float data-toc-label="gradient(String, FontFace, float, ColorView, ColorView, float)" }

**gradient(String, FontFace, float, ColorView, ColorView, float)**

Creates a rich text run filled with a linear gradient.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | [Text](<../../widgets/display/text.md>) string. |
| `font` | `FontFace` | Font face or null for default. |
| `pixelSize` | `float` | Font size in UI pixels. |
| `startColor` | `ColorView` | Starting gradient color. |
| `endColor` | `ColorView` | Ending gradient color. |
| `angleDegrees` | `float` | Angle of gradient direction in degrees. |

Returns `RichText`: Gradient RichText instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`ofSpans(List)` {: #ofspanslist data-toc-label="ofSpans(List)" }

**ofSpans(List)**

Constructs an immutable rich text from a complete list of layout spans.

| Parameter | Type | Description |
| --- | --- | --- |
| `spans` | `List` | List of text runs and inline spans. |

Returns `RichText`: Normalized RichText instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`builder()` {: #builder data-toc-label="builder()" }

**builder()**

Creates a new fluent builder for constructing rich text with mixed styling and inline items.

Returns `Builder`: New RichText.Builder instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`runs()` {: #runs data-toc-label="runs()" }

**runs()**

Returns an unmodifiable list of text-only runs, excluding inline content.

Returns `List`: [Text](<../../widgets/display/text.md>) runs list..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`spans()` {: #spans data-toc-label="spans()" }

**spans()**

Returns an unmodifiable list of all layout spans, including inline items.

Returns `List`: Complete spans list..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`plainText()` {: #plaintext data-toc-label="plainText()" }

**plainText()**

Returns the flattened plain text string representation of all spans.

Returns `String`: Plain text string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`hasInlineContent()` {: #hasinlinecontent data-toc-label="hasInlineContent()" }

**hasInlineContent()**

Checks whether this rich text contains any non-text inline spans.

Returns `boolean`: true if inline content exists..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`isEmpty()` {: #isempty data-toc-label="isEmpty()" }

**isEmpty()**

Checks whether this rich text contains no spans after normalization.

Returns `boolean`: true if empty..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`withBrush(TextBrush)` {: #withbrushtextbrush data-toc-label="withBrush(TextBrush)" }

**withBrush(TextBrush)**

Returns a copy of this rich text with the specified brush applied to all text runs.

| Parameter | Type | Description |
| --- | --- | --- |
| `brush` | `TextBrush` | New text brush, or null for solid color. |

Returns `RichText`: Updated RichText copy..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`withTextStyle(Float, Float, TextTransform)` {: #withtextstylefloat-float-texttransform data-toc-label="withTextStyle(Float, Float, TextTransform)" }

**withTextStyle(Float, Float, TextTransform)**

Returns a copy of this rich text with overridden font size, tracking, and transform.

| Parameter | Type | Description |
| --- | --- | --- |
| `pixelSize` | `Float` | New font size in UI pixels, or null to keep unchanged. |
| `tracking` | `Float` | New letter spacing in UI pixels, or null to keep unchanged. |
| `transform` | `TextTransform` | New text transformation, or null to keep unchanged. |

Returns `RichText`: Updated RichText copy..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`withLinearGradient(ColorView, ColorView, float)` {: #withlineargradientcolorview-colorview-float data-toc-label="withLinearGradient(ColorView, ColorView, float)" }

**withLinearGradient(ColorView, ColorView, float)**

Returns a copy of this rich text with a linear gradient brush applied across all runs.

| Parameter | Type | Description |
| --- | --- | --- |
| `startColor` | `ColorView` | Start color. |
| `endColor` | `ColorView` | End color. |
| `angleDegrees` | `float` | Angle in degrees. |

Returns `RichText`: Updated RichText copy..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`append(RichText)` {: #appendrichtext data-toc-label="append(RichText)" }

**append(RichText)**

Concatenates another rich text to the end of this instance.

| Parameter | Type | Description |
| --- | --- | --- |
| `other` | `RichText` | Rich text to append, or null. |

Returns `RichText`: Combined RichText instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RichText::</span>`slice(int, int)` {: #sliceint-int data-toc-label="slice(int, int)" }

**slice(int, int)**

Extracts a range slice of this rich text using character offsets.

| Parameter | Type | Description |
| --- | --- | --- |
| `startInclusive` | `int` | Starting index inclusive. |
| `endExclusive` | `int` | Ending index exclusive. |

Returns `RichText`: Sliced RichText instance..

</div>

