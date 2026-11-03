---
title: "VirtualTableView"
slug: "virtual-table-view"
category: "Widgets/Data"
locale: "en"
order: 30
---

# VirtualTableView

High-performance virtualized grid/table view widget supporting column resizing, reordering, sorting, and cell editing.

## Overview

`VirtualTableView` renders large tabular data sets with high performance by virtualizing rows. Features draggable column headers, column resizing, interactive sort indicators, cell selection models, keyboard navigation, and inline cell editing.

## Basic Table Setup

```java
VirtualTableView table = new VirtualTableView()
        .addColumn("ID", 60.0f)
        .addColumn("Name", 150.0f)
        .addColumn("Score", 80.0f)
        .rowCount(1000);
```

## API

### API

| Method | Description |
| --- | --- |
| [`addColumn(String, float)`](#addcolumnstring-float) | Appends a new column with specified header and width. |
| [`columnResizeEnabled(boolean)`](#columnresizeenabledboolean) | Controls whether user can drag column borders to resize columns. |
| [`columnWidth(int)`](#columnwidthint) | Returns width in pixels of column at given index. |
| [`columns(List)`](#columnslist) | Sets or retrieves the list of table column definitions. |
| [`minColumnWidth(float)`](#mincolumnwidthfloat) | Returns or sets minimum allowed column width when resizing. |
| [`moveColumn(int, int)`](#movecolumnint-int) | Reorders column from old index to new index. |
| [`resizeColumn(int, float)`](#resizecolumnint-float) | Sets width of column at given index. |
| [`rowCount()`](#rowcount) | Returns or sets the total number of virtual rows. |
| [`rowCount(int)`](#rowcountint) | Returns or sets the total number of virtual rows. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`addColumn(String, float)` {: #addcolumnstring-float data-toc-label="addColumn(String, float)" }

**Add Column**

Appends a new column with specified header and width.

| Parameter | Type | Description |
| --- | --- | --- |
| `header` | `String` | [Text](<../display/text.md>) displayed in the column header. |
| `width` | `float` | Initial column width in UI pixels; constrained by the minimum column width. |

Returns `VirtualTableView`: This table for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`columnResizeEnabled(boolean)` {: #columnresizeenabledboolean data-toc-label="columnResizeEnabled(boolean)" }

**Column Resize Enabled**

Controls whether user can drag column borders to resize columns.

| Parameter | Type | Description |
| --- | --- | --- |
| `columnResizeEnabled` | `boolean` | — |

Returns `VirtualTableView`.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`columnWidth(int)` {: #columnwidthint data-toc-label="columnWidth(int)" }

**Get Column Width**

Returns width in pixels of column at given index.

| Parameter | Type | Description |
| --- | --- | --- |
| `columnIndex` | `int` | — |

Returns `float`.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`columns(List)` {: #columnslist data-toc-label="columns(List)" }

**Columns List**

Sets or retrieves the list of table column definitions.

| Parameter | Type | Description |
| --- | --- | --- |
| `columns` | `List` | Columns in display order; null clears all columns and null entries are skipped. |

Returns `VirtualTableView`: This table for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`minColumnWidth(float)` {: #mincolumnwidthfloat data-toc-label="minColumnWidth(float)" }

**Min Column Width**

Returns or sets minimum allowed column width when resizing.

| Parameter | Type | Description |
| --- | --- | --- |
| `minColumnWidth` | `float` | — |

Returns `VirtualTableView`.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`moveColumn(int, int)` {: #movecolumnint-int data-toc-label="moveColumn(int, int)" }

**Move Column**

Reorders column from old index to new index.

| Parameter | Type | Description |
| --- | --- | --- |
| `oldIndex` | `int` | — |
| `newIndex` | `int` | — |

Returns `VirtualTableView`.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`resizeColumn(int, float)` {: #resizecolumnint-float data-toc-label="resizeColumn(int, float)" }

**Resize Column**

Sets width of column at given index.

| Parameter | Type | Description |
| --- | --- | --- |
| `columnIndex` | `int` | — |
| `width` | `float` | — |

Returns `VirtualTableView`.

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`rowCount()` {: #rowcount data-toc-label="rowCount()" }

**Row Count**

Returns or sets the total number of virtual rows.

Returns `int`: The number of rows currently exposed by the virtualized table..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`rowCount(int)` {: #rowcountint data-toc-label="rowCount(int)" }

**Row Count**

Returns or sets the total number of virtual rows.

| Parameter | Type | Description |
| --- | --- | --- |
| `rowCount` | `int` | New row count; negative values are normalized to zero. |

Returns `VirtualTableView`: This table for fluent configuration..

</div>

### Rows and Columns

| Method | Description |
| --- | --- |
| [`insertRows(int, int)`](#insertrowsint-int) | Inserts a range of rows at a model index. |
| [`addRow()`](#addrow) | Appends one row to the table. |
| [`removeRows(int, int)`](#removerowsint-int) | Removes a clamped row range and updates related state. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`insertRows(int, int)` {: #insertrowsint-int data-toc-label="insertRows(int, int)" }

**Insert rows**

Inserts a range of rows at a model index.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | Insertion index, clamped to the range from zero through the current row count. |
| `count` | `int` | Number of rows to insert; non-positive values are ignored. |

Returns `VirtualTableView`: This table for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`addRow()` {: #addrow data-toc-label="addRow()" }

**Append a row**

Appends one row to the table.

Returns `VirtualTableView`: This table for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`removeRows(int, int)` {: #removerowsint-int data-toc-label="removeRows(int, int)" }

**Remove rows**

Removes a clamped row range and updates related state.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | First model row to remove; clamped to an existing row when rows are present. |
| `count` | `int` | Maximum number of rows to remove; non-positive values are ignored. |

Returns `VirtualTableView`: This table for fluent configuration..

</div>

### Sorting

| Method | Description |
| --- | --- |
| [`sortBy(int, SortDirection)`](#sortbyint-sortdirection) | Sets the active sort column and direction. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`sortBy(int, SortDirection)` {: #sortbyint-sortdirection data-toc-label="sortBy(int, SortDirection)" }

**Sort rows**

Sets the active sort column and direction.

| Parameter | Type | Description |
| --- | --- | --- |
| `columnIndex` | `int` | Displayed column index to sort by; ignored when direction is NONE. |
| `direction` | `SortDirection` | Sort direction; null or NONE clears sorting. |

Returns `VirtualTableView`: This table for fluent configuration..

</div>

### Cell Editing

| Method | Description |
| --- | --- |
| [`beginEdit(int, int)`](#begineditint-int) | Starts editing the requested row and displayed column when editing is enabled. |
| [`commitEdit()`](#commitedit) | Completes the active edit and emits the committed text. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`beginEdit(int, int)` {: #begineditint-int data-toc-label="beginEdit(int, int)" }

**Begin cell editing**

Starts editing the requested row and displayed column when editing is enabled.

| Parameter | Type | Description |
| --- | --- | --- |
| `row` | `int` | Model row index to edit. |
| `column` | `int` | Displayed column index to edit. |

Returns `VirtualTableView`: This table for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">VirtualTableView::</span>`commitEdit()` {: #commitedit data-toc-label="commitEdit()" }

**Commit cell editing**

Completes the active edit and emits the committed text.

Returns `VirtualTableView`: This table for fluent configuration..

</div>

