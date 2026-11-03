---
title: "NumberField"
slug: "number-field"
category: "Widgets/Interaction"
locale: "en"
order: 130
---

# NumberField

Input field strictly accepting numeric values with range bounds, step increments, and drag adjustment.

## Overview

`NumberField` extends `TextInput` to parse and format floating-point and integer numbers. Users can type values directly, use arrow keys to increment/decrement by `step()`, or scrub/drag to adjust values.

## Integer Range Input

```java
NumberField volume = new NumberField()
        .range(0.0, 100.0)
        .step(1.0)
        .value(50.0);
volume.onValueChanged(e -> audio.setVolume(e.newValue()));
```

## Related links

- [Slider](<slider.md>)
- [TextInput](<text-input.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">NumberField::</span>`range(double, double)` {: #rangedouble-double data-toc-label="range(double, double)" }

**Set Range**

Sets both minimum and maximum permissible bounds.

| Parameter | Type | Description |
| --- | --- | --- |
| `min` | `double` | minimum bound |
| `max` | `double` | maximum bound |

Returns `NumberField`: this widget for fluent configuration.

</div>

