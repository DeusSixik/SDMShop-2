---
title: "Popup"
slug: "popup"
category: "Виджеты/Обратная связь"
locale: "ru"
order: 0
---

# Popup

Всплывающий виджет, позиционируемый относительно целевых элементов интерфейса или экранных координат.

## Обзор

`Popup` представляет собой легковесный плавающий контейнер, привязанный к определенному целевому виджету или координате экрана.

Ключевые возможности:
- Относительное позиционирование по сторонам виджета-якоря (`TOP`, `BOTTOM`, `LEFT`, `RIGHT`).
- Контроль выхода за границы родительского контейнера.
- Автоматическое закрытие при клике вне границ всплывающего окна (`closeOnOutsideClick`).
- Слушатели событий открытия и закрытия (`onOpenChanged`).

## Открытие привязанного всплывающего окна

```java
Popup popup = new Popup(button, menuContent);
popup.open();
```

## Связанные ссылки

- [OverlayLayer](<overlay-layer.md>): Полноэкранный слой оверлеев, управляющий модальными подложками, диалогами, всплывающими окнами и проверкой попаданий.
- [Tooltip](<tooltip.md>): Всплывающая подсказка (тултип), отображающая вспомогательный текст, форматированный контент и таймеры задержки.

## API

<div class="api-member-card" markdown="1">

### `Popup()` {: #popup data-toc-label="Popup()" }

Создает пустой экземпляр Popup.

</div>

<div class="api-member-card" markdown="1">

### `Popup(Widget, Widget)` {: #popupwidget-widget data-toc-label="Popup(Widget, Widget)" }

Создает Popup, привязанный к виджету-якорю и отображающий заданный контент.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `anchor` | `Widget` | Виджет-якорь, относительно которого выполняется позиционирование. |
| `content` | `Widget` | Вложенный виджет контента. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`anchor()` {: #anchor data-toc-label="anchor()" }

Возвращает виджет-якорь, к которому привязан попап.

Возвращает `Widget`: Целевой виджет-якорь..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`anchor(Widget)` {: #anchorwidget data-toc-label="anchor(Widget)" }

Устанавливает виджет-якорь для привязки попапа.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `anchor` | `Widget` | Виджет-якорь, относительно которого выполняется позиционирование. |

Возвращает `Popup`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`arrangeInHost(RectView)` {: #arrangeinhostrectview data-toc-label="arrangeInHost(RectView)" }

Позиционирует попап внутри границ родительского контейнера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `bounds` | `RectView` | Прямоугольная область границ родительского контейнера. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`close()` {: #close data-toc-label="close()" }

Закрывает попап.

Возвращает `Popup`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`closeOnOutsideClick()` {: #closeonoutsideclick data-toc-label="closeOnOutsideClick()" }

Возвращает true, если попап закрывается при клике снаружи.

Возвращает `boolean`: True, если закрытие по клику вне виджета включено..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`closeOnOutsideClick(boolean)` {: #closeonoutsideclickboolean data-toc-label="closeOnOutsideClick(boolean)" }

Устанавливает, должен ли попап закрываться при клике снаружи.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `closeOnOutsideClick` | `boolean` | Закрывать ли виджет автоматически при клике вне его области. |

Возвращает `Popup`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`content()` {: #content data-toc-label="content()" }

Возвращает вложенный виджет контента.

Возвращает `Widget`: Вложенный виджет контента..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`content(Widget)` {: #contentwidget data-toc-label="content(Widget)" }

Устанавливает вложенный виджет контента.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `content` | `Widget` | Вложенный виджет контента. |

Возвращает `Popup`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`offset(float, float)` {: #offsetfloat-float data-toc-label="offset(float, float)" }

Устанавливает дополнительное смещение от вычисленного положения в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Горизонтальная координата X. |
| `y` | `float` | Вертикальная координата Y. |

Возвращает `Popup`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`onOpenChanged(Runnable)` {: #onopenchangedrunnable data-toc-label="onOpenChanged(Runnable)" }

Устанавливает слушатель изменения состояния открытия попапа.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `onOpenChanged` | `Runnable` | Колбэк, вызываемый при изменении состояния открытия всплывающего окна. |

Возвращает `Popup`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`open()` {: #open data-toc-label="open()" }

Открывает попап.

Возвращает `Popup`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`open(boolean)` {: #openboolean data-toc-label="open(boolean)" }

Устанавливает состояние открытия попапа.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `open` | `boolean` | Флаг целевого состояния открытия. |

Возвращает `Popup`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`opened()` {: #opened data-toc-label="opened()" }

Возвращает true, если попап в данный момент открыт.

Возвращает `boolean`: True, если виджет в данный момент открыт..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`padding(EdgeInsets)` {: #paddingedgeinsets data-toc-label="padding(EdgeInsets)" }

Устанавливает внутренние отступы содержимого всплывающего окна.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `padding` | `EdgeInsets` | Внутренние отступы контента. |

Возвращает `Popup`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`placement()` {: #placement data-toc-label="placement()" }

Возвращает относительное положение попапа.

Возвращает `Placement`: Текущее положение размещения..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`placement(Placement)` {: #placementplacement data-toc-label="placement(Placement)" }

Устанавливает относительное положение попапа.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `placement` | `Placement` | Положение всплывающего окна относительно виджета-якоря. |

Возвращает `Popup`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Popup::</span>`toggle()` {: #toggle data-toc-label="toggle()" }

Переключает состояние открытия попапа.

Возвращает `Popup`: Текущий экземпляр виджета для цепочки вызовов..

</div>

