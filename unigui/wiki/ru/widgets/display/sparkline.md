---
title: "Sparkline"
slug: "sparkline"
category: "Widgets/Display"
locale: "ru"
order: 130
---

# Sparkline

Компактный микрографик трендов и телеметрии с градиентной заливкой и точками при наведении.

## Обзор

`Sparkline` отображает числовые ряды значений в компактном виде. Поддерживает сглаживание линий, градиентную заливку под графиком, интерактивные подсказки при наведении и подсветку точек данных.

## График телеметрии

```java
Sparkline fpsChart = new Sparkline()
        .values(List.of(60, 58, 59, 62, 60, 55, 60))
        .lineColor(Color.GREEN);
```

## API

### API

| Метод | Описание |
| --- | --- |
| [`fillColor()`](#fillcolor) | Возвращает цвет градиентной заливки под линией графика. |
| [`lineColor()`](#linecolor) | Возвращает цвет линии графика. |
| [`pointColor()`](#pointcolor) | Возвращает цвет маркеров точек данных. |
| [`values(List)`](#valueslist) | Устанавливает список числовых значений для построения графика. |
| [`Sparkline()`](#sparkline) | Creates an empty sparkline with its default preferred size. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`fillColor()` {: #fillcolor data-toc-label="fillColor()" }

**Цвет заливки**

Возвращает цвет градиентной заливки под линией графика.

Возвращает `MutableColor`: The mutable RGBA fill color used below the line..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`lineColor()` {: #linecolor data-toc-label="lineColor()" }

**Цвет линии**

Возвращает цвет линии графика.

Возвращает `MutableColor`: The mutable RGBA line color; changing it overrides the style value..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`pointColor()` {: #pointcolor data-toc-label="pointColor()" }

**Цвет точек данных**

Возвращает цвет маркеров точек данных.

Возвращает `MutableColor`: The mutable RGBA color used for data-point markers..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`values(List)` {: #valueslist data-toc-label="values(List)" }

**Значения данных**

Устанавливает список числовых значений для построения графика.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `values` | `List` | Values in series order; null clears the series and null entries are treated as zero. |

Возвращает `Sparkline`: This sparkline for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### `Sparkline()` {: #sparkline data-toc-label="Sparkline()" }

**Create a sparkline**

Creates an empty sparkline with its default preferred size.

</div>

### Data

| Метод | Описание |
| --- | --- |
| [`values(float[])`](#valuesfloat) | Replaces the series without boxing numeric inputs. |
| [`values()`](#values) | Returns the current series as an immutable view. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`values(float[])` {: #valuesfloat data-toc-label="values(float[])" }

**Set sparkline values from floats**

Replaces the series without boxing numeric inputs.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `data` | `float[]` | Values in series order; null clears the series and non-finite values create gaps. |

Возвращает `Sparkline`: This sparkline for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`values()` {: #values data-toc-label="values()" }

**Get sparkline values**

Returns the current series as an immutable view.

Возвращает `List`: An unmodifiable view of the current float values..

</div>

### Appearance

| Метод | Описание |
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

Возвращает `MutableColor`: The mutable RGBA hovered-point color..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`labelColor()` {: #labelcolor data-toc-label="labelColor()" }

**Get point label color**

Returns the mutable color used for point value labels.

Возвращает `MutableColor`: The mutable RGBA point-label color..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`tooltipBackground()` {: #tooltipbackground data-toc-label="tooltipBackground()" }

**Get tooltip background color**

Returns the mutable sparkline tooltip background color.

Возвращает `MutableColor`: The mutable RGBA tooltip background color..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`tooltipBorder()` {: #tooltipborder data-toc-label="tooltipBorder()" }

**Get tooltip border color**

Returns the mutable sparkline tooltip border color.

Возвращает `MutableColor`: The mutable RGBA tooltip border color..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`fillVisible(boolean)` {: #fillvisibleboolean data-toc-label="fillVisible(boolean)" }

**Show sparkline fill**

Enables or disables the area fill beneath the line.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `fillVisible` | `boolean` | True to render the fill; this explicit value overrides the style setting. |

Возвращает `Sparkline`: This sparkline for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`pointMode(PointMode)` {: #pointmodepointmode data-toc-label="pointMode(PointMode)" }

**Set point mode**

Selects which data points are rendered and interactive.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `pointMode` | `PointMode` | Point mode; null is treated as NONE. |

Возвращает `Sparkline`: This sparkline for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`pointMode()` {: #pointmode data-toc-label="pointMode()" }

**Get point mode**

Returns the current marker and hit-testing mode.

Возвращает `PointMode`: The active point mode..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`pointLabelsVisible(boolean)` {: #pointlabelsvisibleboolean data-toc-label="pointLabelsVisible(boolean)" }

**Show point labels**

Enables or disables numeric labels beside data points.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `pointLabelsVisible` | `boolean` | True to render point value labels. |

Возвращает `Sparkline`: This sparkline for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`pointLabelPlacement(PointLabelPlacement)` {: #pointlabelplacementpointlabelplacement data-toc-label="pointLabelPlacement(PointLabelPlacement)" }

**Set point label placement**

Sets where point value labels are placed relative to markers.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `pointLabelPlacement` | `PointLabelPlacement` | Placement mode; null resets to ABOVE. |

Возвращает `Sparkline`: This sparkline for fluent configuration..

</div>

### Custom Rendering

| Метод | Описание |
| --- | --- |
| [`pointRenderer(SparkPointRenderer)`](#pointrenderersparkpointrenderer) | Installs or clears a custom data-point renderer. |
| [`pointLabelRenderer(SparkPointLabelRenderer)`](#pointlabelrenderersparkpointlabelrenderer) | Installs or clears a custom point-label renderer. |
| [`pointTooltipRenderer(SparkPointTooltipRenderer)`](#pointtooltiprenderersparkpointtooltiprenderer) | Installs or clears a custom tooltip renderer. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`pointRenderer(SparkPointRenderer)` {: #pointrenderersparkpointrenderer data-toc-label="pointRenderer(SparkPointRenderer)" }

**Customize point rendering**

Installs or clears a custom data-point renderer.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `pointRenderer` | `SparkPointRenderer` | Custom renderer, or null to use built-in point rendering. |

Возвращает `Sparkline`: This sparkline for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`pointLabelRenderer(SparkPointLabelRenderer)` {: #pointlabelrenderersparkpointlabelrenderer data-toc-label="pointLabelRenderer(SparkPointLabelRenderer)" }

**Customize point labels**

Installs or clears a custom point-label renderer.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `pointLabelRenderer` | `SparkPointLabelRenderer` | Custom renderer, or null to use built-in label rendering. |

Возвращает `Sparkline`: This sparkline for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`pointTooltipRenderer(SparkPointTooltipRenderer)` {: #pointtooltiprenderersparkpointtooltiprenderer data-toc-label="pointTooltipRenderer(SparkPointTooltipRenderer)" }

**Customize point tooltips**

Installs or clears a custom tooltip renderer.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `pointTooltipRenderer` | `SparkPointTooltipRenderer` | Custom renderer, or null to use built-in tooltip rendering. |

Возвращает `Sparkline`: This sparkline for fluent configuration..

</div>

### Layout

| Метод | Описание |
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

Возвращает `float`: Preferred width in UI pixels..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`preferredWidth(float)` {: #preferredwidthfloat data-toc-label="preferredWidth(float)" }

**Set preferred sparkline width**

Sets the preferred width in UI pixels.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `preferredWidth` | `float` | Preferred width in UI pixels; non-positive or non-finite values use the default. |

Возвращает `Sparkline`: This sparkline for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`preferredHeight()` {: #preferredheight data-toc-label="preferredHeight()" }

**Get preferred sparkline height**

Returns the preferred height in UI pixels.

Возвращает `float`: Preferred height in UI pixels..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`preferredHeight(float)` {: #preferredheightfloat data-toc-label="preferredHeight(float)" }

**Set preferred sparkline height**

Sets the preferred height in UI pixels.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `preferredHeight` | `float` | Preferred height in UI pixels; non-positive or non-finite values use the default. |

Возвращает `Sparkline`: This sparkline for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`preferredSize(float, float)` {: #preferredsizefloat-float data-toc-label="preferredSize(float, float)" }

**Set preferred sparkline size**

Sets preferred width and height in UI pixels.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | Preferred width in UI pixels. |
| `height` | `float` | Preferred height in UI pixels. |

Возвращает `Sparkline`: This sparkline for fluent configuration..

</div>

### Interaction

| Метод | Описание |
| --- | --- |
| [`onPointClick(EventListener)`](#onpointclickeventlistener) | Registers a listener for clicks on data points. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Sparkline::</span>`onPointClick(EventListener)` {: #onpointclickeventlistener data-toc-label="onPointClick(EventListener)" }

**Listen for point clicks**

Registers a listener for clicks on data points.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Listener receiving the clicked point event. |

Возвращает `EventSubscription`: A subscription handle that can be cancelled..

</div>

