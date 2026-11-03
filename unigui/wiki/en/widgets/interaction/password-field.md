---
title: "PasswordField"
slug: "password-field"
category: "Widgets/Interaction"
locale: "en"
order: 140
---

# PasswordField

Secure masked text input hiding characters with bullets or custom mask glyphs.

## Overview

`PasswordField` extends `TextInput` while rendering a mask character (such as `*` or `•`) instead of raw characters. The actual plain text remains accessible securely via `text()`.

## Login Password Input

```java
PasswordField password = new PasswordField()
        .placeholder("Password")
        .mask('*');
```

## Related links

- [TextInput](<text-input.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PasswordField::</span>`mask(char)` {: #maskchar data-toc-label="mask(char)" }

**Set Mask Character**

Sets the character glyph used to obscure password input.

| Parameter | Type | Description |
| --- | --- | --- |
| `mask` | `char` | mask char |

Returns `PasswordField`: this widget for fluent configuration.

</div>

