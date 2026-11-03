---
title: "VirtualTableColumn"
slug: "virtual-table-column"
category: "Core/Widgets/Data"
locale: "en"
order: 20
---

# VirtualTableColumn

Column configuration for [VirtualTableView](<../../../widgets/data/virtual-table-view.md>) defining header text, width, alignments, and overflow mode.

## Overview

`VirtualTableColumn` is an immutable record configuring a column within a `VirtualTableView`. It specifies the plain or [RichText](<../../text/rich-text.md>) header, width, horizontal and vertical content alignments, and text clipping behavior.

## Defining Table Columns

```java
VirtualTableColumn nameCol = new VirtualTableColumn("Player Name", 120.0f);
VirtualTableColumn scoreCol = new VirtualTableColumn("Score", 80.0f)
        .align(Alignment.END, Alignment.CENTER);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">VirtualTableColumn::</span>`align(Alignment, Alignment)` {: #alignalignment-alignment data-toc-label="align(Alignment, Alignment)" }

**Set Alignment**

Returns a copy of column with updated horizontal and vertical alignments.

| Parameter | Type | Description |
| --- | --- | --- |
| `horizontal` | `Alignment` | — |
| `vertical` | `Alignment` | — |

Returns `VirtualTableColumn`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">VirtualTableColumn::</span>`overflowMode(TextOverflowMode)` {: #overflowmodetextoverflowmode data-toc-label="overflowMode(TextOverflowMode)" }

**Set Overflow Mode**

Returns a copy of column with specified [TextOverflowMode](<../../text/text-overflow-mode.md>).

| Parameter | Type | Description |
| --- | --- | --- |
| `overflowMode` | `TextOverflowMode` | — |

Returns `VirtualTableColumn`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">VirtualTableColumn::</span>`richHeader(RichText)` {: #richheaderrichtext data-toc-label="richHeader(RichText)" }

**Set Rich Header**

Returns a copy of column with styled [RichText](<../../text/rich-text.md>) header.

| Parameter | Type | Description |
| --- | --- | --- |
| `header` | `RichText` | — |

Returns `VirtualTableColumn`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">VirtualTableColumn::</span>`width(float)` {: #widthfloat data-toc-label="width(float)" }

**Set Width**

Returns a copy of column configuration with updated width.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | — |

Returns `VirtualTableColumn`.

</div>

