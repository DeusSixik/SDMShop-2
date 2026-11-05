---
title: "CodeValidator"
slug: "code-validator"
category: "Core/Виджеты/Взаимодействие"
locale: "ru"
order: 330
---

# CodeValidator

Функциональный интерфейс статического анализа текста, формирующий диагностические сообщения об ошибках.

## Обзор

`CodeValidator` анализирует содержимое документа и возвращает список объектов `CodeDiagnostic`, указывающих на ошибки, предупреждения или подсказки.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CodeValidator::</span>`validate(CodeValidationContext)` {: #validatecodevalidationcontext data-toc-label="validate(CodeValidationContext)" }

**Проверить код**

Выполняет статический анализ кода в контексте валидации и возвращает список диагностик.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `context` | `CodeValidationContext` | — |

Возвращает `List`.

</div>

