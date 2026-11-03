---
title: "Slider"
slug: "slider"
category: "Widgets/Interaction"
locale: "en"
order: 180
---

# Slider

Range slider control allowing continuous or stepped value adjustment with draggable thumb.

## Overview

`Slider` extends `Box` to provide a draggable range control. It renders a background track, an active filled track portion, and a draggable circular thumb.

Supports continuous float values or quantization via `step()`, clamping within `min()` and `max()`, and emits `SliderValueChangedEvent`.

## Brightness Slider

```java
Slider brightness = new Slider()
        .range(0.0f, 100.0f)
        .value(75.0f)
        .step(5.0f);
brightness.onValueChanged(e -> setBrightness(e.newValue()));
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`dragging()` {: #dragging data-toc-label="dragging()" }

**Is Dragging**

Returns whether thumb is currently being dragged.

Returns `boolean`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`fillColor()` {: #fillcolor data-toc-label="fillColor()" }

**Fill Color**

Returns mutable active fill color.

Returns `MutableColor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`knobColor()` {: #knobcolor data-toc-label="knobColor()" }

**Thumb Color**

Returns mutable thumb knob color.

Returns `MutableColor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`max()` {: #max data-toc-label="max()" }

**Get Maximum**

Returns the maximum value bound.

Returns `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`min()` {: #min data-toc-label="min()" }

**Get Minimum**

Returns the minimum value bound.

Returns `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`onValueChanged(EventListener)` {: #onvaluechangedeventlistener data-toc-label="onValueChanged(EventListener)" }

**On Value Changed Listener**

Registers callback triggered when slider value changes.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | — |

Returns `EventSubscription`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`preferredHeight()` {: #preferredheight data-toc-label="preferredHeight()" }

**Get Preferred Height**

Returns preferred layout height.

Returns `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`preferredHeight(float)` {: #preferredheightfloat data-toc-label="preferredHeight(float)" }

**Set Preferred Height**

Sets preferred layout height.

| Parameter | Type | Description |
| --- | --- | --- |
| `preferredHeight` | `float` | — |

Returns `Slider`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`preferredWidth()` {: #preferredwidth data-toc-label="preferredWidth()" }

**Get Preferred Width**

Returns preferred layout width.

Returns `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`preferredWidth(float)` {: #preferredwidthfloat data-toc-label="preferredWidth(float)" }

**Set Preferred Width**

Sets preferred layout width.

| Parameter | Type | Description |
| --- | --- | --- |
| `preferredWidth` | `float` | — |

Returns `Slider`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`range(float, float)` {: #rangefloat-float data-toc-label="range(float, float)" }

**Set Range**

Sets minimum and maximum value bounds.

| Parameter | Type | Description |
| --- | --- | --- |
| `min` | `float` | minimum value |
| `max` | `float` | maximum value |

Returns `Slider`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`step()` {: #step data-toc-label="step()" }

**Get Step**

Returns quantization step interval.

Returns `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`trackColor()` {: #trackcolor data-toc-label="trackColor()" }

**Track Color**

Returns mutable track background color.

Returns `MutableColor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Slider::</span>`value()` {: #value data-toc-label="value()" }

**Get Value**

Returns current normalized slider value.

Returns `float`.

</div>

