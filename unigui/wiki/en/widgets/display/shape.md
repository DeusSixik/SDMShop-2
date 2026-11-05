---
title: "Shape"
slug: "shape"
category: "Widgets/Display"
locale: "en"
order: 70
---

# Shape

Vector geometric shape widget supporting rectangles, rounded rectangles, circles, ovals, and strokes.

## Overview

`Shape` draws geometric 2D primitives using hardware-accelerated shaders. Supports filled or stroked rectangles, rounded corners, ellipses, and circles with customizable border widths.

## Circle Shape

```java
Shape badge = new Shape()
        .type(Shape.Type.CIRCLE)
        .color(Color.GREEN)
        .stroke(false);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Shape::</span>`color(ColorView)` {: #colorcolorview data-toc-label="color(ColorView)" }

**Shape Color**

Returns or sets fill or stroke color of shape.

| Parameter | Type | Description |
| --- | --- | --- |
| `color` | `ColorView` | — |

Returns `Shape`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Shape::</span>`stroke(boolean)` {: #strokeboolean data-toc-label="stroke(boolean)" }

**Stroke Mode**

Configures whether shape is rendered as an outline stroke or solid fill.

| Parameter | Type | Description |
| --- | --- | --- |
| `stroke` | `boolean` | — |

Returns `Shape`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Shape::</span>`strokeWidth(float)` {: #strokewidthfloat data-toc-label="strokeWidth(float)" }

**Stroke Width**

Returns or sets outline stroke line thickness.

| Parameter | Type | Description |
| --- | --- | --- |
| `strokeWidth` | `float` | — |

Returns `Shape`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Shape::</span>`type(Type)` {: #typetype data-toc-label="type(Type)" }

**Shape Type**

Returns or sets geometric primitive type.

| Parameter | Type | Description |
| --- | --- | --- |
| `type` | `Type` | — |

Returns `Shape`.

</div>

