# `MenuBar` (extends `LinearBox`)

Desktop-style top menu bar displaying menus, keyboard shortcuts, command execution, and popup dropdowns.

### API

- `MenuBar()` — Constructs an empty top-level MenuBar.
- `clearMenus()` → MenuBar — Removes all menus from the menu bar.
- `closeOpenMenu()` → MenuBar — Closes the active dropdown menu.
- `commandManager()` → CommandManager — Returns the CommandManager used to resolve commands and shortcut bindings.
- `commandManager(CommandManager)` → MenuBar — Sets the CommandManager used to execute menu commands.
- `menu(Menu)` → MenuBar — Appends a top-level Menu to the menu bar.
- `menu(String)` → Menu — Finds a top-level menu by its label, or returns null if not found.
- `menuCount()` → int — Returns the number of top-level menus in the menu bar.
- `menus()` → List<Menu> — Returns an unmodifiable list of top-level menus.
- `menus(List<Menu>)` → MenuBar — Replaces the entire list of top-level menus.
- `openMenu(int)` → MenuBar — Opens the dropdown menu at the specified index.
- `openedMenuIndex()` → int — Returns the index of the currently opened dropdown menu, or -1 if closed.
- `openedPopup()` → ContextMenu — Returns the active dropdown ContextMenu popup widget, or null if closed.

