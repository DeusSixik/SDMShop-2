---
title: "ColorPicker"
slug: "color-picker"
category: "Widgets/Interaction"
locale: "ru"
order: 220
---

# ColorPicker

Интерактивный компонент выбора цвета с ползунками каналов RGBA, вводом hex и всплывающей палитрой.

## Обзор

`ColorPicker` отображает кнопку предварительного просмотра цвета и раскрывающееся диалоговое окно редактирования с поддержкой каналов RGBA, прозрачности, шестнадцатеричных кодов и событий изменения.

## Пример ColorPicker

```java
ColorPicker picker = new ColorPicker()
        .argb(0xFFFF8800);
picker.onColorChanged(e -> updateThemeColor(e.newColor()));
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ColorPicker::</span>`argb()` {: #argb data-toc-label="argb()" }

**Получить ARGB**

Возвращает текущий цвет в виде упакованного 32-битного целого числа ARGB.

Возвращает `int`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ColorPicker::</span>`argb(int)` {: #argbint data-toc-label="argb(int)" }

**Задать ARGB**

Устанавливает цвет из упакованного 32-битного целого числа ARGB.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `argb` | `int` | — |

Возвращает `ColorPicker`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ColorPicker::</span>`color()` {: #color data-toc-label="color()" }

**Получить цвет**

Возвращает текущий выбранный цвет в виде ColorView.

Возвращает `ColorView`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ColorPicker::</span>`color(ColorView)` {: #colorcolorview data-toc-label="color(ColorView)" }

**Задать цвет**

Устанавливает цвет выбора из ColorView.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `color` | `ColorView` | — |

Возвращает `ColorPicker`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ColorPicker::</span>`onColorChanged(EventListener)` {: #oncolorchangedeventlistener data-toc-label="onColorChanged(EventListener)" }

**Слушатель изменения цвета**

Регистрирует слушатель событий смены цвета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | — |

Возвращает `EventSubscription`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ColorPicker::</span>`popup()` {: #popup data-toc-label="popup()" }

**Получить [Popup](<../feedback/popup.md>)**

Возвращает всплывающий контейнер с элементами управления каналами.

Возвращает `Popup`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ColorPicker::</span>`preview()` {: #preview data-toc-label="preview()" }

**Кнопка предпросмотра**

Возвращает внутренний виджет кнопки предпросмотра цвета.

Возвращает `Button`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ColorPicker::</span>`rgba255(int, int, int, int)` {: #rgba255int-int-int-int data-toc-label="rgba255(int, int, int, int)" }

**Задать RGBA (0-255)**

Задает цвет с указанием целочисленных каналов RGBA в диапазоне 0-255.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `red` | `int` | — |
| `green` | `int` | — |
| `blue` | `int` | — |
| `alpha` | `int` | — |

Возвращает `ColorPicker`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ColorPicker::</span>`type()` {: #type data-toc-label="type()" }

**Получить тип**

Возвращает режим отображения каналов (RGB или RGBA).

Возвращает `Type`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ColorPicker::</span>`type(Type)` {: #typetype data-toc-label="type(Type)" }

**Задать тип**

Устанавливает режим отображения каналов пикера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `type` | `Type` | — |

Возвращает `ColorPicker`.

</div>

