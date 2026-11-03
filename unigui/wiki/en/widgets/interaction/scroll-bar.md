---
title: "ScrollBar"
slug: "scroll-bar"
category: "Widgets/Interaction"
locale: "en"
order: 190
---

# ScrollBar

Draggable scrollbar control for scrolling content along horizontal or vertical axes.

## Overview

`ScrollBar` extends `Box` to render an animated track and draggable thumb. Supports smooth hover expansion, wheel steps, discrete page jumps, and change event subscriptions.

## Vertical ScrollBar

```java
ScrollBar scroll = new ScrollBar()
        .orientation(Orientation.VERTICAL)
        .range(0.0f, 500.0f)
        .pageSize(50.0f)
        .value(0.0f);
scroll.onValueChanged(e -> list.scrollOffset(e.newValue()));
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`dragging()` {: #dragging data-toc-label="dragging()" }

**Is Dragging**

Returns whether scrollbar thumb is actively dragged.

Returns `boolean`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`hoverThickness()` {: #hoverthickness data-toc-label="hoverThickness()" }

**Get Hover Thickness**

Returns expanded scrollbar thickness when hovered.

Returns `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`hoverThickness(float)` {: #hoverthicknessfloat data-toc-label="hoverThickness(float)" }

**Set Hover Thickness**

Sets expanded scrollbar thickness when hovered.

| Parameter | Type | Description |
| --- | --- | --- |
| `hoverThickness` | `float` | — |

Returns `ScrollBar`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`idleThickness()` {: #idlethickness data-toc-label="idleThickness()" }

**Get Idle Thickness**

Returns scrollbar thickness when not hovered.

Returns `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`idleThickness(float)` {: #idlethicknessfloat data-toc-label="idleThickness(float)" }

**Set Idle Thickness**

Sets scrollbar thickness when not hovered.

| Parameter | Type | Description |
| --- | --- | --- |
| `idleThickness` | `float` | — |

Returns `ScrollBar`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`max()` {: #max data-toc-label="max()" }

**Get Maximum**

Returns scroll upper bound.

Returns `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`max(float)` {: #maxfloat data-toc-label="max(float)" }

**Set Maximum**

Sets scroll upper bound.

| Parameter | Type | Description |
| --- | --- | --- |
| `max` | `float` | — |

Returns `ScrollBar`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`min()` {: #min data-toc-label="min()" }

**Get Minimum**

Returns scroll lower bound.

Returns `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`min(float)` {: #minfloat data-toc-label="min(float)" }

**Set Minimum**

Sets scroll lower bound.

| Parameter | Type | Description |
| --- | --- | --- |
| `min` | `float` | — |

Returns `ScrollBar`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`onValueChanged(EventListener)` {: #onvaluechangedeventlistener data-toc-label="onValueChanged(EventListener)" }

**On Value Changed**

Registers event listener for scroll offset changes.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | — |

Returns `EventSubscription`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`orientation()` {: #orientation data-toc-label="orientation()" }

**Get Orientation**

Returns current scrollbar axis orientation.

Returns `Orientation`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`orientation(Orientation)` {: #orientationorientation data-toc-label="orientation(Orientation)" }

**Set Orientation**

Configures scrollbar axis orientation (HORIZONTAL or VERTICAL).

| Parameter | Type | Description |
| --- | --- | --- |
| `orientation` | `Orientation` | — |

Returns `ScrollBar`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`pageSize()` {: #pagesize data-toc-label="pageSize()" }

**Get Page Size**

Returns viewport page size used to calculate thumb proportion.

Returns `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`pageSize(float)` {: #pagesizefloat data-toc-label="pageSize(float)" }

**Set Page Size**

Sets viewport page size.

| Parameter | Type | Description |
| --- | --- | --- |
| `pageSize` | `float` | — |

Returns `ScrollBar`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`range(float, float)` {: #rangefloat-float data-toc-label="range(float, float)" }

**Set Range**

Sets both lower and upper scroll boundaries.

| Parameter | Type | Description |
| --- | --- | --- |
| `min` | `float` | — |
| `max` | `float` | — |

Returns `ScrollBar`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`silentValue(float)` {: #silentvaluefloat data-toc-label="silentValue(float)" }

**Set Silent Value**

Sets scroll offset value without firing change listeners.

| Parameter | Type | Description |
| --- | --- | --- |
| `value` | `float` | — |

Returns `ScrollBar`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`step()` {: #step data-toc-label="step()" }

**Get Step**

Returns step distance per scroll increment.

Returns `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`step(float)` {: #stepfloat data-toc-label="step(float)" }

**Set Step**

Sets step distance per scroll increment.

| Parameter | Type | Description |
| --- | --- | --- |
| `step` | `float` | — |

Returns `ScrollBar`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`thumbColor()` {: #thumbcolor data-toc-label="thumbColor()" }

**Thumb Color**

Returns mutable thumb color in resting state.

Returns `MutableColor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`thumbHoverColor()` {: #thumbhovercolor data-toc-label="thumbHoverColor()" }

**Hover Thumb Color**

Returns mutable thumb color when hovered.

Returns `MutableColor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`trackColor()` {: #trackcolor data-toc-label="trackColor()" }

**Track Color**

Returns mutable track background color.

Returns `MutableColor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`value()` {: #value data-toc-label="value()" }

**Get Value**

Returns current scroll offset value.

Returns `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`value(float)` {: #valuefloat data-toc-label="value(float)" }

**Set Value**

Sets scroll offset value clamped to valid range.

| Parameter | Type | Description |
| --- | --- | --- |
| `value` | `float` | — |

Returns `ScrollBar`.

</div>

