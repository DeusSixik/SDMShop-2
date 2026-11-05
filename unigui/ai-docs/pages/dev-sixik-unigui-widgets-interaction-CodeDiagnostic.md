# `CodeDiagnostic` (extends `Record`)

Diagnostic report entry representing an error, warning, or hint at a specific code range.

- **Purpose:** Represents a single static analysis finding reported to the code editor user.

### USE WHEN

- Emitted by CodeValidator implementations.

### CRITICAL RULES & GOTCHAS:

- Severity determines icon, squiggle color, and status bar impact.

- **Related:** `CodeValidator`, `CodeEditor`

