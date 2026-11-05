---
title: "TextArea"
slug: "text-area"
category: "Widgets/Interaction"
locale: "en"
order: 170
---

# TextArea

Multi-line scrollable text editor with line wrapping, cursor navigation, selection, and scrollbar support.

## Overview

`TextArea` extends `Box` to provide a complete multi-line text editor. It handles line breaks, soft/hard wrapping, vertical and horizontal scrolling (`scrollX`, `scrollY`), line metrics, read-only mode, and multi-line selection.

## Notes [Text](<../display/text.md>) Area

```java
TextArea notes = new TextArea()
        .placeholder("Write notes here...")
        .visibleLines(8)
        .readOnly(false);
```

## Related links

- [CodeEditor](<code-editor.md>)
- [TextInput](<text-input.md>)

