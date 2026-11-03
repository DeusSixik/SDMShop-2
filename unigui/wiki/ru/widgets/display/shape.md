---
title: "Shape"
slug: "shape"
category: "Widgets/Display"
locale: "ru"
order: 70
---

# Shape

Виджет векторных геометрических фигур: прямоугольников, скруглений, кругов, овалов и контуров.

## Обзор

`Shape` отображает векторные 2D-примитивы с аппаратным сглаживанием. Поддерживает заливку и обводку прямоугольников, скругленных фигур, эллипсов и кругов с регулируемой толщиной границы.

## Круглая фигура

```java
Shape badge = new Shape()
        .type(Shape.Type.CIRCLE)
        .color(Color.GREEN)
        .stroke(false);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Shape::</span>`color(ColorView)` {: #colorcolorview data-toc-label="color(ColorView)" }

**Цвет фигуры**

Возвращает или устанавливает цвет заливки или контура фигуры.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `color` | `ColorView` | — |

Возвращает `Shape`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Shape::</span>`stroke(boolean)` {: #strokeboolean data-toc-label="stroke(boolean)" }

**Режим обводки**

Управляет отрисовкой фигуры в виде контура (stroke) или сплошной заливки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `stroke` | `boolean` | — |

Возвращает `Shape`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Shape::</span>`strokeWidth(float)` {: #strokewidthfloat data-toc-label="strokeWidth(float)" }

**Толщина контура**

Возвращает или устанавливает толщину линии контура фигуры.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `strokeWidth` | `float` | — |

Возвращает `Shape`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Shape::</span>`type(Type)` {: #typetype data-toc-label="type(Type)" }

**Тип фигуры**

Возвращает или устанавливает геометрический тип фигуры.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `type` | `Type` | — |

Возвращает `Shape`.

</div>

