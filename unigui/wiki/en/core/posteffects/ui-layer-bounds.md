---
title: "UiLayerBounds"
slug: "ui-layer-bounds"
category: "Core/PostEffects"
locale: "en"
order: 80
---

# UiLayerBounds

Dimensions, position, and resolution scale for an offscreen post-processing layer target.

## Overview

`UiLayerBounds` represents the coordinate rect and pixel scale of a UI layer rendered to an intermediate offscreen texture. Offscreen dimensions (`targetWidth()`, `targetHeight()`) are clamped safely to avoid zero or negative framebuffer allocations.

## Creating Viewport Bounds

```java
UiLayerBounds bounds = UiLayerBounds.viewport(800.0f, 600.0f);
int texWidth = bounds.targetWidth();
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiLayerBounds::</span>`of(float, float, float, float)` {: #offloat-float-float-float data-toc-label="of(float, float, float, float)" }

**of(float, float, float, float)**

Creates layer bounds with standard 1.0 scale.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | Left coordinate. |
| `y` | `float` | Top coordinate. |
| `width` | `float` | Layer width. |
| `height` | `float` | Layer height. |

Returns `UiLayerBounds`: Resulting UiLayerBounds..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiLayerBounds::</span>`safeHeight()` {: #safeheight data-toc-label="safeHeight()" }

**safeHeight()**

Returns height guaranteed to be at least 1.0.

Returns `float`: Resulting float..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiLayerBounds::</span>`safeScale()` {: #safescale data-toc-label="safeScale()" }

**safeScale()**

Returns scale guaranteed to be at least 0.01.

Returns `float`: Resulting float..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiLayerBounds::</span>`safeWidth()` {: #safewidth data-toc-label="safeWidth()" }

**safeWidth()**

Returns width guaranteed to be at least 1.0.

Returns `float`: Resulting float..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiLayerBounds::</span>`targetHeight()` {: #targetheight data-toc-label="targetHeight()" }

**targetHeight()**

Returns offscreen target texture pixel height.

Returns `int`: Resulting int..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiLayerBounds::</span>`targetWidth()` {: #targetwidth data-toc-label="targetWidth()" }

**targetWidth()**

Returns offscreen target texture pixel width.

Returns `int`: Resulting int..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UiLayerBounds::</span>`viewport(float, float)` {: #viewportfloat-float data-toc-label="viewport(float, float)" }

**viewport(float, float)**

Creates layer bounds covering the full viewport at (0, 0).

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | Viewport width. |
| `height` | `float` | Viewport height. |

Returns `UiLayerBounds`: Resulting UiLayerBounds..

</div>

