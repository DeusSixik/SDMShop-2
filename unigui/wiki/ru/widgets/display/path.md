---
title: "Path"
slug: "path"
category: "Widgets/Display"
locale: "ru"
order: 80
---

# Path

Виджет векторного пути для произвольных 2D-кривых Безье, ломаных линий и контуров.

## Обзор

`Path` отображает произвольную векторную геометрию, заданную объектом `VectorPath`. Поддерживает квадратичные и кубические кривые Безье, отрезки линий, закрашенные многоугольники и обводку.

## Векторный контур

```java
VectorPath star = VectorPath.builder()
        .moveTo(10, 0).lineTo(13, 7).lineTo(20, 7).lineTo(15, 12).build();
Path starWidget = new Path()
        .path(star)
        .color(Color.GOLD);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Path::</span>`stroke(boolean)` {: #strokeboolean data-toc-label="stroke(boolean)" }

**Режим обводки**

Определяет, отображается ли путь в режиме обводки или заливки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `stroke` | `boolean` | — |

Возвращает `Path`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Path::</span>`strokeWidth(float)` {: #strokewidthfloat data-toc-label="strokeWidth(float)" }

**Толщина обводки**

Возвращает или устанавливает толщину линии при обводке пути.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `strokeWidth` | `float` | — |

Возвращает `Path`.

</div>

