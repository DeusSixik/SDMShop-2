---
title: "RichTextView"
slug: "rich-text-view"
category: "Widgets/Display"
locale: "en"
order: 50
---

# RichTextView

Formatted rich text widget rendering styled runs, inline icons, color spans, and links.

## Overview

`RichTextView` renders composite `RichText` objects containing multiple styled spans, bold/italic text, colored badges, inline texture icons, and clickable URL links.

## Displaying [RichText](<../../core/text/rich-text.md>)

```java
RichText styled = RichText.builder()
        .text("Server Status: ").color(Color.GRAY)
        .text("ONLINE").color(Color.GREEN).bold()
        .build();
RichTextView view = new RichTextView(styled);
```

