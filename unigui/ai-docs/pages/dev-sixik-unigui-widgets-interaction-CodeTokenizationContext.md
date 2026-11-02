# `CodeTokenizationContext` (extends `Record`)

Context record passed to syntax tokenizers containing source text and language ID.

- **Purpose:** Provides raw source string and language identifier to CodeTokenizer.

### USE WHEN

- Invoking syntax tokenizers in CodeEditor.

### CRITICAL RULES & GOTCHAS:

- languageId can be null if untyped text is edited.

- **Related:** `CodeTokenizer`, `CodeToken`

