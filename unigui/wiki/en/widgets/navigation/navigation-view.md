---
title: "NavigationView"
slug: "navigation-view"
category: "Widgets/Navigation"
locale: "en"
order: 0
---

# NavigationView

Sidebar navigation with collapsible categories of selectable items and a full-width accent selection.

## Overview

`NavigationView` is a `TreeView` specialised for gallery-style left menus. Add groups with `addCategory`, add pages with `NavigationCategory.addItem`, and react with `onItemSelected`. A category header only expands or collapses its group and never clears the selected item. Item ids are unique across the view and can be selected from code with `selectItem`.

## Gallery sidebar

```java
NavigationView nav = new NavigationView();
nav.addCategory("Basic Input").addItem("slider", "Slider");
nav.onItemSelected(item -> open(item.id()));
```

## Related links

- [TreeView](<tree-view.md>): Hierarchical tree container rendering collapsable nodes with custom icons, inline editing, and selection.

## API

### API

| Method | Description |
| --- | --- |
| [`NavigationView()`](#navigationview) | Creates an empty sidebar navigation view. |

<div class="api-member-card" markdown="1">

#### `NavigationView()` {: #navigationview data-toc-label="NavigationView()" }

**Create a navigation view**

Creates an empty sidebar navigation view.

</div>

### Categories

| Method | Description |
| --- | --- |
| [`addCategory(String)`](#addcategorystring) | Appends a category in the expanded state. |
| [`addCategory(String, boolean)`](#addcategorystring-boolean) | Appends a category and controls whether its items are initially visible. |
| [`categories()`](#categories) | Returns categories in insertion order. |
| [`clear()`](#clear) | Removes every category and item. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`addCategory(String)` {: #addcategorystring data-toc-label="addCategory(String)" }

**Add a category**

Appends a category in the expanded state.

| Parameter | Type | Description |
| --- | --- | --- |
| `title` | `String` | Visible category header text. |

Returns `NavigationCategory`: The newly created category, used to add navigation items..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`addCategory(String, boolean)` {: #addcategorystring-boolean data-toc-label="addCategory(String, boolean)" }

**Add a category with initial state**

Appends a category and controls whether its items are initially visible.

| Parameter | Type | Description |
| --- | --- | --- |
| `title` | `String` | Visible category header text. |
| `expanded` | `boolean` | True to show the category items initially; false to collapse the category. |

Returns `NavigationCategory`: The newly created category..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`categories()` {: #categories data-toc-label="categories()" }

**Get categories**

Returns categories in insertion order.

Returns `List`: An unmodifiable list of categories in insertion order..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`clear()` {: #clear data-toc-label="clear()" }

**Clear navigation**

Removes every category and item.

Returns `NavigationView`: This view, for fluent configuration..

</div>

### Items and selection

| Method | Description |
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

Returns `List`: An immutable snapshot of all items in insertion order..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`item(String)` {: #itemstring data-toc-label="item(String)" }

**Find an item**

Looks up an item by its id.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Item id; null returns null. |

Returns `NavigationItem`: The matching item, or null when the id is null or unknown..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`selectedItem()` {: #selecteditem data-toc-label="selectedItem()" }

**Get selected item**

Returns the currently selected item, if any.

Returns `NavigationItem`: The selected item, or null when there is no selection..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`selectItem(String)` {: #selectitemstring data-toc-label="selectItem(String)" }

**Select an item**

Selects an enabled item by id and expands its category if needed.

| Parameter | Type | Description |
| --- | --- | --- |
| `id` | `String` | Id of an item in this view; unknown or null ids are ignored. |

Returns `NavigationView`: This view, for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`onItemSelected(Consumer)` {: #onitemselectedconsumer data-toc-label="onItemSelected(Consumer)" }

**Listen for item selection**

Registers a callback invoked when an item is selected.

| Parameter | Type | Description |
| --- | --- | --- |
| `listener` | `Consumer` | Callback receiving the selected item; null is accepted and ignored. |

Returns `EventSubscription`: A subscription that can be cancelled with unsubscribe()..

</div>

### Appearance

| Method | Description |
| --- | --- |
| [`categoryColor()`](#categorycolor) | Returns the mutable background color used for category headers. |
| [`categoryColor(ColorView)`](#categorycolorcolorview) | Sets the category header background color and invalidates the view visually. |
| [`categoryTextColor()`](#categorytextcolor) | Returns the mutable text color used for category headers. |
| [`categoryTextColor(ColorView)`](#categorytextcolorcolorview) | Sets the category header text color and refreshes category rows. |

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`categoryColor()` {: #categorycolor data-toc-label="categoryColor()" }

**Get category background color**

Returns the mutable background color used for category headers.

Returns `MutableColor`: The mutable RGBA category header background color; components are in [0, 1]..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`categoryColor(ColorView)` {: #categorycolorcolorview data-toc-label="categoryColor(ColorView)" }

**Set category background color**

Sets the category header background color and invalidates the view visually.

| Parameter | Type | Description |
| --- | --- | --- |
| `color` | `ColorView` | Source RGBA color; null is ignored. |

Returns `NavigationView`: This view, for fluent configuration..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`categoryTextColor()` {: #categorytextcolor data-toc-label="categoryTextColor()" }

**Get category text color**

Returns the mutable text color used for category headers.

Returns `MutableColor`: The mutable RGBA category header text color; components are in [0, 1]..

</div>

<div class="api-member-card" markdown="1">

#### <span class="api-class-prefix">NavigationView::</span>`categoryTextColor(ColorView)` {: #categorytextcolorcolorview data-toc-label="categoryTextColor(ColorView)" }

**Set category text color**

Sets the category header text color and refreshes category rows.

| Parameter | Type | Description |
| --- | --- | --- |
| `color` | `ColorView` | Source RGBA color; null is ignored. |

Returns `NavigationView`: This view, for fluent configuration..

</div>

