---
title: "CompletionProvider"
slug: "completion-provider"
category: "Core/Виджеты/Взаимодействие"
locale: "ru"
order: 350
---

# CompletionProvider

Интерфейс формирования предложений автодополнения на основе контекста курсора.

## Обзор

`CompletionProvider` анализирует текст документа и позицию курсора в `CompletionContext` для генерации списка вариантов `CompletionItem`.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CompletionProvider::</span>`complete(CompletionContext)` {: #completecompletioncontext data-toc-label="complete(CompletionContext)" }

**Вычислить варианты дополнения**

Формирует список вариантов автодополнения для текущей позиции курсора.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `context` | `CompletionContext` | — |

Возвращает `List`.

</div>

