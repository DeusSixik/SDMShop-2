---
title: "DragOverEvent"
slug: "drag-over-event"
category: "Core/Виджеты/Drag-and-Drop"
locale: "ru"
order: 0
---

# DragOverEvent

Маршрутизируемое событие, опрашивающее зоны сброса под указателем в процессе перемещения перетаскивания и тиков анимации.

## Обзор

`DragOverEvent` непрерывно доставляется виджетам-кандидатам на сброс при движении указателя над ними.

### Валидация цели

Зоны сброса проверяют `payload()` и подтверждают готовность принять данные вызовом `event.accept(this)`:

```java
widget.addListener(DragOverEvent.class, event -> {
    if (event.payload().matchesType("inventory/*")) {
        event.accept(widget);
    }
});
```

После вызова `accept` виджет становится текущей целью `session.dropTarget()`, активируя визуальную подсветку.

## Связанные ссылки

- [DragSession](<drag-session.md>): Состояние и контроллер активного жеста перетаскивания, управляющий координатами указателя, активной целью сброса, позиционированием ghost, индикаторами вставки и автопрокруткой.
- [DropController](<drop-controller.md>): Контроллер цели сброса, подключаемый к любому виджету через композицию для фильтрации payload и обработки событий drag enter, over, leave и drop.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragOverEvent::</span>`accept(Widget)` {: #acceptwidget data-toc-label="accept(Widget)" }

Отмечает данный виджет как принявший перетаскиваемый payload.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `zone` | `Widget` | Виджет, принимающий на себя статус цели сброса. |

</div>

