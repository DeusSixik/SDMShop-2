---
title: "VirtualTableView"
slug: "virtual-table-view"
category: "Widgets/Data"
locale: "ru"
order: 30
---

# VirtualTableView

Высокопроизводительная виртуализированная таблица с изменением размера колонок, перетаскиванием, сортировкой и редактированием ячеек.

## Обзор

`VirtualTableView` отображает большие объёмы табличных данных с высокой производительностью благодаря виртуализации строк. Поддерживает перетаскивание заголовков колонок, изменение ширины мышью, индикаторы сортировки, выделение ячеек, навигацию с клавиатуры и встроенное редактирование.

## Создание таблицы данных

```java
VirtualTableView table = new VirtualTableView()
        .addColumn("ID", 60.0f)
        .addColumn("Name", 150.0f)
        .addColumn("Score", 80.0f)
        .rowCount(1000);
```

## API

### API

| Метод | Описание |
| --- | --- |
| [`addColumn(String, float)`](#addcolumnstring-float) | Добавляет новый столбец с заданным заголовком и шириной. |
| [`columnResizeEnabled(boolean)`](#columnresizeenabledboolean) | Управляет возможностью пользователя интерактивно менять ширину столбцов перетаскиванием границы. |
| [`columnWidth(int)`](#columnwidthint) | Возвращает ширину в пикселях столбца по указанному индексу. |
| [`columns(List)`](#columnslist) | Устанавливает или возвращает список определений колонок таблицы. |
| [`minColumnWidth(float)`](#mincolumnwidthfloat) | Возвращает или устанавливает минимально допустимую ширину столбца при изменении размера. |
| [`moveColumn(int, int)`](#movecolumnint-int) | Перемещает столбец из исходного индекса в новую позицию. |
| [`resizeColumn(int, float)`](#resizecolumnint-float) | Устанавливает ширину столбца по указанному индексу. |
| [`rowCount()`](#rowcount) | Возвращает или устанавливает общее количество виртуальных строк таблицы. |
| [`rowCount(int)`](#rowcountint) | Возвращает или устанавливает общее количество виртуальных строк таблицы. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`addColumn(String, float)` {: #addcolumnstring-float data-toc-label="addColumn(String, float)" }

**Добавить столбец**

Добавляет новый столбец с заданным заголовком и шириной.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `header` | `String` | [Text](<../display/text.md>) displayed in the column header. |
| `width` | `float` | Initial column width in UI pixels; constrained by the minimum column width. |

Возвращает `VirtualTableView`: This table for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`columnResizeEnabled(boolean)` {: #columnresizeenabledboolean data-toc-label="columnResizeEnabled(boolean)" }

**Разрешение изменения ширины**

Управляет возможностью пользователя интерактивно менять ширину столбцов перетаскиванием границы.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `columnResizeEnabled` | `boolean` | — |

Возвращает `VirtualTableView`.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`columnWidth(int)` {: #columnwidthint data-toc-label="columnWidth(int)" }

**Ширина столбца**

Возвращает ширину в пикселях столбца по указанному индексу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `columnIndex` | `int` | — |

Возвращает `float`.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`columns(List)` {: #columnslist data-toc-label="columns(List)" }

**Список столбцов**

Устанавливает или возвращает список определений колонок таблицы.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `columns` | `List` | Columns in display order; null clears all columns and null entries are skipped. |

Возвращает `VirtualTableView`: This table for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`minColumnWidth(float)` {: #mincolumnwidthfloat data-toc-label="minColumnWidth(float)" }

**Минимальная ширина столбца**

Возвращает или устанавливает минимально допустимую ширину столбца при изменении размера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `minColumnWidth` | `float` | — |

Возвращает `VirtualTableView`.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`moveColumn(int, int)` {: #movecolumnint-int data-toc-label="moveColumn(int, int)" }

**Переместить столбец**

Перемещает столбец из исходного индекса в новую позицию.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `oldIndex` | `int` | — |
| `newIndex` | `int` | — |

Возвращает `VirtualTableView`.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`resizeColumn(int, float)` {: #resizecolumnint-float data-toc-label="resizeColumn(int, float)" }

**Изменить ширину столбца**

Устанавливает ширину столбца по указанному индексу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `columnIndex` | `int` | — |
| `width` | `float` | — |

Возвращает `VirtualTableView`.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`rowCount()` {: #rowcount data-toc-label="rowCount()" }

**Количество строк**

Возвращает или устанавливает общее количество виртуальных строк таблицы.

Возвращает `int`: The number of rows currently exposed by the virtualized table..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`rowCount(int)` {: #rowcountint data-toc-label="rowCount(int)" }

**Количество строк**

Возвращает или устанавливает общее количество виртуальных строк таблицы.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `rowCount` | `int` | New row count; negative values are normalized to zero. |

Возвращает `VirtualTableView`: This table for fluent configuration..

</div>

### Rows and Columns

| Метод | Описание |
| --- | --- |
| [`insertRows(int, int)`](#insertrowsint-int) | Inserts a range of rows at a model index. |
| [`addRow()`](#addrow) | Appends one row to the table. |
| [`removeRows(int, int)`](#removerowsint-int) | Removes a clamped row range and updates related state. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`insertRows(int, int)` {: #insertrowsint-int data-toc-label="insertRows(int, int)" }

**Insert rows**

Inserts a range of rows at a model index.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | Insertion index, clamped to the range from zero through the current row count. |
| `count` | `int` | Number of rows to insert; non-positive values are ignored. |

Возвращает `VirtualTableView`: This table for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`addRow()` {: #addrow data-toc-label="addRow()" }

**Append a row**

Appends one row to the table.

Возвращает `VirtualTableView`: This table for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`removeRows(int, int)` {: #removerowsint-int data-toc-label="removeRows(int, int)" }

**Remove rows**

Removes a clamped row range and updates related state.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | First model row to remove; clamped to an existing row when rows are present. |
| `count` | `int` | Maximum number of rows to remove; non-positive values are ignored. |

Возвращает `VirtualTableView`: This table for fluent configuration..

</div>

### Sorting

| Метод | Описание |
| --- | --- |
| [`sortBy(int, SortDirection)`](#sortbyint-sortdirection) | Sets the active sort column and direction. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`sortBy(int, SortDirection)` {: #sortbyint-sortdirection data-toc-label="sortBy(int, SortDirection)" }

**Sort rows**

Sets the active sort column and direction.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `columnIndex` | `int` | Displayed column index to sort by; ignored when direction is NONE. |
| `direction` | `SortDirection` | Sort direction; null or NONE clears sorting. |

Возвращает `VirtualTableView`: This table for fluent configuration..

</div>

### Cell Editing

| Метод | Описание |
| --- | --- |
| [`beginEdit(int, int)`](#begineditint-int) | Starts editing the requested row and displayed column when editing is enabled. |
| [`commitEdit()`](#commitedit) | Completes the active edit and emits the committed text. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`beginEdit(int, int)` {: #begineditint-int data-toc-label="beginEdit(int, int)" }

**Begin cell editing**

Starts editing the requested row and displayed column when editing is enabled.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `row` | `int` | Model row index to edit. |
| `column` | `int` | Displayed column index to edit. |

Возвращает `VirtualTableView`: This table for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`commitEdit()` {: #commitedit data-toc-label="commitEdit()" }

**Commit cell editing**

Completes the active edit and emits the committed text.

Возвращает `VirtualTableView`: This table for fluent configuration..

</div>

