---
title: "LinearBox"
slug: "linear-box"
category: "Widgets/Containers"
locale: "en"
order: 40
---

# LinearBox

One-dimensional flex container arranging children sequentially along a main axis without wrapping.

## Overview

`LinearBox` is a 1D flex container that synchronizes its `orientation` and `spacing` with its internal `LayoutStyle`. Horizontal orientation maps to `FlexDirection.ROW` with `columnGap`, while vertical orientation maps to `FlexDirection.COLUMN` with `rowGap`.

For common fixed-orientation layouts, prefer `VBox` (vertical) or `HBox` (horizontal). Use `LinearBox` directly when the orientation axis must change dynamically at runtime.

## Opt-in Visual Surface

`LinearBox` inherits `SurfaceWidget`, meaning backgrounds, borders, and textures are supported. To preserve transparency by default, surface rendering is initially disabled (`boxVisualEnabled=false`). The first call to `background(color)` or `border(color)` automatically enables surface rendering.

## Dynamic Orientation Row

```java
LinearBox box = new LinearBox(Orientation.HORIZONTAL)
        .spacing(8.0f)
        .background(ColorView.ofHex("#2A2D32"));
box.addChild(btn1);
box.addChild(btn2);
```

## Related links

- [FlexBox](<flex-box.md>)
- [HBox](<hbox.md>)
- [VBox](<vbox.md>)

## API

<div class="api-member-card" markdown="1">

### `LinearBox(Orientation)` {: #linearboxorientation data-toc-label="LinearBox(Orientation)" }

**Constructor**

Creates a LinearBox with the specified orientation.

| Parameter | Type | Description |
| --- | --- | --- |
| `orientation` | `Orientation` | target orientation |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LinearBox::</span>`orientation(Orientation)` {: #orientationorientation data-toc-label="orientation(Orientation)" }

**Set Orientation**

Sets the arrangement axis (HORIZONTAL or VERTICAL).

| Parameter | Type | Description |
| --- | --- | --- |
| `orientation` | `Orientation` | target orientation |

Returns `LinearBox`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LinearBox::</span>`spacing(float)` {: #spacingfloat data-toc-label="spacing(float)" }

**Set Spacing**

Sets uniform gap in UI pixels between adjacent children along main axis.

| Parameter | Type | Description |
| --- | --- | --- |
| `spacing` | `float` | gap in UI pixels |

Returns `LinearBox`: this widget for fluent configuration.

</div>

