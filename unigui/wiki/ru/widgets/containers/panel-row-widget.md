---
title: "PanelRowWidget"
slug: "panel-row-widget"
category: "Widgets/Containers"
locale: "ru"
order: 140
---

# PanelRowWidget

Двусторонняя строка, выравнивающая виджеты по левому и правому краям.

## Обзор

PanelRowWidget предназначен для заголовков, панелей инструментов, title bar и строк форм. Он содержит две группы виджетов: выровненные слева (слева направо) и выровненные справа (справа налево). Обе группы участвуют в расчёте разметки, обеспечивая чёткое разделение между основными заголовками и вспомогательными элементами управления.

## Размеры и отступы

Общая высота строки настраивается через `rowHeight(float)`. Расстояния между элементами регулируются через `gap(float)`, `leftGap(float)` и `rightGap(float)`. Ширину групп можно зафиксировать через `leftWidth(float)` и `rightWidth(float)` или оставить автоматической (`LayoutConstraints.AUTO`).

## Строка панели инструментов

```java
PanelRowWidget row = new PanelRowWidget()
        .rowHeight(24.0f)
        .gap(8.0f);
row.left(titleLabel);
row.addRight(applyButton);
row.addRight(closeButton);
```

## Связанные ссылки

- [HBox](<hbox.md>)
- [PanelWidget](<panel-widget.md>)
- [SettingRow](<setting-row.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelRowWidget::</span>`left(Widget)` {: #leftwidget data-toc-label="left(Widget)" }

**Задать левый виджет**

Заменяет всю левую группу одним виджетом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `widget` | `Widget` | виджет для левой стороны |

Возвращает `PanelRowWidget`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelRowWidget::</span>`right(Widget)` {: #rightwidget data-toc-label="right(Widget)" }

**Задать правый виджет**

Заменяет всю правую группу одним виджетом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `widget` | `Widget` | виджет для правой стороны |

Возвращает `PanelRowWidget`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelRowWidget::</span>`addLeft(Widget)` {: #addleftwidget data-toc-label="addLeft(Widget)" }

**Добавить слева**

Добавляет виджет в конец левой группы.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `widget` | `Widget` | виджет для добавления |

Возвращает `PanelRowWidget`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelRowWidget::</span>`addRight(Widget)` {: #addrightwidget data-toc-label="addRight(Widget)" }

**Добавить справа**

Добавляет виджет в правую группу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `widget` | `Widget` | виджет для добавления |

Возвращает `PanelRowWidget`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelRowWidget::</span>`rowHeight(float)` {: #rowheightfloat data-toc-label="rowHeight(float)" }

**Высота строки**

Задаёт предпочитаемую высоту строки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `rowHeight` | `float` | высота строки в пикселях |

Возвращает `PanelRowWidget`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelRowWidget::</span>`gap(float)` {: #gapfloat data-toc-label="gap(float)" }

**Отступ строки**

Задаёт равномерное расстояние между элементами обеих сторон и между группами.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `gap` | `float` | размер отступа в пикселях |

Возвращает `PanelRowWidget`: этот виджет для fluent-настройки.

</div>

