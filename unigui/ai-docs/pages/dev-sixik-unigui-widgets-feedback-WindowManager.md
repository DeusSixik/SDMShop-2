# `WindowManager`

Overlay-scoped controller managing floating WindowWidget instances, modal stacks, activation, and Z-ordering.

### API

- `activate(WindowWidget)` → WindowManager — Activates the specified window, brings it to front, and grants keyboard focus.
- `activeWindow()` → WindowWidget — Returns the currently active focused WindowWidget, or null if none.
- `bringToFront(WindowWidget)` → WindowManager — Brings the specified window to the top of its Z-order layer.
- `host()` → OverlayLayer — Returns the host OverlayLayer containing the managed floating windows.
- `modalStackDepth()` → int — Returns the current number of open modal windows in the stack.
- `register(WindowWidget)` → WindowManager — Registers a window with this manager and associates it with the overlay host.
- `registered(WindowWidget)` → boolean — Checks whether the specified window is registered in this manager.
- `topModalWindow()` → WindowWidget — Returns the topmost open modal window, or null if no modal windows are open.
- `unregister(WindowWidget)` → WindowManager — Unregisters a window and activates the next topmost window if needed.
- `windows()` → List<WindowWidget> — Returns an unmodifiable list of all registered window instances.

