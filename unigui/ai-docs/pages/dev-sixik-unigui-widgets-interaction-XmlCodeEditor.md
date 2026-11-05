# `XmlCodeEditor` (extends `CodeEditor`)

Specialized XML and UI layout code editor with schema validation, formatting, and tag autocompletion.

- **Purpose:** Provides specialized tooling for authoring UniGUI XML layout files with instant error checking and formatting.

### USE WHEN

- Live in-game UI designer and XML document preview.
- Custom UI file editor screens.

### CRITICAL RULES & GOTCHAS:

- Automatically registers XML tokenizers and schema validators.
- formatXml() safely normalizes tags and indentation.

- **Related:** `CodeEditor`, `CodeTokenizer`, `CodeValidator`

