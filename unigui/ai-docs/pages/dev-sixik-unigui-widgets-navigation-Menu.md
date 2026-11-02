# `Menu`

Hierarchical menu model containing actions, commands, submenus, and separators for MenuBar.

### API

- `Menu(String)` — Constructs a Menu with the given display label.
- `Menu(String, String)` — Constructs a Menu with an explicit identifier and display label.
- `action(String, Runnable)` → Menu — Appends an action item with label and callback.
- `clear()` → Menu — Clears all items from this menu.
- `command(String)` → Menu — Appends an editor command item by command identifier.
- `command(String, String)` → Menu — Appends an editor command item with custom display label.
- `id()` → String — Returns the menu identifier string.
- `id(String)` → Menu — Sets the menu identifier string.
- `item(MenuItem)` → Menu — Appends a MenuItem to this menu.
- `items()` → List<MenuItem> — Returns an unmodifiable list of menu items.
- `label()` → String — Returns the menu display label.
- `label(String)` → Menu — Sets the menu display label.
- `separator()` → Menu — Appends a visual separator divider.
- `submenu(Menu)` → Menu — Appends a cascading nested submenu.

