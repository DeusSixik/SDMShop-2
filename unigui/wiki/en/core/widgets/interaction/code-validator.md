---
title: "CodeValidator"
slug: "code-validator"
category: "Core/Widgets/Interaction"
locale: "en"
order: 330
---

# CodeValidator

Functional interface performing static analysis and reporting code diagnostics.

## Overview

`CodeValidator` inspects document contents and returns a collection of `CodeDiagnostic` objects indicating errors, warnings, or hints.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">CodeValidator::</span>`validate(CodeValidationContext)` {: #validatecodevalidationcontext data-toc-label="validate(CodeValidationContext)" }

**Validate Code**

Analyzes code within validation context and returns diagnostics.

| Parameter | Type | Description |
| --- | --- | --- |
| `context` | `CodeValidationContext` | — |

Returns `List`.

</div>

