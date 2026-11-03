---
title: "ToolButton"
slug: "tool-button"
category: "Widgets/Interaction"
locale: "ru"
order: 30
---

# ToolButton

Компактная квадратная кнопка панели инструментов с иконкой, подсказкой и стилем тулбара.

## Обзор

`ToolButton` расширяет `Button`, предоставляя компактные квадратные пропорции и визуальный стиль панели инструментов.

## Кнопка панели инструментов

```java
ToolButton cutBtn = new ToolButton("Вырезать")
        .icon("icons/cut.png");
cutBtn.onClick(e -> performCut());
```

## Связанные ссылки

- [Button](<button.md>)
- [ToggleToolButton](<toggle-tool-button.md>)

