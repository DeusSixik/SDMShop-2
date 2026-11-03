---
title: "ToolButton"
slug: "tool-button"
category: "Widgets/Interaction"
locale: "en"
order: 30
---

# ToolButton

Compact square toolbar button with icon, tooltip label, and toolbar styling.

## Overview

`ToolButton` extends `Button` with square dimensions and streamlined toolbar aesthetics. It is typically housed inside a toolbar row alongside other command tools.

## Toolbar [Button](<button.md>)

```java
ToolButton cutBtn = new ToolButton("Cut")
        .icon("icons/cut.png");
cutBtn.onClick(e -> performCut());
```

## Related links

- [Button](<button.md>)
- [ToggleToolButton](<toggle-tool-button.md>)

