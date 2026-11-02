# `NodeType`

Catalog entry for a node kind: id, title, category and a factory assembling the node from modules.

- **Purpose:** Separates what a node is from where it is placed, enabling palettes, search and save/restore.

### USE WHEN

- Register Blueprint-like node kinds (events, functions, variables) with categories.
- Spawn a node by type id from a context menu or when restoring a snapshot.

### CRITICAL RULES & GOTCHAS:

- The factory receives the new item id and must return a fresh node on every call.
- `create` stamps the node with this type's id.

- **Related:** `NodeTypeRegistry`, `NodeBuilder`, `NodeGraphItem`

