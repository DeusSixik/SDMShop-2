---
title: "Tooltip"
slug: "tooltip"
category: "Виджеты/Обратная связь"
locale: "ru"
order: 0
---

# Tooltip

Всплывающая подсказка (тултип), отображающая вспомогательный текст, форматированный контент и таймеры задержки.

## Обзор

`Tooltip` отображает всплывающее информационное окно с подсказкой при наведении курсора на интерактивный элемент интерфейса.

Ключевые возможности:
- Поддержка простого текста и форматированного контента [RichText](<../../core/text/rich-text.md>).
- Автоматическая коррекция координат для предотвращения выхода за границы экрана.
- Задержки показа и скрытия для предотвращения мерцания при случайном наведении.
- Настраиваемые цвета фона, обводки, текста и внутренних отступов.

## Прикрепление всплывающей подсказки к виджету

```java
Tooltip tooltip = new Tooltip(button, "Click to save progress");
```

## Связанные ссылки

- [OverlayLayer](<overlay-layer.md>): Полноэкранный слой оверлеев, управляющий модальными подложками, диалогами, всплывающими окнами и проверкой попаданий.
- [Popup](<popup.md>): Всплывающий виджет, позиционируемый относительно целевых элементов интерфейса или экранных координат.

## API

<div class="api-member-card" markdown="1">

### `Tooltip()` {: #tooltip data-toc-label="Tooltip()" }

Создает пустой экземпляр Tooltip.

</div>

<div class="api-member-card" markdown="1">

### `Tooltip(Widget, RichText)` {: #tooltipwidget-richtext data-toc-label="Tooltip(Widget, RichText)" }

Создает Tooltip, привязанный к виджету с форматированным текстом [RichText](<../../core/text/rich-text.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `anchor` | `Widget` | Виджет-якорь, относительно которого выполняется позиционирование. |
| `text` | `RichText` | Компонент форматированного текста [RichText](<../../core/text/rich-text.md>). |

</div>

<div class="api-member-card" markdown="1">

### `Tooltip(Widget, String)` {: #tooltipwidget-string data-toc-label="Tooltip(Widget, String)" }

Создает Tooltip, привязанный к виджету с простым текстом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `anchor` | `Widget` | Виджет-якорь, относительно которого выполняется позиционирование. |
| `text` | `String` | Отображаемая текстовая строка. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`anchor()` {: #anchor data-toc-label="anchor()" }

Возвращает виджет-якорь, к которому привязана подсказка.

Возвращает `Widget`: Целевой виджет-якорь..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`anchor(Widget)` {: #anchorwidget data-toc-label="anchor(Widget)" }

Устанавливает виджет-якорь для привязки подсказки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `anchor` | `Widget` | Виджет-якорь, относительно которого выполняется позиционирование. |

Возвращает `Tooltip`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`arrangeInHost(RectView)` {: #arrangeinhostrectview data-toc-label="arrangeInHost(RectView)" }

Позиционирует подсказку внутри границ родительского контейнера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `bounds` | `RectView` | Прямоугольная область границ родительского контейнера. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`maxWidth()` {: #maxwidth data-toc-label="maxWidth()" }

Возвращает ограничение максимальной ширины в пикселях.

Возвращает `float`: Верхняя граница диапазона..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`maxWidth(float)` {: #maxwidthfloat data-toc-label="maxWidth(float)" }

Устанавливает ограничение максимальной ширины в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `maxWidth` | `float` | Ограничение максимальной ширины. |

Возвращает `Tooltip`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`offset(float, float)` {: #offsetfloat-float data-toc-label="offset(float, float)" }

Устанавливает смещение относительно курсора в пикселях интерфейса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Горизонтальная координата X. |
| `y` | `float` | Вертикальная координата Y. |

Возвращает `Tooltip`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`richText()` {: #richtext data-toc-label="richText()" }

Возвращает форматированный контент подсказки [RichText](<../../core/text/rich-text.md>).

Возвращает `RichText`: Форматированный текст [RichText](<../../core/text/rich-text.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`richText(RichText)` {: #richtextrichtext data-toc-label="richText(RichText)" }

Устанавливает форматированный контент подсказки [RichText](<../../core/text/rich-text.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `richText` | `RichText` | Компонент форматированного текста [RichText](<../../core/text/rich-text.md>). |

Возвращает `Tooltip`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`showing()` {: #showing data-toc-label="showing()" }

Возвращает true, если подсказка видна в данный момент.

Возвращает `boolean`: True, если виджет виден в данный момент..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`text()` {: #text data-toc-label="text()" }

Возвращает строку простого текста подсказки.

Возвращает `String`: Отображаемая текстовая строка..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`text(String)` {: #textstring data-toc-label="text(String)" }

Устанавливает строку простого текста подсказки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Отображаемая текстовая строка. |

Возвращает `Tooltip`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Tooltip::</span>`textColor()` {: #textcolor data-toc-label="textColor()" }

Возвращает изменяемый цвет текста подсказки.

Возвращает `MutableColor`: Экземпляр изменяемого цвета..

</div>

