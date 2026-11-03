---
title: "TabControl"
slug: "tab-control"
category: "Виджеты/Навигация"
locale: "ru"
order: 0
---

# TabControl

Вкладочный навигационный контейнер с прокручиваемой строкой заголовков и переключаемой областью содержимого.

## Обзор

Вкладочный навигационный контейнер с прокручиваемой строкой заголовков и переключаемой областью содержимого. Предоставляет широкие возможности настройки компоновки и взаимодействия.

## Базовое использование

```java
TabControl tabs = new TabControl();
tabs.addTab("Editor", new PanelWidget());
tabs.addTab("Console", new PanelWidget());
tabs.selectTab(0);
```

## Связанные ссылки

- [PageView](<page-view.md>): Контейнер страниц с сохранением состояния, отображающий ровно одну страницу за раз.

## API

<div class="api-member-card" markdown="1">

### `TabControl()` {: #tabcontrol data-toc-label="TabControl()" }

Создает экземпляр TabControl.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`addTab(RichText, Widget)` {: #addtabrichtext-widget data-toc-label="addTab(RichText, Widget)" }

Добавляет новую вкладку с форматированным заголовком [RichText](<../../core/text/rich-text.md>) и содержимым.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `title` | `RichText` | Текст заголовка. |
| `content` | `Widget` | Виджет содержимого. |

Возвращает `TabControl`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`addTab(String, Widget)` {: #addtabstring-widget data-toc-label="addTab(String, Widget)" }

Добавляет новую вкладку с текстовым заголовком и виджетом содержимого.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `title` | `String` | Текст заголовка. |
| `content` | `Widget` | Виджет содержимого. |

Возвращает `TabControl`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`clearTabs()` {: #cleartabs data-toc-label="clearTabs()" }

Удаляет все вкладки и их содержимое.

Возвращает `TabControl`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`contentHost()` {: #contenthost data-toc-label="contentHost()" }

Возвращает контейнер отображения содержимого страниц вкладок.

Возвращает `StackPanel`: Контейнер, содержащий представления содержимого..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`insertTab(int, RichText, Widget)` {: #inserttabint-richtext-widget data-toc-label="insertTab(int, RichText, Widget)" }

Вставляет вкладку с заголовком [RichText](<../../core/text/rich-text.md>) по указанному индексу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | Позиция индекса. |
| `title` | `RichText` | Текст заголовка. |
| `content` | `Widget` | Виджет содержимого. |

Возвращает `TabControl`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`insertTab(int, String, Widget)` {: #inserttabint-string-widget data-toc-label="insertTab(int, String, Widget)" }

Вставляет вкладку с текстовым заголовком по указанному индексу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | Позиция индекса. |
| `title` | `String` | Текст заголовка. |
| `content` | `Widget` | Виджет содержимого. |

Возвращает `TabControl`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`onSelectionChanged(EventListener)` {: #onselectionchangedeventlistener data-toc-label="onSelectionChanged(EventListener)" }

Регистрирует слушателя события смены выбранной вкладки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | Колбэк слушателя события. |

Возвращает `EventSubscription`: Дескриптор подписки на событие..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`removeTab(int)` {: #removetabint data-toc-label="removeTab(int)" }

Удаляет вкладку по указанному индексу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | Позиция индекса. |

Возвращает `TabControl`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`selectRelative(int)` {: #selectrelativeint data-toc-label="selectRelative(int)" }

Смещает выбор вкладки на указанное относительное смещение.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `delta` | `int` | Относительное смещение. |

Возвращает `TabControl`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`selectTab(int)` {: #selecttabint data-toc-label="selectTab(int)" }

Выбирает вкладку по индексу с вызовом события смены выбора.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | Позиция индекса. |

Возвращает `TabControl`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`selectedContent()` {: #selectedcontent data-toc-label="selectedContent()" }

Возвращает виджет содержимого выбранной вкладки, или null.

Возвращает `Widget`: Содержимое выбранной вкладки или панели..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`selectedIndex()` {: #selectedindex data-toc-label="selectedIndex()" }

Возвращает индекс текущей выбранной вкладки, или -1.

Возвращает `int`: Индекс выбранного элемента или -1, если выбор отсутствует..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`selectedTab()` {: #selectedtab data-toc-label="selectedTab()" }

Возвращает дескриптор выбранной вкладки, или null.

Возвращает `Tab`: Дескриптор выбранной вкладки или null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`silentSelectTab(int)` {: #silentselecttabint data-toc-label="silentSelectTab(int)" }

Выбирает вкладку по индексу без вызова событий.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | Позиция индекса. |

Возвращает `TabControl`: Текущий экземпляр для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`tabButton(int)` {: #tabbuttonint data-toc-label="tabButton(int)" }

Возвращает кнопку [ToggleButton](<../interaction/toggle-button.md>) для вкладки с указанным индексом.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | Позиция индекса. |

Возвращает `ToggleButton`: Кнопка [ToggleButton](<../interaction/toggle-button.md>) для указанной вкладки..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`tabCount()` {: #tabcount data-toc-label="tabCount()" }

Возвращает общее количество вкладок.

Возвращает `int`: Общее количество вкладок..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`tabHeader()` {: #tabheader data-toc-label="tabHeader()" }

Возвращает контейнер заголовков вкладок.

Возвращает `HBox`: Контейнер заголовков вкладок..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`tabHeaderScroll()` {: #tabheaderscroll data-toc-label="tabHeaderScroll()" }

Возвращает прокручиваемый контейнер заголовка вкладок.

Возвращает `ScrollView`: Прокручиваемый контейнер заголовка вкладок..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">TabControl::</span>`tabs()` {: #tabs data-toc-label="tabs()" }

Возвращает неизменяемый список дескрипторов вкладок.

Возвращает `List`: Неизменяемый список дескрипторов вкладок..

</div>

