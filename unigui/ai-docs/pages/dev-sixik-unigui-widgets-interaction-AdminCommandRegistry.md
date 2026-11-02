# `AdminCommandRegistry`

Central registry for console commands with argument completion, alias resolution, and handlers.

- **Purpose:** Manages registered console command handlers and calculates parameter completion candidates.

### USE WHEN

- Registering custom developer commands for AdminConsole.
- Custom autocomplete suggestion providers for command arguments.

### CRITICAL RULES & GOTCHAS:

- Commands can have aliases and typed argument suggestion providers.

- **Related:** `AdminConsole`

