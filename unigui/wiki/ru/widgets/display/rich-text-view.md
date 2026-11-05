---
title: "RichTextView"
slug: "rich-text-view"
category: "Widgets/Display"
locale: "ru"
order: 50
---

# RichTextView

Виджет форматированного [RichText](<../../core/text/rich-text.md>) текста со стилизованными фрагментами, иконками, цветами и ссылками.

## Обзор

`RichTextView` отображает форматированные составные объекты `RichText`, содержащие стилизованные фрагменты текста, полужирное/курсивное начертание, цветовые выделения, встроенные иконки и кликабельные ссылки.

## Отображение [RichText](<../../core/text/rich-text.md>)

```java
RichText styled = RichText.builder()
        .text("Server Status: ").color(Color.GRAY)
        .text("ONLINE").color(Color.GREEN).bold()
        .build();
RichTextView view = new RichTextView(styled);
```

