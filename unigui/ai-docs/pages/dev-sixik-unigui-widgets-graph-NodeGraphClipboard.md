# `NodeGraphClipboard`

Immutable copy of selected modular nodes and the wires between them, pasteable into the same or another graph.

- **Purpose:** Backs copy, paste and duplicate for node graphs.

### USE WHEN

- Copy a selection with `NodeGraph.copySelection()` and paste it with `paste(...)`.
- Move nodes between two graphs that share a NodeTypeRegistry.

### CRITICAL RULES & GOTCHAS:

- Only nodes whose type is registered are copied.
- Only wires with both ends inside the copied set are kept.
- Values of input editors are copied with the node.

- **Related:** `NodeGraph`, `NodeType`

