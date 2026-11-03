---
title: "CodeEditor"
slug: "code-editor"
category: "Widgets/Interaction"
locale: "ru"
order: 260
---

# CodeEditor

Продвинутый редактор кода с подсветкой синтаксиса, диагностикой ошибок, нумерацией строк и автодополнением.

## Обзор

`CodeEditor` расширяет `TextArea` и предоставляет полнофункциональный редактор исходного кода. Он интегрирует лексическую подсветку синтаксиса через `CodeTokenizer`, вывод ошибок и предупреждений через `CodeValidator` и всплывающее меню автодополнения через `CompletionProvider`.

## Настройка CodeEditor

```java
CodeEditor editor = new CodeEditor()
        .languagePreset(CodeLanguagePreset.XML)
        .showLineNumbers(true)
        .text("<Widget type=\"Button\"/>");
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CodeEditor::</span>`completionProvider(CompletionProvider)` {: #completionprovidercompletionprovider data-toc-label="completionProvider(CompletionProvider)" }

**Задать провайдер автодополнения**

Устанавливает провайдер вариантов автодополнения.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `completionProvider` | `CompletionProvider` | — |

Возвращает `CodeEditor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CodeEditor::</span>`dirty()` {: #dirty data-toc-label="dirty()" }

**Флаг изменений**

Возвращает true, если текст редактора содержит несохраненные изменения.

Возвращает `boolean`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CodeEditor::</span>`languagePreset(CodeLanguagePreset)` {: #languagepresetcodelanguagepreset data-toc-label="languagePreset(CodeLanguagePreset)" }

**Задать пресет языка**

Применяет настройки токенизатора и валидатора из указанного пресета языка.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `languagePreset` | `CodeLanguagePreset` | — |

Возвращает `CodeEditor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CodeEditor::</span>`lineNumbersVisible(boolean)` {: #linenumbersvisibleboolean data-toc-label="lineNumbersVisible(boolean)" }

**Отображение номеров строк**

Включает или выключает отображение колонки с номерами строк.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `lineNumbersVisible` | `boolean` | — |

Возвращает `CodeEditor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CodeEditor::</span>`markClean()` {: #markclean data-toc-label="markClean()" }

**Сбросить флаг изменений**

Сбрасывает флаг изменений после успешного сохранения текста.

Возвращает `CodeEditor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CodeEditor::</span>`tokenizer(CodeTokenizer)` {: #tokenizercodetokenizer data-toc-label="tokenizer(CodeTokenizer)" }

**Задать токенизатор**

Устанавливает пользовательский токенизатор подсветки синтаксиса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `tokenizer` | `CodeTokenizer` | — |

Возвращает `CodeEditor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CodeEditor::</span>`validator(CodeValidator)` {: #validatorcodevalidator data-toc-label="validator(CodeValidator)" }

**Задать валидатор**

Устанавливает валидатор статического анализа кода.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `validator` | `CodeValidator` | — |

Возвращает `CodeEditor`.

</div>

