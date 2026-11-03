---
title: "ReorderController"
slug: "reorder-controller"
category: "Core/Виджеты/Drag-and-Drop"
locale: "ru"
order: 0
---

# ReorderController

Контроллер переупорядочивания при перетаскивании, подключаемый к контейнерам ([VBox](<../../../widgets/containers/vbox.md>), [HBox](<../../../widgets/containers/hbox.md>), [PanelWidget](<../../../widgets/containers/panel-widget.md>)) с поддержкой линий вставки и автоматического изменения порядка элементов.

## Обзор

`ReorderController` реализует переупорядочивание элементов перетаскиванием внутри линейных контейнеров (`VBox`, `HBox`, `PanelWidget`).

### Возможности

- **Индикатор вставки**: Отображает визуальную линию вставки в позиции будущего размещения элемента.
- **Автоматическое применение**: Автоматически меняет порядок детей в контейнере при `autoApply(true)`.
- **Внутреннее и внешнее перетаскивание**: Поддерживает перемещение только собственных элементов или прием внешних элементов.

```java
ReorderController.attach(vbox)
        .orientation(Orientation.VERTICAL)
        .accepts("task/*")
        .autoApply(true)
        .indicator(3.0f, 4.0f, ColorView.ofHex("#007ACC"))
        .onReorder((from, to) -> syncDatabaseOrder(from, to));
```

## Переупорядочивание дочерних элементов в контейнере

```java
ReorderController.attach(vbox)
        .orientation(Orientation.VERTICAL)
        .accepts("card")
        .onReorder((from, to) -> dataList.add(to, dataList.remove((int) from)));
```

## Связанные ссылки

- [DragController](<drag-controller.md>): Контроллер жеста перетаскивания, подключаемый к любому виджету через композицию для инициации drag-and-drop с ghost-предпросмотром, порогом смещения и автопрокруткой.
- [DragSession](<drag-session.md>): Состояние и контроллер активного жеста перетаскивания, управляющий координатами указателя, активной целью сброса, позиционированием ghost, индикаторами вставки и автопрокруткой.
- [DropController](<drop-controller.md>): Контроллер цели сброса, подключаемый к любому виджету через композицию для фильтрации payload и обработки событий drag enter, over, leave и drop.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`accepts(Predicate)` {: #acceptspredicate data-toc-label="accepts(Predicate)" }

Задает предикат-валидатор допустимых payload для переупорядочивания.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `validator` | `Predicate` | Пользовательский валидатор данных. |

Возвращает `ReorderController`: Данный ReorderController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`accepts(String)` {: #acceptsstring data-toc-label="accepts(String)" }

Настраивает типы payload, разрешенные для вызова переупорядочивания.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `types` | `String` | Принимаемые типы данных для переупорядочивания. |

Возвращает `ReorderController`: Данный ReorderController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`attach(Widget)` {: #attachwidget data-toc-label="attach(Widget)" }

Подключает ReorderController к указанному контейнеру дочерних элементов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `container` | `Widget` | Виджет-контейнер, чьи дочерние элементы переупорядочиваются. |

Возвращает `ReorderController`: Вновь подключенный экземпляр ReorderController..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`autoApply(boolean)` {: #autoapplyboolean data-toc-label="autoApply(boolean)" }

Включает или отключает автоматическое перемещение дочерних элементов в списке контейнера.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `autoApply` | `boolean` | Применять ли изменения порядка дочерних элементов автоматически. |

Возвращает `ReorderController`: Данный ReorderController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`container()` {: #container data-toc-label="container()" }

Возвращает целевой виджет-контейнер, к которому подключен данный контроллер.

Возвращает `Widget`: Виджет-контейнер, элементы которого переупорядочиваются..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`detach()` {: #detach data-toc-label="detach()" }

Отключает ReorderController от контейнера и удаляет слушатели.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`enabled(boolean)` {: #enabledboolean data-toc-label="enabled(boolean)" }

Включает или отключает данный контроллер переупорядочивания.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `enabled` | `boolean` | Включен ли контроллер переупорядочивания. |

Возвращает `ReorderController`: Данный ReorderController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`indicator(ColorView, float, float)` {: #indicatorcolorview-float-float data-toc-label="indicator(ColorView, float, float)" }

Настраивает толщину, отступ и цвет линии индикатора вставки при переупорядочивании.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `color` | `ColorView` | Цвет линии вставки при переупорядочивании. |
| `thickness` | `float` | Толщина линии вставки при переупорядочивании. |
| `inset` | `float` | Боковой отступ линии вставки. |

Возвращает `ReorderController`: Данный ReorderController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`internalOnly(boolean)` {: #internalonlyboolean data-toc-label="internalOnly(boolean)" }

Ограничивает переупорядочивание элементами, принадлежащими исключительно данному контейнеру.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `internalOnly` | `boolean` | Принимать ли элементы только из данного контейнера. |

Возвращает `ReorderController`: Данный ReorderController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`itemFilter(Predicate)` {: #itemfilterpredicate data-toc-label="itemFilter(Predicate)" }

Задает предикат-фильтр, определяющий возможность перетаскивания конкретных дочерних элементов.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `itemFilter` | `Predicate` | Предикат, определяющий возможность перетаскивания дочернего виджета. |

Возвращает `ReorderController`: Данный ReorderController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`onReorder(BiConsumer)` {: #onreorderbiconsumer data-toc-label="onReorder(BiConsumer)" }

Регистрирует упрощенный коллбэк с исходным и целевым индексами перемещения.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `BiConsumer` | Коллбэк, получающий fromIndex и toIndex. |

Возвращает `ReorderController`: Данный ReorderController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`onReorder(Consumer)` {: #onreorderconsumer data-toc-label="onReorder(Consumer)" }

Регистрирует коллбэк, принимающий объект события ReorderEvent.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `Consumer` | Коллбэк, вызываемый при переупорядочивании элементов. |

Возвращает `ReorderController`: Данный ReorderController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">ReorderController::</span>`orientation(Orientation)` {: #orientationorientation data-toc-label="orientation(Orientation)" }

Настраивает ориентацию компоновки (HORIZONTAL или VERTICAL) для переупорядочивания.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `orientation` | `Orientation` | Ориентация компоновки дочерних элементов контейнера. |

Возвращает `ReorderController`: Данный ReorderController для цепочки вызовов..

</div>

