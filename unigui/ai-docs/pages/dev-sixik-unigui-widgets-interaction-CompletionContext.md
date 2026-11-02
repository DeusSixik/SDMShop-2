# `CompletionContext` (extends `Record`)

Context record supplied to completion providers containing editor text and cursor position.

- **Purpose:** Supplies cursor position, line numbers, and text to CompletionProvider queries.

### USE WHEN

- Calculating autocomplete choices for CodeEditor.

### CRITICAL RULES & GOTCHAS:

- caretIndex is the zero-based offset in text where completion was invoked.

- **Related:** `CompletionProvider`, `CompletionItem`

