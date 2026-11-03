---
title: "VirtualTableColumn"
slug: "virtual-table-column"
category: "Core/Виджеты/Данные"
locale: "ru"
order: 20
---

# VirtualTableColumn

Конфигурация столбца для [VirtualTableView](<../../../widgets/data/virtual-table-view.md>) с заголовком, шириной, выравниванием и режимом переполнения текста.

## Обзор

`VirtualTableColumn` — неизменяемая запись, задающая параметры столбца в `VirtualTableView`. Определяет заголовок (строковый или [RichText](<../../text/rich-text.md>)), ширину, горизонтальное и вертикальное выравнивание ячеек, а также режим обрезки текста.

## Определение столбцов таблицы

```java
VirtualTableColumn nameCol = new VirtualTableColumn("Player Name", 120.0f);
VirtualTableColumn scoreCol = new VirtualTableColumn("Score", 80.0f)
        .align(Alignment.END, Alignment.CENTER);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">VirtualTableColumn::</span>`align(Alignment, Alignment)` {: #alignalignment-alignment data-toc-label="align(Alignment, Alignment)" }

**Задать выравнивание**

Возвращает копию столбца с обновленным выравниванием ячеек.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `horizontal` | `Alignment` | — |
| `vertical` | `Alignment` | — |

Возвращает `VirtualTableColumn`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">VirtualTableColumn::</span>`overflowMode(TextOverflowMode)` {: #overflowmodetextoverflowmode data-toc-label="overflowMode(TextOverflowMode)" }

**Задать режим переполнения**

Возвращает копию столбца с заданным режимом переполнения текста.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `overflowMode` | `TextOverflowMode` | — |

Возвращает `VirtualTableColumn`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">VirtualTableColumn::</span>`richHeader(RichText)` {: #richheaderrichtext data-toc-label="richHeader(RichText)" }

**Задать [RichText](<../../text/rich-text.md>) заголовок**

Возвращает копию столбца со стилизованным [RichText](<../../text/rich-text.md>) заголовком.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `header` | `RichText` | — |

Возвращает `VirtualTableColumn`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">VirtualTableColumn::</span>`width(float)` {: #widthfloat data-toc-label="width(float)" }

**Задать ширину**

Возвращает копию конфигурации столбца с обновленной шириной.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `width` | `float` | — |

Возвращает `VirtualTableColumn`.

</div>

