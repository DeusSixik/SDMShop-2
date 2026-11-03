---
title: "TextInput"
slug: "text-input"
category: "Widgets/Interaction"
locale: "ru"
order: 110
---

# TextInput

Базовое однострочное поле ввода текста с выделением, перемещением курсора, буфером обмена и фокусной рамкой.

## Обзор

`TextInput` расширяет `Box`, предоставляя редактируемое однострочное поле ввода на базе `TextEditorModel`. Поддерживает мигание курсора, выделение мышью, двойной клик для выделения слова, копирование/вставку через буфер обмена, ограничение длины и текст-подсказку (placeholder).

Оформление поля адаптивно меняется между обычным состоянием, наведением и фокусом.

## Поле ввода имени

```java
TextInput username = new TextInput()
        .placeholder("Введите имя...")
        .maxLength(24);
username.onTextChanged(e -> validateUser(e.newText()));
```

## Связанные ссылки

- [TextArea](<text-area.md>)
- [TextField](<text-field.md>)

## API

<div class="api-member-card" markdown="1">

### `TextInput()` {: #textinput data-toc-label="TextInput()" }

**Конструктор**

Создаёт пустое поле TextInput.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TextInput::</span>`onTextChanged(EventListener)` {: #ontextchangedeventlistener data-toc-label="onTextChanged(EventListener)" }

**Слушатель изменения текста**

Регистрирует коллбэк, вызываемый при любом изменении текста в поле.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | — |

Возвращает `EventSubscription`.

</div>

