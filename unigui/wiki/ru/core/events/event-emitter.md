---
title: "EventEmitter"
slug: "event-emitter"
category: "Core/Events"
locale: "ru"
order: 10
---

# EventEmitter

Контракт для объектов, способных регистрировать слушателей и рассылать типизированные события.

## Обзор

`EventEmitter` — базовый контракт подписки и диспетчеризации событий, реализуемый интерфейсом `Widget` и другими наблюдаемыми компонентами UniGUI. Он предоставляет строго типизированный интерфейс без применения рефлексии для регистрации слушателей и синхронной рассылки событий.

Каждое событие идентифицируется токеном `EventType<T>`, исключая строковые идентификаторы и рефлексию.

## Управление подписками

Вызов `on(EventType, listener)` регистрирует слушателя и возвращает дескриптор `EventSubscription`:

- **Отписка**: Вызовите `subscription.unsubscribe()` или `subscription.close()`, чтобы прекратить получение событий.
- **Управление жизненным циклом**: Благодаря реализации `AutoCloseable`, дескриптор отлично сочетается с `try-with-resources` или `EventScope` для автоматической отписки при закрытии экрана или виджета.
- **Потокобезопасность**: Реализация гарантирует, что регистрация и удаление слушателей во время активной рассылки не приводят к ошибкам модификации коллекций.

## Отмена событий

События в UniGUI поддерживают кооперативную отмену через `Event.cancel()` и `Event.isCancelled()`.

- **Стандартный слушатель**: `on(type, listener)` автоматически игнорирует события, которые уже были отменены.
- **Слушатель аудита / мониторинга**: Используйте перегрузку `on(type, true, listener)` с параметром `receiveCancelled = true`, чтобы получать события даже после вызова `cancel()`. Это полезно для телеметрии, логов и глобальных перехватчиков.

## Подписка на события

```java
EventSubscription sub = widget.on(ButtonClickEvent.TYPE, e -> {
    System.out.println("Кнопка нажата!");
});

// Позже — отписка:
sub.unsubscribe();
```

## Диспетчеризация событий

```java
// Синхронно рассылает событие всем подходящим слушателям
widget.emit(new ButtonClickEvent(widget));
```

## Связанные ссылки

- [FastEventBus](<fast-event-bus.md>)
- [Widget](<../../widgets/widget.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">EventEmitter::</span>`on(EventType, EventListener)` {: #oneventtype-eventlistener data-toc-label="on(EventType, EventListener)" }

**Подписаться на событие**

Регистрирует слушателя для указанного типа события.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `type` | `EventType` | Токен типа события |
| `listener` | `EventListener` | Callback, принимающий экземпляры событий |

Возвращает `EventSubscription`: Дескриптор EventSubscription для отписки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">EventEmitter::</span>`on(EventType, boolean, EventListener)` {: #oneventtype-boolean-eventlistener data-toc-label="on(EventType, boolean, EventListener)" }

**Подписаться с приёмом отменённых событий**

Регистрирует слушателя с явным контролем приёма отменённых событий.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `type` | `EventType` | Токен типа события |
| `receiveCancelled` | `boolean` | True для получения отменённых событий |
| `listener` | `EventListener` | Callback, принимающий экземпляры событий |

Возвращает `EventSubscription`: Дескриптор EventSubscription для отписки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">EventEmitter::</span>`emit(Event)` {: #emitevent data-toc-label="emit(Event)" }

**Вызвать событие**

Синхронно рассылает событие всем зарегистрированным слушателям.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `event` | `Event` | Экземпляр события для рассылки |

</div>

