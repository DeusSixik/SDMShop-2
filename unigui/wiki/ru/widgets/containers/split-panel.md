---
title: "SplitPanel"
slug: "split-panel"
category: "Widgets/Containers"
locale: "ru"
order: 130
---

# SplitPanel

Двухсекционный контейнер, разделённый интерактивным перемещаемым разделителем.

## Обзор

SplitPanel управляет двумя слотами для дочерних виджетов: `first` и `second`. Между ними расположен внутренний интерактивный разделитель `Splitter`, позволяющий пользователю изменять пропорции секций перетаскиванием мыши.

## Ориентация и ограничения

Панель поддерживает как горизонтальную (`Orientation.HORIZONTAL` — первый слот слева, второй справа), так и вертикальную (`Orientation.VERTICAL` — первый слот сверху, второй снизу) ориентацию. Пропорция задаётся через `splitRatio` (от 0.0 до 1.0) и ограничивается `minFirstSize` и `minSecondSize`.

## Горизонтальный сплит

```java
SplitPanel split = new SplitPanel(sidebar, editor)
        .orientation(Orientation.HORIZONTAL)
        .splitRatio(0.28f)
        .minFirstSize(120.0f);
```

## Связанные ссылки

- [DockPanel](<dock-panel.md>)
- [HBox](<hbox.md>)
- [PanelWidget](<panel-widget.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SplitPanel::</span>`first(Widget)` {: #firstwidget data-toc-label="first(Widget)" }

**Задать первую панель**

Устанавливает виджет в первый слот (слева или сверху).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `first` | `Widget` | виджет для первого слота |

Возвращает `SplitPanel`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SplitPanel::</span>`second(Widget)` {: #secondwidget data-toc-label="second(Widget)" }

**Задать вторую панель**

Устанавливает виджет во второй слот (справа или снизу).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `second` | `Widget` | виджет для второго слота |

Возвращает `SplitPanel`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SplitPanel::</span>`orientation(Orientation)` {: #orientationorientation data-toc-label="orientation(Orientation)" }

**Ориентация разделения**

Задаёт ориентацию оси разделения (горизонтальная или вертикальная).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `orientation` | `Orientation` | желаемая ориентация |

Возвращает `SplitPanel`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SplitPanel::</span>`splitRatio(float)` {: #splitratiofloat data-toc-label="splitRatio(float)" }

**Пропорция разделения**

Задаёт долю доступного пространства для первого слота (в диапазоне от 0.0 до 1.0).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `splitRatio` | `float` | пропорция сплита от 0.0 до 1.0 |

Возвращает `SplitPanel`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SplitPanel::</span>`minFirstSize(float)` {: #minfirstsizefloat data-toc-label="minFirstSize(float)" }

**Минимальный размер первой панели**

Задаёт минимальный размер первого слота в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `minFirstSize` | `float` | минимальный размер в пикселях |

Возвращает `SplitPanel`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SplitPanel::</span>`minSecondSize(float)` {: #minsecondsizefloat data-toc-label="minSecondSize(float)" }

**Минимальный размер второй панели**

Задаёт минимальный размер второго слота в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `minSecondSize` | `float` | минимальный размер в пикселях |

Возвращает `SplitPanel`: этот виджет для fluent-настройки.

</div>

