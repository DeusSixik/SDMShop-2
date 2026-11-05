---
title: "DragPayload"
slug: "drag-payload"
category: "Core/Виджеты/Drag-and-Drop"
locale: "ru"
order: 0
---

# DragPayload

Неизменяемый контейнер передаваемых данных (payload), переносящий идентификатор, тип, текст предпросмотра и пользовательский объект между источником и целью сброса.

## Обзор

`DragPayload` инкапсулирует данные, передаваемые между инициатором перетаскивания и зонами сброса.

### Архитектура данных

- **`id`**: Уникальный строковый идентификатор перетаскиваемого объекта.
- **`type`**: Строковый MIME или семантический тип (например, `item/inventory`, `dock/tab`). Поддерживает маски поиска, например `item/*`.
- **`preview`**: Краткий текст предпросмотра, отображаемый во встроенных ghost-бэйджах и подсказках.
- **`data`**: Необязательный Java-объект в оперативной памяти (`Object`), доступный через `data(Class<T>)`.

```java
DragPayload payload = DragPayload.of("item-42", "inventory/slot", "Diamond Sword", swordItem);
if (payload.matchesType("inventory/*")) {
    Sword item = payload.data(Sword.class);
}
```

## Создание и проверка payload

```java
DragPayload payload = DragPayload.of("item-42", "item.stack", "Iron Ingot", stackItem);
```

## Связанные ссылки

- [DragController](<drag-controller.md>): Контроллер жеста перетаскивания, подключаемый к любому виджету через композицию для инициации drag-and-drop с ghost-предпросмотром, порогом смещения и автопрокруткой.
- [DropController](<drop-controller.md>): Контроллер цели сброса, подключаемый к любому виджету через композицию для фильтрации payload и обработки событий drag enter, over, leave и drop.

## API

<div class="api-member-card" markdown="1">

### `DragPayload(String, String, String)` {: #dragpayloadstring-string-string data-toc-label="DragPayload(String, String, String)" }

Создает объект DragPayload с идентификатором, типом и текстом предпросмотра.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор данных. |
| `type` | `String` | MIME-тип или семантический строковый тип. |
| `preview` | `String` | Короткий текст предпросмотра. |

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragPayload::</span>`data(Class)` {: #dataclass data-toc-label="data(Class)" }

Возвращает прикрепленный объект данных, безопасно приведенный к запрошенному классу.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `type` | `Class` | Ожидаемый класс объекта данных. |

Возвращает `Object`: Данные payload, приведенные к целевому типу, или null..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragPayload::</span>`hasData()` {: #hasdata data-toc-label="hasData()" }

Проверяет, содержит ли данный payload непустой объект пользовательских данных.

Возвращает `boolean`: True, если прикреплен непустой объект данных..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragPayload::</span>`matchesType(String)` {: #matchestypestring data-toc-label="matchesType(String)" }

Проверяет, совпадает ли тип payload с ожидаемой строкой типа (с поддержкой wildcard '*').

| Параметр | Тип | Описание |
| --- | --- | --- |
| `expectedType` | `String` | Ожидаемый строковый тип для сопоставления. |

Возвращает `boolean`: True, если тип совпадает с ожидаемой строкой типа..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragPayload::</span>`of(String, String, String)` {: #ofstring-string-string data-toc-label="of(String, String, String)" }

Статическая фабрика для создания DragPayload без объекта данных.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор данных. |
| `type` | `String` | MIME-тип или семантический строковый тип. |
| `preview` | `String` | Короткий текст предпросмотра. |

Возвращает `DragPayload`: Вновь созданный неизменяемый экземпляр DragPayload..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DragPayload::</span>`of(String, String, String, Object)` {: #ofstring-string-string-object data-toc-label="of(String, String, String, Object)" }

Фабричный метод создания объекта payload с произвольными данными текущего процесса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `id` | `String` | Уникальный идентификатор данных. |
| `type` | `String` | MIME-тип или семантический строковый тип. |
| `preview` | `String` | Короткий текст предпросмотра. |
| `data` | `Object` | Пользовательский объект данных. |

Возвращает `DragPayload`: Вновь созданный неизменяемый экземпляр DragPayload..

</div>

