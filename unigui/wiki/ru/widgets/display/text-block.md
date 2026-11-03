---
title: "TextBlock"
slug: "text-block"
category: "Widgets/Display"
locale: "ru"
order: 40
---

# TextBlock

Виджет отображения многострочных абзацев текста с поддержкой переноса, межстрочного интервала и выравнивания.

## Обзор

`TextBlock` оптимизирован для отображения описательных абзацев, документации и многострочных пояснений с настраиваемым межстрочным интервалом и выравниванием абзаца.

## Абзац текста

```java
TextBlock paragraph = new TextBlock()
        .text("UniGUI provides modern, hardware-accelerated UI rendering for Minecraft.")
        .wrap(true);
```

