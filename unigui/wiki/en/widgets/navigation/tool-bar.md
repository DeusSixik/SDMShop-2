---
title: "ToolBar"
slug: "tool-bar"
category: "Widgets/Navigation"
locale: "en"
order: 0
---

# ToolBar

Compact command toolbar with grouped buttons, separators, and command manager integration.

## Overview

Compact command toolbar with grouped buttons, separators, and command manager integration. Provides extensive layout configuration and interaction capabilities.

## Basic Usage

```java
ToolBar toolbar = new ToolBar();
toolbar.command("edit.undo", "icon:undo", ToolButton.DisplayMode.ICON_ONLY);
toolbar.separator();
toolbar.command("edit.redo", "icon:redo", ToolButton.DisplayMode.ICON_ONLY);
```

## Related links

- [MenuBar](<menu-bar.md>): Top-level horizontal menu bar hosting dropdown menus with command binding, keyboard accelerators, and popup coordination.

## API

<div class="api-member-card" markdown="1">

### `ToolBar()` {: #toolbar data-toc-label="ToolBar()" }

Constructs a horizontal ToolBar instance with default 26px height and spacing.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`button(ToolButton)` {: #buttontoolbutton data-toc-label="button(ToolButton)" }

Adds a [ToolButton](<../interaction/tool-button.md>) to the toolbar and binds it to the command manager if configured.

| Parameter | Type | Description |
| --- | --- | --- |
| `button` | `ToolButton` | The button instance. |

Returns `ToolBar`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`command(String)` {: #commandstring data-toc-label="command(String)" }

Adds a command button by ID using text-only display mode.

| Parameter | Type | Description |
| --- | --- | --- |
| `commandId` | `String` | The command identifier. |

Returns `ToolButton`: The created or added [ToolButton](<../interaction/tool-button.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`command(String, String, DisplayMode)` {: #commandstring-string-displaymode data-toc-label="command(String, String, DisplayMode)" }

Adds a command button with command ID, icon, and display mode.

| Parameter | Type | Description |
| --- | --- | --- |
| `commandId` | `String` | The command identifier. |
| `icon` | `String` | The icon identifier or handle. |
| `displayMode` | `DisplayMode` | The button display mode. |

Returns `ToolButton`: The created or added [ToolButton](<../interaction/tool-button.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`commandManager()` {: #commandmanager data-toc-label="commandManager()" }

Returns the CommandManager associated with this toolbar.

Returns `CommandManager`: The associated CommandManager..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`commandManager(CommandManager)` {: #commandmanagercommandmanager data-toc-label="commandManager(CommandManager)" }

Sets the CommandManager used by toolbar command buttons.

| Parameter | Type | Description |
| --- | --- | --- |
| `commandManager` | `CommandManager` | The CommandManager instance. |

Returns `ToolBar`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`iconCommand(String, String)` {: #iconcommandstring-string data-toc-label="iconCommand(String, String)" }

Adds an icon-only command button bound to the command manager.

| Parameter | Type | Description |
| --- | --- | --- |
| `commandId` | `String` | The command identifier. |
| `icon` | `String` | The icon identifier or handle. |

Returns `IconButton`: The created or added [IconButton](<../interaction/icon-button.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`refreshCommandButtons()` {: #refreshcommandbuttons data-toc-label="refreshCommandButtons()" }

Refreshes labels, enabled state, and checked state for all command buttons.

Returns `ToolBar`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`separator()` {: #separator data-toc-label="separator()" }

Appends a vertical separator dividing button groups.

Returns `ToolBar`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`spacer()` {: #spacer data-toc-label="spacer()" }

Appends a flexible expanding spacer pushing trailing buttons to the right.

Returns `ToolBar`: This instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`toggleCommand(String, String, DisplayMode)` {: #togglecommandstring-string-displaymode data-toc-label="toggleCommand(String, String, DisplayMode)" }

Adds a toggleable tool button bound to a command.

| Parameter | Type | Description |
| --- | --- | --- |
| `commandId` | `String` | The command identifier. |
| `icon` | `String` | The icon identifier or handle. |
| `displayMode` | `DisplayMode` | The button display mode. |

Returns `ToggleToolButton`: The created or added [ToggleToolButton](<../interaction/toggle-tool-button.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ToolBar::</span>`widget(Widget)` {: #widgetwidget data-toc-label="widget(Widget)" }

Appends an arbitrary custom widget to the toolbar.

| Parameter | Type | Description |
| --- | --- | --- |
| `widget` | `Widget` | The widget component. |

Returns `ToolBar`: This instance for chaining..

</div>

