# `TreeView` (extends `LinearBox`)

Hierarchical tree container rendering collapsable nodes with custom icons, inline editing, and selection.

### API

- `TreeView()` — Constructs an empty TreeView container.
- `addRoot(RichText)` → TreeViewNode — Creates and appends a new root node with RichText label.
- `addRoot(String)` → TreeViewNode — Creates and appends a new root node with plain text label.
- `addRoot(TreeViewNode)` → TreeView — Appends an existing node as a root.
- `beginBatch()` → TreeView — Begins a batch update session, suppressing layout recalculations until endBatch is called.
- `cancelEditing()` → void — Cancels the active inline node edit session without committing changes.
- `clearRoots()` → TreeView — Removes all root nodes from the tree.
- `clearSelection()` → TreeView — Clears any current node selection.
- `editingNode()` → TreeViewNode — Returns the node currently being edited in-place, or null if not editing.
- `endBatch()` → TreeView — Ends a batch update session, triggering row rebuilding if mutations occurred.
- `hoverScrollSpeed()` → float — Returns the text hover scrolling speed.
- `hoverScrollSpeed(float)` → TreeView (default: `24`) — Sets the text hover scrolling speed.
- `iconMode()` → TreeIconMode — Returns the current tree icon display mode.
- `iconMode(TreeIconMode)` → TreeView — Sets the tree icon display mode.
- `insertRoot(int, TreeViewNode)` → TreeView — Inserts a root node at the specified index.
- `isBatching()` → boolean — Returns true if batch mode is currently active.
- `isEditing()` → boolean — Returns true if an inline node label edit operation is active.
- `onRowCreated()` → Consumer<TreeRowButton> — Returns the row creation callback listener.
- `onRowCreated(Consumer<TreeRowButton>)` → TreeView — Registers a listener invoked whenever a row button is instantiated or re-bound.
- `onSelectionChanged(EventListener<? super SelectionChangedEvent>)` → EventSubscription — Registers a listener called when node selection changes.
- `removeRoot(TreeViewNode)` → TreeView — Removes the specified root node from the tree.
- `root(int)` → TreeViewNode — Returns the root node at the specified index.
- `rootCount()` → int — Returns the total number of root nodes.
- `roots()` → List<TreeViewNode> — Returns an unmodifiable list of top-level root nodes.
- `rowTextHoverScrollSpeed()` → float — Returns the text hover scrolling speed in pixels per second for overflowing node labels.
- `rowTextHoverScrollSpeed(float)` → TreeView (default: `24`) — Sets the text hover scrolling speed in pixels per second.
- `rowsHost()` → VBox — Returns the vertical container hosting row button widgets.
- `select(TreeViewNode)` → TreeView — Selects the specified node and fires a selection change event.
- `selectedIndex()` → int — Returns the visible index of the currently selected node.
- `selectedNode()` → TreeViewNode — Returns the currently selected tree node, or null if none.
- `selectedPath()` → List<Integer> — Returns the hierarchical index path of the currently selected node.
- `silentSelect(TreeViewNode)` → TreeView — Selects the specified node without emitting a selection change event.
- `startEditing(TreeViewNode, Consumer<String>)` → void — Starts inline text editing for the specified node with default input configuration.
- `startEditing(TreeViewNode, Consumer<TextInput>, Consumer<String>)` → void — Starts inline text editing for the specified node with custom text input configuration and commit callback.
- `stopEditing()` → void — Commits and closes the active inline node edit session.
- `visibleNodes()` → List<TreeViewNode> — Returns an unmodifiable list of currently visible (flattened) tree nodes.

