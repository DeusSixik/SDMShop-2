---
title: "AdminCommandRegistry"
slug: "admin-command-registry"
category: "Core/Виджеты/Взаимодействие"
locale: "ru"
order: 390
---

# AdminCommandRegistry

Центральный реестр команд консоли с поддержкой автодополнения аргументов, псевдонимов и обработчиков.

## Обзор

`AdminCommandRegistry` управляет зарегистрированными командами, выполняет их диспетчеризацию и вычисляет варианты автодополнения параметров для `AdminConsole`.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AdminCommandRegistry::</span>`commands()` {: #commands data-toc-label="commands()" }

**Все команды**

Возвращает неизменяемый список всех зарегистрированных команд.

Возвращает `List`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AdminCommandRegistry::</span>`find(String)` {: #findstring data-toc-label="find(String)" }

**Найти команду**

Ищет зарегистрированную команду по имени или псевдониму.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | — |

Возвращает `CommandDefinition`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AdminCommandRegistry::</span>`register(String, String, CommandExecutor)` {: #registerstring-string-commandexecutor data-toc-label="register(String, String, CommandExecutor)" }

**Зарегистрировать команду**

Регистрирует определение команды или обработчик.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | — |
| `description` | `String` | — |
| `executor` | `CommandExecutor` | — |

Возвращает `AdminCommandRegistry`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">AdminCommandRegistry::</span>`unregister(String)` {: #unregisterstring data-toc-label="unregister(String)" }

**Удалить команду**

Удаляет команду из реестра по имени.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `name` | `String` | — |

Возвращает `AdminCommandRegistry`.

</div>

