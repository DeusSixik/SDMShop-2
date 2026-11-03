---
title: "MenuBar"
slug: "menu-bar"
category: "Widgets/Navigation"
locale: "en"
order: 0
---

# MenuBar

Top-level horizontal menu bar hosting dropdown menus with command binding, keyboard accelerators, and popup coordination.

## Overview

Top-level horizontal menu bar hosting dropdown menus with command binding, keyboard accelerators, and popup coordination. Provides extensive layout configuration and interaction capabilities.

## Basic Usage

```java
MenuBar menuBar = new MenuBar();
menuBar.menu(fileMenu).menu(editMenu);
```

## Related links

- [Menu](<../../core/widgets/navigation/menu.md>): Floating popup or dropdown menu widget hosting a vertical stack of menu items, command triggers, and separators.
- [MenuItem](<../../core/widgets/navigation/menu-item.md>): Selectable menu entry supporting regular actions, checkboxes, commands, keyboard shortcuts, or cascading submenus.

## API

<div class="api-member-card" markdown="1">

### `MenuBar()` {: #menubar data-toc-label="MenuBar()" }

Constructs an empty top-level MenuBar.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`clearMenus()` {: #clearmenus data-toc-label="clearMenus()" }

Removes all menus from the menu bar.

Returns `MenuBar`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`closeOpenMenu()` {: #closeopenmenu data-toc-label="closeOpenMenu()" }

Closes the active dropdown menu.

Returns `MenuBar`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`commandManager()` {: #commandmanager data-toc-label="commandManager()" }

Returns the CommandManager used to resolve commands and shortcut bindings.

Returns `CommandManager`: The associated CommandManager..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`commandManager(CommandManager)` {: #commandmanagercommandmanager data-toc-label="commandManager(CommandManager)" }

Sets the CommandManager used to execute menu commands.

| Parameter | Type | Description |
| --- | --- | --- |
| `commandManager` | `CommandManager` | The CommandManager instance. |

Returns `MenuBar`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`menu(Menu)` {: #menumenu data-toc-label="menu(Menu)" }

Appends a top-level [Menu](<../../core/widgets/navigation/menu.md>) to the menu bar.

| Parameter | Type | Description |
| --- | --- | --- |
| `menu` | `Menu` | The menu instance. |

Returns `MenuBar`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`menu(String)` {: #menustring data-toc-label="menu(String)" }

Finds a top-level menu by its label, or returns null if not found.

| Parameter | Type | Description |
| --- | --- | --- |
| `label` | `String` | The label text. |

Returns `Menu`: The menu instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`menuCount()` {: #menucount data-toc-label="menuCount()" }

Returns the number of top-level menus in the menu bar.

Returns `int`: The total item count..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`menus()` {: #menus data-toc-label="menus()" }

Returns an unmodifiable list of top-level menus.

Returns `List`: An unmodifiable list of menus..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`menus(List)` {: #menuslist data-toc-label="menus(List)" }

Replaces the entire list of top-level menus.

| Parameter | Type | Description |
| --- | --- | --- |
| `menus` | `List` | The list of menus. |

Returns `MenuBar`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`openMenu(int)` {: #openmenuint data-toc-label="openMenu(int)" }

Opens the dropdown menu at the specified index.

| Parameter | Type | Description |
| --- | --- | --- |
| `index` | `int` | The index position. |

Returns `MenuBar`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`openedMenuIndex()` {: #openedmenuindex data-toc-label="openedMenuIndex()" }

Returns the index of the currently opened dropdown menu, or -1 if closed.

Returns `int`: The result value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuBar::</span>`openedPopup()` {: #openedpopup data-toc-label="openedPopup()" }

Returns the active dropdown [ContextMenu](<../feedback/context-menu.md>) popup widget, or null if closed.

Returns `ContextMenu`: The floating popup container..

</div>

