---
title: "TextEngine"
slug: "text-engine"
category: "Core/Text"
locale: "en"
order: 30
---

# TextEngine

Core text layout, line-wrapping, dimension measurement, and inline-content rendering engine.

## Overview

`TextEngine` is the centralized subsystem responsible for measuring character widths, wrapping long text runs across multiple lines, aligning blocks horizontally/vertically, and laying out embedded inline spans (icons, textures, badges) alongside regular text.

## [Text](<../../widgets/display/text.md>) and Content Measurement

Measurement methods compute line dimensions (`measureLineWidth`) and block heights (`measureTextHeight`) taking into account font ascent, descent, character code-points, and atomic inline spans. The `minContentWidth` method determines the minimum width required to display the longest word without clipping.

## Line Wrapping

`wrapLines()` splits rich text across word boundaries to fit within a given maximum width (`maxWidth`). If single words exceed the available line width, they are broken at character boundaries to prevent line overflows.

## Rendering and Inline Content

When rendering plain text runs, TextEngine delegates directly to `RenderBackend`. If inline spans exist, TextEngine computes exact coordinates for each atom and invokes its `InlineContentRenderer` with final screen bounds.

## Measuring and Wrapping Rich [Text](<../../widgets/display/text.md>)

```java
float maxWidth = 200.0f;
List<RichText> lines = TextEngine.wrapLines(context, richText, maxWidth);
float totalHeight = TextEngine.linesHeight(context, lines);
```

## Drawing Aligned [Text](<../../widgets/display/text.md>) into Canvas

```java
TextEngine.draw(context, richText, x, y, width, height, paint, transform,
    Alignment.CENTER, Alignment.CENTER);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`measureLineWidth(String)` {: #measurelinewidthstring data-toc-label="measureLineWidth(String)" }

**measureLineWidth(String)**

Measures line width using approximate glyph spacing without active backend.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | [Text](<../../widgets/display/text.md>) to measure. |

Returns `float`: Calculated line width in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`minContentWidth(RichText)` {: #mincontentwidthrichtext data-toc-label="minContentWidth(RichText)" }

**minContentWidth([RichText](<rich-text.md>))**

Calculates the width of the longest unbreakable word in the rich text.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `RichText` | Rich text to inspect. |

Returns `float`: Minimum content width in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`measureLineWidth(RenderContext, String)` {: #measurelinewidthrendercontext-string data-toc-label="measureLineWidth(RenderContext, String)" }

**measureLineWidth(RenderContext, String)**

Measures string width using active render backend font metrics.

| Parameter | Type | Description |
| --- | --- | --- |
| `context` | `RenderContext` | Active render context. |
| `text` | `String` | [Text](<../../widgets/display/text.md>) string. |

Returns `float`: Line width in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`measureLineWidth(RenderContext, RichText)` {: #measurelinewidthrendercontext-richtext data-toc-label="measureLineWidth(RenderContext, RichText)" }

**measureLineWidth(RenderContext, [RichText](<rich-text.md>))**

Measures rich text width taking inline spans and fonts into account.

| Parameter | Type | Description |
| --- | --- | --- |
| `context` | `RenderContext` | Active render context. |
| `text` | `RichText` | Rich text instance. |

Returns `float`: Maximum line width in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`measureLineWidth(RichText)` {: #measurelinewidthrichtext data-toc-label="measureLineWidth(RichText)" }

**measureLineWidth([RichText](<rich-text.md>))**

Measures rich text width using font faces embedded in runs.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `RichText` | Rich text instance. |

Returns `float`: Line width in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`measureTextHeight(RichText)` {: #measuretextheightrichtext data-toc-label="measureTextHeight(RichText)" }

**measureTextHeight([RichText](<rich-text.md>))**

Calculates total vertical height for all lines of rich text.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `RichText` | Rich text instance. |

Returns `float`: Total text height in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`measureTextHeight(RenderContext, RichText)` {: #measuretextheightrendercontext-richtext data-toc-label="measureTextHeight(RenderContext, RichText)" }

**measureTextHeight(RenderContext, [RichText](<rich-text.md>))**

Calculates rich text height using active render backend font metrics.

| Parameter | Type | Description |
| --- | --- | --- |
| `context` | `RenderContext` | Active render context. |
| `text` | `RichText` | Rich text instance. |

Returns `float`: [Text](<../../widgets/display/text.md>) height in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`wrapLines(RichText, float)` {: #wraplinesrichtext-float data-toc-label="wrapLines(RichText, float)" }

**wrapLines([RichText](<rich-text.md>), float)**

Splits rich text into wrapped lines fitting within the specified maximum width.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `RichText` | Rich text to wrap. |
| `maxWidth` | `float` | Maximum line width. |

Returns `List`: List of wrapped rich text lines..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`wrapLines(RenderContext, RichText, float)` {: #wraplinesrendercontext-richtext-float data-toc-label="wrapLines(RenderContext, RichText, float)" }

**wrapLines(RenderContext, [RichText](<rich-text.md>), float)**

Wraps rich text using backend font measurements.

| Parameter | Type | Description |
| --- | --- | --- |
| `context` | `RenderContext` | Active render context. |
| `text` | `RichText` | Rich text to wrap. |
| `maxWidth` | `float` | Maximum line width. |

Returns `List`: List of wrapped rich text lines..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`lineHeight(RichText)` {: #lineheightrichtext data-toc-label="lineHeight(RichText)" }

**lineHeight([RichText](<rich-text.md>))**

Calculates the height of a single rich text line based on runs and inline content.

| Parameter | Type | Description |
| --- | --- | --- |
| `line` | `RichText` | Line to evaluate. |

Returns `float`: Line height in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`lineHeight(RenderContext, RichText)` {: #lineheightrendercontext-richtext data-toc-label="lineHeight(RenderContext, RichText)" }

**lineHeight(RenderContext, [RichText](<rich-text.md>))**

Calculates line height using active render context metrics.

| Parameter | Type | Description |
| --- | --- | --- |
| `context` | `RenderContext` | Active render context. |
| `line` | `RichText` | Line to evaluate. |

Returns `float`: Line height in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`linesHeight(List)` {: #linesheightlist data-toc-label="linesHeight(List)" }

**linesHeight(List)**

Calculates the combined height of multiple wrapped lines.

| Parameter | Type | Description |
| --- | --- | --- |
| `lines` | `List` | List of rich text lines. |

Returns `float`: Combined height in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`linesHeight(RenderContext, List)` {: #linesheightrendercontext-list data-toc-label="linesHeight(RenderContext, List)" }

**linesHeight(RenderContext, List)**

Calculates combined lines height using active render context.

| Parameter | Type | Description |
| --- | --- | --- |
| `context` | `RenderContext` | Active render context. |
| `lines` | `List` | List of rich text lines. |

Returns `float`: Combined height in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`draw(RenderContext, String, float, float, float, float, Paint, Transform, Alignment, Alignment)` {: #drawrendercontext-string-float-float-float-float-paint-transform-alignment-alignment data-toc-label="draw(RenderContext, String, float, float, float, float, Paint, Transform, Alignment, Alignment)" }

**draw(RenderContext, String, float, float, float, float, [Paint](<../render/paint.md>), Transform, Alignment, Alignment)**

Renders plain text aligned within the specified bounding box.

| Parameter | Type | Description |
| --- | --- | --- |
| `context` | `RenderContext` | Active render context. |
| `text` | `String` | [Text](<../../widgets/display/text.md>) string. |
| `x` | `float` | X coordinate. |
| `y` | `float` | Y coordinate. |
| `width` | `float` | Container width. |
| `height` | `float` | Container height. |
| `paint` | `Paint` | [Paint](<../render/paint.md>) styling. |
| `transform` | `Transform` | Optional transform. |
| `horizontalAlignment` | `Alignment` | Horizontal alignment. |
| `verticalAlignment` | `Alignment` | Vertical alignment. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`draw(RenderContext, RichText, float, float, float, float, Paint, Transform, Alignment, Alignment)` {: #drawrendercontext-richtext-float-float-float-float-paint-transform-alignment-alignment data-toc-label="draw(RenderContext, RichText, float, float, float, float, Paint, Transform, Alignment, Alignment)" }

**draw(RenderContext, [RichText](<rich-text.md>), float, float, float, float, [Paint](<../render/paint.md>), Transform, Alignment, Alignment)**

Renders rich text with alignment and inline content support.

| Parameter | Type | Description |
| --- | --- | --- |
| `context` | `RenderContext` | Active render context. |
| `text` | `RichText` | Rich text instance. |
| `x` | `float` | X coordinate. |
| `y` | `float` | Y coordinate. |
| `width` | `float` | Container width. |
| `height` | `float` | Container height. |
| `paint` | `Paint` | [Paint](<../render/paint.md>) styling. |
| `transform` | `Transform` | Optional transform. |
| `horizontalAlignment` | `Alignment` | Horizontal alignment. |
| `verticalAlignment` | `Alignment` | Vertical alignment. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`drawInline(DrawScope, RichText, float, float, float, float, Paint)` {: #drawinlinedrawscope-richtext-float-float-float-float-paint data-toc-label="drawInline(DrawScope, RichText, float, float, float, float, Paint)" }

**drawInline([DrawScope](<../render/draw-scope.md>), [RichText](<rich-text.md>), float, float, float, float, [Paint](<../render/paint.md>))**

Lays out and draws rich text with inline content into a [DrawScope](<../render/draw-scope.md>).

| Parameter | Type | Description |
| --- | --- | --- |
| `draw` | `DrawScope` | Target draw scope. |
| `text` | `RichText` | Rich text instance. |
| `x` | `float` | X coordinate. |
| `y` | `float` | Y coordinate. |
| `width` | `float` | Container width. |
| `height` | `float` | Container height. |
| `paint` | `Paint` | [Paint](<../render/paint.md>) styling. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`drawInline(DrawScope, RichText, float, float, float, float, Paint, float)` {: #drawinlinedrawscope-richtext-float-float-float-float-paint-float data-toc-label="drawInline(DrawScope, RichText, float, float, float, float, Paint, float)" }

**drawInline([DrawScope](<../render/draw-scope.md>), [RichText](<rich-text.md>), float, float, float, float, [Paint](<../render/paint.md>), float)**

Lays out and draws rich text into a [DrawScope](<../render/draw-scope.md>) with visual vertical offset.

| Parameter | Type | Description |
| --- | --- | --- |
| `draw` | `DrawScope` | Target draw scope. |
| `text` | `RichText` | Rich text instance. |
| `x` | `float` | X coordinate. |
| `y` | `float` | Y coordinate. |
| `width` | `float` | Container width. |
| `height` | `float` | Container height. |
| `paint` | `Paint` | [Paint](<../render/paint.md>) styling. |
| `textVisualOffset` | `float` | Vertical visual offset. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`usesPositionalBrush(RichText)` {: #usespositionalbrushrichtext data-toc-label="usesPositionalBrush(RichText)" }

**usesPositionalBrush([RichText](<rich-text.md>))**

Checks whether any text run in the rich text uses a coordinate-dependent brush.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `RichText` | Rich text instance. |

Returns `boolean`: true if positional brush is present..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`alignedStart(float, float, float, Alignment)` {: #alignedstartfloat-float-float-alignment data-toc-label="alignedStart(float, float, float, Alignment)" }

**alignedStart(float, float, float, Alignment)**

Calculates the starting coordinate of an item given available space and alignment.

| Parameter | Type | Description |
| --- | --- | --- |
| `start` | `float` | Base offset coordinate. |
| `available` | `float` | Total available dimension. |
| `size` | `float` | Item dimension. |
| `alignment` | `Alignment` | Alignment mode. |

Returns `float`: Aligned start position..

</div>

