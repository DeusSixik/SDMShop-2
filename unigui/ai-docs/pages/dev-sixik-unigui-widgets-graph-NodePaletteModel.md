# `NodePaletteModel`

Widget-free model of the node palette: category tree or flat ranked search results, selection and a scrolling window.

- **Purpose:** Keeps palette logic testable and reusable independently of how the list is drawn.

### USE WHEN

- Back a custom node picker with the same tree, ranking and keyboard navigation as NodePalette.
- Test palette behavior headless.

### CRITICAL RULES & GOTCHAS:

- An empty query shows the category tree; any query shows a flat ranked list without category rows.
- Changing the query or the filter resets the selection to the first entry; toggling a category keeps it.
- `move` wraps around at both ends; the visible window always contains the selected entry.
- Categories without any matching node are never shown.

- **Related:** `NodePalette`, `NodeTypeRegistry`, `NodeType`

