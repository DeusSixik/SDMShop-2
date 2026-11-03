---
title: "DragDropEvent"
slug: "drag-drop-event"
category: "Core/Виджеты/Drag-and-Drop"
locale: "ru"
order: 0
---

# DragDropEvent

Маршрутизируемое событие, доставляемое в целевую зону сброса при отпускании указателя с принятым payload.

## Обзор

`DragDropEvent` доставляется подтвержденной цели сброса в момент отпускания кнопки мыши.

### Обработка сброса

```java
widget.addListener(DragDropEvent.class, event -> {
    MyData item = event.payload().data(MyData.class);
    insertItem(item);
});
```

Успешная доставка `DragDropEvent` завершает сессию перетаскивания с результатом `DragOutcome.DROPPED`.

## Связанные ссылки

- [DragSession](<drag-session.md>): Состояние и контроллер активного жеста перетаскивания, управляющий координатами указателя, активной целью сброса, позиционированием ghost, индикаторами вставки и автопрокруткой.
- [DropController](<drop-controller.md>): Контроллер цели сброса, подключаемый к любому виджету через композицию для фильтрации payload и обработки событий drag enter, over, leave и drop.

