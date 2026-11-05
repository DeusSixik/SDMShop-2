---
title: "Separator"
slug: "separator"
category: "Widgets/Display"
locale: "en"
order: 60
---

# Separator

Horizontal or vertical divider line widget for visually grouping interface sections.

## Overview

`Separator` draws a clean divider line along either horizontal or vertical orientation. Useful for demarcating menu items, toolbars, and section boundaries.

## Divider Line

```java
Separator divider = new Separator()
        .orientation(Orientation.HORIZONTAL)
        .thickness(1.0f)
        .color(Color.of(0.2f, 0.2f, 0.2f, 1.0f));
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Separator::</span>`color()` {: #color data-toc-label="color()" }

**Line Color**

Returns mutable color of separator divider.

Returns `MutableColor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Separator::</span>`orientation(Orientation)` {: #orientationorientation data-toc-label="orientation(Orientation)" }

**Orientation**

Returns or sets horizontal or vertical orientation.

| Parameter | Type | Description |
| --- | --- | --- |
| `orientation` | `Orientation` | — |

Returns `Separator`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Separator::</span>`thickness(float)` {: #thicknessfloat data-toc-label="thickness(float)" }

**Line Thickness**

Returns or sets divider line stroke thickness in pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `thickness` | `float` | — |

Returns `Separator`.

</div>

