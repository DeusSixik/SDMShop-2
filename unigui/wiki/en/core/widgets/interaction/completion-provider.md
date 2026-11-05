---
title: "CompletionProvider"
slug: "completion-provider"
category: "Core/Widgets/Interaction"
locale: "en"
order: 350
---

# CompletionProvider

Interface producing autocompletion proposals based on editor cursor context.

## Overview

`CompletionProvider` inspects the document text and caret offset in `CompletionContext` to generate a list of candidate `CompletionItem` choices.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CompletionProvider::</span>`complete(CompletionContext)` {: #completecompletioncontext data-toc-label="complete(CompletionContext)" }

**Compute Completions**

Generates completion items for current cursor context.

| Parameter | Type | Description |
| --- | --- | --- |
| `context` | `CompletionContext` | — |

Returns `List`.

</div>

