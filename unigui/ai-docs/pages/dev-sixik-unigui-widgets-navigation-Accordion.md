# `Accordion` (extends `LinearBox`)

Vertical accordion container managing a stack of collapsible ExpandablePanel sections with optional single-panel open constraint.

### API

- `Accordion()` — Constructs an empty Accordion container.
- `addPanel(ExpandablePanel)` → Accordion — Appends an ExpandablePanel to this accordion.
- `clearPanels()` → Accordion — Removes all panels from this accordion.
- `panels()` → List<ExpandablePanel> — Returns an unmodifiable list of expandable panels hosted in this accordion.
- `removePanel(ExpandablePanel)` → Accordion — Removes an ExpandablePanel from this accordion.
- `singleOpen()` → boolean — Returns true if opening one panel automatically closes all other panels.
- `singleOpen(boolean)` → Accordion (default: `true`) — Sets whether only a single panel may be expanded simultaneously.

