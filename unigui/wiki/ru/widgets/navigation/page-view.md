---
title: "PageView"
slug: "page-view"
category: "Виджеты/Навигация"
locale: "ru"
order: 0
---

# PageView

Контейнер страниц с сохранением состояния, отображающий ровно одну страницу за раз.

## Обзор

Контейнер страниц с сохранением состояния, отображающий ровно одну страницу за раз. Предоставляет широкие возможности настройки компоновки и взаимодействия.

## Базовое использование

```java
PageView pageView = new PageView();
pageView.addPage(new Label("Page 1"));
pageView.addPage(new Label("Page 2"));
pageView.selectedIndex(0);
```

## Связанные ссылки

- [TabControl](<tab-control.md>): Вкладочный навигационный контейнер с прокручиваемой строкой заголовков и переключаемой областью содержимого.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`addPage(Widget)` {: #addpagewidget data-toc-label="addPage(Widget)" }

Добавляет виджет страницы в конец списка страниц.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `page` | `Widget` | Виджет страницы. |

Возвращает `PageView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`clearPages()` {: #clearpages data-toc-label="clearPages()" }

Удаляет все страницы из контейнера страниц.

Возвращает `PageView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`insertPage(int, Widget)` {: #insertpageint-widget data-toc-label="insertPage(int, Widget)" }

Вставляет виджет страницы по указанному индексу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | Позиция индекса. |
| `page` | `Widget` | Виджет страницы. |

Возвращает `PageView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`onSelectionChanged(EventListener)` {: #onselectionchangedeventlistener data-toc-label="onSelectionChanged(EventListener)" }

Регистрирует слушателя события изменения выбранной страницы.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Колбэк слушателя события. |

Возвращает `EventSubscription`: Дескриптор подписки на событие..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`pageCount()` {: #pagecount data-toc-label="pageCount()" }

Возвращает общее количество страниц в контейнере.

Возвращает `int`: Общее количество страниц..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`pages()` {: #pages data-toc-label="pages()" }

Возвращает неизменяемый список всех страниц.

Возвращает `List`: Неизменяемый список виджетов страниц..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`removePage(Widget)` {: #removepagewidget data-toc-label="removePage(Widget)" }

Удаляет указанный виджет страницы из контейнера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `page` | `Widget` | Виджет страницы. |

Возвращает `PageView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`removePage(int)` {: #removepageint data-toc-label="removePage(int)" }

Удаляет страницу по указанному индексу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | Позиция индекса. |

Возвращает `PageView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`selectRelative(int)` {: #selectrelativeint data-toc-label="selectRelative(int)" }

Смещает выбор страницы на указанное относительное смещение дельты.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `delta` | `int` | Относительное смещение. |

Возвращает `PageView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`selectedIndex()` {: #selectedindex data-toc-label="selectedIndex()" }

Возвращает индекс текущей выбранной страницы.

Возвращает `int`: Индекс выбранного элемента или -1, если выбор отсутствует..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`selectedIndex(int)` {: #selectedindexint data-toc-label="selectedIndex(int)" }

Выбирает активную страницу по индексу с генерацией события.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `selectedIndex` | `int` | Выбранный индекс. |

Возвращает `PageView`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`selectedPage()` {: #selectedpage data-toc-label="selectedPage()" }

Возвращает текущую активную страницу или null, если список пуст.

Возвращает `Widget`: Выбранный виджет страницы или null, если отсутствует..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PageView::</span>`silentSelectedIndex(int)` {: #silentselectedindexint data-toc-label="silentSelectedIndex(int)" }

Выбирает страницу по индексу без генерации событий смены выбора.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `selectedIndex` | `int` | Выбранный индекс. |

Возвращает `PageView`: Текущий экземпляр для цепочки вызовов..

</div>

