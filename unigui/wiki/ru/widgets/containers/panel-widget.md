---
title: "PanelWidget"
slug: "panel-widget"
category: "Widgets/Containers"
locale: "ru"
order: 10
---

# PanelWidget

Базовый контейнер для виджетов с несколькими детьми: управление иерархией, отложенные мутации и порядок отрисовки.

## Обзор

`PanelWidget` — базовый абстрактный класс для всех контейнеров с несколькими дочерними элементами в UniGUI. Он управляет владением детьми, назначает ссылки на родителей, прокидывает `UIContext` и обрабатывает очередь отложенных изменений, благодаря чему добавление и удаление детей во время событий или отрисовки абсолютно безопасно.

По умолчанию `PanelWidget` размещает обычных детей по принципу overlay/stack в границах контента, а абсолютные элементы позиционирует через `AbsoluteLayoutEngine`.

## Отложенные мутации и снимки

- **Очередь мутаций**: Изменения через `addChild()`, `removeChild()` и `clearChildren()` ставятся в очередь и применяются вызовом `applyQueuedMutations()` перед фазами компоновки и отрисовки.
- **Кэш-массив детей**: Горячие циклы tick и render используют кэшированный массив `childSnapshot()`, исключая аллокации итераторов.
- **Z-порядок и отсечение**: Дети сортируются согласно `ZOrder` и отсекаются по границам видимости.

## Управление дочерними элементами

```java
PanelWidget panel = new PanelWidget();
panel.addChild(new Label("Заголовок"));
panel.addChild(new Button("Нажми"));
panel.removeChild(oldChild);
```

## Связанные ссылки

- [StackPanel](<stack-panel.md>)
- [SurfaceWidget](<surface-widget.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelWidget::</span>`addChild(Widget)` {: #addchildwidget data-toc-label="addChild(Widget)" }

**Добавить ребёнка**

Ставит дочерний виджет в очередь на добавление в конец списка.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `child` | `Widget` | Добавляемый дочерний виджет |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelWidget::</span>`insertChild(int, Widget)` {: #insertchildint-widget data-toc-label="insertChild(int, Widget)" }

**Вставить дочерний виджет**

Ставит в очередь вставку дочернего виджета по указанному индексу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `index` | `int` | индекс в списке детей (с 0) |
| `child` | `Widget` | Добавляемый дочерний виджет |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelWidget::</span>`removeChild(Widget)` {: #removechildwidget data-toc-label="removeChild(Widget)" }

**Удалить ребёнка**

Ставит дочерний виджет в очередь на удаление из контейнера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `child` | `Widget` | Удаляемый дочерний виджет |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelWidget::</span>`clearChildren()` {: #clearchildren data-toc-label="clearChildren()" }

**Очистить детей**

Ставит в очередь удаление и освобождение всех дочерних виджетов.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">PanelWidget::</span>`children()` {: #children data-toc-label="children()" }

**Дочерние виджеты**

Возвращает дочерние элементы в порядке обхода/раскладки.

Возвращает `List`: список дочерних элементов.

</div>

