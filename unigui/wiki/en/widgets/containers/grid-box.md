---
title: "GridBox"
slug: "grid-box"
category: "Widgets/Containers"
locale: "en"
order: 80
---

# GridBox

Multi-column grid container placing children into uniform cells or custom track templates.

## Overview

`GridBox` arranges children in a two-dimensional grid. By default, it splits available width into a fixed number of uniform columns (`columns(n)`) and lays out children sequentially in row-major order.

Spacing can be configured globally via `spacing(px)` or independently via `horizontalSpacing(px)` and `verticalSpacing(px)`.

For complex tracks (proportional `fr`, pixel `px`, or `minmax`), assign custom track templates with `gridColumns(GridTrack...)`.

## Inventory Grid

```java
GridBox grid = new GridBox()
        .columns(4)
        .spacing(6.0f);
for (ItemSlot slot : slots) {
    grid.addChild(slot);
}
```

## Related links

- [FlexBox](<flex-box.md>)
- [LayoutStyle](<../../core/layout/layout-style.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">GridBox::</span>`columns(int)` {: #columnsint data-toc-label="columns(int)" }

**Set Columns**

Sets fixed column count for automatic grid distribution.

| Parameter | Type | Description |
| --- | --- | --- |
| `columns` | `int` | number of columns; minimum 1 |

Returns `GridBox`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">GridBox::</span>`spacing(float)` {: #spacingfloat data-toc-label="spacing(float)" }

**Set Spacing**

Sets uniform row and column spacing in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `spacing` | `float` | gap in UI pixels |

Returns `GridBox`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">GridBox::</span>`horizontalSpacing(float)` {: #horizontalspacingfloat data-toc-label="horizontalSpacing(float)" }

**Set Horizontal Spacing**

Sets column gap in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `horizontalSpacing` | `float` | horizontal gap in pixels |

Returns `GridBox`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">GridBox::</span>`verticalSpacing(float)` {: #verticalspacingfloat data-toc-label="verticalSpacing(float)" }

**Set Vertical Spacing**

Sets row gap in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `verticalSpacing` | `float` | vertical gap in pixels |

Returns `GridBox`: this widget for fluent configuration.

</div>

