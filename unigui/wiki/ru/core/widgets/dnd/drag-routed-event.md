---
title: "DragRoutedEvent"
slug: "drag-routed-event"
category: "Core/Виджеты/Drag-and-Drop"
locale: "ru"
order: 0
---

# DragRoutedEvent

Абстрактный базовый класс для маршрутизируемых событий drag-and-drop, передающих активную сессию [DragSession](<drag-session.md>) по иерархии виджетов.

## Обзор

`DragRoutedEvent` — абстрактный базовый класс для маршрутизируемых событий drag-and-drop, распространяемых по дереву виджетов.

### Фазы маршрутизации

| Фаза | Описание |
| --- | --- |
| `CAPTURING` | Распространяется от корня вниз к целевому виджету под курсором. |
| `AT_TARGET` | Доставляется непосредственно целевому виджету под курсором. |
| `BUBBLING` | Поднимается от целевого виджета обратно к корню интерфейса. |

Все события перетаскивания предоставляют доступ к активной сессии `DragSession`, объекту `DragPayload` и экранным координатам курсора.

## Связанные ссылки

- [DragSession](<drag-session.md>): Состояние и контроллер активного жеста перетаскивания, управляющий координатами указателя, активной целью сброса, позиционированием ghost, индикаторами вставки и автопрокруткой.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragRoutedEvent::</span>`currentTarget()` {: #currenttarget data-toc-label="currentTarget()" }

Возвращает текущий обрабатывающий виджет события.

Возвращает `Widget`: Текущий виджет, обрабатывающий маршрутизируемое событие..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragRoutedEvent::</span>`payload()` {: #payload data-toc-label="payload()" }

Возвращает передаваемый объект данных [DragPayload](<drag-payload.md>).

Возвращает `DragPayload`: Передаваемый объект данных payload сессии перетаскивания..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragRoutedEvent::</span>`phase()` {: #phase data-toc-label="phase()" }

Возвращает текущую фазу диспетчеризации события.

Возвращает `EventPhase`: Фаза диспетчеризации (CAPTURING, AT_TARGET, BUBBLING)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragRoutedEvent::</span>`rootX()` {: #rootx data-toc-label="rootX()" }

Возвращает корневую координату X указателя мыши.

Возвращает `float`: Корневая координата указателя X..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragRoutedEvent::</span>`rootY()` {: #rooty data-toc-label="rootY()" }

Возвращает корневую координату Y указателя мыши.

Возвращает `float`: Корневая координата указателя Y..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragRoutedEvent::</span>`session()` {: #session data-toc-label="session()" }

Возвращает активную сессию перетаскивания [DragSession](<drag-session.md>).

Возвращает `DragSession`: Активная сессия перетаскивания [DragSession](<drag-session.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragRoutedEvent::</span>`target()` {: #target data-toc-label="target()" }

Возвращает целевой виджет события.

Возвращает `Widget`: Исходный целевой виджет события..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragRoutedEvent::</span>`type()` {: #type data-toc-label="type()" }

Возвращает тип события.

Возвращает `EventType`: Идентификатор конкретного типа события..

</div>

