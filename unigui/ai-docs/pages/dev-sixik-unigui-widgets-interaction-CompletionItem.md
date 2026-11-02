# `CompletionItem` (extends `Record`)

Individual autocompletion suggestion with rich label, insertion text, and replacement range.

- **Purpose:** Represents a single candidate item within the CodeEditor autocomplete popup menu.

### USE WHEN

- Emitted by CompletionProvider implementations.

### CRITICAL RULES & GOTCHAS:

- insertText defines the characters inserted on confirmation.

- **Related:** `CompletionProvider`, `CompletionContext`

