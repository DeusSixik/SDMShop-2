---
title: "DropController"
slug: "drop-controller"
category: "Core/Виджеты/Drag-and-Drop"
locale: "ru"
order: 0
---

# DropController

Контроллер цели сброса, подключаемый к любому виджету через композицию для фильтрации payload и обработки событий drag enter, over, leave и drop.

## Обзор

`DropController` управляет валидацией и жизненным циклом зон приема сброса виджетов.

### Пример использования

```java
DropController.attach(targetContainer)
        .accepts("items/weapon", "items/armor")
        .onDragEnter(event -> targetContainer.style().border(ColorView.GREEN, 2.0f))
        .onDragLeave(event -> targetContainer.style().clearBorder())
        .onDrop(event -> addItem(event.payload().data(Item.class)));
```

### Возможности

- **Декларативная фильтрация**: Фильтрация типов через `accepts(String...)` или предикаты `accepts(Predicate<DragPayload>)`.
- **Автоматическое подтверждение**: Автоматически вызывает `event.accept(zone)` при совпадении payload.
- **Коллбэки жизненного цикла**: Удобная цепочка методов `onDragEnter`, `onDragOver`, `onDragLeave` и `onDrop`.

## Подключение DropController с валидацией

```java
DropController.attach(targetPanel)
        .accepts("card")
        .onDrop(event -> System.out.println("Dropped: " + event.payload().id()));
```

## Связанные ссылки

- [DragController](<drag-controller.md>): Контроллер жеста перетаскивания, подключаемый к любому виджету через композицию для инициации drag-and-drop с ghost-предпросмотром, порогом смещения и автопрокруткой.
- [DragSession](<drag-session.md>): Состояние и контроллер активного жеста перетаскивания, управляющий координатами указателя, активной целью сброса, позиционированием ghost, индикаторами вставки и автопрокруткой.

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`accepts(DragPayload)` {: #acceptsdragpayload data-toc-label="accepts(DragPayload)" }

Проверяет, принимаются ли переданные данные payload согласно настроенным фильтрам.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `payload` | `DragPayload` | Данные для валидации. |

Возвращает `boolean`: True, если данные принимаются данной зоной сброса..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`accepts(Predicate)` {: #acceptspredicate data-toc-label="accepts(Predicate)" }

Задает предикат для валидации принимаемых данных payload.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `validator` | `Predicate` | Предикат-валидатор данных. |

Возвращает `DropController`: Данный DropController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`accepts(String)` {: #acceptsstring data-toc-label="accepts(String)" }

Настраивает фильтр принимаемых типов данных payload.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `types` | `String` | Принимаемые типы данных. |

Возвращает `DropController`: Данный DropController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`attach(Widget)` {: #attachwidget data-toc-label="attach(Widget)" }

Подключает контроллер приема сброса к указанному виджету зоны сброса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `zone` | `Widget` | Виджет, выступающий в роли зоны сброса. |

Возвращает `DropController`: Вновь подключенный экземпляр DropController..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`detach()` {: #detach data-toc-label="detach()" }

Отключает данный контроллер зоны сброса от виджета и удаляет слушатели.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`enabled(boolean)` {: #enabledboolean data-toc-label="enabled(boolean)" }

Включает или выключает данный контроллер зоны сброса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `enabled` | `boolean` | Включена ли зона сброса. |

Возвращает `DropController`: Данный DropController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`hovering()` {: #hovering data-toc-label="hovering()" }

Проверяет, находится ли в данный момент курсор с подходящим payload над зоной сброса.

Возвращает `boolean`: True, если перетаскивание зависло над данной зоной сброса..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`onDragEnter(Consumer)` {: #ondragenterconsumer data-toc-label="onDragEnter(Consumer)" }

Регистрирует слушатель события входа перетаскиваемого элемента в зону сброса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `Consumer` | Коллбэк при входе указателя в зону сброса. |

Возвращает `DropController`: Данный DropController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`onDragLeave(Consumer)` {: #ondragleaveconsumer data-toc-label="onDragLeave(Consumer)" }

Регистрирует слушатель события выхода перетаскиваемого элемента из зоны сброса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `Consumer` | Коллбэк при выходе указателя из зоны сброса. |

Возвращает `DropController`: Данный DropController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`onDragOver(Consumer)` {: #ondragoverconsumer data-toc-label="onDragOver(Consumer)" }

Регистрирует слушатель события перемещения перетаскиваемого элемента над зоной сброса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `Consumer` | Коллбэк при перемещении указателя над зоной сброса. |

Возвращает `DropController`: Данный DropController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`onDrop(Consumer)` {: #ondropconsumer data-toc-label="onDrop(Consumer)" }

Регистрирует слушатель события успешного сброса данных в зону сброса.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `listener` | `Consumer` | Коллбэк при сбросе данных в зону. |

Возвращает `DropController`: Данный DropController для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">DropController::</span>`zone()` {: #zone data-toc-label="zone()" }

Возвращает виджет, зарегистрированный в качестве зоны сброса.

Возвращает `Widget`: Виджет, выступающий в роли зоны сброса..

</div>

