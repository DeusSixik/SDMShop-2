---
title: "TextEngine"
slug: "text-engine"
category: "Core/Text"
locale: "ru"
order: 30
---

# TextEngine

Базовый движок компоновки текста, переноса строк, расчёта метрик и отрисовки встроенного inline-контента.

## Обзор

`TextEngine` — централизованная подсистема, отвечающая за измерение ширины символов, перенос длинных текстовых фрагментов по строкам, выравнивание блоков по горизонтали и вертикали, а также позиционирование встроенных inline-элементов (иконок, бейджей, текстур).

## Измерение текста и содержимого

Методы измерения рассчитывают ширину строк (`measureLineWidth`) и высоту текстовых блоков (`measureTextHeight`) с учётом метрик шрифта (ascent, descent), кодовых точек Unicode и атомарных inline-span'ов. Метод `minContentWidth` находит ширину самого длинного неделимого слова.

## Перенос по строкам (Line Wrapping)

Метод `wrapLines()` разбивает форматированный текст по границам слов так, чтобы он не превышал заданную максимальную ширину (`maxWidth`). Если отдельное слово длиннее строки, оно разбивается по символам.

## Рендеринг и inline-содержимое

При отрисовке обычного текста TextEngine передаёт команды напрямую в `RenderBackend`. Если текст содержит inline-элементы, движок рассчитывает точные координаты для каждого блока и вызывает соответствующий `InlineContentRenderer` с вычисленными границами.

## Измерение и перенос форматированного текста

```java
float maxWidth = 200.0f;
List<RichText> lines = TextEngine.wrapLines(context, richText, maxWidth);
float totalHeight = TextEngine.linesHeight(context, lines);
```

## Отрисовка выровненного текста на холсте

```java
TextEngine.draw(context, richText, x, y, width, height, paint, transform,
    Alignment.CENTER, Alignment.CENTER);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`measureLineWidth(String)` {: #measurelinewidthstring data-toc-label="measureLineWidth(String)" }

**measureLineWidth(String)**

Измеряет ширину строки по усреднённым глифам без активного бэкенда.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Текст для измерения. |

Возвращает `float`: Ширина строки в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`minContentWidth(RichText)` {: #mincontentwidthrichtext data-toc-label="minContentWidth(RichText)" }

**minContentWidth([RichText](<rich-text.md>))**

Вычисляет ширину самого длинного неделимого слова в тексте.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `RichText` | Rich-текст для анализа. |

Возвращает `float`: Минимальная контентная ширина в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`measureLineWidth(RenderContext, String)` {: #measurelinewidthrendercontext-string data-toc-label="measureLineWidth(RenderContext, String)" }

**measureLineWidth(RenderContext, String)**

Измеряет ширину строки по метрикам шрифта активного бэкенда.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `context` | `RenderContext` | Активный контекст рендера. |
| `text` | `String` | Текстовая строка. |

Возвращает `float`: Ширина строки в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`measureLineWidth(RenderContext, RichText)` {: #measurelinewidthrendercontext-richtext data-toc-label="measureLineWidth(RenderContext, RichText)" }

**measureLineWidth(RenderContext, [RichText](<rich-text.md>))**

Измеряет ширину rich-текста с учётом шрифтов и inline-элементов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `context` | `RenderContext` | Активный контекст рендера. |
| `text` | `RichText` | Экземпляр [RichText](<rich-text.md>). |

Возвращает `float`: Максимальная ширина строки в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`measureLineWidth(RichText)` {: #measurelinewidthrichtext data-toc-label="measureLineWidth(RichText)" }

**measureLineWidth([RichText](<rich-text.md>))**

Измеряет ширину rich-текста по встроенным в фрагменты шрифтам.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `RichText` | Экземпляр [RichText](<rich-text.md>). |

Возвращает `float`: Ширина строки в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`measureTextHeight(RichText)` {: #measuretextheightrichtext data-toc-label="measureTextHeight(RichText)" }

**measureTextHeight([RichText](<rich-text.md>))**

Вычисляет общую высоту всех строк rich-текста.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `RichText` | Экземпляр [RichText](<rich-text.md>). |

Возвращает `float`: Полная высота текста в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`measureTextHeight(RenderContext, RichText)` {: #measuretextheightrendercontext-richtext data-toc-label="measureTextHeight(RenderContext, RichText)" }

**measureTextHeight(RenderContext, [RichText](<rich-text.md>))**

Вычисляет высоту rich-текста по метрикам шрифта активного бэкенда.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `context` | `RenderContext` | Активный контекст рендера. |
| `text` | `RichText` | Экземпляр [RichText](<rich-text.md>). |

Возвращает `float`: Высота текста в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`wrapLines(RichText, float)` {: #wraplinesrichtext-float data-toc-label="wrapLines(RichText, float)" }

**wrapLines([RichText](<rich-text.md>), float)**

Разбивает rich-текст на строки, укладывающиеся в максимальную ширину.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `RichText` | Текст для переноса. |
| `maxWidth` | `float` | Максимальная ширина строки. |

Возвращает `List`: Список перенесённых строк [RichText](<rich-text.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`wrapLines(RenderContext, RichText, float)` {: #wraplinesrendercontext-richtext-float data-toc-label="wrapLines(RenderContext, RichText, float)" }

**wrapLines(RenderContext, [RichText](<rich-text.md>), float)**

Разбивает rich-текст на строки с использованием бэкенда.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `context` | `RenderContext` | Активный контекст рендера. |
| `text` | `RichText` | Текст для переноса. |
| `maxWidth` | `float` | Максимальная ширина строки. |

Возвращает `List`: Список перенесённых строк [RichText](<rich-text.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`lineHeight(RichText)` {: #lineheightrichtext data-toc-label="lineHeight(RichText)" }

**lineHeight([RichText](<rich-text.md>))**

Вычисляет высоту одной строки rich-текста по фрагментам и inline-элементам.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `line` | `RichText` | Строка текста. |

Возвращает `float`: Высота строки в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`lineHeight(RenderContext, RichText)` {: #lineheightrendercontext-richtext data-toc-label="lineHeight(RenderContext, RichText)" }

**lineHeight(RenderContext, [RichText](<rich-text.md>))**

Вычисляет высоту строки через метрики шрифта активного бэкенда.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `context` | `RenderContext` | Активный контекст рендера. |
| `line` | `RichText` | Строка текста. |

Возвращает `float`: Высота строки в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`linesHeight(List)` {: #linesheightlist data-toc-label="linesHeight(List)" }

**linesHeight(List)**

Вычисляет суммарную высоту списка строк.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `lines` | `List` | Список строк [RichText](<rich-text.md>). |

Возвращает `float`: Суммарная высота в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`linesHeight(RenderContext, List)` {: #linesheightrendercontext-list data-toc-label="linesHeight(RenderContext, List)" }

**linesHeight(RenderContext, List)**

Вычисляет суммарную высоту списка строк через активный бэкенд.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `context` | `RenderContext` | Активный контекст рендера. |
| `lines` | `List` | Список строк [RichText](<rich-text.md>). |

Возвращает `float`: Суммарная высота в пикселях..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`draw(RenderContext, String, float, float, float, float, Paint, Transform, Alignment, Alignment)` {: #drawrendercontext-string-float-float-float-float-paint-transform-alignment-alignment data-toc-label="draw(RenderContext, String, float, float, float, float, Paint, Transform, Alignment, Alignment)" }

**draw(RenderContext, String, float, float, float, float, [Paint](<../render/paint.md>), Transform, Alignment, Alignment)**

Отрисовывает обычный текст с выравниванием внутри заданных границ.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `context` | `RenderContext` | Активный контекст рендера. |
| `text` | `String` | Текстовая строка. |
| `x` | `float` | Координата X. |
| `y` | `float` | Координата Y. |
| `width` | `float` | Ширина контейнера. |
| `height` | `float` | Высота контейнера. |
| `paint` | `Paint` | Стиль заливки. |
| `transform` | `Transform` | Опциональная трансформация. |
| `horizontalAlignment` | `Alignment` | Горизонтальное выравнивание. |
| `verticalAlignment` | `Alignment` | Вертикальное выравнивание. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`draw(RenderContext, RichText, float, float, float, float, Paint, Transform, Alignment, Alignment)` {: #drawrendercontext-richtext-float-float-float-float-paint-transform-alignment-alignment data-toc-label="draw(RenderContext, RichText, float, float, float, float, Paint, Transform, Alignment, Alignment)" }

**draw(RenderContext, [RichText](<rich-text.md>), float, float, float, float, [Paint](<../render/paint.md>), Transform, Alignment, Alignment)**

Отрисовывает rich-текст с выравниванием и поддержкой inline-содержимого.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `context` | `RenderContext` | Активный контекст рендера. |
| `text` | `RichText` | Экземпляр [RichText](<rich-text.md>). |
| `x` | `float` | Координата X. |
| `y` | `float` | Координата Y. |
| `width` | `float` | Ширина контейнера. |
| `height` | `float` | Высота контейнера. |
| `paint` | `Paint` | Стиль заливки. |
| `transform` | `Transform` | Опциональная трансформация. |
| `horizontalAlignment` | `Alignment` | Горизонтальное выравнивание. |
| `verticalAlignment` | `Alignment` | Вертикальное выравнивание. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`drawInline(DrawScope, RichText, float, float, float, float, Paint)` {: #drawinlinedrawscope-richtext-float-float-float-float-paint data-toc-label="drawInline(DrawScope, RichText, float, float, float, float, Paint)" }

**drawInline([DrawScope](<../render/draw-scope.md>), [RichText](<rich-text.md>), float, float, float, float, [Paint](<../render/paint.md>))**

Выполняет раскладку и отрисовку rich-текста с inline-контентом в [DrawScope](<../render/draw-scope.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `draw` | `DrawScope` | Целевой [DrawScope](<../render/draw-scope.md>). |
| `text` | `RichText` | Экземпляр [RichText](<rich-text.md>). |
| `x` | `float` | Координата X. |
| `y` | `float` | Координата Y. |
| `width` | `float` | Ширина контейнера. |
| `height` | `float` | Высота контейнера. |
| `paint` | `Paint` | Стиль заливки. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`drawInline(DrawScope, RichText, float, float, float, float, Paint, float)` {: #drawinlinedrawscope-richtext-float-float-float-float-paint-float data-toc-label="drawInline(DrawScope, RichText, float, float, float, float, Paint, float)" }

**drawInline([DrawScope](<../render/draw-scope.md>), [RichText](<rich-text.md>), float, float, float, float, [Paint](<../render/paint.md>), float)**

Выполняет раскладку и отрисовку rich-текста с вертикальным смещением.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `draw` | `DrawScope` | Целевой [DrawScope](<../render/draw-scope.md>). |
| `text` | `RichText` | Экземпляр [RichText](<rich-text.md>). |
| `x` | `float` | Координата X. |
| `y` | `float` | Координата Y. |
| `width` | `float` | Ширина контейнера. |
| `height` | `float` | Высота контейнера. |
| `paint` | `Paint` | Стиль заливки. |
| `textVisualOffset` | `float` | Визуальное вертикальное смещение. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`usesPositionalBrush(RichText)` {: #usespositionalbrushrichtext data-toc-label="usesPositionalBrush(RichText)" }

**usesPositionalBrush([RichText](<rich-text.md>))**

Проверяет, использует ли какой-либо фрагмент кисть с привязкой к координатам.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `RichText` | Экземпляр [RichText](<rich-text.md>). |

Возвращает `boolean`: true, если есть позиционная кисть..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextEngine::</span>`alignedStart(float, float, float, Alignment)` {: #alignedstartfloat-float-float-alignment data-toc-label="alignedStart(float, float, float, Alignment)" }

**alignedStart(float, float, float, Alignment)**

Вычисляет начальную координату элемента с учётом доступного места и выравнивания.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `start` | `float` | Базовая координата смещения. |
| `available` | `float` | Доступное пространство. |
| `size` | `float` | Размер элемента. |
| `alignment` | `Alignment` | Режим выравнивания. |

Возвращает `float`: Выровненная начальная позиция..

</div>

