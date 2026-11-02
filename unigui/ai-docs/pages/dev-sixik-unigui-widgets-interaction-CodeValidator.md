# `CodeValidator`

Functional interface performing static analysis and reporting code diagnostics.

- **Purpose:** Allows plugging in real-time linting and syntax error checkers into CodeEditor.

### USE WHEN

- Validating configuration schemas, script syntax, or expression correctness.

### CRITICAL RULES & GOTCHAS:

- Returns list of CodeDiagnostic items displayed as squiggles and tooltips in the editor.

- **Related:** `CodeDiagnostic`, `CodeValidationContext`, `CodeEditor`

