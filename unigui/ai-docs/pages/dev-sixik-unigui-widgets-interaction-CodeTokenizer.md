# `CodeTokenizer`

Functional interface converting source code text into colored syntax tokens.

- **Purpose:** Enables custom lexical tokenization and syntax highlighting rules for CodeEditor.

### USE WHEN

- Implementing syntax highlighting for custom domain-specific languages.
- Customizing token coloring for existing languages.

### CRITICAL RULES & GOTCHAS:

- Tokens must have non-overlapping, monotonically increasing index spans.

- **Related:** `CodeToken`, `TokenStyle`, `CodeTokenizationContext`, `CodeEditor`

