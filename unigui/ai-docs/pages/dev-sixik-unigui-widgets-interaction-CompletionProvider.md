# `CompletionProvider`

Interface producing autocompletion proposals based on editor cursor context.

- **Purpose:** Provides contextual autocomplete candidates when typing in CodeEditor.

### USE WHEN

- Implementing tag, keyword, or identifier autocompletion.

### CRITICAL RULES & GOTCHAS:

- Returns list of CompletionItem suggestions displayed in an overlay popup.

- **Related:** `CompletionItem`, `CompletionContext`, `CodeEditor`

