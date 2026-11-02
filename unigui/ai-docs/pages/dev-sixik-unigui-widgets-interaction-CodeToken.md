# `CodeToken` (extends `Record`)

Immutable range token associating character indices with a TokenStyle.

- **Purpose:** Represents a single highlighted syntax token range in source text.

### USE WHEN

- Emitted by CodeTokenizer implementations.

### CRITICAL RULES & GOTCHAS:

- startIndex is inclusive, endIndex is exclusive.

- **Related:** `TokenStyle`, `CodeTokenizer`

