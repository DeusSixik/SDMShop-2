---
title: "ToggleToolButton"
slug: "toggle-tool-button"
category: "Widgets/Interaction"
locale: "ru"
order: 60
---

# ToggleToolButton

Двухпозиционная кнопка панели инструментов, объединяющая стиль [ToolButton](<tool-button.md>) и функционал [ToggleButton](<toggle-button.md>).

## Обзор

`ToggleToolButton` — кнопка панели инструментов, остающаяся подсвеченной при активации (например, жирный шрифт, курсив, привязка к сетке).

## Переключатель жирного шрифта

```java
ToggleToolButton boldBtn = new ToggleToolButton()
        .icon("icons/bold.png")
        .checked(false);
boldBtn.onCheckedChanged(e -> textEditor.setBold(e.newValue()));
```

## Связанные ссылки

- [ToggleButton](<toggle-button.md>)
- [ToolButton](<tool-button.md>)

