---
title: "ComboBox"
slug: "combo-box"
category: "Widgets/Interaction"
locale: "en"
order: 210
---

# ComboBox

Selection control pairing a current selection button with a dropdown options list.

## Overview

`ComboBox` allows selecting one item from a list of predefined options. When opened, an overlay popup displays the options with scroll support and hover highlighting.

## Resolution Selector

```java
ComboBox resolution = new ComboBox()
        .options("1920x1080", "2560x1440", "3840x2160")
        .selectedIndex(0);
resolution.onSelectionChanged(e -> applyResolution(resolution.selectedItem()));
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`addItem(String)` {: #additemstring data-toc-label="addItem(String)" }

**Add Item**

Appends a single item option.

| Parameter | Type | Description |
| --- | --- | --- |
| `item` | `String` | — |

Returns `ComboBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`close()` {: #close data-toc-label="close()" }

**Close List**

Closes popup dropdown options list.

Returns `ComboBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`itemCount()` {: #itemcount data-toc-label="itemCount()" }

**Get Item Count**

Returns total number of options.

Returns `int`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`items()` {: #items data-toc-label="items()" }

**Get Items**

Returns list of string options.

Returns `List`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`items(List)` {: #itemslist data-toc-label="items(List)" }

**Set Items**

Sets selectable string options.

| Parameter | Type | Description |
| --- | --- | --- |
| `items` | `List` | — |

Returns `ComboBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`open()` {: #open data-toc-label="open()" }

**Open List**

Opens popup dropdown options list.

Returns `ComboBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`opened()` {: #opened data-toc-label="opened()" }

**Is Opened**

Returns whether dropdown list is open.

Returns `boolean`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`optionHoverBackground(ColorView)` {: #optionhoverbackgroundcolorview data-toc-label="optionHoverBackground(ColorView)" }

**Hover Background**

Sets option highlight background color when hovered.

| Parameter | Type | Description |
| --- | --- | --- |
| `color` | `ColorView` | — |

Returns `ComboBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`removeItem(int)` {: #removeitemint data-toc-label="removeItem(int)" }

**Remove Item**

Removes item option at given index.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | — |

Returns `ComboBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`selectedIndex()` {: #selectedindex data-toc-label="selectedIndex()" }

**Get Selected Index**

Returns zero-based index of selected item or -1.

Returns `int`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`selectedIndex(int)` {: #selectedindexint data-toc-label="selectedIndex(int)" }

**Set Selected Index**

Selects option at index and updates header.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | option index |

Returns `ComboBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`selectedItem()` {: #selecteditem data-toc-label="selectedItem()" }

**Get Selected Item**

Returns text of selected option or empty string.

Returns `String`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`silentSelectedIndex(int)` {: #silentselectedindexint data-toc-label="silentSelectedIndex(int)" }

**Set Silent Selected Index**

Updates selected option index without firing listeners.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | — |

Returns `ComboBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`toggle()` {: #toggle data-toc-label="toggle()" }

**Toggle List**

Toggles popup dropdown open/closed state.

Returns `ComboBox`.

</div>

