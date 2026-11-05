# `MenuItem`

Menu item model representing an action, command, visual separator, or cascading submenu.

### API

- `action()` → Runnable — Returns the callback runnable executed when this item is clicked.
- `action(Runnable)` → MenuItem — Sets the callback runnable executed when this item is clicked.
- `action(String, Runnable)` → MenuItem — Creates an action menu item with display label and click callback.
- `checked()` → boolean — Returns true if this item displays a checkmark indicator.
- `checked(boolean)` → MenuItem — Sets whether this item displays a checkmark indicator.
- `command(String)` → MenuItem — Creates a command menu item linked to an editor command identifier.
- `command(String, String)` → MenuItem — Creates a command menu item with an explicit custom label.
- `commandId()` → String — Returns the linked editor command identifier.
- `commandId(String)` → MenuItem — Sets the linked editor command identifier.
- `enabled()` → boolean — Returns true if this item is interactive and clickable.
- `enabled(boolean)` → MenuItem — Sets whether this item is interactive and clickable.
- `kind()` → Kind — Returns the kind of this menu item (ACTION, COMMAND, SEPARATOR, SUBMENU).
- `label()` → String — Returns the item display label text.
- `label(String)` → MenuItem — Sets the item display label text.
- `menu(Menu)` → MenuItem — Sets the nested submenu container.
- `separator()` → MenuItem — Creates a visual divider separator item.
- `shortcutText()` → String — Returns the shortcut key combination display text.
- `shortcutText(String)` → MenuItem — Sets the shortcut key combination display text.
- `submenu()` → Menu — Returns the nested submenu, or null if not a submenu item.
- `submenu(Menu)` → MenuItem — Creates a cascading submenu item.

