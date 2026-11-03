---
title: "AdminConsole"
slug: "admin-console"
category: "Widgets/Interaction"
locale: "en"
order: 380
---

# AdminConsole

Interactive developer and administrative console widget with command execution and log output.

## Overview

`AdminConsole` provides an in-game terminal for debugging and executing commands. It integrates command autocompletion via `AdminCommandRegistry`, command history navigation, and colored log scrolling.

## Admin Console Setup

```java
AdminConsole console = new AdminConsole()
        .title("Developer Terminal")
        .prompt("> ");
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AdminConsole::</span>`commandRegistry()` {: #commandregistry data-toc-label="commandRegistry()" }

**Command Registry**

Returns command registry powering console execution.

Returns `AdminCommandRegistry`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AdminConsole::</span>`inputText()` {: #inputtext data-toc-label="inputText()" }

**Input [Text](<../display/text.md>)**

Returns or sets current command input string.

Returns `String`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AdminConsole::</span>`prompt(String)` {: #promptstring data-toc-label="prompt(String)" }

**Command Prompt**

Returns or sets prompt prefix string (e.g. "> ").

| Parameter | Type | Description |
| --- | --- | --- |
| `prompt` | `String` | — |

Returns `AdminConsole`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AdminConsole::</span>`title(String)` {: #titlestring data-toc-label="title(String)" }

**Console Title**

Returns or sets console window title text.

| Parameter | Type | Description |
| --- | --- | --- |
| `title` | `String` | — |

Returns `AdminConsole`.

</div>

