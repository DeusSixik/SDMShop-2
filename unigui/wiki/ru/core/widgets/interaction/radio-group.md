---
title: "RadioGroup"
slug: "radio-group"
category: "Core/Виджеты/Взаимодействие"
locale: "ru"
order: 100
---

# RadioGroup

Контроллер, управляющий взаимоисключающим выбором и состоянием группы радиокнопок.

## Обзор

`RadioGroup` — это логический контроллер, координирующий коллекцию кнопок `RadioButton`. Гарантирует, что только одна кнопка активна одновременно, и позволяет считывать выбранное значение.

## Управление группой

```java
RadioGroup group = new RadioGroup()
        .add(optionA)
        .add(optionB);
String selected = group.selectedValue();
```

## Связанные ссылки

- [RadioButton](<../../../widgets/interaction/radio-button.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RadioGroup::</span>`add(RadioButton)` {: #addradiobutton data-toc-label="add(RadioButton)" }

**Добавить радиокнопку**

Добавляет радиокнопку в управляемую группу взаимоисключающего выбора.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `button` | `RadioButton` | — |

Возвращает `RadioGroup`: этот виджет для fluent-настройки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RadioGroup::</span>`selectedValue()` {: #selectedvalue data-toc-label="selectedValue()" }

**Получить выбранное значение**

Возвращает строковое значение выбранной радиокнопки или null.

Возвращает `String`.

</div>

