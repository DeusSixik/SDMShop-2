---
title: "VirtualListView"
slug: "virtual-list-view"
category: "Widgets/Data"
locale: "en"
order: 10
---

# VirtualListView

Virtualized presentation of large item sets as a responsive grid or list.

## When to use

Use `VirtualListView` when a collection contains hundreds or thousands of similar entries. The factory creates only visible items and a small overscan; ordinary forms are better built with `VBox` or `LinearBox`.

## Layout and [StylePack](<../../core/styling/style-pack.md>)

The viewport respects `layoutStyle().padding()`, `rowGap()`, and `columnGap()`. The same parameters can be configured through `StyleKeys.VIRTUAL_LIST_*` in a `StylePack`.

## Responsive grid

```java
VirtualListView view = new VirtualListView()
        .viewMode(VirtualListView.ViewMode.GRID)
        .gridCellWidth(120.0f)
        .gridCellHeight(144.0f)
        .itemCount(entries.size())
        .itemFactory(index -> createEntryCard(entries.get(index)));

view.layout(style -> style
        .padding(12.0f)
        .columnGap(8.0f)
        .rowGap(12.0f));
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">VirtualListView::</span>`itemFactory(IntFunction)` {: #itemfactoryintfunction data-toc-label="itemFactory(IntFunction)" }

**Item factory**

Sets the factory that creates a UI widget for an item index.

| Parameter | Type | Description |
| --- | --- | --- |
| `itemFactory` | `IntFunction` | Function from item index to [Widget](<../widget.md>); null restores the default [Label](<../display/label.md>) factory. |

Returns `VirtualListView`: this VirtualListView for fluent configuration.

#### Connect a model

```java
view.itemFactory(index -> createCard(entries.get(index)));
```

</div>

