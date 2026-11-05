---
title: "ContextMenu"
slug: "context-menu"
category: "Виджеты/Обратная связь"
locale: "ru"
order: 0
---

# ContextMenu

Всплывающее контекстное меню, управляющее иерархическими элементами, обработчиками действий и автозакрытием.

## Обзор

`ContextMenu` — плавающее выпадающее контекстное меню, отображающее упорядоченный список действий, подменю и разделителей.

Ключевые возможности:
- Обычные и форматированные пункты меню с колбэками `Runnable`.
- Вложенные иерархические подменю.
- Умное позиционирование внутри границ экрана (`hostBounds`, `arrangeInHost`).
- Автоматическое закрытие при клике вне области меню (`closeOnOutsideClick`).

## Создание и открытие контекстного меню

```java
ContextMenu menu = new ContextMenu();
menu.item("Copy", () -> copy()).separator().item("Paste", () -> paste());
menu.openAt(mouseX, mouseY);
```

## Связанные ссылки

- [OverlayLayer](<overlay-layer.md>): Полноэкранный слой оверлеев, управляющий модальными подложками, диалогами, всплывающими окнами и проверкой попаданий.
- [Popup](<popup.md>): Всплывающий виджет, позиционируемый относительно целевых элементов интерфейса или экранных координат.

## API

<div class="api-member-card" markdown="1">

### `ContextMenu()` {: #contextmenu data-toc-label="ContextMenu()" }

Создает пустое контекстное меню.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`arrangeInHost(RectView)` {: #arrangeinhostrectview data-toc-label="arrangeInHost(RectView)" }

Компонует меню внутри границ экрана, корректируя сторону размещения при выходе за границы.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `hostBounds` | `RectView` | Прямоугольная область границ родительского контейнера. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`close()` {: #close data-toc-label="close()" }

Закрывает меню и все активные подменю.

Возвращает `ContextMenu`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`closeOnOutsideClick()` {: #closeonoutsideclick data-toc-label="closeOnOutsideClick()" }

Возвращает true, если клик снаружи закрывает меню.

Возвращает `boolean`: True, если закрытие по клику вне виджета включено..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`closeOnOutsideClick(boolean)` {: #closeonoutsideclickboolean data-toc-label="closeOnOutsideClick(boolean)" }

Устанавливает, должен ли клик снаружи закрывать меню.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `closeOnOutsideClick` | `boolean` | Закрывать ли виджет автоматически при клике вне его области. |

Возвращает `ContextMenu`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`item(Button)` {: #itembutton data-toc-label="item(Button)" }

Добавляет готовую кнопку в качестве пункта меню.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `button` | `Button` | Пользовательская кнопка для прикрепления. |

Возвращает `ContextMenu`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`item(RichText)` {: #itemrichtext data-toc-label="item(RichText)" }

Добавляет пункт меню с форматированным текстом [RichText](<../../core/text/rich-text.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `RichText` | Отображаемая текстовая строка. |

Возвращает `ContextMenu`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`item(RichText, Runnable)` {: #itemrichtext-runnable data-toc-label="item(RichText, Runnable)" }

Добавляет пункт меню с форматированным текстом [RichText](<../../core/text/rich-text.md>) и действием по клику.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `RichText` | Отображаемая текстовая строка. |
| `action` | `Runnable` | Действие для выполнения. |

Возвращает `ContextMenu`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`item(String)` {: #itemstring data-toc-label="item(String)" }

Добавляет текстовый пункт меню с простым текстом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Отображаемая текстовая строка. |

Возвращает `ContextMenu`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`item(String, Runnable)` {: #itemstring-runnable data-toc-label="item(String, Runnable)" }

Добавляет текстовый пункт меню с действием по клику.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Отображаемая текстовая строка. |
| `action` | `Runnable` | Действие для выполнения. |

Возвращает `ContextMenu`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`itemButton(int)` {: #itembuttonint data-toc-label="itemButton(int)" }

Возвращает кнопку элемента по указанному индексу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | Индекс элемента (начиная с нуля). |

Возвращает `Button`: Экземпляр кнопки..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`itemCount()` {: #itemcount data-toc-label="itemCount()" }

Возвращает общее количество элементов в меню.

Возвращает `int`: Общее количество элементов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`open()` {: #open data-toc-label="open()" }

Открывает меню в его последней настроенной позиции.

Возвращает `ContextMenu`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`open(boolean)` {: #openboolean data-toc-label="open(boolean)" }

Устанавливает состояние открытия контекстного меню.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `open` | `boolean` | Флаг целевого состояния открытия. |

Возвращает `ContextMenu`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`openAt(float, float)` {: #openatfloat-float data-toc-label="openAt(float, float)" }

Открывает меню по указанным координатам экрана.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Горизонтальная координата X. |
| `y` | `float` | Вертикальная координата Y. |

Возвращает `ContextMenu`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`opened()` {: #opened data-toc-label="opened()" }

Возвращает true, если контекстное меню открыто.

Возвращает `boolean`: True, если виджет в данный момент открыт..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`padding(EdgeInsets)` {: #paddingedgeinsets data-toc-label="padding(EdgeInsets)" }

Устанавливает внутренние отступы вокруг пунктов меню.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `padding` | `EdgeInsets` | Внутренние отступы контента. |

Возвращает `ContextMenu`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`selectItem(int)` {: #selectitemint data-toc-label="selectItem(int)" }

Выбирает элемент меню по указанному индексу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | Индекс элемента (начиная с нуля). |

Возвращает `ContextMenu`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`selectedItemIndex()` {: #selecteditemindex data-toc-label="selectedItemIndex()" }

Возвращает индекс текущего наведенного или выбранного с клавиатуры элемента.

Возвращает `int`: Индекс элемента..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`separator()` {: #separator data-toc-label="separator()" }

Добавляет разделительную полосу между элементами.

Возвращает `ContextMenu`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`separator(Separator)` {: #separatorseparator data-toc-label="separator(Separator)" }

Добавляет пользовательский разделитель [Separator](<../display/separator.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `separator` | `Separator` | Визуальный элемент разделителя. |

Возвращает `ContextMenu`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`submenu(Button, ContextMenu)` {: #submenubutton-contextmenu data-toc-label="submenu(Button, ContextMenu)" }

Добавляет каскадный пункт меню с подменю и пользовательской кнопкой.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `button` | `Button` | Пользовательская кнопка для прикрепления. |
| `submenu` | `ContextMenu` | Вложенный контейнер подменю. |

Возвращает `ContextMenu`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`submenu(RichText, ContextMenu)` {: #submenurichtext-contextmenu data-toc-label="submenu(RichText, ContextMenu)" }

Добавляет каскадный пункт меню с подменю и текстом [RichText](<../../core/text/rich-text.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `RichText` | Отображаемая текстовая строка. |
| `submenu` | `ContextMenu` | Вложенный контейнер подменю. |

Возвращает `ContextMenu`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`submenu(String, ContextMenu)` {: #submenustring-contextmenu data-toc-label="submenu(String, ContextMenu)" }

Добавляет каскадный пункт меню с подменю и простым текстом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Отображаемая текстовая строка. |
| `submenu` | `ContextMenu` | Вложенный контейнер подменю. |

Возвращает `ContextMenu`: Текущий экземпляр виджета для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ContextMenu::</span>`toggle(float, float)` {: #togglefloat-float data-toc-label="toggle(float, float)" }

Переключает состояние открытия по указанным координатам.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `x` | `float` | Горизонтальная координата X. |
| `y` | `float` | Вертикальная координата Y. |

Возвращает `ContextMenu`: Текущий экземпляр виджета для цепочки вызовов..

</div>

