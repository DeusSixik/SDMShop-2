---
title: "CodeTokenizer"
slug: "code-tokenizer"
category: "Core/Виджеты/Взаимодействие"
locale: "ru"
order: 300
---

# CodeTokenizer

Функциональный интерфейс для разбиения исходного кода на токены с цветовыми стилями.

## Обзор

Реализации `CodeTokenizer` сканируют текст редактора в контексте `CodeTokenizationContext` и возвращают список диапазонов `CodeToken` для подсветки синтаксиса.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CodeTokenizer::</span>`tokenize(CodeTokenizationContext)` {: #tokenizecodetokenizationcontext data-toc-label="tokenize(CodeTokenizationContext)" }

**Токенизировать текст**

Разбивает строку исходного текста на список стилизованных токенов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `context` | `CodeTokenizationContext` | — |

Возвращает `List`.

</div>

