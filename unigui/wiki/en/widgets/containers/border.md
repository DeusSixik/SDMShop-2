---
title: "Border"
slug: "border"
category: "Widgets/Containers"
locale: "en"
order: 170
---

# Border

Render-only decorative outline widget without child widgets.

## Overview

`Border` is a lightweight, render-only decorative outline widget without child widgets. It is useful as a standalone overlay layer — for example, to draw an active selection outline, highlight bounds during debugging, or draw decorative frames.

If you need a container with background, border, and nested children, prefer `Box`.

## Selection Outline

```java
Border outline = new Border()
        .thickness(2.0f)
        .radius(4.0f);
outline.color().set(0.2f, 0.6f, 1.0f, 1.0f);
```

## Related links

- [Box](<box.md>)

## API

<div class="api-member-card" markdown="1">

### `Border()` {: #border data-toc-label="Border()" }

**Constructor**

Creates a border decorator with default 1px white border.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Border::</span>`color()` {: #color data-toc-label="color()" }

**Border Color**

Returns the live mutable border color.

Returns `MutableColor`: live MutableColor of the border.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Border::</span>`thickness(float)` {: #thicknessfloat data-toc-label="thickness(float)" }

**Set thickness**

Sets border stroke thickness in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `thickness` | `float` | Stroke thickness in UI pixels |

Returns `Border`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Border::</span>`radius(float)` {: #radiusfloat data-toc-label="radius(float)" }

**Set radius**

Sets corner radius in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `radius` | `float` | Corner radius in UI pixels |

Returns `Border`: this widget for fluent configuration.

</div>

