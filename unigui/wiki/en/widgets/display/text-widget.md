---
title: "TextWidget"
slug: "text-widget"
category: "Widgets/Display"
locale: "en"
order: 10
---

# TextWidget

Abstract foundation widget for text rendering components supporting plain strings, [RichText](<../../core/text/rich-text.md>), fonts, colors, and gradients.

## Overview

`TextWidget` serves as the base class for `Label`, `Text`, `TextBlock`, and `RichTextView`. It encapsulates font selection, font pixel size, plain text and `RichText` models, color gradients, and text layout measurement.

## Configuring TextWidget

```java
Label label = new Label("UniGUI")
        .font(Fonts.TITLE, 18.0f)
        .textGradient(Color.WHITE, Color.CYAN, 90.0f);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextWidget::</span>`font(FontFace, float)` {: #fontfontface-float data-toc-label="font(FontFace, float)" }

**Set Font and Size**

Configures font face and pixel size.

| Parameter | Type | Description |
| --- | --- | --- |
| `font` | `FontFace` | — |
| `pixelSize` | `float` | — |

Returns `TextWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextWidget::</span>`richText(RichText)` {: #richtextrichtext data-toc-label="richText(RichText)" }

**[RichText](<../../core/text/rich-text.md>) Content**

Returns or sets styled [RichText](<../../core/text/rich-text.md>) model.

| Parameter | Type | Description |
| --- | --- | --- |
| `richText` | `RichText` | — |

Returns `TextWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextWidget::</span>`text(String)` {: #textstring data-toc-label="text(String)" }

**[Text](<text.md>) Content**

Returns or sets plain string text content.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | — |

Returns `TextWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextWidget::</span>`textBrush(TextBrush)` {: #textbrushtextbrush data-toc-label="textBrush(TextBrush)" }

**Set [Text](<text.md>) Brush**

Applies custom TextBrush for special rendering effects.

| Parameter | Type | Description |
| --- | --- | --- |
| `brush` | `TextBrush` | — |

Returns `TextWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextWidget::</span>`textGradient(ColorView, ColorView, float)` {: #textgradientcolorview-colorview-float data-toc-label="textGradient(ColorView, ColorView, float)" }

**Set [Text](<text.md>) Gradient**

Applies two-color linear gradient to rendered text glyphs.

| Parameter | Type | Description |
| --- | --- | --- |
| `startColor` | `ColorView` | — |
| `endColor` | `ColorView` | — |
| `angleDegrees` | `float` | — |

Returns `TextWidget`.

</div>

