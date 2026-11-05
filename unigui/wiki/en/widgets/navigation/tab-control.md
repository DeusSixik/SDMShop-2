---
title: "TabControl"
slug: "tab-control"
category: "Widgets/Navigation"
locale: "en"
order: 0
---

# TabControl

Tabbed navigation container with a scrollable header bar and switched content area.

## Overview

Tabbed navigation container with a scrollable header bar and switched content area. Provides extensive layout configuration and interaction capabilities.

## Basic Usage

```java
TabControl tabs = new TabControl();
tabs.addTab("Editor", new PanelWidget());
tabs.addTab("Console", new PanelWidget());
tabs.selectTab(0);
```

## Related links

- [PageView](<page-view.md>): Retained page container that keeps exactly one page visible at a time.

## API

<div class="api-member-card" markdown="1">

### `TabControl()` {: #tabcontrol data-toc-label="TabControl()" }

Constructs a new TabControl instance.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`addTab(RichText, Widget)` {: #addtabrichtext-widget data-toc-label="addTab(RichText, Widget)" }

Appends a new tab with [RichText](<../../core/text/rich-text.md>) title and content widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `title` | `RichText` | The title text. |
| `content` | `Widget` | The content widget. |

Returns `TabControl`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`addTab(String, Widget)` {: #addtabstring-widget data-toc-label="addTab(String, Widget)" }

Appends a new tab with plain text title and content widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `title` | `String` | The title text. |
| `content` | `Widget` | The content widget. |

Returns `TabControl`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`clearTabs()` {: #cleartabs data-toc-label="clearTabs()" }

Removes all tabs and their content.

Returns `TabControl`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`contentHost()` {: #contenthost data-toc-label="contentHost()" }

Returns the stack container holding tab page contents.

Returns `StackPanel`: The container hosting content views..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`insertTab(int, RichText, Widget)` {: #inserttabint-richtext-widget data-toc-label="insertTab(int, RichText, Widget)" }

Inserts a new tab with [RichText](<../../core/text/rich-text.md>) title at the specified index.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | The index position. |
| `title` | `RichText` | The title text. |
| `content` | `Widget` | The content widget. |

Returns `TabControl`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`insertTab(int, String, Widget)` {: #inserttabint-string-widget data-toc-label="insertTab(int, String, Widget)" }

Inserts a new tab with plain text title at the specified index.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | The index position. |
| `title` | `String` | The title text. |
| `content` | `Widget` | The content widget. |

Returns `TabControl`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`onSelectionChanged(EventListener)` {: #onselectionchangedeventlistener data-toc-label="onSelectionChanged(EventListener)" }

Registers a listener invoked when the selected tab index changes.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | The event listener callback. |

Returns `EventSubscription`: The event subscription handle..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`removeTab(int)` {: #removetabint data-toc-label="removeTab(int)" }

Removes the tab at the specified index.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | The index position. |

Returns `TabControl`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`selectRelative(int)` {: #selectrelativeint data-toc-label="selectRelative(int)" }

Shifts tab selection by the relative delta offset.

| Parameter | Type | Description |
| --- | --- | --- |
| `delta` | `int` | The relative offset delta. |

Returns `TabControl`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`selectTab(int)` {: #selecttabint data-toc-label="selectTab(int)" }

Selects the tab at the specified index, firing a selection change event.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | The index position. |

Returns `TabControl`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`selectedContent()` {: #selectedcontent data-toc-label="selectedContent()" }

Returns the content widget of the selected tab, or null if none.

Returns `Widget`: The content of the selected tab or panel..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`selectedIndex()` {: #selectedindex data-toc-label="selectedIndex()" }

Returns the 0-based index of the currently selected tab, or -1 if none.

Returns `int`: The selected index, or -1 if none..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`selectedTab()` {: #selectedtab data-toc-label="selectedTab()" }

Returns the currently selected Tab descriptor, or null if none.

Returns `Tab`: The selected Tab descriptor, or null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`silentSelectTab(int)` {: #silentselecttabint data-toc-label="silentSelectTab(int)" }

Silently selects the tab at the given index without emitting events.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | The index position. |

Returns `TabControl`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`tabButton(int)` {: #tabbuttonint data-toc-label="tabButton(int)" }

Returns the toggle button corresponding to the tab at the given index.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | The index position. |

Returns `ToggleButton`: The [ToggleButton](<../interaction/toggle-button.md>) for the specified tab..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`tabCount()` {: #tabcount data-toc-label="tabCount()" }

Returns the total number of tabs.

Returns `int`: The total number of tabs..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`tabHeader()` {: #tabheader data-toc-label="tabHeader()" }

Returns the header container holding tab toggle buttons.

Returns `HBox`: The tab header container..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`tabHeaderScroll()` {: #tabheaderscroll data-toc-label="tabHeaderScroll()" }

Returns the scroll view wrapping the tab header.

Returns `ScrollView`: The scroll container wrapping the tab header..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`tabs()` {: #tabs data-toc-label="tabs()" }

Returns an unmodifiable list of tab descriptors.

Returns `List`: An unmodifiable list of tab descriptors..

</div>

