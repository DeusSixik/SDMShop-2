---
title: "ToggleToolButton"
slug: "toggle-tool-button"
category: "Widgets/Interaction"
locale: "en"
order: 60
---

# ToggleToolButton

Two-state toolbar button combining [ToolButton](<tool-button.md>) styling with [ToggleButton](<toggle-button.md>) mechanics.

## Overview

`ToggleToolButton` is a toolbar button that stays highlighted when toggled active (e.g. Bold, Italic, Magnet tools).

## Toolbar Bold Toggle

```java
ToggleToolButton boldBtn = new ToggleToolButton()
        .icon("icons/bold.png")
        .checked(false);
boldBtn.onCheckedChanged(e -> textEditor.setBold(e.newValue()));
```

## Related links

- [ToggleButton](<toggle-button.md>)
- [ToolButton](<tool-button.md>)

