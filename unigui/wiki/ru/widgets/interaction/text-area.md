---
title: "TextArea"
slug: "text-area"
category: "Widgets/Interaction"
locale: "ru"
order: 170
---

# TextArea

Многострочный текстовый редактор с переносом строк, навигацией курсора, выделением и полосой прокрутки.

## Обзор

`TextArea` расширяет `Box`, предоставляя полноценный многострочный редактор текста. Поддерживает переносы строк, вертикальный и горизонтальный скроллинг (`scrollX`, `scrollY`), метрики строк, режим «только чтение» и многострочное выделение.

## Поле заметок

```java
TextArea notes = new TextArea()
        .placeholder("Введите заметки...")
        .visibleLines(8)
        .readOnly(false);
```

## Связанные ссылки

- [CodeEditor](<code-editor.md>)
- [TextInput](<text-input.md>)

