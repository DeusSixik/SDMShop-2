---
title: "RibbonOptions"
slug: "ribbon-options"
category: "Core/Render"
locale: "ru"
order: 45
---

# RibbonOptions

Неизменяемый объект настроек для генерации полигональной ленты, угловых стыков, круглых заглушек и UV-трансформаций текстуры.

## Обзор

`RibbonOptions` объединяет параметры генерации ленты (замыкание контура, лимит miter, транспонирование UV, зеркалирование осей и круглые стыки) в неизменяемую структуру, предотвращая перегрузку методов [DrawScope](<draw-scope.md>) и RenderContext множеством аргументов.

## Неизменяемость и методы модификации

Каждый метод-модификатор возвращает новый экземпляр `RibbonOptions` либо возвращает текущий, если значение не изменилось. Базовый экземпляр доступен через `RibbonOptions.defaults()`.

## Битовое поле и кэширование

Все флаги, квантованный лимит miter и количество сегментов стыка упакованы в 32-битное целое число через `packed()`. Это позволяет использовать options в качестве легковесного ключа кэша без лишних аллокаций.

## Использование параметров по умолчанию

```java
RibbonOptions basic = RibbonOptions.defaults();
scope.ribbon(texture, points, 10.0f, 32.0f, ColorView.WHITE, basic);
```

## Настройка вертикального кабеля с круглыми стыками

```java
RibbonOptions cableOptions = RibbonOptions.defaults()
    .swapUv(true)
    .jointDots(true)
    .jointSegments(12)
    .miterLimit(3.0f);
scope.ribbon(cableTexture, wirePoints, 6.0f, 24.0f, ColorView.rgb(0, 220, 255), cableOptions);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`defaults()` {: #defaults data-toc-label="defaults()" }

**defaults()**

Возвращает дефолтные параметры ленты: разомкнутый контур, miter-лимит 4.0, без стыков и трансформаций UV.

Возвращает `RibbonOptions`: Общий shared-экземпляр RibbonOptions..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`closed()` {: #closed data-toc-label="closed()" }

**closed()**

Проверяет, замкнут ли контур ленты.

Возвращает `boolean`: true, если контур замкнут и соединяет конец с началом..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`miterLimit()` {: #miterlimit data-toc-label="miterLimit()" }

**miterLimit()**

Возвращает максимальную длину miter в половинах ширины ленты.

Возвращает `float`: Лимит miter, не менее 1.0..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`swapUv()` {: #swapuv data-toc-label="swapUv()" }

**swapUv()**

Проверяет, меняются ли U и V местами.

Возвращает `boolean`: true, если U идёт вдоль пути, а V поперёк ленты..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`flipU()` {: #flipu data-toc-label="flipU()" }

**flipU()**

Проверяет, отражается ли горизонтальная координата U.

Возвращает `boolean`: true, если U отражается (1 - u)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`flipV()` {: #flipv data-toc-label="flipV()" }

**flipV()**

Проверяет, отражается ли вертикальная координата V.

Возвращает `boolean`: true, если V отражается (1 - v)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`jointDots()` {: #jointdots data-toc-label="jointDots()" }

**jointDots()**

Проверяет, включены ли круглые диски на стыках.

Возвращает `boolean`: true, если круглые стыки включены..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`jointSegments()` {: #jointsegments data-toc-label="jointSegments()" }

**jointSegments()**

Возвращает число сегментов диска стыка.

Возвращает `int`: Число сегментов или 0 для автоматического расчёта по радиусу..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`packed()` {: #packed data-toc-label="packed()" }

**packed()**

Возвращает компактный 32-битный ключ со всеми флагами, miter-лимитом и сегментами стыка.

Возвращает `int`: Стабильный 32-битный ключ кэша..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`closed(boolean)` {: #closedboolean data-toc-label="closed(boolean)" }

**closed(boolean)**

Возвращает копию с новым флагом замыкания контура.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `closed` | `boolean` | true для замыкания последней точки на первую. |

Возвращает `RibbonOptions`: Новый экземпляр RibbonOptions или текущий, если значение не изменилось..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`miterLimit(float)` {: #miterlimitfloat data-toc-label="miterLimit(float)" }

**miterLimit(float)**

Возвращает копию с новым лимитом miter.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `miterLimit` | `float` | Лимит в половинах ширины; значения меньше 1.0 нормализуются к 1.0. |

Возвращает `RibbonOptions`: Новый экземпляр RibbonOptions или текущий, если значение не изменилось..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`swapUv(boolean)` {: #swapuvboolean data-toc-label="swapUv(boolean)" }

**swapUv(boolean)**

Возвращает копию с новым флагом поворота текстуры.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `swapUv` | `boolean` | true для транспонирования U и V (для вертикальных текстур). |

Возвращает `RibbonOptions`: Новый экземпляр RibbonOptions или текущий, если значение не изменилось..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`flipU(boolean)` {: #flipuboolean data-toc-label="flipU(boolean)" }

**flipU(boolean)**

Возвращает копию с новым флагом отражения U.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `flipU` | `boolean` | true для инвертирования координаты U. |

Возвращает `RibbonOptions`: Новый экземпляр RibbonOptions или текущий, если значение не изменилось..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`flipV(boolean)` {: #flipvboolean data-toc-label="flipV(boolean)" }

**flipV(boolean)**

Возвращает копию с новым флагом отражения V.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `flipV` | `boolean` | true для инвертирования координаты V. |

Возвращает `RibbonOptions`: Новый экземпляр RibbonOptions или текущий, если значение не изменилось..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`jointDots(boolean)` {: #jointdotsboolean data-toc-label="jointDots(boolean)" }

**jointDots(boolean)**

Возвращает копию с новым флагом круглых стыков.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `jointDots` | `boolean` | true для закрытия внутренних углов дисками. |

Возвращает `RibbonOptions`: Новый экземпляр RibbonOptions или текущий, если значение не изменилось..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`jointSegments(int)` {: #jointsegmentsint data-toc-label="jointSegments(int)" }

**jointSegments(int)**

Возвращает копию с новым числом сегментов стыка.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `jointSegments` | `int` | Число сегментов; 0 включает автоматический подбор по радиусу. |

Возвращает `RibbonOptions`: Новый экземпляр RibbonOptions или текущий, если значение не изменилось..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RibbonOptions::</span>`isDefault()` {: #isdefault data-toc-label="isDefault()" }

**isDefault()**

Проверяет, совпадают ли параметры с настройками по умолчанию.

Возвращает `boolean`: true, если все параметры равны дефолтным..

</div>

