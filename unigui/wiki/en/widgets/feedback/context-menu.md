---
title: "ContextMenu"
slug: "context-menu"
category: "Widgets/Feedback"
locale: "en"
order: 0
---

# ContextMenu

Floating context menu container managing hierarchical items, action callbacks, and auto-dismiss behavior.

## Overview

`ContextMenu` is a floating dropdown menu container that presents an ordered list of actionable options, submenus, and visual separators.

Key features:
- [Text](<../display/text.md>) and rich-text menu items with associated `Runnable` click handlers.
- Nested hierarchical submenus.
- Smart auto-repositioning to remain fully visible within host screen bounds (`hostBounds`, `arrangeInHost`).
- Dismissal on outside clicks (`closeOnOutsideClick`).

## Creating and Opening Context [Menu](<../../core/widgets/navigation/menu.md>)

```java
ContextMenu menu = new ContextMenu();
menu.item("Copy", () -> copy()).separator().item("Paste", () -> paste());
menu.openAt(mouseX, mouseY);
```

## Related links

- [OverlayLayer](<overlay-layer.md>): Full-screen overlay root managing modal backdrops, dialogs, popups, and layer-based hit testing.
- [Popup](<popup.md>): Anchored floating popup widget that positions itself relative to target widgets or screen coordinates.

## API

<div class="api-member-card" markdown="1">

### `ContextMenu()` {: #contextmenu data-toc-label="ContextMenu()" }

Constructs an empty ContextMenu.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`arrangeInHost(RectView)` {: #arrangeinhostrectview data-toc-label="arrangeInHost(RectView)" }

Arranges the menu within host boundaries, flipping placement if screen edges are exceeded.

| Parameter | Type | Description |
| --- | --- | --- |
| `hostBounds` | `RectView` | Available host rectangular boundary. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`close()` {: #close data-toc-label="close()" }

Closes the menu and any active submenus.

Returns `ContextMenu`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`closeOnOutsideClick()` {: #closeonoutsideclick data-toc-label="closeOnOutsideClick()" }

Returns true if clicking outside automatically dismisses the menu.

Returns `boolean`: True if closing on outside click is enabled..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`closeOnOutsideClick(boolean)` {: #closeonoutsideclickboolean data-toc-label="closeOnOutsideClick(boolean)" }

Sets whether clicking outside closes the menu.

| Parameter | Type | Description |
| --- | --- | --- |
| `closeOnOutsideClick` | `boolean` | Whether clicking outside automatically dismisses the widget. |

Returns `ContextMenu`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`item(Button)` {: #itembutton data-toc-label="item(Button)" }

Adds a preconfigured [Button](<../interaction/button.md>) widget as a menu item.

| Parameter | Type | Description |
| --- | --- | --- |
| `button` | `Button` | Custom button widget to attach. |

Returns `ContextMenu`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`item(RichText)` {: #itemrichtext data-toc-label="item(RichText)" }

Adds a menu item with [RichText](<../../core/text/rich-text.md>).

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `RichText` | Display text string. |

Returns `ContextMenu`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`item(RichText, Runnable)` {: #itemrichtext-runnable data-toc-label="item(RichText, Runnable)" }

Adds a menu item with [RichText](<../../core/text/rich-text.md>) and click callback.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `RichText` | Display text string. |
| `action` | `Runnable` | Callback action to execute. |

Returns `ContextMenu`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`item(String)` {: #itemstring data-toc-label="item(String)" }

Adds a menu item with plain text.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | Display text string. |

Returns `ContextMenu`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`item(String, Runnable)` {: #itemstring-runnable data-toc-label="item(String, Runnable)" }

Adds a menu item with plain text and click callback.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | Display text string. |
| `action` | `Runnable` | Callback action to execute. |

Returns `ContextMenu`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`itemButton(int)` {: #itembuttonint data-toc-label="itemButton(int)" }

Returns the button widget for the item at the specified index.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | Zero-based item index. |

Returns `Button`: The button widget instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`itemCount()` {: #itemcount data-toc-label="itemCount()" }

Returns the total number of menu items.

Returns `int`: Total count of items..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`open()` {: #open data-toc-label="open()" }

Opens the menu at its last configured position.

Returns `ContextMenu`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`open(boolean)` {: #openboolean data-toc-label="open(boolean)" }

Sets the open state.

| Parameter | Type | Description |
| --- | --- | --- |
| `open` | `boolean` | Target open state flag. |

Returns `ContextMenu`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`openAt(float, float)` {: #openatfloat-float data-toc-label="openAt(float, float)" }

Opens the menu positioned at the specified root coordinates.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | Horizontal X coordinate. |
| `y` | `float` | Vertical Y coordinate. |

Returns `ContextMenu`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`opened()` {: #opened data-toc-label="opened()" }

Returns true if this menu is open.

Returns `boolean`: True if currently open..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`padding(EdgeInsets)` {: #paddingedgeinsets data-toc-label="padding(EdgeInsets)" }

Sets interior padding around the menu items.

| Parameter | Type | Description |
| --- | --- | --- |
| `padding` | `EdgeInsets` | Internal content padding. |

Returns `ContextMenu`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`selectItem(int)` {: #selectitemint data-toc-label="selectItem(int)" }

Selects the item at the specified index.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | Zero-based item index. |

Returns `ContextMenu`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`selectedItemIndex()` {: #selecteditemindex data-toc-label="selectedItemIndex()" }

Returns the index of the currently hovered or keyboard-selected item.

Returns `int`: Item index..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`separator()` {: #separator data-toc-label="separator()" }

Adds a visual divider separator line.

Returns `ContextMenu`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`separator(Separator)` {: #separatorseparator data-toc-label="separator(Separator)" }

Adds a custom [Separator](<../display/separator.md>) widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `separator` | `Separator` | Visual separator item. |

Returns `ContextMenu`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`submenu(Button, ContextMenu)` {: #submenubutton-contextmenu data-toc-label="submenu(Button, ContextMenu)" }

Adds a cascading submenu item with a custom button.

| Parameter | Type | Description |
| --- | --- | --- |
| `button` | `Button` | Custom button widget to attach. |
| `submenu` | `ContextMenu` | Nested submenu container. |

Returns `ContextMenu`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`submenu(RichText, ContextMenu)` {: #submenurichtext-contextmenu data-toc-label="submenu(RichText, ContextMenu)" }

Adds a cascading submenu item with [RichText](<../../core/text/rich-text.md>).

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `RichText` | Display text string. |
| `submenu` | `ContextMenu` | Nested submenu container. |

Returns `ContextMenu`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`submenu(String, ContextMenu)` {: #submenustring-contextmenu data-toc-label="submenu(String, ContextMenu)" }

Adds a cascading submenu item with plain text.

| Parameter | Type | Description |
| --- | --- | --- |
| `text` | `String` | Display text string. |
| `submenu` | `ContextMenu` | Nested submenu container. |

Returns `ContextMenu`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`toggle(float, float)` {: #togglefloat-float data-toc-label="toggle(float, float)" }

Toggles open/closed state at the specified coordinates.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | Horizontal X coordinate. |
| `y` | `float` | Vertical Y coordinate. |

Returns `ContextMenu`: This widget instance for chaining..

</div>

