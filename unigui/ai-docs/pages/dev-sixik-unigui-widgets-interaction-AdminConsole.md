# `AdminConsole` (extends `Box`)

Interactive developer and administrative console widget with command execution and log output.

- **Purpose:** Provides an in-game terminal for debugging, command dispatching, and formatted log viewing.

### USE WHEN

- Developer debugging overlays or admin control screens.
- Custom command-line interfaces within Minecraft GUIs.

### CRITICAL RULES & GOTCHAS:

- Integrates with AdminCommandRegistry for command definitions and autocompletion.
- Stores history of executed commands accessible via Up and Down arrow keys.

- **Related:** `AdminCommandRegistry`, `Box`, `TextField`

