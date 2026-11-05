# `ContextMenu` (extends `Box`, implements `OverlayHostAware`)

Hierarchical popup context menu supporting actions, submenus, separators, and keyboard navigation.

### API

- `ContextMenu()` — Constructs an empty ContextMenu.
- `arrangeInHost(RectView)` → void — Arranges the menu within host boundaries, flipping placement if screen edges are exceeded.
- `close()` → ContextMenu — Closes the menu and any active submenus.
- `closeOnOutsideClick()` → boolean — Returns true if clicking outside automatically dismisses the menu.
- `closeOnOutsideClick(boolean)` → ContextMenu (default: `true`) — Sets whether clicking outside closes the menu.
- `item(Button)` → ContextMenu — Adds a preconfigured Button widget as a menu item.
- `item(RichText)` → ContextMenu — Adds a menu item with RichText.
- `item(RichText, Runnable)` → ContextMenu — Adds a menu item with RichText and click callback.
- `item(String)` → ContextMenu — Adds a menu item with plain text.
- `item(String, Runnable)` → ContextMenu — Adds a menu item with plain text and click callback.
- `itemButton(int)` → Button — Returns the button widget for the item at the specified index.
- `itemCount()` → int — Returns the total number of menu items.
- `open()` → ContextMenu — Opens the menu at its last configured position.
- `open(boolean)` → ContextMenu (default: `false`) — Sets the open state.
- `openAt(float, float)` → ContextMenu — Opens the menu positioned at the specified root coordinates.
- `opened()` → boolean — Returns true if this menu is open.
- `padding(EdgeInsets)` → ContextMenu (default: `3`) — Sets interior padding around the menu items.
- `selectItem(int)` → ContextMenu (default: `0`) — Selects the item at the specified index.
- `selectedItemIndex()` → int — Returns the index of the currently hovered or keyboard-selected item.
- `separator()` → ContextMenu — Adds a visual divider separator line.
- `separator(Separator)` → ContextMenu — Adds a custom Separator widget.
- `submenu(Button, ContextMenu)` → ContextMenu — Adds a cascading submenu item with a custom button.
- `submenu(RichText, ContextMenu)` → ContextMenu — Adds a cascading submenu item with RichText.
- `submenu(String, ContextMenu)` → ContextMenu — Adds a cascading submenu item with plain text.
- `toggle(float, float)` → ContextMenu — Toggles open/closed state at the specified coordinates.

