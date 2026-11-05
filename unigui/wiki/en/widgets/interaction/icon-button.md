---
title: "IconButton"
slug: "icon-button"
category: "Widgets/Interaction"
locale: "en"
order: 40
---

# IconButton

Standalone graphic icon button without text.

## Overview

`IconButton` extends `ToolButton` to render a clean icon glyph without borders or labels, ideal for close buttons, window controls, and media icons.

## Close Window [Button](<button.md>)

```java
IconButton closeBtn = new IconButton("icons/close.png");
closeBtn.onClick(e -> window.close());
```

## Related links

- [ToolButton](<tool-button.md>)

