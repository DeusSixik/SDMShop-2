---
title: "UniRenderLayerRegistration"
slug: "uni-render-layer-registration"
category: "Core/Screens"
locale: "ru"
order: 60
---

# UniRenderLayerRegistration

Дескриптор зарегистрированного слоя отрисовки с возможностью динамического управления.

## Обзор

`UniRenderLayerRegistration` инкапсулирует зарегистрированный слой. Позволяет динамически включать и отключать слой, изменять приоритет отрисовки, обновлять предикат видимости и корректно освобождать ресурсы через вызов `close()`.

## Управление жизненным циклом

Реализует `AutoCloseable`, что позволяет использовать try-with-resources или вручную закрывать регистрацию при выключении плагина/мода, немедленно удаляя слой из конвейера отрисовки.

## Переключение и удаление слоя

```java
UniRenderLayerRegistration reg = UniHudRender.register(myWidget);
// Временное отключение
reg.enabled(false);
// Окончательное удаление
reg.close();
```

## API

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayerRegistration::</span>`close()` {: #close data-toc-label="close()" }

**close()**

Отменяет регистрацию и закрывает этот слой в бэкенде.

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayerRegistration::</span>`closed()` {: #closed data-toc-label="closed()" }

**closed()**

Возвращает true, если эта регистрация была закрыта.

Возвращает `boolean`: True, если закрыта..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayerRegistration::</span>`enabled()` {: #enabled data-toc-label="enabled()" }

**enabled()**

Возвращает true, если слой в данный момент активен для отрисовки.

Возвращает `boolean`: True, если включён..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayerRegistration::</span>`enabled(boolean)` {: #enabledboolean data-toc-label="enabled(boolean)" }

**enabled(boolean)**

Включает или временно отключает отрисовку слоя без его удаления.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `enabled` | `boolean` | True для включения, false для отключения. |

Возвращает `UniRenderLayerRegistration`: Текущий дескриптор регистрации для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayerRegistration::</span>`layer()` {: #layer data-toc-label="layer()" }

**layer()**

Возвращает ассоциированный экземпляр [UniRenderLayer](<uni-render-layer.md>).

Возвращает `UniRenderLayer`: Связанный [UniRenderLayer](<uni-render-layer.md>)..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayerRegistration::</span>`priority()` {: #priority data-toc-label="priority()" }

**priority()**

Возвращает приоритет порядка отрисовки.

Возвращает `int`: Текущее значение приоритета..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayerRegistration::</span>`priority(int)` {: #priorityint data-toc-label="priority(int)" }

**priority(int)**

Обновляет приоритет порядка отрисовки.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `priority` | `int` | Новый приоритет. |

Возвращает `UniRenderLayerRegistration`: Текущий дескриптор регистрации для цепочки вызовов..

</div>

<div class="api-member-card" markdown="1">

### <span class="api-class-prefix">UniRenderLayerRegistration::</span>`visibleWhen(Predicate)` {: #visiblewhenpredicate data-toc-label="visibleWhen(Predicate)" }

**visibleWhen(Predicate<Object>)**

Обновляет динамический предикат видимости.

| Параметр | Тип | Описание |
| --- | --- | --- |
| `visibility` | `Predicate` | Предикат, проверяющий контекстный объект. |

Возвращает `UniRenderLayerRegistration`: Текущий дескриптор регистрации для цепочки вызовов..

</div>

