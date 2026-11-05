---
title: "AdminCommandRegistry"
slug: "admin-command-registry"
category: "Core/Widgets/Interaction"
locale: "en"
order: 390
---

# AdminCommandRegistry

Central registry for console commands with argument completion, alias resolution, and handlers.

## Overview

`AdminCommandRegistry` manages registered commands, dispatches execution, and calculates parameter autocompletions for `AdminConsole`.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AdminCommandRegistry::</span>`commands()` {: #commands data-toc-label="commands()" }

**All Commands**

Returns unmodifiable list of registered commands.

Returns `List`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AdminCommandRegistry::</span>`find(String)` {: #findstring data-toc-label="find(String)" }

**Find Command**

Finds command definition matching name or alias.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | — |

Returns `CommandDefinition`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AdminCommandRegistry::</span>`register(String, String, CommandExecutor)` {: #registerstring-string-commandexecutor data-toc-label="register(String, String, CommandExecutor)" }

**Register Command**

Registers a command definition or handler.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | — |
| `description` | `String` | — |
| `executor` | `CommandExecutor` | — |

Returns `AdminCommandRegistry`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AdminCommandRegistry::</span>`unregister(String)` {: #unregisterstring data-toc-label="unregister(String)" }

**Unregister Command**

Removes a command by name.

| Parameter | Type | Description |
| --- | --- | --- |
| `name` | `String` | — |

Returns `AdminCommandRegistry`.

</div>

