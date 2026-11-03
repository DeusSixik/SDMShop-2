---
title: "ResizeGripStyle"
slug: "resize-grip-style"
category: "Core/Виджеты/Докинг"
locale: "ru"
order: 0
---

# ResizeGripStyle

Интерфейс стилизации и отрисовки угловых ручек изменения размера окна, включая встроенные стили TRIANGLE и LINES.

## Обзор

`ResizeGripStyle` управляет отрисовкой угловой ручки изменения размера плавающего окна.

### Встроенные стили

| Стиль | Визуальное отображение |
| --- | --- |
| `ResizeGripStyle.TRIANGLE` | Сплошной треугольник в правом нижнем углу. |
| `ResizeGripStyle.LINES` | Диагональные штрихи ручки захвата. |

## Пользовательская отрисовка ручки изменения размера

```java
theme.resizeGrip(ResizeGripStyle.LINES);
```

## Связанные ссылки

- [DockTheme](<dock-theme.md>): Тема оформления системы докинга, настраивающая палитру цветов ([DockColor](<dock-color.md>)), геометрию вкладок, разделители, тени и ручки изменения размера окон.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ResizeGripStyle::</span>`paint(DrawScope, float, float, float, float, ColorView)` {: #paintdrawscope-float-float-float-float-colorview data-toc-label="paint(DrawScope, float, float, float, float, ColorView)" }

Отрисовывает визуальный элемент ручки изменения размера в правом нижнем углу окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `draw` | `DrawScope` | [DrawScope](<../../render/draw-scope.md>) для двухмерной отрисовки. |
| `right` | `float` | Координата X правого края. |
| `bottom` | `float` | Координата Y нижнего края. |
| `size` | `float` | Размер ручки изменения размера в пикселях. |
| `cornerRadius` | `float` | Радиус скругления угла окна. |
| `color` | `ColorView` | Цвет линий или индикатора ручки изменения размера. |

</div>

