# `TreeViewNode`

Hierarchical node item in a TreeView holding text, custom icon, expansion state, and child nodes.

### API

- `TreeViewNode()` — Constructs an empty TreeViewNode with empty label.
- `TreeViewNode(RichText)` — Constructs a TreeViewNode with a RichText label.
- `TreeViewNode(String)` — Constructs a TreeViewNode with a plain text label.
- `addChild(RichText)` → TreeViewNode — Creates and appends a child node with RichText label.
- `addChild(String)` → TreeViewNode — Creates and appends a child node with plain text label.
- `addChild(TreeViewNode)` → TreeViewNode — Appends an existing node as a child.
- `child(int)` → TreeViewNode — Returns the child node at the specified index.
- `childCount()` → int — Returns the number of child nodes under this node.
- `children()` → List<TreeViewNode> — Returns an unmodifiable list of child nodes.
- `clearChildren()` → TreeViewNode — Removes all child nodes from this node.
- `depth()` → int — Returns the nesting depth of this node from the root.
- `emptyFolder()` → boolean — Returns true if this node is configured to display as an empty folder.
- `emptyFolder(boolean)` → TreeViewNode — Sets whether this node displays folder semantics even without children.
- `expanded()` → boolean — Returns true if this node is currently expanded.
- `expanded(boolean)` → TreeViewNode — Expands or collapses this node, rebuilding tree rows.
- `folder()` → boolean — Returns true if this node is explicitly designated as a folder.
- `folder(boolean)` → TreeViewNode — Designates whether this node represents a folder item.
- `hasChildren()` → boolean — Returns true if this node has one or more children.
- `icon()` → TextureHandle — Returns the texture handle icon for this node.
- `icon(TextureHandle)` → TreeViewNode — Sets the texture icon for this node.
- `iconDraw()` → IconDraw — Returns the custom icon draw callback.
- `iconDraw(IconDraw)` → TreeViewNode — Sets a custom icon draw callback.
- `iconSuppressed()` → boolean — Returns true if icon rendering is explicitly suppressed for this node.
- `insertChild(int, TreeViewNode)` → TreeViewNode — Inserts a child node at the given index.
- `isAncestor(TreeViewNode)` → boolean — Returns true if this node is an ancestor of the candidate node.
- `noIcon()` → TreeViewNode — Explicitly suppresses icon rendering on this node.
- `owner()` → TreeView — Returns the TreeView that owns this node.
- `parent()` → TreeViewNode — Returns the parent node in the hierarchy, or null if root.
- `removeChild(TreeViewNode)` → TreeViewNode — Removes the specified child node from this node.
- `richText()` → RichText — Returns the RichText representation of this node label.
- `richText(RichText)` → TreeViewNode — Sets the RichText label of this node.
- `rowButton()` → Button — Returns the button widget rendering this node row, or null if not currently visible.
- `selectable()` → boolean — Returns true if this node can be selected by user interaction.
- `selectable(boolean)` → TreeViewNode — Sets whether this node can be selected.
- `silentExpanded(boolean)` → TreeViewNode — Sets the expanded state without triggering row rebuilds.
- `text()` → String — Returns the plain text representation of this node label.
- `text(String)` → TreeViewNode — Sets the plain text label of this node.
- `toggleExpanded()` → TreeViewNode — Toggles the expanded state of this node.
- `userData()` → Object — Returns the arbitrary user data object attached to this node.
- `userData(Class<T>)` → T — Returns typed user data attached to this node, or null if cast fails.
- `userData(Object)` → TreeViewNode — Attaches arbitrary user data to this node.
- `value()` → String — Returns the semantic identifier string value of this node.
- `value(String)` → TreeViewNode — Sets the semantic identifier string value of this node.

