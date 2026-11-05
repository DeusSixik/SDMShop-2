# `ToolBar` (extends `LinearBox`)

Compact command toolbar with grouped buttons, separators, and command manager integration.

### API

- `ToolBar()` — Constructs a horizontal ToolBar instance with default 26px height and spacing.
- `button(ToolButton)` → ToolBar — Adds a ToolButton to the toolbar and binds it to the command manager if configured.
- `command(String)` → ToolButton — Adds a command button by ID using text-only display mode.
- `command(String, String, DisplayMode)` → ToolButton — Adds a command button with command ID, icon, and display mode.
- `commandManager()` → CommandManager — Returns the CommandManager associated with this toolbar.
- `commandManager(CommandManager)` → ToolBar — Sets the CommandManager used by toolbar command buttons.
- `iconCommand(String, String)` → IconButton — Adds an icon-only command button bound to the command manager.
- `refreshCommandButtons()` → ToolBar — Refreshes labels, enabled state, and checked state for all command buttons.
- `separator()` → ToolBar — Appends a vertical separator dividing button groups.
- `spacer()` → ToolBar — Appends a flexible expanding spacer pushing trailing buttons to the right.
- `toggleCommand(String, String, DisplayMode)` → ToggleToolButton — Adds a toggleable tool button bound to a command.
- `widget(Widget)` → ToolBar — Appends an arbitrary custom widget to the toolbar.

