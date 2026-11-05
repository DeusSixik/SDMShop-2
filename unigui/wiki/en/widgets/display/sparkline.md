---
title: "Sparkline"
slug: "sparkline"
category: "Widgets/Display"
locale: "en"
order: 130
---

# Sparkline

Compact mini trendline and telemetry chart widget with fill gradient and hover data points.

## Overview

`Sparkline` visualizes numerical trend series in a compact space. Features smooth line smoothing, under-curve area fill gradient, interactive cursor hover tooltips, and data point highlighting.

## Telemetry Sparkline

```java
Sparkline fpsChart = new Sparkline()
        .values(List.of(60, 58, 59, 62, 60, 55, 60))
        .lineColor(Color.GREEN);
```

## API

### API

| Method | Description |
| --- | --- |
| [`fillColor()`](#fillcolor) | Returns mutable under-curve gradient fill color. |
| [`lineColor()`](#linecolor) | Returns mutable line stroke color. |
| [`pointColor()`](#pointcolor) | Returns mutable point marker color. |
| [`values(List)`](#valueslist) | Sets numerical data points plotted on the sparkline. |
| [`Sparkline()`](#sparkline) | Creates an empty sparkline with its default preferred size. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`fillColor()` {: #fillcolor data-toc-label="fillColor()" }

**Fill Color**

Returns mutable under-curve gradient fill color.

Returns `MutableColor`: The mutable RGBA fill color used below the line..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`lineColor()` {: #linecolor data-toc-label="lineColor()" }

**Line Color**

Returns mutable line stroke color.

Returns `MutableColor`: The mutable RGBA line color; changing it overrides the style value..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`pointColor()` {: #pointcolor data-toc-label="pointColor()" }

**Point Color**

Returns mutable point marker color.

Returns `MutableColor`: The mutable RGBA color used for data-point markers..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`values(List)` {: #valueslist data-toc-label="values(List)" }

**Data Values**

Sets numerical data points plotted on the sparkline.

| Parameter | Type | Description |
| --- | --- | --- |
| `values` | `List` | Values in series order; null clears the series and null entries are treated as zero. |

Returns `Sparkline`: This sparkline for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### `Sparkline()` {: #sparkline data-toc-label="Sparkline()" }

**Create a sparkline**

Creates an empty sparkline with its default preferred size.

</div>

### Data

| Method | Description |
| --- | --- |
| [`values(float[])`](#valuesfloat) | Replaces the series without boxing numeric inputs. |
| [`values()`](#values) | Returns the current series as an immutable view. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`values(float[])` {: #valuesfloat data-toc-label="values(float[])" }

**Set sparkline values from floats**

Replaces the series without boxing numeric inputs.

| Parameter | Type | Description |
| --- | --- | --- |
| `data` | `float[]` | Values in series order; null clears the series and non-finite values create gaps. |

Returns `Sparkline`: This sparkline for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`values()` {: #values data-toc-label="values()" }

**Get sparkline values**

Returns the current series as an immutable view.

Returns `List`: An unmodifiable view of the current float values..

</div>

### Appearance

| Method | Description |
| --- | --- |
| [`hoveredPointColor()`](#hoveredpointcolor) | Returns the mutable color used for the hovered point. |
| [`labelColor()`](#labelcolor) | Returns the mutable color used for point value labels. |
| [`tooltipBackground()`](#tooltipbackground) | Returns the mutable sparkline tooltip background color. |
| [`tooltipBorder()`](#tooltipborder) | Returns the mutable sparkline tooltip border color. |
| [`fillVisible(boolean)`](#fillvisibleboolean) | Enables or disables the area fill beneath the line. |
| [`pointMode(PointMode)`](#pointmodepointmode) | Selects which data points are rendered and interactive. |
| [`pointMode()`](#pointmode) | Returns the current marker and hit-testing mode. |
| [`pointLabelsVisible(boolean)`](#pointlabelsvisibleboolean) | Enables or disables numeric labels beside data points. |
| [`pointLabelPlacement(PointLabelPlacement)`](#pointlabelplacementpointlabelplacement) | Sets where point value labels are placed relative to markers. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`hoveredPointColor()` {: #hoveredpointcolor data-toc-label="hoveredPointColor()" }

**Get hovered point color**

Returns the mutable color used for the hovered point.

Returns `MutableColor`: The mutable RGBA hovered-point color..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`labelColor()` {: #labelcolor data-toc-label="labelColor()" }

**Get point label color**

Returns the mutable color used for point value labels.

Returns `MutableColor`: The mutable RGBA point-label color..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`tooltipBackground()` {: #tooltipbackground data-toc-label="tooltipBackground()" }

**Get tooltip background color**

Returns the mutable sparkline tooltip background color.

Returns `MutableColor`: The mutable RGBA tooltip background color..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`tooltipBorder()` {: #tooltipborder data-toc-label="tooltipBorder()" }

**Get tooltip border color**

Returns the mutable sparkline tooltip border color.

Returns `MutableColor`: The mutable RGBA tooltip border color..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`fillVisible(boolean)` {: #fillvisibleboolean data-toc-label="fillVisible(boolean)" }

**Show sparkline fill**

Enables or disables the area fill beneath the line.

| Parameter | Type | Description |
| --- | --- | --- |
| `fillVisible` | `boolean` | True to render the fill; this explicit value overrides the style setting. |

Returns `Sparkline`: This sparkline for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`pointMode(PointMode)` {: #pointmodepointmode data-toc-label="pointMode(PointMode)" }

**Set point mode**

Selects which data points are rendered and interactive.

| Parameter | Type | Description |
| --- | --- | --- |
| `pointMode` | `PointMode` | Point mode; null is treated as NONE. |

Returns `Sparkline`: This sparkline for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`pointMode()` {: #pointmode data-toc-label="pointMode()" }

**Get point mode**

Returns the current marker and hit-testing mode.

Returns `PointMode`: The active point mode..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`pointLabelsVisible(boolean)` {: #pointlabelsvisibleboolean data-toc-label="pointLabelsVisible(boolean)" }

**Show point labels**

Enables or disables numeric labels beside data points.

| Parameter | Type | Description |
| --- | --- | --- |
| `pointLabelsVisible` | `boolean` | True to render point value labels. |

Returns `Sparkline`: This sparkline for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`pointLabelPlacement(PointLabelPlacement)` {: #pointlabelplacementpointlabelplacement data-toc-label="pointLabelPlacement(PointLabelPlacement)" }

**Set point label placement**

Sets where point value labels are placed relative to markers.

| Parameter | Type | Description |
| --- | --- | --- |
| `pointLabelPlacement` | `PointLabelPlacement` | Placement mode; null resets to ABOVE. |

Returns `Sparkline`: This sparkline for fluent configuration..

</div>

### Custom Rendering

| Method | Description |
| --- | --- |
| [`pointRenderer(SparkPointRenderer)`](#pointrenderersparkpointrenderer) | Installs or clears a custom data-point renderer. |
| [`pointLabelRenderer(SparkPointLabelRenderer)`](#pointlabelrenderersparkpointlabelrenderer) | Installs or clears a custom point-label renderer. |
| [`pointTooltipRenderer(SparkPointTooltipRenderer)`](#pointtooltiprenderersparkpointtooltiprenderer) | Installs or clears a custom tooltip renderer. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`pointRenderer(SparkPointRenderer)` {: #pointrenderersparkpointrenderer data-toc-label="pointRenderer(SparkPointRenderer)" }

**Customize point rendering**

Installs or clears a custom data-point renderer.

| Parameter | Type | Description |
| --- | --- | --- |
| `pointRenderer` | `SparkPointRenderer` | Custom renderer, or null to use built-in point rendering. |

Returns `Sparkline`: This sparkline for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`pointLabelRenderer(SparkPointLabelRenderer)` {: #pointlabelrenderersparkpointlabelrenderer data-toc-label="pointLabelRenderer(SparkPointLabelRenderer)" }

**Customize point labels**

Installs or clears a custom point-label renderer.

| Parameter | Type | Description |
| --- | --- | --- |
| `pointLabelRenderer` | `SparkPointLabelRenderer` | Custom renderer, or null to use built-in label rendering. |

Returns `Sparkline`: This sparkline for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`pointTooltipRenderer(SparkPointTooltipRenderer)` {: #pointtooltiprenderersparkpointtooltiprenderer data-toc-label="pointTooltipRenderer(SparkPointTooltipRenderer)" }

**Customize point tooltips**

Installs or clears a custom tooltip renderer.

| Parameter | Type | Description |
| --- | --- | --- |
| `pointTooltipRenderer` | `SparkPointTooltipRenderer` | Custom renderer, or null to use built-in tooltip rendering. |

Returns `Sparkline`: This sparkline for fluent configuration..

</div>

### Layout

| Method | Description |
| --- | --- |
| [`preferredWidth()`](#preferredwidth) | Returns the preferred width in UI pixels. |
| [`preferredWidth(float)`](#preferredwidthfloat) | Sets the preferred width in UI pixels. |
| [`preferredHeight()`](#preferredheight) | Returns the preferred height in UI pixels. |
| [`preferredHeight(float)`](#preferredheightfloat) | Sets the preferred height in UI pixels. |
| [`preferredSize(float, float)`](#preferredsizefloat-float) | Sets preferred width and height in UI pixels. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`preferredWidth()` {: #preferredwidth data-toc-label="preferredWidth()" }

**Get preferred sparkline width**

Returns the preferred width in UI pixels.

Returns `float`: Preferred width in UI pixels..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`preferredWidth(float)` {: #preferredwidthfloat data-toc-label="preferredWidth(float)" }

**Set preferred sparkline width**

Sets the preferred width in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `preferredWidth` | `float` | Preferred width in UI pixels; non-positive or non-finite values use the default. |

Returns `Sparkline`: This sparkline for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`preferredHeight()` {: #preferredheight data-toc-label="preferredHeight()" }

**Get preferred sparkline height**

Returns the preferred height in UI pixels.

Returns `float`: Preferred height in UI pixels..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`preferredHeight(float)` {: #preferredheightfloat data-toc-label="preferredHeight(float)" }

**Set preferred sparkline height**

Sets the preferred height in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `preferredHeight` | `float` | Preferred height in UI pixels; non-positive or non-finite values use the default. |

Returns `Sparkline`: This sparkline for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`preferredSize(float, float)` {: #preferredsizefloat-float data-toc-label="preferredSize(float, float)" }

**Set preferred sparkline size**

Sets preferred width and height in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | Preferred width in UI pixels. |
| `height` | `float` | Preferred height in UI pixels. |

Returns `Sparkline`: This sparkline for fluent configuration..

</div>

### Interaction

| Method | Description |
| --- | --- |
| [`onPointClick(EventListener)`](#onpointclickeventlistener) | Registers a listener for clicks on data points. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`onPointClick(EventListener)` {: #onpointclickeventlistener data-toc-label="onPointClick(EventListener)" }

**Listen for point clicks**

Registers a listener for clicks on data points.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | Listener receiving the clicked point event. |

Returns `EventSubscription`: A subscription handle that can be cancelled..

</div>

