---
title: "TextInput"
slug: "text-input"
category: "Widgets/Interaction"
locale: "en"
order: 110
---

# TextInput

Base single-line text input with selection, caret navigation, clipboard integration, and reactive focus chrome.

## Overview

`TextInput` extends `Box` to provide single-line editable text input backed by `TextEditorModel`. It features smooth caret blinking, mouse drag/click selection, double-click word selection, clipboard copy/cut/paste, max length limits, and placeholder text.

Input chrome dynamically updates between normal, hovered, and focused states with dedicated border and ring colors.

## Username Input

```java
TextInput username = new TextInput()
        .placeholder("Enter username...")
        .maxLength(24);
username.onTextChanged(e -> validateUser(e.newText()));
```

## Related links

- [TextArea](<text-area.md>)
- [TextField](<text-field.md>)

## API

<div class="api-member-card" markdown="1">

### `TextInput()` {: #textinput data-toc-label="TextInput()" }

**Constructor**

Creates an empty TextInput.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextInput::</span>`onTextChanged(EventListener)` {: #ontextchangedeventlistener data-toc-label="onTextChanged(EventListener)" }

**On [Text](<../display/text.md>) Changed Listener**

Registers a callback invoked whenever the text content changes.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `EventListener` | — |

Returns `EventSubscription`.

</div>

