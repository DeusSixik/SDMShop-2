---
title: "FastEventBus"
slug: "fast-event-bus"
category: "Core/Events"
locale: "ru"
order: 20
---

# FastEventBus

Сверхбыстрый lock-free event bus без рефлексии, хэш-мап и аллокаций памяти при диспетчеризации.

## Обзор

`FastEventBus` — высокопроизводительный движок шины событий UniGUI. Спроектирован специально для критичных к задержкам сценариев (игровой цикл, проход ввода, тики), полностью исключая рефлексию, поиск в хэш-таблицах и создание объектов-мусора при диспетчеризации.

## Архитектура без мусора (Zero-Garbage)

Классические шины событий используют мапы соответствия классов методам или строки, что приводит к аллокациям итераторов, боксингу и хэшированию на каждый вызов.

`FastEventBus` решает это через плотную целочисленную индексацию:

- **`EventType<T>`**: Каждому типу события при создании автоматически назначается порядковый индекс (`id = 0, 1, 2...`).
- **AtomicReferenceArray**: Слушатели хранятся в непрерывных типизированных массивах по индексу события.
- **Прямой обход**: `fire()` обращается напрямую по индексу массива и проходит слушателей без создания промежуточных объектов.

## Многопоточность и безопасность

Шина использует lock-free чтение в сочетании с Copy-On-Write при изменении:

- **`fire()`**: Полностью lock-free. Атомарно считывает текущий массив слушателей и производит обход.
- **`subscribe()` / `unsubscribe()`**: Синхронизированы и используют дублирование массива (Copy-On-Write), вообще не блокируя читающие потоки.
- **Изоляция ошибок**: Исключения внутри слушателей перехватываются и логируются через SLF4J, поэтому сбой одного слушателя не прерывает работу остальных.

## Подписка и вызов

```java
FastEventBus.EventType<String> CHAT_EVENT = new FastEventBus.EventType<>("chat");
FastEventBus bus = FastEventBus.DEFAULT_BUS;

EventSubscription sub = bus.subscribe(CHAT_EVENT, msg -> {
    System.out.println("Получено: " + msg);
});

bus.fire(CHAT_EVENT, "Привет, UniGUI!");
sub.unsubscribe();
```

## Типизированное событие-record

```java
public record PlayerScore(String player, int score) {}
public static final FastEventBus.EventType<PlayerScore> SCORE = new FastEventBus.EventType<>("score");

FastEventBus.DEFAULT_BUS.subscribe(SCORE, e -> {
    System.out.printf("%s заработал %d очков%n", e.player(), e.score());
});

FastEventBus.DEFAULT_BUS.fire(SCORE, new PlayerScore("Alex", 100));
```

## Связанные ссылки

- [EventEmitter](<event-emitter.md>)
- [EventScope](<event-scope.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">FastEventBus::</span>`subscribe(EventType, EventListener)` {: #subscribeeventtype-eventlistener data-toc-label="subscribe(EventType, EventListener)" }

**Подписать слушателя**

Регистрирует слушателя для типа события с использованием copy-on-write.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `type` | `EventType` | Уникальный токен типа события |
| `listener` | `EventListener` | Слушатель события |

Возвращает `EventSubscription`: Дескриптор EventSubscription для отписки.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">FastEventBus::</span>`unsubscribe(EventType, EventListener)` {: #unsubscribeeventtype-eventlistener data-toc-label="unsubscribe(EventType, EventListener)" }

**Отписать слушателя**

Удаляет слушателя сжатием массива.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `type` | `EventType` | Токен типа события |
| `listener` | `EventListener` | Удаляемый слушатель |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">FastEventBus::</span>`fire(EventType, Object)` {: #fireeventtype-object data-toc-label="fire(EventType, Object)" }

**Вызвать событие**

Вызывает событие lock-free без создания мусора в памяти.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `type` | `EventType` | Токен типа события |
| `event` | `Object` | Данные события |

</div>

