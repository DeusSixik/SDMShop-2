---
title: "SearchableGridPickerWidget"
slug: "searchable-grid-picker-widget"
category: "Widgets/Interaction"
locale: "en"
order: 250
---

# SearchableGridPickerWidget

Abstract searchable dialog picker displaying items in a virtualized grid with preview, filter, and tooltip.

## Overview

`SearchableGridPickerWidget<T>` provides a complete modal picker architecture. Clicking the preview button opens a dialog window hosting a `SearchField`, a virtualized `VirtualListView` grid of items, and tooltips.

## Custom Item Picker

```java
public class ItemPicker extends SearchableGridPickerWidget<ItemDefinition> {
    @Override
    protected String entryIdText(ItemDefinition entry) {
        return entry.id();
    }
    @Override
    protected String searchText(ItemDefinition entry) {
        return entry.name();
    }
}
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`clearQuery()` {: #clearquery data-toc-label="clearQuery()" }

**Clear Query**

Clears search filter query.

Returns `SearchableGridPickerWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`clearSelection()` {: #clearselection data-toc-label="clearSelection()" }

**Clear Selection**

Deselects currently selected item.

Returns `SearchableGridPickerWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`close()` {: #close data-toc-label="close()" }

**Close Dialog**

Closes search and selection dialog window.

Returns `SearchableGridPickerWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`dialog()` {: #dialog data-toc-label="dialog()" }

**Dialog Window**

Returns modal dialog [WindowWidget](<../feedback/window-widget.md>).

Returns `WindowWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`entries()` {: #entries data-toc-label="entries()" }

**Get Entries**

Returns unmodifiable list of all registered entries.

Returns `List`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`entries(Collection)` {: #entriescollection data-toc-label="entries(Collection)" }

**Set Entries**

Populates selectable items collection.

| Parameter | Type | Description |
| --- | --- | --- |
| `entries` | `Collection` | — |

Returns `SearchableGridPickerWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`entryCount()` {: #entrycount data-toc-label="entryCount()" }

**Entry Count**

Returns total count of registered entries.

Returns `int`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`filteredEntries()` {: #filteredentries data-toc-label="filteredEntries()" }

**Filtered Entries**

Returns list of entries matching current search query.

Returns `List`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`onSelectionChanged(EventListener)` {: #onselectionchangedeventlistener data-toc-label="onSelectionChanged(EventListener)" }

**On Selection Changed**

Registers callback triggered when selected item changes.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | — |

Returns `EventSubscription`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`open()` {: #open data-toc-label="open()" }

**Open Dialog**

Opens search and selection dialog window.

Returns `SearchableGridPickerWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`openButton()` {: #openbutton data-toc-label="openButton()" }

**Open [Button](<button.md>)**

Returns trigger button opening the dialog.

Returns `Button`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`opened()` {: #opened data-toc-label="opened()" }

**Is Dialog Opened**

Returns whether dialog window is currently visible.

Returns `boolean`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`query()` {: #query data-toc-label="query()" }

**Get Query**

Returns active search filter query string.

Returns `String`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`query(String)` {: #querystring data-toc-label="query(String)" }

**Set Query**

Sets search filter query and refreshes grid.

| Parameter | Type | Description |
| --- | --- | --- |
| `query` | `String` | — |

Returns `SearchableGridPickerWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`resultGrid()` {: #resultgrid data-toc-label="resultGrid()" }

**Result Grid**

Returns virtualized list view hosting grid items.

Returns `VirtualListView`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`searchField()` {: #searchfield data-toc-label="searchField()" }

**Search Field**

Returns internal [SearchField](<search-field.md>) widget in the dialog.

Returns `SearchField`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`selectIndex(int)` {: #selectindexint data-toc-label="selectIndex(int)" }

**Select By Index**

Selects entry at index in master list.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | — |

Returns `SearchableGridPickerWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`selectedEntry()` {: #selectedentry data-toc-label="selectedEntry()" }

**Selected Entry**

Returns optional containing selected item.

Returns `Optional`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`selectedPreview()` {: #selectedpreview data-toc-label="selectedPreview()" }

**Selected Preview**

Returns widget displaying preview of current selection.

Returns `WidgetBase`.

</div>

