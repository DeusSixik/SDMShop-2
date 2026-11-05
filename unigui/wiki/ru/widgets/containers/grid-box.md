---
title: "GridBox"
slug: "grid-box"
category: "Widgets/Containers"
locale: "ru"
order: 80
---

# GridBox

Сетка с равномерным делением на колонки или настраиваемыми шаблонами треков.

## Обзор

`GridBox` размещает дочерние элементы в двухмерной сетке. По умолчанию доступная ширина делится на фиксированное число равномерных колонок (`columns(n)`), а элементы заполняют ячейки построчно.

Отступы настраиваются единым вызовом `spacing(px)` или раздельно через `horizontalSpacing(px)` и `verticalSpacing(px)`.

Для гибких колонок (дробных `fr`, пиксельных `px` или `minmax`) можно задать явный шаблон треков через `gridColumns(GridTrack...)`.

## Сетка слотов инвентаря

```java
GridBox grid = new GridBox()
        .columns(4)
        .spacing(6.0f);
for (ItemSlot slot : slots) {
    grid.addChild(slot);
}
```

## Связанные ссылки

- [FlexBox](<flex-box.md>)
- [LayoutStyle](<../../core/layout/layout-style.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">GridBox::</span>`columns(int)` {: #columnsint data-toc-label="columns(int)" }

**Колонки сетки**

Задаёт фиксированное число колонок сетки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `columns` | `int` | количество колонок; минимум 1 |

Возвращает `GridBox`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">GridBox::</span>`spacing(float)` {: #spacingfloat data-toc-label="spacing(float)" }

**Общий интервал сетки**

Задаёт равномерные отступы между строками и колонками.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `spacing` | `float` | отступ в пикселях UI |

Возвращает `GridBox`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">GridBox::</span>`horizontalSpacing(float)` {: #horizontalspacingfloat data-toc-label="horizontalSpacing(float)" }

**Горизонтальный интервал**

Задаёт отступ между колонками.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `horizontalSpacing` | `float` | отступ между колонками в пикселях |

Возвращает `GridBox`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">GridBox::</span>`verticalSpacing(float)` {: #verticalspacingfloat data-toc-label="verticalSpacing(float)" }

**Вертикальный интервал**

Задаёт отступ между строками.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `verticalSpacing` | `float` | отступ между строками в пикселях |

Возвращает `GridBox`: этот виджет для fluent-настройки.

</div>

