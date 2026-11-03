---
title: "Breadcrumb"
slug: "breadcrumb"
category: "Widgets/Navigation"
locale: "en"
order: 0
---

# Breadcrumb

Interactive navigation trail widget displaying hierarchical breadcrumb items with customizable separators and selection handling.

## Overview

Interactive navigation trail widget displaying hierarchical breadcrumb items with customizable separators and selection handling. Provides extensive layout configuration and interaction capabilities.

## Basic Usage

```java
Breadcrumb breadcrumb = new Breadcrumb();
breadcrumb.addItem("Home").addItem("Settings");
```

## Related links

- [BreadcrumbItem](<../../core/widgets/navigation/breadcrumb-item.md>): Navigation item descriptor representing a step or crumb in a breadcrumb trail.

## API

<div class="api-member-card" markdown="1">

### `Breadcrumb()` {: #breadcrumb data-toc-label="Breadcrumb()" }

Constructs an empty Breadcrumb navigation bar.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`addItem(BreadcrumbItem)` {: #additembreadcrumbitem data-toc-label="addItem(BreadcrumbItem)" }

Appends a [BreadcrumbItem](<../../core/widgets/navigation/breadcrumb-item.md>) instance to the trail.

| Parameter | Type | Description |
| --- | --- | --- |
| `item` | `BreadcrumbItem` | The target item. |

Returns `Breadcrumb`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`addItem(RichText)` {: #additemrichtext data-toc-label="addItem(RichText)" }

Appends a [RichText](<../../core/text/rich-text.md>) formatted item to the trail.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `RichText` | The text label. |

Returns `Breadcrumb`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`addItem(String)` {: #additemstring data-toc-label="addItem(String)" }

Appends a plain text item to the trail.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | The text label. |

Returns `Breadcrumb`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`breadcrumbItems(List)` {: #breadcrumbitemslist data-toc-label="breadcrumbItems(List)" }

Replaces the trail with [BreadcrumbItem](<../../core/widgets/navigation/breadcrumb-item.md>) instances.

| Parameter | Type | Description |
| --- | --- | --- |
| `items` | `List` | The collection of items. |

Returns `Breadcrumb`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`clearItems()` {: #clearitems data-toc-label="clearItems()" }

Clears all items from the breadcrumb trail.

Returns `Breadcrumb`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`host()` {: #host data-toc-label="host()" }

Returns the internal [WrapPanel](<../containers/wrap-panel.md>) container hosting the breadcrumb buttons.

Returns `WrapPanel`: The container hosting items..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`itemButton(int)` {: #itembuttonint data-toc-label="itemButton(int)" }

Returns the [Button](<../interaction/button.md>) widget representing the item at the specified index.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | The index position. |

Returns `Button`: The associated button component..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`itemCount()` {: #itemcount data-toc-label="itemCount()" }

Returns the total number of items in the breadcrumb trail.

Returns `int`: The total item count..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`items()` {: #items data-toc-label="items()" }

Returns an unmodifiable list of breadcrumb items.

Returns `List`: An unmodifiable list of node items..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`items(List)` {: #itemslist data-toc-label="items(List)" }

Replaces the trail with plain-text items.

| Parameter | Type | Description |
| --- | --- | --- |
| `items` | `List` | The collection of items. |

Returns `Breadcrumb`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`lineSpacing(float)` {: #linespacingfloat data-toc-label="lineSpacing(float)" }

Sets the vertical line spacing when items wrap to multiple lines.

| Parameter | Type | Description |
| --- | --- | --- |
| `lineSpacing` | `float` | The line spacing distance in pixels. |

Returns `Breadcrumb`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`onSelectionChanged(EventListener)` {: #onselectionchangedeventlistener data-toc-label="onSelectionChanged(EventListener)" }

Registers a listener invoked when the selected breadcrumb step changes.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | The event listener callback. |

Returns `EventSubscription`: The event subscription handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`removeItem(int)` {: #removeitemint data-toc-label="removeItem(int)" }

Removes the breadcrumb item at the specified index.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | The index position. |

Returns `Breadcrumb`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`richItems(List)` {: #richitemslist data-toc-label="richItems(List)" }

Replaces the trail with [RichText](<../../core/text/rich-text.md>) formatted items.

| Parameter | Type | Description |
| --- | --- | --- |
| `items` | `List` | The collection of items. |

Returns `Breadcrumb`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`selectedIndex()` {: #selectedindex data-toc-label="selectedIndex()" }

Returns the zero-based index of the currently selected breadcrumb item.

Returns `int`: The result value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`selectedIndex(int)` {: #selectedindexint data-toc-label="selectedIndex(int)" }

Selects the item at the specified index and dispatches a selection changed event.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | The index position. |

Returns `Breadcrumb`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`selectedItem()` {: #selecteditem data-toc-label="selectedItem()" }

Returns the currently selected [BreadcrumbItem](<../../core/widgets/navigation/breadcrumb-item.md>), or null if none.

Returns `BreadcrumbItem`: The node item instance, or null if not found..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`selectedPath()` {: #selectedpath data-toc-label="selectedPath()" }

Returns a sublist of items from index 0 up to the selected index inclusive.

Returns `List`: An unmodifiable list of node items..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`separator()` {: #separator data-toc-label="separator()" }

Returns the separator string placed between items.

Returns `String`: The separator widget instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`separator(String)` {: #separatorstring data-toc-label="separator(String)" }

Sets the separator string displayed between breadcrumb items.

| Parameter | Type | Description |
| --- | --- | --- |
| `separator` | `String` | The custom separator widget. |

Returns `Breadcrumb`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`silentSelectedIndex(int)` {: #silentselectedindexint data-toc-label="silentSelectedIndex(int)" }

Selects the item at the specified index without firing event callbacks.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | The index position. |

Returns `Breadcrumb`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`spacing(float)` {: #spacingfloat data-toc-label="spacing(float)" }

Sets the horizontal spacing between items and separators.

| Parameter | Type | Description |
| --- | --- | --- |
| `spacing` | `float` | The spacing distance in pixels. |

Returns `Breadcrumb`: This instance for chaining..

</div>

