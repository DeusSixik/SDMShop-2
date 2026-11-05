---
title: "ColorPicker"
slug: "color-picker"
category: "Widgets/Interaction"
locale: "en"
order: 220
---

# ColorPicker

Interactive color selection control providing RGBA sliders, hex input, and popup palette.

## Overview

`ColorPicker` displays an interactive color swatch preview button and an expandable popup editor supporting RGBA channels, alpha transparency, hex strings, and change events.

## Basic ColorPicker

```java
ColorPicker picker = new ColorPicker()
        .argb(0xFFFF8800);
picker.onColorChanged(e -> updateThemeColor(e.newColor()));
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ColorPicker::</span>`argb()` {: #argb data-toc-label="argb()" }

**Get ARGB**

Returns current packed 32-bit ARGB integer value.

Returns `int`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ColorPicker::</span>`argb(int)` {: #argbint data-toc-label="argb(int)" }

**Set ARGB**

Sets picker color from packed 32-bit ARGB value.

| Parameter | Type | Description |
| --- | --- | --- |
| `argb` | `int` | — |

Returns `ColorPicker`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ColorPicker::</span>`color()` {: #color data-toc-label="color()" }

**Get Color**

Returns current ColorView representation.

Returns `ColorView`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ColorPicker::</span>`color(ColorView)` {: #colorcolorview data-toc-label="color(ColorView)" }

**Set Color**

Sets picker color from ColorView.

| Parameter | Type | Description |
| --- | --- | --- |
| `color` | `ColorView` | — |

Returns `ColorPicker`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ColorPicker::</span>`onColorChanged(EventListener)` {: #oncolorchangedeventlistener data-toc-label="onColorChanged(EventListener)" }

**On Color Changed**

Registers event listener triggered on color changes.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | — |

Returns `EventSubscription`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ColorPicker::</span>`popup()` {: #popup data-toc-label="popup()" }

**Get [Popup](<../feedback/popup.md>)**

Returns overlay popup hosting channel sliders.

Returns `Popup`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ColorPicker::</span>`preview()` {: #preview data-toc-label="preview()" }

**Preview [Button](<button.md>)**

Returns internal swatch preview button widget.

Returns `Button`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ColorPicker::</span>`rgba255(int, int, int, int)` {: #rgba255int-int-int-int data-toc-label="rgba255(int, int, int, int)" }

**Set RGBA 0-255**

Sets picker color with 0-255 channel integers.

| Parameter | Type | Description |
| --- | --- | --- |
| `red` | `int` | — |
| `green` | `int` | — |
| `blue` | `int` | — |
| `alpha` | `int` | — |

Returns `ColorPicker`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ColorPicker::</span>`type()` {: #type data-toc-label="type()" }

**Get Type**

Returns color picker display type (RGB or RGBA).

Returns `Type`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ColorPicker::</span>`type(Type)` {: #typetype data-toc-label="type(Type)" }

**Set Type**

Sets color picker display mode.

| Parameter | Type | Description |
| --- | --- | --- |
| `type` | `Type` | — |

Returns `ColorPicker`.

</div>

