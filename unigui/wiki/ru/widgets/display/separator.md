---
title: "Separator"
slug: "separator"
category: "Widgets/Display"
locale: "ru"
order: 60
---

# Separator

Горизонтальный или вертикальный разделитель для визуального зонирования интерфейса.

## Обзор

`Separator` отображает разделительную линию по горизонтали или вертикали. Применяется для разделения пунктов меню, панелей инструментов и логических блоков интерфейса.

## Разделительная линия

```java
Separator divider = new Separator()
        .orientation(Orientation.HORIZONTAL)
        .thickness(1.0f)
        .color(Color.of(0.2f, 0.2f, 0.2f, 1.0f));
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Separator::</span>`color()` {: #color data-toc-label="color()" }

**Цвет линии**

Возвращает цвет разделительной линии.

Возвращает `MutableColor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Separator::</span>`orientation(Orientation)` {: #orientationorientation data-toc-label="orientation(Orientation)" }

**Ориентация**

Возвращает или устанавливает ориентацию (HORIZONTAL или VERTICAL).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `orientation` | `Orientation` | — |

Возвращает `Separator`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Separator::</span>`thickness(float)` {: #thicknessfloat data-toc-label="thickness(float)" }

**Толщина линии**

Возвращает или устанавливает толщину разделительной линии в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `thickness` | `float` | — |

Возвращает `Separator`.

</div>

