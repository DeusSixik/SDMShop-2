---
title: "Menu"
slug: "menu"
category: "Core/Widgets/Navigation"
locale: "en"
order: 0
---

# Menu

Floating popup or dropdown menu widget hosting a vertical stack of menu items, command triggers, and separators.

## Overview

Floating popup or dropdown menu widget hosting a vertical stack of menu items, command triggers, and separators. Provides extensive layout configuration and interaction capabilities.

## Basic Usage

```java
Menu fileMenu = new Menu("File")
    .action("New", this::onNew)
    .separator()
    .action("Exit", this::onExit);
```

## Related links

- [MenuBar](<../../../widgets/navigation/menu-bar.md>): Top-level horizontal menu bar hosting dropdown menus with command binding, keyboard accelerators, and popup coordination.
- [MenuItem](<menu-item.md>): Selectable menu entry supporting regular actions, checkboxes, commands, keyboard shortcuts, or cascading submenus.

## API

<div class="api-member-card" markdown="1">

### `Menu(String)` {: #menustring data-toc-label="Menu(String)" }

Constructs a Menu with the given display label.

| Parameter | Type | Description |
| --- | --- | --- |
| `label` | `String` | The label text. |

</div>

<div class="api-member-card" markdown="1">

### `Menu(String, String)` {: #menustring-string data-toc-label="Menu(String, String)" }

Constructs a Menu with an explicit identifier and display label.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | The unique identifier. |
| `label` | `String` | The label text. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`action(String, Runnable)` {: #actionstring-runnable data-toc-label="action(String, Runnable)" }

Appends an action item with label and callback.

| Parameter | Type | Description |
| --- | --- | --- |
| `label` | `String` | The label text. |
| `action` | `Runnable` | The action callback. |

Returns `Menu`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`clear()` {: #clear data-toc-label="clear()" }

Clears all items from this menu.

Returns `Menu`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`command(String)` {: #commandstring data-toc-label="command(String)" }

Appends an editor command item by command identifier.

| Parameter | Type | Description |
| --- | --- | --- |
| `commandId` | `String` | The command identifier. |

Returns `Menu`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`command(String, String)` {: #commandstring-string data-toc-label="command(String, String)" }

Appends an editor command item with custom display label.

| Parameter | Type | Description |
| --- | --- | --- |
| `commandId` | `String` | The command identifier. |
| `label` | `String` | The label text. |

Returns `Menu`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`id()` {: #id data-toc-label="id()" }

Returns the menu identifier string.

Returns `String`: The unique identifier string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`id(String)` {: #idstring data-toc-label="id(String)" }

Sets the menu identifier string.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | The unique identifier. |

Returns `Menu`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`item(MenuItem)` {: #itemmenuitem data-toc-label="item(MenuItem)" }

Appends a [MenuItem](<menu-item.md>) to this menu.

| Parameter | Type | Description |
| --- | --- | --- |
| `item` | `MenuItem` | The target item. |

Returns `Menu`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`items()` {: #items data-toc-label="items()" }

Returns an unmodifiable list of menu items.

Returns `List`: An unmodifiable list of node items..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`label()` {: #label data-toc-label="label()" }

Returns the menu display label.

Returns `String`: The label string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`label(String)` {: #labelstring data-toc-label="label(String)" }

Sets the menu display label.

| Parameter | Type | Description |
| --- | --- | --- |
| `label` | `String` | The label text. |

Returns `Menu`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`separator()` {: #separator data-toc-label="separator()" }

Appends a visual separator divider.

Returns `Menu`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Menu::</span>`submenu(Menu)` {: #submenumenu data-toc-label="submenu(Menu)" }

Appends a cascading nested submenu.

| Parameter | Type | Description |
| --- | --- | --- |
| `submenu` | `Menu` | The child submenu. |

Returns `Menu`: This instance for chaining..

</div>

