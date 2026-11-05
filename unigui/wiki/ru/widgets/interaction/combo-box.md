---
title: "ComboBox"
slug: "combo-box"
category: "Widgets/Interaction"
locale: "ru"
order: 210
---

# ComboBox

Элемент выбора значения из выпадающего списка с возможностью прокрутки опций.

## Обзор

`ComboBox` позволяет выбрать один элемент из списка предложенных вариантов. При открытии отображается всплывающее меню с поддержкой прокрутки и подсветкой опций.

## Выбор разрешения

```java
ComboBox resolution = new ComboBox()
        .options("1920x1080", "2560x1440", "3840x2160")
        .selectedIndex(0);
resolution.onSelectionChanged(e -> applyResolution(resolution.selectedItem()));
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`addItem(String)` {: #additemstring data-toc-label="addItem(String)" }

**Добавить вариант**

Добавляет один вариант в список выбора.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `item` | `String` | — |

Возвращает `ComboBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`close()` {: #close data-toc-label="close()" }

**Закрыть список**

Закрывает всплывающее меню вариантов.

Возвращает `ComboBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`itemCount()` {: #itemcount data-toc-label="itemCount()" }

**Количество вариантов**

Возвращает общее количество доступных элементов выбора.

Возвращает `int`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`items()` {: #items data-toc-label="items()" }

**Получить список вариантов**

Возвращает список текстовых вариантов выбора.

Возвращает `List`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`items(List)` {: #itemslist data-toc-label="items(List)" }

**Задать варианты выбора**

Устанавливает варианты выбора выпадающего списка.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `items` | `List` | — |

Возвращает `ComboBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`open()` {: #open data-toc-label="open()" }

**Открыть список**

Раскрывает всплывающее меню вариантов.

Возвращает `ComboBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`opened()` {: #opened data-toc-label="opened()" }

**Состояние открытия**

Возвращает true, если выпадающий список открыт.

Возвращает `boolean`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`optionHoverBackground(ColorView)` {: #optionhoverbackgroundcolorview data-toc-label="optionHoverBackground(ColorView)" }

**Фон при наведении**

Задает цвет подсветки пункта меню при наведении курсора.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `color` | `ColorView` | — |

Возвращает `ComboBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`removeItem(int)` {: #removeitemint data-toc-label="removeItem(int)" }

**Удалить вариант**

Удаляет вариант выбора по указанному индексу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | — |

Возвращает `ComboBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`selectedIndex()` {: #selectedindex data-toc-label="selectedIndex()" }

**Индекс выбранного элемента**

Возвращает индекс выбранного элемента или -1, если ничего не выбрано.

Возвращает `int`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`selectedIndex(int)` {: #selectedindexint data-toc-label="selectedIndex(int)" }

**Выбрать индекс**

Выбирает вариант по индексу и обновляет заголовок.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | индекс варианта |

Возвращает `ComboBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`selectedItem()` {: #selecteditem data-toc-label="selectedItem()" }

**Выбранная строка**

Возвращает текст выбранного варианта или пустую строку.

Возвращает `String`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`silentSelectedIndex(int)` {: #silentselectedindexint data-toc-label="silentSelectedIndex(int)" }

**Задать индекс без событий**

Устанавливает индекс выбранного элемента без генерации событий изменения.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | — |

Возвращает `ComboBox`.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ComboBox::</span>`toggle()` {: #toggle data-toc-label="toggle()" }

**Переключить список**

Инвертирует открытое/закрытое состояние списка.

Возвращает `ComboBox`.

</div>

