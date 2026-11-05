# `CodeValidationContext` (extends `Record`)

Context record passed to code validators containing document text and language ID.

- **Purpose:** Provides document source code and language identifier to CodeValidator.

### USE WHEN

- Executing static analysis passes in CodeEditor.

### CRITICAL RULES & GOTCHAS:

- text is the complete current document string.

- **Related:** `CodeValidator`, `CodeDiagnostic`

