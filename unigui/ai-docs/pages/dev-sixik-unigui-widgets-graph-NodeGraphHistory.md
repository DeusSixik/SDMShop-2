# `NodeGraphHistory`

Undo/redo history of a NodeGraph recorded in explicit transactions; each transaction is one undo step.

- **Purpose:** Makes node graph editing reversible without serializing nodes: operations hold the original objects.

### USE WHEN

- Wrap programmatic edits in `graph.transact(name, ...)` to make them undoable as one step.
- Bind `undo()`/`redo()` to toolbar buttons and read `undoName()` for their tooltips.

### CRITICAL RULES & GOTCHAS:

- Nothing is recorded outside a transaction.
- A new recorded step discards the redo stack.
- Undo and redo are ignored while a transaction is open.
- If replaying a step throws, the history is cleared and the exception is rethrown.

- **Related:** `NodeGraph`

