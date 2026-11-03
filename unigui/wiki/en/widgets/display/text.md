---
title: "Text"
slug: "text"
category: "Widgets/Display"
locale: "en"
order: 30
---

# Text

Lightweight text display widget for static or dynamic strings and [RichText](<../../core/text/rich-text.md>).

## Overview

`Text` is a lightweight display component extending `TextWidget`. Unlike `Label`, it does not handle focus delegation, making it ideal for headers, read-only stats, and notifications.

## Simple Text [Widget](<../widget.md>)

```java
Text title = new Text("Welcome to UniGUI!")
        .color(Color.YELLOW);
```

