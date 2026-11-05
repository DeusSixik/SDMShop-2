# `ExpandablePanel` (extends `LinearBox`)

Collapsible container widget with interactive title bar header and animated show/hide content host.

### API

- `ExpandablePanel()` — Constructs an empty ExpandablePanel.
- `ExpandablePanel(RichText)` — Constructs an ExpandablePanel with a RichText formatted title.
- `ExpandablePanel(String)` — Constructs an ExpandablePanel with a plain text title.
- `addContent(Widget)` → ExpandablePanel — Adds a content widget into the expandable body.
- `clearContent()` → ExpandablePanel — Clears all content widgets from the expandable body.
- `contentHost()` → VBox — Returns the vertical container hosting expandable content widgets.
- `expanded()` → boolean — Returns true if the panel content is currently expanded.
- `expanded(boolean)` → ExpandablePanel — Sets the expanded state and dispatches an expansion event.
- `headerButton()` → ToggleButton — Returns the header ToggleButton controlling panel expansion.
- `onExpandedChanged(EventListener<? super ExpandedChangedEvent>)` → EventSubscription — Registers a listener invoked when the panel expands or collapses.
- `removeContent(Widget)` → ExpandablePanel — Removes a content widget from the expandable body.
- `richTitle()` → RichText — Returns the RichText formatted title.
- `richTitle(RichText)` → ExpandablePanel — Sets the RichText formatted title.
- `silentExpanded(boolean)` → ExpandablePanel (default: `true`) — Sets the expanded state without triggering event callbacks.
- `title()` → String — Returns the plain text title string.
- `title(String)` → ExpandablePanel — Sets the plain text title.
- `toggleExpanded()` → ExpandablePanel — Toggles between expanded and collapsed states.

