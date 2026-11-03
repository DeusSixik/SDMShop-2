---
title: "DockingRoot"
slug: "docking-root"
category: "Widgets/Docking"
locale: "en"
order: 0
---

# DockingRoot

Root visual widget for the IDE docking system hosting dock tree nodes, tab strips, split handles, drop previews, and tear-off floating window integrations.

## Overview

`DockingRoot` is the top-level visual container integrating the entire docking system.

### Core Features

- Renders hierarchical split trees with draggable divider handles.
- Renders tab strips with close buttons, icons, and active indicators.
- Integrates with `WindowWidget` hosts for tear-off floating windows.
- Displays drop preview highlights and cross-docking targets.
- Dispatches docking lifecycle events (`onLayoutChanged`, `onDropPreviewChanged`).

## Configuring Docking Root and Adding Panes

```java
DockingRoot root = new DockingRoot();
root.addDocument("code-1", "CodeEditor.java", editorWidget);
root.addToolPane("proj-1", "Project", projectTreeWidget, DockArea.LEFT);
```

## Related links

- [DockPane](<../../core/widgets/docking/dock-pane.md>): Descriptor representing an individual dockable pane hosting arbitrary widget content, tab title, icon, dirty state, and close guards.
- [DockTheme](<../../core/widgets/docking/dock-theme.md>): Styling theme for the docking system configuring palette colors ([DockColor](<../../core/widgets/docking/dock-color.md>)), tab geometry, split handles, shadows, and window resize grips.
- [DockingManager](<../../core/widgets/docking/docking-manager.md>): Non-visual layout controller managing the hierarchical docking tree, pane additions, splits, tab reordering, and snapshot serialization/restoration.

## API

<div class="api-member-card" markdown="1">

### `DockingRoot()` {: #dockingroot data-toc-label="DockingRoot()" }

Constructs a new DockingRoot widget initialized with default theme and styling.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`acceptsDockDrops()` {: #acceptsdockdrops data-toc-label="acceptsDockDrops()" }

Returns true if this docking root currently accepts drop operations based on window dock policy.

Returns `boolean`: True if the root accepts docking drop operations..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`addDocument(String, String, Widget)` {: #adddocumentstring-string-widget data-toc-label="addDocument(String, String, Widget)" }

Convenience method to add a DOCUMENT kind pane into the central editor workspace.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique pane identifier. |
| `title` | `String` | Pane title text. |
| `content` | `Widget` | [Widget](<../widget.md>) content of the pane. |

Returns `DockingRoot`: This DockingRoot for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`addPane(DockPane)` {: #addpanedockpane data-toc-label="addPane(DockPane)" }

Adds a pane to the docking layout.

| Parameter | Type | Description |
| --- | --- | --- |
| `pane` | `DockPane` | [DockPane](<../../core/widgets/docking/dock-pane.md>) to add. |

Returns `DockingRoot`: This DockingRoot for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`addToolPane(String, String, Widget, DockArea)` {: #addtoolpanestring-string-widget-dockarea data-toc-label="addToolPane(String, String, Widget, DockArea)" }

Convenience method to add a TOOL kind utility pane docked to a perimeter area.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Unique pane identifier. |
| `title` | `String` | Pane title text. |
| `content` | `Widget` | [Widget](<../widget.md>) content of the pane. |
| `area` | `DockArea` | [DockArea](<../../core/widgets/docking/dock-area.md>) where the pane should be placed. |

Returns `DockingRoot`: This DockingRoot for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`allowFloatingOutsideHost()` {: #allowfloatingoutsidehost data-toc-label="allowFloatingOutsideHost()" }

Returns true if floating dock windows are allowed to move outside host bounds.

Returns `boolean`: True if floating windows can move beyond the host bounds..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`allowFloatingOutsideHost(boolean)` {: #allowfloatingoutsidehostboolean data-toc-label="allowFloatingOutsideHost(boolean)" }

Sets whether floating dock windows may move outside root host bounds.

| Parameter | Type | Description |
| --- | --- | --- |
| `allowFloatingOutsideHost` | `boolean` | Whether floating windows may move outside the host bounds. |

Returns `DockingRoot`: This DockingRoot for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`clearForeignWindowDropPreview(WindowWidget)` {: #clearforeignwindowdroppreviewwindowwidget data-toc-label="clearForeignWindowDropPreview(WindowWidget)" }

Clears any active foreign window drop preview highlighting.

| Parameter | Type | Description |
| --- | --- | --- |
| `window` | `WindowWidget` | Floating window being dragged. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`closeActivePane()` {: #closeactivepane data-toc-label="closeActivePane()" }

Closes the currently active tab.

Returns `boolean`: True if the pane was closed; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`crossCellRect(RectView, DockArea)` {: #crosscellrectrectview-dockarea data-toc-label="crossCellRect(RectView, DockArea)" }

Calculates the geometric bounds of a drop cross directional trigger cell.

| Parameter | Type | Description |
| --- | --- | --- |
| `leafBounds` | `RectView` | Screen bounds of the dock leaf. |
| `area` | `DockArea` | [DockArea](<../../core/widgets/docking/dock-area.md>) where the pane should be placed. |

Returns `MutableRect`: The bounding rectangle of the target docking cell..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`dockDragging()` {: #dockdragging data-toc-label="dockDragging()" }

Returns true if a docking tab drag gesture is currently underway.

Returns `boolean`: True if a docking drag operation is currently active..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`dockDropPreview()` {: #dockdroppreview data-toc-label="dockDropPreview()" }

Returns the current drop preview intent.

Returns `DockDropIntent`: Current drop preview intent, or null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`dockForeignWindow(WindowWidget, float, float)` {: #dockforeignwindowwindowwidget-float-float data-toc-label="dockForeignWindow(WindowWidget, float, float)" }

Docks the content of an external [WindowWidget](<../feedback/window-widget.md>) into this DockingRoot.

| Parameter | Type | Description |
| --- | --- | --- |
| `window` | `WindowWidget` | Floating window being dragged. |
| `rootX` | `float` | Pointer root X coordinate. |
| `rootY` | `float` | Pointer root Y coordinate. |

Returns `boolean`: True if the pane was successfully docked; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`dockingManager()` {: #dockingmanager data-toc-label="dockingManager()" }

Returns the non-visual [DockingManager](<../../core/widgets/docking/docking-manager.md>) coordinating this root.

Returns `DockingManager`: The underlying [DockingManager](<../../core/widgets/docking/docking-manager.md>) managing layout..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`dragController()` {: #dragcontroller data-toc-label="dragController()" }

Returns the [DockDragController](<../../core/widgets/docking/dock-drag-controller.md>) managing tab and window dragging.

Returns `DockDragController`: The [DockDragController](<../../core/widgets/docking/dock-drag-controller.md>) managing tab gestures..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`dropPreviewRenderer()` {: #droppreviewrenderer data-toc-label="dropPreviewRenderer()" }

Returns custom drop preview renderer delegate, or null if default.

Returns `WidgetRender`: The requested renderer..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`dropPreviewRenderer(WidgetRender)` {: #droppreviewrendererwidgetrender data-toc-label="dropPreviewRenderer(WidgetRender)" }

Sets custom drop preview renderer delegate.

| Parameter | Type | Description |
| --- | --- | --- |
| `dropPreviewRenderer` | `WidgetRender` | Custom drop preview renderer. |

Returns `DockingRoot`: This DockingRoot for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`findPane(String)` {: #findpanestring data-toc-label="findPane(String)" }

Finds a [DockPane](<../../core/widgets/docking/dock-pane.md>) by its unique identifier.

| Parameter | Type | Description |
| --- | --- | --- |
| `paneId` | `String` | Identifier of the pane to find or select. |

Returns `DockPane`: The matching [DockPane](<../../core/widgets/docking/dock-pane.md>), or null if not found..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`finishForeignWindowDrop(WindowWidget, float, float)` {: #finishforeignwindowdropwindowwidget-float-float data-toc-label="finishForeignWindowDrop(WindowWidget, float, float)" }

Completes drop of an external [WindowWidget](<../feedback/window-widget.md>) into the docking layout.

| Parameter | Type | Description |
| --- | --- | --- |
| `window` | `WindowWidget` | Floating window being dragged. |
| `rootX` | `float` | Pointer root X coordinate. |
| `rootY` | `float` | Pointer root Y coordinate. |

Returns `boolean`: True if the pane was successfully docked; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`floatingWindowsRedockLocked()` {: #floatingwindowsredocklocked data-toc-label="floatingWindowsRedockLocked()" }

Returns whether floating windows spawned from this root are prevented from redocking.

Returns `boolean`: True if floating windows cannot be re-docked..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`floatingWindowsRedockLocked(boolean)` {: #floatingwindowsredocklockedboolean data-toc-label="floatingWindowsRedockLocked(boolean)" }

Locks or unlocks redocking capability for floating windows.

| Parameter | Type | Description |
| --- | --- | --- |
| `floatingWindowsRedockLocked` | `boolean` | Whether floating windows are locked from re-docking. |

Returns `DockingRoot`: This DockingRoot for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`lastFloatingWindow()` {: #lastfloatingwindow data-toc-label="lastFloatingWindow()" }

Returns the last [WindowWidget](<../feedback/window-widget.md>) created by a float or tear-off operation, or null.

Returns `WindowWidget`: The most recently detached floating window, or null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`manager()` {: #manager data-toc-label="manager()" }

Convenience shortcut returning the [DockingManager](<../../core/widgets/docking/docking-manager.md>).

Returns `DockingManager`: The underlying [DockingManager](<../../core/widgets/docking/docking-manager.md>) managing layout..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`onDragEnded(EventListener)` {: #ondragendedeventlistener data-toc-label="onDragEnded(EventListener)" }

Registers a listener called when a tab drag gesture ends.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | Listener for drop preview change events. |

Returns `EventSubscription`: Subscription token for unregistering the listener..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`onDragMoved(EventListener)` {: #ondragmovedeventlistener data-toc-label="onDragMoved(EventListener)" }

Registers a listener called on pointer movement during tab drag.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | Listener for drop preview change events. |

Returns `EventSubscription`: Subscription token for unregistering the listener..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`onDragStarted(EventListener)` {: #ondragstartedeventlistener data-toc-label="onDragStarted(EventListener)" }

Registers a listener called when a dock tab drag gesture starts.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | Listener for drop preview change events. |

Returns `EventSubscription`: Subscription token for unregistering the listener..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`onDropPreviewChanged(EventListener)` {: #ondroppreviewchangedeventlistener data-toc-label="onDropPreviewChanged(EventListener)" }

Registers a listener called when the drop preview target intent updates.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | Listener for drop preview change events. |

Returns `EventSubscription`: Subscription token for unregistering the listener..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`onLayoutChanged(EventListener)` {: #onlayoutchangedeventlistener data-toc-label="onLayoutChanged(EventListener)" }

Registers a listener called when docking layout structure changes.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | Listener for drop preview change events. |

Returns `EventSubscription`: Subscription token for unregistering the listener..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`onLayoutRestored(EventListener)` {: #onlayoutrestoredeventlistener data-toc-label="onLayoutRestored(EventListener)" }

Registers a listener called when layout is restored from snapshot.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | Listener for drop preview change events. |

Returns `EventSubscription`: Subscription token for unregistering the listener..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`paneRenderer()` {: #panerenderer data-toc-label="paneRenderer()" }

Returns custom pane renderer delegate, or null if default.

Returns `WidgetRender`: The requested renderer..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`paneRenderer(WidgetRender)` {: #panerendererwidgetrender data-toc-label="paneRenderer(WidgetRender)" }

Sets custom pane renderer delegate.

| Parameter | Type | Description |
| --- | --- | --- |
| `paneRenderer` | `WidgetRender` | Custom drop preview renderer. |

Returns `DockingRoot`: This DockingRoot for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`previewForeignWindowDrop(WindowWidget, float, float)` {: #previewforeignwindowdropwindowwidget-float-float data-toc-label="previewForeignWindowDrop(WindowWidget, float, float)" }

Calculates and updates docking drop preview for an externally dragged [WindowWidget](<../feedback/window-widget.md>).

| Parameter | Type | Description |
| --- | --- | --- |
| `window` | `WindowWidget` | Floating window being dragged. |
| `rootX` | `float` | Pointer root X coordinate. |
| `rootY` | `float` | Pointer root Y coordinate. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`restoreLayout(DockLayoutSnapshot, Map)` {: #restorelayoutdocklayoutsnapshot-map data-toc-label="restoreLayout(DockLayoutSnapshot, Map)" }

Restores layout structure from snapshot.

| Parameter | Type | Description |
| --- | --- | --- |
| `snapshot` | `DockLayoutSnapshot` | Layout snapshot to restore. |
| `paneRegistry` | `Map` | Registry mapping pane IDs to [DockPane](<../../core/widgets/docking/dock-pane.md>) instances. |

Returns `DockingRoot`: This DockingRoot for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`rootNode()` {: #rootnode data-toc-label="rootNode()" }

Returns the root [DockNode](<../../core/widgets/docking/dock-node.md>) of the docking layout tree.

Returns `DockNode`: The root node of the docking tree..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`selectNextTab()` {: #selectnexttab data-toc-label="selectNextTab()" }

Selects the next tab in the active leaf.

Returns `boolean`: True if the active tab changed; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`selectPane(String)` {: #selectpanestring data-toc-label="selectPane(String)" }

Selects and brings into view the pane with the specified ID.

| Parameter | Type | Description |
| --- | --- | --- |
| `paneId` | `String` | Identifier of the pane to find or select. |

Returns `DockingRoot`: This DockingRoot for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`selectPreviousTab()` {: #selectprevioustab data-toc-label="selectPreviousTab()" }

Selects the previous tab in the active leaf.

Returns `boolean`: True if the active tab changed; false otherwise..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`splitHandleRenderer()` {: #splithandlerenderer data-toc-label="splitHandleRenderer()" }

Returns custom split handle renderer delegate, or null if default.

Returns `WidgetRender`: The requested renderer..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`splitHandleRenderer(WidgetRender)` {: #splithandlerendererwidgetrender data-toc-label="splitHandleRenderer(WidgetRender)" }

Sets custom split handle renderer delegate.

| Parameter | Type | Description |
| --- | --- | --- |
| `splitHandleRenderer` | `WidgetRender` | Custom drop preview renderer. |

Returns `DockingRoot`: This DockingRoot for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`splitHandleStyle()` {: #splithandlestyle data-toc-label="splitHandleStyle()" }

Returns the visual rendering style of split handles.

Returns `SplitHandleStyle`: Current split handle visual style..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`splitHandleStyle(SplitHandleStyle)` {: #splithandlestylesplithandlestyle data-toc-label="splitHandleStyle(SplitHandleStyle)" }

Sets the visual rendering style of split handles (BLOCK or SEAM).

| Parameter | Type | Description |
| --- | --- | --- |
| `splitHandleStyle` | `SplitHandleStyle` | Visual style of split handles. |

Returns `DockingRoot`: This DockingRoot for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`splitHandleThickness()` {: #splithandlethickness data-toc-label="splitHandleThickness()" }

Returns the split handle thickness in pixels.

Returns `float`: Split handle thickness in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`splitHandleThickness(float)` {: #splithandlethicknessfloat data-toc-label="splitHandleThickness(float)" }

Sets the split handle thickness in pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `splitHandleThickness` | `float` | Thickness of split handles in pixels. |

Returns `DockingRoot`: This DockingRoot for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`splitPane(String, DockArea, DockPane)` {: #splitpanestring-dockarea-dockpane data-toc-label="splitPane(String, DockArea, DockPane)" }

Splits the leaf containing targetPaneId along the specified area.

| Parameter | Type | Description |
| --- | --- | --- |
| `targetPaneId` | `String` | Reference pane ID for split or tab placement. |
| `area` | `DockArea` | [DockArea](<../../core/widgets/docking/dock-area.md>) where the pane should be placed. |
| `pane` | `DockPane` | [DockPane](<../../core/widgets/docking/dock-pane.md>) to add. |

Returns `DockingRoot`: This DockingRoot for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`splitPane(String, DockArea, DockPane, float)` {: #splitpanestring-dockarea-dockpane-float data-toc-label="splitPane(String, DockArea, DockPane, float)" }

Splits the leaf containing targetPaneId along the specified area with a custom split ratio.

| Parameter | Type | Description |
| --- | --- | --- |
| `targetPaneId` | `String` | Reference pane ID for split or tab placement. |
| `area` | `DockArea` | [DockArea](<../../core/widgets/docking/dock-area.md>) where the pane should be placed. |
| `pane` | `DockPane` | [DockPane](<../../core/widgets/docking/dock-pane.md>) to add. |
| `splitRatio` | `float` | Split ratio dividing the area. |

Returns `DockingRoot`: This DockingRoot for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`tabBounds(String)` {: #tabboundsstring data-toc-label="tabBounds(String)" }

Returns the bounding rectangle of a tab in root coordinates, or null if invisible.

| Parameter | Type | Description |
| --- | --- | --- |
| `paneId` | `String` | Identifier of the pane to find or select. |

Returns `RectView`: Screen-space bounds of the requested tab header..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`tabCloseBounds(String)` {: #tabcloseboundsstring data-toc-label="tabCloseBounds(String)" }

Returns the bounding rectangle of a tab close button in root coordinates, or null if invisible/unclosable.

| Parameter | Type | Description |
| --- | --- | --- |
| `paneId` | `String` | Identifier of the pane to find or select. |

Returns `RectView`: Screen-space bounds of the tab close button..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`tabHeight()` {: #tabheight data-toc-label="tabHeight()" }

Returns the height of tab strips in pixels.

Returns `float`: Height of docking tab bars in pixels..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`tabHeight(float)` {: #tabheightfloat data-toc-label="tabHeight(float)" }

Sets the height of tab strips in pixels.

| Parameter | Type | Description |
| --- | --- | --- |
| `tabHeight` | `float` | Height of tab headers in pixels. |

Returns `DockingRoot`: This DockingRoot for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`tabPane(String, DockPane)` {: #tabpanestring-dockpane data-toc-label="tabPane(String, DockPane)" }

Adds a pane as a tab into the leaf containing targetPaneId.

| Parameter | Type | Description |
| --- | --- | --- |
| `targetPaneId` | `String` | Reference pane ID for split or tab placement. |
| `pane` | `DockPane` | [DockPane](<../../core/widgets/docking/dock-pane.md>) to add. |

Returns `DockingRoot`: This DockingRoot for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`tearOffOnDragOut()` {: #tearoffondragout data-toc-label="tearOffOnDragOut()" }

Returns whether dragging a tab out of the docking area tears it off into a floating window.

Returns `boolean`: True if dragging tabs out creates floating windows..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`tearOffOnDragOut(boolean)` {: #tearoffondragoutboolean data-toc-label="tearOffOnDragOut(boolean)" }

Enables or disables automatic tear-off of tabs into floating windows when dropped outside dock zones.

| Parameter | Type | Description |
| --- | --- | --- |
| `tearOffOnDragOut` | `boolean` | Whether dragging tabs outside tears them off into floating windows. |

Returns `DockingRoot`: This DockingRoot for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`theme()` {: #theme data-toc-label="theme()" }

Returns the active [DockTheme](<../../core/widgets/docking/dock-theme.md>) applied to this root.

Returns `DockTheme`: The current docking theme..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`theme(DockTheme)` {: #themedocktheme data-toc-label="theme(DockTheme)" }

Applies a custom [DockTheme](<../../core/widgets/docking/dock-theme.md>) to this root.

| Parameter | Type | Description |
| --- | --- | --- |
| `theme` | `DockTheme` | Docking theme configuration. |

Returns `DockingRoot`: This DockingRoot for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`useDefaultDockingRenderers()` {: #usedefaultdockingrenderers data-toc-label="useDefaultDockingRenderers()" }

Resets all custom renderers to built-in docking defaults.

Returns `DockingRoot`: This DockingRoot for method chaining..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`windowDragZoneAt(float, float)` {: #windowdragzoneatfloat-float data-toc-label="windowDragZoneAt(float, float)" }

Checks whether pressing at (rootX, rootY) initiates window drag for host WindowWidgets.

| Parameter | Type | Description |
| --- | --- | --- |
| `rootX` | `float` | Pointer root X coordinate. |
| `rootY` | `float` | Pointer root Y coordinate. |

Returns `boolean`: True if pointer coordinates hit the window drag zone..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`windowHost()` {: #windowhost data-toc-label="windowHost()" }

Returns the parent [WindowWidget](<../feedback/window-widget.md>) hosting this docking root, or null if root is top-level.

Returns `WindowWidget`: The host widget containing floating windows..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DockingRoot::</span>`windowHost(WindowWidget)` {: #windowhostwindowwidget data-toc-label="windowHost(WindowWidget)" }

Binds this docking root to a host [WindowWidget](<../feedback/window-widget.md>).

| Parameter | Type | Description |
| --- | --- | --- |
| `windowHost` | `WindowWidget` | [WindowWidget](<../feedback/window-widget.md>) host for floating windows. |

</div>

