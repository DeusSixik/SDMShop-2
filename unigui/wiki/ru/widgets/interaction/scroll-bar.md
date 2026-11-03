---
title: "ScrollBar"
slug: "scroll-bar"
category: "Widgets/Interaction"
locale: "ru"
order: 190
---

# ScrollBar

Интерактивная полоса прокрутки для прокрутки содержимого по вертикали или горизонтали.

## Обзор

`ScrollBar` расширяет `Box` и отображает интерактивную полосу с анимированным ползунком. Поддерживает плавное расширение при наведении, шаг колеса мыши, постраничную прокрутку и события изменения значения.

## Вертикальный ScrollBar

```java
ScrollBar scroll = new ScrollBar()
        .orientation(Orientation.VERTICAL)
        .range(0.0f, 500.0f)
        .pageSize(50.0f)
        .value(0.0f);
scroll.onValueChanged(e -> list.scrollOffset(e.newValue()));
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`dragging()` {: #dragging data-toc-label="dragging()" }

**Состояние перетаскивания**

Возвращает true, если ползунок прокрутки перетаскивается пользователем.

Возвращает `boolean`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`hoverThickness()` {: #hoverthickness data-toc-label="hoverThickness()" }

**Толщина при наведении**

Возвращает толщину полосы прокрутки при наведении курсора.

Возвращает `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`hoverThickness(float)` {: #hoverthicknessfloat data-toc-label="hoverThickness(float)" }

**Задать толщину при наведении**

Устанавливает толщину полосы прокрутки при наведении курсора.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `hoverThickness` | `float` | — |

Возвращает `ScrollBar`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`idleThickness()` {: #idlethickness data-toc-label="idleThickness()" }

**Толщина в покое**

Возвращает толщину полосы прокрутки в состоянии покоя.

Возвращает `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`idleThickness(float)` {: #idlethicknessfloat data-toc-label="idleThickness(float)" }

**Задать толщину в покое**

Устанавливает толщину полосы прокрутки в состоянии покоя.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `idleThickness` | `float` | — |

Возвращает `ScrollBar`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`max()` {: #max data-toc-label="max()" }

**Получить максимум**

Возвращает максимальное значение прокрутки.

Возвращает `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`max(float)` {: #maxfloat data-toc-label="max(float)" }

**Задать максимум**

Устанавливает верхнюю границу прокрутки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `max` | `float` | — |

Возвращает `ScrollBar`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`min()` {: #min data-toc-label="min()" }

**Получить минимум**

Возвращает минимальное значение прокрутки.

Возвращает `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`min(float)` {: #minfloat data-toc-label="min(float)" }

**Задать минимум**

Устанавливает нижнюю границу прокрутки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `min` | `float` | — |

Возвращает `ScrollBar`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`onValueChanged(EventListener)` {: #onvaluechangedeventlistener data-toc-label="onValueChanged(EventListener)" }

**Слушатель изменения позиции**

Регистрирует слушатель изменения значения полосы прокрутки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | — |

Возвращает `EventSubscription`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`orientation()` {: #orientation data-toc-label="orientation()" }

**Получить ориентацию**

Возвращает ориентацию оси полосы прокрутки.

Возвращает `Orientation`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`orientation(Orientation)` {: #orientationorientation data-toc-label="orientation(Orientation)" }

**Задать ориентацию**

Устанавливает ориентацию полосы прокрутки (HORIZONTAL или VERTICAL).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `orientation` | `Orientation` | — |

Возвращает `ScrollBar`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`pageSize()` {: #pagesize data-toc-label="pageSize()" }

**Получить размер страницы**

Возвращает размер страницы окна просмотра для вычисления длины ползунка.

Возвращает `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`pageSize(float)` {: #pagesizefloat data-toc-label="pageSize(float)" }

**Задать размер страницы**

Устанавливает размер страницы окна просмотра.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `pageSize` | `float` | — |

Возвращает `ScrollBar`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`range(float, float)` {: #rangefloat-float data-toc-label="range(float, float)" }

**Задать диапазон**

Устанавливает границы диапазона прокрутки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `min` | `float` | — |
| `max` | `float` | — |

Возвращает `ScrollBar`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`silentValue(float)` {: #silentvaluefloat data-toc-label="silentValue(float)" }

**Задать значение без событий**

Устанавливает значение прокрутки без генерации событий изменения.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `value` | `float` | — |

Возвращает `ScrollBar`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`step()` {: #step data-toc-label="step()" }

**Получить шаг**

Возвращает шаг дискретной прокрутки.

Возвращает `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`step(float)` {: #stepfloat data-toc-label="step(float)" }

**Задать шаг**

Устанавливает шаг дискретной прокрутки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `step` | `float` | — |

Возвращает `ScrollBar`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`thumbColor()` {: #thumbcolor data-toc-label="thumbColor()" }

**Цвет ползунка**

Возвращает цвет ползунка в состоянии покоя.

Возвращает `MutableColor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`thumbHoverColor()` {: #thumbhovercolor data-toc-label="thumbHoverColor()" }

**Цвет ползунка при наведении**

Возвращает цвет ползунка при наведении курсора.

Возвращает `MutableColor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`trackColor()` {: #trackcolor data-toc-label="trackColor()" }

**Цвет дорожки**

Возвращает цвет фоновой дорожки прокрутки.

Возвращает `MutableColor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`value()` {: #value data-toc-label="value()" }

**Получить значение**

Возвращает текущую позицию прокрутки.

Возвращает `float`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ScrollBar::</span>`value(float)` {: #valuefloat data-toc-label="value(float)" }

**Задать значение**

Устанавливает позицию прокрутки с ограничением по диапазону.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `value` | `float` | — |

Возвращает `ScrollBar`.

</div>

