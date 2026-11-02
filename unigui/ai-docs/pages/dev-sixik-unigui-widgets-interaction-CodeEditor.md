# `CodeEditor` (extends `TextArea`)

Rich code editor widget supporting syntax highlighting, diagnostics, line numbers, and autocompletion.

- **Purpose:** Provides a complete text editing environment for source code, configuration files, and script authoring.

### USE WHEN

- Editing code or XML/JSON documents within the UI.
- In-game script editors, shader tweak panes, or configuration file viewing.

### CRITICAL RULES & GOTCHAS:

- Integrates CodeTokenizer for lexical styling and CodeValidator for real-time error annotations.
- CompletionProvider handles popup suggestion list when typing.

- **Related:** `XmlCodeEditor`, `CodeTokenizer`, `CodeValidator`, `CompletionProvider`, `TextArea`

