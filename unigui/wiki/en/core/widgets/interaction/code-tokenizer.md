---
title: "CodeTokenizer"
slug: "code-tokenizer"
category: "Core/Widgets/Interaction"
locale: "en"
order: 300
---

# CodeTokenizer

Functional interface converting source code text into colored syntax tokens.

## Overview

Implementations of `CodeTokenizer` scan editor text in a `CodeTokenizationContext` and emit a list of `CodeToken` ranges for syntax rendering.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CodeTokenizer::</span>`tokenize(CodeTokenizationContext)` {: #tokenizecodetokenizationcontext data-toc-label="tokenize(CodeTokenizationContext)" }

**Tokenize [Text](<../../../widgets/display/text.md>)**

Parses text into list of styled syntax tokens.

| Parameter | Type | Description |
| --- | --- | --- |
| `context` | `CodeTokenizationContext` | — |

Returns `List`.

</div>

