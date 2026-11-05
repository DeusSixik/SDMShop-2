---
title: "RadioButton"
slug: "radio-button"
category: "Widgets/Interaction"
locale: "ru"
order: 90
---

# RadioButton

Круглый переключатель для взаимоисключающего выбора внутри [RadioGroup](<../../core/widgets/interaction/radio-group.md>).

## Обзор

`RadioButton` отображает круглый переключатель рядом с текстом. При объединении в `RadioGroup` выбор одной кнопки автоматически снимает выбор со всех остальных кнопок группы.

## Выбор опции

```java
RadioGroup difficulty = new RadioGroup();
RadioButton easy = new RadioButton("Легко", "easy").group(difficulty);
RadioButton hard = new RadioButton("Сложно", "hard").group(difficulty);
easy.checked(true);
```

## Связанные ссылки

- [Checkbox](<checkbox.md>)
- [RadioGroup](<../../core/widgets/interaction/radio-group.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">RadioButton::</span>`group(RadioGroup)` {: #groupradiogroup data-toc-label="group(RadioGroup)" }

**Привязать [RadioGroup](<../../core/widgets/interaction/radio-group.md>)**

Привязывает группу [RadioGroup](<../../core/widgets/interaction/radio-group.md>), управляющую взаимоисключающим выбором.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `group` | `RadioGroup` | — |

Возвращает `RadioButton`: этот виджет для fluent-настройки.

</div>

