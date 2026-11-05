---
title: "ScrollView"
slug: "scroll-view"
category: "Widgets/Containers"
locale: "en"
order: 110
---

# ScrollView

Scrollable viewport container hosting a single content widget with horizontal and vertical scroll support.

## Overview

ScrollView provides a scrollable viewport for content that exceeds visible panel bounds. Scrolling can be configured along X and Y axes via overflow settings (`Overflow.AUTO`, `Overflow.SCROLL`, `Overflow.HIDDEN`). It supports mouse wheel scrolling with Shift modifier toggling horizontal axis, clamp bounds, and fine-grained scroll step customization.

## Scrollbar Modes & Styling

Scrollbars can operate in overlay mode (floating on top of content with interactive hover effect) or reserved mode (allocating dedicated screen space for scrollbar tracks). You can customize track colors, thumb colors, thumb hover colors, track thickness, and gap between viewport and bars.

## Basic ScrollView with Overflow

```java
ScrollView scroll = new ScrollView(largeWidget).scrollStep(18.0f);
scroll.layout(style -> style.overflowX(Overflow.HIDDEN).overflowY(Overflow.AUTO));
```

## Related links

- [Box](<box.md>)
- [PanelWidget](<panel-widget.md>)
- [VBox](<vbox.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollView::</span>`content(Widget)` {: #contentwidget data-toc-label="content(Widget)" }

**Set Content**

Sets the single content widget inside the viewport.

| Parameter | Type | Description |
| --- | --- | --- |
| `content` | `Widget` | the widget to display in the viewport |

Returns `ScrollView`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollView::</span>`scrollTo(float, float)` {: #scrolltofloat-float data-toc-label="scrollTo(float, float)" }

**Scroll To**

Scrolls to absolute coordinates (x, y), clamped to valid bounds.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | target horizontal scroll position |
| `y` | `float` | target vertical scroll position |

Returns `ScrollView`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollView::</span>`scrollBy(float, float)` {: #scrollbyfloat-float data-toc-label="scrollBy(float, float)" }

**Scroll By**

Scrolls relative to current position by dx and dy.

| Parameter | Type | Description |
| --- | --- | --- |
| `dx` | `float` | horizontal delta |
| `dy` | `float` | vertical delta |

Returns `ScrollView`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollView::</span>`scrollStep(float)` {: #scrollstepfloat data-toc-label="scrollStep(float)" }

**Set Scroll Step**

Sets the scroll step multiplier in UI pixels per wheel delta unit.

| Parameter | Type | Description |
| --- | --- | --- |
| `scrollStep` | `float` | scroll step multiplier; minimum 1.0 |

Returns `ScrollView`: this widget for fluent configuration.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollView::</span>`overlayScrollBars(boolean)` {: #overlayscrollbarsboolean data-toc-label="overlayScrollBars(boolean)" }

**Set Overlay Scrollbars**

Controls whether scrollbars overlay content or reserve layout space.

| Parameter | Type | Description |
| --- | --- | --- |
| `overlayScrollBars` | `boolean` | true for overlay mode, false for reserved mode |

Returns `ScrollView`: this widget for fluent configuration.

</div>

