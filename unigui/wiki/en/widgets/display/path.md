---
title: "Path"
slug: "path"
category: "Widgets/Display"
locale: "en"
order: 80
---

# Path

Vector path widget drawing complex custom 2D shapes, bezier curves, and polylines.

## Overview

`Path` renders arbitrary vector geometry described by `VectorPath`. Supports quadratic/cubic bezier curves, line segments, filled polygons, and stroke line caps.

## Custom Vector Path

```java
VectorPath star = VectorPath.builder()
        .moveTo(10, 0).lineTo(13, 7).lineTo(20, 7).lineTo(15, 12).build();
Path starWidget = new Path()
        .path(star)
        .color(Color.GOLD);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Path::</span>`stroke(boolean)` {: #strokeboolean data-toc-label="stroke(boolean)" }

**Stroke Mode**

Configures whether vector path is stroked or filled.

| Parameter | Type | Description |
| --- | --- | --- |
| `stroke` | `boolean` | — |

Returns `Path`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Path::</span>`strokeWidth(float)` {: #strokewidthfloat data-toc-label="strokeWidth(float)" }

**Stroke Width**

Returns or sets line width for stroked path.

| Parameter | Type | Description |
| --- | --- | --- |
| `strokeWidth` | `float` | — |

Returns `Path`.

</div>

