---
title: "Chart"
slug: "chart"
category: "Widgets/Display"
locale: "ru"
order: 140
---

# Chart

Виджет визуализации данных с поддержкой столбчатых и линейных графиков, координатных осей и подсказок.

## Обзор

`Chart` расширяет `Sparkline` до полноценного компонента диаграмм. Поддерживает переключение между столбчатым (BAR) и линейным (LINE) режимами, координатные оси, числовые метки и подсветку столбцов при наведении.

## Столбчатая диаграмма

```java
Chart latencyChart = new Chart()
        .type(Chart.Type.BAR)
        .values(List.of(12, 18, 15, 25, 14))
        .barColor(Color.CYAN);
```

## API

### API

| Метод | Описание |
| --- | --- |
| [`axisColor()`](#axiscolor) | Возвращает цвет координатных осей диаграммы. |
| [`barColor()`](#barcolor) | Возвращает цвет столбцов в режиме столбчатой диаграммы. |
| [`type(Type)`](#typetype) | Устанавливает режим отображения диаграммы (LINE или BAR). |
| [`Chart()`](#chart) | Creates a chart with its default size and line mode. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Chart::</span>`axisColor()` {: #axiscolor data-toc-label="axisColor()" }

**Цвет осей**

Возвращает цвет координатных осей диаграммы.

Возвращает `MutableColor`: The mutable RGBA axis color; changing it overrides the style value..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Chart::</span>`barColor()` {: #barcolor data-toc-label="barColor()" }

**Цвет столбцов**

Возвращает цвет столбцов в режиме столбчатой диаграммы.

Возвращает `MutableColor`: The mutable RGBA bar color; changing it overrides the style value..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Chart::</span>`type(Type)` {: #typetype data-toc-label="type(Type)" }

**Тип диаграммы**

Устанавливает режим отображения диаграммы (LINE или BAR).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `type` | `Type` | Rendering mode; null selects LINE. |

Возвращает `Chart`: This chart for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### `Chart()` {: #chart data-toc-label="Chart()" }

**Create a chart**

Creates a chart with its default size and line mode.

</div>

### Data

| Метод | Описание |
| --- | --- |
| [`values(List)`](#valueslist) | Replaces the plotted values from a Number list. |
| [`values(float[])`](#valuesfloat) | Replaces the plotted values without boxing numeric inputs. |
| [`categories(List)`](#categorieslist) | Sets optional labels aligned by index with the plotted values. |
| [`categories()`](#categories) | Returns the current immutable category-label list. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Chart::</span>`values(List)` {: #valueslist data-toc-label="values(List)" }

**Set chart values**

Replaces the plotted values from a Number list.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `values` | `List` | Values in data order; null clears the series and null entries are treated as zero. |

Возвращает `Chart`: This chart for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Chart::</span>`values(float[])` {: #valuesfloat data-toc-label="values(float[])" }

**Set chart values from floats**

Replaces the plotted values without boxing numeric inputs.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `data` | `float[]` | Values in data order; null clears the series and non-finite values create gaps. |

Возвращает `Chart`: This chart for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Chart::</span>`categories(List)` {: #categorieslist data-toc-label="categories(List)" }

**Set category labels**

Sets optional labels aligned by index with the plotted values.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `categories` | `List` | Category labels in value order; null or an empty list hides labels. |

Возвращает `Chart`: This chart for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Chart::</span>`categories()` {: #categories data-toc-label="categories()" }

**Get category labels**

Returns the current immutable category-label list.

Возвращает `List`: An immutable list of category labels, empty when labels are not configured..

</div>

### Appearance

| Метод | Описание |
| --- | --- |
| [`type()`](#type) | Returns the active line or bar rendering mode. |
| [`hoveredBarColor()`](#hoveredbarcolor) | Returns the mutable color used for the hovered bar. |
| [`barValuesVisible(boolean)`](#barvaluesvisibleboolean) | Enables or hides numeric labels rendered on bars. |
| [`barValuePlacement(BarValuePlacement)`](#barvalueplacementbarvalueplacement) | Selects where numeric labels appear relative to each bar. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Chart::</span>`type()` {: #type data-toc-label="type()" }

**Get chart type**

Returns the active line or bar rendering mode.

Возвращает `Type`: The current rendering mode; defaults to LINE..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Chart::</span>`hoveredBarColor()` {: #hoveredbarcolor data-toc-label="hoveredBarColor()" }

**Get hovered bar color**

Returns the mutable color used for the hovered bar.

Возвращает `MutableColor`: The mutable RGBA hovered-bar color..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Chart::</span>`barValuesVisible(boolean)` {: #barvaluesvisibleboolean data-toc-label="barValuesVisible(boolean)" }

**Show bar values**

Enables or hides numeric labels rendered on bars.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `barValuesVisible` | `boolean` | True to render bar value labels. |

Возвращает `Chart`: This chart for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Chart::</span>`barValuePlacement(BarValuePlacement)` {: #barvalueplacementbarvalueplacement data-toc-label="barValuePlacement(BarValuePlacement)" }

**Place bar value labels**

Selects where numeric labels appear relative to each bar.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `barValuePlacement` | `BarValuePlacement` | Placement mode; null resets to HEAD. |

Возвращает `Chart`: This chart for fluent configuration..

</div>

### Custom Rendering

| Метод | Описание |
| --- | --- |
| [`barRenderer(BarRenderer)`](#barrendererbarrenderer) | Installs or clears a custom bar renderer. |
| [`barValueRenderer(BarValueRenderer)`](#barvaluerendererbarvaluerenderer) | Installs or clears a custom bar-value label renderer. |
| [`barTooltipRenderer(BarTooltipRenderer)`](#bartooltiprendererbartooltiprenderer) | Installs or clears a custom tooltip renderer for hovered bars. |
| [`renderer(WidgetRender)`](#rendererwidgetrender) | Sets the widget-level renderer used for chart rendering hooks. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Chart::</span>`barRenderer(BarRenderer)` {: #barrendererbarrenderer data-toc-label="barRenderer(BarRenderer)" }

**Customize bar rendering**

Installs or clears a custom bar renderer.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `barRenderer` | `BarRenderer` | Custom renderer callback, or null to use the built-in bar rendering. |

Возвращает `Chart`: This chart for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Chart::</span>`barValueRenderer(BarValueRenderer)` {: #barvaluerendererbarvaluerenderer data-toc-label="barValueRenderer(BarValueRenderer)" }

**Customize bar value labels**

Installs or clears a custom bar-value label renderer.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `barValueRenderer` | `BarValueRenderer` | Custom renderer callback, or null to use built-in value labels. |

Возвращает `Chart`: This chart for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Chart::</span>`barTooltipRenderer(BarTooltipRenderer)` {: #bartooltiprendererbartooltiprenderer data-toc-label="barTooltipRenderer(BarTooltipRenderer)" }

**Customize bar tooltips**

Installs or clears a custom tooltip renderer for hovered bars.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `barTooltipRenderer` | `BarTooltipRenderer` | Custom renderer callback, or null to use the built-in tooltip. |

Возвращает `Chart`: This chart for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Chart::</span>`renderer(WidgetRender)` {: #rendererwidgetrender data-toc-label="renderer(WidgetRender)" }

**Set chart renderer**

Sets the widget-level renderer used for chart rendering hooks.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `renderer` | `WidgetRender` | [Widget](<../widget.md>) renderer callback. |

Возвращает `Chart`: This chart for fluent configuration..

</div>

### Layout

| Метод | Описание |
| --- | --- |
| [`preferredWidth(float)`](#preferredwidthfloat) | Sets the preferred chart width in UI pixels. |
| [`preferredHeight(float)`](#preferredheightfloat) | Sets the preferred chart height in UI pixels. |
| [`preferredSize(float, float)`](#preferredsizefloat-float) | Sets preferred width and height in UI pixels. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Chart::</span>`preferredWidth(float)` {: #preferredwidthfloat data-toc-label="preferredWidth(float)" }

**Set preferred chart width**

Sets the preferred chart width in UI pixels.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `preferredWidth` | `float` | Preferred width in UI pixels; non-positive or non-finite values use the default. |

Возвращает `Chart`: This chart for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Chart::</span>`preferredHeight(float)` {: #preferredheightfloat data-toc-label="preferredHeight(float)" }

**Set preferred chart height**

Sets the preferred chart height in UI pixels.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `preferredHeight` | `float` | Preferred height in UI pixels; non-positive or non-finite values use the default. |

Возвращает `Chart`: This chart for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Chart::</span>`preferredSize(float, float)` {: #preferredsizefloat-float data-toc-label="preferredSize(float, float)" }

**Set preferred chart size**

Sets preferred width and height in UI pixels.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | Preferred width in UI pixels. |
| `height` | `float` | Preferred height in UI pixels. |

Возвращает `Chart`: This chart for fluent configuration..

</div>

### Interaction

| Метод | Описание |
| --- | --- |
| [`onBarClick(EventListener)`](#onbarclickeventlistener) | Registers a listener for clicks on chart bars. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Chart::</span>`onBarClick(EventListener)` {: #onbarclickeventlistener data-toc-label="onBarClick(EventListener)" }

**Listen for bar clicks**

Registers a listener for clicks on chart bars.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Listener receiving the clicked bar event. |

Возвращает `EventSubscription`: A subscription handle that can be cancelled..

</div>

