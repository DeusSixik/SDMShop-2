# `DockPane`

Descriptor representing an individual dockable pane hosting arbitrary widget content, tab title, icon, dirty state, and close guards.

### API

- `DockPane(String, RichText, Widget)` — Creates a dock pane with explicit identifier, RichText title, and content.
- `DockPane(String, String, Widget)` — Creates a dock pane with explicit identifier, string title, and widget content.
- `DockPane(String, Widget)` — Creates a dock pane with generated unique identifier.
- `autoHide()` → boolean — Returns true if this pane automatically hides when focus is lost.
- `autoHide(boolean)` → DockPane — Enables or disables auto-hide for unpinned panes.
- `canClose()` → boolean — Returns true if closable and closeGuard permits closing.
- `closable()` → boolean — Returns true if this pane can be closed by the user.
- `closable(boolean)` → DockPane — Sets whether the pane can be closed.
- `content()` → Widget — Returns the hosted widget content.
- `dirty()` → boolean — Returns true if this pane has unsaved changes (shows dot indicator).
- `dirty(boolean)` → DockPane — Marks the pane as having unsaved modifications.
- `document()` → boolean — Returns true if this pane is classified as a document.
- `document(String, RichText, Widget)` → DockPane — Creates a document-style dock pane with RichText title.
- `document(String, String, Widget)` → DockPane — Creates a document-style dock pane with string title.
- `icon()` → UiIcon — Returns the icon associated with this tab.
- `icon(UiIcon)` → DockPane — Sets the icon displayed in tab strips and floating window headers.
- `id()` → String — Returns the unique pane identifier.
- `kind()` → DockPaneKind — Returns the pane kind (DOCUMENT or TOOL).
- `kind(DockPaneKind)` → DockPane — Sets the pane classification (DOCUMENT or TOOL).
- `onCloseRequested(Predicate<DockPane>)` → DockPane — Sets a close request guard predicate that can prompt for saving or prevent closing.
- `pinned()` → boolean — Returns true if this pane is pinned in the dock hierarchy.
- `pinned(boolean)` → DockPane — Sets whether the pane is pinned.
- `richTitle()` → RichText — Returns the RichText formatted title.
- `richTitle(RichText)` → DockPane — Sets the RichText title of the pane.
- `title()` → String — Returns the plain-text title string.
- `title(String)` → DockPane — Sets the plain-text title of the pane.
- `tool()` → boolean — Returns true if this pane is classified as a tool window.
- `tool(String, RichText, Widget)` → DockPane — Creates a tool-style dock pane with RichText title.
- `tool(String, String, Widget)` → DockPane — Creates a tool-style dock pane with string title.

