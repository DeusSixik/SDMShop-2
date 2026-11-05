# `DockingManager`

Non-visual layout controller managing the hierarchical docking tree, pane additions, splits, tab reordering, and snapshot serialization/restoration.

### API

- `addPane(DockPane)` → DockingManager — Adds a pane to the preferred leaf node.
- `closeActivePane()` → boolean — Closes the currently active pane if permitted by close guards.
- `closePane(String)` → DockingManager — Closes and removes the pane with the specified ID.
- `containsPane(String)` → boolean — Checks whether a pane with the given ID exists in the docking tree.
- `detachPaneForFloating(String)` → DockPane — Detaches a pane from the dock tree for floating without creating the window directly.
- `dockPane(String, String, DockArea)` → boolean — Moves an existing pane to a new dock area relative to targetPaneId.
- `empty()` → boolean — Returns true if the docking manager contains no panes.
- `findPane(String)` → DockPane — Finds a DockPane by its identifier, or null if not found.
- `floatPane(String)` → WindowWidget — Detaches the pane into a new independent WindowWidget.
- `paneCount()` → int — Returns the total number of registered dock panes.
- `panes()` → List<DockPane> — Returns an immutable list of all panes across all leaves.
- `reorderPane(String, String)` → boolean — Reorders a pane before another tab in the same leaf during live drag reordering.
- `restore(DockLayoutSnapshot, Map<String, DockPane>)` → RestoreResult — Restores docking layout from a snapshot using the provided pane registry.
- `rootNode()` → DockNode — Returns the root DockNode of the docking hierarchy.
- `selectNextTab()` → boolean — Selects the next tab in the active leaf.
- `selectPane(String)` → DockingManager — Selects and activates the pane with the specified ID.
- `selectPreviousTab()` → boolean — Selects the previous tab in the active leaf.
- `selectedLeaf()` → DockNode — Returns the leaf node hosting the currently active pane.
- `selectedPane()` → DockPane — Returns the currently active selected dock pane.
- `snapshot()` → DockLayoutSnapshot — Captures an immutable snapshot of the current docking layout.
- `splitPane(String, DockArea, DockPane)` → DockingManager — Splits the leaf containing targetPaneId along the specified area with 0.5 ratio.
- `splitPane(String, DockArea, DockPane, float)` → DockingManager — Splits the leaf containing targetPaneId along the specified area with a custom split ratio.
- `tabPane(String, DockPane)` → DockingManager — Adds a pane as a tab into the leaf containing targetPaneId.

