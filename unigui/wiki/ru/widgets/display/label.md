---
title: "Label"
slug: "label"
category: "Widgets/Display"
locale: "ru"
order: 20
---

# Label

Текстовая метка, связанная с интерактивным элементом ввода или управления.

## Обзор

`Label` отображает однострочную или переносимую текстовую метку. При нажатии на метку фокус ввода автоматически передается на связанный виджет `focusTarget` (например, `TextField` или `Checkbox`), повышая удобство форм.

## Метка поля формы

```java
TextField nameInput = new TextField();
Label nameLabel = new Label("Username:")
        .labeledControl(nameInput);
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Label::</span>`color(ColorView)` {: #colorcolorview data-toc-label="color(ColorView)" }

**Задать цвет текста**

Устанавливает цвет шрифта текстовой метки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `color` | `ColorView` | — |

Возвращает `Label`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Label::</span>`focusTarget(Widget)` {: #focustargetwidget data-toc-label="focusTarget(Widget)" }

**Целевой виджет фокуса**

Возвращает или устанавливает виджет, получающий фокус при клике по метке.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `focusTarget` | `Widget` | — |

Возвращает `Label`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Label::</span>`labeledControl(Widget)` {: #labeledcontrolwidget data-toc-label="labeledControl(Widget)" }

**Связать с элементом**

Связывает элемент управления в качестве цели фокуса при клике.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `control` | `Widget` | — |

Возвращает `Label`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Label::</span>`textAlignment(Alignment)` {: #textalignmentalignment data-toc-label="textAlignment(Alignment)" }

**Выравнивание текста**

Устанавливает горизонтальное выравнивание текста метки в границах.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `alignment` | `Alignment` | — |

Возвращает `Label`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Label::</span>`wrap(boolean)` {: #wrapboolean data-toc-label="wrap(boolean)" }

**Перенос строк**

Включает или выключает автоматический перенос длинных строк.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `wrap` | `boolean` | — |

Возвращает `Label`.

</div>

