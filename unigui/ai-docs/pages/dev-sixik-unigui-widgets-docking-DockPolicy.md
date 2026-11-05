# `DockPolicy` (extends `Enum`)

Docking integration policy for windows: FULL (bidirectional docking and hosting), GUEST_ONLY (can dock into hosts but accepts no drops), or DISABLED.

### API

- `acceptsDrops()` → boolean — Returns true if the window can accept dropped panels or foreign windows.
- `canBeDocked()` → boolean — Returns true if the window can be dragged and docked into a host.

