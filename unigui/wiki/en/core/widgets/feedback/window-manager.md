---
title: "WindowManager"
slug: "window-manager"
category: "Core/Widgets/Feedback"
locale: "en"
order: 0
---

# WindowManager

Manager controlling floating window z-order, focus stack, active states, and desktop window arrangements.

## Overview

`WindowManager` controls the lifecycle, z-ordering, focus stacking, and spatial positioning of floating desktop windows inside an `OverlayLayer`.

Key responsibilities:
- Window registration, activation, and focus management.
- Z-order stacking with `bringToFront` and `sendToBack`.
- Modal dialog stacking, preventing interaction with lower windows until the modal dialog is dismissed.
- Active window queries and window hierarchy iteration.

## Managing Floating Windows

```java
WindowManager manager = overlayLayer.windows();
manager.activate(window);
```

## Related links

- [OverlayLayer](<../../../widgets/feedback/overlay-layer.md>): Full-screen overlay root managing modal backdrops, dialogs, popups, and layer-based hit testing.
- [WindowWidget](<../../../widgets/feedback/window-widget.md>): Desktop-style floating window widget supporting title bar, dragging, resizing, collapse, modal dialogs, and docking integrations.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowManager::</span>`activate(WindowWidget)` {: #activatewindowwidget data-toc-label="activate(WindowWidget)" }

Activates the specified window, brings it to front, and grants keyboard focus.

| Parameter | Type | Description |
| --- | --- | --- |
| `window` | `WindowWidget` | Window widget instance. |

Returns `WindowManager`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowManager::</span>`activeWindow()` {: #activewindow data-toc-label="activeWindow()" }

Returns the currently active focused [WindowWidget](<../../../widgets/feedback/window-widget.md>), or null if none.

Returns `WindowWidget`: The currently active focused window, or null if none..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowManager::</span>`bringToFront(WindowWidget)` {: #bringtofrontwindowwidget data-toc-label="bringToFront(WindowWidget)" }

Brings the specified window to the top of its Z-order layer.

| Parameter | Type | Description |
| --- | --- | --- |
| `window` | `WindowWidget` | Window widget instance. |

Returns `WindowManager`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowManager::</span>`host()` {: #host data-toc-label="host()" }

Returns the host [OverlayLayer](<../../../widgets/feedback/overlay-layer.md>) containing the managed floating windows.

Returns `OverlayLayer`: The host [OverlayLayer](<../../../widgets/feedback/overlay-layer.md>) containing managed windows..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowManager::</span>`modalStackDepth()` {: #modalstackdepth data-toc-label="modalStackDepth()" }

Returns the current number of open modal windows in the stack.

Returns `int`: The number of active modal dialogs in the modal stack..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowManager::</span>`register(WindowWidget)` {: #registerwindowwidget data-toc-label="register(WindowWidget)" }

Registers a window with this manager and associates it with the overlay host.

| Parameter | Type | Description |
| --- | --- | --- |
| `window` | `WindowWidget` | Window widget instance. |

Returns `WindowManager`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowManager::</span>`registered(WindowWidget)` {: #registeredwindowwidget data-toc-label="registered(WindowWidget)" }

Checks whether the specified window is registered in this manager.

| Parameter | Type | Description |
| --- | --- | --- |
| `window` | `WindowWidget` | Window widget instance. |

Returns `boolean`: True if the window is currently registered with this manager..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowManager::</span>`topModalWindow()` {: #topmodalwindow data-toc-label="topModalWindow()" }

Returns the topmost open modal window, or null if no modal windows are open.

Returns `WindowWidget`: The topmost active modal window, or null if no modal dialog is open..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowManager::</span>`unregister(WindowWidget)` {: #unregisterwindowwidget data-toc-label="unregister(WindowWidget)" }

Unregisters a window and activates the next topmost window if needed.

| Parameter | Type | Description |
| --- | --- | --- |
| `window` | `WindowWidget` | Window widget instance. |

Returns `WindowManager`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowManager::</span>`windows()` {: #windows data-toc-label="windows()" }

Returns an unmodifiable list of all registered window instances.

Returns `List`: Unmodifiable list of all registered window widgets..

</div>

