---
title: "MenuItem"
slug: "menu-item"
category: "Core/Widgets/Navigation"
locale: "en"
order: 0
---

# MenuItem

Selectable menu entry supporting regular actions, checkboxes, commands, keyboard shortcuts, or cascading submenus.

## Overview

Selectable menu entry supporting regular actions, checkboxes, commands, keyboard shortcuts, or cascading submenus. Provides extensive layout configuration and interaction capabilities.

## Basic Usage

```java
MenuItem item = MenuItem.action("Save", this::save)
    .shortcutText("Ctrl+S");
```

## Related links

- [Menu](<menu.md>): Floating popup or dropdown menu widget hosting a vertical stack of menu items, command triggers, and separators.
- [MenuBar](<../../../widgets/navigation/menu-bar.md>): Top-level horizontal menu bar hosting dropdown menus with command binding, keyboard accelerators, and popup coordination.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`action()` {: #action data-toc-label="action()" }

Returns the callback runnable executed when this item is clicked.

Returns `Runnable`: The item action callback..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`action(Runnable)` {: #actionrunnable data-toc-label="action(Runnable)" }

Sets the callback runnable executed when this item is clicked.

| Parameter | Type | Description |
| --- | --- | --- |
| `action` | `Runnable` | The action callback. |

Returns `MenuItem`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`action(String, Runnable)` {: #actionstring-runnable data-toc-label="action(String, Runnable)" }

Creates an action menu item with display label and click callback.

| Parameter | Type | Description |
| --- | --- | --- |
| `label` | `String` | The label text. |
| `action` | `Runnable` | The action callback. |

Returns `MenuItem`: The created MenuItem instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`checked()` {: #checked data-toc-label="checked()" }

Returns true if this item displays a checkmark indicator.

Returns `boolean`: True if checked; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`checked(boolean)` {: #checkedboolean data-toc-label="checked(boolean)" }

Sets whether this item displays a checkmark indicator.

| Parameter | Type | Description |
| --- | --- | --- |
| `checked` | `boolean` | The checked state. |

Returns `MenuItem`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`command(String)` {: #commandstring data-toc-label="command(String)" }

Creates a command menu item linked to an editor command identifier.

| Parameter | Type | Description |
| --- | --- | --- |
| `commandId` | `String` | The command identifier. |

Returns `MenuItem`: The created MenuItem instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`command(String, String)` {: #commandstring-string data-toc-label="command(String, String)" }

Creates a command menu item with an explicit custom label.

| Parameter | Type | Description |
| --- | --- | --- |
| `commandId` | `String` | The command identifier. |
| `label` | `String` | The label text. |

Returns `MenuItem`: The created MenuItem instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`commandId()` {: #commandid data-toc-label="commandId()" }

Returns the linked editor command identifier.

Returns `String`: The result value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`commandId(String)` {: #commandidstring data-toc-label="commandId(String)" }

Sets the linked editor command identifier.

| Parameter | Type | Description |
| --- | --- | --- |
| `commandId` | `String` | The command identifier. |

Returns `MenuItem`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`enabled()` {: #enabled data-toc-label="enabled()" }

Returns true if this item is interactive and clickable.

Returns `boolean`: True if enabled; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`enabled(boolean)` {: #enabledboolean data-toc-label="enabled(boolean)" }

Sets whether this item is interactive and clickable.

| Parameter | Type | Description |
| --- | --- | --- |
| `enabled` | `boolean` | Whether the feature or item is enabled. |

Returns `MenuItem`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`kind()` {: #kind data-toc-label="kind()" }

Returns the kind of this menu item (ACTION, COMMAND, SEPARATOR, SUBMENU).

Returns `Kind`: The menu item kind..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`label()` {: #label data-toc-label="label()" }

Returns the item display label text.

Returns `String`: The label string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`label(String)` {: #labelstring data-toc-label="label(String)" }

Sets the item display label text.

| Parameter | Type | Description |
| --- | --- | --- |
| `label` | `String` | The label text. |

Returns `MenuItem`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`menu(Menu)` {: #menumenu data-toc-label="menu(Menu)" }

Sets the nested submenu container.

| Parameter | Type | Description |
| --- | --- | --- |
| `submenu` | `Menu` | The child submenu. |

Returns `MenuItem`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`separator()` {: #separator data-toc-label="separator()" }

Creates a visual divider separator item.

Returns `MenuItem`: The created MenuItem instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`shortcutText()` {: #shortcuttext data-toc-label="shortcutText()" }

Returns the shortcut key combination display text.

Returns `String`: The shortcut accelerator string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`shortcutText(String)` {: #shortcuttextstring data-toc-label="shortcutText(String)" }

Sets the shortcut key combination display text.

| Parameter | Type | Description |
| --- | --- | --- |
| `shortcutText` | `String` | The shortcut label text. |

Returns `MenuItem`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`submenu()` {: #submenu data-toc-label="submenu()" }

Returns the nested submenu, or null if not a submenu item.

Returns `Menu`: The child submenu, or null if none..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">MenuItem::</span>`submenu(Menu)` {: #submenumenu data-toc-label="submenu(Menu)" }

Creates a cascading submenu item.

| Parameter | Type | Description |
| --- | --- | --- |
| `submenu` | `Menu` | The child submenu. |

Returns `MenuItem`: The created MenuItem instance..

</div>

