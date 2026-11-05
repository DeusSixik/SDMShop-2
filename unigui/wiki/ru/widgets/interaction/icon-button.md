---
title: "IconButton"
slug: "icon-button"
category: "Widgets/Interaction"
locale: "ru"
order: 40
---

# IconButton

Кнопка с графической иконкой без текстовой подписи.

## Обзор

`IconButton` расширяет `ToolButton` для отображения чистой графической иконки без лишних рамок, отлично подходит для кнопок закрытия и медиа-элементов.

## Кнопка закрытия окна

```java
IconButton closeBtn = new IconButton("icons/close.png");
closeBtn.onClick(e -> window.close());
```

## Связанные ссылки

- [ToolButton](<tool-button.md>)

