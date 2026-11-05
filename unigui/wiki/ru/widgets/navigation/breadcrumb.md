---
title: "Breadcrumb"
slug: "breadcrumb"
category: "Виджеты/Навигация"
locale: "ru"
order: 0
---

# Breadcrumb

Интерактивная цепочка навигации («хлебные крошки»), отображающая иерархические элементы с настраиваемыми разделителями и обработкой выбора.

## Обзор

Интерактивная цепочка навигации («хлебные крошки»), отображающая иерархические элементы с настраиваемыми разделителями и обработкой выбора. Предоставляет широкие возможности настройки компоновки и взаимодействия.

## Базовое использование

```java
Breadcrumb breadcrumb = new Breadcrumb();
breadcrumb.addItem("Home").addItem("Settings");
```

## Связанные ссылки

- [BreadcrumbItem](<../../core/widgets/navigation/breadcrumb-item.md>): Дескриптор навигационного элемента, представляющий шаг или крошку в цепочке навигации.

## API

<div class="api-member-card" markdown="1">

### `Breadcrumb()` {: #breadcrumb data-toc-label="Breadcrumb()" }

Создает пустую навигационную цепочку Breadcrumb.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`addItem(BreadcrumbItem)` {: #additembreadcrumbitem data-toc-label="addItem(BreadcrumbItem)" }

Добавляет элемент [BreadcrumbItem](<../../core/widgets/navigation/breadcrumb-item.md>) в навигационную цепочку.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `item` | `BreadcrumbItem` | Целевой элемент. |

Возвращает `Breadcrumb`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`addItem(RichText)` {: #additemrichtext data-toc-label="addItem(RichText)" }

Создает и добавляет элемент навигационной цепочки с форматированной меткой [RichText](<../../core/text/rich-text.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `RichText` | Текстовая метка. |

Возвращает `Breadcrumb`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`addItem(String)` {: #additemstring data-toc-label="addItem(String)" }

Создает и добавляет элемент навигационной цепочки с текстовой меткой.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `text` | `String` | Текстовая метка. |

Возвращает `Breadcrumb`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`breadcrumbItems(List)` {: #breadcrumbitemslist data-toc-label="breadcrumbItems(List)" }

Возвращает неизменяемый список объектов [BreadcrumbItem](<../../core/widgets/navigation/breadcrumb-item.md>) цепочки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `items` | `List` | Коллекция элементов. |

Возвращает `Breadcrumb`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`clearItems()` {: #clearitems data-toc-label="clearItems()" }

Очищает все элементы навигационной цепочки.

Возвращает `Breadcrumb`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`host()` {: #host data-toc-label="host()" }

Возвращает контейнер [LinearBox](<../containers/linear-box.md>), размещающий кнопки элементов цепочки.

Возвращает `WrapPanel`: Контейнер, содержащий элементы..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`itemButton(int)` {: #itembuttonint data-toc-label="itemButton(int)" }

Возвращает виджет кнопки для элемента цепочки по указанному индексу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | Позиция индекса. |

Возвращает `Button`: Связанный компонент кнопки..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`itemCount()` {: #itemcount data-toc-label="itemCount()" }

Возвращает общее количество элементов в навигационной цепочке.

Возвращает `int`: Общее количество элементов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`items()` {: #items data-toc-label="items()" }

Возвращает список текстовых меток элементов цепочки.

Возвращает `List`: Неизменяемый список элементов-узлов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`items(List)` {: #itemslist data-toc-label="items(List)" }

Задает элементы цепочки из массива текстовых строк.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `items` | `List` | Коллекция элементов. |

Возвращает `Breadcrumb`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`lineSpacing(float)` {: #linespacingfloat data-toc-label="lineSpacing(float)" }

Устанавливает вертикальный интервал строк при переносе цепочки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `lineSpacing` | `float` | Межстрочный интервал в пикселях. |

Возвращает `Breadcrumb`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`onSelectionChanged(EventListener)` {: #onselectionchangedeventlistener data-toc-label="onSelectionChanged(EventListener)" }

Регистрирует слушателя события смены выбранного элемента цепочки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Колбэк слушателя события. |

Возвращает `EventSubscription`: Дескриптор подписки на событие..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`removeItem(int)` {: #removeitemint data-toc-label="removeItem(int)" }

Удаляет указанный элемент из навигационной цепочки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | Позиция индекса. |

Возвращает `Breadcrumb`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`richItems(List)` {: #richitemslist data-toc-label="richItems(List)" }

Задает элементы цепочки из массива форматированных строк [RichText](<../../core/text/rich-text.md>).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `items` | `List` | Коллекция элементов. |

Возвращает `Breadcrumb`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`selectedIndex()` {: #selectedindex data-toc-label="selectedIndex()" }

Возвращает индекс текущего выбранного элемента цепочки.

Возвращает `int`: Результирующее значение..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`selectedIndex(int)` {: #selectedindexint data-toc-label="selectedIndex(int)" }

Выбирает элемент цепочки по индексу с генерацией события изменения выбора.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | Позиция индекса. |

Возвращает `Breadcrumb`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`selectedItem()` {: #selecteditem data-toc-label="selectedItem()" }

Возвращает выбранный элемент навигационной цепочки, или null.

Возвращает `BreadcrumbItem`: Экземпляр элемента-узла или null, если не найден..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`selectedPath()` {: #selectedpath data-toc-label="selectedPath()" }

Возвращает иерархический индексный путь выбранного элемента цепочки.

Возвращает `List`: Неизменяемый список элементов-узлов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`separator()` {: #separator data-toc-label="separator()" }

Возвращает текстовую строку разделителя элементов цепочки.

Возвращает `String`: Экземпляр виджета-разделителя..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`separator(String)` {: #separatorstring data-toc-label="separator(String)" }

Устанавливает текстовый разделитель между элементами навигационной цепочки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `separator` | `String` | Кастомный виджет-разделитель. |

Возвращает `Breadcrumb`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`silentSelectedIndex(int)` {: #silentselectedindexint data-toc-label="silentSelectedIndex(int)" }

Выбирает элемент цепочки по индексу без вызова событий смены выбора.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | Позиция индекса. |

Возвращает `Breadcrumb`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">Breadcrumb::</span>`spacing(float)` {: #spacingfloat data-toc-label="spacing(float)" }

Устанавливает горизонтальный интервал между элементами в пикселях.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `spacing` | `float` | Расстояние между элементами в пикселях. |

Возвращает `Breadcrumb`: Текущий экземпляр для цепочки вызовов..

</div>

