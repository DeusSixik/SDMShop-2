---
title: "DragController"
slug: "drag-controller"
category: "Core/Виджеты/Drag-and-Drop"
locale: "ru"
order: 0
---

# DragController

Контроллер жеста перетаскивания, подключаемый к любому виджету через композицию для инициации drag-and-drop с ghost-предпросмотром, порогом смещения и автопрокруткой.

## Обзор

`DragController` добавляет функционал инициатора перетаскивания любому виджету посредством композиции.

### Пример использования

```java
DragController.attach(headerWidget)
        .dragged(entireCardWidget)
        .payload(() -> DragPayload.of("card-1", "cards/item", "Card 1"))
        .threshold(4.0f)
        .ghostWidget(entireCardWidget, 0.75f)
        .onDragEnd(end -> System.out.println("Результат: " + end.outcome()));
```

### Параметры настройки

| Параметр | Метод | Назначение |
| --- | --- | --- |
| Ручка | `attach(widget)` | Виджет, перехватывающий нажатие мыши для старта перетаскивания. |
| Перемещаемый виджет | `dragged(widget)` | Логический компонент, который фактически перемещается. |
| Ленивый payload | `payload(Supplier)` | Вычисляет данные только при фактическом превышении порога смещения. |
| Порог смещения | `threshold(px)` | Мертвая зона в пикселях до начала перетаскивания. |
| Фиксация оси | `lockGhostAxis(Orientation)` | Ограничивает движение ghost-виджета только по горизонтали или вертикали. |
| Автопрокрутка | `autoScroll(edge, speed)` | Прокручивает родительские контейнеры при поднесении к краю. |

## Подключение DragController к виджету

```java
DragController.attach(header)
        .dragged(card)
        .payload(() -> DragPayload.of("card-1", "card", "Card #1", data))
        .threshold(4.0f)
        .onDragEnd(end -> System.out.println("Outcome: " + end.outcome()));
```

## Связанные ссылки

- [DragSession](<drag-session.md>): Состояние и контроллер активного жеста перетаскивания, управляющий координатами указателя, активной целью сброса, позиционированием ghost, индикаторами вставки и автопрокруткой.
- [DropController](<drop-controller.md>): Контроллер цели сброса, подключаемый к любому виджету через композицию для фильтрации payload и обработки событий drag enter, over, leave и drop.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`attach(Widget)` {: #attachwidget data-toc-label="attach(Widget)" }

Подключает новый DragController к указанному виджету-ручке.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `handle` | `Widget` | Виджет, выступающий в роли ручки для инициации перетаскивания. |

Возвращает `DragController`: Вновь подключенный экземпляр DragController..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`autoScroll(boolean)` {: #autoscrollboolean data-toc-label="autoScroll(boolean)" }

Включает или отключает поведение автопрокрутки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `autoScroll` | `boolean` | Включена ли автоматическая прокрутка. |

Возвращает `DragController`: Данный DragController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`autoScroll(float, float)` {: #autoscrollfloat-float data-toc-label="autoScroll(float, float)" }

Настраивает параметры автоматической прокрутки контейнера при поднесении к краю.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `edge` | `float` | Отступ от края в пикселях для срабатывания автопрокрутки. |
| `pixelsPerSecond` | `float` | Скорость автоматической прокрутки в пикселях в секунду. |

Возвращает `DragController`: Данный DragController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`cancel()` {: #cancel data-toc-label="cancel()" }

Программно отменяет активный жест перетаскивания.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`detach()` {: #detach data-toc-label="detach()" }

Отключает контроллер перетаскивания от виджета-ручки и удаляет зарегистрированные слушатели.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`dragged(Widget)` {: #draggedwidget data-toc-label="dragged(Widget)" }

Задает логический виджет, перемещаемый операцией перетаскивания (например, всю карточку при перетаскивании за заголовок).

| Параметр | Тип | Описание |
| --- | --- | --- |
| `dragged` | `Widget` | Логический виджет, перемещаемый операцией перетаскивания. |

Возвращает `DragController`: Данный DragController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`dragging()` {: #dragging data-toc-label="dragging()" }

Проверяет, находится ли жест перетаскивания в активном состоянии с перемещением.

Возвращает `boolean`: True, если жест перетаскивания активен в данный момент..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`enabled(boolean)` {: #enabledboolean data-toc-label="enabled(boolean)" }

Включает или выключает данный контроллер перетаскивания.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `enabled` | `boolean` | Включен ли контроллер перетаскивания. |

Возвращает `DragController`: Данный DragController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`filter(Predicate)` {: #filterpredicate data-toc-label="filter(Predicate)" }

Задает предикат-фильтр для выборочного отклонения событий нажатия указателя.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `filter` | `Predicate` | Предикат-фильтр для событий нажатия указателя. |

Возвращает `DragController`: Данный DragController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`ghost(DragGhost)` {: #ghostdragghost data-toc-label="ghost(DragGhost)" }

Настраивает пользовательский отрисовщик ghost-представления перетаскиваемого элемента.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `ghost` | `DragGhost` | Отрисовщик ghost-представления перетаскиваемого элемента. |

Возвращает `DragController`: Данный DragController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`ghost(Widget)` {: #ghostwidget data-toc-label="ghost(Widget)" }

Настраивает ghost-отображение, дублирующее визуальный облик указанного виджета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `widget` | `Widget` | Виджет, визуальный облик которого клонируется для ghost-отображения. |

Возвращает `DragController`: Данный DragController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`ghostOpacity(float)` {: #ghostopacityfloat data-toc-label="ghostOpacity(float)" }

Задает уровень непрозрачности для предпросмотра ghost-виджета.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `opacity` | `float` | Непрозрачность ghost-отображения перетаскивания. |

Возвращает `DragController`: Данный DragController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`handle()` {: #handle data-toc-label="handle()" }

Возвращает виджет-ручку, к которому подключен данный контроллер.

Возвращает `Widget`: Виджет, настроенный как ручка перетаскивания..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`lockGhostAxis(Orientation)` {: #lockghostaxisorientation data-toc-label="lockGhostAxis(Orientation)" }

Ограничивает перемещение ghost-отображения только по горизонтали или вертикали.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `axis` | `Orientation` | Ограничение оси перемещения ghost-отображения. |

Возвращает `DragController`: Данный DragController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`onDragEnd(Consumer)` {: #ondragendconsumer data-toc-label="onDragEnd(Consumer)" }

Регистрирует коллбэк, вызываемый при завершении перетаскивания с указанием финального результата.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `Consumer` | Коллбэк, вызываемый при завершении перетаскивания. |

Возвращает `DragController`: Данный DragController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`onDragMove(Consumer)` {: #ondragmoveconsumer data-toc-label="onDragMove(Consumer)" }

Регистрирует коллбэк, вызываемый при каждом перемещении указателя во время перетаскивания.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `Consumer` | Коллбэк, вызываемый при перемещении указателя во время перетаскивания. |

Возвращает `DragController`: Данный DragController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`onDragStart(Consumer)` {: #ondragstartconsumer data-toc-label="onDragStart(Consumer)" }

Регистрирует коллбэк, вызываемый в момент фактического начала перетаскивания.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `Consumer` | Коллбэк, вызываемый при старте сессии перетаскивания. |

Возвращает `DragController`: Данный DragController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`payload(DragPayload)` {: #payloaddragpayload data-toc-label="payload(DragPayload)" }

Настраивает статический константный payload для жеста перетаскивания.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `payload` | `DragPayload` | Статический payload для передачи. |

Возвращает `DragController`: Данный DragController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`payload(Supplier)` {: #payloadsupplier data-toc-label="payload(Supplier)" }

Настраивает ленивый поставщик payload, вызываемый при превышении порога смещения перетаскивания.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `payload` | `Supplier` | Ленивый поставщик payload, вызываемый в момент начала перетаскивания. |

Возвращает `DragController`: Данный DragController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`session()` {: #session data-toc-label="session()" }

Возвращает активную сессию [DragSession](<drag-session.md>) или null, если перетаскивание неактивно.

Возвращает `DragSession`: Активная сессия перетаскивания [DragSession](<drag-session.md>) или null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`sourceOpacity(float)` {: #sourceopacityfloat data-toc-label="sourceOpacity(float)" }

Задает прозрачность исходного виджета во время активного перетаскивания.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `opacity` | `float` | Непрозрачность ghost-отображения перетаскивания. |

Возвращает `DragController`: Данный DragController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragController::</span>`threshold(float)` {: #thresholdfloat data-toc-label="threshold(float)" }

Задает минимальный порог смещения в пикселях до начала сессии перетаскивания.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `threshold` | `float` | Порог смещения в пикселях до активации перетаскивания. |

Возвращает `DragController`: Данный DragController для цепочки вызовов..

</div>

