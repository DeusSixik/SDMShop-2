---
title: "Text"
slug: "text"
category: "Widgets/Display"
locale: "ru"
order: 30
---

# Text

Легковесный виджет отображения статических или динамических строк и [RichText](<../../core/text/rich-text.md>).

## Обзор

`Text` — легковесный компонент отображения текста, расширяющий `TextWidget`. В отличие от `Label`, он не делегирует фокус ввода, что делает его оптимальным для заголовков, статических надписей и уведомлений.

## Простой текст

```java
Text title = new Text("Welcome to UniGUI!")
        .color(Color.YELLOW);
```

