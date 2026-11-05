# `CodeLanguagePresets`

Factory registry supplying tokenizers and validators for language presets.

- **Purpose:** Provides static helper methods to apply preconfigured language presets to CodeEditor.

### USE WHEN

- Setting up tokenizers and validators for known languages.
- Extending or retrieving built-in tokenizer implementations.

### CRITICAL RULES & GOTCHAS:

- apply() registers default styles and tokenizers on the specified CodeEditor.

- **Related:** `CodeLanguagePreset`, `CodeEditor`, `CodeTokenizer`

