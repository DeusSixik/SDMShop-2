---
title: "CodeEditor"
slug: "code-editor"
category: "Widgets/Interaction"
locale: "en"
order: 260
---

# CodeEditor

Rich code editor widget supporting syntax highlighting, diagnostics, line numbers, and autocompletion.

## Overview

`CodeEditor` extends `TextArea` to provide a full-featured code editor. It integrates syntax tokenization via `CodeTokenizer`, static analysis diagnostics via `CodeValidator`, and intelligent code completion via `CompletionProvider`.

## Basic CodeEditor Setup

```java
CodeEditor editor = new CodeEditor()
        .languagePreset(CodeLanguagePreset.XML)
        .showLineNumbers(true)
        .text("<Widget type=\"Button\"/>");
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CodeEditor::</span>`completionProvider(CompletionProvider)` {: #completionprovidercompletionprovider data-toc-label="completionProvider(CompletionProvider)" }

**Set Completion Provider**

Sets code autocompletion provider.

| Parameter | Type | Description |
| --- | --- | --- |
| `completionProvider` | `CompletionProvider` | — |

Returns `CodeEditor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CodeEditor::</span>`dirty()` {: #dirty data-toc-label="dirty()" }

**Is Dirty**

Returns true if editor text has unsaved changes.

Returns `boolean`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CodeEditor::</span>`languagePreset(CodeLanguagePreset)` {: #languagepresetcodelanguagepreset data-toc-label="languagePreset(CodeLanguagePreset)" }

**Set Language Preset**

Applies tokenizer and validator configurations from language preset.

| Parameter | Type | Description |
| --- | --- | --- |
| `languagePreset` | `CodeLanguagePreset` | — |

Returns `CodeEditor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CodeEditor::</span>`lineNumbersVisible(boolean)` {: #linenumbersvisibleboolean data-toc-label="lineNumbersVisible(boolean)" }

**Show Line Numbers**

Toggles display of gutter line numbers.

| Parameter | Type | Description |
| --- | --- | --- |
| `lineNumbersVisible` | `boolean` | — |

Returns `CodeEditor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CodeEditor::</span>`markClean()` {: #markclean data-toc-label="markClean()" }

**Mark Clean**

Resets dirty state marker after saving text.

Returns `CodeEditor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CodeEditor::</span>`tokenizer(CodeTokenizer)` {: #tokenizercodetokenizer data-toc-label="tokenizer(CodeTokenizer)" }

**Set Tokenizer**

Sets custom syntax tokenizer.

| Parameter | Type | Description |
| --- | --- | --- |
| `tokenizer` | `CodeTokenizer` | — |

Returns `CodeEditor`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CodeEditor::</span>`validator(CodeValidator)` {: #validatorcodevalidator data-toc-label="validator(CodeValidator)" }

**Set Validator**

Sets code diagnostic validator.

| Parameter | Type | Description |
| --- | --- | --- |
| `validator` | `CodeValidator` | — |

Returns `CodeEditor`.

</div>

