# `DockNode`

Hierarchical docking tree node: either a LEAF containing tabbed DockPanes or a SPLIT node dividing two children horizontally or vertically.

### API

- `containsPane(String)` → boolean — Checks whether this node or its descendants contain a pane with the given ID.
- `empty()` → boolean — Returns true if this leaf contains zero panes.
- `findPane(String)` → DockPane — Finds a DockPane by identifier within this node and its descendants.
- `first()` → DockNode — Returns the first child node of a split.
- `id()` → String — Returns the unique identifier of this dock node.
- `isLeaf()` → boolean — Returns true if this node is a leaf containing panes.
- `isSplit()` → boolean — Returns true if this node is a split node containing two children.
- `kind()` → Kind — Returns whether this node is a LEAF or SPLIT.
- `leaf()` → DockNode — Creates an empty leaf dock node.
- `leaf(DockPane)` → DockNode — Creates a leaf dock node populated with an initial pane.
- `orientation()` → DockSplitOrientation — Returns the split orientation (HORIZONTAL or VERTICAL) if this is a split node.
- `panes()` → List<DockPane> — Returns the unmodifiable list of panes hosted in this leaf node.
- `second()` → DockNode — Returns the second child node of a split.
- `selectedIndex()` → int — Returns the index of the currently active tab in this leaf.
- `selectedPane()` → DockPane — Returns the currently selected DockPane in this leaf, or null if empty.
- `split(DockSplitOrientation, DockNode, DockNode, float)` → DockNode — Creates a split dock node dividing two children according to orientation and ratio.
- `splitRatio()` → float — Returns the fractional split ratio between first and second children (0.1 to 0.9).

