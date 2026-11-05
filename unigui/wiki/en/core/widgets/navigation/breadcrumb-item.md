---
title: "BreadcrumbItem"
slug: "breadcrumb-item"
category: "Core/Widgets/Navigation"
locale: "en"
order: 0
---

# BreadcrumbItem

Navigation item descriptor representing a step or crumb in a breadcrumb trail.

## Overview

Overview and usage documentation.

## Basic Usage

```java
BreadcrumbItem item = new BreadcrumbItem("Dashboard");
item.enabled(true);
```

## Related links

- [Breadcrumb](<../../../widgets/navigation/breadcrumb.md>): Interactive navigation trail widget displaying hierarchical breadcrumb items with customizable separators and selection handling.

## API

<div class="api-member-card" markdown="1">

### `BreadcrumbItem()` {: #breadcrumbitem data-toc-label="BreadcrumbItem()" }

Constructs an empty BreadcrumbItem.

</div>

<div class="api-member-card" markdown="1">

### `BreadcrumbItem(RichText)` {: #breadcrumbitemrichtext data-toc-label="BreadcrumbItem(RichText)" }

Constructs a BreadcrumbItem with a [RichText](<../../text/rich-text.md>) formatted label.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `RichText` | The text label. |

</div>

<div class="api-member-card" markdown="1">

### `BreadcrumbItem(String)` {: #breadcrumbitemstring data-toc-label="BreadcrumbItem(String)" }

Constructs a BreadcrumbItem with a plain text label.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | The text label. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">BreadcrumbItem::</span>`enabled()` {: #enabled data-toc-label="enabled()" }

Returns true if this item is interactive.

Returns `boolean`: True if enabled; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">BreadcrumbItem::</span>`enabled(boolean)` {: #enabledboolean data-toc-label="enabled(boolean)" }

Sets whether this item is interactive.

| Parameter | Type | Description |
| --- | --- | --- |
| `enabled` | `boolean` | Whether the feature or item is enabled. |

Returns `BreadcrumbItem`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">BreadcrumbItem::</span>`richText()` {: #richtext data-toc-label="richText()" }

Returns the [RichText](<../../text/rich-text.md>) formatted label.

Returns `RichText`: The rich text representation..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">BreadcrumbItem::</span>`richText(RichText)` {: #richtextrichtext data-toc-label="richText(RichText)" }

Sets the [RichText](<../../text/rich-text.md>) formatted label.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `RichText` | The text label. |

Returns `BreadcrumbItem`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">BreadcrumbItem::</span>`text()` {: #text data-toc-label="text()" }

Returns the plain text label.

Returns `String`: The plain text label string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">BreadcrumbItem::</span>`text(String)` {: #textstring data-toc-label="text(String)" }

Sets the plain text label.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | The text label. |

Returns `BreadcrumbItem`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">BreadcrumbItem::</span>`value()` {: #value data-toc-label="value()" }

Returns the associated data payload or route string.

Returns `String`: The semantic identifier value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">BreadcrumbItem::</span>`value(String)` {: #valuestring data-toc-label="value(String)" }

Sets the associated data payload or route string.

| Parameter | Type | Description |
| --- | --- | --- |
| `value` | `String` | The semantic value. |

Returns `BreadcrumbItem`: This instance for chaining..

</div>

