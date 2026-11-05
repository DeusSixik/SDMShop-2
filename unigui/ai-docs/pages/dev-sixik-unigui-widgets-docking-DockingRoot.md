# `DockingRoot` (extends `Box`)

Root visual widget for the IDE docking system hosting dock tree nodes, tab strips, split handles, drop previews, and tear-off floating window integrations.

### API

- `DockingRoot()` — Constructs a new DockingRoot widget initialized with default theme and styling.
- `acceptsDockDrops()` → boolean — Returns true if this docking root currently accepts drop operations based on window dock policy.
- `addDocument(String, String, Widget)` → DockingRoot — Convenience method to add a DOCUMENT kind pane into the central editor workspace.
- `addPane(DockPane)` → DockingRoot — Adds a pane to the docking layout.
- `addToolPane(String, String, Widget, DockArea)` → DockingRoot — Convenience method to add a TOOL kind utility pane docked to a perimeter area.
- `allowFloatingOutsideHost()` → boolean — Returns true if floating dock windows are allowed to move outside host bounds.
- `allowFloatingOutsideHost(boolean)` → DockingRoot (default: `false`) — Sets whether floating dock windows may move outside root host bounds.
- `clearForeignWindowDropPreview(WindowWidget)` → void — Clears any active foreign window drop preview highlighting.
- `closeActivePane()` → boolean — Closes the currently active tab.
- `crossCellRect(RectView, DockArea)` → MutableRect — Calculates the geometric bounds of a drop cross directional trigger cell.
- `dockDragging()` → boolean — Returns true if a docking tab drag gesture is currently underway.
- `dockDropPreview()` → DockDropIntent — Returns the current drop preview intent.
- `dockForeignWindow(WindowWidget, float, float)` → boolean — Docks the content of an external WindowWidget into this DockingRoot.
- `dockingManager()` → DockingManager — Returns the non-visual DockingManager coordinating this root.
- `dragController()` → DockDragController — Returns the DockDragController managing tab and window dragging.
- `dropPreviewRenderer()` → WidgetRender — Returns custom drop preview renderer delegate, or null if default.
- `dropPreviewRenderer(WidgetRender)` → DockingRoot — Sets custom drop preview renderer delegate.
- `findPane(String)` → DockPane — Finds a DockPane by its unique identifier.
- `finishForeignWindowDrop(WindowWidget, float, float)` → boolean — Completes drop of an external WindowWidget into the docking layout.
- `floatingWindowsRedockLocked()` → boolean — Returns whether floating windows spawned from this root are prevented from redocking.
- `floatingWindowsRedockLocked(boolean)` → DockingRoot (default: `false`) — Locks or unlocks redocking capability for floating windows.
- `lastFloatingWindow()` → WindowWidget — Returns the last WindowWidget created by a float or tear-off operation, or null.
- `manager()` → DockingManager — Convenience shortcut returning the DockingManager.
- `onDragEnded(EventListener<? super DockDragEndedEvent>)` → EventSubscription — Registers a listener called when a tab drag gesture ends.
- `onDragMoved(EventListener<? super DockDragMovedEvent>)` → EventSubscription — Registers a listener called on pointer movement during tab drag.
- `onDragStarted(EventListener<? super DockDragStartedEvent>)` → EventSubscription — Registers a listener called when a dock tab drag gesture starts.
- `onDropPreviewChanged(EventListener<? super DockDropPreviewChangedEvent>)` → EventSubscription — Registers a listener called when the drop preview target intent updates.
- `onLayoutChanged(EventListener<? super DockLayoutChangedEvent>)` → EventSubscription — Registers a listener called when docking layout structure changes.
- `onLayoutRestored(EventListener<? super DockLayoutRestoredEvent>)` → EventSubscription — Registers a listener called when layout is restored from snapshot.
- `paneRenderer()` → WidgetRender — Returns custom pane renderer delegate, or null if default.
- `paneRenderer(WidgetRender)` → DockingRoot — Sets custom pane renderer delegate.
- `previewForeignWindowDrop(WindowWidget, float, float)` → void — Calculates and updates docking drop preview for an externally dragged WindowWidget.
- `restoreLayout(DockLayoutSnapshot, Map<String, DockPane>)` → DockingRoot — Restores layout structure from snapshot.
- `rootNode()` → DockNode — Returns the root DockNode of the docking layout tree.
- `selectNextTab()` → boolean — Selects the next tab in the active leaf.
- `selectPane(String)` → DockingRoot — Selects and brings into view the pane with the specified ID.
- `selectPreviousTab()` → boolean — Selects the previous tab in the active leaf.
- `splitHandleRenderer()` → WidgetRender — Returns custom split handle renderer delegate, or null if default.
- `splitHandleRenderer(WidgetRender)` → DockingRoot — Sets custom split handle renderer delegate.
- `splitHandleStyle()` → SplitHandleStyle — Returns the visual rendering style of split handles.
- `splitHandleStyle(SplitHandleStyle)` → DockingRoot (default: `block`) — Sets the visual rendering style of split handles (BLOCK or SEAM).
- `splitHandleThickness()` → float — Returns the split handle thickness in pixels.
- `splitHandleThickness(float)` → DockingRoot (default: `5`) — Sets the split handle thickness in pixels.
- `splitPane(String, DockArea, DockPane)` → DockingRoot — Splits the leaf containing targetPaneId along the specified area.
- `splitPane(String, DockArea, DockPane, float)` → DockingRoot — Splits the leaf containing targetPaneId along the specified area with a custom split ratio.
- `tabBounds(String)` → RectView — Returns the bounding rectangle of a tab in root coordinates, or null if invisible.
- `tabCloseBounds(String)` → RectView — Returns the bounding rectangle of a tab close button in root coordinates, or null if invisible/unclosable.
- `tabHeight()` → float — Returns the height of tab strips in pixels.
- `tabHeight(float)` → DockingRoot (default: `22`) — Sets the height of tab strips in pixels.
- `tabPane(String, DockPane)` → DockingRoot — Adds a pane as a tab into the leaf containing targetPaneId.
- `tearOffOnDragOut()` → boolean — Returns whether dragging a tab out of the docking area tears it off into a floating window.
- `tearOffOnDragOut(boolean)` → DockingRoot (default: `true`) — Enables or disables automatic tear-off of tabs into floating windows when dropped outside dock zones.
- `theme()` → DockTheme — Returns the active DockTheme applied to this root.
- `theme(DockTheme)` → DockingRoot — Applies a custom DockTheme to this root.
- `useDefaultDockingRenderers()` → DockingRoot — Resets all custom renderers to built-in docking defaults.
- `windowDragZoneAt(float, float)` → boolean — Checks whether pressing at (rootX, rootY) initiates window drag for host WindowWidgets.
- `windowHost()` → WindowWidget — Returns the parent WindowWidget hosting this docking root, or null if root is top-level.
- `windowHost(WindowWidget)` → void — Binds this docking root to a host WindowWidget.

