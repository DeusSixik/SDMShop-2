---
title: "VirtualListView"
slug: "virtual-list-view"
category: "Widgets/Data"
locale: "ru"
order: 10
---

# VirtualListView

Виртуализированное представление больших наборов элементов в виде адаптивной сетки или списка.

## Когда использовать

Используй `VirtualListView`, когда набор содержит сотни или тысячи однотипных записей. Фабрика создаёт только видимые элементы и небольшой overscan; обычные формы лучше строить на `VBox` или `LinearBox`.

## Layout и [StylePack](<../../core/styling/style-pack.md>)

Viewport учитывает `layoutStyle().padding()`, `rowGap()` и `columnGap()`. Те же параметры можно задавать через `StyleKeys.VIRTUAL_LIST_*` в `StylePack`.

## Адаптивная сетка

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

**Фабрика элементов**

Устанавливает фабрику, создающую UI-виджет по индексу элемента данных.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `itemFactory` | `IntFunction` | Функция index → [Widget](<../widget.md>); null восстанавливает фабрику [Label](<../display/label.md>) по умолчанию. |

Возвращает `VirtualListView`: этот VirtualListView для fluent-настройки.

#### Подключение модели

```java
view.itemFactory(index -> createCard(entries.get(index)));
```

</div>

