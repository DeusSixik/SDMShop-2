---
title: "LinearBox"
slug: "linear-box"
category: "Widgets/Containers"
locale: "ru"
order: 40
---

# LinearBox

Одномерный flex-контейнер, последовательно размещающий детей вдоль главной оси без переноса строк.

## Обзор

`LinearBox` — одномерный flex-контейнер, автоматически синхронизирующий свои свойства `orientation` и `spacing` с `LayoutStyle`. Горизонтальная ориентация использует `FlexDirection.ROW` и `columnGap`, а вертикальная — `FlexDirection.COLUMN` и `rowGap`.

Для фиксированных направлений обычно удобнее использовать готовые `VBox` или `HBox`. Сам `LinearBox` применяется, когда направление раскладки меняется динамически.

## Опциональная поверхность

`LinearBox` наследует `SurfaceWidget`, поддерживая фон, рамку и текстуры. По умолчанию отрисовка поверхности выключена (`boxVisualEnabled=false`), сохраняя контейнер прозрачным. Первый же вызов `background(color)` или `border(color)` автоматически включает отрисовку поверхности.

## Динамический контейнер

```java
LinearBox box = new LinearBox(Orientation.HORIZONTAL)
        .spacing(8.0f)
        .background(ColorView.ofHex("#2A2D32"));
box.addChild(btn1);
box.addChild(btn2);
```

## Связанные ссылки

- [FlexBox](<flex-box.md>)
- [HBox](<hbox.md>)
- [VBox](<vbox.md>)

## API

<div class="api-member-card" markdown="1">

### `LinearBox(Orientation)` {: #linearboxorientation data-toc-label="LinearBox(Orientation)" }

**Конструктор**

Создаёт LinearBox с указанной ориентацией раскладки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `orientation` | `Orientation` | целевая ориентация |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LinearBox::</span>`orientation(Orientation)` {: #orientationorientation data-toc-label="orientation(Orientation)" }

**Ориентация**

Задаёт ось раскладки (HORIZONTAL или VERTICAL).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `orientation` | `Orientation` | целевая ориентация |

Возвращает `LinearBox`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">LinearBox::</span>`spacing(float)` {: #spacingfloat data-toc-label="spacing(float)" }

**Интервал между элементами**

Задаёт равномерный отступ между соседними элементами вдоль главной оси.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `spacing` | `float` | отступ в пикселях UI |

Возвращает `LinearBox`: этот виджет для fluent-настройки.

</div>

