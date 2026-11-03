---
title: "Paint"
slug: "paint"
category: "Core/Render"
locale: "ru"
order: 30
---

# Paint

Дескриптор стиля команд отрисовки, настраивающий цвет, толщину обводки, режимы заливки/контура, Porter-Duff смешивание и пунктирные линии.

## Обзор и архитектура

`Paint` описывает визуальный стиль векторных фигур, геометрических контуров, текстур и теней в UniGUI.

### Основные возможности

- **Цвет**: Инкапсулирует компонент цвета RGBA (`ColorView`) с возможностью цепочечной настройки.
- **Тип отрисовки**: Переключает между сплошной заливкой (`stroke = false`) и контурной обводкой (`stroke = true`) заданной толщины.
- **Смешивание цветов**: Управляет аппаратным блендингом через `BlendMode` (NORMAL, ADDITIVE, MULTIPLY, SCREEN).
- **Пунктир**: Настраивает пунктирные штрихи (`dash(length, gap)`) и фазу смещения паттерна (`dashOffset`).

## Семантика копирования при записи

Класс `Paint` спроектирован как **изменяемый (mutable) объект** для удобной цепочечной настройки параметров в fluent-стиле.

При этом архитектура буферизованных команд UniGUI гарантирует полную изоляцию:

> [!NOTE]
> Когда `DrawScope` записывает `DrawCommand`, он внутренне копирует текущее состояние `Paint`. Любые последующие изменения вашего объекта `Paint` **никогда** не повлияют на команды, уже добавленные в буфер текущего кадра.

## Режимы заливки и обводки

Для быстрого создания используйте статические фабричные методы:

```java
// Сплошная заливка фигуры:
Paint fillPaint = Paint.fill(ColorView.rgb(255, 100, 0));

// Контурная обводка (толщиной 2.0 UI-пикселя):
Paint borderPaint = Paint.stroke(ColorView.WHITE, 2.0f);
```

В высокочастотных циклах рендеринга, где важна экономия памяти, рекомендуется использовать методы `DrawScope#rectFilled(...)`, `DrawScope#rectStroke(...)`, принимающие `ColorView` напрямую **без создания объектов**.

## Режимы смешивания (Blend Modes)

Настроить аппаратное смешивание цветов можно с помощью `paint.blend(BlendMode)`:

| Режим смешивания | Основное применение | Визуальный эффект |
| --- | --- | --- |
| `BlendMode.NORMAL` | Стандартные элементы UI, полупрозрачные панели | Обычное наложение цвета с учётом альфа-канала |
| `BlendMode.ADDITIVE` | Магическое свечение, искры, неоновые лазеры | Цвет источника прибавляется к фону, создавая яркий свет |
| `BlendMode.MULTIPLY` | Мягкие реалистичные тени, затенение окружения | Цвета перемножаются, затемняя нижележащий фон |
| `BlendMode.SCREEN` | Мягкие блики, голограммы | Инвертированное умножение для плавного высветления |

## Пунктир и бегущие рамки

Для обводки контуров поддерживается настройка пунктира в UI-пикселях:

```java
Paint dashPaint = Paint.stroke(ColorView.rgb(0, 180, 255), 1.5f)
        .dash(6.0f, 4.0f) // 6px штрих, 4px промежуток
        .dashOffset((System.currentTimeMillis() / 20.0f) % 10.0f); // анимированная бегущая рамка
```

Метод `clearDash()` возвращает непрерывную сплошную линию.

## Анимированная рамка выделения

```java
// Создаём анимированную пунктирную рамку
float animPhase = (System.currentTimeMillis() % 1000) / 100.0f;
Paint selectionPaint = Paint.stroke(ColorView.rgb(80, 160, 255), 2.0f)
        .dash(5.0f, 5.0f)
        .dashOffset(animPhase);

RectView bounds = widget.layoutBounds();
scope.roundedRect(bounds.x(), bounds.y(), bounds.width(), bounds.height(), 4.0f, selectionPaint);
```

## Светящаяся сфера с аддитивным блендингом

```java
// Аддитивное свечение вокруг иконки
Paint glowPaint = Paint.fill(ColorView.rgba(0, 220, 255, 90))
        .blend(BlendMode.ADDITIVE);

scope.circle(centerX - 16, centerY - 16, 32, 32, glowPaint);
```

## API

### Фабричные методы и конструктор

| Метод | Описание |
| --- | --- |
| [`Paint()`](#paint) | Создаёт новый Paint с белой заливкой, стандартным режимом смешивания и без обводки. |
| [`fill(ColorView)`](#fillcolorview) | Создаёт Paint для сплошной заливки фигур указанным цветом. |
| [`stroke(ColorView, float)`](#strokecolorview-float) | Создаёт Paint для контурной обводки с заданным цветом и толщиной. |

<div class="api-member-card" markdown="1">

#### `Paint()` {: #paint data-toc-label="Paint()" }

**Создать Paint по умолчанию**

Создаёт новый Paint с белой заливкой, стандартным режимом смешивания и без обводки.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`fill(ColorView)` {: #fillcolorview data-toc-label="fill(ColorView)" }

**Создать Paint для заливки**

Создаёт Paint для сплошной заливки фигур указанным цветом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `color` | `ColorView` | Цвет для геометрии или тонирования. |

Возвращает `Paint`: Новый экземпляр Paint в режиме заливки..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`stroke(ColorView, float)` {: #strokecolorview-float data-toc-label="stroke(ColorView, float)" }

**Создать Paint для обводки**

Создаёт Paint для контурной обводки с заданным цветом и толщиной.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `color` | `ColorView` | Цвет для геометрии или тонирования. |
| `width` | `float` | Толщина линии контура в UI-пикселях. |

Возвращает `Paint`: Новый экземпляр Paint в режиме обводки..

</div>

### Цвет и режим заливки

| Метод | Описание |
| --- | --- |
| [`color()`](#color) | Возвращает изменяемый объект цвета этого Paint. |
| [`color(ColorView)`](#colorcolorview) | Обновляет цвет Paint из переданного ColorView. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`color()` {: #color data-toc-label="color()" }

**Получить цвет**

Возвращает изменяемый объект цвета этого Paint.

Возвращает `MutableColor`: Ссылка на внутренний объект MutableColor..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`color(ColorView)` {: #colorcolorview data-toc-label="color(ColorView)" }

**Задать цвет**

Обновляет цвет Paint из переданного ColorView.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `color` | `ColorView` | Цвет для геометрии или тонирования. |

Возвращает `Paint`: Текущий экземпляр Paint для цепочечного вызова..

</div>

### Обводка и толщина линий

| Метод | Описание |
| --- | --- |
| [`strokeWidth()`](#strokewidth) | Возвращает толщину обводки в UI-пикселях. |
| [`strokeWidth(float)`](#strokewidthfloat) | Устанавливает толщину обводки в UI-пикселях. |
| [`isStroke()`](#isstroke) | Возвращает true, если Paint настроен на отрисовку контуров, а не сплошной заливки. |
| [`stroke(boolean)`](#strokeboolean) | Переключает между контурной обводкой и сплошной заливкой. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`strokeWidth()` {: #strokewidth data-toc-label="strokeWidth()" }

**Получить толщину обводки**

Возвращает толщину обводки в UI-пикселях.

Возвращает `float`: Толщина обводки в UI-пикселях..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`strokeWidth(float)` {: #strokewidthfloat data-toc-label="strokeWidth(float)" }

**Задать толщину обводки**

Устанавливает толщину обводки в UI-пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `strokeWidth` | `float` | Толщина линии контура в UI-пикселях. |

Возвращает `Paint`: Текущий экземпляр Paint для цепочечного вызова..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`isStroke()` {: #isstroke data-toc-label="isStroke()" }

**Проверить режим обводки**

Возвращает true, если Paint настроен на отрисовку контуров, а не сплошной заливки.

Возвращает `boolean`: True для режима обводки, false для режима заливки..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`stroke(boolean)` {: #strokeboolean data-toc-label="stroke(boolean)" }

**Переключить режим обводки**

Переключает между контурной обводкой и сплошной заливкой.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `stroke` | `boolean` | True для режима обводки, false для режима заливки. |

Возвращает `Paint`: Текущий экземпляр Paint для цепочечного вызова..

</div>

### Смешивание и режимы прозрачности

| Метод | Описание |
| --- | --- |
| [`blendMode()`](#blendmode) | Возвращает текущий режим аппаратного смешивания цветов. |
| [`blend(BlendMode)`](#blendblendmode) | Устанавливает режим аппаратного смешивания цветов. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`blendMode()` {: #blendmode data-toc-label="blendMode()" }

**Получить режим смешивания**

Возвращает текущий режим аппаратного смешивания цветов.

Возвращает `BlendMode`: Установленный режим BlendMode..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`blend(BlendMode)` {: #blendblendmode data-toc-label="blend(BlendMode)" }

**Задать режим смешивания**

Устанавливает режим аппаратного смешивания цветов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `blendMode` | `BlendMode` | Режим аппаратного смешивания цветов или null для стандартного NORMAL. |

Возвращает `Paint`: Текущий экземпляр Paint для цепочечного вызова..

</div>

### Пунктирные линии

| Метод | Описание |
| --- | --- |
| [`dashed()`](#dashed) | Возвращает true, если обводка будет рисоваться пунктиром. |
| [`dashLength()`](#dashlength) | Возвращает длину видимого сегмента пунктира в пикселях. |
| [`dashGap()`](#dashgap) | Возвращает длину пустого промежутка между штрихами. |
| [`dashOffset()`](#dashoffset) | Возвращает фазовое смещение паттерна пунктира. |
| [`dash(float, float)`](#dashfloat-float) | Задаёт длину видимого штриха и пустого промежутка для обводки. |
| [`dashOffset(float)`](#dashoffsetfloat) | Устанавливает смещение фазы пунктира для анимации бегущих линий. |
| [`clearDash()`](#cleardash) | Удаляет пунктирный паттерн, возвращая сплошную линию. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`dashed()` {: #dashed data-toc-label="dashed()" }

**Проверить пунктирный режим**

Возвращает true, если обводка будет рисоваться пунктиром.

Возвращает `boolean`: True, если длина штриха и промежутка больше нуля..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`dashLength()` {: #dashlength data-toc-label="dashLength()" }

**Получить длину штриха**

Возвращает длину видимого сегмента пунктира в пикселях.

Возвращает `float`: Длина штриха в UI-пикселях..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`dashGap()` {: #dashgap data-toc-label="dashGap()" }

**Получить длину промежутка**

Возвращает длину пустого промежутка между штрихами.

Возвращает `float`: Длина промежутка в UI-пикселях..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`dashOffset()` {: #dashoffset data-toc-label="dashOffset()" }

**Получить смещение пунктира**

Возвращает фазовое смещение паттерна пунктира.

Возвращает `float`: Смещение фазы пунктира в UI-пикселях..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`dash(float, float)` {: #dashfloat-float data-toc-label="dash(float, float)" }

**Настроить пунктирный паттерн**

Задаёт длину видимого штриха и пустого промежутка для обводки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `length` | `float` | Длина видимого штриха пунктира в UI-пикселях. |
| `gap` | `float` | Длина пустого промежутка между штрихами в UI-пикселях. |

Возвращает `Paint`: Текущий экземпляр Paint для цепочечного вызова..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`dashOffset(float)` {: #dashoffsetfloat data-toc-label="dashOffset(float)" }

**Задать смещение пунктира**

Устанавливает смещение фазы пунктира для анимации бегущих линий.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `dashOffset` | `float` | Фазовое смещение паттерна пунктира в UI-пикселях. |

Возвращает `Paint`: Текущий экземпляр Paint для цепочечного вызова..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`clearDash()` {: #cleardash data-toc-label="clearDash()" }

**Сбросить пунктир**

Удаляет пунктирный паттерн, возвращая сплошную линию.

Возвращает `Paint`: Текущий экземпляр Paint для цепочечного вызова..

</div>

### Копирование и управление состоянием

| Метод | Описание |
| --- | --- |
| [`copy()`](#copy) | Создаёт независимую точную копию этого Paint со всеми параметрами. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">Paint::</span>`copy()` {: #copy data-toc-label="copy()" }

**Клонировать Paint**

Создаёт независимую точную копию этого Paint со всеми параметрами.

Возвращает `Paint`: Независимая копия Paint..

</div>

