---
title: "SearchableGridPickerWidget"
slug: "searchable-grid-picker-widget"
category: "Widgets/Interaction"
locale: "ru"
order: 250
---

# SearchableGridPickerWidget

Абстрактный диалоговый компонент выбора элементов в виртуализированной сетке с поисковой фильтрацией, предпросмотром и подсказками.

## Обзор

`SearchableGridPickerWidget<T>` реализует модальный выбор элементов. Нажатие на кнопку предпросмотра открывает диалоговое окно `WindowWidget` с поисковой строкой `SearchField`, виртуализированной сеткой элементов `VirtualListView` и всплывающими подсказками.

## Реализация кастомного пикера

```java
public class ItemPicker extends SearchableGridPickerWidget<ItemDefinition> {
    @Override
    protected String entryIdText(ItemDefinition entry) {
        return entry.id();
    }
    @Override
    protected String searchText(ItemDefinition entry) {
        return entry.name();
    }
}
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`clearQuery()` {: #clearquery data-toc-label="clearQuery()" }

**Очистить поисковый запрос**

Очищает строку поискового запроса.

Возвращает `SearchableGridPickerWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`clearSelection()` {: #clearselection data-toc-label="clearSelection()" }

**Сбросить выбор**

Сбрасывает текущий выбранный элемент.

Возвращает `SearchableGridPickerWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`close()` {: #close data-toc-label="close()" }

**Закрыть диалог**

Закрывает диалоговое окно выбора.

Возвращает `SearchableGridPickerWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`dialog()` {: #dialog data-toc-label="dialog()" }

**Диалоговое окно**

Возвращает модальное диалоговое окно [WindowWidget](<../feedback/window-widget.md>).

Возвращает `WindowWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`entries()` {: #entries data-toc-label="entries()" }

**Получить список элементов**

Возвращает неизменяемый список всех зарегистрированных элементов.

Возвращает `List`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`entries(Collection)` {: #entriescollection data-toc-label="entries(Collection)" }

**Задать список элементов**

Заполняет коллекцию элементов, доступных для выбора.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `entries` | `Collection` | — |

Возвращает `SearchableGridPickerWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`entryCount()` {: #entrycount data-toc-label="entryCount()" }

**Количество элементов**

Возвращает общее количество доступных элементов.

Возвращает `int`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`filteredEntries()` {: #filteredentries data-toc-label="filteredEntries()" }

**Отфильтрованные элементы**

Возвращает список элементов, соответствующих текущему поисковому запросу.

Возвращает `List`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`onSelectionChanged(EventListener)` {: #onselectionchangedeventlistener data-toc-label="onSelectionChanged(EventListener)" }

**Слушатель изменения выбора**

Регистрирует коллбэк изменения выбранного элемента.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `EventListener` | — |

Возвращает `EventSubscription`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`open()` {: #open data-toc-label="open()" }

**Открыть диалог**

Открывает диалоговое окно поиска и выбора.

Возвращает `SearchableGridPickerWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`openButton()` {: #openbutton data-toc-label="openButton()" }

**Кнопка открытия**

Возвращает кнопку-триггер для открытия модального окна.

Возвращает `Button`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`opened()` {: #opened data-toc-label="opened()" }

**Состояние открытия диалога**

Возвращает true, если диалоговое окно в данный момент открыто.

Возвращает `boolean`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`query()` {: #query data-toc-label="query()" }

**Получить поисковый запрос**

Возвращает текущую строку поискового запроса.

Возвращает `String`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`query(String)` {: #querystring data-toc-label="query(String)" }

**Задать поисковый запрос**

Устанавливает поисковый запрос и обновляет сетку результатов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `query` | `String` | — |

Возвращает `SearchableGridPickerWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`resultGrid()` {: #resultgrid data-toc-label="resultGrid()" }

**Виртуализированная сетка**

Возвращает виртуализированный список, отображающий сетку элементов.

Возвращает `VirtualListView`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`searchField()` {: #searchfield data-toc-label="searchField()" }

**Поле поиска**

Возвращает виджет строки поиска внутри диалогового окна.

Возвращает `SearchField`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`selectIndex(int)` {: #selectindexint data-toc-label="selectIndex(int)" }

**Выбрать по индексу**

Выбирает элемент по индексу в основном списке.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | — |

Возвращает `SearchableGridPickerWidget`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`selectedEntry()` {: #selectedentry data-toc-label="selectedEntry()" }

**Выбранный элемент**

Возвращает Optional с выбранным элементом.

Возвращает `Optional`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">SearchableGridPickerWidget::</span>`selectedPreview()` {: #selectedpreview data-toc-label="selectedPreview()" }

**Виджет предпросмотра**

Возвращает виджет отображения текущего выбранного элемента.

Возвращает `WidgetBase`.

</div>

