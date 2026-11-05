---
title: "EventScope"
slug: "event-scope"
category: "Core/Events"
locale: "ru"
order: 30
---

# EventScope

Контейнер управления жизненным циклом подписок на события.

## Обзор

`EventScope` — контейнер с интерфейсом `AutoCloseable`, отслеживающий подписки на события на протяжении жизненного цикла компонента. В интерфейсах забытая отписка при смене экрана или закрытии диалога — частая причина утечек памяти и фантомных обновлений.

Привязка подписок к `EventScope` позволяет отписать всех слушателей единым вызовом.

## Управление жизненным циклом

- **`listen(event, listener)`**: Подписывает слушателя на событие и сохраняет подписку в скоупе.
- **`clear()`**: Отписывает всех текущих слушателей в обратном порядке, оставляя скоуп открытым для новых подписок.
- **`close()`**: Безвозвратно закрывает скоуп и отписывает всех слушателей. Последующие вызовы `listen()` возвращают `EventSubscription.NOOP`.
- **`EventScope.noop()`**: Фабрика создания неактивного скоупа, игнорирующего подписки, удобного при опциональной передаче скоупа.

## Интеграция с жизненным циклом экрана

```java
public class InventoryScreen extends UniScreen {
    private final EventScope scope = new EventScope();

    @Override
    public void onOpen() {
        scope.listen(ITEM_EQUIPPED, item -> updateSlot(item));
        scope.listen(CURRENCY_CHANGED, balance -> updateBalance(balance));
    }

    @Override
    public void onClose() {
        scope.close(); // все слушатели отписываются автоматически
    }
}
```

## Связанные ссылки

- [EventEmitter](<event-emitter.md>)
- [FastEventBus](<fast-event-bus.md>)

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">EventScope::</span>`noop()` {: #noop data-toc-label="noop()" }

**Пустой scope**

Возвращает неактивный scope, игнорирующий все вызовы listen.

Возвращает `EventScope`: экземпляр неактивного EventScope.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">EventScope::</span>`listen(EventPtr, EventListener)` {: #listeneventptr-eventlistener data-toc-label="listen(EventPtr, EventListener)" }

**Отслеживаемая подписка**

Регистрирует слушателя и сохраняет подписку для последующей очистки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `event` | `EventPtr` | Указатель на событие |
| `listener` | `EventListener` | Слушатель события |

Возвращает `EventSubscription`: Дескриптор EventSubscription.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">EventScope::</span>`clear()` {: #clear data-toc-label="clear()" }

**Очистить подписки**

Отписывает всех зарегистрированных слушателей без закрытия scope.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">EventScope::</span>`close()` {: #close data-toc-label="close()" }

**Закрыть scope**

Закрывает scope и отписывает всех слушателей.

</div>

