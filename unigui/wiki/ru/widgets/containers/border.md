---
title: "Border"
slug: "border"
category: "Widgets/Containers"
locale: "ru"
order: 170
---

# Border

Render-only декоративная рамка без дочерних виджетов.

## Обзор

`Border` — легковесная декоративная рамка без дочерних элементов. Она полезна в качестве отдельного overlay-слоя — например, для подсветки активного элемента, контуров выделения или декоративного обрамления.

Если требуется контейнер с фоном, рамкой и вложенными детьми, используйте `Box`.

## Рамка выделения

```java
Border outline = new Border()
        .thickness(2.0f)
        .radius(4.0f);
outline.color().set(0.2f, 0.6f, 1.0f, 1.0f);
```

## Связанные ссылки

- [Box](<box.md>)

## API

<div class="api-member-card" markdown="1">

### `Border()` {: #border data-toc-label="Border()" }

**Конструктор**

Создаёт декоратор рамки с белой рамкой толщиной 1px по умолчанию.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Border::</span>`color()` {: #color data-toc-label="color()" }

**Цвет рамки**

Возвращает изменяемый цвет рамки.

Возвращает `MutableColor`: изменяемый цвет MutableColor.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Border::</span>`thickness(float)` {: #thicknessfloat data-toc-label="thickness(float)" }

**Толщина рамки**

Устанавливает толщину линии рамки в UI-пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `thickness` | `float` | Толщина в UI-пикселях |

Возвращает `Border`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Border::</span>`radius(float)` {: #radiusfloat data-toc-label="radius(float)" }

**Радиус скругления**

Устанавливает радиус скругления углов в UI-пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `radius` | `float` | Радиус в UI-пикселях |

Возвращает `Border`: этот виджет для fluent-настройки.

</div>

