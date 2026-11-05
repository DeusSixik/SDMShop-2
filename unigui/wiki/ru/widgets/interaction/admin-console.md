---
title: "AdminConsole"
slug: "admin-console"
category: "Widgets/Interaction"
locale: "ru"
order: 380
---

# AdminConsole

Интерактивная консоль разработчика и администратора с выполнением команд, историей и выводом логов.

## Обзор

`AdminConsole` реализует внутриигровой терминал для отладки и выполнения административных команд. Включает автодополнение команд через `AdminCommandRegistry`, историю ввода стрелками вверх/вниз и прокрутку цветного журнала сообщений.

## Использование AdminConsole

```java
AdminConsole console = new AdminConsole()
        .title("Developer Terminal")
        .prompt("> ");
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AdminConsole::</span>`commandRegistry()` {: #commandregistry data-toc-label="commandRegistry()" }

**Реестр команд**

Возвращает реестр команд, используемый консолью.

Возвращает `AdminCommandRegistry`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AdminConsole::</span>`inputText()` {: #inputtext data-toc-label="inputText()" }

**Текст ввода**

Возвращает или устанавливает текущую строку ввода команды.

Возвращает `String`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AdminConsole::</span>`prompt(String)` {: #promptstring data-toc-label="prompt(String)" }

**Приглашение командной строки**

Возвращает или устанавливает строку приглашения ввода (например, "> ").

| Параметр | Тип | Описание |
| --- | --- | --- |
| `prompt` | `String` | — |

Возвращает `AdminConsole`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AdminConsole::</span>`title(String)` {: #titlestring data-toc-label="title(String)" }

**Заголовок консоли**

Возвращает или устанавливает заголовок окна консоли.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `title` | `String` | — |

Возвращает `AdminConsole`.

</div>

