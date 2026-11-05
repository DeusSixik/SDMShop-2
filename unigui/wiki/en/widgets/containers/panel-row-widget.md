---
title: "PanelRowWidget"
slug: "panel-row-widget"
category: "Widgets/Containers"
locale: "en"
order: 140
---

# PanelRowWidget

Two-sided row container organizing widgets into left-aligned and right-aligned groups.

## Overview

PanelRowWidget is designed for headers, toolbars, title bars, and form rows. It maintains two distinct widget groups: left-aligned (stacked left-to-right) and right-aligned (stacked right-to-left). Both groups participate in layout calculations while keeping a clear separation between primary actions/titles and secondary controls.

## Sizing & Spacing

The overall row height is configured with `rowHeight(float)`. Inter-element spacing can be fine-tuned via `gap(float)`, `leftGap(float)`, and `rightGap(float)`. Slot widths can be fixed using `leftWidth(float)` and `rightWidth(float)` or left automatic (`LayoutConstraints.AUTO`).

## Toolbar Row Layout

```java
PanelRowWidget row = new PanelRowWidget()
        .rowHeight(24.0f)
        .gap(8.0f);
row.left(titleLabel);
row.addRight(applyButton);
row.addRight(closeButton);
```

## Related links

- [HBox](<hbox.md>)
- [PanelWidget](<panel-widget.md>)
- [SettingRow](<setting-row.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelRowWidget::</span>`left(Widget)` {: #leftwidget data-toc-label="left(Widget)" }

**Set Left [Widget](<../widget.md>)**

Replaces the entire left group with a single widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `widget` | `Widget` | the widget for the left side |

Returns `PanelRowWidget`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelRowWidget::</span>`right(Widget)` {: #rightwidget data-toc-label="right(Widget)" }

**Set Right [Widget](<../widget.md>)**

Replaces the entire right group with a single widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `widget` | `Widget` | the widget for the right side |

Returns `PanelRowWidget`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelRowWidget::</span>`addLeft(Widget)` {: #addleftwidget data-toc-label="addLeft(Widget)" }

**Add Left [Widget](<../widget.md>)**

Appends a widget to the left-aligned group.

| Parameter | Type | Description |
| --- | --- | --- |
| `widget` | `Widget` | the widget to append |

Returns `PanelRowWidget`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelRowWidget::</span>`addRight(Widget)` {: #addrightwidget data-toc-label="addRight(Widget)" }

**Add Right [Widget](<../widget.md>)**

Appends a widget to the right-aligned group.

| Parameter | Type | Description |
| --- | --- | --- |
| `widget` | `Widget` | the widget to append |

Returns `PanelRowWidget`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelRowWidget::</span>`rowHeight(float)` {: #rowheightfloat data-toc-label="rowHeight(float)" }

**Set Row Height**

Sets the preferred height of the row.

| Parameter | Type | Description |
| --- | --- | --- |
| `rowHeight` | `float` | row height in pixels |

Returns `PanelRowWidget`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelRowWidget::</span>`gap(float)` {: #gapfloat data-toc-label="gap(float)" }

**Set Gap**

Sets uniform spacing between elements on both sides and between groups.

| Parameter | Type | Description |
| --- | --- | --- |
| `gap` | `float` | gap size in pixels |

Returns `PanelRowWidget`: this widget for fluent configuration.

</div>

