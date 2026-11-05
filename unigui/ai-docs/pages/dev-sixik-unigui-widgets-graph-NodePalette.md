# `NodePalette` (extends `Box`, implements `OverlayHostAware`, implements `OverlayDismissable`)

Floating searchable node picker opened at a point over an OverlayLayer: a collapsible category tree, or a flat ranked list while typing.

- **Purpose:** Lets users add nodes to a graph with the mouse and keyboard instead of code.

### USE WHEN

- Open it from a custom button or shortcut with `openAt(x, y)` and react in `onPick`.
- Restrict the list with `filter(...)`, for example to nodes that accept a given output type.
- Group node types by giving them a category path such as `Math/Vector`.

### CRITICAL RULES & GOTCHAS:

- It is an overlay: it only works inside an OverlayLayer and closes itself on outside clicks and Escape.
- Opening resets the query and focuses the search field, but remembers which groups were expanded.
- Picking closes the palette first and then invokes the callback.
- Clicking or pressing Enter on a group toggles it and never closes the palette.

- **Related:** `NodePaletteModel`, `NodeTypeRegistry`, `NodeGraph`, `OverlayLayer`

