# `NodeTypeRegistry`

Catalog of NodeTypes with category grouping and ranked text search.

- **Purpose:** Backs 'add node' menus and snapshot restore for modular node editors.

### USE WHEN

- Register all node kinds of an editor once and reuse them in menus and persistence.
- Implement type-ahead search over node titles and categories.

### CRITICAL RULES & GOTCHAS:

- Registering an id twice replaces the previous type.
- Search is case-insensitive; prefix matches rank before substring matches.

- **Related:** `NodeType`, `NodeBuilder`

