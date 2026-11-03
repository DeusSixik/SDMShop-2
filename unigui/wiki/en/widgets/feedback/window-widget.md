---
title: "WindowWidget"
slug: "window-widget"
category: "Widgets/Feedback"
locale: "en"
order: 0
---

# WindowWidget

Desktop-style floating window widget supporting title bar, dragging, resizing, collapse, modal dialogs, and docking integrations.

## Overview

`WindowWidget` is a full-featured desktop-style floating window container with title bar, window actions, dragging, resizing, collapse, modal dialogs, and docking system integrations.

Key capabilities:
- Title bar with plain-text or [RichText](<../../core/text/rich-text.md>) titles, icons, and action buttons (minimize, collapse, close).
- 8-direction and corner resizing with configurable minimum size and resize grips.
- Boundary snapping and host constraints (`constrainToHost`).
- Modal mode support with background scrim dimming.
- Seamless docking integration with `DockingRoot` and `DockPane` docking trees.

## Creating and Positioning Window [Widget](<../widget.md>)

```java
WindowWidget window = new WindowWidget("Settings", settingsForm);
window.position(100, 100).open();
```

## Related links

- [DockingRoot](<../docking/docking-root.md>): Root visual widget for the IDE docking system hosting dock tree nodes, tab strips, split handles, drop previews, and tear-off floating window integrations.
- [OverlayLayer](<overlay-layer.md>): Full-screen overlay root managing modal backdrops, dialogs, popups, and layer-based hit testing.
- [WindowManager](<../../core/widgets/feedback/window-manager.md>): Manager controlling floating window z-order, focus stack, active states, and desktop window arrangements.

## API

<div class="api-member-card" markdown="1">

### `WindowWidget()` {: #windowwidget data-toc-label="WindowWidget()" }

Constructs an empty WindowWidget with default title and dimensions.

</div>

<div class="api-member-card" markdown="1">

### `WindowWidget(RichText, Widget)` {: #windowwidgetrichtext-widget data-toc-label="WindowWidget(RichText, Widget)" }

Constructs a WindowWidget with a [RichText](<../../core/text/rich-text.md>) formatted title and hosted content.

| Parameter | Type | Description |
| --- | --- | --- |
| `title` | `RichText` | [RichText](<../../core/text/rich-text.md>) formatted title component. |
| `content` | `Widget` | Content widget hosted inside. |

</div>

<div class="api-member-card" markdown="1">

### `WindowWidget(String, Widget)` {: #windowwidgetstring-widget data-toc-label="WindowWidget(String, Widget)" }

Constructs a WindowWidget with a plain-text title and hosted content widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `title` | `String` | Title string. |
| `content` | `Widget` | Content widget hosted inside. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`acceptsDocking()` {: #acceptsdocking data-toc-label="acceptsDocking()" }

Returns true if this window accepts foreign panes dropped into it.

Returns `boolean`: True if this window accepts dragged dock panes..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`active()` {: #active data-toc-label="active()" }

Returns true if this window currently holds focus in its [WindowManager](<../../core/widgets/feedback/window-manager.md>).

Returns `boolean`: True if this window is currently focused and active..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`arrangeInHost(RectView)` {: #arrangeinhostrectview data-toc-label="arrangeInHost(RectView)" }

Positions the window inside the host boundaries.

| Parameter | Type | Description |
| --- | --- | --- |
| `bounds` | `RectView` | Available host rectangular boundary. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`captionless()` {: #captionless data-toc-label="captionless()" }

Returns true if the window renders without a title bar header.

Returns `boolean`: True if the window has no header bar..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`close()` {: #close data-toc-label="close()" }

Closes the window.

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`closeButton()` {: #closebutton data-toc-label="closeButton()" }

Returns the title bar close button widget.

Returns `Button`: The title bar button instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`closeButtonVisible()` {: #closebuttonvisible data-toc-label="closeButtonVisible()" }

Returns true if the title bar close button is visible.

Returns `boolean`: True if the close button is visible..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`closeButtonVisible(boolean)` {: #closebuttonvisibleboolean data-toc-label="closeButtonVisible(boolean)" }

Sets the visibility of the close button.

| Parameter | Type | Description |
| --- | --- | --- |
| `closeButtonVisible` | `boolean` | Whether the title bar close button is visible. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`closeOnOutsideClick()` {: #closeonoutsideclick data-toc-label="closeOnOutsideClick()" }

Returns true if clicking outside dismisses this window.

Returns `boolean`: True if closing on outside click is enabled..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`closeOnOutsideClick(boolean)` {: #closeonoutsideclickboolean data-toc-label="closeOnOutsideClick(boolean)" }

Configures whether clicking outside closes the window.

| Parameter | Type | Description |
| --- | --- | --- |
| `closeOnOutsideClick` | `boolean` | Whether clicking outside automatically dismisses the widget. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`collapseButtonVisible()` {: #collapsebuttonvisible data-toc-label="collapseButtonVisible()" }

Returns true if the collapse toggle button is shown in the header.

Returns `boolean`: True if the collapse button is visible..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`collapseButtonVisible(boolean)` {: #collapsebuttonvisibleboolean data-toc-label="collapseButtonVisible(boolean)" }

Sets the visibility of the header collapse button.

| Parameter | Type | Description |
| --- | --- | --- |
| `visible` | `boolean` | Target visibility flag. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`collapsed()` {: #collapsed data-toc-label="collapsed()" }

Returns true if the window is currently collapsed to header-only.

Returns `boolean`: True if the window is currently collapsed..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`collapsed(boolean)` {: #collapsedboolean data-toc-label="collapsed(boolean)" }

Sets whether the window is collapsed.

| Parameter | Type | Description |
| --- | --- | --- |
| `collapsed` | `boolean` | Whether the window is collapsed to its header bar. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`commitDockHost()` {: #commitdockhost data-toc-label="commitDockHost()" }

Commits an auto-created docking host, making it permanent.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`constrainToHost()` {: #constraintohost data-toc-label="constrainToHost()" }

Returns true if window coordinates are clamped within the host boundary.

Returns `boolean`: True if window movement is constrained inside host boundaries..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`constrainToHost(boolean)` {: #constraintohostboolean data-toc-label="constrainToHost(boolean)" }

Configures host boundary clamping.

| Parameter | Type | Description |
| --- | --- | --- |
| `constrainToHost` | `boolean` | Whether window movement is strictly constrained inside host bounds. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`content()` {: #content data-toc-label="content()" }

Returns the hosted content widget.

Returns `Widget`: The hosted content widget..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`content(Widget)` {: #contentwidget data-toc-label="content(Widget)" }

Sets the hosted content widget.

| Parameter | Type | Description |
| --- | --- | --- |
| `content` | `Widget` | Content widget hosted inside. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockDocument(String, String, Widget)` {: #dockdocumentstring-string-widget data-toc-label="dockDocument(String, String, Widget)" }

Docks a document pane into this window's docking host.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique string identifier. |
| `title` | `String` | Title string. |
| `content` | `Widget` | Content widget hosted inside. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockKind()` {: #dockkind data-toc-label="dockKind()" }

Returns the classification kind (DOCUMENT or TOOL) when docked.

Returns `DockPaneKind`: The dock pane kind configured for this window..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockKind(DockPaneKind)` {: #dockkinddockpanekind data-toc-label="dockKind(DockPaneKind)" }

Sets the docking classification kind.

| Parameter | Type | Description |
| --- | --- | --- |
| `dockKind` | `DockPaneKind` | Kind of the source dock pane. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockPane(DockPane)` {: #dockpanedockpane data-toc-label="dockPane(DockPane)" }

Docks a pane into this window's embedded docking host.

| Parameter | Type | Description |
| --- | --- | --- |
| `pane` | `DockPane` | Dock pane instance. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockPaneId()` {: #dockpaneid data-toc-label="dockPaneId()" }

Returns the pane ID representing this window when docked.

Returns `String`: The string value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockPaneId(String)` {: #dockpaneidstring data-toc-label="dockPaneId(String)" }

Sets the pane ID representing this window when docked.

| Parameter | Type | Description |
| --- | --- | --- |
| `dockPaneId` | `String` | Identifier of the source dock pane. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockPolicy()` {: #dockpolicy data-toc-label="dockPolicy()" }

Returns the docking policy (FULL, GUEST_ONLY, DISABLED).

Returns `DockPolicy`: The docking policy configured for this window..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockPolicy(DockPolicy)` {: #dockpolicydockpolicy data-toc-label="dockPolicy(DockPolicy)" }

Sets the docking integration policy.

| Parameter | Type | Description |
| --- | --- | --- |
| `dockPolicy` | `DockPolicy` | Docking placement policy. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockRedockLocked()` {: #dockredocklocked data-toc-label="dockRedockLocked()" }

Returns true if redocking this window into other hosts is locked.

Returns `boolean`: True if redocking into the docking root is locked..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockRedockLocked(boolean)` {: #dockredocklockedboolean data-toc-label="dockRedockLocked(boolean)" }

Locks or unlocks redocking capability for this window.

| Parameter | Type | Description |
| --- | --- | --- |
| `dockRedockLocked` | `boolean` | Whether redocking back into the dock root is locked. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockSourceId()` {: #docksourceid data-toc-label="dockSourceId()" }

Returns the source docking host ID if this window was torn off from a dock.

Returns `String`: The identifier of the source dock pane..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockSourcePane()` {: #docksourcepane data-toc-label="dockSourcePane()" }

Returns the source [DockPane](<../../core/widgets/docking/dock-pane.md>) representation when floating.

Returns `DockPane`: The source dock pane associated with this window..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockSourcePane(DockPane)` {: #docksourcepanedockpane data-toc-label="dockSourcePane(DockPane)" }

Sets the source [DockPane](<../../core/widgets/docking/dock-pane.md>) representation.

| Parameter | Type | Description |
| --- | --- | --- |
| `pane` | `DockPane` | Dock pane instance. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockTool(String, String, Widget)` {: #docktoolstring-string-widget data-toc-label="dockTool(String, String, Widget)" }

Docks a tool pane into this window's docking host.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique string identifier. |
| `title` | `String` | Title string. |
| `content` | `Widget` | Content widget hosted inside. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockable()` {: #dockable data-toc-label="dockable()" }

Returns true if this window can be docked into an external host.

Returns `boolean`: True if the window can be docked..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockable(boolean)` {: #dockableboolean data-toc-label="dockable(boolean)" }

Enables or disables docking capability.

| Parameter | Type | Description |
| --- | --- | --- |
| `dockable` | `boolean` | Whether the window can be docked into a docking container. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockingEnabled()` {: #dockingenabled data-toc-label="dockingEnabled()" }

Returns true if docking features are actively enabled.

Returns `boolean`: True if docking features are enabled..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockingEnabled(boolean)` {: #dockingenabledboolean data-toc-label="dockingEnabled(boolean)" }

Enables or disables docking integration.

| Parameter | Type | Description |
| --- | --- | --- |
| `dockingEnabled` | `boolean` | Whether docking interactions are enabled for this window. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dockingRoot()` {: #dockingroot data-toc-label="dockingRoot()" }

Returns the internal [DockingRoot](<../docking/docking-root.md>) hosted inside this window, or null.

Returns `DockingRoot`: The internal [DockingRoot](<../docking/docking-root.md>) instance hosting dock panes for this window..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`draggable()` {: #draggable data-toc-label="draggable()" }

Returns true if window dragging by the header is enabled.

Returns `boolean`: True if the window can be dragged by its header..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`draggable(boolean)` {: #draggableboolean data-toc-label="draggable(boolean)" }

Enables or disables window dragging.

| Parameter | Type | Description |
| --- | --- | --- |
| `draggable` | `boolean` | Whether the window can be moved by dragging its header. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`dragging()` {: #dragging data-toc-label="dragging()" }

Returns true if the window is currently being moved.

Returns `boolean`: True if the window is currently being dragged..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`ensureDockHost()` {: #ensuredockhost data-toc-label="ensureDockHost()" }

Initializes and returns an embedded [DockingRoot](<../docking/docking-root.md>) instance as content.

Returns `DockingRoot`: The internal [DockingRoot](<../docking/docking-root.md>) instance hosting dock panes for this window..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`fixedModal()` {: #fixedmodal data-toc-label="fixedModal()" }

Returns true if the modal window is fixed and cannot be dragged.

Returns `boolean`: True if the modal window position is fixed in place..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`fixedModal(boolean)` {: #fixedmodalboolean data-toc-label="fixedModal(boolean)" }

Configures whether this modal window has a fixed non-draggable position.

| Parameter | Type | Description |
| --- | --- | --- |
| `fixedModal` | `boolean` | Whether the modal dialog position is locked in place. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`headerColor()` {: #headercolor data-toc-label="headerColor()" }

Returns the mutable background color of the title bar header.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`headerHeight()` {: #headerheight data-toc-label="headerHeight()" }

Returns the title bar header height in UI pixels.

Returns `float`: The title bar header height..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`headerHeight(float)` {: #headerheightfloat data-toc-label="headerHeight(float)" }

Sets the title bar header height in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `headerHeight` | `float` | Height of the title bar header in pixels. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`headerSeparatorColor()` {: #headerseparatorcolor data-toc-label="headerSeparatorColor()" }

Returns the mutable separator line color between header and content.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`icon()` {: #icon data-toc-label="icon()" }

Returns the title bar icon.

Returns `UiIcon`: The window title bar icon..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`icon(UiIcon)` {: #iconuiicon data-toc-label="icon(UiIcon)" }

Sets the title bar icon.

| Parameter | Type | Description |
| --- | --- | --- |
| `icon` | `UiIcon` | Icon displayed in the window title bar. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`lastDragRootX()` {: #lastdragrootx data-toc-label="lastDragRootX()" }

Returns the last pointer root X coordinate during dragging.

Returns `float`: The window coordinate position..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`lastDragRootY()` {: #lastdragrooty data-toc-label="lastDragRootY()" }

Returns the last pointer root Y coordinate during dragging.

Returns `float`: The window coordinate position..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`mergeDockingFrom(WindowWidget)` {: #mergedockingfromwindowwidget data-toc-label="mergeDockingFrom(WindowWidget)" }

Merges docking panes from another window into this window's dock host.

| Parameter | Type | Description |
| --- | --- | --- |
| `source` | `WindowWidget` | Parameter value for source. |

Returns `boolean`: True if docking state was successfully merged..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`minWindowSize(float, float)` {: #minwindowsizefloat-float data-toc-label="minWindowSize(float, float)" }

Sets minimum width and height limits for window resizing.

| Parameter | Type | Description |
| --- | --- | --- |
| `width` | `float` | Width dimension in pixels. |
| `height` | `float` | Height dimension in pixels. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`modal()` {: #modal data-toc-label="modal()" }

Returns true if the window is modal, blocking interaction with windows below.

Returns `boolean`: True if the window is in modal mode..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`modal(boolean)` {: #modalboolean data-toc-label="modal(boolean)" }

Configures modal behavior for this window.

| Parameter | Type | Description |
| --- | --- | --- |
| `modal` | `boolean` | Whether the window operates as a modal dialog blocking underlying content. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`open()` {: #open data-toc-label="open()" }

Opens the window.

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`open(boolean)` {: #openboolean data-toc-label="open(boolean)" }

Sets the open state of the window.

| Parameter | Type | Description |
| --- | --- | --- |
| `open` | `boolean` | Target open state flag. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`openModal()` {: #openmodal data-toc-label="openModal()" }

Opens this window as a modal dialog.

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`opened()` {: #opened data-toc-label="opened()" }

Returns true if the window is currently opened and active in the overlay.

Returns `boolean`: True if currently open..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`padding()` {: #padding data-toc-label="padding()" }

Returns the window content padding.

Returns `EdgeInsets`: The content padding insets..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`padding(EdgeInsets)` {: #paddingedgeinsets data-toc-label="padding(EdgeInsets)" }

Sets the window content padding.

| Parameter | Type | Description |
| --- | --- | --- |
| `padding` | `EdgeInsets` | Internal content padding. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`position(float, float)` {: #positionfloat-float data-toc-label="position(float, float)" }

Sets the window coordinates in UI pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `x` | `float` | Horizontal X coordinate. |
| `y` | `float` | Vertical Y coordinate. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`releaseAutoDockHost()` {: #releaseautodockhost data-toc-label="releaseAutoDockHost()" }

Releases an automatically created docking host if empty.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`resizable()` {: #resizable data-toc-label="resizable()" }

Returns true if window border and grip resizing is enabled.

Returns `boolean`: True if the window boundaries are resizable..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`resizable(boolean)` {: #resizableboolean data-toc-label="resizable(boolean)" }

Enables or disables border and grip resizing.

| Parameter | Type | Description |
| --- | --- | --- |
| `resizable` | `boolean` | Whether the window boundaries can be resized. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`resizeGrip()` {: #resizegrip data-toc-label="resizeGrip()" }

Returns the active resize grip visual style.

Returns `ResizeGripStyle`: The corner resize grip visual style..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`resizeGrip(ResizeGripStyle)` {: #resizegripresizegripstyle data-toc-label="resizeGrip(ResizeGripStyle)" }

Sets the corner resize grip visual style.

| Parameter | Type | Description |
| --- | --- | --- |
| `resizeGrip` | `ResizeGripStyle` | Visual style of the corner resize grip. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`resizeGripVisible()` {: #resizegripvisible data-toc-label="resizeGripVisible()" }

Returns true if the corner resize grip is rendered.

Returns `boolean`: True if the corner resize grip is visible..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`resizeGripVisible(boolean)` {: #resizegripvisibleboolean data-toc-label="resizeGripVisible(boolean)" }

Sets whether the corner resize grip is visible.

| Parameter | Type | Description |
| --- | --- | --- |
| `visible` | `boolean` | Target visibility flag. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`resizeHandle()` {: #resizehandle data-toc-label="resizeHandle()" }

Returns the name of the active resize handle (e.g. BOTTOM_RIGHT).

Returns `String`: The currently hovered resize edge or corner identifier..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`resizing()` {: #resizing data-toc-label="resizing()" }

Returns true if the window is actively being resized.

Returns `boolean`: True if the window is currently being resized..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`richTitle()` {: #richtitle data-toc-label="richTitle()" }

Returns the [RichText](<../../core/text/rich-text.md>) formatted title component.

Returns `RichText`: The [RichText](<../../core/text/rich-text.md>) formatted title component..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`richTitle(RichText)` {: #richtitlerichtext data-toc-label="richTitle(RichText)" }

Sets the [RichText](<../../core/text/rich-text.md>) formatted title component.

| Parameter | Type | Description |
| --- | --- | --- |
| `title` | `RichText` | [RichText](<../../core/text/rich-text.md>) formatted title component. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`theme()` {: #theme data-toc-label="theme()" }

Returns the active [DockTheme](<../../core/widgets/docking/dock-theme.md>) applied to window chrome.

Returns `DockTheme`: The [DockTheme](<../../core/widgets/docking/dock-theme.md>) value..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`theme(DockTheme)` {: #themedocktheme data-toc-label="theme(DockTheme)" }

Applies a custom [DockTheme](<../../core/widgets/docking/dock-theme.md>) to window chrome and controls.

| Parameter | Type | Description |
| --- | --- | --- |
| `theme` | `DockTheme` | Dock theme configuration. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`title()` {: #title data-toc-label="title()" }

Returns the window title string.

Returns `String`: The title text string..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`title(String)` {: #titlestring data-toc-label="title(String)" }

Sets the window plain-text title.

| Parameter | Type | Description |
| --- | --- | --- |
| `title` | `String` | Title string. |

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`titleColor()` {: #titlecolor data-toc-label="titleColor()" }

Returns the mutable title text color.

Returns `MutableColor`: The mutable color instance..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`toggle()` {: #toggle data-toc-label="toggle()" }

Toggles between open and closed states.

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`toggleCollapsed()` {: #togglecollapsed data-toc-label="toggleCollapsed()" }

Toggles collapsed state between full and header-only.

Returns `WindowWidget`: This widget instance for chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`windowX()` {: #windowx data-toc-label="windowX()" }

Returns the window horizontal X coordinate.

Returns `float`: The window coordinate position..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">WindowWidget::</span>`windowY()` {: #windowy data-toc-label="windowY()" }

Returns the window vertical Y coordinate.

Returns `float`: The window coordinate position..

</div>

