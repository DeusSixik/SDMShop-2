---
title: "NavigationView"
slug: "navigation-view"
category: "Виджеты/Навигация"
locale: "ru"
order: 0
---

# NavigationView

Боковая навигация со сворачиваемыми группами пунктов и акцентным выделением выбранного пункта на всю ширину.

## Обзор

`NavigationView` — `TreeView`, заточенный под левое меню галереи. Группы добавляются через `addCategory`, страницы через `NavigationCategory.addItem`, реакция на выбор через `onItemSelected`. Заголовок группы только сворачивает и разворачивает её и не сбрасывает выбранный пункт. Идентификаторы пунктов уникальны в пределах навигации, выбрать пункт из кода можно через `selectItem`.

## Боковое меню галереи

```java
NavigationView nav = new NavigationView();
nav.addCategory("Basic Input").addItem("slider", "Slider");
nav.onItemSelected(item -> open(item.id()));
```

## Связанные ссылки

- [TreeView](<tree-view.md>): Иерархический древовидный контейнер со сворачиваемыми узлами, кастомными иконками, инлайн-редактированием и выбором.

## API

### API

| Метод | Описание |
| --- | --- |
| [`NavigationView()`](#navigationview) | Creates an empty sidebar navigation view. |

<div class="api-member-card" markdown="1">

#### `NavigationView()` {: #navigationview data-toc-label="NavigationView()" }

**Create a navigation view**

Creates an empty sidebar navigation view.

</div>

### Categories

| Метод | Описание |
| --- | --- |
| [`addCategory(String)`](#addcategorystring) | Appends a category in the expanded state. |
| [`addCategory(String, boolean)`](#addcategorystring-boolean) | Appends a category and controls whether its items are initially visible. |
| [`categories()`](#categories) | Returns categories in insertion order. |
| [`clear()`](#clear) | Removes every category and item. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`addCategory(String)` {: #addcategorystring data-toc-label="addCategory(String)" }

**Add a category**

Appends a category in the expanded state.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `title` | `String` | Visible category header text. |

Возвращает `NavigationCategory`: The newly created category, used to add navigation items..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`addCategory(String, boolean)` {: #addcategorystring-boolean data-toc-label="addCategory(String, boolean)" }

**Add a category with initial state**

Appends a category and controls whether its items are initially visible.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `title` | `String` | Visible category header text. |
| `expanded` | `boolean` | True to show the category items initially; false to collapse the category. |

Возвращает `NavigationCategory`: The newly created category..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`categories()` {: #categories data-toc-label="categories()" }

**Get categories**

Returns categories in insertion order.

Возвращает `List`: An unmodifiable list of categories in insertion order..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`clear()` {: #clear data-toc-label="clear()" }

**Clear navigation**

Removes every category and item.

Возвращает `NavigationView`: This view, for fluent configuration..

</div>

### Items and selection

| Метод | Описание |
| --- | --- |
| [`items()`](#items) | Returns all items in category and insertion order. |
| [`item(String)`](#itemstring) | Looks up an item by its id. |
| [`selectedItem()`](#selecteditem) | Returns the currently selected item, if any. |
| [`selectItem(String)`](#selectitemstring) | Selects an enabled item by id and expands its category if needed. |
| [`onItemSelected(Consumer)`](#onitemselectedconsumer) | Registers a callback invoked when an item is selected. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`items()` {: #items data-toc-label="items()" }

**Get items**

Returns all items in category and insertion order.

Возвращает `List`: An immutable snapshot of all items in insertion order..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`item(String)` {: #itemstring data-toc-label="item(String)" }

**Find an item**

Looks up an item by its id.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Item id; null returns null. |

Возвращает `NavigationItem`: The matching item, or null when the id is null or unknown..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`selectedItem()` {: #selecteditem data-toc-label="selectedItem()" }

**Get selected item**

Returns the currently selected item, if any.

Возвращает `NavigationItem`: The selected item, or null when there is no selection..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`selectItem(String)` {: #selectitemstring data-toc-label="selectItem(String)" }

**Select an item**

Selects an enabled item by id and expands its category if needed.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Id of an item in this view; unknown or null ids are ignored. |

Возвращает `NavigationView`: This view, for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`onItemSelected(Consumer)` {: #onitemselectedconsumer data-toc-label="onItemSelected(Consumer)" }

**Listen for item selection**

Registers a callback invoked when an item is selected.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `Consumer` | Callback receiving the selected item; null is accepted and ignored. |

Возвращает `EventSubscription`: A subscription that can be cancelled with unsubscribe()..

</div>

### Appearance

| Метод | Описание |
| --- | --- |
| [`categoryColor()`](#categorycolor) | Returns the mutable background color used for category headers. |
| [`categoryColor(ColorView)`](#categorycolorcolorview) | Sets the category header background color and invalidates the view visually. |
| [`categoryTextColor()`](#categorytextcolor) | Returns the mutable text color used for category headers. |
| [`categoryTextColor(ColorView)`](#categorytextcolorcolorview) | Sets the category header text color and refreshes category rows. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`categoryColor()` {: #categorycolor data-toc-label="categoryColor()" }

**Get category background color**

Returns the mutable background color used for category headers.

Возвращает `MutableColor`: The mutable RGBA category header background color; components are in [0, 1]..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`categoryColor(ColorView)` {: #categorycolorcolorview data-toc-label="categoryColor(ColorView)" }

**Set category background color**

Sets the category header background color and invalidates the view visually.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `color` | `ColorView` | Source RGBA color; null is ignored. |

Возвращает `NavigationView`: This view, for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`categoryTextColor()` {: #categorytextcolor data-toc-label="categoryTextColor()" }

**Get category text color**

Returns the mutable text color used for category headers.

Возвращает `MutableColor`: The mutable RGBA category header text color; components are in [0, 1]..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`categoryTextColor(ColorView)` {: #categorytextcolorcolorview data-toc-label="categoryTextColor(ColorView)" }

**Set category text color**

Sets the category header text color and refreshes category rows.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `color` | `ColorView` | Source RGBA color; null is ignored. |

Возвращает `NavigationView`: This view, for fluent configuration..

</div>

