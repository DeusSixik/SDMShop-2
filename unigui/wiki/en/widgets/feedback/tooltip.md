---
title: "Tooltip"
slug: "tooltip"
category: "Widgets/Feedback"
locale: "en"
order: 0
---

# Tooltip

Hover-activated floating information bubble displaying helper text, rich layout, and delay timers.

## Overview

`Tooltip` displays a floating helper box containing contextual information when a user hovers over an interactive UI element.

Key features:
- Plain text and [RichText](<../../core/text/rich-text.md>) formatted layout.
- Automatic boundary detection to keep tooltip content within screen bounds.
- Show and hide delay thresholds to prevent accidental flickering.
- Customizable background, border, text color, and padding.

## Attaching Tooltip to [Widget](<../widget.md>)

```java
Tooltip tooltip = new Tooltip(button, "Click to save progress");
```

## Related links

- [OverlayLayer](<overlay-layer.md>): Full-screen overlay root managing modal backdrops, dialogs, popups, and layer-based hit testing.
- [Popup](<popup.md>): Anchored floating popup widget that positions itself relative to target widgets or screen coordinates.

## API

<div class="api-member-card" markdown="1">

### `Tooltip()` {: #tooltip data-toc-label="Tooltip()" }

Constructs an empty Tooltip.

</div>

<div class="api-member-card" markdown="1">

### `Tooltip(Widget, RichText)` {: #tooltipwidget-richtext data-toc-label="Tooltip(Widget, RichText)" }

Constructs a Tooltip anchored to a widget with [RichText](<../../core/text/rich-text.md>).

| Parameter | Type | Description |
| --- | --- | --- |
| `anchor` | `Widget` | Anchor widget to position relative to. |
| `text` | `RichText` | [RichText](<../../core/text/rich-text.md>) formatted text component. |

</div>

<div class="api-member-card" markdown="1">

### `Tooltip(Widget, String)` {: #tooltipwidget-string data-toc-label="Tooltip(Widget, String)" }

Constructs a Tooltip anchored to a widget with plain text.

| Parameter | Type | Description |
| --- | --- | --- |
| `anchor` | `Widget` | Anchor widget to position relative to. |
| `text` | `String` | Display text string. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`anchor()` {: #anchor data-toc-label="anchor()" }

Returns the anchor widget this tooltip observes.

Returns `Widget`: The target anchor widget..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`anchor(Widget)` {: #anchorwidget data-toc-label="anchor(Widget)" }

Sets the anchor widget this tooltip observes.

| Parameter | Type | Description |
| --- | --- | --- |
| `anchor` | `Widget` | Anchor widget to position relative to. |

Returns `Tooltip`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`arrangeInHost(RectView)` {: #arrangeinhostrectview data-toc-label="arrangeInHost(RectView)" }

Clamps and positions the tooltip relative to pointer within host bounds.

| Parameter | Type | Description |
| --- | --- | --- |
| `bounds` | `RectView` | Available host rectangular boundary. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`maxWidth()` {: #maxwidth data-toc-label="maxWidth()" }

Returns the maximum tooltip wrap width.

Returns `float`: Maximum range bound..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`maxWidth(float)` {: #maxwidthfloat data-toc-label="maxWidth(float)" }

Sets the maximum tooltip wrap width.

| Parameter | Type | Description |
| --- | --- | --- |
| `maxWidth` | `float` | Maximum width constraint. |

Returns `Tooltip`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`offset(float, float)` {: #offsetfloat-float data-toc-label="offset(float, float)" }

Sets cursor displacement offset in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | Horizontal X coordinate. |
| `y` | `float` | Vertical Y coordinate. |

Returns `Tooltip`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`richText()` {: #richtext data-toc-label="richText()" }

Returns the formatted [RichText](<../../core/text/rich-text.md>) content.

Returns `RichText`: The [RichText](<../../core/text/rich-text.md>) formatted content..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`richText(RichText)` {: #richtextrichtext data-toc-label="richText(RichText)" }

Sets the formatted [RichText](<../../core/text/rich-text.md>) content.

| Parameter | Type | Description |
| --- | --- | --- |
| `richText` | `RichText` | [RichText](<../../core/text/rich-text.md>) formatted text component. |

Returns `Tooltip`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`showing()` {: #showing data-toc-label="showing()" }

Returns true if the tooltip is currently visible.

Returns `boolean`: True if currently visible..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`text()` {: #text data-toc-label="text()" }

Returns the tooltip plain text.

Returns `String`: The displayed text string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`text(String)` {: #textstring data-toc-label="text(String)" }

Sets the tooltip plain text.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | Display text string. |

Returns `Tooltip`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`textColor()` {: #textcolor data-toc-label="textColor()" }

Returns the mutable text foreground color.

Returns `MutableColor`: The mutable color instance..

</div>

