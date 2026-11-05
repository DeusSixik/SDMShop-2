---
title: "PageView"
slug: "page-view"
category: "Widgets/Navigation"
locale: "en"
order: 0
---

# PageView

Retained page container that keeps exactly one page visible at a time.

## Overview

Retained page container that keeps exactly one page visible at a time. Provides extensive layout configuration and interaction capabilities.

## Basic Usage

```java
PageView pageView = new PageView();
pageView.addPage(new Label("Page 1"));
pageView.addPage(new Label("Page 2"));
pageView.selectedIndex(0);
```

## Related links

- [TabControl](<tab-control.md>): Tabbed navigation container with a scrollable header bar and switched content area.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`addPage(Widget)` {: #addpagewidget data-toc-label="addPage(Widget)" }

Appends a page widget to the end of the page list.

| Parameter | Type | Description |
| --- | --- | --- |
| `page` | `Widget` | The page widget. |

Returns `PageView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`clearPages()` {: #clearpages data-toc-label="clearPages()" }

Removes all pages from the container.

Returns `PageView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`insertPage(int, Widget)` {: #insertpageint-widget data-toc-label="insertPage(int, Widget)" }

Inserts a page widget at the specified index.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | The index position. |
| `page` | `Widget` | The page widget. |

Returns `PageView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`onSelectionChanged(EventListener)` {: #onselectionchangedeventlistener data-toc-label="onSelectionChanged(EventListener)" }

Registers a listener invoked when the selected page index changes.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | The event listener callback. |

Returns `EventSubscription`: The event subscription handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`pageCount()` {: #pagecount data-toc-label="pageCount()" }

Returns the total number of pages.

Returns `int`: The total number of pages..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`pages()` {: #pages data-toc-label="pages()" }

Returns an unmodifiable list of all contained page widgets.

Returns `List`: An unmodifiable list of page widgets..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`removePage(Widget)` {: #removepagewidget data-toc-label="removePage(Widget)" }

Removes the specified page widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `page` | `Widget` | The page widget. |

Returns `PageView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`removePage(int)` {: #removepageint data-toc-label="removePage(int)" }

Removes the page at the specified index.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | The index position. |

Returns `PageView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`selectRelative(int)` {: #selectrelativeint data-toc-label="selectRelative(int)" }

Shifts current page selection by the specified relative delta offset.

| Parameter | Type | Description |
| --- | --- | --- |
| `delta` | `int` | The relative offset delta. |

Returns `PageView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`selectedIndex()` {: #selectedindex data-toc-label="selectedIndex()" }

Returns the 0-based index of the currently selected page.

Returns `int`: The selected index, or -1 if none..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`selectedIndex(int)` {: #selectedindexint data-toc-label="selectedIndex(int)" }

Selects the active page by index, firing a selection change event.

| Parameter | Type | Description |
| --- | --- | --- |
| `selectedIndex` | `int` | The selected index. |

Returns `PageView`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`selectedPage()` {: #selectedpage data-toc-label="selectedPage()" }

Returns the currently selected active page widget, or null if none.

Returns `Widget`: The selected page widget, or null if none..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`silentSelectedIndex(int)` {: #silentselectedindexint data-toc-label="silentSelectedIndex(int)" }

Silently selects a page index without emitting selection change events.

| Parameter | Type | Description |
| --- | --- | --- |
| `selectedIndex` | `int` | The selected index. |

Returns `PageView`: This instance for chaining..

</div>

